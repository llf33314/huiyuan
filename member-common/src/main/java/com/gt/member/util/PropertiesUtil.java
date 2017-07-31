package com.gt.member.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


/**
 * Properties 读取工具
 * @author lfx
 *
 */
public class PropertiesUtil {
	
	private static Properties prop=null;
	static {   
        prop = new Properties();   
        InputStream in = PropertiesUtil.class.getResourceAsStream("/system.properties");   
        try {
			prop.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		}   
    }   
	
	/**
	 * socket链接
	 * @return
	 */
	public static String getSocketUrl(){
		String url=prop.getProperty("socket.url");
		return url;
	}	
	/**
	 * redis IP
	 * @return
	 */
	public static String getRedisIp(){
		return prop.getProperty("redis.ip");
	}
	/**
	 * redis 端口号
	 * @return
	 */
	public static Integer getRedisPort(){
		return Integer.valueOf(prop.getProperty("redis.port"));
	}
	/**
	 * redis pwd
	 * @return
	 */
	public static String getRedisPwd(){
		return prop.getProperty("redis.pwd");
	}
	
	/**
	 * wxmp  主项目域名
	 * @return
	 */
	public static String getWxmpWebUrl(){
		return prop.getProperty("wxmp_web_url");
	}
	/**
	 * 本地项目访问域名
	 * @return
	 */
	public static String getWebHome(){
		return prop.getProperty("web_home");
	}
	
}
