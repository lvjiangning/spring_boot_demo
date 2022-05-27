package com.rihao.property.modules.system.vo;

import com.anteng.boot.web.bind.view.annotation.Excel;
import com.rihao.property.common.enums.Gender;
import com.rihao.property.modules.system.entity.SysUser;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author gaoy
 * 2020/2/29/029
 */
@ApiModel
@Data
public class SysUserVo implements Serializable {
    @Excel(value = "用户编码", index = 0, width = 5000)
    private Long id;

    @ApiModelProperty("状态")
    private SysUser.State state;

    @ApiModelProperty("角色ID")
    private Long roleId;

    @ApiModelProperty("角色名称")
    @Excel(value = "用户角色", index = 2, width = 5000)
    private String roleName;

    @ApiModelProperty("账号")
    @Excel(value = "用户账号", index = 1, width = 5000)
    private String username;

    @ApiModelProperty("姓名")
    @Excel(value = "用户姓名", index = 3, width = 5000)
    private String realName;

    @ApiModelProperty("性别")
    private Gender gender;

    @ApiModelProperty("联系方式")
    @Excel(value = "联系方式", index = 4, width = 5000)
    private String telephone;

    @Excel(value = "创建时间", index = 5, width = 5000)
    private LocalDateTime createTime;
    @Excel(value = "最近访问时间", index = 6, width = 5000)
    private LocalDateTime lastLoginTime;

    private Long[] parkIdList;

    private String parkNameListDetail;

    private String establish;
    private Long establishId;
}
