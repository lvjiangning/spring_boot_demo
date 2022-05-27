package com.rihao.property.modules.ent.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel
@Accessors(chain = true)
public class EntCreateVo implements Serializable {
    private Long batchId;
    private Long batchDetailId;
    private String name;
    private String mainIndustry;
    private String registeredCapital;
    private String registeredArea;
    private String registrationTime;
    private String registeredStreet;
    private String unifiedSocialCreditCode;
    private String registrationType;
    private String legal;
    private String legalPhoneNumber;
    private String legalIDNumber;
    private String contact;
    private String contactPhoneNumber;
    private String contactIDNumber;
    private List<PartnerVo> partners;
    private String[] categorys;
    private String actualControllerName;
    private String actualControllerPhone;
    private  Long parkId;//园区id
}
