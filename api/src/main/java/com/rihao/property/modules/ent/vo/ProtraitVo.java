package com.rihao.property.modules.ent.vo;

import com.rihao.property.modules.ent.entity.EntMatters;
import com.rihao.property.modules.ent.entity.EntPartner;
import com.rihao.property.modules.lease.contract.entity.Contract;
import com.rihao.property.modules.lease.contract.vo.ContractVo;
import io.swagger.annotations.ApiModel;
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
public class ProtraitVo implements Serializable {

    private Long id;
    private Long batchId;
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
    private LocalDateTime createTime;
    private List<EntPartner> partners;
    private List<EntMatters> matters;
    private List<ContractVo> contracts;
    private String usedNames;
    private String categoryNames;
}