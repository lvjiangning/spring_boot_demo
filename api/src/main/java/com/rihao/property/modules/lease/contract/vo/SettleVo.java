package com.rihao.property.modules.lease.contract.vo;

import com.rihao.property.modules.close_history.enums.SettleTypeEnum;
import com.rihao.property.modules.ent.vo.PartnerVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Data
public class SettleVo implements Serializable {
    @ApiModelProperty("企业名称")
    private String name;
    @ApiModelProperty("统一社会信用代码")
    private String unifiedSocialCreditCode;
    @ApiModelProperty("主要从事行业类别")
    private String mainIndustry;
    @ApiModelProperty("单位注册资本(万元)")
    private String registeredCapital;
    @ApiModelProperty("注册所在区")
    private String registeredArea;
    @ApiModelProperty("注册时间")
    private String registrationTime;
    @ApiModelProperty("注册所在街道")
    private String registeredStreet;
    @ApiModelProperty("登记注册类型")
    private String registrationType;
    @ApiModelProperty("法人")
    private String legal;
    private String legalPhoneNumber;
    private String legalIDNumber;
    private String contact;
    private String contactPhoneNumber;
    private String contactIDNumber;
    private List<PartnerVo> partners;
    private String[] categorys;
    @ApiModelProperty("合同编号")
    private String code;

    @ApiModelProperty("签订类型")
    private String changeType;

    @ApiModelProperty("租赁公司ID")
    private Long entId;

    @ApiModelProperty("楼栋ID")
    private Long buildingId;

    @ApiModelProperty("签订日期")
    private LocalDate signDate;

    @ApiModelProperty("起租时间")
    private LocalDate leaseStartDate;

    @ApiModelProperty("合同结束时间")
    private LocalDate dueDate;

    @ApiModelProperty("租期")
    private String leaseTerm;

    @ApiModelProperty("价格")
    private String price;

    @ApiModelProperty("单元")
    private Long[] unitIds;

    @ApiModelProperty("单元名称数组")
    private String[] unitNames;

    @ApiModelProperty("面积")
    private String area;

    @ApiModelProperty("单元字符串")
    private String unit;

    @ApiModelProperty("租金/月")
    private String rent;

    @ApiModelProperty("免租期（月）")
    private String rentFreePeriod;

    @ApiModelProperty("合同签订类型")
    private SettleTypeEnum settleType;
}
