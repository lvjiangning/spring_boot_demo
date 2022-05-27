package com.rihao.property.modules.ent.service.impl;

import com.rihao.property.modules.ent.entity.EntPartner;
import com.rihao.property.modules.ent.mapper.EntPartnerMapper;
import com.rihao.property.modules.ent.service.IEntPartnerService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wangyu
 * @since 2021-04-06
 */
@Service
public class EntPartnerServiceImpl extends ServiceImpl<EntPartnerMapper, EntPartner> implements IEntPartnerService {

    @Override
    public List<EntPartner> search(Long entId) {
        QueryWrapper<EntPartner> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(EntPartner::getEntId, entId);
        List<EntPartner> list = this.list(wrapper);
        return list;
    }

    @Override
    public Boolean createNew(EntPartner entPartner) {
        return this.save(entPartner);
    }

    @Override
    public Boolean update(EntPartner entPartner) {
        return this.updateById(entPartner);
    }

    @Override
    public EntPartner detail(Long id) {
        return this.getById(id);
    }
}
