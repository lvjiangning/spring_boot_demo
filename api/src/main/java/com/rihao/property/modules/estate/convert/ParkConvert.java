package com.rihao.property.modules.estate.convert;

import com.rihao.property.modules.estate.vo.ParkCreateVo;
import com.rihao.property.modules.estate.vo.ParkUpdateVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.rihao.property.modules.estate.entity.Park;
import com.rihao.property.modules.estate.vo.ParkVo;

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
public interface ParkConvert {

    ParkConvert INSTANCE = Mappers.getMapper(ParkConvert.class);

    List<ParkVo> entity2ListItemBatch(List<Park> list);

    ParkVo entity2Vo(Park byId);

    Park updateVo2Entity(ParkUpdateVo parkUpdateVo);

    Park createVo2Entity(ParkCreateVo parkCreateVo);
}