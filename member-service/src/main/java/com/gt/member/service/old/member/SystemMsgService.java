package com.gt.member.service.old.member;


import com.gt.member.entity.Member;
import com.gt.member.entity.MemberCardrecord;

/**
 * 系统消息 提醒包含（会员系统消息  微信消息模板 短信提醒）
 * @author Administrator
 *
 */
public interface SystemMsgService {


	/**
	 * 积分操作 包含消息推送
	 * @param cardRecord
	 * @return
	 */
	public boolean jifenMsg(MemberCardrecord cardRecord, Member member);

	/**
	 * 粉币操作 包含消息推送
	 * @param cardRecord
	 * @return
	 */
	public boolean fenbiMsg(MemberCardrecord cardRecord, Member member);

	/**
	 * 流量操作  包含消息推送
	 * @param cardRecord
	 * @return
	 */
	public boolean flowMsg(MemberCardrecord cardRecord, Member member);

	/**
	 * 会员卡升级通知
	 * @return
	 */
	public boolean upgradeMemberMsg(Member member, String cardNo, String dateTime);

	/**
	 * 新增会员短信通知
	 * @param member
	 * @return
	 */
	public boolean sendNewMemberMsg(Member member);


	/**
	 * 储值卡充值推送
	 * @param member
	 * @return
	 */
	public boolean sendChuzhiCard(Member member, Double money);


	/**
	 * 次卡充值推送
	 * @param member
	 * @param money
	 * @param count
	 * @return
	 */
	public boolean sendCikaCard(Member member, double money, int count);


	/**
	 * 次卡消费推送
	 * @param member
	 * @return
	 */
	public boolean sendCikaXiaofei(Member member);



	/**
	 * 储值卡消费推送
	 * @param member
	 * @return
	 */
	public boolean sendChuzhiXiaofei(Member member, Double money);


	/**
	 * 储值卡退款推送
	 * @param member
	 * @return
	 */
	public boolean sendChuzhiTuikuan(Member member, Double money);

}
