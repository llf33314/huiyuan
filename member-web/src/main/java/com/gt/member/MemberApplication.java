package com.gt.member;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.PropertySource;

/**
 * 会员ERP 程序入口
 *
 * @author zhangmz
 * @create 2017/7/8
 */

@ServletComponentScan
@SpringBootApplication
public class MemberApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure( SpringApplicationBuilder application ) {
	return application.sources( MemberApplication.class );
    }

    public static void main( String[] args ) {
	SpringApplication.run( MemberApplication.class, args );
    }
}
