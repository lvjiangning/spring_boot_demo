package com.rihao.property.modules.lease.contract.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.rihao.property.common.BaseEntity;
import com.rihao.property.modules.lease.contract.enums.BizType;
import com.rihao.property.modules.lease.contract.enums.ContractStatus;
import com.rihao.property.modules.lease.contract.enums.SignUpStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author wangyu
 * @since 2021-03-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("tb_contract_adjust")
public class ContractAdjust extends BaseEntity {

    private static final long serialVersionUID = 1L;


    @TableField("type")
    private String type; //调整类型 1、合同到期调整 2、合同中途终止

    /**
     * 调整事由
     */
    @TableField("remark")
    private String remark;


    /**
     * 附件id
     */
    @TableField("file_id")
    private Long fileId;


    //实际结束时间
    @TableField("estimate_close_date")
    private Date estimateCloseDate;

    //实际结束时间
    @TableField("contract_id")
    private Long contractId;
}
