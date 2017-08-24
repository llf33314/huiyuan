package com.gt.member.service.count;

import com.gt.member.exception.BusinessException;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * erp统一结算接口
 * Created by Administrator on 2017/8/15 0015.
 */
public interface ERPCountService {

    /**
     * 查询会员信息或联盟卡信息
     * @param busId
     * @param shopId
     * @param cardNo
     * @return
     */
    public Map<String,Object> findMemberByERP(Integer busId,Integer shopId,String cardNo);

    /**
     * erp计算
     * @param mallAllEntityQuery
     * @param param
     * @return
     */
    public Map<String,Object> erpCountMoney(String mallAllEntityQuery, String param );

    /**
     * erp 储值卡支付
     * @param mallAllEntityQuery
     * @param param
     * @return
     */
    public void erpChuzhiPayMent(String mallAllEntityQuery, String param )throws BusinessException;


    /**
     * erp 扫码支付支付
     * @param mallAllEntityQuery
     * @param param
     * @return
     */
    public Map<String,Object> saomaPayMent(String mallAllEntityQuery, String param )throws BusinessException;

    /**
     * erp 钱包支付
     * @return
     */
    public Map<String,Object> saomaQianBaoPay(String mallAllEntityQuery,
                    String param);

    /**
     * 支付成功回调
     * @param params
     */
    public void successPay(Map<String,Object> params);
}
