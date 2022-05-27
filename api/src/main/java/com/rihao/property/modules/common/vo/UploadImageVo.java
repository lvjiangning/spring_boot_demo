package com.rihao.property.modules.common.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author Ken
 * @date 2020/5/29
 * @description
 */
@Data
@ApiModel
@Accessors(chain = true)
public class UploadImageVo implements Serializable {

    private String status;

    private String url;

    private String thumbUrl;

}
