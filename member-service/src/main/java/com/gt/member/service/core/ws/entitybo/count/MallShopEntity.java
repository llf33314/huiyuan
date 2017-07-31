package com.gt.member.service.core.ws.entitybo.count;

import java.util.Map;

/**
 * 商场门店订单
 * @author pengjiangli
 *
 */
public class MallShopEntity {
	
	private Integer shopId;  //门店id
	private Map<Integer,MallEntity> malls;  //商品订单详情
	
	private Double discountMemberMoney=0.0; //会员优惠券金额
	
	private Integer useCoupon=0;  //是否使用优惠券
	private Integer couponType; //优惠券类型 0微信 1多粉优惠券
	private Integer coupondId;  //卡券id
	private String codes;  //使用优惠券code值 用来核销卡券 不存在set
	private Integer couponNum=1;  //使用优惠券数量 不存在set
	private Integer canUseConpon=0;  //是否能用优惠券
	private Double discountConponMoney=0.0;   //优惠券优惠券金额
	
	private Double discountfenbiMoney=0.0; //粉币抵扣金额  不存在set
	
	private Double discountjifenMoney=0.0; //积分抵扣金额  不存在set
	
	private Double balanceMoney=0.0;  //支付金额

	public Integer getShopId() {
		return shopId;
	}

	public void setShopId(Integer shopId) {
		this.shopId = shopId;
	}

	public Map<Integer, MallEntity> getMalls() {
		return malls;
	}

	public void setMalls(Map<Integer, MallEntity> malls) {
		this.malls = malls;
	}

	public Double getDiscountMemberMoney() {
		return discountMemberMoney;
	}

	public void setDiscountMemberMoney(Double discountMemberMoney) {
		this.discountMemberMoney = discountMemberMoney;
	}

	public Integer getUseCoupon() {
		return useCoupon;
	}

	public void setUseCoupon(Integer useCoupon) {
		this.useCoupon = useCoupon;
	}

	public Integer getCouponType() {
		return couponType;
	}

	public void setCouponType(Integer couponType) {
		this.couponType = couponType;
	}

	public Integer getCoupondId() {
		return coupondId;
	}

	public void setCoupondId(Integer coupondId) {
		this.coupondId = coupondId;
	}

	public String getCodes() {
		return codes;
	}

	public void setCodes(String codes) {
		this.codes = codes;
	}

	public Integer getCouponNum() {
		return couponNum;
	}

	public void setCouponNum(Integer couponNum) {
		this.couponNum = couponNum;
	}

	public Integer getCanUseConpon() {
		return canUseConpon;
	}

	public void setCanUseConpon(Integer canUseConpon) {
		this.canUseConpon = canUseConpon;
	}

	public Double getDiscountConponMoney() {
		return discountConponMoney;
	}

	public void setDiscountConponMoney(Double discountConponMoney) {
		this.discountConponMoney = discountConponMoney;
	}

	public Double getDiscountfenbiMoney() {
		return discountfenbiMoney;
	}

	public void setDiscountfenbiMoney(Double discountfenbiMoney) {
		this.discountfenbiMoney = discountfenbiMoney;
	}

	public Double getDiscountjifenMoney() {
		return discountjifenMoney;
	}

	public void setDiscountjifenMoney(Double discountjifenMoney) {
		this.discountjifenMoney = discountjifenMoney;
	}

	public Double getBalanceMoney() {
		return balanceMoney;
	}

	public void setBalanceMoney(Double balanceMoney) {
		this.balanceMoney = balanceMoney;
	}

}
