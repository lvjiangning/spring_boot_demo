package com.rihao.property.modules.estate.vo;

import lombok.Data;
import lombok.NonNull;

import java.math.BigDecimal;
import java.util.List;

@Data
public class UnitSplitDetailVo {
    @NonNull
    private String unitNo;
    @NonNull
    private BigDecimal usableArea;
    @NonNull
    private BigDecimal builtUpArea;
}