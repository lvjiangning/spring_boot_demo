package com.rihao.property.modules.system.service;

import com.rihao.property.modules.system.entity.SysRoleResource;
import com.rihao.property.modules.system.vo.SysRolePermissionVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Set;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author gaoy
 * @since 2020-02-26
 */
public interface ISysRoleResourceService extends IService<SysRoleResource> {

    /**
     * 角色的权限code列表
     */
    Set<String> getPressionCodesByRoleId(Long roleId);

    /**
     * 角色赋权
     */
    boolean grantPermission(SysRolePermissionVo rolePermission);

    /**
     * 获取角色的权限ids
     */
    SysRolePermissionVo getGrantedPermissions(Long roleId);
}
