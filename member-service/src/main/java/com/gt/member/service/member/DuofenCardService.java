package com.gt.member.service.member;//package com.gt.member.service.old.member;
//
//import com.gt.member.entity.BusUserEntity;
//import com.gt.member.entity.MemberEntity;
//import com.gt.member.entity.WxPublicUsersEntity;
//import com.gt.member.util.Page;
//
//import java.util.Map;
//
//import javax.servlet.http.HttpServletRequest;
//
//
//
//public interface DuofenCardService {
//
//	/**
//	 * 查询多粉优惠券
//	 *
//	 * @param param
//	 * @return
//	 */
//	public Page findDuofenCard(Integer busId, Map<String, Object> param);
//
//	/**
//	 * 保存多粉卡券信息
//	 *
//	 * @param duofenardParam
//	 * @return
//	 */
//	public Map<String, Object> saveOrUpdateCard(int busId, String duofenardParam);
//
//	/**
//	 * 保存或修改卡包信息
//	 *
//	 * @param param
//	 * @return
//	 */
//	public Map<String, Object> saveOrUpdateCardReceive(String param, int busId);
//
//	/**
//	 * 卡包分页查询
//	 * @return
//	 */
//	public Page findDuofenCardReceive(int busId, Map<String, Object> params);
//
//	/**
//	 * 手动领取卡券
//	 *
//	 * @return
//	 */
//	public Map<String, Object> receiveCard(Map<String, Object> param,
//                                           Integer memberId) throws Exception;
//
//	/**
//	 * 卡券领取分页
//	 *
//	 * @param params
//	 * @return
//	 */
//	public Page findCardGet(Integer cardId, Map<String, Object> params);
//
//	/**
//	 * 查询用户名下的多粉优惠券
//	 *
//	 * @param memberId
//	 * @return
//	 */
//	public void findCardGetByMemberId(HttpServletRequest request,
//                                      Integer busId, Integer memberId);
//
//	/**
//	 * 领取卡包中优惠券
//	 *
//	 * @param receiveId
//	 * @return
//	 */
//	public Map<String, Object> oneKeyToGet(Integer receiveId, Integer memberId,
//                                           Integer getType) throws Exception;
//
//
//	/**
//	 * 赠送卡包中优惠券
//	 *
//	 * @param receiveId
//	 * @return
//	 */
//	public Map<String, Object> sendDuofenCard(Integer busId, Integer receiveId, String memberIds) throws Exception;
//
//	/**
//	 * 卡券信息查询
//	 *
//	 * @param code
//	 * @return
//	 */
//	public Map<String, Object> findCardReceive(BusUserEntity busUser, String code);
//
//	/**
//	 * 卡券核销
//	 *
//	 * @param request
//	 * @param code
//	 * @param money
//	 * @param payType
//	 * @param jifenmoney
//	 * @param fenbimoney
//	 * @return
//	 */
//	public Map<String, Object> cardReceive(HttpServletRequest request,
//                                           String code, Double money, BusUserEntity busUser,
//                                           Integer payType, Double jifenmoney, Double fenbimoney)
//			throws Exception;
//
//	/**
//	 * 卡券核销记录查询
//	 *
//	 * @param params
//	 * @return
//	 */
//	public Map<String, Object> findWxcardReceive(BusUserEntity busUser, Map<String, Object> params);
//
//	/**
//	 * 查看卡券审核不通过原因
//	 *
//	 * @param id
//	 * @return
//	 */
//	public Map<String, Object> findExamine(Integer id);
//
//	/**
//	 * 第三方优惠券
//	 *
//	 * @param wxPublicUsers
//	 * @return
//	 */
//	public Page findThreeCard(WxPublicUsersEntity wxPublicUsers,
//                              Map<String, Object> params);
//
//	/**
//	 * 卡券短信投放
//	 *
//	 * @param receiveId
//	 * @param ctIds
//	 * @param gtIds
//	 */
//	public void duanxiTF(Integer receiveId, String ctIds, String gtIds,
//                         WxPublicUsersEntity wxPublicUsers, BusUserEntity busUser);
//
//
//
//	/**
//	 * 统计多粉优惠券
//	 *
//	 * @param request
//	 * @param param
//	 */
//	public void duofencardChart(HttpServletRequest request,
//                                Map<String, Object> param, Integer busId);
//
//
//
//
//	/**
//	 * 查询门店信息
//	 * @param cardId
//	 * @param lat
//	 * @param lng
//	 */
//	public Map<String,Object> findDuofenCardStore(Integer cardId, Double lat, Double lng);
//
//
//
//	/**
//	 * 查询手机核销授权人员
//	 *
//	 * @return
//	 */
//	public Page findDuofenCardAuthorization(Integer busId,
//                                            Map<String, Object> params);
//
//
//	/**
//	 * 卡券手机快捷核销
//	 * @param member
//	 * @param code
//	 * @return
//	 */
//	public void duofencardHeiXiao(HttpServletRequest request, MemberEntity member, String code);
//
//
//	/**
//	 * 推荐优惠券
//	 * @param request
//	 * @param member
//	 * @param cardId
//	 */
//	public void tuijianfriend(HttpServletRequest request, MemberEntity member, Integer cardId);
//
//
//
//
//	/**
//	 * 推荐领取优惠券
//	 * @param request
//	 * @param member
//	 * @param memberId
//	 * @param cardId
//	 */
//	public void tuijianget(HttpServletRequest request, MemberEntity member, Integer memberId, Integer cardId)throws Exception;
//
//}
