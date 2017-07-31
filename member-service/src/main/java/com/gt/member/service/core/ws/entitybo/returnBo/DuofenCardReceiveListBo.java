package com.gt.member.service.core.ws.entitybo.returnBo;


import java.io.Serializable;
import java.util.List;

/**
 *  卡包集合 包含卡券信息
 * Created by pengjiangli on 2017/7/31 0031.
 */
public class DuofenCardReceiveListBo implements Serializable{

    /**
     * 查询第三方平台下所有优惠券
     * 接口名:findByThreeMemberId
     * 参数:duofenCardBos  duofenCardReceiveBos
     */

    /**
     * 根据商家 查询商家拥有的卡包信息
     * 接口名:findReceiveByBusUserId_1
     * 参数:duofenCardReceiveBos
     */
    private List<DuofenCardBo > duofenCardBos;

    private List<DuofenCardReceiveBo> duofenCardReceiveBos;

    public List< DuofenCardBo > getDuofenCardBos() {
	return duofenCardBos;
    }

    public void setDuofenCardBos( List< DuofenCardBo > duofenCardBos ) {
	this.duofenCardBos = duofenCardBos;
    }

    public List< DuofenCardReceiveBo > getDuofenCardReceiveBos() {
	return duofenCardReceiveBos;
    }

    public void setDuofenCardReceiveBos( List< DuofenCardReceiveBo > duofenCardReceiveBos ) {
	this.duofenCardReceiveBos = duofenCardReceiveBos;
    }
}
