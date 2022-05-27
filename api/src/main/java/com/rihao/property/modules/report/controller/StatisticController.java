package com.rihao.property.modules.report.controller;

import com.anteng.boot.web.BaseController;
import com.anteng.boot.web.bind.response.ResBody;
import com.rihao.property.modules.report.service.IStatisticService;
import com.rihao.property.modules.report.vo.ParkStatisticVo;
import io.swagger.annotations.Api;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api/statistic")
@RestController
@Api(tags = "统计")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
public class StatisticController extends BaseController {
    IStatisticService statisticService;

    @GetMapping("/park")
    public ResBody<ParkStatisticVo> statisticParks() {
        ParkStatisticVo vo = this.statisticService.statisticParks();
        return ResBody.success(vo);
    }

    @GetMapping("/unit-settle")
    public ResBody<ParkStatisticVo.UnitSettleStatisticVo> statisticUnitSettle() {
        ParkStatisticVo.UnitSettleStatisticVo vo = this.statisticService.statisticUnitSettle();
        return ResBody.success(vo);
    }

    @GetMapping("/contract-state")
    public ResBody<List<ParkStatisticVo.ContractStateStatisticVo>> statisticContractState() {
        List<ParkStatisticVo.ContractStateStatisticVo> vo = this.statisticService.statisticContractState();
        return ResBody.success(vo);
    }

    @GetMapping("/district-ent")
    public ResBody<List<ParkStatisticVo.DistrictEntStatisticVo>> statisticDistrict() {
        List<ParkStatisticVo.DistrictEntStatisticVo> vo = this.statisticService.statisticDistrict();
        return ResBody.success(vo);
    }
}
