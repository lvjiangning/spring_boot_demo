package com.rihao.property.modules.report.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

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
public class UnitLedgerVo implements Serializable {

    @ApiModelProperty("楼栋ID")
    private Long buildingId;
    @ApiModelProperty("楼栋名称")
    private String buildingName;
    @ApiModelProperty("单元号码")
    private String unit;
    @ApiModelProperty("面积")
    private BigDecimal area;
    @ApiModelProperty("企业名称")
    private String entName;
    @ApiModelProperty("入驻时间")
    private String settleDate;
    @ApiModelProperty("到期时间")
    private String dueDate;
    @ApiModelProperty("结束时间")
    private String endDate;
    @ApiModelProperty("租金")
    private String rent;
    @ApiModelProperty("状态")
    private String status;
}