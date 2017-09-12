///**
// * P 2016年4月5日
// */
//package com.gt.member.service.old.memberpay.impl;
//
//import io.socket.client.socke;
//
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.SortedMap;
//
//import javax.servlet.http.HttpServletRequest;
//
//import net.sf.json.JSONArray;
//import net.sf.json.JSONObject;
//
//import org.apache.log4j.Logger;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import com.gt.dao.member.CardBuyMapper;
//import com.gt.dao.member.CardLentMapper;
//import com.gt.dao.member.CardMapper;
//import com.gt.dao.member.CardRecordMapper;
//import com.gt.dao.member.GiveConsumeMapper;
//import com.gt.dao.member.GiveRuleGoodsTypeMapper;
//import com.gt.dao.member.GiveRuleMapper;
//import com.gt.dao.member.GradeTypeMapper;
//import com.gt.dao.member.MemberAppletOpenidMapper;
//import com.gt.dao.member.MemberDateMapper;
//import com.gt.dao.member.MemberMapper;
//import com.gt.dao.member.MemberOldMapper;
//import com.gt.dao.member.PublicParameterSetMapper;
//import com.gt.dao.member.RechargeGiveMapper;
//import com.gt.dao.member.UserConsumeMapper;
//import com.gt.dao.user.BusFlowMapper;
//import com.gt.dao.user.BusUserMapper;
//import com.gt.dao.user.FenbiFlowRecordMapper;
//import com.gt.dao.user.WxPublicUsersMapper;
//import com.gt.entity.member.Card;
//import com.gt.entity.member.CardBuy;
//import com.gt.entity.member.CardLent;
//import com.gt.entity.member.CardRecord;
//import com.gt.entity.member.CardType;
//import com.gt.entity.member.GiveConsume;
//import com.gt.entity.member.GiveRule;
//import com.gt.entity.member.GiveRuleGoodsType;
//import com.gt.entity.member.GiveRuleGoodsTypeKey;
//import com.gt.entity.member.GradeType;
//import com.gt.entity.member.MemberEntity;
//import com.gt.entity.member.MemberDate;
//import com.gt.entity.member.MemberOld;
//import com.gt.entity.member.PublicParameterSet;
//import com.gt.entity.member.UserConsume;
//import com.gt.entity.set.WxShop;
//import com.gt.entity.user.BusUserEntity;
//import com.gt.entity.user.WxPublicUsersEntity;
//import com.gt.service.common.dict.DictService;
//import com.gt.service.member.MemberService;
//import com.gt.service.member.SystemMsgService;
//import com.gt.service.memberpay.MemberPayService;
//import com.gt.service.set.shop.WxShopService;
//import com.gt.util.CommonUtil;
//import com.gt.util.DateTimeKit;
//import com.gt.util.JedisUtil;
//import com.gt.util.Page;
//import com.gt.util.PropertiesUtil;
//import com.gt.wx.service.event.WxPayService;
//
///**
// * @author pengjiangli
// * @version 创建时间:2016年4月5日
// *
// */
//@Service
//public class MemberPayServiceImpl implements MemberPayService {
//
//	private static final Logger LOG = Logger
//			.getLogger(MemberPayServiceImpl.class);
//
//	@Autowired
//	private CardMapper cardMapper;
//
//	@Autowired
//	private MemberMapper memberMapper;
//
//	@Autowired
//	private GiveRuleMapper giveRuleMapper;
//
//	@Autowired
//	private UserConsumeMapper userConsumeMapper;
//
//	@Autowired
//	private GiveRuleGoodsTypeMapper giveRuleGoodsTypeMapper;
//
//	@Autowired
//	private GiveConsumeMapper giveConsumeMapper;
//
//	@Autowired
//	private WxPublicUsersMapper wxPublicUsersMapper;
//
//	@Autowired
//	private BusUserMapper busUserMapper;
//
//	@Autowired
//	private BusFlowMapper busFlowMapper;
//
//	@Autowired
//	private CardRecordMapper cardRecordMapper;
//
//	@Autowired
//	private WxPayService wxPayService;
//
//	@Autowired
//	private RechargeGiveMapper rechargeGiveMapper;
//
//	@Autowired
//	private GradeTypeMapper gradeTypeMapper;
//
//	@Autowired
//	private FenbiFlowRecordMapper fenbiFlowRecordMapper;
//
//	@Autowired
//	private CardBuyMapper cardBuyMapper;
//
//	@Autowired
//	private MemberDateMapper memberDateMapper;
//
//	@Autowired
//	private CardLentMapper cardLentMapper;
//
//	@Autowired
//	private PublicParameterSetMapper publicParameterSetMapper;
//
//	@Autowired
//	private DictService dictService;
//
//	@Autowired
//	private SystemMsgService systemMsgService;
//
//	@Autowired
//	private MemberService memberService;
//
//	@Autowired
//	private MemberAppletOpenidMapper memberAppletOpenidMapper;
//
//	@Autowired
//	private MemberOldMapper memberOldMapper;
//
//	@Autowired
//	private WxShopService wxShopService;
//
//	/**
//	 * 会员卡充值
//	 */
//	@Override
//	@Transactional(rollbackFor = Exception.class)
//	public SortedMap<Object, Object> recharge(String url, Integer memberId,
//			String cardNo, Double money, Integer count) throws Exception {
//		if (CommonUtil.isEmpty(memberId) || CommonUtil.isEmpty(cardNo)
//				|| CommonUtil.isEmpty(money)) {
//			throw new Exception();
//		}
//		MemberEntity member = memberMapper.selectByPrimaryKey(memberId);
//		Card card = cardMapper.findCardByCardNo(member.getBusid(), cardNo);
//		if (CommonUtil.isEmpty(card)) {
//			throw new Exception();
//		}
//		// 添加会员记录
//		UserConsume uc = new UserConsume();
//		uc.setPublicId(member.getPublicId());
//		uc.setBususerid(member.getBusid());
//		uc.setMemberid(memberId);
//		uc.setMcid(card.getMcId());
//		uc.setCtid(card.getCtId());
//		uc.setGtId(card.getGtId());
//		uc.setRecordtype((byte) 1);
//		uc.setUctype((byte) 7);
//		uc.setTotalmoney(money);
//		uc.setCreatedate(new Date());
//		uc.setPaystatus((byte) 0);
//		uc.setDiscount(100);
//		uc.setDiscountmoney(money);
//		uc.setPaymenttype((byte) 1);
//		uc.setDatasource((byte) 1);
//		uc.setIschongzhi((byte) 1);
//		if (card.getCtId() == 3) {
//			uc.setGivegift("赠送金额");
//			GiveRule gr = findGive(member.getBusid(), card.getGtId(), 3);
//			if (CommonUtil.isNotEmpty(gr)) {
//				count = findRechargegive(money, gr.getGrId(),
//						member.getBusid(), card.getCtId());
//				uc.setGiftcount(count);
//			}
//			uc.setUccount(0);
//		} else if (card.getCtId() == 5) {
//			uc.setGivegift("赠送次数");
//			GiveRule gr = findGive(member.getBusid(), card.getGtId(), 5);
//			if (CommonUtil.isNotEmpty(gr)) {
//				int givecount = findRechargegive(money, gr.getGrId(),
//						member.getBusid(), card.getCtId());
//				uc.setGiftcount(givecount);
//			}
//			uc.setUccount(count);
//		}
//		String orderCode = CommonUtil.getMEOrderCode();
//		uc.setOrdercode(orderCode);
//
//		// 获取主账户id
//		List<Map<String, Object>> shops = dictService.shopList(busUserMapper
//				.selectByPrimaryKey(member.getBusid()));
//		if (CommonUtil.isNotEmpty(shops) && shops.size() > 0) {
//			uc.setStoreid(CommonUtil.toInteger(shops.get(0).get("id")));
//		}
//
//		userConsumeMapper.insertSelective(uc);
//
//		WxPublicUsersEntity wxPublicUsers = wxPublicUsersMapper
//				.selectByPrimaryKey(member.getPublicId());
//		// 统一下单调用
//		Map<String, Object> params = new HashMap<String, Object>();
//		params.put("appid", wxPublicUsers.getAppid());
//		params.put("mchid", wxPublicUsers.getMchId());
//		params.put("sysOrderNo", orderCode);
//		params.put("key", wxPublicUsers.getApiKey());
//		params.put("productId", orderCode);
//		params.put("desc", "会员卡充值");
//		params.put("totalFee", money);
//		params.put("ip", "127.0.0.1");
//		params.put("openid", member.getOpenid());
//		params.put("url", url);
//		params.put("model", 2);
//		params.put("authRefreshToken", wxPublicUsers.getAuthRefreshToken());
//		SortedMap<Object, Object> sortedMap = wxPayService.memberPay(params);
//		if (CommonUtil.isNotEmpty(sortedMap.get("code"))
//				&& sortedMap.get("code").equals("1")) {
//			sortedMap.put("public_id", member.getPublicId());
//		}
//		return sortedMap;
//	}
//
//	/**
//	 * 查询赠送规则
//	 *
//	 * @param publicId
//	 * @param gtId
//	 * @param ctId
//	 * @return
//	 */
//	public GiveRule findGive(Integer busId, Integer gtId, Integer ctId) {
//		GiveRule gr = giveRuleMapper.findBybusIdAndGtIdAndCtId(busId, gtId,
//				ctId);
//		return gr;
//	}
//
//	/**
//	 * 根据订单号添加赠送物品记录
//	 *
//	 * @param orderId
//	 *            订单号
//	 * @param itemName
//	 *            物品名称
//	 * @param type
//	 *            0不送赠送物品 1赠送物品
//	 * @throws Exception
//	 */
//	@Transactional(rollbackFor = Exception.class)
//	public void findGiveRule(String phone, String orderId, String itemName,
//			byte type) throws Exception {
//		List<Map<String, Object>> ucs = userConsumeMapper
//				.findByOrderCode(orderId);
//		if (CommonUtil.isEmpty(ucs) || ucs.size() == 0 || ucs.size() > 1) {
//			LOG.error("赠送物品查询订单出现异常");
//			throw new Exception();
//		}
//
//		try {
//			Integer busUserId = Integer.parseInt(ucs.get(0).get("busUserId")
//					.toString());
//
//			Integer gtId = Integer.parseInt(ucs.get(0).get("gt_id").toString());
//			Integer ctId = Integer.parseInt(ucs.get(0).get("ctId").toString());
//			double price = Double.parseDouble(ucs.get(0).get("discountMoney")
//					.toString());
//
//			Integer recFreezeType = 0;
//			switch (ctId) {
//			case 1:
//				recFreezeType = 20;
//				break;
//			case 2:
//				recFreezeType = 21;
//				break;
//			case 3:
//				recFreezeType = 22;
//				break;
//			case 4:
//				recFreezeType = 23;
//				break;
//			case 5:
//				recFreezeType = 24;
//				break;
//
//			default:
//				break;
//			}
//
//			// 查询粉笔数量
//			Integer fenbi = fenbiFlowRecordMapper.getFenbiSurplus(busUserId, 1,
//					recFreezeType, ctId);
//			// 如果是折扣卡 金额用折后金额
//			if (ctId == 2) {
//				price = Double.parseDouble(ucs.get(0).get("discountMoney")
//						.toString());
//			}
//			// 如果是次卡 次数默认为金额
//			if (ctId == 5) {
//				price = Double
//						.parseDouble(ucs.get(0).get("uccount").toString());
//			}
//			String recordType = ucs.get(0).get("recordType").toString();
//
//			Integer ucId = Integer.parseInt(ucs.get(0).get("id").toString());
//			// 如果是次卡 和 储值卡 就查询第一个等级的赠送规则
//			GiveRule gr = null;
//			if (ctId == 5 || ctId == 3) {
//				List<Map<String, Object>> grs = giveRuleMapper
//						.findByBusIdAndCtId(busUserId, ctId);
//				gr = new GiveRule();
//				if (grs.size() != 0) {
//					gr.setGrId(CommonUtil.toInteger(grs.get(0).get("gr_id")));
//				}
//			} else {
//				gr = giveRuleMapper.findBybusIdAndGtIdAndCtId(busUserId, gtId,
//						ctId);
//			}
//
//			Double fans_currency = 0.0;// 粉笔
//			int integral = 0; // 积分
//			int flow = 0;
//
//			MemberDate memberday = findMemeberDate(busUserId, ctId);
//			boolean flag = false; // 表示今天是否是会员日
//			if (CommonUtil.isNotEmpty(memberday)) {
//				flag = true;
//			}
//
//			if (type == 1) {
//				if (CommonUtil.isNotEmpty(gr)) {
//					List<Map<String, Object>> grgts = giveRuleGoodsTypeMapper
//							.findByGrId(gr.getGrId());
//					GiveConsume giveConsume = null;
//					GiveRuleGoodsType grgt = null;
//					for (Map<String, Object> map : grgts) {
//						giveConsume = new GiveConsume();
//						if (CommonUtil.isEmpty(map.get("gId")))
//							continue;
//						if ("1".equals(map.get("gId").toString())) {
//							if ("1".equals(map.get("give_type").toString())) {
//								// 积分
//								if (CommonUtil.isEmpty(map.get("money")))
//									continue;
//								Double money = Double.parseDouble(map.get(
//										"money").toString());
//								int count = (int) Math.floor(price / money);
//								if (count == 0)
//									continue;
//								if (CommonUtil.isEmpty(map.get("number")))
//									continue;
//								int num = count
//										* Integer.parseInt(map.get("number")
//												.toString());
//								Integer upperLmit = Integer.parseInt(map.get(
//										"upperLmit").toString());
//								if (upperLmit != 0) {
//									num = num > upperLmit ? upperLmit : num;
//								}
//								// 会员日 积分赠送
//								if (flag) {
//									num = num * memberday.getIntegral();
//								}
//
//								giveConsume.setGcTotal(num);
//								giveConsume.setGtId(Integer.parseInt(map.get(
//										"gId").toString()));
//								giveConsume.setGtName(map.get("gt_name")
//										.toString());
//								giveConsume.setGtUnit(map.get("gt_unit")
//										.toString());
//								giveConsume.setUcId(ucId);
//								giveConsume.setMemberid(CommonUtil
//										.toInteger(ucs.get(0).get("memberId")));
//								giveConsume.setSenddate(new Date());
//								giveConsumeMapper.insertSelective(giveConsume);
//								integral = num;
//							}
//						} else {
//							// 添加赠送物品记录
//							Integer upperLmit = Integer.parseInt(map.get(
//									"upperLmit").toString());
//
//							if ("2".equals(map.get("gId").toString())
//									|| "3".equals(map.get("gId").toString())
//									|| upperLmit > 0) {
//								Integer count = Integer.parseInt(map.get(
//										"number").toString());
//								Double money = Double.parseDouble(map.get(
//										"money").toString());
//								if (price < money)
//									continue;
//								if (upperLmit < count) {
//									// 扣除商家粉币数量
//									if ("3".equals(map.get("gId").toString())) {
//										if (fenbi < count) {
//											continue;
//										}
//										// 会员日 粉币赠送
//										if (flag) {
//											count = count
//													* memberday
//															.getFansCurrency();
//										}
//
//										giveConsume.setGcTotal(count);
//										// 冻结商家粉笔数量
//										fenbiFlowRecordMapper
//												.updateFenbiReduce(busUserId,
//														count, ctId,
//														recFreezeType);
//
//										fans_currency = (double) count;
//									} else if ("2".equals(map.get("gId")
//											.toString())) {
//										Integer flowCount = Integer
//												.parseInt(map.get("number")
//														.toString());
//										// 会员日赠送流量
//										if (flag) {
//											flowCount = flowCount
//													* memberday.getFlow();
//											giveConsume.setGcTotal(flowCount);
//											flow = flowCount
//													* memberday.getFlow();
//										} else {
//											giveConsume.setGcTotal(flowCount);
//											flow = flowCount;
//										}
//
//									}
//									// 上限非等于0 认为是商家自定义物品
//									if (upperLmit != 0) {
//										giveConsume.setGcTotal(upperLmit);
//									}
//								} else {
//									giveConsume.setGcTotal(count);
//								}
//
//								giveConsume.setGtId(Integer.parseInt(map.get(
//										"gId").toString()));
//								giveConsume.setGtName(map.get("gt_name")
//										.toString());
//								giveConsume.setGtUnit(map.get("gt_unit")
//										.toString());
//								giveConsume.setUcId(ucId);
//								giveConsume.setMemberid(CommonUtil
//										.toInteger(ucs.get(0).get("memberId")));
//								giveConsume.setSenddate(new Date());
//								giveConsumeMapper.insertSelective(giveConsume);
//
//								if (!"2".equals(map.get("gId").toString())
//										&& !"3".equals(map.get("gId")
//												.toString())) {
//									// 修改赠送规则物品剩余数量(商家自定义物品)
//									grgt = new GiveRuleGoodsType();
//									grgt.setGrId(Integer.parseInt(map.get(
//											"gr_id").toString()));
//									grgt.setGtId(Integer.parseInt(map
//											.get("gId").toString()));
//
//									if (upperLmit < count) {
//										grgt.setUpperlmit(0);
//										grgt.setGiveType((byte) 2);
//									} else {
//										grgt.setUpperlmit(upperLmit - count);
//									}
//									giveRuleGoodsTypeMapper
//											.updateByPrimaryKeySelective(grgt);
//								}
//							}
//						}
//					}
//				}
//			}
//
//			if (CommonUtil.isNotEmpty(ucs.get(0).get("mcId"))) {
//				Card card = cardMapper.selectByPrimaryKey(Integer.parseInt(ucs
//						.get(0).get("mcId").toString()));
//
//				// 修改会员的流量 粉笔 积分信息
//				MemberEntity member1 = memberMapper.findByMcIdAndbusId(
//						card.getBusid(),
//						Integer.parseInt(ucs.get(0).get("mcId").toString()));
//				// 消费 积分为负数 改为正数
//				if (integral < 0) {
//					integral = -integral;
//				}
//
//				if (CommonUtil.isNotEmpty(member1)) {
//					MemberEntity member = new MemberEntity();
//					member.setId(member1.getId());
//					member.setFansCurrency(member1.getFansCurrency()
//							+ fans_currency);
//					member.setFlow(member1.getFlow() + flow);
//					member.setIntegral(member1.getIntegral() + integral);
//					member.setFlowdate(new Date());
//					member.setIntegraldate(new Date());
//					member.setTotalintegral(member1.getTotalintegral()
//							+ integral);
//					if (ctId == 5) {
//						if (CommonUtil.isNotEmpty(ucs.get(0).get("totalMoney"))) {
//							price = Double.parseDouble(ucs.get(0)
//									.get("totalMoney").toString());
//						}
//					}
//					member.setTotalmoney(member1.getTotalmoney() + price);
//					try {
//						memberMapper.updateByPrimaryKeySelective(member);
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				}
//				Map<String, Object> map = null;
//				// 判断时效卡升级
//				if (ctId == 4) {
//					map = findNextGradeCtId4(busUserId, gtId, price);
//				} else if (card.getApplytype() != 4) { // 泛会员升级
//					// 判断会员是否是要升级
//					map = findNextGrade(busUserId, ctId, gtId,
//							member1.getTotalintegral() + integral,
//							member1.getTotalmoney() + price);
//				}
//
//				// 用来标示该价格正负
//				if (!"1".equals(recordType)) {
//					price = -price;
//				}
//
//				double balance = 0.0;
//				if (CommonUtil.isNotEmpty(card)
//						&& CommonUtil.isNotEmpty(card.getMoney())) {
//					balance = card.getMoney();
//					if ("3".equals(CommonUtil.toString(ucs.get(0).get(
//							"paymentType")))
//							|| "5".equals(CommonUtil.toString(ucs.get(0).get(
//									"paymentType")))) {
//						card.setMoney(balance + price > 0 ? balance + price : 0);
//					}
//
//					if (CommonUtil.isNotEmpty(ucs.get(0).get("uccount"))) {
//						Integer uccount = Integer.parseInt(ucs.get(0)
//								.get("uccount").toString());
//						if (ctId == 5) {
//							if (CommonUtil.isNotEmpty(ucs.get(0).get(
//									"giftCount"))) {
//								Integer giftCount = Integer.parseInt(ucs.get(0)
//										.get("giftCount").toString());
//								uccount = uccount + giftCount;
//							}
//						}
//						if (uccount != 0) {
//							card.setFrequency(card.getFrequency() - uccount);
//						}
//					}
//					// 修改会员卡等级和赠送规则
//					if (CommonUtil.isNotEmpty(map)) {
//						card.setGtId(Integer.parseInt(map.get("gtId")
//								.toString()));
//						card.setGrId(Integer.parseInt(map.get("grId")
//								.toString()));
//
//						// 升级通知
//						systemMsgService
//								.upgradeMemberMsg(
//										member1,
//										card.getCardno(),
//										CommonUtil.isEmpty(card.getExpiredate()) ? "长期有效"
//												: DateTimeKit.format(card
//														.getExpiredate()));
//					}
//					cardMapper.updateByPrimaryKeySelective(card);
//				}
//				if (card.getCtId() == 5) {
//					if ("1".equals(ucs.get(0).get("recordType").toString())) {
//						saveCardRecordNew(Integer.parseInt(ucs.get(0)
//								.get("mcId").toString()), (byte) 1,
//								ucs.get(0).get("uccount") + "次,送"
//										+ ucs.get(0).get("giftcount") + "次",
//								itemName, member1.getBusid(), card
//										.getFrequency().toString(),
//								card.getCtId(), 0.0);
//					} else {
//						if ("0".equals(CommonUtil.toString(ucs.get(0).get(
//								"uccount")))) {
//							saveCardRecordNew(
//									Integer.parseInt(ucs.get(0).get("mcId")
//											.toString()), (byte) 1,
//									price + "元", itemName, member1.getBusid(),
//									card.getFrequency().toString(),
//									card.getCtId(), 0.0);
//						} else {
//							saveCardRecordNew(Integer.parseInt(ucs.get(0)
//									.get("mcId").toString()), (byte) 1, ucs
//									.get(0).get("uccount") + "次", itemName,
//									member1.getBusid(), card.getFrequency()
//											.toString(), card.getCtId(), 0.0);
//						}
//
//					}
//				} else {
//					saveCardRecordNew(
//							Integer.parseInt(ucs.get(0).get("mcId").toString()),
//							(byte) 1, price + "元", itemName,
//							member1.getBusid(), card.getMoney().toString(),
//							card.getCtId(), 0.0);
//				}
//			}
//		} catch (Exception e) {
//			LOG.error("添加赠送记录数据查询异常异常", e);
//			throw new Exception();
//		}
//	}
//
//	/**
//	 * 判断时效卡升级
//	 *
//	 * @param publicId
//	 *            公众号
//	 * @param ctId
//	 *            卡类型
//	 * @param gtId
//	 *            等级id
//	 * @param integral
//	 *            累计积分
//	 * @param totalmoney
//	 *            累计消费金额
//	 * @return
//	 */
//	public Map<String, Object> findNextGradeCtId4(Integer busId, Integer gtId,
//			Double totalmoney) {
//		Map<String, Object> map = new HashMap<String, Object>();
//		List<Map<String, Object>> giveRules = giveRuleMapper
//				.findByBusIdAndCtId(busId, 4);
//		if (giveRules.size() != 0) {
//			for (Map<String, Object> giveRule : giveRules) {
//				if (CommonUtil.isNotEmpty(giveRule.get("gr_rechargeMoney"))) {
//					Double rechargeMoney = CommonUtil.toDouble(giveRule
//							.get("gr_rechargeMoney"));
//					if (totalmoney.equals(rechargeMoney)) {
//						map.put("gtId", giveRule.get("gt_id"));
//						map.put("grId", giveRule.get("gr_id"));
//						return map;
//					}
//				}
//			}
//		}
//		return null;
//	}
//
//	/**
//	 * 判断是否升级
//	 *
//	 * @param publicId
//	 *            公众号
//	 * @param ctId
//	 *            卡类型
//	 * @param gtId
//	 *            等级id
//	 * @param integral
//	 *            累计积分
//	 * @param totalmoney
//	 *            累计消费金额
//	 * @return
//	 */
//	public Map<String, Object> findNextGrade(Integer busId, Integer ctId,
//			Integer gtId, Integer integral, double totalmoney) {
//		Map<String, Object> map = new HashMap<String, Object>();
//		// <!--查询下一个等级start-->
//		List<Map<String, Object>> gradeTypes = gradeTypeMapper.findByCtId(
//				busId, ctId);
//
//		if (gradeTypes != null) {
//			for (int i = 0; i < gradeTypes.size(); i++) {
//				if (CommonUtil.isNotEmpty(gradeTypes.get(i).get("gtId"))) {
//					if (gtId.equals(gradeTypes.get(i).get("gtId"))) {
//						if (i < gradeTypes.size() - 1) {
//							// 下一级id
//							if (CommonUtil.isNotEmpty(gradeTypes.get(i + 1)
//									.get("gtId"))) {
//								Integer id = Integer.parseInt(gradeTypes
//										.get(i + 1).get("gtId").toString());
//								GiveRule nextGiveRule = giveRuleMapper
//										.findBybusIdAndGtIdAndCtId(busId, id,
//												Integer.parseInt(ctId
//														.toString()));
//								if (CommonUtil.isEmpty(nextGiveRule)) {
//									break;
//								}
//								// 金额升级
//								if (0 == nextGiveRule.getGrUpgradetype()) {
//									if (totalmoney >= nextGiveRule
//											.getGrUpgradecount()) {
//										map.put("gtId", id);
//										map.put("grId", nextGiveRule.getGrId());
//										return map;
//									}
//								}
//								if (1 == nextGiveRule.getGrUpgradetype()) {
//									if (integral >= nextGiveRule
//											.getGrUpgradecount()) {
//										map.put("gtId", id);
//										map.put("grId", nextGiveRule.getGrId());
//										return map;
//									}
//								}
//							}
//						}
//					}
//				}
//			}
//
//			GiveRule nextGiveRule = giveRuleMapper.findBybusIdAndGtIdAndCtId(
//					busId, gtId, Integer.parseInt(ctId.toString()));
//			map.put("gtId", gtId);
//			map.put("grId", nextGiveRule.getGrId());
//			return map;
//		}
//		return null;
//	}
//
//	/**
//	 * 添加会员卡记录(老数据接口)
//	 *
//	 * @param cardId
//	 *            卡类型id
//	 * @param recordType
//	 *            消费类型
//	 * @param number
//	 *            数量
//	 * @param itemName
//	 *            物品名称
//	 * @param publicId
//	 *            公众号
//	 * @param balance
//	 *            余额
//	 */
//	@Override
//	public CardRecord saveCardRecord(Integer cardId, Byte recordType,
//			String number, String itemName, Integer publicId, String balance,
//			Integer ctId) {
//		CardRecord cr = new CardRecord();
//		cr.setCardid(cardId);
//		cr.setRecordtype(recordType);
//		cr.setNumber(number);
//		cr.setCreatedate(new Date());
//		cr.setItemname(itemName);
//		cr.setPublicid(publicId);
//		cr.setBalance(balance);
//		cr.setCtid(ctId);
//		try {
//			cardRecordMapper.insertSelective(cr);
//			if (recordType == 2) {
//				MemberEntity member = memberMapper.findByMcId1(cardId);
//				if (CommonUtil.isNotEmpty(member.getPublicId())) {
//					// 积分变动通知
//					systemMsgService.jifenMsg(cr, member);
//				}
//			}
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return cr;
//	}
//
//	/**
//	 * 添加会员卡记录((新数据接口))
//	 *
//	 * @param cardId
//	 *            卡类型id
//	 * @param recordType
//	 *            消费类型
//	 * @param number
//	 *            数量
//	 * @param itemName
//	 *            物品名称
//	 * @param publicId
//	 *            公众号
//	 * @param balance
//	 *            余额
//	 */
//	@Override
//	public CardRecord saveCardRecordNew(Integer cardId, Byte recordType,
//			String number, String itemName, Integer busId, String balance,
//			Integer ctId, double amount) {
//		if (CommonUtil.isEmpty(busId)) {
//			return null;
//		}
//
//		CardRecord cr = new CardRecord();
//		cr.setCardid(cardId);
//		cr.setRecordtype(recordType);
//		cr.setNumber(number);
//		cr.setCreatedate(new Date());
//		cr.setItemname(itemName);
//		cr.setBusid(busId);
//		cr.setBalance(balance);
//		cr.setCtid(ctId);
//		cr.setAmount(amount);
//		try {
//			cardRecordMapper.insertSelective(cr);
//			if (recordType == 2) {
//				MemberEntity member = memberMapper.findByMcId1(cardId);
//				// 积分变动通知
//				systemMsgService.jifenMsg(cr, member);
//
//			}
//
//		} catch (Exception e) {
//			e.printStackTrace();
//			LOG.error("保存手机端记录异常", e);
//		}
//		return cr;
//	}
//
//	/**
//	 * 会员记录表 修改支付状态
//	 *
//	 * @param id
//	 * @param payStatus
//	 *            支付状态 0未支付 1已支付 2支付失败
//	 */
//	public void updateUserConsume(Integer id, Byte payStatus) {
//		UserConsume uc = new UserConsume();
//		uc.setId(id);
//		uc.setPaystatus(payStatus);
//		userConsumeMapper.updateByPrimaryKeySelective(uc);
//	}
//
//	/**
//	 * 根据赠送规则和商户id查询
//	 *
//	 * @param ctId
//	 * @param publicId
//	 * @return
//	 */
//	public int findRechargegive(double price, Integer grId, Integer busId,
//			Integer ctId) {
//		MemberDate memberdate = findMemeberDate(busId, ctId);
//		List<Map<String, Object>> rechargeGives = null;
//		if (CommonUtil.isNotEmpty(memberdate)) {
//			rechargeGives = rechargeGiveMapper.findBybusIdAndGrId(busId, grId,
//					1);
//		} else {
//			rechargeGives = rechargeGiveMapper.findBybusIdAndGrId(busId, grId,
//					0);
//		}
//		if (rechargeGives == null || rechargeGives.size() == 0) {
//			return 0;
//		}
//		for (int i = 0; i < rechargeGives.size(); i++) {
//			if (i + 1 == rechargeGives.size()) {
//				double money = Double.parseDouble(rechargeGives.get(i)
//						.get("money").toString());
//				if (money <= price) {
//					return Integer.parseInt(rechargeGives.get(i)
//							.get("giveCount").toString());
//				} else {
//					return 0;
//				}
//			}
//			if (CommonUtil.isNotEmpty(rechargeGives.get(i).get("money"))) {
//				double money = Double.parseDouble(rechargeGives.get(i)
//						.get("money").toString());
//				double money1 = Double.parseDouble(rechargeGives.get(i + 1)
//						.get("money").toString());
//				if (price >= money && price < money1) {
//					return Integer.parseInt(rechargeGives.get(i)
//							.get("giveCount").toString());
//				}
//			}
//
//		}
//		return 0;
//	}
//
//	/**
//	 * 会员回调接口
//	 */
//	@Override
//	public void memberCallBack(String orderId, Byte payStatus) {
//		try {
//
//			LOG.error("订单单号 ：" + orderId);
//			List<Map<String, Object>> ucs = userConsumeMapper
//					.findByOrderCode(orderId);
//			try {
//				if (payStatus == 1) {
//
//					memberGive(orderId, (byte) 1);
//
//					if (CommonUtil.isNotEmpty(ucs.get(0).get("mcId"))) {
//						Card card = cardMapper.selectByPrimaryKey(Integer
//								.parseInt(ucs.get(0).get("mcId").toString()));
//
//						// 修改会员的流量 粉笔 积分信息
//						MemberEntity member = memberMapper.findByMcIdAndbusId(
//								card.getBusid(),
//								Integer.parseInt(ucs.get(0).get("mcId")
//										.toString()));
//
//						if (card.getCtId() == 5) {
//							String uccount = "";
//							if (CommonUtil.isNotEmpty(ucs.get(0).get(
//									"giftCount"))) {
//								uccount = ucs.get(0).get("uccount") + "次,送"
//										+ ucs.get(0).get("giftCount") + "次";
//							} else {
//								uccount = ucs.get(0).get("uccount") + "次";
//							}
//							saveCardRecordNew(Integer.parseInt(ucs.get(0)
//									.get("mcId").toString()), (byte) 1,
//									uccount, "充值", member.getBusid(), card
//											.getFrequency().toString(),
//									card.getCtId(), 0.0);
//						} else {
//							saveCardRecordNew(Integer.parseInt(ucs.get(0)
//									.get("mcId").toString()), (byte) 1, ucs
//									.get(0).get("totalMoney")
//									+ "元,送"
//									+ ucs.get(0).get("giftCount") + "元", "充值",
//									member.getBusid(), card.getMoney()
//											.toString(), card.getCtId(), 0.0);
//						}
//
//						// 消息模板推送
//						if (card.getCtId() == 3) {
//							systemMsgService.sendChuzhiCard(
//									member,
//									CommonUtil.toDouble(ucs.get(0).get(
//											"totalMoney")));
//						} else if (card.getCtId() == 5) {
//							systemMsgService.sendCikaCard(
//									member,
//									CommonUtil.toDouble(ucs.get(0).get(
//											"totalMoney")),
//									CommonUtil.toInteger(ucs.get(0).get(
//											"uccount")));
//						}
//
//					}
//				}
//			} catch (Exception e) {
//				LOG.error("发放赠送物品异常", e);
//			}
//		} catch (Exception e) {
//			LOG.error("回调异常", e);
//		}
//
//	}
//
//	/**
//	 * 会员消费
//	 *
//	 * @throws Exception
//	 */
//	@Override
//	@Transactional(rollbackFor = Exception.class)
//	public Map<String, Object> memberConsume(HttpServletRequest request,
//			Integer publicId1, BusUserEntity busUser, String cardNo, double money,
//			Byte recordType, Byte type, Integer integral, Integer fenbi,
//			Integer uccount, Integer discount, Double discountmoney,
//			Integer orderid, String uctable, Byte paymenttype, Byte paystatus,
//			String givegift, Integer giftCount, String orderCode,
//			String itemName, Integer jie, Integer clId, Integer payType,
//			double jifenmoney, double fenbimoney) throws Exception {
//
//		Map<String, Object> map = new HashMap<String, Object>();
//		if (CommonUtil.isEmpty(cardNo) || CommonUtil.isEmpty(money)) {
//			map.put("result", false);
//			map.put("result", "卡片和金额不错误");
//			return map;
//		}
//		Card card = cardMapper.findCardByCardNo(busUser.getId(), cardNo);
//		if (CommonUtil.isEmpty(card)) {
//			map.put("result", false);
//			map.put("message", "卡号不存在");
//			return map;
//		}
//
//		MemberEntity member = memberMapper.findByMcIdAndbusId(busUser.getId(),
//				card.getMcId());
//
//		switch (card.getCtId()) {
//		case 3:
//			if (payType == 1) {
//				if (card.getMoney() < (discountmoney)) {
//					map.put("result", false);
//					map.put("message", "卡内余额不足,请充值");
//					return map;
//				}
//			}
//			break;
//		case 5:
//			if (card.getFrequency() < (uccount)) {
//				map.put("result", false);
//				map.put("message", "卡内次数不够,请充值");
//				return map;
//			}
//		default:
//			break;
//		}
//
//		if (CommonUtil.isEmpty(member)) {
//			map.put("result", false);
//			map.put("message", "不存在该会员信息");
//			return map;
//		}
//
//		if (jie == 1) {
//			// 借给他人数据处理
//			CardLent c = new CardLent();
//			c.setId(clId);
//			c.setUsestate((byte) 1);
//			cardLentMapper.updateByPrimaryKeySelective(c);
//		}
//
//		GiveRule gr = giveRuleMapper.findBybusIdAndGtIdAndCtId(busUser.getId(),
//				card.getGtId(), card.getCtId());
//		if (CommonUtil.isNotEmpty(gr)
//				&& CommonUtil.isNotEmpty(gr.getGrStartdate())) {
//
//			if (DateTimeKit.laterThanNow(gr.getGrStartdate())) {
//				String date = DateTimeKit.format(gr.getGrStartdate());
//				map.put("result", false);
//				map.put("message", "卡片还未启用,启用时间为:" + date);
//				return map;
//			}
//		}
//		int jifen = 0;
//		if (jifenmoney > 0) {
//			jifen = deductJifen(jifenmoney, busUser.getId());
//		}
//
//		int fenbi1 = 0;
//		if (fenbimoney > 0) {
//			fenbi1 = deductFenbi(fenbimoney, busUser.getId()).intValue();
//		}
//
//		// 添加会员记录
//		UserConsume uc = new UserConsume();
//		uc.setPublicId(member.getPublicId());
//		uc.setBususerid(busUser.getId());
//		uc.setMemberid(member.getId());
//		uc.setMcid(card.getMcId());
//		uc.setCtid(card.getCtId());
//		uc.setGtId(card.getGtId());
//		uc.setRecordtype(recordType);
//		uc.setTotalmoney(money);
//		uc.setCreatedate(new Date());
//		uc.setPaystatus((byte) 0);
//		uc.setUctype(type);
//		uc.setIntegral(jifen);
//		uc.setFenbi(fenbi1);
//		uc.setUccount(uccount);
//		uc.setDiscount(discount);
//		uc.setDiscountmoney(discountmoney);
//		uc.setOrderid(orderid);
//		uc.setUctable(uctable);
//		uc.setCreatedate(new Date());
//		uc.setPaymenttype(paymenttype);
//		uc.setPaystatus(paystatus);
//		uc.setGivegift(givegift);
//		uc.setOrdercode(orderCode);
//		uc.setDatasource((byte) 0);
//
//		// 门店
//		List<Map<String, Object>> shops = dictService.shopList(CommonUtil
//				.getLoginUser(request));
//		if (CommonUtil.isNotEmpty(shops) && shops.size() > 0) {
//			uc.setStoreid(CommonUtil.toInteger(shops.get(0).get("id")));
//		}
//
//		userConsumeMapper.insertSelective(uc);
//
//		if (CommonUtil.isNotEmpty(cardNo)) {
//			if (jifen > 0 || fenbi1 > 0) {
//				MemberEntity m = new MemberEntity();
//				m.setId(member.getId());
//				m.setFansCurrency(member.getFansCurrency() - fenbi1);
//				m.setIntegral(member.getIntegral() - jifen);
//				memberMapper.updateByPrimaryKeySelective(m);
//				if (jifen > 0) {
//					// 添加会员卡操作记录
//					saveCardRecordNew(card.getMcId(), (byte) 2, jifen + "",
//							"线下支付积分抵扣", member.getBusid(), "", card.getCtId(),
//							-jifen);
//				}
//				if (fenbi1 > 0) {
//					// 归还到商家账户
//					busUser.setFansCurrency(busUser.getFansCurrency() + fenbi1);
//					CommonUtil.setLoginUser(request, busUser);
//					BusUserEntity b = new BusUserEntity();
//					b.setId(busUser.getId());
//					b.setFansCurrency(busUser.getFansCurrency());
//					busUserMapper.updateByPrimaryKeySelective(b);
//
//					// 添加会员卡操作记录
//					saveCardRecordNew(card.getMcId(), (byte) 3, fenbi1 + "",
//							"线下支付粉币抵扣", member.getBusid(), null,
//							card.getCtId(), -fenbi1);
//				}
//			}
//			findGiveRule(member.getPhone(), orderCode, itemName, (byte) 1);
//		}
//		// 微信推送模板
//		if (card.getCtId() == 3) {
//			systemMsgService.sendChuzhiXiaofei(member, money);
//		} else if (card.getCtId() == 5) {
//			systemMsgService.sendCikaXiaofei(member);
//		}
//
//		map.put("result", true);
//		map.put("message", "消费成功");
//		return map;
//	}
//
//	/**
//	 * pc会员充值
//	 */
//	@Override
//	public Map<String, Object> recharge(HttpServletRequest request,
//			Integer busId, String cardNo, double money, int count)
//			throws Exception {
//		Map<String, Object> map = new HashMap<String, Object>();
//		if (CommonUtil.isEmpty(cardNo) || CommonUtil.isEmpty(money)) {
//			map.put("result", false);
//			map.put("message", "数据异常");
//			return map;
//		}
//
//		Card card = cardMapper.findCardByCardNo(busId, cardNo);
//		MemberEntity member = null;
//		try {
//			member = memberMapper.findByMcIdAndbusId(busId, card.getMcId());
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		if (CommonUtil.isEmpty(card)) {
//			map.put("result", false);
//			map.put("message", "卡号不存在");
//			return map;
//		}
//
//		// 获取当前登录人所属门店
//		List<Map<String, Object>> shops = dictService.shopList(CommonUtil
//				.getLoginUser(request));
//
//		// 添加会员记录
//		UserConsume uc = new UserConsume();
//		uc.setPublicId(member.getPublicId());
//		uc.setBususerid(member.getBusid());
//		uc.setMemberid(member.getId());
//		uc.setMcid(card.getMcId());
//		uc.setCtid(card.getCtId());
//		uc.setGtId(card.getGtId());
//		uc.setRecordtype((byte) 1);
//		uc.setUctype((byte) 7);
//		uc.setTotalmoney(money);
//		uc.setDiscountmoney(money);
//		uc.setCreatedate(new Date());
//		uc.setPaystatus((byte) 0);
//		uc.setPaymenttype((byte) 3);
//		uc.setIschongzhi((byte) 1);
//		if (CommonUtil.isNotEmpty(shops) && shops.size() > 0) {
//			uc.setStoreid(CommonUtil.toInteger(shops.get(0).get("id")));
//		}
//
//		uc.setModuletype((byte) 3);
//		if (card.getCtId() == 3) {
//			uc.setGivegift("赠送金额");
//			GiveRule gr = findGive(member.getBusid(), card.getGtId(), 3);
//			if (CommonUtil.isNotEmpty(gr)) {
//				count = findRechargegive(money, gr.getGrId(),
//						member.getBusid(), card.getCtId());
//				uc.setGiftcount(count);
//			}
//			uc.setUccount(0);
//		} else if (card.getCtId() == 5) {
//			uc.setGivegift("赠送次数");
//			GiveRule gr = findGive(member.getBusid(), card.getGtId(), 5);
//			if (CommonUtil.isNotEmpty(gr)) {
//				int givecount = findRechargegive(money, gr.getGrId(),
//						member.getBusid(), card.getCtId());
//				uc.setGiftcount(givecount);
//			}
//			uc.setUccount(count);
//		}
//		String orderCode = CommonUtil.getMEOrderCode();
//		uc.setOrdercode(orderCode);
//		userConsumeMapper.insertSelective(uc);
//		if (card.getCtId() == 4) {
//			memberGive(orderCode, (byte) 1);
//			findGiveRule(member.getPhone(), orderCode, "充值", (byte) 1);
//		} else {
//			memberGive(orderCode, (byte) 1);
//			card = cardMapper.findCardByCardNo(busId, cardNo);
//			if (card.getCtId() == 5) {
//				String uccount = "";
//				if (CommonUtil.isNotEmpty(uc.getGiftcount())) {
//					uccount = uc.getUccount() + "次,送" + uc.getGiftcount() + "次";
//				} else {
//					uccount = uc.getUccount() + "次";
//				}
//				saveCardRecordNew(uc.getMcid(), (byte) 1, uccount, "充值",
//						member.getBusid(), card.getFrequency().toString(),
//						card.getCtId(), 0.0);
//			} else {
//				if (CommonUtil.isNotEmpty(uc.getGiftcount())
//						&& uc.getGiftcount() > 0) {
//					saveCardRecordNew(uc.getMcid(), (byte) 1, money + "元,送"
//							+ uc.getGiftcount() + "元", "充值", member.getBusid(),
//							card.getMoney().toString(), card.getCtId(), 0.0);
//				} else {
//					saveCardRecordNew(uc.getMcid(), (byte) 1, money + "元",
//							"充值", member.getBusid(),
//							card.getMoney().toString(), card.getCtId(), 0.0);
//				}
//			}
//		}
//
//		// 充值调用微信消息模板
//		if (card.getCtId() == 3) {
//			systemMsgService.sendChuzhiCard(member, money);
//		} else if (card.getCtId() == 5) {
//			systemMsgService.sendCikaCard(member, money, count);
//		}
//
//		map.put("result", true);
//		map.put("message", "充值成功");
//		return map;
//	}
//
//	/**
//	 * 查询赠送信息
//	 *
//	 * @param orderId
//	 * @param payStatus
//	 * @throws Exception
//	 */
//	public void memberGive(String orderId, Byte payStatus) throws Exception {
//		List<Map<String, Object>> ucs = userConsumeMapper
//				.findByOrderCode(orderId);
//		if (CommonUtil.isEmpty(ucs) || ucs.size() == 0 || ucs.size() > 1) {
//			LOG.error("支付查询订单出现异常");
//			return;
//		}
//
//		Integer id = Integer.parseInt(ucs.get(0).get("id").toString());
//		updateUserConsume(id, payStatus);
//		try {
//			if (payStatus == 1) {
//				if (CommonUtil.isNotEmpty(ucs.get(0).get("mcId"))) {
//					Card card = cardMapper.selectByPrimaryKey(Integer
//							.parseInt(ucs.get(0).get("mcId").toString()));
//
//					// 判断是否是会员日
//					MemberDate membetdate = findMemeberDate(card.getBusid(),
//							card.getCtId());
//
//					double balance = 0.0;
//					if (CommonUtil.isNotEmpty(card)
//							&& CommonUtil.isNotEmpty(card.getMoney())) {
//						if (card.getCtId() == 4) {
//							// 时效卡
//							Double totalMoney = Double.parseDouble(ucs.get(0)
//									.get("totalMoney").toString());
//
//							List<Integer> dateCount = findTimeCard(totalMoney,
//									card.getBusid(), membetdate);
//							if (dateCount == null) {
//								throw new Exception();
//							}
//							if (dateCount.size() == 0) {
//								card.setExpiredate(null);
//							} else {
//								Date expireDate = card.getExpiredate();
//								if (expireDate == null) {
//									card.setExpiredate(DateTimeKit
//											.addMonths(dateCount.get(0)));
//								} else {
//									if (DateTimeKit.laterThanNow(card
//											.getExpiredate())) {
//										card.setExpiredate(DateTimeKit
//												.addMonths(expireDate,
//														dateCount.get(0)));
//									} else {
//										card.setExpiredate(DateTimeKit
//												.addMonths(new Date(),
//														dateCount.get(0)));
//									}
//								}
//
//								// 会员日延期多少天
//								if (CommonUtil.isNotEmpty(membetdate)) {
//									card.setExpiredate(DateTimeKit.addDate(
//											card.getExpiredate(),
//											dateCount.get(0)));
//								}
//							}
//						} else {
//							// 次卡和储值卡充值 修改卡片信息
//							balance = card.getMoney();
//							if (CommonUtil.isNotEmpty(ucs.get(0).get(
//									"totalMoney"))) {
//								Double totalMoney = Double.parseDouble(ucs
//										.get(0).get("totalMoney").toString());
//								if (CommonUtil.isNotEmpty(totalMoney)
//										&& totalMoney != 0
//										&& CommonUtil.isNotEmpty(ucs.get(0)
//												.get("giftCount"))) {
//									Double giftCount = Double
//											.parseDouble(ucs.get(0)
//													.get("giftCount")
//													.toString());
//									if (card.getCtId() == 3) {
//										totalMoney = totalMoney + giftCount;
//										card.setMoney(balance + totalMoney);
//									}
//								}
//							}
//
//							if (CommonUtil
//									.isNotEmpty(ucs.get(0).get("uccount"))) {
//								Integer uccount = Integer.parseInt(ucs.get(0)
//										.get("uccount").toString());
//								if (uccount != 0) {
//									Integer giftCount = Integer
//											.parseInt(ucs.get(0)
//													.get("giftCount")
//													.toString());
//									uccount = uccount + giftCount;
//									card.setFrequency(card.getFrequency()
//											+ uccount);
//								}
//							}
//						}
//
//						cardMapper.updateByPrimaryKeySelective(card);
//					}
//				}
//
//			}
//		} catch (Exception e) {
//			LOG.error("发放赠送物品异常");
//			throw new Exception();
//		}
//
//	}
//
//	/**
//	 * 查询时效卡的有效时间
//	 *
//	 * @param money
//	 * @param publicId
//	 * @return
//	 */
//	public List<Integer> findTimeCard(Double money, Integer busId,
//			MemberDate memberDate) {
//		List<Map<String, Object>> giveRules = giveRuleMapper
//				.findByBusIdAndCtId(busId, 4);
//		List<Integer> times = new ArrayList<Integer>();
//		if (giveRules.size() == 0) {
//			return null;
//		}
//		for (Map<String, Object> map : giveRules) {
//			if (CommonUtil.isNotEmpty(map.get("gr_rechargeMoney"))) {
//				Double rechargeMoney = CommonUtil.toDouble(map
//						.get("gr_rechargeMoney"));
//
//				if (money.equals(rechargeMoney)) {
//					times.add(CommonUtil.toInteger(map.get("gr_validDate")));
//					if (CommonUtil.isNotEmpty(memberDate)) {
//						times.add(CommonUtil.toInteger(map.get("delayDay")));
//					}
//					return times;
//				}
//			}
//		}
//		return null;
//	}
//
//	@Override
//	public Map<String, Object> findCardType(Integer memberId) {
//		Map<String, Object> map = new HashMap<String, Object>();
//		map.put("isCz", false);
//		if (CommonUtil.isEmpty(memberId)) {
//			map.put("result", false);
//			map.put("message", "会员id为空");
//			return map;
//		}
//		MemberEntity member = memberMapper.selectByPrimaryKey(memberId);
//		if (CommonUtil.isEmpty(member) || CommonUtil.isEmpty(member.getMcId())) {
//			map.put("result", false);
//			map.put("message", "未找到该会员");
//			return map;
//		}
//		Card card = cardMapper.selectByPrimaryKey(member.getMcId());
//		if (CommonUtil.isEmpty(card)) {
//			map.put("result", false);
//			map.put("message", "未找到该会员");
//			return map;
//		}
//
//		if (card.getCtId() == 2) {
//			MemberDate memberDate = findMemeberDate(member.getBusid(), 2);
//
//			GiveRule giveRule = giveRuleMapper.selectByPrimaryKey(card
//					.getGrId());
//			map.put("result", true);
//			map.put("message", "折扣卡");
//			if (CommonUtil.isNotEmpty(memberDate)) {
//				map.put("memberDate", true);
//				map.put("discount", new Double(giveRule.getGrDiscount()
//						* memberDate.getDiscount()) / 1000);
//			} else {
//				map.put("discount", new Double(giveRule.getGrDiscount()) / 100);
//			}
//			return map;
//		}
//		if (card.getCtId() == 3) {
//			map.put("isCz", true);
//		}
//		map.put("result", true);
//		map.put("message", "非折扣卡");
//		map.put("discount", 1.0);
//		return map;
//	}
//
//	/**
//	 * 会员储值卡消费
//	 */
//	public Map<String, Object> storePay(Integer memberId, double totalMoney) {
//		Map<String, Object> map = new HashMap<String, Object>();
//		MemberEntity member = memberMapper.selectByPrimaryKey(memberId);
//		if (CommonUtil.isEmpty(member.getMcId())) {
//			map.put("result", 0);
//			map.put("message", "非储值会员");
//			return map;
//		}
//		Card card = cardMapper.selectByPrimaryKey(member.getMcId());
//		if (CommonUtil.isNotEmpty(card)) {
//			if (card.getCtId() == 3) {
//				if (card.getMoney() < totalMoney) {
//					map.put("result", 1);
//					map.put("message", "余额不足请充值");
//					return map;
//				}
//
//				double banlan = card.getMoney() - totalMoney;
//				card.setMoney(banlan);
//				cardMapper.updateByPrimaryKey(card);
//				map.put("result", 2);
//				map.put("message", "支付成功");
//				saveCardRecordNew(card.getMcId(), (byte) 1, "-" + totalMoney,
//						"储值卡消费", member.getBusid(), null, 0, -totalMoney);
//				systemMsgService.sendChuzhiXiaofei(member, totalMoney);
//				return map;
//			}
//		}
//
//		map.put("result", 0);
//		map.put("message", "非储值会员");
//		return map;
//
//	}
//
//	/**
//	 * 可供商家选择的会员卡
//	 */
//	@Override
//	public List<CardType> findMemberCard(Integer publicId) {
//		List<CardType> mapList = new ArrayList<CardType>();
//		CardType ct = null;
//
//		List<Map<String, Object>> gradeTypes = gradeTypeMapper
//				.findByPublicId1(publicId);
//		if (CommonUtil.isNotEmpty(gradeTypes) && gradeTypes.size() > 0) {
//			for (Map<String, Object> map : gradeTypes) {
//				ct = new CardType();
//				ct.setCtId(CommonUtil.toInteger(map.get("ctId")));
//				ct.setCtName(CommonUtil.toString(map.get("ctName")));
//				mapList.add(ct);
//			}
//		}
//
//		return mapList;
//	}
//
//	/**
//	 * 可供商家选择的会员卡
//	 */
//	@Override
//	public List<CardType> findMemberCard_1(Integer busId) {
//		List<CardType> mapList = new ArrayList<CardType>();
//		CardType ct = null;
//
//		List<Map<String, Object>> gradeTypes = gradeTypeMapper
//				.findBybusId1(busId);
//		if (CommonUtil.isNotEmpty(gradeTypes) && gradeTypes.size() > 0) {
//			for (Map<String, Object> map : gradeTypes) {
//				ct = new CardType();
//				ct.setCtId(CommonUtil.toInteger(map.get("ctId")));
//				ct.setCtName(CommonUtil.toString(map.get("ctName")));
//				mapList.add(ct);
//			}
//		}
//
//		return mapList;
//	}
//
//	/**
//	 * 判断用户是否是会员 false 不是 true 是
//	 */
//	@Override
//	public boolean isMemember(Integer memberId) {
//		MemberEntity member = memberMapper.selectByPrimaryKey(memberId);
//		if (CommonUtil.isNotEmpty(member)
//				&& CommonUtil.isNotEmpty(member.getMcId())) {
//			Card card = cardMapper.selectByPrimaryKey(member.getMcId());
//			if (card.getIschecked() == 0 || card.getCardstatus() == 1) {
//				return false;
//			} else {
//				return true;
//			}
//		}
//		return false;
//	}
//
//	/**
//	 * 判断用户的卡类型 1积分卡 2折扣卡 3储值卡 4时效卡 5次卡
//	 *
//	 * @param memberId
//	 * @return
//	 */
//	public Integer isCardType(Integer memberId) {
//		if (CommonUtil.isEmpty(memberId)) {
//			return -1;
//		}
//		MemberEntity member = memberMapper.selectByPrimaryKey(memberId);
//		if (CommonUtil.isEmpty(memberId)
//				|| CommonUtil.isEmpty(member.getMcId())) {
//			return -1;
//		}
//		Card card = cardMapper.selectByPrimaryKey(member.getMcId());
//		if (CommonUtil.isEmpty(card)) {
//			return -1;
//		}
//		return card.getCtId();
//	}
//
//	/**
//	 * 获取card信息
//	 *
//	 * @param memberId
//	 * @return
//	 */
//	public Card findCardByMemberId(Integer memberId) {
//		if (CommonUtil.isEmpty(memberId)) {
//			return null;
//		}
//		MemberEntity member = memberMapper.selectByPrimaryKey(memberId);
//		if (CommonUtil.isEmpty(memberId)
//				|| CommonUtil.isEmpty(member.getMcId())) {
//			return null;
//		}
//		Card card = cardMapper.selectByPrimaryKey(member.getMcId());
//		return card;
//	}
//
//	/**
//	 * 根据粉丝id 查询 赠送
//	 */
//	@Override
//	public Map<String, Object> findCardGiveRule(Integer memberId,
//			Double totalMoney) {
//		Map<String, Object> map = new HashMap<String, Object>();
//		if (!isMemember(memberId)) {
//			map.put("result", false);
//			map.put("message", "非会员");
//			return map;
//		}
//
//		// 根据会员id查询赠送规则
//		MemberEntity member = memberMapper.selectByPrimaryKey(memberId);
//		Card card = cardMapper.selectByPrimaryKey(member.getMcId());
//		GiveRule giveRule = giveRuleMapper.selectByPrimaryKey(card.getGrId());
//		switch (card.getCtId()) {
//		case 1:
//			// 积分卡 查询赠送积分
//			GiveRuleGoodsTypeKey key = new GiveRuleGoodsTypeKey();
//			key.setGrId(card.getGrId());
//			key.setGtId(1);
//			GiveRuleGoodsType grgt = giveRuleGoodsTypeMapper
//					.selectByPrimaryKey(key);
//			Double count = totalMoney / grgt.getMoney();
//			Integer number = count.intValue() * grgt.getNumber();
//			map.put("result", true);
//			map.put("ctId", 1);
//			map.put("number", number);
//			return map;
//		case 2:
//			// 查询折扣
//			double discount = giveRule.getGrDiscount() / 100.0;
//			map.put("result", true);
//			map.put("ctId", 2);
//			map.put("discount", discount);
//			map.put("disCountmoney", totalMoney * discount);
//			return map;
//		case 3:
//			// 查询剩余金额
//			map.put("result", true);
//			map.put("ctId", 3);
//			map.put("balance", card.getMoney());
//			return map;
//		default:
//			break;
//		}
//
//		map.put("result", false);
//		map.put("message", "会员,非指定卡类型");
//		return null;
//	}
//
//	/**
//	 * 根据订单号添加赠送物品记录 延迟送
//	 *
//	 * @param orderNo
//	 *            订单号
//	 * @param phone
//	 *            手机号码
//	 * @throws Exception
//	 */
//	@Transactional(rollbackFor = Exception.class)
//	public void findGiveRuleDelay(String phone, String orderNo)
//			throws Exception {
//		List<Map<String, Object>> ucs = userConsumeMapper
//				.findByOrderCode(orderNo);
//		if (CommonUtil.isEmpty(ucs) || ucs.size() == 0 || ucs.size() > 1) {
//			LOG.error("赠送物品查询订单出现异常");
//			throw new Exception();
//		}
//
//		try {
//
//			Integer busUserId = Integer.parseInt(ucs.get(0).get("busUserId")
//					.toString());
//
//			Integer gtId = Integer.parseInt(ucs.get(0).get("gt_id").toString());
//			Integer ctId = Integer.parseInt(ucs.get(0).get("ctId").toString());
//			double price = Double.parseDouble(ucs.get(0).get("totalMoney")
//					.toString());
//
//			// 判断是否是会员日
//			MemberDate memberDate = findMemeberDate(busUserId, ctId);
//			boolean flag = false; // 表示今天是否是会员日
//			if (CommonUtil.isNotEmpty(memberDate)) {
//				flag = true;
//			}
//
//			Integer recFreezeType = 0;
//			switch (ctId) {
//			case 1:
//				recFreezeType = 20;
//				break;
//			case 2:
//				recFreezeType = 21;
//				break;
//			case 3:
//				recFreezeType = 22;
//				break;
//			case 4:
//				recFreezeType = 23;
//				break;
//			case 5:
//				recFreezeType = 24;
//				break;
//
//			default:
//				break;
//			}
//
//			// 查询粉笔数量
//			Integer fenbi = fenbiFlowRecordMapper.getFenbiSurplus(busUserId, 1,
//					recFreezeType, ctId);
//			// 如果是折扣卡 金额用折后金额
//			if (ctId == 2) {
//				price = Double.parseDouble(ucs.get(0).get("discountMoney")
//						.toString());
//			}
//			// 如果是次卡 次数默认为金额
//			if (ctId == 5) {
//				price = Double
//						.parseDouble(ucs.get(0).get("uccount").toString());
//			}
//			Integer ucId = Integer.parseInt(ucs.get(0).get("id").toString());
//			// 如果是次卡 和 储值卡 就查询第一个等级的赠送规则
//			GiveRule gr = null;
//			if (ctId == 5 || ctId == 3) {
//				List<Map<String, Object>> grs = giveRuleMapper
//						.findByBusIdAndCtId(busUserId, ctId);
//				gr = new GiveRule();
//				if (grs.size() != 0) {
//					gr.setGrId(CommonUtil.toInteger(grs.get(0).get("gr_id")));
//				}
//			} else {
//				gr = giveRuleMapper.findBybusIdAndGtIdAndCtId(busUserId, gtId,
//						ctId);
//			}
//
//			if (CommonUtil.isNotEmpty(gr)) {
//				List<Map<String, Object>> grgts = giveRuleGoodsTypeMapper
//						.findByGrId(gr.getGrId());
//				GiveConsume giveConsume = null;
//				GiveRuleGoodsType grgt = null;
//				for (Map<String, Object> map : grgts) {
//					giveConsume = new GiveConsume();
//					if (CommonUtil.isEmpty(map.get("gId")))
//						continue;
//					if ("1".equals(map.get("gId").toString())) {
//						if ("1".equals(map.get("give_type").toString())) {
//							// 积分
//							if (CommonUtil.isEmpty(map.get("money")))
//								continue;
//							Double money = Double.parseDouble(map.get("money")
//									.toString());
//							int count = (int) Math.floor(price / money);
//							if (count == 0)
//								continue;
//							if (CommonUtil.isEmpty(map.get("number")))
//								continue;
//							int num = count
//									* Integer.parseInt(map.get("number")
//											.toString());
//							Integer upperLmit = Integer.parseInt(map.get(
//									"upperLmit").toString());
//							if (upperLmit != 0) {
//
//								num = num > upperLmit ? upperLmit : num;
//							}
//
//							// 会员日赠送翻倍
//							if (flag) {
//								num = num * memberDate.getIntegral();
//							}
//
//							// 添加赠送物品记录
//							giveConsume.setGcTotal(num);
//							giveConsume.setGtId(Integer.parseInt(map.get("gId")
//									.toString()));
//							giveConsume
//									.setGtName(map.get("gt_name").toString());
//							giveConsume
//									.setGtUnit(map.get("gt_unit").toString());
//							giveConsume.setUcId(ucId);
//							giveConsume.setMemberid(CommonUtil.toInteger(ucs
//									.get(0).get("memberId")));
//							giveConsume.setSendtype((byte) 0);
//							giveConsume.setSenddate(new Date());
//							giveConsumeMapper.insertSelective(giveConsume);
//						}
//					} else {
//
//						Integer upperLmit = Integer.parseInt(map.get(
//								"upperLmit").toString());
//
//						if ("2".equals(map.get("gId").toString())
//								|| "3".equals(map.get("gId").toString())
//								|| upperLmit > 0) {
//							Integer count = Integer.parseInt(map.get("number")
//									.toString());
//							Double money = Double.parseDouble(map.get("money")
//									.toString());
//							if (price < money)
//								continue;
//							if (upperLmit < count) {
//								// 扣除商家粉币数量
//								if ("3".equals(map.get("gId").toString())) {
//									// 会员日赠送翻倍
//									if (flag) {
//										count = count
//												* memberDate.getFansCurrency();
//									}
//
//									if (fenbi < count) {
//										continue;
//									}
//									giveConsume.setGcTotal(count);
//									// 冻结商家粉笔数量
//									fenbiFlowRecordMapper.updateFenbiReduce(
//											busUserId, count, ctId,
//											recFreezeType);
//
//								} else if ("2"
//										.equals(map.get("gId").toString())) {
//
//									Integer flowCount = Integer.parseInt(map
//											.get("number").toString());
//									if (flag) {
//										flowCount = flowCount
//												* memberDate.getFlow();
//									}
//
//									giveConsume.setGcTotal(flowCount);
//								}
//								// 上限非等于0 认为是商家自定义物品
//								if (upperLmit != 0) {
//									giveConsume.setGcTotal(upperLmit);
//								}
//							} else {
//								giveConsume.setGcTotal(count);
//							}
//
//							giveConsume.setGtId(Integer.parseInt(map.get("gId")
//									.toString()));
//							giveConsume
//									.setGtName(map.get("gt_name").toString());
//							giveConsume
//									.setGtUnit(map.get("gt_unit").toString());
//							giveConsume.setUcId(ucId);
//							giveConsume.setMemberid(CommonUtil.toInteger(ucs
//									.get(0).get("memberId")));
//							giveConsume.setSenddate(new Date());
//							giveConsume.setSendtype((byte) 0);
//							giveConsumeMapper.insertSelective(giveConsume);
//
//							if (!"2".equals(map.get("gId").toString())
//									&& !"3".equals(map.get("gId").toString())) {
//								// 修改赠送规则物品剩余数量(商家自定义物品)
//								grgt = new GiveRuleGoodsType();
//								grgt.setGrId(Integer.parseInt(map.get("gr_id")
//										.toString()));
//								grgt.setGtId(Integer.parseInt(map.get("gId")
//										.toString()));
//
//								if (upperLmit < count) {
//									grgt.setUpperlmit(0);
//									grgt.setGiveType((byte) 2);
//								} else {
//									grgt.setUpperlmit(upperLmit - count);
//								}
//								giveRuleGoodsTypeMapper
//										.updateByPrimaryKeySelective(grgt);
//							}
//						}
//					}
//				}
//			}
//
//			if (CommonUtil.isNotEmpty(ucs.get(0).get("mcId"))) {
//				Card card = cardMapper.selectByPrimaryKey(Integer.parseInt(ucs
//						.get(0).get("mcId").toString()));
//
//				Double money = CommonUtil.toDouble(ucs.get(0).get(
//						"discountMoney"));
//
//				saveCardRecordNew(CommonUtil.toInteger(ucs.get(0).get("mcId")),
//						(byte) 1, money + "元", "消费", card.getBusid(), card
//								.getMoney().toString(), card.getCtId(), 0.0);
//
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			LOG.error("添加赠送记录数据查询异常异常", e);
//			throw new Exception();
//		}
//	}
//
//	/**
//	 * 支付回调
//	 *
//	 * @param phone
//	 * @param orderId
//	 * @throws Exception
//	 */
//	public void backPay(String phone, String orderCode) throws Exception {
//		List<Map<String, Object>> ucs = userConsumeMapper
//				.findByOrderCode(orderCode);
//		if (CommonUtil.isEmpty(ucs) || ucs.size() == 0 || ucs.size() > 1) {
//			LOG.error("支付回调查询订单出现异常");
//			return;
//		}
//		findGiveRuleDelay(phone, orderCode);
//	}
//
//	/**
//	 * 储值卡退款订单
//	 *
//	 * @param orderCode
//	 *            订单号
//	 */
//	public Map<String, Object> chargeBack(Integer memberId, double refundMoney) {
//		Map<String, Object> map = new HashMap<String, Object>();
//		try {
//			MemberEntity member = memberMapper.selectByPrimaryKey(memberId);
//			if (CommonUtil.isEmpty(member)
//					|| CommonUtil.isEmpty(member.getMcId())) {
//				map.put("result", false);
//				map.put("message", "未找到该会员数据");
//				return map;
//			}
//			Card card = cardMapper.selectByPrimaryKey(member.getMcId());
//			Card card1 = new Card();
//			// 储值卡
//			card1.setMcId(member.getMcId());
//			card1.setMoney(card.getMoney() + refundMoney);
//			cardMapper.updateByPrimaryKeySelective(card1);
//			map.put("result", true);
//			map.put("message", "退款成功");
//
//			saveCardRecordNew(card.getMcId(), (byte) 1, refundMoney + "",
//					"储值卡退款", card.getBusid(), null, 0, 0);
//			systemMsgService.sendChuzhiTuikuan(member, refundMoney);
//
//		} catch (Exception e) {
//			map.put("result", false);
//			map.put("message", "退款异常");
//			e.printStackTrace();
//		}
//
//		return map;
//	}
//
//	/**
//	 * 退款成功 回调 添加card操作记录
//	 */
//	public void refundBack(String orderNo) {
//		UserConsume uc = userConsumeMapper.findByOrderCode1(orderNo);
//		if (CommonUtil.isEmpty(uc) || CommonUtil.isEmpty(uc.getMcid())) {
//			LOG.error("支付回调查询订单出现异常");
//			return;
//		}
//		Card card = cardMapper.selectByPrimaryKey(uc.getMcid());
//		saveCardRecordNew(uc.getMcid(), (byte) 1, uc.getTotalmoney() + "元",
//				"退款", uc.getBususerid(), card.getMoney().toString(),
//				uc.getCtid(), 0.0);
//	}
//
//	/**
//	 * 发送赠送物品给用户
//	 */
//	@Override
//	@Transactional(rollbackFor = Exception.class)
//	public void giveGood(String orderNo) throws Exception {
//		UserConsume uc = userConsumeMapper.findByOrderCode1(orderNo);
//		if (CommonUtil.isEmpty(uc)) {
//			return;
//		}
//		List<GiveConsume> gcs = giveConsumeMapper.findByUcId(uc.getId());
//		MemberEntity member = memberMapper.selectByPrimaryKey(uc.getMemberid());
//		int integral = 0; // 积分
//		int flow = 0;
//		double fanCurrency = 0.0;
//		if (CommonUtil.isNotEmpty(gcs) && gcs.size() > 0) {
//			for (GiveConsume giveConsume : gcs) {
//				switch (giveConsume.getGtId()) {
//				case 1:
//					// 积分赠送
//					integral = giveConsume.getGcTotal();
//					saveCardRecordNew(member.getMcId(), (byte) 2, integral
//							+ "积分", "积分赠送", member.getBusid(),
//							(member.getIntegral() + integral) + "",
//							uc.getCtid(), integral);
//					break;
//				case 2:
//					// 流量赠送
//					flow = giveConsume.getGcTotal();
//					break;
//				case 3:
//					// 粉币赠送
//					fanCurrency = giveConsume.getGcTotal();
//					saveCardRecordNew(member.getMcId(), (byte) 3, fanCurrency
//							+ "粉币", "粉币", member.getBusid(),
//							(member.getFansCurrency() + fanCurrency) + "",
//							uc.getCtid(), fanCurrency);
//					break;
//				default:
//					break;
//				}
//			}
//		}
//
//		if (CommonUtil.isNotEmpty(uc.getMcid())) {
//			Card card = cardMapper.selectByPrimaryKey(CommonUtil.toInteger(uc
//					.getMcid()));
//			if (CommonUtil.isNotEmpty(member)) {
//				member.setFansCurrency(member.getFansCurrency() + fanCurrency);
//				member.setFlow(member.getFlow() + flow);
//				member.setIntegral(member.getIntegral() + integral);
//				member.setFlowdate(new Date());
//				member.setIntegraldate(new Date());
//				member.setTotalintegral(member.getTotalintegral() + integral);
//				member.setTotalmoney(member.getTotalmoney()
//						+ uc.getDiscountmoney());
//				memberMapper.updateByPrimaryKeySelective(member);
//
//			}
//			if (card.getApplytype() != 4) { // 泛会员不升级
//				// 判断会员是否是要升级
//				Map<String, Object> map = findNextGrade(uc.getBususerid(),
//						uc.getCtid(), uc.getGtId(), member.getTotalintegral()
//								+ integral,
//						member.getTotalmoney() + uc.getDiscountmoney());
//
//				// 修改会员卡等级和赠送规则
//				if (CommonUtil.isNotEmpty(map)) {
//					card.setGtId(Integer.parseInt(map.get("gtId").toString()));
//					card.setGrId(Integer.parseInt(map.get("grId").toString()));
//
//					// 升级通知
//					systemMsgService.upgradeMemberMsg(member, card.getCardno(),
//							CommonUtil.isEmpty(card.getExpiredate()) ? "长期有效"
//									: DateTimeKit.format(card.getExpiredate()));
//
//				}
//				cardMapper.updateByPrimaryKeySelective(card);
//			}
//		}
//	}
//
//	/**
//	 * 购买会员卡
//	 */
//	@Override
//	@Transactional(rollbackFor = Exception.class)
//	public SortedMap<Object, Object> buyCard(String url, Integer memberId,
//			Integer ctId, Integer gtId) throws Exception {
//		if (CommonUtil.isEmpty(memberId) || CommonUtil.isEmpty(ctId)) {
//			throw new Exception();
//		}
//		MemberEntity member = memberMapper.selectByPrimaryKey(memberId);
//
//		GradeType gradeTypes = gradeTypeMapper.selectByPrimaryKey(gtId);
//		if (CommonUtil.isEmpty(gradeTypes)
//				|| CommonUtil.isEmpty(gradeTypes.getBuymoney() <= 0)) {
//			throw new Exception();
//		}
//
//		// 添加会员记录
//		UserConsume uc = new UserConsume();
//		uc.setBususerid(member.getBusid());
//		uc.setPublicId(member.getPublicId());
//		uc.setMemberid(memberId);
//		uc.setCtid(ctId);
//		uc.setRecordtype((byte) 2);
//		uc.setUctype((byte) 13);
//		uc.setTotalmoney(gradeTypes.getBuymoney());
//		uc.setCreatedate(new Date());
//		uc.setPaystatus((byte) 0);
//		uc.setDiscount(100);
//		uc.setDiscountmoney(gradeTypes.getBuymoney());
//		uc.setPaymenttype((byte) 1);
//		String orderCode = CommonUtil.getMEOrderCode();
//		uc.setOrdercode(orderCode);
//		uc.setGtId(gtId);
//		// 获取主账户id
//		List<Map<String, Object>> shops = dictService.shopList(busUserMapper
//				.selectByPrimaryKey(member.getBusid()));
//		if (CommonUtil.isNotEmpty(shops) && shops.size() > 0) {
//			uc.setStoreid(CommonUtil.toInteger(shops.get(0).get("id")));
//		}
//		uc.setDatasource((byte) 1);
//
//		userConsumeMapper.insertSelective(uc);
//
//		WxPublicUsersEntity wxPublicUsers = wxPublicUsersMapper
//				.selectByPrimaryKey(member.getPublicId());
//		// 统一下单调用
//		Map<String, Object> params = new HashMap<String, Object>();
//		params.put("appid", wxPublicUsers.getAppid());
//		params.put("mchid", wxPublicUsers.getMchId());
//		params.put("sysOrderNo", orderCode);
//		params.put("key", wxPublicUsers.getApiKey());
//		params.put("productId", orderCode);
//		params.put("desc", "购买会员卡");
//		params.put("totalFee", gradeTypes.getBuymoney());
//		params.put("ip", "127.0.0.1");
//		params.put("openid", member.getOpenid());
//		params.put("url", url);
//		params.put("model", 7);
//		params.put("authRefreshToken", wxPublicUsers.getAuthRefreshToken());
//		SortedMap<Object, Object> sortedMap = wxPayService.memberPay(params);
//		if (CommonUtil.isNotEmpty(sortedMap.get("code"))
//				&& sortedMap.get("code").equals("1")) {
//			sortedMap.put("public_id", member.getPublicId());
//		}
//		return sortedMap;
//	}
//
//	@Override
//	public void buyCardCallBack(String orderId, Byte payStatus) {
//		LOG.error("微信支付回调订单单号 ：" + orderId);
//		List<Map<String, Object>> ucs = userConsumeMapper
//				.findByOrderCode(orderId);
//		if (CommonUtil.isEmpty(ucs) || ucs.size() == 0 || ucs.size() > 1) {
//			LOG.error("支付回调查询订单出现异常");
//			return;
//		}
//		Integer id = Integer.parseInt(ucs.get(0).get("id").toString());
//		UserConsume uc = new UserConsume();
//		uc.setId(id);
//		uc.setPaymenttype((byte)1);
//		uc.setPaystatus(payStatus);
//		userConsumeMapper.updateByPrimaryKeySelective(uc);
//
//		//updateUserConsume(id, payStatus);
//		if (payStatus == 1) {
//			CardBuy cardbuy = new CardBuy();
//			cardbuy.setBuymoney(CommonUtil.toDouble(ucs.get(0).get(
//					"discountMoney")));
//			cardbuy.setCtid(CommonUtil.toInteger(ucs.get(0).get("ctId")));
//			cardbuy.setMemberid(CommonUtil
//					.toInteger(ucs.get(0).get("memberId")));
//			cardbuy.setBusid(CommonUtil.toInteger(ucs.get(0).get("busUserId")));
//			cardBuyMapper.insertSelective(cardbuy);
//
//			// 添加会员卡
//			Card card = new Card();
//			card.setBusid(CommonUtil.toInteger(ucs.get(0).get("busUserId")));
//			card.setIschecked((byte) 1);
//			card.setCardno(CommonUtil.getCode());
//			card.setCtId(CommonUtil.toInteger(ucs.get(0).get("ctId")));
//
//			card.setSystemcode(CommonUtil.getNominateCode());
//			card.setApplytype((byte) 3);
//			card.setMemberid(CommonUtil.toInteger(ucs.get(0).get("memberId")));
//			card.setGtId(CommonUtil.toInteger(ucs.get(0).get("gt_id")));
//			GiveRule giveRule = giveRuleMapper.findBybusIdAndGtIdAndCtId(
//					CommonUtil.toInteger(ucs.get(0).get("busUserId")),
//					card.getGtId(), card.getCtId());
//			card.setGrId(giveRule.getGrId());
//
//			card.setCardno(CommonUtil.getCode());
//
//			card.setPublicId(CommonUtil.toInteger(ucs.get(0).get("public_id")));
//			card.setReceivedate(new Date());
//			card.setIsbinding((byte) 1);
//
//			GradeType gradeType = gradeTypeMapper.selectByPrimaryKey(card
//					.getGtId());
//			if (card.getCtId() == 5) {
//				if (CommonUtil.isNotEmpty(gradeType.getBalance())) {
//					card.setFrequency(new Double(gradeType.getBalance())
//							.intValue());
//				} else {
//					card.setFrequency(0);
//				}
//			} else {
//				if (CommonUtil.isNotEmpty(gradeType.getBalance())) {
//					card.setMoney(new Double(gradeType.getBalance()));
//				} else {
//					card.setMoney(0.0);
//				}
//			}
//
//			cardMapper.insertSelective(card);
//
//			MemberEntity member = new MemberEntity();
//			member.setId(CommonUtil.toInteger(ucs.get(0).get("memberId")));
//			member.setIsbuy((byte) 1);
//			member.setMcId(card.getMcId());
//			memberMapper.updateByPrimaryKeySelective(member);
//			String balance = null;
//			if (card.getCtId() == 5) {
//				balance = card.getFrequency() + "次";
//			} else {
//				balance = card.getMoney() + "元";
//			}
//			saveCardRecordNew(card.getMcId(), (byte) 1,
//					ucs.get(0).get("discountMoney") + "元", "购买会员卡",
//					card.getBusid(), balance, card.getCtId(), 0.0);
//
//			// 新增会员短信通知
//			member = memberMapper.selectByPrimaryKey(CommonUtil.toInteger(ucs
//					.get(0).get("memberId")));
//			systemMsgService.sendNewMemberMsg(member);
//
//
//			socke.sendMessage2("member_" + member.getId(), "");
//
//		}
//	}
//
//	@Transactional(rollbackFor = Exception.class)
//	@Override
//	public Map<String, Object> intergralConsume(int busId, String cardNo,
//			Integer intergral, String gift) throws Exception {
//		Map<String, Object> map = new HashMap<String, Object>();
//		try {
//			Card card = cardMapper.findCardByCardNo(busId, cardNo);
//			if (CommonUtil.isEmpty(card)) {
//				map.put("result", false);
//				map.put("message", "该会员卡不存在!");
//				return map;
//			}
//			MemberEntity member = memberMapper.findByMcIdAndbusId(busId,
//					card.getMcId());
//			Integer jifen = member.getIntegral();
//			if (intergral > jifen) {
//				map.put("result", false);
//				map.put("message", "积分不够!");
//				return map;
//			}
//			MemberEntity mem = new MemberEntity();
//			mem.setId(member.getId());
//			mem.setIntegral(member.getIntegral() - intergral);
//			memberMapper.updateByPrimaryKeySelective(mem);
//			// 添加会员记录
//			UserConsume uc = new UserConsume();
//			uc.setPublicId(member.getPublicId());
//			uc.setBususerid(busId);
//			uc.setMemberid(member.getId());
//			uc.setMcid(card.getMcId());
//			uc.setCtid(card.getCtId());
//			uc.setGtId(card.getGtId());
//			uc.setRecordtype((byte) 0);
//			uc.setTotalmoney(0.0);
//			uc.setCreatedate(new Date());
//			uc.setPaystatus((byte) 1);
//			uc.setUctype((byte) 5);
//			uc.setIntegral(intergral);
//			uc.setFenbi(0);
//			uc.setUccount(0);
//			uc.setDiscount(100);
//			uc.setDiscountmoney(0.0);
//			uc.setOrderid(null);
//			uc.setUctable(null);
//			uc.setCreatedate(new Date());
//			uc.setPaymenttype(null);
//			uc.setGivegift(gift);
//			uc.setGiftcount(1);
//			uc.setOrdercode(null);
//			userConsumeMapper.insertSelective(uc);
//
//			saveCardRecordNew(card.getMcId(), (byte) 2, "-" + intergral,
//					"积分兑换", card.getBusid(), null, 0, -intergral);
//			map.put("result", true);
//			map.put("message", "兑换成功");
//		} catch (Exception e) {
//			e.printStackTrace();
//			LOG.error("积分兑换礼品异常", e);
//			throw new Exception();
//		}
//		return map;
//	}
//
//	@Override
//	public Map<String, Object> updateMemberIntergral(
//			HttpServletRequest request, Integer memberId, Integer intergral) {
//		Map<String, Object> map = new HashMap<String, Object>();
//		try {
//			MemberEntity member = memberMapper.selectByPrimaryKey(memberId);
//			Integer mIntergral = member.getIntegral();
//			if (mIntergral < -intergral) {
//				map.put("result", 1);
//				map.put("message", "积分不足");
//				return map;
//			}
//			MemberEntity member1 = new MemberEntity();
//			member1.setId(member.getId());
//			member1.setIntegral(member.getIntegral() + intergral);
//			memberMapper.updateByPrimaryKeySelective(member1);
//
//			if (CommonUtil.isNotEmpty(request)) {
//				member.setIntegral(member1.getIntegral());
//				CommonUtil.setLoginMember(request, member);
//			}
//			if (CommonUtil.isNotEmpty(member.getMcId())) {
//				saveCardRecordNew(member.getMcId(), (byte) 2, intergral + "积分",
//						"积分", member.getBusid(), "", null, intergral);
//			}
//			map.put("result", 2);
//			map.put("message", "积分支付成功");
//		} catch (Exception e) {
//			LOG.error("积分支付失败", e);
//			map.put("result", 1);
//			map.put("message", "积分支付异常");
//		}
//		return map;
//
//	}
//
//	@Override
//	public Map<String, Object> findBuyCard(MemberEntity member) {
//		Map<String, Object> map = new HashMap<String, Object>();
//		if (CommonUtil.isEmpty(member.getMcId())) {
//			// 判断该用户是否已购买
//			if (1 == member.getIsbuy()) {
//				String url = "/phoneMemberController/" + member.getPublicId()
//						+ "/79B4DE7C/findMember_1.do";
//				map.put("code", "-1");
//				map.put("msg", "已购买会员卡");
//				map.put("url", url);
//			} else {
//				String url = "/phoneMemberController/" + member.getPublicId()
//						+ "/79B4DE7C/findMember_1.do";
//				map.put("code", "1");
//				map.put("msg", "未购买会员卡");
//				map.put("url", url);
//			}
//		} else {
//			map.put("code", "-2");
//			map.put("msg", "未购买会员卡");
//		}
//		return map;
//	}
//
//	@Override
//	public Map<String, Object> buyCard(MemberEntity member, Double money, Integer ctId) {
//		Map<String, Object> map = new HashMap<String, Object>();
//		try {
//			CardBuy cardbuy = new CardBuy();
//			cardbuy.setBuymoney(money);
//			cardbuy.setPublicid(member.getPublicId());
//			cardbuy.setCtid(ctId);
//			cardbuy.setMemberid(member.getId());
//			cardBuyMapper.insertSelective(cardbuy);
//			MemberEntity m = new MemberEntity();
//			m.setId(member.getId());
//			m.setIsbuy((byte) 1);
//			memberMapper.updateByPrimaryKeySelective(m);
//			map.put("code", 1);
//			map.put("msg", "购买成功");
//		} catch (Exception e) {
//			map.put("code", -1);
//			map.put("msg", "购买异常");
//		}
//		return map;
//	}
//
//	@Override
//	public Map<String, Object> returnfansCurrency(Integer busId,
//			Double fans_currency) {
//		Map<String, Object> map = new HashMap<String, Object>();
//		try {
//			BusUserEntity busUser = busUserMapper.selectByPrimaryKey(busId);
//			BusUserEntity busUser1 = new BusUserEntity();
//			busUser1.setId(busId);
//			busUser1.setFansCurrency(busUser.getFansCurrency() + fans_currency);
//			busUserMapper.updateByPrimaryKeySelective(busUser1);
//			map.put("result", true);
//			map.put("message", "归还商户粉币成功");
//		} catch (Exception e) {
//			map.put("result", false);
//			map.put("message", "归还商户粉币异常");
//			LOG.error("归还商户粉币异常", e);
//		}
//		return map;
//	}
//
//	/**
//	 * 根据订单号添加赠送物品记录 （只添加记录 不做操作）
//	 *
//	 * @param orderId
//	 *            订单号
//	 * @param itemName
//	 *            物品名称
//	 *
//	 * @throws Exception
//	 */
//	@Transactional(rollbackFor = Exception.class)
//	public void saveGiveConsume(String phone, String orderId) throws Exception {
//		List<Map<String, Object>> ucs = userConsumeMapper
//				.findByOrderCode(orderId);
//		if (CommonUtil.isEmpty(ucs) || ucs.size() == 0 || ucs.size() > 1) {
//			LOG.error("赠送物品查询订单出现异常");
//			throw new Exception();
//		}
//		try {
//			Integer busUserId = Integer.parseInt(ucs.get(0).get("busUserId")
//					.toString());
//			Integer gtId = Integer.parseInt(ucs.get(0).get("gt_id").toString());
//			Integer ctId = Integer.parseInt(ucs.get(0).get("ctId").toString());
//
//			// 判断是否是会员日
//			MemberDate memberDate = findMemeberDate(busUserId, ctId);
//
//			boolean flag = false; // 表示今天是否是会员日
//			if (CommonUtil.isNotEmpty(memberDate)) {
//				flag = true;
//			}
//
//			double price = Double.parseDouble(ucs.get(0).get("discountMoney")
//					.toString());
//
//			Integer recFreezeType = 0;
//			switch (ctId) {
//			case 1:
//				recFreezeType = 20;
//				break;
//			case 2:
//				recFreezeType = 21;
//				break;
//			case 3:
//				recFreezeType = 22;
//				break;
//			case 4:
//				recFreezeType = 23;
//				break;
//			case 5:
//				recFreezeType = 24;
//				break;
//
//			default:
//				break;
//			}
//
//			// 查询粉笔数量
//			Integer fenbi = fenbiFlowRecordMapper.getFenbiSurplus(busUserId, 1,
//					recFreezeType, ctId);
//			// 如果是折扣卡 金额用折后金额
//			if (ctId == 2) {
//				price = Double.parseDouble(ucs.get(0).get("discountMoney")
//						.toString());
//			}
//			// 如果是次卡 次数默认为金额
//			if (ctId == 5) {
//				price = Double
//						.parseDouble(ucs.get(0).get("uccount").toString());
//			}
//			Integer ucId = Integer.parseInt(ucs.get(0).get("id").toString());
//			// 如果是次卡 和 储值卡 就查询第一个等级的赠送规则
//			GiveRule gr = null;
//			if (ctId == 5 || ctId == 3) {
//				List<Map<String, Object>> grs = giveRuleMapper
//						.findByBusIdAndCtId(busUserId, ctId);
//				gr = new GiveRule();
//				if (grs.size() != 0) {
//					gr.setGrId(CommonUtil.toInteger(grs.get(0).get("gr_id")));
//				}
//			} else {
//				gr = giveRuleMapper.findBybusIdAndGtIdAndCtId(busUserId, gtId,
//						ctId);
//			}
//			if (CommonUtil.isNotEmpty(gr)) {
//				List<Map<String, Object>> grgts = giveRuleGoodsTypeMapper
//						.findByGrId(gr.getGrId());
//				GiveConsume giveConsume = null;
//				GiveRuleGoodsType grgt = null;
//				for (Map<String, Object> map : grgts) {
//					giveConsume = new GiveConsume();
//					if (CommonUtil.isEmpty(map.get("gId")))
//						continue;
//					if ("1".equals(map.get("gId").toString())) {
//						if ("1".equals(map.get("give_type").toString())) {
//							// 积分
//							if (CommonUtil.isEmpty(map.get("money")))
//								continue;
//							Double money = Double.parseDouble(map.get("money")
//									.toString());
//							int count = (int) Math.floor(price / money);
//							if (count == 0)
//								continue;
//							if (CommonUtil.isEmpty(map.get("number")))
//								continue;
//
//							int num = count
//									* Integer.parseInt(map.get("number")
//											.toString());
//							Integer upperLmit = Integer.parseInt(map.get(
//									"upperLmit").toString());
//							if (upperLmit != 0) {
//								num = num > upperLmit ? upperLmit : num;
//							}
//							// 会员日赠送翻倍
//							if (flag) {
//								num = num * memberDate.getIntegral();
//							}
//
//							giveConsume.setGcTotal(num);
//							giveConsume.setGtId(Integer.parseInt(map.get("gId")
//									.toString()));
//							giveConsume
//									.setGtName(map.get("gt_name").toString());
//							giveConsume
//									.setGtUnit(map.get("gt_unit").toString());
//							giveConsume.setUcId(ucId);
//							giveConsume.setMemberid(CommonUtil.toInteger(ucs
//									.get(0).get("memberId")));
//							giveConsume.setSenddate(new Date());
//							giveConsumeMapper.insertSelective(giveConsume);
//						}
//					} else {
//						// 添加赠送物品记录
//						Integer upperLmit = Integer.parseInt(map.get(
//								"upperLmit").toString());
//
//						if ("2".equals(map.get("gId").toString())
//								|| "3".equals(map.get("gId").toString())
//								|| upperLmit > 0) {
//							Integer count = Integer.parseInt(map.get("number")
//									.toString());
//							Double money = Double.parseDouble(map.get("money")
//									.toString());
//							if (price < money)
//								continue;
//							if (upperLmit < count) {
//								// 扣除商家粉币数量
//								if ("3".equals(map.get("gId").toString())) {
//									// 会员日赠送翻倍
//									if (flag) {
//										count = count
//												* memberDate.getFansCurrency();
//									}
//
//									if (fenbi < count) {
//										continue;
//									}
//
//									giveConsume.setGcTotal(count);
//									// 冻结商家粉笔数量
//									fenbiFlowRecordMapper.updateFenbiReduce(
//											busUserId, count, ctId,
//											recFreezeType);
//								} else if ("2"
//										.equals(map.get("gId").toString())) {
//									Integer flowCount = Integer.parseInt(map
//											.get("number").toString());
//
//									if (flag) {
//										flowCount = flowCount
//												* memberDate.getFlow();
//									}
//
//									giveConsume.setGcTotal(flowCount);
//
//								}
//								// 上限非等于0 认为是商家自定义物品
//								if (upperLmit != 0) {
//									giveConsume.setGcTotal(upperLmit);
//								}
//							} else {
//								giveConsume.setGcTotal(count);
//							}
//
//							giveConsume.setGtId(Integer.parseInt(map.get("gId")
//									.toString()));
//							giveConsume
//									.setGtName(map.get("gt_name").toString());
//							giveConsume
//									.setGtUnit(map.get("gt_unit").toString());
//							giveConsume.setUcId(ucId);
//							giveConsume.setMemberid(CommonUtil.toInteger(ucs
//									.get(0).get("memberId")));
//							giveConsume.setSenddate(new Date());
//							giveConsumeMapper.insertSelective(giveConsume);
//
//							if (!"2".equals(map.get("gId").toString())
//									&& !"3".equals(map.get("gId").toString())) {
//								// 修改赠送规则物品剩余数量(商家自定义物品)
//								grgt = new GiveRuleGoodsType();
//								grgt.setGrId(Integer.parseInt(map.get("gr_id")
//										.toString()));
//								grgt.setGtId(Integer.parseInt(map.get("gId")
//										.toString()));
//
//								if (upperLmit < count) {
//									grgt.setUpperlmit(0);
//									grgt.setGiveType((byte) 2);
//								} else {
//									grgt.setUpperlmit(upperLmit - count);
//								}
//								giveRuleGoodsTypeMapper
//										.updateByPrimaryKeySelective(grgt);
//							}
//						}
//					}
//				}
//			}
//		} catch (Exception e) {
//			LOG.error("会员赠送物品异常", e);
//		}
//	}
//
//	/**
//	 * 判断是否是会员日
//	 */
//	public MemberDate findMemeberDate(Integer busId, Integer ctId) {
//		try {
//			GradeType gradeType = gradeTypeMapper.findIsmemberDateByCtId(busId,
//					ctId);
//			if (CommonUtil.isEmpty(gradeType)
//					|| gradeType.getIsmemberdate() == 1) {
//				return null;
//			}
//			// 未设置会员日
//			MemberDate memberdate = memberDateMapper.findByBusIdAndCtId(busId,
//					ctId);
//			if (CommonUtil.isEmpty(memberdate)) {
//				return null;
//			}
//			switch (memberdate.getDatetype()) {
//			case 0:
//				int d = DateTimeKit.getNow().getDay();
//				Integer day = 0;
//				if (d == 0) {
//					day = 7;
//				} else if (d == 1) {
//					day = 1;
//				} else if (d == 2) {
//					day = 2;
//				} else if (d == 3) {
//					day = 3;
//				} else if (d == 4) {
//					day = 4;
//				} else if (d == 5) {
//					day = 5;
//				} else if (d == 6) {
//					day = 6;
//				}
//				if (day == CommonUtil.toInteger(memberdate.getDatestr())) {
//					return memberdate;
//				}
//				break;
//			case 1:
//				Integer date = DateTimeKit.getNow().getDate();
//				if (date == CommonUtil.toInteger(memberdate.getDatestr())) {
//					return memberdate;
//				}
//				break;
//			case 2:
//				if (DateTimeKit.isSameDay(new Date(),
//						DateTimeKit.parseDate(memberdate.getDatestr()))) {
//					return memberdate;
//				}
//				break;
//			case 3:
//				// 区间
//				String dateStr = memberdate.getDatestr();
//				List<Map<String, Object>> list = JSONArray.toList(
//						JSONArray.fromObject(dateStr), Map.class);
//				Integer year = DateTimeKit.getYear(new Date());
//				for (Map<String, Object> map : list) {
//					String time = CommonUtil.toString(map.get("time"));
//					if (time.length() == 1) {
//						time = "0" + time;
//					}
//					String time1 = CommonUtil.toString(map.get("time1"));
//					if (time1.length() == 1) {
//						time1 = "0" + time1;
//					}
//					String time2 = CommonUtil.toString(map.get("time2"));
//					if (time2.length() == 1) {
//						time2 = "0" + time2;
//					}
//					String time3 = CommonUtil.toString(map.get("time3"));
//					if (time3.length() == 1) {
//						time3 = "0" + time3;
//					}
//
//					String date1 = year + "-" + time + "-" + time1
//							+ " 00:00:00";
//					String date2 = year + "-" + time2 + "-" + time3
//							+ " 23:59:59";
//					Date d1 = DateTimeKit.parse(date1, "yyyy-MM-dd HH:mm:ss");
//					Date d2 = DateTimeKit.parse(date2, "yyyy-MM-dd HH:mm:ss");
//					if (isBetween(d1, d2)) {
//						return memberdate;
//					}
//				}
//				break;
//			default:
//				break;
//			}
//			return null;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
//
//	/**
//	 * 比较当前时间是否在两者之间
//	 *
//	 * @param date1
//	 *            开始时间
//	 * @param date2
//	 *            结束时间
//	 * @return
//	 */
//	public boolean isBetween(Date date1, Date date2) {
//		Date date = new Date();
//		if (date.getTime() >= date1.getTime()
//				&& date.getTime() <= date2.getTime()) {
//			return true;
//		} else {
//			return false;
//		}
//	}
//
//	@Transactional(rollbackFor = Exception.class)
//	@Override
//	public Map<String, Object> reduceFansCurrencyMoney(
//			HttpServletRequest request, MemberEntity member, Integer busId,
//			Double fenbimoney) throws Exception {
//		Map<String, Object> map = new HashMap<String, Object>();
//		try {
//			int fenbi = 0;
//			if (fenbimoney > 0) {
//				Map<String, Object> dict = dictService.getDict("1058");
//				fenbi = new Double(fenbimoney
//						* CommonUtil.toDouble(dict.get("1"))).intValue();
//			} else {
//				map.put("code", "-1");
//				map.put("message", "未存在粉币扣除");
//				return map;
//			}
//
//			MemberEntity m = new MemberEntity();
//			m.setId(member.getId());
//			m.setFansCurrency(member.getFansCurrency() - fenbi);
//			memberMapper.updateByPrimaryKeySelective(m);
//
//			if (CommonUtil.isNotEmpty(request)) {
//				member.setFansCurrency(member.getFansCurrency() - fenbi);
//				CommonUtil.setLoginMember(request, member);
//			}
//			if (CommonUtil.isNotEmpty(member.getMcId())) {
//				saveCardRecordNew(member.getMcId(), (byte) 3, "-" + fenbi
//						+ "粉币", "粉币", member.getBusid(), "", null, -fenbi);
//			}
//
//			BusUserEntity busUser = busUserMapper.selectByPrimaryKey(busId);
//			BusUserEntity busUser1 = new BusUserEntity();
//			busUser1.setId(busId);
//			busUser1.setFansCurrency(busUser.getFansCurrency() + fenbi);
//			busUserMapper.updateByPrimaryKeySelective(busUser1);
//
//			map.put("code", 2);
//			map.put("message", "粉币扣除成功");
//		} catch (Exception e) {
//			LOG.error("粉币抵扣异常", e);
//			throw new Exception();
//		}
//		return map;
//	}
//
//	@Transactional(rollbackFor = Exception.class)
//	@Override
//	public Map<String, Object> reduceFansCurrency(HttpServletRequest request,
//			MemberEntity member, Integer busId, Double fenbi) throws Exception {
//		Map<String, Object> map = new HashMap<String, Object>();
//		try {
//			if (member.getFansCurrency() < fenbi) {
//				map.put("result", "1");
//				map.put("message", "粉币不足");
//				return map;
//			}
//			MemberEntity m = new MemberEntity();
//			m.setId(member.getId());
//			m.setFansCurrency(member.getFansCurrency() - fenbi);
//			memberMapper.updateByPrimaryKeySelective(m);
//
//			if (CommonUtil.isNotEmpty(request)) {
//				member.setFansCurrency(member.getFansCurrency() - fenbi);
//				CommonUtil.setLoginMember(request, member);
//			}
//			if (CommonUtil.isNotEmpty(member.getMcId())) {
//				saveCardRecordNew(member.getMcId(), (byte) 3, "-" + fenbi
//						+ "粉币", "粉币", member.getBusid(), "", null, -fenbi);
//			}
//
//			BusUserEntity busUser = busUserMapper.selectByPrimaryKey(busId);
//			BusUserEntity busUser1 = new BusUserEntity();
//			busUser1.setId(busId);
//			busUser1.setFansCurrency(busUser.getFansCurrency() + fenbi);
//			busUserMapper.updateByPrimaryKeySelective(busUser1);
//
//			map.put("result", 2);
//			map.put("message", "粉币扣除成功");
//		} catch (Exception e) {
//			LOG.error("粉币抵扣异常", e);
//			throw new Exception();
//		}
//		return map;
//	}
//
//	@Transactional(rollbackFor = Exception.class)
//	@Override
//	public GradeType findGradeType(Integer memberId) {
//		MemberEntity member = memberMapper.selectByPrimaryKey(memberId);
//		if (CommonUtil.isEmpty(member) || CommonUtil.isEmpty(member.getMcId())) {
//			return null;
//		}
//		Integer mcId = member.getMcId();
//		Card card = cardMapper.selectByPrimaryKey(mcId);
//		GradeType gradeType = gradeTypeMapper
//				.selectByPrimaryKey(card.getGtId());
//		return gradeType;
//	}
//
//	@Override
//	public Map<String, Object> updateMemberFansCurrency(
//			HttpServletRequest request, Integer memberId, Integer busId,
//			Double fenbi) throws Exception {
//		Map<String, Object> map = new HashMap<String, Object>();
//		try {
//			MemberEntity member = memberMapper.selectByPrimaryKey(memberId);
//			if (member.getFansCurrency() < -fenbi) {
//				map.put("result", "1");
//				map.put("message", "粉币不足");
//				return map;
//			}
//			BusUserEntity busUser = busUserMapper.selectByPrimaryKey(busId);
//
//			MemberEntity m = new MemberEntity();
//			m.setId(member.getId());
//			m.setFansCurrency(member.getFansCurrency() + fenbi);
//			memberMapper.updateByPrimaryKeySelective(m);
//
//			if (CommonUtil.isNotEmpty(request)) {
//				member.setFansCurrency(member.getFansCurrency() + fenbi);
//				CommonUtil.setLoginMember(request, member);
//			}
//
//			if (CommonUtil.isNotEmpty(member.getMcId())) {
//				if (fenbi > 0) {
//					saveCardRecordNew(member.getMcId(), (byte) 3, fenbi + "粉币",
//							"粉币赠送", member.getBusid(), "", null, fenbi);
//				} else {
//					saveCardRecordNew(member.getMcId(), (byte) 3, fenbi + "粉币",
//							"粉币扣除", member.getBusid(), "", null, -fenbi);
//				}
//
//			}
//
//			BusUserEntity busUser1 = new BusUserEntity();
//			busUser1.setId(busId);
//			busUser1.setFansCurrency(busUser.getFansCurrency() - fenbi);
//			busUserMapper.updateByPrimaryKeySelective(busUser1);
//			map.put("result", 2);
//			map.put("message", "操作成功");
//		} catch (Exception e) {
//			LOG.error("粉币操作异常", e);
//			throw new Exception();
//		}
//		return map;
//	}
//
//	/**
//	 * 粉币计算
//	 *
//	 * @param totalMoney
//	 *            能抵抗消费金额
//	 * @param fans_currency
//	 *            粉币值
//	 * @param ratio
//	 *            比例 字典表1058 Map集合key为1
//	 * @return 返回兑换金额
//	 */
//	@Override
//	public Double currencyCount(Double totalMoney, Double fans_currency) {
//		try {
//			Map<String, Object> dict = dictService.getDict("1058");
//			Double ratio = CommonUtil.toDouble(dict.get("1"));
//			if (fans_currency < ratio * 10) {
//				return 0.0;
//			}
//			Integer money = new Double(fans_currency / ratio / 10).intValue();
//			if (CommonUtil.isEmpty(totalMoney) || totalMoney == 0) {
//				return new Double(money * 10);
//			} else {
//				if (totalMoney >= money * 10) {
//					return new Double(money * 10);
//				} else {
//					return totalMoney;
//				}
//			}
//		} catch (Exception e) {
//			LOG.error("计算粉币抵扣异常");
//			e.printStackTrace();
//		}
//		return 0.0;
//	}
//
//	@Override
//	public Double deductFenbi(Double jifenMoney, int busId) {
//		Map<String, Object> dict = dictService.getDict("1058");
//		Double ratio = CommonUtil.toDouble(dict.get("1"));
//		Double fenbi = jifenMoney * ratio;
//		return fenbi;
//	}
//
//	@Override
//	public Integer deductJifen(Double jifenMoney, int busId) {
//		PublicParameterSet pps = publicParameterSetMapper.findBybusId(busId);
//		if (CommonUtil.isEmpty(pps)) {
//			return 0;
//		}
//		Integer jifen = new Double(jifenMoney / pps.getChangemoney()
//				* pps.getIntegralratio()).intValue();
//		return jifen;
//	}
//
//	/**
//	 * 积分计算
//	 *
//	 * @param totalMoney
//	 *            能抵抗消费金额
//	 * @param integral
//	 *            积分
//	 * @param ps
//	 * @return
//	 */
//	@Override
//	public Double integralCount(Double totalMoney, Double integral, int busId) {
//		try {
//			PublicParameterSet ps = publicParameterSetMapper.findBybusId(busId);
//			if (CommonUtil.isEmpty(ps)) {
//				return 0.0;
//			}
//			double startMoney = ps.getStartmoney();
//			double integralratio = ps.getIntegralratio();
//			double changMoney = ps.getChangemoney();
//			if (integralratio <= 0) {
//				return 0.0;
//			}
//
//			// 积分启兑
//			double integralNum = startMoney * integralratio;
//			if (integral < integralNum) {
//				return 0.0;
//			}
//
//			if (CommonUtil.isNotEmpty(totalMoney)) {
//				// 订单金额小于订单启兑金额
//				if (totalMoney < changMoney) {
//					return 0.0;
//				}
//				Integer money = new Double(integral / integralratio).intValue();
//				if (totalMoney >= money) {
//					return new Double(money);
//				} else {
//					return totalMoney;
//				}
//			} else {
//				Integer money = new Double(integral / integralratio).intValue();
//				return new Double(money);
//			}
//		} catch (Exception e) {
//			LOG.error("计算积分抵扣异常");
//			e.printStackTrace();
//		}
//		return 0.0;
//	}
//
//	/**
//	 * 会员卡充值
//	 */
//	@Override
//	@Transactional(rollbackFor = Exception.class)
//	public Map<String, Object> rechargeMember(Integer memberId, String cardNo,
//			Double money, Integer count) throws Exception {
//		if (CommonUtil.isEmpty(memberId) || CommonUtil.isEmpty(cardNo)
//				|| CommonUtil.isEmpty(money)) {
//			throw new Exception();
//		}
//		try {
//			MemberEntity member = memberMapper.selectByPrimaryKey(memberId);
//			Card card = cardMapper.findCardByCardNo(member.getBusid(), cardNo);
//			if (CommonUtil.isEmpty(card)) {
//				throw new Exception();
//			}
//			// 添加会员记录
//			UserConsume uc = new UserConsume();
//			uc.setPublicId(member.getPublicId());
//			uc.setBususerid(member.getBusid());
//			uc.setMemberid(memberId);
//			uc.setMcid(card.getMcId());
//			uc.setCtid(card.getCtId());
//			uc.setGtId(card.getGtId());
//			uc.setRecordtype((byte) 1);
//			uc.setUctype((byte) 7);
//			uc.setTotalmoney(money);
//			uc.setCreatedate(new Date());
//			uc.setPaystatus((byte) 0);
//			uc.setDiscount(100);
//			uc.setDiscountmoney(money);
//			uc.setPaymenttype((byte) 0);
//			uc.setDatasource((byte) 2);
//			uc.setIschongzhi((byte) 1);
//			if (card.getCtId() == 3) {
//				uc.setGivegift("赠送金额");
//				GiveRule gr = findGive(member.getBusid(), card.getGtId(), 3);
//				if (CommonUtil.isNotEmpty(gr)) {
//					count = findRechargegive(money, gr.getGrId(),
//							member.getBusid(), card.getCtId());
//					uc.setGiftcount(count);
//				}
//				uc.setUccount(0);
//			} else if (card.getCtId() == 5) {
//				uc.setGivegift("赠送次数");
//				GiveRule gr = findGive(member.getBusid(), card.getGtId(), 5);
//				if (CommonUtil.isNotEmpty(gr)) {
//					int givecount = findRechargegive(money, gr.getGrId(),
//							member.getBusid(), card.getCtId());
//					uc.setGiftcount(givecount);
//				}
//				uc.setUccount(count);
//			}
//
//			// 获取主账户id
//			List<Map<String, Object>> shops = dictService
//					.shopList(busUserMapper.selectByPrimaryKey(member
//							.getBusid()));
//			if (CommonUtil.isNotEmpty(shops) && shops.size() > 0) {
//				uc.setStoreid(CommonUtil.toInteger(shops.get(0).get("id")));
//			}
//
//			String orderCode = CommonUtil.getMEOrderCode();
//			uc.setOrdercode(orderCode);
//			userConsumeMapper.insertSelective(uc);
//			Map<String, Object> params = new HashMap<String, Object>();
//			String red_url = "/alipay/79B4DE7C/alipayApi.do?out_trade_no="
//					+ orderCode
//					+ "&subject=会员卡充值&model=2&businessUtilName=alipayNotifyUrlBuinessServiceChargeCard&total_fee="
//					+ money + "&busId=" + member.getBusid() + "&return_url="
//					+ PropertiesUtil.getWebHomeUrl()
//					+ "/phoneMemberController/79B4DE7C/recharge.do?id="
//					+ member.getId();
//			params.put("result", true);
//			params.put("message", "未支付");
//			params.put("red_url", red_url);
//			return params;
//		} catch (Exception e) {
//			LOG.error("会员卡支付宝充值异常", e);
//			throw new Exception();
//		}
//	}
//
//	@Override
//	public Map<String, Object> findMember(String openId,Integer busId) {
//		Map<String, Object> returnMap = new HashMap<String, Object>();
//		MemberEntity member = memberMapper.selectByOpenidAndBusId(openId, busId);
//		if (CommonUtil.isEmpty(member) || CommonUtil.isEmpty(member.getMcId())) {
//			returnMap.put("result", 0);
//			returnMap.put("message", "非会员");
//			return returnMap;
//		}
//
//		Card card = cardMapper.selectByPrimaryKey(member.getMcId());
//		if (card.getIschecked() == 0 || card.getCardstatus() == 1) {
//			returnMap.put("result", 0);
//			returnMap.put("message", "非会员");
//			return returnMap;
//		}
//		// 会员查看粉币抵扣金额和积分抵扣金额
//		Double jifenMoney = integralCount(null, member.getIntegral()
//				.doubleValue(), member.getBusid());
//		returnMap.put("jifenMoney", jifenMoney);
//
//		Double fenbiMoney = currencyCount(null, member.getFansCurrency());
//
//		returnMap.put("fenbiMoney", fenbiMoney);
//
//		if (card.getCtId() == 2) {
//			MemberDate memberDate = findMemeberDate(member.getPublicId(), 2);
//
//			GiveRule giveRule = giveRuleMapper.selectByPrimaryKey(card
//					.getGrId());
//			if (CommonUtil.isNotEmpty(memberDate)) {
//				returnMap.put("memberDate", true);
//				returnMap.put("discount", new Double(giveRule.getGrDiscount()
//						* memberDate.getDiscount()) / 1000);
//			} else {
//				returnMap.put("discount",
//						new Double(giveRule.getGrDiscount()) / 100);
//			}
//		}
//		GradeType gt = gradeTypeMapper.selectByPrimaryKey(card.getGtId());
//		returnMap.put("dengji", gt.getGtGradeName());
//		switch (card.getCtId()) {
//		case 1:
//			returnMap.put("leixing", "积分卡");
//			break;
//		case 2:
//			returnMap.put("leixing", "折扣卡");
//			break;
//		case 3:
//			returnMap.put("leixing", "储值卡");
//			break;
//		case 4:
//			returnMap.put("leixing", "时效卡");
//			break;
//		case 5:
//			returnMap.put("leixing", "次卡");
//			break;
//
//		default:
//			break;
//		}
//		returnMap.put("member", member);
//		returnMap.put("result", 1);
//		returnMap.put("card", card);
//		return returnMap;
//	}
//
//	@Transactional(rollbackFor = Exception.class)
//	@Override
//	public Map<String, Object> weChatPayment(String orderCode, String openId,
//			Double orderMoney, Integer payType, Double fenbiMoney,
//			Double jifenMoney,Integer busId) throws Exception {
//		Map<String, Object> returnMap = new HashMap<String, Object>();
//		try {
//			MemberEntity member = memberMapper.selectByOpenidAndBusId(openId, busId);
//			UserConsume uc = new UserConsume();
//			// 添加会员记录
//			Card card = null;
//			if (isMemember(member.getId())) {
//				card = cardMapper.selectByPrimaryKey(member.getMcId());
//				uc.setMcid(member.getMcId());
//				uc.setCtid(card.getCtId());
//				uc.setGtId(card.getGtId());
//				uc.setDiscount(100);
//			}
//			Double pay = orderMoney;
//			// 会员卡 折扣卡
//			if (CommonUtil.isNotEmpty(card) && card.getCtId() == 2) {
//				// 查询赠送规则
//				GiveRule gr = giveRuleMapper.selectByPrimaryKey(card.getGrId());
//				uc.setDiscount(gr.getGrDiscount());
//				pay = pay * gr.getGrDiscount() / 100;
//			}
//			// 粉币
//			if (fenbiMoney > 0) {
//				// 粉币抵消
//				Map<String, Object> dict = dictService.getDict("1058");
//				Double fenbi = CommonUtil.toDouble(dict.get("1")) * fenbiMoney;
//				uc.setFenbi(fenbi);
//				if (fenbiMoney >= pay) {
//					pay = 0.0;
//				} else {
//					pay = reduce(pay - fenbiMoney);
//				}
//			}
//			// 积分
//			if (jifenMoney > 0) {
//				// 积分抵扣
//				PublicParameterSet ps = publicParameterSetMapper
//						.findByPublicId(member.getPublicId());
//				int integralNum = new Double(jifenMoney / ps.getChangemoney()
//						* ps.getIntegralratio()).intValue();
//				uc.setIntegral(-integralNum);
//				if (jifenMoney >= pay) {
//					pay = 0.0;
//				} else {
//					pay = reduce(pay - jifenMoney);
//				}
//			}
//			WxPublicUsersEntity wxPublicUsers = wxPublicUsersMapper
//					.selectByPrimaryKey(member.getPublicId());
//			uc.setBususerid(wxPublicUsers.getBusUserId());
//			uc.setPublicId(member.getPublicId());
//			uc.setMemberid(member.getId());
//			uc.setRecordtype((byte) 2);
//			// uc.setStoreid(CommonUtil.toInteger(obj.get("shopid")));
//			uc.setUctype((byte) 19);
//			uc.setTotalmoney(orderMoney);
//			uc.setCreatedate(new Date());
//			uc.setPaystatus((byte) 0);
//			uc.setDiscountmoney(pay);
//			uc.setPaymenttype((byte) 1);
//			uc.setOrdercode(orderCode);
//			uc.setDatasource((byte) 1);
//			if (pay <= 0) {
//				uc.setPaystatus((byte) 1);
//			} else {
//				uc.setPaystatus((byte) 0);
//			}
//			if (payType == 0 && pay > 0) {
//				uc.setPaymenttype((byte) 1);
//				uc.setPaystatus((byte) 0);
//				userConsumeMapper.insertSelective(uc);
//				returnMap.put("payMoney", pay);
//				returnMap.put("result", 0);
//				returnMap.put("message", "未支付");
//			} else {
//				uc.setPaymenttype((byte) 5);
//				// <!---------储值卡支付start-------------->
//				if (isMemember(member.getId())) {
//					// 储值卡付款
//					if (CommonUtil.isNotEmpty(card) && card.getCtId() == 3
//							&& pay > 0) {
//						if (pay > card.getMoney()) {
//							throw new Exception();
//						}
//						Card c = new Card();
//						c.setMcId(member.getMcId());
//						c.setMoney(card.getMoney() - pay);
//						cardMapper.updateByPrimaryKeySelective(c);
//					}
//				}
//				uc.setPaystatus((byte) 1);
//				userConsumeMapper.insertSelective(uc);
//
//				if (isMemember(member.getId())) {
//					saveGiveConsume(member.getPhone(), orderCode);
//					giveGood(orderCode);
//					saveCardRecordNew(member.getMcId(), (byte) 1,
//							uc.getDiscountmoney() + "", "线下扫码消费",
//							member.getBusid(), null, 0, 0);
//				}
//
//				if (fenbiMoney > 0) {
//					// 归还商户粉币
//					returnfansCurrency(wxPublicUsers.getBusUserId(), -uc
//							.getFenbi().doubleValue());
//					MemberEntity member1 = new MemberEntity();
//					member1.setId(member.getId());
//					member1.setFansCurrency(member.getFansCurrency()
//							+ uc.getFenbi());
//					memberMapper.updateByPrimaryKeySelective(member1);
//
//				}
//				if (jifenMoney > 0) {
//					// 扣除用户积分数量
//					MemberEntity member1 = new MemberEntity();
//					member1.setId(member.getId());
//					member1.setIntegral(member.getIntegral() + uc.getIntegral());
//					memberMapper.updateByPrimaryKeySelective(member1);
//				}
//				returnMap.put("result", 1);
//				returnMap.put("message", "已支付");
//			}
//		} catch (Exception e) {
//			LOG.error("扫码支付异常", e);
//			throw new Exception();
//		}
//		return returnMap;
//	}
//
//	/**
//	 * 保留两位小数
//	 *
//	 * @param oneNumber
//	 * @param twoNumber
//	 * @return
//	 */
//	public Double reduce(Double number) {
//		long l1 = Math.round(number * 100); // 四舍五入
//		double ret = l1 / 100.0;
//		return ret;
//	}
//
//	/**
//	 * 扫码支付回调
//	 */
//	@Override
//	public void backWeChatPayment(String orderCode) throws Exception {
//		try {
//			UserConsume userConsume = userConsumeMapper
//					.findByOrderCode1(orderCode);
//			if (CommonUtil.isEmpty(userConsume)) {
//				throw new Exception();
//			}
//			MemberEntity member = memberMapper.selectByPrimaryKey(userConsume
//					.getMemberid());
//			MemberEntity m1 = new MemberEntity();
//			boolean flag = false;
//			// 粉币
//			if (userConsume.getFenbi() != 0) {
//				m1.setFansCurrency(member.getFansCurrency()
//						+ userConsume.getFenbi());
//				flag = true;
//			}
//			// 积分
//			if (userConsume.getIntegral() != 0) {
//				m1.setIntegral(member.getIntegral() + userConsume.getIntegral());
//				flag = true;
//			}
//			if (flag) {
//				m1.setId(member.getId());
//				memberMapper.updateByPrimaryKeySelective(m1);
//			}
//			// 修改订单状态
//			UserConsume uc = new UserConsume();
//			uc.setId(userConsume.getId());
//			uc.setPaystatus((byte) 1);
//			userConsumeMapper.updateByPrimaryKeySelective(uc);
//			if (isMemember(member.getId())) {
//				saveGiveConsume(member.getPhone(), orderCode);
//				giveGood(orderCode);
//				saveCardRecordNew(member.getMcId(), (byte) 1,
//						userConsume.getDiscountmoney() + "", "线下扫码消费",
//						member.getBusid(), null, 0, 0.0);
//			}
//			if (userConsume.getFenbi() != 0) {
//				// 归还商户粉币
//				returnfansCurrency(userConsume.getBususerid(), -uc.getFenbi()
//						.doubleValue());
//			}
//		} catch (Exception e) {
//			throw new Exception();
//		}
//
//	}
//
//	@Override
//	public Map<String, Object> cancelOrder(String orderCode) throws Exception {
//		Map<String, Object> map = new HashMap<String, Object>();
//		try {
//			UserConsume userConsume = userConsumeMapper
//					.findByOrderCode1(orderCode);
//			if (CommonUtil.isEmpty(userConsume)) {
//				throw new Exception();
//			}
//			MemberEntity member = memberMapper.selectByPrimaryKey(userConsume
//					.getMemberid());
//			Double fenbi = member.getFansCurrency();
//			Integer jifen = member.getIntegral();
//			Integer flow = member.getFlow();
//
//			MemberEntity m1 = new MemberEntity();
//			m1.setId(member.getId());
//			boolean flag = false;
//
//			// 粉币
//			if (userConsume.getFenbi() != 0) {
//				m1.setFansCurrency(member.getFansCurrency()
//						- userConsume.getFenbi());
//				fenbi = m1.getFansCurrency();
//				flag = true;
//			}
//			// 积分
//			if (userConsume.getIntegral() != 0) {
//				m1.setIntegral(member.getIntegral() - userConsume.getIntegral());
//				flag = true;
//
//				jifen = m1.getIntegral();
//			}
//			if (flag) {
//				memberMapper.updateByPrimaryKeySelective(m1);
//			}
//			// 修改订单状态
//			UserConsume uc = new UserConsume();
//			uc.setId(userConsume.getId());
//			uc.setPaystatus((byte) 3);
//			userConsumeMapper.updateByPrimaryKeySelective(uc);
//
//			Double fenbi1 = 0.0;
//			if (isMemember(member.getId())) {
//				boolean flag1 = false;
//				List<GiveConsume> giveConsume = giveConsumeMapper
//						.findByUcId(userConsume.getId());
//				for (GiveConsume gc : giveConsume) {
//					switch (gc.getGtId()) {
//					case 1:
//						m1.setIntegral(jifen - gc.getGcTotal());
//						flag1 = true;
//						break;
//					case 2:
//						m1.setFlow(flow - gc.getGcTotal());
//						flag1 = true;
//						break;
//					case 3:
//						fenbi1 = CommonUtil.toDouble(gc.getGcTotal());
//						m1.setFansCurrency(fenbi - gc.getGcTotal());
//						flag1 = true;
//						break;
//					default:
//						break;
//					}
//				}
//				if (flag1) {
//					memberMapper.updateByPrimaryKeySelective(m1);
//				}
//
//				Card card = cardMapper.selectByPrimaryKey(member.getMcId());
//				// 储值卡付款
//				if (userConsume.getPaymenttype() == 5) {
//					Card c = new Card();
//					c.setMcId(member.getMcId());
//					c.setMoney(card.getMoney() + userConsume.getDiscountmoney());
//					cardMapper.updateByPrimaryKeySelective(c);
//				}
//
//				saveCardRecordNew(member.getMcId(), (byte) 1,
//						uc.getDiscountmoney() + "", "线下消费撤单",
//						member.getBusid(), null, 0, 0.0);
//
//			}
//			if (userConsume.getFenbi() != 0) {
//				// 归还商户粉币
//				returnfansCurrency(userConsume.getBususerid(), uc.getFenbi()
//						+ fenbi1);
//			}
//			if (userConsume.getPaymenttype() == 5) {
//				map.put("result", 0);
//				map.put("message", "撤单成功");
//				return map;
//			}
//			map.put("result", 1);
//			return map;
//		} catch (Exception e) {
//			LOG.error("撤单失败", e);
//			throw new Exception();
//		}
//	}
//
//	@Transactional(rollbackFor = Exception.class)
//	@Override
//	public Map<String, Object> bingdingPhone(Map<String, Object> params)
//			throws Exception {
//		Map<String, Object> map = new HashMap<String, Object>();
//		try {
//			if (CommonUtil.isEmpty(params)) {
//				map.put("result", false);
//				map.put("message", "数据不完整");
//				return map;
//			}
//
//			// 短信校验
//			Integer memberId = CommonUtil.toInteger(params.get("memberId"));
//			if (CommonUtil.isEmpty(memberId)) {
//				map.put("result", false);
//				map.put("message", "数据不完整");
//				return map;
//			}
//			String code = CommonUtil.toString(params.get("code"));
//			if (CommonUtil.isEmpty(code)) {
//				map.put("result", false);
//				map.put("message", "请输入校验码");
//				return map;
//			}
//			String phone = CommonUtil.toString(params.get("phone"));
//			if (CommonUtil.isEmpty(phone)) {
//				map.put("result", false);
//				map.put("message", "数据不完整");
//				return map;
//			}
//			Integer busId = CommonUtil.toInteger(params.get("busId"));
//			if (CommonUtil.isEmpty(busId)) {
//				map.put("result", false);
//				map.put("message", "数据不完整");
//				return map;
//			}
//			// 短信判断
//			if (CommonUtil.isEmpty(JedisUtil.get(code))) {
//				map.put("result", false);
//				map.put("message", "短信校验码不正确");
//				return map;
//			}
//			// 查询要绑定的手机号码
//			MemberEntity oldMember = memberMapper.findByPhone(busId, phone);
//
//			if (CommonUtil.isEmpty(oldMember)) {
//				// 新用户
//				MemberEntity member = memberMapper.selectByPrimaryKey(memberId);
//				MemberEntity m = new MemberEntity();
//				m.setId(member.getId());
//				m.setPhone(phone);
//				memberMapper.updateByPrimaryKeySelective(m);
//				member.setPhone(phone);
//				map.put("member", member);
//			} else {
//				MemberEntity m1 = memberMapper.selectByPrimaryKey(memberId);
//
//				MemberEntity member = new MemberEntity();
//				member.setFlow(m1.getFlow() + oldMember.getFlow());
//				member.setIntegral(m1.getIntegral() + oldMember.getIntegral());
//				member.setFansCurrency(m1.getFansCurrency()
//						+ oldMember.getFansCurrency());
//				member.setId(oldMember.getId());
//
//				if (CommonUtil.isNotEmpty(oldMember.getOldid())
//						&& !oldMember.getOldid().contains(
//								oldMember.getId().toString())) {
//					member.setOldid(oldMember.getOldid() + ","
//							+ oldMember.getId() + "," + m1.getId());
//				} else {
//					if (CommonUtil.isNotEmpty(oldMember.getOldid())) {
//						member.setOldid(oldMember.getOldid() + "," + m1.getId());
//					} else {
//						member.setOldid(m1.getId() + ",");
//					}
//				}
//				if (CommonUtil.isEmpty(oldMember.getOpenid())) {
//					member.setOpenid(m1.getOpenid());
//				}
//
//				if (CommonUtil.isEmpty(oldMember.getPublicId())
//						&& CommonUtil.isNotEmpty(m1.getPublicId())) {
//					member.setPublicId(m1.getPublicId());
//				}
//
//				if (CommonUtil.isEmpty(oldMember.getHeadimgurl())) {
//					member.setHeadimgurl(m1.getHeadimgurl());
//				}
//
//				memberMapper.deleteByPrimaryKey(m1.getId());
//				memberMapper.updateByPrimaryKeySelective(member);
//
//				MemberOld old = (MemberOld) JSONObject.toBean(
//						JSONObject.fromObject(m1), MemberOld.class);
//				memberOldMapper.insertSelective(old);
//				// 修改小程序之前openId对应的memberId
//				memberAppletOpenidMapper.updateMemberId(member.getId(),
//						m1.getId());
//
//				member.setPhone(phone);
//				map.put("member", member);
//			}
//			JedisUtil.del(code);
//			map.put("result", true);
//			map.put("message", "绑定成功");
//		} catch (Exception e) {
//			e.printStackTrace();
//			LOG.error("小程序绑定手机号码异常", e);
//			throw new Exception();
//		}
//		return map;
//
//	}
//
//	@Override
//	public List<Map<String, Object>> findMemberCardRecharge(Integer busId,
//			String cardNo) {
//		Card card = cardMapper.findCardByCardNo(busId, cardNo);
//		if (CommonUtil.isEmpty(card)) {
//			return null;
//		}
//		List<Map<String, Object>> recharges = null;
//		// 判断会员日
//		MemberDate memberDate = findMemeberDate(busId, card.getCtId());
//		if (CommonUtil.isNotEmpty(memberDate)) {
//			recharges = rechargeGiveMapper.findBybusIdAndGrId(busId,
//					card.getGrId(), 1);
//		} else {
//			recharges = rechargeGiveMapper.findBybusIdAndGrId(busId,
//					card.getGrId(), 0);
//		}
//		return recharges;
//	}
//
//	@Override
//	public Map<String, Object> successReCharge(Integer busId, String cardNo,
//			Double money) throws Exception {
//		Map<String, Object> map = new HashMap<String, Object>();
//		try {
//			if (CommonUtil.isEmpty(cardNo) || CommonUtil.isEmpty(money)) {
//				map.put("result", false);
//				map.put("message", "数据异常");
//				return map;
//			}
//			Card card = cardMapper.findCardByCardNo(busId, cardNo);
//			MemberEntity member = null;
//			try {
//				member = memberMapper.findByMcIdAndbusId(busId, card.getMcId());
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//			if (CommonUtil.isEmpty(card)) {
//				map.put("result", false);
//				map.put("message", "卡号不存在");
//				return map;
//			}
//			// 添加会员记录
//			UserConsume uc = new UserConsume();
//			uc.setPublicId(member.getPublicId());
//			uc.setBususerid(member.getBusid());
//			uc.setMemberid(member.getId());
//			uc.setMcid(card.getMcId());
//			uc.setCtid(card.getCtId());
//			uc.setGtId(card.getGtId());
//			uc.setRecordtype((byte) 1);
//			uc.setUctype((byte) 7);
//			uc.setTotalmoney(money);
//			uc.setDiscountmoney(money);
//			uc.setCreatedate(new Date());
//			uc.setPaystatus((byte) 1);
//			uc.setPaymenttype((byte) 3);
//			uc.setDatasource((byte) 0);
//			int count = 0;
//			if (card.getCtId() == 3) {
//				uc.setGivegift("赠送金额");
//				GiveRule gr = findGive(member.getBusid(), card.getGtId(), 3);
//				if (CommonUtil.isNotEmpty(gr)) {
//					count = findRechargegive(money, gr.getGrId(),
//							member.getBusid(), card.getCtId());
//					uc.setGiftcount(count);
//				}
//				uc.setUccount(0);
//			} else if (card.getCtId() == 5) {
//				uc.setGivegift("赠送次数");
//				GiveRule gr = findGive(member.getBusid(), card.getGtId(), 5);
//				if (CommonUtil.isNotEmpty(gr)) {
//					int givecount = findRechargegive(money, gr.getGrId(),
//							member.getBusid(), card.getCtId());
//					uc.setGiftcount(givecount);
//				}
//				uc.setUccount(count);
//			}
//			String orderCode = CommonUtil.getMEOrderCode();
//			uc.setOrdercode(orderCode);
//			userConsumeMapper.insertSelective(uc);
//			if (card.getCtId() == 4) {
//				memberGive(orderCode, (byte) 1);
//				findGiveRule(member.getPhone(), orderCode, "充值", (byte) 1);
//			} else {
//				memberGive(orderCode, (byte) 1);
//				card = cardMapper.findCardByCardNo(busId, cardNo);
//				if (card.getCtId() == 5) {
//					String uccount = "";
//					if (CommonUtil.isNotEmpty(uc.getGiftcount())) {
//						uccount = uc.getUccount() + "次,送" + uc.getGiftcount()
//								+ "次";
//					} else {
//						uccount = uc.getUccount() + "次";
//					}
//					saveCardRecordNew(uc.getMcid(), (byte) 1, uccount, "充值",
//							member.getBusid(), card.getFrequency().toString(),
//							card.getCtId(), 0.0);
//				} else {
//					if (CommonUtil.isNotEmpty(uc.getGiftcount())
//							&& uc.getGiftcount() > 0) {
//						saveCardRecordNew(uc.getMcid(), (byte) 1, money + "元,送"
//								+ uc.getGiftcount() + "元", "充值",
//								member.getBusid(), card.getMoney().toString(),
//								card.getCtId(), 0.0);
//					} else {
//						saveCardRecordNew(uc.getMcid(), (byte) 1, money + "元",
//								"充值", member.getPublicId(), card.getMoney()
//										.toString(), card.getCtId(), 0.0);
//					}
//				}
//			}
//
//			// 消息模板推送
//			if (card.getCtId() == 3) {
//				systemMsgService.sendChuzhiCard(member, money);
//			} else if (card.getCtId() == 5) {
//				systemMsgService.sendCikaCard(member, money, uc.getUccount());
//			}
//
//			map.put("result", true);
//			map.put("message", "充值成功");
//		} catch (Exception e) {
//			LOG.error("充值会员卡异常,卡号：" + cardNo, e);
//			throw new Exception();
//		}
//		return map;
//	}
//
//	@Override
//	public Page findConsumeByMemberId(Integer busId, Map<String, Object> params) {
//
//		params.put("curPage", CommonUtil.isEmpty(params.get("curPage")) ? 1
//				: CommonUtil.toInteger(params.get("curPage")));
//		int pageSize = 10;
//
//		Integer memberId = 0;
//		if (CommonUtil.isNotEmpty(params.get("memberId"))) {
//			memberId = CommonUtil.toInteger(params.get("memberId"));
//		} else {
//			return null;
//		}
//
//		String startDate = null;
//		if (CommonUtil.isNotEmpty(params.get("startDate"))) {
//			startDate = params.get("startDate").toString();
//		}
//		String endDate = null;
//		if (CommonUtil.isNotEmpty(params.get("endDate"))) {
//			endDate = params.get("endDate").toString();
//		}
//
//		int rowCount = userConsumeMapper.countUserConumeByMember(busId,
//				memberId, startDate, endDate);
//
//		Page page = new Page(CommonUtil.toInteger(params.get("curPage")),
//				pageSize, rowCount, "");
//		params.put("firstResult", pageSize
//				* ((page.getCurPage() <= 0 ? 1 : page.getCurPage()) - 1));
//		params.put("maxResult", pageSize);
//
//		List<Map<String, Object>> list = userConsumeMapper
//				.findUserConumeByMember(busId, memberId,
//						Integer.parseInt(params.get("firstResult").toString()),
//						pageSize, startDate, endDate);
//		page.setSubList(list);
//		return page;
//	}
//
//	@Override
//	public Map<String, Object> findMemberByCardNo(String cardNo) {
//		List<Map<String, Object>> listMap = memberMapper.findCardNo(cardNo);
//		if (CommonUtil.isEmpty(listMap) || listMap.size() <= 0) {
//			return null;
//		}
//		return listMap.get(0);
//	}
//
//	@Override
//	public PublicParameterSet findjifenRule(Integer busId) {
//		return publicParameterSetMapper.findBybusId_1(busId);
//	}
//
//	@Override
//	public Map<String, Object> fenbiRule(Integer busId) {
//		Map<String, Object> map = new HashMap<String, Object>();
//		Map<String, Object> dict = dictService.getDict("1058");
//		Double ratio = CommonUtil.toDouble(dict.get("1"));
//		map.put("ratio", ratio);
//		map.put("startMoney", 10);
//		return map;
//	}
//
//	@Override
//	public List<Integer> findMemberIds(Integer memberId) {
//		List<Integer> list = new ArrayList<Integer>();
//		MemberEntity member = memberMapper.selectByPrimaryKey(memberId);
//		if (CommonUtil.isEmpty(member.getOldid())) {
//			list.add(memberId);
//			return list;
//		}
//		String[] str = member.getOldid().split(",");
//		for (int i = 0; i < str.length; i++) {
//			if (CommonUtil.isNotEmpty(str[i]) && !str[i].contains("null")
//					&& !list.contains(CommonUtil.toInteger(str[i]))) {
//				list.add(CommonUtil.toInteger(str[i]));
//			}
//		}
//
//		if (!list.contains(memberId)) {
//			list.add(memberId);
//		}
//
//		return list;
//	}
//
//	@Override
//	public Map<String, Object> backFlow(String orderCode) {
//		Map<String, Object> map = new HashMap<>();
//		try {
//			UserConsume userConsume = userConsumeMapper
//					.findByOrderCode1(orderCode);
//			if (CommonUtil.isEmpty(userConsume)) {
//				map.put("result", false);
//				map.put("message", "未查询到相对应的数据");
//				return map;
//			}
//			Integer flow = userConsume.getGiveflow();
//			Integer memberId = userConsume.getMemberid();
//			if (CommonUtil.isEmpty(flow) || CommonUtil.isEmpty(memberId)) {
//				map.put("result", false);
//				map.put("message", "缺少对应的数据");
//				return map;
//			}
//			MemberEntity member = memberMapper.selectByPrimaryKey(memberId);
//			MemberEntity m = new MemberEntity();
//			m.setId(memberId);
//			m.setFlow(flow + member.getFlow());
//			memberMapper.updateByPrimaryKeySelective(m);
//			saveCardRecordNew(member.getMcId(), (byte) 4, flow + "MB",
//					"流量兑换失败,已退回", member.getBusid(), "", 0, flow);
//			UserConsume uc = new UserConsume();
//			uc.setId(userConsume.getId());
//			uc.setFlowstate((byte) 0);
//			userConsumeMapper.updateByPrimaryKeySelective(uc);
//
//			map.put("result", true);
//			map.put("message", "兑换流量回滚成功");
//		} catch (Exception e) {
//			LOG.error("兑换流量回滚异常", e);
//			map.put("result", false);
//			map.put("message", "未查询到相对应的数据");
//			return map;
//		}
//		return map;
//	}
//
//	@Override
//	public Map<String, Object> saveMemberConsume(Integer busUserId,
//			Integer memberId, String cardNo, double money, Byte recordType,
//			Byte type, Integer integral, Integer fenbi, Integer uccount,
//			Integer discount, Double discountmoney, Integer orderid,
//			String uctable, Byte paymenttype, Byte paystatus, String givegift,
//			Integer giftCount, String orderCode) {
//		Map<String, Object> map = new HashMap<>();
//		try {
//			// 添加会员记录
//			UserConsume uc = new UserConsume();
//			uc.setBususerid(busUserId);
//			uc.setMemberid(memberId);
//			if (CommonUtil.isNotEmpty(cardNo)) {
//				Card card = cardMapper.findCardByCardNo(busUserId, cardNo);
//				uc.setMcid(card.getMcId());
//				uc.setCtid(card.getCtId());
//				uc.setGtId(card.getGtId());
//			}
//			uc.setRecordtype(recordType);
//			uc.setTotalmoney(money);
//			uc.setCreatedate(new Date());
//			uc.setPaystatus((byte) 0);
//			uc.setUctype(type);
//			uc.setIntegral(integral);
//			uc.setFenbi(fenbi);
//			uc.setUccount(uccount);
//			uc.setDiscount(discount);
//			uc.setDiscountmoney(discountmoney);
//			uc.setOrderid(orderid);
//			uc.setUctable(uctable);
//			uc.setCreatedate(new Date());
//			uc.setPaymenttype(paymenttype);
//			uc.setPaystatus(paystatus);
//			uc.setGivegift(givegift);
//			uc.setOrdercode(orderCode);
//			userConsumeMapper.insertSelective(uc);
//			map.put("result", true);
//			map.put("message", "添加记录成功");
//		} catch (Exception e) {
//			map.put("result", false);
//			map.put("message", "添加记录失败");
//		}
//		return map;
//	}
//
//	@Override
//	public Map<String, Object> findMemberShopId(String phone, Integer busId,
//			Integer shopId) {
//		Map<String, Object> map = new HashMap<>();
//
//		MemberEntity member = memberMapper.findByPhone(busId, phone);
//		if (CommonUtil.isEmpty(member) || CommonUtil.isEmpty(member.getMcId())) {
//			map.put("result", false);
//			map.put("message", "当前用户非会员");
//			return map;
//		}
//		Card card = cardMapper.selectByPrimaryKey(member.getMcId());
//
//		if (card.getShopid() > 0) {
//			if (shopId != card.getShopid() && !shopId.equals(card.getShopid())) {
//				map.put("result", false);
//				map.put("message", "当前用户会员不是该门店会员");
//				return map;
//			}
//		} else {
//			WxShop shop = wxShopService.selectMainShopByBusId(busId);
//			if (CommonUtil.isNotEmpty(shop) && !shop.getId().equals(shopId)) {
//				map.put("result", false);
//				map.put("message", "当前用户会员不是该门店会员");
//				return map;
//			}
//		}
//
//		List<Map<String, Object>> cards = memberMapper
//				.findMemberBybusIdAndPhone(busId, card.getMcId());
//
//		map.put("result", true);
//		map.put("message", "当前用户会员是该门店会员");
//		if (CommonUtil.isNotEmpty(cards) && cards.size() > 0) {
//			Map<String, Object> c = cards.get(0);
//			if ("0".equals(CommonUtil.toString(c.get("isChecked")))) {
//				map.put("result", false);
//				map.put("message", "当前用户会员还未审核");
//				return map;
//			}
//
//			if ("1".equals(CommonUtil.toString(c.get("cardStatus")))) {
//				map.put("result", false);
//				map.put("message", "当前用户会员已禁用");
//				return map;
//			}
//			map.put("card", cards.get(0));
//		}
//		return map;
//
//	}
//
//	@Override
//	public List<Map<String, Object>> findGradeTypeByApplyType(Integer busId) {
//		return gradeTypeMapper.findGradeTypeByApplyType(busId);
//	}
//
//	@Transactional(rollbackFor = Exception.class)
//	@Override
//	public Map<String, Object> getMemberCardByXiaochangmao(
//			Map<String, Object> params) throws Exception {
//		Map<String, Object> map = new HashMap<String, Object>();
//		try {
//			if (CommonUtil.isEmpty(params)) {
//				map.put("result", false);
//				map.put("message", "数据不完整");
//				return map;
//			}
//
//			// 短信校验
//			Integer memberId = CommonUtil.toInteger(params.get("memberId"));
//			if (CommonUtil.isEmpty(memberId)) {
//				map.put("result", false);
//				map.put("message", "数据不完整");
//				return map;
//			}
//
//			String code = CommonUtil.toString(params.get("code"));
//			if (CommonUtil.isEmpty(code)) {
//				map.put("result", false);
//				map.put("message", "请输入校验码");
//				return map;
//			}
//
//			String phone = CommonUtil.toString(params.get("phone"));
//			if (CommonUtil.isEmpty(phone)) {
//				map.put("result", false);
//				map.put("message", "数据不完整");
//				return map;
//			}
//
//			Integer busId = CommonUtil.toInteger(params.get("busId"));
//			if (CommonUtil.isEmpty(busId)) {
//				map.put("result", false);
//				map.put("message", "数据不完整");
//				return map;
//			}
//
//			Integer ctId = CommonUtil.toInteger(params.get("ctId"));
//			if (CommonUtil.isEmpty(ctId)) {
//				map.put("result", false);
//				map.put("message", "数据不完整");
//				return map;
//			}
//
//			// 短信判断
//			if (CommonUtil.isEmpty(JedisUtil.get(code))) {
//				map.put("result", false);
//				map.put("message", "短信校验码不正确");
//				return map;
//			}
//
//			// 分配会员卡
//			// 添加会员卡
//			Card card = new Card();
//			card.setIschecked((byte) 1);
//			card.setCardno(CommonUtil.getCode());
//			card.setCtId(ctId);
//			card.setSystemcode(CommonUtil.getNominateCode());
//			card.setApplytype((byte) 0);
//			card.setMemberid(memberId);
//			card.setBusid(busId);
//
//			List<Map<String, Object>> gradeTyps = gradeTypeMapper
//					.findAllBybusId(busId, ctId);
//			if (CommonUtil.isEmpty(gradeTyps) || gradeTyps.size() == 0) {
//				map.put("result", false);
//				map.put("message", "数据查询异常");
//				return map;
//			}
//			card.setGtId(CommonUtil.toInteger(gradeTyps.get(0).get("gt_id")));
//			card.setGrId(CommonUtil.toInteger(gradeTyps.get(0).get("gr_id")));
//			card.setReceivedate(new Date());
//			card.setIsbinding((byte) 1);
//			card.setIschecked((byte) 1);
//			card.setFrequency(0);
//			card.setMoney(0.0);
//
//			// 查询要绑定的手机号码
//			MemberEntity oldMember = memberMapper.findByPhone(busId, phone);
//
//			MemberEntity member = null;
//
//			MemberEntity m1 = memberMapper.selectByPrimaryKey(memberId);
//
//			if (CommonUtil.isNotEmpty(oldMember)
//					&& !m1.getId().equals(oldMember.getId())) {
//				member = new MemberEntity();
//				member.setFlow(m1.getFlow() + oldMember.getFlow());
//				member.setIntegral(m1.getIntegral() + oldMember.getIntegral());
//				member.setFansCurrency(m1.getFansCurrency()
//						+ oldMember.getFansCurrency());
//
//				if (CommonUtil.isNotEmpty(oldMember.getOldid())
//						&& !oldMember.getOldid().contains(
//								oldMember.getId().toString())) {
//					member.setOldid(oldMember.getOldid() + ","
//							+ oldMember.getId() + "," + m1.getId());
//				} else {
//					if (CommonUtil.isNotEmpty(oldMember.getOldid())) {
//						member.setOldid(oldMember.getOldid() + "," + m1.getId());
//					} else {
//						member.setOldid(m1.getId() + ",");
//					}
//				}
//
//				if (CommonUtil.isEmpty(oldMember.getHeadimgurl())) {
//					member.setHeadimgurl(m1.getHeadimgurl());
//				}
//
//				member.setId(oldMember.getId());
//				if (CommonUtil.isEmpty(oldMember.getMcId())) {
//					cardMapper.insertSelective(card);
//					member.setMcId(card.getMcId());
//
//				}
//				memberMapper.deleteByPrimaryKey(m1.getId());
//				memberMapper.updateByPrimaryKeySelective(member);
//
//				MemberOld old = (MemberOld) JSONObject.toBean(
//						JSONObject.fromObject(m1), MemberOld.class);
//				memberOldMapper.insertSelective(old);
//				// 修改小程序之前openId对应的memberId
//				memberAppletOpenidMapper.updateMemberId(member.getId(),
//						m1.getId());
//
//				map.put("member", member);
//			} else {
//				// 新用户
//				member = memberMapper.selectByPrimaryKey(memberId);
//				MemberEntity m = new MemberEntity();
//				m.setId(member.getId());
//				m.setPhone(phone);
//				if (CommonUtil.isEmpty(member.getMcId())) {
//					cardMapper.insertSelective(card);
//					m.setMcId(card.getMcId());
//					member.setMcId(card.getMcId());
//				}
//				memberMapper.updateByPrimaryKeySelective(m);
//				member.setPhone(phone);
//				map.put("member", member);
//			}
//			JedisUtil.del(code);
//			map.put("result", true);
//			map.put("message", "领取成功");
//		} catch (Exception e) {
//			e.printStackTrace();
//			LOG.error("小程序绑定手机号码异常", e);
//			throw new Exception();
//		}
//		return map;
//	}
//
//	@Override
//	public Map<String, Object> updateIntergral(HttpServletRequest request,
//			Integer memberId, Integer intergral) {
//		Map<String, Object> map = new HashMap<String, Object>();
//		try {
//			MemberEntity member = memberMapper.selectByPrimaryKey(memberId);
//			Integer mIntergral = member.getIntegral();
//			if (mIntergral < -intergral) {
//				map.put("result", 1);
//				map.put("message", "积分不足");
//				return map;
//			}
//			MemberEntity member1 = new MemberEntity();
//			member1.setId(member.getId());
//			member1.setIntegral(member.getIntegral() + intergral);
//			memberMapper.updateByPrimaryKeySelective(member1);
//
//			if (CommonUtil.isNotEmpty(request)) {
//				member.setIntegral(member1.getIntegral());
//				CommonUtil.setLoginMember(request, member);
//			}
//			map.put("result", 2);
//			map.put("message", "积分支付成功");
//		} catch (Exception e) {
//			LOG.error("积分支付失败", e);
//			map.put("result", 1);
//			map.put("message", "积分支付异常");
//		}
//		return map;
//	}
//
//	@Override
//	public boolean isAdequateMoney(Integer memberId, double totalMoney) {
//		MemberEntity member = memberMapper.selectByPrimaryKey(memberId);
//		if (CommonUtil.isEmpty(member.getMcId())) {
//			return false;
//		}
//		Card card = cardMapper.selectByPrimaryKey(member.getMcId());
//		if (CommonUtil.isNotEmpty(card)) {
//			if (card.getCtId() == 3) {
//				if (card.getMoney() >= totalMoney) {
//					return true;
//				}
//			}
//		}
//		return false;
//	}
//}
