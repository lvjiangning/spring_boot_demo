package com.rihao.property.modules.system.entity;

import com.rihao.property.common.TreeEntity;
import com.rihao.property.common.enums.EnableState;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 数据字典表
 * </p>
 *
 * @author ken
 * @since 2020-05-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("tb_sys_dict")
public class SysDict extends TreeEntity {

    private static final long serialVersionUID=1L;

    /**
     * 字典名称
     */
    @TableField("name")
    private String name;

    /**
     * 类型。0：字典类别；1：字典项
     */
    @TableField("type")
    private Integer type;

    /**
     * 字典编码
     */
    @TableField("code")
    private String code;

    /**
     * 字典项值
     */
    @TableField("value")
    private String value;

    /**
     * 描述
     */
    @TableField("description")
    private String description;

    /**
     * 排序号
     */
    @TableField("orders")
    private Integer orders;

    /**
     * 状态
     */
    @TableField("state")
    private EnableState state = EnableState.enable;


}
