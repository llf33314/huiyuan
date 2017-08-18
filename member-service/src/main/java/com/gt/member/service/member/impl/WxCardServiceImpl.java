package com.gt.member.service.member.impl;/*package com.gt.controller.member.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.gt.dao.common.WxCardMapper;
import com.gt.dao.common.WxCardQrcodeMapper;
import com.gt.dao.common.WxCardReceiveMapper;
import com.gt.dao.common.WxCardShelvesMapper;
import com.gt.dao.common.WxCardUserPayMapper;
import com.gt.dao.eat.RestaurantMapper;
import com.gt.dao.hotel.HotelMainMapper;
import com.gt.dao.mall.StoreMapper;
import com.gt.dao.member.CardMapper;
import com.gt.dao.member.CardRecordMapper;
import com.gt.dao.member.GiveRuleMapper;
import com.gt.dao.member.MemberMapper;
import com.gt.dao.member.PublicParameterSetMapper;
import com.gt.dao.member.UserConsumeMapper;
import com.gt.dao.member.card.DuofenCardMapper;
import com.gt.dao.set.WxShopMapper;
import com.gt.dao.user.BusUserBranchRelationMapper;
import com.gt.dao.user.BusUserMapper;
import com.gt.dao.util.DaoUtil;
import com.gt.entity.common.WxCard;
import com.gt.entity.common.WxCardQrcode;
import com.gt.entity.common.WxCardReceive;
import com.gt.entity.common.WxCardShelves;
import com.gt.entity.common.WxCardUserPay;
import com.gt.entity.mall.Store;
import com.gt.entity.member.Card;
import com.gt.entity.member.CardRecord;
import com.gt.entity.member.GiveRule;
import com.gt.entity.member.Member;
import com.gt.entity.member.PublicParameterSet;
import com.gt.entity.member.UserConsume;
import com.gt.entity.set.WxShop;
import com.gt.entity.user.BusUser;
import com.gt.entity.user.WxPublicUsers;
import com.gt.controller.common.dict.DictService;
import com.gt.controller.common.wxcard.color.CardState;
import com.gt.controller.common.wxcard.color.CardStatus;
import com.gt.controller.common.wxcard.color.Color;
import com.gt.controller.memberpay.MemberPayService;
import com.gt.util.CommonUtil;
import com.gt.util.DateTimeKit;
import com.gt.util.JsonUtil;
import com.gt.util.Page;
import com.gt.util.PropertiesUtil;
import com.gt.wx.controller.event.WxCardService;

*//**
 * 
 * 
 * @author 李逢喜
 * @version 创建时间：2015年9月7日 下午7:14:20
 * 
 *//*
@Service
public class WxCardServiceImpl implements IWxCardService {

	private static final Logger LOG = Logger.getLogger(WxCardServiceImpl.class);

	@Autowired
	private WxCardMapper wxCardMapper;

	@Autowired
	private WxCardReceiveMapper wxCardReceiveMapper;

	@Autowired
	private WxCardUserPayMapper wxCardUserPayMapper;

	@Autowired
	private WxCardQrcodeMapper wxCardQrcodeMapper;

	@Autowired
	private WxCardService wxCardService;

	@Autowired
	private WxCardShelvesMapper wxCardShelvesMapper;

	@Autowired
	private DaoUtil daoUtil;

	@Autowired
	private WxShopMapper wxShopMapper;

	@Autowired
	private StoreMapper storeMapper;

	@Autowired
	private HotelMainMapper hotelMainMapper;

	@Autowired
	private RestaurantMapper restaurantMapper;

	@Autowired
	private MemberMapper memberMapper;

	@Autowired
	private CardMapper cardMapper;

	@Autowired
	private BusUserBranchRelationMapper busUserBranchRelationMapper;

	@Autowired
	private UserConsumeMapper userConsumeMapper;

	@Autowired
	private CardRecordMapper cardRecordMapper;
	
	@Autowired
	private MemberPayService memberPayService;
	
	@Autowired
	private GiveRuleMapper giveRuleMapper;
	
	@Autowired
	private PublicParameterSetMapper publicParameterSetMapper;
	
	@Autowired
	private DictService dictService;
	
	@Autowired
	private BusUserMapper busUserMapper;

	@Override
	public WxCard findWxCardById(String card_id) {
		return wxCardMapper.selectByCardId(card_id);
	}

	@Override
	public Map<String, Object> upWxCard(WxCard wxCard) {
		Map<String, Object> result = new HashMap<String, Object>();
		Integer rows = 0;
		if (CommonUtil.isNotEmpty(wxCard.getId())) {
			rows = wxCardMapper.updateByPrimaryKeySelective(wxCard);
		} else {
			rows = wxCardMapper.insertSelective(wxCard);
		}
		if (rows > 0) {
			result.put("code", 1);
			result.put("msg", "操作成功");
		} else {
			result.put("code", -1);
			result.put("msg", "操作失败");
		}
		return result;
	}

	@Override
	public Map<String, Object> saveWxCardReceive(WxCardReceive cardReceive) {
		Map<String, Object> result = new HashMap<String, Object>();
		Integer rows = 0;
		if (CommonUtil.isNotEmpty(cardReceive.getId())) {
			rows = wxCardReceiveMapper.updateByPrimaryKeySelective(cardReceive);
		} else {
			rows = wxCardReceiveMapper.insertSelective(cardReceive);
		}
		if (rows > 0) {
			result.put("code", 1);
			result.put("msg", "操作成功");
		} else {
			result.put("code", -1);
			result.put("msg", "操作失败");
		}
		return result;
	}

	@Override
	public WxCardReceive findWxCardReceiveByCode(String code) {
		return wxCardReceiveMapper.selectByCode(code);
	}

	@Override
	public Map<String, Object> addWxCardUserPay(WxCardUserPay wxCardUserPay) {
		Map<String, Object> result = new HashMap<String, Object>();
		Integer rows = 0;
		rows = wxCardUserPayMapper.insertSelective(wxCardUserPay);
		if (rows > 0) {
			result.put("code", 1);
			result.put("msg", "操作成功");
		} else {
			result.put("code", -1);
			result.put("msg", "操作失败");
		}
		return result;
	}

	@Override
	public WxCardUserPay findWxCardUserPayByTransId(String transId) {
		return wxCardUserPayMapper.findWxCardUserPayByTransId(transId);
	}

	@Override
	public Map<String, Object> upQrcode(WxCardQrcode cardQrcode) {
		Map<String, Object> result = new HashMap<String, Object>();
		Integer rows = 0;
		rows = wxCardQrcodeMapper.updateByPrimaryKeySelective(cardQrcode);
		if (rows > 0) {
			result.put("url", cardQrcode.getUrl());
			result.put("code", 1);
			result.put("msg", "操作成功");
		} else {
			result.put("code", -1);
			result.put("msg", "操作失败");
		}
		return result;
	}

	@Override
	public Map<String, Object> saveOrUpdateCard(WxPublicUsers wxPublicUsers,
			BusUser busUser, String wxcardParam) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			JSONObject obj = JSONObject.fromObject(wxcardParam);
			WxCard wxCard = new WxCard();
			if (CommonUtil.isNotEmpty(obj.get("id"))) {
				wxCard.setId(obj.getInt("id"));
			}

			if (CommonUtil.isNotEmpty(obj.get("logo_url"))) {
				if (obj.get("logo_url").toString().contains("/upload")) {
					wxCard.setLogoUrl(obj.get("logo_url").toString());
				} else {
					wxCard.setLogoUrl(PropertiesUtil.getArticleUrl()
							+ File.separator + obj.get("logo_url").toString());
				}
			} else {
				map.put("result", false);
				map.put("message", "请选择商户logo");
				return map;
			}
			wxCard.setPublicId(wxPublicUsers.getId());
			wxCard.setUserId(busUser.getId());
			if (CommonUtil.isNotEmpty(obj.get("code_type"))) {
				wxCard.setCodeType(obj.get("code_type").toString());
			}
			if (CommonUtil.isNotEmpty(obj.get("brand_name"))) {
				wxCard.setBrandName(obj.getString("brand_name"));
			}
			if (CommonUtil.isNotEmpty(obj.get("card_type"))) {
				wxCard.setCardType(obj.get("card_type").toString());
			}
			if (CommonUtil.isNotEmpty(obj.get("deal_detail"))) {
				wxCard.setDealDetail(obj.get("deal_detail").toString());
			}
			if (CommonUtil.isNotEmpty(obj.get("cash_least_cost"))) {
				wxCard.setCashLeastCost(CommonUtil.toDouble(obj
						.get("cash_least_cost")));
			}
			if (CommonUtil.isNotEmpty(obj.get("reduce_cost"))) {
				wxCard.setReduceCost(obj.getDouble("reduce_cost"));
			}
			if (CommonUtil.isNotEmpty(obj.get("discount"))) {
				wxCard.setDiscount(CommonUtil.toDouble(obj.get("discount")));
			}
			if (CommonUtil.isNotEmpty(obj.get("gift"))) {
				wxCard.setGift(obj.get("gift").toString());
			}
			if (CommonUtil.isNotEmpty(obj.get("default_detail"))) {
				wxCard.setDefaultDetail(obj.get("default_detail").toString());
			}
			if (CommonUtil.isNotEmpty(obj.get("title"))) {
				wxCard.setTitle(obj.get("title").toString());
			}
			if (CommonUtil.isNotEmpty(obj.get("sub_title"))) {
				wxCard.setSubTitle(obj.getString("sub_title"));
			}
			if (CommonUtil.isNotEmpty(obj.get("color"))) {
				wxCard.setColor(Color.getColor(obj.getString("color")));
			}
			if (CommonUtil.isNotEmpty(obj.get("notice"))) {
				wxCard.setNotice(obj.getString("notice"));
			}
			if (CommonUtil.isNotEmpty(obj.get("description"))) {
				wxCard.setDescription(obj.getString("description"));
			}
			if (CommonUtil.isNotEmpty(obj.get("quantity"))) {
				wxCard.setQuantity(obj.getInt("quantity"));
			}
			if (CommonUtil.isNotEmpty(obj.get("type"))) {
				wxCard.setType(obj.getString("type"));
			}
			if (CommonUtil.isNotEmpty(obj.get("begin_timestamp"))) {
				wxCard.setBeginTimestamp(DateTimeKit.parse(
						obj.getString("begin_timestamp"), "yyyy-MM-dd"));
			}
			if (CommonUtil.isNotEmpty(obj.get("end_timestamp"))) {
				wxCard.setEndTimestamp(DateTimeKit.parse(
						obj.getString("end_timestamp"), "yyyy-MM-dd HH:mm:ss"));
			}
			if (CommonUtil.isNotEmpty(obj.get("fixed_term"))) {
				wxCard.setFixedTerm(obj.getInt("fixed_term"));
			}
			if (CommonUtil.isNotEmpty(obj.get("fixed_begin_term"))) {
				wxCard.setFixedBeginTerm(obj.getInt("fixed_begin_term"));
			}
			if (CommonUtil.isNotEmpty(obj.get("use_custom_code"))) {
				wxCard.setUseCustomCode(obj.getInt("use_custom_code") == 0 ? false
						: true);
			}
			if (CommonUtil.isNotEmpty(obj.get("bind_openid"))) {
				wxCard.setBindOpenid(obj.getInt("bind_openid") == 0 ? false
						: true);
			}
			if (CommonUtil.isNotEmpty(obj.get("service_phone"))) {
				wxCard.setServicePhone(obj.getString("service_phone"));
			}
			if (CommonUtil.isNotEmpty(obj.get("location_id_list"))) {
				wxCard.setLocationIdList(obj.getString("location_id_list"));
			}
			if (CommonUtil.isNotEmpty(obj.get("source"))) {
				wxCard.setLocationIdList(obj.getString("source"));
			}
			if (CommonUtil.isNotEmpty(obj.get("custom_url_name"))) {
				wxCard.setCustomUrlName(obj.getString("custom_url_name"));
			}
			if (CommonUtil.isNotEmpty(obj.get("center_title"))) {
				wxCard.setCenterTitle(obj.getString("center_title"));
			}
			if (CommonUtil.isNotEmpty(obj.get("center_sub_title"))) {
				wxCard.setCenterSubTitle(obj.getString("center_sub_title"));
			}
			if (CommonUtil.isNotEmpty(obj.get("center_url"))) {
				wxCard.setCenterUrl(obj.getString("center_url"));
			}
			if (CommonUtil.isNotEmpty(obj.get("custom_url"))) {
				wxCard.setCustomUrl(obj.getString("custom_url"));
			}
			if (CommonUtil.isNotEmpty(obj.get("custom_url_sub_title"))) {
				wxCard.setCustomUrlSubTitle(obj
						.getString("custom_url_sub_title"));
			}
			if (CommonUtil.isNotEmpty(obj.get("promotion_url_name"))) {
				wxCard.setPromotionUrlName(obj.getString("promotion_url_name"));
			}
			if (CommonUtil.isNotEmpty(obj.get("promotion_url"))) {
				wxCard.setPromotionUrl(obj.getString("promotion_url"));
			}
			if (CommonUtil.isNotEmpty(obj.get("promotion_url_sub_title"))) {
				wxCard.setPromotionUrlSubTitle(obj
						.getString("promotion_url_sub_title"));
			}
			if (CommonUtil.isNotEmpty(obj.get("get_limit"))) {
				wxCard.setGetLimit(obj.getInt("get_limit"));
			}
			if (CommonUtil.isNotEmpty(obj.get("can_share"))) {
				wxCard.setCanShare(obj.getInt("can_share") == 0 ? false : true);
			}
			if (CommonUtil.isNotEmpty(obj.get("can_give_friend"))) {
				wxCard.setCanGiveFriend(obj.getInt("can_give_friend") == 0 ? false
						: true);
			}
			if (CommonUtil.isNotEmpty(obj.get("accept_category"))) {
				wxCard.setAcceptCategory(obj.getString("accept_category"));
			}
			if (CommonUtil.isNotEmpty(obj.get("reject_category"))) {
				wxCard.setRejectCategory(obj.getString("reject_category"));
			}
			if (CommonUtil.isNotEmpty(obj.get("least_cost"))) {
				wxCard.setLeastCost(obj.getDouble("least_cost"));
			}
			if (CommonUtil.isNotEmpty(obj.get("object_use_for"))) {
				wxCard.setObjectUseFor(obj.getString("object_use_for"));
			}
			if (CommonUtil.isNotEmpty(obj.get("can_use_with_other_discount"))) {
				wxCard.setCanUseWithOtherDiscount(obj
						.getInt("can_use_with_other_discount") == 0 ? false
						: true);
			}
			if (CommonUtil.isNotEmpty(obj.get("summary"))) {
				wxCard.setSummary(obj.getString("summary"));
			}
			if (CommonUtil.isNotEmpty(obj.get("icon_url_list"))) {
				if (obj.getString("icon_url_list").contains("/upload")) {
					List<Object> list = new ArrayList<Object>();
					list.add(obj.get("icon_url_list"));
					wxCard.setIconUrlList(JSONArray.fromObject(list).toString());
				} else {
					List<Object> list = new ArrayList<Object>();
					list.add(PropertiesUtil.getArticleUrl() + File.separator
							+ obj.getString("icon_url_list"));
					wxCard.setIconUrlList(JSONArray.fromObject(list).toString());
				}
			}
			if (CommonUtil.isNotEmpty(obj.get("text_image_list"))) {
				wxCard.setTextImageList(obj.getString("text_image_list"));
			}
			if (CommonUtil.isNotEmpty(obj.get("business_service"))) {
				wxCard.setBusinessService(obj.getString("business_service"));
			}
			if (CommonUtil.isNotEmpty(obj.get("time_limit"))) {
				wxCard.setTimeLimit(obj.getString("time_limit"));
			}
			if (CommonUtil.isNotEmpty(obj.get("card_status"))) {
				wxCard.setCardStatus(0);
			}
			if (CommonUtil.isNotEmpty(obj.get("isCallSMS"))) {
				wxCard.setIscallsms(Byte.parseByte(obj.get("isCallSMS")
						.toString()));
			}
			if (CommonUtil.isNotEmpty(obj.get("image"))) {
				if (obj.getString("image").contains("/upload")) {
					wxCard.setImage(obj.getString("image"));
				} else {
					wxCard.setImage(PropertiesUtil.getArticleUrl()
							+ File.separator + obj.getString("image"));
				}
			}
			if (CommonUtil.isNotEmpty(obj.get("phone"))) {
				wxCard.setPhone(obj.getString("phone"));
			}
			if(CommonUtil.isNotEmpty(obj.get("timeType"))){
				wxCard.setTimetype(Byte.valueOf(obj.get("timeType").toString()));
			}
			if (CommonUtil.isNotEmpty(obj.get("id"))) {
				wxCardMapper.updateByPrimaryKeySelective(wxCard);
			} else {
				// 修改服务场景入口
				wxCardMapper.insertSelective(wxCard);
				
				WxCard wc=new WxCard();
				wc.setId(wxCard.getId());
				String url = PropertiesUtil.getArticleUrl() +"/WxCardPhoneController/"
						+ wxPublicUsers.getId() 
						+ "/79B4DE7C/findStore.do?id="
						+ wxCard.getId();
				wc.setCustomUrl(url);
				wxCardMapper.updateByPrimaryKeySelective(wc);
			}
			map.put("result", true);
			map.put("message", "卡券保存成功");
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", false);
			map.put("message", "卡券保存失败");
			LOG.error("保存卡券出现异常", e);
		}
		return map;
	}

	@Override
	public Page findWxCard(Integer publicId, Map<String, Object> params) {
		try {
			params.put("curPage", CommonUtil.isEmpty(params.get("curPage")) ? 1
					: CommonUtil.toInteger(params.get("curPage")));
			int pageSize = 10;
			Object search1 = params.get("search");
			String search = null;
			if (CommonUtil.isNotEmpty(search1)) {
				search = search1.toString();
			}
			int rowCount = wxCardMapper.countWxCard(publicId, search);
			Page page = new Page(CommonUtil.toInteger(params.get("curPage")),
					pageSize, rowCount, "wxCard/findCardCoupon.do");
			params.put("firstResult", pageSize
					* ((page.getCurPage() <= 0 ? 1 : page.getCurPage()) - 1));
			params.put("maxResult", pageSize);
			List<Map<String, Object>> list = wxCardMapper.findByTitle(
					Integer.parseInt(params.get("firstResult").toString()),
					pageSize, publicId, search);
			page.setSubList(list);
			return page;
		} catch (Exception e) {
			LOG.error("分页查询异常", e);
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Page findWxCardReceive(String cardId, Map<String, Object> params) {
		try {
			if (CommonUtil.isEmpty(params)) {
				params = new HashMap<String, Object>();
			}
			params.put("curPage", CommonUtil.isEmpty(params.get("curPage")) ? 1
					: CommonUtil.toInteger(params.get("curPage")));
			int pageSize = 10;
			int rowCount = wxCardReceiveMapper.findByCardIdCount(cardId);
			Page page = new Page(CommonUtil.toInteger(params.get("curPage")),
					pageSize, rowCount, "wxCard/findReceiveCard.do");
			params.put("firstResult", pageSize
					* ((page.getCurPage() <= 0 ? 1 : page.getCurPage()) - 1));
			params.put("maxResult", pageSize);
			List<Map<String, Object>> list = wxCardReceiveMapper.findByCardId(
					cardId,
					Integer.parseInt(params.get("firstResult").toString()),
					pageSize);
			List<Map<String, Object>> memberList = new ArrayList<Map<String, Object>>();
			for (Map<String, Object> map : list) {
				if (map.containsKey("nickname")) {
					try {
						byte[] bytes = (byte[]) map.get("nickname");
						map.put("nickname", new String(bytes, "UTF-8"));
					} catch (Exception e) {
						map.put("nickname", null);
					}
					memberList.add(map);
				} else {
					memberList.add(map);
				}

			}
			page.setSubList(memberList);
			return page;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Map<String, Object> saveShelves(WxPublicUsers wxPublicUsers,
			Map<String, Object> params) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			String jsonStr = params.get("params").toString();
			Map<String, Object> jsonMap = JsonUtil.json2Map(jsonStr);
			WxCardShelves ws = new WxCardShelves();
			ws.setBanner(jsonMap.get("banner").toString());
			ws.setPageTitle(jsonMap.get("pagetitle").toString());
			ws.setCanShare(CommonUtil.toInteger(jsonMap.get("canShare")) == 0 ? false
					: true);
			ws.setEnddate(DateTimeKit.parse(jsonMap.get("endDate").toString(), "yyyy-MM-dd HH:mm:ss"));
			ws.setCardname(jsonMap.get("titles").toString());
			String[] ids = jsonMap.get("ids").toString().split(",");
			if (ids.length > 0) {
				List<Map<String, Object>> cardList = new ArrayList<Map<String, Object>>();
				List<Integer> cardIds=new ArrayList<Integer>();
				Map<String, Object> map = null;
				StringBuffer sb = new StringBuffer(
						"select card_id ,image,card_status from t_wx_card where id in (");
				for (int i = 0; i < ids.length; i++) {
					if (CommonUtil.isNotEmpty(ids[i])) {
						sb.append(ids[i] + ",");
						cardIds.add(CommonUtil.toInteger(ids[i]));
					}
				}
				sb.append("-1)");
				List<Map<String, Object>> listMap = daoUtil.queryForList(sb
						.toString());
				
				if (CommonUtil.isNotEmpty(listMap) && listMap.size() > 0) {
					for (Map<String, Object> map2 : listMap) {
						map = new HashMap<String, Object>();
						 if(!"2".equals(map2.get("card_status").toString())){
							 result.put("msg", "卡券请先送审通过后再添加到货架中!");
							 return result;
						}
						map.put("card_id", map2.get("card_id"));
						map.put("thumb_url", map2.get("image"));
						cardList.add(map);
					}
				}
				ws.setCardList(JSONArray.fromObject(cardList).toString());
				ws.setPublicid(wxPublicUsers.getId());
				ws.setScene("SCENE_NEAR_BY");
				result = wxCardService.createPage(ws, wxPublicUsers);
				if ("1".equals(result.get("code").toString())) {
					ws.setUrl(result.get("url").toString());
					ws.setPageId(CommonUtil.toInteger(result.get("page_id")));
					ws.setStatus(1);
					wxCardShelvesMapper.insertSelective(ws);
					
					//修改投放信息
					wxCardMapper.updateCardIsDelivery(cardIds);
					
					result.put("id", ws.getId());
					result.put("url", result.get("url"));
				}
				return result;
			}
			result.put("msg", "数据不完整,创建失败");
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("保存货架异常", e);
			result = new HashMap<String, Object>();
			result.put("msg", "数据不完整出现异常");
		}
		return result;
	}

	@Override
	public Map<String, Object> saveQrcode(WxPublicUsers wxPublicUsers,
			Map<String, Object> params) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			String jsonStr = params.get("params").toString();
			Map<String, Object> jsonMap = JsonUtil.json2Map(jsonStr);
			WxCardQrcode wq = new WxCardQrcode();
			wq.setActionName("QR_MULTIPLE_CARD");
			wq.setExpireSeconds(CommonUtil.toInteger(jsonMap
					.get("expire_seconds")) * 24 * 3600);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("card_id", jsonMap.get("cardId"));
			map.put("is_unique_code", true);
			JSONArray jsonArray = JSONArray.fromObject(map);
			wq.setActionInfo(jsonArray.toString());
			wxCardQrcodeMapper.insertSelective(wq);
			result = wxCardService.qrcodeCardCreate(wq, wxPublicUsers);
			if("1".equals(result.get("code").toString())){
				wxCardMapper.updateCardByCardId(jsonMap.get("cardId").toString());
			}
			
		} catch (Exception e) {
			LOG.error("二维码投放异常", e);
			result.put("code", -1);
			result.put("msg", "操作失败");
		}
		return result;
	}

	@Override
	public Map<String, List<Map<String, Object>>> findReceiveCard(
			WxPublicUsers wxPublicUsers, String openid) {
		Map<String, List<Map<String, Object>>> maps = new HashMap<String, List<Map<String, Object>>>();
		List<Map<String, Object>> cardRecord0 = new ArrayList<Map<String, Object>>(); // 正常
		List<Map<String, Object>> cardRecord4 = new ArrayList<Map<String, Object>>(); // 已使用
		List<Map<String, Object>> cardRecord5 = new ArrayList<Map<String, Object>>(); // 已过期

		try {
			List<Map<String, Object>> cardRecord = wxCardReceiveMapper
					.findByOpenId(wxPublicUsers.getId(), openid);
			WxCardReceive wxcr = null;
			for (Map<String, Object> map : cardRecord) {
				if ("0".equals(map.get("status").toString())) {
					map.put("code", map.get("user_card_code"));
					Map<String, Object> result = wxCardService.getCodeStatus(map,
							wxPublicUsers);
					if ("1".equals(result.get("code").toString())) {
						String status = result.get("status").toString();
						Integer sta = CardStatus.getCode(status);
						switch (sta) {
						case 0:
							cardRecord0.add(map);
							break;
						case 4:
							cardRecord4.add(map);
							break;
						case 2:
							cardRecord5.add(map);
							break;
						default:
							break;
						}
						if (sta != 0) {
							wxcr = new WxCardReceive();
							wxcr.setId(CommonUtil.toInteger(map.get("id")));
							wxcr.setStatus(sta);
							wxCardReceiveMapper.updateByPrimaryKeySelective(wxcr);
						}
					} else {
						LOG.error("校验用户领取优惠卷错误,code=" + map.get("user_card_code"));
					}
				} else if ("4".equals(map.get("status").toString())) {
					cardRecord4.add(map);
				} else if ("2".equals(map.get("status").toString())) {
					cardRecord5.add(map);
				}
			}
			maps.put("cardRecord0", cardRecord0);
			maps.put("cardRecord4", cardRecord4);
			maps.put("cardRecord5", cardRecord5);
			return maps;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

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

		List<Map<String, Object>> shopes = new ArrayList<Map<String, Object>>();
		Map<String, Object> maps = null;
		for (Map<String, Object> map : shops) {
			maps = new HashMap<String, Object>();
			// 商城
			Store sto = new Store();
			sto.setWxShopId(CommonUtil.toInteger(map.get("id")));
			List<Map<String, Object>> stores = storeMapper.findByShopId(sto);
			if (CommonUtil.isNotEmpty(stores) && stores.size() > 0) {
				maps.put("nameStore", map.get("business_name"));
				maps.put("modelStore", 3);
				maps.put("idStore", CommonUtil.toInteger(map.get("id")));
				maps.put("phoneStore", map.get("telephone"));
				maps.put("addrStore", map.get("address"));
			}
			// 酒店
			List<Map<String, Object>> hotelMains = hotelMainMapper
					.findShopId(CommonUtil.toInteger(map.get("id")));
			if (CommonUtil.isNotEmpty(hotelMains) && hotelMains.size() > 0) {
				maps.put("nameHotel", map.get("business_name"));
				maps.put("modelHotel", 2);
				maps.put("idHotel", hotelMains.get(0).get("id"));
				maps.put("phoneHotel", map.get("telephone"));
				maps.put("addrHotel", map.get("address"));
			}
			// 餐饮
			List<Map<String, Object>> eatStores = restaurantMapper
					.findByShopId(CommonUtil.toInteger(map.get("id")));
			if (CommonUtil.isNotEmpty(eatStores) && eatStores.size() > 0) {
				maps.put("nameEat", map.get("business_name"));
				maps.put("modelEat", 1);
				maps.put("idEat", eatStores.get(0).get("id"));
				maps.put("phoneEat", map.get("telephone"));
				maps.put("addrEat", map.get("address"));
			}
			shopes.add(maps);
		}

		return shopes;
	}

	*//**
	 * 查询卡券信息
	 *//*
	@Override
	public Map<String, Object> findCardReceive(WxPublicUsers wxPublicUsers,
			String code, BusUser busUser) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			WxCardReceive wcr = wxCardReceiveMapper.findByCode(
					wxPublicUsers.getId(), code);
			if (CommonUtil.isEmpty(wcr)) {
				wcr = wxCardReceiveMapper.findByCode(
						wxPublicUsers.getId(), "0"+code);
				if(CommonUtil.isEmpty(wcr)){
					map.put("result", false);
					map.put("message", "卡券信息不存在");
					return map;
				}
			}
			

			WxCard wxCard = wxCardMapper.selectByCardId(wcr.getCardId());
			
			//时间判断
			if(CommonUtil.isNotEmpty(wxCard.getBeginTimestamp()) && CommonUtil.isNotEmpty(wxCard.getEndTimestamp())){
				if(DateTimeKit.laterThanNow(wxCard.getBeginTimestamp())){
					map.put("result", false);
					map.put("message", "卡券未到有效时间");
					return map;
				}
				if(!DateTimeKit.laterThanNow(wxCard.getEndTimestamp())){
					map.put("result", false);
					map.put("message", "卡券已过期");
					return map;
				}
			}else{
				if(DateTimeKit.laterThanNow(DateTimeKit.addDays(wcr.getCtime(),wxCard.getFixedBeginTerm()))){
					map.put("result", false);
					map.put("message", "卡券未到有效时间");
					return map;
				}
				if(!DateTimeKit.laterThanNow(DateTimeKit.addDays(wcr.getCtime(),wxCard.getFixedTerm()))){
					map.put("result", false);
					map.put("message", "卡券已过期");
					return map;
				}
				
			}
			
			String day=DateTimeKit.getDayToEnglish();
			if(!wxCard.getTimeLimit().contains(day)){
				map.put("result", false);
				map.put("message", "该卡券不能"+DateTimeKit.getDay()+"使用");
				return map;
			}
			
			map.put("wxCard", JSONObject.fromObject(wxCard));
			// 门店信息
			List<Map<String, Object>> shopes = findShopes(wxCard.getId(),wxPublicUsers.getId(),
					busUser);
			if (CommonUtil.isEmpty(shopes) || shopes.size() == 0) {
				map.put("result", false);
				map.put("message", "该卡券不能在本店使用");
				return map;
			}
			map.put("shopes", JSONArray.fromObject(shopes));
			Member member = memberMapper.selectByOpenid(wcr.getOpenid(),wxPublicUsers.getId());
			if (CommonUtil.isNotEmpty(member.getMcId())) {
				List<Map<String, Object>> cards = cardMapper
						.findCardById(member.getMcId());
				if (cards.size() > 0) {
					map.put("cards", JSONObject.fromObject(cards.get(0)));
				}
			}
			map.put("result", true);
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("查询优惠券信息异常", e);
			map.put("result", false);
			map.put("message", "查询优惠券信息错误");
		}
		return map;
	}

	public List<Map<String, Object>> findShopes(Integer id,Integer publicId, BusUser busUser) {
		List<Map<String, Object>> poiIds=null;
		WxCard wxCard = wxCardMapper.selectByPrimaryKey(id);
		String location_id_list = wxCard.getLocationIdList();
		if (CommonUtil.isEmpty(location_id_list)) {
			return null;
		}
		List<Integer> poids = new ArrayList<Integer>();
		if(busUser.getPid()==0){
			poiIds=wxShopMapper.findWxShopbyPublicId1Ver2(busUser.getId());
			if (CommonUtil.isEmpty(poiIds) || poiIds.size() == 0) {
				return null;
			}
			for (Map<String, Object> map : poiIds) {
				if(CommonUtil.isEmpty(map.get("poiId")))continue;
				if (location_id_list.contains(map.get("poiId").toString())) {
					poids.add(CommonUtil.toInteger(map.get("poiId")));
				}
			}
		}else{
			poiIds = busUserBranchRelationMapper
					.findBusUserShop(busUser.getId());
			if (CommonUtil.isEmpty(poiIds) || poiIds.size() == 0) {
				return null;
			}
			for (Map<String, Object> map : poiIds) {
				if (location_id_list.contains(map.get("poiId").toString())) {
					poids.add(CommonUtil.toInteger(map.get("poiId")));
				}
			}
			
		}
		if (poids.size() == 0) {
			return null;
		}
		List<Map<String, Object>> shops = wxShopMapper
				.findWxShopByPoiIds(poids);

		List<Map<String, Object>> shopes = new ArrayList<Map<String, Object>>();
		Map<String, Object> maps = null;
		for (Map<String, Object> map : shops) {
			maps = new HashMap<String, Object>();
			// 商城
			Store sto = new Store();
			sto.setWxShopId(CommonUtil.toInteger(map.get("id")));
			List<Map<String, Object>> stores = storeMapper.findByShopId(sto);
			if (CommonUtil.isNotEmpty(stores) && stores.size() > 0) {
				maps.put("nameStore", map.get("business_name"));
				maps.put("modelStore", 3);
				maps.put("idStore", map.get("id"));
				maps.put("phoneStore", map.get("telephone"));
				maps.put("addrStore", map.get("address"));
			}
			// 酒店
			List<Map<String, Object>> hotelMains = hotelMainMapper
					.findShopId(CommonUtil.toInteger(map.get("id")));
			if (CommonUtil.isNotEmpty(hotelMains) && hotelMains.size() > 0) {
				maps.put("nameHotel", map.get("business_name"));
				maps.put("modelHotel", 2);
				maps.put("idHotel", map.get("id"));
				maps.put("phoneHotel", map.get("telephone"));
				maps.put("addrHotel", map.get("address"));
			}
			// 餐饮
			List<Map<String, Object>> eatStores = restaurantMapper
					.findByShopId(CommonUtil.toInteger(map.get("id")));
			if (CommonUtil.isNotEmpty(eatStores) && eatStores.size() > 0) {
				maps.put("nameEat", map.get("business_name"));
				maps.put("modelEat", 1);
				maps.put("idEat", map.get("id"));
				maps.put("phoneEat", map.get("telephone"));
				maps.put("addrEat", map.get("address"));
			}
			shopes.add(maps);
		}

		return shopes;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Map<String, Object> cardReceive(HttpServletRequest request,WxPublicUsers wxPublicUsers,
			String code, Integer storeId,Double money, BusUser bususer,
			Integer payType, Integer model,Double jifenmoney,Double fenbimoney) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			WxCardReceive wcr = wxCardReceiveMapper.findByCode1(
					wxPublicUsers.getId(), code);
			map.put("card_id", wcr.getCardId());
			map.put("code", code);
			Member member = memberMapper.selectByOpenid(wcr.getOpenid(),wxPublicUsers.getId());
			
			UserConsume uc = new UserConsume();
			// 添加会员记录
			WxCard wxCard = wxCardMapper.selectByCardId(wcr.getCardId());
			
			Card card=null;
			if (CommonUtil.isNotEmpty(member.getMcId())) {
				card = cardMapper.selectByPrimaryKey(member.getMcId());
				uc.setMcid(card.getMcId());
				uc.setCtid(card.getCtId());
				uc.setGtId(card.getGtId());
				
				
			}
			uc.setTotalmoney(money);

			switch (wxCard.getCardType()) {
			case "CASH":
				if(CommonUtil.isNotEmpty(card)){
					if(card.getCtId()==2){
						GiveRule give=giveRuleMapper.selectByPrimaryKey(card.getGrId());
						money=money*give.getGrDiscount()/100;
						uc.setDiscount(give.getGrDiscount());
					}
				}
				// 代金券
				if (money >= wxCard.getCashLeastCost()) {
					uc.setDiscountmoney(money - wxCard.getReduceCost());
				} else {
					map.put("result", false);
					map.put("message", "消费金额不足,卡券不能核销!");
					return map;
				}
				break;
			case "DISCOUNT":
				uc.setDiscount(new Double(wxCard.getDiscount() * 10)
						.intValue());
				uc.setDiscountmoney(money * wxCard.getDiscount() / 10);
				break;
			case "GIFT":
				uc.setDiscount(100);
				uc.setDiscountmoney(money);
				uc.setGivegift(wxCard.getGift());
				uc.setGiftcount(1);
				uc.setDiscount(100);
				uc.setDiscountmoney(money);
				break;
			case "GENERAL_COUPON":
				uc.setDiscount(100);
				uc.setDiscountmoney(money);
				uc.setDiscountdepict(wxCard.getDefaultDetail());
				uc.setDiscount(100);
				uc.setDiscountmoney(money);
				break;
			default:
				break;
			}
			
			if (CommonUtil.isNotEmpty(card) && payType == 1 && money > 0 && card.getCtId() == 3) {
				double surplusMoney = card.getMoney()
						- uc.getDiscountmoney();
				if (surplusMoney < 0) {
					map.put("result", false);
					map.put("message", "储值卡余额不足,请充值或现金支付");
					return map;
				}
				Card c=new Card();
				c.setMoney(surplusMoney);
				c.setMcId(card.getMcId());
				cardMapper.updateByPrimaryKeySelective(c);
				
			}
			
			int jifen=0;
			if(jifenmoney>0){
				PublicParameterSet pps=publicParameterSetMapper.findByPublicId(card.getPublicId());
				jifen=new Double(jifenmoney/pps.getChangemoney()*pps.getIntegralratio()).intValue();
			}
			
			int fenbi1=0;
			if(fenbimoney>0){
				Map<String, Object> dict = dictService.getDict("1058");
				fenbi1=new Double(fenbimoney*CommonUtil.toDouble(dict.get("1"))).intValue();
			}

			uc.setPublicId(member.getPublicId());
			uc.setBususerid(bususer.getId());
			uc.setMemberid(member.getId());
			uc.setRecordtype((byte) 2);
			uc.setCreatedate(new Date());
			uc.setPaystatus((byte) 0);
			uc.setUctype((byte) 14);
			uc.setIntegral(jifen);
			uc.setFenbi(fenbi1);
			uc.setUccount(0);
			uc.setCreatedate(new Date());
			uc.setModuletype(Byte.parseByte(model.toString()));
			uc.setStoreid(storeId);
			uc.setDvid(wxCard.getId());
			uc.setDiscountdepict(code);
			uc.setCardtype((byte)0);
			
			String orderCode=CommonUtil.getMEOrderCode();
			uc.setOrdercode(orderCode);
			uc.setPaystatus((byte)1);
			
			Map<String, Object> result = wxCardService.codeConsume(map,
					wxPublicUsers);
			if ("-1".equals(result.get("code").toString())) {
				map.put("result", false);
				map.put("message", "微信核销失败");
				throw new Exception();
			}
			userConsumeMapper.insertSelective(uc);
			
			if(CommonUtil.isNotEmpty(member.getMcId())){
				if(jifen>0 ||fenbi1>0 ){
					Member m =new Member();
					m.setId(member.getId());
					m.setFansCurrency(member.getFansCurrency()-fenbi1);
					m.setIntegral(member.getIntegral()-jifen);
					memberMapper.updateByPrimaryKeySelective(m);
					if(jifen>0){
						//添加会员卡操作记录
						memberPayService.saveCardRecordNew(card.getMcId(), (byte)2, jifen+"", "线下支付积分抵扣", member.getBusid(), null, card.getCtId(),-jifen);
					}
					if(fenbi1>0){
						//归还到商家账户
						bususer.setFansCurrency(bususer.getFansCurrency()+fenbi1);
						CommonUtil.setLoginUser(request, bususer);
						BusUser b=new BusUser();
						b.setId(bususer.getId());
						b.setFansCurrency(bususer.getFansCurrency());
						busUserMapper.updateByPrimaryKeySelective(b);
						
						//添加会员卡操作记录
						memberPayService.saveCardRecordNew(card.getMcId(), (byte)3, fenbi1+"", "线下支付粉币抵扣",  member.getBusid(), null, card.getCtId(),-fenbi1);
					}
				}
				
				//会员赠送
				memberPayService.findGiveRule(member.getPhone(), orderCode, "线下消费", (byte) 1);
			}
			
			map.put("result", true);
			map.put("message", "核销成功");
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("线下核销失败", e);
			map.put("result", false);
			map.put("message", "核销失败");
			throw new Exception();
		}
		return map;
	}
	
	
	

	@Override
	public Page findWxcardReceive(WxPublicUsers wxPublicUsers, BusUser busUser,
			Map<String, Object> params) {
		try {
			List<Integer> poids = new ArrayList<Integer>();
			List<Map<String, Object>> poiIds=null;
			if(busUser.getPid()==0){
				poiIds=wxShopMapper.findWxShopbyPublicId1Ver2(busUser.getId());
				if (CommonUtil.isEmpty(poiIds) || poiIds.size() == 0) {
					return null;
				}
				for (Map<String, Object> map : poiIds) {
					poids.add(CommonUtil.toInteger(map.get("id")));
				}
			}else{
				poiIds = busUserBranchRelationMapper
						.findBusUserShop(busUser.getId());
				if (CommonUtil.isEmpty(poiIds) || poiIds.size() == 0) {
					return null;
				}
				for (Map<String, Object> map : poiIds) {
					poids.add(CommonUtil.toInteger(map.get("branchid")));
				}
			}
			
		
			if (CommonUtil.isEmpty(poids) || poids.size() == 0) {
				return null;
			}

			params.put("curPage", CommonUtil.isEmpty(params.get("curPage")) ? 1
					: CommonUtil.toInteger(params.get("curPage")));
			int pageSize = 10;
			int rowCount = userConsumeMapper.countWxcardReceive(
					wxPublicUsers.getBusUserId(), poids,0);
			Page page = new Page(CommonUtil.toInteger(params.get("curPage")),
					pageSize, rowCount, "wxCard/redirectVerification.do");
			params.put("firstResult", pageSize
					* ((page.getCurPage() <= 0 ? 1 : page.getCurPage()) - 1));
			params.put("maxResult", pageSize);
			List<Map<String, Object>> list = userConsumeMapper
					.findWxcardReceive(wxPublicUsers.getBusUserId(), Integer
							.parseInt(params.get("firstResult").toString()),
							pageSize, poids,0);
			
			List<Map<String, Object>> shopes = findShopes1(wxPublicUsers.getId(),busUser);
			// 组装数据
			List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
			for (Map<String, Object> map : list) {
				for (Map<String, Object> shop : shopes) {
					if (CommonUtil.isEmpty(map.get("moduleType")))
						continue;
					switch (CommonUtil.toInteger(map.get("moduleType"))) {
					case 0:
						// 商城
						if (CommonUtil.isNotEmpty(shop.get("idStore")) &&  shop.get("idStore").equals(map.get("storeId"))) {
							map.put("storeName", shop.get("nameStore") + "(商城)");
						}
						break;
					case 1:
						// 酒店
						if (CommonUtil.isNotEmpty(shop.get("idHotel")) && shop.get("idHotel").equals(map.get("storeId"))) {
							map.put("storeName", shop.get("nameHotel") + "(酒店)");
						}
						break;
					case 2:
						if (CommonUtil.isNotEmpty(shop.get("idEat")) && shop.get("idEat").equals(map.get("storeId"))) {
							map.put("storeName", shop.get("nameEat") + "(餐饮)");
						}
						break;
					default:
						map.put("storeName", "未知");
						break;
					}
				}
				listMap.add(map);
			}
			page.setSubList(listMap);
			return page;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	*//**
	 * 登陆用户所有管理的门店
	 * 
	 * @param busUser
	 * @return
	 *//*
	public List<Map<String, Object>> findShopes1(Integer publicId,BusUser busUser) {
		List<Integer> poids = new ArrayList<Integer>();
		List<Map<String, Object>> poiIds=null;
		if(busUser.getPid()==0){
			poiIds=wxShopMapper.findWxShopbyPublicId1Ver2(busUser.getId());
			if (CommonUtil.isEmpty(poiIds) || poiIds.size() == 0) {
				return null;
			}
			for (Map<String, Object> map : poiIds) {
				if(CommonUtil.isNotEmpty(map.get("poiId"))){
					poids.add(CommonUtil.toInteger(map.get("poiId")));
				}
			}
		}else{
			poiIds = busUserBranchRelationMapper
					.findBusUserShop(busUser.getId());
			if (CommonUtil.isEmpty(poiIds) || poiIds.size() == 0) {
				return null;
			}
			for (Map<String, Object> map : poiIds) {
				poids.add(CommonUtil.toInteger(map.get("poiId")));
			}
		}
	
		if (poids.size() == 0) {
			return null;
		}
		List<Map<String, Object>> shops = wxShopMapper
				.findWxShopByPoiIds(poids);

		List<Map<String, Object>> shopes = new ArrayList<Map<String, Object>>();
		Map<String, Object> maps = null;
		for (Map<String, Object> map : shops) {
			maps = new HashMap<String, Object>();
			// 商城
			Store sto = new Store();
			sto.setWxShopId(CommonUtil.toInteger(map.get("id")));
			List<Map<String, Object>> stores = storeMapper.findByShopId(sto);
			if (CommonUtil.isNotEmpty(stores) && stores.size() > 0) {
				maps.put("nameStore", map.get("business_name"));
				maps.put("modelStore", 0);
				maps.put("idStore", map.get("id"));
				maps.put("phoneStore", map.get("telephone"));
				maps.put("addrStore", map.get("address"));
			}
			// 酒店
			List<Map<String, Object>> hotelMains = hotelMainMapper
					.findShopId(CommonUtil.toInteger(map.get("id")));
			if (CommonUtil.isNotEmpty(hotelMains) && hotelMains.size() > 0) {
				maps.put("nameHotel", map.get("business_name"));
				maps.put("modelHotel", 1);
				maps.put("idHotel", map.get("id"));
				maps.put("phoneHotel", map.get("telephone"));
				maps.put("addrHotel", map.get("address"));
			}
			// 餐饮
			List<Map<String, Object>> eatStores = restaurantMapper
					.findByShopId(CommonUtil.toInteger(map.get("id")));
			if (CommonUtil.isNotEmpty(eatStores) && eatStores.size() > 0) {
				maps.put("nameEat", map.get("business_name"));
				maps.put("modelEat", 2);
				maps.put("idEat", map.get("id"));
				maps.put("phoneEat", map.get("telephone"));
				maps.put("addrEat", map.get("address"));
			}
			shopes.add(maps);
		}

		return shopes;
	}

	@Override
	public Map<String, Object> synchro(WxPublicUsers wxPublicUsers, String card_Id) {
		Map<String, Object> map=new HashMap<String, Object>();
		try {
			String status=wxCardService.cardInfo(card_Id, wxPublicUsers);
			Integer code=CardState.getCode(status);
			wxCardMapper.updateCardByCardId1(code, card_Id);
			map.put("result", true);
			map.put("message", "同步成功");
		} catch (Exception e) {
			LOG.error("同步异常",e);
			map.put("result", false);
			map.put("message", "同步失败");
		}
		return map;
	}
	

	@Override
	public Page findWxCardShelve(WxPublicUsers wxPublicUsers,
			Map<String, Object> params) {
		try {
			if (CommonUtil.isEmpty(params)) {
				params = new HashMap<String, Object>();
			}
			params.put("curPage", CommonUtil.isEmpty(params.get("curPage")) ? 1
					: CommonUtil.toInteger(params.get("curPage")));
			int pageSize = 10;
			int rowCount = wxCardShelvesMapper.countWxCardShelves(wxPublicUsers.getId());
			Page page = new Page(CommonUtil.toInteger(params.get("curPage")),
					pageSize, rowCount, "wxCard/findWxCardShelve.do");
			params.put("firstResult", pageSize
					* ((page.getCurPage() <= 0 ? 1 : page.getCurPage()) - 1));
			params.put("maxResult", pageSize);
			List<Map<String, Object>> list = wxCardShelvesMapper.findWxCardShelves(wxPublicUsers.getId(), 
					Integer.parseInt(params.get("firstResult").toString()),
					pageSize);
			page.setSubList(list);
			return page;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public Map<String, Object> updatePublicParameterSet(Integer busId,
			Byte isOpenCard) {
		Map<String, Object> map=new HashMap<String, Object>();
		try {
			PublicParameterSet publicParameterSet = publicParameterSetMapper
					.findBybusId(busId);
			if (CommonUtil.isEmpty(publicParameterSet)) {
				publicParameterSet = new PublicParameterSet();
				publicParameterSet.setBusid(busId);
				publicParameterSet.setIsopencard(isOpenCard);
				publicParameterSetMapper.insertSelective(publicParameterSet);
			} else {
				publicParameterSet.setIsopencard(isOpenCard);
				publicParameterSetMapper
						.updateByPrimaryKeySelective(publicParameterSet);
			}
			map.put("result", true);
			map.put("message", "操作成功");
		} catch (Exception e) {
			LOG.error("操作失败", e);
			map.put("result", false);
			map.put("message", "修改是否开启卡券领取操作失败");
		}
		return map;
	}
	

	//<!-----------卡券对外接口Start------------>
	@Override
	public List<Map<String, Object>> findWxCardByShopId(Integer shopId,
			WxPublicUsers wxPublicUsers,Member member) {
		if(CommonUtil.isEmpty(wxPublicUsers)){
			return null;
		}
		
		WxShop wxShop=wxShopMapper.selectByPrimaryKey(shopId);
		if(CommonUtil.isEmpty(wxShop.getPoiid())){
			return null;
		}
		
		if(CommonUtil.isEmpty(member.getOpenid())){
			return null;
		}
		
		//查询优惠券信息
		List<Map<String, Object>> cardList=wxCardReceiveMapper.findByOpenId1(wxPublicUsers.getId(), member.getOpenid());
		List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
		if(CommonUtil.isNotEmpty(cardList) && cardList.size()>0){
			for (Map<String,Object> map2 : cardList) {
				if("GIFT".equals(map2.get("card_type").toString()) ||"GENERAL_COUPON".equals(map2.get("card_type").toString())){
					continue;
				}
				//时间判断
				if(CommonUtil.isNotEmpty(map2.get("begin_timestamp")) && CommonUtil.isNotEmpty(map2.get("end_timestamp"))){
					if(DateTimeKit.laterThanNow(DateTimeKit.parse(map2.get("begin_timestamp").toString(), "yyyy-MM-dd hh:mm:ss"))){
						continue;
					}
					if(!DateTimeKit.laterThanNow(DateTimeKit.parse(map2.get("end_timestamp").toString(), "yyyy-MM-dd hh:mm:ss"))){
						continue;
					}
				}else{
					if(DateTimeKit.laterThanNow(DateTimeKit.addDays(DateTimeKit.parse(map2.get("ctime").toString(), "yyyy-MM-dd hh:mm:ss"),CommonUtil.toInteger(map2.get("fixed_begin_term"))))){
						continue;
					}
					if(!DateTimeKit.laterThanNow(DateTimeKit.addDays(DateTimeKit.parse(map2.get("ctime").toString(), "yyyy-MM-dd hh:mm:ss"),CommonUtil.toInteger(map2.get("fixed_term"))))){
						continue;
					}
				}
				
				String day=DateTimeKit.getDayToEnglish();
				if(!map2.get("time_limit").toString().contains(day)){
					continue;
				}
				if(map2.get("location_id_list").toString().contains(wxShop.getPoiid())){
					list.add(map2);
				}
			}
		}
		return list;
	}
	
	
	
	@Override
	public List<Map<String, Object>> findWxCardByShopIdAndMoney(Integer shopId,
			WxPublicUsers wxPublicUsers,Member member,Double money) {
		if(CommonUtil.isEmpty(wxPublicUsers)){
			return null;
		}
		
		WxShop wxShop=wxShopMapper.selectByPrimaryKey(shopId);
		if(CommonUtil.isEmpty(wxShop.getPoiid())){
			return null;
		}
		
		if(CommonUtil.isEmpty(member.getOpenid())){
			return null;
		}
		
		//查询优惠券信息
		List<Map<String, Object>> cardList=wxCardReceiveMapper.findByOpenId1(wxPublicUsers.getId(), member.getOpenid());
		List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
		if(CommonUtil.isNotEmpty(cardList) && cardList.size()>0){
			for (Map<String,Object> map2 : cardList) {
				if("GIFT".equals(map2.get("card_type").toString()) ||"GENERAL_COUPON".equals(map2.get("card_type").toString())){
					continue;
				}
				//时间判断
				if(CommonUtil.isNotEmpty(map2.get("begin_timestamp")) && CommonUtil.isNotEmpty(map2.get("end_timestamp"))){
					if(DateTimeKit.laterThanNow(DateTimeKit.parse(map2.get("begin_timestamp").toString(), "yyyy-MM-dd hh:mm:ss"))){
						continue;
					}
					if(!DateTimeKit.laterThanNow(DateTimeKit.parse(map2.get("end_timestamp").toString(), "yyyy-MM-dd hh:mm:ss"))){
						continue;
					}
				}else{
					if(DateTimeKit.laterThanNow(DateTimeKit.addDays(DateTimeKit.parse(map2.get("ctime").toString(), "yyyy-MM-dd hh:mm:ss"),CommonUtil.toInteger(map2.get("fixed_begin_term"))))){
						continue;
					}
					if(!DateTimeKit.laterThanNow(DateTimeKit.addDays(DateTimeKit.parse(map2.get("ctime").toString(), "yyyy-MM-dd hh:mm:ss"),CommonUtil.toInteger(map2.get("fixed_term"))))){
						continue;
					}
				}
				
				String day=DateTimeKit.getDayToEnglish();
				if(!map2.get("time_limit").toString().contains(day)){
					continue;
				}
				
				if("CASH".equals(map2.get("card_type").toString())){
					Double cash_least_cost=CommonUtil.toDouble(map2.get("cash_least_cost"));
					if(cash_least_cost>0 && cash_least_cost>money){
						continue;
					}
				}
				
				
				
				if(map2.get("location_id_list").toString().contains(wxShop.getPoiid())){
					list.add(map2);
				}
			}
		}
		return list;
	}
	
	
	
	@Override
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.SUPPORTS)
	public Map<String, Object> wxCardReceive(WxPublicUsers wxPublicUsers,
			String code){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			if(CommonUtil.isEmpty(wxPublicUsers)){
				map.put("result", -1);
				map.put("message", "核销失败");
				return map;
			}
			WxCardReceive wcr = wxCardReceiveMapper.findByCode1(
					wxPublicUsers.getId(), code);
			if(CommonUtil.isEmpty(wcr)){
				map.put("result", -1);
				map.put("message", "核销失败");
				return map;
			}
			map.put("card_id", wcr.getCardId());
			map.put("code", code);
			Map<String, Object> result = wxCardService.codeConsume(map,
					wxPublicUsers);
			if ("-1".equals(result.get("code").toString())) {
				map.put("result", -1);
				map.put("message", "微信核销失败");
				return map;
			}
			map.put("result", 1);
			map.put("message", "核销成功");
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("线下核销失败", e);
			map.put("result", -1);
			map.put("message", "核销失败");
		}
		return map;
	
	}
	
	
	@Override
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.SUPPORTS)
	public Map<String, Object> wxCardReceiveBackName(WxPublicUsers wxPublicUsers,
			String code){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			if(CommonUtil.isEmpty(wxPublicUsers)){
				map.put("result", -1);
				map.put("message", "核销失败");
				return map;
			}
			WxCardReceive wcr = wxCardReceiveMapper.findByCode1(
					wxPublicUsers.getId(), code);
			if(CommonUtil.isEmpty(wcr)){
				map.put("result", -1);
				map.put("message", "核销失败");
				return map;
			}
			map.put("card_id", wcr.getCardId());
			map.put("code", code);
			Map<String, Object> result = wxCardService.codeConsume(map,
					wxPublicUsers);
			if ("-1".equals(result.get("code").toString())) {
				map.put("result", -1);
				map.put("message", "微信核销失败");
				return map;
			}
			WxCard wxcard=wxCardMapper.selectByCardId(wcr.getCardId());
			
			map.put("cardId", wxcard.getId());
			map.put("cardName", wxcard.getTitle());
			map.put("result", 1);
			map.put("message", "核销成功");
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("线下核销失败", e);
			map.put("result", -1);
			map.put("message", "核销失败");
		}
		return map;
	}

	@Override
	public List<Map<String, Object>> findWxCard(Integer publicId) {
		List<Map<String, Object>> wxCards=wxCardMapper.findWxCard(publicId, new Date());
		return wxCards;
	}

	@Override
	public WxCard findWxCardById(Integer id) {
		return wxCardMapper.selectByPrimaryKey(id);
	}
	
	
	

	//<!-----------卡券对外接口END-------------->

}
*/