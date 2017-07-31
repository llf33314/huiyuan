package com.gt.member.service.core.ws.business;

import com.gt.member.dao.*;
import com.gt.member.entity.*;
import com.gt.member.service.core.ws.CardCouponsWSServiceImpl;
import com.gt.member.service.core.ws.entitybo.queryBo.*;
import com.gt.member.service.core.ws.entitybo.returnBo.*;
import com.gt.member.service.core.ws.util.ConvertClass;
import com.gt.member.service.old.common.dict.DictService;
import com.gt.member.service.old.member.MemberCardService;
import com.gt.member.util.*;
import com.gt.member.util.token.TokenUitl;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * 优惠券数据查询接口
 *
 * Created by pengjiangli on 2017/7/28 0028.
 */
@Service
public class CardCouponsWS {
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

    @Autowired
    private MemberRecommendDAO recommendMapper;

    @Autowired
    private MemberDAO memberDAO;

    /**
     *
     * 查询
     * 商户下所有微信优惠券 card_type:判断卡片类型 card_type=DISCOUNT折扣券 discount=值 折扣数
     * card_type=CASH 代金券 cash_least_cost=值 条件值 reduce_cost=值 减免金额
     * user_card_code 卡券code image 卡券图标
     * @return
     * @throws Exception
     */
    //<!-----------卡券对外接口Start------------>
    public BaseResult<WxCardListBo> findWxCardByShopId(BaseParam baseParam) throws Exception {
	BaseResult br=new BaseResult();
	try {
	    QueryWxCardBo wcbsb = com.alibaba.fastjson.JSONObject.parseObject( com.alibaba.fastjson.JSONObject.toJSONString( baseParam.getReqdata() ), QueryWxCardBo.class );
	    if ( CommonUtil.isEmpty( wcbsb ) || CommonUtil.isEmpty( wcbsb.getPublicId() ) ) {
		br.setCode( ReturnCode.ERROR_7 );
		br.setMsg( "缺少参数" );
		return br;
	    }
	    Integer shopId = wcbsb.getShopId();
	    JSONObject json = new JSONObject();
	    json.put( "token", TokenUitl.getToken() );
	    json.put( "shopId", shopId );

	    JSONObject returnJSON = HttpClienUtil.httpPost( memberConfig.getWxmp_home() + GET_SHOP_URL, json, false );
	    Map< String,Object > map = (Map< String,Object >) returnJSON.get( "wxshop" );

	    if ( CommonUtil.isEmpty( map ) || CommonUtil.isEmpty( map.get( "poiid" ) ) ) {
		br.setCode( ReturnCode.ERROR_7 );
		br.setMsg( "未查询到门店信息" );
		return br;
	    }

	    Member member = memberDAO.selectById( wcbsb.getMemberId() );
	    if ( CommonUtil.isEmpty( member.getOpenid() ) ) {
		br.setCode( ReturnCode.ERROR_7 );
		br.setMsg( "当前粉丝非微信粉丝" );
		return br;
	    }

	    //查询优惠券信息
	    List< Map< String,Object > > cardList = wxCardReceiveMapper.findByOpenId1( wcbsb.getPublicId(), member.getOpenid() );
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
			if ( DateTimeKit.laterThanNow( DateTimeKit.addDays( DateTimeKit.parse( map2.get( "ctime" ).toString(), "yyyy-MM-dd hh:mm:ss" ), CommonUtil.toInteger( map2.get( "fixed_begin_term" ) ) ) ) ) {
			    continue;
			}
			if ( !DateTimeKit.laterThanNow( DateTimeKit.addDays( DateTimeKit.parse( map2.get( "ctime" ).toString(), "yyyy-MM-dd hh:mm:ss" ), CommonUtil.toInteger( map2.get( "fixed_term" ) ) ) ) ) {
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
		List< com.gt.member.service.core.ws.entitybo.returnBo.WxCardBo > wxCardBos = new ArrayList<>();
		com.gt.member.service.core.ws.entitybo.returnBo.WxCardBo wb = null;
		for ( Map m : list ) {
		    wb = new com.gt.member.service.core.ws.entitybo.returnBo.WxCardBo();
		    wb.setUser_card_code( CommonUtil.toString( m.get( "user_card_code" ) ) );
		    wb.setCard_type( CommonUtil.toString( m.get( "card_type" ) ) );
		    wb.setCash_least_cost( CommonUtil.toDouble( m.get( "cash_least_cost" ) ) );
		    wb.setDiscount( CommonUtil.toInteger( m.get( "discount" ) ) );
		    wb.setImage( CommonUtil.toString( m.get( "image" ) ) );
		    wb.setReduce_cost( CommonUtil.toDouble( m.get( "reduce_cost" ) ) );
		    wxCardBos.add( wb );
		}

		WxCardListBo wxCardListBo = new WxCardListBo();
		wxCardListBo.setWxCardBos( wxCardBos );
		br.setCode( ReturnCode.SUCCESS );
		br.setData( com.alibaba.fastjson.JSONObject.toJSONString( wxCardListBo ) );
		return br;
	    }
	}catch ( Exception e ){
	    br.setCode( ReturnCode.ERROR_1 );
	    br.setMsg( "查询微信卡券异常" );
	    return br;
	}
	br.setCode( ReturnCode.SUCCESS );
	br.setMsg( "未查询到数据" );
	return br;
    }

    /**
     *
     * 查询 商户下所有用的卡券信息 过滤不满足的优惠券 card_type:判断卡片类型 card_type=DISCOUNT折扣券
     * discount=值 折扣数 card_type=CASH 代金券 cash_least_cost=值 条件值 reduce_cost=值
     * 减免金额 user_card_code 卡券code image 卡券图标
     *
     * @return
     * @throws Exception
     */
    public BaseResult findWxCardByShopIdAndMoney( BaseParam baseParam) throws Exception {
	BaseResult br=new BaseResult();
	try {
	    QueryWxCardBo wcbsb = com.alibaba.fastjson.JSONObject.parseObject( com.alibaba.fastjson.JSONObject.toJSONString( baseParam.getReqdata() ), QueryWxCardBo.class );
        if ( CommonUtil.isEmpty( wcbsb.getPublicId() ) ) {
	    br.setCode( ReturnCode.ERROR_7 );
	    br.setMsg( "缺少参数" );
	    return br;
	}
	Integer shopId=wcbsb.getShopId();
	JSONObject json = new JSONObject();
	json.put( "token", TokenUitl.getToken() );
	json.put( "shopId", shopId );
	JSONObject returnJSON = HttpClienUtil.httpPost( memberConfig.getWxmp_home()+GET_SHOP_URL, json, false );
	Map< String,Object > map = (Map< String,Object >) returnJSON.get( "wxshop" );
	if ( CommonUtil.isEmpty( map ) || CommonUtil.isEmpty( map.get( "poiid" ) ) ) {
	    br.setCode( ReturnCode.ERROR_7 );
	    br.setMsg( "缺少参数" );
	    return br;
	}
	Member member=memberDAO.selectById(  wcbsb.getMemberId());

	if ( CommonUtil.isEmpty( member.getOpenid() ) ) {
	    br.setCode( ReturnCode.ERROR_7 );
	    br.setMsg( "当前粉丝非微信粉丝" );
	    return br;
	}

	//查询优惠券信息
	List< Map< String,Object > > cardList = wxCardReceiveMapper.findByOpenId1( wcbsb.getPublicId(), member.getOpenid() );
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
		    if ( cash_least_cost > 0 && cash_least_cost > wcbsb.getMoney() ) {
			continue;
		    }
		}

		if ( map2.get( "location_id_list" ).toString().contains( CommonUtil.toString( map.get( "poiid" ) ) ) ) {
		    list.add( map2 );
		}
		List< com.gt.member.service.core.ws.entitybo.returnBo.WxCardBo > wxCardBos = new ArrayList<>();
		com.gt.member.service.core.ws.entitybo.returnBo.WxCardBo wb = null;
		for ( Map m : list ) {
		    wb = new com.gt.member.service.core.ws.entitybo.returnBo.WxCardBo();
		    wb.setUser_card_code( CommonUtil.toString( m.get( "user_card_code" ) ) );
		    wb.setCard_type( CommonUtil.toString( m.get( "card_type" ) ) );
		    wb.setCash_least_cost( CommonUtil.toDouble( m.get( "cash_least_cost" ) ) );
		    wb.setDiscount( CommonUtil.toInteger( m.get( "discount" ) ) );
		    wb.setImage( CommonUtil.toString( m.get( "image" ) ) );
		    wb.setReduce_cost( CommonUtil.toDouble( m.get( "reduce_cost" ) ) );
		    wxCardBos.add( wb );
		}

		WxCardListBo wxCardListBo = new WxCardListBo();
		wxCardListBo.setWxCardBos( wxCardBos );
		br.setCode( ReturnCode.SUCCESS );
		br.setData( com.alibaba.fastjson.JSONObject.toJSONString( wxCardListBo ) );
		return br;
	    }
	    }
	    }catch( Exception e){
		br.setCode( ReturnCode.ERROR_1 );
		br.setMsg( "查询微信卡券异常" );
		return br;
	    }
	    br.setCode( ReturnCode.SUCCESS );
	    br.setMsg( "未查询到数据" );
	    return br;
	}



    public BaseResult wxCardReceive( BaseParam baseParam) {
	BaseResult br=new BaseResult( );
	try {
	    QueryWxCardBo wcbsb = com.alibaba.fastjson.JSONObject.parseObject( com.alibaba.fastjson.JSONObject.toJSONString( baseParam.getReqdata() ), QueryWxCardBo.class );
	    if ( CommonUtil.isEmpty( wcbsb.getPublicId() ) ) {
		br.setCode( ReturnCode.ERROR_5 );
		br.setMsg( "缺少参数" );
		return br;
	    }
	    WxCardReceive wcr = wxCardReceiveMapper.findByCode1( wcbsb.getPublicId(), wcbsb.getCode() );
	    if ( CommonUtil.isEmpty( wcr ) ) {
		br.setCode( ReturnCode.ERROR_7 );
		br.setMsg( "核销失败" );
		return br;
	    }


	//    JSONObject returnJSON = HttpClienUtil.httpPost( memberConfig.getWxmp_home()+CODE_CONSUME, JSONObject.fromObject( map ), false );

//	    if ( "-1".equals( returnJSON.get( "code" ).toString() ) ) {
//		map.put( "result", -1 );
//		map.put( "message", "微信核销失败" );
//		return map;
//	    }
//
//	    if ( "-2".equals( returnJSON.get( "code" ).toString() ) ) {
//		map.put( "result", -1 );
//		map.put( "message", "token已失效" );
//		return map;
//	    }
//
//	    map.put( "result", 1 );
//	    map.put( "message", "核销成功" );
	} catch ( Exception e ) {
	    e.printStackTrace();
	    LOG.error( "线下核销失败", e );
	    br.setCode(ReturnCode.ERROR_1 );
	    br.setMsg("核销失败" );
	}
	return br;

    }

    /**
     * 查询卡券信息
     * @param baseParam
     * @return
     */
    public BaseResult findWxCardById( BaseParam baseParam ) {
	BaseResult br=new BaseResult( );
	try {
	    QueryWxCardBo wcbsb = com.alibaba.fastjson.JSONObject.parseObject( com.alibaba.fastjson.JSONObject.toJSONString( baseParam.getReqdata() ), QueryWxCardBo.class );
	    if ( CommonUtil.isEmpty( wcbsb.getId() ) ) {
		br.setCode( ReturnCode.ERROR_5 );
		br.setMsg( "缺少参数" );
		return br;
	    }
	    WxCard wxCard=wxCardMapper.selectById( wcbsb.getId()  );
	    if(CommonUtil.isEmpty( wxCard )){
		br.setCode( ReturnCode.ERROR_7 );
		br.setMsg( "数据不存在" );
		return br;
	    }
	    WxCardBo wxCardBo=new WxCardBo();
	    wxCardBo.setId( wxCard.getId() );
	    wxCardBo.setCard_id( wxCard.getCardId() );
	    wxCardBo.setTitle( wxCard.getTitle() );
	    br.setData( wxCardBo );
	    br.setCode( ReturnCode.SUCCESS );

	}catch ( Exception e ){
	    LOG.error( "findWxCardById：查询数据异常了",e );
	    br.setCode( ReturnCode.ERROR_1 );
	    br.setMsg( "查询数据异常" );
	}
	return br;
    }

    //<!-----------卡券对外接口END-------------->

    // <!------------------------多粉卡券对外接口------------------------>

    /**
     * 返回数据中 能使用的到值 属性 image：图片 gId：用户拥有卡券表id, code=用户拥有卡券表 卡券code,
     *         addUser是否允许叠加使用 0不允许 1已允许 , countId=1, 减免券能叠加使用最高使用数量值
     *         discount=0.0, //折扣值 card_type=1, //卡券类型 0折扣券 1减免券
     *         cash_least_cost=10.0, 抵扣条件 reduce_cost=5.0, 抵扣金额 cId=43 卡券id
     *
     * 查询用户拥有的优惠券
     */
    public BaseResult findDuofenCardByMemberId(BaseParam baseParam) {
        BaseResult br=new BaseResult(  );
        try {
	    QueryDuofenCardBo dfcb = com.alibaba.fastjson.JSONObject.parseObject( com.alibaba.fastjson.JSONObject.toJSONString( baseParam.getReqdata() ), QueryDuofenCardBo.class );

	    List< Map< String,Object > > duofencardgets = duofenCardGetMapper.findCardByMemberId( dfcb.getMemberId() );
	    if ( CommonUtil.isEmpty( duofencardgets ) || duofencardgets.size() == 0 ) {
		br.setCode( ReturnCode.ERROR_7 );
		br.setMsg( "未查询到数据" );
		return br;
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
		    if ( location_id_list.contains( dfcb.getWxshopId().toString() ) ) {
			duofencards.add( map );
		    }
		} else {
		    duofencards.add( map );
		}
	    }

	    if ( duofencards.size() > 0 ) {
		DuofenCardListBo duofenCardListBo = new DuofenCardListBo();
		List< DuofenCardBo > list = new ArrayList<>();
		DuofenCardBo dcb = null;
		for ( Map map : duofencards ) {
		    dcb = new DuofenCardBo();
		    dcb.setgId( CommonUtil.toInteger( map.get( "gId" ) ) );
		    dcb.setAddUser( CommonUtil.toInteger( map.get( "addUser" ) ) );
		    dcb.setCard_type( CommonUtil.toInteger( map.get( "type" ) ) );
		    dcb.setCash_least_cost( CommonUtil.toDouble( map.get( "cash_least_cost" ) ) );
		    dcb.setCode( CommonUtil.toString( map.get( "code" ) ) );
		    dcb.setCountId( CommonUtil.toInteger( map.get( "countId" ) ) );
		    dcb.setDiscount( CommonUtil.toDouble( map.get( "discount" ) ) );
		    dcb.setReduce_cost( CommonUtil.toDouble( map.get( "reduce_cost" ) ) );
		    dcb.setImage( CommonUtil.toString( map.get( "image" ) ) );
		    list.add( dcb );
		}
		duofenCardListBo.setDuofenCardBos( list );
		br.setCode( ReturnCode.SUCCESS );
		br.setData(duofenCardListBo);
	    }else{
		br.setCode( ReturnCode.ERROR_7 );
		br.setMsg( "没有存在相应的数据" );
	    }
	}catch ( Exception e ){
            LOG.error( "findDuofenCardByMemberId查询数据异常",e );
            br.setCode( ReturnCode.ERROR_1 );
            br.setMsg( "查询数据失败" );
	}
	return br;
    }

    /**
     * 查询用户拥有的优惠券
     */
    public BaseResult findDuofenCardByMemberIdAndMoney( BaseParam baseParam ) {
	BaseResult br=new BaseResult(  );
	try {
	    QueryDuofenCardBo dfcb = com.alibaba.fastjson.JSONObject.parseObject( com.alibaba.fastjson.JSONObject.toJSONString( baseParam.getReqdata() ), QueryDuofenCardBo.class );
	    Integer memberId=dfcb.getMemberId();
	    Double money=dfcb.getMoney();
	    Integer shopId=dfcb.getWxshopId();
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
		    if ( location_id_list.contains( shopId.toString() ) ) {
			duofencards.add( map );
		    }
		} else {
		    duofencards.add( map );
		}
	    }

	    if ( duofencards.size() > 0 ) {
		DuofenCardListBo duofenCardListBo = new DuofenCardListBo();
		List< DuofenCardBo > list = new ArrayList<>();
		DuofenCardBo dcb = null;
		for ( Map map : duofencards ) {
		    dcb = new DuofenCardBo();
		    dcb.setgId( CommonUtil.toInteger( map.get( "gId" ) ) );
		    dcb.setAddUser( CommonUtil.toInteger( map.get( "addUser" ) ) );
		    dcb.setCard_type( CommonUtil.toInteger( map.get( "card_type" ) ) );
		    dcb.setCash_least_cost( CommonUtil.toDouble( map.get( "cash_least_cost" ) ) );
		    dcb.setCode( CommonUtil.toString( map.get( "code" ) ) );
		    dcb.setCountId( CommonUtil.toInteger( map.get( "countId" ) ) );
		    dcb.setDiscount( CommonUtil.toDouble( map.get( "discount" ) ) );
		    dcb.setReduce_cost( CommonUtil.toDouble( map.get( "reduce_cost" ) ) );
		    dcb.setImage( CommonUtil.toString( map.get( "image" ) ) );
		    list.add( dcb );
		}
		duofenCardListBo.setDuofenCardBos( list );
		br.setCode( ReturnCode.SUCCESS );
		br.setData(duofenCardListBo);
	    }else{
		br.setCode( ReturnCode.ERROR_7 );
		br.setMsg( "没有存在相应的数据" );
	    }

	}catch(Exception e){
	    LOG.error( "findDuofenCardByMemberIdAndMoney查询数据异常",e );
	    br.setCode( ReturnCode.ERROR_1 );
	    br.setMsg( "查询数据失败" );
	}
	return br;

    }

    /**
     *   根据查询本商家商场投放的包
     */

    public BaseResult findReceiveBybusId(BaseParam baseParam) {
	BaseResult br=new BaseResult(  );
	try {
	    QueryDuofenCardBo dfcb = com.alibaba.fastjson.JSONObject.parseObject( com.alibaba.fastjson.JSONObject.toJSONString( baseParam.getReqdata() ), QueryDuofenCardBo.class );
	    Integer busId=dfcb.getBusId();
	    List< DuofenCardReceive > receives = duofenCardReceiveMapper.findCardReceiveBybusId( busId );

	    List<DuofenCardReceiveBo> list=new ArrayList<>(  );
	    for ( DuofenCardReceive d:receives) {
		DuofenCardReceiveBo db= ConvertClass.convertClass(d,  DuofenCardReceiveBo.class);
		list.add( db );
	    }
	    br.setData( list );
	    br.setCode( ReturnCode.SUCCESS );
	} catch ( Exception e ) {
	    LOG.error( "findReceiveBybusId查询数据异常",e );
	    br.setCode( ReturnCode.ERROR_1 );
	    br.setMsg( "查询数据失败" );
	}
	return br;
    }

    /**
     * 根据卡包查询卡券信息
     */
    public BaseResult findCardByReceiveId(BaseParam baseParam) {
	BaseResult br=new BaseResult(  );
	try {
	    GetById dfcb = com.alibaba.fastjson.JSONObject.parseObject( com.alibaba.fastjson.JSONObject.toJSONString( baseParam.getReqdata() ), GetById.class );
	    DuofenCardReceive receives = duofenCardReceiveMapper.selectById( dfcb.getId() );
	    String[] strId = receives.getCardIds().split( "," );
	    List< Integer > ids = new ArrayList< Integer >();
	    for ( String str : strId ) {
		if ( CommonUtil.isNotEmpty( str ) ) {
		    ids.add( CommonUtil.toInteger( str ) );
		}
	    }
	    DuofenCardReceiveBo db=ConvertClass.convertClass( receives,DuofenCardReceiveBo.class );

	    if ( ids.size() > 0 ) {
		List< DuofenCard > duofencards = duofenCardMapper.findInCardIds( ids );
		if ( duofencards.size() > 0 ) {
		    DuofenCardListBo duofenCardListBo = new DuofenCardListBo();
		    List< DuofenCardBo > list = new ArrayList<>();
		    DuofenCardBo dcb = null;
		    for ( DuofenCard d : duofencards ) {
			dcb = new DuofenCardBo();
			dcb.setId( d.getId() );
			dcb.setImage( d.getImage() );
			dcb.setCard_type( d.getCardType() );
			dcb.setTitle( d.getTitle() );
			dcb.setReduce_cost( d.getReduceCost() );
			dcb.setDiscount( d.getDiscount() );
			dcb.setCash_least_cost( d.getCashLeastCost() );
			dcb.setType( d.getType() );
			dcb.setFixed_begin_term( d.getFixedBeginTerm() );
			dcb.setFixed_term( d.getFixedTerm() );
			dcb.setBegin_timestamp( d.getBeginTimestamp() );
			dcb.setEnd_timestamp( d.getEndTimestamp() );
			list.add( dcb );
		    }
		    db.setDuofenCardBos( list );
		}

		br.setCode( ReturnCode.SUCCESS );
		br.setData( JSONObject.fromObject( db ) );
	    }
	}catch ( Exception e ){
	    LOG.error( "findCardByReceiveId查询数据异常",e );
	    br.setCode( ReturnCode.ERROR_1 );
	    br.setMsg( "查询数据失败" );
	}
	return br;
    }

    /**
     * 查询第三方平台下所有优惠券
     */
    public BaseResult findByThreeMemberId( BaseParam baseParam) {
	BaseResult br=new BaseResult(  );
	try {
	    QueryFindByThreeMemberId dfcb = com.alibaba.fastjson.JSONObject
			    .parseObject( com.alibaba.fastjson.JSONObject.toJSONString( baseParam.getReqdata() ), QueryFindByThreeMemberId.class );
	    Integer page = dfcb.getPage();
	    Integer threeMemberId = dfcb.getThreeMemberId();
	    if ( CommonUtil.isEmpty( page ) ) {
		page = 0;
	    }
	    Integer firstResult = 10 * page;
	    Integer pageSize = 10;

	    List< Map< String,Object > > receiveLogs = duofenCardReceiveLogMapper.findByThreeMemberId( threeMemberId, firstResult, pageSize );
	    // 未查询到数据
	    if ( CommonUtil.isEmpty( receiveLogs ) || receiveLogs.size() <= 0 ) {
		br.setCode( ReturnCode.ERROR_7 );
		br.setMsg( "未查询到相应的数据" );
		return br;
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

	    DuofenCardReceiveListBo fb = new DuofenCardReceiveListBo();

	    List< DuofenCardReceiveBo > dr = new ArrayList<>();
	    DuofenCardReceiveBo db = null;
	    for ( Map map : receives ) {
		db = new DuofenCardReceiveBo();
		db.setCardsName( CommonUtil.toString( map.get( "cardsName" ) ) );
		db.setBackColor( CommonUtil.toString( map.get( "backColor" ) ) );
		db.setReceiveDate( DateTimeKit.parseDate( map.get( "receiveDate" ).toString() ) );
		db.setDeliveryType1( CommonUtil.toInteger( map.get( "deliveryType1" ) ) );
		db.setNumlimit( CommonUtil.toInteger( map.get( "numlimit" ) ) );
		db.setMaxNumType( CommonUtil.toInteger( map.get( "maxNumType" ) ) );
		db.setMaxNum( CommonUtil.toInteger( map.get( "maxNum" ) ) );
		dr.add( db );
	    }
	    fb.setDuofenCardReceiveBos( dr );

	    List< DuofenCard > duofencards = duofenCardMapper.findInCardIds( cardIdList );

	    List< DuofenCardBo > list = new ArrayList<>();
	    DuofenCardBo dcb = null;
	    for ( DuofenCard d : duofencards ) {
		dcb = new DuofenCardBo();
		dcb.setId( d.getId() );
		dcb.setImage( d.getImage() );
		dcb.setCard_type( d.getCardType() );
		dcb.setTitle( d.getTitle() );
		dcb.setReduce_cost( d.getReduceCost() );
		dcb.setDiscount( d.getDiscount() );
		dcb.setCash_least_cost( d.getCashLeastCost() );
		dcb.setType( d.getType() );
		dcb.setFixed_begin_term( d.getFixedBeginTerm() );
		dcb.setFixed_term( d.getFixedTerm() );
		dcb.setBegin_timestamp( d.getBeginTimestamp() );
		dcb.setEnd_timestamp( d.getEndTimestamp() );
		list.add( dcb );
	    }
	    fb.setDuofenCardBos( list );
	    br.setCode( ReturnCode.SUCCESS );
	    br.setData( JSONObject.fromObject( fb ) );
	}catch ( Exception e ){
	    LOG.error( "findByThreeMemberId查询数据异常",e );
	    br.setCode( ReturnCode.ERROR_1 );
	    br.setMsg( "查询数据失败" );
	    return br;
	}
	return br;

    }

    /**
     * 根据商家 查询商家拥有的卡包信息(新版本)
     */
    public BaseResult findReceiveByBusUserId_1( BaseParam baseParam) {
	BaseResult br=new BaseResult();

	try {
	    QueryDuofenCardBo dfcb = com.alibaba.fastjson.JSONObject.parseObject( com.alibaba.fastjson.JSONObject.toJSONString( baseParam.getReqdata() ), QueryDuofenCardBo.class );
	    List< Map< String,Object > > receives = duofenCardReceiveMapper.findByBusUserIdAndTime( dfcb.getBusId(), new Date() );

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
	    Integer receiveId = dfcb.getReceiveId();
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
	    DuofenCardReceiveListBo df = new DuofenCardReceiveListBo();
	    List< DuofenCardReceiveBo > list = new ArrayList<>();
	    DuofenCardReceiveBo db = null;
	    for ( Map map : returnList ) {
		db = new DuofenCardReceiveBo();
		db.setCardsName( CommonUtil.toString( map.get( "cardsName" ) ) );
		db.setBackColor( CommonUtil.toString( map.get( "backColor" ) ) );
		db.setReceiveDate( DateTimeKit.parseDate( map.get( "receiveDate" ).toString() ) );
		db.setDeliveryType1( CommonUtil.toInteger( map.get( "deliveryType1" ) ) );
		db.setNumlimit( CommonUtil.toInteger( map.get( "numlimit" ) ) );
		db.setMaxNumType( CommonUtil.toInteger( map.get( "maxNumType" ) ) );
		db.setMaxNum( CommonUtil.toInteger( map.get( "maxNum" ) ) );
		db.setCardType( CommonUtil.toInteger( map.get( "cardType" ) ) );
		db.setLpjBuyMoney( CommonUtil.toDouble( map.get( "buyMoney" ) ) );
		list.add( db );
	    }
	    df.setDuofenCardReceiveBos( list );
	    br.setCode( ReturnCode.SUCCESS );
	}catch ( Exception e ){
	    LOG.error( "findReceiveByBusUserId_1查询数据异常",e );
	    br.setCode( ReturnCode.ERROR_1 );
	    br.setMsg( "查询数据失败" );
	}
	return br;
    }

    /**
     * 商场支付成功回调 分配卡券
     */
    public BaseResult successPayBack(BaseParam baseParam) {
	BaseResult br=new BaseResult();
	try {
	    SuccessPayBackBo dfcb = com.alibaba.fastjson.JSONObject.parseObject( com.alibaba.fastjson.JSONObject.toJSONString( baseParam.getReqdata() ), SuccessPayBackBo.class );
	    Integer receiveId=dfcb.getReceiveId();
	    Integer memberId=dfcb.getMemberId();
	    Integer num=dfcb.getNum();
	    LOG.error( "用户id:" + memberId + "购买了" + num + "个卡券包id:" + receiveId );
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
				    dfg.setCode( getCode() );
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
			LOG.error( "successPayBack短信发送失败", e );
		    }
		}
	    }
	    br.setCode( ReturnCode.SUCCESS );
	    br.setMsg( "发放成功" );
	} catch ( Exception e ) {
	    e.printStackTrace();
	    LOG.error( "successPayBack商城发放卡包异常",e );
	    br.setCode( ReturnCode.ERROR_1 );
	    br.setMsg( "商城发放卡包失败" );
	}
	return br;
    }

    /**
     * 自定义长度校验码
     *
     * @return
     */
    public static String getCode() {
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
    public BaseResult findDuofenCardByReceiveId(BaseParam baseParam) {
	BaseResult br=new BaseResult();
	try {
	    GetById getById = com.alibaba.fastjson.JSONObject.parseObject( com.alibaba.fastjson.JSONObject.toJSONString( baseParam.getReqdata() ), GetById.class );
	    DuofenCardReceive receives = duofenCardReceiveMapper.selectById( getById.getId() );
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

		DuofenCardReceiveBo db = new DuofenCardReceiveBo();
		db.setCardsName( CommonUtil.toString( recevieMap.get( "cardsName" ) ) );
		db.setBackColor( CommonUtil.toString( recevieMap.get( "backColor" ) ) );
		db.setReceiveDate( DateTimeKit.parseDate( recevieMap.get( "receiveDate" ).toString() ) );
		db.setDeliveryType1( CommonUtil.toInteger( recevieMap.get( "deliveryType1" ) ) );
		db.setNumlimit( CommonUtil.toInteger( recevieMap.get( "numlimit" ) ) );
		db.setMaxNumType( CommonUtil.toInteger( recevieMap.get( "maxNumType" ) ) );
		db.setMaxNum( CommonUtil.toInteger( recevieMap.get( "maxNum" ) ) );
		db.setCardType( CommonUtil.toInteger( recevieMap.get( "cardType" ) ) );
		db.setLpjBuyMoney( CommonUtil.toDouble( recevieMap.get( "buyMoney" ) ) );
		db.setGuoqi( CommonUtil.toInteger( recevieMap.get( "guoqi" ) ) );


		List< DuofenCardBo > list = new ArrayList<>();
		DuofenCardBo dcb = null;
		for ( DuofenCard d : duofencards ) {
		    dcb = new DuofenCardBo();
		    dcb.setId( d.getId() );
		    dcb.setImage( d.getImage() );
		    dcb.setCard_type( d.getCardType() );
		    dcb.setTitle( d.getTitle() );
		    dcb.setReduce_cost( d.getReduceCost() );
		    dcb.setDiscount( d.getDiscount() );
		    dcb.setCash_least_cost( d.getCashLeastCost() );
		    dcb.setType( d.getType() );
		    dcb.setFixed_begin_term( d.getFixedBeginTerm() );
		    dcb.setFixed_term( d.getFixedTerm() );
		    dcb.setBegin_timestamp( d.getBeginTimestamp() );
		    dcb.setEnd_timestamp( d.getEndTimestamp() );
		    list.add( dcb );
		}
		db.setDuofenCardBos( list );
		br.setCode( ReturnCode.SUCCESS );
		br.setData( com.alibaba.fastjson.JSONObject.toJSONString( db ) );
		return br;
	    }
	    br.setCode( ReturnCode.ERROR_7 );
	}catch ( Exception e ){
	    e.printStackTrace();
	    LOG.error( "findDuofenCardByReceiveId根据卡包查询卡券信息",e );
	    br.setCode( ReturnCode.ERROR_1 );
	    br.setMsg( "根据卡包查询卡券信息失败" );
	}
	return br;
    }


    public BaseResult verificationCard( BaseParam baseParam ) {
	BaseResult br=new BaseResult();
	try {
	    VerificationCardBo verificationCardBo = com.alibaba.fastjson.JSONObject.parseObject( com.alibaba.fastjson.JSONObject.toJSONString( baseParam.getReqdata() ), VerificationCardBo.class );

	    String codes=verificationCardBo.getCodes();
	    if ( CommonUtil.isEmpty(codes)) {
		br.setCode( ReturnCode.ERROR_5 );
		br.setMsg("没有要核销的卡券code值" );
		return br;
	    }

	    // 多粉优惠券处理
	    String[] str = codes.split( "," );
	    List< String > codeList = new ArrayList< String >();
	    for ( String s : str ) {
		if ( CommonUtil.isNotEmpty( s ) ) {
		    codeList.add( CommonUtil.toString( s ) );
		}
	    }

	    List< Map< String,Object > > stateMap = duofenCardGetMapper.findByCodes( codeList );
	    if ( CommonUtil.isEmpty( stateMap ) || stateMap.size() == 0 ) {
		br.setCode( ReturnCode.ERROR_7);
		br.setMsg("未查询到相应数据" );
		return br;
	    }
	    for ( Map< String,Object > map2 : stateMap ) {
		if ( "1".equals( map2.get( "state" ) ) || "2".equals( map2.get( "state" ) ) ) {
		    br.setCode( ReturnCode.ERROR_7);
		    br.setMsg("卡券过期或已核销,不能执行卡券核销操作" );
		    return br;
		}
	    }
		Integer storeId=verificationCardBo.getStoreId();
	    if ( CommonUtil.isEmpty( storeId ) ) {
		duofenCardGetMapper.updateByCodes( codeList ); // 卡券核销
	    } else {
		duofenCardGetMapper.updateStoreIdByCodes( codeList, storeId ); // 卡券核销
	    }

	    //推荐优惠券赠送
	    for ( Map< String,Object > map2 : stateMap ) {
		if ( CommonUtil.toInteger( map2.get( "recommendId" ) ) > 0 ) {
		    MemberRecommend recommend = recommendMapper.selectById( CommonUtil.toInteger( map2.get( "recommendId" ) ) );
		    tuijianGive( recommend );
		}
	    }
	    br.setCode( ReturnCode.SUCCESS );
	    br.setMsg( "卡券核销成功" );
	} catch ( Exception e ) {
	    LOG.error( "verificationCard卡券核销异常",e );
	    br.setCode( ReturnCode.ERROR_1 );
	    br.setMsg( "卡券核销失败" );
	}
	return br;
    }



//    public Map< String,Object > threeShopGetCard( Integer threeMemberId, Member member, Integer busId, Integer bagId ) throws Exception {
//	Map< String,Object > returnMap = new HashMap< String,Object >();
//	try {
//	    DuofenCardReceive dfcr = duofenCardReceiveMapper.selectById( bagId );
//
//	    Map< String,Object > dfcrl = duofenCardReceiveLogMapper.countByCrIdAndMemberId( dfcr.getId(), member.getId() );
//
//	    if ( 1 == dfcr.getNumlimit() ) {
//		if ( 1 == dfcr.getMaxNumType() ) {
//		    if ( CommonUtil.isNotEmpty( dfcrl ) && dfcrl.size() > 0 ) {
//			if ( CommonUtil.isNotEmpty( dfcrl.get( "crId" ) ) && CommonUtil.toString( dfcr.getId() ).equals( dfcrl.get( "crId" ).toString() ) ) {
//			    Integer maxNum = CommonUtil.toInteger( dfcr.getMaxNum() );
//			    Integer count = CommonUtil.toInteger( dfcrl.get( "cId" ) );
//			    if ( maxNum <= count ) {
//				returnMap.put( "result", false );
//				returnMap.put( "message", "该卡券包您已领取完" ); // 领取状态
//				return returnMap;
//			    }
//			}
//		    }
//		} else {
//		    Integer id = dfcr.getId();
//		    Date beginDate = DateTimeKit.parse( DateTimeKit.getDate() + " 00:00:00", "yyyy-MM-dd HH:mm:ss" );
//		    Map< String,Object > logList = duofenCardReceiveLogMapper.countByCrIdAndDate( id, member.getId(), beginDate );
//		    if ( CommonUtil.isNotEmpty( logList ) ) {
//			Integer maxNum = dfcr.getMaxNum();
//			Integer count = CommonUtil.toInteger( logList.get( "cId" ) );
//			if ( maxNum <= count ) {
//			    returnMap.put( "result", false );
//			    returnMap.put( "message", "该卡券包您今天已领取完" ); // 领取状态
//			    return returnMap;
//			}
//		    }
//		}
//	    }
//
//	    Member m1 = new Member();
//	    boolean flag = false; // 用来标示是否修改修改用户数据
//	    if ( dfcr.getJifen() > 0 ) {
//		// 扣除用户积分
//		if ( member.getIntegral() < dfcr.getJifen() ) {
//		    returnMap.put( "result", false );
//		    returnMap.put( "message", "积分不足,不能领取" );
//		    return returnMap;
//		}
//		if ( CommonUtil.isNotEmpty( member.getMcId() ) ) {
//		    memberCardService.saveCardRecordNew( member.getMcId(), (byte) 2, dfcr.getJifen() + "积分", "领取优惠券扣除积分", member.getBusId(), null, 0, -dfcr.getJifen() );
//		}
//		flag = true;
//		m1.setIntegral( member.getIntegral() - dfcr.getJifen() );
//	    }
//
//	    if ( dfcr.getFenbi() > 0 ) {
//		// 扣除用户粉币
//		if ( member.getFansCurrency() < dfcr.getFenbi() ) {
//		    returnMap.put( "result", false );
//		    returnMap.put( "message", "粉币不足,不能领取" );
//		    return returnMap;
//		}
//		if ( CommonUtil.isNotEmpty( member.getMcId() ) ) {
//		    memberCardService.saveCardRecordNew( member.getMcId(), (byte) 3, dfcr.getFenbi() + "粉币", "领取优惠券扣除粉币", member.getBusId(), null, 0, -dfcr.getFenbi() );
//		}
//		flag = true;
//		m1.setFansCurrency( member.getFansCurrency() - dfcr.getFenbi() );
//
//		// 归还商户粉币
//		returnfansCurrency( member.getBusId(), new Double( -dfcr.getFenbi() ) );
//	    }
//	    if ( flag ) {
//		m1.setId( member.getId() );
//		memberMapper.updateById( m1 );
//	    }
//
//	    String[] cardIds = dfcr.getCardIds().split( "," );
//	    List< Integer > cardList = new ArrayList< Integer >();
//	    for ( int i = 0; i < cardIds.length; i++ ) {
//		if ( CommonUtil.isNotEmpty( cardIds[i] ) ) {
//		    cardList.add( CommonUtil.toInteger( cardIds[i] ) );
//		}
//	    }
//
//	    List< Map< String,Object > > listMap = duofenCardMapper.findByCardIds( dfcr.getBusId(), cardList );
//	    for ( Map< String,Object > map : listMap ) {
//		DuofenCardGet duofenCardGet = new DuofenCardGet();
//		duofenCardGet.setPublicId( dfcr.getPublicId() );
//		duofenCardGet.setMemberId( member.getId() );
//		String code = getCode( 12 );
//		duofenCardGet.setCode( code );
//		duofenCardGet.setGetType( 3 );
//		duofenCardGet.setCardId( CommonUtil.toInteger( map.get( "id" ) ) );
//		duofenCardGet.setGetDate( new Date() );
//		duofenCardGet.setCardReceiveId( dfcr.getId() );
//
//		if ( "DATE_TYPE_FIX_TIME_RANGE".equals( map.get( "type" ) ) ) {
//		    duofenCardGet.setStartTime( DateTimeKit.parseDate( map.get( "begin_timestamp" ).toString() ) );
//		    duofenCardGet.setEndTime( DateTimeKit.parseDate( map.get( "end_timestamp" ).toString() ) );
//		} else {
//		    duofenCardGet.setStartTime( DateTimeKit.addDate( new Date(), CommonUtil.toInteger( map.get( "fixed_begin_term" ) ) ) );
//		    duofenCardGet.setEndTime( DateTimeKit.addDate( new Date(), CommonUtil.toInteger( map.get( "fixed_term" ) ) ) );
//		}
//		duofenCardGet.setBusId( member.getBusId() );
//		duofenCardGetMapper.insert( duofenCardGet );
//
//	    }
//
//	    DuofenCardReceivelog duofenCardReceiveLog = new DuofenCardReceivelog();
//	    duofenCardReceiveLog.setCrId( dfcr.getId() );
//	    duofenCardReceiveLog.setCreateDate( new Date() );
//	    duofenCardReceiveLog.setMemberId( member.getId() );
//	    duofenCardReceiveLog.setThreeMemberId( threeMemberId );
//	    duofenCardReceiveLogMapper.insert( duofenCardReceiveLog );
//
//	    // 短信通知
//	    if ( dfcr.getIsCallSms() == 1 ) {
//		Map< String,Object > params = new HashMap< String,Object >();
//		params.put( "busId", member.getBusId() );
//		params.put( "model", 12 );
//		params.put( "phone", dfcr.getMobilePhone() );
//		params.put( "content", "用户领取一个包,包名：" + dfcr.getCardsName() );
//		try {
//		    HttpClienUtil.httpPost( memberConfig.getWxmp_home()+SEND_SMS, JSONObject.fromObject( params ), true );
//		} catch ( Exception e ) {
//		    LOG.error( "短信发送失败", e );
//		}
//	    }
//
//	    int type = 0;
//	    if ( flag ) {
//		type = 2;
//	    }
//	    if ( dfcr.getDeliveryType1() == 1 ) {
//		type = 1;
//	    }
//	    Map< String,Object > params = new HashMap< String,Object >();
//	    params.put( "bagId", bagId );
//	    params.put( "memberId", threeMemberId );
//	    params.put( "type", type );
//	    if ( CommonUtil.isNotEmpty( dfcr.getReceiveDate() ) ) {
//		params.put( "day", DateTimeKit.daysBetween( new Date(), dfcr.getReceiveDate() ) );
//	    }
//
//	    //	couponMallOrderService.saveCouponMallOrder(params);
//
//	    returnMap.put( "result", true );
//	    returnMap.put( "message", "领取成功" );
//
//	    return returnMap;
//	} catch ( Exception e ) {
//	    LOG.error( "领取优惠券异常", e );
//	    throw new Exception();
//	}
//    }

//    @Override
//    public List< DuofenCardGet > findUserCardByReceiveId( Integer memberId, Integer receiveId ) {
//	return duofenCardGetMapper.findUserCardByReceiveId( receiveId, memberId );
//
//    }

//    @Override
//    public List< DuofenCard > findCardOverTime( Integer cardReceiveId, Integer memberId ) {
//
//	List< DuofenCardGet > gs = duofenCardGetMapper.findThreeByOverTime( cardReceiveId, memberId );
//	if ( CommonUtil.isEmpty( gs ) ) {
//	    return null;
//	}
//	List< Integer > ids = new ArrayList< Integer >();
//	for ( DuofenCardGet d : gs ) {
//	    ids.add( d.getCardId() );
//	}
//	if ( ids.size() > 0 ) {
//	    List< DuofenCard > list = duofenCardMapper.findInCardIds( ids );
//	    return list;
//	}
//	return null;
//    }

//    @Override
//    public List< Map< String,Object > > findCardReceive( Integer busId, Integer memberId1, Integer page ) {
//	if ( CommonUtil.isEmpty( page ) ) {
//	    page = 0;
//	}
//	Integer firstResult = 5 * page;
//	Integer pageSize = 10;
//
//	// 查询卡包下卡券信息
//	List< Map< String,Object > > receives = duofenCardReceiveMapper.findCardReceiveBydeliveryType1( busId, new Date(), 0, firstResult, pageSize );
//
//	if ( CommonUtil.isEmpty( receives ) || receives.size() == 0 ) {
//	    return null;
//	}
//
//	List< Integer > cardIdList = new ArrayList< Integer >();
//	for ( Map< String,Object > map : receives ) {
//	    String[] cardIds = CommonUtil.toString( map.get( "cardIds" ) ).split( "," );
//	    for ( String str : cardIds ) {
//		if ( CommonUtil.isNotEmpty( str ) ) {
//		    cardIdList.add( CommonUtil.toInteger( str ) );
//		}
//	    }
//	}
//
//	// 卡券没有 返回null
//	if ( cardIdList.size() == 0 ) {
//	    return null;
//	}
//
//	List< Map< String,Object > > duofencards = duofenCardMapper.findByCardIds( busId, cardIdList );
//
//	List< Map< String,Object > > receiveList = new ArrayList< Map< String,Object > >();
//	List< Map< String,Object > > dList = new ArrayList< Map< String,Object > >();
//	for ( Map< String,Object > r : receives ) {
//	    for ( Map< String,Object > d : duofencards ) {
//		if ( CommonUtil.toString( r.get( "cardIds" ) ).contains( CommonUtil.toString( d.get( "id" ) ) ) ) {
//		    dList.add( d );
//		}
//	    }
//	    r.put( "duofencard", dList );
//	    receiveList.add( r );
//	}
//	return receiveList;
//    }

//    @Override
//    public List< Map< String,Object > > findCardReceive1( Integer busId, Integer memberId, Integer page ) {
//	if ( CommonUtil.isEmpty( page ) ) {
//	    page = 0;
//	}
//	Integer firstResult = 5 * page;
//	Integer pageSize = 10;
//
//	// 查询卡包下卡券信息
//	List< Map< String,Object > > receives = duofenCardReceiveMapper.findCardReceiveBydeliveryType1( busId, new Date(), 1, firstResult, pageSize );
//
//	if ( CommonUtil.isEmpty( receives ) || receives.size() == 0 ) {
//	    return null;
//	}
//
//	List< Integer > cardIdList = new ArrayList< Integer >();
//	for ( Map< String,Object > map : receives ) {
//	    String[] cardIds = CommonUtil.toString( map.get( "cardIds" ) ).split( "," );
//	    for ( String str : cardIds ) {
//		if ( CommonUtil.isNotEmpty( str ) ) {
//		    cardIdList.add( CommonUtil.toInteger( str ) );
//		}
//	    }
//	}
//
//	// 卡券没有 返回null
//	if ( cardIdList.size() == 0 ) {
//	    return null;
//	}
//
//	List< Map< String,Object > > duofencards = duofenCardMapper.findByCardIds( busId, cardIdList );
//
//	List< Map< String,Object > > receiveList = new ArrayList< Map< String,Object > >();
//	List< Map< String,Object > > dList = new ArrayList< Map< String,Object > >();
//	for ( Map< String,Object > r : receives ) {
//	    for ( Map< String,Object > d : duofencards ) {
//		if ( CommonUtil.toString( r.get( "cardIds" ) ).contains( CommonUtil.toString( d.get( "id" ) ) ) ) {
//		    dList.add( d );
//		}
//	    }
//	    r.put( "duofencard", dList );
//	    receiveList.add( r );
//	}
//	return receiveList;
//    }
//
//    @Transactional( rollbackFor = Exception.class )
//    @Override
//    public Map< String,Object > pcBuyReceive( Integer memberId, Integer busId, Integer cardreceiveId ) throws Exception {
//	Map< String,Object > map = new HashMap< String,Object >();
//	try {
//	    DuofenCardReceive dfcr = duofenCardReceiveMapper.selectById( cardreceiveId );
//
//	    Map< String,Object > dfcrl = duofenCardReceiveLogMapper.countByCrIdAndMemberId( dfcr.getId(), memberId );
//
//	    if ( 1 == dfcr.getNumlimit() ) {
//		if ( 1 == dfcr.getMaxNumType() ) {
//		    if ( CommonUtil.isNotEmpty( dfcrl ) && dfcrl.size() > 0 ) {
//			if ( CommonUtil.isNotEmpty( dfcrl.get( "crId" ) ) && CommonUtil.toString( dfcr.getId() ).equals( dfcrl.get( "crId" ).toString() ) ) {
//			    Integer maxNum = CommonUtil.toInteger( dfcr.getMaxNum() );
//			    Integer count = CommonUtil.toInteger( dfcrl.get( "cId" ) );
//			    if ( maxNum <= count ) {
//				map.put( "result", false );
//				map.put( "message", "该卡券包您已领取完" ); // 领取状态 1已领取完
//				// 0还可以领取
//				return map;
//			    }
//			}
//		    }
//		} else {
//		    Integer id = dfcr.getId();
//		    Date beginDate = DateTimeKit.parse( DateTimeKit.getDate() + " 00:00:00", "yyyy-MM-dd HH:mm:ss" );
//		    Map< String,Object > logList = duofenCardReceiveLogMapper.countByCrIdAndDate( id, memberId, beginDate );
//		    if ( CommonUtil.isNotEmpty( logList ) ) {
//			Integer maxNum = dfcr.getMaxNum();
//			Integer count = CommonUtil.toInteger( logList.get( "cId" ) );
//			if ( maxNum <= count ) {
//			    map.put( "result", false );
//			    map.put( "message", "该卡券包您今天已领取完" ); // 领取状态 1已领取完
//			    return map;
//			}
//		    }
//		}
//	    }
//
//	    String[] cardIds = dfcr.getCardIds().split( "," );
//	    List< Integer > cardList = new ArrayList< Integer >();
//	    for ( int i = 0; i < cardIds.length; i++ ) {
//		if ( CommonUtil.isNotEmpty( cardIds[i] ) ) {
//		    cardList.add( CommonUtil.toInteger( cardIds[i] ) );
//		}
//	    }
//
//	    List< Map< String,Object > > cardMessage = JsonUtil.json2List( dfcr.getCardMessage() );
//
//	    List< Map< String,Object > > listMap = duofenCardMapper.findByCardIds( dfcr.getBusId(), cardList );
//
//	    List< DuofenCardGet > dcg = new ArrayList<>();
//	    for ( Map< String,Object > map1 : listMap ) {
//		for ( Map< String,Object > card : cardMessage ) {
//		    if ( CommonUtil.toString( card.get( "cardId" ) ).equals( CommonUtil.toString( map1.get( "id" ) ) ) ) {
//			int num = CommonUtil.toInteger( card.get( "num" ) );
//			if ( num == 0 ) {
//			    num = 1;
//			}
//			for ( int i = 0; i < num; i++ ) {
//			    DuofenCardGet duofenCardGet = new DuofenCardGet();
//			    duofenCardGet.setPublicId( dfcr.getPublicId() );
//			    duofenCardGet.setMemberId( memberId );
//			    String code = getCode( 12 );
//			    duofenCardGet.setCode( code );
//			    duofenCardGet.setGetType( 4 );
//			    duofenCardGet.setCardId( CommonUtil.toInteger( map1.get( "id" ) ) );
//			    duofenCardGet.setGetDate( new Date() );
//			    duofenCardGet.setCardReceiveId( dfcr.getId() );
//			    duofenCardGet.setBusId( busId );
//			    duofenCardGet.setState( 0 );
//			    if ( "DATE_TYPE_FIX_TIME_RANGE".equals( map1.get( "type" ) ) ) {
//				duofenCardGet.setStartTime( DateTimeKit.parseDate( map1.get( "begin_timestamp" ).toString() ) );
//				duofenCardGet.setEndTime( DateTimeKit.parseDate( map1.get( "end_timestamp" ).toString() ) );
//			    } else {
//				duofenCardGet.setStartTime( DateTimeKit.addDate( new Date(), CommonUtil.toInteger( map1.get( "fixed_begin_term" ) ) ) );
//				duofenCardGet.setEndTime( DateTimeKit.addDate( new Date(), CommonUtil.toInteger( map1.get( "fixed_term" ) ) ) );
//			    }
//			    //	duofenCardGetMapper.insertSelective(duofenCardGet);
//			    dcg.add( duofenCardGet );
//			}
//		    }
//		}
//	    }
//
//	    if ( dcg.size() > 0 ) {
//		duofenCardGetMapper.insertList( dcg );
//
//		DuofenCardReceivelog duofenCardReceiveLog = new DuofenCardReceivelog();
//		duofenCardReceiveLog.setCrId( dfcr.getId() );
//		duofenCardReceiveLog.setCreateDate( new Date() );
//		duofenCardReceiveLog.setMemberId( memberId );
//		duofenCardReceiveLogMapper.insert( duofenCardReceiveLog );
//	    }
//
//	    // 短信通知
//	    if ( dfcr.getIsCallSms() == 1 ) {
//		Map< String,Object > params = new HashMap< String,Object >();
//		params.put( "busId", busId );
//		params.put( "model", 12 );
//		params.put( "phone", dfcr.getMobilePhone() );
//		params.put( "content", "用户领取一个包,包名：" + dfcr.getCardsName() );
//		try {
//		    HttpClienUtil.httpPost( memberConfig.getWxmp_home()+SEND_SMS, JSONObject.fromObject( params ), true );
//		} catch ( Exception e ) {
//		    LOG.error( "短信发送失败", e );
//		}
//	    }
//
//	    map.put( "result", true );
//	    map.put( "message", "领取成功" );
//	} catch ( Exception e ) {
//	    LOG.error( "ERP领取优惠券失败", e );
//	    throw new Exception();
//	}
//	return map;
//    }

    /**
     * (汽车ERP) 购买 手机端购买支付成功 回调
     */
    public BaseResult successBuyReceive( BaseParam baseParam ) {
	BaseResult br=new BaseResult();
	try {
	    SuccessPayBackBo dfcb = com.alibaba.fastjson.JSONObject.parseObject( com.alibaba.fastjson.JSONObject.toJSONString( baseParam.getReqdata() ), SuccessPayBackBo.class );
	    Integer receiveId=dfcb.getReceiveId();
	    Integer memberId=dfcb.getMemberId();
	    Integer num=dfcb.getNum();
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
				    dfg.setCode( getCode() );
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
	    br.setCode( ReturnCode.SUCCESS );
	    br.setMsg( "发放成功" );
	} catch ( Exception e ) {
	    e.printStackTrace();
	    LOG.error( "successPayBack商城发放卡包异常",e );
	    br.setCode( ReturnCode.ERROR_1 );
	    br.setMsg( "商城发放卡包失败" );
	}
	return br;
    }

    /**
     * 卡包信息（购买） 美容
     * @return
     */
    public BaseResult findReceive(BaseParam baseParam) {
	// 查询卡包下卡券信息
	BaseResult br=new BaseResult(  );
	try {
	    QueryDuofenCardBo dfcb = com.alibaba.fastjson.JSONObject.parseObject( com.alibaba.fastjson.JSONObject.toJSONString( baseParam.getReqdata() ), QueryDuofenCardBo.class );
	    List< Map< String,Object > > receives = duofenCardReceiveMapper.findCardReceivesBydeliveryType1( dfcb.getBusId(), new Date(), 1 );
	    DuofenCardReceiveListBo drs=new DuofenCardReceiveListBo();
	    List< DuofenCardReceiveBo > dr = new ArrayList<>();
	    DuofenCardReceiveBo db = null;
	    for ( Map map : receives ) {
		db = new DuofenCardReceiveBo();
		db.setCardsName( CommonUtil.toString( map.get( "cardsName" ) ) );
		db.setBackColor( CommonUtil.toString( map.get( "backColor" ) ) );
		db.setReceiveDate( DateTimeKit.parseDate( map.get( "receiveDate" ).toString() ) );
		db.setDeliveryType1( CommonUtil.toInteger( map.get( "deliveryType1" ) ) );
		db.setNumlimit( CommonUtil.toInteger( map.get( "numlimit" ) ) );
		db.setMaxNumType( CommonUtil.toInteger( map.get( "maxNumType" ) ) );
		db.setMaxNum( CommonUtil.toInteger( map.get( "maxNum" ) ) );
		db.setBuyMoney( CommonUtil.toDouble( map.get( "buyMoney" ) ) );
		db.setCardMessage( CommonUtil.toString( map.get( "cardMessage" ) ) );
		dr.add( db );
	    }
	    drs.setDuofenCardReceiveBos( dr );
	    br.setCode( ReturnCode.SUCCESS );
	    br.setData( com.alibaba.fastjson.JSONObject.toJSONString(drs  ) );
	} catch ( Exception e ) {
	    LOG.error( "findReceive查询异常",e );
	    br.setCode( ReturnCode.ERROR_1 );
	    br.setMsg( "查询数据失败" );
	}
	return br;
    }

    /**
     * 根据卡包查询卡券信息
     */
    public BaseResult findDuofenCard( BaseParam baseParam) {
	BaseResult br=new BaseResult(  );
	try {
	    QueryDuofenCardBo dfcb = com.alibaba.fastjson.JSONObject.parseObject( com.alibaba.fastjson.JSONObject.toJSONString( baseParam.getReqdata() ), QueryDuofenCardBo.class );
	    DuofenCardReceive receives = duofenCardReceiveMapper.selectById( dfcb.getReceiveId() );
	    String[] strId = receives.getCardIds().split( "," );
	    List< Integer > ids = new ArrayList< Integer >();
	    for ( String str : strId ) {
		if ( CommonUtil.isNotEmpty( str ) ) {
		    ids.add( CommonUtil.toInteger( str ) );
		}
	    }
	    if ( ids.size() > 0 ) {
	        DuofenCardReceiveListBo dfb=new DuofenCardReceiveListBo();
		List< Map< String,Object > > duofencards = duofenCardMapper.findByCardIds( dfcb.getBusId(), ids );
		List< DuofenCardBo > list = new ArrayList<>();
		DuofenCardBo dcb = null;
		for ( Map map : duofencards ) {
		    dcb = new DuofenCardBo();
		    dcb.setId(CommonUtil.toInteger( map.get( "id" ) ));
		    dcb.setCard_type( CommonUtil.toInteger( map.get( "card_type" ) ) );
		    dcb.setTitle( CommonUtil.toString( map.get( "title" ) ) );
		    dcb.setReduce_cost( CommonUtil.toDouble( map.get( "reduce_cost" ) ));
		    dcb.setDiscount(CommonUtil.toDouble( map.get( "discount" ) ) );
		    dcb.setCash_least_cost( CommonUtil.toDouble( map.get(  "cash_least_cost") ));
		    dcb.setType(CommonUtil.toString( map.get( "type" ) ) );
		    dcb.setFixed_begin_term( CommonUtil.toInteger(  map.get( "fixed_begin_term" )) );
		    dcb.setFixed_term(  CommonUtil.toInteger(  map.get( "fixed_term" )) );
		    dcb.setBegin_timestamp( DateTimeKit.parseDate( CommonUtil.toString( map.get( "begin_timestamp" ) ) ) );
		    dcb.setEnd_timestamp( DateTimeKit.parseDate( CommonUtil.toString( map.get( "end_timestamp" ) ) ));
		    list.add( dcb );
		}
		dfb.setDuofenCardBos( list );
		br.setCode( ReturnCode.SUCCESS );
		br.setData( com.alibaba.fastjson.JSONObject.toJSONString( dfb ) );
	    }
	} catch ( Exception e ) {
	    LOG.error( "findDuofenCard查询异常",e );
	    br.setCode( ReturnCode.ERROR_1 );
	    br.setMsg( "查询数据失败" );
	}
	return br;
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
