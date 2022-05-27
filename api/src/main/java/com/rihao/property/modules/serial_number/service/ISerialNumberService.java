package com.rihao.property.modules.serial_number.service;

import com.rihao.property.modules.lease.contract.entity.Contract;
import com.rihao.property.modules.serial_number.entity.SerialNumber;
import com.baomidou.mybatisplus.extension.service.IService;

import java.time.LocalDate;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wangyu
 * @since 2021-04-10
 */
public interface ISerialNumberService extends IService<SerialNumber> {

    String getSettleSerialNumber();

    String getContractSerialNumber(Contract contract);

    void addContractNo(String code);

    void addSettleNo(String code);

    String getSequence(Long entId);
}
