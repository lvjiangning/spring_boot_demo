package com.rihao.property.modules.ent.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.rihao.property.common.page.PageVo;
import com.rihao.property.modules.config.service.ISysFileService;
import com.rihao.property.modules.ent.controller.params.EntMattersQueryParam;
import com.rihao.property.modules.ent.convert.EntMattersConvert;
import com.rihao.property.modules.ent.entity.Ent;
import com.rihao.property.modules.ent.entity.EntMatters;
import com.rihao.property.modules.ent.mapper.EntMattersMapper;
import com.rihao.property.modules.ent.service.IEntMattersService;
import com.rihao.property.modules.ent.service.IEntService;
import com.rihao.property.modules.ent.vo.EntMattersVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wangyu
 * @since 2021-03-29
 */
@Service
public class EntMattersServiceImpl extends ServiceImpl<EntMattersMapper, EntMatters> implements IEntMattersService {

    @Autowired
    private IEntService entService;
    @Autowired
    private ISysFileService sysFileService;
    @Override
    public PageVo<EntMattersVo> search(EntMattersQueryParam entMattersQueryParam) {
        Page<EntMatters> page = new Page<>(entMattersQueryParam.getCurrent(), entMattersQueryParam.getPageSize());
        page.setOrders(Lists.newArrayList(
                OrderItem.desc("create_time")
        ));
        EntMattersMapper.QueryParam params = new EntMattersMapper.QueryParam();
        if (StringUtils.hasText(entMattersQueryParam.getEntId())) {
            params.setEntId(entMattersQueryParam.getEntId());
        }
        Page<EntMattersVo> result = this.getBaseMapper().selectByQueryParam(page, params);
        List<EntMattersVo> vos = result.getRecords();
        for (EntMattersVo vo : vos) {
            Ent ent = this.entService.getById(vo.getEntId());
            vo.setFile(sysFileService.getById(vo.getFileId()));
            vo.setEntName(ent.getName());
        }

        return PageVo.create(entMattersQueryParam.getCurrent(), entMattersQueryParam.getPageSize(),
                result.getTotal(), vos);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean createNew(EntMattersVo entMattersVo) {
        EntMatters entMatters = EntMattersConvert.INSTANCE.createParam2Entity(entMattersVo);
        return this.save(entMatters);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean update(EntMattersVo entMattersVo) {
        EntMatters matters = EntMattersConvert.INSTANCE.updateParam2Entity(entMattersVo);
        return this.updateById(matters);
    }

    @Override
    public EntMattersVo detail(Long id) {
        EntMattersVo vo = EntMattersConvert.INSTANCE.entity2Vo(this.getById(id));
        Ent ent = this.entService.getById(vo.getEntId());
        vo.setEntName(ent.getName());
        return vo;
    }

    @Override
    public List<EntMatters> getByEntId(Long entId) {
        QueryWrapper<EntMatters> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(EntMatters::getEntId, entId);
        return this.list(wrapper);
    }
}
