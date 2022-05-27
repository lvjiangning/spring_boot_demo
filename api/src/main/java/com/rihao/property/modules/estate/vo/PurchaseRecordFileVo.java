package com.rihao.property.modules.estate.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 视图实体
 * </p>
 *
 * @author wangyu
 * @date 2021-07-08
 * @description
 */
@Data
@ApiModel
@Accessors(chain = true)
public class PurchaseRecordFileVo extends PurchaseRecordCreateVo {

    private Long id;
    private String winningCompany;
}