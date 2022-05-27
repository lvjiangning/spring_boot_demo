package com.rihao.property.modules.report.vo;

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
public class LandUseReportVo implements Serializable {

    private String code;
    private String buildingName;
    private String unitNo;
    private String builtUpArea;
    private String leaseTerm;
    private String approvalDate;
    private String entName;
}