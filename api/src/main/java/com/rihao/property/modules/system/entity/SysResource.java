package com.rihao.property.modules.system.entity;

import com.rihao.property.common.TreeEntity;
import com.rihao.property.common.enums.EnableState;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author gaoy
 * @since 2020-02-26
 */
@Data
@Accessors(chain = true)
@TableName("tb_sys_resource")
public class SysResource extends TreeEntity implements Serializable {

    public enum Type {
        menu, button, other
    }

    private static final long serialVersionUID = 1L;

    @TableField("name")
    private String name;

    @TableField("text")
    private String text;

    @TableField("icon")
    private String icon;

    @TableField("orders")
    private Integer orders;

    @TableField("code")
    private String code;

    @TableField("type")
    private Type type;

    @TableField("url")
    private String url;

    @TableField("state")
    private EnableState state;

}
