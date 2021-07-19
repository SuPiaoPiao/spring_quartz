package com.example.quartz.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: sujing
 * @version: 1.0.0
 * @ClassName: MybatisPlusConfig.java
 * @date: 2021/6/30 18:37
 */
@Configuration
public class MybatisPlusConfig {

    /**
     * mybatisPlus注册分页插件
     * @return
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }
}