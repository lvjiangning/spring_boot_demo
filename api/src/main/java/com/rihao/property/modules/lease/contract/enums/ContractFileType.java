package com.rihao.property.modules.lease.contract.enums;

import com.anteng.boot.pojo.enum_.IMEnum;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 合同文件类型
 */
public enum ContractFileType implements IMEnum {
    APPROVE(1, "批复文件"), CONTRACT(2, "合同文件");

    ContractFileType(int value, String label) {
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
