package com.gt.member.exception;

import com.gt.api.enums.ResponseEnums;
import com.gt.member.enums.ResponseMemberEnums;

/**
 *
 * @author pengjiangli
 * @version 1.0.0
 * @date 2017-07-15
 * @since 1.0.0
 */
public class NeedLoginException extends BaseException {

    public NeedLoginException(){

    }

    public NeedLoginException( String message ) {
	    super( message );
    }

    public NeedLoginException( int code, String message,String url) {
	super( code, message,url );
    }

    public NeedLoginException(ResponseEnums responseEnums) {
        super(responseEnums);
    }

    public NeedLoginException(ResponseMemberEnums responseMemberEnums) {
        super(responseMemberEnums);
    }

}
