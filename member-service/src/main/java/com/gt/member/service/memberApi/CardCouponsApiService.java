package com.gt.member.service.memberApi;

import com.gt.member.entity.DuofenCard;
import com.gt.member.entity.DuofenCardGet;
import com.gt.member.entity.DuofenCardReceive;
import com.gt.member.entity.WxCard;
import com.gt.member.exception.BusinessException;
import com.gt.member.util.Page;

import java.util.List;
import java.util.Map;

public interface CardCouponsApiService {

    // <!-----------微信卡券对外接口Start------------>

    /**
     * 查询 商户下所有用的卡券信息 card_type:判断卡片类型 card_type=DISCOUNT折扣券 discount=值 折扣数
     * card_type=CASH 代金券 cash_least_cost=值 条件值 reduce_cost=值 减免金额
     * user_card_code 卡券code image 卡券图标
     *
     * @return
     */
    public List<Map<String, Object>> findWxCardByShopId( Integer shopId, Integer wxPublicUsersId, Integer memberId ) throws Exception;

    /**
     * 查询 商户下所有用的卡券信息 过滤不满足的优惠券 card_type:判断卡片类型 card_type=DISCOUNT折扣券
     * discount=值 折扣数 card_type=CASH 代金券 cash_least_cost=值 条件值 reduce_cost=值
     * 减免金额 user_card_code 卡券code image 卡券图标
     *
     * @return
     */
    public List<Map<String, Object>> findWxCardByShopIdAndMoney( Integer shopId, Integer wxPublicUsersId, Integer memberId, Double money ) throws Exception;

    /**
     * 微信卡券核销 -1 核销失败 1核销成功
     *
     * @param code
     * @return
     * @throws Exception
     */
    public void wxCardReceive( Integer wxPublicUsersId, String code )throws BusinessException;

    /**
     * 微信卡券核销返回卡券id和name -1 核销失败 1核销成功
     *
     * @param code
     * @return
     * @throws Exception
     */
    public Map<String, Object> wxCardReceiveBackName( Integer wxPublicUsersId, String code ) throws BusinessException;

    /**
     * 查询 所有的微信优惠券
     *
     * @param publicId
     * @return
     */
    public List<Map<String, Object>> findWxCard( Integer publicId );

    /**
     * 查询卡券信息
     *
     * @param id
     * @return
     */
    public WxCard findWxCardById( Integer id );

    // <!-----------微信卡券对外接口END-------------->

    // <!------------------------多粉对外接口start------------------------>

    /**
     * 查询用户拥有的优惠券
     *
     * @param memberId
     * @return 返回数据中 能使用的到值 属性 image：图片 gId：用户拥有卡券表id, code=用户拥有卡券表 卡券code,
     * addUser是否允许叠加使用 0不允许 1已允许 , countId=1, 减免券能叠加使用最高使用数量值
     * discount=0.0, //折扣值 card_type=1, //卡券类型 0折扣券 1减免券
     * cash_least_cost=10.0, 抵扣条件 reduce_cost=5.0, 抵扣金额 cId=43 卡券id
     */
    public List<Map<String, Object>> findDuofenCardByMemberId( Integer memberId, Integer wxshopId );

    /**
     * 查询用户拥有的优惠券 过滤满足的金额
     *
     * @param memberId
     * @return 返回数据中 能使用的到值 属性 image：图片 gId：用户拥有卡券表id, code=用户拥有卡券表 卡券code,
     * addUser是否允许叠加使用 0不允许 1已允许 , countId=1, 减免券能叠加使用最高使用数量值
     * discount=0.0, //折扣值 card_type=1, //卡券类型 0折扣券 1减免券
     * cash_least_cost=10.0, 抵扣条件 reduce_cost=5.0, 抵扣金额 cId=43 卡券id
     */
    public List<Map<String, Object>> findDuofenCardByMemberIdAndMoney( Integer memberId, Integer wxshopId, Double money )throws BusinessException;

    /**
     * 根据商家 查询商家拥有的卡包信息
     *
     * @return List<Map<String, Object>> 查看数据库文档
     */
    public List<Map<String, Object>> findReceiveByBusUserId( Integer busId );


    /**
     * 根据商家查询商家拥有的卡包信息（商城调用）
     */
    public List<Map<String, Object>> findReceiveToMallByBusUserId( Integer busId );


    /**
     * 根据商家 查询商家拥有的卡包信息(新版本)
     *
     * @return List<Map<String, Object>> 查看数据库文档
     */
    public List<Map<String, Object>> findReceiveByBusUserId_1( Integer busId, Integer receiveId );

    /**
     * 根据查询本公众号商场投放的包
     *
     * @param busId
     * @return
     */
    public List<DuofenCardReceive > findReceiveBybusId( Integer busId );

    /**
     * 根据卡包查询卡券信息
     *
     * @param receiveId
     * @return
     */
    public Map<String, Object> findCardByReceiveId( Integer receiveId );

    /**
     * 查询第三方平台下所有优惠券
     *
     * @param threeMemberId
     * @return
     */
    public Map<String, Object> findByThreeMemberId( Integer threeMemberId, Integer page );

    /**
     * 商场支付成功回调 分配卡券
     *
     * @param receiveId 卡包id
     * @param num       数量
     * @param memberId  用户
     * @return
     */
    public void successPayBack( Integer receiveId, Integer num, Integer memberId ) throws BusinessException;

    /**
     * 根据卡包查询卡券信息展示 map中key guoqi=1标示该包或该券过期
     *
     * @param receiveId 卡包id
     */
    public Map<String, Object> findDuofenCardByReceiveId( Integer receiveId )throws BusinessException;

    /**
     * 使用多张卡券获取code值
     *
     * @param cardId   卡券id
     * @param memberId 用户
     * @param num      使用数量
     * @return
     */
    public String findCardCode( Integer cardId, Integer memberId, int num );

    /**
     * 卡券核销(新方法)
     *
     * @param params 包含codes 多粉卡券code ,storeId 门店id
     * @return
     */
    public void verificationCard_2( Map< String,Object > params )throws BusinessException;

    /**
     * 卡包投放
     *
     * @param id
     * @return
     */
    public Map<String, Object> publishShelve( Integer id ) throws  Exception;

    /**
     * 第三方商场购买 领取优惠券
     *
     * @param threeMemberId 商场粉丝id
     * @param busId         卡券商户id
     * @param bagId         卡包id
     * @return
     */
    public void threeShopGetCard( Integer threeMemberId, Integer memberId, Integer busId, Integer bagId ) throws BusinessException;

    /**
     * 根据卡包id 和 粉丝信息 查询粉丝的卡券信息
     *
     * @param memberId
     * @param receiveId
     * @return
     */
    public List<DuofenCardGet > findUserCardByReceiveId( Integer memberId, Integer receiveId );

    /**
     * 第三方商场 过期优惠券
     *
     * @param memberId 卡包所属用户id
     * @return
     */
    public List<DuofenCard > findCardOverTime( Integer cardReceiveId, Integer memberId );

    /**
     * 查询能赠送的卡包信息(免费领取) (汽车ERP)
     *
     * @param busId
     * @param memberId
     * @return
     */
    public Page findCardReceive( Integer busId, Integer memberId, Integer page );

    /**
     * 查询能赠送的卡包信息(商城购买) (汽车ERP)
     *
     * @param busId
     * @param memberId
     * @return
     */
    public Page findCardReceive1( Integer busId, Integer memberId, Integer page );

    /**
     * (汽车ERP) 购买 或免费领取 pc端
     *
     * @param busId
     * @param cardreceiveId 卡包id
     * @return
     * @throws Exception
     */
    public void pcBuyReceive( Integer memberId, Integer busId, Integer cardreceiveId ) throws BusinessException;

    /**
     * (汽车ERP) 购买 手机端购买支付成功 回调
     *
     * @param receiveId
     * @param num
     * @param memberId
     * @return
     */
    public void successBuyReceive( Integer receiveId, Integer num, Integer memberId )  throws BusinessException;

    /**
     * 卡包信息（购买） 美容
     *
     * @param busId
     * @return
     */
    public List<Map<String, Object>> findReceive( Integer busId );

    /**
     * 卡包中卡券信息 美容
     *
     * @param receiveId
     * @return
     */
    public List<Map<String, Object>> findDuofenCard( Integer busId, Integer receiveId );

    /**
     * 购买和免费领取 pc端 美容
     *
     * @param busId
     * @param cardreceiveId 卡包id
     * @return
     * @throws Exception
     */
    public List<Map<String, Object>> findReceviceAll( Integer busId, Integer cardreceiveId ) throws BusinessException;

    /**
     * 赠送卡包 回滚卡包数据
     *
     * @param memberId
     * @param cardReceiveId
     * @return
     */
    public void backDuofenCardGet( Integer memberId, Integer cardReceiveId )throws BusinessException;

    /**
     * 查询游戏卡包信息
     *
     * @param busId
     * @return
     */
    public List<Map<String, Object>> gameDuofenCardRecevice( Integer busId );

    /**
     * 游戏领取优惠券
     *
     * @return
     */
    public void getDuofenCardGame( Integer receiveId, Integer num, Integer memberId ) throws BusinessException;

    /**
     * 卡券核销 并返回卡券id和名称
     *
     * @param param
     * @return
     */
    public Map<String,Object> verificationCard_3( Map< String,Object > param )throws BusinessException;

    /**
     * 美容 查询卡包信息
     * @param memberId
     * @return
     * @throws BusinessException
     */
    public List<Map<String,Object>> findMeiRongDuofenCardByMemberId( Integer memberId );

    /**
     * 美容  查询卡包中卡券信息
     * @param memberId
     * @param receiceId
     * @return
     */
    public List<Map<String,Object>> findMeiRongCardGetByMemberId( Integer memberId, Integer receiceId );

    /**
     * 美容 根据领取的卡券id查询 卡券信息
     * @param gId
     * @return
     */
    public Map<String,Object> findDuofenCardOne( Integer gId );

    /**
     * 美容  根据领取的卡券id 查询详情
     * @param gid
     * @return
     */
    public Map<String,Object> findCardDetails( Integer gid );


    /**
     * 查询卡券数量
     * @param memberId
     * @return
     * @throws BusinessException
     */
    public Integer countMemberDuofenCard( Integer memberId ) throws BusinessException;
}
