package com.rihao.property.modules.system.mapper;

import com.rihao.property.modules.system.entity.SysRoleResource;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.Set;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author gaoy
 * @since 2020-02-26
 */
public interface SysRoleResourceMapper extends BaseMapper<SysRoleResource> {
    Set<String> getPressionCodesByRoleId(Long roleId);
}
