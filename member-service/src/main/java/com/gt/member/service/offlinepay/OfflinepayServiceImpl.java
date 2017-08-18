package com.gt.member.service.offlinepay; /**
 * P 2016年7月27日
 *//*
package com.gt.controller.offlinepay;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gt.dao.common.WxCardMapper;
import com.gt.dao.common.WxCardReceiveMapper;
import com.gt.dao.member.CardMapper;
import com.gt.dao.member.CardRecordMapper;
import com.gt.dao.member.GiveRuleMapper;
import com.gt.dao.member.MemberMapper;
import com.gt.dao.member.UserConsumeMapper;
import com.gt.dao.member.card.DuofenCardGetMapper;
import com.gt.dao.member.card.DuofenCardMapper;
import com.gt.dao.offlinepay.OfflinepayDetailMapper;
import com.gt.dao.offlinepay.OfflinepayMapper;
import com.gt.dao.set.WxShopMapper;
import com.gt.dao.user.BusUserBranchRelationMapper;
import com.gt.dao.user.BusUserMapper;
import com.gt.dao.user.WxPublicUsersMapper;
import com.gt.entity.common.WxCard;
import com.gt.entity.common.WxCardReceive;
import com.gt.entity.member.Card;
import com.gt.entity.member.Member;
import com.gt.entity.member.UserConsume;
import com.gt.entity.member.card.DuofenCard;
import com.gt.entity.offlinepay.Offlinepay;
import com.gt.entity.offlinepay.OfflinepayDetail;
import com.gt.entity.set.WxShop;
import com.gt.entity.user.BusUser;
import com.gt.entity.user.WxPublicUsers;
import com.gt.controller.common.dict.DictService;
import com.gt.controller.common.sms.SmsSpendingService;
import com.gt.controller.common.wxcard.IWxCardService;
import com.gt.controller.member.DuofenCardService;
import com.gt.controller.memberpay.MemberPayService;
import com.gt.controller.personCenter.PaySuccessService;
import com.gt.util.CommonUtil;
import com.gt.util.DateTimeKit;
import com.gt.util.JedisUtil;
import com.gt.util.PropertiesUtil;
import com.gt.wx.controller.event.WxCardService;
import com.gt.wx.controller.event.WxPayService;

*//**
 * @author pengjiangli
 * @version 创建时间:2016年7月27日
 * 
 *//*
@Service
public class OfflinepayServiceImpl implements IofflinepayService {

	private static final Logger LOG = Logger
			.getLogger(OfflinepayServiceImpl.class);

	@Autowired
	private OfflinepayMapper offlinepayMapper;

	@Autowired
	private BusUserBranchRelationMapper busUserBranchRelationMapper;

	@Autowired
	private WxShopMapper wxShopMapper;

	@Autowired
	private MemberMapper memberMapper;

	@Autowired
	private GiveRuleMapper giveRuleMapper;

	@Autowired
	private CardMapper cardMapper;

	@Autowired
	private MemberPayService memberPayService;

	@Autowired
	private WxCardReceiveMapper wxCardReceiveMapper;

	@Autowired
	private WxCardMapper wxCardMapper;

	@Autowired
	private DictService dictService;

	@Autowired
	private UserConsumeMapper userConsumeMapper;

	@Autowired
	private WxPublicUsersMapper wxPublicUsersMapper;

	@Autowired
	private WxPayService wxPayService;

	@Autowired
	private SmsSpendingService smsSpendingService;

	@Autowired
	private IWxCardService iwxCardService;


	@Autowired
	private CardRecordMapper cardRecordMapper;

	@Autowired
	private WxCardService wxCardService;

	@Autowired
	private OfflinepayDetailMapper offlinepayDetailMapper;

	@Autowired
	private PaySuccessService paySuccessService;

	@Autowired
	private DuofenCardGetMapper duofenCardGetMapper;

	@Autowired
	private DuofenCardMapper duofenCardMapper;
	
	@Autowired
	private BusUserMapper busUserMapper;
	
	@Autowired
	private DuofenCardService duofenCardService;

	@Override
	public List<Map<String, Object>> findOfflinepay(BusUser busUser) {
		try {
			if (busUser.getPid() == 0) {
				// 主用户
				List<Map<String, Object>> listMap = offlinepayMapper
						.findAll(busUser.getId());
				return listMap;
			} else {
				// 子用户
				List<Map<String, Object>> shopIdList = busUserBranchRelationMapper
						.findBusUserShop(busUser.getId());
				List<Integer> shopIds = new ArrayList<Integer>();
				if (CommonUtil.isNotEmpty(shopIdList) && shopIdList.size() > 0) {
					for (Map<String, Object> map : shopIdList) {
						shopIds.add(CommonUtil.toInteger(map.get("branchid")));
					}
					List<Map<String, Object>> listMap = offlinepayMapper
							.findByShopIds(busUser.getPid(), shopIds);
					return listMap;
				}
			}
		} catch (Exception e) {
			LOG.error("查询减免规则异常", e);
		}
		return null;
	}

	@Override
	public List<Map<String, Object>> findShop(BusUser busUser) {
		try {
			if (busUser.getPid() == 0) {
				// 主用户
				List<Map<String, Object>> listMap = offlinepayMapper
						.findShop(busUser.getId());
				return listMap;
			} else {
				// 子用户
				List<Map<String, Object>> shopIdList = busUserBranchRelationMapper
						.findBusUserShop(busUser.getId());
				List<Integer> shopIds = new ArrayList<Integer>();
				if (CommonUtil.isNotEmpty(shopIdList) && shopIdList.size() > 0) {
					for (Map<String, Object> map : shopIdList) {
						shopIds.add(CommonUtil.toInteger(map.get("branchid")));
					}
					List<Map<String, Object>> listMap = offlinepayMapper
							.findShopByBusId(busUser.getPid(), shopIds);
					return listMap;
				}

			}
		} catch (Exception e) {
			LOG.error("查询门店信息异常", e);
		}
		return null;
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public Map<String, Object> saveOfflinepay(BusUser busUser, String param) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			Offlinepay offlinepay = (Offlinepay) JSONObject.toBean(
					JSONObject.fromObject(param), Offlinepay.class);
			offlinepay.setBusid(busUser.getId());
			if (CommonUtil.isNotEmpty(offlinepay.getId())) {
				offlinepayMapper.updateByPrimaryKeySelective(offlinepay);
				offlinepayDetailMapper.deleteByoffId(offlinepay.getId());
			} else {
				offlinepay.setCreatedate(new Date());
				offlinepayMapper.insertSelective(offlinepay);
			}
			// 添加数据
			String detail = offlinepay.getDetail();
			List<OfflinepayDetail> details = JSONArray.toList(
					JSONArray.fromObject(detail), OfflinepayDetail.class);
			if (details.size() > 0) {
				for (OfflinepayDetail offlinepayDetail : details) {
					offlinepayDetail.setOffid(offlinepay.getId());
					offlinepayDetailMapper.insertSelective(offlinepayDetail);
				}
			}
			map.put("result", true);
			map.put("message", "操作成功");
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("操作减免规则异常", e);
			throw new Exception();
		}
		return map;
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public SortedMap<Object, Object> pay(String param, String url)
			throws Exception {
		SortedMap<Object, Object> parameters = new TreeMap<Object, Object>();
		try {
			// 1支付方式 2储值卡支付不需要调用微信 3 会员需赠送物品
			JSONObject obj = JSONObject.fromObject(param);
			Member member = memberMapper.selectByPrimaryKey(CommonUtil
					.toInteger(obj.get("memberId")));
		
			Double total = CommonUtil.toDouble(obj.get("total"));
			Double countMoney = CommonUtil.toDouble(obj.get("countMoney"));
			Double pay = total;
			Double youhui = reduce(total - countMoney);
			Offlinepay offlinepay = offlinepayMapper.findByshopId(
					member.getBusid(),
					CommonUtil.toInteger(obj.get("shopid")));
			String date = CommonUtil.toString(obj.get("date"));
			offlinepay = findByReduction(
					DateTimeKit.parse(date, "yyyy-MM-dd HH:mm:ss"), offlinepay);
			// 添加会员记录
			UserConsume uc = new UserConsume();
			// 商家优惠
			String rules = offlinepay.getRule();
			if (rules.contains("0")) {
				Integer count = new Double(youhui / offlinepay.getEachmoney())
						.intValue();
				Double youhuiMoney = count * offlinepay.getReducemoney();
				pay = reduce(youhui - youhuiMoney);
			}
			List<Map<String, Object>> card =null;
			if (CommonUtil.isNotEmpty(member.getMcId())) {
				card=cardMapper.findCardById(member.getMcId());
				uc.setMcid(member.getMcId());
				uc.setCtid(CommonUtil.toInteger(card.get(0)
						.get("ct_id")));
				uc.setGtId(CommonUtil.toInteger(card.get(0)
						.get("gt_id")));
				uc.setDiscount(100);
			}
			
			// 会员卡优惠
			if (rules.contains("1")) {
				// 会员卡 折扣卡
				if (CommonUtil.toInteger(card.get(0).get("ct_id")) == 2) {
					Integer discount = CommonUtil.toInteger(obj
							.get("discount"));
					uc.setDiscount(discount);
					pay = reduce(pay * discount / 100);
				}

			}

			if (rules.contains("2")
					&& CommonUtil.isNotEmpty(obj.get("cardType"))) {
				String cardType = CommonUtil.toString(obj.get("cardType"));
				// 优惠券优惠
				if ("wx".equals(cardType)
						&& CommonUtil.isNotEmpty(obj.get("code"))) {
					// 微信优惠券处理
					String code = CommonUtil.toString(obj.get("code"));
					WxCardReceive wxCardReceive = wxCardReceiveMapper
							.findByCode(member.getPublicId(), code);
					WxCard wxCard = wxCardMapper.selectByCardId(wxCardReceive
							.getCardId());
					uc.setDvid(wxCard.getId());
					uc.setDiscountdepict(code);
					if ("DISCOUNT".equals(wxCard.getCardType())) {
						// 折扣优惠券
						pay = reduce(pay * wxCard.getDiscount() / 10);
						uc.setDiscount(new Double(wxCard.getDiscount() * 10)
								.intValue());
					} else if ("CASH".equals(wxCard.getCardType())) {
						Double cashleastcost = wxCard.getCashLeastCost();
						if (pay < cashleastcost) {
							parameters.put("result", 0);
							parameters.put("message", "消费金额未达到该优惠券的起用金额");
							return parameters;
						} else {
							Double reducecost = wxCard.getReduceCost();
							pay = reduce(pay - reducecost);
						}
					}
				} else {
					// 多粉优惠券处理
					String type = CommonUtil.toString(obj.get("type")); // 用来区分折扣券
																		// 和减免券
					if ("CASH".equals(type)) {
						// 减免券
						Integer cardId = CommonUtil
								.toInteger(obj.get("cardId"));
						int cardNum = CommonUtil.toInteger(obj.get("cardNum"));
						List<Map<String, Object>> dfcg = duofenCardGetMapper
								.findByCardId(cardId, member.getId(), cardNum);
						String duofenCode = "";
						for (Map<String, Object> map : dfcg) {
							duofenCode += map.get("code") + ",";
						}
						uc.setDvid(cardId);
						uc.setDiscountdepict(duofenCode);
						uc.setCardtype((byte) 1);

						DuofenCard duofencard = duofenCardMapper
								.selectByPrimaryKey(cardId);

						Double cashleastcost = duofencard.getCashLeastCost();
						if (pay < cashleastcost * cardNum) {
							parameters.put("result", 0);
							parameters.put("message", "消费金额未达到该优惠券的起用金额");
							return parameters;
						} else {
							Double reducecost = duofencard.getReduceCost()
									* cardNum;
							pay = reduce(pay - reducecost);
						}
					} else {
						// 折扣券
						String code = CommonUtil.toString(obj.get("code"));
						Map<String, Object> dfcg = duofenCardGetMapper
								.findByCode(member.getBusid(), code);
						DuofenCard duofencard = duofenCardMapper
								.selectByPrimaryKey(CommonUtil.toInteger(dfcg
										.get("cId")));
						// 折扣优惠券
						pay = pay * duofencard.getDiscount() / 10;
						uc.setDiscount(new Double(duofencard.getDiscount() * 10)
								.intValue());
						uc.setDvid(CommonUtil.toInteger(dfcg.get("cId")));
						uc.setDiscountdepict(code);
						uc.setCardtype((byte) 1);
					}
				}
			}

			// 粉币
			if (offlinepay.getFenbiopen() == 1
					&& CommonUtil.isNotEmpty(obj.get("fenbiMoney"))
					&& !"0".equals(obj.get("fenbiMoney"))) {
				// 粉币抵消
				Double fenbiMoney = CommonUtil.toDouble(obj.get("fenbiMoney"));
				Double fenbi=memberPayService.deductFenbi(fenbiMoney, member.getBusid());
				uc.setFenbi(fenbi);
				if (fenbiMoney >= pay) {
					pay = 0.0;
				} else {
					pay = reduce(pay - fenbiMoney);
				}
			}
			// 积分
			if (offlinepay.getJifenopen() == 1
					&& CommonUtil.isNotEmpty(obj.get("integralMoney"))
					&& !"0".equals(obj.get("integralMoney"))) {
				// 积分抵扣
				
				
				Double integralMoney = CommonUtil.toDouble(obj
						.get("integralMoney"));
				
				int integralNum=memberPayService.deductJifen(integralMoney, member.getBusid());
				uc.setIntegral(integralNum);
				if (integralMoney >= pay) {
					pay = 0.0;
				} else {
					pay = reduce(pay - integralMoney);
				}
			}

			pay = countBalance(offlinepay.getBalancetype(),
					offlinepay.getHoldnum(), pay + countMoney);
			uc.setBususerid(member.getBusid());
			uc.setPublicId(member.getPublicId());
			uc.setMemberid(member.getId());
			uc.setRecordtype((byte) 2);
			uc.setStoreid(CommonUtil.toInteger(obj.get("shopid")));
			uc.setUctype((byte) 18);
			uc.setTotalmoney(total);
			uc.setCreatedate(new Date());
			uc.setPaystatus((byte) 0);
			uc.setDiscountmoney(pay);
			
			Integer payType = 0; // 支付方式
			if (CommonUtil.isNotEmpty(obj.get("payType"))) {
				payType = CommonUtil.toInteger(obj.get("payType"));
				if(payType==0){
					uc.setPaymenttype((byte) 1);
				}else{
					uc.setPaymenttype((byte) 0);
				}
			} else {
				payType = 0;
				uc.setPaymenttype((byte) 5);
			}
			
			String orderCode = CommonUtil.getMEOrderCode();
			uc.setOrdercode(orderCode);
			if (pay <= 0) {
				uc.setPaystatus((byte) 1);
			} else {
				uc.setPaystatus((byte) 0);
			}
			

			WxPublicUsers wxPublicUsers = wxPublicUsersMapper
					.selectByUserId(member.getBusid());
			
			Integer paytype = 1; // 默认储值卡支付
			if(payType==2 && pay>0){
				// <!--------支付有礼-------->
				Map<String, Object> psMap = paySuccessService.findPaySuccess(6,
						0, member.getId(), orderCode,
						new Date(), pay, 1, 1);
				String return_url = null;
				if ("-1".equals(CommonUtil.toString(psMap.get("code")))) {
					return_url = PropertiesUtil.getWebHomeUrl()
							+ "/offlinePayPhoneController/" + member.getBusid()
							+ "/" + obj.get("shopid")
							+ "/79B4DE7C/findOfflinePay_1.do";
				} else {
					String jedisUrl ="/phonePaySuccessController/79B4DE7C/paysuccessStr.do?busId="
							+ member.getBusid()
							+ "&model=6&urlDetail=null&returnUrl=/offlinePayPhoneController/"
							+ member.getBusid() + "/" + obj.get("shopid")
							+ "/79B4DE7C/findOfflinePay_1.do&orderMoney="
							+ psMap.get("orderMoney")
							+ "&continueUrl=/offlinePayPhoneController/"
							+ member.getBusid() + "/" + obj.get("shopid")
							+ "/79B4DE7C/findOfflinePay_1.do&payType=" + paytype
							+ "&memberId=" + obj.get("memberId") + "&orderNo="
							+ orderCode + "&orderMoneys="
							+ psMap.get("orderMoneys");
					
					String code=CommonUtil.toString(new Date().getTime());
					return_url=PropertiesUtil.getWebHomeUrl()
							+ "/phonePaySuccessController/"+code+"/79B4DE7C/paysuccessJedis.do";
					JedisUtil.set(code, jedisUrl, 5*60);
					
				}
				userConsumeMapper.insertSelective(uc);
				//支付包支付
				String red_url = "/alipay/79B4DE7C/alipayApi.do?out_trade_no="
						+ orderCode
						+ "&subject=优惠买单&model=15&businessUtilName=alipayNotifyUrlBuinessServiceOfflinepay_1&total_fee="
						+ pay + "&busId=" + member.getBusid()+"&return_url="+return_url;
				parameters.put("result", true);
				parameters.put("message", "未支付");
				parameters.put("red_url", red_url);
				return parameters;
			}else if (payType == 0 && pay > 0) {
				userConsumeMapper.insertSelective(uc);
				
				// 统一下单调用
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("appid", wxPublicUsers.getAppid());
				params.put("mchid", wxPublicUsers.getMchId());
				params.put("sysOrderNo", orderCode);
				params.put("key", wxPublicUsers.getApiKey());
				params.put("productId", orderCode);
				params.put("desc", "优惠支付");
				params.put("totalFee", pay);
				params.put("ip", "127.0.0.1");
				params.put("openid", member.getOpenid());

				params.put("url", url);
				params.put("model", 14);
				params.put("authRefreshToken",
						wxPublicUsers.getAuthRefreshToken());
				parameters = wxPayService.memberPay(params);
				parameters.put("shopid", obj.get("shopid"));

				if (CommonUtil.isNotEmpty(parameters.get("code"))
						&& parameters.get("code").equals("1")) {
					// 调用微信支付
					parameters.put("result", 3);
				} else {
					// 调用微信支付失败
					parameters.put("result", 4);
				}
				paytype = 2;
			} else {
				// <!---------储值卡支付start-->
				if (memberPayService.isMemember(member.getId())) {
					// 储值卡付款
					if (CommonUtil.isNotEmpty(card)
							&& card.size() > 0
							&& CommonUtil.toInteger(card.get(0).get("ct_id")) == 3
							&& pay > 0) {
						if (pay > CommonUtil.toDouble(card.get(0).get("money"))) {
							throw new Exception();
						}
						Card c = new Card();
						c.setMcId(member.getMcId());
						c.setMoney(CommonUtil
								.toDouble(card.get(0).get("money")) - pay);
						cardMapper.updateByPrimaryKeySelective(c);
					}
				}
				uc.setPaymenttype((byte)5);
				uc.setPaystatus((byte) 1);
				userConsumeMapper.insertSelective(uc);
				// 优惠券优惠 储值卡支付成功后
				if (rules.contains("2")
						&& CommonUtil.isNotEmpty(obj.get("cardType"))) {
					String cardType = CommonUtil.toString(obj.get("cardType"));
					if ("wx".equals(cardType)
							&& CommonUtil.isNotEmpty(obj.get("code"))) {
						String code = CommonUtil.toString(obj.get("code"));
						WxCardReceive wcr = wxCardReceiveMapper.findByCode1(
								member.getPublicId(), code);

						Map<String, Object> params = new HashMap<String, Object>();
						params.put("card_id", wcr.getCardId());
						params.put("code", code);

						Map<String, Object> result = wxCardService.codeConsume(
								params, wxPublicUsers);
						if ("-1".equals(result.get("code").toString())) {
							parameters.put("result", 0);
							parameters.put("message", "微信核销失败");
							return parameters;
						}
					} else {
						// 多粉优惠券处理
						String codes = uc.getDiscountdepict();
						Map<String, Object> map=new HashMap<>();
						map.put("codes", codes);
						map.put("storeId", uc.getStoreid());
						duofenCardService.verificationCard_2(map);  //卡券核销 
					//	duofenCardGetMapper.updateByCodes(codeList); // 卡券核销
					}
				}

				if (CommonUtil.isNotEmpty(offlinepay.getPhone())) {
					// 短信通知
					WxShop shop = wxShopMapper.selectByPrimaryKey(CommonUtil
							.toInteger(obj.get("shopid")));

					String content = shop.getBusinessName() + "优惠买单（"
							+ orderCode + "），消费" + total + "元，实付" + pay + "元";

					Map<String, Object> params = new HashMap<String, Object>();
					params.put("busId", member.getBusid());
					params.put("model", 10);
					String[] phone = offlinepay.getPhone().split(",");
					if (phone.length == 1) {
						params.put("mobiles", phone[0]);
					} else {
						params.put("mobiles", offlinepay.getPhone());
					}
					
					if(CommonUtil.isNotEmpty(wxPublicUsers)){
						params.put("company", wxPublicUsers.getAuthorizerInfo());
					}else{
						BusUser bususer=busUserMapper.selectByPrimaryKey(member.getBusid());
						params.put("company", bususer.getMerchant_name());
					}
					
					
					params.put("content", content);
					try {
						smsSpendingService.sendSms(params);
					} catch (Exception e) {
						LOG.error("短信发送失败", e);
					}
				}

				if (memberPayService.isMemember(member.getId())) {
					memberPayService.saveGiveConsume(member.getPhone(),
							orderCode);
					memberPayService.giveGood(orderCode);
					memberPayService.saveCardRecordNew(member.getMcId(), (byte) 1, uc.getDiscountmoney() + "元", "线下免费消费", 0, null, 0,uc.getDiscountmoney());
					
				}

				if (offlinepay.getFenbiopen() == 1
						&& CommonUtil.isNotEmpty(obj.get("fenbiMoney"))
						&& !"0".equals(obj.get("fenbiMoney"))) {
					// 归还商户粉币
					memberPayService.returnfansCurrency(
							wxPublicUsers.getBusUserId(), -uc.getFenbi().doubleValue());
					Member member1 = new Member();
					member1.setId(member.getId());
					member1.setFansCurrency(member.getFansCurrency()
							- uc.getFenbi());
					memberMapper.updateByPrimaryKeySelective(member1);

				}
				if (offlinepay.getJifenopen() == 1
						&& CommonUtil.isNotEmpty(obj.get("integralMoney"))
						&& !"0".equals(obj.get("integralMoney"))) {
					// 扣除用户积分数量
					Member member1 = new Member();
					member1.setId(member.getId());
					member1.setIntegral(member.getIntegral() - uc.getIntegral());
					memberMapper.updateByPrimaryKeySelective(member1);
				}
			}
			// <!--储值卡支付end-->

			// <!--------支付有礼-------->
			Map<String, Object> psMap = paySuccessService.findPaySuccess(6,
					0, member.getId(), orderCode,
					new Date(), pay, 1, 1);
			String red_url = null;
			if ("-1".equals(CommonUtil.toString(psMap.get("code")))) {
				red_url = PropertiesUtil.getWebHomeUrl()
						+ "/offlinePayPhoneController/" + member.getBusid()
						+ "/" + obj.get("shopid")
						+ "/79B4DE7C/findOfflinePay_1.do";
			} else {
				red_url = PropertiesUtil.getWebHomeUrl()
						+ "/phonePaySuccessController/79B4DE7C/paysuccessStr.do?busId="
						+ member.getBusid()
						+ "&model=6&urlDetail=null&returnUrl=/offlinePayPhoneController/"
						+ member.getBusid() + "/" + obj.get("shopid")
						+ "/79B4DE7C/findOfflinePay_1.do&orderMoney="
						+ psMap.get("orderMoney")
						+ "&continueUrl=/offlinePayPhoneController/"
						+ member.getBusid() + "/" + obj.get("shopid")
						+ "/79B4DE7C/findOfflinePay_1.do&payType=" + paytype
						+ "&memberId=" + obj.get("memberId") + "&orderNo="
						+ orderCode + "&orderMoneys="
						+ psMap.get("orderMoneys");
			}
			parameters.put("result", 1);
			parameters.put("message", "支付成功");
			parameters.put("red_url", red_url);
		} catch (Exception e) {
			LOG.error("优惠买单支付失败", e);
			throw new Exception();
		}
		return parameters;
	}

	@Override
	public void backCall(String out_trade_no) {
		List<Map<String, Object>> ucs = userConsumeMapper
				.findByOrderCode(out_trade_no);
		UserConsume uc = new UserConsume();
		uc.setId(CommonUtil.toInteger(ucs.get(0).get("id")));
		uc.setPaystatus((byte) 1);
		userConsumeMapper.updateByPrimaryKeySelective(uc);

		// 查询是否是会员
		Member member = memberMapper.selectByPrimaryKey(CommonUtil
				.toInteger(ucs.get(0).get("memberId")));

		// 微信回调处理
		if (CommonUtil.isNotEmpty(ucs.get(0).get("fenbi"))
				&& CommonUtil.toDouble(ucs.get(0).get("fenbi")) !=0) {
			Member member1 = new Member();
			member1.setId(CommonUtil.toInteger(ucs.get(0).get("memberId")));
			member1.setFansCurrency(member.getFansCurrency()
					+ CommonUtil.toDouble(ucs.get(0).get("fenbi")));
			memberMapper.updateByPrimaryKeySelective(member1);

			// 归还商户粉币
			memberPayService.returnfansCurrency(
					CommonUtil.toInteger(ucs.get(0).get("busUserId")),
					-CommonUtil.toDouble(ucs.get(0).get("fenbi")));

		}

		Integer cardType = CommonUtil.toInteger(ucs.get(0).get("cardType"));
		// 优惠券优惠
		if (cardType == 0) {
			WxPublicUsers wxPublicUsers = wxPublicUsersMapper
					.selectByPrimaryKey(member.getPublicId());
			iwxCardService.wxCardReceive(wxPublicUsers,
					CommonUtil.toString(ucs.get(0).get("disCountdepict")));
		} else if(cardType==1){
			
			// 多粉优惠券处理
			String codes = CommonUtil
					.toString(ucs.get(0).get("disCountdepict"));
			Map<String, Object> map=new HashMap<>();
			map.put("codes", codes);
			map.put("storeId", uc.getStoreid());
			duofenCardService.verificationCard_2(map);  //卡券核销 
		}

		if (CommonUtil.isNotEmpty(ucs.get(0).get("integral"))
				&& CommonUtil.toDouble(ucs.get(0).get("integral")) != 0) {
			// 扣除用户积分
			Member member1 = new Member();
			member1.setId(CommonUtil.toInteger(ucs.get(0).get("memberId")));
			int integral = new Double(member.getIntegral()
					+ CommonUtil.toDouble(ucs.get(0).get("integral")))
					.intValue();
			member1.setIntegral(integral);
			memberMapper.updateByPrimaryKeySelective(member1);
		}

		if (CommonUtil.isNotEmpty(member.getMcId())) {
			try {
				memberPayService.saveGiveConsume(member.getPhone(),
						out_trade_no);
				memberPayService.giveGood(out_trade_no);
				
				
				memberPayService.saveCardRecordNew(member.getMcId(), (byte) 1, ucs.get(0).get("discountMoney") + "元", "优惠消费", 0, null, 0,CommonUtil.toDouble(ucs.get(0).get("discountMoney")));
				
			} catch (Exception e) {
				LOG.error("会员支付回调赠送异常", e);
			}
		}

		// 商家自定义规则
		Offlinepay offlinepay = offlinepayMapper.findByshopId(
				member.getBusid(),
				CommonUtil.toInteger(ucs.get(0).get("storeId")));

		if (CommonUtil.isNotEmpty(offlinepay.getPhone())) {
			// 短信通知
			WxShop shop = wxShopMapper.selectByPrimaryKey(CommonUtil
					.toInteger(ucs.get(0).get("storeId")));

			String content = shop.getBusinessName() + "优惠买单（"
					+ ucs.get(0).get("orderCode") + "），消费"
					+ ucs.get(0).get("totalMoney") + "元，实付"
					+ ucs.get(0).get("discountMoney") + "元";

			WxPublicUsers wxPublicUsers = wxPublicUsersMapper
					.selectByUserId(member.getBusid());

			Map<String, Object> params = new HashMap<String, Object>();
			params.put("busId", wxPublicUsers.getBusUserId());
			params.put("model", 10);
			String[] phone = offlinepay.getPhone().split(",");
			if (phone.length == 1) {
				params.put("mobiles", phone[0]);
			} else {
				params.put("mobiles", offlinepay.getPhone());
			}
			
			if(CommonUtil.isNotEmpty(wxPublicUsers)){
				params.put("company", wxPublicUsers.getAuthorizerInfo());
			}else{
				BusUser bususer=busUserMapper.selectByPrimaryKey(member.getBusid());
				params.put("company", bususer.getMerchant_name());
			}
			params.put("content", content);
			try {
				smsSpendingService.sendSms(params);
			} catch (Exception e) {
				LOG.error("短信发送失败", e);
			}
		}
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public SortedMap<Object, Object> radioPay(String param, String url)
			throws Exception {

		SortedMap<Object, Object> parameters = new TreeMap<Object, Object>();
		try {
			// 1支付方式 2储值卡支付不需要调用微信 3 会员需赠送物品
			JSONObject obj = JSONObject.fromObject(param);
			Integer checked = CommonUtil.toInteger(obj.get("checked"));
			Member member = memberMapper.selectByPrimaryKey(CommonUtil
					.toInteger(obj.get("memberId")));
			Double total = CommonUtil.toDouble(obj.get("total"));
			Double countMoney = CommonUtil.toDouble(obj.get("countMoney"));
			Double pay = reduce(total - countMoney);
			// 商家自定义规则
			Offlinepay offlinepay = offlinepayMapper.findByshopId(
					member.getBusid(),
					CommonUtil.toInteger(obj.get("shopid")));
			String date = CommonUtil.toString(obj.get("date"));
			offlinepay = findByReduction(
					DateTimeKit.parse(date, "yyyy-MM-dd HH:mm:ss"), offlinepay);
			// 添加会员记录
			UserConsume uc = new UserConsume();
			// 商家优惠
			if (checked == 0) {
				Integer count = new Double(pay / offlinepay.getEachmoney())
						.intValue();
				Double youhuiMoney = count * offlinepay.getReducemoney();
				pay = reduce(pay - youhuiMoney);
			}
			List<Map<String, Object>> card = null;

			if (memberPayService.isMemember(member.getId())) {
				card = cardMapper.findCardById(member.getMcId());
				uc.setMcid(member.getMcId());
				uc.setCtid(CommonUtil.toInteger(card.get(0).get("ct_id")));
				uc.setGtId(CommonUtil.toInteger(card.get(0).get("gt_id")));
				uc.setDiscount(100);
			}

			// 会员卡优惠
			if (checked == 1) {
				// 会员卡 折扣卡
				if (CommonUtil.toInteger(card.get(0).get("ct_id")) == 2) {
					Integer discount = CommonUtil
							.toInteger(obj.get("discount"));
					uc.setDiscount(discount);
					pay = reduce(pay * discount / 100);

				}
			}
			// 优惠券优惠
			if (checked == 2 && CommonUtil.isNotEmpty(obj.get("cardType"))) {
				String cardType = CommonUtil.toString(obj.get("cardType"));
				if ("wx".equals(cardType)
						&& CommonUtil.isNotEmpty(obj.get("code"))) {
					// 微信优惠券处理
					String code = CommonUtil.toString(obj.get("code"));
					WxCardReceive wxCardReceive = wxCardReceiveMapper
							.findByCode(member.getPublicId(), code);
					WxCard wxCard = wxCardMapper.selectByCardId(wxCardReceive
							.getCardId());
					uc.setDvid(wxCard.getId());
					uc.setDiscountdepict(code);
					if ("DISCOUNT".equals(wxCard.getCardType())) {
						// 折扣优惠券
						pay = pay * wxCard.getDiscount() / 10;
						uc.setDiscount(new Double(wxCard.getDiscount() * 10)
								.intValue());
					} else if ("CASH".equals(wxCard.getCardType())) {
						Double cashleastcost = wxCard.getCashLeastCost();
						if (pay < cashleastcost) {
							parameters.put("result", 0);
							parameters.put("message", "消费金额未达到该优惠券的起用金额");
							return parameters;
						} else {
							Double reducecost = wxCard.getReduceCost();
							pay = reduce(pay - reducecost);
						}
					}
				} else {
					// 多粉优惠券处理
					String type = CommonUtil.toString(obj.get("type")); // 用来区分折扣券
																		// 和减免券
					if ("CASH".equals(type)) {
						// 减免券
						Integer cardId = CommonUtil
								.toInteger(obj.get("cardId"));
						int cardNum = CommonUtil.toInteger(obj.get("cardNum"));
						List<Map<String, Object>> dfcg = duofenCardGetMapper
								.findByCardId(cardId, member.getId(), cardNum);
						String duofenCode = "";
						for (Map<String, Object> map : dfcg) {
							duofenCode += map.get("code") + ",";
						}
						uc.setDvid(cardId);
						uc.setDiscountdepict(duofenCode);
						uc.setCardtype((byte) 1);

						DuofenCard duofencard = duofenCardMapper
								.selectByPrimaryKey(cardId);

						Double cashleastcost = duofencard.getCashLeastCost();
						if (pay < cashleastcost * cardNum) {
							parameters.put("result", 0);
							parameters.put("message", "消费金额未达到该优惠券的起用金额");
							return parameters;
						} else {
							Double reducecost = duofencard.getReduceCost()
									* cardNum;
							pay = reduce(pay - reducecost);
						}
					} else {
						// 折扣券
						String code = CommonUtil.toString(obj.get("code"));
						Map<String, Object> dfcg = duofenCardGetMapper
								.findByCode(member.getBusid(), code);
						DuofenCard duofencard = duofenCardMapper
								.selectByPrimaryKey(CommonUtil.toInteger(dfcg
										.get("cId")));
						// 折扣优惠券
						pay = pay * duofencard.getDiscount() / 10;
						uc.setDiscount(new Double(duofencard.getDiscount() * 10)
								.intValue());
						uc.setDvid(CommonUtil.toInteger(dfcg.get("cId")));
						uc.setDiscountdepict(code);
						uc.setCardtype((byte) 1);
					}
				}

			}
			// 粉币
			if (offlinepay.getFenbiopen() == 1
					&& CommonUtil.isNotEmpty(obj.get("fenbiMoney"))
					&& !"0".equals(obj.get("fenbiMoney"))) {
				// 粉币抵消
				Double fenbiMoney = CommonUtil.toDouble(obj.get("fenbiMoney"));
				Double fenbi =memberPayService.deductFenbi(fenbiMoney, member.getBusid());
				uc.setFenbi(fenbi);
				if (fenbiMoney >= pay) {
					pay = 0.0;
				} else {
					pay = reduce(pay - fenbiMoney);
				}
			}
			// 积分
			if (offlinepay.getJifenopen() == 1
					&& CommonUtil.isNotEmpty(obj.get("integralMoney"))
					&& !"0".equals(obj.get("integralMoney"))) {
				// 积分抵扣
				Double integralMoney = CommonUtil.toDouble(obj
						.get("integralMoney"));
				int integralNum=memberPayService.deductJifen(integralMoney, member.getBusid());
				uc.setIntegral(integralNum);
				if (integralMoney >= pay) {
					pay = 0.0;
				} else {
					pay = reduce(pay - integralMoney);
				}
			}
			pay = countBalance(offlinepay.getBalancetype(),
					offlinepay.getHoldnum(), (pay + countMoney));
			WxPublicUsers wxPublicUsers = wxPublicUsersMapper
					.selectByUserId(member.getBusid());
			uc.setBususerid(member.getBusid());
			uc.setPublicId(member.getPublicId());
			uc.setMemberid(member.getId());
			uc.setRecordtype((byte) 2);
			uc.setStoreid(CommonUtil.toInteger(obj.get("shopid")));
			uc.setUctype((byte) 18);
			uc.setTotalmoney(total);
			uc.setCreatedate(new Date());
			uc.setPaystatus((byte) 0);
			uc.setDiscountmoney(pay);
			Integer payType = 0; // 支付方式
			if (CommonUtil.isNotEmpty(obj.get("payType"))) {
				payType = CommonUtil.toInteger(obj.get("payType"));
				if(payType==0){
					uc.setPaymenttype((byte) 1);
				}else{
					uc.setPaymenttype((byte) 0);
				}
			} else {
				payType = 0;
				uc.setPaymenttype((byte) 5);
			}
			String orderCode = CommonUtil.getMEOrderCode();
			uc.setOrdercode(orderCode);
			if (pay <= 0) {
				uc.setPaystatus((byte) 1);
			} else {
				uc.setPaystatus((byte) 0);
			}

			Integer paytype = 1; // 默认处置卡卡支付

			if(payType==2 && pay > 0){
				
				// <!--------支付有礼-------->
				Map<String, Object> psMap = paySuccessService.findPaySuccess(6,
						0, member.getId(), orderCode,
						new Date(), pay, 1, 1);
				String return_url = null;
				if ("-1".equals(CommonUtil.toString(psMap.get("code")))) {
					return_url = PropertiesUtil.getWebHomeUrl()
							+ "/offlinePayPhoneController/" + member.getBusid()
							+ "/" + obj.get("shopid")
							+ "/79B4DE7C/findOfflinePay_1.do";
				} else {
					String jedisUrl ="/phonePaySuccessController/79B4DE7C/paysuccessStr.do?busId="
							+ member.getBusid()
							+ "&model=6&urlDetail=null&returnUrl=/offlinePayPhoneController/"
							+ member.getBusid() + "/" + obj.get("shopid")
							+ "/79B4DE7C/findOfflinePay_1.do&orderMoney="
							+ psMap.get("orderMoney")
							+ "&continueUrl=/offlinePayPhoneController/"
							+ member.getBusid() + "/" + obj.get("shopid")
							+ "/79B4DE7C/findOfflinePay_1.do&payType=" + paytype
							+ "&memberId=" + obj.get("memberId") + "&orderNo="
							+ orderCode + "&orderMoneys="
							+ psMap.get("orderMoneys");
					
					String code=CommonUtil.toString(new Date().getTime());
					return_url=PropertiesUtil.getWebHomeUrl()
							+ "/phonePaySuccessController/"+code+"/79B4DE7C/paysuccessJedis.do";
					JedisUtil.set(code, jedisUrl, 5*60);
				}
				userConsumeMapper.insertSelective(uc);
				//支付包支付
				String red_url = "/alipay/79B4DE7C/alipayApi.do?out_trade_no="
						+ orderCode
						+ "&subject=优惠买单&model=15&businessUtilName=alipayNotifyUrlBuinessServiceOfflinepay_1&total_fee="
						+ pay + "&busId=" + member.getBusid()+"&return_url="+return_url;
				parameters.put("result", true);
				parameters.put("message", "未支付");
				parameters.put("red_url", red_url);
				return parameters;
				
				
			}else if (payType == 0 && pay > 0) {
				userConsumeMapper.insertSelective(uc);
				// 统一下单调用
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("appid", wxPublicUsers.getAppid());
				params.put("mchid", wxPublicUsers.getMchId());
				params.put("sysOrderNo", orderCode);
				params.put("key", wxPublicUsers.getApiKey());
				params.put("productId", orderCode);
				params.put("desc", "优惠支付");
				params.put("totalFee", pay);
				params.put("ip", "127.0.0.1");
				params.put("openid", member.getOpenid());

				params.put("url", url);
				params.put("model", 14);
				params.put("authRefreshToken",
						wxPublicUsers.getAuthRefreshToken());
				parameters = wxPayService.memberPay(params);
				parameters.put("shopid", obj.get("shopid"));

				if (CommonUtil.isNotEmpty(parameters.get("code"))
						&& parameters.get("code").equals("1")) {
					// 调用微信支付
					parameters.put("result", 3);
				} else {
					// 调用微信支付失败
					parameters.put("result", 4);
				}
				paytype = 2;
			} else {
				uc.setPaymenttype((byte) 5);
				uc.setPaymenttype((byte) 4);
				// <!---------储值卡支付start-------------->
				if (memberPayService.isMemember(member.getId())) {
					// 储值卡付款
					if (CommonUtil.isNotEmpty(card)
							&& card.size() > 0
							&& CommonUtil.toInteger(card.get(0).get("ct_id")) == 3
							&& pay > 0) {
						if (pay > CommonUtil.toDouble(card.get(0).get("money"))) {
							throw new Exception();
						}
						Card c = new Card();
						c.setMcId(member.getMcId());
						c.setMoney(CommonUtil
								.toDouble(card.get(0).get("money")) - pay);
						cardMapper.updateByPrimaryKeySelective(c);
					}
				}
				uc.setPaystatus((byte) 1);
				userConsumeMapper.insertSelective(uc);

				// 优惠券优惠
				if (checked == 2 && CommonUtil.isNotEmpty(obj.get("cardType"))) {
					String cardType = CommonUtil.toString(obj.get("cardType"));
					if ("wx".equals(cardType)
							&& CommonUtil.isNotEmpty(obj.get("code"))) {
						String code = CommonUtil.toString(obj.get("code"));
						WxCardReceive wcr = wxCardReceiveMapper.findByCode1(
								member.getPublicId(), code);

						Map<String, Object> params = new HashMap<String, Object>();
						params.put("card_id", wcr.getCardId());
						params.put("code", code);

						Map<String, Object> result = wxCardService.codeConsume(
								params, wxPublicUsers);
						if ("-1".equals(result.get("code").toString())) {
							parameters.put("result", 0);
							parameters.put("message", "微信核销失败");
							return parameters;
						}
					} else {
						// 多粉优惠券处理
						String codes = uc.getDiscountdepict();
						Map<String, Object> map=new HashMap<>();
						map.put("codes", codes);
						map.put("storeId", uc.getStoreid());
						duofenCardService.verificationCard_2(map);  //卡券核销 
					}
				}

				if (CommonUtil.isEmpty(offlinepay.getPhone())) {
					// 短信通知
					WxShop shop = wxShopMapper.selectByPrimaryKey(CommonUtil
							.toInteger(obj.get("shopid")));

					String content = shop.getBusinessName() + "优惠买单（"
							+ orderCode + "），消费" + total + "元，实付" + pay + "元";

					Map<String, Object> params = new HashMap<String, Object>();
					params.put("busId", wxPublicUsers.getBusUserId());
					params.put("model", 10);
					String[] phone = offlinepay.getPhone().split(",");
					if (phone.length == 1) {
						params.put("mobiles", phone[0]);
					} else {
						params.put("mobiles", offlinepay.getPhone());
					}
					if(CommonUtil.isNotEmpty(wxPublicUsers)){
						params.put("company", wxPublicUsers.getAuthorizerInfo());
					}else{
						BusUser bususer=busUserMapper.selectByPrimaryKey(member.getBusid());
						params.put("company", bususer.getMerchant_name());
					}
					
					
					params.put("content", content);
					try {
						smsSpendingService.sendSms(params);
					} catch (Exception e) {
						LOG.error("短信发送失败", e);
					}
				}

				if (memberPayService.isMemember(member.getId())) {
					memberPayService.saveGiveConsume(member.getPhone(),
							orderCode);
					memberPayService.giveGood(orderCode);
					memberPayService.saveCardRecordNew(member.getMcId(), (byte) 1,uc.getDiscountmoney() + "元", "线下免费消费", 0, null, 0,uc.getDiscountmoney());
				}

				if (offlinepay.getFenbiopen() == 1
						&& CommonUtil.isNotEmpty(obj.get("fenbiMoney"))
						&& !"0".equals(obj.get("fenbiMoney"))) {
					// 归还商户粉币
					memberPayService.returnfansCurrency(
							wxPublicUsers.getBusUserId(), -uc.getFenbi().doubleValue());
					Member member1 = new Member();
					member1.setId(member.getId());
					member1.setFansCurrency(member.getFansCurrency()
							- uc.getFenbi());
					memberMapper.updateByPrimaryKeySelective(member1);

				}
				if (offlinepay.getJifenopen() == 1
						&& CommonUtil.isNotEmpty(obj.get("integralMoney"))
						&& !"0".equals(obj.get("integralMoney"))) {
					// 扣除用户积分数量
					Member member1 = new Member();
					member1.setId(member.getId());
					member1.setIntegral(member.getIntegral() - uc.getIntegral());
					memberMapper.updateByPrimaryKeySelective(member1);
				}
			}
			// <!---------储值卡支付end-------------->

			// <!---------支付有礼-------------->
			Map<String, Object> psMap = paySuccessService.findPaySuccess(6,
					0, member.getId(), orderCode,
					new Date(), pay, 1, 1);
			String red_url = null;
			if ("-1".equals(CommonUtil.toString(psMap.get("code")))) {
				red_url = PropertiesUtil.getWebHomeUrl()
						+ "/offlinePayPhoneController/" + member.getBusid()
						+ "/" + obj.get("shopid")
						+ "/79B4DE7C/findOfflinePay_1.do";
			} else {
				

				red_url = PropertiesUtil.getWebHomeUrl()
						+ "/phonePaySuccessController/79B4DE7C/paysuccessStr.do?busId="
						+ member.getBusid()
						+ "&model=6&urlDetail=null&returnUrl=/offlinePayPhoneController/"
						+ member.getBusid() + "/" + obj.get("shopid")
						+ "/79B4DE7C/findOfflinePay_1.do&orderMoney="
						+ psMap.get("orderMoney")
						+ "&continueUrl=/offlinePayPhoneController/"
						+ member.getBusid() + "/" + obj.get("shopid")
						+ "/79B4DE7C/findOfflinePay_1.do&payType=" + paytype
						+ "&memberId=" + obj.get("memberId") + "&orderNo="
						+ orderCode + "&orderMoneys="
						+ psMap.get("orderMoneys");
			}
			parameters.put("result", 1);
			parameters.put("message", "支付成功");
			parameters.put("red_url", red_url);
		} catch (Exception e) {
			LOG.error("优惠买单，支付失败", e);
			throw new Exception();
		}
		return parameters;
	}

	@Override
	public Offlinepay findByReduction(Date date, Offlinepay offlinepay) {
		List<Map<String, Object>> details = offlinepayDetailMapper
				.findByOffId(offlinepay.getId());
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		if (details.size() == 0) {
			return offlinepay;
		}
		switch (offlinepay.getTimetype()) {
		case 0:
			// 无
			return offlinepay;
		case 1:
			// 周
			for (Map<String, Object> map : details) {
				// 判断当前是星期几
				int d = date.getDay();
				if (d == 0) {
					d = 7; // 星期天 重新赋值7
				}
				if (d == CommonUtil.toInteger(map.get("dayTime"))) {
					int hours = date.getHours();
					if (hours >= CommonUtil.toInteger(map.get("beginTime"))
							&& hours < CommonUtil.toInteger(map.get("endTime"))) {
						offlinepay.setEachmoney(CommonUtil.toDouble(map
								.get("eachMoney")));
						offlinepay.setReducemoney(CommonUtil.toDouble(map
								.get("reduceMoney")));
					}
				}
			}
			return offlinepay;
		case 2:
			// 月
			for (Map<String, Object> map : details) {
				// 判断当前是哪一天
				int d = date.getDate();
				if (d == CommonUtil.toInteger(map.get("dayTime"))) {
					int hours = date.getHours();
					if (hours >= CommonUtil.toInteger(map.get("beginTime"))
							&& hours < CommonUtil.toInteger(map.get("endTime"))) {
						offlinepay.setEachmoney(CommonUtil.toDouble(map
								.get("eachMoney")));
						offlinepay.setReducemoney(CommonUtil.toDouble(map
								.get("reduceMoney")));
					}
				}
			}
			return offlinepay;
		case 3:
			// 年
			for (Map<String, Object> map : details) {
				// 判断当前是哪一天
				String time = c.get(Calendar.YEAR) + "/"
						+ map.get("dayTime").toString();
				Date dateTime = DateTimeKit.parse(time, "yyyy/MM/dd");
				if (DateTimeKit.isSameDay(date, dateTime)) {
					int hours = date.getHours();
					if (hours >= CommonUtil.toInteger(map.get("beginTime"))
							&& hours < CommonUtil.toInteger(map.get("endTime"))) {
						offlinepay.setEachmoney(CommonUtil.toDouble(map
								.get("eachMoney")));
						offlinepay.setReducemoney(CommonUtil.toDouble(map
								.get("reduceMoney")));
					}
				}
			}

			return offlinepay;

		default:
			break;
		}
		return offlinepay;
	}

	*//**
	 * 优惠买单 最终结算金额方式
	 * 
	 * @param balanceType
	 * @param holdNum
	 * @return
	 *//*
	public Double countBalance(Byte balanceType, Byte holdNum, Double money) {
		if ("2".equals(balanceType.toString())) {
			return reduceOne(money, 2);
		} else if ("0".equals(balanceType.toString())) {
			// 四舍五入
			switch (holdNum) {
			case 0:
				// 保留元
				return (double) Math.round(money);
			case 1:
				// 保留角
				return reduceOne(money, 1);
			case 2:
				return reduceOne(money, 2);
			default:
				break;
			}
		} else {
			// 抹零
			switch (holdNum) {
			case 0:
				// 保留元
				return (double) Math.floor(money);
			case 1:
				// 保留角
				return clearZero(money, 1);
			case 2:
				return clearZero(money, 2);
			case 3:
				// 保留10元
				if (money < 10) {
					return 0.0;
				} else {
					return clearZero(money / 10, 1) * 10;
				}
			default:
				break;
			}
		}
		return null;

	}

	*//**
	 * 保留两位小数
	 * 
	 * @param oneNumber
	 * @param twoNumber
	 * @return
	 *//*
	public Double reduce(Double number) {
		long l1 = Math.round(number * 100); // 四舍五入
		double ret = l1 / 100.0;
		return ret;
	}

	*//**
	 * 四舍五入保留小数
	 * 
	 * @param number
	 *            保留值
	 * @param num
	 *            小数位数
	 * @return
	 *//*
	public Double reduceOne(Double number, int num) {
		long l1 = Math.round(number * Math.pow(10, num)); // 四舍五入
		double ret = l1 / Math.pow(10, num);
		return ret;
	}

	*//**
	 * 清楚小数点后n位小数
	 * 
	 * @param number
	 * @param num
	 * @return
	 *//*
	public static Double clearZero(Double number, int num) {
		Integer b1 = new Double(number * Math.pow(10, num)).intValue();
		return b1 / Math.pow(10, num);
	}

	@Transactional(rollbackFor=Exception.class)
	@Override
	public Map<String, Object> otherBrowserPay(String param) throws Exception {
		Map<String, Object> parameters = new HashMap<String, Object>();
		try {
			// 1支付方式 2储值卡支付不需要调用微信 3 会员需赠送物品
			JSONObject obj = JSONObject.fromObject(param);
			Double total = CommonUtil.toDouble(obj.get("total"));
			Double countMoney = CommonUtil.toDouble(obj.get("countMoney"));
			Double pay = reduce(total - countMoney);
			// 商家自定义规则

			Integer busId=CommonUtil.toInteger(obj.get("busId"));
			Offlinepay offlinepay = offlinepayMapper.findByshopId(
					busId,
					CommonUtil.toInteger(obj.get("shopid")));
			String date = CommonUtil.toString(obj.get("date"));
			offlinepay = findByReduction(
					DateTimeKit.parse(date, "yyyy-MM-dd HH:mm:ss"), offlinepay);

			// 添加会员记录
			UserConsume uc = new UserConsume();
			// 商家优惠

			Integer count = new Double(pay / offlinepay.getEachmoney())
					.intValue();
			Double youhuiMoney = count * offlinepay.getReducemoney();
			pay = reduce(pay - youhuiMoney);
			
			pay = countBalance(offlinepay.getBalancetype(),
					offlinepay.getHoldnum(), (pay + countMoney));
			uc.setBususerid(busId);
			uc.setRecordtype((byte) 2);
			uc.setStoreid(CommonUtil.toInteger(obj.get("shopid")));
			uc.setUctype((byte) 18);
			uc.setTotalmoney(total);
			uc.setCreatedate(new Date());
			uc.setPaystatus((byte) 0);
			uc.setDiscountmoney(pay);
			uc.setPaymenttype((byte) 0);
			String orderCode = CommonUtil.getMEOrderCode();
			uc.setOrdercode(orderCode);
			if (pay <= 0) {
				uc.setPaystatus((byte) 1);
			} else {
				uc.setPaystatus((byte) 0);
			}

			
			
			if (pay > 0) {
				// <!--------支付有礼-------->
				String return_url = PropertiesUtil.getWebHomeUrl()
						+ "/offlinePayPhoneController/" + busId
						+ "/" + obj.get("shopid")
						+ "/79B4DE7C/findOfflinePay_1.do";
				userConsumeMapper.insertSelective(uc);
				String red_url="/alipay/79B4DE7C/alipayApi.do?out_trade_no="+orderCode+"&subject=优惠买单&model=14&businessUtilName=alipayNotifyUrlBuinessServiceOfflinepay"
						+ "&total_fee="+pay+"&busId="+busId+"&return_url="+return_url;
				parameters.put("result", 0);
				parameters.put("message", "未支付");
				parameters.put("red_url", red_url);
			} else {
				if (CommonUtil.isEmpty(offlinepay.getPhone())) {
					// 短信通知
					WxShop shop = wxShopMapper.selectByPrimaryKey(CommonUtil
							.toInteger(obj.get("shopid")));

					String content = shop.getBusinessName() + "优惠买单（"
							+ orderCode + "），消费" + total + "元，实付" + pay + "元";

					Map<String, Object> params = new HashMap<String, Object>();
					params.put("busId", busId);
					params.put("model", 10);
					String[] phone = offlinepay.getPhone().split(",");
					if (phone.length == 1) {
						params.put("mobiles", phone[0]);
					} else {
						params.put("mobiles", offlinepay.getPhone());
					}
					
					WxPublicUsers wxPublicUsers=wxPublicUsersMapper.selectByUserId(busId);
					
					if(CommonUtil.isEmpty(wxPublicUsers)){
						BusUser busUser=busUserMapper.selectByPrimaryKey(busId);
						params.put("company", busUser.getMerchant_name());
					}else{
						params.put("company", wxPublicUsers.getAuthorizerInfo());
					}
					params.put("content", content);
					try {
						smsSpendingService.sendSms(params);
					} catch (Exception e) {
						LOG.error("短信发送失败", e);
					}
				}
				parameters.put("result", 1);
				parameters.put("message", "支付成功");
			}
		} catch (Exception e) {
			LOG.error("优惠买单，支付失败", e);
			throw new Exception();
		}
		return parameters;
	}

	@Override
	public void aliPayPayBack(String orderNo) {
		try {
			UserConsume uc = userConsumeMapper.findByOrderCode1(orderNo);
			if (CommonUtil.isNotEmpty(uc)) {
				UserConsume u1 = new UserConsume();
				u1.setId(uc.getId());
				u1.setPaystatus((byte) 1);
				userConsumeMapper.updateByPrimaryKeySelective(u1);
			}
			
			// 商家自定义规则
			Offlinepay offlinepay = offlinepayMapper.findByshopId(
					uc.getBususerid(),
					uc.getStoreid());

			if (CommonUtil.isNotEmpty(offlinepay.getPhone())) {
				// 短信通知
				WxShop shop = wxShopMapper.selectByPrimaryKey(uc.getStoreid());

				String content = shop.getBusinessName() + "优惠买单（"
						+ uc.getOrdercode() + "），消费"
						+ uc.getTotalmoney() + "元，实付"
						+ uc.getDiscountmoney() + "元";

				WxPublicUsers wxPublicUsers = wxPublicUsersMapper
						.selectByUserId(uc.getBususerid());

				Map<String, Object> params = new HashMap<String, Object>();
				params.put("busId", uc.getBususerid());
				params.put("model", 10);
				String[] phone = offlinepay.getPhone().split(",");
				if (phone.length == 1) {
					params.put("mobiles", phone[0]);
				} else {
					params.put("mobiles", offlinepay.getPhone());
				}
				if(CommonUtil.isNotEmpty(wxPublicUsers)){
					params.put("company", wxPublicUsers.getAuthorizerInfo());
				}else{
					BusUser bususer=busUserMapper.selectByPrimaryKey(offlinepay.getBusid());
					params.put("company", bususer.getMerchant_name());
				}
				
				params.put("company", wxPublicUsers.getAuthorizerInfo());
				params.put("content", content);
				try {
					smsSpendingService.sendSms(params);
				} catch (Exception e) {
					LOG.error("短信发送失败", e);
				}
			}
			
		} catch (Exception e) {
			LOG.error("优惠买单异常支付宝支付回调订单号："+orderNo, e);
		}
		
	}

}
*/