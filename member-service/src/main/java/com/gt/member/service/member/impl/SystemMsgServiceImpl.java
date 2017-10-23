package com.gt.member.service.member.impl;


import com.gt.member.entity.MemberCardrecordNew;
import com.gt.member.entity.MemberEntity;
import com.gt.member.entity.MemberCardrecord;
import com.gt.member.service.member.SystemMsgService;
import org.springframework.stereotype.Service;


/**
 * 系统消息 提醒包含（会员系统消息 微信消息模板 短信提醒）
 *
 * @author Administrator
 *
 */
@Service
public class SystemMsgServiceImpl implements SystemMsgService {

	@Override
	public boolean jifenMsg(MemberCardrecordNew cardRecord, MemberEntity memberEntity ) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean fenbiMsg(MemberCardrecordNew cardRecord, MemberEntity memberEntity ) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean flowMsg(MemberCardrecordNew cardRecord, MemberEntity memberEntity ) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean upgradeMemberMsg(MemberEntity memberEntity, String cardNo,
			String dateTime) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean sendNewMemberMsg(MemberEntity memberEntity ) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean sendChuzhiCard(MemberEntity memberEntity, Double money) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean sendCikaCard(MemberEntity memberEntity, double money, int count) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean sendCikaXiaofei(MemberEntity memberEntity ) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean sendChuzhiXiaofei(MemberEntity memberEntity, Double money) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean sendChuzhiTuikuan(MemberEntity memberEntity, Double money) {
		// TODO Auto-generated method stub
		return false;
	}

	/*private static final Logger LOG = Logger
			.getLogger(SystemMsgServiceImpl.class);

	@Autowired
	private SystemNoticeMapper systemNoticeMapper;

	@Autowired
	private MsgTemplateService msgTemplateService;

	@Autowired
	private WxPublicUsersMapper wxPublicUsersMapper;

	@Autowired
	private BusUserMapper busUserMapper;

	@Autowired
	private SmsSpendingService smsSpendingService;

	@Autowired
	private CardMapper cardMapper;

	@Override
	public boolean jifenMsg(CardRecord cardRecord, MemberEntity member) {
		try {
			SystemNotice systemNotice = systemNoticeMapper
					.findBybusIdAndCallType(member.getBusid(), (byte) 2);
			// 公众号消息推送
			if (CommonUtil.isNotEmpty(systemNotice)
					&& systemNotice.getPublicmsg() == 1) {
				WxPublicUsersEntity wxPublicUsers = wxPublicUsersMapper
						.selectByPrimaryKey(member.getPublicId());
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("id", systemNotice.getPublicidmsgid());
				params.put("memberId", member.getId());
				String url = PropertiesUtil.getWebHomeUrl()
						+ "/phoneMemberController/" + member.getBusid()
						+ "/79B4DE7C/findMember_1.do";
				params.put("url", url);
				List<Object> list = new ArrayList<Object>();
				// first,keyword1,keyword2,keyword3,keyword4,remark
				list.add("积分提醒");
				list.add(DateTimeKit.getDateTime());
				list.add(cardRecord.getNumber());
				list.add(cardRecord.getItemname());
				list.add(member.getIntegral());
				list.add("积分详情：请到会员卡积分记录查看");
				msgTemplateService.sendMsg(wxPublicUsers, params, list);
			}
		} catch (Exception e) {
			LOG.error("积分变动消息推送异常", e);
			return false;
		}
		return true;
	}

	@Override
	public boolean fenbiMsg(CardRecord cardRecord, MemberEntity member) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean flowMsg(CardRecord cardRecord, MemberEntity member) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean upgradeMemberMsg(MemberEntity member, String cardNo,
			String dateTime) {
		try {
			SystemNotice systemNotice = systemNoticeMapper
					.findBybusIdAndCallType(member.getBusid(), (byte) 7);
			// 公众号消息推送
			if (CommonUtil.isNotEmpty(systemNotice)
					&& systemNotice.getPublicmsg() == 1) {
				WxPublicUsersEntity wxPublicUsers = wxPublicUsersMapper
						.selectByPrimaryKey(member.getPublicId());
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("id", systemNotice.getPublicidmsgid());
				params.put("memberId", member.getId());
				String url = PropertiesUtil.getWebHomeUrl()
						+ "/phoneMemberController/" + member.getBusid()
						+ "/79B4DE7C/findMember_1.do";
				params.put("url", url);
				List<Object> list = new ArrayList<Object>();
				// first,keyword1,keyword2,keyword3,keyword4,remark
				list.add("会员升级通知");
				list.add(cardNo);
				list.add(dateTime);
				list.add("通知详情：请到公众号会员卡信息将会体现");
				msgTemplateService.sendMsg(wxPublicUsers, params, list);
			}
		} catch (Exception e) {
			LOG.error("积分变动消息推送异常", e);
			return false;
		}
		return true;
	}

	@Override
	public boolean sendNewMemberMsg(MemberEntity member) {
		SystemNotice systemNotice = systemNoticeMapper.findBybusIdAndCallType(
				member.getBusid(), (byte) 11);
		if (CommonUtil.isNotEmpty(systemNotice)
				&& CommonUtil.isNotEmpty(member.getPhone())) {
			BusUserEntity bususer = busUserMapper.selectByPrimaryKey(member
					.getBusid());

			String content = systemNotice.getSmscontent();

			Map<String, Object> params = new HashMap<String, Object>();
			params.put("busId", member.getBusid());
			params.put("model", 3);
			params.put("mobiles", member.getPhone());
			params.put("company", bususer.getMerchant_name());
			params.put("content", content);
			try {
				smsSpendingService.sendSms(params);
			} catch (Exception e) {
				LOG.error("短信发送失败", e);
				return false;
			}
			return true;
		}

		return true;
	}

	@Override
	public boolean sendChuzhiCard(MemberEntity member, Double money) {
		try {
			SystemNotice systemNotice = systemNoticeMapper
					.findBybusIdAndCallType(member.getBusid(), (byte) 4);
			// 公众号消息推送
			if (CommonUtil.isNotEmpty(systemNotice)
					&& systemNotice.getPublicmsg() == 1) {
				WxPublicUsersEntity wxPublicUsers = wxPublicUsersMapper
						.selectByPrimaryKey(member.getPublicId());
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("id", systemNotice.getPublicidmsgid());
				params.put("memberId", member.getId());
				String url = PropertiesUtil.getWebHomeUrl()
						+ "/phoneMemberController/" + member.getBusid()
						+ "/79B4DE7C/findMember_1.do";
				params.put("url", url);
				List<Object> list = new ArrayList<Object>();
				// {{first.DATA}}
				// 充值金额：{{keyword1.DATA}}
				// 充值次数：{{keyword2.DATA}}
				// 剩余次数：{{keyword3.DATA}}
				// 充值时间：{{keyword4.DATA}}
				// {{remark.DATA}}

				// {{first.DATA}}
				// 用户名：{{keyword1.DATA}}
				// 变动时间：{{keyword2.DATA}}
				// 金额变动：{{keyword3.DATA}}
				// 可用余额：{{keyword4.DATA}}
				// 变动原因：{{keyword5.DATA}}
				// {{remark.DATA}}
				Card card = cardMapper.selectByPrimaryKey(member.getMcId());
				// first,keyword1,keyword2,keyword3,keyword4,remark
				list.add("储值卡充值");
				list.add(member.getNickname());
				list.add(DateTimeKit.getDateTime());
				list.add(money + "元");
				list.add(card.getMoney() + "元");
				list.add("储值卡充值");
				list.add("充值金额详情：请到会员卡交易记录查看");
				msgTemplateService.sendMsg(wxPublicUsers, params, list);
			}
		} catch (Exception e) {
			LOG.error("储值卡充值消息推送异常", e);
			return false;
		}
		return true;
	}

	@Override
	public boolean sendCikaCard(MemberEntity member, double money, int count) {
		try {
			SystemNotice systemNotice = systemNoticeMapper
					.findBybusIdAndCallType(member.getBusid(), (byte) 5);
			// 公众号消息推送
			if (CommonUtil.isNotEmpty(systemNotice)
					&& systemNotice.getPublicmsg() == 1) {
				WxPublicUsersEntity wxPublicUsers = wxPublicUsersMapper
						.selectByPrimaryKey(member.getPublicId());
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("id", systemNotice.getPublicidmsgid());
				params.put("memberId", member.getId());
				String url = PropertiesUtil.getWebHomeUrl()
						+ "/phoneMemberController/" + member.getBusid()
						+ "/79B4DE7C/findMember_1.do";
				params.put("url", url);
				List<Object> list = new ArrayList<Object>();
				// {{first.DATA}}
				// 充值金额：{{keyword1.DATA}}
				// 充值次数：{{keyword2.DATA}}
				// 剩余次数：{{keyword3.DATA}}
				// 充值时间：{{keyword4.DATA}}
				// {{remark.DATA}}

				Card card = cardMapper.selectByPrimaryKey(member.getMcId());

				// first,keyword1,keyword2,keyword3,keyword4,remark
				list.add("次卡充值提醒");
				list.add(money + "元");
				list.add(count + "次");
				list.add(card.getFrequency() + "次");
				list.add(DateTimeKit.getDateTime());
				list.add("充值详情：请到会员卡交易记录查看");
				msgTemplateService.sendMsg(wxPublicUsers, params, list);
			}
		} catch (Exception e) {
			LOG.error("次卡充值消息推送异常", e);
			return false;
		}
		return true;
	}

	@Override
	public boolean sendCikaXiaofei(MemberEntity member) {
		try {
			SystemNotice systemNotice = systemNoticeMapper
					.findBybusIdAndCallType(member.getBusid(), (byte) 8);
			// 公众号消息推送
			if (CommonUtil.isNotEmpty(systemNotice)
					&& systemNotice.getPublicmsg() == 1) {
				WxPublicUsersEntity wxPublicUsers = wxPublicUsersMapper
						.selectByPrimaryKey(member.getPublicId());
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("id", systemNotice.getPublicidmsgid());
				params.put("memberId", member.getId());
				String url = PropertiesUtil.getWebHomeUrl()
						+ "/phoneMemberController/" + member.getBusid()
						+ "/79B4DE7C/findMember_1.do";
				params.put("url", url);
				List<Object> list = new ArrayList<Object>();

				Card card = cardMapper.selectByPrimaryKey(member.getMcId());
				// {{first.DATA}}
				// 卡号：{{keyword1.DATA}}
				// 消费时间：{{keyword2.DATA}}
				// 剩余次数：{{keyword3.DATA}}
				// 有效期：{{keyword4.DATA}}
				// {{remark.DATA}}

				list.add("次卡消费提醒");
				list.add(card.getCardno());
				list.add(DateTimeKit.getDateTime());
				list.add(card.getFrequency() + "次");
				list.add(DateTimeKit.getDateTime());
				list.add("消费详情：请到会员卡交易记录查看");
				msgTemplateService.sendMsg(wxPublicUsers, params, list);
			}
		} catch (Exception e) {
			LOG.error("次卡消费消息推送异常", e);
			return false;
		}
		return true;
	}

	@Override
	public boolean sendChuzhiXiaofei(MemberEntity member, Double money) {
		try {
			SystemNotice systemNotice = systemNoticeMapper
					.findBybusIdAndCallType(member.getBusid(), (byte) 12);
			// 公众号消息推送
			if (CommonUtil.isNotEmpty(systemNotice)
					&& systemNotice.getPublicmsg() == 1) {
				WxPublicUsersEntity wxPublicUsers = wxPublicUsersMapper
						.selectByPrimaryKey(member.getPublicId());
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("id", systemNotice.getPublicidmsgid());
				params.put("memberId", member.getId());
				String url = PropertiesUtil.getWebHomeUrl()
						+ "/phoneMemberController/" + member.getBusid()
						+ "/79B4DE7C/findMember_1.do";
				params.put("url", url);
				List<Object> list = new ArrayList<Object>();
				Card card = cardMapper.selectByPrimaryKey(member.getMcId());
				// first,keyword1,keyword2,keyword3,keyword4,remark
				list.add("储值卡消费");
				list.add(member.getNickname());
				list.add(DateTimeKit.getDateTime());
				list.add(money + "元");
				list.add(card.getMoney() + "元");
				list.add("储值卡消费");
				list.add("消费详情：请到会员卡交易记录查看");
				msgTemplateService.sendMsg(wxPublicUsers, params, list);
			}
		} catch (Exception e) {
			LOG.error("储值卡消费消息推送异常", e);
			return false;
		}
		return true;
	}


	@Override
	public boolean sendChuzhiTuikuan(MemberEntity member, Double money) {
		try {
			SystemNotice systemNotice = systemNoticeMapper
					.findBybusIdAndCallType(member.getBusid(), (byte) 12);
			// 公众号消息推送
			if (CommonUtil.isNotEmpty(systemNotice)
					&& systemNotice.getPublicmsg() == 1) {
				WxPublicUsersEntity wxPublicUsers = wxPublicUsersMapper
						.selectByPrimaryKey(member.getPublicId());
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("id", systemNotice.getPublicidmsgid());
				params.put("memberId", member.getId());
				String url = PropertiesUtil.getWebHomeUrl()
						+ "/phoneMemberController/" + member.getBusid()
						+ "/79B4DE7C/findMember_1.do";
				params.put("url", url);
				List<Object> list = new ArrayList<Object>();
				Card card = cardMapper.selectByPrimaryKey(member.getMcId());
				// first,keyword1,keyword2,keyword3,keyword4,remark
				list.add("储值卡退款");
				list.add(member.getNickname());
				list.add(DateTimeKit.getDateTime());
				list.add(money + "元");
				list.add(card.getMoney() + "元");
				list.add("储值卡退款");
				list.add("退款详情：请到会员卡交易记录查看");
				msgTemplateService.sendMsg(wxPublicUsers, params, list);
			}
		} catch (Exception e) {
			LOG.error("储值卡充值消息推送异常", e);
			return false;
		}
		return true;
	}

*/
}
