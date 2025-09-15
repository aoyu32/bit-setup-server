package com.aoyu.bitsetup.common.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName：MybatisPlusConfig
 * @Author: aoyu
 * @Date: 2025-09-15 14:07
 * @Description: mybatis plus配置类
 */

@Configuration
public class MybatisPlusConfig {
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 添加分页插件
        PaginationInnerInterceptor paginationInterceptor = new PaginationInnerInterceptor(DbType.MYSQL);
        paginationInterceptor.setMaxLimit(1000L); // 设置最大单页限制数量
//        paginationInterceptor.setOverflow(true); // 溢出总页数后是否进行处理
        interceptor.addInnerInterceptor(paginationInterceptor);
        return interceptor;
    }
}
