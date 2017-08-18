package com.gt.member.config;

import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

/**
 * Servlet 配置类
 *
 * @author zhangmz
 * @create 2017/6/20
 */
@Configuration
public class ServletConfig {
    // 设置默认错误 401 404 500 错误页
    private static final String UNAUTHORIZED          = "/401.html";
    private static final String NOT_FOUND             = "/404.html";
    private static final String INTERNAL_SERVER_ERROR = "/500.html";

    @Bean
    public EmbeddedServletContainerCustomizer containerCustomizer() {
	return new EmbeddedServletContainerCustomizer() {
	    @Override
	    public void customize( ConfigurableEmbeddedServletContainer container ) {
		ErrorPage error401Page = new ErrorPage( HttpStatus.UNAUTHORIZED, UNAUTHORIZED );
		ErrorPage error404Page = new ErrorPage( HttpStatus.NOT_FOUND, NOT_FOUND );
		ErrorPage error500Page = new ErrorPage( HttpStatus.INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR );
		container.addErrorPages( error401Page, error404Page, error500Page );
	    }
	};
    }
}
