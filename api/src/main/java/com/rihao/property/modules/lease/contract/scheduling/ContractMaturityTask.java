package com.rihao.property.modules.lease.contract.scheduling;

import com.rihao.property.modules.lease.contract.service.IContractService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component("contract-maturity")
public class ContractMaturityTask {

    private final IContractService contractService;

    public ContractMaturityTask(IContractService contractService) {
        this.contractService = contractService;
    }

    //从开始时间计算，30s执行一次  用 ContractTask
//    @Scheduled(fixedRate = 30000)
//    public void execute() {
//        this.processContract();
//    }

//    private void processContract() {
//        //合同状态处理
//        this.contractService.processContract();
//        //处理计划关闭的合同
//        this.contractService.planClose();
//    }
}
