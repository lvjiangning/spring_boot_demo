package com.rihao.property.modules.ent.controller.params;

import lombok.Data;
import org.apache.shiro.util.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "app.aliyun-oss")
public class AliyunOssProperties {
    private String accessKeyId;
    private String secret;
    private String bucket;
    private String endpoint;
    private int expireTime = 3600 * 24 * 3;  //60s

    public String getBucketHost() {
        return String.join(".", bucket, endpoint);
    }

    public boolean isValid() {
        return StringUtils.hasText(accessKeyId) && StringUtils.hasText(secret) && StringUtils.hasText(bucket) && StringUtils.hasText(endpoint);
    }
}
