//package com.gt.member.service.old.memberpay.impl;
//
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//
//import org.apache.log4j.Logger;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.gt.dao.member.CardMapper;
//import com.gt.dao.member.UserConsumeMapper;
//import com.gt.dao.member.UserConsumeRefundMapper;
//import com.gt.entity.member.Card;
//import com.gt.entity.member.UserConsume;
//import com.gt.entity.member.UserConsumeRefundKey;
//import com.gt.service.memberpay.MemberTongjiService;
//import com.gt.util.CommonUtil;
//
///**
// * t_wx_user_consume  消费记录操作表
// *
// * @author pengjiangli
// *
// */
//@Service
//public class MemberTongJiServiceImpl implements MemberTongjiService{
//
//	private static final Logger LOG=Logger.getLogger(MemberTongJiServiceImpl.class);
//
//	@Autowired
//	private  CardMapper cardMapper;
//
//	@Autowired
//	private UserConsumeMapper userConsumeMapper;
//
//	@Autowired
//	private UserConsumeRefundMapper userConsumeRefundMapper;
//
//
//
//	@Override
//	public Map<String, Object> saveMemberConsume(Integer busUserId,Integer storeId,
//			Integer memberId, String cardNo, double totalMoney, Byte recordType,
//			Byte type, Integer integral, Integer fenbi, Integer uccount,
//			Integer discount, Double discountmoney, Integer orderid,
//			String uctable, Byte paymenttype, Byte paystatus, String givegift,
//			Integer giftCount, String orderCode,
//			Byte isend,Byte ischongzhi,Integer dvId,String disCountdepict,Byte cardType) throws Exception {
//		Map<String, Object> map=new HashMap<>();
//		try {
//			if(CommonUtil.isEmpty(busUserId) || CommonUtil.isEmpty(storeId) || CommonUtil.isEmpty(type) || CommonUtil.isEmpty(cardType)){
//				throw new Exception();
//			}
//			// 添加会员记录
//			UserConsume uc = new UserConsume();
//			uc.setBususerid(busUserId);
//			uc.setMemberid(memberId);
//			if(CommonUtil.isNotEmpty(cardNo)){
//				Card card = cardMapper.findCardByCardNo(busUserId, cardNo);
//				uc.setMcid(card.getMcId());
//				uc.setCtid(card.getCtId());
//				uc.setGtId(card.getGtId());
//			}else{
//				uc.setCtid(0);
//			}
//			uc.setRecordtype(recordType);
//			uc.setTotalmoney(totalMoney);
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
//			if(CommonUtil.isNotEmpty(isend)){
//				uc.setIsend(isend);
//			}
//			if(CommonUtil.isNotEmpty(ischongzhi)){
//				uc.setIschongzhi(ischongzhi);
//			}
//			if(1==isend ||"1".equals(isend)){
//				uc.setIsenddate(new Date());
//			}
//			if(CommonUtil.isNotEmpty(cardType) && -1!=cardType){
//				uc.setDvid(dvId);
//				uc.setDiscountdepict(disCountdepict);
//				uc.setCardtype(cardType);
//			}else{
//				uc.setCardtype(cardType);
//			}
//			userConsumeMapper.insertSelective(uc);
//			map.put("result", true);
//			map.put("message", "添加记录成功");
//		} catch (Exception e) {
//			LOG.error("添加saveMemberConsume记录异常", e);
//			throw new Exception();
//		}
//		return map;
//	}
//
//
//
//	@Override
//	public Map<String, Object> refundConsume(double totalMoney, Integer fenbi,
//			Double discountmoney, Integer orderid, String uctable,
//			String orderCode) throws Exception {
//		Map<String, Object> map=new HashMap<>();
//		try {
//			if (CommonUtil.isEmpty(totalMoney) || CommonUtil.isEmpty(fenbi)
//					|| CommonUtil.isEmpty(discountmoney)
//					|| CommonUtil.isEmpty(orderid)
//					|| CommonUtil.isEmpty(uctable)
//					|| CommonUtil.isEmpty(orderCode)) {
//				throw new Exception();
//			}
//			UserConsume uc = userConsumeMapper.findByOrderCode1(orderCode);
//			if (CommonUtil.isEmpty(uc)) {
//				throw new Exception();
//			}
//			Integer ucId = uc.getId();
//			uc.setId(null);
//			uc.setTotalmoney(totalMoney);
//			uc.setFenbi(fenbi);
//			uc.setDiscountmoney(discountmoney);
//			uc.setOrderid(orderid);
//			uc.setUctable(uctable);
//			userConsumeMapper.insertSelective(uc);
//			UserConsumeRefundKey uk = new UserConsumeRefundKey();
//			uk.setConsumeid(ucId);
//			uk.setRefundid(uc.getId());
//			userConsumeRefundMapper.insertSelective(uk);
//			map.put("result", true);
//			map.put("message", "添加记录成功");
//		} catch (Exception e) {
//			LOG.error("添加saveMemberConsume记录异常", e);
//			throw new Exception();
//		}
//		return map;
//	}
//
//
//
//	@Override
//	public Map<String, Object> updateIsend(Integer busId, Integer moduleType,
//			String orderCode) throws Exception {
//		Map<String, Object> map=new HashMap<>();
//		UserConsume userConsume=userConsumeMapper.findBybusIdAndOrderCode(busId, moduleType, orderCode);
//		if(CommonUtil.isEmpty(userConsume)){
//			throw new Exception();
//		}
//		try {
//			userConsume.setIsend((byte) 1);
//			userConsume.setIsenddate(new Date());
//			userConsumeMapper.updateByPrimaryKeySelective(userConsume);
//
//			map.put("result",true);
//			map.put("message", "修改成功");
//		} catch (Exception e) {
//			LOG.error("修改订单终结状态异常", e);
//			map.put("result",false);
//			map.put("message", "操作数据异常");
//		}
//		return map;
//	}
//
//
//
//	@Override
//	public Map<String, Object> updatePayStatus(Integer busId,
//			Integer moduleType, String orderCode) throws Exception {
//		Map<String, Object> map=new HashMap<>();
//		UserConsume userConsume=userConsumeMapper.findBybusIdAndOrderCode(busId, moduleType, orderCode);
//		if(CommonUtil.isEmpty(userConsume)){
//			throw new Exception();
//		}
//		try {
//			userConsume.setPaystatus((byte)3);
//			userConsume.setIsend((byte) 1);
//			userConsumeMapper.updateByPrimaryKeySelective(userConsume);
//
//			map.put("result",true);
//			map.put("message", "退单成功");
//		} catch (Exception e) {
//			LOG.error("修改退单异常", e);
//			map.put("result",false);
//			map.put("message", "修改退单异常");
//		}
//		return map;
//	}
//
//
//}
