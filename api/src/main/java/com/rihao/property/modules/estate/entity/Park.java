package com.rihao.property.modules.estate.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.rihao.property.common.BaseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 *
 * </p>
 *
 * @author wangyu
 * @since 2021-07-05
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("tb_park")
//园区对象
public class Park extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 单位ID
     */
    @TableField("est_id")
    private Long estId;

    @TableField("name")
    private String name;

    /**
     * 合同模版地址
     */
    @TableField("contract_temp_url")
    private String contractTempUrl;

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

    /**
     * 车位数
     */
    @TableField("parking_amount")
    private Integer parkingAmount;
    /**
     * 地址
     */
    @TableField("address")
    private String address;
    /**
     *  主要用途
     */
    @TableField("used")
    private String used;
    /**
     *  产权单位
     */
    @TableField("belong_unit")
    private String belongUnit;

}
