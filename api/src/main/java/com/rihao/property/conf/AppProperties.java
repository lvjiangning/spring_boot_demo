

package com.rihao.property.conf;

import com.rihao.property.shiro.conf.ShiroProperties;
import com.rihao.property.shiro.jwt.JwtProperties;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.util.List;

/**
 * spring-boot-plus属性配置信息
 *
 * @author
 * @date 2019-08-04
 * @since 1.2.0-RELEASE
 */
@Data
@ConfigurationProperties(prefix = "app")
public class AppProperties {

    private String initPassword = "123456";
    /**
     * 允许上传的文件后缀集合
     */
    private List<String> allowUploadFileExtensions;
    /**
     * 允许下载的文件后缀集合
     */
    private List<String> allowDownloadFileExtensions;

    /**
     * JWT配置
     */
    @NestedConfigurationProperty
    private JwtProperties jwt;

    /**
     * Shiro配置
     */
    @NestedConfigurationProperty
    private ShiroProperties shiro = new ShiroProperties();


    /**
     * 项目静态资源访问配置
     *
     * @see AppConf addResourceHandlers
     */
    private String resourceHandlers;

}
