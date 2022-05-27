package com.rihao.property.modules.ent.convert;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.rihao.property.modules.ent.entity.EntMatters;
import com.rihao.property.modules.ent.vo.EntMattersVo;

import java.util.List;
/**
 * <p>
 * 实体转换器
 * </p>
 *
 * @author wangyu
 * @since 2021-03-29
 * @description
 */
@Mapper
public interface EntMattersConvert {

    EntMattersConvert INSTANCE = Mappers.getMapper(EntMattersConvert.class);

    List<EntMattersVo> entity2ListItemBatch(List<EntMatters> list);

    EntMatters createParam2Entity(EntMattersVo entMattersVo);

    EntMattersVo entity2Vo(EntMatters byId);

    EntMatters updateParam2Entity(EntMattersVo entMattersVo);
}