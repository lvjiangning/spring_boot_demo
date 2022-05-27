package com.rihao.property.modules.ent.controller.params;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class AreaImport {

    @ExcelProperty(index = 0)
    private String area;
    @ExcelProperty(index = 1)
    private String child;
}
