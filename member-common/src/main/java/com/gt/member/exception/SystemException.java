package com.gt.member.exception;

/**
 * 系统统一异常类
 * <pre>
 *     所有自定义的异常，都继承此类。
 * </pre>
 *
 * @author zhangmz
 * @create 2017/6/16
 */
public class SystemException extends RuntimeException {

    private int code;//状态码

    private String message;//错误消息

    public SystemException(){

    }
    public SystemException( String message ) {
	super( message );
	this.message = message;
    }

    public SystemException( int code, String message ) {
	super( message );
	this.message = message;
	this.code = code;
    }

    public int getCode() {
	return code;
    }

    @Override
    public String getMessage() {
	return message;
    }
}
