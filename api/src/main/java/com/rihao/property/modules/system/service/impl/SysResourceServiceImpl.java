package com.rihao.property.modules.system.service.impl;

import com.rihao.property.common.enums.EnableState;
import com.rihao.property.constant.CommonConstant;
import com.rihao.property.modules.system.entity.SysResource;
import com.rihao.property.modules.system.mapper.SysResourceMapper;
import com.rihao.property.modules.system.service.ISysResourceService;
import com.rihao.property.modules.system.utils.TreeResourceUtil;
import com.rihao.property.modules.system.vo.TreeResourceVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import java.util.stream.Stream;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author gaoy
 * @since 2020-02-26
 */
@Service
public class SysResourceServiceImpl extends ServiceImpl<SysResourceMapper, SysResource> implements ISysResourceService {

    @Override
    public List<TreeResourceVo> getAllResourceTree() {
        //获取启用的资源列表
        List<SysResource> allResource = this.list(new QueryWrapper<SysResource>()
                .lambda()
                .eq(SysResource::getState, EnableState.enable));

        //转换
        return TreeResourceUtil.convertToTreeResource(allResource, false);
    }

    @Override
    public List<TreeResourceVo> getAllMenuTree(String username) {
        List<SysResource> allResource = getSysResourcesByUsername(username);
        //转换
        return TreeResourceUtil.convertToTreeResource(allResource, false);
    }

    @Override
    public List<SysResource> getSysResourcesByUsername(String username) {
        List<SysResource> childResource;
        // 如果是超级管理员admin账号，拥有所有菜单权限
        if (CommonConstant.DEFAULT_ADMIN_NAME.equals(username)) {
            //查询所以菜单
            childResource = this.getAllResource();
        }else {
            //查询这个岗位拥有的菜单权限
            childResource = getBaseMapper().getMenuListByUsername(username);
        }

        //将父节点选择出来
        Set<Long> ids = childResource.stream().flatMapToLong(item -> {
            String treePath = item.getParentIds();
            String[] parents = StringUtils.split(treePath, "/");
            LongStream longStream = Stream.of(parents).mapToLong(Long::parseLong).filter(s -> s > 0L);
            return longStream;
        }).boxed().collect(Collectors.toSet());
        childResource.forEach(item -> ids.add(item.getId()));
        if (CollectionUtils.isEmpty(ids)) {
            return Lists.newArrayList();
        }
        return this.getBaseMapper().selectBatchIds(ids);
    }

    @Override
    public List<SysResource> getAllResource() {
        return this.list(new QueryWrapper<SysResource>().lambda().eq(SysResource::getState, EnableState.enable));
    }
}
