package com.rihao.property.modules.estate.service.impl;

import cn.hutool.core.text.StrBuilder;
import cn.hutool.core.util.StrUtil;
import com.anteng.boot.core.facade.code.BizCodeFace;
import com.anteng.boot.core.facade.code.ErrorCode;
import com.anteng.boot.core.facade.exception.BizException;
import com.anteng.boot.pojo.query.Filter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.rihao.property.common.page.PageParams;
import com.rihao.property.common.page.PageVo;
import com.rihao.property.common.vo.KeyValueVo;
import com.rihao.property.modules.close_history.entity.ContractCloseHistory;
import com.rihao.property.modules.close_history.service.IContractCloseHistoryService;
import com.rihao.property.modules.ent.entity.Ent;
import com.rihao.property.modules.ent.service.IEntService;
import com.rihao.property.modules.estate.controller.params.UnitQueryParam;
import com.rihao.property.modules.estate.convert.UnitConvert;
import com.rihao.property.modules.estate.entity.*;
import com.rihao.property.modules.estate.enums.SplitAndMergeState;
import com.rihao.property.modules.estate.enums.UnitState;
import com.rihao.property.modules.estate.enums.UnitUseType;
import com.rihao.property.modules.estate.mapper.UnitMapper;
import com.rihao.property.modules.estate.service.*;
import com.rihao.property.modules.estate.vo.*;
import com.rihao.property.modules.lease.contract.convert.ContractConvert;
import com.rihao.property.modules.lease.contract.entity.Contract;
import com.rihao.property.modules.lease.contract.service.IContractService;
import com.rihao.property.modules.lease.contract.vo.ContractVo;
import com.rihao.property.modules.lease.unit.entity.ContractUnit;
import com.rihao.property.modules.lease.unit.service.IContractUnitService;
import com.rihao.property.modules.report.controller.params.UseHouseDetailQueryParam;
import com.rihao.property.shiro.util.JwtUtil;
import com.rihao.property.util.NumberUtils;
import com.rihao.property.util.ValidationUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.regexp.RE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
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
public class UnitServiceImpl extends ServiceImpl<UnitMapper, Unit> implements IUnitService {
    @Autowired
    @Lazy
    private IContractUnitService contractUnitService;
    @Autowired
    @Lazy
    private IFloorService floorService;
    @Autowired
    @Lazy
    private IEntService entService;
    @Autowired
    @Lazy
    private IContractService contractService;
    @Autowired
    @Lazy
    private ISplitMergeHistoryService splitMergeHistoryService;
    @Autowired
    @Lazy
    private IBuildingService buildingService;
    @Autowired
    @Lazy
    private IParkService parkService;
    @Autowired
    @Lazy
    private IContractCloseHistoryService contractCloseHistoryService;
    @Autowired
    @Lazy
    private IUnitUpdateLogService unitUpdateLogService;

    @Override
    public PageVo<UnitVo> search(UnitQueryParam unitQueryParam) {
        Page<Unit> page = new Page<>(unitQueryParam.getCurrent(), unitQueryParam.getPageSize());
        page.setOrders(Lists.newArrayList(
                OrderItem.desc("create_time")
        ));
        UnitMapper.QueryParam params = new UnitMapper.QueryParam();
        if (unitQueryParam.getFloorId() != null) {
            params.setFloorId(unitQueryParam.getFloorId());
        }
        if (unitQueryParam.getStatus() != null) {
            params.setStatus(unitQueryParam.getStatus());
        }
        Page<UnitVo> result = this.getBaseMapper().selectByQueryParam(page, params);

        return PageVo.create(unitQueryParam.getCurrent(), unitQueryParam.getPageSize(),
                result.getTotal(), result.getRecords());
    }

    @Override
    public UnitVo detail(Long id) {
        return UnitConvert.INSTANCE.entity2Vo(this.getById(id));
    }

    @Override
    public String getAreaByBuildingIdAndUnitNames(Long buildingId, String unitNames) {
        String[] names = unitNames.split(",");
        BigDecimal area = BigDecimal.ZERO;
        for (String name : names) {
            Unit unit = this.getUnitByBuildingIdAndName(buildingId, name);
            area = area.add(unit.getUsableArea());
        }
        return area.toString();
    }

    @Override
    public Unit getUnitByBuildingIdAndName(Long buildingId, String unitName) {
        QueryWrapper<Unit> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(Unit::getUnitNo, unitName)
                .eq(Unit::getBuildingId, buildingId);
        return getOne(queryWrapper);
    }

    @Override
    public Unit getUnitByName(String name) {
        QueryWrapper<Unit> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(Unit::getUnitNo, name);
        return getOne(queryWrapper);
    }

    @Override
    public List<UnitKeyValueVo> searchFreeList(Long buildingId) {
        return this.getFreeList(buildingId);
    }

    @Override
    public List<UnitKeyValueVo> searchContractUnitList(Long contractId) {
        List<ContractUnit> list = this.contractUnitService.getUnitListByContractId(contractId);
        List<UnitKeyValueVo> keyValueVos = new ArrayList<>();
        for (ContractUnit o : list) {
            Unit unit = this.getById(o.getUnitId());
            UnitKeyValueVo unitKeyValueVo = new UnitKeyValueVo();
            unitKeyValueVo.setId(unit.getId());
            unitKeyValueVo.setName(unit.getUnitNo());
            keyValueVos.add(unitKeyValueVo);
        }

        return keyValueVos;
    }

    @Override
    public List<UnitKeyValueVo> searchFreeListByContract(Long buildingId, Long contractId) {
        List<UnitKeyValueVo> keyValueVos = this.getFreeList(buildingId);
        List<Unit> list = new ArrayList<>();

        List<ContractUnit> contractUnits = this.contractUnitService.getUnitListByContractId(contractId);
        for (ContractUnit contractUnit : contractUnits) {
            Unit unit = this.getById(contractUnit.getUnitId());
            list.add(unit);
        }
        for (Unit unit : list) {
            UnitKeyValueVo unitKeyValueVo = new UnitKeyValueVo();
            unitKeyValueVo.setId(unit.getId());
            unitKeyValueVo.setName(unit.getUnitNo());
            keyValueVos.add(unitKeyValueVo);
        }

        return keyValueVos;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean createNew(UnitCreateVo createVo) {
        Floor floor = this.floorService.getByBuildingIdAndFloorNo(createVo.getBuildingId(), createVo.getFloorNo());
        if (floor == null) {
            floor = new Floor();
            floor.setFloorNo(createVo.getFloorNo());
            floor.setBuildingId(createVo.getBuildingId());
            this.floorService.save(floor);
        }
        this.validUnitName(createVo.getUnitNo(), floor.getId(), null);
        Unit unit = UnitConvert.INSTANCE.createVo2Entity(createVo);
        unit.setFloorNo(floor.getFloorNo());
        unit.setFloorId(floor.getId());
        unit.setStatus(UnitState.free);
        unit.setDelFlag("0");
        Boolean result = this.save(unit);
        this.countFloorInfo(floor);
        //将新增的单元的面积累加到楼栋面积中
        this.buildingService.countBuildingInfo(createVo.getBuildingId());
        return result;
    }

    public void validUnitName(String unitNo, Long floorId, Long id) {
        QueryWrapper<Unit> wrapper = new QueryWrapper<>();
        wrapper.lambda()
                .eq(Unit::getFloorId, floorId)
                .eq(Unit::getUnitNo, unitNo)
                .eq(Unit::getSplitAndMergeState, SplitAndMergeState.original);
        if (id != null && id != 0) {
            wrapper.lambda().ne(Unit::getId, id);
        }
        Unit entity = this.getOne(wrapper);
        ValidationUtil.notNull(entity, "单元", "单元号", unitNo);
    }

    @Override
    public List<Unit> getAllListByBuildingId(Long buildingId) {
        QueryWrapper<Unit> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Unit::getBuildingId, buildingId)
                .eq(Unit::getDelFlag, "0")
                .orderByAsc(Unit::getFloorNo)
                .orderByAsc(Unit::getUnitNo);
        return this.list(wrapper);
    }

    @Override
    public List<Unit> getFreeListByBuildingId(List<Long> buildingIds) {
        QueryWrapper<Unit> wrapper = new QueryWrapper<>();
        wrapper.lambda()
                .in(Unit::getBuildingId, buildingIds)
                .eq(Unit::getStatus, UnitState.free)
                .eq(Unit::getDelFlag, "0")
                .eq(Unit::getSplitAndMergeState, SplitAndMergeState.original)
                .and(w -> w.eq(Unit::getUseType, UnitUseType.set)
                        .or().eq(Unit::getUseType, UnitUseType.industry));
        return this.list(wrapper);
    }

    @Override
    public PageVo<UnitVo> searchAllList(UnitQueryParam unitQueryParam) {
        Page<Unit> page = new Page<>(unitQueryParam.getCurrent(), unitQueryParam.getPageSize());
        page.setOrders(Lists.newArrayList(
                OrderItem.desc("create_time")
        ));
        UnitMapper.QueryParam params = paramTransition(unitQueryParam);

        Page<UnitVo> result = this.getBaseMapper().selectByQueryParam(page, params);

        List<UnitVo> vos = result.getRecords();
        for (UnitVo vo : vos) {
            if (vo.getStatus().equals(UnitState.busy)) {
                //合同与单元关联
                ContractUnit contractUnit = this.contractUnitService.getNormalByUnitId(vo.getId(), LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),null);
                if (contractUnit != null && contractUnit.getId() != null) {
                    //合同
                    Contract contract = this.contractService.getById(contractUnit.getContractId());
                    if (contract != null && contract.getId() != null) {
                        //合同对应的企业
                        Ent ent = this.entService.getById(contract.getEntId());
                        if (ent != null && ent.getId() != null) {
                            vo.setEntName(ent.getName());
                            vo.setEntId(ent.getId());
                        }
                    }
                }

            }
        }
        return PageVo.create(unitQueryParam.getCurrent(), unitQueryParam.getPageSize(),
                result.getTotal(), vos);
    }

    /**
     * 参数转换
     * @param unitQueryParam
     * @return
     */
    private UnitMapper.QueryParam  paramTransition(UnitQueryParam unitQueryParam){
        UnitMapper.QueryParam params = new UnitMapper.QueryParam();
        params.setParkIds(JwtUtil.getCurrentUserParkIds());
        if (unitQueryParam.getParkId() != null)
            params.setParkId(unitQueryParam.getParkId());
        if (unitQueryParam.getBuildingId() != null)
            params.setBuildingId(unitQueryParam.getBuildingId());
        if (unitQueryParam.getFloorId() != null)
            params.setFloorId(unitQueryParam.getFloorId());
        if (unitQueryParam.getUnitNo() != null)
            params.setUnitNo(Filter.LikeValue.both(unitQueryParam.getUnitNo()));
        if (unitQueryParam.getLikeParam() != null)
            params.setLikeParam(unitQueryParam.getLikeParam());
        if (unitQueryParam.getStatus() != null)
            params.setStatus(unitQueryParam.getStatus());
        params.setDelFlag("0");
        return params;
    }
     @Override
    public Map<String, Double> searchAllArea(UnitQueryParam unitQueryParam) {
         UnitMapper.QueryParam params = paramTransition(unitQueryParam);
         Map<String,Double> result = this.getBaseMapper().selectByQueryParamSum(params);
         return result;
     }

    @Override
    public Boolean deleteById(Long id) {
        Unit unit = this.getById(id);
        if (unit.getStatus().equals(UnitState.busy)) {
            throw new BizException(BizCodeFace.createBizCode(ErrorCode.FAIL).message("该单元处于租赁中，不允许删除"));
        }
        //删除单据
        unit.setDelFlag("1");
        boolean result = this.updateById(unit);
        if (result) {
            Building building = buildingService.getById(unit.getBuildingId());
            List<Unit> units = this.getAllListByBuildingId(unit.getBuildingId());
            if (CollectionUtils.isNotEmpty(units)) {
                int floorAmount = units.stream().collect(Collectors.groupingBy(Unit::getFloorNo, Collectors.counting())).size();
                building.setFloorAmount(floorAmount);
                buildingService.updateById(building);
            }
        }
        return result;

    }

    @Override
    public PageVo<UnitVo> useHouseDetailReport(UseHouseDetailQueryParam useHouseDetailQueryParam) {
        UnitMapper.QueryParam queryParam=new UnitMapper.QueryParam();
        queryParam.setParkId(useHouseDetailQueryParam.getParkId());
        if(JwtUtil.getCurrentUser().getRoleId() !=1){
            String currentUserParkIds = JwtUtil.getCurrentUserParkIds();
            queryParam.setParkIds(currentUserParkIds);
        }

        Page<Unit> page = new Page<>(useHouseDetailQueryParam.getCurrent(), useHouseDetailQueryParam.getPageSize());
        Page<UnitVo> result = this.getBaseMapper().selectByQueryParam(page, queryParam);
        return  PageVo.create(useHouseDetailQueryParam.getCurrent(),useHouseDetailQueryParam.getPageSize(),result.getTotal(),result.getRecords());
    }

    /**&
     * //新增修改记录
     * @param updateVo
     * @param oldUnit
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean createUpdateLog(UnitUpdateVo updateVo,Unit oldUnit){
        //记录修改信息，
        UnitUpdateLog unitUpdateLog=new UnitUpdateLog();
        Boolean isUpdate=false; //是否有修改数据
        StrBuilder updateBeforeText=new StrBuilder();
        StrBuilder updateAfterText=new StrBuilder();
        //单元名称
        if(!StrUtil.equals(updateVo.getFloorNo(),oldUnit.getFloorNo())){
            updateBeforeText.append("楼层："+oldUnit.getFloorNo()+",");
            updateAfterText.append("楼层："+updateVo.getFloorNo()+",");
            isUpdate=true;
        }
        //单元名称
        if(!StrUtil.equals(updateVo.getUnitNo(),oldUnit.getUnitNo())){
            updateBeforeText.append("单元名称："+oldUnit.getUnitNo()+",");
            updateAfterText.append("单元名称："+updateVo.getUnitNo()+",");
            isUpdate=true;
        }
        //房屋用途
        if(updateVo.getUseType() != oldUnit.getUseType()){
            updateBeforeText.append("房屋用途："+getUseTypeName(oldUnit.getUseType())+",");
            updateAfterText.append("房屋用途："+getUseTypeName(updateVo.getUseType())+",");
            isUpdate=true;
        }
        //使用面积
        if(new BigDecimal(updateVo.getUsableArea()).compareTo(oldUnit.getUsableArea()) != 0){
            updateBeforeText.append("使用面积："+oldUnit.getUsableArea()+",");
            updateAfterText.append("使用面积："+updateVo.getUsableArea()+",");
            isUpdate=true;
        }
        //建筑面积
        if(new BigDecimal(updateVo.getBuiltUpArea()).compareTo(oldUnit.getBuiltUpArea()) != 0){
            updateBeforeText.append("建筑面积："+oldUnit.getBuiltUpArea()+",");
            updateAfterText.append("建筑面积："+updateVo.getBuiltUpArea()+",");
            isUpdate=true;
        }
        //有修改信息
        if (isUpdate){
            updateAfterText.del(updateAfterText.length()-1,updateAfterText.length());
            updateBeforeText.del(updateBeforeText.length()-1,updateBeforeText.length());
            unitUpdateLog.setUpdateAfterText(updateAfterText.toString());
            unitUpdateLog.setUpdateBeforeText(updateBeforeText.toString());
            unitUpdateLog.setUnitId(oldUnit.getId());
            unitUpdateLogService.save(unitUpdateLog);
        }
        return isUpdate;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean update(UnitUpdateVo updateVo) {
        this.validUnitName(updateVo.getUnitNo(), updateVo.getFloorId(), updateVo.getId());
        Unit unit = UnitConvert.INSTANCE.updateVo2Entity(updateVo);
        //需要以前的单元状态
        Unit oldUnit = getById(unit.getId());
        unit.setStatus(oldUnit.getStatus());
        //新增修改记录
        createUpdateLog(updateVo,oldUnit);
        Boolean result = this.updateById(unit);
        Floor floor = this.floorService.getById(unit.getFloorId());
        // 计算楼层数据
        this.countFloorInfo(floor);
        // 计算大楼数据
        this.buildingService.countBuildingInfo(floor.getBuildingId());
        return result;
    }

    @Override
    public String getAreaByUnitIds(String unitIds) {
        String[] ids = unitIds.split(",");
        BigDecimal area = BigDecimal.ZERO;
        for (String id : ids) {
            Unit unit = this.getById(id);
            area = area.add(unit.getBuiltUpArea());
        }
        return area.toString();
    }

    @Override
    public List<UnitVo> getByFloorId(Long floorId) {
        QueryWrapper<Unit> wrapper = new QueryWrapper<>();
        wrapper.lambda()
                .eq(Unit::getFloorId, floorId)
                .eq(Unit::getSplitAndMergeState, SplitAndMergeState.original)
                .eq(Unit::getDelFlag, "0");
        List<Unit> units = this.list(wrapper);
        List<UnitVo> vos = UnitConvert.INSTANCE.entity2ListItemBatch(units);
        for (UnitVo vo : vos) {
            if (vo.getStatus().equals(UnitState.busy)) {
                ContractUnit contractUnit = this.contractUnitService.getNormalByUnitId(vo.getId(), LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),null);
                Contract contract = this.contractService.getById(contractUnit.getContractId());
                Ent ent = this.entService.getById(contract.getEntId());
                vo.setEntName(ent.getName());
                vo.setEntId(ent.getId());
            }
        }
        return vos;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean split(UnitSplitVo unitSplitVo) {
        Unit unit = this.getById(unitSplitVo.getUnitId());

        // 判断拆分后的单元数目
        if (unitSplitVo.getSplitDetailList() == null || unitSplitVo.getSplitDetailList().size() < 2) {
            throw new BizException(BizCodeFace.createBizCode(ErrorCode.FAIL).message("拆分后的单元至少为2个，请重新输入"));
        }

        // 判断拆分后的数据是否正确
        BigDecimal originalUsableArea = unit.getUsableArea();
        BigDecimal originalBuiltUpArea = unit.getBuiltUpArea();
        BigDecimal newUsableArea = BigDecimal.ZERO, newBuiltUpArea = BigDecimal.ZERO;
        for (UnitSplitDetailVo splitDetail : unitSplitVo.getSplitDetailList()) {
            newUsableArea = newUsableArea.add(splitDetail.getUsableArea());
            newBuiltUpArea = newBuiltUpArea.add(splitDetail.getBuiltUpArea());
        }
        if (newUsableArea.compareTo(originalUsableArea) != 0 || newBuiltUpArea.compareTo(originalBuiltUpArea) != 0) {
            throw new BizException(BizCodeFace.createBizCode(ErrorCode.FAIL).message("拆分前后的(建筑或使用)总面积不相等"));
        }

        // 设置原先单元状态为 已拆分
        unit.setSplitAndMergeState(SplitAndMergeState.split);
        //拆分后 当前单元为无效状态
        unit.setDelFlag("1");
        this.updateById(unit);

        // 判断名称是否唯一
        for (UnitSplitDetailVo splitDetail : unitSplitVo.getSplitDetailList()) {
            this.validUnitName(splitDetail.getUnitNo(), unit.getFloorId(), null);
        }

        List<String> orginalIds = new ArrayList<>();
        orginalIds.add(unit.getId().toString());

        List<String> resultIds = new ArrayList<>();
        for (UnitSplitDetailVo splitDetail : unitSplitVo.getSplitDetailList()) {
            Unit newUnit = new Unit();
            newUnit.setUnitNo(splitDetail.getUnitNo());
            newUnit.setUsableArea(splitDetail.getUsableArea());
            newUnit.setBuiltUpArea(splitDetail.getBuiltUpArea());
            newUnit.setBuildingId(unit.getBuildingId());
            newUnit.setFloorId(unit.getFloorId());
            newUnit.setFloorNo(unit.getFloorNo());
            newUnit.setDelFlag("0");
            // newUnit.setBuildingName(unit.getBuildingName());
            newUnit.setUseType(unit.getUseType());
            // newUnit.setRentType(unit.getRentType());
            this.save(newUnit);
            resultIds.add(newUnit.getId().toString());
        }

        SplitMergeHistory history = new SplitMergeHistory();
        history.setSplitMergeState(SplitAndMergeState.split);
        history.setOrginalUnitIds(String.join(",", orginalIds));
        history.setResultUnitIds(String.join(",", resultIds));
        this.splitMergeHistoryService.save(history);

        // 计算楼层数据
        Floor floor = this.floorService.getById(unit.getFloorId());
        this.countFloorInfo(floor);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean merge(UnitMergeVo unitMergeVo) {
        // 判断合并前的单元数目
        if (unitMergeVo.getOriginalIds() == null || unitMergeVo.getOriginalIds().length < 2) {
            throw new BizException(BizCodeFace.createBizCode(ErrorCode.FAIL).message("合并的的单元至少为2个，请重新输入"));
        }

        // 先关闭所有的单元，再去验证名称是否重复
        BigDecimal usableArea = BigDecimal.ZERO, builtUpArea = BigDecimal.ZERO;
        Long[] originalIds = unitMergeVo.getOriginalIds();
        for (Long id : originalIds) {
            Unit unit = this.getById(id);
            unit.setSplitAndMergeState(SplitAndMergeState.merge);
            //合并后单元就不显示
            unit.setDelFlag("1");
            usableArea = usableArea.add(unit.getUsableArea());
            builtUpArea = builtUpArea.add(unit.getBuiltUpArea());
            this.updateById(unit);
        }

        this.validUnitName(unitMergeVo.getUnitNo(), unitMergeVo.getFloorId(), null);

        Unit newUnit = new Unit();
        newUnit.setUnitNo(unitMergeVo.getUnitNo());
        newUnit.setUsableArea(usableArea);
        newUnit.setBuiltUpArea(builtUpArea);
        newUnit.setFloorId(unitMergeVo.getFloorId());
        newUnit.setDelFlag("0");
        Floor floor = this.floorService.getById(newUnit.getFloorId());
        //设置楼栋数
        newUnit.setFloorNo(floor.getFloorNo());
        Building building = this.buildingService.getById(floor.getBuildingId());
        newUnit.setBuildingId(building.getId());
        // newUnit.setBuildingName(building.getName());
        newUnit.setUseType(UnitUseType.industry);
        // newUnit.setRentType(RentType.industry);
        this.save(newUnit);

        SplitMergeHistory history = new SplitMergeHistory();
        history.setSplitMergeState(SplitAndMergeState.merge);
        String[] ids = new String[originalIds.length];
        for (int index = 0; index < originalIds.length; index++) {
            ids[index] = originalIds[index].toString();
        }
        history.setOrginalUnitIds(String.join(",", ids));
        history.setResultUnitIds(newUnit.getId().toString());
        this.splitMergeHistoryService.save(history);

        // 计算楼层数据
        this.countFloorInfo(floor);
        return true;
    }

    @Override
    public List<UnitVo> searchFreeListByFloorId(Long floorId) {
        QueryWrapper<Unit> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(Unit::getFloorId, floorId)
                .eq(Unit::getStatus, UnitState.free)
                .eq(Unit::getDelFlag, "0")
                .eq(Unit::getSplitAndMergeState, SplitAndMergeState.original)
                .and(wrapper -> wrapper.eq(Unit::getUseType, UnitUseType.set)
                        .or().eq(Unit::getUseType, UnitUseType.industry));

        List<Unit> freeList = this.list(queryWrapper);
        return UnitConvert.INSTANCE.entity2ListItemBatch(freeList);
    }

    private List<UnitKeyValueVo> getFreeList(Long buildingId) {
        QueryWrapper<Unit> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(Unit::getDelFlag, "0")
                .eq(Unit::getBuildingId, buildingId)
                .eq(Unit::getStatus, UnitState.free)
                .eq(Unit::getSplitAndMergeState, SplitAndMergeState.original)
                .and(wrapper -> wrapper.eq(Unit::getUseType, UnitUseType.set)
                        .or().eq(Unit::getUseType, UnitUseType.industry));

        List<Unit> freeList = this.list(queryWrapper);
        List<UnitKeyValueVo> keyValueVos = new ArrayList<>();
        for (Unit o : freeList) {
            UnitKeyValueVo unitKeyValueVo = new UnitKeyValueVo();
            unitKeyValueVo.setId(o.getId());
            unitKeyValueVo.setName(o.getUnitNo());
            keyValueVos.add(unitKeyValueVo);
        }

        return keyValueVos;
    }

    @Override
    public void countFloorInfo(Floor floor) {
        QueryWrapper<Unit> wrapper = new QueryWrapper<>();
        wrapper.lambda()
                .eq(Unit::getDelFlag, "0")
                .eq(Unit::getFloorId, floor.getId())
                .eq(Unit::getSplitAndMergeState, SplitAndMergeState.original);
        List<Unit> units = this.list(wrapper);

        // DecimalFormat df1 = new DecimalFormat("0.00");
        int freeUnitAmount = 0, busyUnitAmount = 0;
        BigDecimal totalUsableArea = BigDecimal.ZERO, totalBuiltUpArea = BigDecimal.ZERO;

        for (Unit unit : units) {
            if (unit.getStatus().equals(UnitState.free)) {
                freeUnitAmount++;
            }
            if (unit.getStatus().equals(UnitState.busy)) {
                busyUnitAmount++;
            }
            totalUsableArea = totalUsableArea.add(unit.getUsableArea());
            totalBuiltUpArea = totalBuiltUpArea.add(unit.getBuiltUpArea());
        }

        floor.setAllUnitAmount(units.size());
        floor.setFreeUnitAmount(freeUnitAmount);
        floor.setBusyUnitAmount(busyUnitAmount);
        floor.setTotalUsableArea(totalUsableArea);
        floor.setTotalBuiltUpArea(totalBuiltUpArea);
        this.floorService.updateById(floor);
    }

    @Override
    public List<UnitKeyValueVo> searchRentList(Long buildingId,String unitState) {
        QueryWrapper<Unit> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(Unit::getDelFlag, "0")
                .eq(Unit::getBuildingId, buildingId)
                .eq(Unit::getSplitAndMergeState, SplitAndMergeState.original)
                .eq(unitState!=null,Unit::getStatus, unitState)
                .and(wrapper -> wrapper.eq(Unit::getUseType, UnitUseType.set)
                        .or().eq(Unit::getUseType, UnitUseType.industry));

        List<Unit> rentList = this.list(queryWrapper);
        List<UnitKeyValueVo> keyValueVos = new ArrayList<>();
        for (Unit o : rentList) {
            UnitKeyValueVo unitKeyValueVo = new UnitKeyValueVo();
            unitKeyValueVo.setId(o.getId());
            if (o.getStatus() == UnitState.busy) {
                unitKeyValueVo.setName(o.getUnitNo() + "（租赁中）");
            } else if (o.getStatus() == UnitState.free) {
                unitKeyValueVo.setName(o.getUnitNo() + "（空闲中）");
            } else {
                unitKeyValueVo.setName(o.getUnitNo());
            }
            keyValueVos.add(unitKeyValueVo);
        }
        return keyValueVos;
    }

    @Override
    public String getUseTypeName(UnitUseType unitUseType) {
        switch (unitUseType) {
            case for_self:
                return "自用";
            case for_public:
                return "公用";
            case for_estate:
                return "物业用房";
            case industry:
                return "产业租赁";
            case set:
                return "配套租赁";
        }
        return null;
    }

    @Override
    public UnitVo panorama(Long id) {
        UnitVo vo = UnitConvert.INSTANCE.entity2Vo(this.getById(id));
        switch (vo.getUseType()) {
            case for_self:
                vo.setUseTypeName("自用");
                break;
            case for_public:
                vo.setUseTypeName("公用");
                break;
            case for_estate:
                vo.setUseTypeName("物业用房");
                break;
            case industry:
                vo.setUseTypeName("产业租赁");
                break;
            case set:
                vo.setUseTypeName("配套租赁");
                break;
        }
        //单元楼栋赋值
        if (vo != null){
            if (vo.getBuildingId() != null){
                Building byId = this.buildingService.getById(vo.getBuildingId());
                vo.setBuildingName(byId.getName());
                if (byId.getParkId() != null){
                    vo.setParkName(this.parkService.getById(byId.getParkId()).getName());
                }
            }

        }
        //拆分合并记录
        List<SplitMergeHistory> mergeHistories = this.splitMergeHistoryService.getMergeHistory(vo.getId());
        List<SplitMergeHistoryVo> mergeHistoryVos = new ArrayList<>();
        for (SplitMergeHistory mergeHistory : mergeHistories) {
            SplitMergeHistoryVo splitMergeHistoryVo = new SplitMergeHistoryVo();
            splitMergeHistoryVo.setType(this.getSplitMergeType(mergeHistory.getSplitMergeState()));

            List<KeyValueVo> beforeList = new ArrayList<>();
            List<String> before = new ArrayList<>();
            for (String unitId : mergeHistory.getOrginalUnitIds().split(",")) {
                Unit unit = this.getById(unitId);
                before.add(unit.getUnitNo());
                KeyValueVo keyValueVo = new KeyValueVo();
                keyValueVo.setKey(unit.getId());
                keyValueVo.setValue(unit.getUnitNo());
                beforeList.add(keyValueVo);
            }
            splitMergeHistoryVo.setBefore(before.toString());
            splitMergeHistoryVo.setBeforeList(beforeList);

            List<KeyValueVo> afterList = new ArrayList<>();
            List<String> after = new ArrayList<>();
            for (String unitId : mergeHistory.getResultUnitIds().split(",")) {
                Unit unit = this.getById(unitId);
                after.add(unit.getUnitNo());
                KeyValueVo keyValueVo = new KeyValueVo();
                keyValueVo.setKey(unit.getId());
                keyValueVo.setValue(unit.getUnitNo());
                afterList.add(keyValueVo);
            }
            splitMergeHistoryVo.setAfter(after.toString());
            splitMergeHistoryVo.setAfterList(afterList);

            splitMergeHistoryVo.setUser(mergeHistory.getCreateBy());
            splitMergeHistoryVo.setTime(mergeHistory.getCreateTime());
            mergeHistoryVos.add(splitMergeHistoryVo);
        }
        vo.setMergeHistories(mergeHistoryVos);
        //合同单元信息
        List<ContractUnit> contractUnits = this.contractUnitService.getAllListByUnitId(vo.getId());
        List<ContractVo> contractVos = new ArrayList<>();
        for (ContractUnit contractUnit : contractUnits) {
            ContractVo contractVo = ContractConvert.INSTANCE.entity2Vo(this.contractService.getById(contractUnit.getContractId()));
            Ent ent = this.entService.getById(contractVo.getEntId());
            contractVo.setEntName(ent.getName());

            ContractCloseHistory history = this.contractCloseHistoryService.getByContractId(contractVo.getId());
            if (history != null) {
                contractVo.setCloseType(history.getCloseType());
                contractVo.setCloseReason(history.getRemark());
                contractVo.setCloseTime(history.getCreateTime());
            }

            contractVos.add(contractVo);
        }
        vo.setContractVos(contractVos);

        QueryWrapper<UnitUpdateLog> quer=new QueryWrapper();
        quer.eq("unit_id",id);
        List<UnitUpdateLog> unitUpdateLogs = unitUpdateLogService.getBaseMapper().selectList(quer);
        if (CollectionUtils.isNotEmpty(unitUpdateLogs)){
            List<UnitUpdateLog> collect = unitUpdateLogs.stream().sorted(Comparator.comparing(UnitUpdateLog::getCreateTime,Comparator.reverseOrder())).collect(Collectors.toList());
            List<UnitUpdateLogVo> logVoList=new ArrayList<>();
            for (int j = 0; j < collect.size(); j++) {
                UnitUpdateLog item = collect.get(j);
                UnitUpdateLogVo logVo=new UnitUpdateLogVo();
                logVo.setId(item.getId());
                logVo.setModifyTime(item.getModifyTime());
                logVo.setModifyBy(item.getModifyBy());
                logVo.setUpdateAfterText(item.getUpdateAfterText());
                logVo.setUpdateBeforeText(item.getUpdateBeforeText());
                logVo.setSort(j+1);
                logVoList.add(logVo);
            }

            vo.setUpdateLogVos(logVoList);
        }
        return vo;
    }

    @Override
    public PageVo<SplitMergeHistoryVo> relateHistory(PageParams params) {
        Page<SplitMergeHistory> page = new Page<>(params.getCurrent(), params.getPageSize());
        page.setOrders(Lists.newArrayList(
                OrderItem.desc("create_time")
        ));
        Page<SplitMergeHistory> splitMergeHistories = this.splitMergeHistoryService.page(page);
        List<SplitMergeHistoryVo> mergeHistoryVos = new ArrayList<>();
        for (SplitMergeHistory mergeHistory : splitMergeHistories.getRecords()) {
            SplitMergeHistoryVo splitMergeHistoryVo = new SplitMergeHistoryVo();
            splitMergeHistoryVo.setType(this.getSplitMergeType(mergeHistory.getSplitMergeState()));

            List<String> before = new ArrayList<>();
            for (String unitId : mergeHistory.getOrginalUnitIds().split(",")) {
                Unit unit = this.getById(unitId);
                before.add(unit.getUnitNo());
            }
            splitMergeHistoryVo.setBefore(before.toString());

            List<String> after = new ArrayList<>();
            for (String unitId : mergeHistory.getResultUnitIds().split(",")) {
                Unit unit = this.getById(unitId);
                after.add(unit.getUnitNo());
            }
            splitMergeHistoryVo.setAfter(after.toString());
            splitMergeHistoryVo.setUser(mergeHistory.getCreateBy());
            splitMergeHistoryVo.setTime(mergeHistory.getCreateTime());
            mergeHistoryVos.add(splitMergeHistoryVo);
        }
        return PageVo.create(params.getCurrent(), params.getPageSize(), splitMergeHistories.getTotal(), mergeHistoryVos);
    }

    private String getSplitMergeType(SplitAndMergeState splitMergeState) {
        switch (splitMergeState) {
            case split:
                return "拆分";
            case merge:
                return "合并";
            default:
                throw new BizException(BizCodeFace.createBizCode(ErrorCode.FAIL).message("数据不合法"));
        }
    }

    @Override
    public Map<String, Double> useTypeAreaSumAndRent(List<UnitVo> vos) {
        Map<String, Double> result=new HashMap<>();
        //租金 60 固定
        result.put("rent",60d);
        if (CollectionUtils.isEmpty(vos)){
            vos=new ArrayList<>();
        }
        //空闲面积计算
       Double freeArea= vos.stream().filter(unitVo -> StrUtil.isNotBlank(unitVo.getBuiltUpArea()) && unitVo.getStatus()==UnitState.free).map(u->Double.valueOf(u.getBuiltUpArea())).reduce(0d,
                NumberUtils::add);
        result.put("freeArea",freeArea);

        //通过类型分类
        Map<UnitUseType, List<UnitVo>> collect = vos.stream().collect(Collectors.groupingBy(UnitCreateVo::getUseType));
        List<UnitVo> for_selfVos = collect.get(UnitUseType.for_self);
        Double for_self=0d; //自用
        if (CollectionUtils.isNotEmpty(for_selfVos)){
            for_self= for_selfVos.stream().filter(unitVo -> StrUtil.isNotBlank(unitVo.getBuiltUpArea())).map(u->Double.valueOf(u.getBuiltUpArea())).reduce(0d,NumberUtils::add);
        }
        result.put("for_self",for_self);

        List<UnitVo> for_publicVos = collect.get(UnitUseType.for_public);
        Double for_public=0d; //公用
        if (CollectionUtils.isNotEmpty(for_publicVos)){
            for_public= for_publicVos.stream().filter(unitVo -> StrUtil.isNotBlank(unitVo.getBuiltUpArea())).map(u->Double.valueOf(u.getBuiltUpArea())).reduce(0d,NumberUtils::add);
        }
        result.put("for_public",for_public);

        List<UnitVo> for_estateVos = collect.get(UnitUseType.for_estate);
        Double for_estate=0d; //物业用房
        if (CollectionUtils.isNotEmpty(for_estateVos)){
            for_estate= for_estateVos.stream().filter(unitVo -> StrUtil.isNotBlank(unitVo.getBuiltUpArea())).map(u->Double.valueOf(u.getBuiltUpArea())).reduce(0d,NumberUtils::add);
        }
        result.put("for_estate",for_estate);

        List<UnitVo> industryVos = collect.get(UnitUseType.industry);
        Double industry=0d; //产业租赁
        if (CollectionUtils.isNotEmpty(industryVos)){
            industry= industryVos.stream().filter(unitVo -> StrUtil.isNotBlank(unitVo.getBuiltUpArea())).map(u->Double.valueOf(u.getBuiltUpArea())).reduce(0d,NumberUtils::add);
        }
        result.put("industry",industry);

        List<UnitVo> setVos = collect.get(UnitUseType.set);
        Double set=0d; //配套租赁
        if (CollectionUtils.isNotEmpty(setVos)){
            set= setVos.stream().filter(unitVo -> StrUtil.isNotBlank(unitVo.getBuiltUpArea())).map(u->Double.valueOf(u.getBuiltUpArea())).reduce(0d,NumberUtils::add);
        }
        result.put("set",set);

        return result;
    }
}
