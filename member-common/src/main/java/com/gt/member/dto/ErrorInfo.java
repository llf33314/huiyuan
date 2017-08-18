package com.gt.member.dto;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gt.member.enums.ResponseEnums;

import java.io.Serializable;

import static com.fasterxml.jackson.databind.annotation.JsonSerialize.Typing.DEFAULT_TYPING;

/**
 * Json 异常处理
 *
 * @author zhangmz
 * @create 2017/6/21
 */
@JsonSerialize( typing = DEFAULT_TYPING )
public class ErrorInfo< T > extends ServerResponse< T > implements Serializable {

    private String url;

    private ErrorInfo( int status, String msg, T data, String url ) {
	super( status, msg, data );
	this.url = url;
    }

    public static < T > ErrorInfo< T > createByError() {
	return createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getDesc() );
    }

    public static < T > ErrorInfo< T > createByErrorCodeMessage( int errorCode, String errorMessage ) {
	return createByErrorCodeMessage( errorCode, errorMessage, null );
    }

    public static < T > ErrorInfo< T > createByErrorCodeMessage( int errorCode, String errorMessage, String url ) {
	return createByErrorCodeMessage( errorCode, errorMessage, null, url );
    }

    public static < T > ErrorInfo< T > createByErrorCodeMessage( int errorCode, String errorMessage, T data, String url ) {
	return new ErrorInfo<>( errorCode, errorMessage, data, url );
    }

    public String getUrl() {
	return url;
    }

    public static void main( String[] args ) {
	ErrorInfo< Object > error = ErrorInfo.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getDesc() );
	System.out.println( " url " + error.getUrl() );
	System.out.println( JSONObject.toJSON( error ) );
    }

}
