package com.gt.member.service.personCenter; /**
 * P 2016年7月14日
 *//*
package com.gt.controller.personCenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.sf.json.JSONObject;

import com.gt.dao.personCenter.PersonCenterMapper;
import com.gt.dao.personCenter.PersonCenterModuleMapper;
import com.gt.dao.util.DaoUtil;
import com.gt.entity.personCenter.PersonCenter;
import com.gt.entity.personCenter.PersonCenterModule;
import com.gt.entity.user.BusUserEntity;
import com.gt.controller.common.dict.DictService;
import com.gt.util.CommonUtil;
import com.gt.util.PropertiesUtil;

*//**
 * @author pengjiangli  
 * @version 
 * 创建时间:2016年7月14日
 * 
 *//*
@Service
public class PersonCenterServiceImpl implements PersonCenterService{

	@Autowired
	private PersonCenterMapper personCenterMapper;
	
	@Autowired
	private PersonCenterModuleMapper personCenterModuleMapper;
	
	@Autowired
	private DictService dictService;
	
	
	@Autowired
	private DaoUtil daoUtil;
	
	@Override
	public Map<String, Object> savePersonCenter(Integer busId, String param) {
		Map<String, Object> map=new HashMap<String, Object>();
		try {
			PersonCenter personCenter=(PersonCenter) JSONObject.toBean(JSONObject.fromObject(param), PersonCenter.class);
			personCenter.setBackimage(personCenter.getBackimage().split("/upload")[1]);
			personCenter.setBusid(busId);
			if(CommonUtil.isNotEmpty(personCenter.getId())){
				personCenterMapper.updateByPrimaryKeySelective(personCenter);
			}else{
				personCenterMapper.insertSelective(personCenter);
			}
			map.put("result", true);
			map.put("message", "保存成功");
		} catch (Exception e) {
			map.put("result", false);
			map.put("message", "保存失败");
		}
		return map;
	}

	
	@Override
	public Map<String, Object> upOrDownModule(Integer oneId,Integer twoId) {
		Map<String, Object> map=new HashMap<String, Object>();
		try {
			
			PersonCenterModule module1= personCenterModuleMapper.selectByPrimaryKey(oneId);
			PersonCenterModule module2= personCenterModuleMapper.selectByPrimaryKey(twoId);
			Integer sort=module1.getSort();
			module1.setSort(module2.getSort());
			personCenterModuleMapper.updateByPrimaryKeySelective(module1);
			module2.setSort(sort);
			personCenterModuleMapper.updateByPrimaryKeySelective(module2);
			map.put("result", true);
			map.put("message", "操作成功");
		} catch (Exception e) {
			map.put("result", false);
			map.put("message", "操作失败");
		}
		return map;
	}


	
	public List<Map<String, Object>> zimenus(Integer userid, Integer level,
			Integer industryid) {
		String identifier=PropertiesUtil.getIdentifier();
		
		String sql =null;
		if ("2".equals(identifier)) {
			//微站
			sql = "SELECT c.menus_id,c.url FROM t_man_telecom_rolemenus a  "
			           +"  LEFT JOIN t_man_telecom_role b ON a.tel_roleid=b.id LEFT JOIN t_bus_menus c ON a.menus_id=c.menus_id "
			           +"   WHERE b.tel_indu="+industryid+" AND b.tel_type= "+level;
			
		}else{
			sql = "SELECT menus_id,url FROM t_bus_menus WHERE parentclass_id!=0 AND (menus_id IN  "
			           +"  ((SELECT DISTINCT(a.menu_id) FROM t_bus_role_menu a WHERE a.role_id IN (SELECT role_id FROM t_bus_user_role WHERE bus_user_id="+userid+"))) "
			           +"  OR menus_id IN ((SELECT DISTINCT(a.menus_id) FROM t_bus_menusgrade a LEFT JOIN t_bus_menus b ON a.menus_id=b.menus_id WHERE  a.level_id="+level+"  AND (b.industry=0 OR b.industry="+industryid+"))))";
		}
		
		
		
		return daoUtil.queryForList(sql);
	}

	@Override
	public List<Map<String, Object>> findOption(BusUserEntity busUser) {
		List<Map<String, Object>> mList=personCenterModuleMapper.findBybusId(busUser.getId());
		String urls="";
		if(CommonUtil.isNotEmpty(mList) && mList.size()>0){
			for (Map<String,Object> map : mList) {
				urls=urls+map.get("url")+",";
			}
		}
		
		List<Map<String, Object>> optionList=new ArrayList<Map<String,Object>>();
		List<Map<String, Object>> list=zimenus(busUser.getId(), busUser.getLevel(), busUser.getIndustryid());
		
		Map<String, Object> optionMap=null;
		for (Map<String,Object> map : list) {
			if(CommonUtil.isEmpty(map.get("url")))continue;
			String url="";
			optionMap=new HashMap<String, Object>();
			switch (CommonUtil.toString(map.get("url"))) {
			case "mircobespeak/indexstart.do":
				optionMap.put("id", 1);
				optionMap.put("name", "微预约");
				url="/mircobespeak/"+busUser.getId()+"/79B4DE7C/phoneMiList_1.do";
				optionMap.put("url", url);
				break;
			case "member/cardindex.do":
				optionMap.put("id", 2);
				optionMap.put("name", "会员卡");
				url="/phoneMemberController/"+busUser.getId()+"/79B4DE7C/findMember_1.do";
				optionMap.put("url", url);
				break;
			case "restaurant/res_start.do":
				optionMap.put("id", 3);
				optionMap.put("name", "微外卖");
				url="/eat/"+busUser.getId()+"/79B4DE7C/storeList_1.do";
				optionMap.put("url", url);
				break;
			case "store/start.do":
				optionMap.put("id", 4);
				optionMap.put("name", "商城");
				url="/mallPage/"+busUser.getId()+"/79B4DE7C/shopall_1.do";
				optionMap.put("url", url);
				break;
			case "hotel/hotelStart.do":
				url="/hotelMobile/"+busUser.getId()+"/79B4DE7C/toHotelList.do";
				optionMap.put("id", 5);
				optionMap.put("name", "酒店");
				optionMap.put("url", url);
				break;
			case "houseStore/start.do":
				url="/housePage/"+busUser.getId()+"/79B4DE7C/viewHomepage.do";
				optionMap.put("id", 6);
				optionMap.put("name", "房产中介");
				optionMap.put("url", url);
				break;
			case "tour/tourStart.do":
				url="/tourMobile/"+busUser.getId()+"/79B4DE7C/selectStore.do";
				optionMap.put("id", 7);
				optionMap.put("name", "旅游");
				optionMap.put("url", url);
				break;
			case "facialStoreMain/index.do":
				url="/facial/phone/store/79B4DE7C/"+busUser.getId()+"/listStore.do";
				optionMap.put("id", 8);
				optionMap.put("name", "美容");
				optionMap.put("url",url);
				break;
			default:
				break;
			}
			if(CommonUtil.isNotEmpty(optionMap.get("url"))){
				if(!urls.contains(optionMap.get("url").toString())){
					optionList.add(optionMap);
				}
			}
			
		}
		optionMap=new HashMap<String, Object>();
		optionMap.put("id", 6);
		optionMap.put("name", "地址管理");
		optionMap.put("url", "/phonePersonCenterController/79B4DE7C/addrIndex.do");
		if(CommonUtil.isNotEmpty(optionMap.get("url"))){
			if(!urls.contains(optionMap.get("url").toString())){
				optionList.add(optionMap);
			}
		}
		return optionList;
	}



	@Override
	public Map<String, Object> saveModule(Integer busId, String param) {
		Map<String, Object> map=new HashMap<String, Object>();
		try {
			PersonCenterModule module=(PersonCenterModule) JSONObject.toBean(JSONObject.fromObject(param), PersonCenterModule.class);
			module.setBusid(busId);
			if(CommonUtil.isEmpty(module.getId())){
				List<Map<String, Object>> list=personCenterModuleMapper.findBybusId(busId);
				if(CommonUtil.isNotEmpty(list) && list.size()>0){
					if(module.getSort()<=CommonUtil.toInteger(list.get(0).get("sort"))){
						map.put("result", false);
						map.put("message", "目前序号已排到"+list.get(0).get("sort"));
						return map;
					}
				}
			}
			module.setModuleimage(module.getModuleimage().split("/upload")[1]);
			if(CommonUtil.isNotEmpty(module.getId())){
				personCenterModuleMapper.updateByPrimaryKeySelective(module);
			}else{
				personCenterModuleMapper.insertSelective(module);
			}
			map.put("result", true);
			map.put("message", "操作成功");
		} catch (Exception e) {
			map.put("result", false);
			map.put("message", "操作失败");
		}
		return map;
	}


	
	@Override
	public List<Map<String, Object>> findModel(BusUserEntity busUser) {
		List<Map<String, Object>> optionList=new ArrayList<Map<String,Object>>();
		List<Map<String, Object>> list=zimenus(busUser.getId(), busUser.getLevel(), busUser.getIndustryid());
		
		Map<String, Object> optionMap=null;
		for (Map<String,Object> map : list) {
			if(CommonUtil.isEmpty(map.get("url")))continue;
			optionMap=new HashMap<String, Object>();
			switch (CommonUtil.toString(map.get("url"))) {
			case "restaurant/res_start.do":
				optionMap.put("id", 1);
				optionMap.put("name", "微餐饮");
				optionList.add(optionMap);
				break;
			case "store/start.do":
				optionMap.put("id", 2);
				optionMap.put("name", "商城");
				optionList.add(optionMap);
				break;
			case "hotel/hotelStart.do":
				optionMap.put("id", 3);
				optionMap.put("name", "酒店");
				optionList.add(optionMap);
				break;
			case "tour/tourStart.do":
				optionMap.put("id", 4);
				optionMap.put("name", "旅游");
				optionList.add(optionMap);
				break;
			case "houseStore/start.do":
				optionMap.put("id", 5);
				optionMap.put("name", "房产");
				optionList.add(optionMap);
				break;
			case "offlinepay/index.do":
				optionMap.put("id", 6);
				optionMap.put("name", "优惠买单");
				optionList.add(optionMap);
				break;
			case "facialStoreMain/index.do":
				optionMap.put("id", 7);
				optionMap.put("name", "美容");
				optionList.add(optionMap);
				break;

			default:
				break;
			}
		}
		return optionList;
	}



	@Override
	public Map<String, Object> findCity(String cityCode) {
		Map<String, Object> map=new HashMap<String, Object>();
		String sheng=cityCode.substring(0, 2)+"0000";
		String sql="select * from basis_city where city_level=2 and city_code = '"+sheng+"'";
		
		List<Map<String, Object>> shengList=daoUtil.queryForList(sql);
		
		String shi=cityCode.substring(0, 4)+"00";
		
		String sql1="select * from basis_city where city_level=3 and city_code = '"+shi+"'";
		
		List<Map<String, Object>> shiList=daoUtil.queryForList(sql1);
		
		
		String sql2="select * from basis_city where city_level=4 and city_code="+cityCode;
		
		List<Map<String, Object>> quList=daoUtil.queryForList(sql2);
		
		map.put("shengList", shengList);
		
		map.put("shiList", shiList);
		
		map.put("quList", quList);
		return map;
	}

}
*/