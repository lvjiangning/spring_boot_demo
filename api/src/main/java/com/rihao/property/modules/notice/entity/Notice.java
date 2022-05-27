package com.rihao.property.modules.notice.entity;

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
 * @since 2021-03-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("tb_notice")
public class Notice extends BaseEntity {

    private static final long serialVersionUID=1L;

    @TableField("type")
    private Integer type;

    @TableField("title")
    private String title;

    @TableField("content")
    private String content;

}
