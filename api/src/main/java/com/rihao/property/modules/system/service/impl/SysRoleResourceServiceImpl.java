package com.rihao.property.modules.system.service.impl;

import com.rihao.property.modules.system.entity.SysRoleResource;
import com.rihao.property.modules.system.mapper.SysRoleResourceMapper;
import com.rihao.property.modules.system.service.ISysRoleResourceService;
import com.rihao.property.modules.system.vo.SysRolePermissionVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
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
public class SysRoleResourceServiceImpl extends ServiceImpl<SysRoleResourceMapper, SysRoleResource> implements ISysRoleResourceService {


    @Override
    public Set<String> getPressionCodesByRoleId(Long roleId) {
        return this.getBaseMapper().getPressionCodesByRoleId(roleId);
    }


    @Override
    public boolean grantPermission(SysRolePermissionVo rolePermission) {
        this.remove(new QueryWrapper<>(
                new SysRoleResource()
                        .setRoleId(rolePermission.getRoleId())
        ));
        List<SysRoleResource> roleResources = Stream.of(rolePermission.getPermissionIds())
                .map(item -> new SysRoleResource()
                        .setResourceId(item)
                        .setRoleId(rolePermission.getRoleId()))
                .collect(Collectors.toList());
        return this.saveBatch(roleResources, 20);
    }

    @Override
    public SysRolePermissionVo getGrantedPermissions(Long roleId) {
        QueryWrapper<SysRoleResource> w = new QueryWrapper<>();
        w.lambda().eq(SysRoleResource::getRoleId, roleId);
        List<SysRoleResource> roleResources = this.list(w);
        return new SysRolePermissionVo()
                .setRoleId(roleId)
                .setPermissionIds(
                        roleResources.stream().map(SysRoleResource::getResourceId)
                                .toArray(Long[]::new)
                );
    }
}
