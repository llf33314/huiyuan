package com.gt.member.controller.RemoteAuthori;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.gt.api.util.sign.SignHttpUtils;
import com.gt.member.util.CommonUtil;
import com.gt.member.util.PropertiesUtil;
import com.gt.member.util.RedisCacheUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;

/**
 * 授权 或 登录 统一调用接口
 *
 * @author Administrator
 */
@Controller
@RequestMapping( "authorizeOrLoginController" )
public class AuthorizeOrLoginController {

    private static final String GETWXPULICMSG="/8A5DA52E/busUserApi/getWxPulbicMsg.do";

    @Autowired
    private RedisCacheUtil redisCacheUtil;

    @Autowired
    private PropertiesUtil propertiesUtil;

    @RequestMapping( value = "/79B4DE7C/authorizeMember" )
    public String authorizeMember( HttpServletRequest request, HttpServletResponse response, Map< String,Object > map ) throws Exception {
	Integer busId = CommonUtil.toInteger( map.get( "busId" ) );
	Integer browser = CommonUtil.judgeBrowser( request );
	Object uclogin = map.get( "uclogin" );
	Map< String,Object > getWxPublicMap = new HashMap<>();
	getWxPublicMap.put( "busId", busId );
	//判断商家信息 1是否过期 2公众号是否变更过
	String wxpublic = SignHttpUtils.WxmppostByHttp( propertiesUtil.getWxmp_home()+GETWXPULICMSG, getWxPublicMap, propertiesUtil.getWxmpsignKey() );
	JSONObject json = JSONObject.parseObject( wxpublic );
	Integer code = CommonUtil.toInteger( json.get( "code" ) );
	if ( code == 0 ) {
	    Object guoqi = json.get( "guoqi" );
	    if ( CommonUtil.isNotEmpty( guoqi ) ) {
		//商家已过期
		Object guoqiUrl = json.get( "guoqiUrl" );
		return "redirect:" + guoqiUrl;
	    }

	    Object remoteUcLogin = json.get( "remoteUcLogin" );
	    if (browser==99 && (CommonUtil.isNotEmpty( uclogin ) || CommonUtil.isNotEmpty( remoteUcLogin )) ) {
		return "";
	    }
	}
	String requestUrl = CommonUtil.toString( map.get( "requestUrl" ) );
	String otherRedisKey = CommonUtil.getCode();

	//redis公用要调wxmp接口
	Map< String,Object > redisMap = new HashMap<>();
	redisMap.put( "redisKey", otherRedisKey );
	redisMap.put( "redisValue", requestUrl );
	redisMap.put( "setime", 300 );
	SignHttpUtils.WxmppostByHttp( propertiesUtil.getWxmp_home()+"/8A5DA52E/redis/SetExApi.do", redisMap, propertiesUtil.getWxmpsignKey() );

	Map< String,Object > queryMap = new HashMap< String,Object >();
	queryMap.put( "otherRedisKey", otherRedisKey );
	queryMap.put( "browser", browser );
	queryMap.put( "busId", busId );
	queryMap.put( "uclogin", uclogin );

	String url = "redirect:"+ propertiesUtil.getWxmp_home()+"/remoteUserAuthoriPhoneController/79B4DE7C/authorizeMember.do?queryBody=" + JSON.toJSONString(  queryMap);
	return url;
    }
}
