//package com.gt.member.service.old.member;
//
//import com.gt.member.entity.BusUser;
//import com.gt.member.util.Page;
//
//import java.util.Map;
//
//
//public interface CardERPService {
//	/**
//	 * 分页查询非会员粉丝信息 只查询微信授权的
//	 * @return
//	 */
//	public Page findMemberIsNotCard(Integer busId, Map<String, Object> params);
//
//	//<!-------erp领取会员-------->
//	/**
//	 * erp领取会员
//	 * @param busUser
//	 * @param params
//	 * @return
//	 */
//	public Map<String, Object> linquMemberCard(BusUser busUser, Map<String, Object> params)throws Exception;
//
//	/**
//	 * 购买会员卡
//	 * @param busUser
//	 * @param params
//	 * @return
//	 */
//	public Map<String, Object> buyMemberCard(BusUser busUser, Map<String, Object> params)throws Exception;
//}
