package com.rihao.property.modules.ent.entity;

import com.rihao.property.common.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.rihao.property.modules.config.entity.SysFile;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 *
 * </p>
 *
 * @author wangyu
 * @since 2021-03-29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("tb_ent_matters")
//企业事项
public class EntMatters extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 事项名称
     */
    @TableField("title")
    private String title;

    /**
     * 事项内容
     */
    @TableField("content")
    private String content;

    /**
     * 企业ID
     */
    @TableField("ent_id")
    private String entId;

    @TableField("happen_time")
    private String happenTime;

    @TableField("file_id")
    private Long fileId;
    @TableField(exist = false)
    private SysFile file;//前端显示
}
