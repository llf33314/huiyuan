package com.gt.member.service.old.member.impl;/*package com.gt.controller.member.impl;

import io.socket.client.socke;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gt.dao.common.BusUserMapper;
import com.gt.dao.member.CardMapper;
import com.gt.dao.member.CardRecordMapper;
import com.gt.dao.member.GiveRuleMapper;
import com.gt.dao.member.MemberMapper;
import com.gt.dao.member.RecommendMapper;
import com.gt.dao.member.UserConsumeMapper;
import com.gt.dao.member.card.DuofenCardAuthorizationMapper;
import com.gt.dao.member.card.DuofenCardGetMapper;
import com.gt.dao.member.card.DuofenCardMapper;
import com.gt.dao.member.card.DuofenCardReceiveLogMapper;
import com.gt.dao.member.card.DuofenCardReceiveMapper;
import com.gt.dao.util.DaoUtil;
import com.gt.entity.common.BusUser;
import com.gt.entity.common.WxPublicUsers;
import com.gt.entity.member.Card;
import com.gt.entity.member.Member;
import com.gt.entity.member.Recommend;
import com.gt.entity.member.UserConsume;
import com.gt.entity.member.card.DuofenCard;
import com.gt.entity.member.card.DuofenCardAuthorization;
import com.gt.entity.member.card.DuofenCardGet;
import com.gt.entity.member.card.DuofenCardReceive;
import com.gt.entity.member.card.DuofenCardReceiveLog;
import com.gt.controller.common.dict.DictService;
import com.gt.controller.member.DuofenCardService;
import com.gt.controller.member.MemberService;
import com.gt.controller.member.StoreListService;
import com.gt.controller.memberpay.MemberPayService;
import com.gt.util.CommonUtil;
import com.gt.util.DateTimeKit;
import com.gt.util.JsonUtil;
import com.gt.util.Page;
import com.gt.util.PropertiesUtil;

*//**
 * 多粉卡券service
 * 
 * @author Administrator
 *//*
@Service
public class DuofenCardServiceImpl implements DuofenCardService {

	private static final Logger LOG = Logger
			.getLogger(DuofenCardServiceImpl.class);

	@Autowired
	private DuofenCardMapper duofenCardMapper;

	@Autowired
	private DuofenCardReceiveMapper duofenCardReceiveMapper;

	@Autowired
	private DuofenCardGetMapper duofenCardGetMapper;


	@Autowired
	private DuofenCardReceiveLogMapper duofenCardReceiveLogMapper;

	@Autowired
	private MemberMapper memberMapper;

	@Autowired
	private MemberPayService memberPayService;

	@Autowired
	private CardMapper cardMapper;

	@Autowired
	private GiveRuleMapper giveRuleMapper;

	@Autowired
	private DictService dictService;

	@Autowired
	private UserConsumeMapper userConsumeMapper;

	@Autowired
	private CardRecordMapper cardRecordMapper;

	@Autowired
	private BusUserMapper busUserMapper;

	@Autowired
	private DaoUtil daoUtil;

	@Autowired
	private MemberService memberService;

	@Autowired
	private StoreListService storeListService;

	@Autowired
	private DuofenCardAuthorizationMapper duofenCardAuthorizationMapper;
	
	@Autowired
	private RecommendMapper recommendMapper;
	
	@Override
	public Page findDuofenCard(Integer busId, Map<String, Object> params) {
		try {
			params.put("curPage", CommonUtil.isEmpty(params.get("curPage")) ? 1
					: CommonUtil.toInteger(params.get("curPage")));
			int pageSize = 10;
			Object search1 = params.get("search");
			String search = null;
			if (CommonUtil.isNotEmpty(search1)) {
				search = search1.toString();
			}
			int rowCount = duofenCardMapper.countDuofenCard(busId, search);
			Page page = new Page(CommonUtil.toInteger(params.get("curPage")),
					pageSize, rowCount, "duofenCard/findCardCoupon.do");
			params.put("firstResult", pageSize
					* ((page.getCurPage() <= 0 ? 1 : page.getCurPage()) - 1));
			params.put("maxResult", pageSize);
			List<Map<String, Object>> list = duofenCardMapper.findDuofenCard(
					busId, search,
					Integer.parseInt(params.get("firstResult").toString()),
					pageSize);
			page.setSubList(list);
			return page;
		} catch (Exception e) {
			LOG.error("分页查询异常", e);
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Map<String, Object> saveOrUpdateCard(int busId, String duofenardParam) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			JSONObject obj = JSONObject.fromObject(duofenardParam);
			DuofenCard duofenCard = new DuofenCard();
			if (CommonUtil.isNotEmpty(obj.get("id"))) {
				duofenCard.setId(obj.getInt("id"));
			}

			if (CommonUtil.isNotEmpty(obj.get("logo_url"))) {
				if (obj.get("logo_url").toString().contains("/upload")) {
					duofenCard.setLogoUrl(obj.get("logo_url").toString()
							.split("/upload")[1]);
				}
			} else {
				map.put("result", false);
				map.put("message", "请选择商户logo");
				return map;
			}
			duofenCard.setUserId(busId);
			if (CommonUtil.isNotEmpty(obj.get("brand_name"))) {
				duofenCard.setBrandName(obj.getString("brand_name"));
			}
			if (CommonUtil.isNotEmpty(obj.get("card_type"))) {
				duofenCard.setCardType(Byte.valueOf(obj.get("card_type")
						.toString()));
			}
			if (CommonUtil.isNotEmpty(obj.get("deal_detail"))) {
				duofenCard.setDealDetail(obj.get("deal_detail").toString());
			}
			if (CommonUtil.isNotEmpty(obj.get("cash_least_cost"))) {
				duofenCard.setCashLeastCost(CommonUtil.toDouble(obj
						.get("cash_least_cost")));
			}
			if (CommonUtil.isNotEmpty(obj.get("reduce_cost"))) {
				duofenCard.setReduceCost(obj.getDouble("reduce_cost"));
			}
			if (CommonUtil.isNotEmpty(obj.get("discount"))) {
				duofenCard
						.setDiscount(CommonUtil.toDouble(obj.get("discount")));
			}
			if (CommonUtil.isNotEmpty(obj.get("gift"))) {
				duofenCard.setGift(obj.get("gift").toString());
			}
			if (CommonUtil.isNotEmpty(obj.get("default_detail"))) {
				duofenCard.setDefaultDetail(obj.get("default_detail")
						.toString());
			}
			if (CommonUtil.isNotEmpty(obj.get("title"))) {
				duofenCard.setTitle(obj.get("title").toString());
			}
			if (CommonUtil.isNotEmpty(obj.get("sub_title"))) {
				duofenCard.setSubTitle(obj.getString("sub_title"));
			}
			if (CommonUtil.isNotEmpty(obj.get("color"))) {
				duofenCard.setColor(obj.getString("color"));
			}
			if (CommonUtil.isNotEmpty(obj.get("notice"))) {
				duofenCard.setNotice(obj.getString("notice"));
			}
			if (CommonUtil.isNotEmpty(obj.get("description"))) {
				duofenCard.setDescription(obj.getString("description"));
			}
			if (CommonUtil.isNotEmpty(obj.get("quantity"))) {
				duofenCard.setQuantity(obj.getInt("quantity"));
			}
			if (CommonUtil.isNotEmpty(obj.get("type"))) {
				duofenCard.setType(obj.getString("type"));
			}
			if (CommonUtil.isNotEmpty(obj.get("begin_timestamp"))) {
				duofenCard.setBeginTimestamp(DateTimeKit.parse(
						obj.getString("begin_timestamp"), "yyyy-MM-dd"));
			}
			if (CommonUtil.isNotEmpty(obj.get("end_timestamp"))) {
				duofenCard.setEndTimestamp(DateTimeKit.parse(
						obj.getString("end_timestamp"), "yyyy-MM-dd HH:mm:ss"));
			}
			if (CommonUtil.isNotEmpty(obj.get("fixed_term"))) {
				duofenCard.setFixedTerm(obj.getInt("fixed_term"));
			}
			if (CommonUtil.isNotEmpty(obj.get("fixed_begin_term"))) {
				duofenCard.setFixedBeginTerm(obj.getInt("fixed_begin_term"));
			}

			if (CommonUtil.isNotEmpty(obj.get("service_phone"))) {
				duofenCard.setServicePhone(obj.getString("service_phone"));
			}
			if (CommonUtil.isNotEmpty(obj.get("location_id_list"))) {
				duofenCard.setLocationIdList(obj.getString("location_id_list"));
			}
			if (CommonUtil.isNotEmpty(obj.get("source"))) {
				duofenCard.setLocationIdList(obj.getString("source"));
			}

			if (CommonUtil.isNotEmpty(obj.get("can_share"))) {
				duofenCard.setCanShare(obj.getInt("can_share") == 0 ? false
						: true);
			}
			if (CommonUtil.isNotEmpty(obj.get("adduser"))) {
				duofenCard.setAdduser((byte) (obj.getInt("adduser")));
			}

			if (CommonUtil.isNotEmpty(obj.get("can_give_friend"))) {
				duofenCard
						.setCanGiveFriend(obj.getInt("can_give_friend") == 0 ? false
								: true);
			}

			if (CommonUtil.isNotEmpty(obj.get("least_cost"))) {
				duofenCard.setLeastCost(obj.getDouble("least_cost"));
			}

			if (CommonUtil.isNotEmpty(obj.get("summary"))) {
				duofenCard.setSummary(obj.getString("summary"));
			}

			if (CommonUtil.isNotEmpty(obj.get("time_limit"))) {
				duofenCard.setTimeLimit(obj.getString("time_limit"));
			}
			if (CommonUtil.isNotEmpty(obj.get("card_status"))) {
				duofenCard.setCardStatus(0);
			}
			if (CommonUtil.isNotEmpty(obj.get("isCallSMS"))) {
				duofenCard.setIscallsms(Byte.parseByte(obj.get("isCallSMS")
						.toString()));
			}
			if (CommonUtil.isNotEmpty(obj.get("image"))) {
				if (obj.getString("image").contains("/upload")) {
					duofenCard
							.setImage(obj.getString("image").split("/upload")[1]);
				}
			}
			if (CommonUtil.isNotEmpty(obj.get("phone"))) {
				duofenCard.setPhone(obj.getString("phone"));
			}
			if (CommonUtil.isNotEmpty(obj.get("timeType"))) {
				duofenCard.setTimetype(Byte.valueOf(obj.get("timeType")
						.toString()));
			}

			if (CommonUtil.isNotEmpty(obj.get("text_image_list"))) {
				duofenCard.setTextImageList(obj.get("text_image_list")
						.toString());
			}
			if (CommonUtil.isNotEmpty(obj.get("datetimeset"))) {
				duofenCard.setDatetimeset(obj.get("datetimeset").toString());
			}
			if (CommonUtil.isNotEmpty(obj.get("icon_url_list"))) {
				duofenCard.setIconUrlList(obj.getString("icon_url_list").split(
						"/upload")[1]);
			}

			if (CommonUtil.isNotEmpty(obj.get("id"))) {
				duofenCardMapper.updateByPrimaryKeySelective(duofenCard);
			} else {
				// 修改服务场景入口
				duofenCardMapper.insertSelective(duofenCard);
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
	public Map<String, Object> saveOrUpdateCardReceive(String param, int busId) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			DuofenCardReceive duofenCardReceive = (DuofenCardReceive) JSONObject
					.toBean(JSONObject.fromObject(param),
							DuofenCardReceive.class);
			JSONObject obj = JSONObject.fromObject(param);
			if (CommonUtil.isEmpty(obj.get("receivedate"))) {
				duofenCardReceive.setReceivedate(null);
			} else {
				duofenCardReceive.setReceivedate(DateTimeKit.parseDate(
						CommonUtil.toString(obj.get("receivedate")
								+ " 23:59:59"), "yyyy/MM/dd HH:mm:ss"));
			}
			duofenCardReceive.setCardimage(duofenCardReceive.getCardimage()
					.split("/upload")[1]);
			if (CommonUtil.isNotEmpty(duofenCardReceive.getId())) {
				duofenCardReceiveMapper
						.updateByPrimaryKeySelective(duofenCardReceive);
			} else {
				duofenCardReceive.setBusid(busId);
				duofenCardReceiveMapper.insertSelective(duofenCardReceive);
			}
			map.put("result", true);
			map.put("message", "保存成功");
		} catch (Exception e) {
			LOG.error("保存卡包失败", e);
			e.printStackTrace();
			map.put("result", false);
			map.put("message", "保存失败");
		}
		return map;
	}

	@Override
	public Page findDuofenCardReceive(int busId, Map<String, Object> params) {
		try {
			params.put("curPage", CommonUtil.isEmpty(params.get("curPage")) ? 1
					: CommonUtil.toInteger(params.get("curPage")));
			int pageSize = 10;

			int rowCount = duofenCardReceiveMapper.countCardReceive(busId);
			Page page = new Page(CommonUtil.toInteger(params.get("curPage")),
					pageSize, rowCount, "duofenCard/findDuofenCardReceive.do");
			params.put("firstResult", pageSize
					* ((page.getCurPage() <= 0 ? 1 : page.getCurPage()) - 1));
			params.put("maxResult", pageSize);
			List<Map<String, Object>> list = duofenCardReceiveMapper
					.findCardReceive(busId, Integer.parseInt(params.get(
							"firstResult").toString()), pageSize);
			page.setSubList(list);
			return page;
		} catch (Exception e) {
			LOG.error("分页查询异常", e);
			e.printStackTrace();
		}
		return null;
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public Map<String, Object> receiveCard(Map<String, Object> param,
			Integer memberId) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			DuofenCardReceive dfcr = duofenCardReceiveMapper
					.selectByPrimaryKey(CommonUtil.toInteger(param
							.get("cardreceiveId")));

			Map<String, Object> dfcrl = duofenCardReceiveLogMapper
					.countByCrIdAndMemberId(dfcr.getId(), memberId);

			if (1 == dfcr.getNumlimit()) {
				if (1 == dfcr.getMaxnumtype()) {
					if (CommonUtil.isNotEmpty(dfcrl) && dfcrl.size() > 0) {
						if (CommonUtil.isNotEmpty(dfcrl.get("crId"))
								&& CommonUtil.toString(dfcr.getId()).equals(
										dfcrl.get("crId").toString())) {
							Integer maxNum = CommonUtil.toInteger(dfcr
									.getMaxnum());
							Integer count = CommonUtil.toInteger(dfcrl
									.get("cId"));
							if (maxNum <= count) {
								map.put("result", false);
								map.put("message", "该卡券包您已领取完"); // 领取状态 1已领取完
																	// 0还可以领取
								return map;
							}
						}
					}
				} else {
					Integer id = dfcr.getId();
					Date beginDate = DateTimeKit.parse(DateTimeKit.getDate()
							+ " 00:00:00", "yyyy-MM-dd HH:mm:ss");
					Map<String, Object> logList = duofenCardReceiveLogMapper
							.countByCrIdAndDate(id, memberId, beginDate);
					if (CommonUtil.isNotEmpty(logList)) {
						Integer maxNum = dfcr.getMaxnum();
						Integer count = CommonUtil
								.toInteger(logList.get("cId"));
						if (maxNum <= count) {
							map.put("result", false);
							map.put("message", "该卡券包您今天已领取完"); // 领取状态 1已领取完
																// 0还可以领取
							return map;
						}
					}
				}
			}
			Member member = memberMapper.selectByPrimaryKey(memberId);
			if (dfcr.getDeliverytype() == 2) {
				if (!memberPayService.isMemember(memberId)) {
					map.put("result", false);
					map.put("message", "该卡包只允许会员领取");
					return map;
				}
				Card card = cardMapper.selectByPrimaryKey(member.getMcId());
				if (!dfcr.getGtids().contains(card.getGtId().toString())) {
					map.put("result", false);
					map.put("message", "当前会员等级不允许领取次卡包");
					return map;
				}

			}
			if (dfcr.getDeliverytype1() == 1 || dfcr.getDeliverytype() == 3) {
				map.put("result", false);
				map.put("message", "该卡包只能允许商场购买,不能领取");
				return map;
			}

			Member m1 = new Member();
			boolean flag = false; // 用来标示是否修改修改用户数据
			if (dfcr.getJifen() > 0) {
				// 扣除用户积分
				if (member.getIntegral() < dfcr.getJifen()) {
					map.put("result", false);
					map.put("message", "积分不足,不能领取");
					return map;
				}
				if (CommonUtil.isNotEmpty(member.getMcId())) {
					memberPayService.saveCardRecordNew(member.getMcId(),
							(byte) 2, dfcr.getJifen() + "积分", "领取优惠券扣除积分", dfcr.getBusid(),
							null, 0, -dfcr.getJifen());
				}
				flag = true;
				m1.setIntegral(member.getIntegral() - dfcr.getJifen());
			}

			if (dfcr.getFenbi() > 0) {
				// 扣除用户粉币
				if (member.getFansCurrency() < dfcr.getFenbi()) {
					map.put("result", false);
					map.put("message", "粉币不足,不能领取");
					return map;
				}
				if (CommonUtil.isNotEmpty(member.getMcId())) {
					memberPayService.saveCardRecordNew(member.getMcId(),
							(byte) 3, dfcr.getFenbi() + "粉币", "领取优惠券扣除粉币", dfcr.getBusid(),
							null, 0, -dfcr.getFenbi());
				}
				flag = true;
				m1.setFansCurrency(member.getFansCurrency() - dfcr.getFenbi());

				// 归还商户粉币
				memberPayService.returnfansCurrency(member.getBusid(),
						new Double(-dfcr.getFenbi()));
			}

			if (flag) {
				m1.setId(member.getId());
				memberMapper.updateByPrimaryKeySelective(m1);
			}

			// 获取方式0商场 1会员卡 2普通领取 3第三方商场 4ERP等
			// getType本公众号投放方式 1普通 2会员领取 3商场购买 4二维码下载 5券号短信投放
			byte getType = 0;
			if ("0".equals(dfcr.getDeliveryaddr())
					|| 0 == dfcr.getDeliveryaddr()) {
				switch (dfcr.getDeliverytype()) {
				case 1:
					getType = 2;
					break;
				case 2:
					getType = 2;
					break;
				case 3:
					getType = 0;
					break;
				case 4:
					getType = 2;
					break;

				default:
					break;
				}
			}

			if ("1".equals(dfcr.getDeliveryaddr())
					|| 1 == dfcr.getDeliveryaddr()) {
				getType = 3;
			}

			if ("2".equals(dfcr.getDeliveryaddr())
					|| 2 == dfcr.getDeliveryaddr()) {
				getType = 4;
			}

			DuofenCardGet duofenCardGet = new DuofenCardGet();
			duofenCardGet.setMemberid(memberId);
			String code = getCode(12);
			duofenCardGet.setCode(code);
			duofenCardGet.setGettype(Byte.valueOf(getType));
			duofenCardGet.setCardid(CommonUtil.toInteger(param.get("cardId")));
			duofenCardGet.setGetdate(new Date());
			duofenCardGet.setCardreceiveid(CommonUtil.toInteger(param
					.get("cardreceiveId")));
			duofenCardGet.setBusid(member.getBusid());
			DuofenCard duofenCard = duofenCardMapper
					.selectByPrimaryKey(CommonUtil.toInteger(param
							.get("cardId")));
			if ("DATE_TYPE_FIX_TIME_RANGE".equals(duofenCard.getType())) {
				duofenCardGet.setStarttime(duofenCard.getBeginTimestamp());
				duofenCardGet.setEndtime(duofenCard.getEndTimestamp());
			} else {
				duofenCardGet.setStarttime(DateTimeKit.addDate(new Date(),
						duofenCard.getFixedBeginTerm()));
				duofenCardGet.setEndtime(DateTimeKit.addDate(new Date(),
						duofenCard.getFixedTerm()));
			}

			duofenCardGetMapper.insertSelective(duofenCardGet);

			// 短信通知
			if (dfcr.getIscallsms() == 1) {
				WxPublicUsers wxPublicUsers = wxPublicUsersMapper
						.selectByPrimaryKey(member.getBusid());
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("busId", member.getBusid());
				params.put("model", 12);
				params.put("mobiles", duofenCard.getPhone());
				if (CommonUtil.isNotEmpty(wxPublicUsers)) {
					params.put("company", wxPublicUsers.getAuthorizerInfo());
				} else {
					BusUser user = busUserMapper.selectByPrimaryKey(member
							.getBusid());
					params.put("company", user.getMerchant_name());
				}
				params.put("content", "用户领取一张" + duofenCard.getTitle()
						+ "优惠券,编号：" + code);
				try {
					smsSpendingService.sendSms(params);
				} catch (Exception e) {
					LOG.error("短信发送失败", e);
				}
			}
			map.put("result", true);
			map.put("message", "领取成功");
		} catch (Exception e) {
			LOG.error("领取优惠券异常", e);
			throw new Exception();
		}
		return map;
	}

	*//**
	 * 自定义长度校验码
	 * 
	 * @return
	 *//*
	public static String getCode(int length) {
		StringBuffer buf = new StringBuffer("1,2,3,4,5,6,7,8,9,0");
		String[] arr = buf.toString().split(",");
		StringBuffer sb = new StringBuffer();
		Random random = new Random();
		for (int i = 0; i < 4; i++) {
			Integer count = arr.length;
			int a = random.nextInt(count);
			sb.append(arr[a]);
		}
		return ""+new Date().getTime()+sb.toString();
	}
	

	@Override
	public Page findCardGet(Integer cardId, Map<String, Object> params) {
		try {
			if (CommonUtil.isEmpty(params)) {
				params = new HashMap<String, Object>();
			}
			params.put("curPage", CommonUtil.isEmpty(params.get("curPage")) ? 1
					: CommonUtil.toInteger(params.get("curPage")));
			int pageSize = 10;
			int rowCount = duofenCardGetMapper.countCardGetByCardcardId(cardId);
			Page page = new Page(CommonUtil.toInteger(params.get("curPage")),
					pageSize, rowCount, "duofenCard/findReceiveCard.do");
			params.put("firstResult", pageSize
					* ((page.getCurPage() <= 0 ? 1 : page.getCurPage()) - 1));
			params.put("maxResult", pageSize);
			List<Map<String, Object>> list = duofenCardGetMapper
					.findCardGetBycardId(cardId, Integer.parseInt(params.get(
							"firstResult").toString()), pageSize);
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
	public void findCardGetByMemberId(HttpServletRequest request,
			Integer busId, Integer memberId) {
		List<Map<String, Object>> notUserCard = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> userCard = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> overTimeCard = new ArrayList<Map<String, Object>>();

		List<Integer> memberIds = memberPayService.findMemberIds(memberId);
		List<Map<String, Object>> cardGets = duofenCardGetMapper
				.findCardGetByMemberId(memberIds);

		List<Integer> notUseIdList = new ArrayList<Integer>();
		List<Integer> useIdList = new ArrayList<Integer>();
		List<Integer> overTimeCardList = new ArrayList<Integer>();
		for (Map<String, Object> card : cardGets) {
			// 未使用的卡券
			if ("0".equals(CommonUtil.toString(card.get("state")))) {
				if (!notUseIdList.contains(CommonUtil.toInteger(card
						.get("cardReceiveId")))) {
					notUseIdList.add(CommonUtil.toInteger(card
							.get("cardReceiveId")));
				}
				notUserCard.add(card);
			} else if ("1".equals(CommonUtil.toString(card.get("state")))) { // 已使用卡券
				if (!useIdList.contains(CommonUtil.toInteger(card
						.get("cardReceiveId")))) {
					useIdList.add(CommonUtil.toInteger(card
							.get("cardReceiveId")));
				}

				userCard.add(card);
			} else if ("2".equals(CommonUtil.toString(card.get("state")))) { // 已过期卡券
				overTimeCard.add(card);
				if (!overTimeCardList.contains(CommonUtil.toInteger(card
						.get("cardReceiveId")))) {
					overTimeCardList.add(CommonUtil.toInteger(card
							.get("cardReceiveId")));
				}

			}
		}
		request.setAttribute("notUserCardSize", notUserCard.size());
		request.setAttribute("userCardSize", userCard.size());
		request.setAttribute("overTimeCardSize", overTimeCard.size());
		// 查询卡包信息
		if (notUseIdList.size() > 0) {
			List<Map<String, Object>> notUserCR = duofenCardReceiveMapper
					.findInIds(notUseIdList);
			request.setAttribute("notUserCR", notUserCR);
		}
		if (useIdList.size() > 0) {
			List<Map<String, Object>> userCR = duofenCardReceiveMapper
					.findInIds(useIdList);
			request.setAttribute("userCR", userCR);
		}
		if (overTimeCardList.size() > 0) {
			List<Map<String, Object>> overTimeCardCR = duofenCardReceiveMapper
					.findInIds(overTimeCardList);
			request.setAttribute("overTimeCardCR", overTimeCardCR);
		}

		// 朋友的券publicId
		List<Map<String, Object>> friendCards = duofenCardGetMapper
				.findByFriendMemberId(busId, memberId);
		request.setAttribute("friendCards", friendCards);
	}

	*//**
	 * 领取卡包中优惠券
	 * 
	 * @throws Exception
	 *//*
	@Transactional(rollbackFor = Exception.class)
	@Override
	public Map<String, Object> oneKeyToGet(Integer receiveId, Integer memberId,
			Integer getType) throws Exception {
		try {
			Map<String, Object> returnMap = new HashMap<String, Object>();
			DuofenCardReceive dfcr = duofenCardReceiveMapper
					.selectByPrimaryKey(receiveId);

			Map<String, Object> dfcrl = duofenCardReceiveLogMapper
					.countByCrIdAndMemberId(dfcr.getId(), memberId);

			if (1 == dfcr.getNumlimit()) {
				if (1 == dfcr.getMaxnumtype()) {
					if (CommonUtil.isNotEmpty(dfcrl) && dfcrl.size() > 0) {
						if (CommonUtil.isNotEmpty(dfcrl.get("crId"))
								&& CommonUtil.toString(dfcr.getId()).equals(
										dfcrl.get("crId").toString())) {
							Integer maxNum = CommonUtil.toInteger(dfcr
									.getMaxnum());
							Integer count = CommonUtil.toInteger(dfcrl
									.get("cId"));
							if (maxNum <= count) {
								returnMap.put("result", false);
								returnMap.put("message", "该卡券包您已领取完"); // 领取状态
																		// 1已领取完
																		// 0还可以领取
								return returnMap;
							}
						}
					}
				} else {
					Integer id = dfcr.getId();
					Date beginDate = DateTimeKit.parse(DateTimeKit.getDate()
							+ " 00:00:00", "yyyy-MM-dd HH:mm:ss");
					Map<String, Object> logList = duofenCardReceiveLogMapper
							.countByCrIdAndDate(id, memberId, beginDate);
					if (CommonUtil.isNotEmpty(logList)) {
						Integer maxNum = dfcr.getMaxnum();
						Integer count = CommonUtil
								.toInteger(logList.get("cId"));
						if (maxNum <= count) {
							returnMap.put("result", false);
							returnMap.put("message", "该卡券包您今天已领取完"); // 领取状态
																		// 1已领取完
																		// 0还可以领取
							return returnMap;
						}
					}
				}
			}

			Member member = memberMapper.selectByPrimaryKey(memberId);
			if (dfcr.getDeliverytype() == 2) {
				if (!memberPayService.isMemember(memberId)) {
					returnMap.put("result", false);
					returnMap.put("message", "该卡包只允许会员领取");
					return returnMap;
				}
				Card card = cardMapper.selectByPrimaryKey(member.getMcId());
				if (!dfcr.getGtids().contains(card.getGtId().toString())) {
					returnMap.put("result", false);
					returnMap.put("message", "当前会员等级不允许领取次卡包");
					return returnMap;
				}

			}
			if (dfcr.getDeliverytype1() == 1 || dfcr.getDeliverytype() == 3) {
				returnMap.put("result", false);
				returnMap.put("message", "该卡包只能允许商场购买,不能领取");
				return returnMap;
			}

			Member m1 = new Member();
			boolean flag = false; // 用来标示是否修改修改用户数据
			if (dfcr.getJifen() > 0) {
				// 扣除用户积分
				if (member.getIntegral() < dfcr.getJifen()) {
					returnMap.put("result", false);
					returnMap.put("message", "积分不足,不能领取");
					return returnMap;
				}
				if (CommonUtil.isNotEmpty(member.getMcId())) {
					memberPayService.saveCardRecordNew(member.getMcId(),
							(byte) 2, dfcr.getJifen() + "积分", "领取优惠券扣除积分", dfcr.getBusid(),
							null, 0, -dfcr.getJifen());

				}
				flag = true;
				m1.setIntegral(member.getIntegral() - dfcr.getJifen());
			}

			if (dfcr.getFenbi() > 0) {
				// 扣除用户粉币
				if (member.getFansCurrency() < dfcr.getFenbi()) {
					returnMap.put("result", false);
					returnMap.put("message", "粉币不足,不能领取");
					return returnMap;
				}
				if (CommonUtil.isNotEmpty(member.getMcId())) {
					memberPayService.saveCardRecordNew(member.getMcId(),
							(byte) 3, dfcr.getFenbi() + "粉币", "领取优惠券扣除粉币", dfcr.getBusid(),
							null, 0, -dfcr.getFenbi());
				}
				flag = true;
				m1.setFansCurrency(member.getFansCurrency() - dfcr.getFenbi());

				// 归还商户粉币
				memberPayService.returnfansCurrency(member.getBusid(),
						new Double(-dfcr.getFenbi()));
			}
			if (flag) {
				m1.setId(member.getId());
				memberMapper.updateByPrimaryKeySelective(m1);
			}

			String[] cardIds = dfcr.getCardids().split(",");
			List<Integer> cardList = new ArrayList<Integer>();
			for (int i = 0; i < cardIds.length; i++) {
				if (CommonUtil.isNotEmpty(cardIds[i])) {
					cardList.add(CommonUtil.toInteger(cardIds[i]));
				}
			}

			// 获取方式0商场 1会员卡 2普通领取 3第三方商场 4ERP等
			// getType本公众号投放方式 1普通 2会员领取 3商场购买 4二维码下载 5券号短信投放
			if ("0".equals(dfcr.getDeliveryaddr())
					|| 0 == dfcr.getDeliveryaddr()) {
				switch (dfcr.getDeliverytype()) {
				case 1:
					getType = 2;
					break;
				case 2:
					getType = 2;
					break;
				case 3:
					getType = 0;
					break;
				case 4:
					getType = 2;
					break;

				default:
					break;
				}
			}

			if ("1".equals(dfcr.getDeliveryaddr())
					|| 1 == dfcr.getDeliveryaddr()) {
				getType = 3;
			}

			if ("2".equals(dfcr.getDeliveryaddr())
					|| 2 == dfcr.getDeliveryaddr()) {
				getType = 4;
			}

			List<Map<String, Object>> listMap = duofenCardMapper.findByCardIds(
					dfcr.getBusid(), cardList);
			for (Map<String, Object> map : listMap) {
				DuofenCardGet duofenCardGet = new DuofenCardGet();
				duofenCardGet.setPublicid(dfcr.getPublicid());
				duofenCardGet.setMemberid(memberId);
				String code = getCode(12);
				duofenCardGet.setCode(code);
				duofenCardGet.setGettype(Byte.valueOf(getType.toString()));
				duofenCardGet.setCardid(CommonUtil.toInteger(map.get("id")));
				duofenCardGet.setGetdate(new Date());
				duofenCardGet.setCardreceiveid(dfcr.getId());
				duofenCardGet.setBusid(member.getBusid());
				if ("DATE_TYPE_FIX_TIME_RANGE".equals(map.get("type"))) {
					duofenCardGet.setStarttime(DateTimeKit.parseDate(map.get(
							"begin_timestamp").toString()));
					duofenCardGet.setEndtime(DateTimeKit.parseDate(map.get(
							"end_timestamp").toString()));
				} else {
					duofenCardGet.setStarttime(DateTimeKit.addDate(new Date(),
							CommonUtil.toInteger(map.get("fixed_begin_term"))));
					duofenCardGet.setEndtime(DateTimeKit.addDate(new Date(),
							CommonUtil.toInteger(map.get("fixed_term"))));
				}

				duofenCardGetMapper.insertSelective(duofenCardGet);

			}

			DuofenCardReceiveLog duofenCardReceiveLog = new DuofenCardReceiveLog();
			duofenCardReceiveLog.setCrid(dfcr.getId());
			duofenCardReceiveLog.setCreatedate(new Date());
			duofenCardReceiveLog.setMemberid(memberId);
			duofenCardReceiveLogMapper.insertSelective(duofenCardReceiveLog);

			// 短信通知
			if (dfcr.getIscallsms() == 1) {
				WxPublicUsers wxPublicUsers = wxPublicUsersMapper
						.selectByUserId(member.getBusid());
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("busId", member.getBusid());
				params.put("model", 12);
				params.put("mobiles", dfcr.getMobilephone());
				if (CommonUtil.isEmpty(wxPublicUsers)) {
					BusUser user = busUserMapper.selectByPrimaryKey(member
							.getBusid());
					params.put("company", user.getMerchant_name());
				} else {
					params.put("company", wxPublicUsers.getAuthorizerInfo());
				}
				params.put("content", "用户领取一个包,包名：" + dfcr.getCardsname());
				try {
					smsSpendingService.sendSms(params);
				} catch (Exception e) {
					LOG.error("短信发送失败", e);
				}
			}
			returnMap.put("result", true);
			returnMap.put("message", "领取成功");
			return returnMap;
		} catch (Exception e) {
			LOG.error("领取优惠券异常", e);
			throw new Exception();
		}
	}

	*//**
	 * 赠送卡包中优惠券
	 * 
	 * @param receiveId
	 * @return
	 *//*
	@Transactional(rollbackFor=Exception.class)
	public Map<String, Object> sendDuofenCard(Integer busId, Integer receiveId,
			String memberIds) throws Exception {
		Map<String, Object> returnMap=new HashMap<>();
		try {
			DuofenCardReceive dfcr = duofenCardReceiveMapper
					.selectByPrimaryKey(receiveId);
			List<Integer> cardList = new ArrayList<Integer>();
			String[] cardIds = dfcr.getCardids().split(",");
			for (int i = 0; i < cardIds.length; i++) {
				if (CommonUtil.isNotEmpty(cardIds[i])) {
					cardList.add(CommonUtil.toInteger(cardIds[i]));
				}
			}

			String[] memberStr = memberIds.split(",");
			List<Map<String, Object>> listMap = duofenCardMapper.findByCardIds(
					busId, cardList);
			List<DuofenCardGet> list=new ArrayList<>();
			for (Map<String, Object> map : listMap) {
				for (int i = 0; i < memberStr.length; i++) {
					DuofenCardGet duofenCardGet = new DuofenCardGet();
					duofenCardGet.setMemberid(CommonUtil
							.toInteger(memberStr[i]));
					String code = new Date().getTime() + "";
					duofenCardGet.setCode(code);
					duofenCardGet.setState((byte)0);
					duofenCardGet.setGettype((byte) 2);
					duofenCardGet
							.setCardid(CommonUtil.toInteger(map.get("id")));
					duofenCardGet.setGetdate(new Date());
					duofenCardGet.setCardreceiveid(dfcr.getId());
					duofenCardGet.setBusid(busId);
					if ("DATE_TYPE_FIX_TIME_RANGE".equals(map.get("type"))) {
						duofenCardGet.setStarttime(DateTimeKit.parseDate(map
								.get("begin_timestamp").toString()));
						duofenCardGet.setEndtime(DateTimeKit.parseDate(map.get(
								"end_timestamp").toString()));
					} else {
						duofenCardGet.setStarttime(DateTimeKit.addDate(
								new Date(), CommonUtil.toInteger(map
										.get("fixed_begin_term"))));
						duofenCardGet.setEndtime(DateTimeKit.addDate(
								new Date(),
								CommonUtil.toInteger(map.get("fixed_term"))));
					}
					list.add(duofenCardGet);
				}
			}
			if(list.size()>0){
				duofenCardGetMapper.insertList(list);
			}

			// 短信通知
			if (dfcr.getIscallsms() == 1) {
				WxPublicUsers wxPublicUsers = wxPublicUsersMapper
						.selectByUserId(busId);
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("busId", busId);
				params.put("model", 12);
				params.put("mobiles", dfcr.getMobilephone());
				if (CommonUtil.isEmpty(wxPublicUsers)) {
					BusUser user = busUserMapper.selectByPrimaryKey(busId);
					params.put("company", user.getMerchant_name());
				} else {
					params.put("company", wxPublicUsers.getAuthorizerInfo());
				}
				params.put("content", "你刚刚赠送了卡包：" + dfcr.getCardsname());
				try {
					smsSpendingService.sendSms(params);
				} catch (Exception e) {
					LOG.error("短信发送失败", e);
				}
			}
			returnMap.put("result", true);
			returnMap.put("message", "发放成功");
			return returnMap;
		} catch (Exception e) {
			LOG.error("发放优惠券异常", e);
			throw new Exception();
		}
	}

	@Override
	public Map<String, Object> findCardReceive(BusUser busUser, String code) {
		Integer busId = 0;
		if (busUser.getPid() == 0) {
			busId = busUser.getId();
		} else {
			busId = dictService.pidUserId(busUser.getId());
		}

		if (code.contains("code")) {
			code = code.split("code=")[1];
		}

		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> cardGet = duofenCardGetMapper.findByCode(busId,
				code);
		if (CommonUtil.isEmpty(cardGet)) {
			map.put("result", false);
			map.put("message", "没有此卡券");
			return map;
		}

		if ("1".equals(cardGet.get("state").toString())) {
			map.put("result", false);
			map.put("message", "卡券已用");
			return map;
		}
		if ("2".equals(cardGet.get("state").toString())) {
			map.put("result", false);
			map.put("message", "卡券已过期");
			return map;
		}
		String startTime = DateTimeKit.format(DateTimeKit.parseDate(CommonUtil
				.toString(cardGet.get("startTime"))), "yyyyMMddHHmmss");
		String endTime = DateTimeKit.format(DateTimeKit.parseDate(CommonUtil
				.toString(cardGet.get("endTime"))), "yyyyMMddHHmmss");

		if (DateTimeKit.laterThanNow(startTime)
				|| !DateTimeKit.laterThanNow(endTime)) {
			map.put("result", false);
			map.put("message", "卡券时间过期或未到使用时间");
			return map;
		}
		String day = DateTimeKit.getDayToEnglish();
		if (!CommonUtil.toString(cardGet.get("time_limit")).contains(day)) {
			map.put("result", false);
			map.put("message", "该卡券不能" + DateTimeKit.getDay() + "使用");
			return map;
		}

		Integer cardId = CommonUtil.toInteger(cardGet.get("cardId"));
		DuofenCard duofenCard = duofenCardMapper.selectByPrimaryKey(cardId);
		Integer memberId = CommonUtil.toInteger(cardGet.get("memberId"));
		Member member = memberMapper.selectByPrimaryKey(memberId);
		if (memberPayService.isMemember(memberId)) {
			List<Map<String, Object>> cards = cardMapper.findCardById(member
					.getMcId());
			if (cards.size() > 0) {
				map.put("cards", JSONObject.fromObject(cards.get(0)));
			}
		}
		String location_id_list = duofenCard.getLocationIdList();
		if (CommonUtil.isNotEmpty(location_id_list)) {
			List<Map<String, Object>> stores = dictService.shopList(busUser);
			if (CommonUtil.isEmpty(stores) || stores.size() == 0) {
				map.put("result", false);
				map.put("message", "当前用户未关联门店,不能操作卡券核销");
				return map;
			}
			if (!location_id_list.contains(CommonUtil.toString(stores.get(0)
					.get("id")))) {
				map.put("result", false);
				map.put("message", "该卡券不能在此店使用");
				return map;
			}
		}
		
		
		map.put("duofenCard", JSONObject.fromObject(duofenCard));
		map.put("member", JSONObject.fromObject(member));
		map.put("result", true);
		map.put("code", code);
		return map;
	}
	
	
	@Transactional(rollbackFor = Exception.class)
	@Override
	public Map<String, Object> cardReceive(HttpServletRequest request,
			String code, Double money, BusUser busUser, Integer payType,
			Double jifenmoney, Double fenbimoney) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			Integer busId = 0;
			if (busUser.getPid() == 0) {
				busId = busUser.getId();
			} else {
				busId = dictService.pidUserId(busUser.getId());
			}

			Map<String, Object> cg = duofenCardGetMapper
					.findByCode(busId, code);

			Member member = memberMapper.selectByPrimaryKey(CommonUtil
					.toInteger(cg.get("memberId")));

			DuofenCard duofencard = duofenCardMapper
					.selectByPrimaryKey(CommonUtil.toInteger(cg.get("cardId")));

			UserConsume uc = new UserConsume();

			Card card = null;
			if (CommonUtil.isNotEmpty(member.getMcId())) {
				card = cardMapper.selectByPrimaryKey(member.getMcId());
				uc.setMcid(card.getMcId());
				uc.setCtid(card.getCtId());
				uc.setGtId(card.getGtId());
			}
			uc.setTotalmoney(money);
			uc.setPaymenttype((byte) 10);

			switch (duofencard.getCardType()) {
			case 1:
				if (CommonUtil.isNotEmpty(card)) {
					// if (card.getCtId() == 2) {
					// GiveRule give =
					// giveRuleMapper.selectByPrimaryKey(card.getGrId());
					// money = money * give.getGrDiscount() / 100;
					// uc.setDiscount(give.getGrDiscount());
					// }
				}
				// 代金券
				if (money >= duofencard.getCashLeastCost()) {
					uc.setDiscountmoney(money - duofencard.getReduceCost());
				} else {
					map.put("result", false);
					map.put("message", "消费金额不足,卡券不能核销!");
					return map;
				}
				break;
			case 0:
				uc.setDiscount(new Double(duofencard.getDiscount() * 10)
						.intValue());
				uc.setDiscountmoney(money * duofencard.getDiscount() / 10);
				break;
			case 2:
				uc.setDiscount(100);
				uc.setDiscountmoney(money);
				uc.setGivegift(duofencard.getGift());
				uc.setGiftcount(1);
				uc.setDiscount(100);
				uc.setDiscountmoney(money);
				break;
			case 3:
				uc.setDiscount(100);
				uc.setDiscountmoney(money);
				uc.setDiscountdepict(duofencard.getDefaultDetail());
				uc.setDiscount(100);
				uc.setDiscountmoney(money);
				break;
			case 4:
				uc.setDiscount(100);
				uc.setDiscountmoney(money);
				uc.setGivegift(duofencard.getGift());
				uc.setGiftcount(1);
				uc.setDiscount(100);
				uc.setDiscountmoney(money);
			default:
				break;
			}

			if (CommonUtil.isNotEmpty(card) && payType == 1 && money > 0
					&& card.getCtId() == 3) {
				double surplusMoney = card.getMoney() - uc.getDiscountmoney();
				if (surplusMoney < 0) {
					map.put("result", false);
					map.put("message", "储值卡余额不足,请充值或现金支付");
					return map;
				}
				Card c = new Card();
				c.setMoney(surplusMoney);
				c.setMcId(card.getMcId());
				cardMapper.updateByPrimaryKeySelective(c);
				uc.setPaymenttype((byte) 5);
			}

			int jifen = 0;
			if (jifenmoney > 0) {
				jifen = memberPayService.deductJifen(jifenmoney, busId);
			}

			int fenbi1 = 0;
			if (fenbimoney > 0) {
				fenbi1 = memberPayService.deductFenbi(fenbimoney, busId)
						.intValue();
			}

			List<Map<String, Object>> stores = dictService.shopList(busUser);

			uc.setBususerid(busId);
			uc.setMemberid(member.getId());
			uc.setRecordtype((byte) 2);
			uc.setCreatedate(new Date());
			uc.setPaystatus((byte) 0);
			uc.setUctype((byte) 14);
			uc.setIntegral(jifen);
			uc.setFenbi(fenbi1);
			uc.setUccount(0);
			uc.setCreatedate(new Date());
			uc.setStoreid(CommonUtil.toInteger(stores.get(0).get("id")));
			uc.setDvid(duofencard.getId());
			uc.setDiscountdepict(code);
			uc.setCardtype((byte) 1);
			String orderCode = CommonUtil.getMEOrderCode();
			uc.setOrdercode(orderCode);
			uc.setPaystatus((byte) 1);
			userConsumeMapper.insertSelective(uc);

			if (CommonUtil.isNotEmpty(member.getMcId())) {
				if (jifen > 0 || fenbi1 > 0) {
					Member m = new Member();
					m.setId(member.getId());
					m.setFansCurrency(member.getFansCurrency() - fenbi1);
					m.setIntegral(member.getIntegral() - jifen);
					memberMapper.updateByPrimaryKeySelective(m);
					if (jifen > 0) {
						// 添加会员卡操作记录
						memberPayService.saveCardRecordNew(card.getMcId(),
								(byte) 2, jifen + "积分", "线下支付积分抵扣",
								member.getBusid(), null, card.getCtId(),
								-jifen);
					}
					if (fenbi1 > 0) {
						BusUser bususer = busUserMapper
								.selectByPrimaryKey(busId);
						// 归还到商家账户
						bususer.setFansCurrency(bususer.getFansCurrency()
								+ fenbi1);
						CommonUtil.setLoginUser(request, bususer);
						BusUser b = new BusUser();
						b.setId(bususer.getId());
						b.setFansCurrency(bususer.getFansCurrency());
						busUserMapper.updateByPrimaryKeySelective(b);

						// 添加会员卡操作记录
						memberPayService.saveCardRecordNew(card.getMcId(),
								(byte) 3, fenbi1 + "粉币", "线下支付粉币抵扣",
								member.getBusid(), null, card.getCtId(),
								-fenbi1);
					}
				}

				// 会员赠送
				memberPayService.findGiveRule(member.getPhone(), orderCode,
						"线下消费", (byte) 1);
			}
			DuofenCardGet dfcg = new DuofenCardGet();
			dfcg.setId(CommonUtil.toInteger(cg.get("gId")));
			dfcg.setState((byte) 1);
			dfcg.setStoreid(CommonUtil.toInteger(stores.get(0).get("id")));
			duofenCardGetMapper.updateByPrimaryKeySelective(dfcg);
			
			//推荐优惠券赠送
			if(CommonUtil.toInteger(cg.get("recommendId"))>0){
				Recommend recommend=recommendMapper.selectByPrimaryKey(CommonUtil.toInteger(cg.get("recommendId")));
				tuijianGive(recommend);
			}
			
			
			map.put("result", true);
			map.put("message", "核销成功");
			map.put("gId", cg.get("gId"));
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
	public Map<String, Object> findWxcardReceive(BusUser busUser,
			Map<String, Object> params) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		try {
			List<Integer> poids = new ArrayList<Integer>();
			List<Map<String, Object>> poiIds = null;
			if (busUser.getPid() == 0) {
				poiIds = wxShopMapper
						.findWxShopbyPublicId1Ver2(busUser.getId());
				if (CommonUtil.isNotEmpty(poiIds) && poiIds.size() > 0) {
					for (Map<String, Object> map : poiIds) {
						poids.add(CommonUtil.toInteger(map.get("id")));
					}
				}
			} else {
				poiIds = busUserBranchRelationMapper.findBusUserShop(busUser
						.getId());
				if (CommonUtil.isEmpty(poiIds) || poiIds.size() == 0) {
					return null;
				}
				for (Map<String, Object> map : poiIds) {
					poids.add(CommonUtil.toInteger(map.get("branchid")));
				}
			}


			int busId = busUser.getId();
			if (busUser.getPid() != 0) {
				busId = dictService.pidUserId(busUser.getPid());
			}

			if(poids.size()>0){
				List<Map<String, Object>> countList = duofenCardGetMapper
						.countCode(busId, poids);
				returnMap.put("countList", countList);
			}

			params.put("curPage", CommonUtil.isEmpty(params.get("curPage")) ? 1
					: CommonUtil.toInteger(params.get("curPage")));
			int pageSize = 10;
			int rowCount = userConsumeMapper.countDuofencardReceive(busId,
					poids, 1);
			Page page = new Page(CommonUtil.toInteger(params.get("curPage")),
					pageSize, rowCount, "/duofenCard/redirectVerification.do");
			params.put("firstResult", pageSize
					* ((page.getCurPage() <= 0 ? 1 : page.getCurPage()) - 1));
			params.put("maxResult", pageSize);
			List<Map<String, Object>> list = userConsumeMapper
					.findDuofencardReceive(busId, Integer.parseInt(params.get(
							"firstResult").toString()), pageSize, poids, 1);
			page.setSubList(list);
			returnMap.put("page", page);
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnMap;
	}

	@Override
	public Map<String, Object> findExamine(Integer id) {
		String sql = "select remark from t_duofen_card_examine where id=" + id;
		List<Map<String, Object>> listMap = daoUtil.queryForList(sql);
		if (listMap.size() > 0) {
			return listMap.get(0);
		}
		return null;
	}

	@Override
	public Page findThreeCard(WxPublicUsers wxPublicUsers,
			Map<String, Object> params) {
		try {
			params.put("curPage", CommonUtil.isEmpty(params.get("curPage")) ? 1
					: CommonUtil.toInteger(params.get("curPage")));
			int pageSize = 10;

			int rowCount = duofenCardReceiveMapper
					.countThreeCardReceive(wxPublicUsers.getId());
			Page page = new Page(CommonUtil.toInteger(params.get("curPage")),
					pageSize, rowCount, "duofenCard/threeCard.do");
			params.put("firstResult", pageSize
					* ((page.getCurPage() <= 0 ? 1 : page.getCurPage()) - 1));
			params.put("maxResult", pageSize);
			List<Map<String, Object>> list = duofenCardReceiveMapper
					.findThreeCardReceive(wxPublicUsers.getId(), Integer
							.parseInt(params.get("firstResult").toString()),
							pageSize);
			page.setSubList(list);
			return page;
		} catch (Exception e) {
			LOG.error("分页查询异常", e);
			e.printStackTrace();
		}
		return null;
	}

	*//** 卡券短信投放 *//*
	@Override
	public void duanxiTF(Integer receiveId, String ctIds, String gtIds,
			WxPublicUsers wxPublicUsers, BusUser busUser) {
		List<Integer> ctIdlist = new ArrayList<Integer>();
		List<Integer> gtIdList = new ArrayList<Integer>();
		String[] ctIdstr = ctIds.split(",");
		for (String str : ctIdstr) {
			if (CommonUtil.isNotEmpty(str)) {
				ctIdlist.add(CommonUtil.toInteger(str));
			}
		}

		String[] gtIdstr = gtIds.split(",");
		for (String str : gtIdstr) {
			if (CommonUtil.isNotEmpty(str)) {
				gtIdList.add(CommonUtil.toInteger(str));
			}
		}

		List<Map<String, Object>> cards = cardMapper.findCardByCtIdsAndGtIds(
				wxPublicUsers.getId(), ctIdlist, gtIdList);
		List<Integer> mcIds = new ArrayList<Integer>();
		for (Map<String, Object> map : cards) {
			if (CommonUtil.isNotEmpty(map.get("mc_id"))) {
				mcIds.add(CommonUtil.toInteger(map.get("mc_id")));
			}
		}

		List<Map<String, Object>> members = memberMapper.findBymcIds(
				wxPublicUsers.getId(), mcIds);

		DuofenCardReceive dfcr = duofenCardReceiveMapper
				.selectByPrimaryKey(receiveId);
		String[] cardIds = dfcr.getCardids().split(",");

		// 添加
		for (Map<String, Object> map : members) {
			for (String str : cardIds) {

			}
			// 短信通知
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("busId", wxPublicUsers.getBusUserId());
			params.put("model", 12);

			params.put("company", wxPublicUsers.getAuthorizerInfo());
			params.put("content", "商家免费你领取");
			try {
				smsSpendingService.sendSms(params);
			} catch (Exception e) {
				LOG.error("短信发送失败", e);
			}
		}
	}

	*//** 查询用户拥有的优惠券 *//*
	@Override
	public List<Map<String, Object>> findDuofenCardByMemberId(Integer memberId,
			Integer wxshopId) {
		List<Map<String, Object>> duofencardgets = duofenCardGetMapper
				.findCardByMemberId(memberId);
		if (CommonUtil.isEmpty(duofencardgets) || duofencardgets.size() == 0) {
			return null;
		}

		List<Map<String, Object>> duofencards = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> map : duofencardgets) {
			if ("2".equals(map.get("card_type").toString())
					|| "3".equals(map.get("card_type").toString())
					|| "4".equals(map.get("card_type").toString())) {
				continue;
			}

			String day = DateTimeKit.getDayToEnglish();
			if (!map.get("time_limit").toString().contains(day)) {
				continue;
			}

			if (CommonUtil.isNotEmpty(map.get("location_id_list"))) {
				String location_id_list = CommonUtil.toString(map
						.get("location_id_list"));
				if (location_id_list.contains(wxshopId.toString())) {
					duofencards.add(map);
				}
			} else {
				duofencards.add(map);
			}
		}
		return duofencards;
	}
	
	
	*//** 查询用户拥有的优惠券 *//*
	@Override
	public List<Map<String, Object>> findDuofenCardByMemberIdAndMoney(Integer memberId,
			Integer wxshopId,Double money) {
		List<Map<String, Object>> duofencardgets = duofenCardGetMapper
				.findCardByMemberId(memberId);
		if (CommonUtil.isEmpty(duofencardgets) || duofencardgets.size() == 0) {
			return null;
		}

		List<Map<String, Object>> duofencards = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> map : duofencardgets) {
			if ("2".equals(map.get("card_type").toString())
					|| "3".equals(map.get("card_type").toString())
					|| "4".equals(map.get("card_type").toString())) {
				continue;
			}

			String day = DateTimeKit.getDayToEnglish();
			if (!map.get("time_limit").toString().contains(day)) {
				continue;
			}
			
			
			if ("1".equals(map.get("card_type").toString())){
				Double cash_least_cost=CommonUtil.toDouble(map.get("cash_least_cost"));
				Integer countId=CommonUtil.toInteger(map.get("countId"));
				if(cash_least_cost>0 && cash_least_cost>money){
					continue;
				}
				
				Integer addUser=CommonUtil.toInteger(map.get("addUser"));
				
				if(addUser==1 || "1".equals(addUser)){
					if(cash_least_cost>0){
						Double count=money/cash_least_cost;
						int num=count.intValue();
						if(countId>num){
							map.put("countId", num);
						}
					}
				}else{
					map.put("countId", 1);
				}
				
			}
			

			if (CommonUtil.isNotEmpty(map.get("location_id_list"))) {
				String location_id_list = CommonUtil.toString(map
						.get("location_id_list"));
				if (location_id_list.contains(wxshopId.toString())) {
					duofencards.add(map);
				}
			} else {
				duofencards.add(map);
			}
		}
		return duofencards;
	}

	*//**
	 * 查询手机核销授权人员
	 * 
	 * @param wxPublicUsers
	 * @return
	 *//*
	public Page findDuofenCardAuthorization(Integer busId,
			Map<String, Object> params) {
		try {
			params.put("curPage", CommonUtil.isEmpty(params.get("curPage")) ? 1
					: CommonUtil.toInteger(params.get("curPage")));
			int pageSize = 10;

			int rowCount = duofenCardAuthorizationMapper
					.countAuthorization(busId);
			Page page = new Page(CommonUtil.toInteger(params.get("curPage")),
					pageSize, rowCount, "duofenCard/authorizationPhone.do");
			params.put("firstResult", pageSize
					* ((page.getCurPage() <= 0 ? 1 : page.getCurPage()) - 1));
			params.put("maxResult", pageSize);
			List<Map<String, Object>> list = duofenCardAuthorizationMapper
					.findAuthorization(busId, Integer.parseInt(params.get(
							"firstResult").toString()), pageSize);
			if (CommonUtil.isEmpty(list) || list.size() == 0) {
				return null;
			}
			List<Integer> memberIds = new ArrayList<Integer>();
			for (Map<String, Object> map : list) {
				if (CommonUtil.isNotEmpty(map.get("memberId"))) {
					memberIds.add(CommonUtil.toInteger(map.get("memberId")));
				}
			}
			List<Map<String, Object>> returnList = new ArrayList<>();
			List<Map<String, Object>> memberList = memberMapper
					.findByMemberIds(memberIds);
			for (Map<String, Object> map : list) {
				for (Map<String, Object> member : memberList) {
					if (CommonUtil.isNotEmpty(map.get("memberId"))
							&& CommonUtil.toInteger(map.get("memberId"))
									.equals(CommonUtil.toInteger(member
											.get("id")))) {
						try {
							byte[] bytes = (byte[]) member.get("nickname");
							member.put("nickname", new String(bytes, "UTF-8"));
						} catch (Exception e) {
							member.put("nickname", null);
						}
						member.put("business_name", map.get("business_name"));
						member.put("status", map.get("status"));
						member.put("aId", map.get("id"));

						returnList.add(member);
					}
				}
			}
			page.setSubList(returnList);
			return page;
		} catch (Exception e) {
			LOG.error("分页查询异常", e);
			e.printStackTrace();
		}
		return null;
	}

	public void duofencardHeiXiao(HttpServletRequest request, Member member,
			String code) {
		try {
			DuofenCardAuthorization dfca = duofenCardAuthorizationMapper
					.findByOpenId(member.getBusid(), member.getOpenid());
			if (CommonUtil.isEmpty(dfca)) {
				request.setAttribute("tiaozhuan", 2);
				request.setAttribute("message", "非法核销人员");
				return;
			}
			Map<String, Object> cardGet = duofenCardGetMapper.findByCode(
					member.getBusid(), code);
			if (CommonUtil.isEmpty(cardGet)) {
				request.setAttribute("tiaozhuan", 2);
				request.setAttribute("message", "没有此卡券");
				return;
			}
			if ("1".equals(cardGet.get("state").toString())) {
				request.setAttribute("tiaozhuan", 2);
				request.setAttribute("message", "卡券已用");
				return;
			}
			if ("2".equals(cardGet.get("state").toString())) {
				request.setAttribute("tiaozhuan", 2);
				request.setAttribute("message", "卡券已过期");
				return;
			}
			String startTime = DateTimeKit.format(DateTimeKit
					.parseDate(CommonUtil.toString(cardGet.get("startTime"))),
					"yyyyMMddHHmmss");
			String endTime = DateTimeKit.format(DateTimeKit
					.parseDate(CommonUtil.toString(cardGet.get("endTime"))),
					"yyyyMMddHHmmss");
			if (DateTimeKit.laterThanNow(startTime)
					|| !DateTimeKit.laterThanNow(endTime)) {
				request.setAttribute("tiaozhuan", 2);
				request.setAttribute("message", "卡券时间过期或未到使用时间");
				return;
			}
			String day = DateTimeKit.getDayToEnglish();
			if (!CommonUtil.toString(cardGet.get("time_limit")).contains(day)) {
				request.setAttribute("tiaozhuan", 2);
				request.setAttribute("message", "该卡券不能" + DateTimeKit.getDay()
						+ "使用");
				return;
			}
			Object location_id_list = cardGet.get("location_id_list");
			if (CommonUtil.isNotEmpty(location_id_list)) {
				if (!CommonUtil.toString(location_id_list).contains(
						dfca.getShopid().toString())) {
					request.setAttribute("tiaozhuan", 2);
					request.setAttribute("message", "当前用户未关联卡券中的门店,不能操作卡券核销");
					return;
				}
			}
			DuofenCardGet df = new DuofenCardGet();
			df.setId(CommonUtil.toInteger(cardGet.get("gId")));
			df.setState((byte) 1);
			df.setStoreid(dfca.getShopid());
			duofenCardGetMapper.updateByPrimaryKeySelective(df);

			UserConsume uc = new UserConsume();
			uc.setBususerid(member.getBusid());
			uc.setMemberid(member.getId());
			uc.setDiscountdepict(code);
			uc.setCardtype((byte) 1);
			uc.setStoreid(dfca.getShopid());
			uc.setDvid(CommonUtil.toInteger(cardGet.get("cId")));
			userConsumeMapper.insertSelective(uc);
			
			
			//推荐优惠券赠送
			if(CommonUtil.toInteger(cardGet.get("recommendId"))>0){
				Recommend recommend=recommendMapper.selectByPrimaryKey(CommonUtil.toInteger(cardGet.get("recommendId")));
				tuijianGive(recommend);
			}

			socke.sendMessage2("duofencard" + cardGet.get("gId"), "卡券已核销");
			request.setAttribute("tiaozhuan", 3);
			request.setAttribute("message", "核销成功");
		} catch (Exception e) {
			LOG.error("核销失败", e);
			request.setAttribute("tiaozhuan", 2);
			request.setAttribute("message", "核销失败");
		}
	}
	

	*//**
	 * 查询门店信息
	 * 
	 * @param request
	 * @param cardId
	 * @param lat
	 * @param lng
	 *//*
	public Map<String, Object> findDuofenCardStore(Integer cardId, Double lat,
			Double lng) {
		Map<String, Object> returnMap = new HashMap<String, Object>();

		DuofenCard df = duofenCardMapper.selectByPrimaryKey(cardId);
		List<Map<String, Object>> list = null;
		if (CommonUtil.isEmpty(df.getLocationIdList())) {
			String sql = "select id,business_name,address,longitude,latitude from t_wx_shop where bus_id="
					+ df.getUserId();
			list = daoUtil.queryForList(sql);
		} else {
			String[] str = df.getLocationIdList().split(",");
			StringBuffer sb = new StringBuffer(
					"select id,business_name,address,longitude,latitude from t_wx_shop where id in (");
			for (int i = 0; i < str.length; i++) {
				if (CommonUtil.isNotEmpty(str[i])) {
					sb.append(str[i] + ",");
				}
			}
			sb.append("-1)");
			list = daoUtil.queryForList(sb.toString());
		}

		list = storeListService.findDuofenCardStore(list);

		List<Map<String, Object>> shopList = new ArrayList<Map<String, Object>>();
		if (CommonUtil.isNotEmpty(lat) && CommonUtil.isNotEmpty(lng)) {
			for (Map<String, Object> map : list) {
				Double longitude = CommonUtil.toDouble(map.get("longitude"));
				Double latitude = CommonUtil.toDouble(map.get("latitude"));
				Double range = CommonUtil.getDistance(longitude, latitude, lng,
						lat);
				map.put("range", range.intValue());
				shopList.add(map);

			}

			Collections.sort(shopList, new Comparator<Map<String, Object>>() {
				@Override
				public int compare(Map<String, Object> o1,
						Map<String, Object> o2) {
					int map1value = (Integer) o1.get("range");
					int map2value = (Integer) o2.get("range");
					return map1value - map2value;
				}
			});
			returnMap.put("shopList", shopList);
			return returnMap;
		}
		returnMap.put("shopList", list);
		return returnMap;
	}
	
	
	
	*//**
	 * 推荐优惠券
	 * @param request
	 * @param member
	 * @param cardId
	 *//*
	public void tuijianfriend(HttpServletRequest request,Member member,Integer cardId){
		DuofenCardGet duofencardget=duofenCardGetMapper.selectByPrimaryKey(cardId);
		Integer count=recommendMapper.countRecommendByCardId(cardId, member.getId());
		DuofenCard dc=duofenCardMapper.selectByPrimaryKey(duofencardget.getCardid());
		
		if(count<=0){
			DuofenCardReceive dcr=duofenCardReceiveMapper.selectByPrimaryKey(duofencardget.getCardreceiveid());
			//新增操作
			Recommend r=new Recommend();
			r.setMemberid(member.getId());
			r.setIscheck((byte)1);
			r.setIntegral(dcr.getGivejifen());
			r.setFenbi(dcr.getGivefenbi());
			r.setMoney(dcr.getGivemoney());
			r.setFlow(dcr.getGiveflow());
			r.setRetype((byte)1);
			r.setCardid(cardId);
			r.setRecommendtype((byte)1);
			r.setDatetime(new Date());
			r.setCardname(dc.getTitle());
			recommendMapper.insertSelective(r);
		}
		
		
		request.setAttribute("dc", dc);
		request.setAttribute("imgPath", PropertiesUtil.getResourceUrl());
		request.setAttribute("identifier", PropertiesUtil.getIdentifier());
		request.setAttribute("cardId", cardId);
		request.setAttribute("member", member);
		request.setAttribute("path", PropertiesUtil.getWebHomeUrl());
		
	}
	
	
	
	
	*//**
	 * 推荐领取优惠券
	 * @throws Exception 
	 *//*
	@Transactional(rollbackFor=Exception.class)
	public void tuijianget(HttpServletRequest request,Member member,Integer memberId,Integer cardId) throws Exception{
		try {
			DuofenCardGet duofencardget = duofenCardGetMapper
					.selectByPrimaryKey(cardId);
			DuofenCard df = duofenCardMapper.selectByPrimaryKey(duofencardget
					.getCardid());
			Recommend recommend = recommendMapper.findRecommendByCardId(cardId,
					memberId);
			duofencardget.setId(null);
			duofencardget.setGettype((byte) 6);
			duofencardget.setCode(getCode(12));
			duofencardget.setGetdate(new Date());
			duofencardget.setMemberid(member.getId());
			if ("DATE_TYPE_FIX_TIME_RANGE".equals(df.getType())) {
				duofencardget.setStarttime(df.getBeginTimestamp());
				duofencardget.setEndtime(df.getEndTimestamp());
			} else {
				duofencardget.setStarttime(DateTimeKit.addDate(new Date(),
						df.getFixedBeginTerm()));
				duofencardget.setEndtime(DateTimeKit.addDate(new Date(),
						df.getFixedTerm()));
			}
			duofencardget.setFriendmemberid("");
			duofencardget.setStoreid(0);
			duofencardget.setState((byte) 0);
			duofencardget.setRecommendid(recommend.getId());
			duofenCardGetMapper.insertSelective(duofencardget);
			//新增领取人数
			Recommend r = new Recommend();
			r.setId(recommend.getId());
			r.setLingqunum(recommend.getLingqunum() + 1);
			recommendMapper.updateByPrimaryKeySelective(r);
		} catch (Exception e) {
			LOG.error("推荐领取优惠券异常", e);
			throw new Exception();
		}
	}

	@Override
	public void duofencardChart(HttpServletRequest request,
			Map<String, Object> param, Integer busId) {
		try {
			// 查询门店信息
//			List<Map<String, Object>> wxshops = wxShopMapper
//					.findWxShopbyPublicId1Ver2(busId);
//			request.setAttribute("wxshops", wxshops);
//
//			List<DuofenCard> dfcs = duofenCardMapper.findByBusId(busId);
//			request.setAttribute("dfcs", dfcs);
//			if (CommonUtil.isEmpty(dfcs) && dfcs.size() <= 0) {
//				return;
//			}
//			// 时间
//			String startDate = null;
//			if (CommonUtil.isNotEmpty(param) && param.size() > 0) {
//				startDate = CommonUtil.toString(param.get("dateTime"));
//			}
//			Date startTime = null;
//			Date endTime = null;
//			if (CommonUtil.isEmpty(startDate)) {
//				// 当前年月日
//				startDate = DateTimeKit.getMonFirstDay();
//				startTime = DateTimeKit.parse(startDate,
//						DateTimeKit.DEFAULT_DATETIME_FORMAT);
//				endTime = DateTimeKit.parse(
//						DateTimeKit.format(DateTimeKit
//								.getMonthLastDay(startTime)) + " 23:59:59",
//						DateTimeKit.DEFAULT_DATETIME_FORMAT);
//			} else {
//				request.setAttribute("startDate", startDate);
//				startTime = DateTimeKit.parse(startDate + "-1 00:00:00",
//						DateTimeKit.DEFAULT_DATETIME_FORMAT);
//				endTime = DateTimeKit.parse(
//						DateTimeKit.format(DateTimeKit
//								.getMonthLastDay(startTime)) + " 23:59:59",
//						DateTimeKit.DEFAULT_DATETIME_FORMAT);
//			}
//
//			// 统计不同渠道领取数量
//			Integer cardId = null;
//			if (CommonUtil.isNotEmpty(param)
//					&& CommonUtil.isNotEmpty(param.get("cardId"))) {
//				cardId = CommonUtil.toInteger(param.get("cardId"));
//				request.setAttribute("cardId", cardId);
//			}
//
//			// 门店
//			Integer storeId = 0;
//			if (CommonUtil.isNotEmpty(param)
//					&& CommonUtil.isNotEmpty(param.get("storeId"))) {
//				storeId = CommonUtil.toInteger(param.get("storeId"));
//				request.setAttribute("storeId", storeId);
//			}
//
//			Byte getType = null;
//			if (CommonUtil.isNotEmpty(param)
//					&& CommonUtil.isNotEmpty(param.get("getType"))) {
//				getType = Byte.parseByte(CommonUtil.toString(param
//						.get("getType")));
//			}
//			List<Map<String, Object>> lqCountMap = duofenCardGetMapper
//					.countGroupbyGetType(busId, cardId, getType, startTime,
//							endTime);
//			request.setAttribute("lqCountMap", lqCountMap);
//			// 统计领取总数量
//			int sumLq = duofenCardGetMapper.countDuofenCard(busId, cardId,
//					getType, startTime, endTime);
//			request.setAttribute("sumLq", sumLq);
//
//			// 使用数量统计
//			List<Map<String, Object>> userCountMap = duofenCardGetMapper
//					.countByUserGroupbyGetType(busId, cardId, getType,
//							startTime, endTime, storeId);
//			request.setAttribute("userCountMap", userCountMap);
//
//			int sumUser = duofenCardGetMapper.countDuofenCardByUser(busId,
//					cardId, getType, startTime, endTime, storeId);
//			request.setAttribute("sumUser", sumUser);
//
//			// 近7天数据统计查询
//			List<Map<String, Object>> select7Day = duofenCardGetMapper
//					.select7Day(busId);
//			Collections.reverse(select7Day);
//
//			List<Map<String, Object>> selectUser7Day = duofenCardGetMapper
//					.selectUser7Day(busId, storeId);
//
//			List<Map<String, Object>> card7day = new ArrayList<Map<String, Object>>();
//			for (Map<String, Object> map1 : select7Day) {
//				boolean flag = false;
//				for (Map<String, Object> map : selectUser7Day) {
//					if (CommonUtil.toString(map.get("a3")).equals(
//							CommonUtil.toString(map1.get("a1")))) {
//						map1.put("a4", CommonUtil.toString(map.get("a4")));
//						card7day.add(map1);
//						flag = true;
//					}
//				}
//				if (!flag) {
//					map1.put("a4", 0);
//					card7day.add(map1);
//				}
//			}
//			request.setAttribute("card7day", card7day);
//			// 近7个月数据统计
//			List<Map<String, Object>> select7Month = duofenCardGetMapper
//					.select7Month(busId);
//			Collections.reverse(select7Month);
//
//			List<Map<String, Object>> selectUser7Month = duofenCardGetMapper
//					.selectUser7Month(busId, storeId);
//
//			List<Map<String, Object>> card7Month = new ArrayList<Map<String, Object>>();
//
//			for (Map<String, Object> map1 : select7Month) {
//				boolean flag1 = false;
//				for (Map<String, Object> map : selectUser7Month) {
//					if (CommonUtil.toString(map.get("a3")).equals(
//							CommonUtil.toString(map1.get("a1")))) {
//						map1.put("a4", CommonUtil.toString(map.get("a4")));
//						card7Month.add(map1);
//						flag1 = true;
//					}
//				}
//				if (!flag1) {
//					map1.put("a4", 0);
//					card7Month.add(map1);
//				}
//			}
//			request.setAttribute("card7Month", JSONArray.fromObject(card7Month));
//			request.setAttribute("lqCountList",
//					JSONArray.fromObject(lqCountMap));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}
}
*/