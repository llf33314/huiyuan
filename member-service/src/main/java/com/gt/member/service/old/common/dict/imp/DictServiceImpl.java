package com.gt.member.service.old.common.dict.imp;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import com.gt.member.dao.BusUserDAO;
import com.gt.member.entity.BusUser;
import com.gt.member.service.old.common.dict.DictService;
import com.gt.member.util.HttpClienUtil;
import com.gt.member.util.JsonUtil;
import com.gt.member.util.MemberConfig;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class DictServiceImpl implements DictService {
	
	private static final Logger LOG=Logger.getLogger(DictServiceImpl.class);

	@Autowired
	private MemberConfig memberConfig;

	@Autowired
	private BusUserDAO busUserDAO;

	@Override
	public SortedMap<String, Object> getDict(String type) {
		// TODO Auto-generated method stub
		String url= memberConfig.getWxmp_home()+"/dict/79B4DE7C/getDict.do";
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("dict_type", type);
		JSONObject obj=JSONObject.fromObject(map);
		try {
			JSONObject json = HttpClienUtil.httpPost(url, obj, false);
			if("0".equals(json.getString("error"))){
				String jsonMap=json.getString("dictMap");
				Map<String, Object> jsonMaps= JsonUtil.json2Map(jsonMap);
				SortedMap<String,Object> sort=new TreeMap<String,Object>(jsonMaps); 
				//return  (SortedMap<String, Object>) JsonUtil.parseJSON2Map(json.get("dictMap").toString());
				return sort;
			}
		} catch (Exception e) {
			LOG.error("getDict 方法http请求异常", e);
		}
		return null;
	}

	@Override
	public String dictBusUserNum(Integer userid, Integer level, Integer style,
			String dictstyle) {
		// TODO Auto-generated method stub
		String url=memberConfig.getWxmp_home()+"/dict/79B4DE7C/getBusUserNum.do";
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("userid", userid);
		map.put("level", level);
		map.put("style", style);
		map.put("dictstyle", dictstyle);
		JSONObject obj=JSONObject.fromObject(map);
		try {
			JSONObject json = HttpClienUtil.httpPost(url, obj, false);
			if("0".equals(json.getString("error"))){
				return json.getString("BusUserNum");
			}
		} catch (Exception e) {
			LOG.error("dictBusUserNum 方法http请求异常", e);
		}
		return null;
	}


	
	
	@Override
	public Integer shopuserid(Integer userid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> pidUserMap(Integer user_id) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	@Override
	public Integer pidUserId(Integer user_id) {
		// TODO Auto-generated method stub
		BusUser busUser=busUserDAO.selectById(user_id);
		if(busUser.getPid()>0){
			pidUserId(busUser.getPid());
		}
		return busUser.getId();
	}
	
}
