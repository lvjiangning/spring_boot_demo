package com.rihao.property.modules.estate.vo;

import lombok.Data;

import java.util.List;

@Data
public class UnitSplitVo {

    private Long unitId;
    private List<UnitSplitDetailVo> splitDetailList;
}