package com.rihao.property.modules.lease.contract.controller.params;

import com.rihao.property.common.page.PageParams;
import com.rihao.property.modules.lease.contract.enums.ContractStatus;
import com.rihao.property.modules.lease.contract.enums.SignUpStatus;
import lombok.Data;

@Data
public class SettleQueryParam extends PageParams {

    private String code;
    private String entName;
    private Long parkId;
    private Long buildingId;
    private String startTime;
    private String endTime;
    private SignUpStatus status;
    private String parkIds;
}
