

package com.rihao.property.shiro.util;

import com.alibaba.fastjson.JSON;
import com.rihao.property.constant.CommonConstant;
import com.rihao.property.modules.estate.entity.Park;
import com.rihao.property.modules.estate.entity.ParkOperator;
import com.rihao.property.modules.estate.service.IParkOperatorService;
import com.rihao.property.modules.estate.service.IParkService;
import com.rihao.property.modules.system.entity.SysUser;
import com.rihao.property.modules.system.service.ISysUserService;
import com.rihao.property.shiro.jwt.JwtProperties;
import com.rihao.property.shiro.jwt.JwtToken;
import com.rihao.property.util.UUIDUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * JWT工具类
 * https://github.com/auth0/java-jwt
 *
 * @author
 * @date 2019-09-30
 * @since 1.3.0.RELEASE
 **/
@Slf4j
@Component
public class JwtUtil {

    private static JwtProperties jwtProperties;
    private static ISysUserService sysUserService;
    private static IParkOperatorService parkOperatorService;
    private static IParkService parkService;

    public JwtUtil(JwtProperties jwtProperties, ISysUserService userService, IParkOperatorService parkOperatorService,IParkService parkService) {
        JwtUtil.jwtProperties = jwtProperties;
        log.info(JSON.toJSONString(JwtUtil.jwtProperties));
        JwtUtil.sysUserService = userService;
        JwtUtil.parkOperatorService = parkOperatorService;
        JwtUtil.parkService=parkService;
    }

    /**
     * 生成JWT Token
     *
     * @param username       用户名
     * @param salt           盐值
     * @param expireDuration 过期时间和单位
     * @return token
     */
    public static String generateToken(String username, String salt, Duration expireDuration) {
        try {
            if (StringUtils.isBlank(username)) {
                log.error("username不能为空");
                return null;
            }
            log.debug("username:{}", username);

            // 如果盐值为空，则使用默认值：666666
            if (StringUtils.isBlank(salt)) {
                salt = jwtProperties.getSecret();
            }
            log.debug("salt:{}", salt);

            // 过期时间，单位：秒
            Long expireSecond;
            // 默认过期时间为1小时
            if (expireDuration == null) {
                expireSecond = jwtProperties.getExpireSecond();
            } else {
                expireSecond = expireDuration.getSeconds();
            }
            log.debug("expireSecond:{}", expireSecond);
            Date expireDate = DateUtils.addSeconds(new Date(), expireSecond.intValue());
            log.debug("expireDate:{}", expireDate);

            // 生成token
            Algorithm algorithm = Algorithm.HMAC256(salt);
            String token = JWT.create()
                    .withClaim(CommonConstant.JWT_USERNAME, username)
                    .withJWTId(UUIDUtil.get32UUID())            // jwt唯一id
                    .withIssuer(jwtProperties.getIssuer())      // 签发人
                    .withSubject(jwtProperties.getSubject())    // 主题
                    .withAudience(jwtProperties.getAudience())  // 签发的目标
                    .withIssuedAt(new Date())                   // 签名时间
                    .withExpiresAt(expireDate)                  // token过期时间
                    .sign(algorithm);                           // 签名
            return token;
        } catch (Exception e) {
            log.error("generateToken exception", e);
        }
        return null;
    }

    public static boolean verifyToken(String token, String salt) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(salt);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(jwtProperties.getIssuer())      // 签发人
                    .withSubject(jwtProperties.getSubject())    // 主题
                    .withAudience(jwtProperties.getAudience())  // 签发的目标
                    .build();
            DecodedJWT jwt = verifier.verify(token);
            if (jwt != null) {
                return true;
            }
        } catch (Exception e) {
            log.error("Verify Token Exception", e);
        }
        return false;
    }

    /**
     * 解析token，获取token数据
     *
     * @param token
     * @return
     */
    public static DecodedJWT getJwtInfo(String token) {
        DecodedJWT decodedJWT = JWT.decode(token);
        return decodedJWT;
    }

    /**
     * 获取用户名
     *
     * @param token
     * @return
     */
    public static String getUsername(String token) {
        if (StringUtils.isBlank(token)) {
            return null;
        }
        DecodedJWT decodedJWT = getJwtInfo(token);
        if (decodedJWT == null) {
            return null;
        }
        String username = decodedJWT.getClaim(CommonConstant.JWT_USERNAME).asString();
        return username;
    }

    public static String getUsername() {
        return getUsername(JwtTokenUtil.getToken());
    }

    /**
     * 当前登录用户
     */
    public static SysUser getCurrentUser() {
        JwtToken jwtToken = (JwtToken) SecurityUtils.getSubject().getPrincipal();
        String username = jwtToken.getUsername();
        return sysUserService.getSysUserByUsername(username);
    }

    /**
     * 获取创建时间
     *
     * @param token
     * @return
     */
    public static Date getIssuedAt(String token) {
        DecodedJWT decodedJWT = getJwtInfo(token);
        if (decodedJWT == null) {
            return null;
        }
        return decodedJWT.getIssuedAt();
    }

    /**
     * 获取过期时间
     *
     * @param token
     * @return
     */
    public static Date getExpireDate(String token) {
        DecodedJWT decodedJWT = getJwtInfo(token);
        if (decodedJWT == null) {
            return null;
        }
        return decodedJWT.getExpiresAt();
    }

    /**
     * 判断token是否已过期
     *
     * @param token
     * @return
     */
    public static boolean isExpired(String token) {
        Date expireDate = getExpireDate(token);
        if (expireDate == null) {
            return true;
        }
        return expireDate.before(new Date());
    }

    public static String getCurrentUserParkIds() {
        SysUser user = JwtUtil.getCurrentUser();
        List<ParkOperator> parkOperators;
        if (user.getRoleId() == 1) {
            //超级管理员有所有园区的查看权限
            List<Park> parks = parkService.list();
            if (CollectionUtils.isNotEmpty(parks)){
                List<Long> collect = parks.stream().map(park -> park.getId()).collect(Collectors.toList());
                String join = StringUtils.join(collect, ",");
                return join;
            }
            return "";
        } else {
            parkOperators = parkOperatorService.getByOperatorId(user.getId());
        }
        StringBuilder parkIds = new StringBuilder();
        for (ParkOperator parkOperator : parkOperators) {
            parkIds.append(parkOperator.getParkId()).append(",");
        }
        if (StringUtils.isNoneEmpty(parkIds)) {
            return parkIds.substring(0, parkIds.length() - 1);
        }
        return "";
    }
}
