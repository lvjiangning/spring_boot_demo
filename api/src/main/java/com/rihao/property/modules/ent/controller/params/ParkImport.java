package com.rihao.property.modules.ent.controller.params;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ParkImport {

    @ExcelProperty(index = 0, value = "序号")
    private String no; //序号
    @ExcelProperty(index = 1, value = "园区名称")
    private String park; //园区名称
    @ExcelProperty(index = 2, value = "楼栋名称")
    private String building; //楼栋名称
    @ExcelProperty(index = 3, value = "楼层")
    private String floor; //楼层
    @ExcelProperty(index = 4, value = "房间号")
    private String unit; //单元
    @ExcelProperty(index = 5, value = "建筑面积")
    private BigDecimal builtUpArea; //建筑面积
    @ExcelProperty(index = 6, value = "使用面积")
    private BigDecimal usableArea; //使用面积
    @ExcelProperty(index = 7, value = "房屋用途")
    private String unitUseType; //房屋用途
    //@ExcelProperty(index = 8, value = "园区地址")
//    private String address; //房屋用途
//    @ExcelProperty(index = 9, value = "主要用途")
//    private String used; //房屋用途
//    @ExcelProperty(index = 10, value = "产权单位")
//    private String belongUnit; //房屋用途
}
