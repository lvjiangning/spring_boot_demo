package com.rihao.property.modules.common.convert;

import com.rihao.property.modules.common.entity.District;
import com.rihao.property.modules.common.vo.DistrictVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author Ken
 * @date 2020/5/19
 * @description
 */
@Mapper
public interface DistrictConvert {

    DistrictConvert INSTANCE = Mappers.getMapper(DistrictConvert.class);

    List<DistrictVo> entity2ListItemBatch(List<District> list);


}
