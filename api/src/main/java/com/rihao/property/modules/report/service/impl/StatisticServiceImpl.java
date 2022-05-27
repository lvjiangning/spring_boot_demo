package com.rihao.property.modules.report.service.impl;

import com.rihao.property.modules.ent.mapper.EntMapper;
import com.rihao.property.modules.estate.mapper.ParkMapper;
import com.rihao.property.modules.estate.mapper.UnitMapper;
import com.rihao.property.modules.lease.contract.mapper.ContractMapper;
import com.rihao.property.modules.report.service.IStatisticService;
import com.rihao.property.modules.report.vo.ParkStatisticVo;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class StatisticServiceImpl implements IStatisticService {
    ParkMapper parkMapper;
    UnitMapper unitMapper;
    ContractMapper contractMapper;
    EntMapper entMapper;

    @Override
    public ParkStatisticVo statisticParks() {
        return this.parkMapper.statisticParks();
    }

    @Override
    public ParkStatisticVo.UnitSettleStatisticVo statisticUnitSettle() {
        return this.unitMapper.statisticUnitSettle();
    }

    @Override
    public List<ParkStatisticVo.ContractStateStatisticVo> statisticContractState() {
        List<ParkStatisticVo.ContractStateNumStatisticVo> groups = this.contractMapper.groupStatus();
        if (CollectionUtils.isEmpty(groups)) {
            return Collections.emptyList();
        }
        Integer totals = groups.stream().map(ParkStatisticVo.ContractStateNumStatisticVo::getNums)
                .reduce(Integer::sum).get();
        return groups.stream().map(item -> {
            ParkStatisticVo.ContractStateStatisticVo vo = new ParkStatisticVo.ContractStateStatisticVo();
            vo.setStatus(item.getStatus());
            vo.setRate(new BigDecimal(item.getNums()).divide(new BigDecimal(totals), 4, BigDecimal.ROUND_HALF_UP));
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    public List<ParkStatisticVo.DistrictEntStatisticVo> statisticDistrict() {
        List<ParkStatisticVo.DistrictEntNumStatisticVo> groups = this.entMapper.groupDistrict();
        if (CollectionUtils.isEmpty(groups)) {
            return Collections.emptyList();
        }
        Integer totals = groups.stream().map(ParkStatisticVo.DistrictEntNumStatisticVo::getNums)
                .reduce(Integer::sum).get();
        return groups.stream().map(item -> {
            ParkStatisticVo.DistrictEntStatisticVo vo = new ParkStatisticVo.DistrictEntStatisticVo();
            vo.setDistrictName(item.getDistrictName());
            vo.setRate(new BigDecimal(item.getNums()).divide(new BigDecimal(totals), 4, BigDecimal.ROUND_HALF_UP));
            return vo;
        }).collect(Collectors.toList());
    }
}
