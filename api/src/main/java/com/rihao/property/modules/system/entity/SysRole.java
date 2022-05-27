package com.rihao.property.modules.system.entity;

import com.rihao.property.common.BaseEntity;
import com.rihao.property.common.enums.EnableState;
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
 * @author gaoy
 * @since 2020-02-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("tb_sys_role")
public class SysRole extends BaseEntity {

    private static final long serialVersionUID=1L;

    @TableField("name")
    private String name;

    @TableField("description")
    private String description;

    @TableField("state")
    private EnableState state = EnableState.enable;

}
