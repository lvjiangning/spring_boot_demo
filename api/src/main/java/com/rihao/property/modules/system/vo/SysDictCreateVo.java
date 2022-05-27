package com.rihao.property.modules.system.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author Ken
 * @date 2020/5/18
 * @description
 */
@Data
@ApiModel
@Accessors(chain = true)
public class SysDictCreateVo implements Serializable {

    @ApiModelProperty("上级id")
    private Long parentId;

    @ApiModelProperty("字典名称")
    @NotNull
    private String name;

    @ApiModelProperty("类型。0：字典类别；1：字典项")
    @NotNull
    private Integer type;

    @ApiModelProperty("字典编码code")
    private String code;

    @ApiModelProperty("字典项值")
    private String value;

    @ApiModelProperty("描述")
    private String description;

    @ApiModelProperty("排序号")
    private Integer orders;
}
