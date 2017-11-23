package com.gt.member.controller.RemoteAuthori;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.gt.api.bean.session.Member;
import com.gt.api.util.KeysUtil;
import com.gt.api.util.SessionUtils;
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
public class AuthorizeOrLoginController {

    private static final String GETWXPULICMSG="/8A5DA52E/busUserApi/getWxPulbicMsg.do";

    @Autowired
    private RedisCacheUtil redisCacheUtil;


    public String authorizeMember( HttpServletRequest request, HttpServletResponse response, Map< String,Object > map ) throws Exception {
	Integer busId = CommonUtil.toInteger( map.get( "busId" ) );
	Integer browser = CommonUtil.judgeBrowser( request );
	Object uclogin = map.get( "uclogin" );
	Map< String,Object > getWxPublicMap = new HashMap<>();
	getWxPublicMap.put( "busId", busId );
	//判断商家信息 1是否过期 2公众号是否变更过
	String wxpublic = SignHttpUtils.WxmppostByHttp( PropertiesUtil.getWxmp_home()+GETWXPULICMSG, getWxPublicMap, PropertiesUtil.getWxmpsignKey() );
	JSONObject json = JSONObject.parseObject( wxpublic );
	Integer code = CommonUtil.toInteger( json.get( "code" ) );
	if ( code == 0 ) {
	    Object guoqi = json.get( "guoqi" );
	    if ( CommonUtil.isNotEmpty( guoqi ) ) {
		//商家已过期
		Object guoqiUrl = json.get( "guoqiUrl" );
		return guoqiUrl.toString();
	    }

	    Object remoteUcLogin = json.get( "remoteUcLogin" );
	    if (browser==99 && (CommonUtil.isNotEmpty( uclogin ) || CommonUtil.isNotEmpty( remoteUcLogin )) ) {
		return "";
	    }
	}
	String requestUrl = CommonUtil.toString( map.get( "requestUrl" ) );
	requestUrl= KeysUtil.getEncString( requestUrl );
	Map< String,Object > queryMap = new HashMap< String,Object >();
	queryMap.put( "returnUrl", requestUrl );
	queryMap.put( "browser", browser );
	queryMap.put( "busId", busId );
	queryMap.put( "uclogin", uclogin );

	String url = PropertiesUtil.getWxmp_home()+"/remoteUserAuthoriPhoneController/79B4DE7C/authorizeMemberNew.do?queryBody=" + JSON.toJSONString(  queryMap);
	return url;
    }




}
