/**
 * P 2016年3月8日
 */
package com.gt.member.service.member.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gt.member.dao.MemberCardmodelDAO;
import com.gt.member.dao.MemberCardrecordDAO;
import com.gt.member.dao.MemberDAO;
import com.gt.member.entity.Member;
import com.gt.member.entity.MemberCardmodel;
import com.gt.member.entity.MemberCardrecord;
import com.gt.member.service.member.SystemMsgService;
import com.gt.member.service.member.MemberCardService;
import com.gt.member.util.CommonUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



/**
 * @author pengjiangli
 * @version
 * 创建时间:2016年3月8日
 *
 */
@Service
public class MemberCardServiceImpl implements MemberCardService {

	private static final Logger LOG=Logger.getLogger(MemberCardServiceImpl.class);

	@Autowired
	private MemberCardmodelDAO cardModelMapper;

	@Autowired
	private MemberCardrecordDAO cardRecordMapper;

	@Autowired
	private SystemMsgService systemMsgService;

	@Autowired
	private MemberDAO memberMapper;
	/**
	 *  分组查询卡片信息
	 */
	@Override
	public List<MemberCardmodel> findGroupByType(Integer busId) {
		return cardModelMapper.findGroupByType(busId);
	}

	/**
	 * 根据类型查询模板
	 */
	@Override
	public List<MemberCardmodel> findByType(Integer cmType) {
		return cardModelMapper.findByType(cmType);
	}

	/**
	 * 根据商户id和模板id 查询同一类型模板
	 */
	@Override
	public List<MemberCardmodel> findCmType(Integer publicId,Integer ctId){
		Integer cmType=cardModelMapper.findCmType(publicId, ctId);
		return findByType(cmType);
	}


	@Transactional(rollbackFor=Exception.class)
	@Override
	public Map<String, Object> saveCardModel(Integer busId, String param) throws Exception {
		Map<String, Object> map=new HashMap<String, Object>();
		try {
			JSONArray json=JSONArray.fromObject(param);
		    	MemberCardmodel cardModel= cardModelMapper.findCardModel();
			int i=0;
			for (Object object : json) {
				i++;
				JSONObject obj=JSONObject.fromObject(object);
			   	 MemberCardmodel cm=new MemberCardmodel();
				cm.setCmUrl(obj.getString("url").split("upload")[1]);
				cm.setCmType(cardModel.getCmType()+1);
				cm.setCmSort(i);
				cm.setBusId(busId);
				cardModelMapper.insert(cm);
			}
			map.put("result", true);
			map.put("message", "保存卡片模板异常");
		} catch (Exception e) {
			map.put("result", false);
			map.put("message", "保存卡片模板异常");
			throw new Exception();
		}
		return map;
	}



	/**
	 * 添加会员卡记录((新数据接口))
	 *
	 * @param cardId
	 *            卡类型id
	 * @param recordType
	 *            消费类型
	 * @param number
	 *            数量
	 * @param itemName
	 *            物品名称
	 *            公众号
	 * @param balance
	 *            余额
	 */
	@Override
	public MemberCardrecord saveCardRecordNew(Integer cardId, Byte recordType,
			String number, String itemName, Integer busId, String balance,
			Integer ctId, double amount) {
		if ( CommonUtil.isEmpty(busId)) {
			return null;
		}

	   	 MemberCardrecord cr = new MemberCardrecord();
		cr.setCardId(cardId);
		cr.setRecordType(recordType.intValue());
		cr.setNumber(number);
		cr.setCreateDate(new Date());
		cr.setItemName(itemName);
		cr.setBusId(busId);
		cr.setBalance(balance);
		cr.setCtId(ctId);
		cr.setAmount(amount);
		try {
			cardRecordMapper.insert(cr);
			if (recordType == 2) {
				Member member = memberMapper.findByMcId1(cardId);
				// 积分变动通知
				systemMsgService.jifenMsg(cr, member);
			}

		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("保存手机端记录异常", e);
		}
		return cr;
	}


	@Override
	public void clearJifen(String busIds) {
		try {
			List<Integer> list=new ArrayList<Integer>();
			String[] str=busIds.split(",");
			for (String s : str) {
				if(CommonUtil.isNotEmpty(s)){
					list.add(CommonUtil.toInteger(s));
				}
			}
		//	memberService.clearJifen(list);
		} catch (Exception e) {
			LOG.error("httpclien调用积分清0异常", e);
		}
	}



	@Override
	public void jifenLog(String str) {
		JSONObject obj=JSONObject.fromObject(str);
		saveCardRecordNew(obj.getInt("mcId"), (byte)2, obj.getString("number"), obj.getString("itemName"), obj.getInt("busId"), null, null, obj.getDouble("amount"));

	}



}
