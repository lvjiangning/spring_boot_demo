

package com.rihao.property.modules.system.convert;

import com.rihao.property.modules.system.entity.SysUser;
import com.rihao.property.modules.system.vo.SysUserCreateVo;
import com.rihao.property.modules.system.vo.SysUserUpdateVo;
import com.rihao.property.modules.system.vo.SysUserVo;
import com.rihao.property.shiro.vo.CurrentUserVo;
import com.rihao.property.shiro.vo.LoginSysUserRedisVo;
import com.rihao.property.shiro.vo.LoginSysUserVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 系统用户对象转换器
 *
 * @author
 * @date 2019-10-05
 **/
@Mapper
public interface SysUserConvert {

    SysUserConvert INSTANCE = Mappers.getMapper(SysUserConvert.class);

    /**
     * 系统用户实体对象转换成登陆用户VO对象
     *
     * @param sysUser
     * @return
     */
    LoginSysUserVo sysUserToLoginSysUserVo(SysUser sysUser);

    /**
     * LoginSysUserVo对象转换成LoginSysUserRedisVo
     *
     * @param loginSysUserVo
     * @return
     */
    LoginSysUserRedisVo loginSysUserVoToLoginSysUserRedisVo(LoginSysUserVo loginSysUserVo);

    SysUser createParam2Entity(SysUserCreateVo createVo);

    SysUser updateParam2Entity(SysUserUpdateVo vo);

    SysUserVo entity2ListItem(SysUser model);

    CurrentUserVo sysUserToCurrentUserVo(SysUser current);
}
