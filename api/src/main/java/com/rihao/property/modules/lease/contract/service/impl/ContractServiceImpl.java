package com.rihao.property.modules.lease.contract.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.text.StrBuilder;
import cn.hutool.core.util.EscapeUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.anteng.boot.core.facade.code.BizCodeFace;
import com.anteng.boot.core.facade.code.ErrorCode;
import com.anteng.boot.core.facade.exception.BizException;
import com.anteng.boot.pojo.query.Filter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.rihao.property.common.BaseEntity;
import com.rihao.property.common.DateInterval;
import com.rihao.property.common.NumberToStringForChineseMoney;
import com.rihao.property.common.enums.EnableState;
import com.rihao.property.common.oss.IOssService;
import com.rihao.property.common.page.PageVo;
import com.rihao.property.common.vo.KeyValueVo;
import com.rihao.property.modules.close_history.entity.ContractCloseHistory;
import com.rihao.property.modules.close_history.service.IContractCloseHistoryService;
import com.rihao.property.modules.common.vo.UploadResultVo;
import com.rihao.property.modules.config.emuns.SysFileType;
import com.rihao.property.modules.config.entity.SysConfig;
import com.rihao.property.modules.config.entity.SysFile;
import com.rihao.property.modules.config.service.ISysConfigService;
import com.rihao.property.modules.config.service.ISysFileService;
import com.rihao.property.modules.ent.entity.Ent;
import com.rihao.property.modules.ent.entity.EntCategory;
import com.rihao.property.modules.ent.enums.NewSettleStatus;
import com.rihao.property.modules.ent.enums.SignupStatus;
import com.rihao.property.modules.ent.service.IEntCategoryService;
import com.rihao.property.modules.ent.service.IEntService;
import com.rihao.property.modules.ent.vo.EntCreateVo;
import com.rihao.property.modules.establish.entity.Establish;
import com.rihao.property.modules.establish.service.IEstablishService;
import com.rihao.property.modules.estate.convert.UnitConvert;
import com.rihao.property.modules.estate.entity.Building;
import com.rihao.property.modules.estate.entity.Park;
import com.rihao.property.modules.estate.entity.Unit;
import com.rihao.property.modules.estate.enums.UnitState;
import com.rihao.property.modules.estate.service.IBuildingService;
import com.rihao.property.modules.estate.service.IParkService;
import com.rihao.property.modules.estate.service.IUnitService;
import com.rihao.property.modules.estate.vo.BuildingKeyValueVo;
import com.rihao.property.modules.estate.vo.UnitVo;
import com.rihao.property.modules.lease.contract.controller.params.ContractQueryParam;
import com.rihao.property.modules.report.controller.params.LeaseReportContractQueryParam;
import com.rihao.property.modules.lease.contract.controller.params.SettleQueryParam;
import com.rihao.property.modules.lease.contract.convert.ContractConvert;
import com.rihao.property.modules.lease.contract.dto.BuildingBasicDTO;
import com.rihao.property.modules.lease.contract.entity.Contract;
import com.rihao.property.modules.lease.contract.entity.ContractFile;
import com.rihao.property.modules.lease.contract.enums.BizType;
import com.rihao.property.modules.lease.contract.enums.ContractFileType;
import com.rihao.property.modules.lease.contract.enums.ContractStatus;
import com.rihao.property.modules.lease.contract.enums.SignUpStatus;
import com.rihao.property.modules.lease.contract.mapper.ContractMapper;
import com.rihao.property.modules.lease.contract.service.IContractFileService;
import com.rihao.property.modules.lease.contract.service.IContractService;
import com.rihao.property.modules.lease.contract.vo.*;
import com.rihao.property.modules.lease.unit.entity.ContractUnit;
import com.rihao.property.modules.lease.unit.enums.ContractUnitState;
import com.rihao.property.modules.lease.unit.service.IContractUnitService;
import com.rihao.property.modules.report.controller.params.LedgerReportBaseQueryParam;
import com.rihao.property.modules.report.controller.params.UseHouseDetailQueryParam;
import com.rihao.property.modules.report.service.impl.ReportServiceImpl;
import com.rihao.property.modules.report.vo.UseHouseLedgerVo;
import com.rihao.property.modules.serial_number.service.ISerialNumberService;
import com.rihao.property.modules.system.entity.SysUser;
import com.rihao.property.shiro.util.JwtUtil;
import com.rihao.property.util.ValidationUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <p>
 * ???????????????
 * </p>
 *
 * @author wangyu
 * @since 2021-03-28
 */
@Service
@Slf4j
public class ContractServiceImpl extends ServiceImpl<ContractMapper, Contract> implements IContractService {

    @Autowired
    private IContractCloseHistoryService closeHistoryService;
    @Autowired
    @Lazy
    private IEntService entService;
    @Autowired
    private IBuildingService buildingService;
    @Autowired
    private IUnitService unitService;
    @Autowired
    private IContractUnitService contractUnitService;
    @Autowired
    private ISerialNumberService serialNumberService;
    @Autowired
    private ISysConfigService configService;
    @Autowired
    private IOssService ossService;
    @Autowired
    private IParkService parkService;
    @Autowired
    private IEntCategoryService entCategoryService;
    @Autowired
    private IContractFileService contractFileService;
    @Autowired
    private ISysFileService sysFileService;
    @Autowired
    private IEstablishService establishService;

    @Override
    public PageVo<ContractVo> search(ContractQueryParam contractQueryParam) {
        Page<Contract> page = new Page<>(contractQueryParam.getCurrent(), contractQueryParam.getPageSize());
//        page.setOrders(Lists.newArrayList(
//                OrderItem.desc("create_time")
//        ));
        ContractMapper.QueryParam params = new ContractMapper.QueryParam();
        params.setParkIds(JwtUtil.getCurrentUserParkIds());
        if (StringUtils.hasText(contractQueryParam.getCode())) {
            params.setCode(Filter.LikeValue.both(contractQueryParam.getCode()));
        }
        if (StringUtils.hasText(contractQueryParam.getEntName())) {
            params.setEntName(Filter.LikeValue.both(contractQueryParam.getEntName()));
        }
        if (contractQueryParam.getParkId() != null) {
            params.setParkId(contractQueryParam.getParkId());
        }
        if (contractQueryParam.getBuildingId() != null) {
            params.setBuildingId(contractQueryParam.getBuildingId());
        }
        if (StringUtils.hasText(contractQueryParam.getLeaseStartDate())) {
            params.setLeaseStartDate(contractQueryParam.getLeaseStartDate());
        }
        if (StringUtils.hasText(contractQueryParam.getDueDate())) {
            params.setDueDate(contractQueryParam.getDueDate());
        }
        if (StringUtils.hasText(contractQueryParam.getLikeParam())) {
            params.setLikeParam(contractQueryParam.getLikeParam());
        }
        if (contractQueryParam.getStatus() != null) {
            params.setStatus(contractQueryParam.getStatus());
        }

        SysUser user = JwtUtil.getCurrentUser();
        if (user.getRoleId() != 1) {
            params.setEstablishId(user.getEstablishId());
        }
        Page<ContractVo> result = this.getBaseMapper().selectByQueryParam(page, params);
        List<ContractVo> list = result.getRecords();

        List<KeyValueVo> parks = this.parkService.getPermissionList();
        Long[] parkIds = new Long[parks.size()];
        for (int index = 0; index < parks.size(); index++) {
            parkIds[index] = Long.parseLong(parks.get(index).getKey().toString());
        }
        List<BuildingKeyValueVo> buildings = this.buildingService.queryPermissionBuildingList(parkIds);

        for (ContractVo contractVo : list) {
            ContractCloseHistory history = this.closeHistoryService.getByContractId(contractVo.getId());
            if (history != null) {
                contractVo.setCloseType(history.getCloseType());
                contractVo.setCloseReason(history.getRemark());
                contractVo.setCloseTime(history.getCreateTime());
            }
            if (contractVo.getParkId() != null){
                Park park = this.parkService.getById(contractVo.getParkId());
                contractVo.setParkName(park.getName());
                contractVo.setParkUsed(park.getUsed());
                //????????????????????????????????????
                contractVo.setEstablish(this.establishService.getById(park.getEstId()).getName());
            }
            contractVo.setBuildings(buildings);
            contractVo.setParks(parks);



            List<UnitVo> unitVos = new ArrayList<>();
            List<ContractUnit> contractUnitList = this.contractUnitService.getUnitListByContractId(contractVo.getId());
            //????????????
            List<String> buildingNames=new ArrayList();
            //????????????
            List<String> unitNames=new ArrayList();
            for (ContractUnit contractUnit : contractUnitList) {
                //??????????????????
                Unit unit = this.unitService.getById(contractUnit.getUnitId());
                UnitVo unitVO = UnitConvert.INSTANCE.entity2Vo(unit);
                unitVos.add(unitVO);
                //??????????????????
                if (contractUnit.getBuildingId() !=null){
                    Building byId = this.buildingService.getById(contractUnit.getBuildingId());
                    if (byId != null){
                        buildingNames.add(byId.getName());
                    }
                }
                if (unit != null) {
                    //????????????
                    unitNames.add(unit.getUnitNo());
                }
            }
            if (!CollectionUtils.isEmpty(buildingNames)){
                contractVo.setBuildingNames(buildingNames.stream().distinct().collect(Collectors.toList()));
            }
            contractVo.setUnitNames(unitNames);
            contractVo.setUnitVos(unitVos);
        }

        return PageVo.create(contractQueryParam.getCurrent(), contractQueryParam.getPageSize(),
                result.getTotal(), list);
    }

    @Override
    public PageVo<ContractVo> searchWaitPage(ContractQueryParam contractQueryParam) {
        Page<Contract> page = new Page<>(contractQueryParam.getCurrent(), contractQueryParam.getPageSize());
        page.setOrders(Lists.newArrayList(
                OrderItem.desc("create_time")
        ));
        ContractMapper.QueryParam params = new ContractMapper.QueryParam();
        params.setParkIds(JwtUtil.getCurrentUserParkIds());
        if (StringUtils.hasText(contractQueryParam.getCode())) {
            params.setCode(Filter.LikeValue.both(contractQueryParam.getCode()));
        }
        if (StringUtils.hasText(contractQueryParam.getEntName())) {
            params.setEntName(Filter.LikeValue.both(contractQueryParam.getEntName()));
        }
        if (contractQueryParam.getParkId() != null) {
            params.setParkId(contractQueryParam.getParkId());
        }
        if (contractQueryParam.getBuildingId() != null) {
            params.setBuildingId(contractQueryParam.getBuildingId());
        }
        if (StringUtils.hasText(contractQueryParam.getLeaseStartDate())) {
            params.setLeaseStartDate(contractQueryParam.getLeaseStartDate());
        }
        if (StringUtils.hasText(contractQueryParam.getDueDate())) {
            params.setDueDate(contractQueryParam.getDueDate());
        }
        if (StringUtils.hasText(contractQueryParam.getLikeParam())) {
            params.setLikeParam(contractQueryParam.getLikeParam());
        }
        if (contractQueryParam.getStatus() != null) {
            params.setStatus(contractQueryParam.getStatus());
        }
        SysUser user = JwtUtil.getCurrentUser();
        if (user.getRoleId() != 1) {
            params.setEstablishId(user.getEstablishId());
        }
        Page<ContractVo> result = this.getBaseMapper().selectWaitByQueryParam(page, params);
        List<ContractVo> list = result.getRecords();

        List<KeyValueVo> parks = this.parkService.getPermissionList();
        Long[] parkIds = new Long[parks.size()];
        for (int index = 0; index < parks.size(); index++) {
            parkIds[index] = Long.parseLong(parks.get(index).getKey().toString());
        }
        List<BuildingKeyValueVo> buildings = this.buildingService.queryPermissionBuildingList(parkIds);

        for (ContractVo contractVo : list) {
            ContractCloseHistory history = this.closeHistoryService.getByContractId(contractVo.getId());
            if (history != null) {
                contractVo.setCloseType(history.getCloseType());
                contractVo.setCloseReason(history.getRemark());
                contractVo.setCloseTime(history.getCreateTime());
            }
            if (contractVo.getBuildingIds() != null) {
                String[] split = contractVo.getBuildingIds().split(",");
                Park park = this.parkService.getById(this.buildingService.getById(Long.parseLong(split[0])).getParkId());
                contractVo.setParkName(park.getName());
            }
            contractVo.setBuildings(buildings);
            contractVo.setParks(parks);
        }

        return PageVo.create(contractQueryParam.getCurrent(), contractQueryParam.getPageSize(),
                result.getTotal(), list);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Contract sign(ContractCreateVo createVo) {
        Boolean result2 = DateInterval.calStringDateDiff(createVo.getLeaseStartDate().toString(), createVo.getDueDate().toString());
        if (result2) {
            throw new BizException(BizCodeFace.createBizCode(ErrorCode.FAIL)
                    .message("???????????????????????????????????????????????????"));
        }

        // ????????????
        Ent ent = this.entService.getById(createVo.getEntId());
        if (ent == null) {
            throw new BizException(BizCodeFace.createBizCode(ErrorCode.FAIL).message("?????????????????????????????????"));
        }

        Contract entity = ContractConvert.INSTANCE.createParam2Entity(createVo);
        String leaseStartDate = createVo.getLeaseStartDate().toString();
        for (Long unitId : createVo.getUnitIds()) {
            List<ContractUnit> contractUnitList = this.contractUnitService.getByUnitId(unitId);
            if (contractUnitList.size() != 0) {
                entity.setStatus(ContractStatus.wait_pre);
                for (ContractUnit contractUnit : contractUnitList) {
                    //????????????????????????????????????
                    String durDate = contractUnit.getDurdate();
                    Boolean result = DateInterval.calStringDateDiff(leaseStartDate, durDate);
                    if (!result) {
                        Unit unit = this.unitService.getById(contractUnit.getUnitId());
                        throw new BizException(BizCodeFace.createBizCode(ErrorCode.FAIL)
                                .message("??????" + unit.getUnitNo() + "????????????????????????????????????????????????????????????????????????"));
                    }
                }
            }
        }

        // ??????????????????
        //String code = this.serialNumberService.getContractSerialNumber();
        //entity.setCode(code);
        entity.setEstablishId(JwtUtil.getCurrentUser().getEstablishId());
        entity.setEntId(ent.getId());
        entity.setEntName(ent.getName());

        entity.setParkId(createVo.getParkId());

        boolean result = this.save(entity);
        // ??????????????????
        //this.serialNumberService.addContractNo(entity.getCode());

        if (result) {
            List<String> unitString = new ArrayList<>();

            // ??????????????????????????????????????????????????????
            for (Long unitId : createVo.getUnitIds()) {
                Unit unit = this.unitService.getById(unitId);
                unitString.add(unit.getUnitNo());
                unit.setStatus(UnitState.busy);
                this.unitService.updateById(unit);

                entity.setUnit(String.join(",", unitString));

                ContractUnit contractUnit = new ContractUnit();
                contractUnit.setUnitId(unit.getId());
                contractUnit.setContractId(entity.getId());
                contractUnit.setStatus(ContractUnitState.normal);
                if (entity.getStatus().equals(ContractStatus.wait_pre)) {
                    contractUnit.setStatus(ContractUnitState.wait_pre);
                }
                entity.setStatus(ContractStatus.wait_contract);
                contractUnit.setDurdate(entity.getDueDate());
                this.contractUnitService.save(contractUnit);
            }

            this.updateById(entity);
        }
        return entity;
    }

    private void validContractNoUnique(String code) {
        QueryWrapper<Contract> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Contract::getCode, code);
        Contract entity = this.getOne(wrapper);
        ValidationUtil.notNull(entity, "??????", "??????", code);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean close(CloseParams closeParams) {
        // ????????????????????????
        Long dateNow = Long.parseLong(DateInterval.getSimpleDate(new Date()).replace("-", ""));
        Long estimate = Long.parseLong(closeParams.getEstimateCloseDate().replace("-", ""));
        Long diff = estimate - dateNow;
        //if (diff < 0) {
        //    throw new BizException(BizCodeFace.createBizCode(ErrorCode.FAIL).message("????????????????????????????????????"));
        //}
        Contract contract = this.getById(closeParams.getId());

        //ContractCloseHistory contractCloseHistory = this.closeHistoryService.getByContractId(closeParams.getId());
        //if (contractCloseHistory == null)
        //    throw new BizException(BizCodeFace.createBizCode(ErrorCode.FAIL).message("????????????OA??????"));
        ContractCloseHistory contractCloseHistory = new ContractCloseHistory();
        contractCloseHistory.setContractId(closeParams.getId());
        contractCloseHistory.setCloseType(closeParams.getType());
        contractCloseHistory.setRemark(closeParams.getReason());
        contractCloseHistory.setEstimateCloseDate(closeParams.getEstimateCloseDate());
        if (diff <= 0) {
            contractCloseHistory.setCloseResult(true);
            contract.setStatus(ContractStatus.stop);

            List<ContractUnit> contractUnits = this.contractUnitService.getUnitListByContractId(contract.getId());
            for (ContractUnit contractUnit : contractUnits) {
                Unit unit = this.unitService.getById(contractUnit.getUnitId());
                unit.setStatus(UnitState.free);
                this.unitService.updateById(unit);

                contractUnit.setStatus(ContractUnitState.close);
                this.contractUnitService.updateById(contractUnit);
            }
        }
        this.closeHistoryService.updateById(contractCloseHistory);

        contract.setEstimateClose(true);
        contract.setEstimateCloseDate(closeParams.getEstimateCloseDate());

        Boolean result = this.updateById(contract);
        // this.planClose();
        //????????????
        if (!CollectionUtils.isEmpty(closeParams.getFiles())){
            List<Long> collect = closeParams.getFiles().stream().map(SysFile::getId).collect(Collectors.toList());
            sysFileService.updateFileForBusId(collect,contract.getId(), SysFileType.CONTRACTCLOSEFILE.getValue());
        }
        return result;
    }


    @Override
    public ContractDetailVo detail(Long id) {
        Contract contract = this.getById(id);
        if (contract == null) {
            return null;
        }
        ContractDetailVo vo = new ContractDetailVo();
        //????????????
        Long entId = contract.getEntId();
        if (entId != null) {
            Ent ent = this.entService.getById(entId);
            if (ent != null) {
                BeanUtils.copyProperties(ent, vo);
                vo.setEntId(ent.getId());
                vo.setName(ent.getName());
                vo.setContact(contract.getContact());
                vo.setContactPhoneNumber(contract.getContactPhoneNumber());
                //????????????
                String categoryIds = ent.getCategoryIds();
                if (StringUtils.hasText(categoryIds)) {
                    String[] ids = org.apache.commons.lang3.StringUtils.split(categoryIds, ",");
                    List<EntCategory> entCategories = this.entCategoryService.listByIds(
                            Stream.of(ids).mapToLong(Long::parseLong).boxed().collect(Collectors.toList())
                    );
                    if (!CollectionUtils.isEmpty(entCategories)) {
                        vo.setCategorys(
                                entCategories.stream().map(item -> new KeyValueVo()
                                        .setKey(item.getId()).setValue(item.getName())
                                ).toArray(KeyValueVo[]::new)
                        );
                    }
                }
                //??????
                vo.setPartners(this.entService.getPartnerByEntId(entId));
            }
        }

        //??????????????????????????????
        vo.setEstimateCloseDate(contract.getEstimateCloseDate());
        vo.setSignDate(contract.getSignDate());
        vo.setLeaseStartDate(contract.getLeaseStartDate());
        vo.setDueDate(contract.getDueDate());
        vo.setLeaseTerm(contract.getLeaseTerm());
        vo.setPrice(contract.getPrice());
        vo.setArea(contract.getArea());
        vo.setRent(contract.getRent());
        vo.setRentFreePeriod(contract.getRentFreePeriod());
        vo.setSignUpStatus(contract.getSignUpStatus());
        vo.setCreateBy(contract.getCreateBy());
        vo.setCreateTime(contract.getCreateTime());
        vo.setCode(contract.getCode());
        vo.setId(contract.getId());
        vo.setParkId(contract.getParkId());
        if (contract.getStatus().equals(ContractStatus.running)) {
            vo.setContractStatus("?????????");
        }
        if (contract.getStatus().equals(ContractStatus.stop)) {
            vo.setContractStatus("?????????");
        }
        //????????????
        if (contract.getBuildingIds() != null) {
            String[] split = contract.getBuildingIds().split(",");
            BuildingBasicDTO buildingBasic = this.buildingService.selectBasic(Long.parseLong(split[0]));
            if (buildingBasic != null) {
                vo.setParkId(buildingBasic.getParkId());
                vo.setParkName(buildingBasic.getParkName());
            }
        }
        //????????????
        List<ContractDetailVo.Unit> contractUnits = this.contractUnitService.getUnitsByContractId(contract.getId());
        vo.setUnits(contractUnits);
        List<Long> unitIds = new ArrayList<>();
        for (ContractDetailVo.Unit contractUnit : contractUnits) {
            //?????????????????????
            contractUnit.setBuildingName(buildingService.getBuildingNameById(contractUnit.getBuildingId()));
            unitIds.add(contractUnit.getId());
        }
        vo.setUnitIds(unitIds);
        StringBuilder unitName = new StringBuilder();
        StringBuilder bulidNames = new StringBuilder();
        for (ContractDetailVo.Unit contractUnit : contractUnits) {
            unitName.append(contractUnit.getUnitNo()).append(",");
            bulidNames.append(contractUnit.getBuildingName()).append(",");
        }
        if (unitName.length() > 0) {
            vo.setUnitName(unitName.substring(0, unitName.length() - 1));
            vo.setBuildingNames(bulidNames.substring(0, bulidNames.length() - 1));
        } else{
            vo.setUnitName("");
        }
        //????????????
        List<ContractFile> contractFiles = this.contractFileService.list(new QueryWrapper<ContractFile>().lambda()
                .eq(ContractFile::getContractId, contract.getId()));
        if (!CollectionUtils.isEmpty(contractFiles)) {
            Map<ContractFileType, List<ContractFile>> map
                    = contractFiles.stream().collect(Collectors.groupingBy(ContractFile::getFileType));
            if (map.containsKey(ContractFileType.APPROVE)) {
                vo.setApproveFiles(map.get(ContractFileType.APPROVE).stream().map(item -> {
                    SettleContractFilesVo.ContractFile file = new SettleContractFilesVo.ContractFile();
                    file.setName(item.getName());
                    file.setFilePath(item.getFilePath());
                    file.setUploader(item.getCreateBy());
                    file.setCreateBy(item.getCreateBy());
                    file.setCreateTime(item.getCreateTime());
                    SysFile byId = sysFileService.getById(item.getFileId());
                    file.setFile(byId);
                    return file;
                }).collect(Collectors.toList()));
            }
            if (map.containsKey(ContractFileType.CONTRACT)) {
                vo.setContractFiles(map.get(ContractFileType.CONTRACT).stream().map(item -> {
                    SettleContractFilesVo.ContractFile file = new SettleContractFilesVo.ContractFile();
                    file.setName(item.getName());
                    file.setCreateBy(item.getCreateBy());
                    file.setCreateTime(item.getCreateTime());
                    file.setFilePath(item.getFilePath());
                    file.setUploader(item.getUploader());
                    SysFile byId = sysFileService.getById(item.getFileId());
                    file.setFile(byId);
                    return file;
                }).collect(Collectors.toList()));
            }
        }

        return vo;
        /*ContractVo vo = ContractConvert.INSTANCE.entity2Vo(this.getById(id));
        QueryWrapper<ContractUnit> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(ContractUnit::getContractId, id);
        this.getContractUnitListById(vo, wrapper);
        // TODO ??????????????????????????????
        vo.setHistoryYears("0");

        ContractCloseHistoryVo closeHistoryVo = ContractCloseHistoryConvert.INSTANCE.entity2Vo(this.closeHistoryService.getByContractId(id));
        vo.setContractCloseHistory(closeHistoryVo);
        return vo;*/
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean change(ContractVo createVo) throws Exception {
        // ??????????????????
        Contract contract = this.getById(createVo.getId());
        contract.setStatus(ContractStatus.stop);
        this.updateById(contract);

        // ??????????????????
        List<ContractUnit> contractUnits = this.contractUnitService.getUnitListByContractId(contract.getId());
        for (ContractUnit contractUnit : contractUnits) {
            Unit unit = this.unitService.getById(contractUnit.getUnitId());
            unit.setStatus(UnitState.free);
            this.unitService.updateById(unit);
        }

        createVo.setEntId(createVo.getEntId());

        // ???????????????
        this.sign(createVo);
        return true;
    }

    @Override
    public List<Contract> getAvailableList() {
        QueryWrapper<Contract> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Contract::getStatus, 1);
        return this.list(wrapper);
    }

    @Override
    public XWPFDocument writeContract(HttpServletResponse response, Long contractId, SysUser user) throws IOException, ParseException {
        if (user.getEstablishId() == null) {
            throw new BizException(BizCodeFace.createBizCode(ErrorCode.FAIL).message("????????????????????????????????????????????????"));
        }
        SysConfig config = this.configService.getConfig(user.getEstablishId());
        if (config == null) {
            Establish establish = establishService.getById(user.getEstablishId());
            throw new BizException(BizCodeFace.createBizCode(ErrorCode.FAIL).message(establish.getName() + "??????????????????????????????????????????????????????????????????????????????"));
        }
        Contract contract = this.getById(contractId);
        Park park = this.parkService.getById(contract.getParkId());
        InputStream is=null;
        List<SysFile> sysFileList = sysFileService.getFilesByBusIdAndType(park.getId(), SysFileType.CONTRACTTEMPFILE.getValue());
        if (org.apache.commons.collections.CollectionUtils.isNotEmpty(sysFileList)) {
            SysFile sysFile = sysFileList.get(0);
            if (sysFile == null) {
                throw new BizException(BizCodeFace.createBizCode(ErrorCode.FAIL).message("?????????????????????????????????"));
            }
            if (!sysFile.getFileName().endsWith(".doc") && !sysFile.getFileName().endsWith(".docx")) {
                throw new BizException(BizCodeFace.createBizCode(ErrorCode.FAIL).message("??????????????????????????????????????????doc????????????docx?????????"));
            }
            is =  new FileInputStream(sysFile.getFilePath());
        }

        Ent ent = this.entService.getById(contract.getEntId());
        Map<String, String> map = new HashMap<>();
        map.put("${lessor}", config.getLessor());
        map.put("${lessor-address}", config.getLessorAddress());
        map.put("${lessee}", ent.getName());
        map.put("${license-code}", ent.getUnifiedSocialCreditCode());
        map.put("${region}", config.getRegion());
        map.put("${road}", config.getRoad());
        map.put("${road-detail}", config.getRoadDetail());
        map.put("${unit}", contract.getUnit());
        map.put("${area}", contract.getArea());
        map.put("${right-holder}", config.getPlaceHolder());
        map.put("${price}", contract.getPrice() + "");
        String upperPrice = NumberToStringForChineseMoney.getChineseMoneyStringForBigDecimal(contract.getPrice());
        map.put("${upper-price}", upperPrice);
        map.put("${rent}", contract.getRent());
        String upperRent = NumberToStringForChineseMoney.getChineseMoneyStringForBigDecimal(BigDecimal.valueOf(Double.parseDouble(contract.getRent())));
        map.put("${upper-rent}", upperRent);
        String startDate = contract.getLeaseStartDate();
        map.put("${start-year}", startDate.substring(0, 4));
        map.put("${start-month}", startDate.substring(5, 7));
        map.put("${start-day}", startDate.substring(8, 10));
        map.put("${payment-date}", config.getPaymentDate());

        SimpleDateFormat sfd = new SimpleDateFormat("yyyy-MM-dd");

        Date date2 = sfd.parse(contract.getLeaseStartDate());
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(date2);
//        if (StringUtils.hasText(contract.getRentFreePeriod())) {
//            Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*(\\.\\d{1})?$");
//            boolean result = pattern.matcher(contract.getRentFreePeriod()).matches();
//            if (result) {
//                calendar2.add(Calendar.MONTH, +Integer.parseInt(contract.getRentFreePeriod()));
//            } else {
//                throw new BizException(BizCodeFace.createBizCode(ErrorCode.FAIL).message("??????????????????????????????????????????????????????????????????????????????"));
//            }
//
//        }
        String rentDate = (sfd.format(calendar2.getTime()));
        map.put("${rent-start-year}", rentDate.substring(0, 4));
        map.put("${rent-start-month}", rentDate.substring(5, 7));
        map.put("${rent-start-day}", rentDate.substring(8, 10));

        String dueDate = contract.getDueDate();

        map.put("${end-year}", dueDate.substring(0, 4));
        map.put("${end-month}", dueDate.substring(5, 7));
        map.put("${end-day}", dueDate.substring(8, 10));

        map.put("${deposit-month}", contract.getDepositMonth());
        contract.setDeposit(BigDecimal.valueOf(Double.parseDouble(contract.getRent()) * 2).toString());
        map.put("${deposit}", contract.getDeposit());
        String upperDeposit = NumberToStringForChineseMoney.getChineseMoneyStringForBigDecimal(BigDecimal.valueOf(Double.parseDouble(contract.getDeposit())));
        map.put("${upper-deposit}", upperDeposit);
        try {
            map.put("${loss-cost}", new BigDecimal(contract.getRent()).multiply(new BigDecimal(config.getArrearsMonth())).toString());
        } catch (Exception exception) {
        }
        map.put("${a-delivery-address}", config.getDeliveryAddress());
        map.put("${arrears-month}", config.getArrearsMonth());
        map.put("${arrears-day}", config.getArrearsDay());
        //??????????????????
        map.put("${park-name}", park.getName());
        //?????????????????????????????????????????????
        List<ContractDetailVo.Unit> contractVos = this.contractUnitService.getUnitsByContractId(contractId);
        if (!CollectionUtils.isEmpty(contractVos)){
            Map<Long, List<ContractDetailVo.Unit>> collect = contractVos.stream().collect(Collectors.groupingBy(item -> item.getBuildingId()));
            //??????????????????
            StrBuilder strBuilder=new StrBuilder();
            Iterator<Long> iterator = collect.keySet().iterator();
            while (iterator.hasNext()){
                //????????????
                Long buildId = iterator.next();
                Building building = this.buildingService.getById(buildId);
                strBuilder.append(building.getName());
                List<ContractDetailVo.Unit> units = collect.get(buildId);
                if (!CollectionUtils.isEmpty(units)){
                    units.forEach(item->{
                        strBuilder.append(item.getUnitNo()).append("???");
                    });
                }
            }

            String s = strBuilder.toString();
            if (StringUtils.hasText(s)){
                //?????????????????? //????????????????????????
                map.put("${build-units}", s.substring(0,s.length()-1));
            }

        }


        if (is != null) {
            XWPFDocument document = new XWPFDocument(is);
            Iterator<XWPFParagraph> itPara = document.getParagraphsIterator();
            while (itPara.hasNext()) {
                XWPFParagraph paragraph = itPara.next();
                List<XWPFRun> runs = paragraph.getRuns();
                for (XWPFRun run : runs) {
                    String oneparaString = run.getText(run.getTextPosition());
                    if (StringUtils.isEmpty(oneparaString)) {
                        continue;
                    }
                    for (Map.Entry<String, String> entry :
                            map.entrySet()) {
                        oneparaString = oneparaString.replace(entry.getKey(), entry.getValue() == null ? "" : entry.getValue());
                    }
                    run.setText(oneparaString, 0);
                }
            }
            return document;
        }
        throw new BizException(BizCodeFace.createBizCode(ErrorCode.FAIL).message("??????????????????????????????????????????"));
    }

    @Override
    public PageVo<ContractVo> getExpirationContractPage(ContractQueryParam contractQueryParam) {
        Page<Contract> page = new Page<>(contractQueryParam.getCurrent(), contractQueryParam.getPageSize());
        ContractMapper.QueryParam params = new ContractMapper.QueryParam();
        params.setParkIds(JwtUtil.getCurrentUserParkIds());
        params.setExpirationMonth(contractQueryParam.getExpirationMonth());
        params.setLikeParam(contractQueryParam.getLikeParam());
        params.setParkId(contractQueryParam.getParkId());
        Page<ContractVo> result = this.getBaseMapper().selectExpiration(page, params);

        for (ContractVo contractVo : result.getRecords()) {
            ContractCloseHistory history = this.closeHistoryService.getByContractId(contractVo.getId());
            if (history != null) {
                contractVo.setCloseType(history.getCloseType());
                contractVo.setCloseReason(history.getRemark());
                contractVo.setCloseTime(history.getCreateTime());
            }
            if (contractVo.getParkId() != null){
                Park park = this.parkService.getById(contractVo.getParkId());
                contractVo.setParkName(park.getName());
                contractVo.setParkUsed(park.getUsed());
                //????????????????????????????????????
                contractVo.setEstablish(this.establishService.getById(park.getEstId()).getName());
            }


            List<UnitVo> unitVos = new ArrayList<>();
            List<ContractUnit> contractUnitList = this.contractUnitService.getUnitListByContractId(contractVo.getId());
            //????????????
            List<String> buildingNames=new ArrayList();
            //????????????
            List<String> unitNames=new ArrayList();
            //????????????
            List<BuildingKeyValueVo> buildingList=new ArrayList();
            for (ContractUnit contractUnit : contractUnitList) {
                //??????????????????
                Unit unit = this.unitService.getById(contractUnit.getUnitId());
                UnitVo unitVO = UnitConvert.INSTANCE.entity2Vo(unit);
                unitVos.add(unitVO);
                //??????????????????
                if (contractUnit.getBuildingId() !=null){
                    Building byId = this.buildingService.getById(contractUnit.getBuildingId());
                    if (byId != null){
                        if (!buildingList.stream().anyMatch(a->a.getId().equals(byId.getId()))){
                            BuildingKeyValueVo buildingKeyValueVo=new BuildingKeyValueVo();
                            buildingKeyValueVo.setId(byId.getId());
                            buildingKeyValueVo.setName(byId.getName());
                            buildingList.add(buildingKeyValueVo);
                        }
                        buildingNames.add(byId.getName());
                    }
                }
                if (unit != null) {
                    //????????????
                    unitNames.add(unit.getUnitNo());
                }
            }
            if (!CollectionUtils.isEmpty(buildingNames)){
                contractVo.setBuildingNames(buildingNames.stream().distinct().collect(Collectors.toList()));
            }
            contractVo.setUnitNames(unitNames);
            contractVo.setUnitVos(unitVos);
            contractVo.setBuildings(buildingList);

        }

        return PageVo.create(contractQueryParam.getCurrent(), contractQueryParam.getPageSize(),
                result.getTotal(), result.getRecords());
    }

    @Override
    public PageVo<ContractVo> history_page(ContractQueryParam contractQueryParam) {
        Page<Contract> page = new Page<>(contractQueryParam.getCurrent(), contractQueryParam.getPageSize());
        page.setOrders(Lists.newArrayList(
                OrderItem.desc("create_time")
        ));
        QueryWrapper<Contract> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Contract::getEntId, contractQueryParam.getEntId());
        Page<Contract> result = this.page(page, wrapper);
        List<Contract> contracts = result.getRecords();
        List<ContractVo> vos = new ArrayList<>();
        for (Contract contract : contracts) {
            ContractVo vo = ContractConvert.INSTANCE.entity2Vo(contract);
            Ent ent = this.entService.getById(vo.getEntId());
            vo.setEntName(ent.getName());

            vos.add(vo);
        }

        return PageVo.create(contractQueryParam.getCurrent(), contractQueryParam.getPageSize(),
                result.getTotal(), vos);
    }

    @Override
    public PageVo<ContractVo> getByBuildingId(Integer current, Integer pageSize, Long buildingId) {
        Page<Contract> page = new Page<>(current, pageSize);
        ContractMapper.QueryParam params = new ContractMapper.QueryParam();
        if (buildingId != null)
            params.setBuildingId(buildingId);
        Page<ContractVo> result = this.getBaseMapper().selectByQueryParam(page, params);
        return PageVo.create(current, pageSize, result.getTotal(), result.getRecords());
    }

    @Override
    public List<Contract> getByBuildingId(Long buildingId) {
        QueryWrapper<Contract> wrapper = new QueryWrapper<>();
        wrapper.lambda().like(Contract::getBuildingIds, buildingId);
        return this.list(wrapper);
    }

    @Override
    public Page<ContractUnit> getContractUnitByBuildingId(LedgerReportBaseQueryParam ledgerReportQueryParam) {
        Page<ContractUnit> page = new Page<>(ledgerReportQueryParam.getCurrent(), ledgerReportQueryParam.getPageSize());
        QueryWrapper<ContractUnit> wrapper = new QueryWrapper<>();
        List<Long> contractIds = new ArrayList<>();
        if (ledgerReportQueryParam.getBuildingId() != null) {
            List<Contract> contracts = this.getByBuildingId(ledgerReportQueryParam.getBuildingId());
            for (Contract contract : contracts) {
                contractIds.add(contract.getId());
            }
            wrapper.lambda().in(ContractUnit::getContractId, contractIds);
        }
        return this.contractUnitService.page(page, wrapper);
    }

    @Override
    public PageVo<Contract> getByEntId(ContractQueryParam contractQueryParam) {
        Page<Contract> page = new Page<>(contractQueryParam.getCurrent(), contractQueryParam.getPageSize());
        QueryWrapper<Contract> wrapper = new QueryWrapper<>();
        wrapper.lambda()
                .eq(Contract::getEntId, Long.parseLong(contractQueryParam.getEntId()))
                .eq(Contract::getStatus, EnableState.enable);
        Page<Contract> result = this.page(page, wrapper);
        return PageVo.create(contractQueryParam.getCurrent(), contractQueryParam.getPageSize(),
                result.getTotal(), result.getRecords());
    }

    @Override
    public Contract getByCode(String code) {
        QueryWrapper<Contract> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Contract::getCode, code);
        return this.getOne(wrapper);
    }

    @Override
    public PageVo<ContractVo> getByEntName(ContractQueryParam contractQueryParam) {
        contractQueryParam.setCurrent(0);
        contractQueryParam.setPageSize(10);
        contractQueryParam.setParkIds(JwtUtil.getCurrentUserParkIds());
        //??????
        Ent ent = this.entService.getByName(contractQueryParam.getEntName());
        if (ent == null)
            throw new BizException(BizCodeFace.createBizCode(ErrorCode.FAIL).message("?????????????????????????????????"));
        if (ent.getNewSettle().equals(NewSettleStatus.yes))
            throw new BizException(BizCodeFace.createBizCode(ErrorCode.FAIL).message("??????????????????????????????????????????"));
        Page<Contract> page = new Page<>(contractQueryParam.getCurrent(), contractQueryParam.getPageSize());
        QueryWrapper<Contract> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Contract::getEntId, ent.getId())
                .eq(Contract::getSignUpStatus, 5);
        //?????????????????????????????????????????????????????????????????????
        if (contractQueryParam.getStatus() != null){
            wrapper.lambda().eq(Contract::getStatus,ContractStatus.running);
        }
        Page<Contract> result = this.page(page, wrapper);
        List<ContractVo> contractVoList = ContractConvert.INSTANCE.entity2ListItemBatch(result.getRecords());
        //??????????????????????????????
        contractVoList = contractVoList.stream().filter(e-> !StringUtils.isEmpty(e.getParkId())).collect(Collectors.toList());
        //??????????????????????????????,?????????????????????????????????????????????????????????????????????
        if (org.apache.commons.lang3.StringUtils.isNotBlank(contractQueryParam.getParkIds())
                && JwtUtil.getCurrentUser().getRoleId() != 1) {
            //???????????????????????????????????????
            String[] parkIdsList = contractQueryParam.getParkIds().split(",");
            if (org.apache.commons.collections.CollectionUtils.isNotEmpty(contractVoList)) {
                    contractVoList = contractVoList.stream().filter(item -> ArrayUtils.contains(parkIdsList, String.valueOf(item.getParkId()))).collect(Collectors.toList());
                }
        }
        if (contractVoList.size() == 0) {
            throw new BizException(BizCodeFace.createBizCode(ErrorCode.FAIL).message("????????????????????????????????????????????????"));
        }
        for (ContractVo contractVo : contractVoList) {
            ContractCloseHistory history = this.closeHistoryService.getByContractId(contractVo.getId());
            if (history != null) {
                contractVo.setCloseType(history.getCloseType());
                contractVo.setCloseReason(history.getRemark());
            }
            Park park = this.parkService.getById(contractVo.getParkId());
            contractVo.setParkName(park.getName());
            contractVo.setEntName(ent.getName());
            List<String> unitNames = new ArrayList();
            Set<String> buildNames = new HashSet<>();
            List<ContractUnit> contractUnits = this.contractUnitService.getUnitListByContractId(contractVo.getId());
            if (org.apache.commons.collections.CollectionUtils.isNotEmpty(contractUnits)) {
                for (ContractUnit contractUnit : contractUnits) {
                    Unit unit = this.unitService.getById(contractUnit.getUnitId());
                    unitNames.add(unit.getUnitNo());
                    if (contractUnit.getBuildingId() != null){
                        Building building = this.buildingService.getById(contractUnit.getBuildingId());
                        if (building !=null){
                            buildNames.add(building.getName());
                        }
                    }
                }
                contractVo.setUnitNames(unitNames);
                contractVo.setBuildingNames(Arrays.asList(buildNames.toArray()));
            }
            //??????????????????????????????????????????????????????
            if (!org.apache.commons.lang.StringUtils.equals(contractVo.getStatus(), "stop") &&  org.apache.commons.lang3.StringUtils.isBlank(contractVo.getEstimateCloseDate())) {
                contractVo.setAdjust(true);
            }
        }
        //?????????????????????????????????
        Optional<ContractVo> stopContract = contractVoList.stream()
                .filter(item -> org.apache.commons.lang.StringUtils.equals(item.getStatus(), "stop")
                        && org.apache.commons.lang3.StringUtils.isBlank(item.getEstimateCloseDate()))
                .sorted(Comparator.comparing(contractVo -> DateUtil.parse(contractVo.getDueDate()), Comparator.reverseOrder()
                )).findFirst();
        if (stopContract.isPresent()){
            stopContract.get().setAdjust(true);
        }

        return PageVo.create(contractQueryParam.getCurrent(), contractQueryParam.getPageSize(),
                result.getTotal(), contractVoList);
    }

    @Override
    public List<ContractVo> getByListEntId(Long entId) {
        QueryWrapper<Contract> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Contract::getEntId, entId);
        List<ContractVo> contractVos = ContractConvert.INSTANCE.entity2ListItemBatch(this.list(wrapper));
        for (ContractVo contractVo : contractVos) {
            List<Unit> units = new ArrayList();
            List<Building> builds = new ArrayList<>();
            List<ContractUnit> contractUnits = this.contractUnitService.getUnitListByContractId(contractVo.getId());
            if(!CollectionUtils.isEmpty(contractUnits)){
                for (ContractUnit contractUnit : contractUnits) {
                    Unit unit = this.unitService.getById(contractUnit.getUnitId());
                    units.add(unit);
                    if (contractUnit.getBuildingId() != null){
                        Building building = this.buildingService.getById(contractUnit.getBuildingId());
                        builds.add(building);
                    }
                }
                contractVo.setUnits(units);
                contractVo.setBuilds(builds.stream().distinct().collect(Collectors.toList()));
                contractVo.setBuildingNames(builds.stream().map(Building::getName).collect(Collectors.toList()));
            }

            Park park = this.parkService.getById(contractVo.getParkId());
            if(park != null){
                contractVo.setParkName(park.getName());
            }
        }
        return contractVos;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean uploadOa(MultipartFile file, Long id) throws IOException {
        Boolean result = false;
        Contract contract = this.getById(id);
        Ent ent = this.entService.getById(contract.getEntId());
        ContractCloseHistory history = this.closeHistoryService.getByContractId(id);
        if (history == null) {
            history = new ContractCloseHistory();
            history.setContractId(id);
            UploadResultVo uploadResultVo = this.ossService.uploadFile(ent.getName() + "-????????????OA??????", file);
            history.setOaFileUrl(uploadResultVo.getUrl());
            result = this.closeHistoryService.save(history);
        } else {
            UploadResultVo uploadResultVo = this.ossService.uploadFile(ent.getName() + "-????????????OA??????", file);
            history.setOaFileUrl(uploadResultVo.getUrl());
            result = this.closeHistoryService.updateById(history);
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean uploadContract(MultipartFile file, Long id) throws IOException {
        Contract contract = this.getById(id);
        UploadResultVo uploadResultVo = this.ossService.uploadFile(contract.getCode() + "-??????", file);
        contract.setContractFilePath(uploadResultVo.getUrl());
        contract.setStatus(ContractStatus.running);
        contract.setSignUpStatus(SignUpStatus.OK);
        List<ContractUnit> contractUnits = this.contractUnitService.getUnitListByContractId(contract.getId());
        for (ContractUnit contractUnit : contractUnits) {
            if (contractUnit.getStatus().equals(ContractUnitState.wait_pre)) {
                contract.setStatus(ContractStatus.wait_pre);
            }
        }
        Ent ent = this.entService.getById(contract.getEntId());
        ent.setNewSettle(NewSettleStatus.no);
        this.entService.updateById(ent);

        return this.updateById(contract);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Contract settle(SettleVo settleVo) {
        // ????????????
        EntCreateVo entCreateVo = new EntCreateVo();
        BeanUtils.copyProperties(settleVo, entCreateVo);
        entCreateVo.setUnifiedSocialCreditCode(entCreateVo.getUnifiedSocialCreditCode().toUpperCase(Locale.ROOT));
        this.entService.createNew(entCreateVo);
        Ent ent = this.entService.getByName(settleVo.getName());

        // ????????????
        ContractCreateVo contractCreateVo = new ContractCreateVo();
        BeanUtils.copyProperties(settleVo, contractCreateVo);
        contractCreateVo.setEntId(ent.getId());
        return this.sign(contractCreateVo);
    }

    @Override
    public Contract settleInBaseInfo(SettleInBaseInfoVo settleVo) {
        if (settleVo.getId() == null || settleVo.getId() == 0L) {
            return createNewSettleContract(settleVo);
        }
        Contract contract = this.getById(settleVo.getId());
        if (contract == null) { //????????????????????????????????????????????????
            return createNewSettleContract(settleVo);
        }
        Long entId = contract.getEntId();
        if (entId != null) {
            //???????????????????????????
            this.entService.clearEntById(entId);
        }
        //??????????????????????????????
        Ent newSettleEnt = this.createNewSettleEnt(settleVo);
        //??????????????????
        newSettleEnt.setName(newSettleEnt.getName().trim());
        this.update(new UpdateWrapper<Contract>().lambda()
                .set(Contract::getEntId, newSettleEnt.getId())
                .set(Contract::getSignUpStatus, settleVo.isSubmit() ? SignUpStatus.CONTRACT_UPLOAD : SignUpStatus.BASIC_INFO)
                .eq(Contract::getId, contract.getId()));
        contract.setEntId(newSettleEnt.getId());
        contract.setSignUpStatus(settleVo.isSubmit() ? SignUpStatus.CONTRACT_UPLOAD : SignUpStatus.BASIC_INFO);
        return contract;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Contract settleInBaseInfoAndContract(SettleInBaseInfoVo settleVo) {
        //???????????? ????????????
        Contract contract = this.settleInBaseInfo(settleVo);
        // ????????????

        ContractCreateVo contractCreateVo = new ContractCreateVo();
        BeanUtils.copyProperties(settleVo, contractCreateVo);
        contractCreateVo.setEntId(contract.getEntId());
        //??????????????????????????????
        return this.addSettleContractInfo(contractCreateVo, contract);
    }

    @Override
    public boolean cancelSettle(Long id) {
        Contract contract = this.getById(id);
        // ????????????
        QueryWrapper<Contract> wrapper = new QueryWrapper<>();
        wrapper.lambda()
                .eq(Contract::getEntId, contract.getEntId());
        List<Contract> contractList = this.list(wrapper);
        if (contractList.size() == 1)
            this.entService.clearEntById(contract.getEntId());
        // ??????????????????
        List<ContractUnit> contractUnits = this.contractUnitService.getUnitListByContractId(id);
        if (!CollectionUtils.isEmpty(contractUnits)) {
            this.contractUnitService.remove(new UpdateWrapper<ContractUnit>().lambda().in(BaseEntity::getId,
                    contractUnits.stream().map(BaseEntity::getId).collect(Collectors.toList())));
            this.unitService.update(new UpdateWrapper<Unit>().lambda()
                    .set(Unit::getStatus, UnitState.free)
                    .in(BaseEntity::getId, contractUnits.stream().map(ContractUnit::getUnitId).collect(Collectors.toSet()))
            );
        }
        // ????????????
        return this.removeById(id);
    }

    @Override
    public PageVo<ContractSettleVo> settleHistory(SettleQueryParam contractQueryParam) {
        Page<Contract> page = new Page<>(contractQueryParam.getCurrent(), contractQueryParam.getPageSize());
        page.setOrders(Lists.newArrayList(
                OrderItem.desc("c.create_time")
        ));
        contractQueryParam.setParkIds(JwtUtil.getCurrentUserParkIds());
        Page<ContractSettleVo> pageResult = this.getBaseMapper().pageContract(page, contractQueryParam);
        List<ContractSettleVo> list = pageResult.getRecords();
        for (ContractSettleVo contractSettleVo : list) {
            List<ContractUnit> contractUnits = this.contractUnitService.getUnitListByContractId(contractSettleVo.getId());
            List<Unit> units = new ArrayList<>();
            Set<Long> buildingSet = new HashSet<>();
            //???????????????????????????
            for (ContractUnit contractUnit : contractUnits) {
                Unit unit = this.unitService.getById(contractUnit.getUnitId());
                units.add(unit);
                buildingSet.add(unit.getBuildingId());
            }
            contractSettleVo.setUnitList(units);
            if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(buildingSet)){
                contractSettleVo.setBuildingList(this.buildingService.getBaseMapper().selectBatchIds(buildingSet));
            }
            contractSettleVo.setEntId(contractSettleVo.getEntId());
        }

        return PageVo.create(contractQueryParam.getCurrent(),
                contractQueryParam.getPageSize(), pageResult.getTotal(), list);
    }

    //??????????????????--????????????????????????
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Contract settleInContract(SettleContractVo contractVo) {
        //??????id
        Long id = contractVo.getId();
        Contract contract = ContractConvert.INSTANCE.vo2Entity(contractVo);
        //?????????????????????????????????????????????????????????
        if (id == null || id == 0L) {
            contract = temporaryContract(contractVo);
        }else {
            contract = this.getById(id);
            contract.setSignUpStatus(contractVo.isSubmit() ? SignUpStatus.CONTRACT_UPLOAD: SignUpStatus.BASIC_INFO);
        }
        // ????????????
        ContractCreateVo contractCreateVo = new ContractCreateVo();
        BeanUtils.copyProperties(contractVo, contractCreateVo);
        //??????????????????????????????????????????
        contractCreateVo.setLeaseStartDate(contractVo.getLeaseStartDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        contractCreateVo.setDueDate(contractVo.getDueDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        contractCreateVo.setEntId(contract.getEntId());
        return this.addSettleContractInfo(contractCreateVo, contract);
    }


    //??????????????????????????????????????????
    @Override
    public Contract temporaryContract(SettleContractVo contractVo){
        Contract contract = ContractConvert.INSTANCE.vo2Entity(contractVo);

        if (contract.getLeaseStartDate() != null && contract.getDueDate() != null) {
            Boolean result2 = DateInterval.calStringDateDiff(contract.getLeaseStartDate(), contract.getDueDate().toString());
            if (result2) {
                throw new BizException(BizCodeFace.createBizCode(ErrorCode.FAIL)
                        .message("???????????????????????????????????????????????????"));
            }
        }

        //??????????????????????????????id?????????id
        if (!CollectionUtils.isEmpty(contractVo.getBuildingList())) {
            Building building = this.buildingService.getById(contractVo.getBuildingList().get(0));
            if (building != null) {
                contract.setBuildingName(building.getName());
            }
            contract.setBuildingIds(org.apache.commons.lang3.StringUtils.join(contractVo.getBuildingList(),","));
            contract.setParkId(building.getParkId());
        }
        contract.setSignUpStatus(contractVo.isSubmit() ? SignUpStatus.CONTRACT_UPLOAD: SignUpStatus.BASIC_INFO);
        if (contract.getId() == null || contract.getId() == 0L) {
            this.save(contract);
        }else {
            boolean result = this.updateById(contract);
            if (!result) {
                throw new BizException(BizCodeFace.createBizCode(ErrorCode.FAIL)
                        .message("??????????????????"));
            }
        }

        //??????????????????????????????
        List<ContractUnit> contractUnits = this.contractUnitService.getUnitListByContractId(contract.getId());
        if (!CollectionUtils.isEmpty(contractUnits)) {
            this.contractUnitService.remove(new UpdateWrapper<ContractUnit>().lambda().in(BaseEntity::getId, contractUnits.stream().map(BaseEntity::getId).collect(Collectors.toList())));
        }
        //???????????????????????????
        if (ArrayUtils.isNotEmpty(contractVo.getUnitIds())) {
            // ??????????????????????????????--????????????????????????????????????
            for (Long unitId : contractVo.getUnitIds()) {
                //????????????????????????
                ContractUnit contractUnit = new ContractUnit();
                contractUnit.setUnitId(unitId);
                contractUnit.setContractId(contract.getId());
                contractUnit.setStatus(ContractUnitState.normal);
                contractUnit.setDurdate(contract.getDueDate());
                this.contractUnitService.save(contractUnit);
            }
        }
        return contract;
    }

    @Override
    public List<ContractVo> selectRunningListForIndex() {
        return this.getBaseMapper().selectRunningListForIndex();
    }

    private Contract addSettleContractInfo(ContractCreateVo createVo, Contract entity) {
        if (createVo.getLeaseStartDate() != null && createVo.getDueDate() != null) {
            Boolean result2 = DateInterval.calStringDateDiff(createVo.getLeaseStartDate(), createVo.getDueDate().toString());
            if (result2) {
                throw new BizException(BizCodeFace.createBizCode(ErrorCode.FAIL)
                        .message("???????????????????????????????????????????????????"));
            }
        }
        if (createVo.getEntId() != null) {
            // ????????????
            Ent ent = this.entService.getById(createVo.getEntId());
            if (ent == null) {
                throw new BizException(BizCodeFace.createBizCode(ErrorCode.FAIL).message("?????????????????????????????????"));
            }
            //???????????????????????????
            entity.setEntId(ent.getId());
            entity.setEntName(ent.getName());
            //????????????????????????????????????
            entity.setContact(ent.getContact());
            entity.setContactPhoneNumber(ent.getContactPhoneNumber());
        }
        //???????????????????????????
        entity.setLegal(createVo.getLegal());
        entity.setLeaseStartDate(createVo.getLeaseStartDate());
        entity.setDueDate(createVo.getDueDate())
                .setParkId(createVo.getParkId())
                .setSignDate(createVo.getSignDate())
                .setLegalPhoneNumber(createVo.getLegalPhoneNumber())
                .setPrice(createVo.getPrice())
                .setDueDate(createVo.getDueDate())
                .setRent(createVo.getRent())
                .setArea(createVo.getArea())
                .setRentFreePeriod(createVo.getRentFreePeriod());
        //?????????????????????
        if (!CollectionUtils.isEmpty(createVo.getBuildingIdList())){
            for (int i = 0; i < createVo.getBuildingIdList().size(); i++) {
                Long buildId = createVo.getBuildingIdList().get(i);
                Building byId = buildingService.getById(buildId);
                if (byId == null){
                    throw new BizException(BizCodeFace.createBizCode(ErrorCode.FAIL)
                            .message("????????????????????????"));
                }
            }
            //???????????????
            entity.setBuildingIds(org.apache.commons.lang3.StringUtils.join(createVo.getBuildingIdList(),","));
        }
        //????????????????????????????????????????????????????????????
        List<ContractUnit> contractUnits = this.contractUnitService.getUnitListByContractId(entity.getId());
        if (!CollectionUtils.isEmpty(contractUnits)) {
            this.contractUnitService.remove(new UpdateWrapper<ContractUnit>().lambda().in(BaseEntity::getId, contractUnits.stream().map(BaseEntity::getId).collect(Collectors.toList())));
            //?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
            for (int i = 0; i < contractUnits.size(); i++) {
                ContractUnit contractUnit = contractUnits.get(i);
                //????????????????????????????????????
                ContractUnit currentUnitInfo = this.contractUnitService.getNormalByUnitId(contractUnit.getUnitId(), LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),entity.getId());
                //????????????????????????????????????????????????????????????????????????
                if (currentUnitInfo == null) {
                    this.unitService.update(new UpdateWrapper<Unit>().lambda()
                            .set(Unit::getStatus, UnitState.free)
                            .in(BaseEntity::getId, contractUnits.stream().map(ContractUnit::getUnitId).collect(Collectors.toSet()))
                    );
                }
            }

        }
        //???????????????????????????????????????????????????
        if (ArrayUtils.isNotEmpty(createVo.getUnitIds()) && createVo.getLeaseStartDate() != null) {
            //?????????????????????
            String dueDate = createVo.getDueDate();
            //?????????????????????
            String leaseStartDate = createVo.getLeaseStartDate();
            //????????????
            for (Long unitId : createVo.getUnitIds()) {
                //????????????id,?????????????????????????????????????????????
                ContractMapper.QueryParam queryParam = new ContractMapper.QueryParam();
                queryParam.setDueDate(dueDate);
                queryParam.setLeaseStartDate(leaseStartDate);
                queryParam.setUnit(String.valueOf(unitId));
                List<ContractVo> contractVos = this.getBaseMapper().selectContractByUnitAndDate(queryParam);
                if (!CollectionUtils.isEmpty(contractVos)) {
                    String codes = contractVos.stream().map(ContractVo::getCode).collect(Collectors.joining(","));
                    Unit unit = this.unitService.getById(unitId);
                    throw new BizException(BizCodeFace.createBizCode(ErrorCode.FAIL)
                            .message("??????" + unit.getUnitNo() + "???????????????????????????????????????????????????????????????????????????"));

                }
            }

        }
        entity.setEstablishId(JwtUtil.getCurrentUser().getEstablishId());

        //??????????????????
        boolean result = this.updateById(entity);
        //????????????????????????????????????????????????
        if (result) {
            List<String> unitString = new ArrayList<>();
            if (ArrayUtils.isNotEmpty(createVo.getUnitIds())) {
                // ??????????????????????????????????????????????????????
                int sort=0;
                for (Long unitId : createVo.getUnitIds()) {
                    Unit unit = this.unitService.getById(unitId);
                    if(unit == null){
                        throw new BizException(BizCodeFace.createBizCode(ErrorCode.FAIL)
                                .message("????????????????????????????????????????????????"));
                    }
                    unitString.add(unit.getUnitNo());
                    entity.setUnit(String.join(",", unitString));

                    ContractUnit contractUnit = new ContractUnit();
                    contractUnit.setUnitId(unit.getId());
                    contractUnit.setContractId(entity.getId());
                    contractUnit.setStatus(ContractUnitState.normal);
                    //???????????????????????????????????????
                    contractUnit.setBuildingId(unit.getBuildingId());
                    if (entity.getStatus().equals(ContractStatus.wait_pre)) {
                        contractUnit.setStatus(ContractUnitState.wait_pre);
                    }
                    entity.setStatus(ContractStatus.wait_contract);
                    contractUnit.setDurdate(entity.getDueDate());
                    contractUnit.setSort(sort++);
                    this.contractUnitService.save(contractUnit);
                }
            }
//            this.updateById(entity);
        }
        return entity;
    }


    @Override
    public Contract submit(Long id) {
        Contract contract = this.getById(id);
        if (contract == null) {
            throw new BizException(BizCodeFace.createBizCode(ErrorCode.DATE_NULL).message("?????????????????????"));
        }
        contract.setSignUpStatus(SignUpStatus.OK);

        //????????????????????????????????????????????????
        Boolean result = DateInterval.calStringDateDiff(DateInterval.getSimpleDate(new Date()), contract.getDueDate().toString());
        if (result){
            contract.setStatus(ContractStatus.stop);
            //?????????????????????????????????
            List<ContractUnit> contractUnits = this.contractUnitService.getUnitListByContractId(contract.getId());
            for (ContractUnit contractUnit : contractUnits) {
                Unit unit = this.unitService.getById(contractUnit.getUnitId());
                unit.setStatus(UnitState.free);
                this.unitService.updateById(unit);
                //??????????????????????????????
                contractUnit.setStatus(ContractUnitState.close);
                this.contractUnitService.updateById(contractUnit);
            }
            //????????????????????????????????????,????????????????????????
        }else if(contract.getLeaseStartDate().compareTo(DateUtil.formatDate(new Date()))> 0) {
            contract.setStatus(ContractStatus.wait_pre);
            //?????????????????????????????????
            List<ContractUnit> contractUnits = this.contractUnitService.getUnitListByContractId(contract.getId());
            for (ContractUnit contractUnit : contractUnits) {
                //??????????????????????????????
                contractUnit.setStatus(ContractUnitState.wait_pre);
                this.contractUnitService.updateById(contractUnit);
            }
        }else {
            contract.setStatus(ContractStatus.running);
            //?????????????????????????????????
            List<ContractUnit> contractUnits = this.contractUnitService.getUnitListByContractId(contract.getId());
            for (ContractUnit contractUnit : contractUnits) {
                //??????????????????????????????
                contractUnit.setStatus(ContractUnitState.normal);
                this.contractUnitService.updateById(contractUnit);
                //????????????????????????
                Unit unit = this.unitService.getById(contractUnit.getUnitId());
                unit.setStatus(UnitState.busy);
                this.unitService.updateById(unit);

            }
        }
        contract.setCode(this.serialNumberService.getContractSerialNumber(contract));
        this.updateById(contract);
        this.entService.update(new UpdateWrapper<Ent>().lambda()
                .set(Ent::getSignupStatus, SignupStatus.OK)
                .set(Ent::getSettleTime, contract.getLeaseStartDate())
                .set(Ent::getNewSettle, NewSettleStatus.no)
                .eq(Ent::getId, contract.getEntId()));

        return contract;
    }

    /**
     * ????????????????????????
     * @param settleVo
     * @return
     */
    private Contract createNewSettleContract(SettleInBaseInfoVo settleVo) {
        // ????????????
        Ent entCreateVo = createNewSettleEnt(settleVo);
        Contract contract = new Contract();
        BeanUtils.copyProperties(settleVo, contract);
        contract.setEntId(entCreateVo.getId())
                .setBizType(BizType.SETTLE)
                .setSignUpStatus(settleVo.isSubmit() ? SignUpStatus.CONTRACT_UPLOAD : SignUpStatus.BASIC_INFO);
        this.save(contract);
        return contract;
    }

    @NotNull
    private Ent createNewSettleEnt(SettleInBaseInfoVo settleVo) {
        EntCreateVo entCreateVo = new EntCreateVo();
        BeanUtils.copyProperties(settleVo, entCreateVo);
        entCreateVo.setName(settleVo.getName());
        if (entCreateVo.getUnifiedSocialCreditCode() != null) {
            entCreateVo.setUnifiedSocialCreditCode(entCreateVo.getUnifiedSocialCreditCode().toUpperCase(Locale.ROOT));
        }
        entCreateVo.setParkId(settleVo.getParkId());
        Ent ent = this.entService.createNew(entCreateVo);
        this.entService.update(new UpdateWrapper<Ent>()
                .lambda()
                .set(Ent::getSignupStatus, SignupStatus.INPUTTING)
                .eq(Ent::getId, ent.getId())
        );
        return ent;
    }


    /**
     * ????????????????????????
     *
     * @param vo
     * @param wrapper
     */
    private void getContractUnitListById(ContractVo vo, QueryWrapper<ContractUnit> wrapper) {
        List<ContractUnit> contractUnits = this.contractUnitService.list(wrapper);
        List<String> unitNames = new ArrayList<>();
        List<Long> unitIds = new ArrayList<>();
        BigDecimal area = BigDecimal.ZERO;
        for (ContractUnit contractUnit : contractUnits) {
            Unit unit = this.unitService.getById(contractUnit.getUnitId());
            unitNames.add(unit.getUnitNo());
            unitIds.add(unit.getId());
            area = area.add(unit.getUsableArea());
        }
//        vo.setUnit(String.join(",", unitNames));
        vo.setUnitIds(Arrays.copyOf(unitIds.toArray(), unitIds.toArray().length, Long[].class));
        // vo.setArea(area);
//        vo.setUnitNames(Arrays.copyOf(unitNames.toArray(), unitNames.toArray().length, String[].class));
    }

    /**
     * ?????????????????????
     * @return
     */
    @Override
    public List<Contract> selectContractByDueDate() {
       return this.getBaseMapper().selectContractByDueDate();
    }

    @Override
    public Boolean contractByToDayStart(Contract contract) {
        //?????????????????????????????????????????????????????????
        if(contract == null || contract.getId() == null){
            return false;
        }
        //?????????????????? ???????????????????????????
        QueryWrapper<ContractUnit> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(ContractUnit::getContractId, contract.getId()).eq(ContractUnit::getStatus,ContractUnitState.wait_pre);
        List<ContractUnit> unitListByContractId = this.contractUnitService.list(wrapper);

        //?????????????????????
        if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(unitListByContractId)){
            for (int i = 0; i < unitListByContractId.size(); i++) {
                ContractUnit contractUnit = unitListByContractId.get(i);
                //??????contractUnit ??????????????????
                contractUnit.setStatus(ContractUnitState.normal);

                contractUnit.setModifyTime(LocalDateTime.now());
                contractUnitService.updateById(contractUnit);
                log.info("?????????"+ DateUtil.formatDateTime(new Date()) +"???;??????????????????????????????"+contract.getCode()+"??????????????????id???"+contractUnit.getId()+"???????????????????????????");

                //??????????????????????????????
                Unit unit = this.unitService.getById(contractUnit.getUnitId());
                if (unit != null && unit.getId() != null){
                    unit.setModifyTime(LocalDateTime.now());
                    //?????????????????????
                    unit.setStatus(UnitState.busy);
                    unitService.updateById(unit);
                    log.info("?????????"+ DateUtil.formatDateTime(new Date()) +"???;??????????????????????????????"+contract.getCode()+"????????????id???"+unit.getId()+"??????????????????????????????");
                }

            }
        }
        return true;
    }

    @Override
    public List<Contract> selectContractByToDayStart() {
       return this.getBaseMapper().selectContractByToDayStart();
    }

    @Override
    public Page<Long> findEntIdByContract(LeaseReportContractQueryParam leaseReportContractQueryParam) {
        String currentUserParkIds = JwtUtil.getCurrentUserParkIds();
        //?????????????????????????????????????????????????????????
        if (JwtUtil.getCurrentUser().getRoleId() != 1) {
            leaseReportContractQueryParam.setParkIds(currentUserParkIds);
        }
        // ???????????????????????????
        ContractMapper.QueryParam queryParam=new ContractMapper.QueryParam();
        BeanUtils.copyProperties(leaseReportContractQueryParam,queryParam);
        try {
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat(DatePattern.NORM_DATE_PATTERN);
            if (StrUtil.isNotBlank(leaseReportContractQueryParam.getStartMonth())){
                Date startDate=simpleDateFormat.parse(leaseReportContractQueryParam.getStartMonth());
                String format = simpleDateFormat.format(startDate);
                queryParam.setLeaseStartDate(format);
            }
            if (StrUtil.isNotBlank(leaseReportContractQueryParam.getEndMonth())){
                Date endDate=simpleDateFormat.parse(leaseReportContractQueryParam.getEndMonth());
                String format = simpleDateFormat.format(endDate);
                queryParam.setDueDate(format);
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException(e.getCause());
        }
        Page<Contract> page = new Page<>(leaseReportContractQueryParam.getCurrent(), leaseReportContractQueryParam.getPageSize());
        Page<Long> entPage = this.getBaseMapper().selectEntIdByContract(page, queryParam);
        return entPage;
    }

    @Override
    public List<ContractVo> findLeaseReportContractByParam(LeaseReportContractQueryParam leaseReportContractQueryParam)  {
        String currentUserParkIds = JwtUtil.getCurrentUserParkIds();
        //?????????????????????????????????????????????????????????
        if (JwtUtil.getCurrentUser().getRoleId() != 1) {
            leaseReportContractQueryParam.setParkIds(currentUserParkIds);
        }
        // ???????????????????????????
        ContractMapper.QueryParam queryParam=new ContractMapper.QueryParam();
        BeanUtils.copyProperties(leaseReportContractQueryParam,queryParam);
        try {
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat(DatePattern.NORM_DATE_PATTERN);
            if (StrUtil.isNotBlank(leaseReportContractQueryParam.getStartMonth())){
                Date startDate=simpleDateFormat.parse(leaseReportContractQueryParam.getStartMonth());
                String format = simpleDateFormat.format(startDate);
                queryParam.setLeaseStartDate(format);
            }
            if (StrUtil.isNotBlank(leaseReportContractQueryParam.getEndMonth())){
                Date endDate=simpleDateFormat.parse(leaseReportContractQueryParam.getEndMonth());
                String format = simpleDateFormat.format(endDate);
                queryParam.setDueDate(format);
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException(e.getCause());
        }
        return this.getBaseMapper().selectContractByLeaseReportParam(queryParam);
    }

    @Override
    public List<ContractVo> useHouseDetailReport(UseHouseDetailQueryParam useHouseDetailQueryParam) {
        ContractMapper.UseHouseDetailQueryParam queryParam=new  ContractMapper.UseHouseDetailQueryParam();
        queryParam.setUnitIds(useHouseDetailQueryParam.getUnitIds());
        queryParam.setStartDate(useHouseDetailQueryParam.getStartDate());
         return   this.getBaseMapper().useHouseDetailReport(queryParam);
    }

    @Override
    public void export(List<ContractVo> content, HttpServletResponse response,String fileName) {
        if (CollectionUtil.isNotEmpty(content)) {
            List<ContractExportVo> list=new ArrayList<>();
            //????????????
            for (int i = 0; i < content.size(); i++) {
                ContractExportVo contractExportVo=new ContractExportVo();
                BeanUtils.copyProperties(content.get(i),contractExportVo);
                contractExportVo.setBuildingNames(StrUtil.join(",",content.get(i).getBuildingNames()));
                contractExportVo.setUnitNames(StrUtil.join(",",content.get(i).getUnitNames()));
                if (content.get(i).getBizType() != null) {
                    contractExportVo.setBizType(content.get(i).getBizType().getLabel());
                }
                contractExportVo.setStatus(getContractStatus(content.get(i).getStatus()));
                //????????????
                list.add(contractExportVo);
            }
            //?????????????????????????????????
            ExcelWriter writer = new ExcelWriter(true);
            writer.setOnlyAlias(true);
            //??????????????? ?????????
            ReportServiceImpl.createExportTitleAndHeadByClass(writer, ContractExportVo.class, fileName+"????????????");
            //?????????????????????????????????
            writer.write(list, true);
            //?????????????????????????????????
            writer.setRowHeight(0, 25);
            writer.setRowHeight(1, 18);

            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
            response.setHeader("Content-Disposition", "attachment;filename="+ EscapeUtil.escape(fileName+"????????????")+".xlsx");
            try {
                ServletOutputStream out = response.getOutputStream();
                writer.flush(out, true);
                // ??????writer???????????????
                writer.close();
                //????????????????????????Servlet???
                IoUtil.close(out);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String getContractStatus(String contractStatus){
        if (StrUtil.equals("stop",contractStatus))
                return   "?????????";
        if ( StrUtil.equals("wait",contractStatus))
                return "";
        if ( StrUtil.equals("wait_contract",contractStatus))
                return  "???????????????";
        if ( StrUtil.equals("wait_pre",contractStatus))
                return "???????????????";
        if ( StrUtil.equals("running",contractStatus))
              return   "?????????";
        if ( StrUtil.equals("due_to_close",contractStatus))
                return "????????????";
        return null;
    }

    @Override
    public Boolean closeUnitByContract(Contract contract) {
        //?????????????????????????????????????????????????????????
        if(contract == null || contract.getId() == null){
            return false;
        }
        //????????????????????????????????????
        List<ContractUnit> unitListByContractId = this.contractUnitService.getUnitListByContractId(contract.getId());
        //?????????????????????
        if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(unitListByContractId)){
            for (int i = 0; i < unitListByContractId.size(); i++) {
                ContractUnit contractUnit = unitListByContractId.get(i);
                //??????contractUnit ????????? ???????????????
                contractUnit.setStatus(ContractUnitState.close);
                //????????????
                contractUnit.setDurdate(contract.getDueDate());
                contractUnit.setModifyTime(LocalDateTime.now());
                contractUnit.setModifyBy("??????");
                contractUnitService.updateById(contractUnit);
                log.info("?????????"+ DateUtil.formatDateTime(new Date()) +"???;??????????????????????????????"+contract.getCode()+"??????????????????id???"+contractUnit.getId()+"???????????????????????????");

                //??????????????????????????????
                Unit unit = this.unitService.getById(contractUnit.getUnitId());
                if (unit != null && unit.getId() != null){
                    unit.setModifyTime(LocalDateTime.now());
                    //?????????????????????
                    unit.setStatus(UnitState.free);
                    unitService.updateById(unit);
                    log.info("?????????"+ DateUtil.formatDateTime(new Date()) +"???;??????????????????????????????"+contract.getCode()+"????????????id???"+unit.getId()+"???????????????????????????");
                }

            }
        }

        ContractCloseHistory closeHistory = new ContractCloseHistory();
        closeHistory.setContractId(contract.getId());
        closeHistory.setCloseResult(true);
        closeHistory.setCloseType(BizType.STOP);
        closeHistory.setRemark("?????????????????????????????????");
        this.closeHistoryService.save(closeHistory);
        return true;
    }
}