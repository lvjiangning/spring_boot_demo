package com.rihao.property.modules.system.vo;

import com.rihao.property.common.enums.Gender;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel("用户创建")
public class SysUserCreateVo {
    /**
     * 角色
     */
    @ApiModelProperty("角色Id")
    @NotNull
    private Long roleId;

    @ApiModelProperty("账号")
    @NotNull
    private String username;
    @ApiModelProperty("姓名")
    private String realName;

    @ApiModelProperty("性别")
    private Gender gender;

    @ApiModelProperty("联系方式")
    @NotNull
    private String telephone;

    private Long[] parkIdList;

    private Long establishId;

    private Long role;
}
