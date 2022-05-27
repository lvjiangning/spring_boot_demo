package com.rihao.property.modules.system.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Ken
 * @date 2020/5/18
 * @description
 */
@Data
@ApiModel
@Accessors(chain = true)
public class SysDictListVo implements Serializable {

    @ApiModelProperty("ID")
    private Long id;

    @ApiModelProperty("字典项值")
    private String value;

    @ApiModelProperty("描述")
    private String description;

    @ApiModelProperty("排序号")
    private Integer orders;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("最近修改时间")
    private LocalDateTime modifyTime;


}
