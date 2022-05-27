package com.rihao.property.modules.cost.entity;

import com.rihao.property.common.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
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
 * @since 2021-03-30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("tb_cost")
public class Cost extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableField("ent_id")
    private Long entId;

    /**
     * 费用类型 1 房租 2 押金
     */
    @TableField("type_id")
    private Integer typeId;

    @TableField("contract_id")
    private Long contractId;

    /**
     * 费用状态 1 未缴纳 2 已缴纳 3 已延期
     */
    @TableField("status")
    private Integer status;

    /**
     * 费用金额
     */
    @TableField("amount")
    private BigDecimal amount;

    /**
     * 费用时间
     */
    @TableField("cost_time")
    private String costTime;

}
