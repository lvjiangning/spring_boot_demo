

package com.rihao.property.shiro.service;

import com.rihao.property.shiro.jwt.JwtToken;
import com.rihao.property.shiro.param.LoginParam;
import com.rihao.property.shiro.vo.LoginSysUserTokenVo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 登录服务接口
 * </p>
 *
 * @author
 * @date 2019-05-23
 **/
public interface LoginService {

    /**
     * 登陆
     *
     * @param loginParam
     * @return
     * @throws Exception
     */
    LoginSysUserTokenVo login(LoginParam loginParam) throws Exception;

    /**
     * 如果(当前时间+倒计时) > 过期时间，则刷新token
     * 并更新缓存
     * 当前token失效，返回新token
     * 当前请求有效，返回状态码：460
     * 前端下次使用新token
     * 如果token继续发往后台，则提示，此token已失效，请使用新token，不在返回新token，返回状态码：461
     *
     * @param jwtToken
     * @param httpServletResponse
     */
    void refreshToken(JwtToken jwtToken, HttpServletResponse httpServletResponse) throws Exception;

    /**
     * 退出
     *
     * @param request
     */
    void logout(HttpServletRequest request) throws Exception;

}
