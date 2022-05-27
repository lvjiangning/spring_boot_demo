package com.rihao.property.modules.system.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author gaoy
 * 2020/2/26/026
 */
@Data
@ApiModel
@Accessors(chain = true)
public class SysRoleUpdateVo extends SysRoleCreateVo {
    @ApiModelProperty("ID")
    private Long id;

}
