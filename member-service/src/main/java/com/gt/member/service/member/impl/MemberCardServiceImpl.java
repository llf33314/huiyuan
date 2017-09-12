/**
 * P 2016年3月8日
 */
package com.gt.member.service.member.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gt.member.dao.MemberCardmodelDAO;
import com.gt.member.dao.MemberCardrecordDAO;
import com.gt.member.dao.MemberEntityDAO;
import com.gt.member.entity.MemberCardmodel;
import com.gt.member.service.common.MemberCommonService;
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
	private MemberEntityDAO memberMapper;

	@Autowired
	private MemberCommonService memberCommonService;
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
		memberCommonService.saveCardRecordNew(obj.getInt("mcId"), (byte)2, obj.getString("number"), obj.getString("itemName"), obj.getInt("busId"), null, null, obj.getDouble("amount"));

	}



}
