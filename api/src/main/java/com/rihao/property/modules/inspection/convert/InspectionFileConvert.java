package com.rihao.property.modules.inspection.convert;

import com.rihao.property.modules.estate.vo.InspectionFileCreateVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.rihao.property.modules.inspection.entity.InspectionFile;
import com.rihao.property.modules.inspection.vo.InspectionFileVo;

import java.util.List;
/**
 * <p>
 * 实体转换器
 * </p>
 *
 * @author wangyu
 * @date 2021-08-13
 * @description
 */
@Mapper
public interface InspectionFileConvert {

    InspectionFileConvert INSTANCE = Mappers.getMapper(InspectionFileConvert.class);

    List<InspectionFileVo> entity2ListItemBatch(List<InspectionFile> list);

    InspectionFile createVo2Entity(InspectionFileCreateVo inspectionFileCreateVo);
}