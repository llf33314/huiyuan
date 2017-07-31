package com.gt.member.util;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.gt.member.entity.BusUser;
import com.gt.member.entity.TCommonStaff;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;




/**
 * 专用来存SESSION 的数据方法
 * @author Administrator
 *
 */
public class SessionUtil {
	private static final Logger log = Logger
			.getLogger(SessionUtil.class);
	
	
	/**
	 * 获取session登陆属性老板属性是loginStyle=1，员工属性是loginStyle=0,老板用户表是bususer，员工登陆表是TEatSTAFF
	 * @param request
	 * @return
	 */
	public static Integer getLoginStyle(HttpServletRequest request){
		try {
			if(CommonUtil.isEmpty(request.getSession().getAttribute("loginStyle"))){
				return null;
			}else{
				return  (Integer) request.getSession().getAttribute("loginStyle");
			}
		} catch (Exception e) {
			log.info(e.getLocalizedMessage());
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	/**
	 * 获取老板登录人的详情信息
	 * @param request
	 * @return
	 */
	public static BusUser getBusUser(HttpServletRequest request){
		try {
			return  (BusUser) request.getSession().getAttribute("BusUser");
		} catch (Exception e) {
			log.info(e.getLocalizedMessage());
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 获取员工登录人的详情信息
	 * @param request
	 * @return
	 */
	public static TCommonStaff getTCommonStaff(HttpServletRequest request){
		try {
			return  (TCommonStaff) request.getSession().getAttribute("TCommonStaff");
		} catch (Exception e) {
			log.info(e.getLocalizedMessage());
			e.printStackTrace();
		}
		return null;
	}
	
	
	public static Integer getZhuBusUser(HttpServletRequest request){
		log.error("开始");
		Integer LoginStyle=getLoginStyle(request);
		log.error("开始获取角色"+LoginStyle);
		Integer userId=0;
		if(LoginStyle==0){
			//员工
			TCommonStaff tc=getTCommonStaff(request);
			userId=tc.getId();
		}else{
			BusUser busUser=getBusUser(request);
			if(busUser.getPid()<=0){
				return busUser.getId();
			}
			userId=busUser.getId();
		}
		log.error("开始获取商家信息Id"+userId);
		String url=PropertiesUtil.getWxmpWebUrl()+"ErpMenus/79B4DE7C/getShop.do";
		JSONObject obj=new JSONObject();
		obj.put("style", LoginStyle);
		obj.put("userId", userId);
		try {
			JSONObject json = HttpClienUtil.httpPost(url, obj, false);
			log.error("开始获取商家JSON信息:"+json);
			if("0".equals(json.getString("error"))){
				return json.getInt("bus_id");
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return 0;
	}
	

}
