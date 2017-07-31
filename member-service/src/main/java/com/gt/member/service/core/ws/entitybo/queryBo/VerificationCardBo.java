package com.gt.member.service.core.ws.entitybo.queryBo;

import java.io.Serializable;

/**
 * 卡券核销
 *
 * 接口:verificationCard
 *
 * Created by pengjiangli on 2017/7/31 0031.
 */
public class VerificationCardBo implements Serializable {
    private String codes; //卡券code值
    private Integer storeId; //门店id

    public String getCodes() {
	return codes;
    }

    public void setCodes( String codes ) {
	this.codes = codes;
    }

    public Integer getStoreId() {
	return storeId;
    }

    public void setStoreId( Integer storeId ) {
	this.storeId = storeId;
    }
}
