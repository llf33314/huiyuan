package com.gt.member.service.memberpay;

import com.gt.common.entity.BusUserEntity;

import java.util.Map;


public interface MemberERPService {
	
	/**
	 * 查询会员的信息
	 * @param busUserEntity
	 * @param cardNoKey
	 * @param cardNo
	 * @param shopId
	 * @return
	 */
	public Map<String, Object> findMemberCard(BusUserEntity busUserEntity,
                                              String cardNoKey, String cardNo, Integer shopId);
	
	/**
	 * 卡券核销 储值卡付款
	 * @param json
	 * @return
	 */
	public Map<String, Object> payMemberCard(String json) throws Exception;
	
	
	/**
	 * 判断用户是否是会员  false 不是 true 是
	 * @param memberId 粉丝id
	 * @return
	 */
	public boolean isMemember(BusUserEntity busUserEntity,
                              String cardNoKey, String cardNo);
	
	
	/**
	 * 统计门店线上和线下会员数量
	 * @param busId
	 * @return
	 */
	public  Map<String, Object>  countMember(Integer busId);
}
