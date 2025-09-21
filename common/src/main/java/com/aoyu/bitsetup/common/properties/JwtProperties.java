package com.aoyu.bitsetup.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @ClassName：JwtProperties
 * @Author: aoyu
 * @Date: 2025-09-20 13:34
 * @Description: jwt配置属性类
 */

@Component
@ConfigurationProperties(prefix = "jwt")
@Data
public class JwtProperties {

    private String secret;
    private String expiration;


}
