package com.rihao.property.modules.config.emuns;

import java.io.Serializable;

public enum SysFileType implements Serializable {


    PURCHASERECORD(1L, "采购记录"),//提交表单api/purchase_record/{id}  获取数据api/park/detail/{id}
    PARKFILE(2L, "园区-文件信息"),//提交表单api/park/file 获取数据api/park/detail/{id}
    APPROVEFILE(3L, "合同-批复文件"),//提交表单api/contract/approve-files  获取数据api/contract/{id}
    CONTRACTFILE(4L, "合同文件"),//提交表单api/contract/contract-files 获取数据api/contract/{id}
    INSPECTIONFILE(5L, "巡查文件"),//提交表单api/inspection 获取数据api/inspection/page
    CONTRACTCLOSEFILE(6L, "合同终止文件"),//提交表单api/contract/close
    CONTRACTTEMPFILE(7L, "合同模板文件"),//获取数据api/park/page
    OLDNAMEFILE(8L, "企业改名文件"),//提交表单api/ent/update_name 获取数据api/ent/used_name
    ENTDETAILINFO(9L, "企业事项"),//提交表单api/matters/ 获取数据api/matters
    ADJUSTCONTRACT(10L, "合同调整");//api/contract/adjust
    private Long value;
    private String text;

    SysFileType(Long value, String text) {
        this.value = value;
        this.text = text;
    }

    public Long getValue() {
        return value;
    }

    public String getText() {
        return text;
    }

    public static String getNameByValue(Long value) {
        SysFileType[] applyTypeEnums = values();
        for (SysFileType applyTypeEnum : applyTypeEnums) {
            if (applyTypeEnum.getValue().equals(value)) {
                return applyTypeEnum.name();
            }
        }
        return "";
    }
}
