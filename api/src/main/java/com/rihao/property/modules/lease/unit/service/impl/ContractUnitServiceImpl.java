package com.rihao.property.modules.lease.unit.service.impl;

import com.rihao.property.modules.lease.contract.vo.ContractDetailVo;
import com.rihao.property.modules.lease.contract.vo.ContractUnitVo;
import com.rihao.property.modules.lease.unit.entity.ContractUnit;
import com.rihao.property.modules.lease.unit.enums.ContractUnitState;
import com.rihao.property.modules.lease.unit.mapper.ContractUnitMapper;
import com.rihao.property.modules.lease.unit.service.IContractUnitService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wangyu
 * @since 2021-04-06
 */
@Service
public class ContractUnitServiceImpl extends ServiceImpl<ContractUnitMapper, ContractUnit> implements IContractUnitService {

    @Override
    public List<ContractUnit> getUnitListByContractId(Long contractId) {
        QueryWrapper<ContractUnit> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(ContractUnit::getContractId, contractId);
        return this.list(wrapper);
    }

    @Override
    public List<ContractUnit> getByUnitId(Long unitId) {
        QueryWrapper<ContractUnit> wrapper = new QueryWrapper<>();
        wrapper.lambda()
                .eq(ContractUnit::getUnitId, unitId)
                .eq(ContractUnit::getStatus, ContractUnitState.normal);
                // .or().eq(ContractUnit::getStatus, ContractUnitState.wait_pre);
        return this.list(wrapper);
    }

    @Override
    public List<ContractUnit> getAllListByUnitId(Long unitId) {
        QueryWrapper<ContractUnit> wrapper = new QueryWrapper<>();
        wrapper.lambda()
                .eq(ContractUnit::getUnitId, unitId);
        return this.list(wrapper);
    }

    /**
     * 查单元与合同关联信息中 ，在时间内是否存在有生效的合同
     * @param unitId
     * @param currentlyDate 时间
     * @param extContractId 排除的合同id
     * @return
     */
    @Override
    public ContractUnit getNormalByUnitId(Long unitId, String currentlyDate,Long extContractId) {
        if(StringUtils.isEmpty(currentlyDate)){
            return null;
        }
        //根据单元id和在合同开始和结束时间内的数据--单元合同关联
        return this.getBaseMapper().selectContractUnitByUnitDate(unitId,currentlyDate,extContractId);
    }

    @Override
    public List<ContractUnitVo> getUnitsInContractIds(Set<Long> contractIds) {
        if (CollectionUtils.isEmpty(contractIds)) {
            return Collections.emptyList();
        }
        return this.getBaseMapper().selectUnitsInContractIds(contractIds);
    }

    @Override
    public List<ContractDetailVo.Unit> getUnitsByContractId(Long contractId) {
        return this.getBaseMapper().selectUnitsByContractId(contractId);
    }
}
