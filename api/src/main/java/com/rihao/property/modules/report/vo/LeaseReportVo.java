package com.rihao.property.modules.report.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 *  资产租赁报表
 * </p>
 *
 * @author wangyu
 * @since 2021-03-31
 * @description
 */
@Data
@ApiModel
@Accessors(chain = true)
public class LeaseReportVo implements Serializable {

    @ApiModelProperty("园区名称")
    private String parkName;
    @ApiModelProperty("单元信息")
    private String unitNames;
    @ApiModelProperty("计量单位（平方米/辆/个/套）")
    private String calUnit;
    private Long entId;
    private Long parkId;
    @ApiModelProperty("承租企业")
    private String entName;
    @ApiModelProperty("主要用途")
    private String used;
    @ApiModelProperty("产权单位")
    private String establish;
    @ApiModelProperty("租赁开始日期")
    private String leaseStartDate;
    @ApiModelProperty("租赁结束日期")
    private String dueDate;
    @ApiModelProperty("招租方式")
    private String letType;
    @ApiModelProperty("合同编号")
    private String contractCode;
    @ApiModelProperty("单位租金（元/平方米/月）")
    private BigDecimal unitRent;
    @ApiModelProperty("建筑面积")
    private BigDecimal builtUpArea;
    @ApiModelProperty("月租金")
    private BigDecimal monthRent;
    @ApiModelProperty("年租金")
    private BigDecimal yearRent;
    @ApiModelProperty("年递增")
    private BigDecimal yearIncrease;
    @ApiModelProperty("已收取租金收入")
    private BigDecimal income;
    @ApiModelProperty("已上缴财政收入")
    private BigDecimal caizhenIncome;
    @ApiModelProperty("历年欠缴租金收入")
    private BigDecimal qianJiaoIncome;
    @ApiModelProperty("审计日止待缴账户余额")
    private BigDecimal balance;
    @ApiModelProperty("相关凭证号")
    private String voucherCode;


}