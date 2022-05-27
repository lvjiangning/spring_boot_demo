package com.rihao.property.modules.cost.vo;

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
 * @description
 * @since 2021-03-30
 */
@Data
@ApiModel
@Accessors(chain = true)
public class CostVo implements Serializable {

    private Long id;

    @ApiModelProperty("合同ID")
    private Long contractId;

    @ApiModelProperty("合同编号")
    private String code;

    @ApiModelProperty("类型ID 1 房租 2 管理费")
    private int typeId;

    @ApiModelProperty("企业名称")
    private String entName;

    @ApiModelProperty("租赁单位")
    private String unit;

    @ApiModelProperty("费用金额")
    private String amount;

    @ApiModelProperty("联系人")
    private String contact;

    @ApiModelProperty("联系人手机")
    private String contactPhoneNumber;

    @ApiModelProperty("费用时间")
    private String costTime;

    @ApiModelProperty("费用状态 1 未缴纳 2 已缴纳 3 已延期")
    private int status;

    private Long entId;

    private String area;
    private String price;
}