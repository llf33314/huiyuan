//package com.gt.member.service.old.member;
//
//import com.gt.member.entity.*;
//import com.gt.member.util.Page;
//
//import java.util.List;
//import java.util.Map;
//
//import javax.servlet.http.HttpServletRequest;
//
//
//
///**
// *
// * 微信卡券服务层
// * @author 李逢喜
// * @version 创建时间：2015年9月7日 下午7:14:20
// *
// */
//public interface IWxCardService {
//
//
//	/**
//	 * 根据卡券ID查询
//	 * @param card_id
//	 * @return
//	 */
//	public WxCard findWxCardById(String card_id);
//
//
//	/**
//	 * 修改卡券状态
//	 * @param wxCard
//	 */
//	public Map<String, Object> upWxCard(WxCard wxCard);
//
//
//	/**
//	 * 添加领取记录
//	 * @param cardReceive
//	 */
//	public  Map<String, Object>  saveWxCardReceive(WxCardReceive cardReceive);
//
//
//	/**
//	 * 通过code查询领取记录
//	 * @param code
//	 * @return
//	 */
//	public WxCardReceive findWxCardReceiveByCode(String code);
//
//
////	/**
////	 * 添加支付记录
////	 * @param wxCardUserPay
////	 * @return
////	 */
////	public Map<String, Object> addWxCardUserPay(WxCardUserPay wxCardUserPay);
//
//
//
////	/**
////	 * 查询付记录
////	 * @param wxCardUserPay
////	 * @return
////	 */
////	public WxCardUserPay findWxCardUserPayByTransId(String transId);
//
//
//	/**
//	 * 修改二维码信息
//	 * @param cardQrcode
//	 * @return
//	 */
//	public  Map<String, Object> upQrcode(WxCardQrcode cardQrcode);
//
//
//	/**
//	 * 保存和修改卡片信息
//	 * @return
//	 */
//	public Map<String, Object> saveOrUpdateCard(WxPublicUsers wxPublicUsers,
//                                                BusUser busUser, String wxcardParam);
//
//	public Page findWxCard(Integer publicId, Map<String, Object> params);
//
//	/**
//	 * 微信领取分页
//	 * @param params
//	 * @return
//	 */
//	public Page findWxCardReceive(String cardId, Map<String, Object> params);
//
//	/**
//	 * 修改是否开启卡券领取
//	 * @param isOpenCard
//	 * @return
//	 */
//	public Map<String, Object> updatePublicParameterSet(Integer busId, Byte isOpenCard);
//
//
//	/**
//	 * 保存货架
//	 * @param wxPublicUsers
//	 * @param params
//	 * @return
//	 */
//	public Map<String, Object> saveShelves(WxPublicUsers wxPublicUsers, Map<String, Object> params);
//
//	/**
//	 * 二维码投放
//	 * @param wxPublicUsers
//	 * @param params
//	 * @return
//	 */
//	public Map<String,Object> saveQrcode(WxPublicUsers wxPublicUsers, Map<String, Object> params);
//
//	/**
//	 * 查询正常的卡片信息
//	 * @return
//	 */
//	public Map<String,List<Map<String, Object>>> findReceiveCard(WxPublicUsers wxPublicUsers, String openid);
//
//	/**
//	 * 查询卡券适用门店信息
//	 * @param id
//	 * @return
//	 */
//	public List<Map<String, Object>> findStore(Integer id);
//
//	/**
//	 * 查询卡券信息
//	 * @param wxPublicUsers
//	 * @param code
//	 * @return
//	 */
//	public Map<String, Object> findCardReceive(WxPublicUsers wxPublicUsers, String code, BusUser busUser);
//
//	/**
//	 * 卡券核销
//	 * @param wxPublicUsers
//	 * @param code
//	 * @param money
//	 * @return
//	 */
//	public Map<String, Object> cardReceive(HttpServletRequest request, WxPublicUsers wxPublicUsers, String code, Integer storeId,
//                                           Double money, BusUser bususer, Integer payType, Integer model, Double jifenmoney, Double fenbimoney) throws Exception;
//
//	/**
//	 * 卡券核销记录查询
//	 * @param wxPublicUsers
//	 * @param params
//	 * @return
//	 */
//	public Page findWxcardReceive(WxPublicUsers wxPublicUsers, BusUser busUser, Map<String, Object> params);
//
//
//	public Map<String,Object> synchro(WxPublicUsers wxPublicUsers, String card_Id);
//
//
//	public Page findWxCardShelve(WxPublicUsers wxPublicUsers, Map<String, Object> params);
//
//
//}
