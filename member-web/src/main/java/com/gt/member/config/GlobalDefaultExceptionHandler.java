package com.gt.member.config;

import com.gt.member.dto.ErrorInfo;
import com.gt.member.exception.ResponseEntityException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * 全局异常统一处理
 * <pre>
 *
 * </pre>
 *
 * @author zhangmz
 * @create 2017/6/21
 */
@ControllerAdvice
public class GlobalDefaultExceptionHandler {
    // 全局默认错误页
    public static final String DEFAULT_ERROR_VIEW = "error/defaultError";

    // 页面
    // 统一异常处理 页面跳转
    @ExceptionHandler( value = Exception.class )
    public ModelAndView defaultErrorHandler( HttpServletRequest request, Exception e ) {
	ModelAndView modelAndView = new ModelAndView();
	modelAndView.addObject( "ex", e );
	modelAndView.addObject( "url", request.getRequestURL() );
	modelAndView.setViewName( DEFAULT_ERROR_VIEW );
	return modelAndView;
    }

    // 统一异常处理 Ajax请求
    @ResponseBody
    @ExceptionHandler( value = ResponseEntityException.class )
    public ErrorInfo< String > defaultErrorHandler(HttpServletRequest request, ResponseEntityException e ) {
	return ErrorInfo.createByErrorCodeMessage( e.getCode(), e.getMessage(), null );
    }
}
