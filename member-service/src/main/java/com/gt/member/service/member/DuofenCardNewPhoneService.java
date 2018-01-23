package com.gt.member.service.member;

import com.gt.duofencard.entity.DuofenCardNew;
import com.gt.member.exception.BusinessException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 多粉手机端
 *
 * Created by Administrator on 2018/1/16.
 */
public interface DuofenCardNewPhoneService {

    /**
     * 查询发布的通用券信息
     * @param busId
     * @return
     */
    public List<Map<String,Object>> findPublishDuofenCard(Integer busId,Integer memberId,Map<String,Object> params);

    /**
     * 查询优惠券信息详情
     * @param cardId
     * @return
     */
    public Map<String,Object> findDuofenCardNewByCardId(Integer cardId);

    /**
     * 免费领取会员券
     * @param cardId
     * @throws BusinessException
     */
    public void getDuofenCard(Integer cardId,Integer memberId)throws BusinessException;

    /**
     * 购买优惠券信息查询
     * @param cardId
     */
    public Map<String,Object> findBuyDuofenCardDetails(HttpServletRequest request,Integer cardId,Integer memberId);
    /**
     * 购买优惠券
     * @param cardId
     * @param memberId
     * @throws BusinessException
     */
    public void buyDuofenCard(HttpServletRequest request,Integer cardId,Integer memberId,Map<String,Object> params)throws BusinessException;

    /**
     * 我的优惠券
     * @param memberId
     * @return
     */
    public Map<String,Object> myDuofenCard(Integer memberId);

    /**
     * 单张优惠券使用
     * @param duofenCardGetId
     * @return
     */
    public Map<String,Object> useDuofenCardByCardId(Integer duofenCardGetId,Integer memberId);

    /**
     * 多粉优惠券详情
     * @param duofenCardGetId
     * @return
     */
    public Map<String,Object> findDuofenCardDetailsByDuofenCardGetId(Integer duofenCardGetId);


    /*
     * 优惠券绑定手机号码
     * @return
     */
    public void bingdingPhone( HttpServletRequest request,Integer memberId, String phone,  Integer busId,String vcode ) throws BusinessException;

}
