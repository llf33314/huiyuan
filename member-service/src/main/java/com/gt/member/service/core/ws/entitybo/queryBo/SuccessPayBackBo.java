package com.gt.member.service.core.ws.entitybo.queryBo;

import java.io.Serializable;

/**
 *
 * 商场购买优惠券包
 * Created by Administrator on 2017/7/31 0031.
 */
public class SuccessPayBackBo implements Serializable{

    private Integer receiveId;//卡包id
    private Integer num;  //数量
    private Integer memberId;  //粉丝id

    public Integer getReceiveId() {
	return receiveId;
    }

    public void setReceiveId( Integer receiveId ) {
	this.receiveId = receiveId;
    }

    public Integer getNum() {
	return num;
    }

    public void setNum( Integer num ) {
	this.num = num;
    }

    public Integer getMemberId() {
	return memberId;
    }

    public void setMemberId( Integer memberId ) {
	this.memberId = memberId;
    }
}
