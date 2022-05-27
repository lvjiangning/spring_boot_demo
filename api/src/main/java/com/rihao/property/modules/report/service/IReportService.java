package com.rihao.property.modules.report.service;

import com.rihao.property.common.page.PageVo;
import com.rihao.property.modules.report.controller.params.LeaseReportContractQueryParam;
import com.rihao.property.modules.report.controller.params.LedgerReportBaseQueryParam;
import com.rihao.property.modules.report.controller.params.LandUseReportBaseQueryParam;
import com.rihao.property.modules.report.controller.params.UseHouseDetailQueryParam;
import com.rihao.property.modules.report.vo.*;

import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wangyu
 * @since 2021-03-31
 */
public interface IReportService {

    PageVo<LandUseReportVo> landUseReport(LandUseReportBaseQueryParam landUseReportQueryParam) throws ParseException;

    PageVo<BuildingLedgerVo> buildingLedger(LedgerReportBaseQueryParam ledgerReportQueryParamx);

    PageVo<UnitLedgerVo> unitLedger(LedgerReportBaseQueryParam ledgerReportQueryParam);

    /**
     * 资产租赁报表
     * 根据合同维度分类计算
     * @param leaseReportContractQueryParam
     * @return
     */
    PageVo<LeaseReportVo> leaseReport(LeaseReportContractQueryParam leaseReportContractQueryParam);

    /**
     * 用房台账
     * 1、查询所有园区
     * 2、通过园区汇总各个面积，与企业数
     * @return
     */
    List<UseHouseLedgerVo> useHouseLedgerReport();

    /**
     * 导出资产报表xml按照月份区分
     * @param leaseReportContractQueryParam
     */
    void leaseReportExport(LeaseReportContractQueryParam leaseReportContractQueryParam,HttpServletResponse response);

    /**
     * 导出用房台账
     * @param response
     */
    void useHouseLedgerReportExport(HttpServletResponse response);

    /**
     * 用房明细列表
     * @param useHouseDetailQueryParam
     * @return
     */
    PageVo<UseHouseDetailVo> useHouseDetailReport(UseHouseDetailQueryParam useHouseDetailQueryParam);

    void useHouseDetailReportExport(UseHouseDetailQueryParam useHouseDetailQueryParam, HttpServletResponse response);
}
