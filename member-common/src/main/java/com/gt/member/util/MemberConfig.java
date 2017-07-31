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
@ConfigurationProperties( prefix = "web" )  // 配置文件中的前缀
@Setter
@Getter
@Data
public class MemberConfig {
  private String wxmp_home;

}
