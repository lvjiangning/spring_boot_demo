

package com.rihao.property.shiro.filter;

import com.anteng.boot.web.bind.request.BufferRequest;
import com.rihao.property.shiro.conf.AppFilterProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;

/**
 * 请求路径过滤器
 *
 * @author
 * @date 2018-11-08
 */
@Slf4j
public class RequestPathFilter implements Filter {

    private static String[] excludePaths;

    private boolean isEnabled;

    public RequestPathFilter(AppFilterProperties.FilterConfig filterConfig) {
        isEnabled = filterConfig.isEnabled();
        excludePaths = filterConfig.getExcludePaths();
        log.debug("isEnabled:" + isEnabled);
        log.debug("excludePaths:" + Arrays.toString(excludePaths));
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        String path = req.getServletPath();

        if (!isEnabled) {
            //对文件上传的参数不缓存
            if (path != null && path.contains("upload")) {
                chain.doFilter(request, response);
            }else {
                chain.doFilter(new BufferRequest(req), response);
            }
            return;
        }

        String url = req.getRequestURL().toString();
        PathMatcher pathMatcher = new AntPathMatcher();
        boolean isOut = true;
        if (ArrayUtils.isNotEmpty(excludePaths)) {
            for (String pattern : excludePaths) {
                if (pathMatcher.match(pattern, path)) {
                    isOut = false;
                    break;
                }
            }
        }
        if (isOut) {
            log.debug(url);
        }

        //对文件上传的参数不缓存
        if (path != null && path.contains("upload")) {
            chain.doFilter(request, response);
        }else {
            chain.doFilter(new BufferRequest(req), response);
        }
    }

    @Override
    public void destroy() {

    }
}
