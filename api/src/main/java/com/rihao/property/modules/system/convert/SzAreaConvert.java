package com.rihao.property.modules.system.convert;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.rihao.property.modules.system.entity.SzArea;
import com.rihao.property.modules.system.vo.SzAreaVo;

import java.util.List;
/**
 * <p>
 * 实体转换器
 * </p>
 *
 * @author wangyu
 * @date 2021-08-28
 * @description
 */
@Mapper
public interface SzAreaConvert {

    SzAreaConvert INSTANCE = Mappers.getMapper(SzAreaConvert.class);

    List<SzAreaVo> entity2ListItemBatch(List<SzArea> list);
}