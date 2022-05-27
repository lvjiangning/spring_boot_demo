package com.rihao.property.modules.estate.convert;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.rihao.property.modules.estate.entity.Floor;
import com.rihao.property.modules.estate.vo.FloorVo;

import java.util.List;
/**
 * <p>
 * 实体转换器
 * </p>
 *
 * @author wangyu
 * @since 2021-03-28
 * @description
 */
@Mapper
public interface FloorConvert {

    FloorConvert INSTANCE = Mappers.getMapper(FloorConvert.class);

    List<FloorVo> entity2ListItemBatch(List<Floor> floorList);
}