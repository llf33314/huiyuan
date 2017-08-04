package com.gt.member.exception;

import com.gt.member.enums.ResponseEnums;
import com.gt.member.enums.ResponseMemberEnums;

/**
 *
 * @author zhangmz
 * @version 1.0.0
 * @date 2017-07-15
 * @since 1.0.0
 */
public class BusinessException extends SystemException {

    public BusinessException(){

    }

    public BusinessException( String message ) {
	    super( message );
    }

    public BusinessException( int code, String message ) {
	super( code, message );
    }

    public BusinessException(ResponseEnums responseEnums) {
        super(responseEnums);
    }

    public BusinessException(ResponseMemberEnums responseMemberEnums) {
        super(responseMemberEnums);
    }
}
