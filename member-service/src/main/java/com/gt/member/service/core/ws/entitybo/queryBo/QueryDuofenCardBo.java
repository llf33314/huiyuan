package com.gt.member.service.core.ws.entitybo.queryBo;

import java.io.Serializable;

/**
 * 多粉卡券接口请求参数信息
 *
 * Created by pengjiangli on 2017/7/31 0031.
 */
public class QueryDuofenCardBo implements Serializable{

    /**
     * 查询用户拥有的优惠券
     * 接口名:findDuofenCardByMemberId
     * 参数:memberId  wxshopId
     */

    /**
     * 查询用户拥有的优惠券 过滤不满足的消费的卡券
     * 接口名：findDuofenCardByMemberIdAndMoney
     * 参数:memberId  wxshopId money
     */

    /**
     * 根据查询商家商场投放的包
     * 接口名:findReceiveBybusId
     * 参数:busId
     */

    /**
     * 根据商家 查询商家拥有的卡包信息
     *  接口名:findReceiveByBusUserId_1
     *  参数：busId  receiveId
     */

    /**
     *卡包信息（购买） 美容
     * 接口名:findReceive
     * 参数:busId
     */

    /**
     * 根据卡包查询卡券信息
     * 接口名:findDuofenCard
     * 参数:busId 、 receiveId
     *
     */

    private Integer memberId;   //粉丝id
    private Integer wxshopId;  //门店信息
    private Double money;  //金额
    private Integer busId; //商家id
    private Integer receiveId;  //卡包id


    public Integer getMemberId() {
	return memberId;
    }

    public void setMemberId( Integer memberId ) {
	this.memberId = memberId;
    }

    public Integer getWxshopId() {
	return wxshopId;
    }

    public void setWxshopId( Integer wxshopId ) {
	this.wxshopId = wxshopId;
    }

    public Double getMoney() {
	return money;
    }

    public void setMoney( Double money ) {
	this.money = money;
    }

    public Integer getBusId() {
	return busId;
    }

    public void setBusId( Integer busId ) {
	this.busId = busId;
    }
    public Integer getReceiveId() {
	return receiveId;
    }

    public void setReceiveId( Integer receiveId ) {
	this.receiveId = receiveId;
    }
}
