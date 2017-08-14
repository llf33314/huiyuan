package com.gt.member.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Redis 缓存配置
 *
 * @author zhangmz
 * @version 1.0.0
 * @date 2017/07/16
 */
@Configuration
@EnableCaching
public class RedisCacheConfig extends CachingConfigurerSupport {
    /** 日志 */
    private static final Logger LOG = LoggerFactory.getLogger( RedisCacheConfig.class );

    @Bean
    public RedisTemplate< String,String > redisTemplate( RedisConnectionFactory cf ) {
        LOG.debug( "注入StringRedisTemplate" );
        RedisTemplate< String,String > redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory( cf );
        return redisTemplate;
    }

    @Bean
    public CacheManager cacheManager( RedisTemplate redisTemplate ) {
        RedisCacheManager cacheManager = new RedisCacheManager( redisTemplate );
        //默认超时时间,单位秒
        cacheManager.setDefaultExpiration( 3000 );
        //根据缓存名称设置超时时间,0为不超时
        Map< String,Long > expires = new ConcurrentHashMap<>();
        cacheManager.setExpires( expires );
        return cacheManager;
    }

}
