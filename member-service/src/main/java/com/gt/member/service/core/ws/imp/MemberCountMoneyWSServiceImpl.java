//package com.gt.member.service.core.ws.imp;
//
//import java.text.DecimalFormat;
//import java.util.Date;
//import java.util.List;
//import java.util.Map;
//
//import javax.jws.WebService;
//
//import org.apache.log4j.Logger;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.gt.dao.member.CardMapper;
//import com.gt.dao.member.GiveRuleMapper;
//import com.gt.dao.member.MemberMapper;
//import com.gt.dao.member.PublicParameterSetMapper;
//import com.gt.dao.member.card.DuofenCardGetMapper;
//import com.gt.dao.member.card.DuofenCardMapper;
//import com.gt.dao.member.wxcard.WxCardMapper;
//import com.gt.dao.member.wxcard.WxCardReceiveMapper;
//import com.gt.entity.member.Card;
//import com.gt.entity.member.GiveRule;
//import com.gt.entity.member.Member;
//import com.gt.entity.member.PublicParameterSet;
//import com.gt.entity.member.card.DuofenCard;
//import com.gt.entity.member.card.DuofenCardGet;
//import com.gt.entity.member.wxcard.WxCard;
//import com.gt.entity.member.wxcard.WxCardReceive;
//import com.gt.service.common.dict.DictService;
//import com.gt.util.BigDecimalUtil;
//import com.gt.util.CommonUtil;
//import com.gt.ws.countservice.MemberCountMoneyWSService;
//import com.gt.ws.entitybo.count.MallAllEntity;
//import com.gt.ws.entitybo.count.MallEntity;
//import com.gt.ws.entitybo.count.MallShopEntity;
//import com.gt.ws.entitybo.count.MemberShopEntity;
//import com.mysql.fabric.xmlrpc.base.Data;
//
///**
// * 会员统一算法
// *
// * @author pengjiangli
// *
// */
//@WebService(serviceName = "memberCountMoneyWS")
//@Service
//public class MemberCountMoneyWSServiceImpl implements MemberCountMoneyWSService {
//
//	private static final Logger LOG = Logger
//			.getLogger(MemberCountMoneyWSServiceImpl.class);
//
//	@Autowired
//	private MemberMapper memberMapper;
//
//	@Autowired
//	private CardMapper cardMapper;
//
//	@Autowired
//	private GiveRuleMapper giveRuleMapper;
//
//	@Autowired
//	private WxCardReceiveMapper wxCardReceiveMapper;
//
//	@Autowired
//	private WxCardMapper wxCardMapper;
//
//	@Autowired
//	private DuofenCardGetMapper duofenCardGetMapper;
//
//	@Autowired
//	private DuofenCardMapper duofenCardMapper;
//
//	@Autowired
//	private DictService dictService;
//
//	@Autowired
//	private PublicParameterSetMapper publicParameterSetMapper;
//
//	/**
//	 * 门店下统一算法
//	 *
//	 * @param ce
//	 * @return
//	 */
//	@Override
//	public MemberShopEntity publicMemberCountMoney(MemberShopEntity ce) {
//		Double pay = ce.getTotalMoney();
//		Member member = memberMapper.selectByPrimaryKey(ce.getMemberId());
//
//		WxCardReceive wxCardReceive = null;
//		WxCard wxcard = null;
//		DuofenCardGet dfget = null;
//		DuofenCard dfcard = null;
//
//		// 使用了折扣优惠券 将不会启动折扣卡打折
//		Boolean isUseDisCount = false;
//		if (ce.isUseCoupon()) {
//			if (ce.getCouponType() == 0) {
//				// 微信优惠券
//				wxCardReceive = wxCardReceiveMapper.selectByPrimaryKey(ce
//						.getCoupondId());
//				wxcard = wxCardMapper.selectByCardId(wxCardReceive.getCardId());
//				if ("DISCOUNT".equals(wxcard.getCardType())) {
//					isUseDisCount = true;
//				}
//			} else if (ce.getCouponType() == 1) {
//				// 多粉优惠券
//				dfget = duofenCardGetMapper.selectByPrimaryKey(ce
//						.getCoupondId());
//				dfcard = duofenCardMapper.selectByPrimaryKey(dfget.getCardid());
//				if (dfcard.getCardType() == 0) {
//					isUseDisCount = true;
//				}
//			}
//		}
//
//		// 会员查询会员信息
//		Card card = cardMapper.selectByPrimaryKey(member.getMcId());
//
//		// 查询会员折扣
//		if (CommonUtil.isNotEmpty(card) && card.getCtId() == 2) {
//			if (!isUseDisCount) {
//				// 折扣卡
//				GiveRule gr = giveRuleMapper.selectByPrimaryKey(card.getGrId());
//				Double discountMemberMoney = formatNumber(pay
//						* (100 - gr.getGrDiscount()) / 100);
//				pay = pay - discountMemberMoney; // 折扣后的金额
//				ce.setDiscountMemberMoney(discountMemberMoney); // 会员优惠金额
//			}
//		}
//
//		// 计算使用优惠券后
//		if (ce.isUseCoupon()) {
//			if (ce.getCouponType() == 0) {
//				if ("DISCOUNT".equals(wxcard.getCardType())) {
//					// 折扣券
//					Double discountConponMoney = formatNumber(pay
//							* (10 - wxcard.getDiscount()) / 10);
//					pay = pay - discountConponMoney;
//					ce.setDiscountConponMoney(discountConponMoney); // 优惠券优惠金额
//					ce.setCodes(wxCardReceive.getUserCardCode());
//					ce.setCanUseConpon(true);
//				} else if ("CASH".equals(wxcard.getCardType())) {
//					// 减免券
//					if (pay >= wxcard.getCashLeastCost()) {
//						pay = pay - wxcard.getReduceCost();
//						ce.setDiscountConponMoney(wxcard.getReduceCost()); // 优惠券优惠金额
//						ce.setCodes(wxCardReceive.getUserCardCode());
//						ce.setCanUseConpon(true);
//					}
//				}
//			} else if (ce.getCouponType() == 1) {
//				// 多粉优惠券
//				if (dfcard.getCardType() == 0) {
//					// 折扣券
//					Double discountConponMoney = formatNumber(pay
//							* (10 - dfcard.getDiscount()) / 10);
//					pay = pay - discountConponMoney;
//					ce.setDiscountConponMoney(discountConponMoney); // 优惠券优惠金额
//					ce.setCanUseConpon(true);
//					ce.setCodes(dfget.getCode());
//				} else if (dfcard.getCardType() == 1) {
//					// 减免券
//					if (dfcard.getAdduser() == 0) {
//						// 不允许叠加使用
//						if (pay >= dfcard.getCashLeastCost()) {
//							pay = pay - dfcard.getReduceCost();
//							ce.setDiscountConponMoney(dfcard.getReduceCost()); // 优惠券优惠金额
//							ce.setCodes(dfget.getCode());
//							ce.setCanUseConpon(true);
//						}
//					} else {
//						Integer num = 0;
//						// 满足使用条件
//						if (pay >= dfcard.getCashLeastCost()) {
//							if (dfcard.getCashLeastCost() > 0) {
//								num = (int) (pay / dfcard.getCashLeastCost()
//										.intValue()); // 能使用优惠券数量
//							}
//							// 允许叠加使用
//							List<Map<String, Object>> dfcg = duofenCardGetMapper
//									.findByCardId(dfcard.getId(),
//											ce.getMemberId(), num);
//							if (dfcg.size() > 0) {
//								String duofenCode = "";
//								for (Map<String, Object> map : dfcg) {
//									duofenCode += map.get("code") + ",";
//								}
//								num = dfcg.size();
//								Double discountConponMoney = formatNumber(num
//										* dfcard.getReduceCost()); // 优惠券金额
//								pay = pay - discountConponMoney;
//								ce.setDiscountConponMoney(discountConponMoney);
//								ce.setCouponNum(num); // 使用优惠券数量
//								ce.setCodes(duofenCode);
//								ce.setCanUseConpon(true);
//							}
//						}
//					}
//				}
//			}
//		}
//
//		// 计算粉币金额 粉币10元启用
//		if (ce.isUseFenbi()) {
//			Double discountfenbiMoney = currencyCount(pay,
//					member.getFansCurrency()); // 计算粉币抵扣
//			if (discountfenbiMoney > 0) {
//				pay = pay - discountfenbiMoney;
//				Integer fenbiNum = deductFenbi(discountfenbiMoney).intValue();
//				ce.setFenbiNum(fenbiNum);
//				ce.setDiscountfenbiMoney(discountfenbiMoney);
//				ce.setCanUsefenbi(true);
//			}
//		}
//
//		// 计算积分金额
//		if (ce.isUseFenbi()) {
//			Double discountjifenMoney = integralCount(pay, member.getIntegral()
//					.doubleValue(), member.getBusid()); // 计算积分
//			if (discountjifenMoney > 0) {
//				pay = pay - discountjifenMoney;
//				Integer jifenNum = deductJifen(discountjifenMoney,
//						member.getBusid()).intValue();
//				ce.setJifenNum(jifenNum);
//				ce.setDiscountjifenMoney(discountjifenMoney);
//				ce.setCanUseJifen(true);
//			}
//		}
//		ce.setBalanceMoney(pay);
//		return ce;
//	}
//
//	@Override
//	public MallAllEntity mallSkipShopCount(MallAllEntity mallAllEntity) {
//		Long start=new Date().getTime();
//		boolean isMemberCard=false;  //用来标示该粉丝是会员
//		Card card=null;
//		Member member = memberMapper.selectByPrimaryKey(mallAllEntity.getMemberId());
//		Double memberDiscount=1.0;  //会员卡折扣
//		if (CommonUtil.isNotEmpty(member) && CommonUtil.isNotEmpty(member.getMcId())) {
//			card=cardMapper.selectByPrimaryKey(member.getMcId());
//			if(card.getCardstatus()==0){  //如果会员卡状态不为0标示该会员卡被禁用了
//				isMemberCard=true;
//				// 折扣卡
//				if(card.getCtId()==2){
//					GiveRule gr = giveRuleMapper.selectByPrimaryKey(card.getGrId());
//					memberDiscount=BigDecimalUtil.div(gr.getGrDiscount(), 100.0);  //会员折扣
//				}
//			}
//		}
//		//所有门店使用卡券信息
//		Map<Integer,MallShopEntity> mallShopMap=mallAllEntity.getMallShops();
//		Double discountMemberMoneyByAll=0.0; //总订单会员优惠券金额
//		Double discountConponMoneyByAll=0.0;   //优惠券券优惠金额
//		Integer canUsefenbiByAll=0;  //是否能用粉币
//		Integer canUseJifenByAll=0;  //是否能用积分
//		boolean isUseDisCount=false;  //实付使用折扣
//		WxCardReceive wxCardReceive = null;
//		WxCard wxcard = null;
//		DuofenCardGet dfget = null;
//		DuofenCard dfcard = null;
//		List<Map<String, Object>> dfcg =null;
//
//		//计算门店下使用了优惠券商品信息
//		for (MallShopEntity mallShopEntity : mallShopMap.values()) {
//			Double discountMemberMoneyByShopId=0.0; //门店会员优惠券金额
//			String codesByShopId="";  //门店使用优惠券code值 用来核销卡券 不存在set
//			Integer couponNumByShopId=1;  //门店使用优惠券数量
//			Integer canUseConponByShopId=0;  //门店是否能用优惠券
//			Double discountConponMoneyByShopId=0.0;   //门店优惠券优惠券金额
//			Map<Integer,MallEntity> mallList=mallShopEntity.getMalls();  //门店下商品信息
//			Integer useCoupon=mallShopEntity.getUseCoupon(); //是否使用了优惠券
//			Integer couponType=mallShopEntity.getCouponType();  //优惠券类型 0微信 1多粉
//			Integer coupondId=mallShopEntity.getCoupondId();   //优惠券id
//			Double discountLinshi=0.0;  //折扣优惠券的折扣值 0到1
//			Double reduceCose=0.0;
//			if (useCoupon==1) {
//				if (couponType == 0) {
//					// 微信优惠券
//					wxCardReceive = wxCardReceiveMapper.selectByPrimaryKey(coupondId);
//					wxcard = wxCardMapper.selectByCardId(wxCardReceive
//							.getCardId());
//					if ("DISCOUNT".equals(wxcard.getCardType())) {
//						isUseDisCount = true;
//						discountLinshi=wxcard.getDiscount()/10;
//					}else{
//						reduceCose=wxcard.getReduceCost();
//					}
//					codesByShopId=wxCardReceive.getUserCardCode();
//				} else if (couponType == 1) {
//					// 多粉优惠券
//					dfget = duofenCardGetMapper.selectByPrimaryKey(coupondId);
//					dfcard = duofenCardMapper.selectByPrimaryKey(dfget
//							.getCardid());
//					if (dfcard.getCardType() == 0) {
//						isUseDisCount = true;
//						discountLinshi=dfcard.getDiscount()/10;
//					}
//					if(dfcard.getAdduser()==1){
//						// 允许叠加使用
//						dfcg = duofenCardGetMapper
//								.findByCardId(dfcard.getId(),
//										mallAllEntity.getMemberId(), 0);
//					}
//					codesByShopId=dfget.getCode();
//				}
//			}
//
//			//<!-------------------start----------------------------->
//			if(useCoupon==0 && isMemberCard && card.getCtId()==2){
//				//未使用优惠券 是折扣卡会员 计算优惠
//				for (MallEntity mallEntity : mallList.values()) {
//					if(mallEntity.getUserCard()==1){
//						//会员卡优惠
//						Double discountMemberMoneyByOne=formatNumber(BigDecimalUtil.mul(mallEntity.getTotalMoneyOne(),BigDecimalUtil.sub(1, memberDiscount)));
//						Double unitPrice=formatNumber(BigDecimalUtil.sub(mallEntity.getTotalMoneyOne(), discountMemberMoneyByOne));
//
//						//商品数据
//						mallEntity.setUnitPrice(unitPrice);
//						mallEntity.setDiscountMemberMoney(formatNumber(BigDecimalUtil.mul(discountMemberMoneyByOne,mallEntity.getNumber())));
//						mallEntity.setBalanceMoney(formatNumber(BigDecimalUtil.mul(unitPrice,mallEntity.getNumber())));  //商品订单总金额
//						mallEntity.setTotalMoneyAll(formatNumber(BigDecimalUtil.sub(mallEntity.getTotalMoneyAll(),mallEntity.getDiscountMemberMoney())));
//						mallEntity.setCanUserCard(1);
//						mallList.put(mallEntity.getMallId(), mallEntity);  //数据处理完归还
//
//						//门店订单数据
//						discountMemberMoneyByShopId=formatNumber(BigDecimalUtil.add(discountMemberMoneyByShopId,BigDecimalUtil.mul(discountMemberMoneyByOne,mallEntity.getNumber())));  //该门店商品会员优惠金额
//
//						//总订单数据
//						discountMemberMoneyByAll=formatNumber(BigDecimalUtil.add(discountMemberMoneyByAll,BigDecimalUtil.mul(discountMemberMoneyByOne,mallEntity.getNumber())));
//					}
//				}
//
//			}else if(useCoupon==1){
//				if(isUseDisCount){
//					//折扣优惠券处理业务
//					for (MallEntity mallEntity : mallList.values()) {
//						//挑选出能使用优惠券的商品
//						if(mallEntity.getUseCoupon()==1){
//							//单个商品优惠券金额
//							Double discountConponMoneyByOne=formatNumber(BigDecimalUtil.mul(mallEntity.getTotalMoneyOne(),1-discountLinshi));
//							Double unitPrice=formatNumber(mallEntity.getTotalMoneyOne()-discountConponMoneyByOne);
//
//							//商品数据
//							mallEntity.setUnitPrice(unitPrice);  //当个商品价格
//							mallEntity.setDiscountConponMoney(formatNumber(BigDecimalUtil.mul(discountConponMoneyByOne,mallEntity.getNumber())));
//							mallEntity.setBalanceMoney(formatNumber(BigDecimalUtil.mul(unitPrice,mallEntity.getNumber())));
//							mallEntity.setTotalMoneyAll(formatNumber(BigDecimalUtil.sub(mallEntity.getTotalMoneyAll(),mallEntity.getDiscountConponMoney())));
//							mallEntity.setCanUseConpon(1); //能使用优惠券
//							mallList.put(mallEntity.getMallId(), mallEntity); //归还到分离的数据中去
//
//							//门店订单数据
//							canUseConponByShopId=1;  //门店商品是否使用优惠券标示
//							discountConponMoneyByShopId=formatNumber(BigDecimalUtil.add(discountConponMoneyByShopId,mallEntity.getDiscountConponMoney()));
//
//							//总订单数据
//							discountConponMoneyByAll=formatNumber(BigDecimalUtil.add(discountConponMoneyByAll,mallEntity.getDiscountConponMoney()));
//						}
//					}
//				}else{
//					//减免券处理方式  1如果存在折扣卡 先处理折扣卡优惠券  2再处理优惠券减免
//					for (MallEntity mallEntity : mallList.values()) {
//						//先处理会员卡
//						if(mallEntity.getUserCard()==1){
//							if(isMemberCard && card.getCtId()==2){
//								//商品会员卡优惠券
//								Double discountMemberMoneyByOne=formatNumber(BigDecimalUtil.mul(mallEntity.getTotalMoneyOne(),(1-memberDiscount)));
//								Double unitPrice=formatNumber(BigDecimalUtil.sub(mallEntity.getTotalMoneyOne(),discountMemberMoneyByOne));
//
//								//商品数据
//								mallEntity.setUnitPrice(unitPrice);
//								mallEntity.setDiscountMemberMoney(formatNumber(BigDecimalUtil.mul(discountMemberMoneyByOne,mallEntity.getNumber())));
//								mallEntity.setBalanceMoney(formatNumber(BigDecimalUtil.mul(unitPrice,mallEntity.getNumber())));  //商品订单总金额
//								mallEntity.setCanUserCard(1);
//								mallEntity.setTotalMoneyAll(formatNumber(BigDecimalUtil.sub(mallEntity.getTotalMoneyAll(),mallEntity.getDiscountMemberMoney())));
//
//								//门店订单数据
//								discountMemberMoneyByShopId=formatNumber(BigDecimalUtil.add(discountMemberMoneyByShopId,mallEntity.getDiscountMemberMoney()));  //该门店商品会员优惠金额
//
//								//总订单数据
//								discountMemberMoneyByAll=formatNumber(BigDecimalUtil.add(discountMemberMoneyByAll,+mallEntity.getDiscountMemberMoney()));
//
//							}
//							mallList.put(mallEntity.getMallId(), mallEntity);  //数据处理完归还
//						}
//					}
//					//<!-------会员卡优惠券已结束-------->
//
//					//<!---------减免券start------------>
//					Double balanceMoneyBili=0.0;//门店订单支付金额
//					Integer couponCount=0;
//					Integer couponCount1=0;
//					Double couponCountFentan=0.0;
//					for (MallEntity mallEntity : mallList.values()) {  //商品信息
//						//挑选出能使用优惠券的商品
//						if(mallEntity.getUseCoupon()==1){
//							balanceMoneyBili=formatNumber(BigDecimalUtil.add(balanceMoneyBili, mallEntity.getTotalMoneyAll()));
//							couponCount++;
//						}
//					}
//
//					// 计算微信减免券优惠
//					Double discountConponMoneyByMall=0.0;
//					if (useCoupon==1) {
//						//<!-------start-------->
//						if (couponType == 0) {
//							for (MallEntity mallEntity : mallList.values()) {
//								if(mallEntity.getUseCoupon()==1){
//									if(balanceMoneyBili>=wxcard.getCashLeastCost()){
//										//商品数据
//										//减免金额分摊
//										couponCount1++;
//										if(couponCount1==couponCount){
//											mallEntity.setDiscountConponMoney(BigDecimalUtil.sub(reduceCose, couponCountFentan));
//										}else{
//											discountConponMoneyByMall=formatNumber(BigDecimalUtil.mul(BigDecimalUtil.div(mallEntity.getTotalMoneyAll(), balanceMoneyBili),reduceCose));  //分摊金额
//											mallEntity.setDiscountConponMoney(discountConponMoneyByMall);
//										}
//										couponCountFentan=couponCountFentan+discountConponMoneyByMall;
//
//										mallEntity.setBalanceMoney(formatNumber(BigDecimalUtil.sub(mallEntity.getTotalMoneyAll(),mallEntity.getDiscountConponMoney())));
//										mallEntity.setUnitPrice(formatNumber(BigDecimalUtil.div(mallEntity.getBalanceMoney(),mallEntity.getNumber())));
//										mallEntity.setCanUseConpon(1); //能使用优惠券
//										mallEntity.setTotalMoneyAll(formatNumber(BigDecimalUtil.sub(mallEntity.getTotalMoneyAll(), mallEntity.getDiscountConponMoney())));
//										mallList.put(mallEntity.getMallId(), mallEntity);
//
//										//门店订单数据
//										canUseConponByShopId=1;  //门店商品是否使用优惠券标示
//										discountConponMoneyByShopId=formatNumber(BigDecimalUtil.add(discountConponMoneyByShopId, mallEntity.getDiscountConponMoney()));  //该门店商品会员优惠金额
//
//										//总订单数据
//										discountConponMoneyByAll=formatNumber(BigDecimalUtil.add(discountConponMoneyByAll, mallEntity.getDiscountConponMoney()));
//
//									}
//								}
//							}
//							//<!-------end-------->
//
//						} else if (couponType == 1) {
//							//计算多粉优惠券减免券优惠
//							reduceCose=dfcard.getReduceCost();
//							for (MallEntity mallEntity : mallList.values()) {
//								if(mallEntity.getUseCoupon()==1){
//								// 减免券
//									if (dfcard.getAdduser() == 0) {
//										// 不允许叠加使用
//										if (balanceMoneyBili >= dfcard.getCashLeastCost()) {
//											//商品数据
//											//减免金额分摊
//											couponCount1++;
//											if(couponCount1==couponCount){
//												discountConponMoneyByMall=BigDecimalUtil.sub(reduceCose, couponCountFentan);
//											}else{
//												discountConponMoneyByMall=formatNumber(BigDecimalUtil.mul(BigDecimalUtil.div(mallEntity.getTotalMoneyAll(), balanceMoneyBili),reduceCose));  //分摊金额
//												mallEntity.setDiscountConponMoney(discountConponMoneyByMall);
//											}
//											couponCountFentan=couponCountFentan+discountConponMoneyByMall;
//
//											mallEntity.setDiscountConponMoney(discountConponMoneyByMall);
//											mallEntity.setBalanceMoney(formatNumber(BigDecimalUtil.sub(mallEntity.getTotalMoneyAll(),mallEntity.getDiscountConponMoney())));  //支付金额
//											mallEntity.setUnitPrice(formatNumber(BigDecimalUtil.div(mallEntity.getBalanceMoney(),mallEntity.getNumber())));
//											mallEntity.setCanUseConpon(1); //能使用优惠券
//											mallEntity.setTotalMoneyAll(formatNumber(BigDecimalUtil.sub(mallEntity.getTotalMoneyAll(),mallEntity.getDiscountConponMoney())));
//											mallList.put(mallEntity.getMallId(), mallEntity);
//
//											//门店订单数据
//											canUseConponByShopId=1;  //门店商品是否使用优惠券标示
//											discountConponMoneyByShopId=formatNumber(BigDecimalUtil.add(discountConponMoneyByShopId, mallEntity.getDiscountConponMoney()));  //该门店商品会员优惠金额
//
//											//总订单数据
//											discountConponMoneyByAll=formatNumber(BigDecimalUtil.add(discountConponMoneyByAll,mallEntity.getDiscountConponMoney()));
//										}
//									} else {
//										Integer num = 0;
//										Integer shiNum=0;
//										// 满足使用条件
//										if (balanceMoneyBili >= dfcard.getCashLeastCost()) {
//											if (dfcard.getCashLeastCost() > 0) {
//												num =BigDecimalUtil.divInteger(balanceMoneyBili, dfcard.getCashLeastCost()); // 能使用优惠券数量
//											}
//											// 允许叠加使用
//											if (dfcg.size() > 0) {
//												for (int i = 0; i < dfcg.size(); i++) {
//													if(i<=num){
//														codesByShopId += dfcg.get(i).get("code") + ",";
//														shiNum=i;
//													}
//												}
//												num = shiNum;
//												couponNumByShopId=num;
//												mallShopEntity.setCouponNum(num);  //优惠券使用数量
//												reduceCose=formatNumber(BigDecimalUtil.mul(dfcard.getReduceCost(),num));
//												//减免金额分摊
//												couponCount1++;
//												if(couponCount1==couponCount){
//													mallEntity.setDiscountConponMoney(BigDecimalUtil.sub(reduceCose, couponCountFentan));
//												}else{
//													discountConponMoneyByMall=formatNumber(BigDecimalUtil.mul(BigDecimalUtil.div(mallEntity.getTotalMoneyAll(), balanceMoneyBili),reduceCose));  //分摊金额
//													mallEntity.setDiscountConponMoney(discountConponMoneyByMall);
//												}
//												couponCountFentan=couponCountFentan+discountConponMoneyByMall;
//
//												//商品数据
//												mallEntity.setDiscountConponMoney(discountConponMoneyByMall);
//												mallEntity.setDiscountConponMoney(discountConponMoneyByMall);
//												mallEntity.setBalanceMoney(formatNumber(BigDecimalUtil.sub(mallEntity.getTotalMoneyAll(),mallEntity.getDiscountConponMoney())));  //支付金额
//												mallEntity.setUnitPrice(formatNumber(BigDecimalUtil.div(mallEntity.getBalanceMoney(),mallEntity.getNumber())));
//												mallEntity.setCanUseConpon(1); //能使用优惠券
//												mallEntity.setTotalMoneyAll(formatNumber(BigDecimalUtil.sub(mallEntity.getTotalMoneyAll(), mallEntity.getDiscountConponMoney())));
//												mallList.put(mallEntity.getMallId(), mallEntity);
//
//												//门店订单数据
//												canUseConponByShopId=1;  //门店商品是否使用优惠券标示
//												discountConponMoneyByShopId=formatNumber(BigDecimalUtil.add(discountConponMoneyByShopId, mallEntity.getDiscountConponMoney()));  //该门店商品会员优惠金额
//
//												//总订单数据
//												discountConponMoneyByAll=formatNumber(BigDecimalUtil.add(discountConponMoneyByAll,mallEntity.getDiscountConponMoney()));
//											}
//										}
//									}
//								}
//						}
//					}
//				}
//			}
//		}
//		//<!-------------------end----------------------------->
//		//保存门店优惠信息
//		mallShopEntity.setDiscountMemberMoney(discountMemberMoneyByShopId); //会员优惠
//		mallShopEntity.setDiscountConponMoney(discountConponMoneyByShopId);  //优惠券优惠金额
//		mallShopEntity.setCodes(codesByShopId);
//		mallShopEntity.setCanUseConpon(canUseConponByShopId);  //门店商品是否使用优惠券标示
//		mallShopEntity.setCouponNum(couponNumByShopId);
//		mallShopMap.put(mallShopEntity.getShopId(), mallShopEntity);
//
//	}
//		//保存总订单优惠信息
//		mallAllEntity.setMallShops(mallShopMap);
//		mallAllEntity.setDiscountMemberMoney(discountMemberMoneyByAll);
//		mallAllEntity.setDiscountConponMoney(discountConponMoneyByAll);
//
//		//<!-----------------粉币计算start---------------------------->
//		if(isMemberCard && mallAllEntity.getUseFenbi()==1){
//			Integer fcount=0; //能抵扣的粉币商品数量
//			Integer fcount1=0; //能抵扣的粉币商品数量
//			Double fenbiFenTanAll=0.0;
//			double fenbiMoney=currencyCount(0.0, member.getFansCurrency());
//			Double discountfenbiMoneyByShopId=0.0;
//			Double balanceMoneyByShopId=0.0;
//			if(fenbiMoney>0){
//				Double fenbiBanlance=0.0;  //订单金额
//				for (MallShopEntity mallShopEntity : mallShopMap.values()) {
//					Map<Integer,MallEntity> mallEntityMap=mallShopEntity.getMalls();
//					for (MallEntity mallEntity : mallEntityMap.values()) {
//						//判断能使用粉币的商品信息
//						if(mallEntity.getUseFenbi()==1){
//							fenbiBanlance=formatNumber(BigDecimalUtil.add(fenbiBanlance,mallEntity.getTotalMoneyAll()));
//							fcount++;
//						}
//					}
//				}
//				if(fenbiBanlance>=10)
//				//计算粉币分摊结果
//				for (MallShopEntity mallShopEntity : mallShopMap.values()) {
//					Map<Integer,MallEntity> mallEntityMap=mallShopEntity.getMalls();
//					for (MallEntity mallEntity : mallEntityMap.values()) {
//						if(mallEntity.getUseFenbi()==1){
//							Double fenbiFenTan=0.0;
//							//处理误差
//							if(fcount==fcount1+1){
//								if(fenbiBanlance>fenbiMoney){
//									fenbiFenTan=formatNumber(BigDecimalUtil.sub(fenbiMoney, fenbiFenTanAll));
//								}else{
//									fenbiFenTan=formatNumber(BigDecimalUtil.sub(fenbiBanlance,fenbiFenTanAll));
//								}
//							}else{
//								if(fenbiBanlance>=fenbiMoney){
//									//消费金额大于粉币金额
//									fenbiFenTan=formatNumber(BigDecimalUtil.mul(BigDecimalUtil.div(mallEntity.getTotalMoneyAll(), fenbiBanlance), fenbiMoney));
//								}else{
//									fenbiFenTan=formatNumber(BigDecimalUtil.mul(BigDecimalUtil.div(mallEntity.getTotalMoneyAll(), fenbiBanlance), fenbiBanlance));
//								}
//								fenbiFenTanAll=formatNumber(BigDecimalUtil.add(fenbiFenTanAll,fenbiFenTan));
//							}
//							//商品数据
//							mallEntity.setDiscountfenbiMoney(fenbiFenTan);
//							Double fenbiNum = deductFenbi(fenbiFenTan);
//							mallEntity.setFenbiNum(fenbiNum);
//							mallEntity.setCanUsefenbi(1);
//							mallEntity.setBalanceMoney(formatNumber(BigDecimalUtil.sub(mallEntity.getTotalMoneyAll(), fenbiFenTan)));
//							mallEntity.setTotalMoneyAll(formatNumber(BigDecimalUtil.sub(mallEntity.getTotalMoneyAll(),fenbiFenTan)));
//							mallEntityMap.put(mallEntity.getMallId(), mallEntity);
//
//							//门店订单数据
//							discountfenbiMoneyByShopId=formatNumber(BigDecimalUtil.add(discountfenbiMoneyByShopId, mallEntity.getDiscountfenbiMoney()));
//							balanceMoneyByShopId=formatNumber(BigDecimalUtil.add(balanceMoneyByShopId,mallEntity.getBalanceMoney()));
//
//							//总订单数据
//							canUsefenbiByAll=1;
//
//							fcount1++;
//						}else{
//							balanceMoneyByShopId=formatNumber(BigDecimalUtil.add(balanceMoneyByShopId,mallEntity.getTotalMoneyAll()));
//						}
//				   }
//					mallShopEntity.setDiscountfenbiMoney(discountfenbiMoneyByShopId);
//					mallShopEntity.setBalanceMoney(balanceMoneyByShopId);
//					mallShopEntity.setMalls(mallEntityMap);
//					mallShopMap.put(mallShopEntity.getShopId(), mallShopEntity);
//				}
//				if(fenbiMoney>0){
//					Double fenbiNumByAll = deductFenbi(discountfenbiMoneyByShopId);
//					mallAllEntity.setFenbiNum(fenbiNumByAll);
//					mallAllEntity.setDiscountfenbiMoney(discountfenbiMoneyByShopId);
//				}
//				mallAllEntity.setMallShops(mallShopMap);
//
//				mallAllEntity.setCanUsefenbi(canUsefenbiByAll);
//			}
//		}
//		//<!-----------------粉币计算end---------------------------->
//
//
//		//<!-----------------积分计算start---------------------------->
//		if(isMemberCard && mallAllEntity.getUserJifen()==1){
//			Double discountJifenMoneyByShopId=0.0;
//			Double jifenBanlance=0.0;  //订单金额
//			Double balanceMoneyByShopId=0.0;
//			Integer jcount=0; //能抵扣的粉币商品数量
//			Integer jcount1=0; //能抵扣的粉币商品数量
//			Double jifenFenTanAll=0.0;
//
//			for (MallShopEntity mallShopEntity : mallShopMap.values()) {
//				Map<Integer,MallEntity> mallEntityMap=mallShopEntity.getMalls();
//				for (MallEntity mallEntity : mallEntityMap.values()) {
//					//判断能使用粉币的商品信息
//					if(mallEntity.getUserJifen()==1){
//						jifenBanlance=formatNumber(BigDecimalUtil.add(jifenBanlance,mallEntity.getTotalMoneyAll()));
//						jcount++;
//					}
//				}
//			}
//			Double jifenMoney=integralCount(jifenBanlance, member.getIntegral().doubleValue(), member.getBusid());
//
//			if(jifenMoney>0){
//				//计算积分分摊结果
//				for (MallShopEntity mallShopEntity : mallShopMap.values()) {
//					Map<Integer,MallEntity> mallEntityMap=mallShopEntity.getMalls();
//					for (MallEntity mallEntity : mallEntityMap.values()) {
//						if(mallEntity.getUserJifen()==1){
//							Double jifenFenTan=0.0;
//							if(jcount==jcount1+1){
//								if(jifenBanlance>=jifenMoney){
//									//消费金额大于粉币金额
//									jifenFenTan=formatNumber(BigDecimalUtil.sub(jifenMoney, jifenFenTanAll));
//								}else{
//									jifenFenTan=formatNumber(BigDecimalUtil.sub(jifenBanlance,jifenFenTanAll));
//								}
//							}else{
//								if(jifenBanlance>=jifenMoney){
//									//消费金额大于粉币金额
//									jifenFenTan=formatNumber(BigDecimalUtil.mul(BigDecimalUtil.div(mallEntity.getTotalMoneyAll(), jifenBanlance), jifenMoney));
//								}else{
//									jifenFenTan=formatNumber(BigDecimalUtil.mul(BigDecimalUtil.div(mallEntity.getTotalMoneyAll(), jifenBanlance), jifenBanlance));
//								}
//								jifenFenTanAll=formatNumber(BigDecimalUtil.add(jifenFenTanAll,jifenFenTan));
//							}
//							//商品数据
//							mallEntity.setDiscountjifenMoney(jifenFenTan);
//							Double jifenNum = deductJifen(jifenFenTan, member.getBusid());
//							mallEntity.setJifenNum(jifenNum);
//							mallEntity.setCanUseJifen(1);
//							mallEntity.setBalanceMoney(formatNumber(BigDecimalUtil.sub(mallEntity.getTotalMoneyAll(),jifenFenTan)));
//							mallEntity.setTotalMoneyAll(formatNumber(BigDecimalUtil.sub(mallEntity.getTotalMoneyAll(), jifenFenTan)));
//							mallEntityMap.put(mallEntity.getMallId(), mallEntity);
//
//							//门店订单数据
//							discountJifenMoneyByShopId=formatNumber(BigDecimalUtil.add(discountJifenMoneyByShopId, mallEntity.getDiscountjifenMoney()));
//							balanceMoneyByShopId=formatNumber(BigDecimalUtil.add(balanceMoneyByShopId,mallEntity.getBalanceMoney()));
//
//							//总订单数据
//							canUseJifenByAll=1;
//							jcount1++;
//						}else{
//							balanceMoneyByShopId=formatNumber(BigDecimalUtil.add(balanceMoneyByShopId,mallEntity.getTotalMoneyAll()));
//						}
//				   }
//					mallShopEntity.setDiscountjifenMoney(discountJifenMoneyByShopId);
//					mallShopEntity.setBalanceMoney(balanceMoneyByShopId);
//					mallShopEntity.setMalls(mallEntityMap);
//					mallShopMap.put(mallShopEntity.getShopId(), mallShopEntity);
//				}
//				if(jifenMoney>0){
//					Integer jifenNumByAll = deductJifen(jifenMoney, member.getBusid()).intValue();
//					mallAllEntity.setJifenNum(jifenNumByAll);
//					mallAllEntity.setDiscountjifenMoney(jifenMoney);
//				}
//				mallAllEntity.setMallShops(mallShopMap);
//				mallAllEntity.setCanUseJifen(canUseJifenByAll);
//			}
//		}
//
//		Double balanceMoneyByAll=0.0;
//		for (MallShopEntity mallShopEntity : mallShopMap.values()){
//			Double balanceMoneyByShopId=0.0;
//			Map<Integer,MallEntity> mallEntitys=mallShopEntity.getMalls();
//			for (MallEntity mallEntity : mallEntitys.values()) {
//				//未任何优惠商品支付金额重新赋值一次
//				if(mallEntity.getBalanceMoney()<=0){
//					mallEntity.setBalanceMoney(mallEntity.getTotalMoneyAll());
//				}
//				balanceMoneyByShopId=formatNumber(BigDecimalUtil.add(balanceMoneyByShopId, mallEntity.getBalanceMoney()));
//			}
//			mallShopEntity.setBalanceMoney(balanceMoneyByShopId);
//			balanceMoneyByAll=formatNumber(BigDecimalUtil.add(balanceMoneyByAll, mallShopEntity.getBalanceMoney()));
//			mallShopMap.put(mallShopEntity.getShopId(), mallShopEntity);
//		}
//		mallAllEntity.setBalanceMoney(balanceMoneyByAll);
//
//		Long end=new Date().getTime();
//		System.out.println("用时:"+(end-start)+"毫秒");
//
//		return mallAllEntity;
//	}
//
//	// <!-----------------积分计算end---------------------------->
//
//	/**
//	 * 数字处理
//	 *
//	 * @param number
//	 * @return
//	 */
//	public Double formatNumber(Double number) {
//		DecimalFormat df = new DecimalFormat("######0.00");
//		return CommonUtil.toDouble(df.format(number));
//	}
//
//	/**
//	 * 粉币计算
//	 *
//	 * @param totalMoney
//	 *            能抵抗消费金额
//	 * @param fans_currency
//	 *            粉币值
//	 * @param ratio
//	 *            比例 字典表1058 Map集合key为1
//	 * @return 返回兑换金额
//	 */
//	public Double currencyCount(Double totalMoney, Double fans_currency) {
//		try {
//			Map<String, Object> dict = dictService.getDict("1058");
//			Double ratio = CommonUtil.toDouble(dict.get("1"));
//			if (fans_currency < ratio * 10) {
//				return 0.0;
//			}
//			Integer money = new Double(fans_currency / ratio / 10).intValue();
//			if (CommonUtil.isEmpty(totalMoney) || totalMoney == 0) {
//				return new Double(money * 10);
//			} else {
//				if (totalMoney >= money * 10) {
//					return new Double(money * 10);
//				} else {
//					return totalMoney;
//				}
//			}
//		} catch (Exception e) {
//			LOG.error("计算粉币抵扣异常");
//			e.printStackTrace();
//		}
//		return 0.0;
//	}
//
//	/**
//	 * 金额计算使用粉币数量
//	 *
//	 * @param jifenMoney
//	 * @param busId
//	 * @return
//	 */
//	public Double deductFenbi(Double fenbiMoney) {
//		Map<String, Object> dict = dictService.getDict("1058");
//		Double ratio = CommonUtil.toDouble(dict.get("1"));
//		Double fenbi = fenbiMoney * ratio;
//		return formatNumber(fenbi);
//	}
//
//	/**
//	 * 金额计算使用积分数量
//	 *
//	 * @param jifenMoney
//	 * @param busId
//	 * @return
//	 */
//	public Double deductJifen(Double jifenMoney, int busId) {
//		PublicParameterSet pps = publicParameterSetMapper.findBybusId(busId);
//		if (CommonUtil.isEmpty(pps)) {
//			return 0.0;
//		}
//		Double jifen = jifenMoney / pps.getChangemoney()
//				* pps.getIntegralratio();
//		return formatNumber(jifen);
//	}
//
//	/**
//	 * 积分计算
//	 *
//	 * @param totalMoney
//	 *            能抵抗消费金额
//	 * @param integral
//	 *            积分
//	 * @param ps
//	 * @return
//	 */
//	public Double integralCount(Double totalMoney, Double integral, int busId) {
//		try {
//			PublicParameterSet ps = publicParameterSetMapper.findBybusId(busId);
//			if (CommonUtil.isEmpty(ps)) {
//				return 0.0;
//			}
//			double startMoney = ps.getStartmoney();
//			double integralratio = ps.getIntegralratio();
//			double changMoney = ps.getChangemoney();
//			if (integralratio <= 0) {
//				return 0.0;
//			}
//
//			// 积分启兑
//			double integralNum = startMoney * integralratio;
//			if (integral < integralNum) {
//				return 0.0;
//			}
//
//			if (CommonUtil.isNotEmpty(totalMoney)) {
//				// 订单金额小于订单启兑金额
//				if (totalMoney < changMoney) {
//					return 0.0;
//				}
//				Integer money = new Double(integral / integralratio).intValue();
//				if (totalMoney >= money) {
//					return new Double(money);
//				} else {
//					return totalMoney;
//				}
//			} else {
//				Integer money = new Double(integral / integralratio).intValue();
//				return new Double(money);
//			}
//		} catch (Exception e) {
//			LOG.error("计算积分抵扣异常");
//			e.printStackTrace();
//		}
//		return 0.0;
//	}
//}