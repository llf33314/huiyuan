package com.gt.member.service.member;


import com.gt.member.entity.MemberCardrecordNew;
import com.gt.member.entity.MemberEntity;
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
	public boolean jifenMsg(MemberCardrecordNew cardRecord, MemberEntity memberEntity );

	/**
	 * 粉币操作 包含消息推送
	 * @param cardRecord
	 * @return
	 */
	public boolean fenbiMsg(MemberCardrecordNew cardRecord, MemberEntity memberEntity );

	/**
	 * 流量操作  包含消息推送
	 * @param cardRecord
	 * @return
	 */
	public boolean flowMsg(MemberCardrecordNew cardRecord, MemberEntity memberEntity );

	/**
	 * 会员卡升级通知
	 * @return
	 */
	public boolean upgradeMemberMsg(MemberEntity memberEntity, String cardNo, String dateTime);

	/**
	 * 新增会员短信通知
	 * @param memberEntity
	 * @return
	 */
	public boolean sendNewMemberMsg(MemberEntity memberEntity );


	/**
	 * 储值卡充值推送
	 * @param memberEntity
	 * @return
	 */
	public boolean sendChuzhiCard(MemberEntity memberEntity, Double money);


	/**
	 * 次卡充值推送
	 * @param memberEntity
	 * @param money
	 * @param count
	 * @return
	 */
	public boolean sendCikaCard(MemberEntity memberEntity, double money, int count);


	/**
	 * 次卡消费推送
	 * @param memberEntity
	 * @return
	 */
	public boolean sendCikaXiaofei(MemberEntity memberEntity );



	/**
	 * 储值卡消费推送
	 * @param memberEntity
	 * @return
	 */
	public boolean sendChuzhiXiaofei(MemberEntity memberEntity, Double money);


	/**
	 * 储值卡退款推送
	 * @param memberEntity
	 * @return
	 */
	public boolean sendChuzhiTuikuan(MemberEntity memberEntity, Double money);

}
