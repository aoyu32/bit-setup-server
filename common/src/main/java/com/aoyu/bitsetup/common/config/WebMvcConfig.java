package com.aoyu.bitsetup.common.config;

import com.aoyu.bitsetup.common.interceptor.AuthInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @ClassName：WebMvcConfig
 * @Author: aoyu
 * @Date: 2025-09-13 14:07
 * @Description: web mvc配置类
 */

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private AuthInterceptor authInterceptor;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:5173/")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/api/**")  // 拦截的请求路径，所有需要 token 的接口
                .excludePathPatterns(
                        "/api/app/**",
                        "/api/category/**",
                        "/api/search/**",
                        "/api/detail/**",
                        "/api/auth/login",
                        "/api/auth/register",
                        "/api/auth/code",
                        "/api/captcha/**",
                        "/api/post/hot",
                        "/api/post/recommend",
                        "/api/post/detail/**",
                        "/api/post/list/**"

                );

    }

}

