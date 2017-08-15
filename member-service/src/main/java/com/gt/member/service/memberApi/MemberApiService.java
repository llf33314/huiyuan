package com.gt.member.service.memberApi;

import com.gt.common.entity.BusUser;
import com.gt.member.entity.*;
import com.gt.member.exception.BusinessException;
import com.gt.member.service.memberApi.entityBo.PaySuccessBo;
import com.gt.member.util.Page;

import java.util.List;
import java.util.Map;

/*
 * 会员支付接口
 *
 * @author pengjiangli
 * @version
 * 创建时间:2016年4月5日
 *
 */
public interface MemberApiService {

    /**
     * 查询粉丝信息
     *
     * @param memberId
     *
     * @return
     */
    public Member findByMemberId( Integer memberId ) throws BusinessException;

    /**
     * 当前立即赠送物品
     *
     * @param orderId
     * @param itemName
     * @param type     0不赠送 1赠送
     *
     * @throws Exception
     */
    public void findGiveRule( String orderId, String itemName, byte type ) throws BusinessException;

    /*
 * 会员回调接口
     */
    public void memberCallBack( String orderId, Byte payStatus ) throws BusinessException;

    /*
     * 根据会员id查询会员卡信息 如果是折扣卡 返回折扣值
     * @param memberId
     * @return
     */
    public Double findCardTypeReturnDiscount( Integer memberId ) throws BusinessException;

    /*
     * 会员储值卡消费
     * @param orderId
     * @return
     */
    public void storePay( Integer memberId, double totalMoney ) throws BusinessException;

    /*
     * 可供商家选择的会员卡
     * @param publicId 公众号id
     * @return
     */
    public List< MemberCardtype > findMemberCard_1( Integer busId );

    /*
     * 判断用户是否是会员  false 不是 true 是
     * @param memberId 粉丝id
     * @return
     */
    public void isMemember( Integer memberId ) throws BusinessException;

    /*
     * 判断用户的卡类型 -1卡片数据未找到 1积分卡 2折扣卡 3储值卡 4时效卡 5次卡
     * @param memberId
     * @return
     */
    public Integer isCardType( Integer memberId )throws  BusinessException;

    /*
     * 获取card信息
     * @param memberId
     * @return
     */
    public MemberCard findCardByMemberId( Integer memberId );

    /*
     * 根据粉丝id 查询
     * 积分卡规则：
     *	传入价格 返回赠送积分  Map中取值map.get("number")
     * 折扣卡规则：
     *	传入价格， Map中取值map.get("disCountmoney")折后金额  Map中取值map.get("discount") 折扣数0~1
     * 储值卡：
     *	返回剩余金额   Map中取值map.get("balance") 剩余金额
     *
     * @param memberId 粉丝id
     * @param totalMoney 消费总金额
     * @return map
     */
    public Map< String,Object > findCardGiveRule( Integer memberId, Double totalMoney );

    /*
     * 支付成功回调
     * @param phone 会员手机号
     * @param orderCode 订单号(自定义订单号)
     */
    public void backPay( String phone, String orderCode ) throws Exception;

    /*
     * 储值卡退款订单
     * refundMoney
     * @param
     */
    public void chargeBack( Integer memberId, double refundMoney ) throws BusinessException;

    /*
     * 退款成功回调  添加card操作记录
     * @param orderNo
     */
    public void refundBack( String orderNo );

    /*
     * 发送赠送物品给用户
     * @param orderNo 自定义订单号
     * @throws Exception
     */
    public void giveGood( String orderNo ) throws Exception;

    public Map< String,Object > findNextGrade( Integer busId, Integer ctId, Integer gtId, Integer integral, double totalmoney );

    /*
	 * 新数据接口
	 *
	 *
	 * 保存会员卡积分、粉币、流量、 消费记录
	 * @param cardId 会员卡id
	 * @param recordType 类型 0充值和消费  1积分 2 粉币  3流量
	 * @param number  消费和赠送积分或粉币流量   描述例如:  10积分  或 10粉币 10元
	 * @param itemName 描述
	 * @param busId 必填
	 * @param balance 可不填
	 * @param ctId 可不填
	 * @param amount 积分 粉币 流量 消费或赠送值  正数赠送 负数消费
	 * @return
	 */
    public void saveCardRecordNew( Integer cardId, Byte recordType, String number, String itemName, Integer busId, String balance, Integer ctId, double amount );

    /*
     *  购买会员卡回调
     * @param orderId
     * @param payStatus
     */
    public void buyCardCallBack( String orderId, Byte payStatus );

    /*
     * 根据订单号添加赠送物品记录 延迟送
     *
     * @param orderNo
     *            订单号
     * @param phone
     *            手机号码
     * @throws Exception
     */
    public void findGiveRuleDelay( String orderNo ) throws Exception;

    /*
     * 积分兑换物品
     * @param busId
     * @param cardNo 卡号
     * @param intergral 积分
     * @param gift 兑换物品名称
     * @return
     */
    public Map< String,Object > intergralConsume( int busId, String cardNo, Integer intergral, String gift ) throws Exception;

    /*
     * 赠送会员积分数量
     * @param member
     * @param intergral
     * @return
     */
    public void updateMemberIntergral( Integer memberId, Integer intergral ) throws BusinessException;

    /*
     * 判断会员是否购买了会员卡
     * @param member
     * @return
     */
    public Map< String,Object > findBuyCard( Member member );

    /*
     * 购买会员卡成功调用
     * @param member
     * @param money
     * @param ctId
     * @return
     */
    public Map< String,Object > buyCard( Member member, Double money, Integer ctId );

    /*
     * 归还商家粉币
     * @param busId
     * @param fans_currency
     * @return
     */
    public Map< String,Object > returnfansCurrency( Integer busId, Double fans_currency );

    /*
     * 根据订单号添加赠送物品记录 （只添加记录  不做操作）
     *
     * @param orderId
     *            订单号
     * @param itemName
     *            物品名称
     *
     * @throws Exception
     */
    public void saveGiveConsume( String phone, String orderId ) throws Exception;

    /*
     * 会员日
     * @param busId
     * @param ctId
     * @return
     */
    public MemberDate findMemeberDate( Integer busId, Integer ctId );

    /*
     * 粉币抵扣
     * @param request
     * @param member
     * @param busId
     * @param Fenbimoney 粉币抵扣金额
     * @return
     */
    public Map< String,Object > reduceFansCurrencyMoney( Member member, Integer busId, Double Fenbimoney ) throws Exception;

    /*
     * 粉币抵扣
     * @param request
     * @param member
     * @param busId
     * @param Fenbi 粉币
     * @return
     */
    public void reduceFansCurrency( Member member, Integer busId, Double fenbi ) throws BusinessException;

    /*
     * 查询卡片页面信息
     * @param memberId
     * @return
     */
    public MemberGradetype findGradeType( Integer memberId );

    /*
     *  扣除用户粉币
     * @param request
     * @param member
     * @param busId
     * @param fenbi
     * @return
     * @throws Exception
     */
    public Map< String,Object > updateMemberFansCurrency( Integer memberId, Integer busId, Double fenbi ) throws Exception;

    /*
	 * 粉币计算
	 *
	 * @param totalMoney
	 *            能抵抗消费金额
	 * @param fans_currency
	 *            粉币值
	 * @return 返回兑换金额
	 */
    public Double currencyCount( Double totalMoney, Double fans_currency );

    /*
     * 计算抵扣粉币
     * @param fenbiMoney 粉币抵扣金额
     * @param busId 商家id
     * @return 消耗粉币值
     */
    public Double deductFenbi( Double fenbiMoney, int busId );

    /*
     * 积分兑换返回兑换金额
     * @param totalMoney
     * @param integral
     * @param busId
     * @return
     */
    public Double integralCount( Double totalMoney, Double integral, int busId );

    /*
     * 计算抵扣积分
     * @param money 积分抵扣金额
     * @param busId 商家id
     * @return
     */
    public Integer deductJifen( Double jifenMoney, int busId );

    /*
     * 会员卡充值
     * @param memberId
     * @param cardNo
     * @param money
     * @param count
     * @return
     * @throws Exception
     */
    public Map< String,Object > rechargeMember( Integer memberId, String cardNo, Double money, Integer count ) throws Exception;

    /*
     * 根据openId 查询会员信息 包括 卡类型 积分抵扣值 粉币 折扣 余额(pc付款功能)
     * @param openId
     * @return  map中包含值
     * result=0非会员 、1会员
     * jifenMoney=抵扣积分金额
     * fenbiMoney=抵扣粉币金额
     * discount=折扣值  0到1
     * leixing=卡类型
     * dengji=卡等级
     * card=卡对象   （储值卡余额card.money）
     * member=对象
     */
    public Map< String,Object > findMember( String openId );

    /*
     * 扫码支付 接口
     * @param orderCode 订单号
     * @param openId
     * @param orderMoney 订单金额
     * @param payType 支付方式 0:扫码方式 1：储值卡方式
     * @param fenbiMoney 粉币抵扣金额
     * @param jifenMoney 积分抵扣金额
     * @return
     * result 0： 需支付  1:已支付
     * payMoney  需继续支付金额
     */
    public Map< String,Object > weChatPayment( String orderCode, String openId, Double orderMoney, Integer payType, Double fenbiMoney, Double jifenMoney ) throws Exception;

    public void backWeChatPayment( String orderCode ) throws Exception;

    /*
     * 撤单
     * @param orderCode
     * @return
     * @throws Exception
     */
    public Map< String,Object > cancelOrder( String orderCode ) throws Exception;

    /*
     * 小程序绑定手机号码
     * @return
     */
    public Member bingdingPhone( Integer memberId, String phone, String code, Integer busId ) throws BusinessException;

    /*
     *
     * 会员卡充值查询接口
     * @param busId 商家id
     * @param cardNo 卡号
     * @return  money 充值金额, giveCount 赠送数量, number 次数(次卡), ctId, isDate 是否会员日
     */
    public List< Map< String,Object > > findMemberCardRecharge( Integer busId, String cardNo );

    /*
     * 会员卡充值充值成功
     * @param busId
     * @param cardNo 卡号
     * @param money 充值金额
     * @return
     */
    public Map< String,Object > successReCharge( Integer busId, String cardNo, Double money ) throws Exception;


	/*
	 * 分页查询会员充值记录
	 * @param busId
	 * @param params 参数  curPage 页面条数   memberId 粉丝id  startDate 开始时间 endDate 结束时间
	 * @return
	 */

    public Page findConsumeByMemberId( Integer busId, Map< String,Object > params );

    /*
     * 根据卡号查询会员卡信息
     * @param cardNo
     * @return
     */
    public Map< String,Object > findMemberByCardNo( String cardNo );

	/*
	 * 查询积分规则对象
	 * @param busId
	 * @return  返回对象中 能用的参数有:integralRatio 积分比例   changeMoney 多少积分抵扣多少金额值   startMoney 启兑金额
	 * 算法   integralRatio*startMoney大于用户积分  可以使用
	 * 用户积分兑换钱算法   用户积分/积分比例*抵扣金额
	 *
	 */

    public PublicParameterset findjifenRule( Integer busId );

    /*
     * 粉币抵扣规则
     * @param busId
     * @return  map中  ratio 比例 1元:粉币 startMoney   最低10元才能使用粉币兑换
     */
    public Map< String,Object > fenbiRule( Integer busId );

	/*
	 * 根据memberId 查询member之前所有id
	 * @param memberId
	 * @return
	 */

    public List< Integer > findMemberIds( Integer memberId );

    /*
     * 流量兑换失败 回滚
     * @param orderCode
     * @return
     */
    public Map< String,Object > backFlow( String orderCode );

    /*
     *  添加消费记录
     * @param busUserId 商户id  必填
     * @param memberId 选填
     * @param cardNo 会员卡号 选填
     * @param money 消费金额   选填
     * @param recordType 记录类型  0:积分记录 1:充值记录 2:消费记录  必填
     * @param type 消费类型 0:积分消费 1:储值卡消费2:折扣卡消费3:次卡消费
     * 4:积分兑换粉币5:积分兑换商品6:游客消费7:充值记录8:让红包飞
     * 9:大转盘10:刮刮乐11:红包    必填
     * @param integral 消费积分 选填
     * @param fenbi 兑换粉币数量   选填
     * @param uccount  消费次数(次卡) 选填
     * @param discount 折扣数 选填
     * @param discountmoney 折扣后金额 选填
     * @param orderid  订单id 选填
     * @param uctable  详情表名 选填
     * @param paymenttype 支付方式 0支付宝 1微信 2银联 3线下支付 必填
     * @param paystatus  支付状态 0未支付 1已支付 2支付失败 选填
     * @param givegift  赠送物品名 选填
     * @param giftCount 赠送数量 选填
     * @param orderCode  订单号微信或支付宝 选填
     * @throws Exception
     */
    public Map< String,Object > saveMemberConsume( Integer busUserId, Integer memberId, String cardNo, double money, Byte recordType, Byte type, Integer integral, Integer fenbi,
		    Integer uccount, Integer discount, Double discountmoney, Integer orderid, String uctable, Byte paymenttype, Byte paystatus, String givegift, Integer giftCount,
		    String orderCode );

    /*
     * 根据手机号码和门店查询会员信息
     * @param cardNo
     * @param shopId
     * @return  result  true
     * card Map中有值  mc_id,
     * 		cardNo 卡号,
     *		ct_id 会员卡类型,ct.ct_name 会员卡类型名称,
     *		frequency 次卡（次数）,
     *		expireDate  到期时间 (时效卡),
     *   	gt_grade_name 等级
     *   	money, 金额（储值卡）
     */
    public Map< String,Object > findMemberShopId( String phone, Integer busId, Integer shopId ) throws Exception;

    /*
     * 查询商家拥有的免费领取的会员卡信息
     * @param busId
     * @return
     */
    public List< Map< String,Object > > findGradeTypeByApplyType( Integer busId );

    /*
     * 小馋猫餐饮系统（员工端分配会员卡）
     * @param params
     * @return
     */
    public Map< String,Object > getMemberCardByXiaochangmao( Map< String,Object > params ) throws Exception;

    /*
     * 修改会员积分数量  不添加积分记录
     * @param member
     * @param intergral 负数扣除
     * @return
     */
    public Map< String,Object > updateIntergral( Integer memberId, Integer intergral );

    /*
     * 判断储值卡 金额是否充足
     * @param memberId
     * @param totalMoney
     * @return
     */
    public boolean isAdequateMoney( Integer memberId, double totalMoney );

    /**
     * 根据粉丝id获取优惠券信息
     *
     * @param memberId
     * @param shopId
     *
     * @return
     * @throws Exception
     */
    public Map< String,Object > findMemberCardByMemberId( Integer memberId, Integer shopId ) throws BusinessException;

    /*
	 * 查询会员的信息
	 * @param busUser
	 * @param cardNoKey
	 * @param cardNo
	 * @param shopId
	 * @return
	 */
    public Map< String,Object > findMemberCard( Integer busId, String cardNoKey, String cardNo, Integer shopId ) throws BusinessException;

    /*
     * 卡券核销 储值卡付款
     * @param json
     * @return
     */
    @Deprecated
    public void payMemberCard( String json ) throws BusinessException;


	/*
	 * 判断用户是否是会员  false 不是 true 是
	 * @param memberId 粉丝id
	 * @return
	 */

    public boolean isMememberByApplet( BusUser busUser, String cardNoKey, String cardNo );

    /**
     * 统计门店线上和线下会员数量
     *
     * @param busId
     *
     * @return
     */
    public Map< String,Object > countMember( Integer busId ) throws Exception;

    //<!-----------------整理出来的会员信息----------------->

    /**
     * 支付成功业务处理
     *
     * @param paySuccessBo
     *
     * @throws Exception
     */
    public void paySuccess( PaySuccessBo paySuccessBo ) throws BusinessException;

    /**
     * 订单退款
     *
     * @param orderNo     订单号
     * @param refundMoney 退款金额
     *
     * @return
     * @throws Exception
     */
    public void refundMoney( Integer busId, String orderNo, double refundMoney ) throws BusinessException;

    /**
     * 判断储值卡金额是否充足
     *
     * @param memberId
     * @param money
     *
     * @throws BusinessException
     */
    public void isAdequateMoney( Integer memberId, Double money ) throws BusinessException;

    /**
     * 查询粉丝集合
     *
     * @param busId
     * @param memberIds
     *
     * @return
     * @throws BusinessException
     */
    public List< Map< String,Object > > findMemberByIds( Integer busId, String memberIds ) throws BusinessException;

    /**
     * 商场积分赠送
     *
     * @param memberId
     * @param jifen
     */
    public void updateJifen( Integer memberId, Integer jifen ) throws BusinessException;

    /**
     * 查询购买的会员卡面信息
     * @param busId
     * @return
     * @throws BusinessException
     */
    public List<Map<String,Object>> findBuyGradeType(Integer busId);

    /**
     * 商场修改订单状态
     * @param orderNo
     * @param payType
     * @param payStatus
     */
    public void updateUserConsume(String orderNo,Integer payType,Integer payStatus)throws BusinessException;


    public void refundMoneyAndJifenAndFenbi(Map<String,Object> map) throws BusinessException;

    /**
     * 商场积分记录查询
     * @param mcId
     * @param page
     * @param pageSize
     * @return
     */
    public List<Map<String,Object>> findCardrecord(Integer mcId,Integer page,Integer pageSize);
}
