

package com.rihao.property.shiro.vo;

import com.rihao.property.common.enums.Gender;
import com.rihao.property.modules.system.entity.SysUser;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Set;

/**
 * <p>
 * 登录用户对象，响应给前端
 * </p>
 *
 * @author
 * @date 2019-05-15
 **/
@Data
@Accessors(chain = true)
public class LoginSysUserVo implements Serializable {

    private static final long serialVersionUID = -1758338570596088158L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "联系电话")
    private String telephone;

    @ApiModelProperty(value = "性别")
    private Gender gender;

    @ApiModelProperty(value = "状态")
    private SysUser.State state;

    @ApiModelProperty("角色id")
    private Long roleId;

    @ApiModelProperty("角色名称")
    private String roleName;

    @ApiModelProperty("权限编码列表")
    private Set<String> permissionCodes;

    @ApiModelProperty("单位名称")
    private String establishName;

    @ApiModelProperty("单位id")
    private Long establishId;

    @ApiModelProperty("登录次数")
    private Integer loginNumber; // 0 或者空第一次登录，需要改密码
}
