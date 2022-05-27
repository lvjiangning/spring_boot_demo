package com.rihao.property.modules.lease.contract.scheduling;

import cn.hutool.core.date.DateUtil;
import com.rihao.property.modules.config.entity.SysFile;
import com.rihao.property.modules.lease.contract.entity.Contract;
import com.rihao.property.modules.lease.contract.enums.ContractStatus;
import com.rihao.property.modules.lease.contract.enums.SignUpStatus;
import com.rihao.property.modules.lease.contract.service.IContractService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * 关于合同的定时任务
 * 每天0点执行
 * 如果实际结束日期为空，则按照合同结束日期结束
 * 如果实际结束日期不为空，则按照合同实际结束日期为准
 */
@Service
@Slf4j
public class ContractTask {
    @Autowired
    private IContractService  contractService;
    /**
     * 每天晚上0.01分执行
     * 查询所有未关闭且（如果实际结束日期== 前一天 或者 合同结束日期 == 前一天）的合同，进行关闭
     * 查询所有当天应该生效的合同生效
     */
    @Scheduled(cron = "0 1 0 * * ? ")
    @Transactional(readOnly = false)
    public  void  updateContractStatus(){
        log.info("========时间【"+ DateUtil.formatDateTime(new Date()) +"】;定时任务开始执行！=======");
        //查询所有未关闭且（如果实际结束日期== 前一天 或者 合同结束日期 == 前一天）的合同
        List<Contract> contracts = contractService.selectContractByDueDate();
        //不为空说明 有到期的合同
        if (CollectionUtils.isNotEmpty(contracts)){
            /**
             * 1、先把合同设为关闭
             * 2、更新合同单元表
             * 3、更新单元的状态
             */
            for (int i = 0; i < contracts.size(); i++) {
                Contract contract = contracts.get(i);
                //合同为已关闭
                contract.setEstimateClose(true);
                //合同状态为过期关闭
                contract.setStatus(ContractStatus.stop);
                contract.setModifyTime(LocalDateTime.now());
                contract.setModifyBy("系统");
                boolean contractResult = contractService.updateById(contract);
                log.info("时间【"+ DateUtil.formatDateTime(new Date()) +"】;定时任务将合同编号【"+contract.getCode()+"】、id【"+contract.getId()+"】的合同状态设为关闭！");
                //合同更新成功处理合同单元表，以及单元信息
                if (contractResult){
                    contractService.closeUnitByContract(contract);
                }

            }
        }
        //查询所有当天应该生效的合同生效
        List<Contract> toDayStartContracts = contractService.selectContractByToDayStart();
        if (CollectionUtils.isNotEmpty(toDayStartContracts)){
            //让今天开始生效的合同开始生效
            for (int i = 0; i < toDayStartContracts.size(); i++) {
                Contract contract = toDayStartContracts.get(i);
                //更新合同信息
                contract.setStatus(ContractStatus.running);
                contract.setModifyTime(LocalDateTime.now());
                contract.setModifyBy("系统");
                boolean contractResult = contractService.updateById(contract);
                log.info("时间【"+ DateUtil.formatDateTime(new Date()) +"】;定时任务将合同编号【"+contract.getCode()+"】、id【"+contract.getId()+"】的合同更新为生效！");
                //合同更新成功处理合同单元表，以及单元信息
                if (contractResult){
                    contractService.contractByToDayStart(contract);
                }
            }
        }
        log.info("========时间【"+ DateUtil.formatDateTime(new Date()) +"】;定时任务执行结束！=======");
    }
}
