package com.rihao.property.common.exception;

import com.anteng.boot.core.facade.code.BizCodeFace;
import com.anteng.boot.core.facade.code.ErrorCode;
import com.anteng.boot.core.facade.exception.BizException;
import com.anteng.boot.web.bind.response.ResBody;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.tomcat.util.http.fileupload.FileUploadBase;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;

/**
 * @author gaoy
 * 2020/2/26/026
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(BizException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ResBody processBizException(BizException e) {
        BizCodeFace.BizCode bizCode = e.getBizCode();
        log.warn(bizCode.getMessage(), e);
        return ResBody.failure(bizCode.getMessage()).code(bizCode.getCode());
    }

    /**
     * 校验错误
     */
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResBody processBindException(BindException e) {
        FieldError fieldError = e.getFieldError();
        log.warn("数据校验失败:" + fieldError.getField() + "[" + fieldError.getDefaultMessage() + "]", e);
        return ResBody.failure(fieldError.getDefaultMessage()).code(ErrorCode.PARAM_ERROR.getCode());
    }

    /**
     * 缺少参数
     */
    @ExceptionHandler({MissingServletRequestParameterException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResBody processMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        log.warn("参数缺失", e);
        return ResBody.failure(ErrorCode.PARAM_ERROR).message("缺少参数[" + e.getParameterName() + "]");
    }

    /**
     * Assert 校验错误
     */
    @ExceptionHandler({IllegalArgumentException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResBody processIllegalArgumentException(NativeWebRequest request, IllegalArgumentException e) {
        log.warn("数据校验失败错误", e);
        return ResBody.failure(ErrorCode.PARAM_ERROR).message(e.getMessage());
    }

    @ExceptionHandler({AuthenticationException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResBody authenticationException(AuthenticationException e) {
        log.warn("认证失败", e);
        return ResBody.failure(ErrorCode.PERMISSION_EXPIRED).message(e.getMessage());
    }

    /**
     * 默认的异常处理
     *
     * @param exception
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResBody exceptionHandler(NativeWebRequest request, Exception exception) {
        log.error("exception:", exception);
        ResBody failure = ResBody.failure();
        String debug = request.getHeader("debug");
        if ("true".equals(debug)) {
            failure.trace(exception.getMessage());
        }
        return failure;
    }

    //上传文件超过限制大小时处理异常，返回提示信息
    @ExceptionHandler(value = MultipartException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResBody handleMaxUploadSizeException(MaxUploadSizeExceededException exception) {
        String msg = null;
        if(exception.getRootCause() instanceof FileUploadBase.FileSizeLimitExceededException){
            //最大限制
            long maxSize = ((FileUploadBase.FileSizeLimitExceededException)(exception.getRootCause())).getPermittedSize();
            msg = "上传文件过大【单文件大小不得超过"+maxSize/(1024*1024)+"MB】";
        }else if(exception.getRootCause() instanceof FileUploadBase.SizeLimitExceededException){
            //最大限制
            long maxSize = ((FileUploadBase.SizeLimitExceededException)(exception.getRootCause())).getPermittedSize();
            msg = "上传文件过大【总上传文件大小不得超过"+maxSize/(1024*1024)+"MB】";
        }else {
            msg = "上传文件失败";
        }
        log.error("exception:", exception.getMessage());
        return ResBody.failure(ErrorCode.DATE_NULL).message(msg);
    }

}
