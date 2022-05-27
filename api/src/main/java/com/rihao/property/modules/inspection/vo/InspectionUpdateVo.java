package com.rihao.property.modules.inspection.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@ApiModel
@Accessors(chain = true)
public class InspectionUpdateVo extends InspectionCreateVo {

    private Long id;
}
