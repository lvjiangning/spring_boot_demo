package com.rihao.property.modules.lease.contract.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rihao.property.common.DateInterval;
import com.rihao.property.modules.config.service.ISysFileService;
import com.rihao.property.modules.cost.entity.Cost;
import com.rihao.property.modules.estate.entity.Unit;
import com.rihao.property.modules.estate.enums.UnitState;
import com.rihao.property.modules.estate.service.IUnitService;
import com.rihao.property.modules.lease.contract.entity.Contract;
import com.rihao.property.modules.lease.contract.entity.ContractAdjust;
import com.rihao.property.modules.lease.contract.entity.ContractFile;
import com.rihao.property.modules.lease.contract.enums.ContractStatus;
import com.rihao.property.modules.lease.contract.mapper.ContractAdjustMapper;
import com.rihao.property.modules.lease.contract.mapper.ContractFileMapper;
import com.rihao.property.modules.lease.contract.service.IContractAdjustService;
import com.rihao.property.modules.lease.contract.service.IContractFileService;
import com.rihao.property.modules.lease.contract.service.IContractService;
import com.rihao.property.modules.lease.contract.vo.ContractAdjustVo;
import com.rihao.property.modules.lease.unit.entity.ContractUnit;
import com.rihao.property.modules.lease.unit.enums.ContractUnitState;
import com.rihao.property.modules.lease.unit.service.IContractUnitService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author wangyu
 * @since 2021-03-28
 */
@Service
public class ContractAdjustServiceImpl extends ServiceImpl<ContractAdjustMapper, ContractAdjust> implements IContractAdjustService {

    @Autowired
    @Lazy
    private IContractService contractService;
    @Autowired
    @Lazy
    private IContractUnitService contractUnitService;
    @Autowired
    @Lazy
    private IUnitService unitService;
    @Autowired
    @Lazy
    private ISysFileService sysFileService;



    @Override
    public String adjustContract(ContractAdjustVo contractAdjustVo) {
        if (contractAdjustVo.getContractId() == null) {
            return "合同参数为空，不允许调整！";
        }

        if (contractAdjustVo.getSysFile() == null || contractAdjustVo.getSysFile().getId() == null) {
            return "附件信息为空，不允许调整！";
        }

        if (org.apache.commons.lang3.StringUtils.isBlank(contractAdjustVo.getRemark())) {
            return "调整事由为空，不允许调整！";
        }

        if (contractAdjustVo.getEstimateCloseDate() == null) {
            return "实际终止时间为空，不允许调整！";
        }
        if (org.apache.commons.lang3.StringUtils.isBlank(contractAdjustVo.getType())) {
            return "调整类型，不允许调整！";
        }
        Contract contract = contractService.getById(contractAdjustVo.getContractId());
        if (contract == null){
            return "合同参数为空，不允许调整！";
        }
        if (org.apache.commons.lang3.StringUtils.equals("1", contractAdjustVo.getType())) { //合同到期终止
            //修正终止时间
            contract.setEstimateCloseDate(DateUtil.formatDate(contractAdjustVo.getEstimateCloseDate()));
            contractService.updateById(contract);
        } else if (org.apache.commons.lang3.StringUtils.equals("2", contractAdjustVo.getType())) { //合同中途终止
            /**
             * 1，实际结束时间必须小于结束时间
             * 2、如果选择的实际终止时间 小于等于当前时间 立即终止。
             * 3、如果选择的实际终止时间 大于当前时间 则等到实际终止时间终止。
             */
            //1，实际结束时间必须小于结束时间,并大于开始时间
            if (DateUtil.formatDate(contractAdjustVo.getEstimateCloseDate()).compareTo(contract.getLeaseStartDate()) < 0){
                  return "实际终止时间不能小于合同生效日期，不允许调整！";
            }
            if (DateUtil.formatDate(contractAdjustVo.getEstimateCloseDate()).compareTo(contract.getDueDate()) > 0){
                return "实际终止时间不能大于合同结束日期，不允许调整！";
            }
            // 2、如果选择的实际终止时间 小于等于当前时间 立即终止。
            int i = DateUtil.formatDate(contractAdjustVo.getEstimateCloseDate()).compareTo(DateUtil.formatDate(new Date()));
            if (i < 0){
                contract.setStatus(ContractStatus.stop);
                //设置对应单元状态为空闲
                List<ContractUnit> contractUnits = this.contractUnitService.getUnitListByContractId(contract.getId());
                for (ContractUnit contractUnit : contractUnits) {
                    Unit unit = this.unitService.getById(contractUnit.getUnitId());
                    unit.setStatus(UnitState.free);
                    this.unitService.updateById(unit);
                    //更新合同单元关联状态
                    contractUnit.setStatus(ContractUnitState.close);
                    this.contractUnitService.updateById(contractUnit);
                }
            }
           // 3、如果选择的实际终止时间 大于当前时间 则等到实际终止时间终止。
            if (DateUtil.formatDate(contractAdjustVo.getEstimateCloseDate()).compareTo(DateUtil.formatDate(new Date()))>0){

            }
            //修正终止时间
            contract.setEstimateCloseDate(DateUtil.formatDate(contractAdjustVo.getEstimateCloseDate()));
            contractService.updateById(contract);
        }
        ContractAdjust param = new ContractAdjust();
        BeanUtils.copyProperties(contractAdjustVo, param);
        param.setFileId(contractAdjustVo.getSysFile().getId());
        this.save(param);
        return null;
    }

    @Override
    public ContractAdjustVo adjustContractDetailByContractId(Long contractId) {
        if (contractId == null){
            return null;
        }
        ContractAdjustVo contractAdjustVo=new ContractAdjustVo();
        ContractAdjust one = this.getOne(new QueryWrapper<ContractAdjust>().lambda().eq(ContractAdjust::getContractId, contractId));
        if (one != null){
            BeanUtils.copyProperties(one,contractAdjustVo);
            contractAdjustVo.setSysFile(sysFileService.getById(one.getFileId()));
        }
        return contractAdjustVo;
    }
}