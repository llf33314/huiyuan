package com.gt.member.service.member.impl;

import com.alibaba.fastjson.JSONObject;
import com.gt.api.bean.session.Member;
import com.gt.api.enums.ResponseEnums;
import com.gt.api.util.KeysUtil;
import com.gt.api.util.RequestUtils;
import com.gt.api.util.SessionUtils;
import com.gt.common.entity.AlipayUser;
import com.gt.common.entity.BusFlow;
import com.gt.common.entity.BusUserEntity;
import com.gt.common.entity.WxPublicUsersEntity;
import com.gt.member.dao.*;
import com.gt.member.dao.common.*;
import com.gt.member.dto.ServerResponse;
import com.gt.member.entity.*;
import com.gt.member.enums.ResponseMemberEnums;
import com.gt.member.exception.BusinessException;
import com.gt.member.service.common.dict.DictService;
import com.gt.member.service.common.membercard.MemberCommonService;
import com.gt.member.service.common.membercard.RequestService;
import com.gt.member.service.member.MemberCardPhoneService;
import com.gt.member.service.member.SystemMsgService;
import com.gt.member.util.CommonUtil;
import com.gt.member.util.DateTimeKit;
import com.gt.member.util.PropertiesUtil;
import com.gt.util.entity.param.fenbiFlow.AdcServicesInfo;
import com.gt.util.entity.param.pay.SubQrPayParams;
import org.apache.ibatis.builder.BuilderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 手机端会员卡
 * Created by Administrator on 2017/10/31.
 */
@Service
public class MemberCardPhoneServiceImpl implements MemberCardPhoneService {

    private static final Logger LOG = LoggerFactory.getLogger( MemberCardPhoneServiceImpl.class );
    @Autowired
    private AlipayUserDAO alipayUserMapper;

    @Autowired
    private WxPublicUsersDAO wxPublicUsersMapper;

    @Autowired
    private MemberEntityDAO memberEntityDAO;

    @Autowired
    private MemberGradetypeDAO memberGradetypeDAO;

    @Autowired
    private MemberOptionDAO memberOptionDAO;

    @Autowired
    private BasisCityDAO basisCityDAO;

    @Autowired
    private MemberCardDAO cardMapper;

    @Autowired
    private BusUserDAO busUserDAO;

    @Autowired
    private DictService dictService;

    @Autowired
    private MemberEntityDAO memberMapper;

    @Autowired
    private MemberCommonService memberCommonService;

    @Autowired
    private MemberParameterDAO memberParameterMapper;

    @Autowired
    private MemberGiveruleDAO memberGiveruleDAO;

    @Autowired
    private SystemMsgService systemMsgService;

    @Autowired
    private UserConsumeNewDAO userConsumeNewDAO;

    @Autowired
    private UserConsumePayDAO userConsumePayDAO;

    @Autowired
    private MemberFindDAO memberFindDAO;

    @Autowired
    private SystemnoticecallDAO systemnoticecallDAO;

    @Autowired
    private MemberNoticeuserDAO noticeUserMapper;

    @Autowired
    private MemberCardrecordNewDAO memberCardrecordNewDAO;

    @Autowired
    private BusFlowDAO busFlowDAO;

    @Autowired
    private RequestService requestService;

    public String wxPayWay( UserConsumeNew consumeNew, int payType ) throws Exception {
	SubQrPayParams subQrPayParams = new SubQrPayParams();
	subQrPayParams.setTotalFee( consumeNew.getDiscountAfterMoney() );
	subQrPayParams.setModel( 7 );
	subQrPayParams.setBusId( consumeNew.getBusId() );
	subQrPayParams.setAppidType( 0 );
	    /*subQrPayParams.setAppid( 0 );*///微信支付和支付宝支付可以不传
	subQrPayParams.setOrderNum( consumeNew.getOrderCode() );//订单号
	subQrPayParams.setMemberId( consumeNew.getMemberId() );//会员id
	subQrPayParams.setDesc( "购买会员卡" );//描述
	subQrPayParams.setIsreturn( 1 );//是否需要同步回调(支付成功后页面跳转),1:需要(returnUrl比传),0:不需要(为0时returnUrl不用传)
	String returnUrl = PropertiesUtil.getWebHome() + "/phoneOrder/79B4DE7C/orderList.do";
	String sucessUrl = PropertiesUtil.getWebHome() + "/memberPhone/member/buyMemberCard.do";
	subQrPayParams.setReturnUrl( returnUrl );
	subQrPayParams.setNotifyUrl( sucessUrl );//异步回调，注：1、会传out_trade_no--订单号,payType--支付类型(0:微信，1：支付宝2：多粉钱包),2接收到请求处理完成后，必须返回回调结果：code(0:成功,-1:失败),msg(处理结果,如:成功)
	subQrPayParams.setIsSendMessage( 1 );//是否需要消息推送,1:需要(sendUrl比传),0:不需要(为0时sendUrl不用传)
	//  subQrPayParams.setSendUrl( PropertiesUtil.getHomeUrl() + "mallOrder/toIndex.do" );//推送路径(尽量不要带参数)

	subQrPayParams.setPayWay( payType );//支付方式  0----系统根据浏览器判断   1---微信支付 2---支付宝 3---多粉钱包支付
	KeysUtil keyUtil = new KeysUtil();
	String params = keyUtil.getEncString( JSONObject.toJSONString( subQrPayParams ) );
	return PropertiesUtil.getWxmp_home() + "/8A5DA52E/payApi/6F6D9AD2/79B4DE7C/payapi.do?obj=" + params;
    }

    /**
     * @param busId
     *
     * @return
     */
    public Map< String,Object > findLingquData( HttpServletRequest request, Integer busId ) {
	Map< String,Object > map = new HashMap<>();
	//查询支付方式
	Integer browser = CommonUtil.judgeBrowser( request );
	// 登录页面 暂时使用 uc端 进入 使用支付宝
	AlipayUser alipayUser = alipayUserMapper.selectByBusId( busId );
	if ( browser.equals( 99 ) && CommonUtil.isNotEmpty( alipayUser ) ) {
	    map.put( "payType", 0 );  //支付宝支付
	}

	WxPublicUsersEntity wxPublicUsers = wxPublicUsersMapper.selectByUserId( busId );
	if ( browser.equals( 1 ) && CommonUtil.judgeBrowser( request ) == 1 && CommonUtil.isNotEmpty( wxPublicUsers ) ) {
	    map.put( "payType", 1 );  //微信支付
	}

	List< Map< String,Object > > gradeTypes = memberGradetypeDAO.findBybusId1( busId );
	map.put( "gradeTypes", gradeTypes );

	MemberOption memberoption = memberOptionDAO.findByBusId( busId );
	map.put( "memberoption", memberoption );

	//省
	List< Map< String,Object > > basisCitys = basisCityDAO.findBasisCity();
	map.put( "basisCitys", basisCitys );
	return map;
    }

    /**
     * uc端注册并领取会员卡
     *
     * @throws Exception
     */
    @Transactional
    public void linquMemberCard( Map< String,Object > params ) throws BusinessException {
	try {
	    Integer busId = CommonUtil.toInteger( params.get( "busId" ) );

	    int count = cardMapper.countCardisBinding( busId );

	    BusUserEntity busUserEntity = busUserDAO.selectById( busId );

	    String dictNum = dictService.dictBusUserNum( busId, busUserEntity.getLevel(), 4, "1093" ); // 多粉 翼粉
	    if ( CommonUtil.toInteger( dictNum ) < count ) {
		throw new BusinessException( ResponseMemberEnums.NOT_MEMBER_COUNT );
	    }

	    String phone = CommonUtil.toString( params.get( "phone" ) );
	    MemberEntity memberEntity = null;
	    if ( CommonUtil.isNotEmpty( params.get( "memberId" ) ) ) {
		Integer memberId = CommonUtil.toInteger( params.get( "memberId" ) );
		memberEntity = memberMapper.selectById( memberId );
		memberCommonService.newMemberMerge( memberEntity, busId, phone );
	    } else {
		memberEntity = memberMapper.findByPhone( busUserEntity.getId(), phone );
	    }

	    if ( CommonUtil.isEmpty( memberEntity ) ) {
		// 新增用户
		memberEntity = new MemberEntity();
		memberEntity.setPhone( phone );
		memberEntity.setBusId( busUserEntity.getId() );
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
		    throw new BusinessException( ResponseMemberEnums.IS_MEMBER_CARD );
		}
	    }

	    Integer ctId = CommonUtil.toInteger( params.get( "ctId" ) );
	    Integer gtId = CommonUtil.toInteger( params.get( "gtId" ) );
	    Integer shopId = CommonUtil.toInteger( params.get( "shopId" ) );

	    // 根据卡片类型 查询第一等级
	    List< Map< String,Object > > gradeTypes = memberGradetypeDAO.findBybusIdAndCtId3( memberEntity.getBusId(), ctId );
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
		    MemberGiverule giveRule = memberGiveruleDAO
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

	    } else {
		//购买会员卡
		MemberGradetype gradeType = memberGradetypeDAO.selectById( gtId );
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
		uc.setBusId( busUserEntity.getId() );
		uc.setShopId( shopId );

		userConsumeNewDAO.insert( uc );

		MemberEntity memberEntity1 = new MemberEntity();
		memberEntity1.setId( memberEntity.getId() );
		memberEntity1.setPhone( phone );
		memberMapper.updateById( memberEntity1 );

		Integer payType = CommonUtil.toInteger( params.get( "payType" ) );
		if ( payType == 0 ) {
		    payType = 2;
		} else if ( payType == 1 ) {
		    payType = 1;
		}
		String url = wxPayWay( uc, payType );
		throw new BusinessException( ResponseMemberEnums.PLEASE_BUY_CARD.getCode(), url );
	    }
	} catch ( BuilderException e ) {
	    throw e;
	} catch ( Exception e ) {
	    LOG.error( "erp 领取会员卡异常", e );
	    throw new BusinessException( ResponseEnums.ERROR );
	}
    }

    @Transactional
    @Override
    public void buyMemberCard( Map< String,Object > params ) throws Exception {

	String orderCode = CommonUtil.toString( params.get( "out_trade_no" ) );
	UserConsumeNew uc = userConsumeNewDAO.findOneByCode( orderCode );
	//购买会员卡
	MemberGradetype gradeType = memberGradetypeDAO.selectById( uc.getGtId() );
	if ( CommonUtil.isEmpty( gradeType ) || CommonUtil.isEmpty( gradeType.getBuyMoney() <= 0 ) ) {
	    throw new Exception();
	}
	Integer payType = CommonUtil.toInteger( params.get( "payType" ) );
	UserConsumeNew u = new UserConsumeNew();
	u.setId( uc.getId() );
	u.setPayStatus( 1 );
	u.setIsendDate( new Date() );
	userConsumeNewDAO.updateById( u );

	UserConsumePay ucpay = new UserConsumePay();
	ucpay.setUcId( uc.getId() );
	ucpay.setPayMoney( uc.getDiscountAfterMoney() );

	if ( payType == 0 ) {
	    ucpay.setPaymentType( 1 );
	} else if ( payType == 1 ) {
	    ucpay.setPaymentType( 0 );
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
	MemberGiverule giveRule = memberGiveruleDAO.findBybusIdAndGtIdAndCtId( uc.getBusId(), card.getGtId(), card.getCtId() );
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
	} else {
	    if ( CommonUtil.isNotEmpty( gradeType.getBalance() ) ) {
		card.setMoney( new Double( gradeType.getBalance() ) );
	    } else {
		card.setMoney( 0.0 );
	    }
	}

	cardMapper.insert( card );

	MemberEntity memberEntity = new MemberEntity();
	memberEntity.setId( uc.getMemberId() );
	memberEntity.setIsBuy( 1 );
	memberEntity.setMcId( card.getMcId() );
	memberMapper.updateById( memberEntity );

	memberCommonService.saveCardRecordOrderCodeNew( memberEntity.getId(), 1, gradeType.getBuyMoney(), "购买会员卡", uc.getBusId(), 0.0, orderCode, 0 );

	// 新增会员短信通知
	memberEntity = memberMapper.selectById( uc.getMemberId() );
	systemMsgService.sendNewMemberMsg( memberEntity );
    }

    /**
     * 查询会员卡信息
     *
     * @param request
     * @param busId
     *
     * @return
     * @throws BusinessException
     */
    public Map< String,Object > findMember( HttpServletRequest request, Integer busId ) throws BusinessException {
	try {
	    Map< String,Object > map = new HashMap<>();
	    Member member = SessionUtils.getLoginMember( request, busId );

	    MemberEntity memberEntity = memberEntityDAO.selectById( member.getId() );
	    map.put( "member", memberEntity );

	    MemberFind memberfind = memberFindDAO.findByQianDao( member.getBusid() );
	    map.put( "qindaoJifen", memberfind.getIntegral() );

	    MemberParameter memberParameter = memberParameterMapper.findByMemberId( member.getId() );
	    if ( CommonUtil.isEmpty( memberParameter ) ) {
		request.setAttribute( "qiandao", 0 );
	    } else {
		if ( CommonUtil.isNotEmpty( memberParameter.getSignDate() ) && DateTimeKit.isSameDay( memberParameter.getSignDate(), new Date() ) ) {
		    map.put( "qiandao", 1 );
		} else {
		    map.put( "qiandao", 0 );
		}
	    }

	    // 查询我的消息
	    int systemCount = systemnoticecallDAO.findCountByMemberId( member.getId() );
	    int noticeCount = noticeUserMapper.findCountNotice( member.getId(), new Date() );
	    int count = systemCount + noticeCount;
	    map.put( "count", count );

	    // 根据卡号查询信息
	    List< Map< String,Object > > card = cardMapper.findCardById( member.getMcId() );
	    map.put( "isleft", card.get( 0 ).get( "isleft" ) );

	    if ( CommonUtil.isEmpty( member.getHeadimgurl() ) ) {
		MemberParameter mp = memberParameterMapper.findByMemberId( member.getId() );
		if ( CommonUtil.isNotEmpty( mp ) ) {
		    map.put( "headimg", mp.getHeadImg() );
		}
	    } else {
		map.put( "headimg", member.getHeadimgurl() );
	    }

	    // 联盟卡查询
	    String unionUrl = PropertiesUtil.getUntion_url() + "/cardPhone/#/toUnionCard?busId=" + busId;
	    map.put( "unionUrl", unionUrl );

	    return map;
	} catch ( Exception e ) {
	    LOG.error( "查询会员信息异常", e );
	    throw new BusinessException( ResponseEnums.ERROR );
	}
    }

    public Map< String,Object > findCardrecordNew( Map< String,Object > params, Integer memberId, Integer recordType ) {
	Map< String,Object > map = new HashMap<>();
	Integer page = CommonUtil.toInteger( params.get( "page" ) );
	Integer pageSize = 10;
	List< Map< String,Object > > listMap = memberCardrecordNewDAO.findCardrecordByMemberIdAndRecordType( memberId, recordType, page, pageSize );
	map.put( "page", listMap );
	MemberEntity memberEntity = memberEntityDAO.selectById( memberId );
	map.put( "jifen", memberEntity.getIntegral() );
	map.put( "fenbi", memberEntity.getFansCurrency() );
	map.put( "flow", memberEntity.getFlow() );
	return map;
    }

    public List< BusFlow > findBusUserFlow( Integer busId ) {
	List< BusFlow > busFlows = busFlowDAO.getBusFlowsByUserId( busId );
	return busFlows;
    }

    @Rollback
    public void changeFlow( Map< String,Object > params, Integer memberId ) throws BusinessException {
	try {
	    Integer busId = CommonUtil.toInteger( params.get( "busId" ) );
	    String phone = CommonUtil.toString( params.get( "phone" ) );

	    UserConsumeNew uc = new UserConsumeNew();
	    uc.setBusId( busId );
	    uc.setMemberId( memberId );
	    MemberEntity memberEntity = memberEntityDAO.selectById( memberId );
	    MemberCard card = cardMapper.selectById( memberEntity.getMcId() );
	    if ( CommonUtil.isEmpty( card ) ) {
		throw new BusinessException( ResponseMemberEnums.MEMBER_NOT_CARD );
	    }

	    Integer prizeCount = CommonUtil.toInteger( params.get( "prizeCount" ) );

	    if ( memberEntity.getFlow() < prizeCount ) {
		throw new BusinessException( ResponseMemberEnums.LESS_THAN_FLOE );
	    }
	    Integer flowBalance = memberEntity.getFlow() - prizeCount;
	    uc.setMcId( card.getMcId() );
	    uc.setCtId( card.getCtId() );
	    uc.setGtId( card.getGtId() );
	    uc.setRecordType( 5 );
	    uc.setUcType( 132 );
	    uc.setChangeFlow( prizeCount );
	    uc.setFlowbalance( flowBalance );
	    String orderCode = CommonUtil.getMEOrderCode();
	    uc.setOrderCode( orderCode );
	    userConsumeNewDAO.insert( uc );

	    RequestUtils< AdcServicesInfo > requestUtils = new RequestUtils<>();
	    AdcServicesInfo adcServicesInfo = new AdcServicesInfo();
	    if ( CommonUtil.isEmpty( phone ) ) {
		if ( CommonUtil.isEmpty( memberEntity.getPhone() ) ) {
		    throw new BusinessException( ResponseMemberEnums.NOT_MEMBER_PHONE );
		}
		adcServicesInfo.setMobile( memberEntity.getPhone() );
	    } else {
		adcServicesInfo.setMobile( phone );
	    }
	    adcServicesInfo.setBusId( busId );
	    adcServicesInfo.setPrizeCount( prizeCount );
	    adcServicesInfo.setMemberId( memberId );
	    adcServicesInfo.setModel( 102 );
	    adcServicesInfo.setId( uc.getId() );
	    String notifyUrl="";
	    adcServicesInfo.setNotifyUrl( notifyUrl );
	    requestUtils.setReqdata( adcServicesInfo );
	    String result = requestService.changeFlow( requestUtils );
	    JSONObject json = JSONObject.parseObject( result );
	    if ( !"0".equals( CommonUtil.toString( json.get( "code" ) ) ) ) {
		throw new BusinessException( ResponseMemberEnums.ERROR_CHARGE_FLOW.getCode(), CommonUtil.toString( json.get( "msg" ) ) );
	    }
	    memberCommonService.saveCardRecordOrderCodeNew( memberId, 4, prizeCount.doubleValue(), "流量兑换", busId, flowBalance.doubleValue(), orderCode, 0 );
	} catch ( BusinessException e ) {
	    throw e;
	}
    }

}
