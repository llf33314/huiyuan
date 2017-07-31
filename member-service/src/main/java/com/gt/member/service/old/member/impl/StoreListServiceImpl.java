package com.gt.member.service.old.member.impl;/*package com.gt.service.member.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gt.dao.common.WxCardMapper;
import com.gt.dao.eat.RestaurantMapper;
import com.gt.dao.facial.FacialStoreMainMapper;
import com.gt.dao.hotel.HotelMainMapper;
import com.gt.dao.mall.StoreMapper;
import com.gt.dao.set.WxShopMapper;
import com.gt.dao.tour.TourMainMapper;
import com.gt.entity.common.WxCard;
import com.gt.entity.facial.FacialStoreMain;
import com.gt.entity.mall.Store;
import com.gt.entity.tour.TourMain;
import com.gt.service.facial.FacialStoreMainService;
import com.gt.service.hotel.HotelMainService;
import com.gt.service.member.StoreListService;
import com.gt.service.tour.TourMainService;
import com.gt.util.CommonUtil;

*//**
 * 门店接口类
 * @author Administrator
 *
 *//*
@Service
public class StoreListServiceImpl implements StoreListService{
	
	@Autowired
	private WxCardMapper wxCardMapper;
	
	@Autowired
	private WxShopMapper wxShopMapper;
	
	@Autowired
	private StoreMapper storeMapper;
	
	@Autowired
	private HotelMainMapper hotelMainMapper;
	
	@Autowired
	private RestaurantMapper restaurantMapper;
	
	@Autowired
	private TourMainMapper tourMainMapper;
	
	@Autowired
	private FacialStoreMainMapper facialStoreMainMapper;
	
	@Autowired
	private HotelMainService hotelMainService;
	
	
	
	@Override
	public List<Map<String, Object>> findStore(Integer id) {
		WxCard wxCard = wxCardMapper.selectByPrimaryKey(id);
		String location_id_list = wxCard.getLocationIdList();
		if (CommonUtil.isEmpty(location_id_list)) {
			return null;
		}
		String[] ids = location_id_list.split(",");
		List<Integer> poids = new ArrayList<Integer>();
		for (String str : ids) {
			poids.add(CommonUtil.toInteger(str));
		}
		List<Map<String, Object>> shops = wxShopMapper
				.findWxShopByPoiIds(poids);

		return filterStore(shops);
	}
	
	
	@Override
	public List<Map<String, Object>> findStore(String location_id_list) {
		if (CommonUtil.isEmpty(location_id_list)) {
			return null;
		}
		String[] ids = location_id_list.split(",");
		List<Integer> idList = new ArrayList<Integer>();
		for (String str : ids) {
			if(CommonUtil.isNotEmpty(str)){
				idList.add(CommonUtil.toInteger(str));
			}
		}
		List<Map<String, Object>> shops = wxShopMapper.findWxShopByIds(idList);
		return filterStore(shops);
	}
	
	*//**
	 * 门店信息筛选
	 * @param shops
	 * @return
	 *//*
	public List<Map<String, Object>> filterStore(List<Map<String, Object>> shops){
		List<Map<String, Object>> shopes = new ArrayList<Map<String, Object>>();
		Map<String, Object> maps = null;
		for (Map<String, Object> map : shops) {
			maps = new HashMap<String, Object>();
			// 商城
			Store sto = new Store();
			sto.setWxShopId(CommonUtil.toInteger(map.get("id")));
			List<Map<String, Object>> stores = storeMapper.findByShopId(sto);
			if (CommonUtil.isNotEmpty(stores) && stores.size() > 0) {
				maps.put("nameStore", map.get("business_name")+"(商城)");
				maps.put("modelStore", 3);
				maps.put("idStore", CommonUtil.toInteger(map.get("id")));
				maps.put("phoneStore", map.get("telephone"));
				maps.put("addrStore", map.get("address"));
			}
			// 酒店
			List<Map<String, Object>> hotelMains = hotelMainMapper
					.findShopId(CommonUtil.toInteger(map.get("id")));
			if (CommonUtil.isNotEmpty(hotelMains) && hotelMains.size() > 0) {
				maps.put("nameStore", map.get("business_name")+"(酒店)");
				maps.put("modelStore", 2);
				maps.put("idStore", hotelMains.get(0).get("id"));
				maps.put("phoneStore", map.get("telephone"));
				maps.put("addrStore", map.get("address"));
			}
			// 餐饮
			List<Map<String, Object>> eatStores = restaurantMapper
					.findByShopId(CommonUtil.toInteger(map.get("id")));
			if (CommonUtil.isNotEmpty(eatStores) && eatStores.size() > 0) {
				maps.put("nameStore", map.get("business_name")+"(餐饮)");
				maps.put("modelStore", 1);
				maps.put("idStore", eatStores.get(0).get("id"));
				maps.put("phoneStore", map.get("telephone"));
				maps.put("addrStore", map.get("address"));
			}
			shopes.add(maps);
		}

		return shopes;
	}
	
	
	@Override
	public List<Map<String, Object>> findDuofenCardStore(List<Map<String, Object>> list) {
		List<Map<String, Object>> shopes = new ArrayList<Map<String, Object>>();
		Map<String, Object> maps = null;
		for (Map<String, Object> map : list) {
			
			// 商城
			Store sto = new Store();
			sto.setWxShopId(CommonUtil.toInteger(map.get("id")));
			
			List<Map<String, Object>> stores = storeMapper.findByShopId(sto);
			if (CommonUtil.isNotEmpty(stores) && stores.size() > 0) {
				maps = new HashMap<String, Object>();
				maps.put("business_name", map.get("business_name")+"(商城)");
				maps.put("shopId", CommonUtil.toInteger(map.get("id")));
				maps.put("latitude", map.get("latitude"));
				maps.put("longitude", map.get("longitude"));
				maps.put("url", "/mallPage/"+map.get("id")+"/79B4DE7C/getPageIds.do");
				shopes.add(maps);
				
			}
			
			// 餐饮
			List<Map<String, Object>> eatStores = restaurantMapper
					.findByShopId(CommonUtil.toInteger(map.get("id")));
			if (CommonUtil.isNotEmpty(eatStores) && eatStores.size() > 0) {
				maps = new HashMap<String, Object>();
				maps.put("business_name", map.get("business_name")+"(餐饮)");
				maps.put("shopId", eatStores.get(0).get("id"));
				maps.put("latitude", map.get("latitude"));
				maps.put("longitude", map.get("longitude"));
				Integer id=CommonUtil.toInteger(eatStores.get(0).get("id"));
				maps.put("url", "/eat/79B4DE7C/index.do?resId="+id);
				shopes.add(maps);
			}
			
			
			List<TourMain> tourMains=tourMainMapper.getTourUrlByShopId(CommonUtil.toInteger(map.get("id")));
			if(CommonUtil.isNotEmpty(tourMains) && tourMains.size()>0){
				maps = new HashMap<String, Object>();
				maps.put("business_name", map.get("business_name")+"(旅游)");
				maps.put("shopId", CommonUtil.toInteger(map.get("id")));
				maps.put("latitude", map.get("latitude"));
				maps.put("longitude", map.get("longitude"));
				Integer id=CommonUtil.toInteger(tourMains.get(0).getId());
				maps.put("url", "/tourMobile/"+id+"/79B4DE7C/toTour.do");
				shopes.add(maps);
			}
			
			List<FacialStoreMain> mains = facialStoreMainMapper.queryByShopId(CommonUtil.toInteger(map.get("id")));
			if(CommonUtil.isNotEmpty(mains)&& mains.size() >0){
				maps = new HashMap<String, Object>();
				maps.put("business_name", map.get("business_name")+"(美容)");
				maps.put("shopId", CommonUtil.toInteger(map.get("id")));
				maps.put("latitude", map.get("latitude"));
				maps.put("longitude", map.get("longitude"));
				Integer id=CommonUtil.toInteger(mains.get(0).getId());
				maps.put("url", "/facial/phone/store/"+id+"/79B4DE7C/storeIndex.do");
				shopes.add(maps);
			}
			
			try {
				//酒店
				String url = hotelMainService.getHotelURLByShopId(CommonUtil.toString(map.get("id")));
				if(CommonUtil.isNotEmpty(url)){
					maps = new HashMap<String, Object>();
					maps.put("business_name", map.get("business_name")+"(酒店)");
					maps.put("shopId", CommonUtil.toInteger(map.get("id")));
					maps.put("latitude", map.get("latitude"));
					maps.put("longitude", map.get("longitude"));
					maps.put("url", url);
					shopes.add(maps);
				}
			}
			catch (Exception e) {
				// TODO: handle exception
			}
			
			
			
		}

		return shopes;
	}
	
}
*/