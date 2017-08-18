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
    private String wxmp_home;  //wxmp

    private String api_signKey;  //wxmp 秘钥

    private String sms_name;  //短信发送名称

    private String cardNoKey;  //会员卡秘钥

    private String redis_ip;  //redis ip

    private String redis_port; //redis端口

    private String redis_pwd; //redis  密码

    private String domain;  //获取域名

    private String signKey;  //签名key值
}
