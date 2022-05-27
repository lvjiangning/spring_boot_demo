package com.rihao.property.modules.estate.convert;

import com.rihao.property.modules.estate.entity.Building;
import com.rihao.property.modules.estate.vo.BuildingVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

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
public interface BuildingConvert {

    BuildingConvert INSTANCE = Mappers.getMapper(BuildingConvert.class);

    List<BuildingVo> entity2ListItemBatch(List<Building> list);

    BuildingVo entity2Vo(Building byId);

    Building updateParam2Entity(BuildingVo updateVo);

    Building createParam2Entity(BuildingVo createVo);
}