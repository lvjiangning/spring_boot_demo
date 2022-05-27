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
@ApiModel("用户授权组织")
@Accessors(chain = true)
public class SysUserOrganizationVo implements Serializable {

    @ApiModelProperty("用户ID")
    @NotNull
    private Long userId;

    @ApiModelProperty("组织ID")
    private Long[] organizationIds;
}
