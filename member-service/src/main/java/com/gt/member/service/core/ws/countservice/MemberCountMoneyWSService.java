//package com.gt.member.service.core.ws.countservice;
//
//import javax.jws.WebMethod;
//import javax.jws.WebService;
//
//import org.apache.cxf.annotations.GZIP;
//
//import com.gt.ws.entitybo.count.MallAllEntity;
//import com.gt.ws.entitybo.count.MemberShopEntity;
//
///**
// * 会员统一算法
// * @author pengjiangli
// *
// */
//@WebService
//@GZIP
//public interface MemberCountMoneyWSService {
//
//	/**
//	 * 门店下统一算法
//	 * @param ce
//	 * @return
//	 */
//	@WebMethod
//	public MemberShopEntity publicMemberCountMoney(MemberShopEntity ce);
//
//
//	/**
//	 * 商场跨门店购买商品  优惠券非会员也能使用
//	 * @param mallAllEntity
//	 * @return
//	 */
//	@WebMethod
//	public MallAllEntity mallSkipShopCount(MallAllEntity mallAllEntity);
//}
