package com.rihao.property.modules.close_history.vo;

import com.rihao.property.modules.close_history.enums.CloseTypeEnum;
import com.rihao.property.modules.lease.contract.enums.BizType;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 视图实体
 * </p>
 *
 * @author wangyu
 * @since 2021-03-31
 * @description
 */
@Data
@ApiModel
@Accessors(chain = true)
public class ContractCloseHistoryVo implements Serializable {

    private Long id;

    private Long contractId;
    private String remark;
    private BizType closeType;
    private String closeTypeString;
    private String createBy;
    private LocalDateTime createTime;
}