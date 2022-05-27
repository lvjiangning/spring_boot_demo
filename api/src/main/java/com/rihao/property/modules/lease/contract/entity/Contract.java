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
@TableName("tb_contract")
public class Contract extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 合同系列编号
     */
    @TableField("sequence")
    private String sequence;

    /**
     * 合同编号
     */
    @TableField("code")
    private String code;

    /**
     * 前置合同编号
     */
    @TableField("pre_code")
    private String preCode;

    @TableField("ent_id")
    private Long entId;

    @TableField("ent_name")
    private String entName;
    /**
     * 租赁的楼栋名称
     */
    @TableField("building_name")
    private String buildingName;


    @TableField("park_id")
    private Long parkId;

    /**
     * 联系人
     */
    @TableField("contact")
    private String contact;

    /**
     * 联系人电话
     */
    @TableField("contact_phone_number")
    private String contactPhoneNumber;

    /**
     * 法人
     */
    @TableField("legal")
    private String legal;

    /**
     * 法人电话
     */
    @TableField("legal_phone_number")
    private String legalPhoneNumber;

    /**
     * 签订日期
     */
    @TableField("sign_date")
    private String signDate;

    /**
     * 租期
     */
    @TableField("lease_term")
    private String leaseTerm;

    /**
     * 起租时间
     */
    @TableField("lease_start_date")
    private String leaseStartDate;

    /**
     * 价格
     */
    @TableField("price")
    private BigDecimal price;

    /**
     * 合同状态
     */
    @TableField("status")
    private ContractStatus status = ContractStatus.wait_contract;

    /**
     * 入驻通知详细ID
     */
    @TableField("batch_detail_id")
    private Long batchDetailId;

    /**
     * 租金（月）
     */
    @TableField("rent")
    private String rent;

    /**
     * 押金月数
     */
    @TableField("deposit_month")
    private String depositMonth;

    /**
     * 押金
     */
    @TableField("deposit")
    private String deposit;

    /**
     * 押金状态 0 未交 1 已缴
     */
    @TableField("deposit_status")
    private int depositStatus;

    /**
     * 单元面积
     */
    @TableField("area")
    private String area;

    /**
     * 显示到前端的单元信息
     */
    @TableField("unit")
    private String unit;

    /**
     * 免租期（月）
     */
    @TableField("rentFreePeriod")
    private String rentFreePeriod;

    /**
     * 到期时间
     */
    @TableField("due_date")
    private String dueDate;

    /**
     * 单位ID
     */
    @TableField("establish_id")
    private Long establishId;

    /**
     * 合同文件地址
     */
    @TableField("contract_file_path")
    private String contractFilePath;

    /**
     * 入驻通知地址
     */
    @TableField("settle_file_path")
    private String settleFilePath;

    /**
     * OA地址
     */
    @TableField("oa_file_path")
    private String oaFilePath;

    @TableField("estimate_close")
    private Boolean estimateClose = false;

    //实际结束时间
    @TableField("estimate_close_date")
    private String estimateCloseDate;

    /**
     * 业务类型
     */
    @TableField("biz_type")
    private BizType bizType;
    /**
     * 签约状态
     */
    @TableField("sign_up_status")
    private SignUpStatus signUpStatus;

    /**
     * 楼栋ids通过,分割
     */
    @TableField("building_ids")
    private String buildingIds;

}
