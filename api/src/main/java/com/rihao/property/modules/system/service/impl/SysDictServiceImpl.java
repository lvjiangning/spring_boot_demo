package com.rihao.property.modules.system.service.impl;

import com.anteng.boot.core.facade.code.BizCodeFace;
import com.anteng.boot.core.facade.code.ErrorCode;
import com.anteng.boot.core.facade.exception.BizException;
import com.anteng.boot.pojo.query.Filter;
import com.rihao.property.common.enums.EnableState;
import com.rihao.property.common.page.PageVo;
import com.rihao.property.modules.system.controller.params.DictQueryParam;
import com.rihao.property.modules.system.convert.SysDictConvert;
import com.rihao.property.modules.system.entity.SysDict;
import com.rihao.property.modules.system.mapper.SysDictMapper;
import com.rihao.property.modules.system.service.ISysDictService;
import com.rihao.property.modules.system.vo.SysDictCreateVo;
import com.rihao.property.modules.system.vo.SysDictListVo;
import com.rihao.property.modules.system.vo.SysDictSmallVo;
import com.rihao.property.modules.system.vo.SysDictVo;
import com.rihao.property.util.ValidationUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * <p>
 * 数据字典表 服务实现类
 * </p>
 *
 * @author ken
 * @since 2020-05-17
 */
@Service
public class SysDictServiceImpl extends ServiceImpl<SysDictMapper, SysDict> implements ISysDictService {

    @Override
    public PageVo<SysDictListVo> search(DictQueryParam queryParam) {
        Page<SysDict> page = new Page<>(queryParam.getCurrent(), queryParam.getPageSize());
        page.setOrders(Lists.newArrayList(
                OrderItem.desc("create_time")
        ));
        QueryWrapper<SysDict> wrapper = new QueryWrapper<>();
        if (queryParam.getParentId() != null) {
            wrapper.lambda().eq(SysDict::getParentId, queryParam.getParentId());
        }
        wrapper.lambda().eq(SysDict::getType, queryParam.getType());
        if (StringUtils.hasText(queryParam.getCode())) {
            wrapper.lambda().like(SysDict::getCode, Filter.LikeValue.both(queryParam.getCode()));
        }
        Page<SysDict> roles = this.page(page, wrapper);

        return PageVo.create(
                queryParam.getCurrent(),
                queryParam.getPageSize(),
                roles.getTotal(),
                SysDictConvert.INSTANCE.entity2ListItemBatch(roles.getRecords())
        );
    }

    @Override
    public boolean createNew(SysDictCreateVo createVo) {
        if (!StringUtils.isEmpty(createVo.getCode())) {
            this.validCodeUnique(createVo.getCode(), null);
        }

        SysDict entity = SysDictConvert.INSTANCE.createParam2Entity(createVo);
        if (createVo.getParentId() != null && createVo.getParentId() != 0) {
            SysDict parent = this.getById(createVo.getParentId());
            entity.setParentIds(parent.makeSelfAsParentsIds());
        }else {
            entity.setParentIds("0/");
            entity.setParentId(0L);
        }

        return this.save(entity);
    }

    @Override
    public boolean update(SysDictVo updateVo) {
        if (!StringUtils.isEmpty(updateVo.getCode())) {
            this.validCodeUnique(updateVo.getCode(), updateVo.getId());
        }

        SysDict entity = SysDictConvert.INSTANCE.updateParam2Entity(updateVo);
        return this.updateById(entity);
    }

    @Override
    public boolean delete(Long id) {
        // 删除前判断是否有子项
        List<SysDict> childs = this.list(new QueryWrapper<SysDict>().lambda().eq(SysDict::getParentId, id).orderByAsc(SysDict::getOrders));
        if (!CollectionUtils.isEmpty(childs)) {
            throw new BizException(BizCodeFace.createBizCode(ErrorCode.FAIL).message("存在子项，不允许删除"));
        }
        return this.removeById(id);
    }

    @Override
    public SysDictVo detail(Long id) {
        SysDict entity = this.getById(id);
        return SysDictConvert.INSTANCE.entity2Update(entity);
    }

    @Override
    public List<SysDictSmallVo> listByParentId(Long parentId) {
        List<SysDict> list = this.list(
                new QueryWrapper<SysDict>()
                        .lambda()
                        .eq(SysDict::getParentId, parentId)
                        .eq(SysDict::getState, EnableState.enable)
                        .orderByAsc(SysDict::getOrders));
        if (CollectionUtils.isEmpty(list)) {
            return Lists.newArrayList();
        }
        return SysDictConvert.INSTANCE.entity2ListSmalllItem(list);
    }

    @Override
    public List<SysDictSmallVo> listByParentCode(String parentCode) {
        SysDict entity = this.getOne(
                new QueryWrapper<SysDict>()
                        .lambda()
                        .eq(SysDict::getCode, parentCode)
                        .last("LIMIT 1"));
        if (entity == null) {
            return Lists.newArrayList();
        }
        return this.listByParentId(entity.getId());
    }

    /**
     * 校验code唯一
     * @param code
     * @param id
     */
    private void validCodeUnique(String code, Long id) {
        QueryWrapper<SysDict> wrapper = new QueryWrapper();
        wrapper.lambda().eq(SysDict::getCode, code);
        if (id != null && id != 0) {
            wrapper.lambda().ne(SysDict::getId, id);
        }
        SysDict entity = this.getOne(wrapper);
        ValidationUtil.notNull(entity, "数据字典", "编码", code);
    }



}
