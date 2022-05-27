package com.rihao.property.modules.ent.service.impl;

import com.anteng.boot.pojo.query.Filter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.rihao.property.common.page.PageVo;
import com.rihao.property.common.vo.KeyValueVo;
import com.rihao.property.modules.ent.convert.EntCategoryConvert;
import com.rihao.property.modules.ent.entity.EntCategory;
import com.rihao.property.modules.ent.mapper.EntCategoryMapper;
import com.rihao.property.modules.ent.service.IEntCategoryService;
import com.rihao.property.modules.ent.vo.EntCategoryVo;
import com.rihao.property.modules.system.controller.params.EntCategoryQueryParam;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wangyu
 * @since 2021-06-09
 */
@Service
public class EntCategoryServiceImpl extends ServiceImpl<EntCategoryMapper, EntCategory> implements IEntCategoryService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createNew(EntCategoryVo createVo) {
        EntCategory entCategory = new EntCategory();
        entCategory.setName(createVo.getName());
        return this.save(entCategory);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(EntCategoryVo updateVo) {
        EntCategory entCategory = this.getById(updateVo.getId());
        entCategory.setName(updateVo.getName());
        return this.updateById(entCategory);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(Long id) {
        // TODO 判断是否可以删除
        return this.delete(id);
    }

    @Override
    public PageVo<EntCategoryVo> search(EntCategoryQueryParam queryParam) {
        Page<EntCategory> page = new Page<>(queryParam.getCurrent(), queryParam.getPageSize());
        page.setOrders(Lists.newArrayList(
                OrderItem.desc("create_time")
        ));
        QueryWrapper<EntCategory> wrapper = new QueryWrapper<>();
        if (queryParam.getName() != null)
            wrapper.lambda().like(EntCategory::getName, Filter.LikeValue.both(queryParam.getName()));
        Page<EntCategory> entCategoryPage = this.page(page, wrapper);
        List<EntCategoryVo> list = EntCategoryConvert.INSTANCE.entity2ListItemBatch(entCategoryPage.getRecords());
        return PageVo.create(queryParam.getCurrent(), queryParam.getPageSize(),
                entCategoryPage.getTotal(), list);
    }

    @Override
    public EntCategory detail(Long id) {
        return this.getById(id);
    }

    @Override
    public List<KeyValueVo> getKeyValueList() {
        List<EntCategory> entCategories = this.list();
        List<KeyValueVo> vos = new ArrayList<>();
        for (EntCategory entCategory : entCategories) {
            KeyValueVo vo = new KeyValueVo();
            vo.setKey(entCategory.getId());
            vo.setValue(entCategory.getName());
            vos.add(vo);
        }
        return vos;
    }
}
