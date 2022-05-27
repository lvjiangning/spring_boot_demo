package com.rihao.property.modules.report.controller;


import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.anteng.boot.web.BaseController;
import com.anteng.boot.web.bind.response.ResBody;
import com.rihao.property.common.annotation.Log;
import com.rihao.property.common.page.PageVo;
import com.rihao.property.modules.report.controller.params.LeaseReportContractQueryParam;
import com.rihao.property.modules.report.controller.params.LedgerReportBaseQueryParam;
import com.rihao.property.modules.report.controller.params.LandUseReportBaseQueryParam;
import com.rihao.property.modules.report.controller.params.UseHouseDetailQueryParam;
import com.rihao.property.modules.report.service.IReportService;
import com.rihao.property.modules.report.vo.*;
import com.rihao.property.shiro.util.JwtUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author wangyu
 * @since 2021-03-31
 */
@RestController
@RequestMapping("/api/report")
public class ReportController extends BaseController {

    private final IReportService reportService;

    public ReportController(IReportService reportService) {
        this.reportService = reportService;
    }

    @Log("用地情况报表")
    @GetMapping("land_use")
    @ApiOperation("用地情况报表")
    public ResBody<?> landUseReport(LandUseReportBaseQueryParam landUseReportQueryParam) throws ParseException {
        PageVo<LandUseReportVo> page = this.reportService.landUseReport(landUseReportQueryParam);
        addLog("查询用地情况报表", JwtUtil.getCurrentUser().getRealName());
        return ResBody.success(page);
    }

    @Log("楼栋台账")
    @GetMapping("/ledger/building")
    @ApiOperation("楼栋台账")
    public ResBody<?> ledgerBuilding(LedgerReportBaseQueryParam ledgerReportQueryParam) {
        PageVo<BuildingLedgerVo> buildingLedgerVoPageVo = this.reportService.buildingLedger(ledgerReportQueryParam);
        return ResBody.success(buildingLedgerVoPageVo);
    }

    @Log("单元台账")
    @GetMapping("/ledger/unit")
    @ApiOperation("单元台账")
    public ResBody<?> ledgerUnit(LedgerReportBaseQueryParam ledgerReportQueryParam) {
        PageVo<UnitLedgerVo> unitLedgerVoPageVo = this.reportService.unitLedger(ledgerReportQueryParam);
        return ResBody.success(unitLedgerVoPageVo);
    }

    @Log("资产租赁报表")
    @GetMapping("leaseReport")
    @ApiOperation("资产租赁报表")
    public ResBody<?> leaseReport(LeaseReportContractQueryParam leaseReportContractQueryParam) throws ParseException {
        //处理参数
        //初始化为查询月份第一天
        if (StrUtil.isNotBlank(leaseReportContractQueryParam.getStartMonth())){
            leaseReportContractQueryParam.setStartMonth(leaseReportContractQueryParam.getStartMonth()+"-1");
        }
        if (StrUtil.isNotBlank(leaseReportContractQueryParam.getEndMonth())){
            String s = leaseReportContractQueryParam.getEndMonth() + "-1";
            int lengthOfMonth = DateUtil.lengthOfMonth(DateUtil.parse(s).month() + 1, DateUtil.isLeapYear(DateUtil.parse(s).year()));
            leaseReportContractQueryParam.setEndMonth(leaseReportContractQueryParam.getEndMonth()+"-"+lengthOfMonth);
        }
        PageVo<LeaseReportVo> page = this.reportService.leaseReport(leaseReportContractQueryParam);
        addLog("资产租赁报表", JwtUtil.getCurrentUser().getRealName());
        return ResBody.success(page);
    }

    @Log("资产租赁报表导出")
    @GetMapping("leaseReportExport")
    @ApiOperation("资产租赁报表导出")
    public void leaseReportExport(LeaseReportContractQueryParam leaseReportContractQueryParam, HttpServletResponse response) throws ParseException {
        //处理参数
        //初始化为查询月份第一天
        if (StrUtil.isNotBlank(leaseReportContractQueryParam.getStartMonth())){
            leaseReportContractQueryParam.setStartMonth(leaseReportContractQueryParam.getStartMonth()+"-1");
        }
        if (StrUtil.isNotBlank(leaseReportContractQueryParam.getEndMonth())){
            String s = leaseReportContractQueryParam.getEndMonth() + "-1";
            int lengthOfMonth = DateUtil.lengthOfMonth(DateUtil.parse(s).month() + 1, DateUtil.isLeapYear(DateUtil.parse(s).year()));
            leaseReportContractQueryParam.setEndMonth(leaseReportContractQueryParam.getEndMonth()+"-"+lengthOfMonth);
        }
        this.reportService.leaseReportExport(leaseReportContractQueryParam, response);
        addLog("资产租赁报表导出", JwtUtil.getCurrentUser().getRealName());

    }

    @Log("用房台账")
    @GetMapping("useHouseLedgerReport")
    @ApiOperation("用房台账")
    public ResBody<UseHouseLedgerVo> useHouseLedgerReport() throws ParseException {
        List<UseHouseLedgerVo> list = this.reportService.useHouseLedgerReport();
        addLog("用房台账", JwtUtil.getCurrentUser().getRealName());
        return ResBody.success(list);
    }

    @Log("用房台账导出")
    @GetMapping("useHouseLedgerReportExport")
    @ApiOperation("用房台账导出")
    public void useHouseLedgerReportExport(HttpServletResponse response) throws ParseException {
       this.reportService.useHouseLedgerReportExport(response);
       addLog("用房台账导出", JwtUtil.getCurrentUser().getRealName());
    }


    @Log("用房明细")
    @GetMapping("useHouseDetailReport")
    @ApiOperation("用房明细")
    public ResBody<UseHouseDetailVo> useHouseDetailReport(UseHouseDetailQueryParam useHouseDetailQueryParam) throws ParseException {
        PageVo<UseHouseDetailVo> pageVo = this.reportService.useHouseDetailReport(useHouseDetailQueryParam);
        addLog("用房明细", JwtUtil.getCurrentUser().getRealName());
        return ResBody.success(pageVo);
    }

    @Log("用房明细导出")
    @GetMapping("useHouseDetailReportExport")
    @ApiOperation("用房明细导出")
    public void useHouseDetailReportExport(UseHouseDetailQueryParam useHouseDetailQueryParam,HttpServletResponse response) throws ParseException {
        this.reportService.useHouseDetailReportExport(useHouseDetailQueryParam,response);
        addLog("用房明细导出", JwtUtil.getCurrentUser().getRealName());
    }
}
