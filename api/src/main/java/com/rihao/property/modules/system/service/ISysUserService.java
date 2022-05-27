package com.rihao.property.modules.system.service;

import com.rihao.property.common.page.PageVo;
import com.rihao.property.modules.system.controller.params.AdminQueryParam;
import com.rihao.property.modules.system.entity.SysUser;
import com.rihao.property.modules.system.vo.SysUserCreateVo;
import com.rihao.property.modules.system.vo.SysUserUpdateVo;
import com.rihao.property.modules.system.vo.SysUserVo;
import com.rihao.property.shiro.vo.ChangeProfileVo;
import com.rihao.property.shiro.vo.ChangePwdVo;
import com.rihao.property.shiro.vo.CurrentUserVo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author gaoy
 * @since 2020-02-26
 */
public interface ISysUserService extends IService<SysUser> {

    boolean createNew(SysUserCreateVo createVo);

    PageVo<SysUserVo> search(SysUser user, AdminQueryParam queryParam);

    SysUserVo findOne(Long id);

    boolean update(SysUserUpdateVo vo);

    boolean deleteUser(Long id);

    boolean resetPassword(Long id);

    boolean changePassword(String username, ChangePwdVo changePwdVo);

    CurrentUserVo getCurrentUserByUsername(String username);

    SysUser getSysUserByUsername(String username);

    SysUser getByTelephone(String telephone);

    boolean changProfile(String username, ChangeProfileVo vo);

    boolean changeState(Long id, SysUser.State state);
}
