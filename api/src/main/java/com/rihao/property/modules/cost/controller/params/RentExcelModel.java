package com.rihao.property.modules.cost.controller.params;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class RentExcelModel {

    @ExcelProperty("合同编号")
    private String code;

    @ExcelProperty("企业名称")
    private String entName;

    @ExcelProperty("联系人")
    private String contact;

    @ExcelProperty("联系人手机")
    private String contactPhoneNumber;

    @ExcelProperty("缴费月份")
    private String costTime;

    @ExcelProperty("费用类型")
    private String costType;

    @ExcelProperty("面积")
    private String area;

    @ExcelProperty("单元")
    private String unit;

    @ExcelProperty("价格(月/平方米)")
    private BigDecimal price;

    @ExcelProperty("月租")
    private String rent;

    @ExcelProperty("状态")
    private String status;
}
