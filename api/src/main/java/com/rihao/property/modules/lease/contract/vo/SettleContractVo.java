package com.rihao.property.modules.lease.contract.vo;

import com.rihao.property.modules.lease.contract.enums.BizType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Data
@ApiModel("新建变更合同")
public class SettleContractVo implements Serializable {
    private Long id;
    @NotNull
    @ApiModelProperty("企业ID")
    private Long entId;
    @ApiModelProperty("变更类型（2 减租 3 续租 4 退租 5 换租 6 调租 7 中止）")
    private BizType bizType;


    @ApiModelProperty("园区id")
    private Long parkId;
    @ApiModelProperty("楼栋IDs")
    private List<Long> buildingList;

    @ApiModelProperty("签订日期")
    private LocalDate signDate;

    @ApiModelProperty("起租时间")
    private LocalDate leaseStartDate;

    @ApiModelProperty("合同结束时间")
    private LocalDate dueDate;

    @ApiModelProperty("租期")
    private String leaseTerm;

    @ApiModelProperty("价格")
    private String price;

    @ApiModelProperty("单元")
    private Long[] unitIds;

    @ApiModelProperty("单元名称数组")
    private String[] unitNames;

    @ApiModelProperty("面积")
    private String area;

    @ApiModelProperty("单元字符串")
    private String unit;

    @ApiModelProperty("租金/月")
    private String rent;

    @ApiModelProperty("免租期（月）")
    private String rentFreePeriod;
    @ApiModelProperty("是否提交")
    private boolean submit = false;
}
