package com.rihao.property.modules.ent.convert;

import com.rihao.property.modules.ent.vo.EntCreateVo;
import com.rihao.property.modules.ent.vo.EntUpdateVo;
import com.rihao.property.modules.ent.vo.ProtraitVo;
import com.rihao.property.modules.ent.entity.Ent;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.rihao.property.modules.ent.vo.EntVo;

import java.util.List;
/**
 * <p>
 * 实体转换器
 * </p>
 *
 * @author wangyu
 * @date 2021-03-26
 * @description
 */
@Mapper
public interface EntConvert {

    EntConvert INSTANCE = Mappers.getMapper(EntConvert.class);

    List<EntVo> entity2ListItemBatch(List<Ent> list);

    Ent createParam2Entity(EntCreateVo entCreateVo);

    EntVo entity2Vo(Ent byId);

    Ent updateParam2Entity(EntUpdateVo entUpdateVo);

    ProtraitVo entity2ProtraitVo(Ent byId);
}