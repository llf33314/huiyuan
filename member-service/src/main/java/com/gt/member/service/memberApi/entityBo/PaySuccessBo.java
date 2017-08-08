package com.gt.member.service.memberApi.entityBo;

import java.io.Serializable;

/**
 * 支付成功 实体类
 * <p>
 * Created by pengjiangli on 2017/8/2 0002.
 */
public class PaySuccessBo implements Serializable {

    private Integer memberId; //粉丝id
    private Integer storeId;  //门店id
    private String  orderCode;  //订单号
    private Double  totalMoney;  //订单金额
    private Double  discountMoney;  //折扣金额
    private Integer payType = 0;  //支付方式 0支付宝 1微信 2银联 3线下充值 4货到付款 5储值卡支付 6积分支付 7粉币支付 8到店支付 9找人代付  10现金支付 11分期支付
    private Double  pay     = 0.0; //支付金额

    private Integer ucType; //消费类型 字典1197加模块消费类型 5积分兑换商品 7会员卡充值 13购买会员卡 14线下核销 18优惠买单  19粉币兑换物品 20流量充值 21微预约

    private boolean useCoupon = false;  //是否使用优惠券
    private Integer couponType; //优惠券类型 0微信 1多粉优惠券
    private String  codes;  //使用优惠券code值 用来核销卡券

    private boolean userFenbi = false; //使用使用粉币
    private Double fenbiNum;  //粉币数量

    private boolean userJifen = false;  //是否使用积分
    private Integer jifenNum;  //积分数量

    private Integer delay      = 0;   //会员赠送物品 0延迟送 1立即送
    private Integer dataSource = 0;  //数据来源 0:pc端 1:微信 2:uc端 3:小程序
    private String ucTable;   //订单表

    public Integer getMemberId() {
	return memberId;
    }

    public void setMemberId( Integer memberId ) {
	this.memberId = memberId;
    }

    public Double getDiscountMoney() {
	return discountMoney;
    }

    public void setDiscountMoney( Double discountMoney ) {
	this.discountMoney = discountMoney;
    }

    public Integer getStoreId() {
	return storeId;
    }

    public void setStoreId( Integer storeId ) {
	this.storeId = storeId;
    }

    public Integer getDataSource() {
	return dataSource;
    }

    public void setDataSource( Integer dataSource ) {
	this.dataSource = dataSource;
    }

    public Double getPay() {
	return pay;
    }

    public void setPay( Double pay ) {
	this.pay = pay;
    }

    public boolean isUseCoupon() {
	return useCoupon;
    }

    public void setUseCoupon( boolean useCoupon ) {
	this.useCoupon = useCoupon;
    }

    public Integer getCouponType() {
	return couponType;
    }

    public void setCouponType( Integer couponType ) {
	this.couponType = couponType;
    }

    public String getCodes() {
	return codes;
    }

    public void setCodes( String codes ) {
	this.codes = codes;
    }

    public boolean isUserFenbi() {
	return userFenbi;
    }

    public void setUserFenbi( boolean userFenbi ) {
	this.userFenbi = userFenbi;
    }

    public Double getFenbiNum() {
	return fenbiNum;
    }

    public void setFenbiNum( Double fenbiNum ) {
	this.fenbiNum = fenbiNum;
    }

    public boolean isUserJifen() {
	return userJifen;
    }

    public void setUserJifen( boolean userJifen ) {
	this.userJifen = userJifen;
    }

    public Integer getJifenNum() {
	return jifenNum;
    }

    public void setJifenNum( Integer jifenNum ) {
	this.jifenNum = jifenNum;
    }

    public String getOrderCode() {
	return orderCode;
    }

    public void setOrderCode( String orderCode ) {
	this.orderCode = orderCode;
    }

    public Integer getUcType() {
	return ucType;
    }

    public void setUcType( Integer ucType ) {
	this.ucType = ucType;
    }

    public Double getTotalMoney() {
	return totalMoney;
    }

    public void setTotalMoney( Double totalMoney ) {
	this.totalMoney = totalMoney;
    }

    public Integer getPayType() {
	return payType;
    }

    public void setPayType( Integer payType ) {
	this.payType = payType;
    }

    public String getUcTable() {
	return ucTable;
    }

    public void setUcTable( String ucTable ) {
	this.ucTable = ucTable;
    }

    public Integer getDelay() {
	return delay;
    }

    public void setDelay( Integer delay ) {
	this.delay = delay;
    }
}
