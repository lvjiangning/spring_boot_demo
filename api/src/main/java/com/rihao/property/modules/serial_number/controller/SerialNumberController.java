package com.rihao.property.modules.serial_number.controller;


import com.anteng.boot.web.bind.response.ResBody;
import com.rihao.property.common.annotation.Log;
import com.rihao.property.modules.serial_number.service.ISerialNumberService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.anteng.boot.web.BaseController;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author wangyu
 * @since 2021-04-10
 */
@RestController
@RequestMapping("/api/serial_number")
public class SerialNumberController extends BaseController {

    private final ISerialNumberService serialNumberService;

    public SerialNumberController(ISerialNumberService serialNumberService) {
        this.serialNumberService = serialNumberService;
    }

    @Log("获取入驻通知编号")
    @GetMapping("settle")
    public ResBody<?> getSettleSerialNumber() {
        String settle_number = this.serialNumberService.getSettleSerialNumber();
        return ResBody.success(settle_number);
    }

    @Log("获取合同编号")
    @GetMapping("contract")
    public ResBody<?> getContractSerialNumber() {
        String contract_number = this.serialNumberService.getContractSerialNumber(null);
        return ResBody.success(contract_number);
    }
}

