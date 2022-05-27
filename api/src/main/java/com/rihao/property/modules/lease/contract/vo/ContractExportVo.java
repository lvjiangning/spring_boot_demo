package com.rihao.property.modules.lease.contract.vo;

import com.rihao.property.common.vo.KeyValueVo;
import com.rihao.property.modules.close_history.vo.ContractCloseHistoryVo;
import com.rihao.property.modules.estate.entity.Building;
import com.rihao.property.modules.estate.entity.Unit;
import com.rihao.property.modules.estate.vo.BuildingKeyValueVo;
import com.rihao.property.modules.estate.vo.UnitVo;
import com.rihao.property.modules.lease.contract.enums.BizType;
import com.rihao.property.modules.lease.contract.enums.SignUpStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 合同列表导出vo
 * </p>
 *
 * @author wangyu
 * @description
 * @since 2021-03-28
 */
@Data
@ApiModel
@Accessors(chain = true)
public class ContractExportVo implements Serializable {

    @ApiModelProperty("合同编号")
    private String code;

    @ApiModelProperty("合同状态")
    private String status;

    @ApiModelProperty("承租单位")
    private String entName;

    @ApiModelProperty("园区名称")
    private String parkName;

    @ApiModelProperty("楼栋名称")
    private String buildingNames;

    @ApiModelProperty("单元名称")
    private String unitNames;

    @ApiModelProperty("建筑面积（㎡）")
    private String area;

    @ApiModelProperty("租金（月）")
    private String rent;

    @ApiModelProperty("合同生效日期")
    private String leaseStartDate;

    @ApiModelProperty("合同结束时间")
    private String dueDate;

    @ApiModelProperty("实际结束日期")
    private String estimateCloseDate;

    @ApiModelProperty("业务类型")
    private String bizType;
    @ApiModelProperty("创建时间")
    private String createTime;
    @ApiModelProperty("创建人")
    private String createBy;


}