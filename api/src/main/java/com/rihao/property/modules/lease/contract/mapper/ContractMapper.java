package com.rihao.property.modules.lease.contract.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rihao.property.modules.lease.contract.controller.params.SettleQueryParam;
import com.rihao.property.modules.lease.contract.entity.Contract;
import com.rihao.property.modules.lease.contract.enums.BizType;
import com.rihao.property.modules.lease.contract.enums.ContractStatus;
import com.rihao.property.modules.lease.contract.vo.ContractSettleVo;
import com.rihao.property.modules.lease.contract.vo.ContractVo;
import com.rihao.property.modules.report.vo.ParkStatisticVo;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author wangyu
 * @since 2021-03-28
 */
public interface ContractMapper extends BaseMapper<Contract> {

    Page<ContractVo> selectByQueryParam(Page<Contract> page, QueryParam params);

    Page<ContractVo> selectExpiration(Page<Contract> page, QueryParam params);

    Page<ContractVo> selectHistoryByQueryParam(Page<Contract> page, QueryParam params);

    Page<ContractVo> selectWaitByQueryParam(Page<Contract> page, QueryParam params);

    Page<ContractSettleVo> pageContract(Page<Contract> page, SettleQueryParam params);

    List<ParkStatisticVo.ContractStateNumStatisticVo> groupStatus();
    //查询昨天过期的合同
    List<Contract> selectContractByDueDate();
    //查询今天生效的合同
    List<Contract> selectContractByToDayStart();

    List<ContractVo> selectRunningListForIndex();

    Page<Long> selectEntIdByContract(Page<Contract> page, QueryParam params);

    List<ContractVo> selectContractByLeaseReportParam(@Param("params") QueryParam  params);

    List<ContractVo> useHouseDetailReport(@Param("params") UseHouseDetailQueryParam  params);

    /**
     * 通过时间区间和单元id 查询是否存在合同
     * @param params
     * @return
     */
    List<ContractVo> selectContractByUnitAndDate(@Param("params") QueryParam  params);

    @Data
    @Accessors(chain = true)
    class QueryParam {
        private String code;
        private String entName;
        private String legal;
        private String unit;
        private ContractStatus status;
        private String buildingIds;
        private String expirationMonth;
        private Long entId;
        private Long establishId;
        private String contact;
        private String contactPhoneNumber;
        private Integer depositStatus;
        private Long buildingId;
        private String parkIds;
        private Long parkId;
        private String leaseStartDate;
        private String dueDate;
        private String likeParam;
    }
    @Data
    @Accessors(chain = true)
    class UseHouseDetailQueryParam {
        private List<Long> unitIds;
        private Date startDate;
    }


}
