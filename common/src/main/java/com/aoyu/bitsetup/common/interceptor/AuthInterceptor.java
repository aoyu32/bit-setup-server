package com.aoyu.bitsetup.common.interceptor;

import com.aoyu.bitsetup.common.enumeration.ResultCode;
import com.aoyu.bitsetup.common.exception.BusinessException;
import com.aoyu.bitsetup.common.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * @ClassName：AuthInterceptor
 * @Author: aoyu
 * @Date: 2025-09-21 14:47
 * @Description: token认证拦截器
 */
@Component
@Slf4j
public class AuthInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 从请求头获取 token
        String token = request.getHeader("Authorization");

        if (token == null || !token.startsWith("Bearer ")) {
            return false; // 拦截请求
        }

        token = token.substring(7); // 去掉 "Bearer " 前缀

        log.info("携带的Token：{}",token);


        //验证是否过期
        boolean tokenExpired = jwtUtil.isTokenExpired(token);
        if (tokenExpired) {
            log.info("携带的Token已经过期");
            throw new BusinessException(ResultCode.TOKEN_EXPIRATION);
        }

        boolean valid = jwtUtil.validateToken(token);

        if (!valid) {
            log.info("携带的Token无效");
            throw new BusinessException(ResultCode.TOKEN_UN_VALID);
        }

        log.info("携带的Token有效");

        return true;
    }

}
