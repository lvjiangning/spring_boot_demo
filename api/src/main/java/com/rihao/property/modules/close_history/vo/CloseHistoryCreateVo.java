package com.rihao.property.modules.close_history.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

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
public class CloseHistoryCreateVo implements Serializable {

    private Long contractId;
    private String remark;
}