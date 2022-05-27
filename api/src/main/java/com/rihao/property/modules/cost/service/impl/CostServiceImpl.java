package com.rihao.property.modules.cost.service.impl;

import com.rihao.property.common.page.PageVo;
import com.rihao.property.modules.cost.controller.params.CostQueryParam;
import com.rihao.property.modules.cost.controller.params.RentExcelModel;
import com.rihao.property.modules.cost.convert.CostConvert;
import com.rihao.property.modules.cost.entity.Cost;
import com.rihao.property.modules.cost.mapper.CostMapper;
import com.rihao.property.modules.cost.service.ICostService;
import com.rihao.property.modules.cost.vo.CostVo;
import com.rihao.property.modules.cost.vo.DepositExcelModel;
import com.rihao.property.modules.ent.entity.Ent;
import com.rihao.property.modules.ent.service.IEntService;
import com.rihao.property.modules.lease.contract.entity.Contract;
import com.rihao.property.modules.lease.contract.service.IContractService;
import com.rihao.property.modules.lease.unit.entity.ContractUnit;
import com.rihao.property.modules.lease.unit.service.IContractUnitService;
import com.rihao.property.modules.estate.entity.Unit;
import com.rihao.property.modules.estate.service.IUnitService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wangyu
 * @since 2021-03-30
 */
@Service
public class CostServiceImpl extends ServiceImpl<CostMapper, Cost> implements ICostService {

    private final IEntService entService;
    private final IContractService contractService;
    private final IContractUnitService contractUnitService;
    private final IUnitService unitService;

    public CostServiceImpl(IEntService entService, IContractService contractService,
                           IContractUnitService contractUnitService, IUnitService unitService) {
        this.entService = entService;
        this.contractService = contractService;
        this.contractUnitService = contractUnitService;
        this.unitService = unitService;
    }

    @Override
    public PageVo<CostVo> search(CostQueryParam costQueryParam) throws ParseException {
        // 处理租金
        this.processRent();

        Page<Cost> page = new Page<>(costQueryParam.getCurrent(), costQueryParam.getPageSize());
        CostMapper.QueryParam params = new CostMapper.QueryParam();
        if (StringUtils.hasText(costQueryParam.getEntName())) {
            Ent ent = this.entService.getByName(costQueryParam.getEntName());
            if (ent != null)
                params.setEntId(ent.getId());
        }
        if (StringUtils.hasText(costQueryParam.getCode())) {
            Contract contract = this.contractService.getByCode(costQueryParam.getCode());
            if (contract != null)
                params.setContractId(contract.getId());
        }
        if (StringUtils.hasText(costQueryParam.getCostTime())) {
            params.setCostTime(costQueryParam.getCostTime());
        }
        if (costQueryParam.getStatus() != null) {
            params.setStatus(costQueryParam.getStatus());
        }
        Page<CostVo> result = this.getBaseMapper().selectByQueryParam(page, params);
        List<CostVo> list = result.getRecords();

        return PageVo.create(costQueryParam.getCurrent(), costQueryParam.getPageSize(),
                result.getTotal(), list);
    }

    private void processRent() {
        List<Contract> contracts = this.contractService.getAvailableList();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        String costTime = format.format(new Date());
        for (Contract contract : contracts) {
            Cost cost = this.getByContractIdAndCostTime(contract.getId(), costTime);
            if (cost == null) {
                cost = new Cost();
                cost.setCostTime(costTime);
                cost.setContractId(contract.getId());
                cost.setEntId(contract.getEntId());
                cost.setStatus(1);
                cost.setTypeId(1);
                cost.setAmount(this.getAmountByContract(contract));
                this.save(cost);
            }
        }
    }

    private BigDecimal getAmountByContract(Contract contract) {
        List<ContractUnit> contractUnits = this.contractUnitService.getUnitListByContractId(contract.getId());
        BigDecimal area = BigDecimal.ZERO;
        for (ContractUnit contractUnit : contractUnits) {
            Unit unit = this.unitService.getById(contractUnit.getUnitId());
            area = area.add(unit.getUsableArea());
//            area += Float.parseFloat(unit.getUsableArea());
        }
        return area.multiply(contract.getPrice()) ;
    }

    private Cost getByContractIdAndCostTime(Long contractId, String costTime) {
        QueryWrapper<Cost> wrapper = new QueryWrapper<>();
        wrapper.lambda()
                .eq(Cost::getContractId, contractId)
                .eq(Cost::getCostTime, costTime);
        return this.getOne(wrapper);
    }

    @Override
    public Boolean createNew(CostVo costVo) {
        Cost entity = CostConvert.INSTANCE.createParam2Entity(costVo);
        return this.save(entity);
    }

    @Override
    public void setStatus(Long id, int status) {
        Cost cost = this.getById(id);
        cost.setStatus(status);
        this.updateById(cost);
    }

    @Override
    public CostVo detail(String id) throws ParseException {
        CostVo costVo = CostConvert.INSTANCE.entity2Vo(this.getById(id));
        costVo.setCostTime(costVo.getCostTime().substring(0, 6));
        return costVo;
    }

    @Override
    public PageVo<CostVo> depositPage(CostQueryParam costQueryParam) {
        Page<Cost> page = new Page<>(costQueryParam.getCurrent(), costQueryParam.getPageSize());
        CostMapper.QueryParam params = new CostMapper.QueryParam();
        if (StringUtils.hasText(costQueryParam.getEntName())) {
            Ent ent = this.entService.getByName(costQueryParam.getEntName());
            params.setEntId(ent.getId());
        }
        Page<CostVo> result = this.getBaseMapper().selectDeposit(page, params);
        return PageVo.create(costQueryParam.getCurrent(), costQueryParam.getPageSize(),
                result.getTotal(), result.getRecords());
    }

    @Override
    public List<DepositExcelModel> getDepositList() {
        List<Contract> contracts = this.contractService.getAvailableList();
        List<DepositExcelModel> depositExcelModelList = new ArrayList<>();
        for (Contract contract : contracts) {
            Ent ent = this.entService.getById(contract.getEntId());
            // 金额
            DepositExcelModel depositExcelModel = new DepositExcelModel();
            depositExcelModel.setAmount(contract.getDeposit());

            // 企业名称
            depositExcelModel.setEntName(ent.getName());

            // 合同编号
            depositExcelModel.setContractCode(contract.getCode());

            // 状态
            if (contract.getDepositStatus() == 0)
                depositExcelModel.setStatus("未缴纳");
            if (contract.getDepositStatus() == 1)
                depositExcelModel.setStatus("已缴纳");

            depositExcelModelList.add(depositExcelModel);
        }
        return depositExcelModelList;
    }

    @Override
    public List<RentExcelModel> getRentList() {
        List<Cost> costList = this.list();
        List<RentExcelModel> rentExcelModels = new ArrayList<>();
        for (Cost cost : costList) {
            RentExcelModel rentExcelModel = new RentExcelModel();
            Contract contract = this.contractService.getById(cost.getContractId());
            rentExcelModel.setCode(contract.getCode());
            Ent ent = this.entService.getById(cost.getEntId());
            rentExcelModel.setEntName(ent.getName());
            rentExcelModel.setContact(contract.getContact());
            rentExcelModel.setContactPhoneNumber(contract.getContactPhoneNumber());
            rentExcelModel.setCostTime(cost.getCostTime());
            rentExcelModel.setCostType("房租");
            rentExcelModel.setArea(contract.getArea());
            rentExcelModel.setUnit(contract.getUnit());
            rentExcelModel.setPrice(contract.getPrice());
            rentExcelModel.setRent(contract.getRent());
            if (cost.getStatus() == 1)
                rentExcelModel.setStatus("未缴纳");
            if (cost.getStatus() == 2)
                rentExcelModel.setStatus("已缴纳");
            rentExcelModels.add(rentExcelModel);
        }
        return rentExcelModels;
    }
}
