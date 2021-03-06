

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
 * ?????????????????????
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
        // ???????????????
        //checkVerifyCode(loginParam.getVerifyToken(), loginParam.getCode());

        // ???????????????????????????????????????
        SysUser sysUser = this.findUserByPatterns(loginParam.getUsername());
        if (sysUser == null) {
            log.error("????????????,loginParam:{}", loginParam);
            throw new AuthenticationException("????????????????????????");
        }
        if (SysUser.State.disabled == sysUser.getState()) {
            throw new AuthenticationException("???????????????");
        }

        String encryptPassword = PasswordHelper.encodePassword(loginParam.getPassword(), sysUser.getSalt());
        if (!encryptPassword.equals(sysUser.getPassword())) {
            throw new AuthenticationException("?????????????????????");
        }

        // ????????????????????????????????????????????????
        LoginSysUserVo loginSysUserVo = SysUserConvert.INSTANCE.sysUserToLoginSysUserVo(sysUser);
        if (loginParam.getUsername().equals("sys-admin")){
            //?????????????????????????????????
            loginSysUserVo.setLoginNumber(99);
        }
        //?????????????????????
        Establish byId = establishService.getById(loginSysUserVo.getEstablishId());
        if (byId != null && byId.getId() != null){
            loginSysUserVo.setEstablishName(byId.getName());
        }

        // ????????????????????????
        Long roleId = sysUser.getRoleId();

        SysRole sysRole = sysRoleService.getById(roleId);
        if (sysRole == null) {
            throw new AuthenticationException("???????????????");
        }
        if (EnableState.disable.equals(sysRole.getState())) {
            throw new AuthenticationException("???????????????");
        }

        loginSysUserVo.setRoleId(loginSysUserVo.getRoleId())
                .setRoleName(sysRole.getName());

        Set<String> permissionCodes;
        if (CommonConstant.DEFAULT_ADMIN_NAME.equals(loginParam.getUsername())) {
            // ?????????admin???????????????????????????
            permissionCodes = this.resourceService.getAllResource().stream().map(SysResource::getCode).collect(Collectors.toSet());
        }else {
            // ????????????????????????
            //permissionCodes = roleResourceService.getPressionCodesByRoleId(roleId);

            List<SysResource> resources = this.resourceService.getSysResourcesByUsername(loginParam.getUsername());

            if (CollectionUtils.isEmpty(resources)) {
                throw new AuthenticationException("????????????");
            }
            permissionCodes = resources.stream().map(SysResource::getCode).collect(Collectors.toSet());
            if (CollectionUtils.isEmpty(permissionCodes)) {
                throw new AuthenticationException("????????????");
            }

        }
        loginSysUserVo.setPermissionCodes(permissionCodes);
        String newSalt = jwtProperties.getSecret();

        if (jwtProperties.isSaltCheck()) {
            // ?????????????????????????????????
            newSalt = DigestUtils.sha256Hex(jwtProperties.getSecret() + sysUser.getSalt());
        }

        String username = sysUser.getUsername();

        // ??????token??????????????????
        Long expireSecond = jwtProperties.getExpireSecond();
        String token = JwtUtil.generateToken(username, newSalt, Duration.ofSeconds(expireSecond));
        log.debug("token:{}", token);

        // ??????AuthenticationToken
        JwtToken jwtToken = JwtToken.build(token, username, newSalt, expireSecond);
        // ???SecurityUtils?????????????????? subject
        Subject subject = SecurityUtils.getSubject();
        // ??????????????????
        subject.login(jwtToken);

        // ????????????????????????????????????????????????????????????????????????????????????token
        boolean singleLogin = jwtProperties.isSingleLogin();
        if (singleLogin) {
            loginRedisService.deleteUserAllCache(username);
        }

        // ?????????????????????Redis
        loginRedisService.cacheLoginInfo(jwtToken, loginSysUserVo);
        log.debug("????????????,username:{}", username);

        //?????????????????????????????????
        if (sysUser.getLoginNumber() != null && sysUser.getLoginNumber()  != 0){
            SysUser param=new SysUser();
            param.setId(sysUser.getId());
            param.setLoginNumber(sysUser.getLoginNumber()+1);
            sysUserService.updateById(param);
        }
        // ??????token???????????????????????????
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
        // ??????????????????token
        boolean isRefreshToken = jwtProperties.isRefreshToken();
        if (!isRefreshToken) {
            return;
        }
        // ??????????????????
        Date expireDate = JwtUtil.getExpireDate(token);
        // ???????????????
        Integer countdown = jwtProperties.getRefreshTokenCountdown();
        // ??????(????????????+?????????) > ????????????????????????token
        boolean refresh = DateUtils.addSeconds(new Date(), countdown).after(expireDate);

        if (!refresh) {
            return;
        }

        //??????token
        String username = jwtToken.getUsername();
        String salt = jwtToken.getSalt();
        Long expireSecond = jwtProperties.getExpireSecond();
        // ?????????token?????????
        String newToken = JwtUtil.generateToken(username, salt, Duration.ofSeconds(expireSecond));
        // ?????????JwtToken??????
        JwtToken newJwtToken = JwtToken.build(newToken, username, salt, expireSecond);
        // ??????redis??????
        loginRedisService.refreshLoginInfo(token, username, newJwtToken);
        log.debug("??????token????????????token:{}??????token:{}", token, newToken);
        // ???????????????
        // ??????token
        //httpServletResponse.setStatus(CommonConstant.JWT_REFRESH_TOKEN_CODE);
        httpServletResponse.setHeader(JwtTokenUtil.getTokenName(), newToken);

        HttpServletRequestUtil.getRequest().getSession()
                .setAttribute(JwtTokenUtil.getTokenName(), newToken);
    }

    @Override
    public void logout(HttpServletRequest request) throws Exception {
        Subject subject = SecurityUtils.getSubject();
        //??????
        subject.logout();
        // ??????token
        String token = JwtTokenUtil.getToken(request);
        String username = JwtUtil.getUsername(token);
        // ??????Redis????????????
        loginRedisService.deleteLoginInfo(token, username);
        log.info("????????????,username:{},token:{}", username, token);
    }

}
