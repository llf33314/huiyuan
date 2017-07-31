package com.gt.member.service.core.ws.entitybo.queryBo;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/7/28 0028.
 */
public class QueryWxCardBo implements Serializable{

    /**
     * 查询 商户下所有微信优惠券
     * 接口名称 findWxCardByShopId
     * 参数: memberId  publicId  shopId
     *
     */
    private Integer memberId;  //粉丝id

    private Integer publicId;  //公众id

    private Integer shopId;  //门店id

    /**
     * 微信卡券核销接口
     * 接口名:wxCardReceive
     * 参数:publicId  、code
     */
    private String code;

    /**
     * 查询 商户下所有用的卡券信息 过滤不满足消费金额的优惠券
     *  接口名:findWxCardByShopIdAndMoney
     *  参数:memberId  publicId  shopId  money
     *
     */
    //<!--------------findWxCardByShopIdAndMoney接口 包含上面三个属性--------------->
    private Double money;  //金额

    /**
     * 查询微信卡券信息
     * 接口名:findWxCardById
     * 参数:id
     */
    private Integer id;


    public Integer getMemberId() {
	return memberId;
    }

    public void setMemberId( Integer memberId ) {
	this.memberId = memberId;
    }

    public Integer getPublicId() {
	return publicId;
    }

    public void setPublicId( Integer publicId ) {
	this.publicId = publicId;
    }

    public Integer getShopId() {
	return shopId;
    }

    public void setShopId( Integer shopId ) {
	this.shopId = shopId;
    }

    public Double getMoney() {
	return money;
    }

    public void setMoney( Double money ) {
	this.money = money;
    }

    public String getCode() {
	return code;
    }

    public void setCode( String code ) {
	this.code = code;
    }

    public Integer getId() {
	return id;
    }

    public void setId( Integer id ) {
	this.id = id;
    }
}
