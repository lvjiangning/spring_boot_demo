package com.rihao.property.modules.system.controller.params;

import com.rihao.property.common.page.PageParams;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Ken
 * @date 2020/5/18
 * @description
 */
@Data
@ApiModel
public class OrganizationQueryParam extends PageParams {

    @ApiModelProperty("单位名称")
    private String name;

    @ApiModelProperty("单位编码")
    private String code;

}
