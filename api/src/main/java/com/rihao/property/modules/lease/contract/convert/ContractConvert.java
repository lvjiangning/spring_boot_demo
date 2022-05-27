package com.rihao.property.modules.lease.contract.convert;

import com.rihao.property.modules.lease.contract.entity.Contract;
import com.rihao.property.modules.lease.contract.vo.ContractCreateVo;
import com.rihao.property.modules.lease.contract.vo.SettleContractVo;
import com.rihao.property.modules.lease.contract.vo.SettleInBaseInfoVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.rihao.property.modules.lease.contract.vo.ContractVo;

import java.util.List;
/**
 * <p>
 * 实体转换器
 * </p>
 *
 * @author wangyu
 * @since 2021-03-28
 * @description
 */
@Mapper
public interface ContractConvert {

    ContractConvert INSTANCE = Mappers.getMapper(ContractConvert.class);

    List<ContractVo> entity2ListItemBatch(List<Contract> list);

    ContractVo entity2Vo(Contract byId);

    Contract createParam2Entity(ContractCreateVo createVo);

    Contract baseInfo2Contract(SettleInBaseInfoVo settleVo);

    Contract vo2Entity(SettleContractVo contractVo);
}