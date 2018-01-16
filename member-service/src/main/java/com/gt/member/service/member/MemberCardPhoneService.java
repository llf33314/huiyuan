package com.gt.member.service.member;

import com.gt.api.bean.session.Member;
import com.gt.common.entity.BusFlow;
import com.gt.member.exception.BusinessException;
import com.gt.util.entity.result.wx.WxJsSdkResult;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/10/31.
 */
public interface MemberCardPhoneService {

    /**
     *  领取会员卡页面查询数据
     * @param busId
     * @return
     */
    public Map<String,Object> findLingquData( HttpServletRequest request,Integer memberId, Integer busId );

    /**
     * 购买会员卡之前手机判断
     * @param memberId
     * @param busId
     * @param phone
     * @param vcode
     * @return
     */
    public void judgeMemberCard(Integer memberId, Integer busId,
                    String phone, String vcode,Integer areaId,String areacode);

    /**
     * 查询购买会员卡等级信息
     * @param json
     * @return
     */
    public List<Map<String,Object>> findBuyGradeTypes(String json);


    public void judgeMember(Integer busId,String phone);


    /**
     * 已有会员卡，合并数据并登录
     * @param params
     * @return
     * @throws BusinessException
     */
    public void loginMemberCard( Map< String,Object > params,Integer memberId  ) throws BusinessException;


    /**
     * 领取会员卡
     * @param params
     * @return
     * @throws BusinessException
     */
    public Map<String,Object> linquMemberCard( HttpServletRequest request,Map< String,Object > params,Integer memberId  ) throws BusinessException;

    /**
     * 购买会员卡成功回调
     * @param params
     * @return
     * @throws Exception
     */
    public void buyMemberCard( Map< String,Object > params ) throws Exception;

    /**
     * 查询会员卡信息
     * @param request
     * @param busId
     * @return
     * @throws BusinessException
     */
    public Map<String,Object> findMember( HttpServletRequest request, Integer busId ) throws BusinessException;

    /**
     * 查询会员手机端交易记录
     * @param params
     * @param recordType
     * @return
     */
    public Map<String,Object> findCardrecordNew( Map< String,Object > params, Integer memberId, Integer recordType );


    public Map<String,Object> findBusUserFlow( Integer memberId,Integer busId );

    public void changeFlow( Map< String,Object > params, Integer memberId )throws  BusinessException;

    /**
     * 查询商家发布充值信息
     * @param params
     * @return
     */
    public List<Map<String,Object>> findRecharge(Map<String,Object> params) throws BusinessException;

    /**
     * 会员权益
     * @param member
     * @return
     */
    public Map<String,Object> findMemberEquities(Member member) throws BusinessException;

    /**
     * 查询会员卡充值信息
     * @param json
     * @param busId
     * @param memberId
     * @return
     * @throws BusinessException
     */
    public Map<String,Object> findRecharge(HttpServletRequest request,String json,Integer busId,Integer memberId)throws  BusinessException;

    /**
     * 会员卡充值
     * @param json
     * @param busId
     * @param memberId
     * @return
     * @throws BusinessException
     */
    public String rechargeMemberCard(HttpServletRequest request,String json,Integer busId,Integer memberId)throws  BusinessException;

    /**
     * 查询会员卡资料
     * @param params
     * @param memberId
     * @return
     * @throws BusinessException
     */
    public Map<String,Object> findMemberUser(Map<String,Object> params,Integer memberId) throws BusinessException;

    /**
     * 修改会员资料
     * @param json
     * @throws BusinessException
     */
    public void updateMemberUser(String json,Integer memberId)throws  BusinessException;

    /**
     * 查询粉丝会员卡卡号 并加密
     * @param memberId
     * @return
     */
    public String findCardNoByMemberId(Integer memberId)throws Exception;

    /**
     * 签到送积分
     * @param memberId
     * @param busId
     */
    public void qiandao(Integer memberId,Integer busId)throws BusinessException;


    /**
     * 查询会员信息
     * @param memberId
     * @return
     */
    public Map<String,Object>  findMemberNotice(Integer memberId);

    /**
     * 查询推荐信息
     * @param memberId
     * @return
     */
    public Map<String,Object> findRecommend(HttpServletRequest request,Integer memberId )throws  BusinessException;

    /**
     * 佣金提取
     * @param memberId
     * @param busId
     * @param pickMoney
     * @throws BusinessException
     */
    public void pickMoney(Integer memberId, Integer busId,Double pickMoney)throws  BusinessException;

    /**
     * 身份证上传
     * @return
     * @throws BusinessException
     */
    public List<Map<String,Object>> updateImage(HttpServletRequest request,Integer memberId,Integer busId)throws BusinessException;

    /**
     * 查询门店信息
     * @param busId
     * @return
     */
    public List<Map> findWxShop(Integer busId,Double longt1, Double lat1) throws BusinessException;

    /**
     * 推荐地址
     * @param memberId
     * @return
     */
    public String tuijianQRcode(Integer busId,String systemCode);


    /**
     * 推荐地址
     * @param memberId
     * @return
     */
    public Map<String,Object> judgeTuijian(Integer memberId,String systemCode);


    /**
     * 微信会员卡分享
     * @param memberId
     * @param busId
     * @return
     */
    public WxJsSdkResult wxshareCard(Integer memberId,Integer busId,String url);

    /**
     * 查询会员卡卡号
     * @param memberId
     * @return
     */
    public Map<String,Object> findMemberCardNo(Integer memberId);

    /**
     * 储值卡转借他人
     * @param memberId
     */
    public String memberLentMoney(Integer memberId,Double money)throws BusinessException;

    /**
     * 判断是否是同一个人
     * @param memberId
     * @param memberLentKey
     * @return
     */
    public Map<String,Object> judgememberLent(Integer memberId,String memberLentKey);
}
