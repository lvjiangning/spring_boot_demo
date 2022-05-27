package com.rihao.property.modules.system.vo;

import io.swagger.annotations.ApiModel;
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
public class SysDictSmallVo implements Serializable {

    private Long id;
    private String code;
    private String value;

}
