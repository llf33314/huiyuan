package com.gt.member.service.memberApi;

import com.gt.member.exception.BusinessException;

import java.util.Map;

/**
 * Created by Administrator on 2017/11/24.
 */
public interface MemberNodoInterceptorService {

    /**
     * 流量兑换通知
     * @param params
     * @throws BusinessException
     */
    public void changeFlow( Map< String,Object > params ) throws BusinessException;

    /**
     * 短信通知回调
     * @param params
     * @throws BusinessException
     */
    public void smsNotice(Map<String,Object> params) throws  BusinessException;

    /**
     * 支付成功回调
     * @param params
     */
    public void paySuccess(Map<String,Object> params)throws BusinessException;

    /**
     * 购买会员卡回调
     * @param params
     * @throws BusinessException
     */
    public void buyCardPaySuccess(Map<String,Object> params)throws  BusinessException;
}
