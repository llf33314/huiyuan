package com.gt.member.exception;

/**
 *
 * @author zhangmz
 * @version 1.0.0
 * @date 2017-07-15
 * @since 1.0.0
 */
public class BusinessException extends SystemException {

    public BusinessException( String message ) {
	super( message );
    }

    public BusinessException( int code, String message ) {
	super( code, message );
    }
}
