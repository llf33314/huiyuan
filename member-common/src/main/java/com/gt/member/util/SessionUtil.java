package com.gt.member.util;

import javax.servlet.http.HttpServletRequest;

import com.gt.common.entity.BusUser;
import com.gt.common.entity.TCommonStaff;
import com.gt.common.entity.WxPublicUsers;
import com.gt.member.constant.CommonConst;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 专用来存SESSION 的数据方法
 *
 * @author Administrator
 */
public class SessionUtil {

    @Autowired
    private MemberConfig memberConfig;

    private static final Logger log = Logger.getLogger( SessionUtil.class );

    /**
     * 获取session登陆属性老板属性是loginStyle=1，员工属性是loginStyle=0,老板用户表是bususer，员工登陆表是TEatSTAFF
     *
     * @param request
     *
     * @return
     */
    public static Integer getLoginStyle( HttpServletRequest request ) {
	try {
	    if ( CommonUtil.isEmpty( request.getSession().getAttribute( "loginStyle" ) ) ) {
		return null;
	    } else {
		return (Integer) request.getSession().getAttribute( "loginStyle" );
	    }
	} catch ( Exception e ) {
	    log.info( e.getLocalizedMessage() );
	    e.printStackTrace();
	}
	return null;
    }

    /**
     * 获取老板登录人的详情信息
     *
     * @param request
     *
     * @return
     */
    public static BusUser getBusUser( HttpServletRequest request ) {
	try {
	    return (BusUser) request.getSession().getAttribute( "BusUser" );
	} catch ( Exception e ) {
	    log.info( e.getLocalizedMessage() );
	    e.printStackTrace();
	}
	return null;
    }

    /**
     * 获取员工登录人的详情信息
     *
     * @param request
     *
     * @return
     */
    public static TCommonStaff getTCommonStaff( HttpServletRequest request ) {
	try {
	    return (TCommonStaff) request.getSession().getAttribute( "TCommonStaff" );
	} catch ( Exception e ) {
	    log.info( e.getLocalizedMessage() );
	    e.printStackTrace();
	}
	return null;
    }

    /**
     * 获取主账户busId
     *
     * @param request
     *
     * @return
     */
    public Integer getBusUserId( HttpServletRequest request ) {
	log.error( "开始" );
	Integer LoginStyle = getLoginStyle( request );
	log.error( "开始获取角色" + LoginStyle );
	Integer userId = 0;
	if ( LoginStyle == 0 ) {
	    //员工
	    TCommonStaff tc = getTCommonStaff( request );
	    userId = tc.getId();
	} else {
	    BusUser busUser = getBusUser( request );
	    if ( busUser.getPid() <= 0 ) {
		return busUser.getId();
	    }
	    userId = busUser.getId();
	}
	return userId;
    }


    //存入 用户bus_user  的值
    @SuppressWarnings("unchecked")
    public static void setLoginUser(HttpServletRequest request, BusUser busUser) {
	try {
	    request.getSession().setAttribute(
			    CommonConst.SESSION_BUSINESS_KEY, com.alibaba.fastjson.JSONObject.toJSONString(busUser));
	} catch (Exception e) {
	    log.info(e.getLocalizedMessage());
	    e.printStackTrace();
	}
    };

    /**
     * 设置session中的t_wx_public_user微信信息
     *
     * @param request
     * @return
     */
    public static void setLoginPbUser(HttpServletRequest request,
		    WxPublicUsers wxPublicUsers) {
	try {
	    request.getSession().setAttribute(
			    CommonConst.SESSION_WXPUBLICUSERS_KEY, com.alibaba.fastjson.JSONObject.toJSONString(wxPublicUsers));
	} catch (Exception e) {
	    log.info(e.getLocalizedMessage());
	    e.printStackTrace();
	}
    };
}
