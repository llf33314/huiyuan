package com.gt.member.service.common.membercard;

import com.gt.api.enums.ResponseEnums;
import com.gt.duofencard.entity.DuofenCardTime;
import com.gt.entityBo.MemberShopEntity;
import com.gt.member.entity.*;
import com.gt.member.exception.BusinessException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

/**
 * 会员公共接口
 * Created by Administrator on 2017/8/15 0015.
 */
public interface MemberCommonService {
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

    public MemberDate findMemeberDate( Integer busId, Integer ctId );



    /*
    * 计算抵扣积分 包含计算规则
    * @param money 积分抵扣金额
    * @param busId 商家id
    * @return
    */
    public Double deductJifen(  PublicParameterset pps,Double jifenMoney, int busId );


    /**
     * 金额计算使用粉币数量
     *
     * @return
     */
    public Double deductFenbi(SortedMap<String, Object> dict, Double fenbiMoney);


    /**
     * 添加记录
     * @param memberId
     * @param recordType
     * @param number
     * @param itemName
     * @param busId
     * @param balance
     * @param orderCode
     * @param rtype
     * @return
     */
    public MemberCardrecordNew saveCardRecordOrderCodeNew(Integer memberId, Integer recordType, Double number,
                    String itemName, Integer busId, Double balance, String orderCode,Integer rtype);


    /**
     * 添加记录 次卡的
     * @param memberId
     * @param recordType
     * @param number
     * @param itemName
     * @param busId
     * @param balance
     * @param orderCode
     * @param rtype
     * @return
     */
    public MemberCardrecordNew saveCardRecordOrderCodeNew(Integer memberId, Integer recordType, Double number,
                    String itemName, Integer busId, Double balance, String orderCode,Integer rtype,Integer unit);

    /**
     * 关注公众号的接口
     */
    public String findWxQcode(Integer busId,Integer busType,String scene_id);

    /**
     * 新增会员处理数据合并问题
     * @param member
     * @param busId
     * @param phone
     */
    public MemberEntity newMemberMerge(MemberEntity member,Integer busId,String phone)throws BusinessException;


    /**
     * 主卡充值
     * @param price
     * @param grId
     * @param busId
     * @param ctId
     * @return
     */
    public MemberRechargegive findRechargegive(double price, Integer grId, Integer busId,
                    Integer ctId) throws BusinessException;

    /**
     * 查询时效卡的充值
     * @return
     */
    public Map<String,Object> findTimeCard(Double money, Integer busId)throws BusinessException;

    /**
     * 副卡充值
     * @param price
     * @param gtId
     * @param busId
     * @return
     */
    public MemberRechargegiveAssistant findAssistantrechargegive(double price, Integer gtId, Integer busId,
                    Integer fuctId) throws BusinessException;

    /**
     * 会员赠送物品延迟送
     * @param orderNo
     */
    public void findGiveRuleDelay(String orderNo);


    /***
     * 当前立即赠送物品
     * @throws Exception
     */
    public void findGiveRule(String orderCode);

    /**
     * 查询省市区信息
     * @param cityCode
     * @return
     */
    public List<Map<String,Object>> findCityByCityCode1(String cityCode);




    /**
     * 判断数据来源
     * @param request
     * @return
     */
    public Integer dataSource(HttpServletRequest request);
    /**
     * 推荐赠送
     * @param recommend
     */
    public void tuijianGive( MemberRecommend recommend );


    /**
     * 统一门店计算 不包括商品详情
     * @param ce
     * @return
     * @throws Exception
     */
    public MemberShopEntity publicMemberCountMoney( MemberShopEntity ce ) throws Exception;


    /**
     * 卡券核销(新方法)
     *
     * @param params 包含codes 多粉卡券code ,storeId 门店id
     * @return
     */
    public void verificationCard_2( Map< String,Object > params )throws BusinessException;

    /**
     * 储值卡充值升级
     */
    public Map<String,Object> rechargeCtId3(Integer busId,Integer ctId,Integer gtId,Integer memberId,Double rechargeMoney);

    /**
     * 查询粉丝所有的id集合
     * @param memberId
     * @return
     */
    public List<Integer> findMemberIds(Integer memberId);


    /**
     * 泛会员 和正式会员完善资料 赠送物品
     * @param memberOld
     * @param memberParameter1
     * @return
     */
    public boolean giveMemberGift(MemberEntity memberOld,
                    MemberParameter memberParameter1);

    /**
     * 判断粉丝当前购买金额
     * @param giftBuyMoney
     * @param buyMoney
     * @return
     */
    public Double getBuyMoney(String giftBuyMoney,Double buyMoney);

    /**
     * 根据商家判断是否自动审核
     * @param busId
     * @return
     */
    boolean getAutoAuditFlag( Integer busId );

    /**
     * 卡券判断当前时间端是否可以用
     * @param duofenCardTime
     * @return
     */
    public boolean isUseDuofenCardTime(DuofenCardTime duofenCardTime);

    /**
     * 优惠券推荐领取
     * @param busId
     * @param code
     */
    public void lingquMemberRecommend(Integer busId,String code);

    /**
     * 优惠券推荐赠送
     * @param recommendId
     */
    public void memberRecommend(Integer recommendId);
}