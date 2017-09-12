///**
// * P 2016年7月27日
// */
//package com.gt.member.service.old.offlinepay;
//
//import java.util.Date;
//import java.util.List;
//import java.util.Map;
//import java.util.SortedMap;
//
//import com.gt.entity.common.BusUserEntity;
//import com.gt.entity.offlinepay.Offlinepay;
//
///**
// * @author pengjiangli
// * @version
// * 创建时间:2016年7月27日
// *
// */
//public interface IofflinepayService {
//
//	/**
//	 * 查询减免规则
//	 * @param busUser
//	 * @return
//	 */
//	public List<Map<String, Object>> findOfflinepay(BusUserEntity busUser);
//
//	/**
//	 * 查询登陆用户下店铺信息
//	 * @param busUser
//	 * @return
//	 */
//	public List<Map<String, Object>> findShop(BusUserEntity busUser);
//
//	/**
//	 * 保存减免规则
//	 * @param publicId
//	 * @param busUser
//	 * @param param
//	 * @return
//	 */
//	public Map<String, Object> saveOfflinepay(BusUserEntity busUser, String param)throws Exception;
//
//	/**
//	 * 多选线下支付
//	 * @param param
//	 * @return
//	 */
//	public SortedMap<Object,Object> pay(String param, String url) throws Exception ;
//
//	/**
//	 * 单选线下支付
//	 * @param param
//	 * @return
//	 */
//	public SortedMap<Object,Object> radioPay(String param, String url) throws Exception ;
//
//	/**
//	 * 微信线下支付回调
//	 * @param out_trade_no
//	 */
//	public void backCall(String out_trade_no);
//
//	/**
//	 * 查询商家时间减免规则
//	 * @param date
//	 * @param id
//	 * @return
//	 */
//	public Offlinepay findByReduction(Date date, Offlinepay offlinepay);
//
//	/**
//	 * 其他浏览器支付宝支付
//	 * @param param
//	 * @return
//	 */
//	public Map<String, Object> otherBrowserPay(String param)throws Exception ;
//
//	/**
//	 * 支付宝支付回调
//	 * @param orderNo
//	 */
//	public void aliPayPayBack(String orderNo);
//}
