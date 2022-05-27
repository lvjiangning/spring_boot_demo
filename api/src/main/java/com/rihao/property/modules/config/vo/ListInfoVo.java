package com.rihao.property.modules.config.vo;

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
 * @date 2021-04-21
 * @description
 */
@Data
@ApiModel
@Accessors(chain = true)
public class ListInfoVo implements Serializable {


    @ApiModelProperty("列表名")
    private String listName;
    @ApiModelProperty("列表内容")
    private String listInfo;

}