package com.rihao.property.modules.ent.entity;

import com.rihao.property.common.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 *
 * </p>
 *
 * @author wangyu
 * @since 2021-04-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("tb_ent_change_history_detail")
public class EntChangeHistoryDetail extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableField("change_id")
    private Long changeId;

    @TableField("filed_name")
    private String filedName;

    @TableField("before_value")
    private String beforeValue;

    @TableField("after_value")
    private String afterValue;
}
