package com.rihao.property.modules.inspection.entity;

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
@TableName("tb_inspection")
//巡查管理
public class Inspection extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableField("title")
    private String title;

    @TableField("content")
    private String content;

    @TableField("location")
    private String location;

    @TableField("inspector")
    private String inspector;

    @TableField("establish_id")
    private Long establishId;

    @TableField("park_id")
    private Long parkId;

    @TableField("inspection_time")
    private String inspectionTime;
}
