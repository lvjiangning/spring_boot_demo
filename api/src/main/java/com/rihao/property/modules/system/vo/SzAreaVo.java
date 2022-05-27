package com.rihao.property.modules.system.vo;

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
 * @date 2021-08-28
 * @description
 */
@Data
@ApiModel
@Accessors(chain = true)
public class SzAreaVo implements Serializable {

    private Long id;
}