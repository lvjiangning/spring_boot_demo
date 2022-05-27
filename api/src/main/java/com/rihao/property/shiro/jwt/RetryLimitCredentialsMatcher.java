package com.rihao.property.shiro.jwt;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import javax.annotation.PostConstruct;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author gaoy
 * 2020/2/22/022
 */
public class RetryLimitCredentialsMatcher extends JwtCredentialsMatcher {
    public static final int MAX_RETRY = 5;
    private Cache passwordRetryCache;

    @Autowired
    private CacheManager cacheManager;

    @PostConstruct
    public void init() {
        passwordRetryCache = cacheManager.getCache("password-retry-cache");
    }

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        String principal = (String) token.getPrincipal();
        //retry count + 1
        AtomicInteger retryCount = passwordRetryCache.get(principal, AtomicInteger.class);
        if (retryCount == null) {
            retryCount = new AtomicInteger(0);
            passwordRetryCache.put(principal, retryCount);
        }
        if (retryCount.incrementAndGet() > MAX_RETRY) {
            //if retry count > 5 throw
            throw new ExcessiveAttemptsException(String.format("5分钟内连续失败%s次，请稍后重试", MAX_RETRY));
        }

        boolean matches = super.doCredentialsMatch(token, info);
        if (matches) {
            //clear retry count
            passwordRetryCache.evict(principal);
        }
        return matches;
    }
}
