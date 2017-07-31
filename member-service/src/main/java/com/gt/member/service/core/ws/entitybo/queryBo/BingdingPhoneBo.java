package com.gt.member.service.core.ws.entitybo.queryBo;

import java.io.Serializable;

/**
 * 绑定手机号码
 *
 * BingdingPhone  方法请求参数
 *
 * Created by Administrator on 2017/7/28 0028.
 */
public class BingdingPhoneBo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer memberId;  //memberId
    private String code;  //短信校验码
    private String phone;  //手机号码
    private Integer busId;  //商家id

    public String getCode() {
	return code;
    }

    public void setCode( String code ) {
	this.code = code;
    }

    public String getPhone() {
	return phone;
    }

    public void setPhone( String phone ) {
	this.phone = phone;
    }

    public Integer getBusId() {
	return busId;
    }

    public void setBusId( Integer busId ) {
	this.busId = busId;
    }

    public Integer getMemberId() {

	return memberId;
    }

    public void setMemberId( Integer memberId ) {
	this.memberId = memberId;
    }
}
