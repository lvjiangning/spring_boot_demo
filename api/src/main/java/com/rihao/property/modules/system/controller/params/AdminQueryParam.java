package com.rihao.property.modules.system.controller.params;

import com.rihao.property.common.page.PageParams;
import lombok.Data;

/**
 * @author gaoy
 * 2020/2/29/029
 */
@Data
public class AdminQueryParam extends PageParams {
    private String username;
    private String realName;
    private Long role;
    private String establishId;
}
