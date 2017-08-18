package com.gt.member.config;

import com.baomidou.mybatisplus.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Mybatis-plus 配置类
 *
 * @author zhangmz
 * @create 2017/6/20
 */
@MapperScan( "com.gt.member.dao" )
@Configuration
public class MybatisPlusConfig {

    /**
     * mybatis-plus分页插件
     *
     * @return PaginationInterceptor
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }
}
