package com.rihao.property.modules.estate.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.rihao.property.common.BaseEntity;
import com.rihao.property.modules.estate.enums.SplitAndMergeState;
import com.rihao.property.modules.estate.enums.UnitState;
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
@TableName("tb_unit")
//单元
public class Unit extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 片区
     */
    @TableField("area")
    private String area;

    /**
     * 地支号
     */
    @TableField("branch_no")
    private String branchNo;

    /**
     * 权利人名称
     */
    @TableField("right_holder_name")
    private String rightHolderName;

    /**
     * 楼栋名称、栋号
     */
    //@TableField("building_name")
    //private String buildingName;

    /**
     * 单元号
     */
    @TableField("unit_no")
    private String unitNo;

    /**
     * 使用面积
     */
    @TableField("usable_area")
    private BigDecimal usableArea;

    /**
     * 建筑面积
     */
    @TableField("built_up_area")
    private BigDecimal builtUpArea;

    /**
     * 套内建筑面积
     */
    @TableField("built_in_area")
    private BigDecimal builtInArea;

    /**
     * 公摊公用面积
     */
    @TableField("shared_public_area")
    private BigDecimal sharedPublicArea;

    /**
     * 用途（公用、自用、物业用房、租赁）
     */
    @TableField("use_type")
    private UnitUseType useType = UnitUseType.industry;

    ///**
    // * 租赁类型 配套租赁 产业租赁
    // */
    //@TableField("rent_type")
    //private RentType rentType;

    @TableField("building_id")
    private Long buildingId;

    @TableField("floor_id")
    private Long floorId;

    /**
     * 楼层号
     */
    @TableField("floor_no")
    private String floorNo;

    /**
     * 状态
     */
    @TableField("status")
    private UnitState status = UnitState.free;

    /**
     * 拆分状态
     */
    @TableField("split_and_merge_state")
    private SplitAndMergeState splitAndMergeState = SplitAndMergeState.original;

    //删除状态 1 已删除，0 正常
    @TableField("del_flag")
    private String delFlag;
}
