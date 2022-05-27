package com.rihao.property.modules.ent.controller.params;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class DuplicateSearch {

    @ExcelProperty(value="公司名称", index = 0)
    private String name;
    @ExcelProperty(value="信用代码", index = 1)
    private String code;
    @ExcelProperty(value="法人姓名", index = 2)
    private String legal;
    @ExcelProperty(value="法人手机号", index = 3)
    private String phone;
}
