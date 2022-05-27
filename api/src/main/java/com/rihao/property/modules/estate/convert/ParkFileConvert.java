package com.rihao.property.modules.estate.convert;

import com.rihao.property.modules.estate.entity.Park;
import com.rihao.property.modules.estate.entity.ParkFile;
import com.rihao.property.modules.estate.vo.ParkCreateVo;
import com.rihao.property.modules.estate.vo.ParkFileVo;
import com.rihao.property.modules.estate.vo.ParkUpdateVo;
import com.rihao.property.modules.estate.vo.ParkVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

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
public interface ParkFileConvert {

    ParkFileConvert INSTANCE = Mappers.getMapper(ParkFileConvert.class);

    List<ParkFileVo> entityList2VoList(List<ParkFile> list);
}