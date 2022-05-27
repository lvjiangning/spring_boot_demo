package com.rihao.property.modules.common.vo;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author Ken
 * @date 2020/5/19
 * @description
 */
@Data
@ApiModel
@Accessors(chain = true)
public class UploadResultVo implements Serializable {

    @ApiModelProperty("文件id")
    private String fileId;

    @ApiModelProperty("文件原始名称")
    private String name;

    @ApiModelProperty("文件URL")
    private String url;
}
