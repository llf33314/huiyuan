///**
// * P 2016年10月11日
// */
//package com.gt.member.service.old.personCenter;
//
//import java.util.Date;
//import java.util.Map;
//
//
//
///**
// * 支付有礼接口
// * @author pengjiangli
// * @version
// * 创建时间:2016年10月11日
// *
// */
//public interface PaySuccessService {
//
//	/**
//	 *
//	 * @param model 1：微餐饮 2：商城 3：酒店 4：旅游 5：房产 6：优惠买单
//	 * @param publicId
//	 * @param memberId
//	 * @param totalMoney 消费金额
//	 * @param orderNo 订单号
//	 * @param date 订单时间
//	 * @param isSave是否保存数据 0不保存 1保存
//	 * @param isCount 是否统计当前订单 0不统计 1统计
//	 * @return code统计类型 -1:未设置支付有礼 0消费金额 1消费次数 2当次消费金额   orderMoney 订单金额或 次数或单次消费金额
//	 */
//	public Map<String, Object> findPaySuccess(Integer model, Integer publicId, Integer memberId, String orderNo, Date date, Double totalMoney, Integer isSave, Integer isCount);
//
//	/**
//	 * 保存支付有礼数据
//	 * @param param
//	 * @param wxPublicUsers
//	 */
//	public Map<String, Object> savePaySuccess(Map<String, Object> param, int busId)throws Exception ;
//}
