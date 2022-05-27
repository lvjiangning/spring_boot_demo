package com.rihao.property.constant;

/**
 * <p>
 * redis key 常量
 * </p>
 *
 * @author
 * @date 2019-05-23
 **/
public interface CommonRedisKey {

    /**
     * 登陆用户信息key
     */
    String LOGIN_USER = "login:user:%s";

    /**
     * 登陆用户盐值信息key
     */
    String LOGIN_SALT = "login:salt:%s";

    /**
     * 登陆用户username token
     */
    String LOGIN_USER_TOKEN = "login:user:token:%s:%s";

    /**
     * 验证码
     */
    String VERIFY_CODE = "verify.code:%s";

    /**
     * 一般性内容
     */
    String COMPANY_INTRO = "content:%s";
}
