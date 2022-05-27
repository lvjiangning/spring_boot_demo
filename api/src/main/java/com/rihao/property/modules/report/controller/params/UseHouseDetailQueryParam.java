package com.rihao.property.modules.report.controller.params;

import com.rihao.property.common.page.PageParams;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class UseHouseDetailQueryParam extends PageParams {
    //查询的园区
    private Long parkId;


    //合同有效开始时间
    private Date startDate;
    //当前登录人员 所能查看园区
    private String parkIds;
    //单元id
    private List<Long> unitIds;
}
