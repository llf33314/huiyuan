///**
// * P 2016年7月14日
// */
//package com.gt.member.service.old.personCenter;
//
//import java.util.List;
//import java.util.Map;
//
//import com.gt.entity.common.BusUserEntity;
//
//
///**
// * @author pengjiangli
// * @version
// * 创建时间:2016年7月14日
// *
// */
//public interface PersonCenterService {
//
//	/**
//	 * 保存个人中心设置
//	 * @param publicId
//	 * @param param
//	 * @return
//	 */
//	public Map<String, Object> savePersonCenter(Integer busId, String param);
//
//	/**
//	 * 将模块排版
//	 * @param id
//	 * @param type
//	 * @return
//	 */
//	public Map<String, Object> upOrDownModule(Integer oneId, Integer twoId);
//
//	public List<Map<String,Object>> findOption(BusUserEntity busUser);
//
//	/**
//	 * 添加模块
//	 * @param publicId
//	 * @param param
//	 * @return
//	 */
//	public Map<String, Object> saveModule(Integer busId, String param);
//
//	/**
//	 * 获取商家拥有的模块
//	 * @param busUser
//	 * @param wxPublicUsers
//	 * @return
//	 */
//	public List<Map<String, Object>> findModel(BusUserEntity busUser);
//
//	/**
//	 * 查询省市区
//	 * @param request
//	 * @param cityCode
//	 */
//	public Map<String, Object> findCity(String cityCode);
//}
