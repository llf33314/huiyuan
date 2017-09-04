package com.gt.member.service.count;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gt.api.enums.ResponseEnums;
import com.gt.api.util.sign.SignHttpUtils;
import com.gt.common.entity.BusUser;
import com.gt.common.entity.WxPublicUsers;
import com.gt.common.entity.WxShop;
import com.gt.member.dao.*;
import com.gt.member.dao.common.BusUserDAO;
import com.gt.member.dao.common.WxPublicUsersDAO;
import com.gt.member.dao.common.WxShopDAO;
import com.gt.member.entity.*;
import com.gt.member.enums.ResponseMemberEnums;
import com.gt.member.exception.BusinessException;
import com.gt.member.service.MemberNewService;
import com.gt.member.service.common.MemberCommonService;
import com.gt.member.service.common.dict.DictService;
import com.gt.member.service.entityBo.queryBo.MallAllEntityQuery;
import com.gt.member.service.entityBo.queryBo.MallEntityQuery;
import com.gt.member.service.memberApi.CardCouponsApiService;
import com.gt.member.service.memberApi.MemberCountMoneyApiService;
import com.gt.member.service.entityBo.MallEntity;
import com.gt.member.service.entityBo.MallNotShopEntity;
import com.gt.member.util.*;
import net.sf.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * ERP 统一计算service
 * <p>
 * Created by Administrator on 2017/8/15 0015.
 */
@Service
public class ERPCountServiceImpl implements ERPCountService {

    private static final Logger LOG = LoggerFactory.getLogger( ERPCountServiceImpl.class );

    private static final String SAOMA_QIANBAO_PAY="/wxMicropay/79B4DE7C/payMicropayVer2_0.do";

    @Autowired
    private MemberConfig      memberConfig;
    @Autowired
    private BusUserDAO        busUserMapper;
    @Autowired
    private DictService       dictService;
    @Autowired
    private MemberCardDAO     cardMapper;
    @Autowired
    private MemberCardLentDAO cardLentMapper;
    @Autowired
    private MemberDAO         memberDAO;
    @Autowired
    private MemberGiveruleDAO giveRuleMapper;

    @Autowired
    private MemberCommonService memberCommonService;

    @Autowired
    private WxShopDAO wxShopDAO;

    @Autowired
    private WxPublicUsersDAO wxPublicUsersMapper;

    @Autowired
    private WxCardReceiveDAO wxCardReceiveMapper;

    @Autowired
    private DuofenCardGetDAO duofenCardGetMapper;

    @Autowired
    private MemberCountMoneyApiService memberCountMoneyApiService;

    @Autowired
    private MemberCardDAO memberCardDAO;

    @Autowired
    private CardCouponsApiService cardCouponsApiService;

    @Autowired
    private MemberNewService memberNewService;

    @Autowired
    private UserConsumeDAO userConsumeDAO;

    @Autowired
    private RedisCacheUtil redisCacheUtil;

    @Autowired
    private MemberLogDAO memberLogDAO;

    @Override
    public Map< String,Object > findMemberByERP( Integer busId, Integer shopId, String cardNo ) {
	Map< String,Object > map = new HashMap<>();
	String cardNodecrypt = "";
	try {
	    try {
		// 如果手动输入 会出现异常
		cardNodecrypt = EncryptUtil.decrypt( memberConfig.getCardNoKey(), cardNo );
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
			map.put( "result", false );
			map.put( "msg", "二维码已超时" );
			return map;
		    }
		}
	    }

	    MemberCard card = null;

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

	    String dictValue = dictService.getDictRuturnValue( "A001", busId );
	    if ( CommonUtil.isNotEmpty( card ) && CommonUtil.isEmpty( dictValue ) ) {
		map.put( "result", false );
		map.put( "msg", "请扫码支付" );
		return map;
	    }

	    if ( CommonUtil.isEmpty( card ) ) {
		map.put( "result", false );
		map.put( "msg", "会员卡不存在" );
		return map;
	    } else if ( card.getCardStatus() == 1 ) {
		map.put( "result", false );
		map.put( "msg", "会员卡被拉黑" );
		return map;
	    } else {
		List< Map< String,Object > > cards = cardMapper.findCardById( card.getMcId() );
		MemberGiverule giveRule = giveRuleMapper.selectById( card.getGrId() );
		map.put( "result", true );
		map.put( "headimg", member.getHeadimgurl() );
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
	} catch ( Exception e ) {
	    LOG.error( "ERP查询会员信息异常", e );
	    map.put( "result", false );
	    map.put( "msg", "查询会员数据失败" );
	}
	return map;
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

    public Map< String,Object > erpCountMoney( String mallAllEntityQuery, String param ) {
	Map< String,Object > map = new HashMap<>();
	try {
	    MallAllEntityQuery mallQuery = JSON.toJavaObject( JSON.parseObject( mallAllEntityQuery ), MallAllEntityQuery.class );

	    JSONObject jsonObject = JSONObject.parseObject( param );

	    MallNotShopEntity mallNotShopEntity = new MallNotShopEntity();

	    mallNotShopEntity.setDerateMoney(mallQuery.getDerateMoney());

	    if(CommonUtil.isNotEmpty( jsonObject.get( "useCoupon" )  )) {
		mallNotShopEntity.setUseCoupon( CommonUtil.toInteger( jsonObject.get( "useCoupon" ) ) );
	    }
	    if(CommonUtil.isNotEmpty( jsonObject.get( "couponType" )  )) {
		mallNotShopEntity.setCouponType( CommonUtil.toInteger( jsonObject.get( "couponType" ) ) );
	    }
	    if(CommonUtil.isNotEmpty( jsonObject.get( "coupondId" )  )) {
		mallNotShopEntity.setCoupondId( CommonUtil.toInteger( jsonObject.get( "coupondId" ) ) );
	    }
	    mallNotShopEntity.setShopId( mallQuery.getShopId() );
	    mallNotShopEntity.setTotalMoney( mallQuery.getTotalMoney() );
	    if(CommonUtil.isNotEmpty( jsonObject.get( "memberId" )  )) {
		mallNotShopEntity.setMemberId( CommonUtil.toInteger( jsonObject.get( "memberId" ) ) );
	    }
	    if(CommonUtil.isNotEmpty( jsonObject.get( "useFenbi" )  )) {
		mallNotShopEntity.setUseFenbi( CommonUtil.toInteger( jsonObject.get( "useFenbi" ) ) );
	    }
	    if(CommonUtil.isNotEmpty( jsonObject.get( "userJifen" )  )) {
		mallNotShopEntity.setUserJifen( CommonUtil.toInteger( jsonObject.get( "userJifen" ) ) );
	    }

	    Map< Integer,MallEntity > mallMap = new HashMap<>();
	    MallEntity mall = null;
	    for ( MallEntityQuery m : mallQuery.getMalls() ) {
		mall = new MallEntity();
		mall.setMallId( m.getMallId() );
		mall.setNumber( m.getNumber() );
		mall.setTotalMoneyOne( m.getTotalMoneyOne() );
		mall.setTotalMoneyAll( m.getTotalMoneyAll() );
		mall.setUserCard( m.getUserCard() );
		mall.setUseCoupon( m.getUseCoupon() );
		mall.setUseFenbi( m.getUseFenbi() );
		mall.setUserJifen( m.getUserJifen() );
		mall.setUseLeague( m.getUseLeague() );
		mallMap.put( mall.getMallId(), mall );
	    }
	    mallNotShopEntity.setMalls( mallMap );
	    mallNotShopEntity = memberCountMoneyApiService.mallSkipNotShopCount( mallNotShopEntity );
	    map.put( "result", true );
	    map.put( "mallNotShopEntity", mallNotShopEntity );
	} catch ( Exception e ) {
	    LOG.error( "ERP计算失败", e );
	    map.put( "result", false );
	    map.put( "msg", "计算失败" );
	}
	return map;
    }

    /**
     * erp 储值卡或现金支付(缺赠送)
     *
     * @param mallAllEntityQuery
     * @param param
     *
     * @return
     */
    @Transactional
    public void erpChuzhiPayMent( String mallAllEntityQuery, String param ) throws BusinessException {
	try {
	    MallAllEntityQuery mallQuery = JSON.toJavaObject( JSON.parseObject( mallAllEntityQuery ), MallAllEntityQuery.class );
	    JSONObject jsonObject = JSONObject.parseObject( param );
	    Integer payType = CommonUtil.toInteger( jsonObject.get( "payType" ) );
	    Integer visitor = CommonUtil.toInteger( jsonObject.get( "visitor" ) );
	    MallNotShopEntity mallNotShopEntity = new MallNotShopEntity();
	    mallNotShopEntity.setTotalMoney( mallQuery.getTotalMoney() );
	    mallNotShopEntity.setOrderCode( mallQuery.getOrderCode() );
	    mallNotShopEntity.setVisitor( visitor );
	    mallNotShopEntity.setPayType( payType );
	    mallNotShopEntity.setDerateMoney(  mallQuery.getDerateMoney());
	    mallNotShopEntity.setBalanceMoney( BigDecimalUtil.sub(mallQuery.getTotalMoney(), mallQuery.getDerateMoney() ) );
	    if ( visitor == 1 ) {
		//游客 直接通知
		Double payMoney = CommonUtil.toDouble( jsonObject.get( "payMoney" ) );
		if ( CommonUtil.isEmpty( payMoney ) ) {
		    throw new BusinessException( ResponseMemberEnums.LESS_THAN_CASH );
		}
		if ( payMoney < mallNotShopEntity.getBalanceMoney() ) {
		    throw new BusinessException( ResponseMemberEnums.LESS_THAN_CASH );
		}
		Object obj=redisCacheUtil.get(mallNotShopEntity.getOrderCode() );
		if(CommonUtil.isNotEmpty(  obj)){
		    redisCacheUtil.del( mallNotShopEntity.getOrderCode()  );
		}
		redisCacheUtil.set( mallNotShopEntity.getOrderCode(), JSON.toJSONString( mallNotShopEntity ), 600 );
		//通知
		String tongzhi=null;
		if(mallQuery.getJumphttpPOST()==0){
		    tongzhi= SignHttpUtils.postByHttp( mallQuery.getSuccessNoticeUrl(),mallQuery.getOrderCode(),mallQuery.getSign() );
		}else{
		    tongzhi= SignHttpUtils.WxmppostByHttp( mallQuery.getSuccessNoticeUrl(),mallQuery.getOrderCode(),mallQuery.getSign() );
		}
		JSONObject tongzhiJson=JSONObject.parseObject( tongzhi );
		if(CommonUtil.toInteger( tongzhiJson.get( "code" ) )!=0){
		    MemberLog ml=new MemberLog();
		    ml.setLogtxt( "ERP计算通知回调异常:"+ JSONObject.toJSONString( mallNotShopEntity ));
		    memberLogDAO.insert( ml );
		}
		return;
	    }

	    if ( payType != 5 && payType != 10 ) {
		throw new BusinessException( ResponseMemberEnums.NOT_PAY_TYPE );
	    }

	    mallNotShopEntity.setUseCoupon( CommonUtil.toInteger( jsonObject.get( "useCoupon" ) ) );
	    mallNotShopEntity.setCouponType( CommonUtil.toInteger( jsonObject.get( "couponType" ) ) );
	    mallNotShopEntity.setCoupondId( CommonUtil.toInteger( jsonObject.get( "coupondId" ) ) );
	    mallNotShopEntity.setShopId( mallQuery.getShopId() );
	    mallNotShopEntity.setTotalMoney( mallQuery.getTotalMoney() );
	    mallNotShopEntity.setMemberId( CommonUtil.toInteger( jsonObject.get( "memberId" ) ) );
	    mallNotShopEntity.setUseFenbi( CommonUtil.toInteger( jsonObject.get( "useFenbi" ) ) );
	    mallNotShopEntity.setUserJifen( CommonUtil.toInteger( jsonObject.get( "userJifen" ) ) );

	    Map< Integer,MallEntity > mallMap = new HashMap<>();
	    MallEntity mall = null;
	    for ( MallEntityQuery m : mallQuery.getMalls() ) {
		mall = new MallEntity();
		mall.setMallId( m.getMallId() );
		mall.setNumber( m.getNumber() );
		mall.setTotalMoneyOne( m.getTotalMoneyOne() );
		mall.setTotalMoneyAll( m.getTotalMoneyAll() );
		mall.setUserCard( m.getUserCard() );
		mall.setUseCoupon( m.getUseCoupon() );
		mall.setUseFenbi( m.getUseFenbi() );
		mall.setUserJifen( m.getUserJifen() );
		mall.setUseLeague( m.getUseLeague() );
		mallMap.put( mall.getMallId(), mall );
	    }
	    mallNotShopEntity.setMalls( mallMap );
	    mallNotShopEntity = memberCountMoneyApiService.mallSkipNotShopCount( mallNotShopEntity );


	    if ( payType == 10 ) {
		//现金支付
		Double payMoney = CommonUtil.toDouble( jsonObject.get( "payMoney" ) );
		if ( CommonUtil.isEmpty( payMoney ) || payMoney < mallNotShopEntity.getBalanceMoney() ) {
		    throw new BusinessException( ResponseMemberEnums.LESS_THAN_CASH );
		}
	    }

	    //增加会员交易记录
	    Member member = memberDAO.selectById( mallNotShopEntity.getMemberId() );
	    UserConsume uc = new UserConsume();
	    uc.setBusUserId( member.getBusId() );
	    uc.setMemberId( member.getId() );
	    if ( CommonUtil.isNotEmpty( member.getMcId() ) ) {
		uc.setMcId( member.getMcId() );
		MemberCard card = memberCardDAO.selectById( member.getMcId() );
		uc.setCtId( card.getCtId() );
		uc.setGtId( card.getGtId() );

		if ( payType == 5 ) {
		    //储值卡支付
		    if ( card.getMoney() < mallNotShopEntity.getBalanceMoney() ) {
			throw new BusinessException( ResponseMemberEnums.MEMBER_LESS_MONEY );
		    }
		    //修改
		    MemberCard upCard = new MemberCard();
		    upCard.setMcId( card.getMcId() );
		    Double balance = card.getMoney() - mallNotShopEntity.getBalanceMoney();
		    upCard.setMoney( balance );
		    memberCardDAO.updateById( upCard );
		    uc.setBalance( balance );
		    uc.setPayStatus( 1 );
		}
	    }
	    uc.setRecordType( 2 );
	    uc.setUcType( mallQuery.getUcType() );
	    uc.setTotalMoney( mallNotShopEntity.getTotalMoney() );
	    uc.setIntegral( mallNotShopEntity.getJifenNum() );
	    uc.setFenbi( mallNotShopEntity.getFenbiNum() );
	    uc.setDiscountMoney( mallNotShopEntity.getBalanceMoney() );
	    uc.setPaymentType( payType );

	    uc.setOrderCode( mallQuery.getOrderCode() );
	    uc.setStoreId( mallNotShopEntity.getShopId() );
	    if ( mallNotShopEntity.getUseCoupon() == 1 ) {
		uc.setCardType( mallNotShopEntity.getCouponType() );
		uc.setDisCountdepict( mallNotShopEntity.getCodes() );
		//优惠券核销
		if ( mallNotShopEntity.getCouponType() == 0 ) {
		    //微信优惠券
		    WxPublicUsers wxPublicUsers = wxPublicUsersMapper.selectByUserId( member.getBusId() );
		    cardCouponsApiService.wxCardReceive( wxPublicUsers.getId(), mallNotShopEntity.getCodes() );
		} else {
		    //多粉优惠券
		    Map< String,Object > params = new HashMap<>();
		    params.put( "codes", mallNotShopEntity.getCodes() );
		    params.put( "storeId", mallNotShopEntity.getShopId() );
		    cardCouponsApiService.verificationCard_2( params );
		}
	    }
	    uc.setDataSource( 0 );
	    uc.setIsend( 1 );
	    uc.setIsendDate( new Date() );

	    boolean flag = false;
	    Member m1 = new Member();
	    m1.setId( member.getId() );
	    //粉币
	    if ( mallNotShopEntity.getCanUsefenbi() == 1 ) {
		Double fansCurrency = member.getFansCurrency() - mallNotShopEntity.getFenbiNum();
		m1.setFansCurrency( fansCurrency );
		memberNewService.saveCardRecordNew( member.getMcId(), 3, mallNotShopEntity.getFenbiNum() + "粉币", "消费抵扣粉币", member.getBusId(), fansCurrency + "", 0, 0 );
		flag = true;

		//归还商家粉币
		memberCommonService.guihuiBusUserFenbi( member.getBusId(), mallNotShopEntity.getFenbiNum() );
	    }
	    //积分
	    if ( mallNotShopEntity.getCanUseJifen() == 1 ) {
		Integer jifen = member.getIntegral() - mallNotShopEntity.getJifenNum();
		m1.setIntegral( jifen );
		memberNewService.saveCardRecordNew( member.getMcId(), 2, mallNotShopEntity.getJifenNum() + "积分", "消费抵扣积分", member.getBusId(), jifen + "", 0,
				mallNotShopEntity.getJifenNum() );
		flag = true;
	    }
	    if ( flag ) {
		memberDAO.updateById( m1 );
	    }

	    //添加会员交易记录
	    if ( CommonUtil.isNotEmpty( member.getMcId() ) ) {
		memberNewService.saveCardRecordNew( member.getMcId(), 1, mallNotShopEntity.getBalanceMoney() + "元", "消费", member.getBusId(), uc.getBalance() + "", 0, 0 );
	    }
	    UserConsume u=userConsumeDAO.findByOrderCode1( uc.getOrderCode() );
	    if(CommonUtil.isEmpty( u )){
		userConsumeDAO.insert( uc );
	    }else{
		uc.setId( u.getId() );
		userConsumeDAO.updateById( uc );
	    }

	    //赠送
	    //TODO

	    //通知
	    Object obj=redisCacheUtil.get(mallNotShopEntity.getOrderCode() );
	    if(CommonUtil.isNotEmpty(  obj)){
		redisCacheUtil.del( mallNotShopEntity.getOrderCode()  );
	    }
	    redisCacheUtil.set( mallNotShopEntity.getOrderCode(), JSON.toJSONString( mallNotShopEntity ), 600 );
	    String tongzhi=null;
	    if(mallQuery.getJumphttpPOST()==0){
		tongzhi= SignHttpUtils.postByHttp( mallQuery.getSuccessNoticeUrl(),mallQuery.getOrderCode(),mallQuery.getSign() );
	    }else{
		tongzhi= SignHttpUtils.WxmppostByHttp( mallQuery.getSuccessNoticeUrl(),mallQuery.getOrderCode(),mallQuery.getSign() );
	    }
	    JSONObject tongzhiJson=JSONObject.parseObject( tongzhi );
	    if(CommonUtil.toInteger( tongzhiJson.get( "code" ) )!=0){
		MemberLog ml=new MemberLog();
		ml.setLogtxt( "ERP计算通知回调异常:"+ JSONObject.toJSONString( mallNotShopEntity ));
		memberLogDAO.insert( ml );
	    }

	} catch ( BusinessException e ) {
	    throw new BusinessException( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    LOG.error( "ERP计算失败", e );
	    throw new BusinessException( ResponseEnums.ERROR );
	}
    }

    /**
     * erp 扫码支付支付
     *
     * @param mallAllEntityQuery
     * @param param
     *
     * @return
     */
    @Transactional
    public Map< String,Object > saomaPayMent( String mallAllEntityQuery, String param ) throws BusinessException {
	Map< String,Object > map = new HashMap<>();
	try {
	    MallAllEntityQuery mallQuery = JSON.toJavaObject( JSON.parseObject( mallAllEntityQuery ), MallAllEntityQuery.class );
	    JSONObject jsonObject = JSONObject.parseObject( param );
	    Integer visitor = CommonUtil.toInteger( jsonObject.get( "visitor" ) );
	    MallNotShopEntity mallNotShopEntity = new MallNotShopEntity();
	    mallNotShopEntity.setOrderCode( mallQuery.getOrderCode() );
	    mallNotShopEntity.setVisitor( visitor );
	    mallNotShopEntity.setShopId( mallQuery.getShopId() );
	    mallNotShopEntity.setTotalMoney( mallQuery.getTotalMoney() );
	    mallNotShopEntity.setDerateMoney(  mallQuery.getDerateMoney());
	    mallNotShopEntity.setBalanceMoney( BigDecimalUtil.sub(mallQuery.getTotalMoney(), mallQuery.getDerateMoney() ) );
	    if(visitor==0) {
		mallNotShopEntity.setUseCoupon( CommonUtil.toInteger( jsonObject.get( "useCoupon" ) ) );
		mallNotShopEntity.setCouponType( CommonUtil.toInteger( jsonObject.get( "couponType" ) ) );
		mallNotShopEntity.setCoupondId( CommonUtil.toInteger( jsonObject.get( "coupondId" ) ) );
		mallNotShopEntity.setMemberId( CommonUtil.toInteger( jsonObject.get( "memberId" ) ) );
		mallNotShopEntity.setUseFenbi( CommonUtil.toInteger( jsonObject.get( "useFenbi" ) ) );
		mallNotShopEntity.setUserJifen( CommonUtil.toInteger( jsonObject.get( "userJifen" ) ) );
	    }

	    Map< Integer,MallEntity > mallMap = new HashMap<>();
	    MallEntity mall = null;
	    for ( MallEntityQuery m : mallQuery.getMalls() ) {
		mall = new MallEntity();
		mall.setMallId( m.getMallId() );
		mall.setNumber( m.getNumber() );
		mall.setTotalMoneyOne( m.getTotalMoneyOne() );
		mall.setTotalMoneyAll( m.getTotalMoneyAll() );
		mall.setUserCard( m.getUserCard() );
		mall.setUseCoupon( m.getUseCoupon() );
		mall.setUseFenbi( m.getUseFenbi() );
		mall.setUserJifen( m.getUserJifen() );
		mall.setUseLeague( m.getUseLeague() );
		mallMap.put( mall.getMallId(), mall );
	    }
	    mallNotShopEntity.setMalls( mallMap );
	    if(visitor==0) {
		mallNotShopEntity = memberCountMoneyApiService.mallSkipNotShopCount( mallNotShopEntity );
	    }

	    if(visitor==0) {
		//增加会员交易记录
		Member member = memberDAO.selectById( mallNotShopEntity.getMemberId() );
		UserConsume uc = new UserConsume();
		uc.setBusUserId( member.getBusId() );
		uc.setMemberId( member.getId() );
		if ( CommonUtil.isNotEmpty( member.getMcId() ) ) {
		    uc.setMcId( member.getMcId() );
		    MemberCard card = memberCardDAO.selectById( member.getMcId() );
		    uc.setCtId( card.getCtId() );
		    uc.setGtId( card.getGtId() );
		}
		uc.setRecordType( 2 );
		uc.setUcType( mallQuery.getUcType() );
		uc.setTotalMoney( mallNotShopEntity.getTotalMoney() );
		uc.setIntegral( mallNotShopEntity.getJifenNum() );
		uc.setFenbi( mallNotShopEntity.getFenbiNum() );
		uc.setDiscountMoney( mallNotShopEntity.getBalanceMoney() );

		uc.setOrderCode( mallQuery.getOrderCode() );
		uc.setStoreId( mallNotShopEntity.getShopId() );
		if ( mallNotShopEntity.getUseCoupon() == 1 ) {
		    uc.setCardType( mallNotShopEntity.getCouponType() );
		    uc.setDisCountdepict( mallNotShopEntity.getCodes() );
		}
		uc.setDataSource( 0 );
		UserConsume u=userConsumeDAO.findByOrderCode1( uc.getOrderCode() );
		if(CommonUtil.isEmpty( u )){
		    userConsumeDAO.insert( uc );
		}else{
		    uc.setId( u.getId() );
		    userConsumeDAO.updateById( uc );
		}

	    }
	    Object obj=redisCacheUtil.get( mallNotShopEntity.getOrderCode() );
	    if(CommonUtil.isNotEmpty(  obj)){
		redisCacheUtil.del( mallNotShopEntity.getOrderCode()  );
	    }
	    redisCacheUtil.set( mallNotShopEntity.getOrderCode(), JSON.toJSONString( mallNotShopEntity ), 600 );

	    Map<String,Object> noticeMap=new HashMap<>(  );
	    noticeMap.put( "successNoticeUrl",mallQuery.getSuccessNoticeUrl() );
	    noticeMap.put( "sign",mallQuery.getSign() );
	    noticeMap.put( "jumphttpPOST",mallQuery.getJumphttpPOST() );
	    redisCacheUtil.set("Memeber_ERP_"+mallNotShopEntity.getOrderCode(), JSON.toJSONString( noticeMap ),600 );

	    WxPublicUsers wxPublicUsers=  wxPublicUsersMapper.selectByUserId( mallQuery.getBusId() );
	    String notityUrl=memberConfig.getWebHome()+"/erpCount/79B4DE7C/successPay.do";
	    String url="";
	    if(visitor==1){
		url = memberConfig.getWxmp_home() + "/pay/B02A45A5/79B4DE7C/createPayQR.do"
				+ "?totalFee=" +mallNotShopEntity.getBalanceMoney()+"&model=51&busId="+mallQuery.getBusId()+"&orderNum="+mallNotShopEntity.getOrderCode()
				+"&memberId=0&desc=支付&notifyUrl="+notityUrl+"&appid="+wxPublicUsers.getAppid()+"&appidType=0&isSendMessage=0&payWay=0&sourceType=1";
	    }else{
		url = memberConfig.getWxmp_home() + "/pay/B02A45A5/79B4DE7C/createPayQR.do"
				+ "?totalFee=" +mallNotShopEntity.getBalanceMoney()+"&model=51&busId="+mallQuery.getBusId()+"&orderNum="+mallNotShopEntity.getOrderCode()
				+"&memberId="+mallNotShopEntity.getMemberId()+"&desc=支付&notifyUrl="+notityUrl+"&appid="+wxPublicUsers.getAppid()+"&appidType=0&isSendMessage=0&payWay=0&sourceType=1";
	    }
	    map.put( "saomaoPayUrl", url );
	    map.put( "code", 0);
	    return map;
	} catch ( BusinessException e ) {
	    throw new BusinessException( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    LOG.error( "ERP计算失败", e );
	    throw new BusinessException( ResponseEnums.ERROR );
	}
    }

    @Transactional
    public Map<String,Object> saomaQianBaoPay(String mallAllEntityQuery,
		    String param){
        Map<String,Object> map=new HashMap<>(  );
	try {
	    MallAllEntityQuery mallQuery = JSON.toJavaObject( JSON.parseObject( mallAllEntityQuery ), MallAllEntityQuery.class );
	    JSONObject jsonObject = JSONObject.parseObject( param );
	    Integer visitor = CommonUtil.toInteger( jsonObject.get( "visitor" ) );
	    MallNotShopEntity mallNotShopEntity = new MallNotShopEntity();
	    mallNotShopEntity.setOrderCode( mallQuery.getOrderCode() );
	    mallNotShopEntity.setVisitor( visitor );
	    mallNotShopEntity.setShopId( mallQuery.getShopId() );
	    mallNotShopEntity.setTotalMoney( mallQuery.getTotalMoney() );
	    mallNotShopEntity.setDerateMoney(  mallQuery.getDerateMoney());
	    mallNotShopEntity.setBalanceMoney( BigDecimalUtil.sub(mallQuery.getTotalMoney(), mallQuery.getDerateMoney() ) );
	    if(visitor==0) {
		mallNotShopEntity.setUseCoupon( CommonUtil.toInteger( jsonObject.get( "useCoupon" ) ) );
		mallNotShopEntity.setCouponType( CommonUtil.toInteger( jsonObject.get( "couponType" ) ) );
		mallNotShopEntity.setCoupondId( CommonUtil.toInteger( jsonObject.get( "coupondId" ) ) );
		mallNotShopEntity.setMemberId( CommonUtil.toInteger( jsonObject.get( "memberId" ) ) );
		mallNotShopEntity.setUseFenbi( CommonUtil.toInteger( jsonObject.get( "useFenbi" ) ) );
		mallNotShopEntity.setUserJifen( CommonUtil.toInteger( jsonObject.get( "userJifen" ) ) );
	    }

	    Map< Integer,MallEntity > mallMap = new HashMap<>();
	    MallEntity mall = null;
	    for ( MallEntityQuery m : mallQuery.getMalls() ) {
		mall = new MallEntity();
		mall.setMallId( m.getMallId() );
		mall.setNumber( m.getNumber() );
		mall.setTotalMoneyOne( m.getTotalMoneyOne() );
		mall.setTotalMoneyAll( m.getTotalMoneyAll() );
		mall.setUserCard( m.getUserCard() );
		mall.setUseCoupon( m.getUseCoupon() );
		mall.setUseFenbi( m.getUseFenbi() );
		mall.setUserJifen( m.getUserJifen() );
		mall.setUseLeague( m.getUseLeague() );
		mallMap.put( mall.getMallId(), mall );
	    }
	    mallNotShopEntity.setMalls( mallMap );
	    mallNotShopEntity = memberCountMoneyApiService.mallSkipNotShopCount( mallNotShopEntity );

	    if(visitor==0) {
		//增加会员交易记录
		Member member = memberDAO.selectById( mallNotShopEntity.getMemberId() );
		UserConsume uc = new UserConsume();
		uc.setBusUserId( member.getBusId() );
		uc.setMemberId( member.getId() );
		if ( CommonUtil.isNotEmpty( member.getMcId() ) ) {
		    uc.setMcId( member.getMcId() );
		    MemberCard card = memberCardDAO.selectById( member.getMcId() );
		    uc.setCtId( card.getCtId() );
		    uc.setGtId( card.getGtId() );
		}
		uc.setRecordType( 2 );
		uc.setUcType( mallQuery.getUcType() );
		uc.setTotalMoney( mallNotShopEntity.getTotalMoney() );

		if ( mallNotShopEntity.getUseCoupon() == 1 ) {
		    uc.setCardType( mallNotShopEntity.getCouponType() );
		    uc.setDisCountdepict( mallNotShopEntity.getCodes() );
		    //优惠券核销
		    if ( mallNotShopEntity.getCouponType() == 0 ) {
			//微信优惠券
			WxPublicUsers wxPublicUsers = wxPublicUsersMapper.selectByUserId( member.getBusId() );
			cardCouponsApiService.wxCardReceive( wxPublicUsers.getId(), mallNotShopEntity.getCodes() );
		    } else {
			//多粉优惠券
			Map< String,Object > params = new HashMap<>();
			params.put( "codes", mallNotShopEntity.getCodes() );
			params.put( "storeId", mallNotShopEntity.getShopId() );
			cardCouponsApiService.verificationCard_2( params );
		    }
		}
		uc.setRecordType( 2 );
		uc.setUcType( mallQuery.getUcType() );
		uc.setTotalMoney( mallNotShopEntity.getTotalMoney() );
		uc.setDiscountMoney( mallNotShopEntity.getBalanceMoney() );
		uc.setPaymentType( 1 );
		uc.setPayStatus( 1 );
		uc.setIsend( 1 );
		uc.setIsendDate( new Date() );
		boolean flag = false;
		Member m1 = new Member();
		m1.setId( member.getId() );
		//粉币
		if ( mallNotShopEntity.getCanUsefenbi() == 1 ) {
		    uc.setFenbi( mallNotShopEntity.getFenbiNum() );
		    Double fansCurrency = member.getFansCurrency() - mallNotShopEntity.getFenbiNum();
		    m1.setFansCurrency( fansCurrency );
		    memberNewService.saveCardRecordNew( member.getMcId(), 3, mallNotShopEntity.getFenbiNum() + "粉币", "消费抵扣粉币", member.getBusId(), fansCurrency + "", 0, 0 );
		    flag = true;

		    //归还商家粉币
		    memberCommonService.guihuiBusUserFenbi( member.getBusId(), mallNotShopEntity.getFenbiNum() );
		}
		//积分
		if ( mallNotShopEntity.getCanUseJifen() == 1 ) {
		    uc.setIntegral(mallNotShopEntity.getJifenNum() );
		    Integer jifen = member.getIntegral() - mallNotShopEntity.getJifenNum();
		    m1.setIntegral( jifen );
		    memberNewService.saveCardRecordNew( member.getMcId(), 2, mallNotShopEntity.getJifenNum() + "积分", "消费抵扣积分", member.getBusId(), jifen + "", 0,
				    mallNotShopEntity.getJifenNum() );
		    flag = true;
		}
		if ( flag ) {
		    memberDAO.updateById( m1 );
		}

		//添加会员交易记录
		if ( CommonUtil.isNotEmpty( member.getMcId() ) ) {
		    memberNewService.saveCardRecordNew( member.getMcId(), 1, mallNotShopEntity.getBalanceMoney() + "元", "消费", member.getBusId(), uc.getBalance() + "", 0, 0 );
		}
		uc.setOrderCode( mallQuery.getOrderCode() );
		uc.setStoreId( mallNotShopEntity.getShopId() );
		uc.setDataSource( 0 );
		UserConsume u=userConsumeDAO.findByOrderCode1( uc.getOrderCode() );
		if(CommonUtil.isEmpty( u )){
		    userConsumeDAO.insert( uc );
		}else{
		    uc.setId( u.getId() );
		    userConsumeDAO.updateById( uc );
		}
	    }

	    Object obj=redisCacheUtil.get(mallNotShopEntity.getOrderCode() );
	    if(CommonUtil.isNotEmpty(  obj)){
		redisCacheUtil.del( mallNotShopEntity.getOrderCode()  );
	    }
	    redisCacheUtil.set( mallNotShopEntity.getOrderCode(), JSON.toJSONString( mallNotShopEntity ), 600 );


	    String auth_code=jsonObject.getString( "auth_code" );
	    String url = memberConfig.getWxmp_home() + SAOMA_QIANBAO_PAY+"?auth_code="+auth_code+"&body=支付&model=51&out_trade_no="+mallNotShopEntity.getOrderCode()
			    +"&total_fee="+ mallNotShopEntity.getBalanceMoney()+"&is_calculate=1";

	    net.sf.json.JSONObject json=HttpClienUtil.httpPost( url, null, false );
	    if("1".equals( CommonUtil.toString( json.get( "code" ) ) )){
		map.put( "code",0 );
		map.put( "msg","支付成功" );
		//通知

		String tongzhi=null;
		if(mallQuery.getJumphttpPOST()==0){
		    tongzhi= SignHttpUtils.postByHttp( mallQuery.getSuccessNoticeUrl(),mallQuery.getOrderCode(),mallQuery.getSign() );
		}else{
		    tongzhi= SignHttpUtils.WxmppostByHttp( mallQuery.getSuccessNoticeUrl(),mallQuery.getOrderCode(),mallQuery.getSign() );
		}
		JSONObject tongzhiJson=JSONObject.parseObject( tongzhi );
		if(CommonUtil.toInteger( tongzhiJson.get( "code" ) )!=0){
		    MemberLog ml=new MemberLog();
		    ml.setLogtxt( "ERP计算通知回调异常:"+ JSONObject.toJSONString( mallNotShopEntity ));
		    memberLogDAO.insert( ml );
		}
	    }else{
	        throw new BusinessException(CommonUtil.toInteger(json.get( "code" )),CommonUtil.toString(json.get( "msg" )));
	    }
	    return map;
	} catch ( BusinessException e ) {
	    throw new BusinessException( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    LOG.error( "ERP计算失败", e );
	    throw new BusinessException( ResponseEnums.ERROR );
	}
    }

    /**
     * 支付成功回调
     * @param params
     */
    public Map<String,Object> successPay(Map<String,Object> params){
        Map<String,Object> map=new HashMap<>(  );
        try {
	    String orderCode = CommonUtil.toString( params.get( "out_trade_no" ) );
	    UserConsume uc = userConsumeDAO.findByOrderCode1( orderCode );
	    if ( CommonUtil.isNotEmpty( uc ) ) {
	        if(uc.getPayStatus()==1){
		    map.put( "code",0 );
		    map.put( "msg","处理成功" );
		    return map;
		}
		//通知地址
		Member member = memberDAO.selectById( uc.getMemberId() );
		boolean flag = false;
		Member m1 = new Member();
		m1.setId( member.getId() );
		//粉币
		if ( uc.getFenbi() > 0 ) {
		    Double fansCurrency = member.getFansCurrency() - uc.getFenbi();
		    m1.setFansCurrency( fansCurrency );
		    memberNewService.saveCardRecordNew( member.getMcId(), 3, uc.getFenbi() + "粉币", "消费抵扣粉币", member.getBusId(), fansCurrency + "", 0, 0 );
		    flag = true;

		    //归还商家粉币
		    memberCommonService.guihuiBusUserFenbi( member.getBusId(), uc.getFenbi() );
		}
		//积分
		if ( uc.getIntegral() > 0 ) {
		    Integer jifen = member.getIntegral() - uc.getIntegral();
		    m1.setIntegral( jifen );
		    memberNewService.saveCardRecordNew( member.getMcId(), 2, uc.getIntegral() + "积分", "消费抵扣积分", member.getBusId(), jifen + "", 0, -uc.getIntegral() );
		    flag = true;
		}
		if ( flag ) {
		    memberDAO.updateById( m1 );
		}
		if ( uc.getCardType() == -1 ) {
		    //卡券类型 -1未使用优惠券 0微信卡券 1多粉卡券
		    if ( uc.getCardType() == 0 ) {
			//微信优惠券
			WxPublicUsers wxPublicUsers = wxPublicUsersMapper.selectByUserId( member.getBusId() );
			cardCouponsApiService.wxCardReceive( wxPublicUsers.getId(), uc.getDisCountdepict() );
		    } else {
			//多粉优惠券
			Map< String,Object > p = new HashMap<>();
			p.put( "codes", uc.getDisCountdepict() );
			p.put( "storeId", uc.getStoreId() );
			cardCouponsApiService.verificationCard_2( p );
		    }
		}
		//添加会员交易记录
		if ( CommonUtil.isNotEmpty( member.getMcId() ) ) {
		    memberNewService.saveCardRecordNew( member.getMcId(), 1, uc.getDiscountMoney() + "元", "消费", member.getBusId(), uc.getBalance() + "", 0, 0 );
		}
		//ToDo
		//修改uc支付状态
		//payType:0微信，1：支付宝 2：多粉钱包
		UserConsume u=new UserConsume();
		u.setId( uc.getId() );
		Integer payType=CommonUtil.toInteger( params.get( "payType" ) );
		if(payType==0){
		    payType=1;
		}else if(payType==1){
		    payType=0;
		}else if(payType==2){

		}
		u.setPaymentType( payType );
		u.setPayStatus( 1 );
		u.setIsend(1);
		u.setIsendDate(new Date(  ));
		userConsumeDAO.updateById( u );
	    }
	    //非游客
	    Object obj = redisCacheUtil.get( "Memeber_ERP_" + orderCode );
	    if(CommonUtil.isNotEmpty( obj )) {
		JSONObject json = JSON.parseObject( obj.toString() );
		String successNoticeUrl = CommonUtil.toString( json.get( "successNoticeUrl" ) );
		String sign = CommonUtil.toString( json.get( "sign" ) );
		Map<String,Object> successMap=new HashMap<>(  );
		successMap.put( "orderCode",orderCode );

		Integer jumphttpPOST=CommonUtil.toInteger( json.get( "jumphttpPOST" ) );

		//通知
		String tongzhi=null;
		if(jumphttpPOST==0){
		    tongzhi= SignHttpUtils.postByHttp( successNoticeUrl,successMap,sign );
		}else{
		    tongzhi= SignHttpUtils.WxmppostByHttp( successNoticeUrl,successMap,sign);
		}
		JSONObject tongzhiJson=JSONObject.parseObject( tongzhi );
		if(CommonUtil.toInteger( tongzhiJson.get( "code" ) )!=0) {
		    Object mallNotShopEntity = redisCacheUtil.get(orderCode );
		    MemberLog ml = new MemberLog();
		    ml.setLogtxt( "ERP计算通知回调异常:" + JSONObject.toJSONString( mallNotShopEntity ) );
		    memberLogDAO.insert( ml );
		}
		redisCacheUtil.del( "Memeber_ERP_" + orderCode );
	    }

	    String wxmpsignKey=memberConfig.getWxmpsignKey();
	    String socketUrl=memberConfig.getWxmp_home()+"/8A5DA52E/socket/getSocketApi.do";
	    Map<String,Object> socketMap=new HashMap<>(  );
	    socketMap.put( "pushName","member_count_"+orderCode );
	    socketMap.put( "pushMsg","支付成功" );
	    SignHttpUtils.WxmppostByHttp( socketUrl, socketMap, wxmpsignKey );  //推送
	    map.put( "code",0 );
	    map.put( "msg","处理成功" );
	}catch ( Exception e ){
            LOG.error( "支付成功回调处理异常",e );
	    map.put( "code",-1 );
	    map.put( "msg","支付成功回调处理异常" );
	}
	return map;

    }

}
