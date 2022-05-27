package com.rihao.property.modules.system.convert;

import com.rihao.property.modules.system.entity.SysDict;
import com.rihao.property.modules.system.vo.SysDictCreateVo;
import com.rihao.property.modules.system.vo.SysDictListVo;
import com.rihao.property.modules.system.vo.SysDictSmallVo;
import com.rihao.property.modules.system.vo.SysDictVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author Ken
 * @date 2020/5/18
 * @description
 */
@Mapper
public interface SysDictConvert {

    SysDictConvert INSTANCE = Mappers.getMapper(SysDictConvert.class);

    List<SysDictListVo> entity2ListItemBatch(List<SysDict> list);

    SysDict createParam2Entity(SysDictCreateVo createVo);

    SysDict updateParam2Entity(SysDictVo updateVo);

    SysDictVo entity2Update(SysDict entity);

    List<SysDictSmallVo> entity2ListSmalllItem(List<SysDict> list);

}
