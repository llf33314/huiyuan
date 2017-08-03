package com.gt.member.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.alibaba.druid.support.spring.stat.DruidStatInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.Advisor;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.JdkRegexpMethodPointcut;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * Druid的DataResource配置类
 * Created by zhangmz on 2017/6/15.
 * <p>
 * 凡是被Spring管理的类，实现接口 EnvironmentAware 重写方法 setEnvironment 可以在工程启动时，
 * 获取到系统环境变量和application配置文件中的变量。
 * 还有一种方式是采用注解的方式获取 @value("${变量的key值}")
 * ：获取application配置文件中的变量。 这里采用第一种要方便些
 * </p>
 */
@Configuration
public class DruidDataSourceConfig {

    /** 日志 */
    private static final Logger   LOG              = LoggerFactory.getLogger( DruidDataSourceConfig.class );
    //注册地址
    private static final String[] URL_MAPINGS      = { "/druid/*" };
    //白名单：
    private static final String   ALLOW            = "127.0.0.1,192.168.3.47";
    //IP黑名单 (存在共同时，deny优先于allow) : 如果满足deny的话提示:Sorry, you are not permitted to view this page.
    private static final String   DENY             = "192.168.2.150";
    //登录查看信息的账号密码.
    private static final String   LOGIN_USERNAME   = "admin";
    private static final String   LOGIN_PASSWORD   = "admin";
    //是否能够重置数据.
    private static final String   RESETENABLE      = "false";
    //添加过滤规则.
    private static final String   URLPATTERNS      = "/*";
    //添加排除的规则.
    private static final String   EXCLUSIONS       = "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*";
    //监控规则
    private static final String[] MONITORING_RULES = { "com.gt.*.dao.*", "com.gt.member.*.controller.*" };

    @ConfigurationProperties( prefix = "datasource.druid" )
    @Bean( name = "datasource", initMethod = "init", destroyMethod = "close" )
    public DruidDataSource getDataSource() {
	return new DruidDataSource();
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
	return new DataSourceTransactionManager( getDataSource() );
    }

    /**
     * 注册一个StatViewServlet
     *
     * @return
     */
    @Bean
    public ServletRegistrationBean DruidStatViewServle() {
	//org.springframework.boot.context.embedded.ServletRegistrationBean提供类的进行注册.
	ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean( new StatViewServlet(), URL_MAPINGS );
	//添加初始化参数：initParams
	LOG.debug( "Druid 白名单IP：{} , 黑名单IP：{} , 登录账号/密码：{}/{} , 是否开启重置数据：{} ", ALLOW, DENY, LOGIN_USERNAME, LOGIN_PASSWORD, RESETENABLE );
	servletRegistrationBean.addInitParameter( "allow", ALLOW );
	servletRegistrationBean.addInitParameter( "deny", DENY );
	servletRegistrationBean.addInitParameter( "loginUsername", LOGIN_USERNAME );
	servletRegistrationBean.addInitParameter( "loginPassword", LOGIN_PASSWORD );
	servletRegistrationBean.addInitParameter( "resetEnable", RESETENABLE );
	return servletRegistrationBean;
    }

    /**
     * 注册一个：filterRegistrationBean
     *
     * @return
     */
    @Bean
    public FilterRegistrationBean druidStatFilter() {
	FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean( new WebStatFilter() );
	filterRegistrationBean.addUrlPatterns( URLPATTERNS );
	filterRegistrationBean.addInitParameter( "exclusions", EXCLUSIONS );
	return filterRegistrationBean;
    }

    /**
     * 监听Spring
     * 1.定义拦截器
     * 2.定义切入点
     * 3.定义通知类
     *
     * @return
     */
    @Bean
    public DruidStatInterceptor druidStatInterceptor() {
	return new DruidStatInterceptor();
    }

    /**
     * 设置方法切入点
     *
     * @return
     */
    @Bean
    public JdkRegexpMethodPointcut druidStatPointcut() {
	JdkRegexpMethodPointcut druidStatPointcut = new JdkRegexpMethodPointcut();
	druidStatPointcut.setPatterns( MONITORING_RULES );
	return druidStatPointcut;
    }

    /**
     * 配置AOP
     *
     * @return
     */
    @Bean
    public Advisor druidStatAdvisor() {
	return new DefaultPointcutAdvisor( druidStatPointcut(), druidStatInterceptor() );
    }
}
