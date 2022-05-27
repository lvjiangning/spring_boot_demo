package com.rihao.property.modules.lease.contract.vo;

import com.rihao.property.common.vo.KeyValueVo;
import com.rihao.property.modules.close_history.enums.CloseTypeEnum;
import com.rihao.property.modules.close_history.vo.ContractCloseHistoryVo;
import com.rihao.property.modules.estate.vo.BuildingKeyValueVo;
import com.rihao.property.modules.estate.vo.UnitVo;
import com.rihao.property.modules.lease.contract.enums.BizType;
import com.rihao.property.modules.lease.contract.enums.SignUpStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 视图实体
 * </p>
 *
 * @author wangyu
 * @description
 * @since 2021-03-28
 */
@Data
@ApiModel
@Accessors(chain = true)
public class ContractVo extends ContractCreateVo {

    private Long id;

    @ApiModelProperty("租赁公司")
    private String entName;

    @ApiModelProperty("租赁的园区名称")
    private String parkName;

    @ApiModelProperty("剩余时间")
    private String leftTime;

    @ApiModelProperty("合同状态")
    private String status;

    @ApiModelProperty("历史租赁年限")
    private String historyYears;

    @ApiModelProperty("剩余到期时间")
    private String intervalMonth;

    @ApiModelProperty("合同序列号，用来标识修改队列")
    private String sequence;

    private String createTime;
    private String createBy;

    @ApiModelProperty("旧合同关闭类型")
    private BizType closeType;

    @ApiModelProperty("合同地址")
    private String contractFilePath;

    @ApiModelProperty("入驻通知地址")
    private String settleFilePath;

    @ApiModelProperty("OA地址")
    private String oaFilePath;

    @ApiModelProperty("合同关闭原因")
    private String closeReason;

    @ApiModelProperty("关闭历史")
    private ContractCloseHistoryVo contractCloseHistory;

    @ApiModelProperty("关闭时间")
    private LocalDateTime closeTime;

    @ApiModelProperty("关闭所剩时间")
    private String intervalDay;
    @ApiModelProperty("园区用途")
    private String parkUsed;

    @ApiModelProperty("是否已计划关闭")
    private Boolean estimateClose;

    @ApiModelProperty("计划关闭时间")
    private String estimateCloseDate;
    @ApiModelProperty("业务类型")
    private BizType bizType;
    @ApiModelProperty("合同办理进度")
    private SignUpStatus signUpStatus;
    private List<KeyValueVo> parks;
    private List<BuildingKeyValueVo> buildings;
    private List<UnitVo> unitVos;
    @ApiModelProperty("是否允许调整，true=可以调整，false=不允许调整")
    private Boolean adjust;
    @ApiModelProperty("合同园区的产权单位")
    private String establish;
    //单元id,用于报表中心用房明细表
    private Long unitId;

}