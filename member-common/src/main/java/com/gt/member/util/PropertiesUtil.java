package com.gt.member.util;

import lombok.Data;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * member 程序中的配置
 */
@Component    //不加这个注解的话, 使用@Autowired 就不能注入进去了
@ConfigurationProperties( prefix = "web" )  // 配置文件中的前缀
@Data
public class PropertiesUtil {

    private static String webHome;  //本项目域名

    private static String wxmp_home;  //wxmp

    private static String wxmpsignKey;  //wxmp 秘钥

    private static String sms_name;  //短信发送名称

    private static String cardNoKey;  //会员卡秘钥

    private static String socket_url;  //socket.io url

    private static String redis_ip;

    private static String redis_port;

    private static String redis_password;

    private static String redis_database;

    private static String res_image_path;

    private static String static_source_ftp_ip;

    private static String static_source_ftp_port;

    private static String static_source_ftp_user;

    private static String static_source_ftp_password;

    private static String res_web_path;

    public static String getWebHome() {
	return webHome;
    }

    public static String getWxmp_home() {
	return wxmp_home;
    }

    public static String getWxmpsignKey() {
	return wxmpsignKey;
    }

    public static String getSms_name() {
	return sms_name;
    }

    public static String getCardNoKey() {
	return cardNoKey;
    }

    public static String getSocket_url() {
	return socket_url;
    }

    public static String getRedis_ip() {
	return redis_ip;
    }

    public static String getRedis_port() {
	return redis_port;
    }

    public static String getRedis_password() {
	return redis_password;
    }

    public static String getRedis_database() {
	return redis_database;
    }

    public static String getRes_image_path() {
	return res_image_path;
    }

    public static String getStatic_source_ftp_ip() {
	return static_source_ftp_ip;
    }

    public static String getStatic_source_ftp_port() {
	return static_source_ftp_port;
    }

    public static String getStatic_source_ftp_user() {
	return static_source_ftp_user;
    }

    public static String getStatic_source_ftp_password() {
	return static_source_ftp_password;
    }

    public void setWebHome( String webHome ) {
	PropertiesUtil.webHome = webHome;
    }

    public void setWxmp_home( String wxmp_home ) {
	PropertiesUtil.wxmp_home = wxmp_home;
    }

    public void setWxmpsignKey( String wxmpsignKey ) {
	PropertiesUtil.wxmpsignKey = wxmpsignKey;
    }

    public void setSms_name( String sms_name ) {
	PropertiesUtil.sms_name = sms_name;
    }

    public void setCardNoKey( String cardNoKey ) {
	PropertiesUtil.cardNoKey = cardNoKey;
    }

    public static void setSocket_url( String socket_url ) {
	PropertiesUtil.socket_url = socket_url;
    }

    public void setRedis_ip( String redis_ip ) {
	PropertiesUtil.redis_ip = redis_ip;
    }

    public void setRedis_port( String redis_port ) {
	PropertiesUtil.redis_port = redis_port;
    }

    public void setRedis_password( String redis_password ) {
	PropertiesUtil.redis_password = redis_password;
    }

    public void setRedis_database( String redis_database ) {
	PropertiesUtil.redis_database = redis_database;
    }

    public void setRes_image_path( String res_image_path ) {
	PropertiesUtil.res_image_path = res_image_path;
    }

    public void setStatic_source_ftp_ip( String static_source_ftp_ip ) {
	PropertiesUtil.static_source_ftp_ip = static_source_ftp_ip;
    }

    public void setStatic_source_ftp_port( String static_source_ftp_port ) {
	PropertiesUtil.static_source_ftp_port = static_source_ftp_port;
    }

    public void setStatic_source_ftp_user( String static_source_ftp_user ) {
	PropertiesUtil.static_source_ftp_user = static_source_ftp_user;
    }

    public void setStatic_source_ftp_password( String static_source_ftp_password ) {
	PropertiesUtil.static_source_ftp_password = static_source_ftp_password;
    }

    public static String getRes_web_path() {
	return res_web_path;
    }

    public  void setRes_web_path( String res_web_path ) {
	PropertiesUtil.res_web_path = res_web_path;
    }
}
