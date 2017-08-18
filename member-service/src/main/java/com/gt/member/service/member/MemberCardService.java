/**
 * P 2016年3月8日
 */
package com.gt.member.service.member;

import java.util.List;
import java.util.Map;


import com.gt.member.entity.MemberCardmodel;
import com.gt.member.entity.MemberCardrecord;

/**
 * 会员卡业务处理
 * @author pengjiangli
 * @version
 * 创建时间:2016年3月8日
 *
 */
public interface MemberCardService {

	/**
	 * 分组查询卡片信息
	 * @return
	 */
	List<MemberCardmodel> findGroupByType(Integer busId);

	/**
     * 根据类型查询模板
     * @param cmType
     * @return
     */
	List<MemberCardmodel>	findByType(Integer cmType);

	/**
	 * 根据商户id和模板id 查询同一类型模板
	 * @param publicId
	 * @param ctId
	 * @return
	 */
	List<MemberCardmodel > findCmType(Integer publicId, Integer ctId);

	Map<String, Object> saveCardModel(Integer busId, String param)throws Exception;


	public MemberCardrecord saveCardRecordNew(Integer cardId, Byte recordType, String number,
                                        String itemName, Integer busId, String balance, Integer ctId, double amount);


	 /**
	  * 积分清0
	  * @param busIds
	  */
	 void clearJifen(String busIds);

	 /**
	  * 添加积分记录
	  * @param str
	  */
	 void jifenLog(String str);
}
