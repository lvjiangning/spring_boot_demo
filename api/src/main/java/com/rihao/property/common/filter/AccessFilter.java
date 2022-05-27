package com.rihao.property.common.filter;

import com.rihao.property.modules.system.entity.SysUser;
import com.rihao.property.shiro.util.JwtUtil;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 访问过滤器
 * 超级管理员只能发起get请求，不能发起post,put等其他请求
 */
@Component
public class AccessFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        if ("/api/login".equals(httpServletRequest.getRequestURI())
                || httpServletRequest.getRequestURI().startsWith("/api/admin")
        || "/api/uaa/role/kv".equals(httpServletRequest.getRequestURI())) {
            filterChain.doFilter(servletRequest, servletResponse);
        }else {
            SysUser currentUser = JwtUtil.getCurrentUser();
            if (currentUser != null && currentUser.getRoleId() == 1) {
                if ("get".equalsIgnoreCase(httpServletRequest.getMethod())) {
                    filterChain.doFilter(servletRequest, servletResponse);
                }else {
                    throw new RuntimeException("超管账号不允许操作数据！");
                }
            } else {
                filterChain.doFilter(servletRequest, servletResponse);
            }
        }
    }
}

