package com.rihao.property.modules.system.controller.params;

import com.rihao.property.common.page.PageParams;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @author gaoy
 * 2020/2/28/028
 */
@ApiModel
@Data
public class RoleQueryParam extends PageParams {
    private String name;
}
