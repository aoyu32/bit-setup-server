package com.aoyu.bitsetup.client.service.redis;

/**
 * @InterfaceName：RedisService
 * @Author: aoyu
 * @Date: 2025/9/20 下午4:54
 * @Description:
 */

public interface RedisService {

    void  setEmailCaptcha(String email, String captcha);

    String getEmailCaptcha(String email);

    void deleteEmailCaptcha(String key);

}
