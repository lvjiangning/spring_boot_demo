package com.rihao.property.modules.lease.unit.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rihao.property.modules.lease.contract.vo.ContractDetailVo;
import com.rihao.property.modules.lease.contract.vo.ContractUnitVo;
import com.rihao.property.modules.lease.unit.entity.ContractUnit;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author wangyu
 * @since 2021-04-06
 */
public interface IContractUnitService extends IService<ContractUnit> {

    List<ContractUnit> getUnitListByContractId(Long contractId);

    List<ContractUnit> getByUnitId(Long unitId);

    List<ContractUnit> getAllListByUnitId(Long unitId);

    /**
     *  查询当前当前正在租赁中的合同单元
     * @param unitId
     * @param currentlyDate
     * @param extContractId 排除的合同id
     * @return
     */
    ContractUnit getNormalByUnitId(Long unitId, String currentlyDate,Long extContractId);

    /**
     * 通过合同ids 查询对于的合同单元信息
     * @param contractIds
     * @return
     */
    List<ContractUnitVo> getUnitsInContractIds(Set<Long> contractIds);

    List<ContractDetailVo.Unit> getUnitsByContractId(Long contractId);
}
