package com.rihao.property.modules.estate.entity;

import com.rihao.property.common.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.rihao.property.common.enums.EnableState;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 *
 * </p>
 *
 * @author wangyu
 * @since 2021-04-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("tb_purchase_record")
public class PurchaseRecord extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 园区
     */
    @TableField("park_id")
    private Long parkId;

    /**
     * 中标公司名称
     */
    @TableField("winning_company")
    private String winningCompany;

    /**
     * 中标公司联系人
     */
    @TableField("contact")
    private String contact;

    /**
     * 联系方式
     */
    @TableField("phone_number")
    private String phoneNumber;

    /**
     * 管理费
     */
    @TableField("management_fee")
    private String managementFee;

    /**
     * 招标时间
     */
    @TableField("bidding_date")
    private String biddingDate;

    /**
     * 中标时间
     */
    @TableField("winning_date")
    private String winningDate;

    /**
     * 签约时间
     */
    @TableField("sign_date")
    private String signDate;

    /**
     * 文件地址
     */
    @TableField("file_path")
    private String filePath;

    /**
     *  合同生效日期
     */
    @TableField("contract_start_date")
    private String contractStartDate;

    /**
     * 合同结束日期
     */
    @TableField("contract_end_date")
    private String contractEndDate;

    @TableField("state")
    private EnableState state = EnableState.enable;
}
