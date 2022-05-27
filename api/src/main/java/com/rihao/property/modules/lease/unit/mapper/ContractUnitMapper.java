package com.rihao.property.modules.lease.unit.mapper;

import com.rihao.property.modules.lease.contract.vo.ContractDetailVo;
import com.rihao.property.modules.lease.contract.vo.ContractUnitVo;
import com.rihao.property.modules.lease.unit.entity.ContractUnit;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;
import java.util.Set;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wangyu
 * @since 2021-04-06
 */
public interface ContractUnitMapper extends BaseMapper<ContractUnit> {


    ContractUnitVo selectContractUnitByUnitDate(Long unitId, String currentlyDate,Long extContractId);

    List<ContractUnitVo> selectUnitsInContractIds(Set<Long> contractIds);

    List<ContractDetailVo.Unit> selectUnitsByContractId(Long contractId);
}
