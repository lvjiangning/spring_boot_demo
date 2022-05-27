package com.rihao.property.modules.inspection.vo;

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
 * @date 2021-08-13
 * @description
 */
@Data
@ApiModel
@Accessors(chain = true)
public class InspectionFileVo implements Serializable {

    private Long id;
    private String name;
    private String fileUrl;
}