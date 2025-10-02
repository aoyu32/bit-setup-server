package com.aoyu.bitsetup.common.config;

import com.aoyu.bitsetup.common.properties.MinioProperties;
import io.minio.MinioClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName：MinioConfig
 * @Author: aoyu
 * @Date: 2025-10-01 16:02
 * @Description: minio配置类
 */

@Slf4j
@Configuration
public class MinioConfig {

    private final MinioProperties minioProperties;

    @Autowired
    public MinioConfig(MinioProperties minioProperties) {
        this.minioProperties = minioProperties;
    }

    @Bean
    public MinioClient minioClient() {
        try {
            MinioClient minioClient = MinioClient.builder()
                    .endpoint(minioProperties.getEndpoint())
                    .credentials(minioProperties.getAccessKey(), minioProperties.getSecretKey())
                    .build();
            log.info("MinIO 客户端初始化成功");
            return minioClient;
        } catch (Exception e) {
            log.error("MinIO 客户端初始化失败", e);
            throw new RuntimeException("MinIO 初始化失败", e);
        }
    }
}