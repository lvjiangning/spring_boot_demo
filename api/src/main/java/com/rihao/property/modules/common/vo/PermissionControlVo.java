package com.rihao.property.modules.common.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author Ken
 * @date 2020/6/16
 * @description
 */
@Data
@ApiModel
@Accessors(chain = true)
public class PermissionControlVo {

    /**
     * 是否是超级管理员（role_id为1）
     */
    private Long roleId;

    /**
     * 非管理员拥有的组织权限
     */
    private long[] organizationIds = new long[0];

}
