package com.rihao.property.modules.establish.convert;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.rihao.property.modules.establish.entity.Establish;
import com.rihao.property.modules.establish.vo.EstablishVo;

import java.util.List;
/**
 * <p>
 * 实体转换器
 * </p>
 *
 * @author wangyu
 * @date 2021-04-23
 * @description
 */
@Mapper
public interface EstablishConvert {

    EstablishConvert INSTANCE = Mappers.getMapper(EstablishConvert.class);

    List<EstablishVo> entity2ListItemBatch(List<Establish> list);

    Establish createParam2Entity(EstablishVo createVo);

    Establish updateParam2Entity(EstablishVo updateVo);
}