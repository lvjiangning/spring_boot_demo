package com.rihao.property.modules.ent.entity;

import com.rihao.property.common.BaseEntity;
import com.rihao.property.modules.ent.enums.NewSettleStatus;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.rihao.property.modules.ent.enums.SignupStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * 企业信息
 * </p>
 *
 * @author wangyu
 * @since 2021-03-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("tb_ent")
public class Ent extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableField("id")
    private Long id;

    @TableField("batch_id")
    private Long batchId;
    //企业名称
    @TableField("name")
    private String name = "";

    @TableField("main_industry")
    private String mainIndustry= "";

    @TableField("registered_capital")
    private String registeredCapital= "";

    @TableField("registered_area")
    private String registeredArea= "";

    @TableField("registration_time")
    private String registrationTime= "";

    @TableField("registered_street")
    private String registeredStreet= "";

    @TableField("unified_social_credit_code")
    private String unifiedSocialCreditCode= "";

    @TableField("registration_type")
    private String registrationType= "";
    //法人
    @TableField("legal")
    private String legal;
    //法人手机号
    @TableField("legal_phone_number")
    private String legalPhoneNumber;

    @TableField("legal_id_number")
    private String legalIDNumber= "";
    //联系人
    @TableField("contact")
    private String contact;
    //联系人号码
    @TableField("contact_phone_number")
    private String contactPhoneNumber;

    @TableField("contact_id_number")
    private String contactIDNumber= "";

    @TableField("new_settle")
    private NewSettleStatus newSettle = NewSettleStatus.yes;

    @TableField("category_ids")
    private String categoryIds;

    @TableField("signup_status")
    private SignupStatus signupStatus;

    @TableField("settle_time")
    private String settleTime;

    @TableField("actual_controller_name")
    private String actualControllerName;

    @TableField("actual_controller_phone")
    private String actualControllerPhone;

    @TableField("park_id")
    private Long parkId;

}
