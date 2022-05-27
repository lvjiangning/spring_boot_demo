package com.rihao.property.modules.ent.service;

import com.rihao.property.modules.common.vo.AliyunOssStsVo;

public interface IAliyunOssService {

    AliyunOssStsVo getAliyunOssSts(String dir);

    void afterPropertiesSet() throws Exception;

    void destroy() throws Exception;
}
