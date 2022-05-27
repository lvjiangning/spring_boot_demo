package com.rihao.property.modules.system.controller.params;

import com.rihao.property.common.page.PageParams;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Ken
 * @date 2020/6/2
 * @description
 */
@Data
@ApiModel
public class LogQueryParam extends PageParams {

    @ApiModelProperty("操作人")
    private String operName;

    @ApiModelProperty("操作内容")
    private String description;

    @ApiModelProperty("操作起始时间")
    private LocalDateTime operStartTime;

    @ApiModelProperty("操作结束时间")
    private LocalDateTime operEndTime;

}
