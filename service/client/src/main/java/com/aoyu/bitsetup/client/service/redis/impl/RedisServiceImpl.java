package com.aoyu.bitsetup.client.service.redis.impl;

import com.aoyu.bitsetup.client.service.redis.RedisService;
import com.aoyu.bitsetup.common.utils.RedisKeyUtil;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

/**
 * @ClassName：RedisServiceImpl
 * @Author: aoyu
 * @Date: 2025-09-20 16:54
 * @Description: redis服务层实现
 */

@Service
@AllArgsConstructor
public class RedisServiceImpl implements RedisService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final RedisKeyUtil redisKeyUtil;


    @Override
    public void setEmailCaptcha(String email,String code) {
        redisTemplate.opsForValue().set(redisKeyUtil.generateUserAuthKey(email),code, Duration.ofMinutes(5));
    }

    @Override
    public String getEmailCaptcha(String email) {
        String key = redisKeyUtil.generateUserAuthKey(email);
        return (String) redisTemplate.opsForValue().get(key);
    }

    @Override
    public void deleteEmailCaptcha(String email) {
        redisTemplate.delete(redisKeyUtil.generateUserAuthKey(email));
    }
}
