package com.gt.member.config.filter;

import com.gt.api.bean.session.BusUser;
import com.gt.api.util.SessionUtils;
import com.gt.member.enums.ResponseMemberEnums;
import com.gt.member.exception.BusinessException;
import com.gt.member.exception.NeedLoginException;
import com.gt.member.util.CommonUtil;
import com.gt.member.util.PropertiesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * pc端拦截器
 * Created by Administrator on 2017/7/24 0024.
 */
public class ProjectPcInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger( ProjectPcInterceptor.class );


    /**
     * 在请求处理之前进行调用（Controller方法调用之前）
     */
    @Override
    public boolean preHandle( HttpServletRequest servletRequest, HttpServletResponse servletResponse, Object handler )
		    throws Exception {
	logger.info( "进入拦截器" );
	boolean isSuccess = true;


//	BusUser b=new BusUser();
//	b.setId( 36 );
//	SessionUtils.setLoginUser( servletRequest,b );
//	SessionUtils.setPidBusId( servletRequest,36 );

	BusUser busUser= SessionUtils.getLoginUser(servletRequest );
	if( CommonUtil.isEmpty( busUser )){
	    throw new NeedLoginException( ResponseMemberEnums.PLEASE_LOGIN.getCode(),ResponseMemberEnums.PLEASE_LOGIN.getMsg(), PropertiesUtil.getWebLoginUrl());
	}

	return isSuccess;// 只有返回true才会继续向下执行，返回false取消当前请求
    }

    /**
     * 请求处理之后进行调用，但是在视图被渲染之前（Controller方法调用之后
     */
    @Override
    public void postHandle( HttpServletRequest request, HttpServletResponse response, Object handler,
		    ModelAndView modelAndView ) throws Exception {


    }

    /**
     * 在整个请求结束之后被调用，也就是在DispatcherServlet 渲染了对应的视图之后执行（主要是用于进行资源清理工作
     */
    @Override
    public void afterCompletion( HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex )
		    throws Exception {

    }

}
