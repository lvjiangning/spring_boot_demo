package com.rihao.property.modules.inspection.convert;

import com.rihao.property.modules.inspection.vo.InspectionCreateVo;
import com.rihao.property.modules.inspection.vo.InspectionUpdateVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.rihao.property.modules.inspection.entity.Inspection;
import com.rihao.property.modules.inspection.vo.InspectionVo;

import java.util.List;
/**
 * <p>
 * 实体转换器
 * </p>
 *
 * @author wangyu
 * @since 2021-03-27
 * @description
 */
@Mapper
public interface InspectionConvert {

    InspectionConvert INSTANCE = Mappers.getMapper(InspectionConvert.class);

    List<InspectionVo> entity2ListItemBatch(List<Inspection> list);

    Inspection createParam2Entity(InspectionCreateVo inspectionCreateVo);

    InspectionVo entity2Vo(Inspection byId);

    Inspection updateParam2Entity(InspectionUpdateVo inspectionUpdateVo);
}