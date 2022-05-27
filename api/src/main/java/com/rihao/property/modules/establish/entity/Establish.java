package com.rihao.property.modules.establish.entity;

import com.rihao.property.common.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 单位信息表
 * </p>
 *
 * @author wangyu
 * @since 2021-04-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("tb_establish")
public class Establish extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 单位名称
     */
    @TableField("name")
    private String name;

    /**
     * 单位地址
     */
    @TableField("address")
    private String address;

    /**
     * 合同前缀
     */
    @TableField("contract_prefix")
    private String contractPrefix;
}
