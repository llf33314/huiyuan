package com.gt.member.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpSession;

/**
 * BaseController
 *
 * @author zhangmz
 * @create 2017/7/10
 */
public abstract class BaseController {
    /**
     * 日志
     */
    protected Logger logger = LoggerFactory.getLogger( this.getClass() );

    /**
     * 获取Sessionid
     *
     * @param session HttpSession
     *
     * @return
     */
    public String getSessionId( HttpSession session ) {
	return session.getId();
    }
}
