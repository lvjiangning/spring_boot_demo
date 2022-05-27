package com.rihao.property.modules.estate.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.rihao.property.common.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.rihao.property.modules.estate.enums.RentType;
import com.rihao.property.modules.estate.enums.UnitUseType;
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
@TableName("tb_building")
public class Building extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 单位ID
     */
    @TableField("est_id")
    private Long estId;

    /**
     * 园区ID
     */
    @TableField("park_id")
    private Long parkId;

    /**
     * 楼栋名称
     */
    @TableField("name")
    private String name;

    /**
     * 楼层总数
     */
    @TableField("floor_amount")
    private Integer floorAmount;

    /**
     * 总面积
     */
    @TableField("total_area")
    private BigDecimal totalArea;

    /**
     * 占地面积
     */
    @TableField("covered_area")
    private BigDecimal coveredArea;

    /**
     * 总建筑面积
     */
    @TableField("built_up_area")
    private BigDecimal builtUpArea;

    /**
     * 总使用面积
     */
    @TableField("usable_area")
    private BigDecimal usableArea;


}
