package com.gt.member.service.memberApi;

import com.gt.common.entity.BusUserEntity;
import com.gt.member.entity.*;
import com.gt.member.exception.BusinessException;
import com.gt.entityBo.PaySuccessBo;
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
    public MemberEntity findByMemberId( Integer memberId ) throws BusinessException;


    /*
     * 根据会员id查询会员卡信息 如果是折扣卡 返回折扣值
     * @param memberId
     * @return
     */
    public Double findCardTypeReturnDiscount( Integer memberId ) throws BusinessException;


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
    public Integer isCardType( Integer memberId ) throws BusinessException;

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




    public Map< String,Object > findNextGrade( Integer busId, Integer ctId, Integer gtId, Integer integral, double totalmoney );



    /*
     * 查询卡片页面信息
     * @param memberId
     * @return
     */
    public MemberGradetype findGradeType( Integer memberId );


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
    public MemberEntity bingdingPhone( Integer memberId, String phone, String code, Integer busId ) throws BusinessException;

    /*
     *
     * 会员卡充值查询接口
     * @param busId 商家id
     * @param cardNo 卡号
     * @return  money 充值金额, giveCount 赠送数量, number 次数(次卡), ctId, isDate 是否会员日
     */
    public List< Map< String,Object > > findMemberCardRecharge( Integer busId, String cardNo );


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
     * 根据memberId 查询member之前所有id
     * @param memberId
     * @return
     */

    public List< Integer > findMemberIds( Integer memberId );



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
	 * 判断用户是否是会员  false 不是 true 是
	 * @param memberId 粉丝id
	 * @return
	 */

    public boolean isMememberByApplet( BusUserEntity busUserEntity, String cardNoKey, String cardNo );

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
     * 查询购买的会员卡面信息
     *
     * @param busId
     *
     * @return
     * @throws BusinessException
     */
    public List< Map< String,Object > > findBuyGradeType( Integer busId );

    /**
     * 商场积分记录查询
     *
     * @param memberId
     * @param page
     * @param pageSize
     *
     * @return
     */
    public List< Map< String,Object > > findCardrecord( Integer memberId, Integer page, Integer pageSize );

    /**
     * 查询会员卡信息
     *
     * @param mcId
     *
     * @return
     */
    public MemberCard findMemberCardByMcId( Integer mcId );

    /**
     * 跨门店 多个门店根据粉丝id获取优惠券信息
     *
     * @param memberId
     * @param shopIds
     *
     * @return
     * @throws Exception
     */
    public Map< String,Object > findMemberCardByMemberIdAndshopIds( Integer memberId, String shopIds ) throws BusinessException;

    /**
     * 修改粉丝手机号码
     *
     * @param map
     *
     * @return
     * @throws BusinessException
     */
    public void updateMemberPhoneByMemberId( Map< String,Object > map ) throws BusinessException;

    /**
     * 根据ids集合查询粉丝信息
     *
     * @param map
     *
     * @return
     * @throws BusinessException
     */
    public List< Map< String,Object > > findMemberByIds( Map< String,Object > map ) throws BusinessException;

    /**
     * 根据手机号查询粉丝信息
     *
     * @param map
     *
     * @return
     * @throws BusinessException
     */
    public List< Map< String,Object > > findMemberByPhoneAndBusId( Map< String,Object > map ) throws BusinessException;

    /**
     * 查询商家发布的卡片类型
     *
     * @param map
     *
     * @return
     * @throws BusinessException
     */
    public Map< String,Object > findMemberCardTypeByBusId( Map< String,Object > map ) throws BusinessException;

    /**
     * 查询会员卡等级
     *
     * @param map
     *
     * @return
     * @throws BusinessException
     */
    public List< Map< String,Object > > findMemberGradeTypeByctId( Map< String,Object > map ) throws BusinessException;

    /**
     * 墨盒会员卡充值接口
     *
     * @param map
     *
     * @return
     * @throws BusinessException
     */
    public Map< String,Object > findMemberAndChongZhi( Map< String,Object > map ) throws BusinessException;

    /**
     * 墨盒领取会员卡
     *
     * @param params
     *
     * @return
     * @throws BusinessException
     */
    public void linquMemberCard( Map< String,Object > params ) throws BusinessException;

    /**
     * 魔盒充值成功接口
     *
     * @param params
     *
     * @throws BusinessException
     */
    public void successChongZhi( Map< String,Object > params ) throws BusinessException;

    /**
     * (商城）评论修改会员积分或粉币
     *
     * @param params
     */
    public void updateJifenAndFenBiByPinglu( Map< String,Object > params )throws BusinessException ;

    /**
     * 粉币，积分抵扣规则
     *
     * @param busId
     *
     * @return map中  ratio 比例 1元:粉币 startMoney   最低10元才能使用粉币兑换
     * 返回对象中 能用的参数有:integralRatio 积分比例   changeMoney 多少积分抵扣多少金额值   startMoney 启兑金额
     */
    public Map< String,Object > jifenAndFenbiRule( Integer busId ) throws BusinessException;


    /**
     * erp计算 会员卡核销接口（包括储值卡扣款 、 借款、优惠券核销 、积分、粉币）
     *
     * @param newErpPaySuccessBo
     */
    public void newPaySuccessByErpBalance( String newErpPaySuccessBo ) throws BusinessException;

    /**
     * erp退款 包括钱 积分 粉币
     * @param erpRefundBo
     * @throws BusinessException
     */
    public void refundErp(String erpRefundBo) throws BusinessException;


    /**
     * 商场积分赠送
     *
     * @param memberId
     * @param jifen
     */
    public void updateJifen( Integer memberId, Integer jifen ) throws BusinessException;
}
