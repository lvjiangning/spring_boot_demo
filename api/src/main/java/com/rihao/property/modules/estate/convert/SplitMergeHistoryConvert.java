package com.rihao.property.modules.estate.convert;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.rihao.property.modules.estate.entity.SplitMergeHistory;
import com.rihao.property.modules.estate.vo.SplitMergeHistoryVo;

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
public interface SplitMergeHistoryConvert {

    SplitMergeHistoryConvert INSTANCE = Mappers.getMapper(SplitMergeHistoryConvert.class);

    List<SplitMergeHistoryVo> entity2ListItemBatch(List<SplitMergeHistory> list);
}