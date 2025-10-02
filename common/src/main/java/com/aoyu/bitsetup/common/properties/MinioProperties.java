package com.aoyu.bitsetup.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @ClassName：MinioProperties
 * @Author: aoyu
 * @Date: 2025-10-01 16:03
 * @Description: minio配置属性类
 */

@Data
@Component
@ConfigurationProperties(prefix = "minio")
public class MinioProperties {
    private String endpoint;
    private String accessKey;
    private String secretKey;
    private String bucketName;
    private boolean secure = false;
}
