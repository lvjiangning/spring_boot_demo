package com.rihao.property.modules.lease.contract.vo;

import com.rihao.property.modules.estate.entity.Building;
import com.rihao.property.modules.estate.entity.Unit;
import com.rihao.property.modules.lease.contract.enums.BizType;
import com.rihao.property.modules.lease.contract.enums.SignUpStatus;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@ApiModel("租赁处理对象")
@Data
public class ContractSettleVo {
    private Long id;
    private String code;
    private String entName;
    private Long entId;
    private String parkName;
    private Long parkId;
    private String rent;
    private String area;
    private String signDate;
    private String leaseStartDate;
    private String dueDate;
    private BizType bizType;
    private SignUpStatus signUpStatus;
    private List<ContractUnitVo> units;
    private List<Unit> unitList;
    private List<Building> buildingList;
    private String createBy;
    private LocalDate createTime;
}
