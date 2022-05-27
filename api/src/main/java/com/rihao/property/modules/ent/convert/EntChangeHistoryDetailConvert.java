package com.rihao.property.modules.ent.convert;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.rihao.property.modules.ent.entity.EntChangeHistoryDetail;
import com.rihao.property.modules.ent.vo.EntChangeHistoryDetailVo;

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
public interface EntChangeHistoryDetailConvert {

    EntChangeHistoryDetailConvert INSTANCE = Mappers.getMapper(EntChangeHistoryDetailConvert.class);

    List<EntChangeHistoryDetailVo> entity2ListItemBatch(List<EntChangeHistoryDetail> list);
}