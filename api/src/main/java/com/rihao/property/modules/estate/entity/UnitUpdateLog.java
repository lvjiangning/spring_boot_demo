package com.rihao.property.modules.estate.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.rihao.property.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("tb_unit_update_log")
public class UnitUpdateLog extends BaseEntity {

    private static final long serialVersionUID = 1L;


    @TableField("unit_id")
    private Long unitId;
    @TableField("update_before_text")
    private String updateBeforeText;
    @TableField("update_after_text")
    private  String updateAfterText;


}
