package com.rihao.property.modules.estate.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.rihao.property.common.BaseEntity;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableField;
import com.rihao.property.modules.estate.enums.SplitAndMergeState;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 *
 * </p>
 *
 * @author wangyu
 * @since 2021-06-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("tb_split_merge_history")
public class SplitMergeHistory extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableField("split_merge_state")
    private SplitAndMergeState splitMergeState;

    /**
     * 原先的单元ID列表
     */
    @TableField("orginal_unit_ids")
    private String orginalUnitIds;

    /**
     * 更改后的单元ID列表
     */
    @TableField("result_unit_ids")
    private String resultUnitIds;
}
