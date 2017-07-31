package com.gt.member.service.core.ws.config;

import com.gt.member.service.core.ws.CardCouponsWSService;
import com.gt.member.service.core.ws.MemberWSService;
import com.gt.member.service.core.ws.MemberpProxyWSservice;
import com.gt.member.service.core.ws.interceptor.CXFServerAuthorInterceptor;
import org.apache.cxf.Bus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.xml.ws.Endpoint;

/**
 * Created by Administrator on 2017/7/27 0027.
 */
@Configuration
public class CXFServiceConfig {
    @Autowired
    private Bus bus;

    // 注入CXF服务端身份验证拦截器
    @Autowired
    private CXFServerAuthorInterceptor cxfServerAuthorInterceptor;


    @Autowired
    private MemberWSService memberWSService;

    @Autowired
    private CardCouponsWSService cardCouponsWSService;

    @Autowired
    private MemberpProxyWSservice memberpProxyWSservice;

//    @Bean
//    public Endpoint userServiceWSendpoint() {
//	EndpointImpl endpoint = new EndpointImpl( bus, memberpProxyWSservice );
//	endpoint.publish( "/memberProxyWS" );
//	endpoint.getInInterceptors().add( cxfServerAuthorInterceptor );
//	return endpoint;
//    }

    @Bean
    public Endpoint userServiceWSendpoint() {
	EndpointImpl endpoint = new EndpointImpl( bus, memberWSService );
	endpoint.publish( "/memberWS" );

	endpoint = new EndpointImpl( bus, cardCouponsWSService );
	endpoint.publish( "/cardCouponsWS" );


	endpoint.getInInterceptors().add( cxfServerAuthorInterceptor );
	return endpoint;
    }
}
