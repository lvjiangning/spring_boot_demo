package com.rihao.property.modules.estate.vo;

import com.rihao.property.modules.estate.enums.UnitState;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 视图实体
 * </p>
 *
 * @author wangyu
 * @description
 * @since 2021-03-28
 */
@Data
@ApiModel
@Accessors(chain = true)
public class UnitUpdateVo extends UnitCreateVo {

    private Long id;
}