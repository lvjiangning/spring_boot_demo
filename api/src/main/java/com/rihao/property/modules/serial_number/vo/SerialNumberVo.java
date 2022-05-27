package com.rihao.property.modules.serial_number.vo;

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
 * @date 2021-04-10
 * @description
 */
@Data
@ApiModel
@Accessors(chain = true)
public class SerialNumberVo implements Serializable {

    private Long id;
}