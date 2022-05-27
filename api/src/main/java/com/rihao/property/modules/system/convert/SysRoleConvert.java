package com.rihao.property.modules.system.convert;

import com.rihao.property.common.vo.KeyValueVo;
import com.rihao.property.modules.system.entity.SysRole;
import com.rihao.property.modules.system.vo.SysRoleCreateVo;
import com.rihao.property.modules.system.vo.SysRoleUpdateVo;
import com.rihao.property.modules.system.vo.SysRoleVo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author gaoy
 * 2020/2/26/026
 */
@Mapper
public interface SysRoleConvert {

    SysRoleConvert INSTANCE = Mappers.getMapper(SysRoleConvert.class);

    SysRole createParam2Entity(SysRoleCreateVo createVo);

    SysRole updateParam2Entity(SysRoleUpdateVo vo);

    SysRoleVo entity2ListItem(SysRole model);

    List<SysRoleVo> entity2ListItemBatch(List<SysRole> models);

    @Mapping(source = "id", target = "key")
    @Mapping(source = "name", target = "value")
    KeyValueVo entity2KeyValueVo(SysRole sysRole);

    List<KeyValueVo> entity2KeyValueListVo(List<SysRole> result);

}
