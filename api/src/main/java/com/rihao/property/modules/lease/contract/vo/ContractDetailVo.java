package com.rihao.property.modules.lease.contract.vo;

import com.rihao.property.common.vo.KeyValueVo;
import com.rihao.property.modules.ent.vo.PartnerVo;
import com.rihao.property.modules.estate.enums.UnitState;
import com.rihao.property.modules.estate.enums.UnitUseType;
import com.rihao.property.modules.lease.contract.enums.ContractStatus;
import com.rihao.property.modules.lease.contract.enums.SignUpStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ContractDetailVo {
    private String createBy;
    private LocalDateTime createTime;
    private Long id;
    private Long entId;
    @ApiModelProperty("企业名称")
    @NotNull(message = "企业名称")
    private String name;
    private List<Long> unitIds;
    private String code;
    @ApiModelProperty("统一社会信用代码")
    private String unifiedSocialCreditCode;
    @ApiModelProperty("实际控股人")
    private String actualControllerName;
    @ApiModelProperty("实际控股人电话")
    private String actualControllerPhone;
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
    private KeyValueVo[] categorys;
    @ApiModelProperty("园区ID")
    private Long parkId;
    private String parkName;

    private SignUpStatus signUpStatus;
    private String contractStatus;

    @ApiModelProperty("签订日期")
    private String signDate;

    @ApiModelProperty("起租时间")
    private String leaseStartDate;

    @ApiModelProperty("合同结束时间")
    private String dueDate;

    @ApiModelProperty("合同实际关闭日期")
    private String estimateCloseDate;

    @ApiModelProperty("租期")
    private String leaseTerm;

    @ApiModelProperty("价格")
    private BigDecimal price;

    @ApiModelProperty("面积")
    private String area;

    @ApiModelProperty("租金/月")
    private String rent;

    @ApiModelProperty("免租期（月）")
    private String rentFreePeriod;
    @ApiModelProperty("单元")
    private List<Unit> units;
    @ApiModelProperty("批复文件")
    private List<SettleContractFilesVo.ContractFile> approveFiles;
    @ApiModelProperty("合同文件")
    private List<SettleContractFilesVo.ContractFile> contractFiles;
    private String unitName;
    @ApiModelProperty("楼栋名称")
    private String buildingNames;

    private ContractAdjustVo contractAdjustVo;
    @Data
    public static class Unit {
        private Long id;
        @ApiModelProperty("楼栋ID")
        private Long buildingId;
        @ApiModelProperty("楼栋名称")
        private String buildingName;
        @ApiModelProperty("单元号")
        private String unitNo;
        @ApiModelProperty("楼层号")
        private String floorNo;
        @ApiModelProperty("使用面积")
        private String usableArea;

        @ApiModelProperty("建筑面积")
        private String builtUpArea;

        @ApiModelProperty("自用、公用、物业用房、租赁")
        private UnitUseType useType;
        @ApiModelProperty("房屋状态")
        private UnitState status;
        private String createBy;
        private LocalDateTime createTime;
    }
}
