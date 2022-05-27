package com.rihao.property.modules.estate.service.impl;

import cn.hutool.core.util.NumberUtil;
import com.anteng.boot.core.facade.code.BizCodeFace;
import com.anteng.boot.core.facade.code.ErrorCode;
import com.anteng.boot.core.facade.exception.BizException;
import com.anteng.boot.pojo.query.Filter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.rihao.property.common.oss.IOssService;
import com.rihao.property.common.page.PageVo;
import com.rihao.property.common.vo.KeyValueVo;
import com.rihao.property.modules.common.vo.UploadResultVo;
import com.rihao.property.modules.config.emuns.SysFileType;
import com.rihao.property.modules.config.service.ISysFileService;
import com.rihao.property.modules.ent.controller.params.ParkImport;
import com.rihao.property.modules.establish.entity.Establish;
import com.rihao.property.modules.establish.service.IEstablishService;
import com.rihao.property.modules.estate.controller.params.ParkQueryParam;
import com.rihao.property.modules.estate.convert.BuildingConvert;
import com.rihao.property.modules.estate.convert.ParkConvert;
import com.rihao.property.modules.estate.convert.UnitConvert;
import com.rihao.property.modules.estate.entity.*;
import com.rihao.property.modules.estate.enums.UnitState;
import com.rihao.property.modules.estate.enums.UnitUseType;
import com.rihao.property.modules.estate.mapper.ParkMapper;
import com.rihao.property.modules.estate.service.*;
import com.rihao.property.modules.estate.vo.*;
import com.rihao.property.modules.system.entity.SysUser;
import com.rihao.property.shiro.util.JwtUtil;
import com.rihao.property.util.NumberUtils;
import com.rihao.property.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wangyu
 * @since 2021-07-05
 */
@Service
public class ParkServiceImpl extends ServiceImpl<ParkMapper, Park> implements IParkService {

    @Autowired
    @Lazy
    private IBuildingService buildingService;
    private IOssService ossService;
    private IPurchaseRecordService purchaseRecordService;
    private IParkFileService fileService;
    private IEstablishService establishService;
    private IFloorService floorService;
    private IUnitService unitService;
    private IParkOperatorService parkOperatorService;
    private ISysFileService sysFileService;
    @Override
    public PageVo<ParkVo> search(ParkQueryParam parkQueryParam) {
        Page<Park> page = new Page<>(parkQueryParam.getCurrent(), parkQueryParam.getPageSize());
        page.setOrders(Lists.newArrayList(
                OrderItem.asc("create_time")
        ));
        ParkMapper.QueryParam params = new ParkMapper.QueryParam();
        if (JwtUtil.getCurrentUser().getRoleId() != 1) {
            params.setParkIds(JwtUtil.getCurrentUserParkIds());
        }
        if (StringUtils.hasText(parkQueryParam.getName())) {
            params.setName(Filter.LikeValue.both(parkQueryParam.getName()));
        }
        Page<ParkVo> result = this.getBaseMapper().selectByQueryParam(page, params);
        List<ParkVo> list = result.getRecords();
        for (ParkVo parkVo : list) {
            PurchaseRecord record = this.purchaseRecordService.getEnableRecord(parkVo.getId());
            if (record != null) {
                parkVo.setManagementFee(record.getManagementFee());
            }
            List<Building> buildings = this.buildingService.getByParkId(parkVo.getId());
            parkVo.setBuildingAmount(buildings.size());
            parkVo.setFiles(this.sysFileService.query().eq("business_id",parkVo.getId()).eq("type", SysFileType.CONTRACTTEMPFILE.getValue()).list());
            //查询空闲单元数，与空闲单元面积
            if (!CollectionUtils.isEmpty(buildings)){
                List<Long> buildIds = buildings.stream().map(Building::getId).collect(Collectors.toList());
                List<Unit> freeListByBuildingId = this.unitService.getFreeListByBuildingId(buildIds);
                if (!CollectionUtils.isEmpty(freeListByBuildingId)){
                    //空闲单元数
                    parkVo.setFreeUnitNum(freeListByBuildingId.size());
                    //空闲面积
                    parkVo.setFreeUnitArea(freeListByBuildingId.stream().map(Unit::getBuiltUpArea).reduce(BigDecimal.ZERO, NumberUtil::add));
                }

            }
        }


        return PageVo.create(parkQueryParam.getCurrent(), parkQueryParam.getPageSize(),
                result.getTotal(), list);
    }

    @Override
    public ParkVo detail(Long id) {
        ParkVo vo = ParkConvert.INSTANCE.entity2Vo(this.getById(id));
        PurchaseRecord record = this.purchaseRecordService.getEnableRecord(id);
        if (record != null)
            vo.setManagementFee(record.getManagementFee());
        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(ParkUpdateVo parkUpdateVo) {
        this.validNameUnique(parkUpdateVo.getName(), parkUpdateVo.getId());
        Park entity = ParkConvert.INSTANCE.updateVo2Entity(parkUpdateVo);
        //需要验证数值大小是否满足数据库字段大小
        this.validNumberData(entity);
        Park oldEntity = this.getById(parkUpdateVo.getId());
        entity.setEstId(oldEntity.getEstId());
        Boolean result = this.updateById(entity);
        this.countParkService(entity.getId());
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createNew(ParkCreateVo parkCreateVo) {
        this.validNameUnique(parkCreateVo.getName(), null);

        Park entity = ParkConvert.INSTANCE.createVo2Entity(parkCreateVo);
        //需要验证数值大小是否满足数据库字段大小
        this.validNumberData(entity);
        entity.setEstId(JwtUtil.getCurrentUser().getEstablishId());
        boolean result = this.save(entity);

        ParkOperator parkOperator = new ParkOperator();
        parkOperator.setParkId(entity.getId());
        parkOperator.setOperatorId(JwtUtil.getCurrentUser().getId());
        this.parkOperatorService.save(parkOperator);

        this.countParkService(entity.getId());

        return result;
    }

    @Override
    public boolean delete(Long id) {
        List<Building> buildingList = this.buildingService.getByParkId(id);
        if (buildingList.size() != 0) {
            throw new BizException(BizCodeFace.createBizCode(ErrorCode.FAIL).message("园区下存在楼栋，不允许删除"));
        }
        return this.removeById(id);
    }

    @Override
    public List<KeyValueVo> getByEstId(Long establishId) {
        QueryWrapper<Park> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Park::getEstId, establishId);
        List<Park> parks = this.list(wrapper);
        List<KeyValueVo> keyValueVos = new ArrayList<>();
        for (Park park : parks) {
            KeyValueVo keyValueVo = new KeyValueVo();
            keyValueVo.setKey(park.getId());
            keyValueVo.setValue(park.getName());
            keyValueVos.add(keyValueVo);
        }
        return keyValueVos;
    }

    @Override
    public Park getByName(String parkName) {
        QueryWrapper<Park> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Park::getName, parkName);
        return this.getOne(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void uploadContractTemp(Long id, MultipartFile file) throws IOException {
        Park park = this.getById(id);
        String name = park.getName() + "-合同模版";
        UploadResultVo uploadResultVo = this.ossService.uploadFile(name, file);
        park.setContractTempUrl(uploadResultVo.getUrl());
        this.updateById(park);
    }

    @Override
    public ParkVo panorama(Long id) {
        ParkVo vo = ParkConvert.INSTANCE.entity2Vo(this.getById(id));
        Float manageFee = this.purchaseRecordService.getManageFee(id);
        if (manageFee != null)
            vo.setManagementFee(manageFee.toString());
        vo.setFileList(this.getFileListByParkId(id, vo.getName()));
        List<BuildingVo> buildingListByParkId = this.getBuildingListByParkId(id);
        vo.setBuildingList(buildingListByParkId);
        vo.setBuildingAmount(vo.getBuildingList().size());
        vo.setPurchaseRecordVoList(this.getPurchaseRecordList(id));
        vo.setFiles(this.sysFileService.query().eq("business_id",id).eq("type",SysFileType.PARKFILE.getValue()).list());

        //通过园区查询所有单元信息
        if (!CollectionUtils.isEmpty(buildingListByParkId)){
            List<Long> buildIds = buildingListByParkId.stream().map(BuildingVo::getId).collect(Collectors.toList());
            List<Unit> list = unitService.list(new QueryWrapper<Unit>().lambda().in(Unit::getBuildingId, buildIds));
            List<UnitVo> unitVos = UnitConvert.INSTANCE.entity2ListItemBatch(list);
            vo.setAreaMap(unitService.useTypeAreaSumAndRent(unitVos));
            //计算楼栋的空闲面积
            for (int i = 0; i < buildingListByParkId.size(); i++) {
                List<Unit> unitList = unitService.list(new QueryWrapper<Unit>().lambda().eq(Unit::getBuildingId, buildingListByParkId.get(i).getId()));
                List<UnitVo> unitVos2 = UnitConvert.INSTANCE.entity2ListItemBatch(unitList);
                buildingListByParkId.get(i).setAreaMap(unitService.useTypeAreaSumAndRent(unitVos2));
            }
        }
        return vo;
    }

    private List<PurchaseRecordVo> getPurchaseRecordList(Long id) {
        List<PurchaseRecordVo> voList = this.purchaseRecordService.getListByParkId(id);
        for (PurchaseRecordVo purchaseRecordVo : voList) {
            /*String signDate = purchaseRecordVo.getSignDate();
            String prefix = signDate.substring(0, 4);
            String suffix = signDate.substring(4, 10);
            prefix = (Integer.parseInt(prefix) + Integer.parseInt(purchaseRecordVo.getContractTerm())) + "";
            purchaseRecordVo.setDueDate(prefix + suffix);*/

            if (purchaseRecordVo.getWinningDate() != null)
                purchaseRecordVo.setWinningDate(purchaseRecordVo.getWinningDate().substring(0, 10));
            if (purchaseRecordVo.getSignDate() != null)
                purchaseRecordVo.setSignDate(purchaseRecordVo.getSignDate().substring(0, 10));
            if (purchaseRecordVo.getBiddingDate() != null)
                purchaseRecordVo.setBiddingDate(purchaseRecordVo.getBiddingDate().substring(0, 10));
            //设置合同开始和结束时间
            if(purchaseRecordVo.getContractStartDate() != null){
                purchaseRecordVo.setContractStartDate(purchaseRecordVo.getContractStartDate().substring(0, 10));
            }
            if(purchaseRecordVo.getContractEndDate() != null){
                purchaseRecordVo.setContractEndDate(purchaseRecordVo.getContractEndDate().substring(0, 10));
            }
            purchaseRecordVo.setFiles(this.sysFileService.query().eq("business_id",purchaseRecordVo.getId()).eq("type",SysFileType.PURCHASERECORD.getValue()).list());

        }
        return voList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void countParkService(Long id) {
        Park park = this.getById(id);
        BigDecimal totalUsableArea = BigDecimal.ZERO, totalBuiltUpArea = BigDecimal.ZERO;
        int floorAmount = 0;
        List<Building> buildingList = this.buildingService.getByParkId(park.getId());
        for (Building building : buildingList) {
            if (building.getUsableArea() != null)
                totalUsableArea = totalUsableArea.add(building.getUsableArea());
            if (building.getBuiltUpArea() != null)
                totalBuiltUpArea = totalBuiltUpArea.add(building.getBuiltUpArea());
            if (building.getFloorAmount() != null) {
                floorAmount = floorAmount + building.getFloorAmount();
            }
        }
        park.setFloorAmount(floorAmount);
        park.setUsableArea(totalUsableArea);
        park.setBuiltUpArea(totalBuiltUpArea);
        this.updateById(park);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean importBuilding(List[] results) {
        List list = results[0];
        if (list.size() == 0) {
            throw new BizException(BizCodeFace.createBizCode(ErrorCode.FAIL).message("列表数据为空，请检查"));
        }
        for (Object o : list) {
            ParkImport parkImport = ((ParkImport) o);
            //通过当前登录的人员得到单位信息
            SysUser currentUser = JwtUtil.getCurrentUser();
            this.validExcelData(parkImport);
            Establish establish = this.establishService.getById(currentUser.getEstablishId());
            if (establish == null) {
                throw new BizException(BizCodeFace.createBizCode(ErrorCode.FAIL).message("单位不存在，请新建单位！"));
            }
            String parkName = parkImport.getPark();
            Park park = this.getByName(parkName);
            if (park == null) {
                park = new Park();
                park.setName(parkName);
                park.setEstId(establish.getId());
                this.save(park);

                ParkOperator parkOperator = new ParkOperator();
                parkOperator.setParkId(park.getId());
                parkOperator.setOperatorId(JwtUtil.getCurrentUser().getId());
                this.parkOperatorService.save(parkOperator);
            }
            String buildingName = parkImport.getBuilding();
            Building building = this.buildingService.getBuildingByNameAndParkId(buildingName, park.getId());
            if (building == null) {
                building = new Building();
                building.setName(buildingName);
                building.setEstId(establish.getId());
                building.setParkId(park.getId());
                this.buildingService.save(building);
            }
            String floorName = parkImport.getFloor();
            Floor floor = this.floorService.getByBuildingIdAndFloorNo(building.getId(), floorName);
            if (floor == null) {
                floor = new Floor();
                floor.setFloorNo(floorName);
                floor.setBuildingId(building.getId());
                floor.setBuildingName(buildingName);
                this.floorService.validFloorName(floorName, building.getId(), null);
                this.floorService.save(floor);
            }
            String unitName = parkImport.getUnit();
            Unit unit = this.unitService.getUnitByBuildingIdAndName(building.getId(), unitName);
            if (unit != null) {
                throw new BizException(BizCodeFace.createBizCode(ErrorCode.FAIL)
                        .message(parkName + "园区下，已经存在单元：" + unitName + "，请检查数据！"));
            }
            unit = new Unit();
            unit.setUnitNo(unitName);
            unit.setFloorId(floor.getId());
            unit.setFloorNo(floorName);
            unit.setBuildingId(building.getId());
            unit.setBuiltUpArea(parkImport.getBuiltUpArea());
            unit.setUsableArea(parkImport.getUsableArea());
            unit.setDelFlag("0");
            this.getUnitUseType(unit, parkImport.getUnitUseType());
            this.unitService.validUnitName(unitName, floor.getId(), null);
            this.unitService.save(unit);

            this.unitService.countFloorInfo(floor);
            this.buildingService.countBuildingInfo(building.getId());
        }
        return true;
    }

    private void validExcelData(ParkImport parkImport) {

        if (!StringUtils.hasText(parkImport.getPark())) {
            throw new BizException(BizCodeFace.createBizCode(ErrorCode.FAIL).message("园区名称不可为空！"));
        }
        if (!StringUtils.hasText(parkImport.getBuilding())) {
            throw new BizException(BizCodeFace.createBizCode(ErrorCode.FAIL).message("楼栋名称不可为空！"));
        }
        if (!StringUtils.hasText(parkImport.getFloor())) {
            throw new BizException(BizCodeFace.createBizCode(ErrorCode.FAIL).message("楼层不可为空！"));
        }
        if (!StringUtils.hasText(parkImport.getUnit())) {
            throw new BizException(BizCodeFace.createBizCode(ErrorCode.FAIL).message("单元不可为空！"));
        }
        if (parkImport.getBuiltUpArea() == null) {
            throw new BizException(BizCodeFace.createBizCode(ErrorCode.FAIL).message("建筑面积不可为空！"));
        }
        if (!StringUtils.hasText(parkImport.getUnitUseType())) {
            throw new BizException(BizCodeFace.createBizCode(ErrorCode.FAIL).message("房屋用途不可为空！"));
        }
    }

    @Override
    public List<KeyValueVo> getPermissionList() {
        QueryWrapper<Park> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(Park::getId, JwtUtil.getCurrentUserParkIds().split(","));
        List<Park> parks = this.list(wrapper);
        List<KeyValueVo> keyValueVos = new ArrayList<>();
        for (Park park : parks) {
            KeyValueVo keyValueVo = new KeyValueVo();
            keyValueVo.setKey(park.getId());
            keyValueVo.setValue(park.getName());
            keyValueVos.add(keyValueVo);
        }
        return keyValueVos;
    }

    @Override
    public String getByBuildingId(Long buildingId) {
        Building building = this.buildingService.getById(buildingId);
        Park park = this.getById(building.getParkId());
        return park.getName();
    }

    private void getUnitUseType(Unit unit, String unitUseType) {
        switch (unitUseType) {
            case "自用":
                unit.setUseType(UnitUseType.for_self);
                break;
            case "公用":
                unit.setUseType(UnitUseType.for_public);
                break;
            case "物业用房":
                unit.setUseType(UnitUseType.for_estate);
                break;
            case "产业租赁":
                unit.setUseType(UnitUseType.industry);
                break;
            case "配套租赁":
                unit.setUseType(UnitUseType.set);
                break;
            default:
                throw new BizException(BizCodeFace.createBizCode(ErrorCode.FAIL).message("房屋用途不合法,或者为空,请检查"));
        }
    }

    private List<BuildingVo> getBuildingListByParkId(Long id) {
        Park park = this.getById(id);
        List<BuildingVo> buildingList = BuildingConvert.INSTANCE.entity2ListItemBatch(this.buildingService.getByParkId(id));
        for (BuildingVo buildingVo : buildingList) {
            buildingVo.setParkName(park.getName());
        }
        return buildingList;
    }

    private List<ParkFileVo> getFileListByParkId(Long parkId, String parkName) {
        List<ParkFileVo> vos = this.fileService.getByParkId(parkId);
        for (ParkFileVo vo : vos) {
            vo.setParkName(parkName);
            vo.setFiles(this.sysFileService.query().eq("business_id",vo.getId()).eq("type",SysFileType.PARKFILE.getValue()).list());
        }
        return vos;
    }

    private void validNameUnique(String name, Long id) {
        QueryWrapper<Park> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Park::getName, name);
        if (id != null && id != 0) {
            wrapper.lambda().ne(Park::getId, id);
        }
        Park entity = this.getOne(wrapper);
        ValidationUtil.notNull(entity, "园区", "名称", name);
    }

    //验证园区的数值大小
    private void validNumberData(Park park){
        if(park != null){
            //数值大于最大值
            ValidationUtil.outMaxValue(park.getBuiltUpArea(),"总建筑面积");
            ValidationUtil.outMaxValue(park.getCoveredArea(),"占地面积");
            ValidationUtil.outMaxValue(park.getTotalArea(),"总面积");
            ValidationUtil.outMaxValue(park.getUsableArea(),"总使用面积");
        }
    }


    @Autowired
    private void setOssService(IOssService ossService) {
        this.ossService = ossService;
    }

    @Autowired
    private void setPurchaseRecordService(IPurchaseRecordService purchaseRecordService) {
        this.purchaseRecordService = purchaseRecordService;
    }

    @Autowired
    private void setFileService(IParkFileService fileService) {
        this.fileService = fileService;
    }

    @Autowired
    private void setEstablishService(IEstablishService establishService) {
        this.establishService = establishService;
    }

    @Autowired
    private void setUnitService(IUnitService unitService) {
        this.unitService = unitService;
    }

    @Autowired
    private void setFloorService(IFloorService floorService) {
        this.floorService = floorService;
    }

    @Autowired
    private void setParkOperatorService(IParkOperatorService parkOperatorService) {
        this.parkOperatorService = parkOperatorService;
    }

    @Autowired
    private void setSysFileService(ISysFileService sysFileService) {
        this.sysFileService = sysFileService;
    }
}
