//package com.gt.member.service.old.member.impl;
//
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.apache.log4j.Logger;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import com.gt.dao.member.CardBuyMapper;
//import com.gt.dao.member.CardMapper;
//import com.gt.dao.member.GiveRuleMapper;
//import com.gt.dao.member.GradeTypeMapper;
//import com.gt.dao.member.MemberMapper;
//import com.gt.dao.member.MemberParameterMapper;
//import com.gt.dao.member.UserConsumeMapper;
//import com.gt.entity.common.BusUser;
//import com.gt.entity.member.Card;
//import com.gt.entity.member.CardBuy;
//import com.gt.entity.member.GiveRule;
//import com.gt.entity.member.GradeType;
//import com.gt.entity.member.Member;
//import com.gt.entity.member.MemberParameter;
//import com.gt.entity.member.UserConsume;
//import com.gt.controller.common.dict.DictService;
//import com.gt.controller.member.CardERPService;
//import com.gt.util.CommonUtil;
//import com.gt.util.DateTimeKit;
//import com.gt.util.Page;
//
//@Service
//public class CardERPServiceImpl implements CardERPService{
//
//	private static final Logger LOG=Logger.getLogger(CardERPServiceImpl.class);
//
//	@Autowired
//	private MemberMapper memberMapper;
//
//	@Autowired
//	private CardMapper cardMapper;
//
//	@Autowired
//	private MemberParameterMapper memberParameterMapper;
//
//
//	@Autowired
//	private GradeTypeMapper gradeTypeMapper;
//
//	@Autowired
//	private GiveRuleMapper giveRuleMapper;
//
//	@Autowired
//	private UserConsumeMapper userConsumeMapper;
//
//	@Autowired
//	private CardBuyMapper cardBuyMapper;
//
//	@Autowired
//	private DictService dictService;
//
//	@Override
//	public Page findMemberIsNotCard(Integer busId, Map<String, Object> params) {
//		try {
//			params.put("curPage", CommonUtil.isEmpty(params.get("curPage")) ? 1
//					: CommonUtil.toInteger(params.get("curPage")));
//			int pageSize = 16;
//
//
//			Date date=DateTimeKit.parseDate(DateTimeKit.format(new Date(), "yyyy-MM-dd")+" 00:00:00");
//
//			int rowCount = memberMapper.countMemberIsNotCard(busId,date);
//			Page page = new Page(CommonUtil.toInteger(params.get("curPage")),
//					pageSize, rowCount, "/memberERP/erpMember.do");
//
//			params.put("firstResult", pageSize
//					* ((page.getCurPage() <= 0 ? 1 : page.getCurPage()) - 1));
//
//			List<Map<String, Object>> members = memberMapper
//					.findMemberIsNotCard(busId,
//							CommonUtil.toInteger(params.get("firstResult")),
//							pageSize,date);
//
//			List<Map<String, Object>> memberList = new ArrayList<Map<String, Object>>();
//			for (Map<String, Object> map : members) {
//				try {
//					byte[] bytes = (byte[]) map.get("nickname");
//					map.put("nickname", new String(bytes, "UTF-8"));
//				} catch (Exception e) {
//					map.put("nickname", null);
//				}
//				memberList.add(map);
//			}
//			page.setSubList(memberList);
//			return page;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
//
//	/**
//	 * uc端注册并领取会员卡
//	 * @throws Exception
//	 */
//	@Transactional(rollbackFor=Exception.class)
//	@Override
//	public Map<String, Object> linquMemberCard(BusUser busUser,
//			Map<String, Object> params) throws Exception {
//		Map<String, Object> returnMap = new HashMap<>();
//		try {
//
//			int count = cardMapper.countCardisBinding(busUser.getId());
//
//			String dictNum = dictService.dictBusUserNum(busUser.getId(),
//					busUser.getLevel(), 4, "1093"); // 多粉 翼粉
//			if (CommonUtil.toInteger(dictNum) < count) {
//				returnMap.put("code", -1);
//				returnMap.put("message", "会员卡已领取完!");
//				return returnMap;
//			}
//
//			String phone = CommonUtil.toString(params.get("phone"));
//
//			Member member=memberMapper.findByPhone(busUser.getId(), phone);
//			if(CommonUtil.isEmpty(member)){
//				// 新增用户
//				member = new Member();
//				member.setPhone(phone);
//				member.setBusid(busUser.getId());
//				member.setLoginmode((byte) 1);
//				member.setNickname("Fans_" + phone.substring(4));
//				memberMapper.insertSelective(member);
//				MemberParameter memberParameter = memberParameterMapper
//						.findByMemberId(member.getId());
//				if (CommonUtil.isEmpty(memberParameter)) {
//					MemberParameter mp = new MemberParameter();
//					mp.setMemberid(member.getId());
//					memberParameterMapper.insertSelective(mp);
//				}
//			}
//
//			Integer applyType=CommonUtil.toInteger(params.get("applyType"));
//			Integer ctId = CommonUtil.toInteger(params.get("ctId"));
//			Integer gtId=CommonUtil.toInteger(params.get("gtId"));
//			Integer shopId = CommonUtil.toInteger(params.get("shopId"));
//			if (applyType != 3) {
//				//非购买会员卡  直接分配会员卡
//				Card card = new Card();
//				card.setIschecked((byte) 1);
//				card.setCardno(CommonUtil.getCode());
//				card.setCtId(ctId);
//				if (card.getCtId() == 4) {
//					card.setExpiredate(new Date());
//				}
//
//				card.setSystemcode(CommonUtil.getNominateCode());
//				card.setApplytype((byte) 0);
//
//				// 根据卡片类型 查询第一等级
//				List<Map<String, Object>> gradeTypes = gradeTypeMapper
//						.findBybusIdAndCtId3(member.getBusid(), ctId);
//
//				if (gradeTypes != null && gradeTypes.size() > 0) {
//					card.setGtId(Integer.parseInt(gradeTypes.get(0)
//							.get("gt_id").toString()));
//					GiveRule giveRule = giveRuleMapper
//							.findBybusIdAndGtIdAndCtId(
//									member.getBusid(),
//									Integer.parseInt(gradeTypes.get(0)
//											.get("gt_id").toString()), ctId);
//					card.setGrId(giveRule.getGrId());
//				}
//				card.setBusid(member.getBusid());
//				card.setCardno(CommonUtil.getCode());
//				card.setReceivedate(new Date());
//				card.setIsbinding((byte) 1);
//
//				card.setShopid(shopId);
//				card.setOnline((byte) 0);
//				cardMapper.insertSelective(card);
//
//				Member member1 = new Member();
//				member1.setMcId(card.getMcId());
//				member1.setId(member.getId());
//				memberMapper.updateByPrimaryKeySelective(member1);
//				returnMap.put("code", 1);
//				returnMap.put("message", "领取成功");
//
//			}else{
//				returnMap.put("memberId", member.getId());
//				returnMap.put("code", 2);
//				returnMap.put("message", "未支付");
//			}
//
//		} catch (Exception e) {
//			LOG.error("erp 领取会员卡异常", e);
//			throw new Exception();
//		}
//		return returnMap;
//	}
//
//	@Transactional(rollbackFor=Exception.class)
//	@Override
//	public Map<String, Object> buyMemberCard(BusUser busUser,
//			Map<String, Object> params) throws Exception {
//		Map<String, Object> returnMap=new HashMap<>();
//
//		Integer memberId=CommonUtil.toInteger(params.get("memberId"));
//		Integer ctId=CommonUtil.toInteger(params.get("ctId"));
//		Integer gtId=CommonUtil.toInteger(params.get("gtId"));
//		Integer shopId=CommonUtil.toInteger(params.get("shopId"));
//		Integer payType=CommonUtil.toInteger(params.get("payType"));
//		//购买会员卡
//		GradeType gradeType = gradeTypeMapper.selectByPrimaryKey(gtId);
//		if (CommonUtil.isEmpty(gradeType)
//				|| CommonUtil.isEmpty(gradeType.getBuymoney() <= 0)) {
//			throw new Exception();
//		}
//		// 添加会员记录
//		UserConsume uc = new UserConsume();
//		uc.setMemberid(memberId);
//		uc.setCtid(ctId);
//		uc.setRecordtype((byte) 2);
//		uc.setUctype((byte) 13);
//		uc.setTotalmoney(gradeType.getBuymoney());
//		uc.setCreatedate(new Date());
//		uc.setPaystatus((byte) 0);
//		uc.setDiscount(100);
//		uc.setDiscountmoney(gradeType.getBuymoney());
//		String orderCode = CommonUtil.getMEOrderCode();
//		uc.setOrdercode(orderCode);
//		uc.setGtId(gtId);
//		uc.setBususerid(busUser.getId());
//		uc.setStoreid(shopId);
//		uc.setStoreid(shopId);
//		if(payType==1){
//			//现金
//			uc.setPaystatus((byte) 1);
//			uc.setPaymenttype((byte)10);
//			userConsumeMapper.insertSelective(uc);
//
//			CardBuy cardbuy = new CardBuy();
//			cardbuy.setBuymoney(gradeType.getBuymoney());
//			cardbuy.setCtid(ctId);
//			cardbuy.setMemberid(memberId);
//			cardbuy.setBusid(busUser.getId());
//			cardBuyMapper.insertSelective(cardbuy);
//
//
//			// 添加会员卡
//			Card card = new Card();
//			card.setIschecked((byte) 1);
//			card.setCardno(CommonUtil.getCode());
//			card.setCtId(ctId);
//
//			card.setSystemcode(CommonUtil.getNominateCode());
//			card.setApplytype((byte) 3);
//			card.setMemberid(memberId);
//			card.setGtId(gtId);
//			GiveRule giveRule = giveRuleMapper.findBybusIdAndGtIdAndCtId(
//					busUser.getId(),
//					card.getGtId(), card.getCtId());
//			card.setGrId(giveRule.getGrId());
//
//			card.setCardno(CommonUtil.getCode());
//			card.setBusid(busUser.getId());
//			card.setReceivedate(new Date());
//			card.setIsbinding((byte) 1);
//
//			if (card.getCtId() == 5) {
//				if (CommonUtil.isNotEmpty(gradeType.getBalance())) {
//					card.setFrequency(new Double(gradeType.getBalance()).intValue());
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
//			Member member = new Member();
//			member.setId(memberId);
//			member.setIsbuy((byte) 1);
//			member.setMcId(card.getMcId());
//			memberMapper.updateByPrimaryKeySelective(member);
//			String balance = null;
//			if (card.getCtId() == 5) {
//				balance = card.getFrequency() + "次";
//			} else {
//				balance = card.getMoney() + "元";
//			}
////			memberPayService.saveCardRecordNew(card.getMcId(), (byte) 1,  gradeType.getBuymoney()+ "元", "购买会员卡", card.getPublicId(),
////					balance, card.getCtId(), 0.0);
//
//			// 新增会员短信通知
//			member = memberMapper.selectByPrimaryKey(memberId);
////			systemMsgService.sendNewMemberMsg(member);
//
//			returnMap.put("code", 1);
//			returnMap.put("message","领取成功");
//		}else if(payType==2){
//			//扫码支付
//			userConsumeMapper.insertSelective(uc);
//
//			returnMap.put("orderid", orderCode);
//			returnMap.put("businessUtilName", "alipayNotifyUrlBuinessServiceBuyCard");
//			returnMap.put("totalFee", gradeType.getBuymoney());
//			returnMap.put("model", 7);
//			returnMap.put("busId", busUser.getId());
//			returnMap.put("appidType", 0);
//			returnMap.put("orderNum", orderCode);
//			returnMap.put("memberId", memberId);
//			returnMap.put("code", 2);
//			returnMap.put("message","领取成功");
//		}
//		return returnMap;
//	}
//}
