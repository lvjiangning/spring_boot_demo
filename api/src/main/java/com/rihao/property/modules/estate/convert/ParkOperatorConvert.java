package com.rihao.property.modules.estate.convert;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.rihao.property.modules.estate.entity.ParkOperator;
import com.rihao.property.modules.estate.vo.ParkOperatorVo;

import java.util.List;
/**
 * <p>
 * 实体转换器
 * </p>
 *
 * @author wangyu
 * @date 2021-07-05
 * @description
 */
@Mapper
public interface ParkOperatorConvert {

    ParkOperatorConvert INSTANCE = Mappers.getMapper(ParkOperatorConvert.class);

    List<ParkOperatorVo> entity2ListItemBatch(List<ParkOperator> list);
}