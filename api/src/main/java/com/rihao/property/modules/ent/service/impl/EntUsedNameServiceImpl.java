package com.rihao.property.modules.ent.service.impl;

import com.rihao.property.modules.ent.entity.EntUsedName;
import com.rihao.property.modules.ent.mapper.EntUsedNameMapper;
import com.rihao.property.modules.ent.service.IEntUsedNameService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wangyu
 * @since 2021-04-11
 */
@Service
public class EntUsedNameServiceImpl extends ServiceImpl<EntUsedNameMapper, EntUsedName> implements IEntUsedNameService {

    @Override
    public List<EntUsedName> getByEntId(Long entId) {
        QueryWrapper<EntUsedName> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(EntUsedName::getEntId, entId);
        return this.list(wrapper);
    }
}
