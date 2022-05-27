package com.rihao.property.modules.lease.contract.vo;

import com.rihao.property.modules.ent.vo.PartnerVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@ApiModel("企业入驻基本资料")
public class SettleInBaseInfoVo implements Serializable {
    private Long id;
    @ApiModelProperty("企业名称")
    @NotNull(message = "企业名称")
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
    @ApiModelProperty("股东信息")
    private List<PartnerVo> partners;
    @ApiModelProperty("领域")
    private String[] categorys;
    @ApiModelProperty("园区ID")
    private Long parkId;
    @ApiModelProperty("楼栋IDs")
    private List<Long> buildingIdList;
    @ApiModelProperty("签订日期")
    private String signDate;

    @ApiModelProperty("起租时间")
    private String leaseStartDate;

    @ApiModelProperty("合同结束时间")
    private String dueDate;

    @ApiModelProperty("租期")
    private String leaseTerm;

    @ApiModelProperty("价格")
    private BigDecimal price;

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
    @ApiModelProperty("是否提交")
    private boolean submit = false;
    private String infoComplete; // yes no
    @ApiModelProperty("实际控股人")
    private String actualControllerName;
    @ApiModelProperty("实际控股人电话")
    private String actualControllerPhone;
}
