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

    Page getCouponListByBusId( Integer curPage, Integer pageSize, Integer busId, String cardStatus, String title, Integer useType );

    Integer updateCouponById( DuofenCardNewVO coupon );

    Page getReceiveCouponListById( Integer curPage, Integer pageSize, Integer couponId, String searchContent );

    Map< String,Object > findCouponDetail( Integer id );

    Map<String,Object> findCouponById( Integer id );

    Map< String,Object > getPaymentDetailById( Integer couponId, Integer code );

    Integer getCouponReceiveQuantity( Integer id );

    List< Map< String,Object > > usageStatistics( Integer busId );

    Page usageList( Integer curPage, Integer pageSize, Integer busId, String searchContent );

    Page recommendList( Integer curPage, Integer pageSize, Integer busId, String searchContent );

    Map<String,Object> getCouponQuantity( Integer busId );

    List<Map<String,Object>> getUsageListByBusId( Integer busId );

    Integer updateCouponPublishById( DuofenCardNewVO coupon );

    Integer updateCouponTimeById( DuofenCardNewVO coupon );

    Integer updateCouponNewById( DuofenCardNewVO coupon );

    List<Map<String,Object>> getReceiveCouponListByCouponId( Integer couponId );

    Integer deleteByCouponId( Integer couponId );

    Page getCouponListByBusId2( Integer curPage, Integer pageSize, Integer busId, Integer cardStatus, String couponName, Integer useType );
}
