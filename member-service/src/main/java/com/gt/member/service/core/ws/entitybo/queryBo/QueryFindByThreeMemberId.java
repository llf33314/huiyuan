package com.gt.member.service.core.ws.entitybo.queryBo;

import java.io.Serializable;

/**
 *
 * 查询第三方平台下所有优惠券
 * FindByThreeMemberId
 * 请求参数
 *
 * Created by pengjiangli on 2017/7/31 0031.
 */
public class QueryFindByThreeMemberId implements Serializable{
    private Integer page;
    private Integer threeMemberId;

    public Integer getPage() {
	return page;
    }

    public void setPage( Integer page ) {
	this.page = page;
    }

    public Integer getThreeMemberId() {
	return threeMemberId;
    }

    public void setThreeMemberId( Integer threeMemberId ) {
	this.threeMemberId = threeMemberId;
    }
}
