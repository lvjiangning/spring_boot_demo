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
 * @since 2021-07-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("tb_purchase_record_file")
public class PurchaseRecordFile extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableField("name")
    private String name;

    @TableField("file_url")
    private String fileUrl;

    @TableField("record_id")
    private Long recordId;
}
