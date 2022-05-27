package com.rihao.property.modules.establish.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 视图实体
 * </p>
 *
 * @author wangyu
 * @date 2021-04-23
 * @description
 */
@Data
@ApiModel
@Accessors(chain = true)
public class EstablishVo implements Serializable {

    private Long id;
    private String name;
    private String address;
    private String contractPrefix;  //合同前缀
}