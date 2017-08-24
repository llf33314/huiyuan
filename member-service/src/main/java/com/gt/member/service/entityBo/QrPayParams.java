package com.gt.member.service.entityBo;

import java.io.Serializable;
import java.util.Map;


/**
 * 二维码合一公共支付接口参数
 * @author LFX
 *
 */
public class QrPayParams  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	/**
	 * 订单id ---墨盒所需字段，其他模块忽略
	 */
	private Integer orderid;

	
	/**
	 * 异步回调处理类  --夸项目调用此字段忽略
	 */
	private String businessUtilName;
	
	
	/**
	 * 支付金额
	 */
	private double totalFee ;
	
	/**
	 * 支付模块ID
	 */
	private int model;
	
	
	
	/**
	 * 商家id
	 */
	private Integer  busId;
	

	/**
	 * appid类型(后期可能会有小程序支付),可为空默认是0：公众号支付,1:小程序支付
	 */
	private int appidType=0;
	
	private String appid;
	
	
	
	/**
	 * 订单号--
	 */
	private String orderNum;


	/**
	 * 会员ID
	 */
	private Integer memberId;
	

 
	
	/**
	 * 描述
	 */
	private String desc;
	
	
	/**
	 * 是否需要同步回调(支付成功后页面跳转),1:需要(returnUrl比传),0:不需要(为0时returnUrl不用传),默认0
	 */
	private Integer isreturn=0;
	
	

	
	
	
	/**
	 * 同步返回url(支付成功后页面跳转)
	 */
	private String returnUrl;
	
	
	/**
	 * 异步回调
	 */
	private String notifyUrl;
	
	/**
	 * 是否需要消息推送,1:需要(sendUrl比传),0:不需要(为0时sendUrl不用传),默认0
	 */
	private Integer isSendMessage=0;
	
	
	/**
	 * 推送路径
	 */
	private String sendUrl;
	
	
	/**
	 * 支付方式  0----系统根据浏览器判断   1---微信支付 2---支付宝 3---多粉钱包支付 
	 */
	private Integer payWay;
	
	
	/**
	 * 墨盒默认0即啊祥不用填,其他人调用填1
	 */
	private Integer sourceType=0;

	/**
	 * 扩展属性，此字段用于处理特殊需求
	 */
	private Map<String, Object> extend;

	public String getBusinessUtilName() {
		return businessUtilName;
	}

	public void setBusinessUtilName(String businessUtilName) {
		this.businessUtilName = businessUtilName;
	}

	public double getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(double totalFee) {
		this.totalFee = totalFee;
	}

	public int getModel() {
		return model;
	}

	public void setModel(int model) {
		this.model = model;
	}

	public Integer getBusId() {
		return busId;
	}

	public void setBusId(Integer busId) {
		this.busId = busId;
	}

	public int getAppidType() {
		return appidType;
	}

	public void setAppidType(int appidType) {
		this.appidType = appidType;
	}

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Integer getOrderid() {
		return orderid;
	}

	public void setOrderid(Integer orderid) {
		this.orderid = orderid;
	}

	public Integer getSourceType() {
		return sourceType;
	}

	public void setSourceType(Integer sourceType) {
		this.sourceType = sourceType;
	}
	
	public String getOrderNum() {
		return orderNum;
	}


	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}

	public Integer getMemberId() {
		return memberId;
	}

	public void setMemberId(Integer memberId) {
		this.memberId = memberId;
	}

	public String getReturnUrl() {
		return returnUrl;
	}

	public void setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
	}

	public Integer getIsreturn() {
		return isreturn;
	}

	public void setIsreturn(Integer isreturn) {
		this.isreturn = isreturn;
	}

	public Integer getIsSendMessage() {
		return isSendMessage;
	}

	public void setIsSendMessage(Integer isSendMessage) {
		this.isSendMessage = isSendMessage;
	}

	public String getSendUrl() {
		return sendUrl;
	}

	public void setSendUrl(String sendUrl) {
		this.sendUrl = sendUrl;
	}

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}

	public Integer getPayWay() {
		return payWay;
	}

	public void setPayWay(Integer payWay) {
		this.payWay = payWay;
	}

	public Map<String, Object> getExtend() {
		return extend;
	}

	public void setExtend(Map<String, Object> extend) {
		this.extend = extend;
	}
	
}
