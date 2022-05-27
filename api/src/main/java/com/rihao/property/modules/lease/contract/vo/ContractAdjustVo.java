package com.rihao.property.modules.lease.contract.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.rihao.property.common.vo.KeyValueVo;
import com.rihao.property.modules.close_history.vo.ContractCloseHistoryVo;
import com.rihao.property.modules.config.entity.SysFile;
import com.rihao.property.modules.estate.vo.BuildingKeyValueVo;
import com.rihao.property.modules.estate.vo.UnitVo;
import com.rihao.property.modules.lease.contract.enums.BizType;
import com.rihao.property.modules.lease.contract.enums.SignUpStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
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
public class ContractAdjustVo implements Serializable {

    private Long id;
    @ApiModelProperty("合同id")
    private Long contractId;

    @ApiModelProperty("调整类型 1、合同到期终止 2、合同中途终止")
    private String type; //调整类型 1、合同到期终止 2、合同中途终止

    /**
     * 调整事由
     */
    @ApiModelProperty("调整事由")
    private String remark;

    /**
     * 附件id
     */
    @ApiModelProperty("提交时使用")
    private Long fileId;

    /**
     * 附件id
     */
    @ApiModelProperty("调整的附件,查看时使用")
    private SysFile sysFile;


    //实际结束时间
    @ApiModelProperty("实际结束时间")
    private Date estimateCloseDate;


    @ApiModelProperty("创建人")
    private String createBy;
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
}