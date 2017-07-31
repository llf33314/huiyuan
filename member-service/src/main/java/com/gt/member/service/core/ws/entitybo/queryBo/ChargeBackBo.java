package com.gt.member.service.core.ws.entitybo.queryBo;

import java.io.Serializable;

/**
 *  储值卡退款接口
 *
 * chargeBack 方法请求参数
 * Created by Administrator on 2017/7/28 0028.
 */
public class ChargeBackBo implements Serializable{
    private Integer memberId;   //粉丝id
    private double refundMoney;  //退款金额

    public Integer getMemberId() {
	return memberId;
    }

    public void setMemberId( Integer memberId ) {
	this.memberId = memberId;
    }

    public double getRefundMoney() {
	return refundMoney;
    }

    public void setRefundMoney( double refundMoney ) {
	this.refundMoney = refundMoney;
    }
}
