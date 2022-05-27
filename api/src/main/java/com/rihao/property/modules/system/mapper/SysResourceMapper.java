package com.rihao.property.modules.system.mapper;

import com.rihao.property.modules.system.entity.SysResource;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author gaoy
 * @since 2020-02-26
 */
public interface SysResourceMapper extends BaseMapper<SysResource> {

    List<SysResource> getMenuListByUsername(String username);
}
