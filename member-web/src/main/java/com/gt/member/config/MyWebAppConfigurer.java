package com.gt.member.config;

import com.gt.member.config.filter.ApiInterceptor;
import com.gt.member.config.filter.NotdoInterceptor;
import com.gt.member.config.filter.PhoneInterceptor;
import com.gt.member.config.filter.SysLogInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 静态文件访问配置
 * User : pengjiangli
 * Date : 2017/7/21 0021
 * Time : 9:29
 */
@Configuration
public class MyWebAppConfigurer extends WebMvcConfigurerAdapter {

    @Override
    public void addResourceHandlers( ResourceHandlerRegistry registry ) {
	registry.addResourceHandler( "/css*" ).addResourceLocations( "classpath:/resources/" );
	registry.addResourceHandler( "/js*" ).addResourceLocations( "classpath:/resources/" );
	registry.addResourceHandler( "/images*" ).addResourceLocations( "classpath:/resources/" );
    }
    /**
     * 注册拦截器
     * @param registry
     */
    @Override
    public void addInterceptors( InterceptorRegistry registry ) {
        // 针对 memberAPI 的拦截器
	registry.addInterceptor( new ApiInterceptor() ).addPathPatterns( "/memberAPI/**" );
        // 定义多个 拦截器
        registry.addInterceptor( new SysLogInterceptor() ).addPathPatterns( "/memberPc/**" );
	registry.addInterceptor( new PhoneInterceptor() ).addPathPatterns( "/memberPhone/**" );
	registry.addInterceptor( new NotdoInterceptor() ).addPathPatterns( "/memberNodoInterceptor/**" );
	super.addInterceptors( registry );
    }

    /**
     * 跨域设置
     *
     * @param registry
     */
    @Override
    public void addCorsMappings( CorsRegistry registry ) {
	registry.addMapping( "/**" ).allowedHeaders( "*" ).allowedMethods( "*" ).allowedOrigins( "*" );
	super.addCorsMappings( registry );
    }
}
