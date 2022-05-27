

package com.rihao.property.shiro.conf;

import lombok.Data;

/**
 * Shiro权限配置映射类
 *
 * @author
 * @date 2019-09-28
 * @since 1.3.0.RELEASE
 **/
@Data
public class ShiroPermissionProperties {

    /**
     * 路径
     */
    private String url;
    /**
     * 路径数组
     */
    private String[] urls;

    /**
     * 权限
     */
    private String permission;

}
