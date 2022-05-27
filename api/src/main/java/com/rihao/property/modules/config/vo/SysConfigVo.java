package com.rihao.property.modules.config.vo;

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
 * @date 2021-04-21
 * @description
 */
@Data
@ApiModel
@Accessors(chain = true)
public class SysConfigVo implements Serializable {

    private Long id;
    private String lessor;
    private String lessorAddress;
    private String region;
    private String road;
    private String roadDetail;
    private String placeHolder;
    private String paymentDate;
    private String arrearsMonth;
    private String arrearsDay;
    private String deliveryAddress;
    private int contactExpiration;
    private Long establishId;
}