package com.gt.member.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gt.member.enums.ResponseEnums;

import java.io.Serializable;

import static com.fasterxml.jackson.databind.annotation.JsonSerialize.Typing.DEFAULT_TYPING;

/**
 * 服务响应类
 * <pre>
 *     统一响应返回数据格式
 * </pre>
 *
 * @author zhangmz
 * @create 2017/6/16
 */
//保证序列化json的时候,如果是null的对象,key也会消失
@JsonSerialize( typing = DEFAULT_TYPING )
public class ServerResponse< T > implements Serializable {

    /*状态码*/
    private int status;

    /*返回消息*/
    private String msg;

    /*泛型数据*/
    private T data;

    protected ServerResponse( int status ) {
	this.status = status;
    }

    protected ServerResponse( int status, T data ) {
	this.status = status;
	this.data = data;
    }

    protected ServerResponse( int status, String msg ) {
	this.status = status;
	this.msg = msg;
    }

    protected ServerResponse( int status, String msg, T data ) {
	this.status = status;
	this.msg = msg;
	this.data = data;
    }

    /**
     * 创建响应成功
     *
     * @return ServerResponse
     */
    public static < T > ServerResponse< T > createBySuccess() {
	return createBySuccessMessage( ResponseEnums.SUCCESS.getDesc() );
    }

    /**
     * 创建响应成功
     *
     * @param data 数据包
     *
     * @return ServerResponse
     */
    public static < T > ServerResponse< T > createBySuccess( T data ) {
	return createBySuccess( null, data );
    }

    /**
     * 创建响应成功
     *
     * @param msg 返回消息
     *
     * @return ServerResponse
     */
    public static < T > ServerResponse< T > createBySuccessMessage( String msg ) {
	return createBySuccess( msg, null );
    }

    /**
     * 创建响应成功
     *
     * @param msg  消息
     * @param data 数据包
     *
     * @return ServerResponse
     */
    public static < T > ServerResponse< T > createBySuccess( String msg, T data ) {
	return createBySuccessCodeMessage( ResponseEnums.SUCCESS.getCode(), msg, data );
    }

    /**
     * 创建响应成功
     *
     * @param status 状态码
     * @param msg    消息
     * @param data   数据包
     *
     * @return ServerResponse
     */
    public static < T > ServerResponse< T > createBySuccessCodeMessage( int status, String msg, T data ) {
	return new ServerResponse<>( status, msg, data );
    }

    /**
     * 创建响应失败
     *
     * @return ServerResponse
     */
    public static < T > ServerResponse< T > createByError() {
	return createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getDesc() );
    }

    /**
     * 创建响应失败
     *
     * @param errorMessage 消息
     *
     * @return ServerResponse
     */
    public static < T > ServerResponse< T > createByErrorMessage( String errorMessage ) {
	return createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), errorMessage );
    }

    /**
     * 创建响应失败
     *
     * @param errorCode    状态码
     * @param errorMessage 消息
     *
     * @return ServerResponse
     */
    public static < T > ServerResponse< T > createByErrorCodeMessage( int errorCode, String errorMessage ) {
	return new ServerResponse<>( errorCode, errorMessage );
    }

    //使之不在json序列化结果当中，作用用于判断
    @JsonIgnore
    public boolean isSuccess() {
	return this.status == ResponseEnums.SUCCESS.getCode();
    }

    public int getStatus() {
	return status;
    }

    public T getData() {
	return data;
    }

    public String getMsg() {
	return msg;
    }

}
