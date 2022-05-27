package com.rihao.property.modules.serial_number.emuns;

import com.anteng.boot.pojo.enum_.IMEnum;

/**
 * 业务类型
 */
public enum SerialType implements IMEnum {
    SETTLE(1, "入驻", "RZTZ-"), CONTRACT(2, "合同", "HT-");

    SerialType(int value, String label, String prefix) {
        this.value = value;
        this.label = label;
        this.prefix = prefix;
    }

    private final int value;
    private final String label;
    private final String prefix;

    public int getValue() {
        return value;
    }

    public String getLabel() {
        return label;
    }

    public String getPrefix() {
        return prefix;
    }
}
