package com.gt.member.service.member;

import com.gt.api.bean.session.WxPublicUsers;
import com.gt.duofencard.entity.DuofenCardNew;
import com.gt.member.exception.BusinessException;
import com.gt.member.util.CommonUtil;
import com.gt.util.entity.result.wx.WxJsSdkResult;

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
    public void getDuofenCard(Integer cardId,Integer memberId,Map< String,Object > params)throws BusinessException;

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
    public Map<String,Object> myDuofenCard(Integer busId,Integer memberId);

    /**
     * 已失效的优惠券
     * @param memberId
     * @return
     */
    public List<Map<String,Object>> invalidDuofenCard(Integer memberId);

    /**
     * 单张优惠券使用
     * @param receiveId
     * @return
     */
    public Map<String,Object> useDuofenCardByCardId(Integer receiveId,Integer memberId);


    /**
     * 购买多粉优惠券详情
     * @param cardId
     * @return
     */
    public Map<String,Object> findDuofenCardDetailsByCardId(Integer cardId);

    /**
     * 多粉优惠券详情
     * @param duofenCardGetId
     * @return
     */
    public Map<String,Object> findDuofenCardDetailsByreceiveId(Integer receiveId);


    /*
     * 优惠券绑定手机号码
     * @return
     */
    public void bingdingPhone( HttpServletRequest request,Integer memberId, String phone,  Integer busId,String vcode ) throws BusinessException;

    public List<Map> findShopByReceiveId(Map<String,Object> params);

    /**
     * 商家核销优惠券
     * @param memberId
     * @param code
     */
    public Map<String,Object> useVerificationByUser(Integer memberId,Integer busId,String code);

    /**
     * 优惠券核销
     * @param receiveId
     * @return
     */
    public Map<String,Object> useVerificationDuofenCard(Integer receiveId );

    /**
     * 自助核销
     * @param receiveId
     * @param shopId
     */
    public void verificationDuofenCardGet(Integer receiveId,Integer shopId);

    /**
     * 推荐统计
     * @param memberId
     * @return
     */
    public Map<String,Object> findTuiJianDuofenCard(HttpServletRequest request,Integer busId,Integer memberId);

    /**
     *  推荐分页查询
     * @param memberId
     * @param params
     * @return
     */
    public List<Map<String,Object>> findRecommendPage(Integer memberId,Map<String,Object> params);


    /**
     * 佣金提取
     * @param memberId
     * @param busId
     * @param pickMoney
     * @throws BusinessException
     */
    public void pickMoney(Integer memberId, Integer busId,Double pickMoney)throws  BusinessException;

    /**
     * 添加核销人员
     * @param memberId
     * @param busId
     * @param params
     * @throws BusinessException
     */
    public void addAuthorization(HttpServletRequest request,Integer memberId,Integer busId,Map<String,Object> params)throws  BusinessException;

    /**
     * 粉丝推荐优惠券
     * @param memberId
     * @param params
     * @return
     */
    public Map<String,Object> tuijianDuofenCard(Integer memberId,Integer busId,Map<String,Object> params);


    public WxJsSdkResult wxshareCard( Integer memberId, Integer busId, String url );
}
