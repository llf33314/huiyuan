package com.gt.member.service.member.impl;

import com.gt.api.bean.session.Member;
import com.gt.api.enums.ResponseEnums;
import com.gt.api.util.SessionUtils;
import com.gt.duofencard.entity.DuofenCardGetNew;
import com.gt.duofencard.entity.DuofenCardNew;
import com.gt.duofencard.entity.DuofenCardPublish;
import com.gt.duofencard.entity.DuofenCardTime;
import com.gt.entityBo.MemberShopEntity;
import com.gt.member.dao.*;
import com.gt.member.dao.duofencard.DuofenCardGetNewDAO;
import com.gt.member.dao.duofencard.DuofenCardNewDAO;
import com.gt.member.dao.duofencard.DuofenCardPublishDAO;
import com.gt.member.dao.duofencard.DuofenCardTimeDAO;
import com.gt.member.entity.*;
import com.gt.member.enums.ResponseMemberEnums;
import com.gt.member.exception.BusinessException;
import com.gt.member.service.common.dict.DictService;
import com.gt.member.service.common.membercard.MemberCommonService;
import com.gt.member.service.common.membercard.RequestService;
import com.gt.member.service.member.DuofenCardNewPhoneService;
import com.gt.member.service.member.SystemMsgService;
import com.gt.member.util.CommonUtil;
import com.gt.member.util.DateTimeKit;
import com.gt.member.util.PropertiesUtil;
import com.gt.member.util.RedisCacheUtil;
import com.gt.util.entity.param.pay.SubQrPayParams;
import com.gt.util.entity.result.shop.WsWxShopInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by Administrator on 2018/1/16.
 */
@Service
@Slf4j
public class DuofenCardNewPhoneServiceImpl implements DuofenCardNewPhoneService {
    @Autowired
    private DuofenCardPublishDAO duofenCardPublishDAO;

    @Autowired
    private DuofenCardNewDAO duofenCardNewDAO;

    @Autowired
    private DuofenCardTimeDAO duofenCardTimeDAO;

    @Autowired
    private DuofenCardGetNewDAO duofenCardGetNewDAO;

    @Autowired
    private MemberEntityDAO memberEntityDAO;

    @Autowired
    private MemberCardDAO memberCardDAO;

    @Autowired
    private MemberGiveruleDAO memberGiveruleDAO;

    @Autowired
    private MemberCommonService memberCommonService;

    @Autowired
    private MemberGradetypeAssistantDAO memberGradetypeAssistantDAO;

    @Autowired
    private PublicParametersetDAO publicParameterSetMapper;

    @Autowired
    private DictService dictService;

    @Autowired
    private RequestService requestService;

    @Autowired
    private SystemMsgService systemMsgService;

    @Autowired
    private UserConsumeNewDAO userConsumeNewDAO;

    @Autowired
    private RedisCacheUtil redisCacheUtil;

    public List< Map< String,Object > > findPublishDuofenCard( Integer busId, Integer memberId, Map< String,Object > params ) {
	Integer page = CommonUtil.toInteger( params.get( "page" ) );
	Integer pageSize = 5;
	page = ( page - 1 ) * pageSize;
	List< Map< String,Object > > listMap = duofenCardPublishDAO.findPublishDuofenCard( busId, page, pageSize );
	//查询对于的领取数量
	List<Integer> cardIds=new ArrayList<>(  );
	for(Map< String,Object > map:listMap){
	    cardIds.add( CommonUtil.toInteger( map.get( "cardId" ) ) );
	}

	List<Map<String,Object>> countMaps=duofenCardGetNewDAO.countByCardIds(cardIds);
	List< Map< String,Object > > returnMap = new ArrayList<>();
	for ( Map< String,Object > map : listMap ) {
	    for(Map< String,Object > countMap:countMaps){
	        if(CommonUtil.toString( map.get( "cardId" ) ).equals( CommonUtil.toString( countMap.get( "cardId" ) ) )){
			Integer number=CommonUtil.toInteger( map.get( "number" ) );
			Integer count=CommonUtil.toInteger( map.get( "count" ) );
			if(number>count){
			    map.put( "lingquOver",0 );
			}else{
			    map.put( "lingquOver",1 );

			}
		}
	    }
	    if ( "0".equals( CommonUtil.toString( map.get( "isBuy" ) ) ) ) {
		if ( CommonUtil.isNotEmpty( memberId ) ) {
		    //粉丝已经授权或登录
		    //非购买查询领取数量是否已到领取最高值
		    if ( "1".equals( CommonUtil.toString( map.get( "isReceiveNum" ) ) ) ) {
			//统计现在数量
			Integer limitNum = CommonUtil.toInteger( map.get( "limitNum" ) );
			Integer cardId = CommonUtil.toInteger( map.get( "cardId" ) );
			Integer count = 0;
			if ( "0".equals( CommonUtil.toString( map.get( "limitType" ) ) ) ) {
			    // 0每人每天最多领取
			    Date date = DateTimeKit.parseDate( DateTimeKit.getDate() + " 00:00:00", "yyyy-MM-dd hh:mm:ss" );
			    count = duofenCardGetNewDAO.countDuofenCardGetByCardId( cardId, memberId, date );
			} else {
			    //1此券最多领取数量
			    count = duofenCardGetNewDAO.countDuofenCardGetByCardId( cardId, memberId, null );
			}
			if ( count >= limitNum ) {
			    map.put( "linquType", 1 );
			} else {
			    map.put( "linquType", 0 );
			}
		    }
		}
	    } else {
		//购买判断时价
		if ( "3".equals( CommonUtil.toString( map.get( "cardType" ) ) ) ) {
		    double buyMoney = memberCommonService.getBuyMoney( CommonUtil.toString( map.get( "giftBuyMoney" ) ), CommonUtil.toDouble( map.get( "buyMoney" ) ) );
		    map.put( "buyMoney", buyMoney );
		}
	    }
	    returnMap.add( map );
	}
	return returnMap;
    }

    public Map< String,Object > findDuofenCardNewByCardId( Integer cardId ) {
	Map< String,Object > map = new HashMap<>();
	DuofenCardNew duofenCardNew = duofenCardNewDAO.selectById( cardId );
	map.put( "duofenCardNew", duofenCardNew );
	if ( duofenCardNew.getUseTime() == 1 ) {
	    DuofenCardTime duofenCardTime = duofenCardTimeDAO.findDuofenCardTimeByCardId( cardId );
	    map.put( "duofenCardTime", duofenCardTime );
	}
	return map;
    }

    @Transactional
    public void getDuofenCard( Integer cardId, Integer memberId ) throws BusinessException {
	MemberEntity memberEntity = memberEntityDAO.selectById( memberId );
	if ( CommonUtil.isEmpty( memberEntity.getPhone() ) ) {
	    throw new BusinessException( ResponseMemberEnums.PLEASE_BINDING_PHONE );
	}
	DuofenCardPublish duofenCardPublish = duofenCardPublishDAO.findByCardId( cardId );
	DuofenCardNew duofenCardNew = duofenCardNewDAO.selectById( cardId );
	if ( duofenCardPublish.getIsBuy() == 0 ) {
	    //免费领取
	    DuofenCardGetNew dc = new DuofenCardGetNew();
	    dc.setCardId( cardId );
	    dc.setGetType( 0 );
	    dc.setIsbuy( 0 );
	    dc.setCode( CommonUtil.getDuofenCardCode( duofenCardNew.getBusId() ) );
	    dc.setGetDate( new Date() );
	    dc.setMemberId( memberId );
	    dc.setBusId( duofenCardNew.getBusId() );
	    if ( duofenCardNew.getType() == 0 ) {
		Date startTime = DateTimeKit.parse( duofenCardNew.getBeginTimestamp() + " 00:00:00", "yyyy-MM-dd hh:mm:ss" );
		dc.setStartTime( startTime );

		Date endTime = DateTimeKit.parse( duofenCardNew.getEndTimestamp() + " 23:59:59", "yyyy-MM-dd hh:mm:ss" );
		dc.setEndTime( endTime );
	    } else {
		Date currentTime = DateTimeKit.parse( DateTimeKit.getDate() + " 00:00:00", "yyyy-MM-dd hh:mm:ss" );
		Date startTime = DateTimeKit.addDate( currentTime, duofenCardNew.getFixedBeginTerm() );
		dc.setStartTime( startTime );

		Date currentTime1 = DateTimeKit.parse( DateTimeKit.getDate() + " 23:59:59", "yyyy-MM-dd hh:mm:ss" );
		Date endTime = DateTimeKit.addDate( currentTime1, duofenCardNew.getFixedTerm() );
		dc.setEndTime( endTime );
	    }
	    duofenCardGetNewDAO.insert( dc );
	} else {
	    throw new BusinessException( ResponseMemberEnums.PLESASE_BUY );
	}
    }

    /**
     * 购买优惠券信息查询
     *
     * @param cardId
     */
    public Map< String,Object > findBuyDuofenCardDetails( HttpServletRequest request, Integer cardId, Integer memberId ) {
	Map< String,Object > map = new HashMap<>();
	DuofenCardNew duofenCardNew = duofenCardNewDAO.selectById( cardId );
	map.put( "duofenCardNew", duofenCardNew );
	DuofenCardPublish duofenCardPublish = duofenCardPublishDAO.findByCardId( cardId );
	map.put( "duofenCardPublish", duofenCardPublish );

	MemberEntity memberEntity = memberEntityDAO.selectById( memberId );
	Integer type = CommonUtil.judgeBrowser( request );
	List< Map< String,Object > > payTypes = requestService.getPayType( memberEntity.getBusId(), type );
	if ( CommonUtil.isNotEmpty( memberEntity.getMcId() ) ) {
	    MemberCard memberCard = memberCardDAO.selectById( memberEntity.getMcId() );
	    if ( memberCard.getCardStatus() == 1 && memberCard.getIsChecked() == 0 ) {
		map.put( "usehuiyuanquanyi", 0 );
		map.put( "tishi", "亲,您会员卡未审核或被拉黑,请联系商家" );
	    } else {
		List< Map< String,Object > > cards = memberCardDAO.findCardById( memberCard.getMcId() );
		MemberGiverule giveRule = memberGiveruleDAO.selectById( memberCard.getGrId() );
		map.put( "usehuiyuanquanyi", 1 );
		map.put( "ctName", cards.get( 0 ).get( "ct_name" ) );
		map.put( "gradeName", cards.get( 0 ).get( "gt_grade_name" ) );
		map.put( "cardNo", memberCard.getCardNo() );
		if ( memberCard.getCtId() == 3 ) {
		    Map< String,Object > paytype = new HashMap<>();
		    paytype.put( "payType", 5 );  //支付宝支付
		    paytype.put( "name", "储值卡" );
		    payTypes.add( paytype );
		}
		map.put( "ctId", memberCard.getCtId() );
		map.put( "discount", giveRule.getGrDiscount() / 10.0 );
		map.put( "money", memberCard.getMoney() );
		map.put( "fans_currency", memberEntity.getFansCurrency() );
		map.put( "integral", memberEntity.getIntegral() );
		map.put( "memberId", memberEntity.getId() );

		if ( "1".equals( CommonUtil.toString( cards.get( 0 ).get( "isrecommend" ) ) ) ) {
		    //卡通副卡
		    if ( memberCard.getCtId() == 3 ) {
			MemberGradetypeAssistant memberGradetypeAssistant = memberGradetypeAssistantDAO
					.findAssistantBygtIdAndFuctId( memberEntity.getBusId(), memberCard.getGtId(), 2 );
			if ( CommonUtil.isNotEmpty( memberGradetypeAssistant ) ) {
			    //返回副卡折扣
			    map.put( "fuCardDiscount", memberGradetypeAssistant.getDiscount() );
			}
		    }
		}

		Double fenbiMoeny = memberCommonService.currencyCount( null, memberEntity.getFansCurrency() );
		map.put( "fenbiMoney", fenbiMoeny );
		map.put( "getFenbiMoeny", 10 );

		Double jifenMoeny = memberCommonService.integralCount( null, new Double( memberEntity.getIntegral() ), memberEntity.getBusId() );
		map.put( "jifenMoney", jifenMoeny );
		PublicParameterset ps = publicParameterSetMapper.findBybusId( memberEntity.getBusId() );
		if ( CommonUtil.isNotEmpty( ps ) ) {
		    map.put( "getJifenMoeny", ps.getStartMoney() );
		    map.put( "jifenRatio", ps.getIntegralRatio() );
		    map.put( "jifenStartMoney", ps.getStartMoney() );
		}

		SortedMap< String,Object > dict = dictService.getDict( "1058" );
		Double ratio = CommonUtil.toDouble( dict.get( "1" ) );
		map.put( "fenbiRatio", ratio );
		map.put( "fenbiStartMoney", 10 );

		MemberDate memberDate = memberCommonService.findMemeberDate( memberEntity.getBusId(), memberCard.getCtId() );
		if ( memberCard.getCtId() == 2 ) {
		    if ( CommonUtil.isNotEmpty( memberDate ) ) {
			map.put( "memberDiscount", memberDate.getDiscount() * giveRule.getGrDiscount() / 100.0 );
			map.put( "memberDate", 1 );
		    }
		}
	    }
	}
	map.put( "payTypes", payTypes );
	return map;
    }

    @Transactional
    public void buyDuofenCard( HttpServletRequest request,Integer cardId, Integer memberId, Map< String,Object > params ) throws BusinessException {
	try {
	    MemberEntity memberEntity = memberEntityDAO.selectById( memberId );
	    if ( CommonUtil.isEmpty( memberEntity.getPhone() ) ) {
		throw new BusinessException( ResponseMemberEnums.PLEASE_BINDING_PHONE );
	    }
	    DuofenCardPublish duofenCardPublish = duofenCardPublishDAO.findByCardId( cardId );
	    DuofenCardNew duofenCardNew = duofenCardNewDAO.selectById( cardId );
	    if ( duofenCardPublish.getIsBuy() == 0 ) {
		throw new BusinessException( ResponseMemberEnums.PLESASE_FREE );
	    }
	    UserConsumeNew uc = new UserConsumeNew();
	    uc.setBusId( memberEntity.getBusId() );
	    uc.setMemberId( memberId );
	    uc.setRecordType( 2 );
	    uc.setUcType( 201 );
	    WsWxShopInfo wsWxShopInfo= requestService.findMainShop( memberEntity.getBusId() );
	    if(CommonUtil.isNotEmpty( wsWxShopInfo )) {
		uc.setShopId( wsWxShopInfo.getId() );
	    }

	    uc.setOrderCode( CommonUtil.getMEOrderCode() );
	    if ( duofenCardNew.getCardType() == 3 ) {
		Double buyMoney = memberCommonService.getBuyMoney( duofenCardPublish.getGiftBuyMoney(), duofenCardPublish.getBuyMoney() );
		uc.setTotalMoney( buyMoney );
	    } else {
		uc.setTotalMoney( duofenCardPublish.getBuyMoney() );
	    }
	    MemberShopEntity m = new MemberShopEntity();
	    m.setTotalMoney( uc.getTotalMoney() );
	    m.setMemberId( memberId );
	    Integer usehuiyuanquanyi = CommonUtil.toInteger( params.get( "usehuiyuanquanyi" ) );
	    m.setUsehuiyuanquanyi( usehuiyuanquanyi );
	    Integer useFenbi = CommonUtil.toInteger( params.get( "useFenbi" ) );
	    m.setUseFenbi( useFenbi );
	    Integer useJifen = CommonUtil.toInteger( params.get( "useJifen" ) );
	    m.setUserJifen( useJifen );
	    //计算粉丝使用金额
	    m = memberCommonService.publicMemberCountMoney( m );

	    Double qianduanMoney = CommonUtil.toDouble( params.get( "qianduanMoney" ) );
	    if ( m.getBalanceMoney() != qianduanMoney ) {
		throw new BusinessException( ResponseMemberEnums.ERROR_COUNT );
	    }
	    uc.setDiscountAfterMoney( m.getBalanceMoney() );
	    uc.setDiscountMoney( m.getTotalMoney() - m.getBalanceMoney() );
	    uc.setIntegral( m.getJifenNum() );
	    uc.setFenbi( m.getFenbiNum().doubleValue() );
	    uc.setPayStatus( 0 );
	    uc.setIschongzhi( 0 );
	    Integer dataSource=memberCommonService.dataSource(request);
	    uc.setDataSource( dataSource );
	    uc.setDvId( cardId );
	    uc.setCardType(2);

	    MemberCard card = null;
	    if ( CommonUtil.isNotEmpty( memberEntity.getMcId() ) ) {
		card = memberCardDAO.selectById( memberEntity.getMcId() );
		if ( CommonUtil.isEmpty( card ) ) {
		    throw new BusinessException( ResponseMemberEnums.NOT_MEMBER_CAR );
		}
		uc.setCtId( card.getCtId() );
		uc.setGtId( card.getGtId() );
		uc.setMcId( card.getMcId() );
	    }

	    Integer payType = CommonUtil.toInteger( params.get( "payType" ) );
	    if ( usehuiyuanquanyi == 1 ) {
		if ( payType == 5 ) {
		    if ( card.getCtId() == 3 ) {
			if ( card.getMoney() < m.getBalanceMoney() ) {
			    throw new BusinessException( ResponseMemberEnums.MEMBER_LESS_MONEY );
			}
			double banlan = card.getMoney() - m.getBalanceMoney();
			MemberCard updateCard = new MemberCard();
			updateCard.setMcId( card.getMcId() );
			updateCard.setMoney( banlan );
			memberCardDAO.updateById( updateCard );
			MemberCardrecordNew memberCardrecordNew = memberCommonService
					.saveCardRecordOrderCodeNew( memberEntity.getId(), 1, m.getBalanceMoney(), "消费", memberEntity.getBusId(), banlan, uc.getOrderCode(), 0 );
			systemMsgService.sendChuzhiCard( memberEntity, memberCardrecordNew );
			uc.setBalance( banlan );
			uc.setPayStatus( 1 );

			Boolean flag=false;
			MemberEntity memberEntity1=new MemberEntity();
			//调用扣除积分和粉币
			if ( m.getUseFenbi() == 1 && m.getFenbiNum() > 0 ) {
			    Double fenbi = memberEntity.getFansCurrency() - m.getFenbiNum();
			    Integer code = requestService.getPowerApi( 1, memberEntity.getBusId(), m.getFenbiNum().doubleValue(), "消费抵扣粉币" );
			    if ( code == 0 ) {
				memberEntity1.setFansCurrency( fenbi );
				memberCommonService.saveCardRecordOrderCodeNew( memberEntity.getId(), 3, m.getFenbiNum().doubleValue(), "消费粉币", memberEntity.getBusId(), fenbi,
						uc.getOrderCode(), 0 );
				flag = true;
			    } else {
				throw new BusinessException( ResponseMemberEnums.ERROR_USER_DEFINED.getCode(), "调用粉币接口异常" );
			    }
			}
			//积分使用
			if ( m.getUserJifen() == 1 && m.getJifenNum() > 0 ) {
			    Integer jifenBanlan = memberEntity.getIntegral() - m.getJifenNum();
			    memberEntity1.setIntegral( jifenBanlan );
			    memberCommonService
					    .saveCardRecordOrderCodeNew( memberEntity.getId(), 2, m.getJifenNum().doubleValue(), "消费积分", memberEntity.getBusId(), jifenBanlan.doubleValue(),
							    uc.getOrderCode(), 0 );
			    flag = true;
			}
			if(flag){
			    memberEntity1.setId( memberId );
			    memberEntityDAO.updateById( memberEntity1 );
			}
		    }
		    //分配优惠券
		    DuofenCardGetNew dc = new DuofenCardGetNew();
		    dc.setCardId( cardId );
		    dc.setGetType( 0 );
		    dc.setIsbuy( 1 );
		    String duofenCardCode=CommonUtil.getDuofenCardCode( memberEntity.getBusId() );
		    uc.setDisCountdepict(duofenCardCode);

		    dc.setCode( duofenCardCode );
		    dc.setGetDate( new Date() );
		    dc.setMemberId( memberId );
		    dc.setBusId( duofenCardNew.getBusId() );
		    if ( duofenCardNew.getType() == 0 ) {
			Date startTime = DateTimeKit.parse( duofenCardNew.getBeginTimestamp() + " 00:00:00", "yyyy-MM-dd hh:mm:ss" );
			dc.setStartTime( startTime );

			Date endTime = DateTimeKit.parse( duofenCardNew.getEndTimestamp() + " 23:59:59", "yyyy-MM-dd hh:mm:ss" );
			dc.setEndTime( endTime );
		    } else {
			Date currentTime = DateTimeKit.parse( DateTimeKit.getDate() + " 00:00:00", "yyyy-MM-dd hh:mm:ss" );
			Date startTime = DateTimeKit.addDate( currentTime, duofenCardNew.getFixedBeginTerm() );
			dc.setStartTime( startTime );

			Date currentTime1 = DateTimeKit.parse( DateTimeKit.getDate() + " 23:59:59", "yyyy-MM-dd hh:mm:ss" );
			Date endTime = DateTimeKit.addDate( currentTime1, duofenCardNew.getFixedTerm() );
			dc.setEndTime( endTime );
		    }
		    duofenCardGetNewDAO.insert( dc );

		}
	    }
	    userConsumeNewDAO.insert( uc );

	    if(payType!=5){
	        //调用支付接口
		SubQrPayParams sub = new SubQrPayParams();
		sub.setTotalFee( uc.getDiscountAfterMoney() );
		sub.setModel( 54 );
		sub.setBusId( memberEntity.getBusId() );
		sub.setAppidType( 0 );
		sub.setOrderNum( uc.getOrderCode() );
		sub.setMemberId( uc.getMemberId() );
		sub.setDesc( "优惠券购买" );
		sub.setIsreturn( 1 );
		String returnUrl= PropertiesUtil.getWebHome()+"/html/phone/index.html#/home/"+memberEntity.getBusId();
		sub.setReturnUrl( returnUrl );
		String notifyUrl = PropertiesUtil.getWebHome() + "/memberNodoInterceptor/memberNotDo/paySuccess";
		sub.setNotifyUrl( notifyUrl );
		sub.setIsSendMessage( 0 );
		sub.setPayWay( payType );
		String url = requestService.payApi( sub );
		throw new BusinessException( ResponseMemberEnums.PLEASASE_BUY_URL.getCode(),url );
	    }


	} catch ( BusinessException e ) {
	    throw e;
	} catch ( Exception e ) {
	    throw new BusinessException( ResponseEnums.ERROR );
	}

    }


    public Map<String,Object> myDuofenCard(Integer memberId){

	return null;
    }


    public Map<String,Object> useDuofenCardByCardId(Integer duofenCardGetId,Integer memberId){
        Map<String,Object> map=new HashMap<>(  );
        DuofenCardGetNew duofenCardGetNew=duofenCardGetNewDAO.selectById( duofenCardGetId );
        Integer cardId=duofenCardGetNew.getCardId();
	DuofenCardPublish duofenCardPublish = duofenCardPublishDAO.findByCardId( cardId );
	DuofenCardNew duofenCardNew = duofenCardNewDAO.selectById( cardId );
	map.put( "duofenCardPublish",duofenCardPublish );
	map.put( "duofenCardNew",duofenCardNew );
	DuofenCardTime duofenCardTime=duofenCardTimeDAO.findDuofenCardTimeByCardId( cardId );
	map.put( "duofenCardTime",duofenCardTime );

	MemberEntity memberEntity=memberEntityDAO.selectById( memberId );
	if(CommonUtil.isNotEmpty( memberEntity.getPublicId() )) {
	   String url= requestService.newqrcodeCreateFinal(memberEntity.getPublicId());
	   map.put( "guanzhuUrl",url );
	}
	return map;
    }

    public Map<String,Object> findDuofenCardDetailsByCardId(Integer cardId){
	Map<String,Object> map=new HashMap<>(  );
	DuofenCardPublish duofenCardPublish = duofenCardPublishDAO.findByCardId( cardId );
	DuofenCardNew duofenCardNew = duofenCardNewDAO.selectById( cardId );
	map.put( "duofenCardPublish",duofenCardPublish );
	map.put( "duofenCardNew",duofenCardNew );
	DuofenCardTime duofenCardTime=duofenCardTimeDAO.findDuofenCardTimeByCardId( cardId );
	map.put( "duofenCardTime",duofenCardTime );
	return map;
    }

    public Map<String,Object> findDuofenCardDetailsByDuofenCardGetId(Integer duofenCardGetId){
	Map<String,Object> map=new HashMap<>(  );
	DuofenCardGetNew duofenCardGetNew=duofenCardGetNewDAO.selectById( duofenCardGetId );
	Integer cardId=duofenCardGetNew.getCardId();
	DuofenCardPublish duofenCardPublish = duofenCardPublishDAO.findByCardId( cardId );
	DuofenCardNew duofenCardNew = duofenCardNewDAO.selectById( cardId );
	map.put( "duofenCardPublish",duofenCardPublish );
	map.put( "duofenCardNew",duofenCardNew );
	DuofenCardTime duofenCardTime=duofenCardTimeDAO.findDuofenCardTimeByCardId( cardId );
	map.put( "duofenCardTime",duofenCardTime );
	return map;
    }


    @Transactional
    @Override
    public void bingdingPhone( HttpServletRequest request, Integer memberId, String phone, Integer busId,String vcode ) throws BusinessException {
	Map< String,Object > map = new HashMap< String,Object >();
	try {
	    // 短信校验
	    if ( CommonUtil.isEmpty( memberId ) ) {
		throw new BusinessException( ResponseMemberEnums.NULL );
	    }

	    if ( CommonUtil.isEmpty( phone ) ) {
		throw new BusinessException( ResponseMemberEnums.NULL );
	    }

	    if ( CommonUtil.isEmpty( busId ) ) {
		throw new BusinessException( ResponseMemberEnums.NULL );
	    }

	    if ( CommonUtil.isEmpty( vcode ) ) {
		throw new BusinessException( ResponseMemberEnums.PLEASE_PHONE_CODE );
	    }
	    String vcode1 = redisCacheUtil.get( phone + "_" + vcode );
	    if ( CommonUtil.isEmpty( vcode1 ) ) {
		throw new BusinessException( ResponseMemberEnums.NO_PHONE_CODE );
	    }


	    // 查询要绑定的手机号码
	    MemberEntity oldMemberEntity = memberEntityDAO.findByPhone( busId, phone );

	    MemberEntity memberEntity = null;
	    if ( CommonUtil.isEmpty( oldMemberEntity ) ) {
		// 新用户
		memberEntity = memberEntityDAO.selectById( memberId );
		MemberEntity m = new MemberEntity();
		m.setId( memberEntity.getId() );
		m.setPhone( phone );
		memberEntityDAO.updateById( m );
		memberEntity.setPhone( phone );
	    } else {
		memberEntity = memberEntityDAO.selectById( memberId );
		memberCommonService.newMemberMerge( memberEntity, busId, phone );
		memberEntity = memberEntityDAO.findByPhone( busId, phone );
	    }
	    Member member = new Member();
	    member.setId( memberEntity.getId() );
	    member.setNickname( memberEntity.getNickname() );
	    member.setIntegral( memberEntity.getIntegral() );
	    member.setFansCurrency( memberEntity.getFansCurrency() );
	    member.setPhone( memberEntity.getPhone() );
	    member.setMcId( memberEntity.getMcId() );
	    member.setHeadimgurl( memberEntity.getHeadimgurl() );
	    SessionUtils.setLoginMember( request, member );
	} catch ( BusinessException e ) {
	    throw new BusinessException( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    e.printStackTrace();
	    log.error( "优惠券绑定手机号码异常", e );
	    throw new BusinessException( ResponseEnums.ERROR );
	}
    }
}
