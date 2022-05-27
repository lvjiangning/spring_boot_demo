package com.rihao.property.common.handler;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.beans.PropertyEditorSupport;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Ken
 * @date 2020/6/2
 * @description LocalDateTime 入参格式化
 */
@RestControllerAdvice
public class CustomerEditorHandler {

    @Value("${spring.jackson.date-format:yyyy-MM-dd HH:mm:ss}")
    private String DATETIME_FORMAT;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {

        binder.registerCustomEditor(LocalDateTime.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                setValue(LocalDateTime.parse(text, DateTimeFormatter.ofPattern(DATETIME_FORMAT)));
            }
        });
    }

}
