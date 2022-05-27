package com.rihao.property.modules.close_history.entity;

import com.rihao.property.common.BaseEntity;
import com.rihao.property.modules.close_history.enums.CloseTypeEnum;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.rihao.property.modules.lease.contract.enums.BizType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 *
 * </p>
 *
 * @author wangyu
 * @since 2021-03-31
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("tb_contract_close_history")
public class ContractCloseHistory extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableField("contract_id")
    private Long contractId;

    /**
     * 关闭或者恢复理由
     */
    @TableField("remark")
    private String remark;

    /**
     * 关闭类型
     */
    @TableField("Close_type")
    private BizType closeType;

    /**
     * 关闭的OA附件
     */
    @TableField("oa_file_url")
    private String oaFileUrl;

    /**
     * 计划关闭时间
     */
    @TableField("estimate_close_date")
    private String estimateCloseDate;

    /**
     * 是否已经关闭
     */
    @TableField("close_result")
    private Boolean closeResult = false;
}
