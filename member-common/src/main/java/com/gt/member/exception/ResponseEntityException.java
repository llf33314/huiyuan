package com.gt.member.exception;

import com.gt.api.enums.ResponseEnums;
import com.gt.member.enums.ResponseMemberEnums;

/**
 * Ajax 异常
 * <pre>
 *     ajax 请求的异常处理类
 * </pre>
 *
 * @author zhangmz
 * @create 2017/6/21
 */
public class ResponseEntityException extends BaseException {

    public ResponseEntityException( String message ) {
	super( message );
    }

    public ResponseEntityException( int code, String message ) {
	super( code, message );
    }

    public ResponseEntityException(ResponseEnums responseEnums) {
        super(responseEnums);
    }

    public ResponseEntityException(ResponseMemberEnums responseMemberEnums) {
        super(responseMemberEnums);
    }
}
