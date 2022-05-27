package com.rihao.property.modules.lease.contract.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rihao.property.modules.lease.contract.controller.params.SettleQueryParam;
import com.rihao.property.modules.lease.contract.entity.Contract;
import com.rihao.property.modules.lease.contract.entity.ContractAdjust;
import com.rihao.property.modules.lease.contract.enums.ContractStatus;
import com.rihao.property.modules.lease.contract.vo.ContractSettleVo;
import com.rihao.property.modules.lease.contract.vo.ContractVo;
import com.rihao.property.modules.report.vo.ParkStatisticVo;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author wangyu
 * @since 2021-03-28
 */
public interface ContractAdjustMapper extends BaseMapper<ContractAdjust> {


}
