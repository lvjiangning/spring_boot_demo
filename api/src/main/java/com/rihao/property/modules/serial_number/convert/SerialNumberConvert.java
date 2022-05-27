package com.rihao.property.modules.serial_number.convert;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.rihao.property.modules.serial_number.entity.SerialNumber;
import com.rihao.property.modules.serial_number.vo.SerialNumberVo;

import java.util.List;
/**
 * <p>
 * 实体转换器
 * </p>
 *
 * @author wangyu
 * @date 2021-04-10
 * @description
 */
@Mapper
public interface SerialNumberConvert {

    SerialNumberConvert INSTANCE = Mappers.getMapper(SerialNumberConvert.class);

    List<SerialNumberVo> entity2ListItemBatch(List<SerialNumber> list);
}