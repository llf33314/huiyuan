package com.gt.member.service.core.ws.entitybo.count;

import java.util.Map;

/**
 * 总订单
 * @author pengjiangli
 *
 */
public class MallAllEntity {
	
	private Integer memberId;  //粉丝信息
	private Double totalMoney=0.0; //订单总金额
	private Map<Integer,MallShopEntity> mallShops;  //门店商品信息  key为门店id
	
	private Double discountMemberMoney=0.0; //会员优惠券金额
	
	private Double discountConponMoney=0.0;   //优惠券券优惠金额
	
	private Integer useFenbi=0;  //是否使用粉币
	private Double fenbiNum=0.0;  //使用粉币数量 不存在set
	private Double discountfenbiMoney=0.0; //粉币抵扣金额  不存在set
	private Integer canUsefenbi=0;  //是否能用粉币
	
	private Integer userJifen=0;  //是否使用积分
	private Integer jifenNum=0;  //使用积分数量 不存在set
	private Double discountjifenMoney=0.0; //积分抵扣金额  不存在set
	private Integer canUseJifen=0;  //是否能用积分
	
	private Double balanceMoney=0.0;  //支付金额

	
	public Integer getMemberId() {
		return memberId;
	}

	public void setMemberId(Integer memberId) {
		this.memberId = memberId;
	}

	public Double getTotalMoney() {
		return totalMoney;
	}

	public void setTotalMoney(Double totalMoney) {
		this.totalMoney = totalMoney;
	}

	public Map<Integer, MallShopEntity> getMallShops() {
		return mallShops;
	}

	public void setMallShops(Map<Integer, MallShopEntity> mallShops) {
		this.mallShops = mallShops;
	}

	public Double getDiscountMemberMoney() {
		return discountMemberMoney;
	}

	public void setDiscountMemberMoney(Double discountMemberMoney) {
		this.discountMemberMoney = discountMemberMoney;
	}

	public Double getDiscountConponMoney() {
		return discountConponMoney;
	}

	public void setDiscountConponMoney(Double discountConponMoney) {
		this.discountConponMoney = discountConponMoney;
	}

	public Integer getUseFenbi() {
		return useFenbi;
	}

	public void setUseFenbi(Integer useFenbi) {
		this.useFenbi = useFenbi;
	}

	public Double getFenbiNum() {
		return fenbiNum;
	}

	public void setFenbiNum(Double fenbiNum) {
		this.fenbiNum = fenbiNum;
	}

	public Double getDiscountfenbiMoney() {
		return discountfenbiMoney;
	}

	public void setDiscountfenbiMoney(Double discountfenbiMoney) {
		this.discountfenbiMoney = discountfenbiMoney;
	}

	public Integer getCanUsefenbi() {
		return canUsefenbi;
	}

	public void setCanUsefenbi(Integer canUsefenbi) {
		this.canUsefenbi = canUsefenbi;
	}

	public Integer getUserJifen() {
		return userJifen;
	}

	public void setUserJifen(Integer userJifen) {
		this.userJifen = userJifen;
	}

	public Integer getJifenNum() {
		return jifenNum;
	}

	public void setJifenNum(Integer jifenNum) {
		this.jifenNum = jifenNum;
	}

	public Double getDiscountjifenMoney() {
		return discountjifenMoney;
	}

	public void setDiscountjifenMoney(Double discountjifenMoney) {
		this.discountjifenMoney = discountjifenMoney;
	}

	public Integer getCanUseJifen() {
		return canUseJifen;
	}

	public void setCanUseJifen(Integer canUseJifen) {
		this.canUseJifen = canUseJifen;
	}

	public Double getBalanceMoney() {
		return balanceMoney;
	}

	public void setBalanceMoney(Double balanceMoney) {
		this.balanceMoney = balanceMoney;
	}
	

}
