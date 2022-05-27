package com.rihao.property.modules.ent.enums;

import com.anteng.boot.pojo.enum_.IMEnum;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 入驻状态
 */
public enum SignupStatus implements IMEnum {
    INPUTTING(1, "未入驻"),
    OK(2, "已入驻");

    SignupStatus(int value, String label) {
        this.value = value;
        this.label = label;
    }

    @JsonValue    //标记响应json值
    private final int value;

    private final String label;

    public int getValue() {
        return value;
    }

    public String getLabel() {
        return label;
    }
}
