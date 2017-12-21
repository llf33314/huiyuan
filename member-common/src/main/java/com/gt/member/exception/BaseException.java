package com.gt.member.exception;

import com.gt.api.enums.ResponseEnums;
import com.gt.member.enums.ResponseMemberEnums;

/**
 * 统一异常类
 * <pre>
 *     所有自定义的异常，都继承此类。
 * </pre>
 *
 * @author pengjiangli
 * @create 2017/6/16
 */
public class BaseException extends RuntimeException {

    private int code=1;//状态码

    private String message="错误";//错误消息

    private String url;  //跳转地址

    public BaseException() {

    }

    /**
     * 构造错误异常信息
     * @param message 错误消息
     */
    public BaseException(String message) {
        super(message);
        this.message = message;
    }
    /**
     * 构造错误异常信息
     * @param code 错误代码
     * @param message 错误消息
     */
    public BaseException(int code, String message,String url) {
        super(message);
        this.message = message;
        this.code = code;
        this.url=url;
    }


    /**
     * 构造错误异常信息
     * @param code 错误代码
     * @param message 错误消息
     */
    public BaseException(int code, String message) {
        super(message);
        this.message = message;
        this.code = code;
    }
    /**
     * 构造错误异常信息
     * @param responseEnums 通用枚举类
     */
    public BaseException(ResponseEnums responseEnums){
        super(responseEnums.getMsg());
        this.code = responseEnums.getCode();
        this.message= responseEnums.getMsg();
    }

    /**
     * 构造错误异常信息
     * @param responseMemberEnums 会员枚举类
     */
    public BaseException(ResponseMemberEnums responseMemberEnums){
        super(responseMemberEnums.getMsg());
        this.code = responseMemberEnums.getCode();
        this.message= responseMemberEnums.getMsg();
    }

    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }


    public String getUrl() {
        return url;
    }
}
