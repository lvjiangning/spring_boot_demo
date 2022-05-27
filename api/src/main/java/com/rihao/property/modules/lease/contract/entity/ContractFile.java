package com.rihao.property.modules.lease.contract.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.rihao.property.common.BaseEntity;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableField;
import com.rihao.property.modules.lease.contract.enums.ContractFileType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 *
 * </p>
 *
 * @author wangyu
 * @since 2021-07-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("tb_contract_file")
public class ContractFile extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableField("contract_id")
    private Long contractId;

    @TableField("file_path")
    private String filePath;

    @TableField("file_id") //sysFile的id
    private Long fileId;

    @TableField("name")
    private String name;

    @TableField("uploader")
    private String uploader;

    @TableField("file_type")
    private ContractFileType fileType;

}
