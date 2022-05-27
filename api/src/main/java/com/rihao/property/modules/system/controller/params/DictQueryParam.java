package com.rihao.property.modules.system.controller.params;

import com.rihao.property.common.page.PageParams;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Ken
 * @date 2020/5/18
 * @description
 */
@Data
@ApiModel
public class DictQueryParam extends PageParams {

    @ApiModelProperty("字典分类code")
    private String code;

    @ApiModelProperty("parentId")
    private Long parentId;

    @ApiModelProperty("类型 0：字典类别；1：字典项")
    private Integer type = 1;
}
