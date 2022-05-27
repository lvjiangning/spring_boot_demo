package com.rihao.property.modules.inspection.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.rihao.property.common.BaseEntity;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableField;
import com.rihao.property.modules.config.emuns.SysFileType;
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
 * @since 2021-08-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("tb_inspection_file")
public class InspectionFile extends BaseEntity {

    private static final long serialVersionUID = 1L;


    @TableField("inspection_id")
    private Long inspectionId;

    @TableField("file_url")
    private String fileUrl;

    @TableField("name")
    private String name;

    @TableField("file_id")
    private Long fileId;
    @TableField(exist = false)
    private SysFile sysFile;
}
