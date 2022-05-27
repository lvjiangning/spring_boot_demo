package com.rihao.property.modules.report.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 视图实体
 * </p>
 *
 * @author wangyu
 * @since 2021-03-31
 * @description
 */
@Data
@ApiModel
@Accessors(chain = true)
public class BuildingLedgerVo implements Serializable {

    @ApiModelProperty("楼栋ID")
    private Long buildingId;
    @ApiModelProperty("楼栋名称")
    private String buildingName;
    @ApiModelProperty("合同编号")
    private String contractCode;
    @ApiModelProperty("企业名称")
    private String entName;
    @ApiModelProperty("联系人")
    private String contact;
    @ApiModelProperty("联系电话")
    private String contactPhonenumber;
    @ApiModelProperty("入驻时间")
    private String settleDate;
    @ApiModelProperty("房租")
    private String rent;
    @ApiModelProperty("起始时间")
    private String contractStartEndData;
    @ApiModelProperty("租期")
    private String leaseTerm;
    @ApiModelProperty("最早入驻日期")
    private String earliestSettleDate;
    @ApiModelProperty("合同状态")
    private String contractStatus;
}