package com.gt.member.controller.RemoteAuthori;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gt.member.util.CommonUtil;
import com.gt.member.util.MemberConfig;
import com.gt.member.util.RedisCacheUtil;
import com.gt.member.util.sign.SignHttpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
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
    private MemberConfig memberConfig;

    @RequestMapping( value = "/79B4DE7C/authorizeMember" )
    public String authorizeMember( HttpServletRequest request, HttpServletResponse response, Map< String,Object > map ) throws Exception {
	Integer busId = CommonUtil.toInteger( map.get( "busId" ) );
	Integer browser = CommonUtil.judgeBrowser( request );
	Object uclogin = map.get( "uclogin" );
	String wxmpSign = "WXMP2017";
	Map< String,Object > getWxPublicMap = new HashMap<>();
	getWxPublicMap.put( "busId", busId );
	//判断商家信息 1是否过期 2公众号是否变更过
	String wxpublic = SignHttpUtils.postByHttp(memberConfig.getWxmp_home()+GETWXPULICMSG, getWxPublicMap, wxmpSign );
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
	    if ( CommonUtil.isNotEmpty( uclogin ) || CommonUtil.isNotEmpty( remoteUcLogin ) ) {
		return "";
	    }
	}

	String requestUrl = CommonUtil.toString( map.get( "requestUrl" ) );
	String otherRedisKey = CommonUtil.getCode();
	redisCacheUtil.set( otherRedisKey, requestUrl, 5 * 60L );
	Map< String,Object > queryMap = new HashMap< String,Object >();
	queryMap.put( "otherRedisKey", otherRedisKey );
	queryMap.put( "browser", browser );
	queryMap.put( "domainName", memberConfig.getWebHome() );
	queryMap.put( "busId", busId );
	queryMap.put( "uclogin", uclogin );
	String url = "redirect:http://192.168.2.240:8080/remoteUserAuthoriPhoneController/79B4DE7C/authorizeMember.do?queryBody=" + queryMap;
	return url;
    }

    /**
     * 返回跳转到之前的页面
     *
     * @param request
     * @param response
     *
     * @return
     * @throws Exception
     */
    @RequestMapping( value = "/{redisKey}/79B4DE7C/returnJump" )
    public String returnJump( HttpServletRequest request, HttpServletResponse response, @PathVariable( "redisKey" ) String redisKey, Map< String,Object > params ) {
	try {
	    Object url = redisCacheUtil.get( redisKey );
	    return "redirect:" + url;
	} catch ( Exception e ) {
	    e.printStackTrace();
	}
	return null;
    }
}
