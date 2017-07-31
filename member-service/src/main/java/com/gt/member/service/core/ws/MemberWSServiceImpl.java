/**
 * P 2016年4月5日
 */
package com.gt.member.service.core.ws;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import javax.jws.WebService;


import com.gt.member.dao.*;
import com.gt.member.entity.*;
import com.gt.member.service.old.common.dict.DictService;
import com.gt.member.service.old.member.MemberCardService;
import com.gt.member.service.old.member.SystemMsgService;
import com.gt.member.util.*;
import com.gt.member.util.token.TokenUitl;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author pengjiangli
 * @version 创建时间:2016年4月5日
 */
@WebService( serviceName = "memberWS")
@Component
public class MemberWSServiceImpl implements MemberWSService {

    @Autowired
    private MemberConfig memberConfig;

    private final String getFenbiSurplus   = "/basics/79B4DE7C/getFenbiSurplus.do";
    private final String updateFenbiReduce =  "/basics/79B4DE7C/updateFenbiReduce.do";
    //获取门店信息
    private final String GET_SHOP_URL      = "/basics/79B4DE7C/findShopById.do";
    //微信卡券核销
    private final String CODE_CONSUME      = "/basics/79B4DE7C/codeConsume.do";
    //获取主门店信息
    private final String MAIN_SHOP         = "/basics/79B4DE7C/selectMainShopByBusId.do";
    //获取门店集合信息
    private final String shopsList         = "/dict/79B4DE7C/shopsList.do";

  //保存粉币资产记录
   private final String saveFenbiFlowRecord =  "/basics/79B4DE7C/saveFenbiFlowRecord.do";




   private static final Logger LOG = Logger.getLogger( MemberWSServiceImpl.class );

    @Autowired
    private MemberCardDAO cardMapper;

    @Autowired
    private MemberDAO memberMapper;

    @Autowired
    private MemberGiveruleDAO giveRuleMapper;

    @Autowired
    private UserConsumeDAO userConsumeMapper;

    @Autowired
    private MemberGiverulegoodstypeDAO giveRuleGoodsTypeMapper;

    @Autowired
    private MemberGiveconsumeDAO giveConsumeMapper;

    @Autowired
    private WxPublicUsersDAO wxPublicUsersMapper;

    @Autowired
    private BusUserDAO busUserMapper;

    @Autowired
    private MemberCardrecordDAO cardRecordMapper;

    @Autowired
    private MemberRechargegiveDAO rechargeGiveMapper;

    @Autowired
    private MemberGradetypeDAO gradeTypeMapper;

    @Autowired
    private MemberCardbuyDAO cardBuyMapper;

    @Autowired
    private MemberDateDAO memberDateMapper;

    @Autowired
    private MemberCardLentDAO cardLentMapper;

    @Autowired
    private PublicParametersetDAO publicParameterSetMapper;

    @Autowired
    private DictService dictService;

    @Autowired
    private SystemMsgService systemMsgService;

    @Autowired
    private MemberAppletOpenidDAO memberAppletOpenidMapper;

    @Autowired
    private MemberOldDAO memberOldMapper;

    @Autowired
    private MemberCardService memberCardService;

    @Autowired
    private WxCardReceiveDAO wxCardReceiveMapper;

    @Autowired
    private DuofenCardGetDAO duofenCardGetMapper;

    @Autowired
    private MemberRecommendDAO recommendMapper;

    /**
     * 查询赠送规则
     *
     * @param busId
     * @param gtId
     * @param ctId
     *
     * @return
     */
    public MemberGiverule findGive( Integer busId, Integer gtId, Integer ctId ) {
	MemberGiverule gr = giveRuleMapper.findBybusIdAndGtIdAndCtId( busId, gtId, ctId );
	return gr;
    }

    /**
     * 根据订单号添加赠送物品记录
     *
     * @param orderId  订单号
     * @param itemName 物品名称
     * @param type     0不送赠送物品 1赠送物品
     *
     * @throws Exception
     */
    @Transactional( rollbackFor = Exception.class )
    public void findGiveRule( String phone, String orderId, String itemName, byte type ) throws Exception {
	List< Map< String,Object > > ucs = userConsumeMapper.findByOrderCode( orderId );
	if ( CommonUtil.isEmpty( ucs ) || ucs.size() == 0 || ucs.size() > 1 ) {
	    LOG.error( "赠送物品查询订单出现异常" );
	    throw new Exception();
	}

	try {
	    Integer busUserId = Integer.parseInt( ucs.get( 0 ).get( "busUserId" ).toString() );

	    Integer gtId = Integer.parseInt( ucs.get( 0 ).get( "gt_id" ).toString() );
	    Integer ctId = Integer.parseInt( ucs.get( 0 ).get( "ctId" ).toString() );
	    double price = Double.parseDouble( ucs.get( 0 ).get( "discountMoney" ).toString() );

	    Integer recFreezeType = 0;
	    switch ( ctId ) {
		case 1:
		    recFreezeType = 20;
		    break;
		case 2:
		    recFreezeType = 21;
		    break;
		case 3:
		    recFreezeType = 22;
		    break;
		case 4:
		    recFreezeType = 23;
		    break;
		case 5:
		    recFreezeType = 24;
		    break;

		default:
		    break;
	    }

	    JSONObject json = new JSONObject();
	    json.put( "token", TokenUitl.getToken() );
	    json.put( "busId", busUserId );
	    json.put( "rec_type", 1 );
	    json.put( "fre_type", recFreezeType );
	    json.put( "fkId", ctId );

	    // 查询粉笔数量
	    Integer fenbi = 0;
	    JSONObject returnJSON = HttpClienUtil.httpPost( memberConfig.getWxmp_home()+getFenbiSurplus, json, false );
	    if ( "1".equals( returnJSON.get( "code" ) ) ) {
		fenbi = returnJSON.getInt( "data" );
	    }

	    // 如果是折扣卡 金额用折后金额
	    if ( ctId == 2 ) {
		price = Double.parseDouble( ucs.get( 0 ).get( "discountMoney" ).toString() );
	    }
	    // 如果是次卡 次数默认为金额
	    if ( ctId == 5 ) {
		price = Double.parseDouble( ucs.get( 0 ).get( "uccount" ).toString() );
	    }
	    String recordType = ucs.get( 0 ).get( "recordType" ).toString();

	    Integer ucId = Integer.parseInt( ucs.get( 0 ).get( "id" ).toString() );
	    // 如果是次卡 和 储值卡 就查询第一个等级的赠送规则
	    MemberGiverule gr = null;
	    if ( ctId == 5 || ctId == 3 ) {
		List< Map< String,Object > > grs = giveRuleMapper.findByBusIdAndCtId( busUserId, ctId );
		gr = new MemberGiverule();
		if ( grs.size() != 0 ) {
		    gr.setGrId( CommonUtil.toInteger( grs.get( 0 ).get( "gr_id" ) ) );
		}
	    } else {
		gr = giveRuleMapper.findBybusIdAndGtIdAndCtId( busUserId, gtId, ctId );
	    }

	    Double fans_currency = 0.0;// 粉笔
	    int integral = 0; // 积分
	    int flow = 0;

	    MemberDate memberday = findMemeberDate( busUserId, ctId );
	    boolean flag = false; // 表示今天是否是会员日
	    if ( CommonUtil.isNotEmpty( memberday ) ) {
		flag = true;
	    }

	    if ( type == 1 ) {
		if ( CommonUtil.isNotEmpty( gr ) ) {
		    List< Map< String,Object > > grgts = giveRuleGoodsTypeMapper.findByGrId( gr.getGrId() );
		    MemberGiveconsume giveConsume = null;
		    MemberGiverulegoodstype grgt = null;
		    for ( Map< String,Object > map : grgts ) {
			giveConsume = new MemberGiveconsume();
			if ( CommonUtil.isEmpty( map.get( "gId" ) ) ) continue;
			if ( "1".equals( map.get( "gId" ).toString() ) ) {
			    if ( "1".equals( map.get( "give_type" ).toString() ) ) {
				// 积分
				if ( CommonUtil.isEmpty( map.get( "money" ) ) ) continue;
				Double money = Double.parseDouble( map.get( "money" ).toString() );
				int count = (int) Math.floor( price / money );
				if ( count == 0 ) continue;
				if ( CommonUtil.isEmpty( map.get( "number" ) ) ) continue;
				int num = count * Integer.parseInt( map.get( "number" ).toString() );
				Integer upperLmit = Integer.parseInt( map.get( "upperLmit" ).toString() );
				if ( upperLmit != 0 ) {
				    num = num > upperLmit ? upperLmit : num;
				}
				// 会员日 积分赠送
				if ( flag ) {
				    num = num * memberday.getIntegral();
				}

				giveConsume.setGcTotal( num );
				giveConsume.setGtId( Integer.parseInt( map.get( "gId" ).toString() ) );
				giveConsume.setGtName( map.get( "gt_name" ).toString() );
				giveConsume.setGtUnit( map.get( "gt_unit" ).toString() );
				giveConsume.setUcId( ucId );
				giveConsume.setMemberId( CommonUtil.toInteger( ucs.get( 0 ).get( "memberId" ) ) );
				giveConsume.setSendDate( new Date() );
				giveConsumeMapper.insert( giveConsume );
				integral = num;
			    }
			} else {
			    // 添加赠送物品记录
			    Integer upperLmit = Integer.parseInt( map.get( "upperLmit" ).toString() );

			    if ( "2".equals( map.get( "gId" ).toString() ) || "3".equals( map.get( "gId" ).toString() ) || upperLmit > 0 ) {
				Integer count = Integer.parseInt( map.get( "number" ).toString() );
				Double money = Double.parseDouble( map.get( "money" ).toString() );
				if ( price < money ) continue;
				if ( upperLmit < count ) {
				    // 扣除商家粉币数量
				    if ( "3".equals( map.get( "gId" ).toString() ) ) {
					if ( fenbi < count ) {
					    continue;
					}
					// 会员日 粉币赠送
					if ( flag ) {
					    count = count * memberday.getFansCurrency();
					}

					giveConsume.setGcTotal( count );

					JSONObject requestJson = new JSONObject();
					requestJson.put( "token", TokenUitl.getToken() );
					requestJson.put( "busId", busUserId );
					requestJson.put( "rec_type", count );
					requestJson.put( "fre_type", recFreezeType );
					requestJson.put( "fkId", ctId );
					// 查询粉笔数量
					HttpClienUtil.httpPost( memberConfig.getWxmp_home()+updateFenbiReduce, requestJson, false );
					fans_currency = (double) count;
				    } else if ( "2".equals( map.get( "gId" ).toString() ) ) {
					Integer flowCount = Integer.parseInt( map.get( "number" ).toString() );
					// 会员日赠送流量
					if ( flag ) {
					    flowCount = flowCount * memberday.getFlow();
					    giveConsume.setGcTotal( flowCount );
					    flow = flowCount * memberday.getFlow();
					} else {
					    giveConsume.setGcTotal( flowCount );
					    flow = flowCount;
					}

				    }
				    // 上限非等于0 认为是商家自定义物品
				    if ( upperLmit != 0 ) {
					giveConsume.setGcTotal( upperLmit );
				    }
				} else {
				    giveConsume.setGcTotal( count );
				}

				giveConsume.setGtId( Integer.parseInt( map.get( "gId" ).toString() ) );
				giveConsume.setGtName( map.get( "gt_name" ).toString() );
				giveConsume.setGtUnit( map.get( "gt_unit" ).toString() );
				giveConsume.setUcId( ucId );
				giveConsume.setMemberId( CommonUtil.toInteger( ucs.get( 0 ).get( "memberId" ) ) );
				giveConsume.setSendDate( new Date() );
				giveConsumeMapper.insert( giveConsume );

				if ( !"2".equals( map.get( "gId" ).toString() ) && !"3".equals( map.get( "gId" ).toString() ) ) {
				    // 修改赠送规则物品剩余数量(商家自定义物品)
				    grgt = new MemberGiverulegoodstype();
				    grgt.setGrId( Integer.parseInt( map.get( "gr_id" ).toString() ) );
				    grgt.setGtId( Integer.parseInt( map.get( "gId" ).toString() ) );

				    if ( upperLmit < count ) {
					grgt.setUpperLmit( 0 );
					grgt.setGiveType( 2 );
				    } else {
					grgt.setUpperLmit( upperLmit - count );
				    }
				    giveRuleGoodsTypeMapper.updateById( grgt );
				}
			    }
			}
		    }
		}
	    }

	    if ( CommonUtil.isNotEmpty( ucs.get( 0 ).get( "mcId" ) ) ) {
		MemberCard card = cardMapper.selectById( Integer.parseInt( ucs.get( 0 ).get( "mcId" ).toString() ) );

		// 修改会员的流量 粉笔 积分信息
		Member member1 = memberMapper.findByMcIdAndbusId( card.getBusId(), Integer.parseInt( ucs.get( 0 ).get( "mcId" ).toString() ) );
		// 消费 积分为负数 改为正数
		if ( integral < 0 ) {
		    integral = -integral;
		}

		if ( CommonUtil.isNotEmpty( member1 ) ) {
		    Member member = new Member();
		    member.setId( member1.getId() );
		    member.setFansCurrency( member1.getFansCurrency() + fans_currency );
		    member.setFlow( member1.getFlow() + flow );
		    member.setIntegral( member1.getIntegral() + integral );
		    member.setFlowDate( new Date() );
		    member.setIntegralDate( new Date() );
		    member.setTotalIntegral( member1.getTotalIntegral() + integral );
		    if ( ctId == 5 ) {
			if ( CommonUtil.isNotEmpty( ucs.get( 0 ).get( "totalMoney" ) ) ) {
			    price = Double.parseDouble( ucs.get( 0 ).get( "totalMoney" ).toString() );
			}
		    }
		    member.setTotalMoney( member1.getTotalMoney() + price );
		    try {
			memberMapper.updateById( member );
		    } catch ( Exception e ) {
			e.printStackTrace();
		    }
		}
		Map< String,Object > map = null;
		// 判断时效卡升级
		if ( ctId == 4 ) {
		    map = findNextGradeCtId4( busUserId, gtId, price );
		} else if ( card.getApplyType() != 4 ) { // 泛会员升级
		    // 判断会员是否是要升级
		    map = findNextGrade( busUserId, ctId, gtId, member1.getTotalIntegral() + integral, member1.getTotalMoney() + price );
		}

		// 用来标示该价格正负
		if ( !"1".equals( recordType ) ) {
		    price = -price;
		}

		double balance = 0.0;
		if ( CommonUtil.isNotEmpty( card ) && CommonUtil.isNotEmpty( card.getMoney() ) ) {
		    balance = card.getMoney();
		    if ( "3".equals( CommonUtil.toString( ucs.get( 0 ).get( "paymentType" ) ) ) || "5".equals( CommonUtil.toString( ucs.get( 0 ).get( "paymentType" ) ) ) ) {
			card.setMoney( balance + price > 0 ? balance + price : 0 );
		    }

		    if ( CommonUtil.isNotEmpty( ucs.get( 0 ).get( "uccount" ) ) ) {
			Integer uccount = Integer.parseInt( ucs.get( 0 ).get( "uccount" ).toString() );
			if ( ctId == 5 ) {
			    if ( CommonUtil.isNotEmpty( ucs.get( 0 ).get( "giftCount" ) ) ) {
				Integer giftCount = Integer.parseInt( ucs.get( 0 ).get( "giftCount" ).toString() );
				uccount = uccount + giftCount;
			    }
			}
			if ( uccount != 0 ) {
			    card.setFrequency( card.getFrequency() - uccount );
			}
		    }
		    // 修改会员卡等级和赠送规则
		    if ( CommonUtil.isNotEmpty( map ) ) {
			card.setGtId( Integer.parseInt( map.get( "gtId" ).toString() ) );
			card.setGrId( Integer.parseInt( map.get( "grId" ).toString() ) );

			// 升级通知
			systemMsgService.upgradeMemberMsg( member1, card.getCardNo(),
					CommonUtil.isEmpty( card.getExpireDate() ) ? "长期有效" : DateTimeKit.format( card.getExpireDate() ) );
		    }
		    cardMapper.updateById( card );
		}
		if ( card.getCtId() == 5 ) {
		    if ( "1".equals( ucs.get( 0 ).get( "recordType" ).toString() ) ) {
			saveCardRecordNew( Integer.parseInt( ucs.get( 0 ).get( "mcId" ).toString() ), (byte) 1,
					ucs.get( 0 ).get( "uccount" ) + "次,送" + ucs.get( 0 ).get( "giftcount" ) + "次", itemName, member1.getBusId(), card.getFrequency().toString(),
					card.getCtId(), 0.0 );
		    } else {
			if ( "0".equals( CommonUtil.toString( ucs.get( 0 ).get( "uccount" ) ) ) ) {
			    saveCardRecordNew( Integer.parseInt( ucs.get( 0 ).get( "mcId" ).toString() ), (byte) 1, price + "元", itemName, member1.getBusId(),
					    card.getFrequency().toString(), card.getCtId(), 0.0 );
			} else {
			    saveCardRecordNew( Integer.parseInt( ucs.get( 0 ).get( "mcId" ).toString() ), (byte) 1, ucs.get( 0 ).get( "uccount" ) + "次", itemName,
					    member1.getBusId(), card.getFrequency().toString(), card.getCtId(), 0.0 );
			}

		    }
		} else {
		    saveCardRecordNew( Integer.parseInt( ucs.get( 0 ).get( "mcId" ).toString() ), (byte) 1, price + "元", itemName, member1.getBusId(), card.getMoney().toString(),
				    card.getCtId(), 0.0 );
		}
	    }
	} catch ( Exception e ) {
	    LOG.error( "添加赠送记录数据查询异常异常", e );
	    throw new Exception();
	}
    }

    /**
     * @param busId
     * @param gtId
     * @param totalmoney
     */
    public Map< String,Object > findNextGradeCtId4( Integer busId, Integer gtId, Double totalmoney ) {
	Map< String,Object > map = new HashMap< String,Object >();
	List< Map< String,Object > > giveRules = giveRuleMapper.findByBusIdAndCtId( busId, 4 );
	if ( giveRules.size() != 0 ) {
	    for ( Map< String,Object > giveRule : giveRules ) {
		if ( CommonUtil.isNotEmpty( giveRule.get( "gr_rechargeMoney" ) ) ) {
		    Double rechargeMoney = CommonUtil.toDouble( giveRule.get( "gr_rechargeMoney" ) );
		    if ( totalmoney.equals( rechargeMoney ) ) {
			map.put( "gtId", giveRule.get( "gt_id" ) );
			map.put( "grId", giveRule.get( "gr_id" ) );
			return map;
		    }
		}
	    }
	}
	return null;
    }

    /**
     * 判断是否升级
     * <p>
     * 公众号
     *
     * @param ctId       卡类型
     * @param gtId       等级id
     * @param integral   累计积分
     * @param totalmoney 累计消费金额
     *
     * @return
     */
    @Override
    public Map< String,Object > findNextGrade( Integer busId, Integer ctId, Integer gtId, Integer integral, double totalmoney ) {
	Map< String,Object > map = new HashMap< String,Object >();
	// <!--查询下一个等级start-->
	List< Map< String,Object > > gradeTypes = gradeTypeMapper.findByCtId( busId, ctId );

	if ( gradeTypes != null ) {
	    for ( int i = 0; i < gradeTypes.size(); i++ ) {
		if ( CommonUtil.isNotEmpty( gradeTypes.get( i ).get( "gtId" ) ) ) {
		    if ( gtId.equals( gradeTypes.get( i ).get( "gtId" ) ) ) {
			if ( i < gradeTypes.size() - 1 ) {
			    // 下一级id
			    if ( CommonUtil.isNotEmpty( gradeTypes.get( i + 1 ).get( "gtId" ) ) ) {
				Integer id = Integer.parseInt( gradeTypes.get( i + 1 ).get( "gtId" ).toString() );
				MemberGiverule nextGiveRule = giveRuleMapper.findBybusIdAndGtIdAndCtId( busId, id, Integer.parseInt( ctId.toString() ) );
				if ( CommonUtil.isEmpty( nextGiveRule ) ) {
				    break;
				}
				// 金额升级
				if ( 0 == nextGiveRule.getGrUpgradeType() ) {
				    if ( totalmoney >= nextGiveRule.getGrUpgradeCount() ) {
					map.put( "gtId", id );
					map.put( "grId", nextGiveRule.getGrId() );
					return map;
				    }
				}
				if ( 1 == nextGiveRule.getGrUpgradeType() ) {
				    if ( integral >= nextGiveRule.getGrUpgradeCount() ) {
					map.put( "gtId", id );
					map.put( "grId", nextGiveRule.getGrId() );
					return map;
				    }
				}
			    }
			}
		    }
		}
	    }

	    MemberGiverule nextGiveRule = giveRuleMapper.findBybusIdAndGtIdAndCtId( busId, gtId, Integer.parseInt( ctId.toString() ) );
	    map.put( "gtId", gtId );
	    map.put( "grId", nextGiveRule.getGrId() );
	    return map;
	}
	return null;
    }

    /**
     * 添加会员卡记录((新数据接口))
     *
     * @param cardId     卡类型id
     * @param recordType 消费类型
     * @param number     数量
     * @param itemName   物品名称
     * @param balance    余额
     */
    @Override
    public MemberCardrecord saveCardRecordNew( Integer cardId, Byte recordType, String number, String itemName, Integer busId, String balance, Integer ctId, double amount ) {
	MemberCardrecord cr = memberCardService.saveCardRecordNew( cardId, recordType, number, itemName, busId, balance, ctId, amount );
	return cr;
    }

    /**
     * 会员记录表 修改支付状态
     *
     * @param id
     * @param payStatus 支付状态 0未支付 1已支付 2支付失败
     */
    public void updateUserConsume( Integer id, Byte payStatus ) {
	UserConsume uc = new UserConsume();
	uc.setId( id );
	uc.setPayStatus( payStatus.intValue() );
	userConsumeMapper.updateById( uc );
    }

    /**
     * 根据赠送规则和商户id查询
     */
    public int findRechargegive( double price, Integer grId, Integer busId, Integer ctId ) {
	MemberDate memberdate = findMemeberDate( busId, ctId );
	List< Map< String,Object > > rechargeGives = null;
	if ( CommonUtil.isNotEmpty( memberdate ) ) {
	    rechargeGives = rechargeGiveMapper.findBybusIdAndGrId( busId, grId, 1 );
	} else {
	    rechargeGives = rechargeGiveMapper.findBybusIdAndGrId( busId, grId, 0 );
	}
	if ( rechargeGives == null || rechargeGives.size() == 0 ) {
	    return 0;
	}
	for ( int i = 0; i < rechargeGives.size(); i++ ) {
	    if ( i + 1 == rechargeGives.size() ) {
		double money = Double.parseDouble( rechargeGives.get( i ).get( "money" ).toString() );
		if ( money <= price ) {
		    return Integer.parseInt( rechargeGives.get( i ).get( "giveCount" ).toString() );
		} else {
		    return 0;
		}
	    }
	    if ( CommonUtil.isNotEmpty( rechargeGives.get( i ).get( "money" ) ) ) {
		double money = Double.parseDouble( rechargeGives.get( i ).get( "money" ).toString() );
		double money1 = Double.parseDouble( rechargeGives.get( i + 1 ).get( "money" ).toString() );
		if ( price >= money && price < money1 ) {
		    return Integer.parseInt( rechargeGives.get( i ).get( "giveCount" ).toString() );
		}
	    }

	}
	return 0;
    }

    /**
     * 会员回调接口
     */
    @Override
    public void memberCallBack( String orderId, Byte payStatus ) {
	try {

	    LOG.error( "订单单号 ：" + orderId );
	    List< Map< String,Object > > ucs = userConsumeMapper.findByOrderCode( orderId );
	    try {
		if ( payStatus == 1 ) {

		    memberGive( orderId, (byte) 1 );

		    if ( CommonUtil.isNotEmpty( ucs.get( 0 ).get( "mcId" ) ) ) {
			MemberCard card = cardMapper.selectById( Integer.parseInt( ucs.get( 0 ).get( "mcId" ).toString() ) );

			// 修改会员的流量 粉笔 积分信息
			Member member = memberMapper.findByMcIdAndbusId( card.getBusId(), Integer.parseInt( ucs.get( 0 ).get( "mcId" ).toString() ) );

			if ( card.getCtId() == 5 ) {
			    String uccount = "";
			    if ( CommonUtil.isNotEmpty( ucs.get( 0 ).get( "giftCount" ) ) ) {
				uccount = ucs.get( 0 ).get( "uccount" ) + "次,送" + ucs.get( 0 ).get( "giftCount" ) + "次";
			    } else {
				uccount = ucs.get( 0 ).get( "uccount" ) + "次";
			    }
			    saveCardRecordNew( Integer.parseInt( ucs.get( 0 ).get( "mcId" ).toString() ), (byte) 1, uccount, "充值", member.getBusId(),
					    card.getFrequency().toString(), card.getCtId(), 0.0 );
			} else {
			    saveCardRecordNew( Integer.parseInt( ucs.get( 0 ).get( "mcId" ).toString() ), (byte) 1,
					    ucs.get( 0 ).get( "totalMoney" ) + "元,送" + ucs.get( 0 ).get( "giftCount" ) + "元", "充值", member.getBusId(), card.getMoney().toString(),
					    card.getCtId(), 0.0 );
			}

			// 消息模板推送
			if ( card.getCtId() == 3 ) {
			    systemMsgService.sendChuzhiCard( member, CommonUtil.toDouble( ucs.get( 0 ).get( "totalMoney" ) ) );
			} else if ( card.getCtId() == 5 ) {
			    systemMsgService.sendCikaCard( member, CommonUtil.toDouble( ucs.get( 0 ).get( "totalMoney" ) ), CommonUtil.toInteger( ucs.get( 0 ).get( "uccount" ) ) );
			}

		    }
		}
	    } catch ( Exception e ) {
		LOG.error( "发放赠送物品异常", e );
	    }
	} catch ( Exception e ) {
	    LOG.error( "回调异常", e );
	}

    }

    /**
     * 查询赠送信息
     *
     * @param orderId
     * @param payStatus
     *
     * @throws Exception
     */
    public void memberGive( String orderId, Byte payStatus ) throws Exception {
	List< Map< String,Object > > ucs = userConsumeMapper.findByOrderCode( orderId );
	if ( CommonUtil.isEmpty( ucs ) || ucs.size() == 0 || ucs.size() > 1 ) {
	    LOG.error( "支付查询订单出现异常" );
	    return;
	}

	Integer id = Integer.parseInt( ucs.get( 0 ).get( "id" ).toString() );
	updateUserConsume( id, payStatus );
	try {
	    if ( payStatus == 1 ) {
		if ( CommonUtil.isNotEmpty( ucs.get( 0 ).get( "mcId" ) ) ) {
		    MemberCard card = cardMapper.selectById( Integer.parseInt( ucs.get( 0 ).get( "mcId" ).toString() ) );

		    // 判断是否是会员日
		    MemberDate membetdate = findMemeberDate( card.getBusId(), card.getCtId() );

		    double balance = 0.0;
		    if ( CommonUtil.isNotEmpty( card ) && CommonUtil.isNotEmpty( card.getMoney() ) ) {
			if ( card.getCtId() == 4 ) {
			    // 时效卡
			    Double totalMoney = Double.parseDouble( ucs.get( 0 ).get( "totalMoney" ).toString() );

			    List< Integer > dateCount = findTimeCard( totalMoney, card.getBusId(), membetdate );
			    if ( dateCount == null ) {
				throw new Exception();
			    }
			    if ( dateCount.size() == 0 ) {
				card.setExpireDate( null );
			    } else {
				Date expireDate = card.getExpireDate();
				if ( expireDate == null ) {
				    card.setExpireDate( DateTimeKit.addMonths( dateCount.get( 0 ) ) );
				} else {
				    if ( DateTimeKit.laterThanNow( card.getExpireDate() ) ) {
					card.setExpireDate( DateTimeKit.addMonths( expireDate, dateCount.get( 0 ) ) );
				    } else {
					card.setExpireDate( DateTimeKit.addMonths( new Date(), dateCount.get( 0 ) ) );
				    }
				}

				// 会员日延期多少天
				if ( CommonUtil.isNotEmpty( membetdate ) ) {
				    card.setExpireDate( DateTimeKit.addDate( card.getExpireDate(), dateCount.get( 0 ) ) );
				}
			    }
			} else {
			    // 次卡和储值卡充值 修改卡片信息
			    balance = card.getMoney();
			    if ( CommonUtil.isNotEmpty( ucs.get( 0 ).get( "totalMoney" ) ) ) {
				Double totalMoney = Double.parseDouble( ucs.get( 0 ).get( "totalMoney" ).toString() );
				if ( CommonUtil.isNotEmpty( totalMoney ) && totalMoney != 0 && CommonUtil.isNotEmpty( ucs.get( 0 ).get( "giftCount" ) ) ) {
				    Double giftCount = Double.parseDouble( ucs.get( 0 ).get( "giftCount" ).toString() );
				    if ( card.getCtId() == 3 ) {
					totalMoney = totalMoney + giftCount;
					card.setMoney( balance + totalMoney );
				    }
				}
			    }

			    if ( CommonUtil.isNotEmpty( ucs.get( 0 ).get( "uccount" ) ) ) {
				Integer uccount = Integer.parseInt( ucs.get( 0 ).get( "uccount" ).toString() );
				if ( uccount != 0 ) {
				    Integer giftCount = Integer.parseInt( ucs.get( 0 ).get( "giftCount" ).toString() );
				    uccount = uccount + giftCount;
				    card.setFrequency( card.getFrequency() + uccount );
				}
			    }
			}

			cardMapper.updateById( card );
		    }
		}

	    }
	} catch ( Exception e ) {
	    LOG.error( "发放赠送物品异常" );
	    throw new Exception();
	}

    }

    /**
     * 查询时效卡的有效时间
     *
     * @param money
     *
     * @return
     */
    public List< Integer > findTimeCard( Double money, Integer busId, MemberDate memberDate ) {
	List< Map< String,Object > > giveRules = giveRuleMapper.findByBusIdAndCtId( busId, 4 );
	List< Integer > times = new ArrayList< Integer >();
	if ( giveRules.size() == 0 ) {
	    return null;
	}
	for ( Map< String,Object > map : giveRules ) {
	    if ( CommonUtil.isNotEmpty( map.get( "gr_rechargeMoney" ) ) ) {
		Double rechargeMoney = CommonUtil.toDouble( map.get( "gr_rechargeMoney" ) );

		if ( money.equals( rechargeMoney ) ) {
		    times.add( CommonUtil.toInteger( map.get( "gr_validDate" ) ) );
		    if ( CommonUtil.isNotEmpty( memberDate ) ) {
			times.add( CommonUtil.toInteger( map.get( "delayDay" ) ) );
		    }
		    return times;
		}
	    }
	}
	return null;
    }

    @Override
    public Map< String,Object > findCardType( Integer memberId ) {
	Map< String,Object > map = new HashMap< String,Object >();
	map.put( "isCz", false );
	if ( CommonUtil.isEmpty( memberId ) ) {
	    map.put( "result", false );
	    map.put( "message", "会员id为空" );
	    return map;
	}
	Member member = memberMapper.selectById( memberId );
	if ( CommonUtil.isEmpty( member ) || CommonUtil.isEmpty( member.getMcId() ) ) {
	    map.put( "result", false );
	    map.put( "message", "未找到该会员" );
	    return map;
	}
	MemberCard card = cardMapper.selectById( member.getMcId() );
	if ( CommonUtil.isEmpty( card ) ) {
	    map.put( "result", false );
	    map.put( "message", "未找到该会员" );
	    return map;
	}

	if ( card.getCtId() == 2 ) {
	    MemberDate memberDate = findMemeberDate( member.getBusId(), 2 );

	    MemberGiverule giveRule = giveRuleMapper.selectById( card.getGrId() );
	    map.put( "result", true );
	    map.put( "message", "折扣卡" );
	    if ( CommonUtil.isNotEmpty( memberDate ) ) {
		map.put( "memberDate", true );
		map.put( "discount", new Double( giveRule.getGrDiscount() * memberDate.getDiscount() ) / 1000 );
	    } else {
		map.put( "discount", new Double( giveRule.getGrDiscount() ) / 100 );
	    }
	    return map;
	}
	if ( card.getCtId() == 3 ) {
	    map.put( "isCz", true );
	}
	map.put( "result", true );
	map.put( "message", "非折扣卡" );
	map.put( "discount", 1.0 );
	return map;
    }

    /**
     * 会员储值卡消费
     */
    public Map< String,Object > storePay( Integer memberId, double totalMoney ) {
	Map< String,Object > map = new HashMap< String,Object >();
	Member member = memberMapper.selectById( memberId );
	if ( CommonUtil.isEmpty( member.getMcId() ) ) {
	    map.put( "result", 0 );
	    map.put( "message", "非储值会员" );
	    return map;
	}
	MemberCard card = cardMapper.selectById( member.getMcId() );
	if ( CommonUtil.isNotEmpty( card ) ) {
	    if ( card.getCtId() == 3 ) {
		if ( card.getMoney() < totalMoney ) {
		    map.put( "result", 1 );
		    map.put( "message", "余额不足请充值" );
		    return map;
		}

		double banlan = card.getMoney() - totalMoney;
		card.setMoney( banlan );
		cardMapper.updateById( card );
		map.put( "result", 2 );
		map.put( "message", "支付成功" );
		saveCardRecordNew( card.getMcId(), (byte) 1, "-" + totalMoney, "储值卡消费", member.getBusId(), null, 0, -totalMoney );
		systemMsgService.sendChuzhiXiaofei( member, totalMoney );
		return map;
	    }
	}

	map.put( "result", 0 );
	map.put( "message", "非储值会员" );
	return map;

    }

    /**
     * 可供商家选择的会员卡
     */
    @Override
    public List< MemberCardtype > findMemberCard_1( Integer busId ) {
	List< MemberCardtype > mapList = new ArrayList< MemberCardtype >();
	MemberCardtype ct = null;

	List< Map< String,Object > > gradeTypes = gradeTypeMapper.findBybusId1( busId );
	if ( CommonUtil.isNotEmpty( gradeTypes ) && gradeTypes.size() > 0 ) {
	    for ( Map< String,Object > map : gradeTypes ) {
		ct = new MemberCardtype();
		ct.setCtId( CommonUtil.toInteger( map.get( "ctId" ) ) );
		ct.setCtName( CommonUtil.toString( map.get( "ctName" ) ) );
		mapList.add( ct );
	    }
	}

	return mapList;
    }

    /**
     * 判断用户是否是会员 false 不是 true 是
     */
    @Override
    public boolean isMemember( Integer memberId ) {
	Member member = memberMapper.selectById( memberId );
	if ( CommonUtil.isNotEmpty( member ) && CommonUtil.isNotEmpty( member.getMcId() ) ) {
	    MemberCard card = cardMapper.selectById( member.getMcId() );
	    if ( card.getIsChecked() == 0 || card.getCardStatus() == 1 ) {
		return false;
	    } else {
		return true;
	    }
	}
	return false;
    }

    /**
     * 判断用户的卡类型 1积分卡 2折扣卡 3储值卡 4时效卡 5次卡
     *
     * @param memberId
     *
     * @return
     */
    public Integer isCardType( Integer memberId ) {
	if ( CommonUtil.isEmpty( memberId ) ) {
	    return -1;
	}
	Member member = memberMapper.selectById( memberId );
	if ( CommonUtil.isEmpty( memberId ) || CommonUtil.isEmpty( member.getMcId() ) ) {
	    return -1;
	}
	MemberCard card = cardMapper.selectById( member.getMcId() );
	if ( CommonUtil.isEmpty( card ) ) {
	    return -1;
	}
	return card.getCtId();
    }

    /**
     * 获取card信息
     *
     * @param memberId
     *
     * @return
     */
    public MemberCard findCardByMemberId( Integer memberId ) {
	if ( CommonUtil.isEmpty( memberId ) ) {
	    return null;
	}
	Member member = memberMapper.selectById( memberId );
	if ( CommonUtil.isEmpty( memberId ) || CommonUtil.isEmpty( member.getMcId() ) ) {
	    return null;
	}
	MemberCard card = cardMapper.selectById( member.getMcId() );
	return card;
    }

    /**
     * 根据粉丝id 查询 赠送
     */
    @Override
    public Map< String,Object > findCardGiveRule( Integer memberId, Double totalMoney ) {
	Map< String,Object > map = new HashMap< String,Object >();
	if ( !isMemember( memberId ) ) {
	    map.put( "result", false );
	    map.put( "message", "非会员" );
	    return map;
	}

	// 根据会员id查询赠送规则
	Member member = memberMapper.selectById( memberId );
	MemberCard card = cardMapper.selectById( member.getMcId() );
	MemberGiverule giveRule = giveRuleMapper.selectById( card.getGrId() );
	switch ( card.getCtId() ) {
	    case 1:
		// 积分卡 查询赠送积分
		//		GiveRuleGoodsTypeKey key = new GiveRuleGoodsTypeKey();
		//		key.setGrId( card.getGrId() );
		//		key.setGtId( 1 );
		//		GiveRuleGoodsType grgt = giveRuleGoodsTypeMapper.selectById( key );
		//		Double count = totalMoney / grgt.getMoney();
		//		Integer number = count.intValue() * grgt.getNumber();
		//		map.put( "result", true );
		//		map.put( "ctId", 1 );
		//		map.put( "number", number );
		//		return map;
	    case 2:
		// 查询折扣
		double discount = giveRule.getGrDiscount() / 100.0;
		map.put( "result", true );
		map.put( "ctId", 2 );
		map.put( "discount", discount );
		map.put( "disCountmoney", totalMoney * discount );
		return map;
	    case 3:
		// 查询剩余金额
		map.put( "result", true );
		map.put( "ctId", 3 );
		map.put( "balance", card.getMoney() );
		return map;
	    default:
		break;
	}

	map.put( "result", false );
	map.put( "message", "会员,非指定卡类型" );
	return null;
    }

    /**
     * 根据订单号添加赠送物品记录 延迟送
     *
     * @param orderNo 订单号
     * @param phone   手机号码
     *
     * @throws Exception
     */
    @Transactional( rollbackFor = Exception.class )
    public void findGiveRuleDelay( String phone, String orderNo ) throws Exception {
	List< Map< String,Object > > ucs = userConsumeMapper.findByOrderCode( orderNo );
	if ( CommonUtil.isEmpty( ucs ) || ucs.size() == 0 || ucs.size() > 1 ) {
	    LOG.error( "赠送物品查询订单出现异常" );
	    throw new Exception();
	}

	try {

	    Integer busUserId = Integer.parseInt( ucs.get( 0 ).get( "busUserId" ).toString() );

	    Integer gtId = Integer.parseInt( ucs.get( 0 ).get( "gt_id" ).toString() );
	    Integer ctId = Integer.parseInt( ucs.get( 0 ).get( "ctId" ).toString() );
	    double price = Double.parseDouble( ucs.get( 0 ).get( "totalMoney" ).toString() );

	    // 判断是否是会员日
	    MemberDate memberDate = findMemeberDate( busUserId, ctId );
	    boolean flag = false; // 表示今天是否是会员日
	    if ( CommonUtil.isNotEmpty( memberDate ) ) {
		flag = true;
	    }

	    Integer recFreezeType = 0;
	    switch ( ctId ) {
		case 1:
		    recFreezeType = 20;
		    break;
		case 2:
		    recFreezeType = 21;
		    break;
		case 3:
		    recFreezeType = 22;
		    break;
		case 4:
		    recFreezeType = 23;
		    break;
		case 5:
		    recFreezeType = 24;
		    break;

		default:
		    break;
	    }

	    JSONObject json = new JSONObject();
	    json.put( "token", TokenUitl.getToken() );
	    json.put( "busId", busUserId );
	    json.put( "rec_type", 1 );
	    json.put( "fre_type", recFreezeType );
	    json.put( "fkId", ctId );

	    // 查询粉笔数量
	    Integer fenbi = 0;
	    JSONObject returnJSON = HttpClienUtil.httpPost( memberConfig.getWxmp_home()+getFenbiSurplus, json, false );
	    if ( "1".equals( returnJSON.get( "code" ) ) ) {
		fenbi = returnJSON.getInt( "data" );
	    }
	    // 如果是折扣卡 金额用折后金额
	    if ( ctId == 2 ) {
		price = Double.parseDouble( ucs.get( 0 ).get( "discountMoney" ).toString() );
	    }
	    // 如果是次卡 次数默认为金额
	    if ( ctId == 5 ) {
		price = Double.parseDouble( ucs.get( 0 ).get( "uccount" ).toString() );
	    }
	    Integer ucId = Integer.parseInt( ucs.get( 0 ).get( "id" ).toString() );
	    // 如果是次卡 和 储值卡 就查询第一个等级的赠送规则
	    MemberGiverule gr = null;
	    if ( ctId == 5 || ctId == 3 ) {
		List< Map< String,Object > > grs = giveRuleMapper.findByBusIdAndCtId( busUserId, ctId );
		gr = new MemberGiverule();
		if ( grs.size() != 0 ) {
		    gr.setGrId( CommonUtil.toInteger( grs.get( 0 ).get( "gr_id" ) ) );
		}
	    } else {
		gr = giveRuleMapper.findBybusIdAndGtIdAndCtId( busUserId, gtId, ctId );
	    }

	    if ( CommonUtil.isNotEmpty( gr ) ) {
		List< Map< String,Object > > grgts = giveRuleGoodsTypeMapper.findByGrId( gr.getGrId() );
		MemberGiveconsume giveConsume = null;
		MemberGiverulegoodstype grgt = null;
		for ( Map< String,Object > map : grgts ) {
		    giveConsume = new MemberGiveconsume();
		    if ( CommonUtil.isEmpty( map.get( "gId" ) ) ) continue;
		    if ( "1".equals( map.get( "gId" ).toString() ) ) {
			if ( "1".equals( map.get( "give_type" ).toString() ) ) {
			    // 积分
			    if ( CommonUtil.isEmpty( map.get( "money" ) ) ) continue;
			    Double money = Double.parseDouble( map.get( "money" ).toString() );
			    int count = (int) Math.floor( price / money );
			    if ( count == 0 ) continue;
			    if ( CommonUtil.isEmpty( map.get( "number" ) ) ) continue;
			    int num = count * Integer.parseInt( map.get( "number" ).toString() );
			    Integer upperLmit = Integer.parseInt( map.get( "upperLmit" ).toString() );
			    if ( upperLmit != 0 ) {

				num = num > upperLmit ? upperLmit : num;
			    }

			    // 会员日赠送翻倍
			    if ( flag ) {
				num = num * memberDate.getIntegral();
			    }

			    // 添加赠送物品记录
			    giveConsume.setGcTotal( num );
			    giveConsume.setGtId( Integer.parseInt( map.get( "gId" ).toString() ) );
			    giveConsume.setGtName( map.get( "gt_name" ).toString() );
			    giveConsume.setGtUnit( map.get( "gt_unit" ).toString() );
			    giveConsume.setUcId( ucId );
			    giveConsume.setMemberId( CommonUtil.toInteger( ucs.get( 0 ).get( "memberId" ) ) );
			    giveConsume.setSendType( 0 );
			    giveConsume.setSendDate( new Date() );
			    giveConsumeMapper.insert( giveConsume );
			}
		    } else {

			Integer upperLmit = Integer.parseInt( map.get( "upperLmit" ).toString() );

			if ( "2".equals( map.get( "gId" ).toString() ) || "3".equals( map.get( "gId" ).toString() ) || upperLmit > 0 ) {
			    Integer count = Integer.parseInt( map.get( "number" ).toString() );
			    Double money = Double.parseDouble( map.get( "money" ).toString() );
			    if ( price < money ) continue;
			    if ( upperLmit < count ) {
				// 扣除商家粉币数量
				if ( "3".equals( map.get( "gId" ).toString() ) ) {
				    // 会员日赠送翻倍
				    if ( flag ) {
					count = count * memberDate.getFansCurrency();
				    }

				    if ( fenbi < count ) {
					continue;
				    }
				    giveConsume.setGcTotal( count );
				    // 冻结商家粉笔数量
				    JSONObject requestJson = new JSONObject();
				    requestJson.put( "token", TokenUitl.getToken() );
				    requestJson.put( "busId", busUserId );
				    requestJson.put( "rec_type", count );
				    requestJson.put( "fre_type", recFreezeType );
				    requestJson.put( "fkId", ctId );
				    // 查询粉笔数量
				    HttpClienUtil.httpPost( memberConfig.getWxmp_home()+updateFenbiReduce, requestJson, false );

				} else if ( "2".equals( map.get( "gId" ).toString() ) ) {

				    Integer flowCount = Integer.parseInt( map.get( "number" ).toString() );
				    if ( flag ) {
					flowCount = flowCount * memberDate.getFlow();
				    }

				    giveConsume.setGcTotal( flowCount );
				}
				// 上限非等于0 认为是商家自定义物品
				if ( upperLmit != 0 ) {
				    giveConsume.setGcTotal( upperLmit );
				}
			    } else {
				giveConsume.setGcTotal( count );
			    }

			    giveConsume.setGtId( Integer.parseInt( map.get( "gId" ).toString() ) );
			    giveConsume.setGtName( map.get( "gt_name" ).toString() );
			    giveConsume.setGtUnit( map.get( "gt_unit" ).toString() );
			    giveConsume.setUcId( ucId );
			    giveConsume.setMemberId( CommonUtil.toInteger( ucs.get( 0 ).get( "memberId" ) ) );
			    giveConsume.setSendDate( new Date() );
			    giveConsume.setSendType( 0 );
			    giveConsumeMapper.insert( giveConsume );

			    if ( !"2".equals( map.get( "gId" ).toString() ) && !"3".equals( map.get( "gId" ).toString() ) ) {
				// 修改赠送规则物品剩余数量(商家自定义物品)
				grgt = new MemberGiverulegoodstype();
				grgt.setGrId( Integer.parseInt( map.get( "gr_id" ).toString() ) );
				grgt.setGtId( Integer.parseInt( map.get( "gId" ).toString() ) );

				if ( upperLmit < count ) {
				    grgt.setUpperLmit( 0 );
				    grgt.setGiveType( 2 );
				} else {
				    grgt.setUpperLmit( upperLmit - count );
				}
				giveRuleGoodsTypeMapper.updateById( grgt );
			    }
			}
		    }
		}
	    }

	    if ( CommonUtil.isNotEmpty( ucs.get( 0 ).get( "mcId" ) ) ) {
		MemberCard card = cardMapper.selectById( Integer.parseInt( ucs.get( 0 ).get( "mcId" ).toString() ) );

		Double money = CommonUtil.toDouble( ucs.get( 0 ).get( "discountMoney" ) );

		saveCardRecordNew( CommonUtil.toInteger( ucs.get( 0 ).get( "mcId" ) ), (byte) 1, money + "元", "消费", card.getBusId(), card.getMoney().toString(), card.getCtId(),
				0.0 );

	    }
	} catch ( Exception e ) {
	    e.printStackTrace();
	    LOG.error( "添加赠送记录数据查询异常异常", e );
	    throw new Exception();
	}
    }

    /**
     * 支付回调
     *
     * @param phone
     * @param orderCode
     *
     * @throws Exception
     */
    public void backPay( String phone, String orderCode ) throws Exception {
	List< Map< String,Object > > ucs = userConsumeMapper.findByOrderCode( orderCode );
	if ( CommonUtil.isEmpty( ucs ) || ucs.size() == 0 || ucs.size() > 1 ) {
	    LOG.error( "支付回调查询订单出现异常" );
	    return;
	}
	findGiveRuleDelay( phone, orderCode );
    }

    /**
     * 储值卡退款订单
     *
     * @param memberId    订单号
     * @param refundMoney 退款金额
     */
    public Map< String,Object > chargeBack( Integer memberId, double refundMoney ) {
	Map< String,Object > map = new HashMap< String,Object >();
	try {
	    Member member = memberMapper.selectById( memberId );
	    if ( CommonUtil.isEmpty( member ) || CommonUtil.isEmpty( member.getMcId() ) ) {
		map.put( "result", false );
		map.put( "message", "未找到该会员数据" );
		return map;
	    }
	    MemberCard card = cardMapper.selectById( member.getMcId() );
	    MemberCard card1 = new MemberCard();
	    // 储值卡
	    card1.setMcId( member.getMcId() );
	    card1.setMoney( card.getMoney() + refundMoney );
	    cardMapper.updateById( card1 );
	    map.put( "result", true );
	    map.put( "message", "退款成功" );

	    saveCardRecordNew( card.getMcId(), (byte) 1, refundMoney + "", "储值卡退款", card.getBusId(), null, 0, 0 );
	    systemMsgService.sendChuzhiTuikuan( member, refundMoney );

	} catch ( Exception e ) {
	    map.put( "result", false );
	    map.put( "message", "退款异常" );
	    e.printStackTrace();
	}
	return map;
    }

    /**
     * 退款成功 回调 添加card操作记录
     */
    public void refundBack( String orderNo ) {
	UserConsume uc = userConsumeMapper.findByOrderCode1( orderNo );
	if ( CommonUtil.isEmpty( uc ) || CommonUtil.isEmpty( uc.getMcId() ) ) {
	    LOG.error( "支付回调查询订单出现异常" );
	    return;
	}
	MemberCard card = cardMapper.selectById( uc.getMcId() );
	saveCardRecordNew( uc.getMcId(), (byte) 1, uc.getTotalMoney() + "元", "退款", uc.getBusUserId(), card.getMoney().toString(), uc.getCtId(), 0.0 );
    }

    /**
     * 发送赠送物品给用户
     */
    @Override
    @Transactional( rollbackFor = Exception.class )
    public void giveGood( String orderNo ) throws Exception {
	UserConsume uc = userConsumeMapper.findByOrderCode1( orderNo );
	if ( CommonUtil.isEmpty( uc ) ) {
	    return;
	}
	List< MemberGiveconsume > gcs = giveConsumeMapper.findByUcId( uc.getId() );
	Member member = memberMapper.selectById( uc.getMemberId() );
	int integral = 0; // 积分
	int flow = 0;
	double fanCurrency = 0.0;
	if ( CommonUtil.isNotEmpty( gcs ) && gcs.size() > 0 ) {
	    for ( MemberGiveconsume giveConsume : gcs ) {
		switch ( giveConsume.getGtId() ) {
		    case 1:
			// 积分赠送
			integral = giveConsume.getGcTotal();
			saveCardRecordNew( member.getMcId(), (byte) 2, integral + "积分", "积分赠送", member.getBusId(), ( member.getIntegral() + integral ) + "", uc.getCtId(),
					integral );
			break;
		    case 2:
			// 流量赠送
			flow = giveConsume.getGcTotal();
			break;
		    case 3:
			// 粉币赠送
			fanCurrency = giveConsume.getGcTotal();
			saveCardRecordNew( member.getMcId(), (byte) 3, fanCurrency + "粉币", "粉币", member.getBusId(), ( member.getFansCurrency() + fanCurrency ) + "", uc.getCtId(),
					fanCurrency );
			break;
		    default:
			break;
		}
	    }
	}

	if ( CommonUtil.isNotEmpty( uc.getMcId() ) ) {
	    MemberCard card = cardMapper.selectById( CommonUtil.toInteger( uc.getMcId() ) );
	    if ( CommonUtil.isNotEmpty( member ) ) {
		member.setFansCurrency( member.getFansCurrency() + fanCurrency );
		member.setFlow( member.getFlow() + flow );
		member.setIntegral( member.getIntegral() + integral );
		member.setFlowDate( new Date() );
		member.setIntegralDate( new Date() );
		member.setTotalIntegral( member.getTotalIntegral() + integral );
		member.setTotalMoney( member.getTotalMoney() + uc.getDiscountMoney() );
		memberMapper.updateById( member );

	    }
	    if ( card.getApplyType() != 4 ) { // 泛会员不升级
		// 判断会员是否是要升级
		Map< String,Object > map = findNextGrade( uc.getBusUserId(), uc.getCtId(), uc.getGtId(), member.getTotalIntegral() + integral,
				member.getTotalMoney() + uc.getDiscountMoney() );

		// 修改会员卡等级和赠送规则
		if ( CommonUtil.isNotEmpty( map ) ) {
		    card.setGtId( Integer.parseInt( map.get( "gtId" ).toString() ) );
		    card.setGrId( Integer.parseInt( map.get( "grId" ).toString() ) );

		    // 升级通知
		    systemMsgService.upgradeMemberMsg( member, card.getCardNo(), CommonUtil.isEmpty( card.getExpireDate() ) ? "长期有效" : DateTimeKit.format( card.getExpireDate() ) );

		}
		cardMapper.updateById( card );
	    }
	}
    }

    @Override
    public void buyCardCallBack( String orderId, Byte payStatus ) {
	LOG.error( "微信支付回调订单单号 ：" + orderId );
	List< Map< String,Object > > ucs = userConsumeMapper.findByOrderCode( orderId );
	if ( CommonUtil.isEmpty( ucs ) || ucs.size() == 0 || ucs.size() > 1 ) {
	    LOG.error( "支付回调查询订单出现异常" );
	    return;
	}
	Integer id = Integer.parseInt( ucs.get( 0 ).get( "id" ).toString() );
	UserConsume uc = new UserConsume();
	uc.setId( id );
	uc.setPaymentType( 1 );
	uc.setPayStatus( payStatus.intValue() );
	userConsumeMapper.updateById( uc );

	//updateUserConsume(id, payStatus);
	if ( payStatus == 1 ) {
	    MemberCardbuy cardbuy = new MemberCardbuy();
	    cardbuy.setBuyMoney( CommonUtil.toDouble( ucs.get( 0 ).get( "discountMoney" ) ) );
	    cardbuy.setCtId( CommonUtil.toInteger( ucs.get( 0 ).get( "ctId" ) ) );
	    cardbuy.setMemberId( CommonUtil.toInteger( ucs.get( 0 ).get( "memberId" ) ) );
	    cardbuy.setBusId( CommonUtil.toInteger( ucs.get( 0 ).get( "busUserId" ) ) );
	    cardBuyMapper.insert( cardbuy );

	    // 添加会员卡
	    MemberCard card = new MemberCard();
	    card.setIsChecked( 1 );
	    card.setCardNo( CommonUtil.getCode() );
	    card.setCtId( CommonUtil.toInteger( ucs.get( 0 ).get( "ctId" ) ) );

	    card.setSystemcode( CommonUtil.getNominateCode() );
	    card.setApplyType( 3 );
	    card.setMemberId( CommonUtil.toInteger( ucs.get( 0 ).get( "memberId" ) ) );
	    card.setGtId( CommonUtil.toInteger( ucs.get( 0 ).get( "gt_id" ) ) );
	    MemberGiverule giveRule = giveRuleMapper.findBybusIdAndGtIdAndCtId( CommonUtil.toInteger( ucs.get( 0 ).get( "busUserId" ) ), card.getGtId(), card.getCtId() );
	    card.setGrId( giveRule.getGrId() );

	    card.setCardNo( CommonUtil.getCode() );

	    card.setPublicId( CommonUtil.toInteger( ucs.get( 0 ).get( "public_id" ) ) );
	    card.setReceiveDate( new Date() );
	    card.setIsbinding( 1 );

	    MemberGradetype gradeType = gradeTypeMapper.selectById( card.getGtId() );
	    if ( card.getCtId() == 5 ) {
		if ( CommonUtil.isNotEmpty( gradeType.getBalance() ) ) {
		    card.setFrequency( new Double( gradeType.getBalance() ).intValue() );
		} else {
		    card.setFrequency( 0 );
		}
	    } else {
		if ( CommonUtil.isNotEmpty( gradeType.getBalance() ) ) {
		    card.setMoney( new Double( gradeType.getBalance() ) );
		} else {
		    card.setMoney( 0.0 );
		}
	    }

	    cardMapper.insert( card );

	    Member member = new Member();
	    member.setId( CommonUtil.toInteger( ucs.get( 0 ).get( "memberId" ) ) );
	    member.setIsBuy( 1 );
	    member.setMcId( card.getMcId() );
	    memberMapper.updateById( member );
	    String balance = null;
	    if ( card.getCtId() == 5 ) {
		balance = card.getFrequency() + "次";
	    } else {
		balance = card.getMoney() + "元";
	    }
	    saveCardRecordNew( card.getMcId(), (byte) 1, ucs.get( 0 ).get( "discountMoney" ) + "元", "购买会员卡", card.getBusId(), balance, card.getCtId(), 0.0 );

	    // 新增会员短信通知
	    member = memberMapper.selectById( CommonUtil.toInteger( ucs.get( 0 ).get( "memberId" ) ) );
	    systemMsgService.sendNewMemberMsg( member );

	    //		socke.sendMessage2("member_" + member.getId(), "");

	}
    }

    @Transactional( rollbackFor = Exception.class )
    @Override
    public Map< String,Object > intergralConsume( int busId, String cardNo, Integer intergral, String gift ) throws Exception {
	Map< String,Object > map = new HashMap< String,Object >();
	try {
	    MemberCard card = cardMapper.findCardByCardNo( busId, cardNo );
	    if ( CommonUtil.isEmpty( card ) ) {
		map.put( "result", false );
		map.put( "message", "该会员卡不存在!" );
		return map;
	    }
	    Member member = memberMapper.findByMcIdAndbusId( busId, card.getMcId() );
	    Integer jifen = member.getIntegral();
	    if ( intergral > jifen ) {
		map.put( "result", false );
		map.put( "message", "积分不够!" );
		return map;
	    }
	    Member mem = new Member();
	    mem.setId( member.getId() );
	    mem.setIntegral( member.getIntegral() - intergral );
	    memberMapper.updateById( mem );
	    // 添加会员记录
	    UserConsume uc = new UserConsume();
	    uc.setPublicId( member.getPublicId() );
	    uc.setBusUserId( busId );
	    uc.setMemberId( member.getId() );
	    uc.setMcId( card.getMcId() );
	    uc.setCtId( card.getCtId() );
	    uc.setGtId( card.getGtId() );
	    uc.setRecordType( 0 );
	    uc.setTotalMoney( 0.0 );
	    uc.setCreateDate( new Date() );
	    uc.setPayStatus( 1 );
	    uc.setUcType( 5 );
	    uc.setIntegral( intergral );
	    uc.setFenbi( 0.0 );
	    uc.setUccount( 0 );
	    uc.setDiscount( 100 );
	    uc.setDiscountMoney( 0.0 );
	    uc.setOrderId( null );
	    uc.setUcTable( null );
	    uc.setCreateDate( new Date() );
	    uc.setPaymentType( null );
	    uc.setGiveGift( gift );
	    uc.setGiftCount( 1 );
	    uc.setOrderCode( null );
	    userConsumeMapper.insert( uc );

	    saveCardRecordNew( card.getMcId(), (byte) 2, "-" + intergral, "积分兑换", card.getBusId(), null, 0, -intergral );
	    map.put( "result", true );
	    map.put( "message", "兑换成功" );
	} catch ( Exception e ) {
	    e.printStackTrace();
	    LOG.error( "积分兑换礼品异常", e );
	    throw new Exception();
	}
	return map;
    }

    @Override
    public Map< String,Object > updateMemberIntergral( Integer memberId, Integer intergral ) {
	Map< String,Object > map = new HashMap< String,Object >();
	try {
	    Member member = memberMapper.selectById( memberId );
	    Integer mIntergral = member.getIntegral();
	    if ( mIntergral < -intergral ) {
		map.put( "result", 1 );
		map.put( "message", "积分不足" );
		return map;
	    }
	    Member member1 = new Member();
	    member1.setId( member.getId() );
	    member1.setIntegral( member.getIntegral() + intergral );
	    memberMapper.updateById( member1 );
	    if ( CommonUtil.isNotEmpty( member.getMcId() ) ) {
		saveCardRecordNew( member.getMcId(), (byte) 2, intergral + "积分", "积分", member.getBusId(), "", null, intergral );
	    }
	    map.put( "result", 2 );
	    map.put( "message", "积分支付成功" );
	} catch ( Exception e ) {
	    LOG.error( "积分支付失败", e );
	    map.put( "result", 1 );
	    map.put( "message", "积分支付异常" );
	}
	return map;

    }

    @Override
    public Map< String,Object > findBuyCard( Member member ) {
	Map< String,Object > map = new HashMap< String,Object >();
	if ( CommonUtil.isEmpty( member.getMcId() ) ) {
	    // 判断该用户是否已购买
	    if ( 1 == member.getIsBuy() ) {
		String url = "/phoneMemberController/" + member.getPublicId() + "/79B4DE7C/findMember_1.do";
		map.put( "code", "-1" );
		map.put( "msg", "已购买会员卡" );
		map.put( "url", url );
	    } else {
		String url = "/phoneMemberController/" + member.getPublicId() + "/79B4DE7C/findMember_1.do";
		map.put( "code", "1" );
		map.put( "msg", "未购买会员卡" );
		map.put( "url", url );
	    }
	} else {
	    map.put( "code", "-2" );
	    map.put( "msg", "未购买会员卡" );
	}
	return map;
    }

    @Override
    public Map< String,Object > buyCard( Member member, Double money, Integer ctId ) {
	Map< String,Object > map = new HashMap< String,Object >();
	try {
	    MemberCardbuy cardbuy = new MemberCardbuy();
	    cardbuy.setBuyMoney( money );
	    cardbuy.setPublicId( member.getPublicId() );
	    cardbuy.setCtId( ctId );
	    cardbuy.setMemberId( member.getId() );
	    cardBuyMapper.insert( cardbuy );
	    Member m = new Member();
	    m.setId( member.getId() );
	    m.setIsBuy( 1 );
	    memberMapper.updateById( m );
	    map.put( "code", 1 );
	    map.put( "msg", "购买成功" );
	} catch ( Exception e ) {
	    map.put( "code", -1 );
	    map.put( "msg", "购买异常" );
	}
	return map;
    }

    @Override
    public Map< String,Object > returnfansCurrency( Integer busId, Double fans_currency ) {
	Map< String,Object > map = new HashMap< String,Object >();
	try {
	    BusUser busUser = busUserMapper.selectById( busId );
	    BusUser busUser1 = new BusUser();
	    busUser1.setId( busId );
	    fans_currency = busUser.getFansCurrency().doubleValue() + fans_currency;
	    busUser1.setFansCurrency( BigDecimal.valueOf( fans_currency ) );
	    busUserMapper.updateById( busUser1 );
	    map.put( "result", true );
	    map.put( "message", "归还商户粉币成功" );
	} catch ( Exception e ) {
	    map.put( "result", false );
	    map.put( "message", "归还商户粉币异常" );
	    LOG.error( "归还商户粉币异常", e );
	}
	return map;
    }

    /**
     * 根据订单号添加赠送物品记录 （只添加记录 不做操作）
     *
     *
     * @throws Exception
     */
    @Transactional( rollbackFor = Exception.class )
    public void saveGiveConsume( String phone, String orderId ) throws Exception {
	List< Map< String,Object > > ucs = userConsumeMapper.findByOrderCode( orderId );
	if ( CommonUtil.isEmpty( ucs ) || ucs.size() == 0 || ucs.size() > 1 ) {
	    LOG.error( "赠送物品查询订单出现异常" );
	    throw new Exception();
	}
	try {
	    Integer busUserId = Integer.parseInt( ucs.get( 0 ).get( "busUserId" ).toString() );
	    Integer gtId = Integer.parseInt( ucs.get( 0 ).get( "gt_id" ).toString() );
	    Integer ctId = Integer.parseInt( ucs.get( 0 ).get( "ctId" ).toString() );

	    // 判断是否是会员日
	    MemberDate memberDate = findMemeberDate( busUserId, ctId );

	    boolean flag = false; // 表示今天是否是会员日
	    if ( CommonUtil.isNotEmpty( memberDate ) ) {
		flag = true;
	    }

	    double price = Double.parseDouble( ucs.get( 0 ).get( "discountMoney" ).toString() );

	    Integer recFreezeType = 0;
	    switch ( ctId ) {
		case 1:
		    recFreezeType = 20;
		    break;
		case 2:
		    recFreezeType = 21;
		    break;
		case 3:
		    recFreezeType = 22;
		    break;
		case 4:
		    recFreezeType = 23;
		    break;
		case 5:
		    recFreezeType = 24;
		    break;

		default:
		    break;
	    }

	    // 查询粉笔数量
	    JSONObject json = new JSONObject();
	    json.put( "token", TokenUitl.getToken() );
	    json.put( "busId", busUserId );
	    json.put( "rec_type", 1 );
	    json.put( "fre_type", recFreezeType );
	    json.put( "fkId", ctId );

	    // 查询粉笔数量
	    Integer fenbi = 0;
	    JSONObject returnJSON = HttpClienUtil.httpPost( memberConfig.getWxmp_home()+getFenbiSurplus, json, false );
	    if ( "1".equals( returnJSON.get( "code" ) ) ) {
		fenbi = returnJSON.getInt( "data" );
	    }

	    // 如果是折扣卡 金额用折后金额
	    if ( ctId == 2 ) {
		price = Double.parseDouble( ucs.get( 0 ).get( "discountMoney" ).toString() );
	    }
	    // 如果是次卡 次数默认为金额
	    if ( ctId == 5 ) {
		price = Double.parseDouble( ucs.get( 0 ).get( "uccount" ).toString() );
	    }
	    Integer ucId = Integer.parseInt( ucs.get( 0 ).get( "id" ).toString() );
	    // 如果是次卡 和 储值卡 就查询第一个等级的赠送规则
	    MemberGiverule gr = null;
	    if ( ctId == 5 || ctId == 3 ) {
		List< Map< String,Object > > grs = giveRuleMapper.findByBusIdAndCtId( busUserId, ctId );
		gr = new MemberGiverule();
		if ( grs.size() != 0 ) {
		    gr.setGrId( CommonUtil.toInteger( grs.get( 0 ).get( "gr_id" ) ) );
		}
	    } else {
		gr = giveRuleMapper.findBybusIdAndGtIdAndCtId( busUserId, gtId, ctId );
	    }
	    if ( CommonUtil.isNotEmpty( gr ) ) {
		List< Map< String,Object > > grgts = giveRuleGoodsTypeMapper.findByGrId( gr.getGrId() );
		MemberGiveconsume giveConsume = null;
		MemberGiverulegoodstype grgt = null;
		for ( Map< String,Object > map : grgts ) {
		    giveConsume = new MemberGiveconsume();
		    if ( CommonUtil.isEmpty( map.get( "gId" ) ) ) continue;
		    if ( "1".equals( map.get( "gId" ).toString() ) ) {
			if ( "1".equals( map.get( "give_type" ).toString() ) ) {
			    // 积分
			    if ( CommonUtil.isEmpty( map.get( "money" ) ) ) continue;
			    Double money = Double.parseDouble( map.get( "money" ).toString() );
			    int count = (int) Math.floor( price / money );
			    if ( count == 0 ) continue;
			    if ( CommonUtil.isEmpty( map.get( "number" ) ) ) continue;

			    int num = count * Integer.parseInt( map.get( "number" ).toString() );
			    Integer upperLmit = Integer.parseInt( map.get( "upperLmit" ).toString() );
			    if ( upperLmit != 0 ) {
				num = num > upperLmit ? upperLmit : num;
			    }
			    // 会员日赠送翻倍
			    if ( flag ) {
				num = num * memberDate.getIntegral();
			    }

			    giveConsume.setGcTotal( num );
			    giveConsume.setGtId( Integer.parseInt( map.get( "gId" ).toString() ) );
			    giveConsume.setGtName( map.get( "gt_name" ).toString() );
			    giveConsume.setGtUnit( map.get( "gt_unit" ).toString() );
			    giveConsume.setUcId( ucId );
			    giveConsume.setMemberId( CommonUtil.toInteger( ucs.get( 0 ).get( "memberId" ) ) );
			    giveConsume.setSendDate( new Date() );
			    giveConsumeMapper.insert( giveConsume );
			}
		    } else {
			// 添加赠送物品记录
			Integer upperLmit = Integer.parseInt( map.get( "upperLmit" ).toString() );

			if ( "2".equals( map.get( "gId" ).toString() ) || "3".equals( map.get( "gId" ).toString() ) || upperLmit > 0 ) {
			    Integer count = Integer.parseInt( map.get( "number" ).toString() );
			    Double money = Double.parseDouble( map.get( "money" ).toString() );
			    if ( price < money ) continue;
			    if ( upperLmit < count ) {
				// 扣除商家粉币数量
				if ( "3".equals( map.get( "gId" ).toString() ) ) {
				    // 会员日赠送翻倍
				    if ( flag ) {
					count = count * memberDate.getFansCurrency();
				    }

				    if ( fenbi < count ) {
					continue;
				    }

				    giveConsume.setGcTotal( count );
				    // 冻结商家粉笔数量
				    JSONObject requestJson = new JSONObject();
				    requestJson.put( "token", TokenUitl.getToken() );
				    requestJson.put( "busId", busUserId );
				    requestJson.put( "rec_type", count );
				    requestJson.put( "fre_type", recFreezeType );
				    requestJson.put( "fkId", ctId );
				    // 查询粉笔数量
				    HttpClienUtil.httpPost(memberConfig.getWxmp_home()+ updateFenbiReduce, requestJson, false );

				} else if ( "2".equals( map.get( "gId" ).toString() ) ) {
				    Integer flowCount = Integer.parseInt( map.get( "number" ).toString() );

				    if ( flag ) {
					flowCount = flowCount * memberDate.getFlow();
				    }

				    giveConsume.setGcTotal( flowCount );

				}
				// 上限非等于0 认为是商家自定义物品
				if ( upperLmit != 0 ) {
				    giveConsume.setGcTotal( upperLmit );
				}
			    } else {
				giveConsume.setGcTotal( count );
			    }

			    giveConsume.setGtId( Integer.parseInt( map.get( "gId" ).toString() ) );
			    giveConsume.setGtName( map.get( "gt_name" ).toString() );
			    giveConsume.setGtUnit( map.get( "gt_unit" ).toString() );
			    giveConsume.setUcId( ucId );
			    giveConsume.setMemberId( CommonUtil.toInteger( ucs.get( 0 ).get( "memberId" ) ) );
			    giveConsume.setSendDate( new Date() );
			    giveConsumeMapper.insert( giveConsume );

			    if ( !"2".equals( map.get( "gId" ).toString() ) && !"3".equals( map.get( "gId" ).toString() ) ) {
				// 修改赠送规则物品剩余数量(商家自定义物品)
				grgt = new MemberGiverulegoodstype();
				grgt.setGrId( Integer.parseInt( map.get( "gr_id" ).toString() ) );
				grgt.setGtId( Integer.parseInt( map.get( "gId" ).toString() ) );

				if ( upperLmit < count ) {
				    grgt.setUpperLmit( 0 );
				    grgt.setGiveType( 2 );
				} else {
				    grgt.setUpperLmit( upperLmit - count );
				}
				giveRuleGoodsTypeMapper.updateById( grgt );
			    }
			}
		    }
		}
	    }
	} catch ( Exception e ) {
	    LOG.error( "会员赠送物品异常", e );
	}
    }

    /**
     * 判断是否是会员日
     */
    public MemberDate findMemeberDate( Integer busId, Integer ctId ) {
	try {
	    MemberGradetype gradeType = gradeTypeMapper.findIsmemberDateByCtId( busId, ctId );
	    if ( CommonUtil.isEmpty( gradeType ) || gradeType.getIsmemberDate() == 1 ) {
		return null;
	    }
	    // 未设置会员日
	    MemberDate memberdate = memberDateMapper.findByBusIdAndCtId( busId, ctId );
	    if ( CommonUtil.isEmpty( memberdate ) ) {
		return null;
	    }
	    switch ( memberdate.getDateType() ) {
		case 0:
		    int d = DateTimeKit.getNow().getDay();
		    Integer day = 0;
		    if ( d == 0 ) {
			day = 7;
		    } else if ( d == 1 ) {
			day = 1;
		    } else if ( d == 2 ) {
			day = 2;
		    } else if ( d == 3 ) {
			day = 3;
		    } else if ( d == 4 ) {
			day = 4;
		    } else if ( d == 5 ) {
			day = 5;
		    } else if ( d == 6 ) {
			day = 6;
		    }
		    if ( day == CommonUtil.toInteger( memberdate.getDateStr() ) ) {
			return memberdate;
		    }
		    break;
		case 1:
		    Integer date = DateTimeKit.getNow().getDate();
		    if ( date == CommonUtil.toInteger( memberdate.getDateStr() ) ) {
			return memberdate;
		    }
		    break;
		case 2:
		    if ( DateTimeKit.isSameDay( new Date(), DateTimeKit.parseDate( memberdate.getDateStr() ) ) ) {
			return memberdate;
		    }
		    break;
		case 3:
		    // 区间
		    String dateStr = memberdate.getDateStr();
		    List< Map< String,Object > > list = JSONArray.toList( JSONArray.fromObject( dateStr ), Map.class );
		    Integer year = DateTimeKit.getYear( new Date() );
		    for ( Map< String,Object > map : list ) {
			String time = CommonUtil.toString( map.get( "time" ) );
			if ( time.length() == 1 ) {
			    time = "0" + time;
			}
			String time1 = CommonUtil.toString( map.get( "time1" ) );
			if ( time1.length() == 1 ) {
			    time1 = "0" + time1;
			}
			String time2 = CommonUtil.toString( map.get( "time2" ) );
			if ( time2.length() == 1 ) {
			    time2 = "0" + time2;
			}
			String time3 = CommonUtil.toString( map.get( "time3" ) );
			if ( time3.length() == 1 ) {
			    time3 = "0" + time3;
			}

			String date1 = year + "-" + time + "-" + time1 + " 00:00:00";
			String date2 = year + "-" + time2 + "-" + time3 + " 23:59:59";
			Date d1 = DateTimeKit.parse( date1, "yyyy-MM-dd HH:mm:ss" );
			Date d2 = DateTimeKit.parse( date2, "yyyy-MM-dd HH:mm:ss" );
			if ( isBetween( d1, d2 ) ) {
			    return memberdate;
			}
		    }
		    break;
		default:
		    break;
	    }
	    return null;
	} catch ( Exception e ) {
	    e.printStackTrace();
	}
	return null;
    }

    /**
     * 比较当前时间是否在两者之间
     *
     * @param date1 开始时间
     * @param date2 结束时间
     *
     * @return
     */
    public boolean isBetween( Date date1, Date date2 ) {
	Date date = new Date();
	if ( date.getTime() >= date1.getTime() && date.getTime() <= date2.getTime() ) {
	    return true;
	} else {
	    return false;
	}
    }

    @Transactional( rollbackFor = Exception.class )
    @Override
    public Map< String,Object > reduceFansCurrencyMoney( Member member, Integer busId, Double fenbimoney ) throws Exception {
	Map< String,Object > map = new HashMap< String,Object >();
	try {
	    int fenbi = 0;
	    if ( fenbimoney > 0 ) {
		Map< String,Object > dict = dictService.getDict( "1058" );
		fenbi = new Double( fenbimoney * CommonUtil.toDouble( dict.get( "1" ) ) ).intValue();
	    } else {
		map.put( "code", "-1" );
		map.put( "message", "未存在粉币扣除" );
		return map;
	    }

	    Member m = new Member();
	    m.setId( member.getId() );
	    m.setFansCurrency( member.getFansCurrency() - fenbi );
	    memberMapper.updateById( m );

	    if ( CommonUtil.isNotEmpty( member.getMcId() ) ) {
		saveCardRecordNew( member.getMcId(), (byte) 3, "-" + fenbi + "粉币", "粉币", member.getBusId(), "", null, -fenbi );
	    }

	    BusUser busUser = busUserMapper.selectById( busId );
	    BusUser busUser1 = new BusUser();
	    busUser1.setId( busId );
	    Double fenbi1 = busUser.getFansCurrency().doubleValue() + fenbi;
	    busUser1.setFansCurrency( BigDecimal.valueOf( fenbi1 ) );
	    busUserMapper.updateById( busUser1 );

	    map.put( "code", 2 );
	    map.put( "message", "粉币扣除成功" );
	} catch ( Exception e ) {
	    LOG.error( "粉币抵扣异常", e );
	    throw new Exception();
	}
	return map;
    }

    @Transactional( rollbackFor = Exception.class )
    @Override
    public Map< String,Object > reduceFansCurrency( Member member, Integer busId, Double fenbi ) throws Exception {
	Map< String,Object > map = new HashMap< String,Object >();
	try {
	    if ( member.getFansCurrency() < fenbi ) {
		map.put( "result", "1" );
		map.put( "message", "粉币不足" );
		return map;
	    }
	    Member m = new Member();
	    m.setId( member.getId() );
	    m.setFansCurrency( member.getFansCurrency() - fenbi );
	    memberMapper.updateById( m );

	    if ( CommonUtil.isNotEmpty( member.getMcId() ) ) {
		saveCardRecordNew( member.getMcId(), (byte) 3, "-" + fenbi + "粉币", "粉币", member.getBusId(), "", null, -fenbi );
	    }

	    BusUser busUser = busUserMapper.selectById( busId );
	    BusUser busUser1 = new BusUser();
	    busUser1.setId( busId );
	    Double fenbi1 = busUser.getFansCurrency().doubleValue() + fenbi;
	    busUser1.setFansCurrency( BigDecimal.valueOf( fenbi1 ) );
	    busUserMapper.updateById( busUser1 );

	    map.put( "result", 2 );
	    map.put( "message", "粉币扣除成功" );
	} catch ( Exception e ) {
	    LOG.error( "粉币抵扣异常", e );
	    throw new Exception();
	}
	return map;
    }

    @Transactional( rollbackFor = Exception.class )
    @Override
    public MemberGradetype findGradeType( Integer memberId ) {
	Member member = memberMapper.selectById( memberId );
	if ( CommonUtil.isEmpty( member ) || CommonUtil.isEmpty( member.getMcId() ) ) {
	    return null;
	}
	Integer mcId = member.getMcId();
	MemberCard card = cardMapper.selectById( mcId );
	MemberGradetype gradeType = gradeTypeMapper.selectById( card.getGtId() );
	return gradeType;
    }

    @Override
    public Map< String,Object > updateMemberFansCurrency( Integer memberId, Integer busId, Double fenbi ) throws Exception {
	Map< String,Object > map = new HashMap< String,Object >();
	try {
	    Member member = memberMapper.selectById( memberId );
	    if ( member.getFansCurrency() < -fenbi ) {
		map.put( "result", "1" );
		map.put( "message", "粉币不足" );
		return map;
	    }
	    BusUser busUser = busUserMapper.selectById( busId );

	    Member m = new Member();
	    m.setId( member.getId() );
	    m.setFansCurrency( member.getFansCurrency() + fenbi );
	    memberMapper.updateById( m );

	    if ( CommonUtil.isNotEmpty( member.getMcId() ) ) {
		if ( fenbi > 0 ) {
		    saveCardRecordNew( member.getMcId(), (byte) 3, fenbi + "粉币", "粉币赠送", member.getBusId(), "", null, fenbi );
		} else {
		    saveCardRecordNew( member.getMcId(), (byte) 3, fenbi + "粉币", "粉币扣除", member.getBusId(), "", null, -fenbi );
		}

	    }

	    BusUser busUser1 = new BusUser();
	    busUser1.setId( busId );
	    Double feibi1 = busUser.getFansCurrency().doubleValue() - fenbi;
	    busUser1.setFansCurrency( BigDecimal.valueOf( feibi1 ) );
	    busUserMapper.updateById( busUser1 );
	    map.put( "result", 2 );
	    map.put( "message", "操作成功" );
	} catch ( Exception e ) {
	    LOG.error( "粉币操作异常", e );
	    throw new Exception();
	}
	return map;
    }

    /**
     * 粉币计算
     *
     * @param totalMoney    能抵抗消费金额
     * @param fans_currency 粉币值
     *
     * @return 返回兑换金额
     */
    @Override
    public Double currencyCount( Double totalMoney, Double fans_currency ) {
	try {
	    Map< String,Object > dict = dictService.getDict( "1058" );
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

    @Override
    public Double deductFenbi( Double jifenMoney, int busId ) {
	Map< String,Object > dict = dictService.getDict( "1058" );
	Double ratio = CommonUtil.toDouble( dict.get( "1" ) );
	Double fenbi = jifenMoney * ratio;
	return fenbi;
    }

    @Override
    public Integer deductJifen( Double jifenMoney, int busId ) {
	PublicParameterset pps = publicParameterSetMapper.findBybusId( busId );
	if ( CommonUtil.isEmpty( pps ) ) {
	    return 0;
	}
	Integer jifen = new Double( jifenMoney / pps.getChangeMoney() * pps.getIntegralRatio() ).intValue();
	return jifen;
    }

    /**
     * 积分计算
     *
     * @param totalMoney 能抵抗消费金额
     * @param integral   积分
     *
     * @return
     */
    @Override
    public Double integralCount( Double totalMoney, Double integral, int busId ) {
	try {
	    PublicParameterset ps = publicParameterSetMapper.findBybusId( busId );
	    if ( CommonUtil.isEmpty( ps ) ) {
		return 0.0;
	    }
	    double startMoney = ps.getStartMoney();
	    double integralratio = ps.getIntegralRatio();
	    double changMoney = ps.getChangeMoney();
	    if ( integralratio <= 0 ) {
		return 0.0;
	    }

	    // 积分启兑
	    double integralNum = startMoney * integralratio;
	    if ( integral < integralNum ) {
		return 0.0;
	    }

	    if ( CommonUtil.isNotEmpty( totalMoney ) ) {
		// 订单金额小于订单启兑金额
		if ( totalMoney < changMoney ) {
		    return 0.0;
		}
		Integer money = new Double( integral / integralratio ).intValue();
		if ( totalMoney >= money ) {
		    return new Double( money );
		} else {
		    return totalMoney;
		}
	    } else {
		Integer money = new Double( integral / integralratio ).intValue();
		return new Double( money );
	    }
	} catch ( Exception e ) {
	    LOG.error( "计算积分抵扣异常" );
	    e.printStackTrace();
	}
	return 0.0;
    }

    /**
     * 会员卡充值
     */
    @Override
    @Transactional( rollbackFor = Exception.class )
    public Map< String,Object > rechargeMember( Integer memberId, String cardNo, Double money, Integer count ) throws Exception {
	if ( CommonUtil.isEmpty( memberId ) || CommonUtil.isEmpty( cardNo ) || CommonUtil.isEmpty( money ) ) {
	    throw new Exception();
	}
	try {
	    Member member = memberMapper.selectById( memberId );
	    MemberCard card = cardMapper.findCardByCardNo( member.getBusId(), cardNo );
	    if ( CommonUtil.isEmpty( card ) ) {
		throw new Exception();
	    }
	    // 添加会员记录
	    UserConsume uc = new UserConsume();
	    uc.setPublicId( member.getPublicId() );
	    uc.setBusUserId( member.getBusId() );
	    uc.setMemberId( memberId );
	    uc.setMcId( card.getMcId() );
	    uc.setCtId( card.getCtId() );
	    uc.setGtId( card.getGtId() );
	    uc.setRecordType( 1 );
	    uc.setUcType( 7 );
	    uc.setTotalMoney( money );
	    uc.setCreateDate( new Date() );
	    uc.setPayStatus( 0 );
	    uc.setDiscount( 100 );
	    uc.setDiscountMoney( money );
	    uc.setPaymentType( 0 );
	    uc.setDataSource( 2 );
	    uc.setIschongzhi( 1 );
	    if ( card.getCtId() == 3 ) {
		uc.setGiveGift( "赠送金额" );
		MemberGiverule gr = findGive( member.getBusId(), card.getGtId(), 3 );
		if ( CommonUtil.isNotEmpty( gr ) ) {
		    count = findRechargegive( money, gr.getGrId(), member.getBusId(), card.getCtId() );
		    uc.setGiftCount( count );
		}
		uc.setUccount( 0 );
	    } else if ( card.getCtId() == 5 ) {
		uc.setGiveGift( "赠送次数" );
		MemberGiverule gr = findGive( member.getBusId(), card.getGtId(), 5 );
		if ( CommonUtil.isNotEmpty( gr ) ) {
		    int givecount = findRechargegive( money, gr.getGrId(), member.getBusId(), card.getCtId() );
		    uc.setGiftCount( givecount );
		}
		uc.setUccount( count );
	    }

	    // 获取主账户id
	    JSONObject json = new JSONObject();
	    json.put( "userId", member.getBusId() );
	    JSONObject returnJSON = HttpClienUtil.httpPost( memberConfig.getWxmp_home()+shopsList, json, false );
	    String shoplist = returnJSON.getString( "shoplist" );

	    List< Map< String,Object > > shops = (List< Map< String,Object > >) JSONArray.toCollection( JSONArray.fromObject( shoplist ) );
	    if ( CommonUtil.isNotEmpty( shops ) && shops.size() > 0 ) {
		uc.setStoreId( CommonUtil.toInteger( shops.get( 0 ).get( "id" ) ) );
	    }

	    String orderCode = CommonUtil.getMEOrderCode();
	    uc.setOrderCode( orderCode );
	    userConsumeMapper.insert( uc );
	    Map< String,Object > params = new HashMap< String,Object >();
	    String red_url =
			    "/alipay/79B4DE7C/alipayApi.do?out_trade_no=" + orderCode + "&subject=会员卡充值&model=2&businessUtilName=alipayNotifyUrlBuinessServiceChargeCard&total_fee="
					    + money + "&busId=" + member.getBusId() + "&return_url=" + memberConfig.getWxmp_home()
					    + "/phoneMemberController/79B4DE7C/recharge.do?id=" + member.getId();
	    params.put( "result", true );
	    params.put( "message", "未支付" );
	    params.put( "red_url", red_url );
	    return params;
	} catch ( Exception e ) {
	    LOG.error( "会员卡支付宝充值异常", e );
	    throw new Exception();
	}
    }

    @Override
    public Map< String,Object > findMember( String openId ) {
	Map< String,Object > returnMap = new HashMap< String,Object >();
	Member member = memberMapper.queryOpenid( openId );
	if ( CommonUtil.isEmpty( member ) || CommonUtil.isEmpty( member.getMcId() ) ) {
	    returnMap.put( "result", 0 );
	    returnMap.put( "message", "非会员" );
	    return returnMap;
	}

	MemberCard card = cardMapper.selectById( member.getMcId() );
	if ( card.getIsChecked() == 0 || card.getCardStatus() == 1 ) {
	    returnMap.put( "result", 0 );
	    returnMap.put( "message", "非会员" );
	    return returnMap;
	}
	// 会员查看粉币抵扣金额和积分抵扣金额
	Double jifenMoney = integralCount( null, member.getIntegral().doubleValue(), member.getBusId() );
	returnMap.put( "jifenMoney", jifenMoney );

	Double fenbiMoney = currencyCount( null, member.getFansCurrency() );

	returnMap.put( "fenbiMoney", fenbiMoney );

	if ( card.getCtId() == 2 ) {
	    MemberDate memberDate = findMemeberDate( member.getPublicId(), 2 );

	    MemberGiverule giveRule = giveRuleMapper.selectById( card.getGrId() );
	    if ( CommonUtil.isNotEmpty( memberDate ) ) {
		returnMap.put( "memberDate", true );
		returnMap.put( "discount", new Double( giveRule.getGrDiscount() * memberDate.getDiscount() ) / 1000 );
	    } else {
		returnMap.put( "discount", new Double( giveRule.getGrDiscount() ) / 100 );
	    }
	}
	MemberGradetype gt = gradeTypeMapper.selectById( card.getGtId() );
	returnMap.put( "dengji", gt.getGtGradeName() );
	switch ( card.getCtId() ) {
	    case 1:
		returnMap.put( "leixing", "积分卡" );
		break;
	    case 2:
		returnMap.put( "leixing", "折扣卡" );
		break;
	    case 3:
		returnMap.put( "leixing", "储值卡" );
		break;
	    case 4:
		returnMap.put( "leixing", "时效卡" );
		break;
	    case 5:
		returnMap.put( "leixing", "次卡" );
		break;

	    default:
		break;
	}
	returnMap.put( "member", member );
	returnMap.put( "result", 1 );
	returnMap.put( "card", card );
	return returnMap;
    }

    @Transactional( rollbackFor = Exception.class )
    @Override
    public Map< String,Object > weChatPayment( String orderCode, String openId, Double orderMoney, Integer payType, Double fenbiMoney, Double jifenMoney ) throws Exception {
	Map< String,Object > returnMap = new HashMap< String,Object >();
	try {
	    Member member = memberMapper.queryOpenid( openId );
	    UserConsume uc = new UserConsume();
	    // 添加会员记录
	    MemberCard card = null;
	    if ( isMemember( member.getId() ) ) {
		card = cardMapper.selectById( member.getMcId() );
		uc.setMcId( member.getMcId() );
		uc.setCtId( card.getCtId() );
		uc.setGtId( card.getGtId() );
		uc.setDiscount( 100 );
	    }
	    Double pay = orderMoney;
	    // 会员卡 折扣卡
	    if ( CommonUtil.isNotEmpty( card ) && card.getCtId() == 2 ) {
		// 查询赠送规则
		MemberGiverule gr = giveRuleMapper.selectById( card.getGrId() );
		uc.setDiscount( gr.getGrDiscount() );
		pay = pay * gr.getGrDiscount() / 100;
	    }
	    // 粉币
	    if ( fenbiMoney > 0 ) {
		// 粉币抵消
		Map< String,Object > dict = dictService.getDict( "1058" );
		Double fenbi = CommonUtil.toDouble( dict.get( "1" ) ) * fenbiMoney;
		uc.setFenbi( fenbi );
		if ( fenbiMoney >= pay ) {
		    pay = 0.0;
		} else {
		    pay = reduce( pay - fenbiMoney );
		}
	    }
	    // 积分
	    if ( jifenMoney > 0 ) {
		// 积分抵扣
		PublicParameterset ps = publicParameterSetMapper.findByPublicId( member.getPublicId() );
		int integralNum = new Double( jifenMoney / ps.getChangeMoney() * ps.getIntegralRatio() ).intValue();
		uc.setIntegral( -integralNum );
		if ( jifenMoney >= pay ) {
		    pay = 0.0;
		} else {
		    pay = reduce( pay - jifenMoney );
		}
	    }
	    WxPublicUsers wxPublicUsers = wxPublicUsersMapper.selectById( member.getPublicId() );
	    uc.setBusUserId( wxPublicUsers.getBusUserId() );
	    uc.setPublicId( member.getPublicId() );
	    uc.setMemberId( member.getId() );
	    uc.setRecordType( 2 );
	    // uc.setStoreid(CommonUtil.toInteger(obj.get("shopid")));
	    uc.setUcType( 19 );
	    uc.setTotalMoney( orderMoney );
	    uc.setCreateDate( new Date() );
	    uc.setPayStatus( 0 );
	    uc.setDiscountMoney( pay );
	    uc.setPaymentType( 1 );
	    uc.setOrderCode( orderCode );
	    uc.setDataSource( 1 );
	    if ( pay <= 0 ) {
		uc.setPayStatus( 1 );
	    } else {
		uc.setPayStatus( 0 );
	    }
	    if ( payType == 0 && pay > 0 ) {
		uc.setPaymentType( 1 );
		uc.setPayStatus( 0 );
		userConsumeMapper.insert( uc );
		returnMap.put( "payMoney", pay );
		returnMap.put( "result", 0 );
		returnMap.put( "message", "未支付" );
	    } else {
		uc.setPaymentType( 5 );
		// <!---------储值卡支付start-------------->
		if ( isMemember( member.getId() ) ) {
		    // 储值卡付款
		    if ( CommonUtil.isNotEmpty( card ) && card.getCtId() == 3 && pay > 0 ) {
			if ( pay > card.getMoney() ) {
			    throw new Exception();
			}
			MemberCard c = new MemberCard();
			c.setMcId( member.getMcId() );
			c.setMoney( card.getMoney() - pay );
			cardMapper.updateById( c );
		    }
		}
		uc.setPayStatus( 1 );
		userConsumeMapper.insert( uc );

		if ( isMemember( member.getId() ) ) {
		    saveGiveConsume( member.getPhone(), orderCode );
		    giveGood( orderCode );
		    saveCardRecordNew( member.getMcId(), (byte) 1, uc.getDiscountMoney() + "", "线下扫码消费", member.getBusId(), null, 0, 0 );
		}

		if ( fenbiMoney > 0 ) {
		    // 归还商户粉币
		    returnfansCurrency( wxPublicUsers.getBusUserId(), -uc.getFenbi().doubleValue() );
		    Member member1 = new Member();
		    member1.setId( member.getId() );
		    member1.setFansCurrency( member.getFansCurrency() + uc.getFenbi() );
		    memberMapper.updateById( member1 );

		}
		if ( jifenMoney > 0 ) {
		    // 扣除用户积分数量
		    Member member1 = new Member();
		    member1.setId( member.getId() );
		    member1.setIntegral( member.getIntegral() + uc.getIntegral() );
		    memberMapper.updateById( member1 );
		}
		returnMap.put( "result", 1 );
		returnMap.put( "message", "已支付" );
	    }
	} catch ( Exception e ) {
	    LOG.error( "扫码支付异常", e );
	    throw new Exception();
	}
	return returnMap;
    }

    /**
     * 保留两位小数
     *
     *
     * @return
     */
    public Double reduce( Double number ) {
	long l1 = Math.round( number * 100 ); // 四舍五入
	double ret = l1 / 100.0;
	return ret;
    }

    /**
     * 扫码支付回调
     */
    @Override
    public void backWeChatPayment( String orderCode ) throws Exception {
	try {
	    UserConsume userConsume = userConsumeMapper.findByOrderCode1( orderCode );
	    if ( CommonUtil.isEmpty( userConsume ) ) {
		throw new Exception();
	    }
	    Member member = memberMapper.selectById( userConsume.getMemberId() );
	    Member m1 = new Member();
	    boolean flag = false;
	    // 粉币
	    if ( userConsume.getFenbi() != 0 ) {
		m1.setFansCurrency( member.getFansCurrency() + userConsume.getFenbi() );
		flag = true;
	    }
	    // 积分
	    if ( userConsume.getIntegral() != 0 ) {
		m1.setIntegral( member.getIntegral() + userConsume.getIntegral() );
		flag = true;
	    }
	    if ( flag ) {
		m1.setId( member.getId() );
		memberMapper.updateById( m1 );
	    }
	    // 修改订单状态
	    UserConsume uc = new UserConsume();
	    uc.setId( userConsume.getId() );
	    uc.setPayStatus( 1 );
	    userConsumeMapper.updateById( uc );
	    if ( isMemember( member.getId() ) ) {
		saveGiveConsume( member.getPhone(), orderCode );
		giveGood( orderCode );
		saveCardRecordNew( member.getMcId(), (byte) 1, userConsume.getDiscountMoney() + "", "线下扫码消费", member.getBusId(), null, 0, 0.0 );
	    }
	    if ( userConsume.getFenbi() != 0 ) {
		// 归还商户粉币
		returnfansCurrency( userConsume.getBusUserId(), -uc.getFenbi().doubleValue() );
	    }
	} catch ( Exception e ) {
	    throw new Exception();
	}

    }

    @Override
    public Map< String,Object > cancelOrder( String orderCode ) throws Exception {
	Map< String,Object > map = new HashMap< String,Object >();
	try {
	    UserConsume userConsume = userConsumeMapper.findByOrderCode1( orderCode );
	    if ( CommonUtil.isEmpty( userConsume ) ) {
		throw new Exception();
	    }
	    Member member = memberMapper.selectById( userConsume.getMemberId() );
	    Double fenbi = member.getFansCurrency();
	    Integer jifen = member.getIntegral();
	    Integer flow = member.getFlow();

	    Member m1 = new Member();
	    m1.setId( member.getId() );
	    boolean flag = false;

	    // 粉币
	    if ( userConsume.getFenbi() != 0 ) {
		m1.setFansCurrency( member.getFansCurrency() - userConsume.getFenbi() );
		fenbi = m1.getFansCurrency();
		flag = true;
	    }
	    // 积分
	    if ( userConsume.getIntegral() != 0 ) {
		m1.setIntegral( member.getIntegral() - userConsume.getIntegral() );
		flag = true;

		jifen = m1.getIntegral();
	    }
	    if ( flag ) {
		memberMapper.updateById( m1 );
	    }
	    // 修改订单状态
	    UserConsume uc = new UserConsume();
	    uc.setId( userConsume.getId() );
	    uc.setPayStatus( 3 );
	    userConsumeMapper.updateById( uc );

	    Double fenbi1 = 0.0;
	    if ( isMemember( member.getId() ) ) {
		boolean flag1 = false;
		List< MemberGiveconsume > giveConsume = giveConsumeMapper.findByUcId( userConsume.getId() );
		for ( MemberGiveconsume gc : giveConsume ) {
		    switch ( gc.getGtId() ) {
			case 1:
			    m1.setIntegral( jifen - gc.getGcTotal() );
			    flag1 = true;
			    break;
			case 2:
			    m1.setFlow( flow - gc.getGcTotal() );
			    flag1 = true;
			    break;
			case 3:
			    fenbi1 = CommonUtil.toDouble( gc.getGcTotal() );
			    m1.setFansCurrency( fenbi - gc.getGcTotal() );
			    flag1 = true;
			    break;
			default:
			    break;
		    }
		}
		if ( flag1 ) {
		    memberMapper.updateById( m1 );
		}

		MemberCard card = cardMapper.selectById( member.getMcId() );
		// 储值卡付款
		if ( userConsume.getPaymentType() == 5 ) {
		    MemberCard c = new MemberCard();
		    c.setMcId( member.getMcId() );
		    c.setMoney( card.getMoney() + userConsume.getDiscountMoney() );
		    cardMapper.updateById( c );
		}

		saveCardRecordNew( member.getMcId(), (byte) 1, uc.getDiscountMoney() + "", "线下消费撤单", member.getBusId(), null, 0, 0.0 );

	    }
	    if ( userConsume.getFenbi() != 0 ) {
		// 归还商户粉币
		returnfansCurrency( userConsume.getBusUserId(), uc.getFenbi() + fenbi1 );
	    }
	    if ( userConsume.getPaymentType() == 5 ) {
		map.put( "result", 0 );
		map.put( "message", "撤单成功" );
		return map;
	    }
	    map.put( "result", 1 );
	    return map;
	} catch ( Exception e ) {
	    LOG.error( "撤单失败", e );
	    throw new Exception();
	}
    }

    @Transactional( rollbackFor = Exception.class )
    @Override
    public Map< String,Object > bingdingPhone( Map< String,Object > params ) throws Exception {
	Map< String,Object > map = new HashMap< String,Object >();
	try {
	    if ( CommonUtil.isEmpty( params ) ) {
		map.put( "result", false );
		map.put( "message", "数据不完整" );
		return map;
	    }

	    // 短信校验
	    Integer memberId = CommonUtil.toInteger( params.get( "memberId" ) );
	    if ( CommonUtil.isEmpty( memberId ) ) {
		map.put( "result", false );
		map.put( "message", "数据不完整" );
		return map;
	    }
	    String code = CommonUtil.toString( params.get( "code" ) );
	    if ( CommonUtil.isEmpty( code ) ) {
		map.put( "result", false );
		map.put( "message", "请输入校验码" );
		return map;
	    }
	    String phone = CommonUtil.toString( params.get( "phone" ) );
	    if ( CommonUtil.isEmpty( phone ) ) {
		map.put( "result", false );
		map.put( "message", "数据不完整" );
		return map;
	    }
	    Integer busId = CommonUtil.toInteger( params.get( "busId" ) );
	    if ( CommonUtil.isEmpty( busId ) ) {
		map.put( "result", false );
		map.put( "message", "数据不完整" );
		return map;
	    }
	    // 短信判断
	    if ( CommonUtil.isEmpty( JedisUtil.get( code ) ) ) {
		map.put( "result", false );
		map.put( "message", "短信校验码不正确" );
		return map;
	    }
	    // 查询要绑定的手机号码
	    Member oldMember = memberMapper.findByPhone( busId, phone );

	    if ( CommonUtil.isEmpty( oldMember ) ) {
		// 新用户
		Member member = memberMapper.selectById( memberId );
		Member m = new Member();
		m.setId( member.getId() );
		m.setPhone( phone );
		memberMapper.updateById( m );
		member.setPhone( phone );
		map.put( "member", member );
	    } else {
		Member m1 = memberMapper.selectById( memberId );

		Member member = new Member();
		member.setFlow( m1.getFlow() + oldMember.getFlow() );
		member.setIntegral( m1.getIntegral() + oldMember.getIntegral() );
		member.setFansCurrency( m1.getFansCurrency() + oldMember.getFansCurrency() );
		member.setId( oldMember.getId() );

		if ( CommonUtil.isNotEmpty( oldMember.getOldId() ) && !oldMember.getOldId().contains( oldMember.getId().toString() ) ) {
		    member.setOldId( oldMember.getOldId() + "," + oldMember.getId() + "," + m1.getId() );
		} else {
		    if ( CommonUtil.isNotEmpty( oldMember.getOldId() ) ) {
			member.setOldId( oldMember.getOldId() + "," + m1.getId() );
		    } else {
			member.setOldId( m1.getId() + "," );
		    }
		}
		if ( CommonUtil.isEmpty( oldMember.getOpenid() ) ) {
		    member.setOpenid( m1.getOpenid() );
		}

		if ( CommonUtil.isEmpty( oldMember.getPublicId() ) && CommonUtil.isNotEmpty( m1.getPublicId() ) ) {
		    member.setPublicId( m1.getPublicId() );
		}

		if ( CommonUtil.isEmpty( oldMember.getHeadimgurl() ) ) {
		    member.setHeadimgurl( m1.getHeadimgurl() );
		}

		memberMapper.deleteById( m1.getId() );
		memberMapper.updateById( member );

		MemberOld old = (MemberOld) JSONObject.toBean( JSONObject.fromObject( m1 ), MemberOld.class );
		memberOldMapper.insert( old );
		// 修改小程序之前openId对应的memberId
		memberAppletOpenidMapper.updateMemberId( member.getId(), m1.getId() );

		member.setPhone( phone );
		map.put( "member", member );
	    }
	    JedisUtil.del( code );
	    map.put( "result", true );
	    map.put( "message", "绑定成功" );
	} catch ( Exception e ) {
	    e.printStackTrace();
	    LOG.error( "小程序绑定手机号码异常", e );
	    throw new Exception();
	}
	return map;

    }

    @Override
    public List< Map< String,Object > > findMemberCardRecharge( Integer busId, String cardNo ) {
	MemberCard card = cardMapper.findCardByCardNo( busId, cardNo );
	if ( CommonUtil.isEmpty( card ) ) {
	    return null;
	}
	List< Map< String,Object > > recharges = null;
	// 判断会员日
	MemberDate memberDate = findMemeberDate( busId, card.getCtId() );
	if ( CommonUtil.isNotEmpty( memberDate ) ) {
	    recharges = rechargeGiveMapper.findBybusIdAndGrId( busId, card.getGrId(), 1 );
	} else {
	    recharges = rechargeGiveMapper.findBybusIdAndGrId( busId, card.getGrId(), 0 );
	}
	return recharges;
    }

    @Override
    public Map< String,Object > successReCharge( Integer busId, String cardNo, Double money ) throws Exception {
	Map< String,Object > map = new HashMap< String,Object >();
	try {
	    if ( CommonUtil.isEmpty( cardNo ) || CommonUtil.isEmpty( money ) ) {
		map.put( "result", false );
		map.put( "message", "数据异常" );
		return map;
	    }
	    MemberCard card = cardMapper.findCardByCardNo( busId, cardNo );
	    Member member = null;
	    try {
		member = memberMapper.findByMcIdAndbusId( busId, card.getMcId() );
	    } catch ( Exception e ) {
		e.printStackTrace();
	    }
	    if ( CommonUtil.isEmpty( card ) ) {
		map.put( "result", false );
		map.put( "message", "卡号不存在" );
		return map;
	    }
	    // 添加会员记录
	    UserConsume uc = new UserConsume();
	    uc.setPublicId( member.getPublicId() );
	    uc.setBusUserId( member.getBusId() );
	    uc.setMemberId( member.getId() );
	    uc.setMcId( card.getMcId() );
	    uc.setCtId( card.getCtId() );
	    uc.setGtId( card.getGtId() );
	    uc.setRecordType( 1 );
	    uc.setUcType( 7 );
	    uc.setTotalMoney( money );
	    uc.setDiscountMoney( money );
	    uc.setCreateDate( new Date() );
	    uc.setPayStatus( 1 );
	    uc.setPaymentType( 3 );
	    uc.setDataSource( 0 );
	    int count = 0;
	    if ( card.getCtId() == 3 ) {
		uc.setGiveGift( "赠送金额" );
		MemberGiverule gr = findGive( member.getBusId(), card.getGtId(), 3 );
		if ( CommonUtil.isNotEmpty( gr ) ) {
		    count = findRechargegive( money, gr.getGrId(), member.getBusId(), card.getCtId() );
		    uc.setGiftCount( count );
		}
		uc.setUccount( 0 );
	    } else if ( card.getCtId() == 5 ) {
		uc.setGiveGift( "赠送次数" );
		MemberGiverule gr = findGive( member.getBusId(), card.getGtId(), 5 );
		if ( CommonUtil.isNotEmpty( gr ) ) {
		    int givecount = findRechargegive( money, gr.getGrId(), member.getBusId(), card.getCtId() );
		    uc.setGiftCount( givecount );
		}
		uc.setUccount( count );
	    }
	    String orderCode = CommonUtil.getMEOrderCode();
	    uc.setOrderCode( orderCode );
	    userConsumeMapper.insert( uc );
	    if ( card.getCtId() == 4 ) {
		memberGive( orderCode, (byte) 1 );
		findGiveRule( member.getPhone(), orderCode, "充值", (byte) 1 );
	    } else {
		memberGive( orderCode, (byte) 1 );
		card = cardMapper.findCardByCardNo( busId, cardNo );
		if ( card.getCtId() == 5 ) {
		    String uccount = "";
		    if ( CommonUtil.isNotEmpty( uc.getGiftCount() ) ) {
			uccount = uc.getUccount() + "次,送" + uc.getGiftCount() + "次";
		    } else {
			uccount = uc.getUccount() + "次";
		    }
		    saveCardRecordNew( uc.getMcId(), (byte) 1, uccount, "充值", member.getBusId(), card.getFrequency().toString(), card.getCtId(), 0.0 );
		} else {
		    if ( CommonUtil.isNotEmpty( uc.getGiftCount() ) && uc.getGiftCount() > 0 ) {
			saveCardRecordNew( uc.getMcId(), (byte) 1, money + "元,送" + uc.getGiftCount() + "元", "充值", member.getBusId(), card.getMoney().toString(), card.getCtId(),
					0.0 );
		    } else {
			saveCardRecordNew( uc.getMcId(), (byte) 1, money + "元", "充值", member.getPublicId(), card.getMoney().toString(), card.getCtId(), 0.0 );
		    }
		}
	    }

	    // 消息模板推送
	    if ( card.getCtId() == 3 ) {
		systemMsgService.sendChuzhiCard( member, money );
	    } else if ( card.getCtId() == 5 ) {
		systemMsgService.sendCikaCard( member, money, uc.getUccount() );
	    }

	    map.put( "result", true );
	    map.put( "message", "充值成功" );
	} catch ( Exception e ) {
	    LOG.error( "充值会员卡异常,卡号：" + cardNo, e );
	    throw new Exception();
	}
	return map;
    }

    @Override
    public Page findConsumeByMemberId( Integer busId, Map< String,Object > params ) {

	params.put( "curPage", CommonUtil.isEmpty( params.get( "curPage" ) ) ? 1 : CommonUtil.toInteger( params.get( "curPage" ) ) );
	int pageSize = 10;

	Integer memberId = 0;
	if ( CommonUtil.isNotEmpty( params.get( "memberId" ) ) ) {
	    memberId = CommonUtil.toInteger( params.get( "memberId" ) );
	} else {
	    return null;
	}

	String startDate = null;
	if ( CommonUtil.isNotEmpty( params.get( "startDate" ) ) ) {
	    startDate = params.get( "startDate" ).toString();
	}
	String endDate = null;
	if ( CommonUtil.isNotEmpty( params.get( "endDate" ) ) ) {
	    endDate = params.get( "endDate" ).toString();
	}

	int rowCount = userConsumeMapper.countUserConumeByMember( busId, memberId, startDate, endDate );

	Page page = new Page( CommonUtil.toInteger( params.get( "curPage" ) ), pageSize, rowCount, "" );
	params.put( "firstResult", pageSize * ( ( page.getCurPage() <= 0 ? 1 : page.getCurPage() ) - 1 ) );
	params.put( "maxResult", pageSize );

	List< Map< String,Object > > list = userConsumeMapper
			.findUserConumeByMember( busId, memberId, Integer.parseInt( params.get( "firstResult" ).toString() ), pageSize, startDate, endDate );
	page.setSubList( list );
	return page;
    }

    @Override
    public Map< String,Object > findMemberByCardNo( String cardNo ) {
	List< Map< String,Object > > listMap = memberMapper.findCardNo( cardNo );
	if ( CommonUtil.isEmpty( listMap ) || listMap.size() <= 0 ) {
	    return null;
	}
	return listMap.get( 0 );
    }

    @Override
    public PublicParameterset findjifenRule( Integer busId ) {
	return publicParameterSetMapper.findBybusId_1( busId );
    }

    @Override
    public Map< String,Object > fenbiRule( Integer busId ) {
	Map< String,Object > map = new HashMap< String,Object >();
	Map< String,Object > dict = dictService.getDict( "1058" );
	Double ratio = CommonUtil.toDouble( dict.get( "1" ) );
	map.put( "ratio", ratio );
	map.put( "startMoney", 10 );
	return map;
    }

    @Override
    public List< Integer > findMemberIds( Integer memberId ) {
	List< Integer > list = new ArrayList< Integer >();
	Member member = memberMapper.selectById( memberId );
	if ( CommonUtil.isEmpty( member.getOldId() ) ) {
	    list.add( memberId );
	    return list;
	}
	String[] str = member.getOldId().split( "," );
	for ( int i = 0; i < str.length; i++ ) {
	    if ( CommonUtil.isNotEmpty( str[i] ) && !str[i].contains( "null" ) && !list.contains( CommonUtil.toInteger( str[i] ) ) ) {
		list.add( CommonUtil.toInteger( str[i] ) );
	    }
	}

	if ( !list.contains( memberId ) ) {
	    list.add( memberId );
	}

	return list;
    }

    @Override
    public Map< String,Object > backFlow( String orderCode ) {
	Map< String,Object > map = new HashMap<>();
	try {
	    UserConsume userConsume = userConsumeMapper.findByOrderCode1( orderCode );
	    if ( CommonUtil.isEmpty( userConsume ) ) {
		map.put( "result", false );
		map.put( "message", "未查询到相对应的数据" );
		return map;
	    }
	    Integer flow = userConsume.getGiveFlow();
	    Integer memberId = userConsume.getMemberId();
	    if ( CommonUtil.isEmpty( flow ) || CommonUtil.isEmpty( memberId ) ) {
		map.put( "result", false );
		map.put( "message", "缺少对应的数据" );
		return map;
	    }
	    Member member = memberMapper.selectById( memberId );
	    Member m = new Member();
	    m.setId( memberId );
	    m.setFlow( flow + member.getFlow() );
	    memberMapper.updateById( m );
	    saveCardRecordNew( member.getMcId(), (byte) 4, flow + "MB", "流量兑换失败,已退回", member.getBusId(), "", 0, flow );
	    UserConsume uc = new UserConsume();
	    uc.setId( userConsume.getId() );
	    uc.setFlowState( 0 );
	    userConsumeMapper.updateById( uc );

	    map.put( "result", true );
	    map.put( "message", "兑换流量回滚成功" );
	} catch ( Exception e ) {
	    LOG.error( "兑换流量回滚异常", e );
	    map.put( "result", false );
	    map.put( "message", "未查询到相对应的数据" );
	    return map;
	}
	return map;
    }

    @Override
    public Map< String,Object > saveMemberConsume( Integer busUserId, Integer memberId, String cardNo, double money, Byte recordType, Byte type, Integer integral, Integer fenbi,
		    Integer uccount, Integer discount, Double discountmoney, Integer orderid, String uctable, Byte paymenttype, Byte paystatus, String givegift, Integer giftCount,
		    String orderCode ) {
	Map< String,Object > map = new HashMap<>();
	try {
	    // 添加会员记录
	    UserConsume uc = new UserConsume();
	    uc.setBusUserId( busUserId );
	    uc.setMemberId( memberId );
	    if ( CommonUtil.isNotEmpty( cardNo ) ) {
		MemberCard card = cardMapper.findCardByCardNo( busUserId, cardNo );
		uc.setMcId( card.getMcId() );
		uc.setCtId( card.getCtId() );
		uc.setGtId( card.getGtId() );
	    }
	    uc.setRecordType( recordType.intValue() );
	    uc.setTotalMoney( money );
	    uc.setCreateDate( new Date() );
	    uc.setPayStatus( 0 );
	    uc.setUcType( type.intValue() );
	    uc.setIntegral( integral );
	    uc.setFenbi( fenbi.doubleValue() );
	    uc.setUccount( uccount );
	    uc.setDiscount( discount );
	    uc.setDiscountMoney( discountmoney );
	    uc.setOrderId( orderid );
	    uc.setUcTable( uctable );
	    uc.setCreateDate( new Date() );
	    uc.setPaymentType( paymenttype.intValue() );
	    uc.setPayStatus( paystatus.intValue() );
	    uc.setGiveGift( givegift );
	    uc.setOrderCode( orderCode );
	    userConsumeMapper.insert( uc );
	    map.put( "result", true );
	    map.put( "message", "添加记录成功" );
	} catch ( Exception e ) {
	    map.put( "result", false );
	    map.put( "message", "添加记录失败" );
	}
	return map;
    }

    @Override
    public Map< String,Object > findMemberShopId( String phone, Integer busId, Integer shopId ) throws Exception {
	Map< String,Object > map = new HashMap<>();

	Member member = memberMapper.findByPhone( busId, phone );
	if ( CommonUtil.isEmpty( member ) || CommonUtil.isEmpty( member.getMcId() ) ) {
	    map.put( "result", false );
	    map.put( "message", "当前用户非会员" );
	    return map;
	}
	MemberCard card = cardMapper.selectById( member.getMcId() );

	if ( card.getShopId() > 0 ) {
	    if ( shopId != card.getShopId() && !shopId.equals( card.getShopId() ) ) {
		map.put( "result", false );
		map.put( "message", "当前用户会员不是该门店会员" );
		return map;
	    }
	} else {
	    JSONObject json = new JSONObject();
	    json.put( "token", TokenUitl.getToken() );
	    json.put( "shopId", shopId );

	    JSONObject returnJSON = HttpClienUtil.httpPost( memberConfig.getWxmp_home()+GET_SHOP_URL, json, false );
	    Map< String,Object > reutnMap = (Map< String,Object >) returnJSON.get( "wxshop" );

	    if ( CommonUtil.isNotEmpty( map ) && !reutnMap.get( "id" ).equals( shopId ) ) {
		map.put( "result", false );
		map.put( "message", "当前用户会员不是该门店会员" );
		return map;
	    }
	}

	List< Map< String,Object > > cards = memberMapper.findMemberBybusIdAndPhone( busId, card.getMcId() );

	map.put( "result", true );
	map.put( "message", "当前用户会员是该门店会员" );
	if ( CommonUtil.isNotEmpty( cards ) && cards.size() > 0 ) {
	    Map< String,Object > c = cards.get( 0 );
	    if ( "0".equals( CommonUtil.toString( c.get( "isChecked" ) ) ) ) {
		map.put( "result", false );
		map.put( "message", "当前用户会员还未审核" );
		return map;
	    }

	    if ( "1".equals( CommonUtil.toString( c.get( "cardStatus" ) ) ) ) {
		map.put( "result", false );
		map.put( "message", "当前用户会员已禁用" );
		return map;
	    }
	    map.put( "card", cards.get( 0 ) );
	}
	return map;

    }

    @Override
    public List< Map< String,Object > > findGradeTypeByApplyType( Integer busId ) {
	return gradeTypeMapper.findGradeTypeByApplyType( busId );
    }

    @Transactional( rollbackFor = Exception.class )
    @Override
    public Map< String,Object > getMemberCardByXiaochangmao( Map< String,Object > params ) throws Exception {
	Map< String,Object > map = new HashMap< String,Object >();
	try {
	    if ( CommonUtil.isEmpty( params ) ) {
		map.put( "result", false );
		map.put( "message", "数据不完整" );
		return map;
	    }

	    // 短信校验
	    Integer memberId = CommonUtil.toInteger( params.get( "memberId" ) );
	    if ( CommonUtil.isEmpty( memberId ) ) {
		map.put( "result", false );
		map.put( "message", "数据不完整" );
		return map;
	    }

	    String code = CommonUtil.toString( params.get( "code" ) );
	    if ( CommonUtil.isEmpty( code ) ) {
		map.put( "result", false );
		map.put( "message", "请输入校验码" );
		return map;
	    }

	    String phone = CommonUtil.toString( params.get( "phone" ) );
	    if ( CommonUtil.isEmpty( phone ) ) {
		map.put( "result", false );
		map.put( "message", "数据不完整" );
		return map;
	    }

	    Integer busId = CommonUtil.toInteger( params.get( "busId" ) );
	    if ( CommonUtil.isEmpty( busId ) ) {
		map.put( "result", false );
		map.put( "message", "数据不完整" );
		return map;
	    }

	    Integer ctId = CommonUtil.toInteger( params.get( "ctId" ) );
	    if ( CommonUtil.isEmpty( ctId ) ) {
		map.put( "result", false );
		map.put( "message", "数据不完整" );
		return map;
	    }

	    // 短信判断
	    if ( CommonUtil.isEmpty( JedisUtil.get( code ) ) ) {
		map.put( "result", false );
		map.put( "message", "短信校验码不正确" );
		return map;
	    }

	    // 分配会员卡
	    // 添加会员卡
	    MemberCard card = new MemberCard();
	    card.setIsChecked( 1 );
	    card.setCardNo( CommonUtil.getCode() );
	    card.setCtId( ctId );
	    card.setSystemcode( CommonUtil.getNominateCode() );
	    card.setApplyType( 0 );
	    card.setMemberId( memberId );
	    card.setBusId( busId );

	    List< Map< String,Object > > gradeTyps = gradeTypeMapper.findAllBybusId( busId, ctId );
	    if ( CommonUtil.isEmpty( gradeTyps ) || gradeTyps.size() == 0 ) {
		map.put( "result", false );
		map.put( "message", "数据查询异常" );
		return map;
	    }
	    card.setGtId( CommonUtil.toInteger( gradeTyps.get( 0 ).get( "gt_id" ) ) );
	    card.setGrId( CommonUtil.toInteger( gradeTyps.get( 0 ).get( "gr_id" ) ) );
	    card.setReceiveDate( new Date() );
	    card.setIsbinding( 1 );
	    card.setIsChecked( 1 );
	    card.setFrequency( 0 );
	    card.setMoney( 0.0 );

	    // 查询要绑定的手机号码
	    Member oldMember = memberMapper.findByPhone( busId, phone );

	    Member member = null;

	    Member m1 = memberMapper.selectById( memberId );

	    if ( CommonUtil.isNotEmpty( oldMember ) && !m1.getId().equals( oldMember.getId() ) ) {
		member = new Member();
		member.setFlow( m1.getFlow() + oldMember.getFlow() );
		member.setIntegral( m1.getIntegral() + oldMember.getIntegral() );
		member.setFansCurrency( m1.getFansCurrency() + oldMember.getFansCurrency() );

		if ( CommonUtil.isNotEmpty( oldMember.getOldId() ) && !oldMember.getOldId().contains( oldMember.getId().toString() ) ) {
		    member.setOldId( oldMember.getOldId() + "," + oldMember.getId() + "," + m1.getId() );
		} else {
		    if ( CommonUtil.isNotEmpty( oldMember.getOldId() ) ) {
			member.setOldId( oldMember.getOldId() + "," + m1.getId() );
		    } else {
			member.setOldId( m1.getId() + "," );
		    }
		}

		if ( CommonUtil.isEmpty( oldMember.getHeadimgurl() ) ) {
		    member.setHeadimgurl( m1.getHeadimgurl() );
		}

		member.setId( oldMember.getId() );
		if ( CommonUtil.isEmpty( oldMember.getMcId() ) ) {
		    cardMapper.insert( card );
		    member.setMcId( card.getMcId() );

		}
		memberMapper.deleteById( m1.getId() );
		memberMapper.updateById( member );

		MemberOld old = (MemberOld) JSONObject.toBean( JSONObject.fromObject( m1 ), MemberOld.class );
		memberOldMapper.insert( old );
		// 修改小程序之前openId对应的memberId
		memberAppletOpenidMapper.updateMemberId( member.getId(), m1.getId() );

		map.put( "member", member );
	    } else {
		// 新用户
		member = memberMapper.selectById( memberId );
		Member m = new Member();
		m.setId( member.getId() );
		m.setPhone( phone );
		if ( CommonUtil.isEmpty( member.getMcId() ) ) {
		    cardMapper.insert( card );
		    m.setMcId( card.getMcId() );
		    member.setMcId( card.getMcId() );
		}
		memberMapper.updateById( m );
		member.setPhone( phone );
		map.put( "member", member );
	    }
	    JedisUtil.del( code );
	    map.put( "result", true );
	    map.put( "message", "领取成功" );
	} catch ( Exception e ) {
	    e.printStackTrace();
	    LOG.error( "小程序绑定手机号码异常", e );
	    throw new Exception();
	}
	return map;
    }

    @Override
    public Map< String,Object > updateIntergral( Integer memberId, Integer intergral ) {
	Map< String,Object > map = new HashMap< String,Object >();
	try {
	    Member member = memberMapper.selectById( memberId );
	    Integer mIntergral = member.getIntegral();
	    if ( mIntergral < -intergral ) {
		map.put( "result", 1 );
		map.put( "message", "积分不足" );
		return map;
	    }
	    Member member1 = new Member();
	    member1.setId( member.getId() );
	    member1.setIntegral( member.getIntegral() + intergral );
	    memberMapper.updateById( member1 );

	    map.put( "result", 2 );
	    map.put( "message", "积分支付成功" );
	} catch ( Exception e ) {
	    LOG.error( "积分支付失败", e );
	    map.put( "result", 1 );
	    map.put( "message", "积分支付异常" );
	}
	return map;
    }

    @Override
    public boolean isAdequateMoney( Integer memberId, double totalMoney ) {
	Member member = memberMapper.selectById( memberId );
	if ( CommonUtil.isEmpty( member.getMcId() ) ) {
	    return false;
	}
	MemberCard card = cardMapper.selectById( member.getMcId() );
	if ( CommonUtil.isNotEmpty( card ) ) {
	    if ( card.getCtId() == 3 ) {
		if ( card.getMoney() >= totalMoney ) {
		    return true;
		}
	    }
	}
	return false;
    }

    @Override
    public Map< String,Object > findMemberCard( BusUser busUser, String cardNoKey, String cardNo, Integer shopId ) {
	Map< String,Object > map = new HashMap< String,Object >();
	String cardNodecrypt = "";
	try {
	    // 如果手动输入 会出现异常
	    cardNodecrypt = EncryptUtil.decrypt( cardNoKey, cardNo );
	} catch ( Exception e ) {
	}

	int busId = busUser.getId();
	if ( busUser.getPid() != 0 ) {
	    busId = dictService.pidUserId( busUser.getPid() );
	}

	if ( cardNodecrypt.contains( "?time" ) ) {
	    // 查询卡号是否存在
	    Long time = Long.parseLong( cardNodecrypt.substring( cardNodecrypt.indexOf( "?time=" ) + 6 ) );

	    cardNo = cardNodecrypt.substring( 0, cardNodecrypt.indexOf( "?time" ) );

	    MemberCard card1 = cardMapper.findCardByCardNo( busId, cardNo );
	    if ( card1.getCtId() == 3 ) {
		// 2分钟后为超时
		if ( DateTimeKit.secondBetween( new Date( time ), new Date() ) > 120 ) {
		    // 二维码已超时
		    map.put( "result", false );
		    map.put( "message", "二维码已超时!" );
		    return map;
		}
	    }
	}

	MemberCard card = null;
	try {
	    // 判断是否借给他人
	    MemberCardLent c = cardLentMapper.findByCode( cardNo );
	    if ( CommonUtil.isNotEmpty( c ) ) {
		// 判断时间是否在有效时间内
		// 5分钟后为超时
		if ( DateTimeKit.secondBetween( c.getCreateDate(), new Date() ) > 300 ) {
		    // 二维码已超时
		    map.put( "result", false );
		    map.put( "message", "二维码已超时!" );
		    return map;
		}
		card = cardMapper.selectById( c.getMcId() );

		map.put( "jie", 1 );
		map.put( "lentMoney", c.getLentMoney() );
		map.put( "clId", c.getId() );
	    }

	} catch ( Exception e ) {

	}

	try {
	    Member member = null;
	    // 查询卡号是否存在
	    if ( CommonUtil.isEmpty( card ) ) {
		card = cardMapper.findCardByCardNo( busId, cardNo );
		if ( CommonUtil.isNotEmpty( card ) ) {
		    member = memberMapper.findByMcIdAndbusId( busId, card.getMcId() );
		}

	    }

	    if ( CommonUtil.isEmpty( card ) ) {
		member = memberMapper.findByPhone( busId, cardNo );

		if ( CommonUtil.isNotEmpty( member ) ) {
		    card = cardMapper.selectById( member.getMcId() );
		}
	    }

	    if ( CommonUtil.isEmpty( card ) ) {
		map.put( "result", false );
		map.put( "message", "卡片不存在!" );
		return map;
	    } else if ( card.getCardStatus() == 1 ) {
		map.put( "result", false );
		map.put( "message", "会员已拉黑!" );
		return map;
	    } else {
		List< Map< String,Object > > cards = cardMapper.findCardById( card.getMcId() );
		MemberGiverule giveRule = giveRuleMapper.selectById( card.getGrId() );
		map.put( "result", true );
		map.put( "nickName", member.getNickname() );
		map.put( "phone", member.getPhone() );
		map.put( "ctName", cards.get( 0 ).get( "ct_name" ) );
		map.put( "gradeName", cards.get( 0 ).get( "gt_grade_name" ) );
		map.put( "cardNo", card.getCardNo() );
		map.put( "ctId", card.getCtId() );
		map.put( "discount", giveRule.getGrDiscount() / 10 );
		map.put( "money", card.getMoney() );
		map.put( "frequency", card.getFrequency() );
		map.put( "fans_currency", member.getFansCurrency() );
		map.put( "integral", member.getIntegral() );
		map.put( "memberId", member.getId() );
		map.put( "cardId", card.getMcId() );

		Double fenbiMoeny = currencyCount( null, member.getFansCurrency() );
		map.put( "fenbiMoeny", fenbiMoeny );

		Double jifenMoeny = integralCount( null, new Double( member.getIntegral() ), busId );
		map.put( "jifenMoeny", jifenMoeny );

		JSONObject json = new JSONObject();
		json.put( "token", TokenUitl.getToken() );
		json.put( "shopId", shopId );

		JSONObject returnJSON = HttpClienUtil.httpPost( memberConfig.getWxmp_home()+GET_SHOP_URL, json, false );
		Map< String,Object > reutnMap = (Map< String,Object >) returnJSON.get( "wxshop" );

		WxPublicUsers wxPublicUsers = wxPublicUsersMapper.selectByUserId( busId );

		if ( CommonUtil.isNotEmpty( wxPublicUsers ) && CommonUtil.isNotEmpty( member.getOpenid() ) && CommonUtil.toInteger( reutnMap.get( "status" ) ) == 2 ) {
		    // 查询优惠券信息
		    List< Map< String,Object > > cardList = wxCardReceiveMapper.findByOpenId1( wxPublicUsers.getId(), member.getOpenid() );
		    List< Map< String,Object > > list = new ArrayList< Map< String,Object > >();
		    if ( CommonUtil.isNotEmpty( cardList ) && cardList.size() > 0 ) {
			for ( Map< String,Object > map2 : cardList ) {
			    // 时间判断
			    if ( CommonUtil.isNotEmpty( map2.get( "begin_timestamp" ) ) && CommonUtil.isNotEmpty( map2.get( "end_timestamp" ) ) ) {
				if ( DateTimeKit.laterThanNow( DateTimeKit.parse( map2.get( "begin_timestamp" ).toString(), "yyyy-MM-dd hh:mm:ss" ) ) ) {
				    continue;
				}
				if ( !DateTimeKit.laterThanNow( DateTimeKit.parse( map2.get( "end_timestamp" ).toString(), "yyyy-MM-dd hh:mm:ss" ) ) ) {
				    continue;
				}
			    } else {
				if ( DateTimeKit.laterThanNow( DateTimeKit.addDays( DateTimeKit.parse( map2.get( "ctime" ).toString(), "yyyy-MM-dd hh:mm:ss" ),
						CommonUtil.toInteger( map2.get( "fixed_begin_term" ) ) ) ) ) {
				    continue;
				}
				if ( !DateTimeKit.laterThanNow( DateTimeKit.addDays( DateTimeKit.parse( map2.get( "ctime" ).toString(), "yyyy-MM-dd hh:mm:ss" ),
						CommonUtil.toInteger( map2.get( "fixed_term" ) ) ) ) ) {
				    continue;
				}
			    }

			    String day = DateTimeKit.getDayToEnglish();
			    if ( !map2.get( "time_limit" ).toString().contains( day ) ) {
				continue;
			    }

			    if ( map2.get( "location_id_list" ).toString().contains( CommonUtil.toString( reutnMap.get( "poiid" ) ) ) ) {
				list.add( map2 );
			    }
			}
		    }
		    if ( list.size() > 0 ) {
			map.put( "code", 1 );
			map.put( "cardList", JSONArray.fromObject( list ) );
		    }
		}
	    }

	    // 查询能使用的多粉优惠券
	    List< Map< String,Object > > duofenCards = findDuofenCardByMemberId( member.getId(), shopId );
	    map.put( "duofenCards", duofenCards );

	    MemberDate memberDate = findMemeberDate( busId, card.getCtId() );
	    if ( card.getCtId() == 2 ) {
		if ( CommonUtil.isNotEmpty( memberDate ) ) {
		    map.put( "memberDiscount", memberDate.getDiscount() );
		    map.put( "memberDate", true );
		}
	    }

	    map.put( "result", true );
	    return map;
	} catch ( Exception e ) {
	    map.put( "result", false );
	    map.put( "message", "查询异常" );
	    LOG.error( "ERP查询会员信息异常", e );
	}
	return map;
    }

    @Transactional( rollbackFor = Exception.class )
    @Override
    public Map< String,Object > payMemberCard( String json ) throws Exception {
	Map< String,Object > returnMap = new HashMap< String,Object >();
	try {
	    JSONObject jsonObject = JSONObject.fromObject( json );
	    Object isUsrYhj = jsonObject.get( "isUsrYhj" ); // 标示 是否使用卡券
	    if ( CommonUtil.isNotEmpty( isUsrYhj ) && CommonUtil.toInteger( isUsrYhj ) == 1 ) {
		String codes = jsonObject.getString( "codes" );
		Integer cardType = jsonObject.getInt( "cardType" );
		if ( cardType == 0 ) {
		    Integer publicId = jsonObject.getInt( "publicId" );
		    WxPublicUsers wxPublicUsers = wxPublicUsersMapper.selectById( publicId );
		    // 微信卡券
		    Map< String,Object > map = wxCardReceive( wxPublicUsers, codes );
		    if ( "-1".equals( CommonUtil.toString( map.get( "result" ) ) ) ) {
			returnMap.put( "result", false );
			returnMap.put( "message", map.get( "message" ) );
			return returnMap;
		    }
		} else {
		    Map< String,Object > params = new HashMap< String,Object >();
		    params.put( "storeId", jsonObject.get( "storeId" ) );
		    params.put( "codes", codes );
		    // 多粉卡券
		    Map< String,Object > map = verificationCard_2( params );
		    if ( !(boolean) map.get( "result" ) ) {
			return map;
		    }
		}
	    }
	    Object isUseCard = jsonObject.get( "isUseCard" ); // 判断是否使用会员储值卡
	    if ( CommonUtil.isNotEmpty( isUseCard ) && CommonUtil.toInteger( isUseCard ) == 1 ) {
		Integer memberId = jsonObject.getInt( "memberId" );
		Double totalMoney = jsonObject.getDouble( "totalMoney" );
		Map< String,Object > map = storePay( memberId, totalMoney );
		if ( "0".equals( CommonUtil.toString( map.get( "result" ) ) ) || "1".equals( CommonUtil.toString( map.get( "result" ) ) ) ) {
		    returnMap.put( "result", false );
		    returnMap.put( "message", map.get( "message" ) );
		    return returnMap;
		}
	    }
	    //
	    Object isUseJifen = jsonObject.get( "isUseJifen" );
	    if ( CommonUtil.isNotEmpty( isUseJifen ) && CommonUtil.toInteger( isUseJifen ) == 1 ) {
		Integer memberId = jsonObject.getInt( "memberId" );
		Double intergral = jsonObject.getDouble( "jifen" );
		Map< String,Object > map = updateMemberIntergral( memberId, -intergral.intValue() );
		if ( "1".equals( CommonUtil.toString( map.get( "result" ) ) ) ) {
		    returnMap.put( "result", false );
		    returnMap.put( "message", map.get( "message" ) );
		    return returnMap;
		}
	    }

	    Object isUsefenbi = jsonObject.get( "isUsefenbi" );
	    if ( CommonUtil.isNotEmpty( isUsefenbi ) && CommonUtil.toInteger( isUsefenbi ) == 1 ) {
		Integer memberId = jsonObject.getInt( "memberId" );
		Double fenbi = jsonObject.getDouble( "fenbi" );
		Member member = memberMapper.selectById( memberId );
		Map< String,Object > map = reduceFansCurrency( member, member.getBusId(), fenbi );
		if ( "1".equals( CommonUtil.toString( map.get( "result" ) ) ) ) {
		    returnMap.put( "result", false );
		    returnMap.put( "message", map.get( "message" ) );
		    return returnMap;
		}
	    }
	} catch ( Exception e ) {
	    LOG.error( "ERP调用payMemberCard接口异常", e );
	    throw new Exception();
	}
	returnMap.put( "result", true );
	returnMap.put( "message", "操作成功" );
	return returnMap;

    }

    /**
     * 判断用户是否是会员 false 不是 true 是
     */
    @Override
    public boolean isMememberByApplet( BusUser busUser, String cardNoKey, String cardNo ) {
	Map< String,Object > map = new HashMap< String,Object >();
	String cardNodecrypt = "";
	try {
	    // 如果手动输入 会出现异常
	    cardNodecrypt = EncryptUtil.decrypt( cardNoKey, cardNo );
	} catch ( Exception e ) {
	}

	int busId = busUser.getId();
	if ( busUser.getPid() != 0 ) {
	    busId = dictService.pidUserId( busUser.getPid() );
	}

	if ( cardNodecrypt.contains( "?time" ) ) {
	    // 查询卡号是否存在
	    Long time = Long.parseLong( cardNodecrypt.substring( cardNodecrypt.indexOf( "?time=" ) + 6 ) );

	    cardNo = cardNodecrypt.substring( 0, cardNodecrypt.indexOf( "?time" ) );

	    MemberCard card1 = cardMapper.findCardByCardNo( busId, cardNo );
	    if ( card1.getCtId() == 3 ) {
		// 2分钟后为超时
		if ( DateTimeKit.secondBetween( new Date( time ), new Date() ) > 120 ) {
		    // 二维码已超时
		    return false;
		}
	    }
	}
	MemberCard card = null;
	try {
	    Member member = null;
	    // 查询卡号是否存在
	    if ( CommonUtil.isEmpty( card ) ) {
		card = cardMapper.findCardByCardNo( busId, cardNo );
		if ( CommonUtil.isNotEmpty( card ) ) {
		    member = memberMapper.findByMcIdAndbusId( busId, card.getMcId() );
		}

	    }

	    if ( CommonUtil.isEmpty( card ) ) {
		member = memberMapper.findByPhone( busId, cardNo );

		if ( CommonUtil.isNotEmpty( member ) ) {
		    card = cardMapper.selectById( member.getMcId() );
		}
	    }

	    if ( CommonUtil.isEmpty( card ) ) {
		return false;
	    } else if ( card.getCardStatus() == 1 ) {
		return false;
	    }
	    return true;
	} catch ( Exception e ) {
	    LOG.error( "小程序查询会员信息异常", e );
	    return false;
	}
    }

    @Override
    public Map< String,Object > countMember( Integer busId ) throws Exception {
	JSONObject json = new JSONObject();
	json.put( "token", TokenUitl.getToken() );
	json.put( "busId", busId );

	JSONObject returnJSON = HttpClienUtil.httpPost( memberConfig.getWxmp_home()+MAIN_SHOP, json, false );
	Map< String,Object > reutnMap = (Map< String,Object >) returnJSON.get( "wxshop" );

	// 统计会员卡
	List< Map< String,Object > > countCard = cardMapper.countMember( busId );
	if ( CommonUtil.isEmpty( countCard ) || countCard.size() == 0 ) {
	    return null;
	}
	Integer onlinecount0 = 0; // 线上
	Integer onlinecount1 = 0; // 线下
	for ( Map< String,Object > map : countCard ) {
	    if ( CommonUtil.isEmpty( map.get( "shopId" ) ) ) {
		if ( "0".equals( CommonUtil.toString( map.get( "online" ) ) ) ) {
		    onlinecount0 = CommonUtil.toInteger( map.get( "countId" ) );
		}

		if ( "1".equals( CommonUtil.toString( map.get( "online" ) ) ) ) {
		    onlinecount1 = CommonUtil.toInteger( map.get( "countId" ) );
		}
	    }
	}

	List< Map< String,Object > > countMemberCard = new ArrayList<>();
	for ( Map< String,Object > map : countCard ) {
	    if ( CommonUtil.isEmpty( map.get( "shopId" ) ) ) {
		continue;
	    }
	    // 主店铺
	    if ( CommonUtil.toString( reutnMap.get( "id" ) ).equals( CommonUtil.toInteger( map.get( "shopId" ) ) ) ) {
		if ( "0".equals( CommonUtil.toString( map.get( "online" ) ) ) ) {
		    map.put( "countId", CommonUtil.toInteger( map.get( "countId" ) ) + onlinecount0 );
		}

		if ( "1".equals( CommonUtil.toString( map.get( "online" ) ) ) ) {
		    map.put( "countId", CommonUtil.toInteger( map.get( "countId" ) ) + onlinecount1 );
		}
	    }
	    countMemberCard.add( map );
	}

	Map< Integer,Map< String,Object > > map1 = new HashMap<>();
	for ( Map< String,Object > map : countMemberCard ) {
	    if ( CommonUtil.isEmpty( map1.get( CommonUtil.toInteger( map.get( "shopId" ) ) ) ) ) {
		if ( "0".equals( CommonUtil.toString( map.get( "online" ) ) ) ) {
		    map.put( "countId1", 0 );
		}

		if ( "1".equals( CommonUtil.toString( map.get( "online" ) ) ) ) {
		    map.put( "countId1", map.get( "countId" ) );
		    map.put( "countId", 0 );

		}
		map1.put( CommonUtil.toInteger( map.get( "shopId" ) ), map );
	    } else {
		if ( "1".equals( CommonUtil.toString( map.get( "online" ) ) ) ) {
		    map.put( "countId1", map.get( "countId" ) );
		}
		map.put( "countId", map1.get( CommonUtil.toInteger( map.get( "shopId" ) ) ).get( "countId" ) );

		map1.put( CommonUtil.toInteger( map.get( "shopId" ) ), map );
	    }
	}

	Map< String,Object > memMap = null;

	Integer online0 = 0;
	Integer online1 = 0;
	List< Map< String,Object > > list = new ArrayList<>();
	for ( Integer in : map1.keySet() ) {
	    memMap = map1.get( in );// 得到每个key多对用value的值
	    online0 = online0 + CommonUtil.toInteger( memMap.get( "countId" ) );
	    online1 = online1 + CommonUtil.toInteger( memMap.get( "countId1" ) );
	    list.add( memMap );
	}
	Map< String,Object > maps = new HashMap<>();
	maps.put( "cardMember", list );
	maps.put( "online0", online0 );
	maps.put( "online1", online1 );
	return maps;
    }

    /**
     * 查询用户拥有的优惠券
     */
    public List< Map< String,Object > > findDuofenCardByMemberId( Integer memberId, Integer wxshopId ) {
	List< Map< String,Object > > duofencardgets = duofenCardGetMapper.findCardByMemberId( memberId );
	if ( CommonUtil.isEmpty( duofencardgets ) || duofencardgets.size() == 0 ) {
	    return null;
	}

	List< Map< String,Object > > duofencards = new ArrayList< Map< String,Object > >();
	for ( Map< String,Object > map : duofencardgets ) {
	    if ( "2".equals( map.get( "card_type" ).toString() ) || "3".equals( map.get( "card_type" ).toString() ) || "4".equals( map.get( "card_type" ).toString() ) ) {
		continue;
	    }

	    String day = DateTimeKit.getDayToEnglish();
	    if ( !map.get( "time_limit" ).toString().contains( day ) ) {
		continue;
	    }

	    if ( CommonUtil.isNotEmpty( map.get( "location_id_list" ) ) ) {
		String location_id_list = CommonUtil.toString( map.get( "location_id_list" ) );
		if ( location_id_list.contains( wxshopId.toString() ) ) {
		    duofencards.add( map );
		}
	    } else {
		duofencards.add( map );
	    }
	}
	return duofencards;
    }

    @Transactional( rollbackFor = Exception.class )
    public Map< String,Object > wxCardReceive( WxPublicUsers wxPublicUsers, String code ) {
	Map< String,Object > map = new HashMap< String,Object >();
	try {
	    if ( CommonUtil.isEmpty( wxPublicUsers ) ) {
		map.put( "result", -1 );
		map.put( "message", "核销失败" );
		return map;
	    }
	    WxCardReceive wcr = wxCardReceiveMapper.findByCode1( wxPublicUsers.getId(), code );
	    if ( CommonUtil.isEmpty( wcr ) ) {
		map.put( "result", -1 );
		map.put( "message", "核销失败" );
		return map;
	    }
	    map.put( "card_id", wcr.getCardId() );
	    map.put( "code", code );
	    map.put( "card_id", wcr.getCardId() );
	    map.put( "code", code );
	    map.put( "token", TokenUitl.getToken() );
	    map.put( "wxPublicUsersId", wxPublicUsers.getId() );

	    JSONObject returnJSON = HttpClienUtil.httpPost( memberConfig.getWxmp_home()+CODE_CONSUME, JSONObject.fromObject( map ), false );

	    if ( "-1".equals( returnJSON.get( "code" ).toString() ) ) {
		map.put( "result", -1 );
		map.put( "message", "微信核销失败" );
		return map;
	    }

	    if ( "-2".equals( returnJSON.get( "code" ).toString() ) ) {
		map.put( "result", -1 );
		map.put( "message", "token已失效" );
		return map;
	    }
	    map.put( "result", 1 );
	    map.put( "message", "核销成功" );
	} catch ( Exception e ) {
	    e.printStackTrace();
	    LOG.error( "线下核销失败", e );
	    map.put( "result", -1 );
	    map.put( "message", "核销失败" );
	}
	return map;
    }

    public Map< String,Object > verificationCard_2( Map< String,Object > params ) {
	Map< String,Object > map = new HashMap< String,Object >();
	try {
	    if ( CommonUtil.isEmpty( params.get( "codes" ) ) ) {
		map.put( "result", false );
		map.put( "message", "没有要核销的卡券code值" );
		return map;
	    }

	    // 多粉优惠券处理
	    String codes = params.get( "codes" ).toString();
	    String[] str = codes.split( "," );
	    List< String > codeList = new ArrayList< String >();
	    for ( String s : str ) {
		if ( CommonUtil.isNotEmpty( s ) ) {
		    codeList.add( CommonUtil.toString( s ) );
		}
	    }

	    List< Map< String,Object > > stateMap = duofenCardGetMapper.findByCodes( codeList );
	    if ( CommonUtil.isEmpty( stateMap ) || stateMap.size() == 0 ) {
		map.put( "result", false );
		map.put( "message", "卡券不存在" );
		return map;
	    }
	    for ( Map< String,Object > map2 : stateMap ) {
		if ( "1".equals( map2.get( "state" ) ) || "2".equals( map2.get( "state" ) ) ) {
		    map.put( "result", false );
		    map.put( "message", "卡券过期或已核销,不能执行卡券核销操作" );
		    return map;
		}
	    }

	    if ( CommonUtil.isEmpty( params.get( "storeId" ) ) ) {
		duofenCardGetMapper.updateByCodes( codeList ); // 卡券核销
	    } else {
		Integer storeId = CommonUtil.toInteger( params.get( "storeId" ) );
		duofenCardGetMapper.updateStoreIdByCodes( codeList, storeId ); // 卡券核销
	    }

	    //推荐优惠券赠送
	    for ( Map< String,Object > map2 : stateMap ) {
		if ( CommonUtil.toInteger( map2.get( "recommendId" ) ) > 0 ) {
		    MemberRecommend recommend = recommendMapper.selectById( CommonUtil.toInteger( map2.get( "recommendId" ) ) );
		    tuijianGive( recommend );
		}
	    }

	    map.put( "result", true );
	    map.put( "message", "核销成功" );
	} catch ( Exception e ) {
	    map.put( "result", false );
	    map.put( "message", "核销失败" );
	}
	return map;
    }

    /**
     * 多粉卡券核销推荐调用
     *
     * @param recommend
     *
     * @throws Exception
     */
    public void tuijianGive( MemberRecommend recommend ) throws Exception {
	boolean flag = false;

	//赠送积分、粉币、流量、金额
	Member tuijianMember = memberMapper.selectById( recommend.getMemberId() );
	Member m1 = new Member();
	m1.setId( recommend.getMemberId() );
	if ( recommend.getIntegral() > 0 ) {
	    m1.setIntegral( tuijianMember.getIntegral() + recommend.getIntegral() );
	    //积分记录
	    memberCardService.saveCardRecordNew( tuijianMember.getMcId(), (byte) 2, recommend.getIntegral() + "", "推荐优惠券赠送", tuijianMember.getBusId(), null, 0,
			    Double.valueOf( recommend.getIntegral() ) );
	    flag = true;
	}
	if ( recommend.getFenbi() > 0 ) {

	    BusUser busUser = busUserMapper.selectById( tuijianMember.getBusId() );
	    if ( busUser.getFansCurrency().doubleValue() >= recommend.getFenbi() ) {
		JSONObject json = new JSONObject();
		json.put( "token", TokenUitl.getToken() );
		json.put( "busId", busUser.getId() );
		json.put( "fenbi", recommend.getFenbi() );

		HttpClienUtil.httpPost( memberConfig.getWxmp_home()+saveFenbiFlowRecord, json, false );
		// 新增粉笔和流量分配表
		//				FenbiFlowRecord fenbi = new FenbiFlowRecord();
		//				fenbi.setBusUserId(busUser.getId());
		//				fenbi.setRecType(1);
		//				fenbi.setRecCount(Double.valueOf(recommend.getFenbi()));
		//				fenbi.setRecUseCount(Double.valueOf(recommend.getFenbi()));
		//				fenbi.setRecDesc("推荐优惠券赠送粉币");
		//				fenbi.setRecFreezeType(102);
		//				fenbi.setRollStatus(2);
		//				fenbi.setFlowType(0);
		//				fenbi.setFlowId(0);
		//				fenbiFlowRecordMapper.insertSelective(fenbi);

		BusUser b = new BusUser();
		b.setId( busUser.getId() );
		Double fenbi1 = busUser.getFansCurrency().doubleValue() - recommend.getFenbi();
		b.setFansCurrency( BigDecimal.valueOf( fenbi1 ) );
		busUserMapper.updateById( b );

		m1.setFansCurrency( tuijianMember.getFansCurrency() + recommend.getFenbi() );
		//粉币记录
		memberCardService.saveCardRecordNew( tuijianMember.getMcId(), (byte) 3, recommend.getFenbi() + "", "推荐优惠券赠送", tuijianMember.getBusId(), null, 0,
				Double.valueOf( recommend.getFenbi() ) );
		flag = true;
	    }
	}

	if ( recommend.getFlow() > 0 ) {
	    m1.setFlow( tuijianMember.getFlow() + recommend.getFlow() );
	    //流量记录
	    memberCardService.saveCardRecordNew( tuijianMember.getMcId(), (byte) 4, recommend.getFlow() + "", "推荐优惠券赠送", tuijianMember.getBusId(), null, 0,
			    Double.valueOf( recommend.getFlow() ) );
	    flag = true;
	}
	if ( flag ) {
	    memberMapper.updateById( m1 );
	}

	if ( recommend.getMoney() > 0 ) {
	    MemberCard card = cardMapper.selectById( tuijianMember.getMcId() );
	    MemberCard c = new MemberCard();
	    c.setMcId( card.getMcId() );
	    c.setGiveMoney( card.getGiveMoney() + recommend.getMoney() );
	    cardMapper.updateById( c );
	    //流量记录
	    memberCardService.saveCardRecordNew( tuijianMember.getMcId(), (byte) 1, recommend.getMoney() + "", "推荐优惠券赠送", tuijianMember.getBusId(), null, 0,
			    Double.valueOf( recommend.getMoney() ) );
	}

	MemberRecommend r = new MemberRecommend();
	r.setId( recommend.getId() );
	r.setUserNum( recommend.getUserNum() + 1 );
	recommendMapper.updateById( r );
    }

    @Override
    public Member findById( Integer memberId ) {
	Member member = memberMapper.selectById( memberId );
	return member;
    }

}
