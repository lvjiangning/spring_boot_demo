package com.rihao.property.modules.estate.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;

@Data
public class ParkCreateVo implements Serializable {
    private String name;
    private Long estId;
    private String floorAmount;
    private String totalArea;
    private String coveredArea;
    private String builtUpArea;
    private String usableArea;
    private String parkingAmount;
    /**
     * 地址
     */
    private String address;
    /**
     *  主要用途
     */
    private String used;
    /**
     *  产权单位
     */
    private String belongUnit;


}
