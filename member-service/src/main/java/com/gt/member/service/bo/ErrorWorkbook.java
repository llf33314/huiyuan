package com.gt.member.service.bo;

import java.io.Serializable;

public class ErrorWorkbook implements Serializable{

	
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	private String cardNo; //卡号
	private String cardNoStyle; 
	
	private String cname; //名称
	private String cnameStyle; 
	
	private String sex;  //性别
	private String sexStyle; 
	
	private String phone;  //手机号
	private String phoneStyle; 
	
	private String lingquDate; //领取时间
	private String lingquDateStyle; 
	
	private String balance;  //余额或次数
	private String balanceStyle; 
	
	private String jifen;  //积分
	private String jifenStyle; 
	
	private String memberType; //会员卡类型
	private String memberTypeStyle; 
	
	private String memberGrade;//会员卡等级
	private String memberGradeStyle; 
	
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	public String getCname() {
		return cname;
	}
	public void setCname(String cname) {
		this.cname = cname;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getLingquDate() {
		return lingquDate;
	}
	public void setLingquDate(String lingquDate) {
		this.lingquDate = lingquDate;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	public String getJifen() {
		return jifen;
	}
	public void setJifen(String jifen) {
		this.jifen = jifen;
	}
	public String getMemberType() {
		return memberType;
	}
	public void setMemberType(String memberType) {
		this.memberType = memberType;
	}
	public String getMemberGrade() {
		return memberGrade;
	}
	public void setMemberGrade(String memberGrade) {
		this.memberGrade = memberGrade;
	}
	public String getCardNoStyle() {
		return cardNoStyle;
	}
	public void setCardNoStyle(String cardNoStyle) {
		this.cardNoStyle = cardNoStyle;
	}
	public String getCnameStyle() {
		return cnameStyle;
	}
	public void setCnameStyle(String cnameStyle) {
		this.cnameStyle = cnameStyle;
	}
	public String getSexStyle() {
		return sexStyle;
	}
	public void setSexStyle(String sexStyle) {
		this.sexStyle = sexStyle;
	}
	public String getPhoneStyle() {
		return phoneStyle;
	}
	public void setPhoneStyle(String phoneStyle) {
		this.phoneStyle = phoneStyle;
	}
	public String getLingquDateStyle() {
		return lingquDateStyle;
	}
	public void setLingquDateStyle(String lingquDateStyle) {
		this.lingquDateStyle = lingquDateStyle;
	}
	public String getBalanceStyle() {
		return balanceStyle;
	}
	public void setBalanceStyle(String balanceStyle) {
		this.balanceStyle = balanceStyle;
	}
	public String getJifenStyle() {
		return jifenStyle;
	}
	public void setJifenStyle(String jifenStyle) {
		this.jifenStyle = jifenStyle;
	}
	public String getMemberTypeStyle() {
		return memberTypeStyle;
	}
	public void setMemberTypeStyle(String memberTypeStyle) {
		this.memberTypeStyle = memberTypeStyle;
	}
	public String getMemberGradeStyle() {
		return memberGradeStyle;
	}
	public void setMemberGradeStyle(String memberGradeStyle) {
		this.memberGradeStyle = memberGradeStyle;
	}
	
	
	
}
