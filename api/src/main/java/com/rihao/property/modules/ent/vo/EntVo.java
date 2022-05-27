package com.rihao.property.modules.ent.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.rihao.property.modules.ent.entity.EntMatters;
import com.rihao.property.modules.ent.entity.EntPartner;
import com.rihao.property.modules.lease.contract.entity.Contract;
import com.rihao.property.modules.lease.contract.vo.ContractVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 视图实体
 * </p>
 *
 * @author wangyu
 * @date 2021-03-26
 * @description
 */
@Data
@ApiModel
@Accessors(chain = true)
public class EntVo implements Serializable {

    private Long id;
    private Long batchId;
    @ApiModelProperty("企业名称")
    private String name;
    @ApiModelProperty("统一社会信用代码")
    private String unifiedSocialCreditCode;
    @ApiModelProperty("入驻时间")
    private String leaseStartDate;//这个企业最早合同的签订时间
    @ApiModelProperty("主要从事行业类别")
    private String mainIndustry;
    @ApiModelProperty("单位注册资本(万元)")
    private String registeredCapital;
    @ApiModelProperty("注册时间")
    private String registrationTime;
    @ApiModelProperty("注册所在区")
    private String registeredArea;
    @ApiModelProperty("注册所在街道")
    private String registeredStreet;
    @ApiModelProperty("登记注册类型")
    private String registrationType;
    @ApiModelProperty("法人代表")
    private String legal;
    @ApiModelProperty("法人电话")
    private String legalPhoneNumber;
    private String legalIDNumber;
    @ApiModelProperty("联系人")
    private String contact;
    @ApiModelProperty("联系人电话")
    private String contactPhoneNumber;
    private String contactIDNumber;
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
    @ApiModelProperty("创建人")
    private String createBy;
    //股东
    private List<EntPartner> partners;
    private String[] categorys;
    private  List<ContractVo> contracts;
    private  List<EntMatters> matters;
    private String actualControllerName;
    private String actualControllerPhone;

    private String categoryNames;//企业领域
    private String usedName;//曾用名
}