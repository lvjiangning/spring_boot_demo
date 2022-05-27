package com.rihao.property.modules.lease.contract.controller.params;

import com.rihao.property.common.page.PageParams;
import com.rihao.property.modules.lease.contract.enums.BizType;
import com.rihao.property.modules.lease.contract.enums.ContractStatus;
import lombok.Data;

@Data
public class ContractQueryParam extends PageParams {

    private String code;
    private String entName;
    private Long parkId;
    private Long buildingId;
    //租赁开始时间
    private String leaseStartDate;
    //结束时间
    private String dueDate;
    private ContractStatus status;

    //过期前多久开始预警
    private String expirationMonth;
    private String entId;

    private String parkIds;

    /**
     * 模糊查询参数
     */
    private String likeParam;

}
