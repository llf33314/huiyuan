package com.gt.member.service.member;

import com.gt.bean.vo.DuofenCardNewVO;
import com.gt.duofencard.entity.DuofenCardNew;
import com.gt.member.base.BaseService;
import com.gt.member.util.Page;

import java.util.List;
import java.util.Map;

/**
 * 多粉优惠券pc端
 *
 * Created by Administrator on 2018/1/16.
 */
public interface DuofenCardNewService extends BaseService<DuofenCardNew > {

    Integer addCoupon( DuofenCardNewVO coupon );

    Page getCouponListByBusId( Integer curPage, Integer pageSize, Integer busId, Integer cardStatus, String title, Integer useType );

    Integer updateCouponById( DuofenCardNewVO coupon );

    Page getReceiveCouponListById( Integer curPage, Integer pageSize, Integer couponId, String searchContent );

    List< Map< String,Object > > selectById( Integer id );
}
