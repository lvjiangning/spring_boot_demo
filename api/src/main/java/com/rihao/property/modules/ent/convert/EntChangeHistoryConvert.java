package com.rihao.property.modules.ent.convert;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.rihao.property.modules.ent.entity.EntChangeHistory;
import com.rihao.property.modules.ent.vo.EntChangeHistoryVo;

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
public interface EntChangeHistoryConvert {

    EntChangeHistoryConvert INSTANCE = Mappers.getMapper(EntChangeHistoryConvert.class);

    List<EntChangeHistoryVo> entity2ListItemBatch(List<EntChangeHistory> list);
}