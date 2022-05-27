package com.rihao.property.modules.estate.controller.params;

import com.rihao.property.common.page.PageParams;
import lombok.Data;

@Data
public class UnitQueryParam extends PageParams {

    private Long parkId;
    private Long floorId;
    private String status;
    private Long buildingId;
    private String unitNo;

    /**
     * 模糊查询
     */
    private String likeParam;
}
