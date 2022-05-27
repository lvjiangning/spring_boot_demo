package com.rihao.property.modules.estate.service.impl;

import cn.hutool.core.util.ArrayUtil;
import com.anteng.boot.core.facade.code.BizCodeFace;
import com.anteng.boot.core.facade.code.ErrorCode;
import com.anteng.boot.core.facade.exception.BizException;
import com.anteng.boot.pojo.query.Filter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rihao.property.common.oss.IOssService;
import com.rihao.property.common.page.PageVo;
import com.rihao.property.common.vo.KeyValueVo;
import com.rihao.property.modules.establish.entity.Establish;
import com.rihao.property.modules.establish.service.IEstablishService;
import com.rihao.property.modules.estate.controller.params.BuildQueryParam;
import com.rihao.property.modules.estate.convert.BuildingConvert;
import com.rihao.property.modules.estate.convert.UnitConvert;
import com.rihao.property.modules.estate.entity.Building;
import com.rihao.property.modules.estate.entity.Floor;
import com.rihao.property.modules.estate.entity.Park;
import com.rihao.property.modules.estate.entity.Unit;
import com.rihao.property.modules.estate.mapper.BuildingMapper;
import com.rihao.property.modules.estate.service.*;
import com.rihao.property.modules.estate.vo.*;
import com.rihao.property.modules.lease.contract.dto.BuildingBasicDTO;
import com.rihao.property.shiro.util.JwtUtil;
import com.rihao.property.util.ValidationUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wangyu
 * @since 2021-03-28
 */
@Service
public class BuildingServiceImpl extends ServiceImpl<BuildingMapper, Building> implements IBuildingService {

    private IEstablishService establishService;
    private IFloorService floorService;
    @Autowired
    @Lazy
    private IUnitService unitService;
    private IParkService parkService;

    @Override
    public PageVo<BuildingVo> search(BuildQueryParam buildQueryParam) {
        Page<Building> page = new Page<>(buildQueryParam.getCurrent(), buildQueryParam.getPageSize());

        //过滤数据权限
        buildQueryParam.setParkIds(JwtUtil.getCurrentUserParkIds());
        Page<Building> result = this.getBaseMapper().findBuildingByLikeName(page, buildQueryParam);
        List<BuildingVo> list = BuildingConvert.INSTANCE.entity2ListItemBatch(result.getRecords());
        for (BuildingVo buildingVo : list) {
            Establish establish = this.establishService.getById(buildingVo.getEstId());
            buildingVo.setEstName(establish.getName());

            Park park = this.parkService.getById(buildingVo.getParkId());
            buildingVo.setParkName(park.getName());
            //查找全部有效的单元数据
            List<Unit> allUnits = this.unitService.getAllListByBuildingId(buildingVo.getId());
            buildingVo.setAllUnitAmount(allUnits.size());
            List<Long> param=new ArrayList<>();
            param.add(buildingVo.getId());
            List<Unit> freeUnits = this.unitService.getFreeListByBuildingId(param);
            buildingVo.setFreeUnitAmount(freeUnits.size());
        }
        return PageVo.create(buildQueryParam.getCurrent(), buildQueryParam.getPageSize(),
                result.getTotal(), list);
    }

    @Override
    public BuildingVo detail(Long id) {
        BuildingVo vo = BuildingConvert.INSTANCE.entity2Vo(this.getById(id));
        Park park = this.parkService.getById(vo.getParkId());
        vo.setParkName(park.getName());
        List<UnitVo> units = UnitConvert.INSTANCE.entity2ListItemBatch(
                this.unitService.getAllListByBuildingId(vo.getId()));
        for (UnitVo unit : units) {
            unit.setKey(unit.getId());
        }
        vo.setUnits(units);
        //公用面积计算
        Map<String, Double> map = unitService.useTypeAreaSumAndRent(units);
        vo.setAreaMap(map);
        //计算租金 面积合计
        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(BuildingVo updateVo) {
        Building entity = BuildingConvert.INSTANCE.updateParam2Entity(updateVo);
        this.validNameUnique(entity);
        Boolean result = this.updateById(entity);
        this.countBuildingInfo(entity.getId());
        return result;
    }

    @Override
    public List<BuildingKeyValueVo> queryPermissionBuildingList(Long[] parkIds) {
        List<BuildingKeyValueVo> list = new ArrayList<>();
        List<Building> buildings;
        if (parkIds.length != 0) {
            QueryWrapper<Building> wrapper = new QueryWrapper<>();
            wrapper.lambda().in(Building::getParkId, parkIds);
            buildings = this.list(wrapper);
        } else {
            buildings = this.list();
        }
        for (Building building : buildings) {
            BuildingKeyValueVo buildingKeyValueVo = new BuildingKeyValueVo();
            buildingKeyValueVo.setId(building.getId());
            buildingKeyValueVo.setName(building.getName());
            list.add(buildingKeyValueVo);
        }
        return list;
    }

    @Override
    public String getBuildingNameById(Long id) {
        return this.getById(id).getName();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createNew(BuildingVo createVo) {
        Building entity = BuildingConvert.INSTANCE.createParam2Entity(createVo);
        this.validNameUnique(entity);
        entity.setEstId(JwtUtil.getCurrentUser().getEstablishId());
        boolean result = this.save(entity);

        // 计算楼栋信息
        this.countBuildingInfo(entity.getId());

        return result;
    }

    @Override
    public Building getBuildingByName(String buildingName) {
        QueryWrapper<Building> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Building::getName, buildingName);
        return this.getOne(wrapper);
    }

    @Override
    public List<BuildingKeyValueVo> getByEstId(Long estId) {
        QueryWrapper<Building> wrapper = new QueryWrapper<>();
        wrapper.lambda()
                .eq(Building::getEstId, estId)
                .in(Building::getParkId, JwtUtil.getCurrentUserParkIds().split(","));
        List<Building> buildings = this.list(wrapper);
        List<BuildingKeyValueVo> buildingKeyValueVos = new ArrayList<>();
        for (Building building : buildings) {
            Park park = this.parkService.getById(building.getParkId());
            BuildingKeyValueVo buildingKeyValueVo = new BuildingKeyValueVo();
            buildingKeyValueVo.setId(building.getId());
            buildingKeyValueVo.setName(park.getName() + "-" + building.getName());
            buildingKeyValueVos.add(buildingKeyValueVo);
        }
        return buildingKeyValueVos;
    }

    @Override
    public BuildingVo panorama(Long id) {
        BuildingVo vo = BuildingConvert.INSTANCE.entity2Vo(this.getById(id));
        if (vo != null) {
            List<FloorVo> floorList = this.floorService.getByBuildingId(id);
            vo.setFloorList(floorList);
        }
        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void countBuildingInfo(Long buildingId) {
        Building building = this.getById(buildingId);
        BigDecimal totalUsableArea = BigDecimal.ZERO, totalBuiltUpArea = BigDecimal.ZERO;
        //更新楼栋面积信息
        List<FloorVo> floorList = this.floorService.getByBuildingId(building.getId());
        for (FloorVo floorVo : floorList) {
            if (StringUtils.hasText(floorVo.getTotalUsableArea()))
                totalUsableArea = totalUsableArea.add(new BigDecimal(floorVo.getTotalUsableArea()));
            if (StringUtils.hasText(floorVo.getTotalBuiltUpArea()))
                totalBuiltUpArea = totalBuiltUpArea.add(new BigDecimal(floorVo.getTotalBuiltUpArea()));
        }
        building.setFloorAmount(floorList.size());
        building.setUsableArea(totalUsableArea);
        building.setBuiltUpArea(totalBuiltUpArea);
        this.updateById(building);
        //更新园区面积信息
        this.parkService.countParkService(building.getParkId());
    }

    @Override
    public List<Building> getByParkId(Long parkId) {
        QueryWrapper<Building> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Building::getParkId, parkId);
        return this.list(wrapper);
    }

    @Override
    public boolean createFull(BuildingCreateVo buildingCreateVo) {
        if (!StringUtils.hasText(buildingCreateVo.getName())) {
            throw new BizException(BizCodeFace.createBizCode(ErrorCode.FAIL).message("楼栋名称不可为空"));
        }
        Building building = new Building();
        building.setName(buildingCreateVo.getName());
        building.setParkId(buildingCreateVo.getParkId());
        building.setEstId(JwtUtil.getCurrentUser().getEstablishId());

        this.validNameUnique(building);

        this.save(building);

        for (UnitCreateVo unitCreateVo : buildingCreateVo.getUnitList()) {
            if (unitCreateVo.getFloorNo() == null) {
                continue;
            }
            if (!StringUtils.hasText(unitCreateVo.getFloorNo())) {
                throw new BizException(BizCodeFace.createBizCode(ErrorCode.FAIL).message("楼层号不可为空"));
            }
            Floor floor = this.floorService.getByBuildingIdAndFloorNo(building.getId(), unitCreateVo.getFloorNo());
            if (floor == null) {
                floor = new Floor();
                floor.setFloorNo(unitCreateVo.getFloorNo());
                floor.setBuildingId(building.getId());
                this.floorService.save(floor);
            }
            Unit unit = UnitConvert.INSTANCE.createVo2Entity(unitCreateVo);
            unit.setBuildingId(building.getId());
            unit.setFloorId(floor.getId());
            unit.setFloorNo(floor.getFloorNo());
            //设置删除标记为0
            unit.setDelFlag("0");
            this.unitService.validUnitName(unit.getUnitNo(), floor.getId(), null);
            this.unitService.save(unit);
            this.unitService.countFloorInfo(floor);
            this.countBuildingInfo(building.getId());
        }
        return true;
    }

    @Override
    public boolean updateFull(BuildingUpdateVo buildingUpdateVo) {
        Building building = this.getById(buildingUpdateVo.getId());
        if (!building.getName().equals(buildingUpdateVo.getName())) {
            building.setName(buildingUpdateVo.getName());
            this.updateById(building);
        }
        for (UnitUpdateVo unitUpdateVo : buildingUpdateVo.getUnitUpdateVos()) {
            if (unitUpdateVo.getFloorNo() == null) {
                continue;
            }
            Floor floor = this.floorService.getByBuildingIdAndFloorNo(building.getId(), unitUpdateVo.getFloorNo());
            if (floor == null) {
                floor = new Floor();
                floor.setFloorNo(unitUpdateVo.getFloorNo());
                floor.setBuildingId(building.getId());
                this.floorService.save(floor);
            }
            Unit unit = UnitConvert.INSTANCE.updateVo2Entity(unitUpdateVo);
            unit.setBuildingId(building.getId());
            unit.setFloorId(floor.getId());
            unit.setFloorNo(floor.getFloorNo());
            if (unit.getId() != null) {
                //需要以前的单元状态
                Unit oldUnit = unitService.getById(unit.getId());
                unit.setStatus(oldUnit.getStatus());
                this.unitService.createUpdateLog(unitUpdateVo,oldUnit);
                this.unitService.validUnitName(unit.getUnitNo(), floor.getId(), unit.getId());
                this.unitService.updateById(unit);
            } else {
                //设置删除标识为0
                unit.setDelFlag("0");
                this.unitService.validUnitName(unit.getUnitNo(), floor.getId(), null);
                this.unitService.save(unit);
            }
            this.unitService.countFloorInfo(floor);
            this.countBuildingInfo(building.getId());
        }

        List<UnitUpdateVo> unitUpdateVos = buildingUpdateVo.getUnitUpdateVos();
        if (CollectionUtils.isNotEmpty(unitUpdateVos)) {
            Building byId = this.getById(building.getId());
            int floorAmount = unitUpdateVos.stream().collect(Collectors.groupingBy(UnitUpdateVo::getFloorNo, Collectors.counting())).size();
            byId.setFloorAmount(floorAmount);
            this.updateById(byId);
        }
        return true;
    }

    @Override
    public boolean delete(Long id) {
        List<Unit> units = this.unitService.getAllListByBuildingId(id);
        if (units.size() != 0) {
            throw new BizException(BizCodeFace.createBizCode(ErrorCode.FAIL).message("楼栋下存在单元，不允许删除"));
        }
        return this.removeById(id);
    }

    @Override
    public Building getBuildingByNameAndParkId(String buildingName, Long parkId) {
        QueryWrapper<Building> wrapper = new QueryWrapper<>();
        wrapper.lambda()
                .eq(Building::getName, buildingName)
                .eq((Building::getParkId), parkId);
        return this.getOne(wrapper);
    }

    @Override
    public List<KeyValueVo> getListByParkId(Long parkId) {
        QueryWrapper<Building> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq((Building::getParkId), parkId);
        List<Building> buildings = this.list(wrapper);
        List<KeyValueVo> list = new ArrayList<>();
        for (Building building : buildings) {
            KeyValueVo keyValueVo = new KeyValueVo();
            keyValueVo.setKey(building.getId());
            keyValueVo.setValue(building.getName());
            list.add(keyValueVo);
        }
        return list;
    }

    @Override
    public BuildingBasicDTO selectBasic(Long buildingId) {
        return this.getBaseMapper().selectBasic(buildingId);
    }

    private void validNameUnique(Building entity) {
        QueryWrapper<Building> wrapper = new QueryWrapper<>();
        wrapper.lambda()
                .eq(Building::getName, entity.getName())
                .eq((Building::getParkId), entity.getParkId());
        if (entity.getId() != null && entity.getId() != 0) {
            wrapper.lambda().ne(Building::getId, entity.getId());
        }
        Building existBuilding = this.getOne(wrapper);
        ValidationUtil.notNull(existBuilding, "楼栋", "名称", entity.getName());
    }



    @Autowired
    private void setEstablishService(IEstablishService establishService) {
        this.establishService = establishService;
    }

    @Autowired
    private void setFloorService(IFloorService floorService) {
        this.floorService = floorService;
    }

    @Autowired
    private void setUnitService(IUnitService unitService) {
        this.unitService = unitService;
    }


    @Autowired
    private void setParkService(IParkService parkService) {
        this.parkService = parkService;
    }
}
