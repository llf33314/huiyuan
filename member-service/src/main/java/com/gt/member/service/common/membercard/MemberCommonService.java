package com.gt.member.service.common.membercard;

import com.gt.member.entity.*;
import com.gt.member.exception.BusinessException;

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
     *
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
     * 关注公众号的接口
     */
    public String findWxQcode(Integer busId,Integer busType,String scene_id);

    /**
     * 新增会员处理数据合并问题
     * @param member
     * @param busId
     * @param phone
     */
    public void newMemberMerge(MemberEntity member,Integer busId,String phone)throws BusinessException;

    /**
     * 推荐赠送
     * @param recommend
     */
    public void tuijianGive( MemberRecommend recommend );

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

}