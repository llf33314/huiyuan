package com.gt.member.service.member;

/**
 *
 *
 * @author 李逢喜
 * @version 创建时间：2015年9月7日 下午7:14:20
 *
 */
public interface MemberService {




//	/**
//	 * 修改手机号码
//	 * @param member
//	 * @return
//	 */
//	public Map<String, Object> upMemberPhone(MemberEntity member);
//
//	public MemberEntity findById(Integer id);
//
//	/**
//	 * 分页查询会员
//	 * @return
//	 */
//	public Page findMemberByPublicId(Integer busId, Map<String, Object> params);
//
//	/**
//	 * 查询
//	 * @param publicId
//	 * @return
//	 */
//	public List<Map<String,Object>> findMember(Integer publicId, Integer ctId, Integer gtId, String search);
//
//	/**
//	 * 导入会员信息
//	 * @param input
//	 */
//	//public List<ErrorWorkbook> upLoadMember(Integer busId, BusUserEntity busUser, InputStream input) throws Exception;
//
//
//	//public SXSSFWorkbook errorMember(List<ErrorWorkbook> wbs);
//
//	/**
//	 * 根据省名称查询所有市
//	 * @return
//	 */
//	public List<Map<Integer, String>> findAllCity(String cityCode);
//
//	public List<Map<Integer, String>> findCityByPid(Integer[] pids);
//
//	/**
//	 * 手机绑定卡片信息
//	 * @param param
//	 */
//	public Map<String, Object> saveOrUpdateMember(Map<String, Object> param);
//
//	/**
//	 * 变更会员卡审核
//	 * @param memberId
//	 * @param isChecked
//	 * @return
//	 */
//	public Map<String, Object> cardTypeChecked(Integer memberId, Byte isChecked);
//
//	/**
//	 * 申请会员卡
//	 */
//	public void applyMember(MemberRecommend recommend, int applyType, MemberEntity member, Integer ctId, int ischecked,
//                            String nominateCode, String phone, String json)throws Exception ;
//	/**
//	 * 消费查询卡片
//	 * @param cardNoKey
//	 * @param cardNo
//	 * @return
//	 */
//	public Map<String, Object> findMemberCard(BusUserEntity busUser, String cardNoKey, String cardNo);
//
//	/**
//	 * 充值查询卡片
//	 * @param cardNoKey
//	 * @param cardNo
//	 * @return
//	 */
//	public Map<String, Object> findMemberCardByrecharge(Integer busId, String cardNoKey, String cardNo);
//
//	/**
//	 * 会员卡审核
//	 * @param memberId
//	 * @param ischecked
//	 * @return
//	 */
//	public Map<String,Object> cardApplyChecked(Integer memberId, Byte ischecked, String phone, Map<String, Object> companyMap);
//
//	/**
//	 * 查询所有门面
//	 * @param lng
//	 * @param lat
//	 * @return
//	 */
//	public List<Map<String, Object>> findStore(Integer busId, String lng, String lat);
//
//	/**
//	 * 查询公众号下所有积分商城
//	 * @return
//	 */
//	public List<Map<String, Object>> findStoreJifenAll(Integer busId);
//
//	/**
//	 * 查询公众号下所有粉币商城
//	 * @return
//	 */
//	public List<Map<String, Object>> findStoreFenbiAll(Integer busId);
//
//	/**
//	 * 修改会员交易笔数或交易金额
//	 * @param memberId
//	 * @param saleNumber
//	 * @param saleMoney
//	 * @return
//	 */
//	public int updateMemberSale(Integer memberId, Integer saleNumber, Integer saleMoney);
//
//	Boolean isBusMember(int memberId, int busId);
//
//	/**
//	 * 保存会员日信息
//	 * @param memberDate
//	 * @return
//	 */
//	Map<String, Object> saveMemberDate(Integer busId, String memberDate);
//
//	/**
//	 * 修改积分和粉币数量
//	 * @param ids
//	 * @param integral
//	 * @param fans_currency
//	 * @return
//	 */
//	Map<String, Object> updateMember(int busId, String ids, Integer integral, double fans_currency, Integer type) throws Exception;
//
//
//	/**
//	 * 兑换流量
//	 * @param id 会员id
//	 * @param type 流量类型
//	 * @return
//	 */
//	Map<String, Object> changeFlow(Integer id, Integer type);
//
//	/**
//	 * 分页查询推荐人信息
//	 * @param param
//	 * @return
//	 */
//	Page findCommend(Integer busId, Map<String, Object> param);
//
//
//	/**
//	 * 泛会员升级
//	 * @param param
//	 */
//	public Map<String, Object> upgradeMember(Map<String, Object> param);
//
//	public Map<String, Object> loadFlowOrFenbi(Integer ctId, int busId);
//
//	/**
//	 * 手机端会员信息会员完善资料
//	 * @return
//	 */
//	public Map<String, Object> updateMember(Map<String, Object> param)throws Exception;
//
//	/**
//	 * 购买会员卡完善资料
//	 * @param memberId
//	 * @param phone
//	 * @param json
//	 * @return
//	 */
//	public Map<String, Object> updateMember(Integer memberId,
//                                            String phone, String json) throws Exception ;
//
//	/**
//	 * 支付宝 购买会员卡
//	 * @param ctId
//	 * @param gtId
//	 * @return
//	 */
//	public Map<String, Object> alipayBuyCard(Integer memberId, Integer ctId, Integer gtId)throws Exception ;
//
//	/**
//	 * 购买会员卡  支付回调
//	 * @param orderCode
//	 */
//	public void aliPayPayBack(String orderCode);
//
//	/**
//	 * 领取会员卡 合并数据
//	 * @param memberId
//	 * @return
//	 */
//	public Map<String, Object> mergeMemberCard(Integer busId, String phone, Integer memberId)throws Exception ;
//
//	/**
//	 * 查询新增用户
//	 * @param busId
//	 * @return
//	 */
//	public List<Map<String, Object>> findnewMember(Integer busId);
//
//
//	/**
//	 * pc会员信息会员完善资料
//	 * @return
//	 */
//	public Map<String, Object> addMemberUser(Map<String, Object> param)throws Exception;
//
//
//	/**
//	 * 流量兑换
//	 * @param request
//	 * @param id
//	 * @param phone
//	 * @param flowCount
//	 * @return
//	 */
//	public Map<String, Object> exchangeFlow(HttpServletRequest request, Integer id,
//                                            String phone, Integer flowCount)throws Exception;
//
//
//	public Map<String, Object> qiandao(HttpServletRequest request, Integer id,
//                                       Integer jifen)throws Exception;
//
//	/**
//	 * 头像上传
//	 * @param request
//	 * @param parma
//	 * @return
//	 */
//	public Map<String, Object> uploadMemberHeadImg(HttpServletRequest request, Map<String, Object> parma);
//
//	/**
//	 * 积分清零
//	 * @param busIds
//	 */
//	public void clearJifen(List<Integer> busIds) throws Exception ;
//
//
//	public String insertTwoCode(String scene_id, WxPublicUsersEntity wxPublicUsers);
//
//
//	/**
//	 * 批量审批会员
//	 * @param memberId
//	 * @param ischecked
//	 * @param phones
//	 * @return
//	 */
//	public Map<String, Object> cardApplyCheckeds(String[] memberId, Byte ischecked, String[] phones,
//                                                 Map<String, Object> companyMap) throws Exception ;
//
//	/**
//	 * 提取
//	 * @param memberId
//	 * @param busId
//	 * @return
//	 */
//	public Map<String, Object> pickMoney(Integer memberId, Integer busId) throws Exception ;
//
//	/**
//	 * 推荐佣金提取记录
//	 * @param busId
//	 * @param params
//	 * @return
//	 */
//	public Page findPickLog(Integer busId, Map<String, Object> params);
//
//	/**
//	 * 实体卡判断
//	 * @param busId
//	 * @param phone
//	 * @param vcode
//	 * @return
//	 */
//	public Map<String,Object> findShiti(Integer memberId, Integer busId, String phone, String vcode) throws Exception;
//
//	Map<String, Object> fansToMemberCard(Map<String, Object> param);
//
//	/**
//	 * 会员统计
//	 * @param request
//	 * @param ctId
//	 * @param startTime
//	 */
//	public void memberTongJi(HttpServletRequest request, Integer ctId, String startTime);
//
//
//	public Map<String, Object> saveRecommend(Map<String, Object> params);
//
//
//
//
////
////	/**
////	 * 分页查询非会员粉丝信息 只查询微信授权的
////	 * @param reqeust
////	 * @return
////	 */
////	public Page findMemberIsNotCard(Integer busId,Map<String, Object> params);
////
////	//<!-------erp领取会员-------->
////	/**
////	 * erp领取会员
////	 * @param busUser
////	 * @param params
////	 * @return
////	 */
////	public Map<String, Object> linquMemberCard(BusUserEntity busUser,Map<String, Object> params)throws Exception;
////
////	/**
////	 * 购买会员卡
////	 * @param busUser
////	 * @param params
////	 * @return
////	 */
////	public Map<String, Object> buyMemberCard(BusUserEntity busUser,Map<String, Object> params)throws Exception;

}


