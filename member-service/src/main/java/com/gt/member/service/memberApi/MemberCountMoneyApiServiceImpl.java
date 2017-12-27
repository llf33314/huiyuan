package com.gt.member.service.memberApi;

import com.gt.entityBo.*;
import com.gt.member.dao.*;
import com.gt.member.entity.*;
import com.gt.member.service.common.dict.DictService;
import com.gt.member.service.common.membercard.MemberCommonService;
import com.gt.member.util.BigDecimalUtil;
import com.gt.member.util.CommonUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

/**
 * Created by Administrator on 2017/8/2 0002.
 */
@Service
public class MemberCountMoneyApiServiceImpl implements MemberCountMoneyApiService {

    private static final Logger LOG = Logger.getLogger( MemberCountMoneyApiServiceImpl.class );

    @Autowired
    private MemberEntityDAO memberMapper;

    @Autowired
    private MemberCardDAO cardMapper;

    @Autowired
    private MemberGiveruleDAO giveRuleMapper;

    @Autowired
    private WxCardReceiveDAO wxCardReceiveMapper;

    @Autowired
    private WxCardDAO wxCardMapper;

    @Autowired
    private DuofenCardGetDAO duofenCardGetMapper;

    @Autowired
    private DuofenCardDAO duofenCardMapper;

    @Autowired
    private DictService dictService;

    @Autowired
    private PublicParametersetDAO publicParameterSetMapper;

    @Autowired
    private MemberGradetypeDAO memberGradetypeDAO;

    @Autowired
    private MemberDateDAO memberDateMapper;

    @Autowired
    private MemberCommonService memberCommonService;


    public MallAllEntity mallSkipShopCount( MallAllEntity mallAllEntity ) {
	if ( mallAllEntity.getUserLeague() == 1 ) {
	    return leagueCount( mallAllEntity );
	}
	Long start = new Date().getTime();
	boolean isMemberCard = false;  //用来标示该粉丝是会员
	MemberCard card = null;
	MemberEntity memberEntity = memberMapper.selectById( mallAllEntity.getMemberId() );
	Double memberDiscount = 1.0;  //会员卡折扣
	if ( CommonUtil.isNotEmpty( memberEntity ) && CommonUtil.isNotEmpty( memberEntity.getMcId() ) ) {
	    card = cardMapper.selectById( memberEntity.getMcId() );
	    if ( card.getCardStatus() == 0 ) {  //如果会员卡状态不为0标示该会员卡被禁用了
		isMemberCard = true;
		// 折扣卡
		//判断是否是会员日
		if ( card.getCtId() == 2 ) {
		    MemberDate memberDate = memberCommonService.findMemeberDate( card.getBusId(), card.getCtId() );
		    if ( CommonUtil.isNotEmpty( memberDate ) ) {
			memberDiscount = BigDecimalUtil.div( memberDate.getDiscount().doubleValue(), 10.0 );  //会员日折扣
		    }
		    MemberGiverule gr = giveRuleMapper.selectById( card.getGrId() );
		    memberDiscount = BigDecimalUtil.mul( BigDecimalUtil.div( gr.getGrDiscount(), 100.0 ), memberDiscount );  //会员折扣
		}
	    }
	}
	//所有门店使用卡券信息
	Map< Integer,MallShopEntity > mallShopMap = mallAllEntity.getMallShops();
	Double discountMemberMoneyByAll = 0.0; //总订单会员优惠券金额
	Double discountConponMoneyByAll = 0.0;   //优惠券券优惠金额
	Integer canUsefenbiByAll = 0;  //是否能用粉币
	Integer canUseJifenByAll = 0;  //是否能用积分
	boolean isUseDisCount = false;  //实付使用折扣
	WxCardReceive wxCardReceive = null;
	WxCard wxcard = null;
	DuofenCardGet dfget = null;
	DuofenCard dfcard = null;
	List< Map< String,Object > > dfcg = null;

	//计算门店下使用了优惠券商品信息
	for ( MallShopEntity mallShopEntity : mallShopMap.values() ) {
	    Double discountMemberMoneyByShopId = 0.0; //门店会员优惠券金额
	    String codesByShopId = "";  //门店使用优惠券code值 用来核销卡券 不存在set
	    Integer couponNumByShopId = 1;  //门店使用优惠券数量
	    Integer canUseConponByShopId = 0;  //门店是否能用优惠券
	    Double discountConponMoneyByShopId = 0.0;   //门店优惠券优惠券金额
	    Map< Object,MallEntity > mallList = mallShopEntity.getMalls();  //门店下商品信息
	    Integer useCoupon = mallShopEntity.getUseCoupon(); //是否使用了优惠券
	    Integer couponType = mallShopEntity.getCouponType();  //优惠券类型 0微信 1多粉
	    Integer coupondId = mallShopEntity.getCoupondId();   //优惠券id
	    Double discountLinshi = 0.0;  //折扣优惠券的折扣值 0到1
	    Double reduceCose = 0.0;
	    if ( useCoupon == 1 ) {
		if ( couponType == 0 ) {
		    // 微信优惠券
		    wxCardReceive = wxCardReceiveMapper.selectById( coupondId );
		    wxcard = wxCardMapper.selectByCardId( wxCardReceive.getCardId() );
		    if ( "DISCOUNT".equals( wxcard.getCardType() ) ) {
			isUseDisCount = true;
			discountLinshi = wxcard.getDiscount() / 10;
		    } else {
			reduceCose = wxcard.getReduceCost();
		    }
		    codesByShopId = wxCardReceive.getUserCardCode();
		} else if ( couponType == 1 ) {
		    // 多粉优惠券
		    dfget = duofenCardGetMapper.selectById( coupondId );
		    dfcard = duofenCardMapper.selectById( dfget.getCardId() );
		    if ( dfcard.getCardType() == 0 ) {
			isUseDisCount = true;
			discountLinshi = dfcard.getDiscount() / 10;
		    }
		    if ( dfcard.getAddUser() == 1 ) {
			// 允许叠加使用
			dfcg = duofenCardGetMapper.findByCardId( dfcard.getId(), mallAllEntity.getMemberId(), 0 );
		    }
		    codesByShopId = dfget.getCode();
		}
	    }

	    //<!-------------------start----------------------------->
	    if ( useCoupon == 0 && isMemberCard && card.getCtId() == 2 ) {
		//未使用优惠券 是折扣卡会员 计算优惠
		for ( MallEntity mallEntity : mallList.values() ) {
		    if ( mallEntity.getUserCard() == 1 ) {
			//会员卡优惠
			Double discountMemberMoneyByOne = formatNumber( BigDecimalUtil.mul( mallEntity.getTotalMoneyOne(), BigDecimalUtil.sub( 1, memberDiscount ) ) );
			Double unitPrice = formatNumber( BigDecimalUtil.sub( mallEntity.getTotalMoneyOne(), discountMemberMoneyByOne ) );

			//商品数据
			mallEntity.setUnitPrice( unitPrice );
			mallEntity.setDiscountMemberMoney( formatNumber( BigDecimalUtil.mul( discountMemberMoneyByOne, mallEntity.getNumber() ) ) );
			mallEntity.setBalanceMoney( formatNumber( BigDecimalUtil.mul( unitPrice, mallEntity.getNumber() ) ) );  //商品订单总金额
			mallEntity.setTotalMoneyAll( formatNumber( BigDecimalUtil.sub( mallEntity.getTotalMoneyAll(), mallEntity.getDiscountMemberMoney() ) ) );
			mallEntity.setCanUserCard( 1 );
			mallList.put( mallEntity.getMallId(), mallEntity );  //数据处理完归还

			//门店订单数据
			discountMemberMoneyByShopId = formatNumber(
					BigDecimalUtil.add( discountMemberMoneyByShopId, BigDecimalUtil.mul( discountMemberMoneyByOne, mallEntity.getNumber() ) ) );  //该门店商品会员优惠金额

			//总订单数据
			discountMemberMoneyByAll = formatNumber(
					BigDecimalUtil.add( discountMemberMoneyByAll, BigDecimalUtil.mul( discountMemberMoneyByOne, mallEntity.getNumber() ) ) );
		    }
		}

	    } else if ( useCoupon == 1 ) {
		if ( isUseDisCount ) {
		    //折扣优惠券处理业务
		    for ( MallEntity mallEntity : mallList.values() ) {
			//挑选出能使用优惠券的商品
			if ( mallEntity.getUseCoupon() == 1 ) {
			    //单个商品优惠券金额
			    Double discountConponMoneyByOne = formatNumber( BigDecimalUtil.mul( mallEntity.getTotalMoneyOne(), 1 - discountLinshi ) );
			    Double unitPrice = formatNumber( mallEntity.getTotalMoneyOne() - discountConponMoneyByOne );

			    //商品数据
			    mallEntity.setUnitPrice( unitPrice );  //当个商品价格
			    mallEntity.setDiscountConponMoney( formatNumber( BigDecimalUtil.mul( discountConponMoneyByOne, mallEntity.getNumber() ) ) );
			    mallEntity.setBalanceMoney( formatNumber( BigDecimalUtil.mul( unitPrice, mallEntity.getNumber() ) ) );
			    mallEntity.setTotalMoneyAll( formatNumber( BigDecimalUtil.sub( mallEntity.getTotalMoneyAll(), mallEntity.getDiscountConponMoney() ) ) );
			    mallEntity.setCanUseConpon( 1 ); //能使用优惠券
			    mallList.put( mallEntity.getMallId(), mallEntity ); //归还到分离的数据中去

			    //门店订单数据
			    canUseConponByShopId = 1;  //门店商品是否使用优惠券标示
			    discountConponMoneyByShopId = formatNumber( BigDecimalUtil.add( discountConponMoneyByShopId, mallEntity.getDiscountConponMoney() ) );

			    //总订单数据
			    discountConponMoneyByAll = formatNumber( BigDecimalUtil.add( discountConponMoneyByAll, mallEntity.getDiscountConponMoney() ) );
			}
		    }
		} else {
		    //减免券处理方式  1如果存在折扣卡 先处理折扣卡优惠券  2再处理优惠券减免
		    for ( MallEntity mallEntity : mallList.values() ) {
			//先处理会员卡
			if ( mallEntity.getUserCard() == 1 ) {
			    if ( isMemberCard && card.getCtId() == 2 ) {
				//商品会员卡优惠券
				Double discountMemberMoneyByOne = formatNumber( BigDecimalUtil.mul( mallEntity.getTotalMoneyOne(), ( 1 - memberDiscount ) ) );
				Double unitPrice = formatNumber( BigDecimalUtil.sub( mallEntity.getTotalMoneyOne(), discountMemberMoneyByOne ) );

				//商品数据
				mallEntity.setUnitPrice( unitPrice );
				mallEntity.setDiscountMemberMoney( formatNumber( BigDecimalUtil.mul( discountMemberMoneyByOne, mallEntity.getNumber() ) ) );
				mallEntity.setBalanceMoney( formatNumber( BigDecimalUtil.mul( unitPrice, mallEntity.getNumber() ) ) );  //商品订单总金额
				mallEntity.setCanUserCard( 1 );
				mallEntity.setTotalMoneyAll( formatNumber( BigDecimalUtil.sub( mallEntity.getTotalMoneyAll(), mallEntity.getDiscountMemberMoney() ) ) );

				//门店订单数据
				discountMemberMoneyByShopId = formatNumber( BigDecimalUtil.add( discountMemberMoneyByShopId, mallEntity.getDiscountMemberMoney() ) );  //该门店商品会员优惠金额

				//总订单数据
				discountMemberMoneyByAll = formatNumber( BigDecimalUtil.add( discountMemberMoneyByAll, +mallEntity.getDiscountMemberMoney() ) );

			    }
			    mallList.put( mallEntity.getMallId(), mallEntity );  //数据处理完归还
			}
		    }
		    //<!-------会员卡优惠券已结束-------->

		    //<!---------减免券start------------>
		    Double balanceMoneyBili = 0.0;//门店订单支付金额
		    Integer couponCount = 0;
		    Integer couponCount1 = 0;
		    Double couponCountFentan = 0.0;
		    for ( MallEntity mallEntity : mallList.values() ) {  //商品信息
			//挑选出能使用优惠券的商品
			if ( mallEntity.getUseCoupon() == 1 ) {
			    balanceMoneyBili = formatNumber( BigDecimalUtil.add( balanceMoneyBili, mallEntity.getTotalMoneyAll() ) );
			    couponCount++;
			}
		    }

		    // 计算微信减免券优惠
		    Double discountConponMoneyByMall = 0.0;
		    if ( useCoupon == 1 ) {
			//<!-------start-------->
			if ( couponType == 0 ) {
			    for ( MallEntity mallEntity : mallList.values() ) {
				if ( mallEntity.getUseCoupon() == 1 ) {
				    if ( balanceMoneyBili >= wxcard.getCashLeastCost() ) {
					//商品数据
					//减免金额分摊
					couponCount1++;
					if ( couponCount1 == couponCount ) {
					    mallEntity.setDiscountConponMoney( BigDecimalUtil.sub( reduceCose, couponCountFentan ) );
					} else {
					    discountConponMoneyByMall = formatNumber(
							    BigDecimalUtil.mul( BigDecimalUtil.div( mallEntity.getTotalMoneyAll(), balanceMoneyBili ), reduceCose ) );  //分摊金额
					    mallEntity.setDiscountConponMoney( discountConponMoneyByMall );
					}
					couponCountFentan = couponCountFentan + discountConponMoneyByMall;

					mallEntity.setBalanceMoney( formatNumber( BigDecimalUtil.sub( mallEntity.getTotalMoneyAll(), mallEntity.getDiscountConponMoney() ) ) );
					mallEntity.setUnitPrice( formatNumber( BigDecimalUtil.div( mallEntity.getBalanceMoney(), mallEntity.getNumber() ) ) );
					mallEntity.setCanUseConpon( 1 ); //能使用优惠券
					mallEntity.setTotalMoneyAll( formatNumber( BigDecimalUtil.sub( mallEntity.getTotalMoneyAll(), mallEntity.getDiscountConponMoney() ) ) );
					mallList.put( mallEntity.getMallId(), mallEntity );

					//门店订单数据
					canUseConponByShopId = 1;  //门店商品是否使用优惠券标示
					discountConponMoneyByShopId = formatNumber(
							BigDecimalUtil.add( discountConponMoneyByShopId, mallEntity.getDiscountConponMoney() ) );  //该门店商品会员优惠金额

					//总订单数据
					discountConponMoneyByAll = formatNumber( BigDecimalUtil.add( discountConponMoneyByAll, mallEntity.getDiscountConponMoney() ) );

				    }
				}
			    }
			    //<!-------end-------->

			} else if ( couponType == 1 ) {
			    //计算多粉优惠券减免券优惠
			    reduceCose = dfcard.getReduceCost();
			    for ( MallEntity mallEntity : mallList.values() ) {
				if ( mallEntity.getUseCoupon() == 1 ) {
				    // 减免券
				    if ( dfcard.getAddUser() == 0 ) {
					// 不允许叠加使用
					if ( balanceMoneyBili >= dfcard.getCashLeastCost() ) {
					    //商品数据
					    //减免金额分摊
					    couponCount1++;
					    if ( couponCount1 == couponCount ) {
						discountConponMoneyByMall = BigDecimalUtil.sub( reduceCose, couponCountFentan );
					    } else {
						discountConponMoneyByMall = formatNumber(
								BigDecimalUtil.mul( BigDecimalUtil.div( mallEntity.getTotalMoneyAll(), balanceMoneyBili ), reduceCose ) );  //分摊金额
						mallEntity.setDiscountConponMoney( discountConponMoneyByMall );
					    }
					    couponCountFentan = couponCountFentan + discountConponMoneyByMall;

					    mallEntity.setDiscountConponMoney( discountConponMoneyByMall );
					    mallEntity.setBalanceMoney(
							    formatNumber( BigDecimalUtil.sub( mallEntity.getTotalMoneyAll(), mallEntity.getDiscountConponMoney() ) ) );  //支付金额
					    mallEntity.setUnitPrice( formatNumber( BigDecimalUtil.div( mallEntity.getBalanceMoney(), mallEntity.getNumber() ) ) );
					    mallEntity.setCanUseConpon( 1 ); //能使用优惠券
					    mallEntity.setTotalMoneyAll( formatNumber( BigDecimalUtil.sub( mallEntity.getTotalMoneyAll(), mallEntity.getDiscountConponMoney() ) ) );
					    mallList.put( mallEntity.getMallId(), mallEntity );

					    //门店订单数据
					    canUseConponByShopId = 1;  //门店商品是否使用优惠券标示
					    discountConponMoneyByShopId = formatNumber(
							    BigDecimalUtil.add( discountConponMoneyByShopId, mallEntity.getDiscountConponMoney() ) );  //该门店商品会员优惠金额

					    //总订单数据
					    discountConponMoneyByAll = formatNumber( BigDecimalUtil.add( discountConponMoneyByAll, mallEntity.getDiscountConponMoney() ) );
					}
				    } else {
					Integer num = 0;
					Integer shiNum = 0;
					// 满足使用条件
					if ( balanceMoneyBili >= dfcard.getCashLeastCost() ) {
					    if ( dfcard.getCashLeastCost() > 0 ) {
						num = BigDecimalUtil.divInteger( balanceMoneyBili, dfcard.getCashLeastCost() ); // 能使用优惠券数量
					    }
					    // 允许叠加使用
					    if ( dfcg.size() > 0 ) {
						for ( int i = 0; i < dfcg.size(); i++ ) {
						    if ( i <= num ) {
							codesByShopId += dfcg.get( i ).get( "code" ) + ",";
							shiNum = i;
						    }
						}
						num = shiNum;
						couponNumByShopId = num;
						mallShopEntity.setCouponNum( num );  //优惠券使用数量
						reduceCose = formatNumber( BigDecimalUtil.mul( dfcard.getReduceCost(), num ) );
						//减免金额分摊
						couponCount1++;
						if ( couponCount1 == couponCount ) {
						    mallEntity.setDiscountConponMoney( BigDecimalUtil.sub( reduceCose, couponCountFentan ) );
						} else {
						    discountConponMoneyByMall = formatNumber(
								    BigDecimalUtil.mul( BigDecimalUtil.div( mallEntity.getTotalMoneyAll(), balanceMoneyBili ),
										    reduceCose ) );  //分摊金额
						    mallEntity.setDiscountConponMoney( discountConponMoneyByMall );
						}
						couponCountFentan = couponCountFentan + discountConponMoneyByMall;

						//商品数据
						mallEntity.setDiscountConponMoney( discountConponMoneyByMall );
						mallEntity.setDiscountConponMoney( discountConponMoneyByMall );
						mallEntity.setBalanceMoney(
								formatNumber( BigDecimalUtil.sub( mallEntity.getTotalMoneyAll(), mallEntity.getDiscountConponMoney() ) ) );  //支付金额
						mallEntity.setUnitPrice( formatNumber( BigDecimalUtil.div( mallEntity.getBalanceMoney(), mallEntity.getNumber() ) ) );
						mallEntity.setCanUseConpon( 1 ); //能使用优惠券
						mallEntity.setTotalMoneyAll(
								formatNumber( BigDecimalUtil.sub( mallEntity.getTotalMoneyAll(), mallEntity.getDiscountConponMoney() ) ) );
						mallList.put( mallEntity.getMallId(), mallEntity );

						//门店订单数据
						canUseConponByShopId = 1;  //门店商品是否使用优惠券标示
						discountConponMoneyByShopId = formatNumber(
								BigDecimalUtil.add( discountConponMoneyByShopId, mallEntity.getDiscountConponMoney() ) );  //该门店商品会员优惠金额

						//总订单数据
						discountConponMoneyByAll = formatNumber( BigDecimalUtil.add( discountConponMoneyByAll, mallEntity.getDiscountConponMoney() ) );
					    }
					}
				    }
				}
			    }
			}
		    }
		}
	    }
	    //<!-------------------end----------------------------->
	    //保存门店优惠信息
	    mallShopEntity.setDiscountMemberMoney( discountMemberMoneyByShopId ); //会员优惠
	    mallShopEntity.setDiscountConponMoney( discountConponMoneyByShopId );  //优惠券优惠金额
	    mallShopEntity.setCodes( codesByShopId );
	    mallShopEntity.setCanUseConpon( canUseConponByShopId );  //门店商品是否使用优惠券标示
	    mallShopEntity.setCouponNum( couponNumByShopId );
	    mallShopMap.put( mallShopEntity.getShopId(), mallShopEntity );

	}
	//保存总订单优惠信息
	mallAllEntity.setMallShops( mallShopMap );
	mallAllEntity.setDiscountMemberMoney( discountMemberMoneyByAll );
	mallAllEntity.setDiscountConponMoney( discountConponMoneyByAll );

	//<!-----------------粉币计算start---------------------------->
	if ( isMemberCard && mallAllEntity.getUseFenbi() == 1 ) {
	    SortedMap<String, Object> dict = dictService.getDict( "1058" );
	    Integer fcount = 0; //能抵扣的粉币商品数量
	    Integer fcount1 = 0; //能抵扣的粉币商品数量
	    Double fenbiFenTanAll = 0.0;
	    double fenbiMoney = currencyCount( 0.0, memberEntity.getFansCurrency() );
	    Double discountfenbiMoneyByShopId = 0.0;
	    Double balanceMoneyByShopId = 0.0;
	    if ( fenbiMoney > 0 ) {
		Double fenbiBanlance = 0.0;  //订单金额
		for ( MallShopEntity mallShopEntity : mallShopMap.values() ) {
		    Map< Object,MallEntity > mallEntityMap = mallShopEntity.getMalls();
		    for ( MallEntity mallEntity : mallEntityMap.values() ) {
			//判断能使用粉币的商品信息
			if ( mallEntity.getUseFenbi() == 1 ) {
			    fenbiBanlance = formatNumber( BigDecimalUtil.add( fenbiBanlance, mallEntity.getTotalMoneyAll() ) );
			    fcount++;
			}
		    }
		}
		if ( fenbiBanlance >= 10 )
		    //计算粉币分摊结果
		    for ( MallShopEntity mallShopEntity : mallShopMap.values() ) {
			Map< Object,MallEntity > mallEntityMap = mallShopEntity.getMalls();
			for ( MallEntity mallEntity : mallEntityMap.values() ) {
			    if ( mallEntity.getUseFenbi() == 1 ) {
				Double fenbiFenTan = 0.0;
				//处理误差
				if ( fcount == fcount1 + 1 ) {
				    if ( fenbiBanlance > fenbiMoney ) {
					fenbiFenTan = formatNumber( BigDecimalUtil.sub( fenbiMoney, fenbiFenTanAll ) );
				    } else {
					fenbiFenTan = formatNumber( BigDecimalUtil.sub( fenbiBanlance, fenbiFenTanAll ) );
				    }
				} else {
				    if ( fenbiBanlance >= fenbiMoney ) {
					//消费金额大于粉币金额
					fenbiFenTan = formatNumber( BigDecimalUtil.mul( BigDecimalUtil.div( mallEntity.getTotalMoneyAll(), fenbiBanlance ), fenbiMoney ) );
				    } else {
					fenbiFenTan = formatNumber( BigDecimalUtil.mul( BigDecimalUtil.div( mallEntity.getTotalMoneyAll(), fenbiBanlance ), fenbiBanlance ) );
				    }
				    fenbiFenTanAll = formatNumber( BigDecimalUtil.add( fenbiFenTanAll, fenbiFenTan ) );
				}
				//商品数据
				mallEntity.setDiscountfenbiMoney( fenbiFenTan );
				Double fenbiNum = memberCommonService.deductFenbi( dict, fenbiFenTan );
				mallEntity.setFenbiNum( fenbiNum );
				mallEntity.setCanUsefenbi( 1 );
				mallEntity.setBalanceMoney( formatNumber( BigDecimalUtil.sub( mallEntity.getTotalMoneyAll(), fenbiFenTan ) ) );
				mallEntity.setUnitPrice( formatNumber( BigDecimalUtil.div( mallEntity.getBalanceMoney(), mallEntity.getNumber() ) ) );
				mallEntity.setTotalMoneyAll( formatNumber( BigDecimalUtil.sub( mallEntity.getTotalMoneyAll(), fenbiFenTan ) ) );
				mallEntityMap.put( mallEntity.getMallId(), mallEntity );

				//门店订单数据
				discountfenbiMoneyByShopId = formatNumber( BigDecimalUtil.add( discountfenbiMoneyByShopId, mallEntity.getDiscountfenbiMoney() ) );
				balanceMoneyByShopId = formatNumber( BigDecimalUtil.add( balanceMoneyByShopId, mallEntity.getBalanceMoney() ) );

				//总订单数据
				canUsefenbiByAll = 1;

				fcount1++;
			    } else {
				balanceMoneyByShopId = formatNumber( BigDecimalUtil.add( balanceMoneyByShopId, mallEntity.getTotalMoneyAll() ) );
			    }
			}
			mallShopEntity.setDiscountfenbiMoney( discountfenbiMoneyByShopId );
			mallShopEntity.setBalanceMoney( balanceMoneyByShopId );
			mallShopEntity.setMalls( mallEntityMap );
			mallShopMap.put( mallShopEntity.getShopId(), mallShopEntity );
		    }
		if ( fenbiMoney > 0 ) {
		    Double fenbiNumByAll = memberCommonService.deductFenbi( dict, discountfenbiMoneyByShopId );
		    mallAllEntity.setFenbiNum( fenbiNumByAll );
		    mallAllEntity.setDiscountfenbiMoney( discountfenbiMoneyByShopId );
		}
		mallAllEntity.setMallShops( mallShopMap );

		mallAllEntity.setCanUsefenbi( canUsefenbiByAll );
	    }
	}
	//<!-----------------粉币计算end---------------------------->

	//<!-----------------积分计算start---------------------------->
	if ( isMemberCard && mallAllEntity.getUserJifen() == 1 ) {
	    PublicParameterset pps = publicParameterSetMapper.findBybusId( memberEntity.getBusId() );
	    Double discountJifenMoneyByShopId = 0.0;
	    Double jifenBanlance = 0.0;  //订单金额
	    Double balanceMoneyByShopId = 0.0;
	    Integer jcount = 0; //能抵扣的粉币商品数量
	    Integer jcount1 = 0; //能抵扣的粉币商品数量
	    Double jifenFenTanAll = 0.0;

	    for ( MallShopEntity mallShopEntity : mallShopMap.values() ) {
		Map< Object,MallEntity > mallEntityMap = mallShopEntity.getMalls();
		for ( MallEntity mallEntity : mallEntityMap.values() ) {
		    //判断能使用粉币的商品信息
		    if ( mallEntity.getUserJifen() == 1 ) {
			jifenBanlance = formatNumber( BigDecimalUtil.add( jifenBanlance, mallEntity.getTotalMoneyAll() ) );
			jcount++;
		    }
		}
	    }
	    Double jifenMoney = memberCommonService.integralCount( jifenBanlance, memberEntity.getIntegral().doubleValue(), memberEntity.getBusId() );

	    if ( jifenMoney > 0 ) {
		//计算积分分摊结果
		for ( MallShopEntity mallShopEntity : mallShopMap.values() ) {
		    Map< Object,MallEntity > mallEntityMap = mallShopEntity.getMalls();
		    for ( MallEntity mallEntity : mallEntityMap.values() ) {
			if ( mallEntity.getUserJifen() == 1 ) {
			    Double jifenFenTan = 0.0;
			    if ( jcount == jcount1 + 1 ) {
				if ( jifenBanlance >= jifenMoney ) {
				    //消费金额大于粉币金额
				    jifenFenTan = formatNumber( BigDecimalUtil.sub( jifenMoney, jifenFenTanAll ) );
				} else {
				    jifenFenTan = formatNumber( BigDecimalUtil.sub( jifenBanlance, jifenFenTanAll ) );
				}
			    } else {
				if ( jifenBanlance >= jifenMoney ) {
				    //消费金额大于粉币金额
				    jifenFenTan = formatNumber( BigDecimalUtil.mul( BigDecimalUtil.div( mallEntity.getTotalMoneyAll(), jifenBanlance ), jifenMoney ) );
				} else {
				    jifenFenTan = formatNumber( BigDecimalUtil.mul( BigDecimalUtil.div( mallEntity.getTotalMoneyAll(), jifenBanlance ), jifenBanlance ) );
				}
				jifenFenTanAll = formatNumber( BigDecimalUtil.add( jifenFenTanAll, jifenFenTan ) );
			    }
			    //商品数据
			    mallEntity.setDiscountjifenMoney( jifenFenTan );
			    Double jifenNum = memberCommonService.deductJifen( pps, jifenFenTan, memberEntity.getBusId() );
			    mallEntity.setJifenNum( jifenNum );
			    mallEntity.setCanUseJifen( 1 );
			    mallEntity.setBalanceMoney( formatNumber( BigDecimalUtil.sub( mallEntity.getTotalMoneyAll(), jifenFenTan ) ) );
			    mallEntity.setUnitPrice( formatNumber( BigDecimalUtil.div( mallEntity.getBalanceMoney(), mallEntity.getNumber() ) ) );
			    mallEntity.setTotalMoneyAll( formatNumber( BigDecimalUtil.sub( mallEntity.getTotalMoneyAll(), jifenFenTan ) ) );
			    mallEntityMap.put( mallEntity.getMallId(), mallEntity );

			    //门店订单数据
			    discountJifenMoneyByShopId = formatNumber( BigDecimalUtil.add( discountJifenMoneyByShopId, mallEntity.getDiscountjifenMoney() ) );
			    balanceMoneyByShopId = formatNumber( BigDecimalUtil.add( balanceMoneyByShopId, mallEntity.getBalanceMoney() ) );

			    //总订单数据
			    canUseJifenByAll = 1;
			    jcount1++;
			} else {
			    balanceMoneyByShopId = formatNumber( BigDecimalUtil.add( balanceMoneyByShopId, mallEntity.getTotalMoneyAll() ) );
			}
		    }
		    mallShopEntity.setDiscountjifenMoney( discountJifenMoneyByShopId );
		    mallShopEntity.setBalanceMoney( balanceMoneyByShopId );
		    mallShopEntity.setMalls( mallEntityMap );
		    mallShopMap.put( mallShopEntity.getShopId(), mallShopEntity );
		}
		if ( discountJifenMoneyByShopId > 0 ) {
		    Integer jifenNumByAll = memberCommonService.deductJifen( pps, discountJifenMoneyByShopId, memberEntity.getBusId() ).intValue();
		    mallAllEntity.setJifenNum( jifenNumByAll );
		    mallAllEntity.setDiscountjifenMoney( discountJifenMoneyByShopId );
		}
		mallAllEntity.setMallShops( mallShopMap );
		mallAllEntity.setCanUseJifen( canUseJifenByAll );
	    }
	    // <!-----------------积分计算end---------------------------->
	}

	Double balanceMoneyByAll = 0.0;
	for ( MallShopEntity mallShopEntity : mallShopMap.values() ) {
	    Double balanceMoneyByShopId = 0.0;
	    Map< Object,MallEntity > mallEntitys = mallShopEntity.getMalls();
	    for ( MallEntity mallEntity : mallEntitys.values() ) {
		//未任何优惠商品支付金额重新赋值一次
		if ( mallEntity.getBalanceMoney() <= 0 ) {
		    mallEntity.setBalanceMoney( mallEntity.getTotalMoneyAll() );
		}
		balanceMoneyByShopId = formatNumber( BigDecimalUtil.add( balanceMoneyByShopId, mallEntity.getBalanceMoney() ) );
	    }
	    mallShopEntity.setBalanceMoney( balanceMoneyByShopId );
	    balanceMoneyByAll = formatNumber( BigDecimalUtil.add( balanceMoneyByAll, mallShopEntity.getBalanceMoney() ) );
	    mallShopMap.put( mallShopEntity.getShopId(), mallShopEntity );
	}
	mallAllEntity.setBalanceMoney( balanceMoneyByAll );
	Long end = new Date().getTime();
	System.out.println( "用时:" + ( end - start ) + "毫秒" );
	return mallAllEntity;
    }

    /**
     * 联盟卡计算
     *
     * @param mallAllEntity
     *
     * @return
     */
    public MallAllEntity leagueCount( MallAllEntity mallAllEntity ) {
	Long start = new Date().getTime();
	MemberCard card = null;
	MemberEntity memberEntity = memberMapper.selectById( mallAllEntity.getMemberId() );

	//所有门店使用卡券信息
	Map< Integer,MallShopEntity > mallShopMap = mallAllEntity.getMallShops();

	Double leagueMoney = 0.0;  //联盟卡优惠
	Double discountConponMoneyByAll = 0.0;   //优惠券券优惠金额
	boolean isUseDisCount = false;  //实付使用折扣
	WxCardReceive wxCardReceive = null;
	WxCard wxcard = null;
	DuofenCardGet dfget = null;
	DuofenCard dfcard = null;
	List< Map< String,Object > > dfcg = null;

	//计算门店下使用了优惠券商品信息
	for ( MallShopEntity mallShopEntity : mallShopMap.values() ) {
	    Double discountMemberMoneyByShopId = 0.0; //门店会员优惠券金额
	    String codesByShopId = "";  //门店使用优惠券code值 用来核销卡券 不存在set
	    Integer couponNumByShopId = 1;  //门店使用优惠券数量
	    Integer canUseConponByShopId = 0;  //门店是否能用优惠券
	    Double discountConponMoneyByShopId = 0.0;   //门店优惠券优惠券金额
	    Map< Object,MallEntity > mallList = mallShopEntity.getMalls();  //门店下商品信息
	    Integer useCoupon = mallShopEntity.getUseCoupon(); //是否使用了优惠券
	    Integer couponType = mallShopEntity.getCouponType();  //优惠券类型 0微信 1多粉
	    Integer coupondId = mallShopEntity.getCoupondId();   //优惠券id
	    Double discountLinshi = 0.0;  //折扣优惠券的折扣值 0到1
	    Double reduceCose = 0.0;
	    if ( useCoupon == 1 ) {
		if ( couponType == 0 ) {
		    // 微信优惠券
		    wxCardReceive = wxCardReceiveMapper.selectById( coupondId );
		    wxcard = wxCardMapper.selectByCardId( wxCardReceive.getCardId() );
		    if ( "DISCOUNT".equals( wxcard.getCardType() ) ) {
			isUseDisCount = true;
			discountLinshi = wxcard.getDiscount() / 10;
		    } else {
			reduceCose = wxcard.getReduceCost();
		    }
		    codesByShopId = wxCardReceive.getUserCardCode();
		} else if ( couponType == 1 ) {
		    // 多粉优惠券
		    dfget = duofenCardGetMapper.selectById( coupondId );
		    dfcard = duofenCardMapper.selectById( dfget.getCardId() );
		    if ( dfcard.getCardType() == 0 ) {
			isUseDisCount = true;
			discountLinshi = dfcard.getDiscount() / 10;
		    }
		    if ( dfcard.getAddUser() == 1 ) {
			// 允许叠加使用
			dfcg = duofenCardGetMapper.findByCardId( dfcard.getId(), mallAllEntity.getMemberId(), 0 );
		    }
		    codesByShopId = dfget.getCode();
		}
	    }

	    //<!-------------------start----------------------------->
	    if ( ( useCoupon == 0 && mallAllEntity.getUserLeague() == 1 ) || ( useCoupon == 1 && !isUseDisCount ) ) {
		//未使用优惠券 使用联盟卡 计算优惠
		for ( MallEntity mallEntity : mallList.values() ) {
		    if ( mallEntity.getUserCard() == 1 ) {
			//会员卡优惠
			Double leagueMoneyByOne = formatNumber( BigDecimalUtil.mul( mallEntity.getTotalMoneyOne(), BigDecimalUtil.sub( 1, mallAllEntity.getLeagueDiscount() ) ) );
			Double unitPrice = formatNumber( BigDecimalUtil.sub( mallEntity.getTotalMoneyOne(), leagueMoneyByOne ) );
			//商品数据
			mallEntity.setUnitPrice( unitPrice );
			mallEntity.setLeagueMoney( formatNumber( BigDecimalUtil.mul( leagueMoneyByOne, mallEntity.getNumber() ) ) );
			mallEntity.setBalanceMoney( formatNumber( BigDecimalUtil.mul( unitPrice, mallEntity.getNumber() ) ) );  //商品订单总金额
			mallEntity.setTotalMoneyAll( formatNumber( BigDecimalUtil.sub( mallEntity.getTotalMoneyAll(), mallEntity.getLeagueMoney() ) ) );
			mallEntity.setCanUserCard( 1 );
			mallList.put( mallEntity.getMallId(), mallEntity );  //数据处理完归还

			//总订单联盟优惠数据
			leagueMoney = formatNumber( BigDecimalUtil.add( leagueMoney, BigDecimalUtil.mul( leagueMoneyByOne, mallEntity.getNumber() ) ) );
		    }
		}

	    } else if ( useCoupon == 1 ) {
		if ( isUseDisCount ) {
		    //折扣优惠券处理业务
		    for ( MallEntity mallEntity : mallList.values() ) {
			//挑选出能使用优惠券的商品
			if ( mallEntity.getUseCoupon() == 1 ) {
			    //单个商品优惠券金额
			    Double discountConponMoneyByOne = formatNumber( BigDecimalUtil.mul( mallEntity.getTotalMoneyOne(), 1 - discountLinshi ) );
			    Double unitPrice = formatNumber( mallEntity.getTotalMoneyOne() - discountConponMoneyByOne );

			    //商品数据
			    mallEntity.setUnitPrice( unitPrice );  //当个商品价格
			    mallEntity.setDiscountConponMoney( formatNumber( BigDecimalUtil.mul( discountConponMoneyByOne, mallEntity.getNumber() ) ) );
			    mallEntity.setBalanceMoney( formatNumber( BigDecimalUtil.mul( unitPrice, mallEntity.getNumber() ) ) );
			    mallEntity.setTotalMoneyAll( formatNumber( BigDecimalUtil.sub( mallEntity.getTotalMoneyAll(), mallEntity.getDiscountConponMoney() ) ) );
			    mallEntity.setCanUseConpon( 1 ); //能使用优惠券
			    mallList.put( mallEntity.getMallId(), mallEntity ); //归还到分离的数据中去

			    //门店订单数据
			    canUseConponByShopId = 1;  //门店商品是否使用优惠券标示
			    discountConponMoneyByShopId = formatNumber( BigDecimalUtil.add( discountConponMoneyByShopId, mallEntity.getDiscountConponMoney() ) );

			    //总订单数据
			    discountConponMoneyByAll = formatNumber( BigDecimalUtil.add( discountConponMoneyByAll, mallEntity.getDiscountConponMoney() ) );
			}
		    }
		}
		//<!-------联盟卡优惠已结束-------->

		//<!---------减免券start------------>
		Double balanceMoneyBili = 0.0;//门店订单支付金额
		Integer couponCount = 0;
		Integer couponCount1 = 0;
		Double couponCountFentan = 0.0;
		for ( MallEntity mallEntity : mallList.values() ) {  //商品信息
		    //挑选出能使用优惠券的商品
		    if ( mallEntity.getUseCoupon() == 1 ) {
			balanceMoneyBili = formatNumber( BigDecimalUtil.add( balanceMoneyBili, mallEntity.getTotalMoneyAll() ) );
			couponCount++;
		    }
		}

		// 计算微信减免券优惠
		Double discountConponMoneyByMall = 0.0;
		if ( useCoupon == 1 ) {
		    //<!-------start-------->
		    if ( couponType == 0 ) {
			for ( MallEntity mallEntity : mallList.values() ) {
			    if ( mallEntity.getUseCoupon() == 1 ) {
				if ( balanceMoneyBili >= wxcard.getCashLeastCost() ) {
				    //商品数据
				    //减免金额分摊
				    couponCount1++;
				    if ( couponCount1 == couponCount ) {
					mallEntity.setDiscountConponMoney( BigDecimalUtil.sub( reduceCose, couponCountFentan ) );
				    } else {
					discountConponMoneyByMall = formatNumber(
							BigDecimalUtil.mul( BigDecimalUtil.div( mallEntity.getTotalMoneyAll(), balanceMoneyBili ), reduceCose ) );  //分摊金额
					mallEntity.setDiscountConponMoney( discountConponMoneyByMall );
				    }
				    couponCountFentan = couponCountFentan + discountConponMoneyByMall;

				    mallEntity.setBalanceMoney( formatNumber( BigDecimalUtil.sub( mallEntity.getTotalMoneyAll(), mallEntity.getDiscountConponMoney() ) ) );
				    mallEntity.setUnitPrice( formatNumber( BigDecimalUtil.div( mallEntity.getBalanceMoney(), mallEntity.getNumber() ) ) );
				    mallEntity.setCanUseConpon( 1 ); //能使用优惠券
				    mallEntity.setTotalMoneyAll( formatNumber( BigDecimalUtil.sub( mallEntity.getTotalMoneyAll(), mallEntity.getDiscountConponMoney() ) ) );
				    mallList.put( mallEntity.getMallId(), mallEntity );

				    //门店订单数据
				    canUseConponByShopId = 1;  //门店商品是否使用优惠券标示
				    discountConponMoneyByShopId = formatNumber(
						    BigDecimalUtil.add( discountConponMoneyByShopId, mallEntity.getDiscountConponMoney() ) );  //该门店商品会员优惠金额

				    //总订单数据
				    discountConponMoneyByAll = formatNumber( BigDecimalUtil.add( discountConponMoneyByAll, mallEntity.getDiscountConponMoney() ) );

				}
			    }
			}
			//<!-------end-------->

		    } else if ( couponType == 1 ) {
			//计算多粉优惠券减免券优惠
			reduceCose = dfcard.getReduceCost();
			for ( MallEntity mallEntity : mallList.values() ) {
			    if ( mallEntity.getUseCoupon() == 1 ) {
				// 减免券
				if ( dfcard.getAddUser() == 0 ) {
				    // 不允许叠加使用
				    if ( balanceMoneyBili >= dfcard.getCashLeastCost() ) {
					//商品数据
					//减免金额分摊
					couponCount1++;
					if ( couponCount1 == couponCount ) {
					    discountConponMoneyByMall = BigDecimalUtil.sub( reduceCose, couponCountFentan );
					} else {
					    discountConponMoneyByMall = formatNumber(
							    BigDecimalUtil.mul( BigDecimalUtil.div( mallEntity.getTotalMoneyAll(), balanceMoneyBili ), reduceCose ) );  //分摊金额
					    mallEntity.setDiscountConponMoney( discountConponMoneyByMall );
					}
					couponCountFentan = couponCountFentan + discountConponMoneyByMall;

					mallEntity.setDiscountConponMoney( discountConponMoneyByMall );
					mallEntity.setBalanceMoney(
							formatNumber( BigDecimalUtil.sub( mallEntity.getTotalMoneyAll(), mallEntity.getDiscountConponMoney() ) ) );  //支付金额
					mallEntity.setUnitPrice( formatNumber( BigDecimalUtil.div( mallEntity.getBalanceMoney(), mallEntity.getNumber() ) ) );
					mallEntity.setCanUseConpon( 1 ); //能使用优惠券
					mallEntity.setTotalMoneyAll( formatNumber( BigDecimalUtil.sub( mallEntity.getTotalMoneyAll(), mallEntity.getDiscountConponMoney() ) ) );
					mallList.put( mallEntity.getMallId(), mallEntity );

					//门店订单数据
					canUseConponByShopId = 1;  //门店商品是否使用优惠券标示
					discountConponMoneyByShopId = formatNumber(
							BigDecimalUtil.add( discountConponMoneyByShopId, mallEntity.getDiscountConponMoney() ) );  //该门店商品会员优惠金额

					//总订单数据
					discountConponMoneyByAll = formatNumber( BigDecimalUtil.add( discountConponMoneyByAll, mallEntity.getDiscountConponMoney() ) );
				    }
				} else {
				    Integer num = 0;
				    Integer shiNum = 0;
				    // 满足使用条件
				    if ( balanceMoneyBili >= dfcard.getCashLeastCost() ) {
					if ( dfcard.getCashLeastCost() > 0 ) {
					    num = BigDecimalUtil.divInteger( balanceMoneyBili, dfcard.getCashLeastCost() ); // 能使用优惠券数量
					}
					// 允许叠加使用
					if ( dfcg.size() > 0 ) {
					    for ( int i = 0; i < dfcg.size(); i++ ) {
						if ( i <= num ) {
						    codesByShopId += dfcg.get( i ).get( "code" ) + ",";
						    shiNum = i;
						}
					    }
					    num = shiNum;
					    couponNumByShopId = num;
					    mallShopEntity.setCouponNum( num );  //优惠券使用数量
					    reduceCose = formatNumber( BigDecimalUtil.mul( dfcard.getReduceCost(), num ) );
					    //减免金额分摊
					    couponCount1++;
					    if ( couponCount1 == couponCount ) {
						mallEntity.setDiscountConponMoney( BigDecimalUtil.sub( reduceCose, couponCountFentan ) );
					    } else {
						discountConponMoneyByMall = formatNumber(
								BigDecimalUtil.mul( BigDecimalUtil.div( mallEntity.getTotalMoneyAll(), balanceMoneyBili ), reduceCose ) );  //分摊金额
						mallEntity.setDiscountConponMoney( discountConponMoneyByMall );
					    }
					    couponCountFentan = couponCountFentan + discountConponMoneyByMall;

					    //商品数据
					    mallEntity.setDiscountConponMoney( discountConponMoneyByMall );
					    mallEntity.setDiscountConponMoney( discountConponMoneyByMall );
					    mallEntity.setBalanceMoney(
							    formatNumber( BigDecimalUtil.sub( mallEntity.getTotalMoneyAll(), mallEntity.getDiscountConponMoney() ) ) );  //支付金额
					    mallEntity.setUnitPrice( formatNumber( BigDecimalUtil.div( mallEntity.getBalanceMoney(), mallEntity.getNumber() ) ) );
					    mallEntity.setCanUseConpon( 1 ); //能使用优惠券
					    mallEntity.setTotalMoneyAll( formatNumber( BigDecimalUtil.sub( mallEntity.getTotalMoneyAll(), mallEntity.getDiscountConponMoney() ) ) );
					    mallList.put( mallEntity.getMallId(), mallEntity );

					    //门店订单数据
					    canUseConponByShopId = 1;  //门店商品是否使用优惠券标示
					    discountConponMoneyByShopId = formatNumber(
							    BigDecimalUtil.add( discountConponMoneyByShopId, mallEntity.getDiscountConponMoney() ) );  //该门店商品会员优惠金额

					    //总订单数据
					    discountConponMoneyByAll = formatNumber( BigDecimalUtil.add( discountConponMoneyByAll, mallEntity.getDiscountConponMoney() ) );
					}
				    }
				}
			    }
			}
		    }
		}
	    }

	    //<!-------------------end----------------------------->
	    //保存门店优惠信息
	    mallShopEntity.setDiscountConponMoney( discountConponMoneyByShopId );  //优惠券优惠金额
	    mallShopEntity.setCodes( codesByShopId );
	    mallShopEntity.setCanUseConpon( canUseConponByShopId );  //门店商品是否使用优惠券标示
	    mallShopEntity.setCouponNum( couponNumByShopId );
	    mallShopMap.put( mallShopEntity.getShopId(), mallShopEntity );

	}
	//保存总订单优惠信息
	mallAllEntity.setMallShops( mallShopMap );
	mallAllEntity.setLeagueMoney( leagueMoney );
	mallAllEntity.setDiscountConponMoney( discountConponMoneyByAll );

	if ( mallAllEntity.getUserLeague() == 1 ) {
	    Double jifenBanlance = 0.0;  //订单金额
	    Double balanceMoneyByShopId = 0.0;
	    Integer jcount = 0; //能抵扣的联盟积分商品数量
	    Integer jcount1 = 0; //能抵扣的联盟积分商品数量
	    Double leaguejifenFenTanAll = 0.0;

	    for ( MallShopEntity mallShopEntity : mallShopMap.values() ) {
		Map< Object,MallEntity > mallEntityMap = mallShopEntity.getMalls();
		for ( MallEntity mallEntity : mallEntityMap.values() ) {
		    //判断能使用积分的商品信息
		    if ( mallEntity.getUseLeague() == 1 ) {
			jifenBanlance = formatNumber( BigDecimalUtil.add( jifenBanlance, mallEntity.getTotalMoneyAll() ) );
			jcount++;
		    }
		}
	    }

	    Double jifenMoney = mallAllEntity.getLeagueJifen() / 100.0;  //联盟最高能抵扣金额

	    if ( jifenMoney > 0 ) {
		//计算积分分摊结果
		for ( MallShopEntity mallShopEntity : mallShopMap.values() ) {
		    Map< Object,MallEntity > mallEntityMap = mallShopEntity.getMalls();
		    for ( MallEntity mallEntity : mallEntityMap.values() ) {
			if ( mallEntity.getUseLeague() == 1 ) {
			    Double jifenFenTan = 0.0;
			    if ( jcount == jcount1 + 1 ) {
				if ( jifenBanlance >= jifenMoney ) {
				    //消费金额大于粉币金额
				    jifenFenTan = formatNumber( BigDecimalUtil.sub( jifenMoney, leaguejifenFenTanAll ) );
				} else {
				    jifenFenTan = formatNumber( BigDecimalUtil.sub( jifenBanlance, leaguejifenFenTanAll ) );
				}
			    } else {
				if ( jifenBanlance >= jifenMoney ) {
				    //消费金额大于粉币金额
				    jifenFenTan = formatNumber( BigDecimalUtil.mul( BigDecimalUtil.div( mallEntity.getTotalMoneyAll(), jifenBanlance ), jifenMoney ) );
				} else {
				    jifenFenTan = formatNumber( BigDecimalUtil.mul( BigDecimalUtil.div( mallEntity.getTotalMoneyAll(), jifenBanlance ), jifenBanlance ) );
				}
				leaguejifenFenTanAll = formatNumber( BigDecimalUtil.add( leaguejifenFenTanAll, jifenFenTan ) );
			    }
			    //商品数据
			    mallEntity.setLeagueMoney( jifenFenTan );
			    mallEntity.setLeagueJifen( BigDecimalUtil.mul( jifenFenTan, 100 ) );
			    mallEntity.setCanUseLeagueJifen( 1 );
			    mallEntity.setBalanceMoney( formatNumber( BigDecimalUtil.sub( mallEntity.getTotalMoneyAll(), jifenFenTan ) ) );
			    mallEntity.setTotalMoneyAll( formatNumber( BigDecimalUtil.sub( mallEntity.getTotalMoneyAll(), jifenFenTan ) ) );
			    mallEntityMap.put( mallEntity.getMallId(), mallEntity );

			    //总订单数据
			    jcount1++;
			} else {
			    balanceMoneyByShopId = formatNumber( BigDecimalUtil.add( balanceMoneyByShopId, mallEntity.getTotalMoneyAll() ) );
			}
		    }
		    mallShopEntity.setBalanceMoney( balanceMoneyByShopId );
		    mallShopEntity.setMalls( mallEntityMap );
		    mallShopMap.put( mallShopEntity.getShopId(), mallShopEntity );
		}
		mallAllEntity.setMallShops( mallShopMap );
	    }
	    // <!-----------------积分计算end---------------------------->
	}

	Double balanceMoneyByAll = 0.0;
	Double leagueMoneyAll = 0.0;
	Double leagueJifenNumAll = 0.0;
	for ( MallShopEntity mallShopEntity : mallShopMap.values() ) {
	    Double balanceMoneyByShopId = 0.0;
	    Map< Object,MallEntity > mallEntitys = mallShopEntity.getMalls();
	    for ( MallEntity mallEntity : mallEntitys.values() ) {
		//未任何优惠商品支付金额重新赋值一次
		if ( mallEntity.getBalanceMoney() <= 0 ) {
		    mallEntity.setBalanceMoney( mallEntity.getTotalMoneyAll() );
		}
		balanceMoneyByShopId = formatNumber( BigDecimalUtil.add( balanceMoneyByShopId, mallEntity.getBalanceMoney() ) );
		leagueMoneyAll = mallEntity.getLeagueMoney();
		leagueJifenNumAll = mallEntity.getLeagueJifen();
	    }
	    mallShopEntity.setBalanceMoney( balanceMoneyByShopId );
	    balanceMoneyByAll = formatNumber( BigDecimalUtil.add( balanceMoneyByAll, mallShopEntity.getBalanceMoney() ) );
	    mallShopMap.put( mallShopEntity.getShopId(), mallShopEntity );
	}
	mallAllEntity.setBalanceMoney( balanceMoneyByAll );
	mallAllEntity.setLeagueJifenNum( leagueJifenNumAll.intValue() );
	mallAllEntity.setLeagueMoney( leagueMoneyAll );
	Long end = new Date().getTime();
	System.out.println( "用时:" + ( end - start ) + "毫秒" );
	return mallAllEntity;
    }

    /**
     * 数字处理
     *
     * @param number
     *
     * @return
     */
    public Double formatNumber( Double number ) {
	DecimalFormat df = new DecimalFormat( "######0.00" );
	return CommonUtil.toDouble( df.format( number ) );
    }

    /**
     * 粉币计算
     *
     * @param totalMoney    能抵抗消费金额
     * @param fans_currency 粉币值
     *
     * @return 返回兑换金额
     */
    public Double currencyCount( Double totalMoney, Double fans_currency ) {
	try {
	    SortedMap<String, Object> dict = dictService.getDict( "1058" );
	    Double ratio = CommonUtil.toDouble( dict.get( "1" ) );
	    if ( fans_currency < ratio * 10 ) {
		return 0.0;
	    }
	    Integer money = new Double( fans_currency / ratio / 10 ).intValue();
	    if ( CommonUtil.isEmpty( totalMoney ) || totalMoney == 0 ) {
		return new Double( money * 10 );
	    } else {
		if ( totalMoney >= money * 10 ) {
		    return new Double( money * 10 );
		} else {
		    return totalMoney;
		}
	    }
	} catch ( Exception e ) {
	    LOG.error( "计算粉币抵扣异常" );
	    e.printStackTrace();
	}
	return 0.0;
    }

    /**
     * 不跨店
     *
     * @param mallNotShopEntity
     *
     * @return
     */
    public MallNotShopEntity mallSkipNotShopCount( MallNotShopEntity mallNotShopEntity ) {
	if ( mallNotShopEntity.getDerateMoney() > 0 ) {
	    //减免金额大于0 商品分摊
	    Double totalMoneys = 0.0;
	    Integer jianmainCount = 0;
	    Integer jianmainCount1 = 0;
	    Map< Object,MallEntity > mallMap=mallNotShopEntity.getMalls();
	    for ( MallEntity mallEntity : mallMap.values() ) {
		totalMoneys += mallEntity.getTotalMoneyAll();
		jianmainCount++;
	    }
	    Double jianmainFenTan = 0.0;  //分摊比例
	    Double jianmainFenTanAll = 0.0;  //
	    for ( MallEntity mallEntity : mallNotShopEntity.getMalls().values() ) {
		if ( jianmainCount == jianmainCount1 + 1 ) {
		    jianmainFenTan = formatNumber( BigDecimalUtil.sub( mallNotShopEntity.getDerateMoney(), jianmainFenTanAll ) );
		} else {
		    jianmainFenTan = formatNumber( BigDecimalUtil.mul( BigDecimalUtil.div( mallEntity.getTotalMoneyAll(), totalMoneys ), mallNotShopEntity.getDerateMoney() ) );
		    jianmainFenTanAll = formatNumber( BigDecimalUtil.add( jianmainFenTanAll, jianmainFenTan ) );
		}
		mallEntity.setTotalMoneyAll( BigDecimalUtil.sub( mallEntity.getTotalMoneyAll(), jianmainFenTan ) );
		mallEntity.setTotalMoneyOne( BigDecimalUtil.div( mallEntity.getTotalMoneyAll(), mallEntity.getNumber() ) );
		jianmainCount1++;
		mallNotShopEntity.getMalls().put( mallEntity.getMallId(), mallEntity );
	    }
	}

	if ( mallNotShopEntity.getUserLeague() == 1 ) {
	    return leagueCount( mallNotShopEntity );
	}
	Long start = new Date().getTime();
	boolean isMemberCard = false;  //用来标示该粉丝是会员
	MemberCard card = null;
	MemberEntity memberEntity = memberMapper.selectById( mallNotShopEntity.getMemberId() );
	Double memberDiscount = 1.0;  //会员卡折扣
	if ( CommonUtil.isNotEmpty( memberEntity ) && CommonUtil.isNotEmpty( memberEntity.getMcId() ) ) {
	    card = cardMapper.selectById( memberEntity.getMcId() );
	    if ( card.getCardStatus() == 0 ) {  //如果会员卡状态不为0标示该会员卡被禁用了
		isMemberCard = true;
		// 折扣卡
		//判断是否是会员日
		if ( card.getCtId() == 2 ) {
		    MemberDate memberDate = memberCommonService.findMemeberDate( card.getBusId(), card.getCtId() );
		    if ( CommonUtil.isNotEmpty( memberDate ) ) {
			memberDiscount = BigDecimalUtil.div( memberDate.getDiscount().doubleValue(), 10.0 );  //会员日折扣
		    }
		    MemberGiverule gr = giveRuleMapper.selectById( card.getGrId() );
		    memberDiscount = BigDecimalUtil.mul( BigDecimalUtil.div( gr.getGrDiscount(), 100.0 ), memberDiscount );  //会员折扣
		}
	    }
	}

	boolean isUseDisCount = false;  //实付使用折扣
	WxCardReceive wxCardReceive = null;
	WxCard wxcard = null;
	DuofenCardGet dfget = null;
	DuofenCard dfcard = null;
	List< Map< String,Object > > dfcg = null;

	//计算门店下使用了优惠券商品信息

	Double discountMemberMoneyByShopId = 0.0; //门店会员优惠券金额
	String codesByShopId = "";  //门店使用优惠券code值 用来核销卡券 不存在set
	Integer couponNumByShopId = 1;  //门店使用优惠券数量
	Integer canUseConponByShopId = 0;  //门店是否能用优惠券
	Double discountConponMoneyByShopId = 0.0;   //门店优惠券优惠券金额
	Map< Object,MallEntity > mallList = mallNotShopEntity.getMalls();  //门店下商品信息
	Integer useCoupon = mallNotShopEntity.getUseCoupon(); //是否使用了优惠券
	Integer couponType = mallNotShopEntity.getCouponType();  //优惠券类型 0微信 1多粉
	Integer coupondId = mallNotShopEntity.getCoupondId();   //优惠券id
	Double discountLinshi = 0.0;  //折扣优惠券的折扣值 0到1
	Double reduceCose = 0.0;
	if ( useCoupon == 1 ) {
	    if ( couponType == 0 ) {
		// 微信优惠券
		wxCardReceive = wxCardReceiveMapper.selectById( coupondId );
		wxcard = wxCardMapper.selectByCardId( wxCardReceive.getCardId() );
		if ( "DISCOUNT".equals( wxcard.getCardType() ) ) {
		    isUseDisCount = true;
		    discountLinshi = wxcard.getDiscount() / 10;
		} else {
		    reduceCose = wxcard.getReduceCost();
		}
		codesByShopId = wxCardReceive.getUserCardCode();
	    } else if ( couponType == 1 ) {
		// 多粉优惠券
		dfget = duofenCardGetMapper.selectById( coupondId );
		dfcard = duofenCardMapper.selectById( dfget.getCardId() );
		if ( dfcard.getCardType() == 0 ) {
		    isUseDisCount = true;
		    discountLinshi = dfcard.getDiscount() / 10;
		}
		if ( dfcard.getAddUser() == 1 ) {
		    // 允许叠加使用
		    dfcg = duofenCardGetMapper.findByCardId( dfcard.getId(), mallNotShopEntity.getMemberId(), 0 );
		}
		codesByShopId = dfget.getCode();
	    }
	}

	//<!-------------------start----------------------------->
	if ( useCoupon == 0 && isMemberCard && card.getCtId() == 2 ) {
	    //未使用优惠券 是折扣卡会员 计算优惠
	    for ( MallEntity mallEntity : mallList.values() ) {
		if ( mallEntity.getUserCard() == 1 ) {
		    //会员卡优惠
		    Double discountMemberMoneyByOne = formatNumber( BigDecimalUtil.mul( mallEntity.getTotalMoneyOne(), BigDecimalUtil.sub( 1, memberDiscount ) ) );
		    Double unitPrice = formatNumber( BigDecimalUtil.sub( mallEntity.getTotalMoneyOne(), discountMemberMoneyByOne ) );

		    //商品数据
		    mallEntity.setUnitPrice( unitPrice );
		    mallEntity.setDiscountMemberMoney( formatNumber( BigDecimalUtil.mul( discountMemberMoneyByOne, mallEntity.getNumber() ) ) );
		    mallEntity.setBalanceMoney( formatNumber( BigDecimalUtil.mul( unitPrice, mallEntity.getNumber() ) ) );  //商品订单总金额
		    mallEntity.setTotalMoneyAll( formatNumber( BigDecimalUtil.sub( mallEntity.getTotalMoneyAll(), mallEntity.getDiscountMemberMoney() ) ) );
		    mallEntity.setCanUserCard( 1 );
		    mallList.put( mallEntity.getMallId(), mallEntity );  //数据处理完归还

		    //门店订单数据
		    discountMemberMoneyByShopId = formatNumber(
				    BigDecimalUtil.add( discountMemberMoneyByShopId, BigDecimalUtil.mul( discountMemberMoneyByOne, mallEntity.getNumber() ) ) );  //该门店商品会员优惠金额

		}
	    }

	} else if ( useCoupon == 1 ) {
	    if ( isUseDisCount ) {
		//折扣优惠券处理业务
		for ( MallEntity mallEntity : mallList.values() ) {
		    //挑选出能使用优惠券的商品
		    if ( mallEntity.getUseCoupon() == 1 ) {
			//单个商品优惠券金额
			Double discountConponMoneyByOne = formatNumber( BigDecimalUtil.mul( mallEntity.getTotalMoneyOne(), 1 - discountLinshi ) );
			Double unitPrice = formatNumber( mallEntity.getTotalMoneyOne() - discountConponMoneyByOne );

			//商品数据
			mallEntity.setUnitPrice( unitPrice );  //当个商品价格
			mallEntity.setDiscountConponMoney( formatNumber( BigDecimalUtil.mul( discountConponMoneyByOne, mallEntity.getNumber() ) ) );
			mallEntity.setBalanceMoney( formatNumber( BigDecimalUtil.mul( unitPrice, mallEntity.getNumber() ) ) );
			mallEntity.setTotalMoneyAll( formatNumber( BigDecimalUtil.sub( mallEntity.getTotalMoneyAll(), mallEntity.getDiscountConponMoney() ) ) );
			mallEntity.setCanUseConpon( 1 ); //能使用优惠券
			mallList.put( mallEntity.getMallId(), mallEntity ); //归还到分离的数据中去

			//门店订单数据
			canUseConponByShopId = 1;  //门店商品是否使用优惠券标示
			discountConponMoneyByShopId = formatNumber( BigDecimalUtil.add( discountConponMoneyByShopId, mallEntity.getDiscountConponMoney() ) );

		    }
		}
	    } else {
		//减免券处理方式  1如果存在折扣卡 先处理折扣卡优惠券  2再处理优惠券减免
		for ( MallEntity mallEntity : mallList.values() ) {
		    //先处理会员卡
		    if ( mallEntity.getUserCard() == 1 ) {
			if ( isMemberCard && card.getCtId() == 2 ) {
			    //商品会员卡优惠券
			    Double discountMemberMoneyByOne = formatNumber( BigDecimalUtil.mul( mallEntity.getTotalMoneyOne(), ( 1 - memberDiscount ) ) );
			    Double unitPrice = formatNumber( BigDecimalUtil.sub( mallEntity.getTotalMoneyOne(), discountMemberMoneyByOne ) );

			    //商品数据
			    mallEntity.setUnitPrice( unitPrice );
			    mallEntity.setDiscountMemberMoney( formatNumber( BigDecimalUtil.mul( discountMemberMoneyByOne, mallEntity.getNumber() ) ) );
			    mallEntity.setBalanceMoney( formatNumber( BigDecimalUtil.mul( unitPrice, mallEntity.getNumber() ) ) );  //商品订单总金额
			    mallEntity.setCanUserCard( 1 );
			    mallEntity.setTotalMoneyAll( formatNumber( BigDecimalUtil.sub( mallEntity.getTotalMoneyAll(), mallEntity.getDiscountMemberMoney() ) ) );

			    //门店订单数据
			    discountMemberMoneyByShopId = formatNumber( BigDecimalUtil.add( discountMemberMoneyByShopId, mallEntity.getDiscountMemberMoney() ) );  //该门店商品会员优惠金额

			}
			mallList.put( mallEntity.getMallId(), mallEntity );  //数据处理完归还
		    }
		}
		//<!-------会员卡优惠券已结束-------->

		//<!---------减免券start------------>
		Double balanceMoneyBili = 0.0;//门店订单支付金额
		Integer couponCount = 0;
		Integer couponCount1 = 0;
		Double couponCountFentan = 0.0;
		for ( MallEntity mallEntity : mallList.values() ) {  //商品信息
		    //挑选出能使用优惠券的商品
		    if ( mallEntity.getUseCoupon() == 1 ) {
			balanceMoneyBili = formatNumber( BigDecimalUtil.add( balanceMoneyBili, mallEntity.getTotalMoneyAll() ) );
			couponCount++;
		    }
		}

		// 计算微信减免券优惠
		Double discountConponMoneyByMall = 0.0;
		if ( useCoupon == 1 ) {
		    //<!-------start-------->
		    if ( couponType == 0 ) {
			for ( MallEntity mallEntity : mallList.values() ) {
			    if ( mallEntity.getUseCoupon() == 1 ) {
				if ( balanceMoneyBili >= wxcard.getCashLeastCost() ) {
				    //商品数据
				    //减免金额分摊
				    couponCount1++;
				    if ( couponCount1 == couponCount ) {
					mallEntity.setDiscountConponMoney( BigDecimalUtil.sub( reduceCose, couponCountFentan ) );
				    } else {
					discountConponMoneyByMall = formatNumber(
							BigDecimalUtil.mul( BigDecimalUtil.div( mallEntity.getTotalMoneyAll(), balanceMoneyBili ), reduceCose ) );  //分摊金额
					mallEntity.setDiscountConponMoney( discountConponMoneyByMall );
				    }
				    couponCountFentan = couponCountFentan + discountConponMoneyByMall;

				    mallEntity.setBalanceMoney( formatNumber( BigDecimalUtil.sub( mallEntity.getTotalMoneyAll(), mallEntity.getDiscountConponMoney() ) ) );
				    mallEntity.setUnitPrice( formatNumber( BigDecimalUtil.div( mallEntity.getBalanceMoney(), mallEntity.getNumber() ) ) );
				    mallEntity.setCanUseConpon( 1 ); //能使用优惠券
				    mallEntity.setTotalMoneyAll( formatNumber( BigDecimalUtil.sub( mallEntity.getTotalMoneyAll(), mallEntity.getDiscountConponMoney() ) ) );
				    mallList.put( mallEntity.getMallId(), mallEntity );

				    //门店订单数据
				    canUseConponByShopId = 1;  //门店商品是否使用优惠券标示
				    discountConponMoneyByShopId = formatNumber(
						    BigDecimalUtil.add( discountConponMoneyByShopId, mallEntity.getDiscountConponMoney() ) );  //该门店商品会员优惠金额

				}
			    }
			}
			//<!-------end-------->

		    } else if ( couponType == 1 ) {
			//计算多粉优惠券减免券优惠
			reduceCose = dfcard.getReduceCost();
			for ( MallEntity mallEntity : mallList.values() ) {
			    if ( mallEntity.getUseCoupon() == 1 ) {
				// 减免券
				if ( dfcard.getAddUser() == 0 ) {
				    // 不允许叠加使用
				    if ( balanceMoneyBili >= dfcard.getCashLeastCost() ) {
					//商品数据
					//减免金额分摊
					couponCount1++;
					if ( couponCount1 == couponCount ) {
					    discountConponMoneyByMall = BigDecimalUtil.sub( reduceCose, couponCountFentan );
					} else {
					    discountConponMoneyByMall = formatNumber(
							    BigDecimalUtil.mul( BigDecimalUtil.div( mallEntity.getTotalMoneyAll(), balanceMoneyBili ), reduceCose ) );  //分摊金额
					    mallEntity.setDiscountConponMoney( discountConponMoneyByMall );
					}
					couponCountFentan = couponCountFentan + discountConponMoneyByMall;

					mallEntity.setDiscountConponMoney( discountConponMoneyByMall );
					mallEntity.setBalanceMoney(
							formatNumber( BigDecimalUtil.sub( mallEntity.getTotalMoneyAll(), mallEntity.getDiscountConponMoney() ) ) );  //支付金额
					mallEntity.setUnitPrice( formatNumber( BigDecimalUtil.div( mallEntity.getBalanceMoney(), mallEntity.getNumber() ) ) );
					mallEntity.setCanUseConpon( 1 ); //能使用优惠券
					mallEntity.setTotalMoneyAll( formatNumber( BigDecimalUtil.sub( mallEntity.getTotalMoneyAll(), mallEntity.getDiscountConponMoney() ) ) );
					mallList.put( mallEntity.getMallId(), mallEntity );

					//门店订单数据
					canUseConponByShopId = 1;  //门店商品是否使用优惠券标示
					discountConponMoneyByShopId = formatNumber(
							BigDecimalUtil.add( discountConponMoneyByShopId, mallEntity.getDiscountConponMoney() ) );  //该门店商品会员优惠金额

				    }
				} else {
				    Integer num = 0;
				    Integer shiNum = 0;
				    // 满足使用条件
				    if ( balanceMoneyBili >= dfcard.getCashLeastCost() ) {
					if ( dfcard.getCashLeastCost() > 0 ) {
					    num = BigDecimalUtil.divInteger( balanceMoneyBili, dfcard.getCashLeastCost() ); // 能使用优惠券数量
					}
					// 允许叠加使用
					if ( dfcg.size() > 0 ) {
					    for ( int i = 0; i < dfcg.size(); i++ ) {
						if ( i <= num ) {
						    codesByShopId += dfcg.get( i ).get( "code" ) + ",";
						    shiNum = i;
						}
					    }
					    num = shiNum;
					    couponNumByShopId = num;
					    mallNotShopEntity.setCouponNum( num );  //优惠券使用数量
					    reduceCose = formatNumber( BigDecimalUtil.mul( dfcard.getReduceCost(), num ) );
					    //减免金额分摊
					    couponCount1++;
					    if ( couponCount1 == couponCount ) {
						mallEntity.setDiscountConponMoney( BigDecimalUtil.sub( reduceCose, couponCountFentan ) );
					    } else {
						discountConponMoneyByMall = formatNumber(
								BigDecimalUtil.mul( BigDecimalUtil.div( mallEntity.getTotalMoneyAll(), balanceMoneyBili ), reduceCose ) );  //分摊金额
						mallEntity.setDiscountConponMoney( discountConponMoneyByMall );
					    }
					    couponCountFentan = couponCountFentan + discountConponMoneyByMall;

					    //商品数据
					    mallEntity.setDiscountConponMoney( discountConponMoneyByMall );
					    mallEntity.setDiscountConponMoney( discountConponMoneyByMall );
					    mallEntity.setBalanceMoney(
							    formatNumber( BigDecimalUtil.sub( mallEntity.getTotalMoneyAll(), mallEntity.getDiscountConponMoney() ) ) );  //支付金额
					    mallEntity.setUnitPrice( formatNumber( BigDecimalUtil.div( mallEntity.getBalanceMoney(), mallEntity.getNumber() ) ) );
					    mallEntity.setCanUseConpon( 1 ); //能使用优惠券
					    mallEntity.setTotalMoneyAll( formatNumber( BigDecimalUtil.sub( mallEntity.getTotalMoneyAll(), mallEntity.getDiscountConponMoney() ) ) );
					    mallList.put( mallEntity.getMallId(), mallEntity );

					    //门店订单数据
					    canUseConponByShopId = 1;  //门店商品是否使用优惠券标示
					    discountConponMoneyByShopId = formatNumber(
							    BigDecimalUtil.add( discountConponMoneyByShopId, mallEntity.getDiscountConponMoney() ) );  //该门店商品会员优惠金额
					}
				    }
				}
			    }
			}
		    }
		}
	    }
	}
	//<!-------------------end----------------------------->
	//保存门店优惠信息
	mallNotShopEntity.setDiscountMemberMoney( discountMemberMoneyByShopId ); //会员优惠
	mallNotShopEntity.setDiscountConponMoney( discountConponMoneyByShopId );  //优惠券优惠金额
	mallNotShopEntity.setCodes( codesByShopId );
	mallNotShopEntity.setCanUseConpon( canUseConponByShopId );  //门店商品是否使用优惠券标示
	mallNotShopEntity.setCouponNum( couponNumByShopId );

	//保存总订单优惠信息

	//<!-----------------粉币计算start---------------------------->
	if ( isMemberCard && mallNotShopEntity.getUseFenbi() == 1 ) {
	    SortedMap<String, Object> dict = dictService.getDict( "1058" );
	    Integer fcount = 0; //能抵扣的粉币商品数量
	    Integer fcount1 = 0; //能抵扣的粉币商品数量
	    Double fenbiFenTanAll = 0.0;
	    double fenbiMoney = currencyCount( 0.0, memberEntity.getFansCurrency() );
	    Double discountfenbiMoneyByShopId = 0.0;
	    Double balanceMoneyByShopId = 0.0;
	    Double fenbiBanlance = 0.0;  //订单金额
	    if ( fenbiMoney > 0 ) {
		Map< Object,MallEntity > mallEntityMap = mallNotShopEntity.getMalls();
		for ( MallEntity mallEntity : mallEntityMap.values() ) {
		    //判断能使用粉币的商品信息
		    if ( mallEntity.getUseFenbi() == 1 ) {
			fenbiBanlance = formatNumber( BigDecimalUtil.add( fenbiBanlance, mallEntity.getTotalMoneyAll() ) );
			fcount++;
		    }
		}

		if ( fenbiBanlance >= 10 )
		    //计算粉币分摊结果
		    for ( MallEntity mallEntity : mallEntityMap.values() ) {
			if ( mallEntity.getUseFenbi() == 1 ) {
			    Double fenbiFenTan = 0.0;
			    //处理误差
			    if ( fcount == fcount1 + 1 ) {
				if ( fenbiBanlance > fenbiMoney ) {
				    fenbiFenTan = formatNumber( BigDecimalUtil.sub( fenbiMoney, fenbiFenTanAll ) );
				} else {
				    fenbiFenTan = formatNumber( BigDecimalUtil.sub( fenbiBanlance, fenbiFenTanAll ) );
				}
			    } else {
				if ( fenbiBanlance >= fenbiMoney ) {
				    //消费金额大于粉币金额
				    fenbiFenTan = formatNumber( BigDecimalUtil.mul( BigDecimalUtil.div( mallEntity.getTotalMoneyAll(), fenbiBanlance ), fenbiMoney ) );
				} else {
				    fenbiFenTan = formatNumber( BigDecimalUtil.mul( BigDecimalUtil.div( mallEntity.getTotalMoneyAll(), fenbiBanlance ), fenbiBanlance ) );
				}
				fenbiFenTanAll = formatNumber( BigDecimalUtil.add( fenbiFenTanAll, fenbiFenTan ) );
			    }
			    //商品数据
			    mallEntity.setDiscountfenbiMoney( fenbiFenTan );
			    Double fenbiNum = memberCommonService.deductFenbi( dict, fenbiFenTan );
			    mallEntity.setFenbiNum( fenbiNum );
			    mallEntity.setCanUsefenbi( 1 );
			    mallEntity.setBalanceMoney( formatNumber( BigDecimalUtil.sub( mallEntity.getTotalMoneyAll(), fenbiFenTan ) ) );
			    mallEntity.setTotalMoneyAll( formatNumber( BigDecimalUtil.sub( mallEntity.getTotalMoneyAll(), fenbiFenTan ) ) );
			    mallEntityMap.put( mallEntity.getMallId(), mallEntity );

			    //门店订单数据
			    discountfenbiMoneyByShopId = formatNumber( BigDecimalUtil.add( discountfenbiMoneyByShopId, mallEntity.getDiscountfenbiMoney() ) );
			    balanceMoneyByShopId = formatNumber( BigDecimalUtil.add( balanceMoneyByShopId, mallEntity.getBalanceMoney() ) );

			    fcount1++;
			} else {
			    balanceMoneyByShopId = formatNumber( BigDecimalUtil.add( balanceMoneyByShopId, mallEntity.getTotalMoneyAll() ) );
			}
		    }
		mallNotShopEntity.setDiscountfenbiMoney( discountfenbiMoneyByShopId );
		mallNotShopEntity.setBalanceMoney( balanceMoneyByShopId );
		mallNotShopEntity.setMalls( mallEntityMap );
	    }
	    if ( fenbiMoney > 0 && fenbiBanlance >= 10.0 ) {
		Double fenbiNumByAll = memberCommonService.deductFenbi( dict, discountfenbiMoneyByShopId );
		mallNotShopEntity.setFenbiNum( fenbiNumByAll );
		mallNotShopEntity.setDiscountfenbiMoney( discountfenbiMoneyByShopId );
		mallNotShopEntity.setCanUsefenbi( 1 );
	    }
	}
	//<!-----------------粉币计算end---------------------------->

	//<!-----------------积分计算start---------------------------->
	if ( isMemberCard && mallNotShopEntity.getUserJifen() == 1 ) {
	    PublicParameterset pps = publicParameterSetMapper.findBybusId( memberEntity.getBusId() );
	    Double discountJifenMoneyByShopId = 0.0;
	    Double jifenBanlance = 0.0;  //订单金额
	    Double balanceMoneyByShopId = 0.0;
	    Integer jcount = 0; //能抵扣的粉币商品数量
	    Integer jcount1 = 0; //能抵扣的粉币商品数量
	    Double jifenFenTanAll = 0.0;

	    Map< Object,MallEntity > mallEntityMap = mallNotShopEntity.getMalls();
	    for ( MallEntity mallEntity : mallEntityMap.values() ) {
		//判断能使用粉币的商品信息
		if ( mallEntity.getUserJifen() == 1 ) {
		    jifenBanlance = formatNumber( BigDecimalUtil.add( jifenBanlance, mallEntity.getTotalMoneyAll() ) );
		    jcount++;
		}
	    }

	    Double jifenMoney = memberCommonService.integralCount( jifenBanlance, memberEntity.getIntegral().doubleValue(), memberEntity.getBusId() );

	    if ( jifenMoney > 0 ) {
		//计算积分分摊结果
		for ( MallEntity mallEntity : mallEntityMap.values() ) {
		    if ( mallEntity.getUserJifen() == 1 ) {
			Double jifenFenTan = 0.0;
			if ( jcount == jcount1 + 1 ) {
			    if ( jifenBanlance >= jifenMoney ) {
				//消费金额大于粉币金额
				jifenFenTan = formatNumber( BigDecimalUtil.sub( jifenMoney, jifenFenTanAll ) );
			    } else {
				jifenFenTan = formatNumber( BigDecimalUtil.sub( jifenBanlance, jifenFenTanAll ) );
			    }
			} else {
			    if ( jifenBanlance >= jifenMoney ) {
				//消费金额大于粉币金额
				jifenFenTan = formatNumber( BigDecimalUtil.mul( BigDecimalUtil.div( mallEntity.getTotalMoneyAll(), jifenBanlance ), jifenMoney ) );
			    } else {
				jifenFenTan = formatNumber( BigDecimalUtil.mul( BigDecimalUtil.div( mallEntity.getTotalMoneyAll(), jifenBanlance ), jifenBanlance ) );
			    }
			    jifenFenTanAll = formatNumber( BigDecimalUtil.add( jifenFenTanAll, jifenFenTan ) );
			}
			//商品数据
			mallEntity.setDiscountjifenMoney( jifenFenTan );
			Double jifenNum = memberCommonService.deductJifen( pps, jifenFenTan, memberEntity.getBusId() );
			mallEntity.setJifenNum( jifenNum );
			mallEntity.setCanUseJifen( 1 );
			mallEntity.setBalanceMoney( formatNumber( BigDecimalUtil.sub( mallEntity.getTotalMoneyAll(), jifenFenTan ) ) );
			mallEntity.setTotalMoneyAll( formatNumber( BigDecimalUtil.sub( mallEntity.getTotalMoneyAll(), jifenFenTan ) ) );
			mallEntityMap.put( mallEntity.getMallId(), mallEntity );

			//门店订单数据
			discountJifenMoneyByShopId = formatNumber( BigDecimalUtil.add( discountJifenMoneyByShopId, mallEntity.getDiscountjifenMoney() ) );
			balanceMoneyByShopId = formatNumber( BigDecimalUtil.add( balanceMoneyByShopId, mallEntity.getBalanceMoney() ) );

			jcount1++;
		    } else {
			balanceMoneyByShopId = formatNumber( BigDecimalUtil.add( balanceMoneyByShopId, mallEntity.getTotalMoneyAll() ) );
		    }
		}
		mallNotShopEntity.setDiscountjifenMoney( discountJifenMoneyByShopId );
		mallNotShopEntity.setBalanceMoney( balanceMoneyByShopId );
		mallNotShopEntity.setMalls( mallEntityMap );
	    }
	    if ( jifenMoney > 0 ) {
		Integer jifenNumByAll = memberCommonService.deductJifen( pps, jifenMoney, memberEntity.getBusId() ).intValue();
		mallNotShopEntity.setJifenNum( jifenNumByAll );
		mallNotShopEntity.setDiscountjifenMoney( jifenMoney );
		mallNotShopEntity.setCanUseJifen( 1 );
	    }
	}
	// <!-----------------积分计算end---------------------------->

	Double balanceMoneyByAll = 0.0;
	Double balanceMoneyByShopId = 0.0;
	Map< Object,MallEntity > mallEntitys = mallNotShopEntity.getMalls();
	for ( MallEntity mallEntity : mallEntitys.values() ) {
	    //未任何优惠商品支付金额重新赋值一次
	    if ( mallEntity.getBalanceMoney() <= 0 ) {
		mallEntity.setBalanceMoney( mallEntity.getTotalMoneyAll() );
	    }
	    balanceMoneyByShopId = formatNumber( BigDecimalUtil.add( balanceMoneyByShopId, mallEntity.getBalanceMoney() ) );
	}

	mallNotShopEntity.setBalanceMoney(balanceMoneyByShopId);
	Long end = new Date().getTime();
	System.out.println( "用时:" + ( end - start ) + "毫秒" );
	return mallNotShopEntity;
    }

    /**
     * 不跨门店 联盟卡
     *
     * @param mallNotShopEntity
     *
     * @return
     */
    public MallNotShopEntity leagueCount( MallNotShopEntity mallNotShopEntity ) {
	Long start = new Date().getTime();
	MemberCard card = null;
	MemberEntity memberEntity = memberMapper.selectById( mallNotShopEntity.getMemberId() );

	Double leagueMoney = 0.0;  //联盟卡优惠
	boolean isUseDisCount = false;  //实付使用折扣
	WxCardReceive wxCardReceive = null;
	WxCard wxcard = null;
	DuofenCardGet dfget = null;
	DuofenCard dfcard = null;
	List< Map< String,Object > > dfcg = null;

	//计算门店下使用了优惠券商品信息
	Double discountMemberMoneyByShopId = 0.0; //门店会员优惠券金额
	String codesByShopId = "";  //门店使用优惠券code值 用来核销卡券 不存在set
	Integer couponNumByShopId = 1;  //门店使用优惠券数量
	Integer canUseConponByShopId = 0;  //门店是否能用优惠券
	Double discountConponMoneyByShopId = 0.0;   //门店优惠券优惠券金额
	Map< Object,MallEntity > mallList = mallNotShopEntity.getMalls();  //门店下商品信息
	Integer useCoupon = mallNotShopEntity.getUseCoupon(); //是否使用了优惠券
	Integer couponType = mallNotShopEntity.getCouponType();  //优惠券类型 0微信 1多粉
	Integer coupondId = mallNotShopEntity.getCoupondId();   //优惠券id
	Double discountLinshi = 0.0;  //折扣优惠券的折扣值 0到1
	Double reduceCose = 0.0;
	if ( useCoupon == 1 ) {
	    if ( couponType == 0 ) {
		// 微信优惠券
		wxCardReceive = wxCardReceiveMapper.selectById( coupondId );
		wxcard = wxCardMapper.selectByCardId( wxCardReceive.getCardId() );
		if ( "DISCOUNT".equals( wxcard.getCardType() ) ) {
		    isUseDisCount = true;
		    discountLinshi = wxcard.getDiscount() / 10;
		} else {
		    reduceCose = wxcard.getReduceCost();
		}
		codesByShopId = wxCardReceive.getUserCardCode();
	    } else if ( couponType == 1 ) {
		// 多粉优惠券
		dfget = duofenCardGetMapper.selectById( coupondId );
		dfcard = duofenCardMapper.selectById( dfget.getCardId() );
		if ( dfcard.getCardType() == 0 ) {
		    isUseDisCount = true;
		    discountLinshi = dfcard.getDiscount() / 10;
		}
		if ( dfcard.getAddUser() == 1 ) {
		    // 允许叠加使用
		    dfcg = duofenCardGetMapper.findByCardId( dfcard.getId(), mallNotShopEntity.getMemberId(), 0 );
		}
		codesByShopId = dfget.getCode();
	    }

	    //<!-------------------start----------------------------->
	    if ( ( useCoupon == 0 && mallNotShopEntity.getUserLeague() == 1 ) || ( useCoupon == 1 && !isUseDisCount ) ) {
		//未使用优惠券 使用联盟卡 计算优惠
		for ( MallEntity mallEntity : mallList.values() ) {
		    if ( mallEntity.getUserCard() == 1 ) {
			//会员卡优惠
			Double leagueMoneyByOne = formatNumber(
					BigDecimalUtil.mul( mallEntity.getTotalMoneyOne(), BigDecimalUtil.sub( 1, mallNotShopEntity.getLeagueDiscount() ) ) );
			Double unitPrice = formatNumber( BigDecimalUtil.sub( mallEntity.getTotalMoneyOne(), leagueMoneyByOne ) );
			//商品数据
			mallEntity.setUnitPrice( unitPrice );
			mallEntity.setLeagueMoney( formatNumber( BigDecimalUtil.mul( leagueMoneyByOne, mallEntity.getNumber() ) ) );
			mallEntity.setBalanceMoney( formatNumber( BigDecimalUtil.mul( unitPrice, mallEntity.getNumber() ) ) );  //商品订单总金额
			mallEntity.setTotalMoneyAll( formatNumber( BigDecimalUtil.sub( mallEntity.getTotalMoneyAll(), mallEntity.getLeagueMoney() ) ) );
			mallEntity.setCanUserCard( 1 );
			mallList.put( mallEntity.getMallId(), mallEntity );  //数据处理完归还

			//总订单联盟优惠数据
			leagueMoney = formatNumber( BigDecimalUtil.add( leagueMoney, BigDecimalUtil.mul( leagueMoneyByOne, mallEntity.getNumber() ) ) );
		    }
		}

	    } else if ( useCoupon == 1 ) {
		if ( isUseDisCount ) {
		    //折扣优惠券处理业务
		    for ( MallEntity mallEntity : mallList.values() ) {
			//挑选出能使用优惠券的商品
			if ( mallEntity.getUseCoupon() == 1 ) {
			    //单个商品优惠券金额
			    Double discountConponMoneyByOne = formatNumber( BigDecimalUtil.mul( mallEntity.getTotalMoneyOne(), 1 - discountLinshi ) );
			    Double unitPrice = formatNumber( mallEntity.getTotalMoneyOne() - discountConponMoneyByOne );

			    //商品数据
			    mallEntity.setUnitPrice( unitPrice );  //当个商品价格
			    mallEntity.setDiscountConponMoney( formatNumber( BigDecimalUtil.mul( discountConponMoneyByOne, mallEntity.getNumber() ) ) );
			    mallEntity.setBalanceMoney( formatNumber( BigDecimalUtil.mul( unitPrice, mallEntity.getNumber() ) ) );
			    mallEntity.setTotalMoneyAll( formatNumber( BigDecimalUtil.sub( mallEntity.getTotalMoneyAll(), mallEntity.getDiscountConponMoney() ) ) );
			    mallEntity.setCanUseConpon( 1 ); //能使用优惠券
			    mallList.put( mallEntity.getMallId(), mallEntity ); //归还到分离的数据中去

			    //门店订单数据
			    canUseConponByShopId = 1;  //门店商品是否使用优惠券标示
			    discountConponMoneyByShopId = formatNumber( BigDecimalUtil.add( discountConponMoneyByShopId, mallEntity.getDiscountConponMoney() ) );
			}
		    }
		}
		//<!-------联盟卡优惠已结束-------->

		//<!---------减免券start------------>
		Double balanceMoneyBili = 0.0;//门店订单支付金额
		Integer couponCount = 0;
		Integer couponCount1 = 0;
		Double couponCountFentan = 0.0;
		for ( MallEntity mallEntity : mallList.values() ) {  //商品信息
		    //挑选出能使用优惠券的商品
		    if ( mallEntity.getUseCoupon() == 1 ) {
			balanceMoneyBili = formatNumber( BigDecimalUtil.add( balanceMoneyBili, mallEntity.getTotalMoneyAll() ) );
			couponCount++;
		    }
		}

		// 计算微信减免券优惠
		Double discountConponMoneyByMall = 0.0;
		if ( useCoupon == 1 ) {
		    //<!-------start-------->
		    if ( couponType == 0 ) {
			for ( MallEntity mallEntity : mallList.values() ) {
			    if ( mallEntity.getUseCoupon() == 1 ) {
				if ( balanceMoneyBili >= wxcard.getCashLeastCost() ) {
				    //商品数据
				    //减免金额分摊
				    couponCount1++;
				    if ( couponCount1 == couponCount ) {
					mallEntity.setDiscountConponMoney( BigDecimalUtil.sub( reduceCose, couponCountFentan ) );
				    } else {
					discountConponMoneyByMall = formatNumber(
							BigDecimalUtil.mul( BigDecimalUtil.div( mallEntity.getTotalMoneyAll(), balanceMoneyBili ), reduceCose ) );  //分摊金额
					mallEntity.setDiscountConponMoney( discountConponMoneyByMall );
				    }
				    couponCountFentan = couponCountFentan + discountConponMoneyByMall;

				    mallEntity.setBalanceMoney( formatNumber( BigDecimalUtil.sub( mallEntity.getTotalMoneyAll(), mallEntity.getDiscountConponMoney() ) ) );
				    mallEntity.setUnitPrice( formatNumber( BigDecimalUtil.div( mallEntity.getBalanceMoney(), mallEntity.getNumber() ) ) );
				    mallEntity.setCanUseConpon( 1 ); //能使用优惠券
				    mallEntity.setTotalMoneyAll( formatNumber( BigDecimalUtil.sub( mallEntity.getTotalMoneyAll(), mallEntity.getDiscountConponMoney() ) ) );
				    mallList.put( mallEntity.getMallId(), mallEntity );

				    //门店订单数据
				    canUseConponByShopId = 1;  //门店商品是否使用优惠券标示
				    discountConponMoneyByShopId = formatNumber(
						    BigDecimalUtil.add( discountConponMoneyByShopId, mallEntity.getDiscountConponMoney() ) );  //该门店商品会员优惠金额

				}
			    }
			}
			//<!-------end-------->

		    } else if ( couponType == 1 ) {
			//计算多粉优惠券减免券优惠
			reduceCose = dfcard.getReduceCost();
			for ( MallEntity mallEntity : mallList.values() ) {
			    if ( mallEntity.getUseCoupon() == 1 ) {
				// 减免券
				if ( dfcard.getAddUser() == 0 ) {
				    // 不允许叠加使用
				    if ( balanceMoneyBili >= dfcard.getCashLeastCost() ) {
					//商品数据
					//减免金额分摊
					couponCount1++;
					if ( couponCount1 == couponCount ) {
					    discountConponMoneyByMall = BigDecimalUtil.sub( reduceCose, couponCountFentan );
					} else {
					    discountConponMoneyByMall = formatNumber(
							    BigDecimalUtil.mul( BigDecimalUtil.div( mallEntity.getTotalMoneyAll(), balanceMoneyBili ), reduceCose ) );  //分摊金额
					    mallEntity.setDiscountConponMoney( discountConponMoneyByMall );
					}
					couponCountFentan = couponCountFentan + discountConponMoneyByMall;

					mallEntity.setDiscountConponMoney( discountConponMoneyByMall );
					mallEntity.setBalanceMoney(
							formatNumber( BigDecimalUtil.sub( mallEntity.getTotalMoneyAll(), mallEntity.getDiscountConponMoney() ) ) );  //支付金额
					mallEntity.setUnitPrice( formatNumber( BigDecimalUtil.div( mallEntity.getBalanceMoney(), mallEntity.getNumber() ) ) );
					mallEntity.setCanUseConpon( 1 ); //能使用优惠券
					mallEntity.setTotalMoneyAll( formatNumber( BigDecimalUtil.sub( mallEntity.getTotalMoneyAll(), mallEntity.getDiscountConponMoney() ) ) );
					mallList.put( mallEntity.getMallId(), mallEntity );

					//门店订单数据
					canUseConponByShopId = 1;  //门店商品是否使用优惠券标示
					discountConponMoneyByShopId = formatNumber(
							BigDecimalUtil.add( discountConponMoneyByShopId, mallEntity.getDiscountConponMoney() ) );  //该门店商品会员优惠金额

				    }
				} else {
				    Integer num = 0;
				    Integer shiNum = 0;
				    // 满足使用条件
				    if ( balanceMoneyBili >= dfcard.getCashLeastCost() ) {
					if ( dfcard.getCashLeastCost() > 0 ) {
					    num = BigDecimalUtil.divInteger( balanceMoneyBili, dfcard.getCashLeastCost() ); // 能使用优惠券数量
					}
					// 允许叠加使用
					if ( dfcg.size() > 0 ) {
					    for ( int i = 0; i < dfcg.size(); i++ ) {
						if ( i <= num ) {
						    codesByShopId += dfcg.get( i ).get( "code" ) + ",";
						    shiNum = i;
						}
					    }
					    num = shiNum;
					    couponNumByShopId = num;
					    mallNotShopEntity.setCouponNum( num );  //优惠券使用数量
					    reduceCose = formatNumber( BigDecimalUtil.mul( dfcard.getReduceCost(), num ) );
					    //减免金额分摊
					    couponCount1++;
					    if ( couponCount1 == couponCount ) {
						mallEntity.setDiscountConponMoney( BigDecimalUtil.sub( reduceCose, couponCountFentan ) );
					    } else {
						discountConponMoneyByMall = formatNumber(
								BigDecimalUtil.mul( BigDecimalUtil.div( mallEntity.getTotalMoneyAll(), balanceMoneyBili ), reduceCose ) );  //分摊金额
						mallEntity.setDiscountConponMoney( discountConponMoneyByMall );
					    }
					    couponCountFentan = couponCountFentan + discountConponMoneyByMall;

					    //商品数据
					    mallEntity.setDiscountConponMoney( discountConponMoneyByMall );
					    mallEntity.setDiscountConponMoney( discountConponMoneyByMall );
					    mallEntity.setBalanceMoney(
							    formatNumber( BigDecimalUtil.sub( mallEntity.getTotalMoneyAll(), mallEntity.getDiscountConponMoney() ) ) );  //支付金额
					    mallEntity.setUnitPrice( formatNumber( BigDecimalUtil.div( mallEntity.getBalanceMoney(), mallEntity.getNumber() ) ) );
					    mallEntity.setCanUseConpon( 1 ); //能使用优惠券
					    mallEntity.setTotalMoneyAll( formatNumber( BigDecimalUtil.sub( mallEntity.getTotalMoneyAll(), mallEntity.getDiscountConponMoney() ) ) );
					    mallList.put( mallEntity.getMallId(), mallEntity );

					    //门店订单数据
					    canUseConponByShopId = 1;  //门店商品是否使用优惠券标示
					    discountConponMoneyByShopId = formatNumber(
							    BigDecimalUtil.add( discountConponMoneyByShopId, mallEntity.getDiscountConponMoney() ) );  //该门店商品会员优惠金额

					}
				    }
				}
			    }

			}
		    }
		}
	    }

	    //<!-------------------end----------------------------->
	    //保存门店优惠信息
	    mallNotShopEntity.setDiscountConponMoney( discountConponMoneyByShopId );  //优惠券优惠金额
	    mallNotShopEntity.setCodes( codesByShopId );
	    mallNotShopEntity.setCanUseConpon( canUseConponByShopId );  //门店商品是否使用优惠券标示
	    mallNotShopEntity.setCouponNum( couponNumByShopId );

	    if ( mallNotShopEntity.getUserLeague() == 1 ) {
		Double jifenBanlance = 0.0;  //订单金额
		Double balanceMoneyByShopId = 0.0;
		Integer jcount = 0; //能抵扣的联盟积分商品数量
		Integer jcount1 = 0; //能抵扣的联盟积分商品数量
		Double leaguejifenFenTanAll = 0.0;

		Map< Object,MallEntity > mallEntityMap = mallNotShopEntity.getMalls();
		for ( MallEntity mallEntity : mallEntityMap.values() ) {
		    //判断能使用积分的商品信息
		    if ( mallEntity.getUseLeague() == 1 ) {
			jifenBanlance = formatNumber( BigDecimalUtil.add( jifenBanlance, mallEntity.getTotalMoneyAll() ) );
			jcount++;
		    }
		}

		Double jifenMoney = mallNotShopEntity.getLeagueJifen() / 100.0;  //联盟最高能抵扣金额

		if ( jifenMoney > 0 ) {
		    //计算积分分摊结果
		    for ( MallEntity mallEntity : mallEntityMap.values() ) {
			if ( mallEntity.getUseLeague() == 1 ) {
			    Double jifenFenTan = 0.0;
			    if ( jcount == jcount1 + 1 ) {
				if ( jifenBanlance >= jifenMoney ) {
				    //消费金额大于粉币金额
				    jifenFenTan = formatNumber( BigDecimalUtil.sub( jifenMoney, leaguejifenFenTanAll ) );
				} else {
				    jifenFenTan = formatNumber( BigDecimalUtil.sub( jifenBanlance, leaguejifenFenTanAll ) );
				}
			    } else {
				if ( jifenBanlance >= jifenMoney ) {
				    //消费金额大于粉币金额
				    jifenFenTan = formatNumber( BigDecimalUtil.mul( BigDecimalUtil.div( mallEntity.getTotalMoneyAll(), jifenBanlance ), jifenMoney ) );
				} else {
				    jifenFenTan = formatNumber( BigDecimalUtil.mul( BigDecimalUtil.div( mallEntity.getTotalMoneyAll(), jifenBanlance ), jifenBanlance ) );
				}
				leaguejifenFenTanAll = formatNumber( BigDecimalUtil.add( leaguejifenFenTanAll, jifenFenTan ) );
			    }
			    //商品数据
			    mallEntity.setLeagueMoney( jifenFenTan );
			    mallEntity.setLeagueJifen( BigDecimalUtil.mul( jifenFenTan, 100 ) );
			    mallEntity.setCanUseLeagueJifen( 1 );
			    mallEntity.setBalanceMoney( formatNumber( BigDecimalUtil.sub( mallEntity.getTotalMoneyAll(), jifenFenTan ) ) );
			    mallEntity.setTotalMoneyAll( formatNumber( BigDecimalUtil.sub( mallEntity.getTotalMoneyAll(), jifenFenTan ) ) );
			    mallEntityMap.put( mallEntity.getMallId(), mallEntity );

			    //总订单数据
			    jcount1++;
			} else {
			    balanceMoneyByShopId = formatNumber( BigDecimalUtil.add( balanceMoneyByShopId, mallEntity.getTotalMoneyAll() ) );
			}
		    }
		    mallNotShopEntity.setBalanceMoney( balanceMoneyByShopId );
		    mallNotShopEntity.setMalls( mallEntityMap );

		}
		// <!-----------------积分计算end---------------------------->
	    }
	}

	Double balanceMoneyByAll = 0.0;
	Double leagueMoneyAll = 0.0;
	Double leagueJifenNumAll = 0.0;

	Double balanceMoneyByShopId = 0.0;
	Map< Object,MallEntity > mallEntitys = mallNotShopEntity.getMalls();
	for ( MallEntity mallEntity : mallEntitys.values() ) {
	    //未任何优惠商品支付金额重新赋值一次
	    if ( mallEntity.getBalanceMoney() <= 0 ) {
		mallEntity.setBalanceMoney( mallEntity.getTotalMoneyAll() );
	    }
	    balanceMoneyByShopId = formatNumber( BigDecimalUtil.add( balanceMoneyByShopId, mallEntity.getBalanceMoney() ) );
	    leagueMoneyAll = mallEntity.getLeagueMoney();
	    leagueJifenNumAll = mallEntity.getLeagueJifen();
	}

	mallNotShopEntity.setBalanceMoney( balanceMoneyByShopId );

	balanceMoneyByAll = formatNumber( BigDecimalUtil.add( balanceMoneyByAll, mallNotShopEntity.getBalanceMoney() ) );

	mallNotShopEntity.setBalanceMoney( balanceMoneyByAll );
	mallNotShopEntity.setLeagueJifenNum( leagueJifenNumAll.intValue() );
	mallNotShopEntity.setLeagueMoney( leagueMoneyAll );
	Long end = new Date().getTime();
	System.out.println( "用时:" + ( end - start ) + "毫秒" );
	return mallNotShopEntity;

    }
}