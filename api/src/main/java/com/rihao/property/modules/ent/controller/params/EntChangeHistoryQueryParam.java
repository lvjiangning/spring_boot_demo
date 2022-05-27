package com.rihao.property.modules.ent.controller.params;

import com.rihao.property.common.page.PageParams;
import lombok.Data;

@Data
public class EntChangeHistoryQueryParam extends PageParams {
    private Long entId;
}
