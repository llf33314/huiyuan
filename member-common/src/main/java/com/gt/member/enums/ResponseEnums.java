package com.gt.member.enums;

/**
 * 响应成功Code 类
 *
 * @author zhangmz
 * @create 2017/6/12
 */
public enum ResponseEnums {
    SUCCESS( 0, "成功" ),
    ERROR( 1, "错误" ),
    NEED_LOGIN( 1001, "需要登录" ),
    AUTHENTICATION( 10, "非法认证" ),
    JWT_TOKEN_EXPIRED( 11, "TOKEN失效" );

    private final int    code;
    private final String desc;

    ResponseEnums( int code, String desc ) {
	this.code = code;
	this.desc = desc;
    }

    public int getCode() {
	return code;
    }

    public String getDesc() {
	return desc;
    }
}
