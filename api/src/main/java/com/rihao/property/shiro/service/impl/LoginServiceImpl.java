

package com.rihao.property.shiro.service.impl;

import com.rihao.property.common.enums.EnableState;
import com.rihao.property.constant.CommonConstant;
import com.rihao.property.constant.RegexPatterns;
import com.rihao.property.modules.establish.entity.Establish;
import com.rihao.property.modules.establish.service.IEstablishService;
import com.rihao.property.modules.system.convert.SysUserConvert;
import com.rihao.property.modules.system.entity.SysResource;
import com.rihao.property.modules.system.entity.SysRole;
import com.rihao.property.modules.system.entity.SysUser;
import com.rihao.property.modules.system.service.ISysResourceService;
import com.rihao.property.modules.system.service.ISysRoleResourceService;
import com.rihao.property.modules.system.service.ISysRoleService;
import com.rihao.property.modules.system.service.ISysUserService;
import com.rihao.property.shiro.jwt.JwtProperties;
import com.rihao.property.shiro.jwt.JwtToken;
import com.rihao.property.shiro.param.LoginParam;
import com.rihao.property.shiro.service.LoginRedisService;
import com.rihao.property.shiro.service.LoginService;
import com.rihao.property.shiro.util.JwtTokenUtil;
import com.rihao.property.shiro.util.JwtUtil;
import com.rihao.property.shiro.vo.LoginSysUserTokenVo;
import com.rihao.property.shiro.vo.LoginSysUserVo;
import com.rihao.property.util.HttpServletRequestUtil;
import com.anteng.common.security.password.PasswordHelper;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 登录服务实现类
 * </p>
 *
 * @author
 * @date 2019-05-23
 **/
@Api
@Slf4j
@Service
public class LoginServiceImpl implements LoginService {

    @Lazy
    @Autowired
    private LoginRedisService loginRedisService;

    @Lazy
    @Autowired
    private JwtProperties jwtProperties;

    @Lazy
    @Autowired
    private ISysUserService sysUserService;

    @Lazy
    @Autowired
    private ISysRoleService sysRoleService;

    @Lazy
    @Autowired
    private ISysRoleResourceService roleResourceService;

    @Lazy
    @Autowired
    private ISysResourceService resourceService;
    @Lazy
    @Autowired
    private IEstablishService establishService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public LoginSysUserTokenVo login(LoginParam loginParam) throws Exception {
        // 校验验证码
        //checkVerifyCode(loginParam.getVerifyToken(), loginParam.getCode());

        // 从数据库中获取登陆用户信息
        SysUser sysUser = this.findUserByPatterns(loginParam.getUsername());
        if (sysUser == null) {
            log.error("登陆失败,loginParam:{}", loginParam);
            throw new AuthenticationException("用户名或密码错误");
        }
        if (SysUser.State.disabled == sysUser.getState()) {
            throw new AuthenticationException("账号已禁用");
        }

        String encryptPassword = PasswordHelper.encodePassword(loginParam.getPassword(), sysUser.getSalt());
        if (!encryptPassword.equals(sysUser.getPassword())) {
            throw new AuthenticationException("账号或密码错误");
        }

        // 将系统用户对象转换成登陆用户对象
        LoginSysUserVo loginSysUserVo = SysUserConvert.INSTANCE.sysUserToLoginSysUserVo(sysUser);
        if (loginParam.getUsername().equals("sys-admin")){
            //超级管理员不需要改密码
            loginSysUserVo.setLoginNumber(99);
        }
        //添加对应的单位
        Establish byId = establishService.getById(loginSysUserVo.getEstablishId());
        if (byId != null && byId.getId() != null){
            loginSysUserVo.setEstablishName(byId.getName());
        }

        // 获取当前用户角色
        Long roleId = sysUser.getRoleId();

        SysRole sysRole = sysRoleService.getById(roleId);
        if (sysRole == null) {
            throw new AuthenticationException("角色不存在");
        }
        if (EnableState.disable.equals(sysRole.getState())) {
            throw new AuthenticationException("角色已禁用");
        }

        loginSysUserVo.setRoleId(loginSysUserVo.getRoleId())
                .setRoleName(sysRole.getName());

        Set<String> permissionCodes;
        if (CommonConstant.DEFAULT_ADMIN_NAME.equals(loginParam.getUsername())) {
            // 如果是admin账号，获取所有权限
            permissionCodes = this.resourceService.getAllResource().stream().map(SysResource::getCode).collect(Collectors.toSet());
        }else {
            // 获取当前用户权限
            //permissionCodes = roleResourceService.getPressionCodesByRoleId(roleId);

            List<SysResource> resources = this.resourceService.getSysResourcesByUsername(loginParam.getUsername());

            if (CollectionUtils.isEmpty(resources)) {
                throw new AuthenticationException("无权访问");
            }
            permissionCodes = resources.stream().map(SysResource::getCode).collect(Collectors.toSet());
            if (CollectionUtils.isEmpty(permissionCodes)) {
                throw new AuthenticationException("无权访问");
            }

        }
        loginSysUserVo.setPermissionCodes(permissionCodes);
        String newSalt = jwtProperties.getSecret();

        if (jwtProperties.isSaltCheck()) {
            // 获取数据库中保存的盐值
            newSalt = DigestUtils.sha256Hex(jwtProperties.getSecret() + sysUser.getSalt());
        }

        String username = sysUser.getUsername();

        // 生成token字符串并返回
        Long expireSecond = jwtProperties.getExpireSecond();
        String token = JwtUtil.generateToken(username, newSalt, Duration.ofSeconds(expireSecond));
        log.debug("token:{}", token);

        // 创建AuthenticationToken
        JwtToken jwtToken = JwtToken.build(token, username, newSalt, expireSecond);
        // 从SecurityUtils里边创建一个 subject
        Subject subject = SecurityUtils.getSubject();
        // 执行认证登陆
        subject.login(jwtToken);

        // 判断是否启用单个用户登陆，如果是，这每个用户只有一个有效token
        boolean singleLogin = jwtProperties.isSingleLogin();
        if (singleLogin) {
            loginRedisService.deleteUserAllCache(username);
        }

        // 缓存登陆信息到Redis
        loginRedisService.cacheLoginInfo(jwtToken, loginSysUserVo);
        log.debug("登陆成功,username:{}", username);

        //登录成功后修改登录次数
        if (sysUser.getLoginNumber() != null && sysUser.getLoginNumber()  != 0){
            SysUser param=new SysUser();
            param.setId(sysUser.getId());
            param.setLoginNumber(sysUser.getLoginNumber()+1);
            sysUserService.updateById(param);
        }
        // 返回token和登陆用户信息对象
        LoginSysUserTokenVo loginSysUserTokenVo = new LoginSysUserTokenVo();
        loginSysUserTokenVo.setToken(token);
        loginSysUserTokenVo.setLoginSysUserVo(loginSysUserVo);
        return loginSysUserTokenVo;
    }

    private SysUser findUserByPatterns(String username) {
        if (username.matches(RegexPatterns.MOBILE_PHONE_NUMBER_PATTERN)) {
            SysUser user = getSysUserByTelephone(username);
            if (user != null) {
                return user;
            }
        }
        return this.sysUserService.getSysUserByUsername(username);
    }

    private SysUser getSysUserByTelephone(String telephone) {
        return this.sysUserService.getByTelephone(telephone);
    }

    @Override
    public void refreshToken(JwtToken jwtToken, HttpServletResponse httpServletResponse) throws Exception {
        if (jwtToken == null) {
            return;
        }
        String token = jwtToken.getToken();
        if (StringUtils.isBlank(token)) {
            return;
        }
        // 判断是否刷新token
        boolean isRefreshToken = jwtProperties.isRefreshToken();
        if (!isRefreshToken) {
            return;
        }
        // 获取过期时间
        Date expireDate = JwtUtil.getExpireDate(token);
        // 获取倒计时
        Integer countdown = jwtProperties.getRefreshTokenCountdown();
        // 如果(当前时间+倒计时) > 过期时间，则刷新token
        boolean refresh = DateUtils.addSeconds(new Date(), countdown).after(expireDate);

        if (!refresh) {
            return;
        }

        //刷新token
        String username = jwtToken.getUsername();
        String salt = jwtToken.getSalt();
        Long expireSecond = jwtProperties.getExpireSecond();
        // 生成新token字符串
        String newToken = JwtUtil.generateToken(username, salt, Duration.ofSeconds(expireSecond));
        // 生成新JwtToken对象
        JwtToken newJwtToken = JwtToken.build(newToken, username, salt, expireSecond);
        // 更新redis缓存
        loginRedisService.refreshLoginInfo(token, username, newJwtToken);
        log.debug("刷新token成功，原token:{}，新token:{}", token, newToken);
        // 设置响应头
        // 刷新token
        //httpServletResponse.setStatus(CommonConstant.JWT_REFRESH_TOKEN_CODE);
        httpServletResponse.setHeader(JwtTokenUtil.getTokenName(), newToken);

        HttpServletRequestUtil.getRequest().getSession()
                .setAttribute(JwtTokenUtil.getTokenName(), newToken);
    }

    @Override
    public void logout(HttpServletRequest request) throws Exception {
        Subject subject = SecurityUtils.getSubject();
        //注销
        subject.logout();
        // 获取token
        String token = JwtTokenUtil.getToken(request);
        String username = JwtUtil.getUsername(token);
        // 删除Redis缓存信息
        loginRedisService.deleteLoginInfo(token, username);
        log.info("登出成功,username:{},token:{}", username, token);
    }

}
