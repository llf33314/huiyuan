package com.gt.member.service.core.ws.entitybo.queryBo;

import java.io.Serializable;

/**
 * 判断储值卡余额是否充足
 * Created by Administrator on 2017/7/28 0028.
 */
public class AdequateMoneyBo implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer memberId;
    private Double totalMoney;

    public Integer getMemberId() {
	return memberId;
    }

    public void setMemberId( Integer memberId ) {
	this.memberId = memberId;
    }

    public Double getTotalMoney() {
	return totalMoney;
    }

    public void setTotalMoney( Double totalMoney ) {
	this.totalMoney = totalMoney;
    }
}
