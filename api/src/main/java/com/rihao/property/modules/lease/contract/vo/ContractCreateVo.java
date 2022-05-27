package com.rihao.property.modules.lease.contract.vo;

import com.rihao.property.modules.close_history.enums.SettleTypeEnum;
import com.rihao.property.modules.estate.entity.Building;
import com.rihao.property.modules.estate.entity.Unit;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class ContractCreateVo implements Serializable {

    @ApiModelProperty("合同编号")
    private String code;

    @ApiModelProperty("签订类型")
    private String changeType;

    @ApiModelProperty("租赁公司ID")
    private Long entId;

    @ApiModelProperty("园区ID")
    private Long parkId;
    @ApiModelProperty("楼栋IDs")
    private List<Long> buildingIdList;

    @ApiModelProperty("楼栋ids拼接的字符串,数据库")
    private String buildingIds;

    @ApiModelProperty("联系人")
    private String contact;

    @ApiModelProperty("联系人电话")
    private String contactPhoneNumber;

    @ApiModelProperty("法人")
    private String legal;

    @ApiModelProperty("法人电话")
    private String legalPhoneNumber;

    @ApiModelProperty("签订日期")
    private String signDate;

    @ApiModelProperty("起租时间")
    private String leaseStartDate;

    @ApiModelProperty("合同结束时间")
    private String dueDate;

    @ApiModelProperty("租期")
    private String leaseTerm;

    @ApiModelProperty("价格")
    private BigDecimal price;

    @ApiModelProperty("单元")
    private Long[] unitIds;

    @ApiModelProperty("楼栋名称数组")
    private List buildingNames;

    @ApiModelProperty("单元名称数组")
    private List unitNames;

    @ApiModelProperty("楼栋")
    private List<Building> builds;

    @ApiModelProperty("单元")
    private List<Unit> units;

    @ApiModelProperty("面积")
    private String area;

    @ApiModelProperty("租金/月")
    private String rent;

    @ApiModelProperty("免租期（月）")
    private String rentFreePeriod;

    private String infoComplete;
}
