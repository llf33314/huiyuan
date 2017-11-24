package com.gt.member.service.member;

import com.gt.api.bean.session.Member;
import com.gt.common.entity.BusFlow;
import com.gt.member.exception.BusinessException;

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
    public Map<String,Object> findLingquData( HttpServletRequest request, Integer busId );

    /**
     * 查询购买会员卡等级信息
     * @param json
     * @return
     */
    public List<Map<String,Object>> findBuyGradeTypes(String json);

    /**
     * 领取会员卡
     * @param params
     * @return
     * @throws BusinessException
     */
    public void linquMemberCard( Map< String,Object > params,Integer memberId  ) throws BusinessException;

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


    public List<BusFlow > findBusUserFlow( Integer busId );

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
    public Map<String,Object> findRecharge(String json,Integer busId,Integer memberId)throws  BusinessException;

    /**
     * 会员卡充值
     * @param json
     * @param busId
     * @param memberId
     * @return
     * @throws BusinessException
     */
    public String rechargeMemberCard(String json,Integer busId,Integer memberId)throws  BusinessException;

    /**
     * 查询会员卡资料
     * @param params
     * @param memberId
     * @return
     * @throws BusinessException
     */
    public Map<String,Object> findMemberUser(Map<String,Object> params,Integer memberId) throws BusinessException;
}
