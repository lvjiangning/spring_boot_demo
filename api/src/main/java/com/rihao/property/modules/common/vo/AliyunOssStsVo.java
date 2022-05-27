package com.rihao.property.modules.common.vo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 阿里云OSS信息
 *
 * @author wangyu
 * @since 2021/1/17 15:13
 **/
@Data
@Accessors(chain = true)
public class AliyunOssStsVo {
    private String accessId;
    private String dir;
    private Long expire;
    private String host;
    private String policy;
    private String signature;
}
