package com.gt.member.service.core.ws;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.jws.WebService;

import com.gt.member.dao.*;
import com.gt.member.entity.*;
import com.gt.member.service.old.common.dict.DictService;
import com.gt.member.service.old.member.MemberCardService;
import com.gt.member.util.*;
import com.gt.member.util.token.TokenUitl;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 多粉和微信卡券ws
 *
 * @author pengjiangli
 */
@WebService( serviceName = "cardCouponsWS" )
@Component
@Service
public class CardCouponsWSServiceImpl implements CardCouponsWSService {

    private static final Logger LOG = Logger.getLogger( CardCouponsWSServiceImpl.class );

    @Autowired
    private MemberConfig memberConfig;

    //获取门店信息
    private final String GET_SHOP_URL = "/basics/79B4DE7C/findShopById.do";
    //微信卡券核销
    private final String CODE_CONSUME =  "/basics/79B4DE7C/codeConsume.do";
    //发送短信
    private final String SEND_SMS     = "/basics/79B4DE7C/sendSMS.do";

    //保存粉币资产记录
    private final String saveFenbiFlowRecord = "/basics/79B4DE7C/saveFenbiFlowRecord.do";


    @Autowired
    private WxCardDAO wxCardMapper;

    @Autowired
    private WxCardReceiveDAO wxCardReceiveMapper;

    @Autowired
    private DuofenCardDAO duofenCardMapper;

    @Autowired
    private DuofenCardReceiveDAO duofenCardReceiveMapper;

    @Autowired
    private DuofenCardGetDAO duofenCardGetMapper;

    @Autowired
    private WxPublicUsersDAO wxPublicUsersMapper;

    @Autowired
    private DuofenCardReceivelogDAO duofenCardReceiveLogMapper;

    @Autowired
    private MemberDAO memberMapper;

    @Autowired
    private MemberCardService memberCardService;

    @Autowired
    private MemberCardDAO cardMapper;

    @Autowired
    private MemberGiveruleDAO giveRuleMapper;

    @Autowired
    private DictService dictService;

    @Autowired
    private UserConsumeDAO userConsumeMapper;

    @Autowired
    private MemberCardrecordDAO cardRecordMapper;

    @Autowired
    private BusUserDAO busUserMapper;

    //	@Autowired
    //	private BusUserBranchRelationMapper busUserBranchRelationMapper;

    //	@Autowired
    //	private CouponMallPutlistService couponMallPutlistService;
    //
    //	@Autowired
    //	private CouponMallOrderService couponMallOrderService;
    //
    //	@Autowired
    //	private IStoreService storeService;

    @Autowired
    private MemberRecommendDAO recommendMapper;

    //<!-----------卡券对外接口Start------------>
    @Override
    public List< Map< String,Object > > findWxCardByShopId( Integer shopId, Integer wxPublicUsersId, Member member ) throws Exception {
	if ( CommonUtil.isEmpty( wxPublicUsersId ) ) {
	    return null;
	}
	JSONObject json = new JSONObject();
	json.put( "token", TokenUitl.getToken() );
	json.put( "shopId", shopId );

	JSONObject returnJSON = HttpClienUtil.httpPost( memberConfig.getWxmp_home()+GET_SHOP_URL, json, false );
	Map< String,Object > map = (Map< String,Object >) returnJSON.get( "wxshop" );

	if ( CommonUtil.isEmpty( map ) || CommonUtil.isEmpty( map.get( "poiid" ) ) ) {
	    return null;
	}

	if ( CommonUtil.isEmpty( member.getOpenid() ) ) {
	    return null;
	}

	//查询优惠券信息
	List< Map< String,Object > > cardList = wxCardReceiveMapper.findByOpenId1( wxPublicUsersId, member.getOpenid() );
	List< Map< String,Object > > list = new ArrayList< Map< String,Object > >();
	if ( CommonUtil.isNotEmpty( cardList ) && cardList.size() > 0 ) {
	    for ( Map< String,Object > map2 : cardList ) {
		if ( "GIFT".equals( map2.get( "card_type" ).toString() ) || "GENERAL_COUPON".equals( map2.get( "card_type" ).toString() ) ) {
		    continue;
		}
		//时间判断
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
		    if ( !DateTimeKit.laterThanNow( DateTimeKit
				    .addDays( DateTimeKit.parse( map2.get( "ctime" ).toString(), "yyyy-MM-dd hh:mm:ss" ), CommonUtil.toInteger( map2.get( "fixed_term" ) ) ) ) ) {
			continue;
		    }
		}

		String day = DateTimeKit.getDayToEnglish();
		if ( !map2.get( "time_limit" ).toString().contains( day ) ) {
		    continue;
		}
		if ( map2.get( "location_id_list" ).toString().contains( CommonUtil.toString( map.get( "poiid" ) ) ) ) {
		    list.add( map2 );
		}
	    }
	}
	return list;
    }

    @Override
    public List< Map< String,Object > > findWxCardByShopIdAndMoney( Integer shopId, Integer wxPublicUsersId, Member member, Double money ) throws Exception {
	if ( CommonUtil.isEmpty( wxPublicUsersId ) ) {
	    return null;
	}
	JSONObject json = new JSONObject();
	json.put( "token", TokenUitl.getToken() );
	json.put( "shopId", shopId );
	JSONObject returnJSON = HttpClienUtil.httpPost( memberConfig.getWxmp_home()+GET_SHOP_URL, json, false );
	Map< String,Object > map = (Map< String,Object >) returnJSON.get( "wxshop" );
	if ( CommonUtil.isEmpty( map ) || CommonUtil.isEmpty( map.get( "poiid" ) ) ) {
	    return null;
	}

	if ( CommonUtil.isEmpty( member.getOpenid() ) ) {
	    return null;
	}

	//查询优惠券信息
	List< Map< String,Object > > cardList = wxCardReceiveMapper.findByOpenId1( wxPublicUsersId, member.getOpenid() );
	List< Map< String,Object > > list = new ArrayList< Map< String,Object > >();
	if ( CommonUtil.isNotEmpty( cardList ) && cardList.size() > 0 ) {
	    for ( Map< String,Object > map2 : cardList ) {
		if ( "GIFT".equals( map2.get( "card_type" ).toString() ) || "GENERAL_COUPON".equals( map2.get( "card_type" ).toString() ) ) {
		    continue;
		}
		//时间判断
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
		    if ( !DateTimeKit.laterThanNow( DateTimeKit
				    .addDays( DateTimeKit.parse( map2.get( "ctime" ).toString(), "yyyy-MM-dd hh:mm:ss" ), CommonUtil.toInteger( map2.get( "fixed_term" ) ) ) ) ) {
			continue;
		    }
		}

		String day = DateTimeKit.getDayToEnglish();
		if ( !map2.get( "time_limit" ).toString().contains( day ) ) {
		    continue;
		}

		if ( "CASH".equals( map2.get( "card_type" ).toString() ) ) {
		    Double cash_least_cost = CommonUtil.toDouble( map2.get( "cash_least_cost" ) );
		    if ( cash_least_cost > 0 && cash_least_cost > money ) {
			continue;
		    }
		}

		if ( map2.get( "location_id_list" ).toString().contains( CommonUtil.toString( map.get( "poiid" ) ) ) ) {
		    list.add( map2 );
		}
	    }
	}
	return list;
    }

    @Override
    public Map< String,Object > wxCardReceive( Integer wxPublicUsersId, String code ) {
	Map< String,Object > map = new HashMap< String,Object >();
	try {
	    if ( CommonUtil.isEmpty( wxPublicUsersId ) ) {
		map.put( "result", -1 );
		map.put( "message", "核销失败" );
		return map;
	    }
	    WxCardReceive wcr = wxCardReceiveMapper.findByCode1( wxPublicUsersId, code );
	    if ( CommonUtil.isEmpty( wcr ) ) {
		map.put( "result", -1 );
		map.put( "message", "核销失败" );
		return map;
	    }
	    map.put( "card_id", wcr.getCardId() );
	    map.put( "code", code );
	    map.put( "token", TokenUitl.getToken() );
	    map.put( "wxPublicUsersId", wxPublicUsersId );

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

    @Override
    @Transactional( rollbackFor = Exception.class )
    public Map< String,Object > wxCardReceiveBackName( Integer wxPublicUsersId, String code ) {
	Map< String,Object > map = new HashMap< String,Object >();
	try {
	    if ( CommonUtil.isEmpty( wxPublicUsersId ) ) {
		map.put( "result", -1 );
		map.put( "message", "核销失败" );
		return map;
	    }
	    WxCardReceive wcr = wxCardReceiveMapper.findByCode1( wxPublicUsersId, code );
	    if ( CommonUtil.isEmpty( wcr ) ) {
		map.put( "result", -1 );
		map.put( "message", "核销失败" );
		return map;
	    }
	    map.put( "card_id", wcr.getCardId() );
	    map.put( "code", code );
	    map.put( "token", TokenUitl.getToken() );
	    map.put( "wxPublicUsersId", wxPublicUsersId );

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

	    WxCard wxcard = wxCardMapper.selectByCardId( wcr.getCardId() );

	    map.put( "cardId", wxcard.getId() );
	    map.put( "cardName", wxcard.getTitle() );
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

    @Override
    public List< Map< String,Object > > findWxCard( Integer publicId ) {
	List< Map< String,Object > > wxCards = wxCardMapper.findWxCard( publicId, new Date() );
	return wxCards;
    }

    @Override
    public WxCard findWxCardById( Integer id ) {
	return wxCardMapper.selectById( id );
    }

    //<!-----------卡券对外接口END-------------->

    // <!------------------------对外接口------------------------>

    /**
     * 查询用户拥有的优惠券
     */
    @Override
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

    /**
     * 查询用户拥有的优惠券
     */
    @Override
    public List< Map< String,Object > > findDuofenCardByMemberIdAndMoney( Integer memberId, Integer wxshopId, Double money ) {
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

	    if ( "1".equals( map.get( "card_type" ).toString() ) ) {
		Double cash_least_cost = CommonUtil.toDouble( map.get( "cash_least_cost" ) );
		Integer countId = CommonUtil.toInteger( map.get( "countId" ) );
		if ( cash_least_cost > 0 && cash_least_cost > money ) {
		    continue;
		}

		Integer addUser = CommonUtil.toInteger( map.get( "addUser" ) );

		if ( addUser == 1 || "1".equals( addUser ) ) {
		    if ( cash_least_cost > 0 ) {
			Double count = money / cash_least_cost;
			int num = count.intValue();
			if ( countId > num ) {
			    map.put( "countId", num );
			}
		    }
		} else {
		    map.put( "countId", 1 );
		}

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

    @Override
    public List< DuofenCardReceive > findReceiveBybusId( Integer busId ) {
	List< DuofenCardReceive > receives = duofenCardReceiveMapper.findCardReceiveBybusId( busId );
	return receives;
    }

    /**
     * 根据卡包查询卡券信息 取值 看数据库文档名称
     */
    @Override
    public Map< String,Object > findCardByReceiveId( Integer receiveId ) {
	DuofenCardReceive receives = duofenCardReceiveMapper.selectById( receiveId );
	String[] strId = receives.getCardIds().split( "," );
	List< Integer > ids = new ArrayList< Integer >();
	for ( String str : strId ) {
	    if ( CommonUtil.isNotEmpty( str ) ) {
		ids.add( CommonUtil.toInteger( str ) );
	    }
	}
	if ( ids.size() > 0 ) {
	    List< DuofenCard > duofencards = duofenCardMapper.findInCardIds( ids );
	    Map< String,Object > map = new HashMap< String,Object >();
	    map.put( "receives", receives );
	    map.put( "duofencards", duofencards );
	    return map;
	}

	return null;
    }

    /**
     * 查询第三方平台下所有优惠券
     */
    @Override
    public Map< String,Object > findByThreeMemberId( Integer threeMemberId, Integer page ) {
	if ( CommonUtil.isEmpty( page ) ) {
	    page = 0;
	}
	Integer firstResult = 10 * page;
	Integer pageSize = 10;

	List< Map< String,Object > > receiveLogs = duofenCardReceiveLogMapper.findByThreeMemberId( threeMemberId, firstResult, pageSize );
	// 未查询到数据
	if ( CommonUtil.isEmpty( receiveLogs ) || receiveLogs.size() <= 0 ) {
	    return null;
	}
	// 卡包集合
	List< Integer > receiveIds = new ArrayList< Integer >();
	for ( Map< String,Object > map : receiveLogs ) {
	    if ( CommonUtil.isNotEmpty( map.get( "crId" ) ) ) {
		receiveIds.add( CommonUtil.toInteger( map.get( "crId" ) ) );
	    }
	}
	if ( receiveIds.size() == 0 ) {
	    return null;
	}
	// 查询卡包下卡券信息
	List< Map< String,Object > > receives = duofenCardReceiveMapper.findInIds( receiveIds );

	List< Integer > cardIdList = new ArrayList< Integer >();

	for ( Map< String,Object > map : receives ) {
	    String[] cardIds = CommonUtil.toString( map.get( "cardIds" ) ).split( "," );
	    for ( String str : cardIds ) {
		if ( CommonUtil.isNotEmpty( str ) ) {
		    cardIdList.add( CommonUtil.toInteger( str ) );
		}
	    }
	}
	// 卡券没有 返回null
	if ( cardIdList.size() == 0 ) {
	    return null;
	}

	List< DuofenCard > duofencards = duofenCardMapper.findInCardIds( cardIdList );

	Map< String,Object > map = new HashMap<>();
	map.put( "receives", receives );
	map.put( "duofencards", duofencards );
	return map;
    }

    /**
     * 根据商家 查询商家拥有的卡包信息
     */
    @Override
    public List< Map< String,Object > > findReceiveByBusUserId( BusUser busUser ) {
	Integer pid = 0;
	if ( busUser.getPid() == 0 ) {
	    pid = busUser.getId();
	}
	List< Map< String,Object > > receives = duofenCardReceiveMapper.findByBusUserId( pid, new Date() );

	List< Map< String,Object > > returnList = new ArrayList< Map< String,Object > >();
	for ( Map< String,Object > map : receives ) {
	    if ( "1".equals( CommonUtil.toString( map.get( "cardType" ) ) ) ) {
		// 如果是礼券
		DuofenCard duofenCard = duofenCardMapper.selectById( CommonUtil.toInteger( map.get( "lqId" ) ) );
		List< Map< String,Object > > moneys = JsonUtil.json2List( duofenCard.getDateTimeSet() );
		double money = CommonUtil.toDouble( moneys.get( 0 ).get( "money" ) );
		for ( Map< String,Object > map2 : moneys ) {
		    if ( money > CommonUtil.toDouble( map2.get( "money" ) ) ) {
			money = CommonUtil.toDouble( map2.get( "money" ) );
		    }
		}
		map.put( "buyMoney", money );
	    }
	    returnList.add( map );
	}
	return returnList;
    }

    /**
     * 根据商家 查询商家拥有的卡包信息(新版本)
     */
    @Override
    public List< Map< String,Object > > findReceiveByBusUserId_1( BusUser busUser, Integer receiveId ) {
	Integer pid = 0;
	if ( busUser.getPid() == 0 ) {
	    pid = busUser.getId();
	}
	List< Map< String,Object > > receives = duofenCardReceiveMapper.findByBusUserIdAndTime( pid, new Date() );

	List< Map< String,Object > > returnList = new ArrayList< Map< String,Object > >();
	for ( Map< String,Object > map : receives ) {
	    if ( "1".equals( CommonUtil.toString( map.get( "cardType" ) ) ) ) {
		// 如果是礼券
		DuofenCard duofenCard = duofenCardMapper.selectById( CommonUtil.toInteger( map.get( "lqId" ) ) );
		List< Map< String,Object > > moneys = JsonUtil.json2List( duofenCard.getDateTimeSet() );
		double money = CommonUtil.toDouble( moneys.get( 0 ).get( "money" ) );
		for ( Map< String,Object > map2 : moneys ) {
		    if ( money > CommonUtil.toDouble( map2.get( "money" ) ) ) {
			money = CommonUtil.toDouble( map2.get( "money" ) );
		    }
		}
		map.put( "buyMoney", money );
	    }
	    returnList.add( map );
	}

	if ( CommonUtil.isNotEmpty( receiveId ) ) {
	    List< Map< String,Object > > receiveList = duofenCardReceiveMapper.findCardReceiveById( receiveId );
	    for ( Map< String,Object > map : receiveList ) {
		if ( "1".equals( CommonUtil.toString( map.get( "cardType" ) ) ) ) {
		    // 如果是礼券
		    DuofenCard duofenCard = duofenCardMapper.selectById( CommonUtil.toInteger( map.get( "lqId" ) ) );
		    List< Map< String,Object > > moneys = JsonUtil.json2List( duofenCard.getDateTimeSet() );
		    double money = CommonUtil.toDouble( moneys.get( 0 ).get( "money" ) );
		    for ( Map< String,Object > map2 : moneys ) {
			if ( money > CommonUtil.toDouble( map2.get( "money" ) ) ) {
			    money = CommonUtil.toDouble( map2.get( "money" ) );
			}
		    }
		    map.put( "buyMoney", money );
		}
		returnList.add( map );
	    }
	}
	return returnList;
    }

    /**
     * 商场支付成功回调 分配卡券
     */
    @Override
    public Map< String,Object > successPayBack( Integer receiveId, Integer num, Integer memberId ) {
	Map< String,Object > map = new HashMap< String,Object >();

	try {
	    DuofenCardReceive receives = duofenCardReceiveMapper.selectById( receiveId );
	    String[] strId = receives.getCardIds().split( "," );
	    List< Integer > ids = new ArrayList<>();
	    for ( String str : strId ) {
		if ( CommonUtil.isNotEmpty( str ) ) {
		    ids.add( CommonUtil.toInteger( str ) );
		}
	    }

	    List< Map< String,Object > > cardMlist = JsonUtil.json2List( receives.getCardMessage() );

	    Member member = memberMapper.selectByKey( memberId );
	    if ( ids.size() > 0 ) {
		List< DuofenCardGet > list = new ArrayList< DuofenCardGet >();
		List< DuofenCard > duofencards = duofenCardMapper.findInCardIds( ids );
		for ( int i = 0; i < num; i++ ) { // 购买数量
		    for ( DuofenCard duofenCard : duofencards ) {
			for ( Map< String,Object > card : cardMlist ) {
			    if ( duofenCard.getId() == CommonUtil.toInteger( card.get( "cardId" ) ) ) {
				int number = CommonUtil.toInteger( card.get( "num" ) );
				for ( int j = 0; j < number; j++ ) {
				    DuofenCardGet dfg = new DuofenCardGet();
				    dfg.setCardId( duofenCard.getId() );
				    dfg.setGetType( 0 );
				    dfg.setState( 0 );
				    dfg.setCode( getCode( 12 ) );
				    dfg.setGetDate( new Date() );
				    dfg.setCardReceiveId( receiveId );
				    dfg.setMemberId( memberId );
				    dfg.setPublicId( member.getPublicId() );
				    dfg.setFriendMemberId( "" );
				    dfg.setBusId( member.getBusId() );
				    if ( "DATE_TYPE_FIX_TIME_RANGE".equals( duofenCard.getType() ) ) {
					dfg.setStartTime( duofenCard.getBeginTimestamp() );
					dfg.setEndTime( duofenCard.getEndTimestamp() );
				    } else {
					dfg.setStartTime( DateTimeKit.addDate( new Date(), duofenCard.getFixedBeginTerm() ) );
					dfg.setEndTime( DateTimeKit.addDate( new Date(), duofenCard.getFixedTerm() ) );
				    }
				    dfg.setBusId( member.getBusId() );
				    list.add( dfg );
				}
			    }
			}
		    }
		}
		duofenCardGetMapper.insertList( list );

		// 短信通知
		if ( receives.getIsCallSms() == 1 ) {
		    Map< String,Object > params = new HashMap< String,Object >();
		    params.put( "busId", member.getBusId() );
		    params.put( "model", 12 );
		    params.put( "phone", receives.getMobilePhone() );
		    params.put( "content", "用户购买了" + num + "个" + receives.getCardIds() + "包,包中有：" + receives.getCardsName() + "优惠券" );
		    try {
			HttpClienUtil.httpPost( memberConfig.getWxmp_home()+SEND_SMS, JSONObject.fromObject( params ), true );
		    } catch ( Exception e ) {
			LOG.error( "短信发送失败", e );
		    }
		}
	    }
	    map.put( "result", true );
	    map.put( "message", "发放成功" );
	} catch ( Exception e ) {
	    e.printStackTrace();
	    map.put( "result", false );
	    map.put( "message", "发放失败" );
	    LOG.error( "用户id:" + memberId + "购买了" + num + "个卡券包id:" + receiveId, e );
	}
	return map;
    }

    /**
     * 自定义长度校验码
     *
     * @return
     */
    public static String getCode( int length ) {
	StringBuffer buf = new StringBuffer( "1,2,3,4,5,6,7,8,9,0" );
	String[] arr = buf.toString().split( "," );
	StringBuffer sb = new StringBuffer();
	Random random = new Random();
	for ( int i = 0; i < 4; i++ ) {
	    Integer count = arr.length;
	    int a = random.nextInt( count );
	    sb.append( arr[a] );
	}
	return "" + new Date().getTime() + sb.toString();
    }

    /**
     * 根据卡包查询卡券信息 取值 看数据库文档名称 map中key guoqi=1标示该包或该券过期
     */
    @Override
    public Map< String,Object > findDuofenCardByReceiveId( Integer receiveId ) {
	DuofenCardReceive receives = duofenCardReceiveMapper.selectById( receiveId );
	String[] strId = receives.getCardIds().split( "," );
	List< Integer > ids = new ArrayList< Integer >();
	for ( String str : strId ) {
	    if ( CommonUtil.isNotEmpty( str ) ) {
		ids.add( CommonUtil.toInteger( str ) );
	    }
	}
	if ( ids.size() > 0 ) {
	    Map< String,Object > map = new HashMap< String,Object >();
	    Map< String,Object > recevieMap = JsonUtil.json2Map( JSONObject.fromObject( receives ).toString() );
	    if ( !DateTimeKit.laterThanNow( receives.getReceiveDate() ) ) {
		recevieMap.put( "guoqi", 1 );
	    } else {
		recevieMap.put( "guoqi", 0 );
	    }
	    List< DuofenCard > duofencards = duofenCardMapper.findInCardIds( ids );
	    JSONArray jsonList = JSONArray.fromObject( duofencards );
	    List< Map< String,Object > > duofencardList = JsonUtil.json2List( jsonList.toString() );

	    List< Map< String,Object > > returnDuofencardList = new ArrayList< Map< String,Object > >();
	    for ( Map< String,Object > map2 : duofencardList ) {
		// 礼券包
		if ( "DATE_TYPE_FIX_TIME_RANGE".equals( CommonUtil.toString( map2.get( "type" ) ) ) ) {
		    JSONObject jsonObject = JSONObject.fromObject( map2.get( "endTimestamp" ) );
		    Date date = new Date( jsonObject.getLong( "time" ) );
		    if ( !DateTimeKit.laterThanNow( date ) ) {
			map2.put( "guoqi", 1 );
		    } else {
			map2.put( "guoqi", 0 );
		    }
		}
		if ( "4".equals( CommonUtil.toString( map2.get( "cardType" ) ) ) ) {
		    String dateTimeSet = CommonUtil.toString( map2.get( "datetimeset" ) );
		    List< Map< String,Object > > timeList = JsonUtil.json2List( dateTimeSet );
		    for ( Map< String,Object > map3 : timeList ) {
			Date startTime = DateTimeKit.parse( CommonUtil.toString( map3.get( "startTime" ) ), "yyyy-MM-dd" );
			Date endTime = DateTimeKit.parse( CommonUtil.toString( map3.get( "endTime" ) ), "yyyy-MM-dd" );
			if ( !DateTimeKit.laterThanNow( startTime ) && DateTimeKit.laterThanNow( endTime ) ) {
			    map2.put( "money", CommonUtil.toString( map3.get( "money" ) ) ); // 礼券销售金
			    recevieMap.put( "buyMoney", CommonUtil.toString( map3.get( "money" ) ) );
			}
		    }
		    if ( CommonUtil.isEmpty( map2.get( "money" ) ) ) {
			map2.put( "money", 0 );
			recevieMap.put( "buyMoney", 0 );
		    }
		}
		returnDuofencardList.add( map2 );
	    }
	    map.put( "recevieMap", recevieMap );
	    map.put( "returnDuofencardList", returnDuofencardList );
	    return map;
	}
	return null;
    }

    @Override
    public String findCardCode( Integer cardId, Integer memberId, int num ) {
	List< Map< String,Object > > dfcg = duofenCardGetMapper.findByCardId( cardId, memberId, num );
	String duofenCode = "";
	for ( Map< String,Object > map : dfcg ) {
	    duofenCode += map.get( "code" ) + ",";
	}
	return duofenCode;
    }

    @Override
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

    @Override
    public Map< String,Object > publishShelve( Integer id ) {
	Map< String,Object > map = new HashMap< String,Object >();
	try {
	    DuofenCardReceive wsc = duofenCardReceiveMapper.selectById( id );
	    int day = -1;
	    if ( CommonUtil.isNotEmpty( wsc.getReceiveDate() ) ) {
		day = DateTimeKit.daysBetween( new Date(), wsc.getReceiveDate() );
		if ( day < 0 ) {
		    map.put( "result", false );
		    map.put( "message", "包已过期不能发布" );
		    return map;
		}
	    }

	    if ( wsc.getDeliveryAddr() == 1 ) {
		// 第三方投放
		Map< String,Object > params = new HashMap< String,Object >();
		params.put( "busId", wsc.getBusId() );
		params.put( "bagId", wsc.getId() );
		params.put( "mainId", wsc.getThreeMallId() );
		params.put( "bagId", wsc.getId() );
		params.put( "classifyId", wsc.getClassifyId() );
		params.put( "day", day );
		params.put( "number", wsc.getThreeNum() );
		params.put( "time", DateTimeKit.getDate() );
		params.put( "details", "" );
		if ( wsc.getDeliveryType1() == 0 && wsc.getJifen() == 0 && wsc.getFenbi() == 0 ) {
		    params.put( "type", 0 );
		} else if ( wsc.getDeliveryType1() == 1 ) {
		    params.put( "type", 1 );
		    params.put( "amount", wsc.getBuyMoney() );
		} else {
		    params.put( "type", 2 );
		    params.put( "integral", wsc.getJifen() );
		    params.put( "fanscurrency", wsc.getFenbi() );
		}
		params.put( "bitGiftCoupon", wsc.getCardType() );
		//				Map<String, Object> returnMap = couponMallPutlistService
		//						.putCouponMall(params);
		//				if ("0".equals(returnMap.get("code").toString())
		//						|| "2".equals(returnMap.get("code").toString())) {
		//					map.put("result", false);
		//					map.put("message", "投放失败");
		//					return map;
		//				}
	    }
	    DuofenCardReceive wcs1 = new DuofenCardReceive();
	    wcs1.setId( id );
	    wcs1.setState( 1 );
	    wcs1.setCode( CommonUtil.getNominateCode( 6 ) );
	    duofenCardReceiveMapper.updateById( wcs1 );
	    map.put( "result", true );
	    map.put( "message", "发布成功" );
	} catch ( Exception e ) {
	    map.put( "result", false );
	    map.put( "message", "发布失败" );
	}
	return map;
    }

    @Transactional( rollbackFor = Exception.class )
    @Override
    public Map< String,Object > threeShopGetCard( Integer threeMemberId, Member member, Integer busId, Integer bagId ) throws Exception {
	Map< String,Object > returnMap = new HashMap< String,Object >();
	try {
	    DuofenCardReceive dfcr = duofenCardReceiveMapper.selectById( bagId );

	    Map< String,Object > dfcrl = duofenCardReceiveLogMapper.countByCrIdAndMemberId( dfcr.getId(), member.getId() );

	    if ( 1 == dfcr.getNumlimit() ) {
		if ( 1 == dfcr.getMaxNumType() ) {
		    if ( CommonUtil.isNotEmpty( dfcrl ) && dfcrl.size() > 0 ) {
			if ( CommonUtil.isNotEmpty( dfcrl.get( "crId" ) ) && CommonUtil.toString( dfcr.getId() ).equals( dfcrl.get( "crId" ).toString() ) ) {
			    Integer maxNum = CommonUtil.toInteger( dfcr.getMaxNum() );
			    Integer count = CommonUtil.toInteger( dfcrl.get( "cId" ) );
			    if ( maxNum <= count ) {
				returnMap.put( "result", false );
				returnMap.put( "message", "该卡券包您已领取完" ); // 领取状态
				return returnMap;
			    }
			}
		    }
		} else {
		    Integer id = dfcr.getId();
		    Date beginDate = DateTimeKit.parse( DateTimeKit.getDate() + " 00:00:00", "yyyy-MM-dd HH:mm:ss" );
		    Map< String,Object > logList = duofenCardReceiveLogMapper.countByCrIdAndDate( id, member.getId(), beginDate );
		    if ( CommonUtil.isNotEmpty( logList ) ) {
			Integer maxNum = dfcr.getMaxNum();
			Integer count = CommonUtil.toInteger( logList.get( "cId" ) );
			if ( maxNum <= count ) {
			    returnMap.put( "result", false );
			    returnMap.put( "message", "该卡券包您今天已领取完" ); // 领取状态
			    return returnMap;
			}
		    }
		}
	    }

	    Member m1 = new Member();
	    boolean flag = false; // 用来标示是否修改修改用户数据
	    if ( dfcr.getJifen() > 0 ) {
		// 扣除用户积分
		if ( member.getIntegral() < dfcr.getJifen() ) {
		    returnMap.put( "result", false );
		    returnMap.put( "message", "积分不足,不能领取" );
		    return returnMap;
		}
		if ( CommonUtil.isNotEmpty( member.getMcId() ) ) {
		    memberCardService.saveCardRecordNew( member.getMcId(), (byte) 2, dfcr.getJifen() + "积分", "领取优惠券扣除积分", member.getBusId(), null, 0, -dfcr.getJifen() );
		}
		flag = true;
		m1.setIntegral( member.getIntegral() - dfcr.getJifen() );
	    }

	    if ( dfcr.getFenbi() > 0 ) {
		// 扣除用户粉币
		if ( member.getFansCurrency() < dfcr.getFenbi() ) {
		    returnMap.put( "result", false );
		    returnMap.put( "message", "粉币不足,不能领取" );
		    return returnMap;
		}
		if ( CommonUtil.isNotEmpty( member.getMcId() ) ) {
		    memberCardService.saveCardRecordNew( member.getMcId(), (byte) 3, dfcr.getFenbi() + "粉币", "领取优惠券扣除粉币", member.getBusId(), null, 0, -dfcr.getFenbi() );
		}
		flag = true;
		m1.setFansCurrency( member.getFansCurrency() - dfcr.getFenbi() );

		// 归还商户粉币
		returnfansCurrency( member.getBusId(), new Double( -dfcr.getFenbi() ) );
	    }
	    if ( flag ) {
		m1.setId( member.getId() );
		memberMapper.updateById( m1 );
	    }

	    String[] cardIds = dfcr.getCardIds().split( "," );
	    List< Integer > cardList = new ArrayList< Integer >();
	    for ( int i = 0; i < cardIds.length; i++ ) {
		if ( CommonUtil.isNotEmpty( cardIds[i] ) ) {
		    cardList.add( CommonUtil.toInteger( cardIds[i] ) );
		}
	    }

	    List< Map< String,Object > > listMap = duofenCardMapper.findByCardIds( dfcr.getBusId(), cardList );
	    for ( Map< String,Object > map : listMap ) {
		DuofenCardGet duofenCardGet = new DuofenCardGet();
		duofenCardGet.setPublicId( dfcr.getPublicId() );
		duofenCardGet.setMemberId( member.getId() );
		String code = getCode( 12 );
		duofenCardGet.setCode( code );
		duofenCardGet.setGetType( 3 );
		duofenCardGet.setCardId( CommonUtil.toInteger( map.get( "id" ) ) );
		duofenCardGet.setGetDate( new Date() );
		duofenCardGet.setCardReceiveId( dfcr.getId() );

		if ( "DATE_TYPE_FIX_TIME_RANGE".equals( map.get( "type" ) ) ) {
		    duofenCardGet.setStartTime( DateTimeKit.parseDate( map.get( "begin_timestamp" ).toString() ) );
		    duofenCardGet.setEndTime( DateTimeKit.parseDate( map.get( "end_timestamp" ).toString() ) );
		} else {
		    duofenCardGet.setStartTime( DateTimeKit.addDate( new Date(), CommonUtil.toInteger( map.get( "fixed_begin_term" ) ) ) );
		    duofenCardGet.setEndTime( DateTimeKit.addDate( new Date(), CommonUtil.toInteger( map.get( "fixed_term" ) ) ) );
		}
		duofenCardGet.setBusId( member.getBusId() );
		duofenCardGetMapper.insert( duofenCardGet );

	    }

	    DuofenCardReceivelog duofenCardReceiveLog = new DuofenCardReceivelog();
	    duofenCardReceiveLog.setCrId( dfcr.getId() );
	    duofenCardReceiveLog.setCreateDate( new Date() );
	    duofenCardReceiveLog.setMemberId( member.getId() );
	    duofenCardReceiveLog.setThreeMemberId( threeMemberId );
	    duofenCardReceiveLogMapper.insert( duofenCardReceiveLog );

	    // 短信通知
	    if ( dfcr.getIsCallSms() == 1 ) {
		Map< String,Object > params = new HashMap< String,Object >();
		params.put( "busId", member.getBusId() );
		params.put( "model", 12 );
		params.put( "phone", dfcr.getMobilePhone() );
		params.put( "content", "用户领取一个包,包名：" + dfcr.getCardsName() );
		try {
		    HttpClienUtil.httpPost( memberConfig.getWxmp_home()+SEND_SMS, JSONObject.fromObject( params ), true );
		} catch ( Exception e ) {
		    LOG.error( "短信发送失败", e );
		}
	    }

	    int type = 0;
	    if ( flag ) {
		type = 2;
	    }
	    if ( dfcr.getDeliveryType1() == 1 ) {
		type = 1;
	    }
	    Map< String,Object > params = new HashMap< String,Object >();
	    params.put( "bagId", bagId );
	    params.put( "memberId", threeMemberId );
	    params.put( "type", type );
	    if ( CommonUtil.isNotEmpty( dfcr.getReceiveDate() ) ) {
		params.put( "day", DateTimeKit.daysBetween( new Date(), dfcr.getReceiveDate() ) );
	    }

	    //	couponMallOrderService.saveCouponMallOrder(params);

	    returnMap.put( "result", true );
	    returnMap.put( "message", "领取成功" );

	    return returnMap;
	} catch ( Exception e ) {
	    LOG.error( "领取优惠券异常", e );
	    throw new Exception();
	}
    }

    @Override
    public List< DuofenCardGet > findUserCardByReceiveId( Integer memberId, Integer receiveId ) {
	return duofenCardGetMapper.findUserCardByReceiveId( receiveId, memberId );

    }

    @Override
    public List< DuofenCard > findCardOverTime( Integer cardReceiveId, Integer memberId ) {

	List< DuofenCardGet > gs = duofenCardGetMapper.findThreeByOverTime( cardReceiveId, memberId );
	if ( CommonUtil.isEmpty( gs ) ) {
	    return null;
	}
	List< Integer > ids = new ArrayList< Integer >();
	for ( DuofenCardGet d : gs ) {
	    ids.add( d.getCardId() );
	}
	if ( ids.size() > 0 ) {
	    List< DuofenCard > list = duofenCardMapper.findInCardIds( ids );
	    return list;
	}
	return null;
    }

    @Override
    public List< Map< String,Object > > findCardReceive( Integer busId, Integer memberId1, Integer page ) {
	if ( CommonUtil.isEmpty( page ) ) {
	    page = 0;
	}
	Integer firstResult = 5 * page;
	Integer pageSize = 10;

	// 查询卡包下卡券信息
	List< Map< String,Object > > receives = duofenCardReceiveMapper.findCardReceiveBydeliveryType1( busId, new Date(), 0, firstResult, pageSize );

	if ( CommonUtil.isEmpty( receives ) || receives.size() == 0 ) {
	    return null;
	}

	List< Integer > cardIdList = new ArrayList< Integer >();
	for ( Map< String,Object > map : receives ) {
	    String[] cardIds = CommonUtil.toString( map.get( "cardIds" ) ).split( "," );
	    for ( String str : cardIds ) {
		if ( CommonUtil.isNotEmpty( str ) ) {
		    cardIdList.add( CommonUtil.toInteger( str ) );
		}
	    }
	}

	// 卡券没有 返回null
	if ( cardIdList.size() == 0 ) {
	    return null;
	}

	List< Map< String,Object > > duofencards = duofenCardMapper.findByCardIds( busId, cardIdList );

	List< Map< String,Object > > receiveList = new ArrayList< Map< String,Object > >();
	List< Map< String,Object > > dList = new ArrayList< Map< String,Object > >();
	for ( Map< String,Object > r : receives ) {
	    for ( Map< String,Object > d : duofencards ) {
		if ( CommonUtil.toString( r.get( "cardIds" ) ).contains( CommonUtil.toString( d.get( "id" ) ) ) ) {
		    dList.add( d );
		}
	    }
	    r.put( "duofencard", dList );
	    receiveList.add( r );
	}
	return receiveList;
    }

    @Override
    public List< Map< String,Object > > findCardReceive1( Integer busId, Integer memberId, Integer page ) {
	if ( CommonUtil.isEmpty( page ) ) {
	    page = 0;
	}
	Integer firstResult = 5 * page;
	Integer pageSize = 10;

	// 查询卡包下卡券信息
	List< Map< String,Object > > receives = duofenCardReceiveMapper.findCardReceiveBydeliveryType1( busId, new Date(), 1, firstResult, pageSize );

	if ( CommonUtil.isEmpty( receives ) || receives.size() == 0 ) {
	    return null;
	}

	List< Integer > cardIdList = new ArrayList< Integer >();
	for ( Map< String,Object > map : receives ) {
	    String[] cardIds = CommonUtil.toString( map.get( "cardIds" ) ).split( "," );
	    for ( String str : cardIds ) {
		if ( CommonUtil.isNotEmpty( str ) ) {
		    cardIdList.add( CommonUtil.toInteger( str ) );
		}
	    }
	}

	// 卡券没有 返回null
	if ( cardIdList.size() == 0 ) {
	    return null;
	}

	List< Map< String,Object > > duofencards = duofenCardMapper.findByCardIds( busId, cardIdList );

	List< Map< String,Object > > receiveList = new ArrayList< Map< String,Object > >();
	List< Map< String,Object > > dList = new ArrayList< Map< String,Object > >();
	for ( Map< String,Object > r : receives ) {
	    for ( Map< String,Object > d : duofencards ) {
		if ( CommonUtil.toString( r.get( "cardIds" ) ).contains( CommonUtil.toString( d.get( "id" ) ) ) ) {
		    dList.add( d );
		}
	    }
	    r.put( "duofencard", dList );
	    receiveList.add( r );
	}
	return receiveList;
    }

    @Transactional( rollbackFor = Exception.class )
    @Override
    public Map< String,Object > pcBuyReceive( Integer memberId, Integer busId, Integer cardreceiveId ) throws Exception {
	Map< String,Object > map = new HashMap< String,Object >();
	try {
	    DuofenCardReceive dfcr = duofenCardReceiveMapper.selectById( cardreceiveId );

	    Map< String,Object > dfcrl = duofenCardReceiveLogMapper.countByCrIdAndMemberId( dfcr.getId(), memberId );

	    if ( 1 == dfcr.getNumlimit() ) {
		if ( 1 == dfcr.getMaxNumType() ) {
		    if ( CommonUtil.isNotEmpty( dfcrl ) && dfcrl.size() > 0 ) {
			if ( CommonUtil.isNotEmpty( dfcrl.get( "crId" ) ) && CommonUtil.toString( dfcr.getId() ).equals( dfcrl.get( "crId" ).toString() ) ) {
			    Integer maxNum = CommonUtil.toInteger( dfcr.getMaxNum() );
			    Integer count = CommonUtil.toInteger( dfcrl.get( "cId" ) );
			    if ( maxNum <= count ) {
				map.put( "result", false );
				map.put( "message", "该卡券包您已领取完" ); // 领取状态 1已领取完
				// 0还可以领取
				return map;
			    }
			}
		    }
		} else {
		    Integer id = dfcr.getId();
		    Date beginDate = DateTimeKit.parse( DateTimeKit.getDate() + " 00:00:00", "yyyy-MM-dd HH:mm:ss" );
		    Map< String,Object > logList = duofenCardReceiveLogMapper.countByCrIdAndDate( id, memberId, beginDate );
		    if ( CommonUtil.isNotEmpty( logList ) ) {
			Integer maxNum = dfcr.getMaxNum();
			Integer count = CommonUtil.toInteger( logList.get( "cId" ) );
			if ( maxNum <= count ) {
			    map.put( "result", false );
			    map.put( "message", "该卡券包您今天已领取完" ); // 领取状态 1已领取完
			    return map;
			}
		    }
		}
	    }

	    String[] cardIds = dfcr.getCardIds().split( "," );
	    List< Integer > cardList = new ArrayList< Integer >();
	    for ( int i = 0; i < cardIds.length; i++ ) {
		if ( CommonUtil.isNotEmpty( cardIds[i] ) ) {
		    cardList.add( CommonUtil.toInteger( cardIds[i] ) );
		}
	    }

	    List< Map< String,Object > > cardMessage = JsonUtil.json2List( dfcr.getCardMessage() );

	    List< Map< String,Object > > listMap = duofenCardMapper.findByCardIds( dfcr.getBusId(), cardList );

	    List< DuofenCardGet > dcg = new ArrayList<>();
	    for ( Map< String,Object > map1 : listMap ) {
		for ( Map< String,Object > card : cardMessage ) {
		    if ( CommonUtil.toString( card.get( "cardId" ) ).equals( CommonUtil.toString( map1.get( "id" ) ) ) ) {
			int num = CommonUtil.toInteger( card.get( "num" ) );
			if ( num == 0 ) {
			    num = 1;
			}
			for ( int i = 0; i < num; i++ ) {
			    DuofenCardGet duofenCardGet = new DuofenCardGet();
			    duofenCardGet.setPublicId( dfcr.getPublicId() );
			    duofenCardGet.setMemberId( memberId );
			    String code = getCode( 12 );
			    duofenCardGet.setCode( code );
			    duofenCardGet.setGetType( 4 );
			    duofenCardGet.setCardId( CommonUtil.toInteger( map1.get( "id" ) ) );
			    duofenCardGet.setGetDate( new Date() );
			    duofenCardGet.setCardReceiveId( dfcr.getId() );
			    duofenCardGet.setBusId( busId );
			    duofenCardGet.setState( 0 );
			    if ( "DATE_TYPE_FIX_TIME_RANGE".equals( map1.get( "type" ) ) ) {
				duofenCardGet.setStartTime( DateTimeKit.parseDate( map1.get( "begin_timestamp" ).toString() ) );
				duofenCardGet.setEndTime( DateTimeKit.parseDate( map1.get( "end_timestamp" ).toString() ) );
			    } else {
				duofenCardGet.setStartTime( DateTimeKit.addDate( new Date(), CommonUtil.toInteger( map1.get( "fixed_begin_term" ) ) ) );
				duofenCardGet.setEndTime( DateTimeKit.addDate( new Date(), CommonUtil.toInteger( map1.get( "fixed_term" ) ) ) );
			    }
			    //	duofenCardGetMapper.insertSelective(duofenCardGet);
			    dcg.add( duofenCardGet );
			}
		    }
		}
	    }

	    if ( dcg.size() > 0 ) {
		duofenCardGetMapper.insertList( dcg );

		DuofenCardReceivelog duofenCardReceiveLog = new DuofenCardReceivelog();
		duofenCardReceiveLog.setCrId( dfcr.getId() );
		duofenCardReceiveLog.setCreateDate( new Date() );
		duofenCardReceiveLog.setMemberId( memberId );
		duofenCardReceiveLogMapper.insert( duofenCardReceiveLog );
	    }

	    // 短信通知
	    if ( dfcr.getIsCallSms() == 1 ) {
		Map< String,Object > params = new HashMap< String,Object >();
		params.put( "busId", busId );
		params.put( "model", 12 );
		params.put( "phone", dfcr.getMobilePhone() );
		params.put( "content", "用户领取一个包,包名：" + dfcr.getCardsName() );
		try {
		    HttpClienUtil.httpPost( memberConfig.getWxmp_home()+SEND_SMS, JSONObject.fromObject( params ), true );
		} catch ( Exception e ) {
		    LOG.error( "短信发送失败", e );
		}
	    }

	    map.put( "result", true );
	    map.put( "message", "领取成功" );
	} catch ( Exception e ) {
	    LOG.error( "ERP领取优惠券失败", e );
	    throw new Exception();
	}
	return map;
    }

    /**
     * 商场支付成功回调 分配卡券
     */
    @Override
    public Map< String,Object > successBuyReceive( Integer receiveId, Integer num, Integer memberId ) {
	Map< String,Object > map = new HashMap< String,Object >();

	try {
	    DuofenCardReceive receives = duofenCardReceiveMapper.selectById( receiveId );
	    String[] strId = receives.getCardIds().split( "," );
	    List< Integer > ids = new ArrayList< Integer >();
	    for ( String str : strId ) {
		if ( CommonUtil.isNotEmpty( str ) ) {
		    ids.add( CommonUtil.toInteger( str ) );
		}
	    }

	    List< Map< String,Object > > cardMlist = JsonUtil.json2List( receives.getCardMessage() );

	    Member member = memberMapper.selectById( memberId );
	    if ( ids.size() > 0 ) {
		List< DuofenCardGet > list = new ArrayList< DuofenCardGet >();
		List< DuofenCard > duofencards = duofenCardMapper.findInCardIds( ids );
		for ( int i = 0; i < num; i++ ) { // 购买数量
		    for ( DuofenCard duofenCard : duofencards ) {
			for ( Map< String,Object > card : cardMlist ) {
			    if ( duofenCard.getId().equals( CommonUtil.toInteger( card.get( "cardId" ) ) ) ) {
				int number = CommonUtil.toInteger( card.get( "num" ) );
				if ( number == 0 ) {
				    number = 1;
				}
				for ( int j = 0; j < number; j++ ) {
				    DuofenCardGet dfg = new DuofenCardGet();
				    dfg.setCardId( duofenCard.getId() );
				    dfg.setGetType( 4 );
				    dfg.setState( 0 );
				    dfg.setCode( getCode( 12 ) );
				    dfg.setGetDate( new Date() );
				    dfg.setCardReceiveId( receiveId );
				    dfg.setMemberId( memberId );
				    dfg.setPublicId( member.getPublicId() );
				    dfg.setBusId( 0 );
				    dfg.setFriendMemberId( "" );
				    dfg.setBusId( member.getBusId() );
				    if ( "DATE_TYPE_FIX_TIME_RANGE".equals( duofenCard.getType() ) ) {
					dfg.setStartTime( duofenCard.getBeginTimestamp() );
					dfg.setEndTime( duofenCard.getEndTimestamp() );
				    } else {
					dfg.setStartTime( DateTimeKit.addDate( new Date(), duofenCard.getFixedBeginTerm() ) );
					dfg.setEndTime( DateTimeKit.addDate( new Date(), duofenCard.getFixedTerm() ) );
				    }
				    dfg.setBusId( member.getBusId() );
				    list.add( dfg );
				}
			    }
			}
		    }
		}
		duofenCardGetMapper.insertList( list );

		// 短信通知
		if ( receives.getIsCallSms() == 1 ) {
		    Map< String,Object > params = new HashMap< String,Object >();
		    params.put( "busId", member.getBusId() );
		    params.put( "model", 12 );
		    params.put( "phone", receives.getMobilePhone() );
		    params.put( "content", "用户购买了" + num + "个" + receives.getCardIds() + "包,包中有：" + receives.getCardsName() + "优惠券" );
		    try {
			HttpClienUtil.httpPost( memberConfig.getWxmp_home()+SEND_SMS, JSONObject.fromObject( params ), true );
		    } catch ( Exception e ) {
			LOG.error( "短信发送失败", e );
		    }
		}
	    }
	    map.put( "result", true );
	    map.put( "message", "发放成功" );
	} catch ( Exception e ) {
	    e.printStackTrace();
	    map.put( "result", false );
	    map.put( "message", "发放失败" );
	    LOG.error( "用户id:" + memberId + "购买了" + num + "个卡券包id:" + receiveId, e );
	}
	return map;
    }

    @Override
    public List< Map< String,Object > > findReceive( Integer busId ) {
	// 查询卡包下卡券信息
	List< Map< String,Object > > receives = duofenCardReceiveMapper.findCardReceivesBydeliveryType1( busId, new Date(), 1 );
	return receives;
    }

    /**
     * 根据卡包查询卡券信息 取值 看数据库文档名称
     */
    @Override
    public List< Map< String,Object > > findDuofenCard( Integer busId, Integer receiveId ) {
	DuofenCardReceive receives = duofenCardReceiveMapper.selectById( receiveId );
	String[] strId = receives.getCardIds().split( "," );
	List< Integer > ids = new ArrayList< Integer >();
	for ( String str : strId ) {
	    if ( CommonUtil.isNotEmpty( str ) ) {
		ids.add( CommonUtil.toInteger( str ) );
	    }
	}
	if ( ids.size() > 0 ) {
	    List< Map< String,Object > > duofencards = duofenCardMapper.findByCardIds( busId, ids );
	    return duofencards;
	}

	return null;
    }

    /**
     * 购买和免费领取 pc端
     *
     * @param busId
     * @param cardreceiveId 卡包id
     *
     * @return
     * @throws Exception
     */
    public List< Map< String,Object > > findReceviceAll( Integer busId, Integer cardreceiveId ) {

	// 查询卡包下卡券信息
	List< Map< String,Object > > receives = duofenCardReceiveMapper.findCardReceivesBydeliveryTypeAll( busId, new Date() );

	if ( CommonUtil.isEmpty( receives ) || receives.size() == 0 ) {
	    return null;
	}

	List< Integer > cardIdList = new ArrayList< Integer >();
	for ( Map< String,Object > map : receives ) {
	    String[] cardIds = CommonUtil.toString( map.get( "cardIds" ) ).split( "," );
	    for ( String str : cardIds ) {
		if ( CommonUtil.isNotEmpty( str ) ) {
		    cardIdList.add( CommonUtil.toInteger( str ) );
		}
	    }
	}

	// 卡券没有 返回null
	if ( cardIdList.size() == 0 ) {
	    return null;
	}

	List< Map< String,Object > > duofencards = duofenCardMapper.findByCardIds( busId, cardIdList );

	List< Map< String,Object > > receiveList = new ArrayList< Map< String,Object > >();
	List< Map< String,Object > > dList = new ArrayList< Map< String,Object > >();
	for ( Map< String,Object > r : receives ) {
	    for ( Map< String,Object > d : duofencards ) {
		if ( CommonUtil.toString( r.get( "cardIds" ) ).contains( CommonUtil.toString( d.get( "id" ) ) ) ) {
		    dList.add( d );
		}
	    }
	    r.put( "duofencard", dList );
	    receiveList.add( r );
	}
	return receiveList;
    }

    @Override
    public Map< String,Object > backDuofenCardGet( Integer memberId, Integer cardReceiveId ) {
	Map< String,Object > map = new HashMap<>();
	try {
	    List< Map< String,Object > > list = duofenCardGetMapper.findByMemberIdAndCardReceiveId( memberId, cardReceiveId );
	    if ( CommonUtil.isEmpty( list ) || list.size() == 0 ) {
		map.put( "result", false );
		map.put( "message", "没有要回滚的卡券信息" );
		return map;
	    }
	    List< Integer > ids = new ArrayList<>();
	    for ( Map< String,Object > map2 : list ) {
		if ( CommonUtil.isNotEmpty( map2.get( "id" ) ) ) {
		    ids.add( CommonUtil.toInteger( map2.get( "id" ) ) );

		}
	    }
	    if ( ids.size() > 0 ) {
		duofenCardGetMapper.deleteByIds( ids );
	    }
	    map.put( "result", true );
	    map.put( "message", "回滚的卡券信息成功" );
	} catch ( Exception e ) {
	    LOG.error( "回滚的卡券信息异常", e );
	    map.put( "result", false );
	    map.put( "message", "回滚的卡券信息异常" );
	}
	return map;
    }

    @Override
    public List< Map< String,Object > > gameDuofenCardRecevice( Integer busId ) {
	List< Map< String,Object > > duofenCardReceiveList = duofenCardReceiveMapper.findCardRecevice( busId, new Date() );
	return duofenCardReceiveList;
    }

    @Override
    public Map< String,Object > getDuofenCard( Integer receiveId, Integer num, Integer memberId ) {
	Map< String,Object > map = new HashMap< String,Object >();

	try {
	    DuofenCardReceive receives = duofenCardReceiveMapper.selectById( receiveId );
	    String[] strId = receives.getCardIds().split( "," );
	    List< Integer > ids = new ArrayList< Integer >();
	    for ( String str : strId ) {
		if ( CommonUtil.isNotEmpty( str ) ) {
		    ids.add( CommonUtil.toInteger( str ) );
		}
	    }

	    List< Map< String,Object > > cardMlist = JsonUtil.json2List( receives.getCardMessage() );

	    Member member = memberMapper.selectById( memberId );
	    if ( ids.size() > 0 ) {
		List< DuofenCardGet > list = new ArrayList< DuofenCardGet >();
		List< DuofenCard > duofencards = duofenCardMapper.findInCardIds( ids );
		for ( int i = 0; i < num; i++ ) { // 中奖数量
		    for ( DuofenCard duofenCard : duofencards ) {
			for ( Map< String,Object > card : cardMlist ) {
			    if ( duofenCard.getId().equals( CommonUtil.toInteger( card.get( "cardId" ) ) ) ) {
				DuofenCardGet dfg = new DuofenCardGet();
				dfg.setCardId( duofenCard.getId() );
				dfg.setGetType( 5 );
				dfg.setState( 0 );
				dfg.setCode( getCode( 12 ) );
				dfg.setGetDate( new Date() );
				dfg.setCardReceiveId( receiveId );
				dfg.setMemberId( memberId );
				dfg.setPublicId( member.getPublicId() );
				dfg.setFriendMemberId( "" );
				dfg.setBusId( member.getBusId() );
				if ( "DATE_TYPE_FIX_TIME_RANGE".equals( duofenCard.getType() ) ) {
				    dfg.setStartTime( duofenCard.getBeginTimestamp() );
				    dfg.setEndTime( duofenCard.getEndTimestamp() );
				} else {
				    dfg.setStartTime( DateTimeKit.addDate( new Date(), duofenCard.getFixedBeginTerm() ) );
				    dfg.setEndTime( DateTimeKit.addDate( new Date(), duofenCard.getFixedTerm() ) );
				}
				dfg.setBusId( member.getBusId() );
				list.add( dfg );
			    }
			}
		    }
		}
		duofenCardGetMapper.insertList( list );

		// 短信通知
		if ( receives.getIsCallSms() == 1 ) {
		    Map< String,Object > params = new HashMap< String,Object >();
		    params.put( "busId", member.getBusId() );
		    params.put( "model", 12 );
		    params.put( "phone", receives.getMobilePhone() );
		    params.put( "content", "用户购买了" + num + "个" + receives.getCardIds() + "包,包中有：" + receives.getCardsName() + "优惠券" );
		    try {
			HttpClienUtil.httpPost( memberConfig.getWxmp_home()+SEND_SMS, JSONObject.fromObject( params ), true );
		    } catch ( Exception e ) {
			LOG.error( "短信发送失败", e );
		    }
		}
	    }
	    map.put( "result", true );
	    map.put( "message", "发放成功" );
	} catch ( Exception e ) {
	    e.printStackTrace();
	    map.put( "result", false );
	    map.put( "message", "发放失败" );
	    LOG.error( "用户id:" + memberId + "游戏中奖了" + num + "个卡券包id:" + receiveId, e );
	}
	return map;
    }

    @Override
    public Map< String,Object > verificationCard_3( Map< String,Object > params ) {
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
		if ( "1".equals( CommonUtil.toString( map2.get( "state" ) ) ) || "2".equals( CommonUtil.toString( map2.get( "state" ) ) ) ) {
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

	    if ( CommonUtil.isNotEmpty( stateMap.get( 0 ).get( "cardId" ) ) ) {
		DuofenCard c = duofenCardMapper.selectById( CommonUtil.toInteger( stateMap.get( 0 ).get( "cardId" ) ) );
		map.put( "cardId", stateMap.get( 0 ).get( "cardId" ) );
		map.put( "cardName", c.getTitle() );
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
		Double fansCurrency = busUser.getFansCurrency().doubleValue() - recommend.getFenbi();
		b.setFansCurrency( BigDecimal.valueOf( fansCurrency ) );
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

    private Map< String,Object > returnfansCurrency( Integer busId, Double fans_currency ) {
	Map< String,Object > map = new HashMap< String,Object >();
	try {
	    BusUser busUser = busUserMapper.selectById( busId );
	    BusUser busUser1 = new BusUser();
	    busUser1.setId( busId );
	    Double fansCurrency = busUser.getFansCurrency().doubleValue() + fans_currency;
	    busUser1.setFansCurrency( BigDecimal.valueOf( fansCurrency ) );
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

}
