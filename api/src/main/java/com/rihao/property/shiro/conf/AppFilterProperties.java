

package com.rihao.property.shiro.conf;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * Filter配置属性
 *
 * @author
 * @date 2019-09-29
 * @since 1.3.0.RELEASE
 **/
@Data
@ConfigurationProperties(prefix = "app.filter")
public class AppFilterProperties {

    /**
     * 请求路径Filter配置
     */
    @NestedConfigurationProperty
    private FilterConfig requestPath = new FilterConfig();

    @Data
    public static class FilterConfig {

        /**
         * 是否启用
         */
        private boolean enabled;

        /**
         * 包含的路径
         */
        private String[] includePaths;

        /**
         * 排除路径
         */
        private String[] excludePaths;

    }
}
