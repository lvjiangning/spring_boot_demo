package com.rihao.property.modules.report.vo;

import com.rihao.property.modules.lease.contract.enums.SignUpStatus;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ParkStatisticVo {
    private Integer parks;
    private Integer buildings;
    private Integer units;
    private Integer ents;


    @Data
    public static class UnitSettleStatisticVo{
        private BigDecimal freeRate;
        private BigDecimal settleRate;
    }

    @Data
    public static class ContractStateStatisticVo{
        private SignUpStatus status;
        private BigDecimal rate;
    }

    @Data
    public static class ContractStateNumStatisticVo{
        private SignUpStatus status;
        private int  nums;
    }

    @Data
    public static class DistrictEntNumStatisticVo{
        private String districtName;
        private int nums;
    }

    @Data
    public static class DistrictEntStatisticVo{
        private String districtName;
        private BigDecimal rate;
    }
}
