package com.rihao.property.modules.config.service.impl;

import com.rihao.property.modules.config.convert.SysConfigConvert;
import com.rihao.property.modules.config.entity.SysConfig;
import com.rihao.property.modules.config.mapper.SysConfigMapper;
import com.rihao.property.modules.config.service.ISysConfigService;
import com.rihao.property.modules.config.vo.SysConfigVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 系统参数设置 服务实现类
 * </p>
 *
 * @author wangyu
 * @since 2021-04-21
 */
@Service
public class SysConfigServiceImpl extends ServiceImpl<SysConfigMapper, SysConfig> implements ISysConfigService {

    @Override
    public SysConfig getConfig(Long estId) {
        QueryWrapper<SysConfig> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(SysConfig::getEstablishId, estId);
        return this.getOne(wrapper);
    }

    @Override
    public Boolean update(SysConfigVo configVo) {
        SysConfig config = SysConfigConvert.INSTANCE.updateParam2Entity(configVo);
        if (config.getId()!=null){
            return this.updateById(config);
        }else {
            return this.save(config);
        }
    }
}
