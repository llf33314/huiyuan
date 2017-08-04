package com.gt.member.service.memberApi;

import com.gt.member.service.memberApi.entityBo.MallAllEntity;
import com.gt.member.service.memberApi.entityBo.MemberShopEntity;

/**
 * 会员计算统一api
 * Created by Administrator on 2017/8/2 0002.
 */
public interface MemberCountMoneyApiService {

    /**
     * 统一门店计算
     * @param ce
     * @return
     * @throws Exception
     */
    public MemberShopEntity publicMemberCountMoney(MemberShopEntity ce) throws Exception;

    /**
     * 跨门店购买
     * @param mallAllEntity
     * @return
     * @throws Exception
     */
    public MallAllEntity mallSkipShopCount(MallAllEntity mallAllEntity) throws Exception;
}
