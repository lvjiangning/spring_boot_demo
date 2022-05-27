package com.rihao.property.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rihao.property.common.vo.KeyValueVo;
import com.rihao.property.modules.system.entity.SzArea;
import com.rihao.property.modules.system.mapper.SzAreaMapper;
import com.rihao.property.modules.system.service.ISzAreaService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wangyu
 * @since 2021-08-28
 */
@Service
public class SzAreaServiceImpl extends ServiceImpl<SzAreaMapper, SzArea> implements ISzAreaService {

    @Override
    public List<KeyValueVo> getAll() {
        QueryWrapper<SzArea> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(SzArea::getParentId, 0);
        List<SzArea> areas = this.list(wrapper);
        List<KeyValueVo> keyValueVos = new ArrayList<>();
        for (SzArea area : areas) {
            KeyValueVo keyValueVo = new KeyValueVo();
            keyValueVo.setKey(area.getId());
            keyValueVo.setValue(area.getName());
            keyValueVos.add(keyValueVo);
        }
        return keyValueVos;
    }

    @Override
    public List<KeyValueVo> getByParentId(Long id) {
        QueryWrapper<SzArea> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(SzArea::getParentId, id);
        List<SzArea> areas = this.list(wrapper);
        List<KeyValueVo> keyValueVos = new ArrayList<>();
        for (SzArea area : areas) {
            KeyValueVo keyValueVo = new KeyValueVo();
            keyValueVo.setKey(area.getId());
            keyValueVo.setValue(area.getName());
            keyValueVos.add(keyValueVo);
        }
        return keyValueVos;
    }

    @Override
    public SzArea getByName(String name) {
        QueryWrapper<SzArea> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(SzArea::getName, name);
        return this.getOne(wrapper);
    }
}
