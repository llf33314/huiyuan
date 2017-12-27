package com.gt.member.service.memberApi;

import com.gt.entityBo.MallAllEntity;
import com.gt.entityBo.MallNotShopEntity;
import com.gt.entityBo.MemberShopEntity;

/**
 * 会员计算统一api
 * Created by Administrator on 2017/8/2 0002.
 */
public interface MemberCountMoneyApiService {


    /**
     * 跨门店购买
     * @param mallAllEntity
     * @return
     * @throws Exception
     */
    public MallAllEntity mallSkipShopCount( MallAllEntity mallAllEntity ) throws Exception;


    /**
     * 不跨门店购买 包括商品详情
     * @param mallNotShopEntity
     * @return
     * @throws Exception
     */
    public MallNotShopEntity mallSkipNotShopCount( MallNotShopEntity mallNotShopEntity ) throws Exception;
}
