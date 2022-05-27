package com.rihao.property.modules.report.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/***
 * 用房总账vo
 */
@Data
@ApiModel
@Accessors(chain = true)
public class UseHouseDetailVo implements Serializable {

    private  Long estId;// 单位名称

    @ApiModelProperty("园区名称")
    private String parkName;

    @ApiModelProperty("楼栋信息")
    private String buildingName;

    @ApiModelProperty("企业名称")
    private  String estName;//

    @ApiModelProperty("单元信息")
    private String unitNames;


    @ApiModelProperty("单元面积(m²)")
    private BigDecimal unitArea;

    @ApiModelProperty("房屋用途")
    private  String unitUse;//

    @ApiModelProperty("状态")
    private  String unitState;//

    @ApiModelProperty("租金(元/平方米/月)")
    private BigDecimal rent;

    @ApiModelProperty("租赁开始日期")
    private String leaseStartDate;
    @ApiModelProperty("租赁结束日期")
    private String dueDate;

    @ApiModelProperty("租赁期限(月)")
    private long timeLimit ;

    @ApiModelProperty("园区地址")
    private String parkAddress;

}
