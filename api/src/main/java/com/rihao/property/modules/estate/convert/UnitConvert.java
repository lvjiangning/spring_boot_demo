package com.rihao.property.modules.estate.convert;

import com.rihao.property.modules.estate.entity.Unit;
import com.rihao.property.modules.estate.vo.UnitCreateVo;
import com.rihao.property.modules.estate.vo.UnitUpdateVo;
import com.rihao.property.modules.estate.vo.UnitVo;
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
public interface UnitConvert {

    UnitConvert INSTANCE = Mappers.getMapper(UnitConvert.class);

    List<UnitVo> entity2ListItemBatch(List<Unit> list);

    UnitVo entity2Vo(Unit byId);

    Unit createVo2Entity(UnitCreateVo createVo);

    Unit updateVo2Entity(UnitUpdateVo updateVo);
}