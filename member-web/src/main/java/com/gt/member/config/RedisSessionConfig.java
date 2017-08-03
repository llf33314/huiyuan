package com.gt.member.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.DefaultCookieSerializer;

/**
 * RedisSession配置Config
 *
 * @author zhangmz
 * @version 1.0.0
 * @date 2017/07/16
 */
@Configuration
@EnableRedisHttpSession
public class RedisSessionConfig {

    /**
     * 日志
     */
    private static final Logger LOG = LoggerFactory.getLogger(RedisSessionConfig.class);
    //maxInactiveIntervalInSeconds session超时时间,单位秒
    private int maxInactiveIntervalInSeconds = 180;

    /**
     * 配置Cookie 作用域
     *
     * @return
     */
    @Bean
    public DefaultCookieSerializer defaultCookieSerializer() {
        DefaultCookieSerializer cookieSerializer = new DefaultCookieSerializer();
//        cookieSerializer.setDomainName( ".example.com" );
        cookieSerializer.setCookieName("JSESSIONID");
        cookieSerializer.setCookiePath("/");
        return cookieSerializer;
    }


}
