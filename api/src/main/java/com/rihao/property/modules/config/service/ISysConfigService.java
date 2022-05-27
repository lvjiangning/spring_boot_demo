package com.rihao.property.modules.config.service;

import com.rihao.property.modules.config.entity.SysConfig;
import com.rihao.property.modules.config.vo.SysConfigVo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 系统参数设置 服务类
 * </p>
 *
 * @author wangyu
 * @since 2021-04-21
 */
public interface ISysConfigService extends IService<SysConfig> {

    SysConfig getConfig(Long estId);

    Boolean update(SysConfigVo configVo);
}
