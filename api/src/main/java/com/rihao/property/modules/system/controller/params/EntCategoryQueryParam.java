package com.rihao.property.modules.system.controller.params;

import com.rihao.property.common.page.PageParams;
import lombok.Data;

@Data
public class EntCategoryQueryParam extends PageParams {

    private String name;
}
