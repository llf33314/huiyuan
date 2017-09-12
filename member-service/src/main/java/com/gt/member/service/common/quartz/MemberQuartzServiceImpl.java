package com.gt.member.service.common.quartz;/*package com.gt.controller.common.quartz;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.gt.dao.common.BusUserMapper;
import com.gt.dao.member.CardRecordMapper;
import com.gt.dao.member.MemberBirMapper;
import com.gt.dao.member.MemberMapper;
import com.gt.dao.member.MemberNoticeMapper;
import com.gt.dao.member.NoticeUserMapper;
import com.gt.dao.member.PublicParameterSetMapper;
import com.gt.dao.member.SystemNoticeCallMapper;
import com.gt.dao.member.SystemNoticeMapper;
import com.gt.dao.util.DaoUtil;
import com.gt.entity.common.BusUserEntity;
import com.gt.entity.member.MemberEntity;
import com.gt.entity.member.MemberBir;
import com.gt.entity.member.MemberNotice;
import com.gt.entity.member.PublicParameterSet;
import com.gt.entity.member.SystemNoticeCall;
import com.gt.controller.memberpay.MemberPayService;
import com.gt.util.CommonUtil;
import com.gt.util.HttpClienUtil;
import com.gt.util.PropertiesUtil;

@Service
@Component
public class MemberQuartzServiceImpl implements MemberQuartzService {

	private static final Logger logger = Logger
			.getLogger(MemberQuartzServiceImpl.class);

	@Autowired
	private DaoUtil daoUtil;

	@Autowired
	private BusUserMapper userMapper;

	@Autowired
	private MemberMapper memberMapper;

	@Autowired
	private MemberPayService memberPayService;

	@Autowired
	private MemberNoticeMapper memberNoticeMapper;

	@Autowired
	private BusUserMapper busUserMapper;

	@Autowired
	private NoticeUserMapper noticeUserMapper;

//	@Autowired
//	private SmsSpendingService smsSpendingService;

	@Autowired
	private PublicParameterSetMapper publicParameterSetMapper;

	@Autowired
	private CardRecordMapper cardRecordMapper;

	@Autowired
	private SystemNoticeCallMapper systemNoticeCallMapper;

	@Autowired
	private SystemNoticeMapper systemNoticeMapper;
	
	@Autowired
	private MemberBirMapper memberBirMapper;

	*//**
	 * 过期卡券 修改领取卡券状态
	 *//*
	@Scheduled(cron = "0 0 1 * * ?")
	// 每天早上1点扫描
	@Override
	public void updateCardGet() {
		try {
			String getDateTime = DateTimeKit.getDateTime();
			String sql = "UPDATE t_duofen_card_get  SET state=2 WHERE state=0 AND endTime<'"
					+ getDateTime + "'";

			daoUtil.update(sql);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("修改优惠券过期状态异常", e);
		}
	}

	*//**
	 * 修改会员消息信息 10分钟扫描一次
	 *//*
	@Scheduled(cron = "0 0/10 * * * ?")
	@Override
	public void updateNotice() {
		try {
			String sql="update t_member_notice set sendStuts=1 where sendStuts=0 and sendType=1 and sendDate<="+DateTimeKit.format(new Date());
			daoUtil.update(sql);
		} catch (Exception e) {
			logger.error("修改修改会员消息信息异常", e);
		}
	}

	*//**
	 * 每2小时访问一次
	 *//*
	@Scheduled(cron = "0 0 0/2 * * ?")
	@Override
	public void sendNotice() {
		try {
			System.out.println("启动定时任务" + new Date());
			List<Map<String, Object>> memberNotices = memberNoticeMapper
					.findAllNotSend(new Date());
			if (memberNotices == null || memberNotices.size() == 0) {
				return;
			}
			List<Integer> memberIds = null;
			StringBuffer sb = null;
			Map<String, Object> params = new HashMap<String, Object>();
			List<Integer> ctId = null;
			List<Map<String, Object>> members = null;
			for (Map<String, Object> map : memberNotices) {
				sb = new StringBuffer();
				// 修改方式状态
				Byte noticeMember = Byte.parseByte(map.get("noticeMember")
						.toString());
				Integer noticeId = CommonUtil.toInteger(map.get("id"));
				Integer busId = CommonUtil.toInteger(map.get("busId"));
				BusUserEntity busUser = busUserMapper.selectByPrimaryKey(busId);
				// 通知某个人群
				if (noticeMember == 1) {
					List<Map<String, Object>> noticeUsers = noticeUserMapper
							.findByNoticeId(noticeId);
					if (CommonUtil.isNotEmpty(noticeUsers)
							|| noticeUsers.size() == 0)
						continue;
					memberIds = new ArrayList<Integer>();
					for (Map<String, Object> noticeUser : noticeUsers) {
						Integer memberId = CommonUtil.toInteger(noticeUser
								.get("busId"));
						memberIds.add(memberId);
					}
					members = memberMapper.findByMemberIds(memberIds);
					if (CommonUtil.isEmpty(members) || members.size() <= 0)
						continue;

					for (Map<String, Object> member : members) {
						if (CommonUtil.isNotEmpty(member.get("phone"))) {
							sb.append(member.get("phone") + ",");
						}
					}
				}

				Byte sendType = Byte.parseByte(map.get("sendType").toString());
				if (sendType != 2) {
					if (sendType == 1) {
						if (CommonUtil.isNotEmpty(map.get("sendDate"))) {
							Date sendDate = DateTimeKit.parseDate(
									map.get("sendDate").toString(),
									"yyyy/MM/dd HH:mm:ss");
							// 未到发送时间
							if (DateTimeKit.laterThanNow(sendDate)) {
								continue;
							}
						}
					}
					String notice_user = map.get("notice_user").toString();
					// 0表示所有会员
					if ("0".equals(notice_user)) {
						members = memberMapper.findBybusIdAndNotMcId(busId);
						if (CommonUtil.isEmpty(members))
							continue;

						for (Map<String, Object> member : members) {
							if (CommonUtil.isNotEmpty(member.get("phone"))) {
								sb.append(member.get("phone") + ",");
							}
						}
					} else if (CommonUtil.isNotEmpty(notice_user)) {
						String[] str = notice_user.split(",");
						ctId = new ArrayList<Integer>();
						for (int i = 0; i < str.length; i++) {
							if (CommonUtil.isNotEmpty(str[i])) {
								ctId.add(CommonUtil.toInteger(str[i]));
							}
						}
						if (CommonUtil.isEmpty(ctId))
							continue;
						members = memberMapper.findBybusIdAndctId(busId, ctId);
						if (CommonUtil.isEmpty(members))
							continue;

						for (Map<String, Object> member : members) {
							if (CommonUtil.isNotEmpty(member.get("phone"))) {
								sb.append(member.get("phone") + ",");
							}
						}
					}
				}
				try {
					if (CommonUtil.isNotEmpty(sb.toString())) {
						String phone = sb.substring(0, sb.lastIndexOf(","));
						params.put("mobiles", phone);
						params.put("content", map.get("content"));
						params.put("busId", busUser.getId());
						params.put("model", 3);
						params.put("company", busUser.getMerchant_name());
						smsSpendingService.sendSms(params);
					}

					// 修改方式状态
					updateMemberNotice(noticeId, (byte) 1);
				} catch (Exception e) {
					logger.error("循环发送消息定时器异常", e);
				}
			}
		} catch (Exception e) {
			logger.error("发送消息定时器异常", e);
		}

	}

	*//**
	 * 修改消息状态
	 * 
	 * @param noticeId
	 * @param sendstuts
	 *//*
	public void updateMemberNotice(Integer noticeId, Byte sendstuts) {
		// 修改方式状态
		MemberNotice memberNotice = new MemberNotice();
		memberNotice.setId(noticeId);
		memberNotice.setSendstuts(sendstuts);
		memberNoticeMapper.updateByPrimaryKeySelective(memberNotice);
	}

	*//**
	 * 每月1号凌晨积分清零
	 *//*
	//@Scheduled(cron = "0 0 0 1 * ?")
	@Scheduled(cron = "0 5 10 * * ?")  //测试
	@Override
	public void clearJifen() {
		//
		List<PublicParameterSet> publicParameterSets = publicParameterSetMapper
				.findMonth();
		if (CommonUtil.isEmpty(publicParameterSets)
				|| publicParameterSets.size() == 0) {
			return;
		}
		Integer month = DateTimeKit.getMonth(new Date());

		StringBuffer busIds = new StringBuffer();
		for (PublicParameterSet p : publicParameterSets) {
			if (p.getMonth() != month) {
				continue;
			}
			// 执行积分清零
			busIds.append(p.getBusid() + ",");
		}
		if (CommonUtil.isEmpty(busIds.toString())) {
			return;
		}
		
		String url=PropertiesUtil.getWebHomeUrl()+"/memberERP/79B4DE7C/clearJifen.do";
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("busIds", busIds.toString());
		JSONObject json=JSONObject.fromObject(map);
		try {
			
			HttpClienUtil.httpPost(url, json, true);
		} catch (Exception e) {
			logger.error("积分清0异常了", e);
		}

	}

	*//**
	 * 每月22号0 点触发 系统消息提醒
	 *//*
	@Scheduled(cron = "0 0 0 22 * ?")
	@Override
	public void clearJifenSendMessage() {
		List<PublicParameterSet> publicParameterSets = publicParameterSetMapper
				.findMonth();
		if (CommonUtil.isEmpty(publicParameterSets)
				|| publicParameterSets.size() == 0) {
			return;
		}
		Integer month = DateTimeKit.getMonth(new Date());

		List<Integer> busIds = new ArrayList<Integer>();
		for (PublicParameterSet p : publicParameterSets) {
			if (p.getMonth() != month) {
				continue;
			}
			// 执行积分清零
			busIds.add(p.getBusid());
		}

		if (busIds.size() <= 0) {
			return;
		}

		List<Map<String, Object>> systemNotices = systemNoticeMapper
				.findBybusIdEq7(busIds);

		List<Integer> newBusIds = new ArrayList<Integer>();
		// 查询用户积分系统消息发送的商家
		for (Map<String, Object> map : systemNotices) {
			if ("1".equals(map.get("msgStatus").toString())) {
				newBusIds.add(CommonUtil.toInteger(map.get("busId")));
			}
		}
		if (newBusIds.size() == 0) {
			return;
		}

		Date d = DateTimeKit.parse(
				DateTimeKit.getMonthBegin(DateTimeKit.format(new Date())),
				"yyyy-MM-dd");

		Date date = DateTimeKit.addMonths(d, -12);
		Date startdate = DateTimeKit.addMonths(d, -24);

		List<Map<String, Object>> upperYear = cardRecordMapper.sumByBusId(
				busIds, startdate, date); // 前一年数据统计

		List<Map<String, Object>> currentYear = cardRecordMapper
				.sumCurrentByBusId(busIds, date, new Date());

		List<SystemNoticeCall> sncList = new ArrayList<SystemNoticeCall>();
		SystemNoticeCall s = null;
		for (Map<String, Object> cmap : upperYear) {
			boolean flag = false;
			Integer amounts = Double.valueOf(cmap.get("amounts").toString())
					.intValue();
			Integer busId = CommonUtil.toInteger(cmap.get("busId"));
			Integer cardId = CommonUtil.toInteger(cmap.get("cardId"));
			MemberEntity m = memberMapper.findByMcIdAndbusId(busId, cardId);

			if (CommonUtil.isNotEmpty(amounts) && amounts > 0) {
				if (currentYear.size() == 0) {
					s = new SystemNoticeCall();
					s.setDescribes("尊敬的用户：你去年还有" + amounts
							+ "积分未使用,请你在本月内使用完。不然，我们将会下个月1号将清除去年未使用的积分。");
					s.setMemberid(m.getId());
					s.setCreatedate(new Date());
					sncList.add(s);
				}

				for (Map<String, Object> map2 : currentYear) {
					Integer cardId1 = CommonUtil.toInteger(map2.get("cardId"));
					Integer amounts1 = -Double.valueOf(
							map2.get("amounts").toString()).intValue();
					if (cardId.equals(cardId1)) {
						flag = true;
						if (amounts > amounts1) {
							Integer amount = amounts - amounts1;
							s = new SystemNoticeCall();
							s.setDescribes("尊敬的用户：你去年还有"
									+ amount
									+ "积分未使用,请你在本月内使用完。不然，我们将会下个月1号将清除去年未使用的积分。");
							s.setMemberid(m.getId());
							s.setCreatedate(new Date());
							sncList.add(s);
						}
					}
				}

				if (!flag) {
					s = new SystemNoticeCall();
					s.setDescribes("尊敬的用户：你去年还有" + amounts
							+ "积分未使用,请你在本月内使用完。不然，我们将会下个月1号将清除去年未使用的积分。");
					s.setMemberid(m.getId());
					s.setCreatedate(new Date());
					sncList.add(s);
				}
			}
			if (sncList.size() > 0) {
				try {
					systemNoticeCallMapper.insertSelective(sncList.get(0));
					// 保存数据
					systemNoticeCallMapper.saveList(sncList);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	*//**
	 * 每月24号下午3点触发 短信提醒
	 *//*
//	@Scheduled(cron = "0 0 15 24 * ?")
	@Scheduled(cron = "0 2 10 * * ?")  //测试
	@Override
	public void clearJifenSendSmsMessage() {
		List<PublicParameterSet> publicParameterSets = publicParameterSetMapper
				.findMonth();
		if (CommonUtil.isEmpty(publicParameterSets)
				|| publicParameterSets.size() == 0) {
			return;
		}
		Integer month = DateTimeKit.getMonth(new Date());

		List<Integer> busIds = new ArrayList<Integer>();
		for (PublicParameterSet p : publicParameterSets) {
			if (p.getMonth() != month) {
				continue;
			}
			// 执行积分清零
			busIds.add(p.getBusid());
		}

		if (busIds.size() <= 0) {
			return;
		}

		// 短信通知积分
		List<Map<String, Object>> systemNotices = systemNoticeMapper
				.findBybusIdEq7(busIds);

		List<Integer> newBusIds = new ArrayList<Integer>();

		StringBuffer sb = new StringBuffer(
				"select id,merchant_name from bus_user where id in (");

		// 查询用户积分短信消息发送的商家
		for (Map<String, Object> map : systemNotices) {
			if ("1".equals(map.get("smsStatus").toString())) {
				newBusIds.add(CommonUtil.toInteger(map.get("busId")));
				sb.append(map.get("busId") + ",");
			}
		}
		if (newBusIds.size() == 0) {
			return;
		}
		sb.append("-1)");

		List<Map<String, Object>> busList = daoUtil.queryForList(sb.toString());

		Date d = DateTimeKit.parse(
				DateTimeKit.getMonthBegin(DateTimeKit.format(new Date())),
				"yyyy-MM-dd");

		Date date = DateTimeKit.addMonths(d, -12);
		Date startdate = DateTimeKit.addMonths(d, -24);

		List<Map<String, Object>> upperYear = cardRecordMapper.sumByBusId(
				busIds, startdate, date); // 前一年数据统计

		List<Map<String, Object>> currentYear = cardRecordMapper
				.sumCurrentByBusId(busIds, date, new Date());
		try {
			for (Map<String, Object> cmap : upperYear) {
				Integer amounts = Double
						.valueOf(cmap.get("amounts").toString()).intValue();
				Integer busId = CommonUtil.toInteger(cmap.get("busId"));
				Integer cardId = CommonUtil.toInteger(cmap.get("cardId"));
				MemberEntity m = memberMapper.findByMcIdAndbusId(busId, cardId);
				boolean flag = false;
				if (CommonUtil.isNotEmpty(amounts) && amounts > 0) {

					if (currentYear.size() == 0) {
						String content = "尊敬的用户：你去年还有" + amounts
								+ "积分未使用,请你在本月内使用完。不然，我们将会下个月1号将清除去年未使用的积分。";
						Map<String, Object> params = new HashMap<String, Object>();
						params.put("busId", busId);
						params.put("model", 3);
						params.put("mobiles", m.getPhone());
						params.put("content", content);
						for (Map<String, Object> busUser : busList) {
							if (busId == CommonUtil
									.toInteger(busUser.get("id"))) {
								params.put("company",
										busUser.get("merchant_name"));
							}
						}
						smsSpendingService.sendSms(params);
					}

					for (Map<String, Object> map2 : currentYear) {
						Integer cardId1 = CommonUtil.toInteger(map2
								.get("cardId"));
						Integer amounts1 = -Double.valueOf(
								map2.get("amounts").toString()).intValue();
						if (cardId.equals(cardId1)) {
							flag = true;
							if (amounts > amounts1) {
								Integer amount = amounts - amounts1;
								String content = "尊敬的用户：你去年还有"
										+ amount
										+ "积分未使用,请你在本月内使用完。不然，我们将会下个月1号将清除去年未使用的积分。";

								Map<String, Object> params = new HashMap<String, Object>();
								params.put("busId", busId);
								params.put("model", 3);
								params.put("mobiles", m.getPhone());
								params.put("content", content);
								for (Map<String, Object> busUser : busList) {
									if (busId == CommonUtil.toInteger(busUser
											.get("id"))) {
										params.put("company",
												busUser.get("merchant_name"));
									}
								}
								smsSpendingService.sendSms(params);
							}
						}
					}

					if (!flag) {
						String content = "尊敬的用户：你去年还有" + amounts
								+ "积分未使用,请你在本月内使用完。不然，我们将会下个月1号将清除去年未使用的积分。";
						Map<String, Object> params = new HashMap<String, Object>();
						params.put("busId", busId);
						params.put("model", 3);
						params.put("mobiles", m.getPhone());
						params.put("content", content);
						for (Map<String, Object> busUser : busList) {
							if (busId == CommonUtil
									.toInteger(busUser.get("id"))) {
								params.put("company",
										busUser.get("merchant_name"));
							}
						}
						smsSpendingService.sendSms(params);
					}
				}
			}
		} catch (Exception e) {
			logger.error("积分清零发送短信异常", e);
		}
	}

	*//**
	 * 每天凌晨 生日推送
	 *//*
	@Scheduled(cron = "0 0 3 * * ?")
	@Override
	public void birthdayMsg() {
		String sql="SELECT  id,nickname,birth,busId,phone FROM t_wx_bus_member  WHERE DATE_FORMAT(birth,'%m%d') = DATE_FORMAT(NOW(),'%m%d')";
		List<Map<String, Object>> memberList=daoUtil.queryForList(sql);
		//
		StringBuffer sbSql=new StringBuffer("select busId,msgStatus,msgContent from system_notice where callType=10 and busId in (");
		String busIds="";
		for (Map<String, Object> map : memberList) {
			if(CommonUtil.isNotEmpty(map.get("busId")) && !busIds.contains(CommonUtil.toString(map.get("busId")))){
				sbSql.append(map.get("busId")+",");
				busIds+=map.get("busId")+",";
			}
		}
		sbSql.append("-1)");
		List<Map<String, Object>> sysNoticeList=daoUtil.queryForList(sbSql.toString());
		
		List<SystemNoticeCall> sncs=new ArrayList<SystemNoticeCall>();
		SystemNoticeCall s=null;
		for (Map<String, Object> sn : sysNoticeList) {
			if(CommonUtil.isEmpty(sn.get("msgContent")) || CommonUtil.toInteger(sn.get("msgStatus"))==0){
				continue;
			}
			for (Map<String, Object> map : memberList) {
				if(CommonUtil.toString(sn.get("busId")).equals(CommonUtil.toString(map.get("busId")))){
					s=new SystemNoticeCall();
					s.setMemberid(CommonUtil.toInteger(map.get("id")));
					s.setDescribes(CommonUtil.toString(sn.get("msgContent")));
					s.setCreatedate(new Date());
					sncs.add(s);
				}
			}
		}
		
		if(sncs.size()>0){
			systemNoticeCallMapper.saveList(sncs);
		}
	}

	*//**
	 * 每天凌晨3点触发  过滤数据
	 *//*
	@Scheduled(cron = "0 0 3 * * ?")
	@Override
	public void birthdaySms() {
		String sql="SELECT  id,nickname,birth,busId,phone FROM t_wx_bus_member WHERE DATE_FORMAT(birth,'%m%d') = DATE_FORMAT(NOW(),'%m%d') ";
		
		List<Map<String, Object>> memberList=daoUtil.queryForList(sql);
		
		List<MemberBir> list=new ArrayList<>();
		for (Map<String, Object> map : memberList) {
			if(CommonUtil.isNotEmpty(map.get("phone"))){
				MemberBir mb=new MemberBir();
				mb.setPhone(CommonUtil.toString(map.get("phone")));
				mb.setBusid(CommonUtil.toInteger(map.get("busId")));
				list.add(mb);
			}
		}
		if(list.size()>0){
			memberBirMapper.insertList(list);
		}
	}
	
	*//**
	 * 每天凌晨9点触发 短信提醒
	 *//*
	@Scheduled(cron = "0 0 9 * * ?")
	public void sendBir(){
	String sql="SELECT  busId,phone FROM t_member_bir ";
		
	List<Map<String, Object>> memberList=daoUtil.queryForList(sql);
		
	StringBuffer sbSql=new StringBuffer("select busId,smsStatus,smsContent from system_notice where callType=10 and busId in (");
		

		StringBuffer sb = new StringBuffer(
				"select id,merchant_name from bus_user where id in (");
		
		String busIds="";
		
		for (Map<String, Object> map : memberList) {
			if(CommonUtil.isNotEmpty(map.get("busId")) && !busIds.contains(CommonUtil.toString(map.get("busId")))){
				sbSql.append(map.get("busId")+",");
				sb.append(map.get("busId")+",");
				busIds+=map.get("busId")+",";
			}
		}
		sbSql.append("-1)");
		sb.append("-1)");
		List<Map<String, Object>> sysNoticeList=daoUtil.queryForList(sbSql.toString());
		String phone="";
		
		List<Map<String, Object>> busList = daoUtil.queryForList(sb.toString());
		
		
		for (Map<String, Object> sn : sysNoticeList) {
			if(CommonUtil.isEmpty(sn.get("smsContent")) || CommonUtil.toInteger(sn.get("smsStatus"))==0){
				continue;
			}
			for (Map<String, Object> map : memberList) {
				if(CommonUtil.toString(sn.get("busId")).equals(CommonUtil.toString(map.get("busId"))) && CommonUtil.isNotEmpty(map.get("phone"))){
					phone+=map.get("phone")+",";
				}
			}
			if(CommonUtil.isEmpty(phone)){
				continue;
			}
			phone=phone.substring(0, phone.lastIndexOf(","));
			
			//发送短信
			String content = CommonUtil.toString(sn.get("smsContent"));
					
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("busId", sn.get("busId"));
			params.put("model", 3);
			params.put("mobiles",phone);
			params.put("content", content);
			
			for (Map<String, Object> busUser : busList) {
				if (CommonUtil.toInteger(sn.get("busId")) == CommonUtil.toInteger(busUser
						.get("id"))) {
					params.put("company",
							busUser.get("merchant_name"));
				}
			}
			try {
				smsSpendingService.sendSms(params);
			}
			catch (Exception e) {
				logger.error("用户发送短信异常", e);
			}
		}
		
		
		String deleteSql="delete  FROM t_member_bir ";
		daoUtil.execute(deleteSql);
	}

	*//**
	 * 每天凌晨2点
	 *//*
	@Scheduled(cron = "0 0 2 * * ?")
	@Override
	public void tongji() {
		try {
			//统计一天数据 统计：商家+门店+行业+会员（粉丝）+支付方式  t_tongji_paytype
			String dateTime = DateTimeKit.format(
					DateTimeKit.addDate(new Date(), -1), "yyyy-MM-dd")
					+ " 00:00:00";
			String enddateTime = DateTimeKit.format(
					DateTimeKit.addDate(new Date(), -1), "yyyy-MM-dd")
					+ " 24:00:00";
			String sql = "SELECT busUserId,storeId,moduleType,ctId,paymentType,SUM(totalMoney) as totalMoney,SUM(discountMoney) as discountMoney ,"
					+ " SUM(integral) as integral,SUM(fenbi) as fenbi,SUM(uccount) as uccount,SUM(freightMoney) as freightMoney  "
					+ "FROM t_wx_user_consume WHERE ischongzhi=0 AND isend=1 and ctId>0 and busUserId!=''  and storeId!='' AND isendDate>='"
					+ dateTime
					+ "' and isendDate <='"
					+ enddateTime
					+ "'"
					+ "GROUP BY busUserId,storeId,moduleType, ctId,paymentType";
			List<Map<String, Object>> payTypeLog = daoUtil.queryForList(sql);
			if (CommonUtil.isEmpty(payTypeLog) || payTypeLog.size() == 0) {
				return;
			}
			//		INSERT INTO users(name, age) VALUES('姚明', 25), ('比尔.盖茨', 50), ('火星人', 600);
			StringBuffer saveSql = new StringBuffer(
					"insert into t_tongji_paytype(busId,shopId,moduleType,ctId,isMember,paymentType,"
							+ "totalMoney,discountMoney,integral,fenbi,uccount,freightMoney) values");
			int i = 0;
			for (Map<String, Object> map : payTypeLog) {
				saveSql.append("(");
				saveSql.append(map.get("busUserId") + ",");
				saveSql.append(map.get("storeId") + ",");
				saveSql.append(map.get("moduleType") + ",");
				if (CommonUtil.isNotEmpty(map.get("ctId"))
						&& "0".equals(CommonUtil.toString(map.get("ctId")))) {
					saveSql.append(map.get("ctId") + ",");
					saveSql.append("1,");
				} else {
					saveSql.append("0,");
					saveSql.append("0,");
				}
				saveSql.append(map.get("paymentType") + ",");
				saveSql.append(map.get("totalMoney") + ",");

				saveSql.append(map.get("discountMoney") + ",");
				saveSql.append(map.get("integral") + ",");
				saveSql.append(map.get("fenbi") + ",");
				saveSql.append(map.get("uccount") + ",");
				saveSql.append(map.get("freightMoney"));

				i++;
				if (i == payTypeLog.size()) {
					saveSql.append(")");
				} else {
					saveSql.append("),");
				}
			}
			//统计：商家+门店+行业+会员（粉丝）+支付方式
			daoUtil.execute(saveSql.toString());
			//统计：商家+门店+行业+粉丝（会员）
			StringBuffer sb = new StringBuffer(
					" SELECT  busId,shopId,moduleType,ctId,isMember,"
							+ "SUM(totalMoney) as totalMoney ,SUM(discountMoney) as discountMoney,"
							+ "SUM(integral) as integral,SUM(fenbi) as fenbi,SUM(uccount) as uccount,SUM(freightMoney) as freightMoney"
							+ " FROM t_tongji_paytype where createDate>='"
							+ enddateTime
							+ "' GROUP BY busId,shopId,moduleType, ctId");
			List<Map<String, Object>> memberList = daoUtil.queryForList(sb
					.toString());
			if (CommonUtil.isEmpty(memberList) || memberList.size() == 0) {
				return;
			}
			StringBuffer savememberSql = new StringBuffer(
					"insert into t_tongji_member(busId,shopId,moduleType,ctId,isMember,"
							+ "totalMoney,discountMoney,integral,fenbi,uccount,freightMoney) values");
			i = 0;
			for (Map<String, Object> map2 : memberList) {
				savememberSql.append("(");
				savememberSql.append(map2.get("busId") + ",");
				savememberSql.append(map2.get("shopId") + ",");
				savememberSql.append(map2.get("moduleType") + ",");
				savememberSql.append(map2.get("ctId") + ",");
				savememberSql.append(map2.get("isMember") + ",");
				savememberSql.append(map2.get("totalMoney") + ",");
				savememberSql.append(map2.get("discountMoney") + ",");
				savememberSql.append(map2.get("integral") + ",");
				savememberSql.append(map2.get("fenbi") + ",");
				savememberSql.append(map2.get("uccount") + ",");
				savememberSql.append(map2.get("freightMoney"));

				i++;
				if (i == memberList.size()) {
					savememberSql.append(")");
				} else {
					savememberSql.append("),");
				}
			}
			daoUtil.execute(savememberSql.toString());
			//统计：商家+门店+行业
			StringBuffer moduleTypesb = new StringBuffer(
					" SELECT  busId,shopId,moduleType,"
							+ "SUM(totalMoney) as totalMoney ,SUM(discountMoney) as discountMoney,"
							+ "SUM(integral) as integral,SUM(fenbi) as fenbi,SUM(uccount) as uccount,SUM(freightMoney) as freightMoney "
							+ " FROM t_tongji_member where createDate>='"
							+ enddateTime
							+ "' GROUP BY busId,shopId,moduleType");
			List<Map<String, Object>> moduleTypeList = daoUtil
					.queryForList(moduleTypesb.toString());
			if (CommonUtil.isEmpty(moduleTypeList)
					|| moduleTypeList.size() == 0) {
				return;
			}
			StringBuffer savemoduleTypeSql = new StringBuffer(
					"insert into t_tongji_moduleType(busId,shopId,moduleType,"
							+ "totalMoney,discountMoney,integral,fenbi,uccount,freightMoney) values");
			i = 0;
			for (Map<String, Object> map2 : moduleTypeList) {
				savemoduleTypeSql.append("(");
				savemoduleTypeSql.append(map2.get("busId") + ",");
				savemoduleTypeSql.append(map2.get("shopId") + ",");
				savemoduleTypeSql.append(map2.get("moduleType") + ",");

				savemoduleTypeSql.append(map2.get("totalMoney") + ",");
				savemoduleTypeSql.append(map2.get("discountMoney") + ",");
				savemoduleTypeSql.append(map2.get("integral") + ",");
				savemoduleTypeSql.append(map2.get("fenbi") + ",");
				savemoduleTypeSql.append(map2.get("uccount") + ",");
				savemoduleTypeSql.append(map2.get("freightMoney"));
				i++;
				if (i == moduleTypeList.size()) {
					savemoduleTypeSql.append(")");
				} else {
					savemoduleTypeSql.append("),");
				}
			}
			daoUtil.execute(savemoduleTypeSql.toString());
			//统计：商家+门店
			StringBuffer shopsb = new StringBuffer(
					" SELECT  busId,shopId,"
							+ "SUM(totalMoney) as totalMoney ,SUM(discountMoney) as discountMoney,"
							+ "SUM(integral) as integral,SUM(fenbi) as fenbi,SUM(uccount) as uccount,SUM(freightMoney) as freightMoney"
							+ " FROM t_tongji_moduleType where createDate>='"
							+ enddateTime + "' GROUP BY busId,shopId");
			List<Map<String, Object>> shopList = daoUtil.queryForList(shopsb
					.toString());
			if (CommonUtil.isEmpty(shopList) || shopList.size() == 0) {
				return;
			}
			StringBuffer saveshopSql = new StringBuffer(
					"insert into t_tongji_shop(busId,shopId,"
							+ "totalMoney,discountMoney,integral,fenbi,uccount,freightMoney) values");
			i = 0;
			for (Map<String, Object> map2 : shopList) {
				saveshopSql.append("(");
				saveshopSql.append(map2.get("busId") + ",");
				saveshopSql.append(map2.get("shopId") + ",");

				saveshopSql.append(map2.get("totalMoney") + ",");
				saveshopSql.append(map2.get("discountMoney") + ",");
				saveshopSql.append(map2.get("integral") + ",");
				saveshopSql.append(map2.get("fenbi") + ",");
				saveshopSql.append(map2.get("uccount") + ",");
				saveshopSql.append(map2.get("freightMoney"));
				i++;
				if (i == shopList.size()) {
					saveshopSql.append(")");
				} else {
					saveshopSql.append("),");
				}
			}
			daoUtil.execute(saveshopSql.toString());
		} catch (Exception e) {
			logger.error("消费数据统计异常了时间："+new Date(),e);
		}
	}
	
	public static void main(String[] args) {
		Map<String, Object> map=new HashMap<>();
		map.put("busIds", "42,");
		JSONObject json=JSONObject.fromObject(map);
		System.out.println(json);
	}
}
*/