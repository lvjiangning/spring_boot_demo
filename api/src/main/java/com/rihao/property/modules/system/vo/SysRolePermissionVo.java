package com.rihao.property.modules.system.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author gaoy
 * 2020/2/28/028
 */
@Data
@ApiModel("角色权限")
@Accessors(chain = true)
public class SysRolePermissionVo implements Serializable {
    @ApiModelProperty("角色ID")
    @NotNull
    private Long roleId;
    @ApiModelProperty("资源ID")
    private Long[] permissionIds;
}
