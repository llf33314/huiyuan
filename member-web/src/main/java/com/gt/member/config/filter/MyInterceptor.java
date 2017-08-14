package com.gt.member.config.filter;

import com.gt.member.enums.SignEnum;
import com.gt.member.util.MemberConfig;
import com.gt.member.util.sign.SignFilterUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/7/24 0024.
 */
public class MyInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger( MyInterceptor.class );

    @Autowired
    private MemberConfig memberConfig;

    //不需要登录就可访问的url
    private static final Map< String,String > urls = new HashMap< String,String >();

    //可通过的文件类型
    private static final List< String > suffixs = new ArrayList< String >();

    static {

	urls.put( "/jsp/error/404.jsp", "/jsp/error/404.jsp" );
	urls.put( "/jsp/error/error.jsp", "/jsp/error/error.jsp" );
	urls.put( "/", "/user/tologin.do" );
	urls.put( "/error/warning.jsp", "/error/warning.jsp" );

	urls.put( "/user/tologin.do", "/user/tologin.do" );
	urls.put( "/user/toregister.do", "/user/toregister.do" );
	urls.put( "/dxuser/login.do", "/dxuser/login.do" );
	urls.put( "/dxuser/login_success.do", "/dxuser/login_success.do" );
	urls.put( "swagger-ui.html", "swagger-ui.html" );

	suffixs.add( "js" );
	suffixs.add( "css" );
	suffixs.add( "gif" );
	suffixs.add( "png" );
	suffixs.add( "jpg" );
	suffixs.add( "ico" );
	suffixs.add( "html" );
	suffixs.add( "dwr" );
	suffixs.add( "mp3" );
	suffixs.add( "txt" );
	suffixs.add( "woff" );
	suffixs.add( "ttf" );
    }

    /**
     * 在请求处理之前进行调用（Controller方法调用之前）
     */
    @Override
    public boolean preHandle( HttpServletRequest servletRequest, HttpServletResponse servletResponse, Object handler )
		    throws Exception {
	HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
	HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;

	// js跨域支持
	httpServletResponse.setHeader( "Access-Control-Allow-Origin", "*" );
	httpServletResponse.setHeader( "Access-Control-Allow-Methods", "POST, GET, PUT, DELETE" );
	httpServletResponse.setHeader( "Access-Control-Max-Age", "3600" );
	httpServletResponse.setHeader( "Access-Control-Allow-Headers", "Accept, Origin, XRequestedWith, Content-Type, LastModified" );

	// 设置返回编码和类型
	servletResponse.setCharacterEncoding( "UTF-8" );
	servletResponse.setContentType( "application/json; charset=utf-8" );

	// 在wrapper中获取新的servletRequest
	servletRequest = new BodyRequestWrapper( httpServletRequest );
	String url = servletRequest.getRequestURI();
	if(passSuffixs(url)||passUrl(url)){
	    return true;
	}
 	if(url.equals("api")||url.indexOf("api")>-1) {
	    String signKey = "MV8MMFQUMU1HJ6F2GNH40ZFJJ7Q8LNVM"; // 定义到配置文件中
	    // 获取签名信息
	    String code = SignFilterUtils.postByHttp( servletRequest, signKey );
	    logger.debug( code );
	    if ( SignEnum.TIME_OUT.getCode().equals( code ) ) {
		// 超过指定时间
		servletResponse.getWriter().append( "{'error':'请求超过指定时间'}" );
		return false;
	    } else if ( SignEnum.SIGN_ERROR.getCode().equals( code ) ) {
		// 签名验证错误
		servletResponse.getWriter().append( "{'error':'签名验证错误，请检查签名信息'}" );
		return false;
	    } else if ( SignEnum.SERVER_ERROR.getCode().equals( code ) ) {
		// 签名系统错误
		servletResponse.getWriter().append( "{'error':'系统错误，请检查传入参数'}" );
		return false;
	    }
	}

	// 跳转请求
	//filterChain.doFilter( servletRequest, servletResponse );
	return true;// 只有返回true才会继续向下执行，返回false取消当前请求
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

    //判断是否是可通过的url
    private boolean passUrl( String url ) {
	return urls.containsKey( url );
    }

    private boolean passSuffixs( String url ) {
	boolean reuslt = false;
	for ( String suffix : suffixs ) {
	    if ( url.endsWith( suffix ) ) {
		reuslt = true;
		break;
	    }
	}
	return reuslt;
    }

    /**
     * 判断ajax请求
     */
    private boolean isAjax( HttpServletRequest request ) {
	return ( request.getHeader( "X-Requested-With" ) != null && "XMLHttpRequest".equals( request.getHeader( "X-Requested-With" ) ) );
    }
}
