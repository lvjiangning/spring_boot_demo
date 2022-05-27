package com.rihao.property.modules.cost.convert;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.rihao.property.modules.cost.entity.Cost;
import com.rihao.property.modules.cost.vo.CostVo;

import java.util.List;
/**
 * <p>
 * 实体转换器
 * </p>
 *
 * @author wangyu
 * @since 2021-03-30
 * @description
 */
@Mapper
public interface CostConvert {

    CostConvert INSTANCE = Mappers.getMapper(CostConvert.class);

    List<CostVo> entity2ListItemBatch(List<Cost> list);

    Cost createParam2Entity(CostVo costVo);

    CostVo entity2Vo(Cost byId);
}