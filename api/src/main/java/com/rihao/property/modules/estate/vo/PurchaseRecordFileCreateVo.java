package com.rihao.property.modules.estate.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@ApiModel
@Accessors(chain = true)
public class PurchaseRecordFileCreateVo implements Serializable {
    private String name;
    private String fileUrl;
    private Long recordId;
}
