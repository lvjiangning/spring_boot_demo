package com.rihao.property.modules.lease.unit.convert;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.rihao.property.modules.lease.unit.entity.ContractUnit;
import com.rihao.property.modules.lease.unit.vo.ContractUnitVo;

import java.util.List;
/**
 * <p>
 * 实体转换器
 * </p>
 *
 * @author wangyu
 * @date 2021-04-06
 * @description
 */
@Mapper
public interface ContractUnitConvert {

    ContractUnitConvert INSTANCE = Mappers.getMapper(ContractUnitConvert.class);

    List<ContractUnitVo> entity2ListItemBatch(List<ContractUnit> list);
}