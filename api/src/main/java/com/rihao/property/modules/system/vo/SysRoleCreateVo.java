package com.rihao.property.modules.system.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
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
@ApiModel
@Accessors(chain = true)
public class SysRoleCreateVo implements Serializable {
    @ApiModelProperty("角色名")
    @NotNull
    private String name;

    @ApiModelProperty("角色描述")
    private String description;
}
