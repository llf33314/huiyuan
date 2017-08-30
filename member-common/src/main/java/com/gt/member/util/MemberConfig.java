package com.gt.member.util;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * member 程序中的配置
 */
@Component    //不加这个注解的话, 使用@Autowired 就不能注入进去了
@ConfigurationProperties(prefix = "web")  // 配置文件中的前缀
@Getter
@Data
public class MemberConfig {
    private String webHome;  //本项目域名

    private String wxmp_home;  //wxmp

    private String wxmpsignKey;  //wxmp 秘钥

    private String sms_name;  //短信发送名称

    private String cardNoKey;  //会员卡秘钥

    private String socket_url;  //socket.io url

    private String redis_ip;

    private String redis_port;

    private String redis_password;

    private String redis_database;

}
