package com.gt.member.service.core.ws;

import org.apache.cxf.annotations.GZIP;

import javax.jws.WebService;

/**
 * 会员ws接口
 * Created by Administrator on 2017/7/27 0027.
 */
@WebService
@GZIP
public interface MemberNewWS {

    /**
     * 数据api
     * @param json 请求参数
     * @return
     */
    String reInvoke(String json);

}
