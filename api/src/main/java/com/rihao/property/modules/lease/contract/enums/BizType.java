package com.rihao.property.modules.lease.contract.enums;

import com.anteng.boot.pojo.enum_.IMEnum;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 合同业务类型
 */
public enum BizType implements IMEnum {
    SETTLE(1, "新入驻"),
    REDUCE(2, "减租"),
    RENEWAL(3, "续租"),
    RETREAT(4, "退租"),
    CHANGE(5, "换租"),
    ADJUST(6, "调租"),
    STOP(7, "中止"),
    EXTEND(8, "扩租")
    ;

    BizType(int value, String label) {
        this.value = value;
        this.label = label;
    }

    @JsonValue    //标记响应json值
    private final int value;

    private final String label;

    @Override
    public int getValue() {
        return value;
    }

    public String getLabel() {
        return label;
    }
}
