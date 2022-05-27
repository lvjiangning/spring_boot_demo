package com.rihao.property.common.interceptor;

import com.anteng.boot.web.Constants;
import com.rihao.property.common.enums.LogType;
import com.rihao.property.modules.system.entity.SysLog;
import com.rihao.property.modules.system.service.ISysLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Ken
 * @date 2020/5/19
 * @description 日志记录拦截器器
 */
@Component
public class LogInterceptor extends HandlerInterceptorAdapter {

    private static final Logger logger = LoggerFactory.getLogger(LogInterceptor.class);
    private ThreadLocal<Long> currentTime = new ThreadLocal<>();

    private ISysLogService logService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        currentTime.set(System.currentTimeMillis());
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        String content = (String) request.getAttribute(Constants.LOG_CONTENT_REQUEST_KEY);
        request.removeAttribute(Constants.LOG_CONTENT_REQUEST_KEY);
        String description = (String) request.getAttribute(Constants.LOG_OPERATOR_REQUEST_KEY);
        request.removeAttribute(Constants.LOG_OPERATOR_REQUEST_KEY);
        if (StringUtils.isEmpty(description)) {
            currentTime.remove();
            return;
        }

        SysLog sysLog = new SysLog(LogType.INFO.name(), System.currentTimeMillis() - currentTime.get());
        currentTime.remove();

        sysLog.setContent(content);
        sysLog.setDescription(description);

        try {
            this.logService.saveLog(sysLog);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("日志写入错误,reason:{}", ex.getMessage());
        }
    }

    @Autowired
    public void setLogService(ISysLogService logService) {
        this.logService = logService;
    }
}
