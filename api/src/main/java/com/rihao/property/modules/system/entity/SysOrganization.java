package com.rihao.property.modules.system.entity;

import com.rihao.property.common.TreeEntity;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 单位表
 * </p>
 *
 * @author ken
 * @since 2020-05-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("tb_sys_organization")
public class SysOrganization extends TreeEntity {

    private static final long serialVersionUID=1L;

    /**
     * 单位名称
     */
    @TableField("name")
    private String name;

    /**
     * 单位全称
     */
    @TableField("full_name")
    private String fullName;

    /**
     * 单位编码
     */
    @TableField("code")
    private String code;

    /**
     * 排序号
     */
    @TableField("orders")
    private Integer orders;

    /**
     * 单位类型 对应字典表tb_dict中的id
     */
    @TableField("type")
    private Long type;

    /**
     * 单位级别 对应字典表tb_dict中的id
     */
    @TableField("level")
    private Long level;

    /**
     * 建制人数
     */
    @TableField("establishment_number")
    private Integer establishmentNumber;

    /**
     * 邮政编码
     */
    @TableField("postal_code")
    private String postalCode;

    /**
     * 联系人
     */
    @TableField("contacts")
    private String contacts;

    /**
     * 联系电话
     */
    @TableField("phone")
    private String phone;

    /**
     * 通信地址
     */
    @TableField("address")
    private String address;

    /**
     * 第三方系统唯一标识
     */
    @TableField(value = "rid", fill = FieldFill.INSERT)
    private String rid;

    /**
     * 第三方系统返回id
     */
    @TableField("uid")
    private Long uid;

    /**
     * 是否删除 0否 1是
     */
    @TableField("deleted")
    @TableLogic
    private Integer deleted;
}
