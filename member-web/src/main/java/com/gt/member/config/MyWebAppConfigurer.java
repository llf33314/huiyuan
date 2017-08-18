package com.gt.member.config;

import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 静态文件访问配置
 * User : pengjiangli
 * Date : 2017/7/21 0021
 * Time : 9:29
 */
public class MyWebAppConfigurer extends WebMvcConfigurerAdapter {

    @Override
    public void addResourceHandlers( ResourceHandlerRegistry registry ) {
	registry.addResourceHandler( "/css/**" ).addResourceLocations( "classpath:/resources/" );
	registry.addResourceHandler( "/js/**" ).addResourceLocations( "classpath:/resources/" );
	registry.addResourceHandler( "/images/**" ).addResourceLocations( "classpath:/resources/" );
    }
}
