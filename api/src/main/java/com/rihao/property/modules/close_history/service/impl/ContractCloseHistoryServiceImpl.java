package com.rihao.property.modules.close_history.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rihao.property.modules.close_history.entity.ContractCloseHistory;
import com.rihao.property.modules.close_history.mapper.ContractCloseHistoryMapper;
import com.rihao.property.modules.close_history.service.IContractCloseHistoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.management.Query;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wangyu
 * @since 2021-03-31
 */
@Service
public class ContractCloseHistoryServiceImpl extends ServiceImpl<ContractCloseHistoryMapper, ContractCloseHistory> implements IContractCloseHistoryService {

    @Override
    public ContractCloseHistory getByContractId(Long contractId) {
        QueryWrapper<ContractCloseHistory> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(ContractCloseHistory::getContractId, contractId);
        return this.getOne(wrapper);
    }
}
