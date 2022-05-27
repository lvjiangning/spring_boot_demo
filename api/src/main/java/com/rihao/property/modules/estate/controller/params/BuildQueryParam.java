package com.rihao.property.modules.estate.controller.params;

import com.rihao.property.common.page.PageParams;
import lombok.Data;

@Data
public class BuildQueryParam extends PageParams {

    private String name;
    private Long parkId;

    private String parkIds;
    private String likeParam;
}
