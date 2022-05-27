package com.rihao.property.modules.lease.unit.vo;

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
 * @date 2021-04-06
 * @description
 */
@Data
@ApiModel
@Accessors(chain = true)
public class ContractUnitVo implements Serializable {

    private Long id;
}