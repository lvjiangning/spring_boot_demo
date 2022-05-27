package com.rihao.property.common.aspect;

import com.rihao.property.common.annotation.Log;
import com.rihao.property.common.enums.LogType;
import com.rihao.property.modules.system.entity.SysLog;
import com.rihao.property.modules.system.service.ISysLogService;
import io.netty.util.internal.ThrowableUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Ken
 * @date 2020/5/17
 * @description 日志切入
 * 保存参数如为对象时 需要重写toString方法 将参数显示输出
 */
@Component
@Aspect
@Slf4j
public class LogAspect {

    private ISysLogService logService;

    private ThreadLocal<Long> currentTime = new ThreadLocal<>();


    /**
     * 配置切入点
     */
    @Pointcut("@annotation(com.rihao.property.common.annotation.Log)")
    public void logPointcut() {
        // 该方法无方法体,主要为了让同类中其他方法使用此切入点
    }

    /**
     * 配置环绕通知,使用在方法logPointcut()上注册的切入点
     *
     * @param joinPoint join point for advice
     */
    @Around("logPointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        /*currentTime.set(System.currentTimeMillis());
        Object result = joinPoint.proceed();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Log aopLog  = signature.getMethod().getAnnotation(Log.class);

        SysLog log = new SysLog(LogType.INFO.name(),System.currentTimeMillis() - currentTime.get());
        currentTime.remove();

        log.setDescription(aopLog.value());

        logService.saveLog(log);
        return result;*/
        currentTime.set(System.currentTimeMillis());
        Object result = joinPoint.proceed();
        currentTime.remove();
        return result;
    }

    /**
     * 配置异常通知
     *
     * @param joinPoint join point for advice
     * @param e exception
     */
    @AfterThrowing(pointcut = "logPointcut()", throwing = "e")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Log aopLog  = signature.getMethod().getAnnotation(Log.class);
        SysLog sysLog = new SysLog(LogType.ERROR.name(), System.currentTimeMillis() - currentTime.get());
        currentTime.remove();
        log.error(ThrowableUtil.stackTraceToString(e));
        sysLog.setExceptionDetail(ThrowableUtil.stackTraceToString(e));

        sysLog.setDescription(aopLog.value());

        logService.saveLog(sysLog);
    }

    @Autowired
    public void setLogService(ISysLogService logService) {
        this.logService = logService;
    }
}
