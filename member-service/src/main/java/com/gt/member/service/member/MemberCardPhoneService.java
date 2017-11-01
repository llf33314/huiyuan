package com.gt.member.service.member;

import com.gt.member.exception.BusinessException;

import javax.servlet.http.HttpServletRequest;
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
    public Map<String,Object> findLingquData(HttpServletRequest request,Integer busId);

    /**
     * 领取会员卡
     * @param params
     * @return
     * @throws BusinessException
     */
    public void linquMemberCard( Map< String,Object > params ) throws BusinessException;

    /**
     * 购买会员卡成功回调
     * @param params
     * @return
     * @throws Exception
     */
    public void buyMemberCard( Map< String,Object > params ) throws Exception;

}
