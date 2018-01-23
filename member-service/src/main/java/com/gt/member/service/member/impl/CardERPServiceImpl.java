package com.gt.member.service.member.impl;

import com.gt.api.bean.session.WxPublicUsers;
import com.gt.api.enums.ResponseEnums;
import com.gt.api.util.sign.SignHttpUtils;
import com.gt.member.dao.*;
import com.gt.member.entity.*;
import com.gt.member.exception.BusinessException;
import com.gt.member.service.common.membercard.MemberCommonService;
import com.gt.member.service.common.dict.DictService;
import com.gt.member.service.common.membercard.RequestService;
import com.gt.member.service.member.CardERPService;
import com.gt.member.service.member.SystemMsgService;
import com.gt.member.util.*;
import org.apache.ibatis.builder.BuilderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class CardERPServiceImpl implements CardERPService {

    private static final Logger LOG = LoggerFactory.getLogger( CardERPServiceImpl.class );

    @Autowired
    private MemberEntityDAO memberMapper;

    @Autowired
    private MemberCardDAO cardMapper;

    @Autowired
    private MemberParameterDAO memberParameterMapper;

    @Autowired
    private MemberGradetypeDAO gradeTypeMapper;

    @Autowired
    private MemberGiveruleDAO giveRuleMapper;

    @Autowired
    private MemberCardbuyDAO cardBuyMapper;

    @Autowired
    private DictService dictService;

    @Autowired
    private MemberCommonService memberCommonService;

    @Autowired
    private SystemMsgService systemMsgService;


    @Autowired
    private RedisCacheUtil redisCacheUtil;

    @Autowired
    private UserConsumeNewDAO userConsumeNewDAO;

    @Autowired
    private UserConsumePayDAO userConsumePayDAO;

    @Autowired
    private  RequestService requestService;

    @Override
    public List< Map< String,Object > > findMemberIsNotCard( Integer busId, Map< String,Object > params ) {
	try {
	    params.put( "curPage", CommonUtil.isEmpty( params.get( "curPage" ) ) ? 1 : CommonUtil.toInteger( params.get( "curPage" ) ) );

	    Date date = DateTimeKit.parseDate( DateTimeKit.format( new Date(), "yyyy-MM-dd" ) + " 00:00:00" );

	    List< Map< String,Object > > members = memberMapper.findMemberIsNotCard( busId );

	    List< Map< String,Object > > memberList = new ArrayList< Map< String,Object > >();
	    for ( Map< String,Object > map : members ) {
		try {
		    byte[] bytes = (byte[]) map.get( "nickname" );
		    map.put( "nickname", new String( bytes, "UTF-8" ) );
		} catch ( Exception e ) {
		    map.put( "nickname", null );
		}
		memberList.add( map );
	    }
	    return members;
	} catch ( Exception e ) {
	    e.printStackTrace();
	}
	return null;
    }

    /**
     * uc端注册并领取会员卡
     *
     * @throws Exception
     */
    @Transactional
    public Map< String,Object > linquMemberCard( Integer busId, Map< String,Object > params ) throws BusinessException  {
	Map< String,Object > returnMap = new HashMap<>();
	try {

	    int count = cardMapper.countCardisBinding( busId );

	    String dictNum = dictService.dictBusUserNum( busId,1093 );

	    if ( CommonUtil.toInteger( dictNum ) < count ) {
		returnMap.put( "code", -1 );
		returnMap.put( "message", "会员卡已领取完!" );
		return returnMap;
	    }

	    String phone = CommonUtil.toString( params.get( "phone" ) );
	    MemberEntity memberEntity = null;
	    if ( CommonUtil.isNotEmpty( params.get( "memberId" ) ) ) {
		Integer memberId = CommonUtil.toInteger( params.get( "memberId" ) );
		memberEntity = memberMapper.selectById( memberId );
		memberCommonService.newMemberMerge( memberEntity, busId, phone );
	    } else {
		memberEntity = memberMapper.findByPhone(busId, phone );
	    }

	    if ( CommonUtil.isEmpty( memberEntity ) ) {
		// 新增用户
		memberEntity = new MemberEntity();
		memberEntity.setPhone( phone );
		memberEntity.setBusId( busId );
		memberEntity.setLoginMode( 1 );
		memberEntity.setNickname( "Fans_" + phone.substring( 4 ) );
		memberMapper.insert( memberEntity );
		MemberParameter memberParameter = memberParameterMapper.findByMemberId( memberEntity.getId() );
		if ( CommonUtil.isEmpty( memberParameter ) ) {
		    MemberParameter mp = new MemberParameter();
		    mp.setMemberId( memberEntity.getId() );
		    memberParameterMapper.insert( mp );
		}
	    } else {
		if ( CommonUtil.isNotEmpty( memberEntity.getMcId() ) ) {
		    returnMap.put( "code", -1 );
		    returnMap.put( "message", "粉丝已成为会员" );
		    return returnMap;
		}
	    }

	    Integer ctId = CommonUtil.toInteger( params.get( "ctId" ) );
	    Integer gtId = CommonUtil.toInteger( params.get( "gtId" ) );
	    Integer shopId = CommonUtil.toInteger( params.get( "shopId" ) );

	    // 根据卡片类型 查询第一等级
	    List< Map< String,Object > > gradeTypes = gradeTypeMapper.findBybusIdAndCtId3( memberEntity.getBusId(), ctId );
	    if ( CommonUtil.toInteger( gradeTypes.get( 0 ).get( "applyType" ) ) != 3 ) {
		//非购买会员卡  直接分配会员卡
		MemberCard card = new MemberCard();
		card.setIsChecked( 1 );
		card.setCardNo( CommonUtil.getCode() );
		card.setCtId( ctId );
		if ( card.getCtId() == 4 ) {
		    card.setExpireDate( new Date() );
		}

		card.setSystemcode( CommonUtil.getNominateCode() );
		card.setApplyType( 0 );

		if ( gradeTypes != null && gradeTypes.size() > 0 ) {
		    card.setGtId( Integer.parseInt( gradeTypes.get( 0 ).get( "gt_id" ).toString() ) );
		    MemberGiverule giveRule = giveRuleMapper
				    .findBybusIdAndGtIdAndCtId( memberEntity.getBusId(), Integer.parseInt( gradeTypes.get( 0 ).get( "gt_id" ).toString() ), ctId );
		    card.setGrId( giveRule.getGrId() );
		}
		card.setBusId( memberEntity.getBusId() );
		card.setReceiveDate( new Date() );
		card.setIsbinding( 1 );

		card.setShopId( shopId );
		card.setOnline( 0 );
		cardMapper.insert( card );

		MemberEntity memberEntity1 = new MemberEntity();
		memberEntity1.setMcId( card.getMcId() );
		memberEntity1.setId( memberEntity.getId() );
		memberEntity1.setPhone( phone );
		memberMapper.updateById( memberEntity1 );

		// 新增会员短信通知
		systemMsgService.sendNewMemberMsg( memberEntity );
		returnMap.put( "code", 1 );
		returnMap.put( "message", "领取成功" );
		returnMap.put( "memberId", memberEntity.getId() );
	    } else {
		//购买会员卡
		MemberGradetype gradeType = gradeTypeMapper.selectById( gtId );
		if ( CommonUtil.isEmpty( gradeType ) || CommonUtil.isEmpty( gradeType.getBuyMoney() <= 0 ) ) {
		    throw new Exception();
		}
		// 添加会员记录
		UserConsumeNew uc = new UserConsumeNew();
		uc.setMemberId( memberEntity.getId() );
		uc.setCtId( ctId );
		uc.setRecordType( 2 );
		uc.setUcType( 13 );
		uc.setTotalMoney( gradeType.getBuyMoney() );
		uc.setCreateDate( new Date() );
		uc.setPayStatus( 0 );
		uc.setDiscountMoney( 0.0 );
		uc.setDiscountAfterMoney( gradeType.getBuyMoney() );
		String orderCode = CommonUtil.getMEOrderCode();
		uc.setOrderCode( orderCode );
		uc.setGtId( gtId );
		uc.setBusId( busId );
		uc.setShopId( shopId );

		String notityUrl = PropertiesUtil.getWebHome() + "/addMember/79B4DE7C/successPayBuyCard";
		WxPublicUsers wxPublicUsers= requestService.findWxPublicUsersByBusId( busId );
		userConsumeNewDAO.insert( uc );

		MemberEntity memberEntity1 = new MemberEntity();
		memberEntity1.setId( memberEntity.getId() );
		memberEntity1.setPhone( phone );
		memberMapper.updateById( memberEntity1 );

		String url = PropertiesUtil.getWxmp_home() + "/pay/B02A45A5/79B4DE7C/createPayQR.do" + "?totalFee=" + gradeType.getBuyMoney() + "&model=13&busId=" + busId
				+ "&orderNum=" + orderCode +"&desc=支付&notifyUrl=" + notityUrl + "&appid=" + wxPublicUsers.getAppid() + "&appidType=0&isSendMessage=0&payWay=0&sourceType=1";
		returnMap.put( "memberId", memberEntity.getId() );
		returnMap.put( "code", 2 );
		returnMap.put( "message", "未支付" );
		returnMap.put( "url", url );
		String memberUser = CommonUtil.toString( params.get( "memberUser" ) );
		redisCacheUtil.set( orderCode, memberUser, 300 );
	    }
	}catch ( BuilderException e ){
	   throw e;
	} catch ( Exception e ) {
	    LOG.error( "erp 领取会员卡异常", e );
	    throw new BusinessException( ResponseEnums.ERROR);
	}
	return returnMap;
    }

    @Transactional
    @Override
    public Map< String,Object > buyMemberCard( Map< String,Object > params ) throws Exception {
	Map< String,Object > returnMap = new HashMap<>();
	try {
	    String orderCode = CommonUtil.toString( params.get( "out_trade_no" ) );
	    UserConsumeNew uc = userConsumeNewDAO.findOneByCode( orderCode );
	    //购买会员卡
	    MemberGradetype gradeType = gradeTypeMapper.selectById( uc.getGtId() );
	    if ( CommonUtil.isEmpty( gradeType ) || CommonUtil.isEmpty( gradeType.getBuyMoney() <= 0 ) ) {
		throw new Exception();
	    }
	    Integer payType = CommonUtil.toInteger( params.get( "payType" ) );
	    UserConsumeNew u=new UserConsumeNew();
	    u.setId( uc.getId() );
	    u.setPayStatus( 1 );
	    u.setIsendDate(new Date(  ));
	    userConsumeNewDAO.updateById( u );

	    UserConsumePay ucpay=new UserConsumePay();
	    ucpay.setUcId( uc.getId());
	    ucpay.setPayMoney(uc.getDiscountAfterMoney());


	    if(payType==0){
		ucpay.setPaymentType(1);
	    }else if(payType==1) {
		ucpay.setPaymentType(0);
	    }
	    userConsumePayDAO.insert( ucpay );

	    // 添加会员卡
	    MemberCard card = new MemberCard();
	    card.setIsChecked( 1 );
	    card.setCardNo( CommonUtil.getCode() );
	    card.setCtId( uc.getCtId() );

	    card.setSystemcode( CommonUtil.getNominateCode() );
	    card.setApplyType( 3 );
	    card.setMemberId( uc.getMemberId() );
	    card.setGtId( uc.getGtId() );
	    MemberGiverule giveRule = giveRuleMapper.findBybusIdAndGtIdAndCtId( uc.getBusId(), card.getGtId(), card.getCtId() );
	    card.setGrId( giveRule.getGrId() );

	    card.setCardNo( CommonUtil.getCode() );
	    card.setBusId( uc.getBusId() );
	    card.setReceiveDate( new Date() );
	    card.setIsbinding( 1 );

	    if ( card.getCtId() == 5 ) {
		if ( CommonUtil.isNotEmpty( gradeType.getBalance() ) ) {
		    card.setFrequency( new Double( gradeType.getBalance() ).intValue() );
		} else {
		    card.setFrequency( 0 );
		}
	    } else if(card.getCtId()==3) {
		if ( CommonUtil.isNotEmpty( gradeType.getBalance() ) ) {
		    card.setMoney( new Double( gradeType.getBalance() ) );
		} else {
		    card.setMoney( 0.0 );
		}
	    }else if(card.getCtId()==4){
		if ( CommonUtil.isNotEmpty( gradeType.getBalance() ) ) {
		    card.setExpireDate( DateTimeKit.addMonths( CommonUtil.toDouble( gradeType.getBalance() ).intValue()  )  );
		} else {
		    card.setExpireDate( new Date(  ));
		}
	    }

	    cardMapper.insert( card );

	    MemberEntity memberEntity = new MemberEntity();
	    memberEntity.setId( uc.getMemberId() );
	    memberEntity.setIsBuy( 1 );
	    memberEntity.setMcId( card.getMcId() );
	    memberMapper.updateById( memberEntity );

	    memberCommonService.saveCardRecordOrderCodeNew( memberEntity.getId(),1,gradeType.getBuyMoney(),"购买会员卡",uc.getBusId(),0.0,orderCode,0 );

	    // 新增会员短信通知
	    memberEntity = memberMapper.selectById( uc.getMemberId() );
	    systemMsgService.sendNewMemberMsg( memberEntity );

	    String wxmpsignKey= PropertiesUtil.getWxmpsignKey();
	    String socketUrl= PropertiesUtil.getWxmp_home()+"/8A5DA52E/socket/getSocketApi.do";

	    Map<String,Object> socketMap=new HashMap<>(  );

	    String pushName=  redisCacheUtil.get( orderCode );

	    LOG.error( "调用推送地址："+pushName );
	    socketMap.put( "pushName",pushName );
	    socketMap.put( "pushMsg",memberEntity.getId() );
	    socketMap.put( "pushStyle","1" );
	    String returnMsg= SignHttpUtils.WxmppostByHttp( socketUrl, socketMap, wxmpsignKey );  //推送

	    LOG.error( "调用推送信息："+returnMsg );
	    returnMap.put( "code", 0 );
	    returnMap.put( "msg", "领取成功" );
	    return returnMap;
	}catch ( Exception e ){
	    throw new Exception(  );
	}

    }

    @Override
    public Map< String,Object > findMemberCard( Integer busId, String cardNo ) {
	Map< String,Object > map = new HashMap< String,Object >();
	String cardNodecrypt = "";
	try {
	    String cardNoKey= PropertiesUtil.getCardNoKey();
	    // 如果手动输入 会出现异常
	    cardNodecrypt = EncryptUtil.decrypt( cardNoKey, cardNo );
	} catch ( Exception e ) {
	}

	busId = requestService.getMainBusId( busId );

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

	MemberCard card=null;
	try {
	    MemberEntity memberEntity = null;
	    // 查询卡号是否存在
	    if ( CommonUtil.isEmpty( card ) ) {
		card = cardMapper.findCardByCardNo( busId, cardNo );
		if ( CommonUtil.isNotEmpty( card ) ) {
		    memberEntity = memberMapper.findByMcIdAndbusId( busId, card.getMcId() );
		}

	    }

	    if ( CommonUtil.isEmpty( card ) ) {
		memberEntity = memberMapper.findByPhone( busId, cardNo );
		if ( CommonUtil.isNotEmpty( memberEntity ) ) {
		    card = cardMapper.selectById( memberEntity.getMcId() );
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
		map.put( "sex", memberEntity.getSex() );
		map.put("headimgUrl", memberEntity.getHeadimgurl());
		map.put( "nickName", memberEntity.getNickname() );
		map.put( "phone", memberEntity.getPhone() );
		map.put( "ctName", cards.get( 0 ).get( "ct_name" ) );
		map.put( "gradeName", cards.get( 0 ).get( "gt_grade_name" ) );
		map.put( "cardNo", card.getCardNo() );
		map.put( "ctId", card.getCtId() );
		map.put( "discount", giveRule.getGrDiscount() / 10.0 );
		map.put( "money", card.getMoney() );
		map.put( "frequency", card.getFrequency() );
		map.put("receiveDate",DateTimeKit.format( card.getReceiveDate() ));
		map.put( "fans_currency", memberEntity.getFansCurrency() );
		map.put( "integral", memberEntity.getIntegral() );
		return map;
	    }
	} catch ( Exception e ) {
	    map.put( "result", false );
	    map.put( "message", "查询异常" );
	    LOG.error( "ERP查询会员信息异常", e );
	}
	return map;
    }
}
