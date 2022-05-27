package com.rihao.property.modules.system.entity;

import com.rihao.property.common.BaseEntity;
import com.rihao.property.common.enums.Gender;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

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
@TableName("tb_sys_user")
public class SysUser extends BaseEntity {

    public enum State {
        disabled, enable, locked
    }

    private static final long serialVersionUID = 1L;

    @TableField("username")
    private String username;

    @TableField("password")
    private String password;

    @TableField("salt")
    private String salt;

    /**
     * 角色 =1 为超级管理员
     */
    @TableField("role_id")
    private Long roleId;

    @TableField("gender")
    private Gender gender;

    @TableField("telephone")
    private String telephone;

    @TableField("state")
    private State state = State.enable;

    @TableField("real_name")
    private String realName;

    /**
     * 单位ID
     */
    @TableField("establish_id")
    private Long establishId;

    /**
     * 最后登录时间
     */
    @TableField("last_login_time")
    private LocalDateTime lastLoginTime;
    /**
     * 登录次数 空等于为登录
     */
    @TableField("login_number")
    private Integer loginNumber;

}
