package com.rihao.property.modules.system.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @author gaoy
 * 2020/2/29/029
 */
@ApiModel
@Data
public class SysUserUpdateVo extends SysUserCreateVo {
    private Long id;
}
