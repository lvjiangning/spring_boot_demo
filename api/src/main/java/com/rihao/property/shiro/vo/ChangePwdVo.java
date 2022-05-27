package com.rihao.property.shiro.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author gaoy
 * 2020/2/29/029
 */
@Data
public class ChangePwdVo implements Serializable {
    private String oldPassword;
    private String newPassword;
}
