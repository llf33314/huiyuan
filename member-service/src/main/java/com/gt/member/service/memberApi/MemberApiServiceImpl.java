/**
 * P 2016年4月5日
 */
package com.gt.member.service.memberApi;

import com.gt.common.entity.FenbiFlowRecord;
import com.gt.member.dao.common.BusUserDAO;
import com.gt.member.dao.common.FenbiFlowRecordDAO;
import com.gt.member.dao.common.WxPublicUsersDAO;
import com.gt.member.dao.common.WxShopDAO;
import com.gt.common.entity.BusUser;
import com.gt.common.entity.WxPublicUsers;
import com.gt.common.entity.WxShop;
import com.gt.member.dao.*;
import com.gt.member.entity.*;
import com.gt.member.enums.ResponseEnums;
import com.gt.member.enums.ResponseMemberEnums;
import com.gt.member.exception.BusinessException;
import com.gt.member.service.common.MemberCommonService;
import com.gt.member.service.entityBo.PaySuccessBo;
import com.gt.member.service.common.dict.DictService;
import com.gt.member.service.member.MemberCardService;
import com.gt.member.service.member.SystemMsgService;
import com.gt.member.util.*;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author pengjiangli
 * @version 创建时间:2016年4月5日
 */
@Service
public class MemberApiServiceImpl implements MemberApiService {

    private static final Logger LOG = LoggerFactory.getLogger( MemberApiServiceImpl.class );

    @Autowired
    private MemberConfig memberConfig;

    @Autowired
    private MemberCardDAO cardMapper;

    @Autowired
    private MemberDAO memberDAO;

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

    @Autowired
    private WxShopDAO wxShopDAO;

    @Autowired
    private CardCouponsApiService cardCouponsApiService;

    @Autowired
    private RedisCacheUtil redisCacheUtil;

    @Autowired
    private MemberLogDAO memberLogDAO;

    @Autowired
    private MemberGradetypeDAO memberGradetypeDAO;

    @Autowired
    private MemberCardrecordDAO memberCardrecordDAO;

    @Autowired
    private MemberCommonService memberCommonService;

    @Autowired
    private FenbiFlowRecordDAO fenbiFlowRecordDAO;

    @Autowired
    private MemberCardDAO memberCardDAO;

    /**
     * 查询粉丝信息
     *
     * @param memberId
     *
     * @return
     */
    public Member findByMemberId( Integer memberId ) throws BusinessException {
	try {
	    return memberDAO.selectById( memberId );
	} catch ( Exception e ) {
	    LOG.error( "查询粉丝信息异常", e );
	    throw new BusinessException( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getDesc() );
	}
    }

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
    @Transactional
    public void findGiveRule( String orderId, String itemName, byte type ) throws BusinessException {
	List< Map< String,Object > > ucs = userConsumeMapper.findByOrderCode( orderId );
	if ( CommonUtil.isEmpty( ucs ) || ucs.size() == 0 || ucs.size() > 1 ) {
	    throw new BusinessException( ResponseMemberEnums.NO_DATA.getCode(), ResponseMemberEnums.NO_DATA.getMsg() );
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

	    // 查询粉笔数量
	    Integer fenbi = fenbiFlowRecordDAO.getFenbiSurplus( busUserId, 1, recFreezeType, ctId );

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

	    MemberDate memberday = memberCommonService.findMemeberDate( busUserId, ctId );
	    boolean flag = false; // 表示今天是否是会员日
	    if ( CommonUtil.isNotEmpty( memberday ) ) {
		flag = true;
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

				    // 修改粉笔数量
				    fenbiFlowRecordDAO.updateFenbiReduce( busUserId, count, ctId, recFreezeType );
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

	    if ( CommonUtil.isNotEmpty( ucs.get( 0 ).get( "mcId" ) ) ) {
		MemberCard card = cardMapper.selectById( Integer.parseInt( ucs.get( 0 ).get( "mcId" ).toString() ) );

		// 修改会员的流量 粉笔 积分信息
		Member member1 = memberDAO.findByMcIdAndbusId( card.getBusId(), Integer.parseInt( ucs.get( 0 ).get( "mcId" ).toString() ) );
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
			memberDAO.updateById( member );
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
	    throw new BusinessException( ResponseEnums.ERROR );
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
    public void saveCardRecordNew( Integer cardId, Byte recordType, String number, String itemName, Integer busId, String balance, Integer ctId, double amount ) {
	memberCardService.saveCardRecordNew( cardId, recordType, number, itemName, busId, balance, ctId, amount );
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
	MemberDate memberdate = memberCommonService.findMemeberDate( busId, ctId );
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
    public void memberCallBack( String orderId, Byte payStatus ) throws BusinessException {
	try {

	    LOG.error( "订单单号 ：" + orderId );
	    List< Map< String,Object > > ucs = userConsumeMapper.findByOrderCode( orderId );

	    if ( payStatus == 1 ) {

		memberGive( orderId, (byte) 1 );

		if ( CommonUtil.isNotEmpty( ucs.get( 0 ).get( "mcId" ) ) ) {
		    MemberCard card = cardMapper.selectById( Integer.parseInt( ucs.get( 0 ).get( "mcId" ).toString() ) );

		    // 修改会员的流量 粉笔 积分信息
		    Member member = memberDAO.findByMcIdAndbusId( card.getBusId(), Integer.parseInt( ucs.get( 0 ).get( "mcId" ).toString() ) );

		    if ( card.getCtId() == 5 ) {
			String uccount = "";
			if ( CommonUtil.isNotEmpty( ucs.get( 0 ).get( "giftCount" ) ) ) {
			    uccount = ucs.get( 0 ).get( "uccount" ) + "次,送" + ucs.get( 0 ).get( "giftCount" ) + "次";
			} else {
			    uccount = ucs.get( 0 ).get( "uccount" ) + "次";
			}
			saveCardRecordNew( Integer.parseInt( ucs.get( 0 ).get( "mcId" ).toString() ), (byte) 1, uccount, "充值", member.getBusId(), card.getFrequency().toString(),
					card.getCtId(), 0.0 );
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
	    throw new BusinessException( ResponseEnums.SYSTEM_ERROR.getCode(), ResponseEnums.SYSTEM_ERROR.getDesc() );
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
		    MemberDate membetdate = memberCommonService.findMemeberDate( card.getBusId(), card.getCtId() );

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
    public Double findCardTypeReturnDiscount( Integer memberId ) throws BusinessException {
	try {
	    if ( CommonUtil.isEmpty( memberId ) ) {
		throw new BusinessException( ResponseMemberEnums.NULL );
	    }
	    Member member = memberDAO.selectById( memberId );
	    if ( CommonUtil.isEmpty( member ) || CommonUtil.isEmpty( member.getMcId() ) ) {
		throw new BusinessException( ResponseMemberEnums.NO_DATA );
	    }
	    MemberCard card = cardMapper.selectById( member.getMcId() );
	    if ( CommonUtil.isEmpty( card ) ) {
		throw new BusinessException( ResponseMemberEnums.NO_DATA );
	    }

	    if ( card.getCtId() == 2 ) {
		MemberDate memberDate = memberCommonService.findMemeberDate( member.getBusId(), 2 );

		MemberGiverule giveRule = giveRuleMapper.selectById( card.getGrId() );
		if ( CommonUtil.isNotEmpty( memberDate ) ) {
		    return new Double( giveRule.getGrDiscount() * memberDate.getDiscount() ) / 1000;
		} else {
		    return new Double( giveRule.getGrDiscount() ) / 100;
		}
	    }
	    return 1.0;
	} catch ( BusinessException e ) {
	    throw new BusinessException( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    LOG.error( "查询会员折扣异常", e );
	    throw new BusinessException( ResponseEnums.ERROR );
	}
    }

    /**
     * 会员储值卡消费
     */
    public void storePay( Integer memberId, double totalMoney ) throws BusinessException {
	Map< String,Object > map = new HashMap< String,Object >();
	Member member = memberDAO.selectById( memberId );
	if ( CommonUtil.isEmpty( member.getMcId() ) ) {
	    throw new BusinessException( ResponseMemberEnums.NOT_MEMBER_CAR.getCode(), ResponseMemberEnums.NOT_MEMBER_CAR.getMsg() );
	}
	MemberCard card = cardMapper.selectById( member.getMcId() );
	if ( CommonUtil.isNotEmpty( card ) ) {
	    if ( card.getCtId() == 3 ) {
		if ( card.getMoney() < totalMoney ) {
		    throw new BusinessException( ResponseMemberEnums.MEMBER_LESS_MONEY.getCode(), ResponseMemberEnums.MEMBER_LESS_MONEY.getMsg() );
		}
		double banlan = card.getMoney() - totalMoney;
		card.setMoney( banlan );
		cardMapper.updateById( card );
		saveCardRecordNew( card.getMcId(), (byte) 1, "-" + totalMoney, "储值卡消费", member.getBusId(), null, 0, -totalMoney );
		systemMsgService.sendChuzhiXiaofei( member, totalMoney );
	    }
	} else {
	    throw new BusinessException( ResponseMemberEnums.MEMBER_CHUZHI_CARD.getCode(), ResponseMemberEnums.MEMBER_CHUZHI_CARD.getMsg() );
	}
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
    public void isMemember( Integer memberId ) throws BusinessException {
	Member member = memberDAO.selectById( memberId );
	if ( CommonUtil.isNotEmpty( member ) && CommonUtil.isNotEmpty( member.getMcId() ) ) {
	    MemberCard card = cardMapper.selectById( member.getMcId() );
	    if ( card.getIsChecked() == 0 || card.getCardStatus() == 1 ) {
		throw new BusinessException( ResponseMemberEnums.CARD_STATUS );
	    }
	}
	throw new BusinessException( ResponseMemberEnums.NOT_MEMBER_CAR );
    }

    /**
     * 判断用户的卡类型 1积分卡 2折扣卡 3储值卡 4时效卡 5次卡
     *
     * @param memberId
     *
     * @return
     */
    public Integer isCardType( Integer memberId ) throws BusinessException {
	if ( CommonUtil.isEmpty( memberId ) ) {
	    throw new BusinessException( ResponseMemberEnums.NULL );
	}
	Member member = memberDAO.selectById( memberId );
	if ( CommonUtil.isEmpty( memberId ) || CommonUtil.isEmpty( member.getMcId() ) ) {
	    throw new BusinessException( ResponseMemberEnums.NOT_MEMBER_CAR );
	}
	MemberCard card = cardMapper.selectById( member.getMcId() );
	if ( CommonUtil.isEmpty( card ) ) {
	    throw new BusinessException( ResponseMemberEnums.CARD_STATUS );
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
	Member member = memberDAO.selectById( memberId );
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
	//TODO
	//	if ( !isMemember( memberId ) ) {
	//	    map.put( "result", false );
	//	    map.put( "message", "非会员" );
	//	    return map;
	//	}

	// 根据会员id查询赠送规则
	Member member = memberDAO.selectById( memberId );
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
     *
     * @throws Exception
     */
    @Transactional( rollbackFor = Exception.class )
    public void findGiveRuleDelay( String orderNo ) throws Exception {
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
	    MemberDate memberDate = memberCommonService.findMemeberDate( busUserId, ctId );
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

	    // 查询粉笔数量
	    Integer fenbi = fenbiFlowRecordDAO.getFenbiSurplus( busUserId, 1, recFreezeType, ctId );

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
				    fenbiFlowRecordDAO.updateFenbiReduce( busUserId, count, ctId, recFreezeType );
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
	findGiveRuleDelay( orderCode );
    }

    /**
     * 储值卡退款订单
     *
     * @param memberId    订单号
     * @param refundMoney 退款金额
     */
    @Transactional
    @Override
    public void chargeBack( Integer memberId, double refundMoney ) throws BusinessException {
	Map< String,Object > map = new HashMap< String,Object >();
	try {
	    Member member = memberDAO.selectById( memberId );
	    if ( CommonUtil.isEmpty( member ) || CommonUtil.isEmpty( member.getMcId() ) ) {
		throw new BusinessException( ResponseMemberEnums.MEMBER_NOT_CARD.getCode(), ResponseMemberEnums.MEMBER_NOT_CARD.getMsg() );
	    }
	    MemberCard card = cardMapper.selectById( member.getMcId() );
	    MemberCard card1 = new MemberCard();
	    // 储值卡
	    card1.setMcId( member.getMcId() );
	    card1.setMoney( card.getMoney() + refundMoney );
	    cardMapper.updateById( card1 );

	    saveCardRecordNew( card.getMcId(), (byte) 1, refundMoney + "", "储值卡退款", card.getBusId(), null, 0, 0 );
	    systemMsgService.sendChuzhiTuikuan( member, refundMoney );

	} catch ( BusinessException e ) {
	    throw new BusinessException( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    LOG.error( "退款异常", e );
	    throw new BusinessException( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getDesc() );
	}

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
	saveCardRecordNew( uc.getMcId(), (byte) 1, uc.getTotalMoney() + "", "退款", uc.getBusUserId(), card.getMoney().toString(), uc.getCtId(), 0.0 );
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
	Member member = memberDAO.selectById( uc.getMemberId() );
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
		memberDAO.updateById( member );

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
	    memberDAO.updateById( member );
	    String balance = null;
	    if ( card.getCtId() == 5 ) {
		balance = card.getFrequency() + "次";
	    } else {
		balance = card.getMoney() + "元";
	    }
	    saveCardRecordNew( card.getMcId(), (byte) 1, ucs.get( 0 ).get( "discountMoney" ) + "元", "购买会员卡", card.getBusId(), balance, card.getCtId(), 0.0 );

	    // 新增会员短信通知
	    member = memberDAO.selectById( CommonUtil.toInteger( ucs.get( 0 ).get( "memberId" ) ) );
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
	    Member member = memberDAO.findByMcIdAndbusId( busId, card.getMcId() );
	    Integer jifen = member.getIntegral();
	    if ( intergral > jifen ) {
		map.put( "result", false );
		map.put( "message", "积分不够!" );
		return map;
	    }
	    Member mem = new Member();
	    mem.setId( member.getId() );
	    mem.setIntegral( member.getIntegral() - intergral );
	    memberDAO.updateById( mem );
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

	    saveCardRecordNew( card.getMcId(), (byte) 2, "-" + intergral + "积分", "积分兑换", card.getBusId(), null, 0, -intergral );
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
    public void updateMemberIntergral( Integer memberId, Integer intergral ) throws BusinessException {
	try {
	    Member member = memberDAO.selectById( memberId );
	    Integer mIntergral = member.getIntegral();
	    Member member1 = new Member();
	    member1.setId( member.getId() );
	    member1.setIntegral( member.getIntegral() + intergral );
	    memberDAO.updateById( member1 );
	    if ( CommonUtil.isNotEmpty( member.getMcId() ) ) {
		saveCardRecordNew( member.getMcId(), (byte) 2, intergral + "积分", "积分", member.getBusId(), "", null, intergral );
	    }
	} catch ( Exception e ) {
	    throw new BusinessException( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getDesc() );
	}
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
	    memberDAO.updateById( m );
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
	    MemberDate memberDate = memberCommonService.findMemeberDate( busUserId, ctId );

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
	    Integer fenbi = fenbiFlowRecordDAO.getFenbiSurplus( busUserId, 1, recFreezeType, ctId );

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
				    fenbiFlowRecordDAO.updateFenbiReduce( busUserId, count, ctId, recFreezeType );
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

    @Transactional( rollbackFor = Exception.class )
    @Override
    public Map< String,Object > reduceFansCurrencyMoney( Member member, Integer busId, Double fenbimoney ) throws Exception {
	Map< String,Object > map = new HashMap< String,Object >();
	try {
	    int fenbi = 0;
	    if ( fenbimoney > 0 ) {
		List< Map< String,Object > > dict = dictService.getDict( "1058" );
		fenbi = new Double( fenbimoney * CommonUtil.toDouble( dict.get( 0 ).get( "1" ) ) ).intValue();
	    } else {
		map.put( "code", "-1" );
		map.put( "message", "未存在粉币扣除" );
		return map;
	    }

	    Member m = new Member();
	    m.setId( member.getId() );
	    m.setFansCurrency( member.getFansCurrency() - fenbi );
	    memberDAO.updateById( m );

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

    @Transactional
    @Override
    public void reduceFansCurrency( Member member, Integer busId, Double fenbi ) throws BusinessException {
	try {
	    if ( member.getFansCurrency() < fenbi ) {
		throw new BusinessException( ResponseMemberEnums.MEMBER_LESS_FENBI.getCode(), ResponseMemberEnums.MEMBER_LESS_FENBI.getMsg() );
	    }
	    Member m = new Member();
	    m.setId( member.getId() );
	    m.setFansCurrency( member.getFansCurrency() - fenbi );
	    memberDAO.updateById( m );

	    if ( CommonUtil.isNotEmpty( member.getMcId() ) ) {
		saveCardRecordNew( member.getMcId(), (byte) 3, "-" + fenbi + "粉币", "粉币", member.getBusId(), "", null, -fenbi );
	    }

	    BusUser busUser = busUserMapper.selectById( busId );
	    BusUser busUser1 = new BusUser();
	    busUser1.setId( busId );
	    Double fenbi1 = busUser.getFansCurrency().doubleValue() + fenbi;
	    busUser1.setFansCurrency( BigDecimal.valueOf( fenbi1 ) );
	    busUserMapper.updateById( busUser1 );
	} catch ( BusinessException e ) {
	    LOG.error( "粉币抵扣异常", e );
	    throw new BusinessException( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    throw new BusinessException( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getDesc() );
	}
    }

    @Override
    public MemberGradetype findGradeType( Integer memberId ) {
	Member member = memberDAO.selectById( memberId );
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
	    Member member = memberDAO.selectById( memberId );
	    if ( member.getFansCurrency() < fenbi ) {
		map.put( "result", "1" );
		map.put( "message", "粉币不足" );
		return map;
	    }
	    BusUser busUser = busUserMapper.selectById( busId );

	    Member m = new Member();
	    m.setId( member.getId() );
	    m.setFansCurrency( member.getFansCurrency() - fenbi );
	    memberDAO.updateById( m );

	    if ( CommonUtil.isNotEmpty( member.getMcId() ) ) {
		saveCardRecordNew( member.getMcId(), (byte) 3, fenbi + "粉币", "粉币扣除", member.getBusId(), "", null, -fenbi );
	    }

	    BusUser busUser1 = new BusUser();
	    busUser1.setId( busId );
	    Double feibi1 = busUser.getFansCurrency().doubleValue() + fenbi;
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
     * 会员卡充值
     */
    @Override
    @Transactional( rollbackFor = Exception.class )
    public Map< String,Object > rechargeMember( Integer memberId, String cardNo, Double money, Integer count ) throws Exception {
	if ( CommonUtil.isEmpty( memberId ) || CommonUtil.isEmpty( cardNo ) || CommonUtil.isEmpty( money ) ) {
	    throw new Exception();
	}
	try {
	    Member member = memberDAO.selectById( memberId );
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
	    WxShop wxShop = wxShopDAO.selectMainShopByBusId( member.getBusId() );

	    if ( CommonUtil.isNotEmpty( wxShop ) ) {
		uc.setStoreId( wxShop.getId() );
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
	Member member = memberDAO.queryOpenid( openId );
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
	Double jifenMoney = memberCommonService.integralCount( null, member.getIntegral().doubleValue(), member.getBusId() );
	returnMap.put( "jifenMoney", jifenMoney );

	Double fenbiMoney = memberCommonService.currencyCount( null, member.getFansCurrency() );

	returnMap.put( "fenbiMoney", fenbiMoney );

	if ( card.getCtId() == 2 ) {
	    MemberDate memberDate = memberCommonService.findMemeberDate( member.getPublicId(), 2 );

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
	    Member member = memberDAO.queryOpenid( openId );
	    UserConsume uc = new UserConsume();
	    // 添加会员记录
	    MemberCard card = null;
	    //TODO
	    //	    if ( isMemember( member.getId() ) ) {
	    //		card = cardMapper.selectById( member.getMcId() );
	    //		uc.setMcId( member.getMcId() );
	    //		uc.setCtId( card.getCtId() );
	    //		uc.setGtId( card.getGtId() );
	    //		uc.setDiscount( 100 );
	    //	    }
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
		List< Map< String,Object > > dict = dictService.getDict( "1058" );
		Double fenbi = CommonUtil.toDouble( dict.get( 0 ).get( "1" ) ) * fenbiMoney;
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
		//TODO
		//		if ( isMemember( member.getId() ) ) {
		//		    // 储值卡付款
		//		    if ( CommonUtil.isNotEmpty( card ) && card.getCtId() == 3 && pay > 0 ) {
		//			if ( pay > card.getMoney() ) {
		//			    throw new Exception();
		//			}
		//			MemberCard c = new MemberCard();
		//			c.setMcId( member.getMcId() );
		//			c.setMoney( card.getMoney() - pay );
		//			cardMapper.updateById( c );
		//		    }
		//		}
		uc.setPayStatus( 1 );
		userConsumeMapper.insert( uc );

		//TODO
		//		if ( isMemember( member.getId() ) ) {
		//		    saveGiveConsume( member.getPhone(), orderCode );
		//		    giveGood( orderCode );
		//		    saveCardRecordNew( member.getMcId(), (byte) 1, uc.getDiscountMoney() + "", "线下扫码消费", member.getBusId(), null, 0, 0 );
		//		}

		if ( fenbiMoney > 0 ) {
		    // 归还商户粉币
		    returnfansCurrency( wxPublicUsers.getBusUserId(), -uc.getFenbi().doubleValue() );
		    Member member1 = new Member();
		    member1.setId( member.getId() );
		    member1.setFansCurrency( member.getFansCurrency() + uc.getFenbi() );
		    memberDAO.updateById( member1 );

		}
		if ( jifenMoney > 0 ) {
		    // 扣除用户积分数量
		    Member member1 = new Member();
		    member1.setId( member.getId() );
		    member1.setIntegral( member.getIntegral() + uc.getIntegral() );
		    memberDAO.updateById( member1 );
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
	    Member member = memberDAO.selectById( userConsume.getMemberId() );
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
		memberDAO.updateById( m1 );
	    }
	    // 修改订单状态
	    UserConsume uc = new UserConsume();
	    uc.setId( userConsume.getId() );
	    uc.setPayStatus( 1 );
	    userConsumeMapper.updateById( uc );
	    //TODO
	    //	    if ( isMemember( member.getId() ) ) {
	    //		saveGiveConsume( member.getPhone(), orderCode );
	    //		giveGood( orderCode );
	    //		saveCardRecordNew( member.getMcId(), (byte) 1, userConsume.getDiscountMoney() + "", "线下扫码消费", member.getBusId(), null, 0, 0.0 );
	    //	    }
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
	    Member member = memberDAO.selectById( userConsume.getMemberId() );
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
		memberDAO.updateById( m1 );
	    }
	    // 修改订单状态
	    UserConsume uc = new UserConsume();
	    uc.setId( userConsume.getId() );
	    uc.setPayStatus( 3 );
	    userConsumeMapper.updateById( uc );

	    Double fenbi1 = 0.0;
	    //TODO
	    //	    if ( isMemember( member.getId() ) ) {
	    //		boolean flag1 = false;
	    //		List< MemberGiveconsume > giveConsume = giveConsumeMapper.findByUcId( userConsume.getId() );
	    //		for ( MemberGiveconsume gc : giveConsume ) {
	    //		    switch ( gc.getGtId() ) {
	    //			case 1:
	    //			    m1.setIntegral( jifen - gc.getGcTotal() );
	    //			    flag1 = true;
	    //			    break;
	    //			case 2:
	    //			    m1.setFlow( flow - gc.getGcTotal() );
	    //			    flag1 = true;
	    //			    break;
	    //			case 3:
	    //			    fenbi1 = CommonUtil.toDouble( gc.getGcTotal() );
	    //			    m1.setFansCurrency( fenbi - gc.getGcTotal() );
	    //			    flag1 = true;
	    //			    break;
	    //			default:
	    //			    break;
	    //		    }
	    //		}
	    //		if ( flag1 ) {
	    //		    memberDAO.updateById( m1 );
	    //		}
	    //
	    //		MemberCard card = cardMapper.selectById( member.getMcId() );
	    //		// 储值卡付款
	    //		if ( userConsume.getPaymentType() == 5 ) {
	    //		    MemberCard c = new MemberCard();
	    //		    c.setMcId( member.getMcId() );
	    //		    c.setMoney( card.getMoney() + userConsume.getDiscountMoney() );
	    //		    cardMapper.updateById( c );
	    //		}
	    //
	    //		saveCardRecordNew( member.getMcId(), (byte) 1, uc.getDiscountMoney() + "", "线下消费撤单", member.getBusId(), null, 0, 0.0 );
	    //
	    //	    }
	    //	    if ( userConsume.getFenbi() != 0 ) {
	    //		// 归还商户粉币
	    //		returnfansCurrency( userConsume.getBusUserId(), uc.getFenbi() + fenbi1 );
	    //	    }
	    //	    if ( userConsume.getPaymentType() == 5 ) {
	    //		map.put( "result", 0 );
	    //		map.put( "message", "撤单成功" );
	    //		return map;
	    //	    }
	    //	    map.put( "result", 1 );
	    return map;
	} catch ( Exception e ) {
	    LOG.error( "撤单失败", e );
	    throw new Exception();
	}
    }

    @Transactional
    @Override
    public Member bingdingPhone( Integer memberId, String phone, String code, Integer busId ) throws BusinessException {
	Map< String,Object > map = new HashMap< String,Object >();
	try {
	    // 短信校验
	    if ( CommonUtil.isEmpty( memberId ) ) {
		throw new BusinessException( ResponseMemberEnums.NULL );
	    }

	    if ( CommonUtil.isEmpty( code ) ) {
		throw new BusinessException( ResponseMemberEnums.NULL );
	    }

	    if ( CommonUtil.isEmpty( phone ) ) {
		throw new BusinessException( ResponseMemberEnums.NULL );
	    }

	    if ( CommonUtil.isEmpty( busId ) ) {
		throw new BusinessException( ResponseMemberEnums.NULL );
	    }
	    // 短信判断

	    if ( CommonUtil.isEmpty( redisCacheUtil.get( code ) ) ) {
		throw new BusinessException( ResponseMemberEnums.NO_PHONE_CODE );
	    }
	    // 查询要绑定的手机号码
	    Member oldMember = memberDAO.findByPhone( busId, phone );

	    redisCacheUtil.remove( code );

	    if ( CommonUtil.isEmpty( oldMember ) ) {
		// 新用户
		Member member = memberDAO.selectById( memberId );
		Member m = new Member();
		m.setId( member.getId() );
		m.setPhone( phone );
		memberDAO.updateById( m );
		member.setPhone( phone );
		return member;
	    } else {
		Member m1 = memberDAO.selectById( memberId );

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

		memberDAO.deleteById( m1.getId() );
		memberDAO.updateById( member );

		MemberOld old = (MemberOld) JSONObject.toBean( JSONObject.fromObject( m1 ), MemberOld.class );
		memberOldMapper.insert( old );
		// 修改小程序之前openId对应的memberId
		memberAppletOpenidMapper.updateMemberId( member.getId(), m1.getId() );

		member.setPhone( phone );
		return member;
	    }
	} catch ( BusinessException e ) {
	    throw new BusinessException( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    e.printStackTrace();
	    LOG.error( "小程序绑定手机号码异常", e );
	    throw new BusinessException( ResponseEnums.ERROR );
	}
    }

    @Override
    public List< Map< String,Object > > findMemberCardRecharge( Integer busId, String cardNo ) {
	MemberCard card = cardMapper.findCardByCardNo( busId, cardNo );
	if ( CommonUtil.isEmpty( card ) ) {
	    return null;
	}
	List< Map< String,Object > > recharges = null;
	// 判断会员日
	MemberDate memberDate = memberCommonService.findMemeberDate( busId, card.getCtId() );
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
		member = memberDAO.findByMcIdAndbusId( busId, card.getMcId() );
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
		findGiveRule( orderCode, "充值", (byte) 1 );
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
	List< Map< String,Object > > listMap = memberDAO.findCardNo( cardNo );
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
	List< Map< String,Object > > dict = dictService.getDict( "1058" );
	Double ratio = CommonUtil.toDouble( dict.get( 0 ).get( "1" ) );
	map.put( "ratio", ratio );
	map.put( "startMoney", 10 );
	return map;
    }

    @Override
    public List< Integer > findMemberIds( Integer memberId ) {
	List< Integer > list = new ArrayList< Integer >();
	Member member = memberDAO.selectById( memberId );
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
	    Member member = memberDAO.selectById( memberId );
	    Member m = new Member();
	    m.setId( memberId );
	    m.setFlow( flow + member.getFlow() );
	    memberDAO.updateById( m );
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

	Member member = memberDAO.findByPhone( busId, phone );
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
	}

	List< Map< String,Object > > cards = memberDAO.findMemberBybusIdAndPhone( busId, card.getMcId() );

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

	    // TODO:短信判断
	   /* if (CommonUtil.isEmpty(JedisUtil.get(code))) {
		map.put("result", false);
                map.put("message", "短信校验码不正确");
                return map;
            }*/

	    // 分配会员卡
	    // 添加会员卡
	    MemberCard card = new MemberCard();
	    //            MemberCard card = new MemberCard();
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
	    Member oldMember = memberDAO.findByPhone( busId, phone );

	    Member member = null;

	    Member m1 = memberDAO.selectById( memberId );

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
		memberDAO.deleteById( m1.getId() );
		memberDAO.updateById( member );

		MemberOld old = (MemberOld) JSONObject.toBean( JSONObject.fromObject( m1 ), MemberOld.class );
		memberOldMapper.insert( old );
		// 修改小程序之前openId对应的memberId
		memberAppletOpenidMapper.updateMemberId( member.getId(), m1.getId() );

		map.put( "member", member );
	    } else {
		// 新用户
		member = memberDAO.selectById( memberId );
		Member m = new Member();
		m.setId( member.getId() );
		m.setPhone( phone );
		if ( CommonUtil.isEmpty( member.getMcId() ) ) {
		    cardMapper.insert( card );
		    m.setMcId( card.getMcId() );
		    member.setMcId( card.getMcId() );
		}
		memberDAO.updateById( m );
		member.setPhone( phone );
		map.put( "member", member );
	    }
	    //   JedisUtil.del(code);
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
	    Member member = memberDAO.selectById( memberId );
	    Integer mIntergral = member.getIntegral();
	    if ( mIntergral < -intergral ) {
		map.put( "result", 1 );
		map.put( "message", "积分不足" );
		return map;
	    }
	    Member member1 = new Member();
	    member1.setId( member.getId() );
	    member1.setIntegral( member.getIntegral() + intergral );
	    memberDAO.updateById( member1 );

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
	Member member = memberDAO.selectById( memberId );
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

    /**
     * 根据粉丝id获取优惠券信息
     *
     * @param memberId
     * @param shopId
     *
     * @return
     * @throws Exception
     */
    public Map< String,Object > findMemberCardByMemberId( Integer memberId, Integer shopId ) throws BusinessException {
	Map< String,Object > map = new HashMap< String,Object >();
	// 查询卡号是否存在
	try {
	    Member member = memberDAO.selectById( memberId );
	    MemberCard card = null;
	    // 查询卡号是否存在
	    if ( CommonUtil.isNotEmpty( member.getMcId() ) ) {
		card = cardMapper.selectById( member.getMcId() );
	    }

	    if ( CommonUtil.isEmpty( card ) ) {
		throw new BusinessException( ResponseMemberEnums.MEMBER_NOT_CARD.getCode(), ResponseMemberEnums.MEMBER_NOT_CARD.getMsg() );

	    } else if ( card.getCardStatus() == 1 ) {
		throw new BusinessException( ResponseMemberEnums.CARD_STATUS.getCode(), ResponseMemberEnums.CARD_STATUS.getMsg() );
	    } else {
		List< Map< String,Object > > cards = cardMapper.findCardById( card.getMcId() );
		MemberGiverule giveRule = giveRuleMapper.selectById( card.getGrId() );
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

		Double fenbiMoeny = memberCommonService.currencyCount( null, member.getFansCurrency() );
		map.put( "fenbiMoeny", fenbiMoeny );

		Double jifenMoeny = memberCommonService.integralCount( null, new Double( member.getIntegral() ), member.getBusId() );
		map.put( "jifenMoeny", jifenMoeny );

		WxShop wxShop = wxShopDAO.selectById( shopId );

		WxPublicUsers wxPublicUsers = wxPublicUsersMapper.selectByUserId( member.getBusId() );

		if ( CommonUtil.isNotEmpty( wxPublicUsers ) && CommonUtil.isNotEmpty( member.getOpenid() ) && wxShop.getStatus() == 2 ) {
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

			    if ( map2.get( "location_id_list" ).toString().contains( CommonUtil.toString( wxShop.getPoiId() ) ) ) {
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

	    MemberDate memberDate = memberCommonService.findMemeberDate( member.getBusId(), card.getCtId() );
	    if ( card.getCtId() == 2 ) {
		if ( CommonUtil.isNotEmpty( memberDate ) ) {
		    map.put( "memberDiscount", memberDate.getDiscount() );
		    map.put( "memberDate", true );
		}
	    }

	    return map;
	} catch ( BusinessException e ) {
	    throw new BusinessException( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    LOG.error( "ERP查询会员信息异常", e );
	    throw new BusinessException( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getDesc() );
	}
    }

    @Override
    public Map< String,Object > findMemberCard( Integer busId, String cardNoKey, String cardNo, Integer shopId ) throws BusinessException {
	Map< String,Object > map = new HashMap<>();
	String cardNodecrypt = "";
	try {
	    try {
		// 如果手动输入 会出现异常
		cardNodecrypt = EncryptUtil.decrypt( cardNoKey, cardNo );
	    } catch ( Exception e ) {
	    }

	    BusUser busUser = busUserMapper.selectById( busId );
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
			throw new BusinessException( ResponseMemberEnums.ERROR_QR_CODE.getCode(), ResponseMemberEnums.ERROR_QR_CODE.getMsg() );
		    }
		}
	    }

	    MemberCard card = null;

	    // 判断是否借给他人
	    MemberCardLent c = cardLentMapper.findByCode( cardNo );
	    if ( CommonUtil.isNotEmpty( c ) ) {
		// 判断时间是否在有效时间内
		// 5分钟后为超时
		if ( DateTimeKit.secondBetween( c.getCreateDate(), new Date() ) > 300 ) {
		    // 二维码已超时
		    throw new BusinessException( ResponseMemberEnums.ERROR_QR_CODE.getCode(), ResponseMemberEnums.ERROR_QR_CODE.getMsg() );

		}
		card = cardMapper.selectById( c.getMcId() );

		map.put( "jie", 1 );
		map.put( "lentMoney", c.getLentMoney() );
		map.put( "clId", c.getId() );
	    }

	    Member member = null;
	    // 查询卡号是否存在
	    if ( CommonUtil.isEmpty( card ) ) {
		card = cardMapper.findCardByCardNo( busId, cardNo );
		if ( CommonUtil.isNotEmpty( card ) ) {
		    member = memberDAO.findByMcIdAndbusId( busId, card.getMcId() );
		}

	    }

	    if ( CommonUtil.isEmpty( card ) ) {
		member = memberDAO.findByPhone( busId, cardNo );
		if ( CommonUtil.isNotEmpty( member ) ) {
		    card = cardMapper.selectById( member.getMcId() );
		}
	    }

	    if ( CommonUtil.isEmpty( card ) ) {
		throw new BusinessException( ResponseMemberEnums.NO_DATA.getCode(), ResponseMemberEnums.NO_DATA.getMsg() );
	    } else if ( card.getCardStatus() == 1 ) {
		throw new BusinessException( ResponseMemberEnums.CARD_STATUS.getCode(), ResponseMemberEnums.CARD_STATUS.getMsg() );
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

		Double fenbiMoeny = memberCommonService.currencyCount( null, member.getFansCurrency() );
		map.put( "fenbiMoeny", fenbiMoeny );

		Double jifenMoeny = memberCommonService.integralCount( null, new Double( member.getIntegral() ), busId );
		map.put( "jifenMoeny", jifenMoeny );

		WxShop wxShop = wxShopDAO.selectById( shopId );

		WxPublicUsers wxPublicUsers = wxPublicUsersMapper.selectByUserId( busId );

		if ( CommonUtil.isNotEmpty( wxPublicUsers ) && CommonUtil.isNotEmpty( member.getOpenid() ) && wxShop.getStatus() == 2 ) {
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

			    if ( map2.get( "location_id_list" ).toString().contains( CommonUtil.toString( wxShop.getPoiId() ) ) ) {
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

	    MemberDate memberDate = memberCommonService.findMemeberDate( busId, card.getCtId() );
	    if ( card.getCtId() == 2 ) {
		if ( CommonUtil.isNotEmpty( memberDate ) ) {
		    map.put( "memberDiscount", memberDate.getDiscount() );
		    map.put( "memberDate", true );
		}
	    }
	    return map;
	} catch ( BusinessException e ) {
	    throw new BusinessException( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    LOG.error( "ERP查询会员信息异常", e );
	    throw new BusinessException( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getDesc() );
	}
    }

    @Deprecated
    @Transactional
    @Override
    public void payMemberCard( String json ) throws BusinessException {
	try {
	    JSONObject jsonObject = JSONObject.fromObject( json );
	    Object isUsrYhj = jsonObject.get( "isUsrYhj" ); // 标示 是否使用卡券
	    if ( CommonUtil.isNotEmpty( isUsrYhj ) && CommonUtil.toInteger( isUsrYhj ) == 1 ) {
		String codes = jsonObject.getString( "codes" );
		Integer cardType = jsonObject.getInt( "cardType" );
		if ( cardType == 0 ) {
		    Integer publicId = jsonObject.getInt( "publicId" );
		    // 微信卡券
		    cardCouponsApiService.wxCardReceive( publicId, codes );
		} else {
		    Map< String,Object > params = new HashMap< String,Object >();
		    params.put( "storeId", jsonObject.get( "storeId" ) );
		    params.put( "codes", codes );
		    // 多粉卡券
		    verificationCard_2( params );
		}
	    }
	    Object isUseCard = jsonObject.get( "isUseCard" ); // 判断是否使用会员储值卡
	    if ( CommonUtil.isNotEmpty( isUseCard ) && CommonUtil.toInteger( isUseCard ) == 1 ) {
		Integer memberId = jsonObject.getInt( "memberId" );
		Double totalMoney = jsonObject.getDouble( "totalMoney" );
		storePay( memberId, totalMoney );
	    }
	    //
	    Object isUseJifen = jsonObject.get( "isUseJifen" );
	    if ( CommonUtil.isNotEmpty( isUseJifen ) && CommonUtil.toInteger( isUseJifen ) == 1 ) {
		Integer memberId = jsonObject.getInt( "memberId" );
		Double intergral = jsonObject.getDouble( "jifen" );
		updateMemberIntergral( memberId, -intergral.intValue() );
	    }

	    Object isUsefenbi = jsonObject.get( "isUsefenbi" );
	    if ( CommonUtil.isNotEmpty( isUsefenbi ) && CommonUtil.toInteger( isUsefenbi ) == 1 ) {
		Integer memberId = jsonObject.getInt( "memberId" );
		Double fenbi = jsonObject.getDouble( "fenbi" );
		Member member = memberDAO.selectById( memberId );
		reduceFansCurrency( member, member.getBusId(), fenbi );
	    }
	} catch ( Exception e ) {
	    LOG.error( "ERP调用payMemberCard接口异常", e );
	    throw new BusinessException( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getDesc() );
	}
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
		    member = memberDAO.findByMcIdAndbusId( busId, card.getMcId() );
		}

	    }

	    if ( CommonUtil.isEmpty( card ) ) {
		member = memberDAO.findByPhone( busId, cardNo );

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
	Member tuijianMember = memberDAO.selectById( recommend.getMemberId() );
	Member m1 = new Member();
	m1.setId( recommend.getMemberId() );
	if ( recommend.getIntegral() > 0 ) {
	    m1.setIntegral( tuijianMember.getIntegral() + recommend.getIntegral() );
	    //积分记录
	    memberCardService.saveCardRecordNew( tuijianMember.getMcId(), (byte) 2, recommend.getIntegral() + "积分", "推荐优惠券赠送", tuijianMember.getBusId(), null, 0,
			    Double.valueOf( recommend.getIntegral() ) );
	    flag = true;
	}
	if ( recommend.getFenbi() > 0 ) {

	    BusUser busUser = busUserMapper.selectById( tuijianMember.getBusId() );
	    if ( busUser.getFansCurrency().doubleValue() >= recommend.getFenbi() ) {

		// 新增粉笔和流量分配表
		FenbiFlowRecord fenbi = new FenbiFlowRecord();
		fenbi.setBusUserId( busUser.getId() );
		fenbi.setRecType( 1 );
		fenbi.setRecCount( new BigDecimal( recommend.getFenbi() ) );
		fenbi.setRecUseCount( new BigDecimal( recommend.getFenbi() ) );
		fenbi.setRecDesc( "推荐优惠券赠送粉币" );
		fenbi.setRecFreezeType( 102 );
		fenbi.setRollStatus( 2 );
		fenbi.setFlowType( 0 );
		fenbi.setFlowId( 0 );
		fenbiFlowRecordDAO.insert( fenbi );

		BusUser b = new BusUser();
		b.setId( busUser.getId() );
		Double fenbi1 = busUser.getFansCurrency().doubleValue() - recommend.getFenbi();
		b.setFansCurrency( BigDecimal.valueOf( fenbi1 ) );
		busUserMapper.updateById( b );

		m1.setFansCurrency( tuijianMember.getFansCurrency() + recommend.getFenbi() );
		//粉币记录
		memberCardService.saveCardRecordNew( tuijianMember.getMcId(), (byte) 3, recommend.getFenbi() + "粉币", "推荐优惠券赠送", tuijianMember.getBusId(), null, 0,
				Double.valueOf( recommend.getFenbi() ) );
		flag = true;
	    }
	}

	if ( recommend.getFlow() > 0 ) {
	    m1.setFlow( tuijianMember.getFlow() + recommend.getFlow() );
	    //流量记录
	    memberCardService.saveCardRecordNew( tuijianMember.getMcId(), (byte) 4, recommend.getFlow() + "MB", "推荐优惠券赠送", tuijianMember.getBusId(), null, 0,
			    Double.valueOf( recommend.getFlow() ) );
	    flag = true;
	}
	if ( flag ) {
	    memberDAO.updateById( m1 );
	}

	if ( recommend.getMoney() > 0 ) {
	    MemberCard card = cardMapper.selectById( tuijianMember.getMcId() );
	    MemberCard c = new MemberCard();
	    c.setMcId( card.getMcId() );
	    c.setGiveMoney( card.getGiveMoney() + recommend.getMoney() );
	    cardMapper.updateById( c );
	    //流量记录
	    memberCardService.saveCardRecordNew( tuijianMember.getMcId(), (byte) 1, recommend.getMoney() + "元", "推荐优惠券赠送", tuijianMember.getBusId(), null, 0,
			    Double.valueOf( recommend.getMoney() ) );
	}

	MemberRecommend r = new MemberRecommend();
	r.setId( recommend.getId() );
	r.setUserNum( recommend.getUserNum() + 1 );
	recommendMapper.updateById( r );
    }

    @Transactional( rollbackFor = Exception.class )
    @Override
    public void paySuccess( PaySuccessBo paySuccessBo ) throws BusinessException {
	try {
	    UserConsume uc = new UserConsume();
	    Member member = memberDAO.selectById( paySuccessBo.getMemberId() );
	    Double totalMoney = paySuccessBo.getPay();
	    //会员消费记录添加
	    uc.setBusUserId( member.getBusId() );
	    uc.setMemberId( member.getId() );
	    uc.setRecordType( 2 );
	    uc.setUcType( paySuccessBo.getUcType() );
	    uc.setTotalMoney( paySuccessBo.getTotalMoney() );
	    uc.setIntegral( paySuccessBo.getJifenNum() );
	    uc.setFenbi( paySuccessBo.getFenbiNum() );
	    uc.setDiscountMoney( paySuccessBo.getDiscountMoney() );
	    uc.setPaymentType( paySuccessBo.getPayType() );
	    uc.setCardType( paySuccessBo.getCouponType() );
	    uc.setDisCountdepict( paySuccessBo.getCodes() );
	    uc.setOrderCode( paySuccessBo.getOrderCode() );
	    uc.setStoreId( paySuccessBo.getStoreId() );
	    uc.setDataSource( paySuccessBo.getDataSource() );
	    uc.setIsend( 0 );

	    MemberCard card = null;
	    if ( CommonUtil.isNotEmpty( member.getMcId() ) ) {
		card = cardMapper.selectById( member.getMcId() );
		uc.setMcId( member.getMcId() );
		uc.setCtId( card.getCtId() );
		uc.setGtId( card.getGtId() );

		if ( paySuccessBo.getPayType() != 5 ) {
		    saveCardRecordNew( card.getMcId(), (byte) 1, "-" + totalMoney + "元", "消费", member.getBusId(), null, 0, -totalMoney );
		}
	    }
	    if ( paySuccessBo.getPayType() == 5 ) {
		//储值卡支付
		if ( CommonUtil.isEmpty( member.getMcId() ) ) {
		    throw new BusinessException( ResponseMemberEnums.NOT_MEMBER_CAR );
		}
		if ( CommonUtil.isNotEmpty( card ) ) {
		    if ( card.getCtId() == 3 ) {
			if ( card.getMoney() < totalMoney ) {
			    throw new BusinessException( ResponseMemberEnums.MEMBER_LESS_MONEY );
			}
			double banlan = card.getMoney() - totalMoney;
			card.setMoney( banlan );
			cardMapper.updateById( card );
			saveCardRecordNew( card.getMcId(), (byte) 1, "-" + totalMoney + "元", "消费", member.getBusId(), banlan + "元", 0, -totalMoney );
			systemMsgService.sendChuzhiXiaofei( member, totalMoney );

			uc.setBalance( banlan );
		    } else {
			throw new BusinessException( ResponseMemberEnums.MEMBER_CHUZHI_CARD );
		    }
		}
	    }

	    //优惠券
	    if ( paySuccessBo.isUseCoupon() ) {
		if ( paySuccessBo.getCouponType() == 0 ) {
		    //微信
		    Integer publicId = wxPublicUsersMapper.selectByUserId( member.getBusId() ).getId();
		    cardCouponsApiService.wxCardReceive( publicId, paySuccessBo.getCodes() );
		} else {
		    //多粉
		    Map< String,Object > duofenMap = new HashMap<>();
		    duofenMap.put( "codes", paySuccessBo.getCodes() );
		    duofenMap.put( "storeId", paySuccessBo.getStoreId() );
		    cardCouponsApiService.verificationCard_2( duofenMap );
		}
	    }

	    //粉币使用
	    if ( paySuccessBo.isUserFenbi() && CommonUtil.isNotEmpty( member.getMcId() ) ) {
		reduceFansCurrency( member, member.getBusId(), paySuccessBo.getFenbiNum() );
	    }
	    //积分使用
	    if ( paySuccessBo.isUserJifen() && CommonUtil.isNotEmpty( member.getMcId() ) ) {
		Member member1 = new Member();
		member1.setId( member.getId() );
		member1.setIntegral( member.getIntegral() - paySuccessBo.getJifenNum() );
		memberDAO.updateById( member1 );
		saveCardRecordNew( member.getMcId(), (byte) 2, paySuccessBo.getJifenNum() + "积分", "消费积分", member.getBusId(), "", null, paySuccessBo.getJifenNum() );
	    }
	    uc.setPayStatus( 1 );
	    userConsumeMapper.insert( uc );

	    if ( CommonUtil.isNotEmpty( member.getMcId() ) ) {
		//会员立即送 和 延迟送
		if ( paySuccessBo.getDelay() == 0 ) {
		    findGiveRuleDelay( paySuccessBo.getOrderCode() );  //延迟送
		} else if ( paySuccessBo.getDelay() == 1 ) {
		    findGiveRule( paySuccessBo.getOrderCode(), "消费会员赠送", (byte) 1 );
		}
	    }

	} catch ( BusinessException e ) {
	    throw new BusinessException( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    MemberLog m = new MemberLog();
	    m.setLogtxt( "方法paySuccess支付成功回调异常,请求参数" + JSONObject.fromObject( paySuccessBo ) );
	    memberLogDAO.insert( m );
	    LOG.error( "支付成功回调异常", e );
	    throw new BusinessException( ResponseEnums.ERROR );
	}

    }

    @Override
    public Map< String,Object > countMember( Integer busId ) {
	WxShop wxShop = wxShopDAO.selectMainShopByBusId( busId );

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
	    if ( wxShop.getId().equals( CommonUtil.toInteger( map.get( "shopId" ) ) ) ) {
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

    @Transactional
    public void refundMoney( Integer busId, String orderNo, double refundMoney ) throws BusinessException {
	try {
	    UserConsume uc = userConsumeMapper.findUserConsumeByBusIdAndOrderCode( busId, orderNo );
	    if ( CommonUtil.isEmpty( uc ) ) {
		throw new BusinessException( ResponseMemberEnums.NO_DATA.getCode(), ResponseMemberEnums.NO_DATA.getMsg() );
	    }
	    if ( uc.getPaymentType() == 5 ) {
		//储值卡付款
		Member member = memberDAO.selectById( uc.getMemberId() );
		MemberCard card = cardMapper.selectById( uc.getMcId() );
		MemberCard card1 = new MemberCard();
		// 储值卡
		card1.setMcId( uc.getMcId() );
		card1.setMoney( card.getMoney() + refundMoney );
		cardMapper.updateById( card1 );

		saveCardRecordNew( card.getMcId(), (byte) 1, refundMoney + "元", "储值卡退款", card.getBusId(), null, 0, 0 );
		systemMsgService.sendChuzhiTuikuan( member, refundMoney );
	    }
	    //添加退款金额
	    uc.setRefundMoney( uc.getRefundMoney() + refundMoney );
	    userConsumeMapper.updateById( uc );
	} catch ( BusinessException e ) {
	    throw new BusinessException( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    MemberLog m = new MemberLog();
	    m.setLogtxt( "方法refundMoney退款异常,请求参数商家：" + busId + "订单号:" + orderNo + "退款金额:" + refundMoney );
	    memberLogDAO.insert( m );
	    LOG.error( "退款异常", e );
	    throw new BusinessException( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getDesc() );
	}
    }

    @Override
    public void isAdequateMoney( Integer memberId, Double money ) throws BusinessException {
	try {
	    Member member = memberDAO.selectById( memberId );
	    if ( CommonUtil.isEmpty( member.getMcId() ) ) {
		throw new BusinessException( ResponseMemberEnums.NOT_MEMBER_CAR );
	    }
	    MemberCard card = cardMapper.selectById( member.getMcId() );
	    if ( CommonUtil.isNotEmpty( card ) ) {
		if ( card.getCtId() == 3 ) {
		    if ( card.getMoney() >= money ) {
			return;
		    }
		}
	    }
	    throw new BusinessException( ResponseMemberEnums.MEMBER_LESS_MONEY );
	} catch ( BusinessException e ) {
	    throw new BusinessException( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    throw new BusinessException( ResponseEnums.ERROR );
	}
    }

    @Override
    public List< Map< String,Object > > findMemberByIds( Integer busId, String memberIds ) throws BusinessException {
	try {
	    String[] str = memberIds.split( "," );
	    List< Integer > ids = new ArrayList<>();
	    for ( int i = 0; i < str.length; i++ ) {
		if ( CommonUtil.isNotEmpty( str[i] ) ) {
		    ids.add( CommonUtil.toInteger( str[i] ) );
		}
	    }
	    return memberDAO.findByMemberIds( busId, ids );
	} catch ( Exception e ) {
	    throw new BusinessException( ResponseEnums.ERROR );
	}
    }

    /**
     * 商场积分赠送
     *
     * @param memberId
     * @param jifen
     */
    @Override
    public void updateJifen( Integer memberId, Integer jifen ) throws BusinessException {
	Member member = memberDAO.selectById( memberId );
	Integer mIntergral = member.getIntegral();
	Member member1 = new Member();
	member1.setId( member.getId() );
	member1.setIntegral( member.getIntegral() + jifen );
	memberDAO.updateById( member1 );

	if ( CommonUtil.isNotEmpty( member.getMcId() ) ) {
	    saveCardRecordNew( member.getMcId(), (byte) 2, jifen + "积分", "积分", member.getBusId(), "", null, jifen );
	}

    }

    @Override
    public List< Map< String,Object > > findBuyGradeType( Integer busId ) {
	return memberGradetypeDAO.findBuyGradeType( busId );
    }

    /**
     * 商场修改订单状态
     *
     * @param orderNo
     * @param payType
     * @param payStatus
     */
    public void updateUserConsume( String orderNo, Integer payType, Integer payStatus ) throws BusinessException {
	UserConsume userConsume = userConsumeMapper.findByOrderCode1( orderNo );
	if ( CommonUtil.isEmpty( userConsume ) ) {
	    throw new BusinessException( ResponseMemberEnums.NOT_ORDER_DATA );
	}
	// 修改订单状态
	UserConsume uc = new UserConsume();
	uc.setId( userConsume.getId() );
	uc.setPayStatus( 1 );
	uc.setPaymentType( payType );
	userConsumeMapper.updateById( uc );
    }

    @Transactional
    public void refundMoneyAndJifenAndFenbi( Map< String,Object > map ) throws BusinessException {
	try {
	    Integer busId = CommonUtil.toInteger( map.get( "busId" ) );
	    String orderNo = CommonUtil.toString( map.get( "orderNo" ) );
	    Double refundMoney = CommonUtil.toDouble( map.get( "money" ) );
	    Double fenbi = CommonUtil.toDouble( map.get( "fenbi" ) );
	    Integer jifen = CommonUtil.toInteger( map.get( "jifen" ) );

	    UserConsume uc = userConsumeMapper.findUserConsumeByBusIdAndOrderCode( busId, orderNo );
	    if ( CommonUtil.isEmpty( uc ) ) {
		throw new BusinessException( ResponseMemberEnums.NO_DATA.getCode(), ResponseMemberEnums.NO_DATA.getMsg() );
	    }
	    Member member = memberDAO.selectById( uc.getMemberId() );

	    //储值卡付款
	    MemberCard card = cardMapper.selectById( uc.getMcId() );
	    if ( uc.getPaymentType() == 5 ) {
		MemberCard card1 = new MemberCard();
		// 储值卡
		card1.setMcId( uc.getMcId() );
		card1.setMoney( card.getMoney() + refundMoney );
		cardMapper.updateById( card1 );

		saveCardRecordNew( card.getMcId(), (byte) 1, refundMoney + "元", "储值卡退款", card.getBusId(), null, 0, 0 );
		systemMsgService.sendChuzhiTuikuan( member, refundMoney );
	    }
	    //添加退款金额
	    uc.setRefundMoney( uc.getRefundMoney() + refundMoney );
	    userConsumeMapper.updateById( uc );

	    Boolean flag = false;
	    Member m1 = new Member();
	    m1.setId( member.getId() );
	    if ( jifen > 0 ) {
		//退积分
		m1.setIntegral( member.getIntegral() + jifen );
		saveCardRecordNew( card.getMcId(), (byte) 2, jifen + "积分", "订单退款", busId, null, 0, jifen );
		flag = true;
	    }
	    if ( fenbi > 0.0 ) {
		m1.setFansCurrency( member.getFansCurrency() + fenbi );
		saveCardRecordNew( card.getMcId(), (byte) 3, fenbi + "粉币", "订单退款", busId, null, 0, 0 );
		flag = true;
	    }
	    if ( flag ) {
		memberDAO.updateById( m1 );
	    }
	} catch ( BusinessException e ) {
	    throw new BusinessException( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    MemberLog m = new MemberLog();
	    m.setLogtxt( "方法refundMoneyAndJifenAndFenbi退款异常,请求参数商家：" + map );
	    memberLogDAO.insert( m );
	    LOG.error( "退款异常", e );
	    throw new BusinessException( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getDesc() );
	}
    }

    public List< Map< String,Object > > findCardrecord( Integer mcId, Integer page, Integer pageSize ) {
	return memberCardrecordDAO.findCardrecordByMcId( mcId, page * pageSize, pageSize );
    }

    public MemberCard findMemberCardByMcId( Integer mcId ) {
	return memberCardDAO.selectById( mcId );
    }

    public Map< String,Object > findMemberCardByMemberIdAndshopIds( Integer memberId, String shopIds ) throws BusinessException {
	Map< String,Object > map = new HashMap< String,Object >();
	// 查询卡号是否存在
	try {
	    Member member = memberDAO.selectById( memberId );
	    MemberCard card = null;
	    // 查询卡号是否存在
	    if ( CommonUtil.isNotEmpty( member.getMcId() ) ) {
		card = cardMapper.selectById( member.getMcId() );
	    }

	    if ( CommonUtil.isEmpty( card ) ) {
		throw new BusinessException( ResponseMemberEnums.MEMBER_NOT_CARD.getCode(), ResponseMemberEnums.MEMBER_NOT_CARD.getMsg() );

	    } else if ( card.getCardStatus() == 1 ) {
		throw new BusinessException( ResponseMemberEnums.CARD_STATUS.getCode(), ResponseMemberEnums.CARD_STATUS.getMsg() );
	    } else {
		List< Map< String,Object > > cards = cardMapper.findCardById( card.getMcId() );
		MemberGiverule giveRule = giveRuleMapper.selectById( card.getGrId() );
		map.put( "nickName", member.getNickname() );
		map.put( "phone", member.getPhone() );
		map.put( "ctName", cards.get( 0 ).get( "ct_name" ) );
		map.put( "gradeName", cards.get( 0 ).get( "gt_grade_name" ) );
		map.put( "cardNo", card.getCardNo() );
		map.put( "ctId", card.getCtId() );
		map.put( "discount", giveRule.getGrDiscount() / 10.0 );
		map.put( "money", card.getMoney() );
		map.put( "frequency", card.getFrequency() );
		map.put( "fans_currency", member.getFansCurrency() );
		map.put( "integral", member.getIntegral() );
		map.put( "memberId", member.getId() );
		map.put( "cardId", card.getMcId() );

		Double fenbiMoeny = memberCommonService.currencyCount( null, member.getFansCurrency() );
		map.put( "fenbiMoeny", fenbiMoeny );

		Double jifenMoeny = memberCommonService.integralCount( null, new Double( member.getIntegral() ), member.getBusId() );
		map.put( "jifenMoeny", jifenMoeny );

		WxPublicUsers wxPublicUsers = wxPublicUsersMapper.selectByUserId( member.getBusId() );

		String[] str = shopIds.split( "," );
		for ( int i = 0; i < str.length; i++ ) {
		    if ( CommonUtil.isEmpty( str[i] ) ) continue;
		    Integer shopId = CommonUtil.toInteger( str[i] );
		    WxShop wxShop = wxShopDAO.selectById( shopId );
		    if ( CommonUtil.isNotEmpty( wxPublicUsers ) && CommonUtil.isNotEmpty( member.getOpenid() ) && wxShop.getStatus() == 2 ) {
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

				if ( map2.get( "location_id_list" ).toString().contains( CommonUtil.toString( wxShop.getPoiId() ) ) ) {
				    list.add( map2 );
				}
			    }
			}
			if ( list.size() > 0 ) {
			    map.put( "code", 1 );
			    map.put( "cardList" + shopId, JSONArray.fromObject( list ) );
			}
		    }
		}
	    }

	    String[] str = shopIds.split( "," );
	    for ( int i = 0; i < str.length; i++ ) {
		if ( CommonUtil.isEmpty( str[i] ) ) continue;
		Integer shopId = CommonUtil.toInteger( str[i] );
		// 查询能使用的多粉优惠券
		List< Map< String,Object > > duofenCards = findDuofenCardByMemberId( member.getId(), shopId );
		map.put( "duofenCards" + shopId, duofenCards );
	    }

	    MemberDate memberDate = memberCommonService.findMemeberDate( member.getBusId(), card.getCtId() );
	    if ( card.getCtId() == 2 ) {
		if ( CommonUtil.isNotEmpty( memberDate ) ) {
		    map.put( "memberDiscount", memberDate.getDiscount() );
		    map.put( "memberDate", true );
		}
	    }

	    return map;
	} catch ( BusinessException e ) {
	    throw new BusinessException( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    LOG.error( "ERP查询会员信息异常", e );
	    throw new BusinessException( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getDesc() );
	}
    }

    public Map< String,Object > updateMemberByTeach( Map< String,Object > map ) throws BusinessException {
        try {
            Map<String,Object> returnMap=new HashMap<>(  );
	    Integer parentId = CommonUtil.toInteger( map.get( "parentId" ) );
	    Integer wxMemberId = CommonUtil.toInteger( map.get( "wxMemberId" ) );

	    Member m1 = memberDAO.selectById( parentId );  //erp粉丝信息
	    Member wxmember = memberDAO.selectById( wxMemberId );  //微信授权粉丝

	    Member member = new Member();
	    member.setFlow( m1.getFlow() + wxmember.getFlow() );
	    member.setIntegral( m1.getIntegral() + wxmember.getIntegral() );
	    member.setFansCurrency( m1.getFansCurrency() + wxmember.getFansCurrency() );
	    member.setId( m1.getId() );

	    if ( CommonUtil.isNotEmpty( m1.getOldId() ) && !m1.getOldId().contains( wxmember.getId().toString() ) ) {
		member.setOldId( m1.getId() + "," + wxmember.getId() );
	    } else {
		if ( CommonUtil.isNotEmpty( wxmember.getOldId() ) ) {
		    member.setOldId( wxmember.getOldId() + "," + m1.getId() );
		} else {
		    member.setOldId( m1.getId() + "," + wxmember.getId() );
		}
	    }
	    if ( CommonUtil.isEmpty( m1.getOpenid() ) ) {
		member.setOpenid( wxmember.getOpenid() );
	    }

	    if ( CommonUtil.isEmpty( m1.getHeadimgurl() ) ) {
		member.setHeadimgurl( wxmember.getHeadimgurl() );
	    }

	    memberDAO.deleteById( wxmember.getId() );
	    memberDAO.updateById( member );

	    MemberOld old = (MemberOld) JSONObject.toBean( JSONObject.fromObject( wxmember ), MemberOld.class );
	    memberOldMapper.insert( old );
	    // 修改小程序之前openId对应的memberId
	    memberAppletOpenidMapper.updateMemberId( member.getId(), wxmember.getId() );

	    map.put("member", member);
	    return map;
	}catch ( Exception e ){
            throw new BusinessException( ResponseEnums.ERROR );
	}
    }

}
