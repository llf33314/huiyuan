package com.gt.member.config.filter;

import com.alibaba.fastjson.JSON;
import com.gt.api.bean.sign.SignBean;
import com.gt.api.bean.sign.SignEnum;
import com.gt.api.util.sign.SignUtils;
import com.gt.member.exception.ResponseEntityException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * api拦截器
 * Created by Administrator on 2017/7/24 0024.
 */
public class ApiInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger( ApiInterceptor.class );

    /**
     * 在请求处理之前进行调用（Controller方法调用之前）
     */
    @Override
    public boolean preHandle( HttpServletRequest servletRequest, HttpServletResponse servletResponse, Object handler )
		    throws Exception {
	logger.info( "进入拦截器" );
	boolean isSuccess = true;


	String signKey = "MV8MMFQUMU1HJ6F2GNH40ZFJJ7Q8LNVM"; // 定义到配置文件中
	String signStr = ((HttpServletRequest)servletRequest).getHeader("sign");
	// 解析签名
	SignBean signBean = JSON.parseObject(signStr, SignBean.class);
	// 获取签名信息
	String code = SignUtils.decSign(signKey, signBean, null);
	logger.debug( code );
	if ( SignEnum.TIME_OUT.getCode().equals( code ) ) {
	    // 超过指定时间
	    throw new ResponseEntityException( "请求超过指定时间" );
	} else if ( SignEnum.SIGN_ERROR.getCode().equals( code ) ) {
	    // 签名验证错误
	    throw new ResponseEntityException( "签名验证错误，请检查签名信息" );
	} else if ( SignEnum.SERVER_ERROR.getCode().equals( code ) ) {
	    // 签名系统错误
	    throw new ResponseEntityException( "系统错误，请检查传入参数" );

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
