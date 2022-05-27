package com.rihao.property.modules.lease.contract.convert;

import com.rihao.property.modules.estate.vo.ContractFileCreateVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.rihao.property.modules.lease.contract.entity.ContractFile;
import com.rihao.property.modules.lease.contract.vo.ContractFileVo;

import java.util.List;
/**
 * <p>
 * 实体转换器
 * </p>
 *
 * @author wangyu
 * @date 2021-07-12
 * @description
 */
@Mapper
public interface ContractFileConvert {

    ContractFileConvert INSTANCE = Mappers.getMapper(ContractFileConvert.class);

    List<ContractFileVo> entity2ListItemBatch(List<ContractFile> list);

    ContractFile createVo2Entity(ContractFileCreateVo contractFileCreateVo);
}