///**
// * P 2016年3月8日
// */
//
//package com.gt.member.service.old.member;
//
//import com.gt.member.entity.BusUserEntity;
//
//import java.util.List;
//import java.util.Map;
//
//
//
//
//
///** @author pengjiangli
// * @version 创建时间:2016年3月8日 */
//public interface GradeTypeService {
//
//	/** 查询商户所拥有的会员卡类型
//	 *
//	 * @return */
//	public List<Map<String, Object>> findByBusId(Integer busId);
//
//	/** 保存或修改卡片
//	 *
//	 * /
//	public Integer saveOrUpdate(String jsonArray, Integer busId) throws Exception;
//
//	/** 根据商户id和卡片类型查询
//	 *
//	 * @param ctId
//	 * @return */
//	public List<Map<String, Object>> findBybusIdAndCtId(Integer busId, Integer ctId);
//
//	/** 根据公众号id和模板类型查询
//	 *
//	 * @param cmType
//	 * @return */
//	public List<Map<String, Object>> findByBusIdAndCmType(Integer busId, Integer cmType);
//
//	/** 根据公众号id和卡片类型删除
//	 *
//	 * @param ctId */
//	public boolean deleteGradeType(int busId, Integer ctId) throws Exception;
//
//	/**
//	 * 会员基础设置
//	 * @param qiandao
//	 * @return
//	 * @throws Exception
//	 */
//	public Map<String, Object> saveOrUpdataRadio(BusUserEntity busUser, String json, Integer qiandao) throws Exception;
//}
