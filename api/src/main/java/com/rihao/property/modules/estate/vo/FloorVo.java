package com.rihao.property.modules.estate.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 视图实体
 * </p>
 *
 * @author wangyu
 * @description
 * @since 2021-03-28
 */
@Data
@ApiModel
@Accessors(chain = true)
public class FloorVo implements Serializable {

    private Long id;

    @ApiModelProperty("楼栋ID")
    private Long buildingId;

    @ApiModelProperty("楼号、楼栋名称")
    private String buildingName;

    @ApiModelProperty("单元数")
    private int unitAmount;

    @ApiModelProperty("总面积")
    private String totalArea;

    @ApiModelProperty("楼层数")
    private String floorNo;

    private List<UnitVo> children;

    private Long key;

    private int allUnitAmount;
    private int freeUnitAmount;
    private int busyUnitAmount;
    private String totalUsableArea;
    private String totalBuiltUpArea;
}