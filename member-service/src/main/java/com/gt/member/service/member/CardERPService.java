package com.gt.member.service.member;

import com.gt.common.entity.BusUser;

import java.util.List;
import java.util.Map;

public interface CardERPService {
    /**
     * 分页查询非会员粉丝信息 只查询微信授权的
     *
     * @return
     */
    public List< Map< String,Object > > findMemberIsNotCard( Integer busId, Map< String,Object > params );

    //<!-------erp领取会员-------->

    /**
     * erp领取会员
     *
     * @param busUser
     * @param params
     *
     * @return
     */
    public Map< String,Object > linquMemberCard( BusUser busUser, Map< String,Object > params ) throws Exception;

    /**
     * 购买会员卡
     *
     * @param busUser
     * @param params
     *
     * @return
     */
    public Map< String,Object > buyMemberCard( BusUser busUser, Map< String,Object > params ) throws Exception;

    /**
     * 查询会员信息
     *
     * @param cardNo
     *
     * @return
     */
    public Map< String,Object > findMemberCard( Integer busId, String cardNo );
}
