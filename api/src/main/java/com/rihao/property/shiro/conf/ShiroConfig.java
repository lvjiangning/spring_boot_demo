

package com.rihao.property.shiro.conf;

import com.alibaba.fastjson.JSON;
import com.rihao.property.shiro.filter.RequestPathFilter;
import com.rihao.property.shiro.jwt.JwtFilter;
import com.rihao.property.shiro.jwt.JwtProperties;
import com.rihao.property.shiro.jwt.JwtRealm;
import com.rihao.property.shiro.jwt.RetryLimitCredentialsMatcher;
import com.rihao.property.shiro.service.LoginRedisService;
import com.rihao.property.shiro.service.LoginService;
import com.rihao.property.util.IniUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.Authenticator;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.pam.FirstSuccessfulStrategy;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.mgt.SessionStorageEvaluator;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.mgt.DefaultWebSessionStorageEvaluator;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.DelegatingFilterProxy;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import java.util.*;

/**
 * Shiro配置
 * https://shiro.apache.org/spring.html
 * https://shiro.apache.org/spring-boot.html
 **/
@Slf4j
@Configuration
@EnableConfigurationProperties({
        ShiroProperties.class,
        AppFilterProperties.class
})
public class ShiroConfig {

    /**
     * JWT过滤器名称
     */
    private static final String JWT_FILTER_NAME = "jwtFilter";
    /**
     * 请求路径过滤器名称
     */
    private static final String REQUEST_PATH_FILTER_NAME = "path";
    /**
     * Shiro过滤器名称
     */
    private static final String SHIRO_FILTER_NAME = "shiroFilter";

    @Bean
    public CredentialsMatcher credentialsMatcher() {
        return new RetryLimitCredentialsMatcher();
    }

    /**
     * JWT数据源验证
     *
     * @return
     */
    @Bean
    public JwtRealm jwtRealm(LoginRedisService loginRedisService) {
        JwtRealm jwtRealm = new JwtRealm(loginRedisService);
        jwtRealm.setCachingEnabled(false);
        jwtRealm.setCredentialsMatcher(credentialsMatcher());
        return jwtRealm;
    }


    @Bean
    public SessionStorageEvaluator sessionStorageEvaluator() {
        DefaultSessionStorageEvaluator sessionStorageEvaluator = new DefaultWebSessionStorageEvaluator();
        sessionStorageEvaluator.setSessionStorageEnabled(false);
        return sessionStorageEvaluator;
    }

    @Bean
    public DefaultSubjectDAO subjectDAO() {
        DefaultSubjectDAO defaultSubjectDAO = new DefaultSubjectDAO();
        defaultSubjectDAO.setSessionStorageEvaluator(sessionStorageEvaluator());
        return defaultSubjectDAO;
    }

    /**
     * 安全管理器配置
     *
     * @return
     */
    @Bean
    public SecurityManager securityManager(LoginRedisService loginRedisService) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(jwtRealm(loginRedisService));
        securityManager.setSubjectDAO(subjectDAO());
        SecurityUtils.setSecurityManager(securityManager);
        return securityManager;
    }

    /**
     * ShiroFilterFactoryBean配置
     *
     * @param securityManager
     * @param loginRedisService
     * @param shiroProperties
     * @param jwtProperties
     * @return
     */
    @Bean(SHIRO_FILTER_NAME)
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager,
                                                         LoginService loginService,
                                                         LoginRedisService loginRedisService,
                                                         AppFilterProperties filterProperties,
                                                         ShiroProperties shiroProperties,
                                                         JwtProperties jwtProperties) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        Map<String, Filter> filterMap = getFilterMap(loginService, loginRedisService, filterProperties, jwtProperties);
        shiroFilterFactoryBean.setFilters(filterMap);
        Map<String, String> filterChainMap = getFilterChainDefinitionMap(shiroProperties);
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainMap);
        return shiroFilterFactoryBean;
    }


    /**
     * 获取filter map
     *
     * @return
     */
    private Map<String, Filter> getFilterMap(LoginService loginService,
                                             LoginRedisService loginRedisService,
                                             AppFilterProperties filterProperties,
                                             JwtProperties jwtProperties) {
        Map<String, Filter> filterMap = new LinkedHashMap<>();
        filterMap.put(REQUEST_PATH_FILTER_NAME, new RequestPathFilter(filterProperties.getRequestPath()));
        filterMap.put(JWT_FILTER_NAME, new JwtFilter(loginService, loginRedisService, jwtProperties));
        return filterMap;
    }


    /**
     * Shiro路径权限配置
     *
     * @return
     */
    private Map<String, String> getFilterChainDefinitionMap(ShiroProperties shiroProperties) {
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap();
        // 获取ini格式配置
        String definitions = shiroProperties.getFilterChainDefinitions();
        if (StringUtils.isNotBlank(definitions)) {
            Map<String, String> section = IniUtil.parseIni(definitions);
            log.debug("definitions:{}", JSON.toJSONString(section));
            for (Map.Entry<String, String> entry : section.entrySet()) {
                filterChainDefinitionMap.put(entry.getKey(), entry.getValue());
            }
        }

        // 获取自定义权限路径配置集合
        List<ShiroPermissionProperties> permissionConfigs = shiroProperties.getPermission();
        log.debug("permissionConfigs:{}", JSON.toJSONString(permissionConfigs));
        if (CollectionUtils.isNotEmpty(permissionConfigs)) {
            for (ShiroPermissionProperties permissionConfig : permissionConfigs) {
                String url = permissionConfig.getUrl();
                String[] urls = permissionConfig.getUrls();
                String permission = permissionConfig.getPermission();
                if (StringUtils.isBlank(url) && ArrayUtils.isEmpty(urls)) {
                    throw new IllegalArgumentException("shiro permission config 路径配置不能为空");
                }
                if (StringUtils.isBlank(permission)) {
                    throw new IllegalArgumentException("shiro permission config permission不能为空");
                }

                if (StringUtils.isNotBlank(url)) {
                    filterChainDefinitionMap.put(url, permission);
                }
                if (ArrayUtils.isNotEmpty(urls)) {
                    for (String string : urls) {
                        filterChainDefinitionMap.put(string, permission);
                    }
                }
            }
        }

        // 如果启用shiro，则设置最后一个设置为JWTFilter，否则全部路径放行
        if (shiroProperties.isEnable()) {
            filterChainDefinitionMap.put("/**", JWT_FILTER_NAME);
        } else {
            filterChainDefinitionMap.put("/**", "anon");
        }

        log.debug("filterChainMap:{}", JSON.toJSONString(filterChainDefinitionMap));

        // 添加默认的filter
        return addDefaultFilterDefinition(filterChainDefinitionMap);
    }

    /**
     * 添加默认的filter权限过滤
     *
     * @param filterChainDefinitionMap
     * @return
     */
    private Map<String, String> addDefaultFilterDefinition(Map<String, String> filterChainDefinitionMap) {
        if (MapUtils.isEmpty(filterChainDefinitionMap)) {
            return filterChainDefinitionMap;
        }
        final Map<String, String> map = new LinkedHashMap<>();
        for (Map.Entry<String, String> entry : filterChainDefinitionMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            String definition;
            if (value.contains(REQUEST_PATH_FILTER_NAME)) {
                definition = value;
            } else {
                String[] strings = value.split(",");
                List<String> list = new ArrayList<>();
                // 添加默认filter过滤
                list.add(REQUEST_PATH_FILTER_NAME);
                list.addAll(Arrays.asList(strings));
                definition = String.join(",", list);
            }
            map.put(key, definition);
        }
        return map;
    }

    /**
     * ShiroFilter配置
     *
     * @return
     */
    @Bean
    public FilterRegistrationBean<DelegatingFilterProxy> delegatingFilterProxy() {
        FilterRegistrationBean<DelegatingFilterProxy> filterRegistrationBean = new FilterRegistrationBean<>();
        DelegatingFilterProxy proxy = new DelegatingFilterProxy();
        proxy.setTargetFilterLifecycle(true);
        proxy.setTargetBeanName(SHIRO_FILTER_NAME);
        filterRegistrationBean.setFilter(proxy);
        filterRegistrationBean.setAsyncSupported(true);
        filterRegistrationBean.setEnabled(true);
        filterRegistrationBean.setDispatcherTypes(DispatcherType.REQUEST, DispatcherType.ASYNC);
        return filterRegistrationBean;
    }

    @Bean
    public Authenticator authenticator(LoginRedisService loginRedisService) {
        ModularRealmAuthenticator authenticator = new ModularRealmAuthenticator();
        authenticator.setRealms(Collections.singletonList(jwtRealm(loginRedisService)));
        authenticator.setAuthenticationStrategy(new FirstSuccessfulStrategy());
        return authenticator;
    }


    /**
     * Enabling Shiro Annotations
     *
     * @return
     */
    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

}
