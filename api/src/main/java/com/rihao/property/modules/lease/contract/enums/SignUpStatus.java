package com.rihao.property.modules.lease.contract.enums;

import com.anteng.boot.pojo.enum_.IMEnum;

/**
 * 合同签约状态
 */
public enum SignUpStatus implements IMEnum {
    BASIC_INFO(1, "待资料填写"),

    CONTRACT_UPLOAD(4, "待上传文件"),
    OK(5, "已完成");

    SignUpStatus(int value, String label) {
        this.value = value;
        this.label = label;
    }

    private final int value;

    private final String label;

    public int getValue() {
        return value;
    }

    public String getLabel() {
        return label;
    }
}
