package com.rihao.property.modules.ent.convert;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.rihao.property.modules.ent.entity.EntUsedName;
import com.rihao.property.modules.ent.vo.EntUsedNameVo;

import java.util.List;
/**
 * <p>
 * 实体转换器
 * </p>
 *
 * @author wangyu
 * @date 2021-04-11
 * @description
 */
@Mapper
public interface EntUsedNameConvert {

    EntUsedNameConvert INSTANCE = Mappers.getMapper(EntUsedNameConvert.class);

    List<EntUsedNameVo> entity2ListItemBatch(List<EntUsedName> list);
}