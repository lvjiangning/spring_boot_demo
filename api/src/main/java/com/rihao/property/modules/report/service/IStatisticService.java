package com.rihao.property.modules.report.service;

import com.rihao.property.modules.report.vo.ParkStatisticVo;

import java.util.List;

public interface IStatisticService {
    ParkStatisticVo statisticParks();

    ParkStatisticVo.UnitSettleStatisticVo statisticUnitSettle();

    List<ParkStatisticVo.ContractStateStatisticVo> statisticContractState();

    List<ParkStatisticVo.DistrictEntStatisticVo> statisticDistrict();
}
