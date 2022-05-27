package com.rihao.property.modules.estate.vo;

import com.rihao.property.modules.estate.enums.SplitAndMergeState;
import com.rihao.property.modules.estate.enums.UnitState;
import com.rihao.property.modules.lease.contract.vo.ContractVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
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
public class UnitVo extends UnitCreateVo {

    private Long id;
    private Long key;

    private Long parkId;
    private String parkName;
    private String buildingName;

    @ApiModelProperty("状态")
    private UnitState status;

    @ApiModelProperty("当前租赁公司")
    private String entName;

    @ApiModelProperty("当前租赁公司ID")
    private Long entId;

    private SplitAndMergeState splitAndMergeState;

    private List<ContractVo> contractVos;

    private List<SplitMergeHistoryVo> mergeHistories;
    private List<SplitMergeHistoryVo> splitHistories;
    //当前单元的修改历史
    private List<UnitUpdateLogVo> updateLogVos;
    private LocalDateTime createTime;
    private String createBy;
}