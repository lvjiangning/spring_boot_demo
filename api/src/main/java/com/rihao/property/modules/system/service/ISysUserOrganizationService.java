package com.rihao.property.modules.system.service;

import com.rihao.property.modules.common.vo.PermissionControlVo;
import com.rihao.property.modules.system.entity.SysUserOrganization;
import com.rihao.property.modules.system.vo.SysUserOrganizationVo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户单位关联表 服务类
 * </p>
 *
 * @author ken
 * @since 2020-05-17
 */
public interface ISysUserOrganizationService extends IService<SysUserOrganization> {

    SysUserOrganizationVo getGrantOrganizations(Long id);

    boolean grantOrganizations(SysUserOrganizationVo userOrganization);

    /**
     * 获取当前用户的组织权限id组
     * @return
     */
    PermissionControlVo getCurrentUserOrganizationIds();
}
