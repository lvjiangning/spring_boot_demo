package com.rihao.property.modules.ent.entity;

import com.rihao.property.common.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.rihao.property.modules.config.entity.SysFile;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

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
@TableName("tb_ent_used_name")
public class EntUsedName extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 企业ID
     */
    @TableField("ent_id")
    private Long entId;

    /**
     * 曾用名
     */
    @TableField("used_name")
    private String usedName;

    /**
     * 新名称
     */
    @TableField("new_name")
    private String newName;

    @TableField("file_path")
    private String filePath;
    @TableField(exist = false)
    private List<SysFile> files;
}
