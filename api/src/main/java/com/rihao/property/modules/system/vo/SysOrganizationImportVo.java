package com.rihao.property.modules.system.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.entity.result.ExcelVerifyHandlerResult;
import cn.afterturn.easypoi.handler.inter.IExcelDataModel;
import cn.afterturn.easypoi.handler.inter.IExcelModel;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author Ken
 * @date 2020/6/7
 * @description
 */
@Data
@ApiModel
@Accessors(chain = true)
public class SysOrganizationImportVo extends ExcelVerifyHandlerResult implements IExcelModel, IExcelDataModel, Serializable {

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

    @NotNull
    @Excel(name = "单位名称")
    private String name;

    @NotNull
    @Excel(name = "单位全称")
    private String fullName;

    @Excel(name = "单位编码")
    private String code;

    @Excel(name = "上级单位名称")
    private String parentName;

    @Excel(name = "单位类型")
    private String type;

    @Excel(name = "单位级别")
    private String level;

    @Excel(name = "建制人数")
    private Integer establishmentNumber;

    @Excel(name = "邮政编码")
    private String postalCode;

    @Excel(name = "联系人")
    private String contacts;

    @Excel(name = "联系电话")
    private String phone;

    @Excel(name = "通信地址")
    private String address;

    @Excel(name = "排序值")
    private Integer orders;

}
