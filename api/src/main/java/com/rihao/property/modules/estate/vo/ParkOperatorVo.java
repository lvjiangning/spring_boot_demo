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
 * @date 2021-07-05
 * @description
 */
@Data
@ApiModel
@Accessors(chain = true)
public class ParkOperatorVo implements Serializable {

    private Long id;
}