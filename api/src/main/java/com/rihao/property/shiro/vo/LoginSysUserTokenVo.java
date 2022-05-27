

package com.rihao.property.shiro.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author
 * @date 2019-10-26
 **/
@Data
@Accessors(chain = true)
@ApiModel("登陆用户信息TokenVO")
public class LoginSysUserTokenVo implements Serializable {
    private static final long serialVersionUID = -4650803752566647697L;

    @ApiModelProperty("token")
    private String token;

    /**
     * 登陆用户对象
     */
    private LoginSysUserVo loginSysUserVo;
}
