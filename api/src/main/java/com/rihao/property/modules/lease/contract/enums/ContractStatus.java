package com.rihao.property.modules.lease.contract.enums;

/**
 * running: { text: '进行中'},
 *         stop: { text: '已结束', },
 *         wait_contract: { text: '合同处理中' },
 *         wait_pre: { text: '待生效合同', },
 *         due_to_close 即将到期
 */
public enum ContractStatus {
    wait, wait_contract, wait_pre, running, stop, due_to_close
}