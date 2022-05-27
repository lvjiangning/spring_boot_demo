package com.rihao.property.modules.estate.controller.params;

import com.rihao.property.common.page.PageParams;
import lombok.Data;

@Data
public class FloorQueryParam extends PageParams {

    private String floorNo;
    private Long buildingId;
}
