package com.gt.member.service.old.member.impl;/*package com.gt.service.member.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gt.controller.personCenter.phone.PhonePaySuccessController;
import com.gt.dao.member.CardRecordMapper;
import com.gt.dao.member.MemberMapper;
import com.gt.dao.member.card.DuofenCardReceiveMapper;
import com.gt.dao.personCenter.PaySuccessCardMapper;
import com.gt.dao.personCenter.PaySuccessLogMapper;
import com.gt.dao.personCenter.PaySuccessMapper;
import com.gt.dao.user.BusUserMapper;
import com.gt.dao.user.FenbiFlowRecordMapper;
import com.gt.dao.user.WxPublicUsersMapper;
import com.gt.entity.member.Member;
import com.gt.entity.member.card.DuofenCardReceive;
import com.gt.entity.personCenter.PaySuccess;
import com.gt.entity.personCenter.PaySuccessLog;
import com.gt.entity.user.BusUser;
import com.gt.entity.user.WxPublicUsers;
import com.gt.service.common.sms.SmsSpendingService;
import com.gt.service.member.DuofenCardService;
import com.gt.service.member.PaysuccessService;
import com.gt.service.member.SystemMsgService;
import com.gt.service.memberpay.MemberPayService;
import com.gt.util.CommonUtil;
import com.gt.util.DateTimeKit;
import com.gt.util.JedisUtil;
import com.gt.util.JsonUtil;
import com.gt.util.PropertiesUtil;

@Service
public class PaysuccessServiceImpl implements PaysuccessService {

	private static final Logger LOG = Logger
			.getLogger(PhonePaySuccessController.class);

	@Autowired
	private PaySuccessMapper paySuccessMapper;

	@Autowired
	private PaySuccessCardMapper paySuccessCardMapper;

	@Autowired
	private MemberMapper memberMapper;

	@Autowired
	private WxPublicUsersMapper wxPublicUsersMapper;

	@Autowired
	private SmsSpendingService smsSpendingService;

	@Autowired
	private BusUserMapper busUserMapper;

	@Autowired
	private FenbiFlowRecordMapper fenbiFlowRecordMapper;

	@Autowired
	private CardRecordMapper cardRecordMapper;

	@Autowired
	private MemberPayService memberPayService;

	@Autowired
	private PaySuccessLogMapper paySuccessLogMapper;

	@Autowired
	private SystemMsgService systemMsgService;

	@Autowired
	private DuofenCardService duofenCardService;

	@Autowired
	private DuofenCardReceiveMapper duofenCardReceiveMapper;

	@Override
	public String paySuccess(HttpServletRequest request, String str) {
		try {
			Map<String, Object> map = JsonUtil.json2Map(str);
			int isGive = 1;
			if (CommonUtil.isNotEmpty(map.get("isGive"))) {
				isGive = CommonUtil.toInteger(map.get("isGive"));

			}
			// 查询订单是否已领取
			String orderNo = CommonUtil.toString(map.get("orderNo"));
			if (!JedisUtil.exists(orderNo)) {
				return "redirect:" + map.get("returnUrl");

			}
			JedisUtil.del(orderNo); // 清除内存数据

			Integer memberId = CommonUtil.toInteger(map.get("memberId"));

			Member member = memberMapper.selectByPrimaryKey(memberId);

			PaySuccess paysuccess = paySuccessMapper.findBybusIdAndModel(
					member.getBusid(),
					CommonUtil.toInteger(map.get("model")));
			if (CommonUtil.isEmpty(paysuccess)) {
				return "redirect:" + map.get("returnUrl");
			}
			
			// 查询该订单号赠送信息
			PaySuccessLog psl = paySuccessLogMapper.findByOrderNo(memberId,
					orderNo);

			if (CommonUtil.isEmpty(psl) || 1 == psl.getOneget()) {
				return "redirect:" + map.get("returnUrl");
			}
			
			
			Map<String, Object> isFirstMap = isFirstOrder(memberId,
					member.getBusid(), CommonUtil.toInteger(map.get("model")),
					orderNo);
			
			String  center="";
			if ((boolean) isFirstMap.get("result") == true) {
				// 首单
				// 自动领取优惠券
				Integer receiveId=CommonUtil.toInteger(isFirstMap.get("receiveId"));
				Integer num=CommonUtil.toInteger(isFirstMap.get("num"));

					DuofenCardReceive duofenCardReceive = duofenCardReceiveMapper
							.selectByPrimaryKey(receiveId);
					
					if (CommonUtil.isNotEmpty(duofenCardReceive)) {
						center = "首单赠送"+num+"个"+duofenCardReceive.getCardsname()+"优惠券包,卡包已存入会员卡。";
						request.setAttribute("center", center);
					}
					
					for (int i = 0; i < num; i++) {
						duofenCardService.oneKeyToGet(receiveId,
								memberId, 2);
					}
					request.setAttribute("rId", receiveId);
					request.setAttribute("num",num);
					request.setAttribute("isOpen", 1);
				
			} else {
				PaySuccessLog paySuccessLog = paySuccessLogMapper
						.findByModelAndMemberId(
								CommonUtil.toInteger(map.get("model")),
								memberId);
				// 优惠券手动领取
				if (paysuccess.getReceivetype() == 1
						&& paysuccess.getDuofencardopen() == 1) {
					JedisUtil.set(orderNo, orderNo);
				}

				request.setAttribute("paysuccess", paysuccess);
				if (paysuccess.getOptiontype() == 2) {
					// 微信优惠券
					List<Map<String, Object>> listMap = paySuccessCardMapper
							.findBySuccessId(paysuccess.getId());
					if (listMap.size() > 0) {
						Map<String, Object> cardMap = null;
						for (int i = 0; i < listMap.size(); i++) {
							cardMap = listMap.get(i);
							if (paysuccess.getCounttype() == 2) {
								Double orderMoney = CommonUtil.toDouble(map
										.get("orderMoney"));
								if (i < listMap.size() - 1) {
									if (CommonUtil.toDouble(cardMap
											.get("payMoney")) <= orderMoney
											&& orderMoney < CommonUtil
													.toDouble(listMap
															.get(i + 1).get(
																	"payMoney"))) {
										cardMap = listMap.get(i);
									}
								} else {
									cardMap = listMap.get(i);
								}
							} else {
								if (CommonUtil.isNotEmpty(paySuccessLog)) {
									// 判断赠送到哪个等级
									Integer id = CommonUtil.toInteger(cardMap
											.get("id"));
									if (i < (listMap.size() - 1)
											&& paySuccessLog.getSuccessid() >= id) {
										cardMap = null;
										continue;
									}
								}
							}

							Double payMoney1 = CommonUtil.toDouble(cardMap
									.get("payMoney"));
							if (i < listMap.size() - 1) {
								Map<String, Object> map2 = listMap.get(i + 1);
								Double payMoney2 = CommonUtil.toDouble(map2
										.get("payMoney"));
								if (CommonUtil.toDouble(map.get("orderMoneys")) >= payMoney1
										&& CommonUtil.toDouble(map
												.get("orderMoneys")) < payMoney2) {
									request.setAttribute("cardMap", cardMap);
									break;
								}
							} else {
								if (CommonUtil.toDouble(map.get("orderMoneys")) >= payMoney1) {
									request.setAttribute("cardMap", cardMap);
									break;
								}
							}
							cardMap = null;
						}

						if (CommonUtil.isEmpty(cardMap)) {
							return "redirect:" + map.get("returnUrl");
						}

						// 修改记录保存赠送的id
						paySuccessLogMapper.updateByModelAndMemberId(
								CommonUtil.toInteger(map.get("model")),
								CommonUtil.toInteger(map.get("memberId")),
								CommonUtil.toInteger(cardMap.get("id")));

						// 赠送粉丝积分和粉币
						Member m = new Member();
						m.setId(member.getId());
						if (paysuccess.getJifenopen() == 1) {
							if (isGive == 1) {
								m.setIntegral(member.getIntegral()
										+ CommonUtil.toDouble(
												cardMap.get("jifenCount"))
												.intValue());
								// 操作记录
								if (CommonUtil.isNotEmpty(member.getMcId())) {
									memberPayService
											.saveCardRecordNew(
													member.getMcId(),
													(byte) 2,
													cardMap.get("jifenCount")
															+ "积分",
													"支付有礼积分赠送",
													member.getBusid(),
													null,
													0,
													CommonUtil.toInteger(cardMap
															.get("jifenCount")));
								}
							}
							psl.setJifen(CommonUtil.toDouble(
									cardMap.get("jifenCount")).intValue());
						}

						if (paysuccess.getFenbiopen() == 1) {
							int fenbiSurplus = fenbiFlowRecordMapper
									.getFenbiSurplus(member.getBusid(), 1, 32,
											1);
							if (isGive == 1) {
								if (fenbiSurplus > CommonUtil.toDouble(cardMap
										.get("fenbiCount"))) {
									m.setFansCurrency(member.getFansCurrency()
											+ CommonUtil.toDouble(
													cardMap.get("fenbiCount"))
													.intValue());
									// 操作记录
									if (CommonUtil.isNotEmpty(member.getMcId())) {
										memberPayService.saveCardRecordNew(
												member.getMcId(), (byte) 3,
												cardMap.get("fenbiCount")
														+ "粉币", "支付有礼粉币赠送",
												member.getBusid(), null,
												null,
												CommonUtil.toInteger(cardMap
														.get("fenbiCount")));
									}
									// 扣除粉币数量
									fenbiFlowRecordMapper.updateFenbiReduce(
											member.getBusid(),
											CommonUtil.toDouble(
													cardMap.get("fenbiCount"))
													.intValue(), 1, 32);
								}
								psl.setFenbi(CommonUtil.toDouble(
										cardMap.get("fenbiCount")).intValue());
							}
						}
						if (isGive == 1
								&& (paysuccess.getJifenopen() == 1 || paysuccess
										.getFenbiopen() == 1)) {
							memberMapper.updateByPrimaryKeySelective(m);
						}

						// 自动领取优惠券
						if (paysuccess.getReceivetype() == 0
								&& paysuccess.getDuofencardopen() == 1) {
							int num = CommonUtil.toInteger(cardMap.get("num"));
							if (num > 0) {
								for (int i = 0; i < num; i++) {
									duofenCardService.oneKeyToGet(CommonUtil
											.toInteger(cardMap.get("rId")),
											memberId, 2);
								}
							}

							DuofenCardReceive duofenCardReceive = duofenCardReceiveMapper
									.selectByPrimaryKey(CommonUtil
											.toInteger(cardMap.get("rId")));
							Integer countType = CommonUtil.toInteger(paysuccess
									.getCounttype());
							if (CommonUtil.isNotEmpty(duofenCardReceive)) {
								if (countType == 0) {
									center = "每月消费满" + cardMap.get("payMoney")
											+ "元，送" + cardMap.get("num") + "个"
											+ duofenCardReceive.getCardsname()
											+ "，已存入你的会员卡中。";
								} else if (countType == 1) {
									center = "每月消费满"
											+ CommonUtil.toDouble(
													cardMap.get("payMoney"))
													.intValue() + "次，送"
											+ cardMap.get("num") + "个"
											+ duofenCardReceive.getCardsname()
											+ "，已存入你的会员卡中。";
								} else {
									center = "单次消费满" + cardMap.get("payMoney")
											+ "元，送" + cardMap.get("num") + "个"
											+ duofenCardReceive.getCardsname()
											+ "，已存入你的会员卡中。";
								}
								request.setAttribute("center", center);
							}
							request.setAttribute("rId", cardMap.get("rId"));
							request.setAttribute("num", cardMap.get("num"));
						}
					}
				}
			}

			BusUser busUser = busUserMapper.selectByPrimaryKey(member
					.getBusid());
			// 短信通知商家
			if (CommonUtil.isEmpty(paysuccess.getPhone())
					&& CommonUtil.toInteger(map.get("payType")) == 2
					&& CommonUtil.toDouble(map.get("orderMoney")) >= 5
					&& member.getIssubscribe() == 0
					&& CommonUtil.isEmpty(member.getMcId())) {

				String realname = null;

				WxPublicUsers wxPublicUsers = wxPublicUsersMapper
						.selectByUserId(member.getBusid());

				if (CommonUtil.isEmpty(wxPublicUsers)) {
					realname = busUser.getMerchant_name();
				} else {
					realname = wxPublicUsers.getAuthorizerInfo();
				}

				String content = "尊敬的" + realname + "：" + DateTimeKit.getDate()
						+ "有粉丝通过商城成功关注您的公众号。";

				Map<String, Object> params = new HashMap<String, Object>();
				params.put("busId", member.getBusid());
				params.put("model", 11);
				params.put("mobiles", paysuccess.getPhone());
				params.put("company", realname);
				params.put("content", content);
				try {
					smsSpendingService.sendSms(params);
				} catch (Exception e) {
					LOG.error("短信发送失败", e);
				}
			}
			psl.setOneget((byte) 1);
			paySuccessLogMapper.updateByPrimaryKey(psl);
			request.setAttribute("advert", busUser.getAdvert());
			request.setAttribute("publicId", map.get("publicId"));
			request.setAttribute("urlDetail", map.get("urlDetail"));
			request.setAttribute("orderMoney", map.get("orderMoney"));
			request.setAttribute("payType", map.get("payType"));
			request.setAttribute("continueUrl", map.get("continueUrl"));
			request.setAttribute("returnUrl", map.get("returnUrl"));
			request.setAttribute("orderNo", map.get("orderNo"));
			request.setAttribute("path", PropertiesUtil.getResourceUrl());
			request.setAttribute("memberId", memberId);
			request.setAttribute("busId", member.getBusid());
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("支付有礼跳转异常", e);
		}
		return "merchants/paysuccess/phone/paysuccess";
	}

	*//**
	 * 判断是否是首单
	 * 
	 * @param memberId
	 * @param orderNo
	 * @return
	 *//*
	public Map<String, Object> isFirstOrder(Integer memberId, Integer busId,
			Integer model, String orderNo) {
		Map<String, Object> map = new HashMap<>();
		PaySuccess paySuccess = paySuccessMapper.findBybusIdAndModel(busId,
				model);
		
		if (CommonUtil.isEmpty(paySuccess)) {
			map.put("result", false);
			return map;
		}
		if (0==paySuccess.getFirstgive() || "0".equals(paySuccess.getFirstgive())) {
			map.put("result", false);
			return map;
		}
		// 0首次 1每周 2每月 3时间段
		byte firstType = paySuccess.getFirsttype();
		String cardIds = paySuccess.getCardids();
		JSONArray json = JSONArray.fromObject(cardIds);
		switch (firstType) {
		case 0:
			// 首次
			Integer count = paySuccessLogMapper.countByModelAndMemberId(model,
					memberId);
			if (count > 1) {
				map.put("result", false);
				return map;
			}
			for (int i = 0; i < json.size(); i++) {
				JSONObject obj = json.getJSONObject(i);
				// 获取cardId 和num
				map.put("result", true);
				map.put("receiveId", obj.get("receiveId"));
				map.put("num", obj.get("num"));
				break;
			}
			return map;
		case 1:
			// 每周
			Date date = getWeekFirst();
			Integer count1 = paySuccessLogMapper
					.countByModelAndMemberIdAndWeek(model, memberId, date);
			if (count1 > 1) {
				map.put("result", false);
				return map;
			}
			for (int i = 0; i < json.size(); i++) {
				JSONObject obj = json.getJSONObject(i);
				// 获取cardId 和num
				map.put("result", true);
				map.put("receiveId", obj.get("receiveId"));
				map.put("num", obj.get("num"));
				break;
			}
			return map;
		case 2:
			// 每月
			Date monthDate = DateTimeKit.parse(DateTimeKit.getMonFirstDay(),
					"yyyy-MM-dd hh:mm:ss");
			Integer count2 = paySuccessLogMapper
					.countByModelAndMemberIdAndWeek(model, memberId, monthDate);
			if (count2 > 1) {
				map.put("result", false);
				return map;
			}
			for (int i = 0; i < json.size(); i++) {
				JSONObject obj = json.getJSONObject(i);
				// 获取cardId 和num
				map.put("result", true);
				map.put("receiveId", obj.get("receiveId"));
				map.put("num", obj.get("num"));
				break;
			}
			return map;
		case 3:
			// 时间段[{"startTime":"2017-03-29","endTime":"2017-04-30","receiveId":"35","num":1}]
			for (int i = 0; i < json.size(); i++) {
				JSONObject obj = json.getJSONObject(i);
				Object startTime = obj.get("startTime");
				Object endTime = obj.get("endTime");
				if (CommonUtil.isNotEmpty(startTime)
						&& CommonUtil.isNotEmpty(endTime)) {
					Date startDate = DateTimeKit.parse(
							CommonUtil.toString(startTime), "yyyy-MM-dd");
					Date endDate = DateTimeKit.parse(
							CommonUtil.toString(endTime), "yyyy-MM-dd");
					if (DateTimeKit.isBetween(startDate, endDate)) {
						Integer count3 = paySuccessLogMapper
								.countByModelAndMemberIdAndWeek(model, memberId, startDate);
						if(count3>1){
							map.put("result", false);
							return map;
						}
						// 获取cardId 和num
						map.put("result", true);
						map.put("receiveId", obj.get("receiveId"));
						map.put("num", obj.get("num"));
						return map;
					}
				}
			}
			map.put("result", false);
			return map;
		default:
			break;
		}
		return map;
	}

	public Date getWeekFirst() {
		Calendar cal = Calendar.getInstance();
		cal.setFirstDayOfWeek(Calendar.MONDAY);// 将每周第一天设为星期一，默认是星期天
		cal.add(Calendar.DATE, -1 * 7);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		String sunday = new SimpleDateFormat("yyyy-MM-dd")
				.format(cal.getTime());
		String time = sunday + " 24:00:00";
		return DateTimeKit.parse(time, "yyyy-MM-dd hh:mm:ss");
	}
}
*/