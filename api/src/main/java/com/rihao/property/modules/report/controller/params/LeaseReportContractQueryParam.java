package com.rihao.property.modules.report.controller.params;

import com.rihao.property.common.page.PageParams;
import com.rihao.property.modules.lease.contract.enums.ContractStatus;
import lombok.Data;

/**
 * 租赁报表合同查询
 */
@Data
public class LeaseReportContractQueryParam extends PageParams {

    //企业id
    private Long entId;
    //租赁企业名
    private String entName;
    //查询的园区
    private Long parkId;

   //区间
    //租赁开始时间 会有年月日
    private String startMonth;

    //租赁开始时间会有年月日
    private String endMonth;

    //当前登录人员 所能查看园区
    private String parkIds;

    private String onlyMonthRent; //租金生成条件，只算月租金，在导出明细功能时不为空


}
