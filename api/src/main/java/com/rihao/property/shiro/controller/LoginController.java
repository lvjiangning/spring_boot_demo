

package com.rihao.property.shiro.controller;

import com.anteng.boot.core.facade.code.BizCodeFace;
import com.anteng.boot.core.facade.code.ErrorCode;
import com.anteng.boot.core.facade.exception.BizException;
import com.anteng.boot.web.bind.response.ResBody;
import com.rihao.property.common.annotation.Log;
import com.rihao.property.modules.system.service.ISysUserService;
import com.rihao.property.shiro.param.LoginParam;
import com.rihao.property.shiro.service.LoginService;
import com.rihao.property.shiro.util.JwtTokenUtil;
import com.rihao.property.shiro.util.JwtUtil;
import com.rihao.property.shiro.vo.ChangeProfileVo;
import com.rihao.property.shiro.vo.ChangePwdVo;
import com.rihao.property.shiro.vo.CurrentUserVo;
import com.rihao.property.shiro.vo.LoginSysUserTokenVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * 登陆控制器
 *
 * @author
 * @date 2019-09-28
 * @since 1.3.0.RELEASE
 **/
@Slf4j
@RestController
@Api(value = "登录", tags = "登录管理")
public class LoginController {

    @Autowired
    private LoginService loginService;
    @Autowired
    private ISysUserService userService;

    @PostMapping("/api/login")
    @ApiOperation(value = "登陆", notes = "系统用户登陆", response = LoginSysUserTokenVo.class)
    public ResBody<LoginSysUserTokenVo> login(@Valid @RequestBody LoginParam loginParam, HttpServletResponse response) throws Exception {
        LoginSysUserTokenVo loginSysUserTokenVo = loginService.login(loginParam);
        // 设置token响应头
        response.setHeader(JwtTokenUtil.getTokenName(), loginSysUserTokenVo.getToken());
        //放入session方便调试
        //HttpServletRequestUtil.getRequest().getSession().setAttribute(JwtTokenUtil.getTokenName(), loginSysUserTokenVo.getToken());
        return ResBody.success(loginSysUserTokenVo).message("登陆成功");
    }

    @DeleteMapping("/api/logout")
    public ResBody<?> logout(HttpServletRequest request) throws Exception {
        loginService.logout(request);
        return ResBody.success().message("退出成功");
    }

    @GetMapping("/api/current-user")
    public ResBody<CurrentUserVo> getCurrentUser() {
        String username = JwtUtil.getUsername(JwtTokenUtil.getToken());
        CurrentUserVo currentUser = this.userService.getCurrentUserByUsername(username);
        return ResBody.success(currentUser);
    }

    @Log("修改密码")
    @PutMapping("/api/password")
    public ResBody<?> changePwd(@RequestBody ChangePwdVo changePwdVo) {
        String username = JwtUtil.getUsername(JwtTokenUtil.getToken());
        if (this.userService.changePassword(username, changePwdVo)) {
            return ResBody.success().message("操作成功");
        }
        throw new BizException(BizCodeFace.createBizCode(ErrorCode.FAIL).message("操作失败"));
    }

    @PutMapping("/api/profile")
    public ResBody<?> changProfile(@RequestBody ChangeProfileVo vo) {
        String username = JwtUtil.getUsername(JwtTokenUtil.getToken());
        if (this.userService.changProfile(username, vo)) {
            return ResBody.success().message("操作成功");
        }
        throw new BizException(BizCodeFace.createBizCode(ErrorCode.FAIL).message("操作失败"));
    }
}
