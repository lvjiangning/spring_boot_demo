package com.rihao.property.modules.estate.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.rihao.property.common.BaseEntity;

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
@TableName("tb_park_operator")
public class ParkOperator extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableField("operator_id")
    private Long operatorId;

    @TableField("park_id")
    private Long parkId;
}
