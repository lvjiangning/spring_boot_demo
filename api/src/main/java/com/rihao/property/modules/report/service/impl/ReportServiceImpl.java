package com.rihao.property.modules.report.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.stream.CollectorUtil;
import cn.hutool.core.util.*;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.google.common.collect.Lists;
import com.rihao.property.common.page.PageVo;
import com.rihao.property.modules.config.entity.SysConfig;
import com.rihao.property.modules.config.service.ISysConfigService;
import com.rihao.property.modules.ent.entity.Ent;
import com.rihao.property.modules.ent.service.IEntService;
import com.rihao.property.modules.establish.entity.Establish;
import com.rihao.property.modules.establish.service.IEstablishService;
import com.rihao.property.modules.estate.entity.Park;
import com.rihao.property.modules.estate.enums.UnitState;
import com.rihao.property.modules.estate.mapper.UnitMapper;
import com.rihao.property.modules.estate.service.IParkService;
import com.rihao.property.modules.estate.vo.UnitVo;
import com.rihao.property.modules.report.controller.params.LeaseReportContractQueryParam;
import com.rihao.property.modules.lease.contract.entity.Contract;
import com.rihao.property.modules.lease.contract.service.IContractService;
import com.rihao.property.modules.lease.contract.vo.ContractCreateVo;
import com.rihao.property.modules.lease.contract.vo.ContractUnitVo;
import com.rihao.property.modules.lease.contract.vo.ContractVo;
import com.rihao.property.modules.lease.unit.entity.ContractUnit;
import com.rihao.property.modules.lease.unit.service.IContractUnitService;
import com.rihao.property.modules.report.controller.params.LandUseReportBaseQueryParam;
import com.rihao.property.modules.report.controller.params.LedgerReportBaseQueryParam;
import com.rihao.property.modules.report.controller.params.UseHouseDetailQueryParam;
import com.rihao.property.modules.report.mapper.ReportMapper;
import com.rihao.property.modules.report.service.IReportService;
import com.rihao.property.modules.report.vo.*;
import com.rihao.property.modules.estate.entity.Unit;
import com.rihao.property.modules.estate.service.IUnitService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rihao.property.shiro.util.JwtUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wangyu
 * @since 2021-03-31
 */
@Slf4j
@Service
public class ReportServiceImpl extends ServiceImpl<ReportMapper, LandUseReportVo> implements IReportService {

    private final IContractService contractService;
    private final IEntService entService;
    private final IContractUnitService contractUnitService;
    private final IUnitService unitService;
    @Autowired
    @Lazy
    private IParkService parkService;
    @Autowired
    @Lazy
    private IEstablishService establishService;
    @Autowired
    @Lazy
    private ISysConfigService sysConfigService;

    public ReportServiceImpl(IContractService contractService,
                             IEntService entService,
                             IContractUnitService contractUnitService,
                             IUnitService unitService) {
        this.contractService = contractService;
        this.entService = entService;
        this.contractUnitService = contractUnitService;
        this.unitService = unitService;
    }

    @Override
    public PageVo<LandUseReportVo> landUseReport(LandUseReportBaseQueryParam landUseReportQueryParam) throws ParseException {
        Page<LandUseReportVo> page = new Page<>(landUseReportQueryParam.getCurrent(), landUseReportQueryParam.getPageSize());
        ReportMapper.QueryParam params = new ReportMapper.QueryParam();
        Page<LandUseReportVo> result = this.getBaseMapper().selectLanUseReportByQueryParam(page, params);
        List<LandUseReportVo> list = result.getRecords();
        for (LandUseReportVo landUseReportVo : list) {
            if (landUseReportVo.getApprovalDate() != null)
                landUseReportVo.setApprovalDate(landUseReportVo.getApprovalDate().substring(0, 10));
        }

        return PageVo.create(landUseReportQueryParam.getCurrent(), landUseReportQueryParam.getPageSize(),
                result.getTotal(), list);
    }

    @Override
    public PageVo<BuildingLedgerVo> buildingLedger(LedgerReportBaseQueryParam ledgerReportQueryParam) {
        Page<Contract> page = new Page<>(ledgerReportQueryParam.getCurrent(), ledgerReportQueryParam.getPageSize());
        PageVo<ContractVo> result = this.contractService.getByBuildingId(ledgerReportQueryParam.getCurrent(),
                ledgerReportQueryParam.getPageSize(), ledgerReportQueryParam.getBuildingId());
        List<BuildingLedgerVo> list = new ArrayList<>();
        for (ContractVo contract : result.getContent()) {
            BuildingLedgerVo buildingLedgerVo = new BuildingLedgerVo();
            buildingLedgerVo.setContractCode(contract.getCode());
            buildingLedgerVo.setEntName(contract.getEntName());
            buildingLedgerVo.setContact(contract.getContact());
            buildingLedgerVo.setContactPhonenumber(contract.getContactPhoneNumber());
            buildingLedgerVo.setRent(contract.getRent());
            buildingLedgerVo.setLeaseTerm(contract.getLeaseTerm());

            String signDate = contract.getLeaseStartDate().toString().substring(0, 10);
            String prefix = signDate.substring(0, 4);
            String suffix = signDate.substring(4, 10);
            prefix = (Integer.parseInt(prefix) + Integer.parseInt(contract.getLeaseTerm())) + "";
            buildingLedgerVo.setContractStartEndData(contract.getLeaseStartDate() + "-" + prefix + suffix);

            Ent ent = this.entService.getById(contract.getEntId());
            buildingLedgerVo.setSettleDate(contract.getCreateTime());
            buildingLedgerVo.setEarliestSettleDate(ent.getCreateTime().toString());
            buildingLedgerVo.setContractStatus(contract.getStatus().equals("0") ? "已关闭" : "正常");

            list.add(buildingLedgerVo);
        }
        return PageVo.create(ledgerReportQueryParam.getCurrent(),
                ledgerReportQueryParam.getPageSize(), result.getTotal(), list);
    }

    @Override
    public PageVo<UnitLedgerVo> unitLedger(LedgerReportBaseQueryParam ledgerReportQueryParam) {
        Page<ContractUnit> page = new Page<>(ledgerReportQueryParam.getCurrent(), ledgerReportQueryParam.getPageSize());
        Page<ContractUnit> result = this.contractService.getContractUnitByBuildingId(ledgerReportQueryParam);
        List<UnitLedgerVo> list = new ArrayList<>();
        for (ContractUnit record : result.getRecords()) {
            UnitLedgerVo unitLedgerVo = new UnitLedgerVo();

            Contract contract = this.contractService.getById(record.getContractId());
            if (contract == null)
                continue;
            Unit unit = this.unitService.getById(record.getUnitId());
            Ent ent = this.entService.getById(contract.getEntId());

            unitLedgerVo.setUnit(unit.getUnitNo());
            unitLedgerVo.setArea(unit.getUsableArea());
            DecimalFormat decimalFormat = new DecimalFormat("0.00");
            BigDecimal rent = contract.getPrice()
                    .multiply(unit.getUsableArea());
            unitLedgerVo.setRent(decimalFormat.format(rent));
            unitLedgerVo.setBuildingName(contract.getBuildingName());
            unitLedgerVo.setEntName(ent.getName());
            unitLedgerVo.setSettleDate(ent.getCreateTime().toString().substring(0, 10));

            String startDate = contract.getLeaseStartDate();
            String prefix = startDate.substring(0, 4);
            String suffix = startDate.substring(4, 10);
            prefix = (Integer.parseInt(prefix) + Integer.parseInt(contract.getLeaseTerm())) + "";

            unitLedgerVo.setDueDate(prefix + suffix);
            unitLedgerVo.setEndDate(record.getDurdate());
            list.add(unitLedgerVo);
        }

        return PageVo.create(ledgerReportQueryParam.getCurrent(),
                ledgerReportQueryParam.getPageSize(), result.getTotal(), list);
    }


    @Override
    public PageVo<LeaseReportVo> leaseReport(LeaseReportContractQueryParam leaseReportContractQueryParam) {
        //保存传入的参数
        LeaseReportContractQueryParam leaseReportContractQueryParam1 = new LeaseReportContractQueryParam();
        BeanUtil.copyProperties(leaseReportContractQueryParam, leaseReportContractQueryParam1);
        //查询出当前页需要展示的企业
        Page<Long> entIdsByContract = this.contractService.findEntIdByContract(leaseReportContractQueryParam);
        //返回的结果
        List<LeaseReportVo> result = new ArrayList<>();
        entIdsByContract.getRecords().forEach(a -> {
            LeaseReportContractQueryParam leaseReportContractQueryParam2 = new LeaseReportContractQueryParam();
            //克隆一份参数，查询每一个企业的数据
            BeanUtil.copyProperties(leaseReportContractQueryParam1, leaseReportContractQueryParam2);
            //随便设，只为查询所有的数据
            leaseReportContractQueryParam2.setPageSize(1000);
            leaseReportContractQueryParam2.setEntId(a);
            result.addAll(generateLeaseReportRow(leaseReportContractQueryParam2));
        });
        return PageVo.create(leaseReportContractQueryParam.getCurrent(),
                leaseReportContractQueryParam.getPageSize(), entIdsByContract.getTotal(), result);
    }

    /**
     * 生成资产租赁报表的行数据，需要根据园区，企业进行分割
     * 1、先通过企业id(传参时封装在了leaseReportContractQueryParam)，以及前端传递的参数，查询出当前企业符合条件的合同数据
     * 2、根据是否要进行月份分割（起租时间leaseStartDate），进行数据生成，如果一个企业存在租赁2个园区的房子，则显示分别根据2个园区展示
     *
     * @param leaseReportContractQueryParam 前端传递的查询参数，限制源数据的提取范围
     * @return key 月份，value= 行数据
     */
    private List<LeaseReportVo> generateLeaseReportRow(LeaseReportContractQueryParam leaseReportContractQueryParam) {
        //封装返回对象
        List<LeaseReportVo> reportVos = new ArrayList<>();
        List<ContractVo> contractVos = this.contractService.findLeaseReportContractByParam(leaseReportContractQueryParam);
        //1、先通过企业id(传参时封装在了leaseReportContractQueryParam)，以及前端传递的参数，查询出当前企业符合条件的合同数据
        if (org.apache.commons.collections.CollectionUtils.isNotEmpty(contractVos)) {
            //根据园区区分合同集
            Map<Long, List<ContractVo>> contractSplitPark = contractVos.stream().collect(Collectors.groupingBy(ContractCreateVo::getParkId));
            Iterator<Long> iterator = contractSplitPark.keySet().iterator();
            while (iterator.hasNext()) {
                Long parkId = iterator.next();
                List<ContractVo> contractVosForPark = contractSplitPark.get(parkId);
                LeaseReportVo leaseReportVo = generateLeaseReportVo(contractVosForPark, leaseReportContractQueryParam);
                //合同结束时间实际的结束时间
                leaseReportVo.setDueDate(getDueDate(contractVosForPark));
                reportVos.add(leaseReportVo);
            }
        }
        return reportVos;
    }

    /**
     * 生成资产租赁报vo对象
     * 主要逻辑在计算租金，其中 租金是当月收当月，
     *
     * @param contractVos
     * @return
     */
    private LeaseReportVo generateLeaseReportVo(List<ContractVo> contractVos, LeaseReportContractQueryParam queryParam) {
        if (CollectionUtils.isEmpty(contractVos)) {
            return null;
        }
        LeaseReportVo leaseReportVo = new LeaseReportVo();
        //开始处理值
        Park park = this.parkService.getById(contractVos.get(0).getParkId());
        //园区
        leaseReportVo.setParkName(park.getName());
        leaseReportVo.setParkId(park.getId());
        //通过合同ids 查询合同信息，包括合同对应的单元信息
        Set<Long> contractIds = contractVos.stream().filter(a -> a.getId() != null && StrUtil.equals("running", a.getStatus())).map(ContractVo::getId).collect(Collectors.toSet());
        List<ContractUnitVo> unitsInContractIds = contractUnitService.getUnitsInContractIds(contractIds);
        if (!CollectionUtils.isEmpty(unitsInContractIds)) {
            Set<Long> unitIds = unitsInContractIds.stream().filter(a -> a.getUnitId() != null).map(ContractUnit::getUnitId).collect(Collectors.toSet());
            //查询单元信息
            if (!CollectionUtils.isEmpty(unitIds)) {
                List<Unit> units = this.unitService.listByIds(unitIds);
                if (!CollectionUtils.isEmpty(units)) {
                    leaseReportVo.setUnitNames(units.stream().map(Unit::getUnitNo).collect(Collectors.joining("、")));
                    //求建筑面积.去重求和
                    List<Long> collect = units.stream().map(Unit::getId).distinct().collect(Collectors.toList());
                    BigDecimal reduce = units.stream().filter(a -> collect.contains(a.getId())).map(Unit::getBuiltUpArea).reduce(BigDecimal.ZERO, NumberUtil::add);
                    leaseReportVo.setBuiltUpArea(reduce);
                }
            }
        }
        leaseReportVo.setCalUnit("平方米");
        Ent ent = this.entService.getById(contractVos.get(0).getEntId());
        //承租企业
        leaseReportVo.setEntName(ent.getName());
        leaseReportVo.setEntId(ent.getId());
        //主要用途
        leaseReportVo.setUsed(park.getUsed());//取园区用途
        //产权单位
        leaseReportVo.setEstablish(park.getBelongUnit());
        //租赁生效日期 与  租赁结束日期，最新一份合同的生效日期，和最后一个合同结束日期
        Stream<ContractVo> sorted = contractVos.stream().filter(a -> a.getLeaseStartDate() != null).sorted(Comparator.comparing(a -> DateUtil.parse(a.getLeaseStartDate())));
        Optional<ContractVo> first = sorted.findFirst();
        leaseReportVo.setLeaseStartDate(first.get().getLeaseStartDate());
        //招租方式 默认为空
        leaseReportVo.setLetType("");
        //合同编号默认为空
        leaseReportVo.setContractCode("");

        //如果查询租金的开始时间的月份大于本月，则月租 年租为0
        if (StrUtil.isNotBlank(queryParam.getStartMonth()) && new Date().compareTo(DateUtil.parse(queryParam.getStartMonth())) < 0) {
            leaseReportVo.setYearRent(BigDecimal.ZERO);
            leaseReportVo.setMonthRent(BigDecimal.ZERO);
        } else {
            //生成租金数据
            generateRent(contractVos, queryParam, leaseReportVo);
        }

        //单价 取合同中最大的单据
        Optional<ContractVo> first1 = contractVos.stream().sorted(Comparator.comparing(ContractVo::getPrice).reversed()).findFirst();
        if (first1.isPresent()) {
            leaseReportVo.setUnitRent(first1.get().getPrice());
        }
        leaseReportVo.setCaizhenIncome(leaseReportVo.getYearRent());
        leaseReportVo.setIncome(leaseReportVo.getYearRent());
        //年递增 默认为0
        leaseReportVo.setYearIncrease(new BigDecimal(0));
        leaseReportVo.setQianJiaoIncome(new BigDecimal(0));
        leaseReportVo.setVoucherCode("");
        leaseReportVo.setBalance(new BigDecimal(0));
        return leaseReportVo;
    }

    /**
     * 只计算合同租赁，开始那一个月的租金，或者结束那一个月的租金。
     * 因为只有开始或者结束那一个月，可能租的不是整月，其他都是按照整月收租
     *
     * @param contractVo 合同对象
     * @param month      最早一个月，或者最晚一个月
     */
    private BigDecimal calRent(ContractVo contractVo, int month) {

        String leaseStartDate = contractVo.getLeaseStartDate();
        //计算可以收租的合同开始时间
        int startMonth = DateUtil.month(DateUtil.parse(leaseStartDate)) + 1;
        //计算可以收租的有效结束时间
        String endDate = StrUtil.isNotBlank(contractVo.getEstimateCloseDate()) ? contractVo.getEstimateCloseDate() : contractVo.getDueDate();
        int endMonth = DateUtil.month(DateUtil.parse(endDate)) + 1;
        if (endMonth == month || startMonth == month) {
            //如果结束月份相等与开始月份相等，则需要按照天数计算
            //合同开始时间
            int startDayOfMonth = 0;
            //得到这个自然月的天数
            int endDayOfMonth = DateUtil.lengthOfMonth(month, DateUtil.isLeapYear(DateUtil.thisYear()));
            //得到这个自然月的天数
            int lengthOfMonth = DateUtil.lengthOfMonth(month, DateUtil.isLeapYear(DateUtil.thisYear()));
            //得到每天的租金  四舍五入 保留2位小数
            double dRent = NumberUtil.div(Double.valueOf(contractVo.getRent()), Double.valueOf(lengthOfMonth), 2);
            if (startMonth == month) { //如果开始时间等于当前月，那么计算当前月份可以收租的开始数据
                //得到开始时间是这个月的第几天
                startDayOfMonth = DateUtil.dayOfMonth(DateUtil.parse(leaseStartDate));
            }
            if (endMonth == month) {//如果结束时间等于当前月，那么计算当前月份可以收租的结束日期
                //得到结束日期是这个月的第几天
                endDayOfMonth = DateUtil.dayOfMonth(DateUtil.parse(endDate));
            }
            //本月的租期天数
            Integer day = endDayOfMonth - startDayOfMonth + 1;
            if (day == lengthOfMonth) { //如果等于本月的天数，就算整月， 如2021-1-1日-2021-12-31合同
                return BigDecimal.valueOf(Double.valueOf(contractVo.getRent()));
            }
            //否则天数乘以天租金 假如14号起租，要算上14号
            double mul = NumberUtil.mul(NumberUtil.sub(endDayOfMonth, startDayOfMonth) + 1, dRent);
            return BigDecimal.valueOf(mul);
        }
        return BigDecimal.valueOf(0d);
    }

    @Override
    public List<UseHouseLedgerVo> useHouseLedgerReport() {
        List<UseHouseLedgerVo> result = new ArrayList<>();
        List<Park> parks =new ArrayList<>();
        if (JwtUtil.getCurrentUser().getRoleId() != 1) {
            String currentUserParkIds = JwtUtil.getCurrentUserParkIds();
             List<Long> parkIds =new ArrayList<>();
             CollUtil.addAll(parkIds,currentUserParkIds.split(","));
            parks = this.parkService.getBaseMapper().selectList(new QueryWrapper<Park>().lambda().in(Park::getId,parkIds));

        }else {
            parks = this.parkService.getBaseMapper().selectList(new QueryWrapper<Park>().lambda());
        }

        if (CollectionUtil.isNotEmpty(parks)) {
            for (int i = 0; i < parks.size(); i++) {
                Park park = parks.get(i);
                UseHouseLedgerVo houseLedgerVo = new UseHouseLedgerVo();
                houseLedgerVo.setEstId(park.getEstId());
                houseLedgerVo.setAddress(park.getAddress());
                houseLedgerVo.setParkBuildArea(park.getBuiltUpArea());
                houseLedgerVo.setUsableArea(park.getUsableArea());
                houseLedgerVo.setParkName(park.getName());
                Establish establish = this.establishService.getById(park.getEstId());
                if (establish != null){
                    houseLedgerVo.setEstName(establish.getName());
                }
                houseLedgerVo.setUsed(park.getUsed());
                //当前运行时的合同
                List<Contract> runningContract = this.contractService.getBaseMapper().selectList(new QueryWrapper<Contract>().lambda().eq(Contract::getStatus, "running").eq(Contract::getParkId,
                        park.getId()));
                if (CollectionUtil.isNotEmpty(runningContract)) {
                    //当前入驻的企业数
                    Set<Long> ents = runningContract.stream().map(Contract::getEntId).collect(Collectors.toSet());
                    houseLedgerVo.setEntSum(ents.size());
                    //已租售面积
                    Set<Long> contractIds = runningContract.stream().map(Contract::getId).collect(Collectors.toSet());
                    List<ContractUnitVo> unitsInContractIds = this.contractUnitService.getUnitsInContractIds(contractIds);
                    if (CollectionUtil.isNotEmpty(unitsInContractIds)) {
                        Set<Long> unitIds = unitsInContractIds.stream().map(ContractUnit::getUnitId).collect(Collectors.toSet());
                        List<Unit> units = this.unitService.listByIds(unitIds);
                        if (CollectionUtil.isNotEmpty(units)) {
                            //统计建筑面积为，已租售面积
                            BigDecimal rentArea = units.stream().map(Unit::getBuiltUpArea).reduce(BigDecimal.ZERO, NumberUtil::add);
                            houseLedgerVo.setRentArea(rentArea);
                        }
                    }
                }
                //剩余面积
                houseLedgerVo.setSurplusArea(houseLedgerVo.getRentArea() == null ? park.getUsableArea() : NumberUtil.sub(park.getUsableArea(), houseLedgerVo.getRentArea()));
                result.add(houseLedgerVo);
            }
        }
        //如果产权单位为空，就不排序了
        if (result.stream().filter(a->a.getEstId() == null).findFirst().isPresent()){
            return result;
        }
        return result.stream().sorted(Comparator.comparing(UseHouseLedgerVo::getEstId)).collect(Collectors.toList());
    }

    /**
     * 根据class创建导出标题,和列名
     */
    public static void createExportTitleAndHeadByClass(ExcelWriter sheet, Class<?> clz, String title) {
        //设置head字体
        Font font = sheet.createFont();
        font.setBold(true);
        font.setFontHeight((short)210);
        Field[] fieldsDirectly = ReflectUtil.getFieldsDirectly(clz, false);
        int merget=0;
        for (Field field : fieldsDirectly) {
            ApiModelProperty annotation = field.getAnnotation(ApiModelProperty.class);
            if (annotation != null) {
                sheet.addHeaderAlias(field.getName(), annotation.value());
                sheet.setColumnWidth(merget,30);
                //第二个参数表示是否忽略头部样式
                CellStyle orCreateCellStyle = sheet.getOrCreateCellStyle(1,merget);
                orCreateCellStyle.setFont(font);
                sheet.getCell(1,merget).setCellStyle(orCreateCellStyle);
                merget++;
            }
        }
        //设置head字体
        Font fontTitle = sheet.createFont();
        fontTitle.setBold(true);
        fontTitle.setFontHeight((short)240);

        // 合并单元格后的标题行，使用默认标题样式
        sheet.merge(merget-1, title);
        CellStyle orCreateCellStyle = sheet.getOrCreateCellStyle(0,0);
        orCreateCellStyle.setFont(fontTitle);
        sheet.getCell(0,0).setCellStyle(orCreateCellStyle);
    }

    @Override
    public void leaseReportExport(LeaseReportContractQueryParam leaseReportContractQueryParam, HttpServletResponse response) {
        Map<Integer, List<LeaseReportVo>> map = generateExportLeaseReport(leaseReportContractQueryParam);
        if (MapUtil.isNotEmpty(map)) {
            //得到年租金，key=园区-企业，value=租金
            Map<String, BigDecimal> yearRentMap = MapUtil.newHashMap();
            //按照年月升序
            List<Integer> yearMonthSort = map.keySet().stream().sorted(Comparator.comparing(Integer::intValue)).collect(Collectors.toList());
            //数据不为空则开始生成表
            ExcelWriter writer = new ExcelWriter(true,String.valueOf(yearMonthSort.get(0)));
            for (int i = 0; i < yearMonthSort.size(); i++) {
                //年月，一个年月 就是一个页签
                Integer yearMonth = yearMonthSort.get(i);
                //添加一个页签
                ExcelWriter sheet= i == 0 ? writer:writer.setSheet(String.valueOf(yearMonth));
                //创建标题行 和列名
                createExportTitleAndHeadByClass(sheet, LeaseReportVo.class, yearMonth + "资产租赁明细");
                // 默认的，未添加alias的属性也会写出，如果想只写出加了别名的字段，可以调用此方法排除之
                sheet.setOnlyAlias(true);
                List<LeaseReportVo> rows = map.get(yearMonth);
                //按照合同开始时间升序
                rows = rows.stream().sorted(Comparator.comparing(a -> DateUtil.parse(a.getLeaseStartDate()))).collect(Collectors.toList());
                //不是第一个页签，需要加上之前页签相同园区，相同企业的月租金为年租金
                for (LeaseReportVo leaseReportVo : rows) {
                    BigDecimal yearRentSum=BigDecimal.ZERO;
                    if (i > 0 ){
                        BigDecimal bigDecimal = yearRentMap.get(leaseReportVo.getParkId() + "-" + leaseReportVo.getEntId());
                        if (bigDecimal != null) {
                            yearRentSum =NumberUtil.add(leaseReportVo.getMonthRent(),bigDecimal);
                        }
                    } else { //第一个年月的年租金=月租金
                        yearRentSum = leaseReportVo.getMonthRent();
                    }
                    //记录年租金
                    yearRentMap.put(leaseReportVo.getParkId() + "-" + leaseReportVo.getEntId(), yearRentSum);
                    leaseReportVo.setCaizhenIncome(yearRentSum);
                    leaseReportVo.setIncome(yearRentSum);
                    leaseReportVo.setYearRent(yearRentSum);

                }

                //添加行，并且强制输出行
                sheet.write(rows, true);
                //设置标题行的样式，行高
                sheet.setRowHeight(0,25);
                sheet.setRowHeight(1,18);

            }

            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
            response.setHeader("Content-Disposition", "attachment;filename="+ EscapeUtil.escape("资产租赁明细")+".xlsx");
            try {
                ServletOutputStream out = response.getOutputStream();
                writer.flush(out, true);
                // 关闭writer，释放内存
                writer.close();
                //此处记得关闭输出Servlet流
                IoUtil.close(out);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    @Override
    public void useHouseLedgerReportExport(HttpServletResponse response) {
        //得到数据后，直接导出
        List<UseHouseLedgerVo> list = useHouseLedgerReport();
        if (CollectionUtil.isNotEmpty(list)) {
            //数据不为空则开始生成表
            ExcelWriter writer = new ExcelWriter(true);
            writer.setOnlyAlias(true);
            //创建标题行 和列名
            createExportTitleAndHeadByClass(writer, UseHouseLedgerVo.class, "用房台账");
            //添加行，并且强制输出行
            writer.write(list, true);
            //设置标题行的样式，行高
            writer.setRowHeight(0, 25);
            writer.setRowHeight(1, 18);

            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
            response.setHeader("Content-Disposition", "attachment;filename"+ EscapeUtil.escape("用房台账")+".xlsx");
            try {
                ServletOutputStream out = response.getOutputStream();
                writer.flush(out, true);
                // 关闭writer，释放内存
                writer.close();
                //此处记得关闭输出Servlet流
                IoUtil.close(out);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void useHouseDetailReportExport(UseHouseDetailQueryParam useHouseDetailQueryParam, HttpServletResponse response) {
        //查询全部单元的
        useHouseDetailQueryParam.setPageSize(100000);
        useHouseDetailQueryParam.setCurrent(1);
        //得到数据后，直接导出
        PageVo<UseHouseDetailVo> pageVo = useHouseDetailReport(useHouseDetailQueryParam);
        if (CollectionUtil.isNotEmpty(pageVo.getContent())) {
            //数据不为空则开始生成表
            ExcelWriter writer = new ExcelWriter(true);
            writer.setOnlyAlias(true);
            //创建标题行 和列名
            createExportTitleAndHeadByClass(writer, UseHouseDetailVo.class, "用房明细");
            //添加行，并且强制输出行
            writer.write(pageVo.getContent(), true);
            //设置标题行的样式，行高
            writer.setRowHeight(0, 25);
            writer.setRowHeight(1, 18);

            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
            response.setHeader("Content-Disposition", "attachment;filename"+ EscapeUtil.escape("用房明细")+".xlsx");
            try {
                ServletOutputStream out = response.getOutputStream();
                writer.flush(out, true);
                // 关闭writer，释放内存
                writer.close();
                //此处记得关闭输出Servlet流
                IoUtil.close(out);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public PageVo<UseHouseDetailVo> useHouseDetailReport(UseHouseDetailQueryParam useHouseDetailQueryParam) {
        PageVo<UnitVo> unitVoPageVo = this.unitService.useHouseDetailReport(useHouseDetailQueryParam);
        List<UseHouseDetailVo>  result=new ArrayList<>();
        if (CollectionUtil.isNotEmpty(unitVoPageVo.getContent())){
            List<Long> unitIds = unitVoPageVo.getContent().stream().map(UnitVo::getId).collect(Collectors.toList());
            useHouseDetailQueryParam.setUnitIds(unitIds);
            //查询符合条件的合同信息
            List<ContractVo> contractVos = this.contractService.useHouseDetailReport(useHouseDetailQueryParam);
            Map<Long, ContractVo> contractVoMap =new HashMap<>();
            if (CollectionUtil.isNotEmpty(contractVos)){
                 contractVoMap = contractVos.stream().collect(Collectors.toMap(ContractVo::getUnitId, Function.identity(),(k1,k2)->k1));
            }
             generateUseHouseDetailVo(unitVoPageVo.getContent(),contractVoMap,result);
        }
        return PageVo.create(unitVoPageVo.getCurrent(),unitVoPageVo.getPageSize(),unitVoPageVo.getTotal(),result);
    }

    private List<UseHouseDetailVo> generateUseHouseDetailVo(List<UnitVo> content, Map<Long, ContractVo> contractVoMap,List<UseHouseDetailVo> result) {
        if (CollectionUtil.isNotEmpty(content)){
            for (UnitVo unitVo:content){
                UseHouseDetailVo useHouseDetailVo=new UseHouseDetailVo();
                useHouseDetailVo.setBuildingName(unitVo.getBuildingName());

                useHouseDetailVo.setParkName(unitVo.getParkName());
                Park park = this.parkService.getById(unitVo.getParkId());
                useHouseDetailVo.setParkAddress(park.getAddress());
                useHouseDetailVo.setUnitArea(BigDecimal.valueOf(Double.valueOf(unitVo.getBuiltUpArea())));
                useHouseDetailVo.setUnitNames(unitVo.getUnitNo());
                useHouseDetailVo.setUnitUse(unitVo.getUseTypeLable());
                useHouseDetailVo.setUnitState("空闲中");
                //根据合同信息设置
                ContractVo contractVo = contractVoMap.get(unitVo.getId());
                if (contractVo != null){
                    useHouseDetailVo.setUnitState("租赁中");
                    useHouseDetailVo.setLeaseStartDate(contractVo.getLeaseStartDate());
                    String closeDate = contractVo.getEstimateCloseDate() == null ? contractVo.getDueDate() : contractVo.getEstimateCloseDate();
                    useHouseDetailVo.setDueDate(closeDate);
                    useHouseDetailVo.setEstName(contractVo.getEntName());
                    useHouseDetailVo.setRent(contractVo.getPrice());
                    //租期间隔
                    useHouseDetailVo.setTimeLimit(DateUtil.betweenMonth(DateUtil.parse(contractVo.getLeaseStartDate()),DateUtil.parse(closeDate),true)+1);
                }
                 result.add(useHouseDetailVo);
            }
        }
        return result;
    }

    /**
     * 查询可以导出的资产租赁的数据
     *
     * @param queryParam
     * @return key年月，value 当前月份的数据
     */
    private Map<Integer, List<LeaseReportVo>> generateExportLeaseReport(LeaseReportContractQueryParam queryParam) {
        Map<Integer, List<LeaseReportVo>> resultMap = new HashMap<>();
        //所以合同
        List<ContractVo> contractVos = this.contractService.findLeaseReportContractByParam(queryParam);
        if (CollectionUtil.isNotEmpty(contractVos)) {
            //按照企业分类
            Map<Long, List<ContractVo>> contractByEntIdMap = contractVos.stream().collect(Collectors.groupingBy(ContractCreateVo::getEntId));
            Iterator<Long> entIterator = contractByEntIdMap.keySet().iterator();
            while (entIterator.hasNext()) {
                //当前企业的所有合同
                List<ContractVo> contractForEnt = contractByEntIdMap.get(entIterator.next());
                //再按照园区区分
                Map<Long, List<ContractVo>> contractByParkIdMap = contractForEnt.stream().collect(Collectors.groupingBy(ContractCreateVo::getParkId));
                Iterator<Long> parkIterator = contractByParkIdMap.keySet().iterator();
                //再循环园区
                while (parkIterator.hasNext()) {
                    List<ContractVo> contractForPark = contractByParkIdMap.get(parkIterator.next());
                    //得到合同最近能够收租的时间
                    Date lastRentYearMonth = getLastRentYearMonth(contractForPark, queryParam);
                    //得到合同能够收租的最早时间
                    Date earlyRentYearMonth = getEarlyRentYearMonth(contractForPark, queryParam);
                    for (Date date = earlyRentYearMonth; date.compareTo(lastRentYearMonth) <= 0; date = DateUtil.offsetMonth(date, 1)) {
                        String yyyyMM = DateUtil.format(date, DatePattern.SIMPLE_MONTH_PATTERN);
                        //生成数据，组装条件
                        LeaseReportContractQueryParam generateCondition = new LeaseReportContractQueryParam();
                        //生成这个月数据的开始时间
                        generateCondition.setStartMonth(DateUtil.format(date, DatePattern.NORM_DATE_PATTERN));

                        //结束条件，当只算这一个月的租金，所以结束日期只能等于这个月最后一天，但是可能这个月中旬是合同的最后一天，所以让与合同判断取哪个时间
                        String currentMonthLastDay = DateUtil.format(date, DatePattern.NORM_MONTH_PATTERN) + "-" + DateUtil.lengthOfMonth(DateUtil.month(date) + 1, DateUtil.isLeapYear(DateUtil.year(date)));
                        LeaseReportContractQueryParam lastTime = new LeaseReportContractQueryParam();
                        lastTime.setEndMonth(currentMonthLastDay);
                        generateCondition.setEndMonth(DateUtil.format(getLastRentYearMonth(contractForPark, lastTime), DatePattern.NORM_DATE_PATTERN));
                        //只算月租金
                        generateCondition.setOnlyMonthRent("true");
                        LeaseReportVo leaseReportVo = generateLeaseReportVo(contractForPark, generateCondition);
                        //合同结束时间实际的结束时间
                        leaseReportVo.setDueDate(getDueDate(contractForPark));
                        List<LeaseReportVo> listForYearMonth = resultMap.get(Integer.valueOf(yyyyMM));
                        //如果当前年月的数据已经存在则添加进集合，否则就新建
                        if (CollectionUtil.isEmpty(listForYearMonth)) {
                            List<LeaseReportVo> list = new ArrayList<>();
                            listForYearMonth = list;
                            resultMap.put(Integer.valueOf(yyyyMM), list);
                        }
                        listForYearMonth.add(leaseReportVo);
                        //earlyRentYearMonth 可能不是1号，当第一次循环以后，重置date为每月1日
                        if (date.compareTo(earlyRentYearMonth) != 0) {
                            date = DateUtil.parse(DateUtil.format(date, DatePattern.NORM_MONTH_PATTERN) + "-1");

                        }
                    }
                }
            }
        }
        return resultMap;
    }

    /**
     * 可以收租收租最早的年月
     *
     * @param contractVos 合同
     * @param queryParam
     * @return
     */
    private Date getEarlyRentYearMonth(List<ContractVo> contractVos, LeaseReportContractQueryParam queryParam) {
        ContractVo contractVo = contractVos.stream().sorted(Comparator.comparing(ContractVo::getLeaseStartDate)).findFirst().get();
        // 合同租赁开始时间晚于或等于开始年月，取合同租赁开始时间
        Date earlyRentYearMonth = DateUtil.parse(contractVo.getLeaseStartDate()); //2021-1
        if (StrUtil.isNotBlank(queryParam.getStartMonth())) { //2020-12
            //合同开始时时间 小于 查询条件开始时间，则取开始时间
            if (DateUtil.parse(contractVo.getLeaseStartDate()).compareTo(DateUtil.parseDate(queryParam.getStartMonth())) < 0) {
                earlyRentYearMonth = DateUtil.parse(queryParam.getStartMonth());
            }
        }
        return earlyRentYearMonth;//2021-1
    }

    /**
     * 能够收租最晚的年月  会精确到某月的最后一天
     *
     * @param contractVos
     * @param queryParam
     * @return
     */
    private Date getLastRentYearMonth(List<ContractVo> contractVos, LeaseReportContractQueryParam queryParam) {
        //合同具体的关闭时间
        String contractCloseDate = getDueDate(contractVos);
        Date lastRentYearMonth = DateUtil.parse(contractCloseDate);
        if (StrUtil.isNotBlank(queryParam.getEndMonth())) {
            //得到这月的天数
            int thisMonthDay = DateUtil.lengthOfMonth(DateUtil.parse(queryParam.getEndMonth()).month() + 1, DateUtil.isLeapYear(DateUtil.parse(queryParam.getEndMonth()).year()));
            //contractCloseDate 大于 条件月份,结束月份要是这个月的最后一天
            queryParam.setEndMonth(DateUtil.year(DateUtil.parse(queryParam.getEndMonth())) + "-" + (DateUtil.month(DateUtil.parse(queryParam.getEndMonth())) + 1) + "-" + thisMonthDay);
            //合同结束时间，大于查询限制结束时间，则取查询结束时间为条件
            if (DateUtil.parse(contractCloseDate).compareTo(DateUtil.parseDate(queryParam.getEndMonth())) == 1) {
                lastRentYearMonth = DateUtil.parse(queryParam.getEndMonth());
            }
        }
        //收租最晚的时间不能大于这个月，下个月的房租下个月收
        if (lastRentYearMonth.compareTo(DateUtil.date()) == 1) {
            //只计算到，这个月的最后一天
            lastRentYearMonth = DateUtil.parse(DateUtil.format(DateUtil.date(), "yyyy-MM") + "-" + DateUtil.lengthOfMonth(DateUtil.thisMonth() + 1,
                    DateUtil.isLeapYear(DateUtil.thisYear())));
        }
        return lastRentYearMonth;
    }

    /**
     * 生成租金数据
     *
     * @param contractVos   需要生成租金数据合同
     * @param queryParam    生成租金数据的条件
     * @param leaseReportVo 值接收的数据
     */
    private void generateRent(List<ContractVo> contractVos, LeaseReportContractQueryParam queryParam, LeaseReportVo leaseReportVo) {
        if (CollectionUtil.isEmpty(contractVos)) {
            return;
        }
        // 月租金从每月1日开始计算，单位配置中的交租日只影响年租金，如配置每月15日，则每天月1日算月租金，15日算年租金
        SysConfig config = this.sysConfigService.getConfig(this.parkService.getById(contractVos.get(0).getParkId()).getEstId());
        //当前月租金
        BigDecimal mRent = new BigDecimal(0d);
        //当前年租金(时间区间的总租金)
        BigDecimal yRent = new BigDecimal(0d);
        if (config != null && config.getPaymentDate() != null) {
            // 每月收租日  当月收当月的租金
            int rentDay = Integer.parseInt(config.getPaymentDate());
            //遍历所有的合同，计算年租金，月租金
            for (int i = 0; i < contractVos.size(); i++) {
                ContractVo contractVo = contractVos.get(i);
                //当前合同的租金,租金为空，就不计算
                if (StrUtil.isBlank(contractVo.getRent())) {
                    continue;
                }
                //如果单价为空，或者价格等于0 ,则不进行计算
                if (contractVo.getPrice() == null || new BigDecimal(0d).compareTo(contractVo.getPrice()) == 0) {
                    continue;
                }
                //合同具体的关闭时间
                String contractCloseDate = StrUtil.isNotBlank(contractVo.getEstimateCloseDate()) ? contractVo.getEstimateCloseDate() : contractVo.getDueDate();

                // 收租最早的年月
                List<ContractVo> contractVos1 = new ArrayList<>();
                contractVos1.add(contractVo);
                Date earlyRentYearMonth = getEarlyRentYearMonth(contractVos1, queryParam);
                // 收租最晚的年月
                Date lastRentYearMonth = getLastRentYearMonth(contractVos1, queryParam);

                //从开始月开始算，算完一个月后+1， 然后继续算，最大不能超过最近的一个收租日
                for (Date date = earlyRentYearMonth; date.compareTo(lastRentYearMonth) < 0; date = DateUtil.offsetMonth(date, 1)) {
                    //如果是当前月，则计算月租金与年租金
                    if (DateUtil.year(date) == DateUtil.thisYear() && DateUtil.month(date) + 1 == DateUtil.thisMonth() + 1) {

                            //合同的结束日期，大于今天，算全月的租金
                            if (DateUtil.parse(contractCloseDate).compareTo(new Date()) == 1) {
                                //如果今天大于等于交租日，则年租金累加
                                if (DateUtil.thisDayOfMonth() >= rentDay) {
                                    yRent = NumberUtil.add(yRent, BigDecimal.valueOf(Double.valueOf(contractVo.getRent())));
                                }
                                //累加月租金
                                mRent =NumberUtil.add(BigDecimal.valueOf(Double.valueOf(contractVo.getRent())),mRent);
                            } else {
                                BigDecimal rent = calRent(contractVo, DateUtil.thisMonth() + 1);
                                mRent = NumberUtil.add(mRent, rent);
                                //如果今天大于等于交租日，则年租金累加
                                if (DateUtil.thisDayOfMonth() >= rentDay) {
                                    yRent = NumberUtil.add(yRent, rent);
                                }
                            }

                    } else if (date == earlyRentYearMonth && earlyRentYearMonth.compareTo(DateUtil.parse(contractVo.getLeaseStartDate())) == 0) {
                        //合同开始第一月，也要按照天计算
                        BigDecimal rent = calRent(contractVo, DateUtil.month(earlyRentYearMonth) + 1);
                        yRent = NumberUtil.add(yRent, rent);
                    } else if (DateUtil.year(earlyRentYearMonth) == DateUtil.parse(contractCloseDate).year()
                            && DateUtil.month(earlyRentYearMonth) == DateUtil.parse(contractCloseDate).month() + 1) {
                        //合同最后一个月，也要按照天计算
                        BigDecimal rent = calRent(contractVo, DateUtil.month(earlyRentYearMonth) + 1);
                        yRent = NumberUtil.add(yRent, rent);
                    } else {  //以前的月份只计算年租金
                        yRent = NumberUtil.add(yRent, BigDecimal.valueOf(Double.valueOf(contractVo.getRent())));
//                        log.error("yRent==="+yRent);
                    }
                    //earlyRentYearMonth 可能不是1号，当第一次循环以后，重置date为每月1日
                    if (date.compareTo(earlyRentYearMonth) != 0) {
                        date = DateUtil.parse(DateUtil.format(date, DatePattern.NORM_MONTH_PATTERN) + "-1");
                    }
                }
            }
        }
        // 只算月工资时，只有月工资有值，且年工资就是月工资
        if (StrUtil.isNotBlank(queryParam.getOnlyMonthRent())) {
            leaseReportVo.setMonthRent(yRent);
        } else {
            leaseReportVo.setMonthRent(mRent);
            leaseReportVo.setYearRent(yRent);
        }

    }

    /**
     * 取开始时间是最近的一份合同的结束时间
     *
     * @param contractVos
     * @return
     */
    private String getDueDate(List<ContractVo> contractVos) {
        //合同结束时间取最近一份合同的的时间，如果实际中止时间不为空则 取时间中止时间，否则取合同结束时间
        Optional<ContractVo> startFirst = contractVos.stream().sorted(Comparator.comparing(a -> DateUtil.parse(a.getLeaseStartDate()), Comparator.reverseOrder())).findFirst();
        if (startFirst.isPresent()) { //值为空false，否则为true
            if (StrUtil.isNotBlank(startFirst.get().getEstimateCloseDate())){
                return startFirst.get().getEstimateCloseDate();
            }else {
                return startFirst.get().getDueDate();
            }
        }
        return null;
    }
}
