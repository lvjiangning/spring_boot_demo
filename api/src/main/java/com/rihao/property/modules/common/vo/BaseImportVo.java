package com.rihao.property.modules.common.vo;

import cn.afterturn.easypoi.excel.entity.result.ExcelVerifyHandlerResult;
import cn.afterturn.easypoi.handler.inter.IExcelDataModel;
import cn.afterturn.easypoi.handler.inter.IExcelModel;

import java.io.Serializable;

/**
 * @author Ken
 * @date 2020/6/13
 * @description
 */
public class BaseImportVo extends ExcelVerifyHandlerResult implements IExcelModel, IExcelDataModel, Serializable {

    private String errorMsg;
    private int rowNum;

    @Override
    public String getErrorMsg() {
        return errorMsg;
    }

    @Override
    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    @Override
    public Integer getRowNum() {
        return rowNum;
    }

    @Override
    public void setRowNum(Integer rowNum) {
        this.rowNum = rowNum;
    }

}
