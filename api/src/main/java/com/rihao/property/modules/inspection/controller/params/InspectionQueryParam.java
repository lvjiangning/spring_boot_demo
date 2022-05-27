package com.rihao.property.modules.inspection.controller.params;

import com.rihao.property.common.page.PageParams;
import lombok.Data;

@Data
public class InspectionQueryParam extends PageParams {

    private String location;
    private String inspector;
    private String title;
    private String parkId;
    //模糊查询
    private String likeParam;
}
