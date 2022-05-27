package com.rihao.property.modules.system.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * @author Ken
 * @date 2020/5/18
 * @description
 */
@Data
@ApiModel
@Accessors(chain = true)
public class SysOrganizationVo extends SysOrganizationCreateVo {

    @NotNull
    @ApiModelProperty("ID")
    private Long id;

}
