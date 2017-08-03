package com.gt.member.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * RestFul API 文档
 *
 * @author zhangmz
 * @create 2017/6/15
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    // 扫描包
    private static final String  BASEPACKAGE         = "com.gt.member.controller";
    // 标题
    private static final String  TITLE               = "member-API";
    // 描述
    private static final String  DESC                = "会员中心";
    // 版本
    private static final String  VERSION             = "1.0.0";
    //
    private static final String  TERMS_OF_SERVICEURL = "NO terms of controller";
    // 作者信息
    private static final Contact CONTACT             = new Contact( "zhangmz", "http://www.zhangmz.me", "3001417980@qq.com" );
    //
    private static final String  LICENSE             = "";
    //
    private static final String  LICENSE_URL         = "";

    /**
     * 开发API
     */
    @Bean
    public Docket devApi() {
	return new Docket( DocumentationType.SWAGGER_2 ).apiInfo( apiInfo() ).select()
			// 获取接口的Package包
			.apis( RequestHandlerSelectors.basePackage( BASEPACKAGE ) ).paths( PathSelectors.any() ).build();
    }

    private ApiInfo apiInfo() {
	return new ApiInfo( TITLE, // 大标题
			DESC, // 小标题
			VERSION, // 版本
			TERMS_OF_SERVICEURL, CONTACT, // 作者
			LICENSE, // 链接显示文字
			LICENSE_URL// 网站链接
	);
    }
}
