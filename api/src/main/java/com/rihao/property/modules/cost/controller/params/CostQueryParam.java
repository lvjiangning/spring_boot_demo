package com.rihao.property.modules.cost.controller.params;

import com.rihao.property.common.page.PageParams;
import lombok.Data;

@Data
public class CostQueryParam extends PageParams {

    private String entName;
    private String costTime;
    private String code;
    private Integer status;
}
