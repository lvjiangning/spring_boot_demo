package com.rihao.property.modules.estate.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.rihao.property.common.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * <p>
 *
 * </p>
 *
 * @author wangyu
 * @since 2021-03-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("tb_floor")
public class Floor extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableField("building_id")
    private Long buildingId;

    /**
     * 楼号、楼栋名称
     */
    @TableField("building_name")
    private String buildingName;

    /**
     * 单元数
     */
    @TableField("unit_amount")
    private String unitAmount;

    /**
     * 空置单元数
     */
    @TableField("free_unit_amount")
    private int freeUnitAmount;

    /**
     * 总面积
     */
    @TableField("total_area")
    private String totalArea;

    /**
     * 楼层数
     */
    @TableField("floor_no")
    private String floorNo;

    /**
     * 所有单元数目
     */
    @TableField("all_unit_amount")
    private int allUnitAmount;

    /**
     * 租赁中单元数目
     */
    @TableField("busy_unit_amount")
    private int busyUnitAmount;

    /**
     * 所有的使用面积
     */
    @TableField("total_usable_area")
    private BigDecimal totalUsableArea;

    /**
     * 所有的建筑面积
     */
    @TableField("total_built_up_area")
    private BigDecimal totalBuiltUpArea;
}
