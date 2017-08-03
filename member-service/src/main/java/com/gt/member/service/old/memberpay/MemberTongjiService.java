package com.gt.member.service.old.memberpay;

import java.util.Map;

/**
 * t_wx_user_consume  消费记录操作表 
 * 
 * @author pengjiangli
 *
 */
public interface MemberTongjiService {

	/**
	 *  添加消费记录
	 * @param busUserId 商户id  必填
	 * @param storeId 门店 必填
	 * @param memberId 选填
	 * @param cardNo 会员卡号 选填
	 * @param totalMoney 消费金额（应付总金额）   选填
	 * @param recordType 记录类型  0:积分记录 1:充值记录 2:消费记录  选填
	 * @param type 消费类型 0:积分卡消费 1:储值卡消费2:折扣卡消费3:次卡消费  
	 * 4:积分兑换粉币5:积分兑换商品6:游客消费7:充值记录8:让红包飞 
	 * 9:大转盘10:刮刮乐11:红包   字典表1197 新增描述 自己去字典表添加   必填
	 * @param integral 消费积分 必填
	 * @param fenbi 消费粉币   必填
	 * @param uccount  消费次数(次卡) 选填
	 * @param discount 折扣数 选填
	 * @param discountmoney 折扣后金额 选填
	 * @param orderid  订单id 选填
	 * @param uctable  详情表名 选填
	 * @param paymenttype 支付方式 字典表1198 0支付宝 1微信 2银联 3线下充值 4货到付款 5储值卡支付 6积分支付 7粉币支付 8到店支付 9找人代付  10现金支付 11分期支付
	 * @param paystatus  支付状态 0未支付 1已支付 2支付失败 3退单 选填
	 * @param givegift  赠送物品名 选填
	 * @param giftCount 赠送数量 选填
	 * @param orderCode  订单号微信或支付宝 选填
	 * @param isend 订单是否已终结（不能退单）0未终结 1终结
	 * @param ischongzhi 是否属于充值 0否 1是
	 * @param dvId  卡券id(创建卡券模板id)
	 * @param disCountdepict 卡券code值（多张逗号隔开）
	 * @param cardType 卡券类型 -1没有卡券 0微信卡券 1多粉卡券
	 * @throws Exception
	 */
	public Map<String, Object> saveMemberConsume(Integer busUserId, Integer storeId, Integer memberId, String cardNo, double totalMoney,
                                                 Byte recordType, Byte type, Integer integral, Integer fenbi,
                                                 Integer uccount, Integer discount, Double discountmoney,
                                                 Integer orderid, String uctable, Byte paymenttype, Byte paystatus,
                                                 String givegift, Integer giftCount, String orderCode,
                                                 Byte isend,
                                                 Byte ischongzhi, Integer dvId, String disCountdepict, Byte cardType)throws Exception ;
	
	
	/**
	 * 商城退款记录
	 * @param totalMoney 应退金额（应退总金额）  必填
	 * @param fenbi 消费粉币   必填
	 * @param discountmoney 实退金额 必填
	 * @param orderid  订单id 必填
	 * @param uctable  详情表名 必填
	 * @param orderCode  订单号微信或支付宝 必填
	 * @throws Exception
	 */
	public Map<String, Object> refundConsume(double totalMoney, Integer fenbi, Double discountmoney,
                                             Integer orderid, String uctable, String orderCode)throws Exception;
	
	
	/**
	 * 修改 订单已终结 和时间
	 * @param busId 商家Id
	 * @param moduleType 行业
	 * @param orderCode 订单号
	 * @return
	 */
	public Map<String, Object> updateIsend(Integer busId, Integer moduleType, String orderCode) throws Exception ;


	/**
	 *  订单退款
	 * 
	 * @param busId
	 * @param moduleType
	 * @param orderCode
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> updatePayStatus(Integer busId, Integer moduleType, String orderCode) throws Exception ;
}
