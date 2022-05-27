package com.rihao.property.modules.system.service;

import com.rihao.property.common.page.PageVo;
import com.rihao.property.modules.system.controller.params.RoleQueryParam;
import com.rihao.property.modules.system.entity.SysRole;
import com.rihao.property.modules.system.vo.SysRoleCreateVo;
import com.rihao.property.modules.system.vo.SysRoleVo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author gaoy
 * @since 2020-02-26
 */
public interface ISysRoleService extends IService<SysRole> {

    PageVo<SysRoleVo> search(RoleQueryParam queryParam);

    boolean createNew(SysRoleCreateVo createVo);

    SysRole getById(Long id);

}
