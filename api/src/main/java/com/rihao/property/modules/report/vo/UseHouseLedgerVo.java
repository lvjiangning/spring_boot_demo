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
public class UseHouseLedgerVo implements Serializable {

    private  Long estId;// 单位名称

    @ApiModelProperty("单位名称")
    private  String estName;// 单位名称

    @ApiModelProperty("园区名称")
    private String parkName;

    @ApiModelProperty("园区地址")
    private String address;


    @ApiModelProperty("园区建筑面积")
    private BigDecimal parkBuildArea;

    @ApiModelProperty("园区使用面积")
    private BigDecimal usableArea;

    @ApiModelProperty("已租/售面积")
    private BigDecimal rentArea;

    @ApiModelProperty("剩余面积")
    private BigDecimal surplusArea;

    @ApiModelProperty("入驻单位数")
    private Integer entSum;

    @ApiModelProperty("主要用途")
    private String used;

}
