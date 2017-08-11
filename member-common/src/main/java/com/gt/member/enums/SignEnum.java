package com.gt.member.enums;

/**
 * Created by Administrator on 2017/8/10 0010.
 */
public enum  SignEnum {
    SUCCESS("001", "检查成功"),

    TIME_OUT("101", "请求超时"),
    SIGN_ERROR("102", "签名错误"),

    SERVER_ERROR("201", "系统错误");

    private String code;
    private String value;

    private SignEnum(String code, String value) { this.code = code;
	this.value = value;
    }

    public String getCode()
    {
	return this.code;
    }

    public void setCode(String code) {
	this.code = code;
    }

    public String getValue() {
	return this.value;
    }

    public void setValue(String value) {
	this.value = value;
    }
}