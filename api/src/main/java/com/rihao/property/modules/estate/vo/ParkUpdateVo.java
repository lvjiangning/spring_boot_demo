package com.rihao.property.modules.estate.vo;

import lombok.Data;

@Data
public class ParkUpdateVo extends ParkCreateVo {
    private Long id;
    private String name;
    private String floorAmount;
    private String upFloorAmount;
    private String downFloorAmount;
    private String totalArea;
    private String coveredArea;
    private String builtUpArea;
    private String usableArea;
    private String upBuiltUpArea;
    private String downBuiltUpArea;
    private String parkingAmount;
}
