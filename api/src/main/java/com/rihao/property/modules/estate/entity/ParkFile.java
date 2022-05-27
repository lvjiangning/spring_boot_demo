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
 * @since 2021-06-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("tb_park_file")
public class ParkFile extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableField("name")
    private String name;

    @TableField("file_url")
    private String fileUrl;

    @TableField("park_id")
    private Long parkId;
}
