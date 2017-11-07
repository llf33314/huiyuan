package com.gt.member.config.filter;

import com.gt.api.bean.session.Member;
import com.gt.api.enums.ResponseEnums;
import com.gt.api.util.SessionUtils;
import com.gt.member.controller.RemoteAuthori.AuthorizeOrLoginController;
import com.gt.member.enums.ResponseMemberEnums;
import com.gt.member.exception.BusinessException;
import com.gt.member.util.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 不做签名API处理拦截器
 * Created by Administrator on 2017/7/24 0024.
 */
public class NotdoInterceptor  implements HandlerInterceptor{

    private static final Logger logger = LoggerFactory.getLogger( NotdoInterceptor.class );

    /**
     * 在请求处理之前进行调用（Controller方法调用之前）
     */
    @Override
    public boolean preHandle( HttpServletRequest servletRequest, HttpServletResponse servletResponse, Object handler )
		    throws BusinessException {
        //地址

	try {
	    String url=servletRequest.getServletPath();
	    logger.error( "进入不做签名处理的拦截器,请求地址:"+url );
	    boolean isSuccess = true;
	    return isSuccess;// 只有返回true才会继续向下执行，返回false取消当前请求
	}catch ( BusinessException e ){
	    throw  e;
	}catch ( Exception e ){
	    throw new BusinessException( ResponseEnums.ERROR);
	}
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


    public  Map getParameterMap(HttpServletRequest request) {
	// 参数Map
	Map properties = request.getParameterMap();
	// 返回值Map
	Map returnMap = new HashMap();
	Iterator entries = properties.entrySet().iterator();
	Map.Entry entry;
	String name = "";
	String value = "";
	while (entries.hasNext()) {
	    entry = (Map.Entry) entries.next();
	    name = (String) entry.getKey();
	    Object valueObj = entry.getValue();
	    if(null == valueObj){
		value = "";
	    }else if(valueObj instanceof String[]){
		String[] values = (String[])valueObj;
		for(int i=0;i<values.length;i++){
		    value = values[i] + ",";
		}
		value = value.substring(0, value.length()-1);
	    }else{
		value = valueObj.toString();
	    }
	    returnMap.put(name, value);
	}
	return returnMap;
    }

}
