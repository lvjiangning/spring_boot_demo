package com.rihao.property.modules.system.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Ken
 * @date 2020/6/2
 * @description
 */
@Data
@ApiModel
@Accessors(chain = true)
public class SysLogVo implements Serializable {

    @ApiModelProperty("ID")
    private Long id;

    @ApiModelProperty("操作人")
    private String operName;

    @ApiModelProperty("操作时间")
    private LocalDateTime createTime;

    @ApiModelProperty("请求IP")
    private String requestIp;

    @ApiModelProperty("操作描述")
    private String description;

    @ApiModelProperty("操作内容")
    private String content;

}
