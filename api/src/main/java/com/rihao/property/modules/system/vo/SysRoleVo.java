package com.rihao.property.modules.system.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @author gaoy
 * 2020/2/26/026
 */
@Data
@Accessors(chain = true)
@ApiModel
public class SysRoleVo extends SysRoleUpdateVo {
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

}
