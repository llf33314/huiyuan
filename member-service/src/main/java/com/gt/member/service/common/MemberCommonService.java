package com.gt.member.service.common;

import com.gt.member.entity.MemberDate;
import com.gt.member.entity.PublicParameterset;
import com.gt.member.exception.BusinessException;

import java.util.Map;

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
    public Double deductFenbi(Map<String, Object> dict, Double fenbiMoney);

    /**
     * 归还商家粉币
     * @param busId
     * @param fenbi
     */
    public void guihuiBusUserFenbi(Integer busId,Double fenbi)throws BusinessException;
}