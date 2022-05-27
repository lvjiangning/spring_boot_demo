package com.rihao.property.modules.serial_number.entity;

import com.rihao.property.common.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.rihao.property.modules.serial_number.emuns.SerialType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDate;

/**
 * <p>
 *
 * </p>
 *
 * @author wangyu
 * @since 2021-04-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("tb_serial_number")
public class SerialNumber extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 类型：1、入驻批次 2 合同编号
     */
    @TableField("type_id")
    private SerialType typeId;

    /**
     * 当前值
     */
    @TableField("current_number")
    private String currentNumber;

    /**
     * 适用日期
     */
    @TableField("applicable_date")
    private LocalDate applicableDate;
}
