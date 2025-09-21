package com.aoyu.bitsetup.common.utils;

import com.aoyu.bitsetup.common.constants.RedisKeyConstant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @ClassName：RedisKeyUtil
 * @Author: aoyu
 * @Date: 2025-09-20 16:46
 * @Description: redis key生成工具类
 */

@Component
public class RedisKeyUtil {

    // 项目前缀，通常从配置文件中读取
    @Value("${spring.application.name:app}")
    private String appName;

    /**
     * 生成标准的Redis Key
     */
    public String generateKey(String module, String business, String id) {
        return String.format("%s:%s:%s:%s", appName, module, business, id);
    }

    public String generateKey(String module, String business) {
        return String.format("%s:%s:%s", appName, module, business);
    }

    public String generateUserAuthKey(String id) {
        return generateKey(RedisKeyConstant.AUTH,"captcha",id);
    }

}
