package com.gt.member.service.common.quartz;

public interface MemberQuartzService {
	
	 /**
	  * 过期卡券 修改领取卡券状态
	  */
	 void updateCardGet();
	 
	 

	/**
	  * 修改会员消息信息
	  */
	public void updateNotice();
	
	
	/**
	 * 定时发送短信
	 */
	public void sendNotice();
	
	/**
	 * 积分清0 七天前短信 或系统消息提醒
	 */
	public void clearJifenSendMessage();
	
	/**
	 * 每月22号触发 短信
	 */
	
	public void clearJifenSendSmsMessage();
	
	
	/**
	 * 积分清0
	 */
	public void clearJifen();
	
	/**
	 * 会员生日推送
	 */
	public void birthdayMsg();
	
	/**
	 * 会员生日发送短信
	 */
	public void sendBir();
	
	
	/**
	 * 短信发送
	 */
	public void birthdaySms();

	
}
