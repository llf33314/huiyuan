package com.gt.member.util;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.gt.common.entity.BusUserEntity;
import com.gt.common.entity.TCommonStaffEntity;
import com.gt.common.entity.WxPublicUsersEntity;
import com.gt.member.entity.MemberEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 专用来存SESSION 的数据方法
 *
 * @author Administrator
 */
public class SessionUtil {

    private static final Logger log = LoggerFactory.getLogger( SessionUtil.class );

	public static String SESSION_BUSINESS_KEY      = "business_key";
	public static String SESSION_WXPUBLICUSERS_KEY = "wxPublicUsers";
	public static String SESSION_MEMBER            = "member";
	public static String SESSION_LOGIN_STYLE       = "loginStyle";
	public static String SESSION_COMMON_STAFF      = "TCommonStaffEntity";
	public static String SESSION_PIDBUSID          = "PidBusId";

	//获取用户bus_user   SESSION的值
	public static BusUserEntity getLoginUser( HttpServletRequest request ) {
	    try {
		Object obj = request.getSession().getAttribute( SESSION_BUSINESS_KEY );

		if ( obj != null ) {
		    BusUserEntity user = JSONObject.toJavaObject( ( JSONObject.parseObject( obj.toString() ) ), BusUserEntity.class );
		    return user;
		} else {
		    return null;
		}
	    } catch ( Exception e ) {
		e.printStackTrace();
	    }
	    return null;
	}

	;

	//存入 用户bus_user  的值
	@SuppressWarnings( "unchecked" )
	public static void setLoginUser( HttpServletRequest request, BusUserEntity busUserEntity ) {
	    try {
		request.getSession().setAttribute( SESSION_BUSINESS_KEY, JSONObject.toJSONString( busUserEntity ) );
	    } catch ( Exception e ) {
		e.printStackTrace();
	    }
	}

	;

	/**
	 * 设置session中的t_wx_public_user微信信息
	 *
	 * @param request
	 *
	 * @return
	 */
	public static void setLoginPbUser( HttpServletRequest request, WxPublicUsersEntity wxPublicUsersEntity ) {
	    try {
		request.getSession().setAttribute( SESSION_WXPUBLICUSERS_KEY, JSONObject.toJSONString( wxPublicUsersEntity ) );
	    } catch ( Exception e ) {
		e.printStackTrace();
	    }
	}

	;

	/**
	 * 获取session中的t_wx_public_user登录用户
	 *
	 * @param request
	 *
	 * @return
	 */
	public static WxPublicUsersEntity getLoginPbUser( HttpServletRequest request ) {
	    try {
		Object obj = request.getSession().getAttribute( SESSION_WXPUBLICUSERS_KEY );
		if ( obj != null ) {
		    WxPublicUsersEntity wx = JSONObject.toJavaObject( ( JSONObject.parseObject( obj.toString() ) ), WxPublicUsersEntity.class );
		    return wx;
		} else {
		    return null;
		}

	    } catch ( Exception e ) {
		e.printStackTrace();
	    }
	    return null;
	}

	;

	public static MemberEntity getLoginMember( HttpServletRequest request,Integer busId ) {
	    try {
		Object obj = request.getSession().getAttribute( SESSION_MEMBER );
		if ( obj != null ) {
		    MemberEntity mem = JSONObject.toJavaObject( ( JSONObject.parseObject( obj.toString() ) ), MemberEntity.class );
		    if(mem.getBusId()==busId){
		        return null;
		    }
		    return mem;
		} else {
		    return null;
		}

	    } catch ( Exception e ) {
		e.printStackTrace();

	    }
	    return null;
	}

	;

	//存入登陆人的属性session   0是员工  1是老板
	public static void setLoginStyle( HttpServletRequest request, Integer loginStyle ) {
	    try {
		if ( loginStyle == 0 ) {
		    request.getSession().setAttribute( SESSION_LOGIN_STYLE, loginStyle );
		    SessionUtil.setLoginUser( request, null );
		} else if ( loginStyle == 1 ) {
		    request.getSession().setAttribute( SESSION_LOGIN_STYLE, loginStyle );
		    SessionUtil.setCommonStaff( request, null );
		}
	    } catch ( Exception e ) {

		e.printStackTrace();
	    }
	}

	;

	//获取登录人的属性
	public static Integer getLoginStyle( HttpServletRequest request ) {
	    try {
		Object obj = request.getSession().getAttribute( SESSION_LOGIN_STYLE );
		if ( obj != null ) {
		    return Integer.parseInt( obj.toString() );
		} else {
		    return null;
		}
	    } catch ( Exception e ) {

		e.printStackTrace();
	    }
	    return null;
	}

	//存入员工session的值
	public static void setCommonStaff( HttpServletRequest request, TCommonStaffEntity staff ) {
	    try {
		request.getSession().setAttribute( SESSION_COMMON_STAFF, JSONObject.toJSONString( staff ) );
	    } catch ( Exception e ) {
		e.printStackTrace();
	    }
	}

	//获取员工session的值
	public static TCommonStaffEntity getCommonStaff( HttpServletRequest request ) {
	    try {
		Object obj = request.getSession().getAttribute( SESSION_COMMON_STAFF );
		if ( obj != null ) {
		    TCommonStaffEntity staff = JSONObject.toJavaObject( ( JSONObject.parseObject( obj.toString() ) ), TCommonStaffEntity.class );
		    return staff;
		} else {
		    return null;
		}

	    } catch ( Exception e ) {
		e.printStackTrace();
	    }
	    return null;
	}

	;

	//存入员工或者子账号最头部的主商家ID
	public static void setPidBusId( HttpServletRequest request, Integer PidBusId ) {
	    try {
		request.getSession().setAttribute( SESSION_PIDBUSID, PidBusId );
	    } catch ( Exception e ) {

		e.printStackTrace();
	    }
	}

	//获取主账号ID信息
	public static Integer getPidBusId( HttpServletRequest request ) {
	    try {
		Object obj = request.getSession().getAttribute( SESSION_PIDBUSID );
		if ( obj != null ) {
		    return Integer.parseInt( obj.toString() );
		} else {
		    return null;
		}

	    } catch ( Exception e ) {

		e.printStackTrace();
	    }
	    return null;
	}
    }
