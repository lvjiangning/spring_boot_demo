package com.rihao.property.modules.ent.convert;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.rihao.property.modules.ent.entity.EntCategory;
import com.rihao.property.modules.ent.vo.EntCategoryVo;

import java.util.List;
/**
 * <p>
 * 实体转换器
 * </p>
 *
 * @author wangyu
 * @date 2021-06-09
 * @description
 */
@Mapper
public interface EntCategoryConvert {

    EntCategoryConvert INSTANCE = Mappers.getMapper(EntCategoryConvert.class);

    List<EntCategoryVo> entity2ListItemBatch(List<EntCategory> list);
}