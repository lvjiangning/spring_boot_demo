package com.rihao.property.modules.system.service.impl;

import com.anteng.boot.pojo.query.Filter;
import com.rihao.property.common.page.PageVo;
import com.rihao.property.modules.system.controller.params.RoleQueryParam;
import com.rihao.property.modules.system.convert.SysRoleConvert;
import com.rihao.property.modules.system.entity.SysRole;
import com.rihao.property.modules.system.mapper.SysRoleMapper;
import com.rihao.property.modules.system.service.ISysRoleService;
import com.rihao.property.modules.system.vo.SysRoleCreateVo;
import com.rihao.property.modules.system.vo.SysRoleVo;
import com.rihao.property.util.ValidationUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.Serializable;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author gaoy
 * @since 2020-02-26
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {


    @Override
    public PageVo<SysRoleVo> search(RoleQueryParam queryParam) {
        Page<SysRole> page = new Page<>(queryParam.getCurrent(), queryParam.getPageSize());
        page.setOrders(Lists.newArrayList(
                OrderItem.desc("create_time")
        ));
        QueryWrapper<SysRole> wrapper = new QueryWrapper<>();
        if (StringUtils.hasText(queryParam.getName())) {
            wrapper.lambda().like(SysRole::getName, Filter.LikeValue.both(queryParam.getName()));
        }
        Page<SysRole> roles = this.page(page, wrapper);

        return PageVo.create(
                queryParam.getCurrent(),
                queryParam.getPageSize(),
                roles.getTotal(),
                SysRoleConvert.INSTANCE.entity2ListItemBatch(roles.getRecords())
        );
    }

    @Override
    public boolean createNew(SysRoleCreateVo createVo) {
        this.validNameUnique(createVo.getName(), null);
        SysRole model = SysRoleConvert.INSTANCE.createParam2Entity(createVo);
        return this.save(model);
    }


    /**
     * 校验名称唯一
     * @param name
     * @param id
     */
    private void validNameUnique(String name, Long id) {
        QueryWrapper<SysRole> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(SysRole::getName, name);
        if (id != null && id != 0) {
            wrapper.lambda().ne(SysRole::getId, id);
        }
        SysRole entity = this.getOne(wrapper);
        ValidationUtil.notNull(entity, "角色", "名称", name);
    }


    @Override
    public boolean removeById(Serializable id) {
        return super.removeById(id);
    }

    @Override
    public SysRole getById(Long id) {
        return super.getById(id);
    }
}
