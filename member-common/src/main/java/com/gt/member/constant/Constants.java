package com.gt.member.constant;


public class Constants {

	/**
	 * 用户类型字典代码
	 */
	public final static String USER_TYPE_CODE="1004";
	
	/**
	 * 会员续费时间字典代码
	 */
	public final static String USER_UPGRADE_TIME_CODE="1005";
	
	/**
	 * 会员续费价格字典代码
	 */
	public final static String USER_UPGRADE_FEE_CODE="1006";
	
	/**
	 *试用期会员权限
	 */
	public final static String USER_LEVEL_TEST_MENUS="1022";
	
	
	/**
	 *基础会员权限
	 */
	public final static String USER_LEVEL_ORDINARY_MENUS="1023";
	/**
	 *升级会员权限
	 */
	public final static String USER_LEVEL_SENIOR_MENUS="1024";
	/**
	 *高级会员权限
	 */
	public final static String USER_LEVEL_IMPERIAL_MENUS="1025";
	/**
	 *至尊会员权限
	 */
	public final static String USER_LEVEL_DIAMONDS_MENUS="1026";
	
	
	/**
	 *白金会员权限
	 */
	public final static String USER_LEVEL_BAIJIN_MENUS="1084";
	
	/**
	 *钻石会员权限
	 */
	public final static String USER_LEVEL_ZUANSHI_MENUS="1085";
	
	
	/**
	 * 会员级别单价字典代码
	 */
	public final static String USER_LEVEL_FEE_CODE="1008";
	
	
	/**
	 * 会员升级订单号前缀字典代码
	 */
	public final static String USER_UPGRADE_SYSORDERNO_NUM_CODE="00001000";
	
	/**
	 * 会员升级产品号前缀字典代码
	 */
	public final static String USER_UPGRADE_PRODUCTID_NUM_CODE="00001100";
	
	
	/**
	 * 商家粉币充值订单号前缀字典代码
	 */
	public final static String USER_RECHARGE_SYSORDERNO_NUM_CODE="00002000";
	
	/**
	 * 商家粉币充值产品号前缀字典代码
	 */
	public final static String USER_RECHARGE_PRODUCTID_NUM_CODE="00002100";
	
	/**
	 * 商家流量充值订单号前缀字典代码
	 */
	public final static String USER_FLOW_RECHARGE_SYSORDERNO_NUM_CODE="00003000";
	
	
	/**
	 * 商家流量充值产品号前缀字典代码
	 */
	public final static String USER_FLOW_RECHARGE_PRODUCTID_NUM_CODE="00003100";
	
	
	/**
	 * 商家购买短信订单号前缀字典代码
	 */
	public final static String USER_SMS_COUNT_SYSORDERNO_NUM_CODE="00005000";
	
	
	/**
	 * 商家购买短信产品号前缀字典代码
	 */
	public final static String USER_SMS_COUNT_PRODUCTID_NUM_CODE="00005100";
	
	
	/**
	 * 商家红包订单号前缀字典代码
	 */
	public final static String USER_RED_PACKETS_SYSORDERNO_NUM_CODE="00004000";
	
	/**
	 * 会员升级时的订单状态 001:等待用户确认下单
	 */
	public final static String USER_ORDER_STATUS_001="001";
	
	
	/**
	 * 会员升级时的订单状态 002:确认下单
	 */
	public final static String USER_ORDER_STATUS_002="002";
	
	
	/**
	 * 会员升级时的订单状态 003:完成订单
	 */
	public final static String USER_ORDER_STATUS_003="003";
	
	
	
	/**
	 * 会员升级时的订单状态 004:取消订单
	 */
	public final static String USER_ORDER_STATUS_004="004";
	
	
	/**
	 * 会员升级时的订单状态 005:订单超时
	 */
	public final static String USER_ORDER_STATUS_005="005";
	
	
	/**
	 * 会员升级时的订单状态 006:订单支付失败
	 */
	public final static String USER_ORDER_STATUS_006="006";
	
	
	/**
	 * 用户授权失败
	 */
	public final static String USER_GRANT_ERR_CODE="007";
	
	
	/**
	 * 参数错误
	 */
	public final static String PARAMETER_ERR_CODE="008";
	
	
	/**
	 * 网址名称
	 */
	public final static String doMainName="多粉";
	
	/**
	 * 微商城商品数量跟用户等级关联字典
	 */
	public final static String MALL_PRO_NUM_CODE="1094";
	
	
	
	/**
	 * 错误描述
	 * @param code
	 * @return
	 */
	public static String getMsg(String code){
		String msg="";
		switch (code) {
		case USER_ORDER_STATUS_004:
			msg="用户取消订单";
			break;
		case USER_ORDER_STATUS_005:
			msg="订单超时";
			break;
		case USER_ORDER_STATUS_006:
			msg="订单支付失败";
			break;
		case USER_GRANT_ERR_CODE:
			msg="用户授权失败";
			break;
		case PARAMETER_ERR_CODE:
			msg="参数错误，请稍后重试...";
			break;
		default:
			msg="未知错误，请联系客服";
			break;
		}
		
		return msg;
	}
	
	/**
	 * 优惠券类型-代金
	 */
	public final static String CASH="CASH";
	
	/**
	 * 优惠券类型-折扣
	 */
	public final static String DISCOUNT="DISCOUNT";
	
}
