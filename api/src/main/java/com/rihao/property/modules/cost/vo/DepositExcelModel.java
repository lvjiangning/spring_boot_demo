package com.rihao.property.modules.cost.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class DepositExcelModel {

    @ExcelProperty("合同编号")
    private String contractCode;

    @ExcelProperty("公司名称")
    private String entName;

    @ExcelProperty("金额")
    private String amount;

    @ExcelProperty("状态")
    private String status;
}
