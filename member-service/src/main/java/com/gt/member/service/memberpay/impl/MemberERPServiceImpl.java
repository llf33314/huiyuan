//package com.gt.member.service.old.memberpay.impl;
//
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.apache.log4j.Logger;
//import org.hibernate.mapping.Array;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import net.sf.json.JSONArray;
//import net.sf.json.JSONObject;
//
//import com.gt.dao.common.WxCardReceiveMapper;
//import com.gt.dao.member.CardLentMapper;
//import com.gt.dao.member.CardMapper;
//import com.gt.dao.member.GiveRuleMapper;
//import com.gt.dao.member.MemberMapper;
//import com.gt.dao.set.WxShopMapper;
//import com.gt.dao.user.BusUserBranchRelationMapper;
//import com.gt.dao.user.WxPublicUsersMapper;
//import com.gt.entity.member.Card;
//import com.gt.entity.member.CardLent;
//import com.gt.entity.member.GiveRule;
//import com.gt.entity.member.MemberEntity;
//import com.gt.entity.member.MemberDate;
//import com.gt.entity.set.WxShop;
//import com.gt.entity.user.BusUserEntity;
//import com.gt.entity.user.WxPublicUsersEntity;
//import com.gt.service.common.dict.DictService;
//import com.gt.service.common.wxcard.IWxCardService;
//import com.gt.service.member.DuofenCardService;
//import com.gt.service.member.MemberService;
//import com.gt.service.memberpay.MemberERPService;
//import com.gt.service.memberpay.MemberPayService;
//import com.gt.util.CommonUtil;
//import com.gt.util.DateTimeKit;
//import com.gt.util.EncryptUtil;
//
//@Service
//public class MemberERPServiceImpl implements MemberERPService {
//
//	private static final Logger LOG = Logger
//			.getLogger(MemberERPServiceImpl.class);
//
//	@Autowired
//	private MemberPayService memberPayService;
//
//	@Autowired
//	private DictService dictService;
//
//	@Autowired
//	private CardMapper cardMapper;
//
//	@Autowired
//	private CardLentMapper cardLentMapper;
//
//	@Autowired
//	private MemberMapper memberMapper;
//
//	@Autowired
//	private GiveRuleMapper giveRuleMapper;
//
//	@Autowired
//	private WxShopMapper wxShopMapper;
//
//	@Autowired
//	private WxPublicUsersMapper wxPublicUsersMapper;
//
//	@Autowired
//	private BusUserBranchRelationMapper busUserBranchRelationMapper;
//
//	@Autowired
//	private WxCardReceiveMapper wxCardReceiveMapper;
//
//	@Autowired
//	private DuofenCardService duofenCardService;
//
//	@Autowired
//	private IWxCardService wxCardService;
//
//	@Autowired
//	private MemberService memberService;
//
//	@Override
//	public Map<String, Object> findMemberCard(BusUserEntity busUser,
//			String cardNoKey, String cardNo, Integer shopId) {
//		Map<String, Object> map = new HashMap<String, Object>();
//		String cardNodecrypt = "";
//		try {
//			// 如果手动输入 会出现异常
//			cardNodecrypt = EncryptUtil.decrypt(cardNoKey, cardNo);
//		} catch (Exception e) {
//		}
//
//		int busId = busUser.getId();
//		if (busUser.getPid() != 0) {
//			busId = dictService.pidUserId(busUser.getPid());
//		}
//
//		if (cardNodecrypt.contains("?time")) {
//			// 查询卡号是否存在
//			Long time = Long.parseLong(cardNodecrypt.substring(cardNodecrypt
//					.indexOf("?time=") + 6));
//
//			cardNo = cardNodecrypt.substring(0, cardNodecrypt.indexOf("?time"));
//
//			Card card1 = cardMapper.findCardByCardNo(busId, cardNo);
//			if (card1.getCtId() == 3) {
//				// 2分钟后为超时
//				if (DateTimeKit.secondBetween(new Date(time), new Date()) > 120) {
//					// 二维码已超时
//					map.put("result", false);
//					map.put("message", "二维码已超时!");
//					return map;
//				}
//			}
//		}
//
//		Card card = null;
//		try {
//			// 判断是否借给他人
//			CardLent c = cardLentMapper.findByCode(cardNo);
//			if (CommonUtil.isNotEmpty(c)) {
//				// 判断时间是否在有效时间内
//				// 5分钟后为超时
//				if (DateTimeKit.secondBetween(c.getCreatedate(), new Date()) > 300) {
//					// 二维码已超时
//					map.put("result", false);
//					map.put("message", "二维码已超时!");
//					return map;
//				}
//				card = cardMapper.selectByPrimaryKey(c.getMcid());
//
//				map.put("jie", 1);
//				map.put("lentMoney", c.getLentmoney());
//				map.put("clId", c.getId());
//			}
//
//		} catch (Exception e) {
//
//		}
//
//		try {
//			MemberEntity member = null;
//			// 查询卡号是否存在
//			if (CommonUtil.isEmpty(card)) {
//				card = cardMapper.findCardByCardNo(busId, cardNo);
//				if (CommonUtil.isNotEmpty(card)) {
//					member = memberMapper.findByMcIdAndbusId(busId,
//							card.getMcId());
//				}
//
//			}
//
//			if (CommonUtil.isEmpty(card)) {
//				member = memberMapper.findByPhone(busId, cardNo);
//
//				if (CommonUtil.isNotEmpty(member)) {
//					card = cardMapper.selectByPrimaryKey(member.getMcId());
//				}
//			}
//
//			if (CommonUtil.isEmpty(card)) {
//				map.put("result", false);
//				map.put("message", "卡片不存在!");
//				return map;
//			} else if (card.getCardstatus() == 1) {
//				map.put("result", false);
//				map.put("message", "会员已拉黑!");
//				return map;
//			} else {
//				List<Map<String, Object>> cards = cardMapper.findCardById(card
//						.getMcId());
//				GiveRule giveRule = giveRuleMapper.selectByPrimaryKey(card
//						.getGrId());
//				map.put("result", true);
//				map.put("nickName", member.getNickname());
//				map.put("phone", member.getPhone());
//				map.put("ctName", cards.get(0).get("ct_name"));
//				map.put("gradeName", cards.get(0).get("gt_grade_name"));
//				map.put("cardNo", card.getCardno());
//				map.put("ctId", card.getCtId());
//				map.put("discount", giveRule.getGrDiscount() / 10);
//				map.put("money", card.getMoney());
//				map.put("frequency", card.getFrequency());
//				map.put("fans_currency", member.getFansCurrency());
//				map.put("integral", member.getIntegral());
//				map.put("memberId", member.getId());
//				map.put("cardId", card.getMcId());
//
//				Double fenbiMoeny = memberPayService.currencyCount(null,
//						member.getFansCurrency());
//				map.put("fenbiMoeny", fenbiMoeny);
//
//				Double jifenMoeny = memberPayService.integralCount(null,
//						new Double(member.getIntegral()), busId);
//				map.put("jifenMoeny", jifenMoeny);
//
//				WxShop wxshop = wxShopMapper.selectByPrimaryKey(shopId);
//
//				WxPublicUsersEntity wxPublicUsers = wxPublicUsersMapper
//						.selectByUserId(busId);
//
//				if (CommonUtil.isNotEmpty(wxPublicUsers)
//						&& CommonUtil.isNotEmpty(member.getOpenid())
//						&& wxshop.getStatus() == 2) {
//					// 查询优惠券信息
//					List<Map<String, Object>> cardList = wxCardReceiveMapper
//							.findByOpenId1(wxPublicUsers.getId(),
//									member.getOpenid());
//					List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
//					if (CommonUtil.isNotEmpty(cardList) && cardList.size() > 0) {
//						for (Map<String, Object> map2 : cardList) {
//							// 时间判断
//							if (CommonUtil.isNotEmpty(map2
//									.get("begin_timestamp"))
//									&& CommonUtil.isNotEmpty(map2
//											.get("end_timestamp"))) {
//								if (DateTimeKit.laterThanNow(DateTimeKit.parse(
//										map2.get("begin_timestamp").toString(),
//										"yyyy-MM-dd hh:mm:ss"))) {
//									continue;
//								}
//								if (!DateTimeKit.laterThanNow(DateTimeKit
//										.parse(map2.get("end_timestamp")
//												.toString(),
//												"yyyy-MM-dd hh:mm:ss"))) {
//									continue;
//								}
//							} else {
//								if (DateTimeKit
//										.laterThanNow(DateTimeKit.addDays(
//												DateTimeKit.parse(
//														map2.get("ctime")
//																.toString(),
//														"yyyy-MM-dd hh:mm:ss"),
//												CommonUtil.toInteger(map2
//														.get("fixed_begin_term"))))) {
//									continue;
//								}
//								if (!DateTimeKit.laterThanNow(DateTimeKit
//										.addDays(DateTimeKit.parse(
//												map2.get("ctime").toString(),
//												"yyyy-MM-dd hh:mm:ss"),
//												CommonUtil.toInteger(map2
//														.get("fixed_term"))))) {
//									continue;
//								}
//							}
//
//							String day = DateTimeKit.getDayToEnglish();
//							if (!map2.get("time_limit").toString()
//									.contains(day)) {
//								continue;
//							}
//
//							if (map2.get("location_id_list").toString()
//									.contains(wxshop.getPoiid())) {
//								list.add(map2);
//							}
//						}
//					}
//					if (list.size() > 0) {
//						map.put("code", 1);
//						map.put("cardList", JSONArray.fromObject(list));
//					}
//				}
//			}
//
//			// 查询能使用的多粉优惠券
//			List<Map<String, Object>> duofenCards = duofenCardService
//					.findDuofenCardByMemberId(member.getId(), shopId);
//			map.put("duofenCards", duofenCards);
//
//			MemberDate memberDate = memberPayService.findMemeberDate(busId,
//					card.getCtId());
//			if (card.getCtId() == 2) {
//				if (CommonUtil.isNotEmpty(memberDate)) {
//					map.put("memberDiscount", memberDate.getDiscount());
//					map.put("memberDate", true);
//				}
//			}
//
//			map.put("result", true);
//			return map;
//		} catch (Exception e) {
//			map.put("result", false);
//			map.put("message", "查询异常");
//			LOG.error("ERP查询会员信息异常", e);
//		}
//		return map;
//	}
//
//	@Transactional(rollbackFor = Exception.class)
//	@Override
//	public Map<String, Object> payMemberCard(String json) throws Exception {
//		Map<String, Object> returnMap = new HashMap<String, Object>();
//		try {
//			JSONObject jsonObject = JSONObject.fromObject(json);
//			Object isUsrYhj = jsonObject.get("isUsrYhj"); // 标示 是否使用卡券
//			if (CommonUtil.isNotEmpty(isUsrYhj)
//					&& CommonUtil.toInteger(isUsrYhj) == 1) {
//				String codes = jsonObject.getString("codes");
//				Integer cardType = jsonObject.getInt("cardType");
//				if (cardType == 0) {
//					Integer publicId = jsonObject.getInt("publicId");
//					WxPublicUsersEntity wxPublicUsers = wxPublicUsersMapper
//							.selectByPrimaryKey(publicId);
//					// 微信卡券
//					Map<String, Object> map = wxCardService.wxCardReceive(
//							wxPublicUsers, codes);
//					if ("-1".equals(CommonUtil.toString(map.get("result")))) {
//						returnMap.put("result", false);
//						returnMap.put("message", map.get("message"));
//						return returnMap;
//					}
//				} else {
//					Map<String, Object> params = new HashMap<String, Object>();
//					params.put("storeId", jsonObject.get("storeId"));
//					params.put("codes", codes);
//					// 多粉卡券
//					Map<String, Object> map = duofenCardService
//							.verificationCard_2(params);
//					if (!(boolean) map.get("result")) {
//						return map;
//					}
//				}
//			}
//			Object isUseCard = jsonObject.get("isUseCard"); // 判断是否使用会员储值卡
//			if (CommonUtil.isNotEmpty(isUseCard)
//					&& CommonUtil.toInteger(isUseCard) == 1) {
//				Integer memberId = jsonObject.getInt("memberId");
//				Double totalMoney = jsonObject.getDouble("totalMoney");
//				Map<String, Object> map = memberPayService.storePay(memberId,
//						totalMoney);
//				if ("0".equals(CommonUtil.toString(map.get("result")))
//						|| "1".equals(CommonUtil.toString(map.get("result")))) {
//					returnMap.put("result", false);
//					returnMap.put("message", map.get("message"));
//					return returnMap;
//				}
//			}
//			//
//			Object isUseJifen = jsonObject.get("isUseJifen");
//			if (CommonUtil.isNotEmpty(isUseJifen)
//					&& CommonUtil.toInteger(isUseJifen) == 1) {
//				Integer memberId = jsonObject.getInt("memberId");
//				Double intergral = jsonObject.getDouble("jifen");
//				Map<String, Object> map = memberPayService
//						.updateMemberIntergral(null, memberId,
//								-intergral.intValue());
//				if ("1".equals(CommonUtil.toString(map.get("result")))) {
//					returnMap.put("result", false);
//					returnMap.put("message", map.get("message"));
//					return returnMap;
//				}
//			}
//
//			Object isUsefenbi = jsonObject.get("isUsefenbi");
//			if (CommonUtil.isNotEmpty(isUsefenbi)
//					&& CommonUtil.toInteger(isUsefenbi) == 1) {
//				Integer memberId = jsonObject.getInt("memberId");
//				Double fenbi = jsonObject.getDouble("fenbi");
//				MemberEntity member = memberMapper.selectByPrimaryKey(memberId);
//				Map<String, Object> map = memberPayService.reduceFansCurrency(
//						null, member, member.getBusid(), fenbi);
//				if ("1".equals(CommonUtil.toString(map.get("result")))) {
//					returnMap.put("result", false);
//					returnMap.put("message", map.get("message"));
//					return returnMap;
//				}
//			}
//		} catch (Exception e) {
//			LOG.error("ERP调用payMemberCard接口异常", e);
//			throw new Exception();
//		}
//		returnMap.put("result", true);
//		returnMap.put("message", "操作成功");
//		return returnMap;
//
//	}
//
//	/**
//	 * 判断用户是否是会员 false 不是 true 是
//	 */
//	@Override
//	public boolean isMemember(BusUserEntity busUser, String cardNoKey, String cardNo) {
//		Map<String, Object> map = new HashMap<String, Object>();
//		String cardNodecrypt = "";
//		try {
//			// 如果手动输入 会出现异常
//			cardNodecrypt = EncryptUtil.decrypt(cardNoKey, cardNo);
//		} catch (Exception e) {
//		}
//
//		int busId = busUser.getId();
//		if (busUser.getPid() != 0) {
//			busId = dictService.pidUserId(busUser.getPid());
//		}
//
//		if (cardNodecrypt.contains("?time")) {
//			// 查询卡号是否存在
//			Long time = Long.parseLong(cardNodecrypt.substring(cardNodecrypt
//					.indexOf("?time=") + 6));
//
//			cardNo = cardNodecrypt.substring(0, cardNodecrypt.indexOf("?time"));
//
//			Card card1 = cardMapper.findCardByCardNo(busId, cardNo);
//			if (card1.getCtId() == 3) {
//				// 2分钟后为超时
//				if (DateTimeKit.secondBetween(new Date(time), new Date()) > 120) {
//					// 二维码已超时
//					return false;
//				}
//			}
//		}
//		Card card = null;
//		try {
//			MemberEntity member = null;
//			// 查询卡号是否存在
//			if (CommonUtil.isEmpty(card)) {
//				card = cardMapper.findCardByCardNo(busId, cardNo);
//				if (CommonUtil.isNotEmpty(card)) {
//					member = memberMapper.findByMcIdAndbusId(busId,
//							card.getMcId());
//				}
//
//			}
//
//			if (CommonUtil.isEmpty(card)) {
//				member = memberMapper.findByPhone(busId, cardNo);
//
//				if (CommonUtil.isNotEmpty(member)) {
//					card = cardMapper.selectByPrimaryKey(member.getMcId());
//				}
//			}
//
//			if (CommonUtil.isEmpty(card)) {
//				return false;
//			} else if (card.getCardstatus() == 1) {
//				return false;
//			}
//			return true;
//		} catch (Exception e) {
//			LOG.error("小程序查询会员信息异常", e);
//			return false;
//		}
//	}
//
//	@Override
//	public Map<String, Object> countMember(Integer busId) {
//		WxShop wxShop = wxShopMapper.selectMainShopByBusId(busId);
//
//		// 统计会员卡
//		List<Map<String, Object>> countCard = cardMapper.countMember(busId);
//		if (CommonUtil.isEmpty(countCard) || countCard.size() == 0) {
//			return null;
//		}
//		Integer onlinecount0 = 0; // 线上
//		Integer onlinecount1 = 0; // 线下
//		for (Map<String, Object> map : countCard) {
//			if (CommonUtil.isEmpty(map.get("shopId"))) {
//				if ("0".equals(CommonUtil.toString(map.get("online")))) {
//					onlinecount0 = CommonUtil.toInteger(map.get("countId"));
//				}
//
//				if ("1".equals(CommonUtil.toString(map.get("online")))) {
//					onlinecount1 = CommonUtil.toInteger(map.get("countId"));
//				}
//			}
//		}
//
//		List<Map<String, Object>> countMemberCard = new ArrayList<>();
//		for (Map<String, Object> map : countCard) {
//			if (CommonUtil.isEmpty(map.get("shopId"))) {
//				continue;
//			}
//			// 主店铺
//			if (wxShop.getId().equals(CommonUtil.toInteger(map.get("shopId")))) {
//				if ("0".equals(CommonUtil.toString(map.get("online")))) {
//					map.put("countId", CommonUtil.toInteger(map.get("countId"))
//							+ onlinecount0);
//				}
//
//				if ("1".equals(CommonUtil.toString(map.get("online")))) {
//					map.put("countId", CommonUtil.toInteger(map.get("countId"))
//							+ onlinecount1);
//				}
//			}
//			countMemberCard.add(map);
//		}
//
//		Map<Integer, Map<String, Object>> map1 = new HashMap<>();
//		for (Map<String, Object> map : countMemberCard) {
//			if (CommonUtil.isEmpty(map1.get(CommonUtil.toInteger(map
//					.get("shopId"))))) {
//				if ("0".equals(CommonUtil.toString(map.get("online")))) {
//					map.put("countId1", 0);
//				}
//
//				if ("1".equals(CommonUtil.toString(map.get("online")))) {
//					map.put("countId1", map.get("countId"));
//					map.put("countId", 0);
//
//				}
//				map1.put(CommonUtil.toInteger(map.get("shopId")), map);
//			}else{
//				if ("1".equals(CommonUtil.toString(map.get("online")))) {
//					map.put("countId1", map.get("countId"));
//				}
//				map.put("countId", map1.get(CommonUtil.toInteger(map
//					.get("shopId"))).get("countId"));
//
//				map1.put(CommonUtil.toInteger(map.get("shopId")), map);
//			}
//		}
//
//		Map<String, Object> memMap = null;
//
//		Integer online0 = 0;
//		Integer online1 = 0;
//		List<Map<String, Object>> list = new ArrayList<>();
//		for (Integer in : map1.keySet()) {
//			memMap = map1.get(in);// 得到每个key多对用value的值
//			online0 = online0 + CommonUtil.toInteger(memMap.get("countId"));
//			online1 = online1 + CommonUtil.toInteger(memMap.get("countId1"));
//			list.add(memMap);
//		}
//		Map<String, Object> maps = new HashMap<>();
//		maps.put("cardMember", list);
//		maps.put("online0", online0);
//		maps.put("online1", online1);
//		return maps;
//	}
//
//}
