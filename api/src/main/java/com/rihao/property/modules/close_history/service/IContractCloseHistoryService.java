package com.rihao.property.modules.close_history.service;

import com.rihao.property.modules.close_history.entity.ContractCloseHistory;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wangyu
 * @since 2021-03-31
 */
public interface IContractCloseHistoryService extends IService<ContractCloseHistory> {

    ContractCloseHistory getByContractId(Long id);
}
