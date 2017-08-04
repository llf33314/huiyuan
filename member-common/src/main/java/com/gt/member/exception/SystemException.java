package com.gt.member.exception;

import com.gt.member.enums.ResponseEnums;
import com.gt.member.enums.ResponseMemberEnums;

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

    public SystemException() {

    }

    /**
     * 构造错误异常信息
     * @param message 错误消息
     */
    public SystemException(String message) {
        super(message);
        this.message = message;
    }
    /**
     * 构造错误异常信息
     * @param code 错误代码
     * @param message 错误消息
     */
    public SystemException(int code, String message) {
        super(message);
        this.message = message;
        this.code = code;
    }
    /**
     * 构造错误异常信息
     * @param responseEnums 通用枚举类
     */
    public SystemException(ResponseEnums responseEnums){
        super(responseEnums.getDesc());
        this.code = responseEnums.getCode();
        this.message= responseEnums.getDesc();
    }

    /**
     * 构造错误异常信息
     * @param responseMemberEnums 会员枚举类
     */
    public SystemException(ResponseMemberEnums responseMemberEnums){
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
}
