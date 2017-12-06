package com.gt.member.service.member.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gt.api.bean.session.Member;
import com.gt.api.enums.ResponseEnums;
import com.gt.api.util.KeysUtil;
import com.gt.api.util.RequestUtils;
import com.gt.api.util.SessionUtils;
import com.gt.common.entity.*;
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
import com.gt.member.util.*;
import com.gt.util.entity.param.fenbiFlow.AdcServicesInfo;
import com.gt.util.entity.param.pay.SubQrPayParams;
import com.gt.util.entity.param.sms.OldApiSms;
import com.gt.util.entity.result.shop.WsWxShopInfoExtend;
import org.apache.ibatis.builder.BuilderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

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

    @Autowired
    private MemberRechargegiveDAO memberRechargegiveDAO;

    @Autowired
    private MemberRechargegiveAssistantDAO memberRechargegiveAssistantDAO;

    @Autowired
    private MemberGradetypeAssistantDAO memberGradetypeAssistantDAO;

    @Autowired
    private WxShopDAO wxShopDAO;

    @Autowired
    private MemberCardDAO memberCardDAO;

    @Autowired
    private RedisCacheUtil redisCacheUtil;

    @Autowired
    private MemberRecommendDAO memberRecommendDAO;

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
	String returnUrl = PropertiesUtil.getWebHome() + "/memberPhone/cardPhone/findMember.do";
	String sucessUrl = PropertiesUtil.getWebHome() + "/memberNodoInterceptor/memberNotDo/buyCardPaySuccess.do";
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
	    map.put( "payType", 2 );  //支付宝支付
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

	map.put( "loginImg",requestService.loginImg( busId ) );
	return map;
    }

    public List< Map< String,Object > > findBuyGradeTypes( String json ) {
	JSONObject params = JSON.parseObject( json );
	Integer busId = CommonUtil.toInteger( params.get( "busId" ) );
	Integer ctId = CommonUtil.toInteger( params.get( "ctId" ) );
	List< Map< String,Object > > gradeTypes = memberGradetypeDAO.findAllBybusId( busId, ctId );
	return gradeTypes;
    }

    /**
     * uc端注册并领取会员卡
     *
     * @throws Exception
     */
    @Transactional
    public void linquMemberCard( Map< String,Object > params, Integer memberId ) throws BusinessException {
	try {
	    Integer busId = CommonUtil.toInteger( params.get( "busId" ) );
	    String phone = CommonUtil.toString( params.get( "phone" ) );

	    Integer ctId = CommonUtil.toInteger( params.get( "ctId" ) );

	    MemberEntity memberEntity = memberEntityDAO.selectById( memberId );

	    //判断商家会员卡数量是否充足
	    int count = cardMapper.countCardisBinding( busId );
	    BusUserEntity busUserEntity = busUserDAO.selectById( busId );
	    String dictNum = dictService.dictBusUserNum( busId, busUserEntity.getLevel(), 4, "1093" ); // 多粉 翼粉
	    if ( CommonUtil.toInteger( dictNum ) < count ) {
		throw new BusinessException( ResponseMemberEnums.NOT_MEMBER_COUNT );
	    }

	    Integer shopId = 0;
	    WxShop wxShop = wxShopDAO.selectMainShopByBusId( busId );
	    if ( CommonUtil.isNotEmpty( wxShop ) ) {
		shopId = wxShop.getId();
	    }

	    List< Map< String,Object > > gradeTypes = memberGradetypeDAO.findBybusIdAndCtId3( memberEntity.getBusId(), ctId );
	    if ( CommonUtil.toInteger( gradeTypes.get( 0 ).get( "applyType" ) ) != 3 ) {

		String code = CommonUtil.toString( params.get( "code" ) );
		String value = redisCacheUtil.get( phone + "_" + code );
		if ( CommonUtil.isEmpty( value ) ) {
		    throw new BusinessException( ResponseMemberEnums.NO_PHONE_CODE );
		}

		// 判断相同的手机号码存在会员卡
		memberCommonService.newMemberMerge( memberEntity, busId, phone );

		MemberEntity member1 = new MemberEntity();
		member1.setId( memberId );
		member1.setPhone( phone );
		if ( CommonUtil.isNotEmpty( params.get( "name" ) ) ) {
		    member1.setName( params.get( "name" ).toString() );
		}
		if ( CommonUtil.isNotEmpty( params.get( "sex" ) ) ) {
		    member1.setSex( CommonUtil.toInteger( params.get( "sex" ) ) );
		}
		if ( CommonUtil.isNotEmpty( params.get( "birth" ) ) ) {
		    member1.setBirth( DateTimeKit.parseDate( params.get( "birth" ).toString() ) );
		}
		if ( CommonUtil.isNotEmpty( params.get( "email" ) ) ) {
		    member1.setEmail( params.get( "email" ).toString() );
		}
		if ( CommonUtil.isNotEmpty( params.get( "cardId" ) ) ) {
		    member1.setCardId( params.get( "cardId" ).toString() );
		}
		MemberParameter mp = memberParameterMapper.findByMemberId( memberId );
		if ( CommonUtil.isEmpty( mp ) ) {
		    mp = new MemberParameter();
		}
		boolean flag = false;
		if ( CommonUtil.isNotEmpty( params.get( "provice" ) ) ) {
		    mp.setProvinceCode( params.get( "provice" ).toString() );
		    flag = true;
		}
		if ( CommonUtil.isNotEmpty( params.get( "city" ) ) ) {
		    mp.setCityCode( params.get( "city" ).toString() );
		    flag = true;
		}
		if ( CommonUtil.isNotEmpty( params.get( "countyCode" ) ) ) {
		    mp.setCountyCode( params.get( "countyCode" ).toString() );
		    flag = true;
		}
		if ( CommonUtil.isNotEmpty( params.get( "address" ) ) ) {
		    mp.setAddress( params.get( "address" ).toString() );
		    flag = true;
		}
		if ( CommonUtil.isNotEmpty( params.get( "getMoney" ) ) ) {
		    mp.setGetMoney( CommonUtil.toDouble( params.get( "getMoney" ) ) );
		    flag = true;
		}
		if ( flag ) {
		    if ( CommonUtil.isEmpty( mp.getId() ) ) {
			mp.setMemberId( memberId );
			memberParameterMapper.insert( mp );
		    } else {
			mp.setId( mp.getId() );
			memberParameterMapper.updateById( mp );
		    }
		}

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
		} else {
		    throw new BusinessException( ResponseMemberEnums.NULL );
		}

		card.setBusId( busId );
		card.setReceiveDate( new Date() );
		card.setIsbinding( 1 );

		card.setShopId( shopId );
		card.setOnline( 0 );
		cardMapper.insert( card );

		member1.setMcId( card.getMcId() );
		member1.setId( memberEntity.getId() );
		member1.setPhone( phone );
		memberMapper.updateById( member1 );
		// 发送短信通知
		systemMsgService.sendNewMemberMsg( member1 );

		//<!------推荐会员卡赠送------>
		Integer isCheck = CommonUtil.toInteger( gradeTypes.get( 0 ).get( "isCheck" ) );
		Integer isrecommend = CommonUtil.toInteger( gradeTypes.get( 0 ).get( "isrecommend" ) );
		// 推荐赠送积分 粉币 流量
		if ( CommonUtil.isNotEmpty( params.get( "tuijianCode" ) ) && isrecommend == 1 ) {
		    String tuijianCode = CommonUtil.toString( params.get( "tuijianCode" ) );
		    //推荐
		    MemberCard memberCard = cardMapper.findBySystemCode( busId, tuijianCode );
		    if ( CommonUtil.isNotEmpty( memberCard ) ) {
			MemberEntity member2 = memberMapper.findByMcIdAndbusId( busId, memberCard.getMcId() );
			boolean bool = false;
			MemberEntity member3 = new MemberEntity();
			member3.setId( member2.getId() );
			MemberRecommend recommend = new MemberRecommend();
			recommend.setMemberId( member2.getId() );
			recommend.setCode( tuijianCode );
			if ( CommonUtil.isNotEmpty( gradeTypes.get( 0 ).get( "giveflow" ) ) && CommonUtil.toInteger( gradeTypes.get( 0 ).get( "giveflow" ) ) > 0 ) {
			    Integer giveFlow = CommonUtil.toInteger( gradeTypes.get( 0 ).get( "giveflow" ) );
			    recommend.setFlow( giveFlow );

			    if ( isCheck == 1 ) {
				Integer balaceFlow = member2.getFlow() + giveFlow;
				member3.setFlow( balaceFlow );
				memberCommonService.saveCardRecordOrderCodeNew( memberId, 4, giveFlow.doubleValue(), "推荐赠送", busId, balaceFlow.doubleValue(), "", 1 );
				bool = true;
			    }
			}

			if ( CommonUtil.isNotEmpty( gradeTypes.get( 0 ).get( "givefenbi" ) ) && CommonUtil.toInteger( gradeTypes.get( 0 ).get( "givefenbi" ) ) > 0 ) {
			    Integer fenbi = CommonUtil.toInteger( gradeTypes.get( 0 ).get( "givefenbi" ) );
			    recommend.setFenbi( fenbi );
			    if ( isCheck == 1 ) {
				Double balaceFenbi = member2.getFansCurrency() + fenbi;
				member3.setFansCurrency( balaceFenbi );
				memberCommonService.saveCardRecordOrderCodeNew( memberId, 3, fenbi.doubleValue(), "推荐赠送", busId, balaceFenbi, "", 1 );
				bool = true;
			    }
			}

			if ( CommonUtil.isNotEmpty( gradeTypes.get( 0 ).get( "giveIntegral" ) ) && CommonUtil.toInteger( gradeTypes.get( 0 ).get( "giveIntegral" ) ) > 0 ) {
			    Integer giveIntegral = CommonUtil.toInteger( gradeTypes.get( 0 ).get( "giveIntegral" ) );
			    recommend.setIntegral( giveIntegral );
			    if ( isCheck == 1 ) {
				Integer balaceIntegral = member2.getIntegral() + giveIntegral;
				member3.setIntegral( balaceIntegral );
				memberCommonService.saveCardRecordOrderCodeNew( memberId, 2, giveIntegral.doubleValue(), "推荐赠送", busId, balaceIntegral.doubleValue(), "", 1 );
				bool = true;
			    }
			}
			if ( bool ) {
			    memberMapper.updateById( member3 );
			}

			if ( CommonUtil.isNotEmpty( gradeTypes.get( 0 ).get( "giveMoney" ) ) && CommonUtil.toDouble( gradeTypes.get( 0 ).get( "giveMoney" ) ) > 0 ) {
			    Double giveMoney = CommonUtil.toDouble( gradeTypes.get( 0 ).get( "giveMoney" ) );
			    recommend.setMoney( giveMoney );
			    if ( isCheck == 1 ) {
				Double balaceMoney = memberCard.getGiveMoney() + giveMoney;
				MemberCard newC = new MemberCard();
				newC.setMcId( memberCard.getMcId() );
				newC.setGiveMoney( balaceMoney );
				cardMapper.updateById( newC );
				memberCommonService.saveCardRecordOrderCodeNew( memberId, 1, giveMoney, "推荐赠送", busId, balaceMoney, "", 1 );
			    }
			}
			recommend.setGetMemberId( memberId );
			recommend.setIsUser( 1 );
			if ( isCheck == 0 ) {
			    recommend.setIsGive( 1 );
			}
			//添加推荐记录
			memberRecommendDAO.insert( recommend );
		    }
		}
		// 如果会员卡需要审核发送短信提醒商家审核
		if ( isCheck == 0 ) {
		    // ischecked==0时会员卡需要审核
		    // 判断商家ID不为空时查询该商家的手机号是否填写
		    BusUserEntity busUser = busUserDAO.selectById( busId );
		    if ( CommonUtil.isNotEmpty( busUser.getPhone() ) ) {// 商家手机号不为空就发送短信提醒商家审核
			// 发送短信
			RequestUtils< OldApiSms > requestUtils = new RequestUtils< OldApiSms >();

			OldApiSms oldApiSms = new OldApiSms();
			oldApiSms.setMobiles( busUser.getPhone() );
			oldApiSms.setContent( "尊敬的商家,手机号：" + phone + "，已提交会员卡申请，请尽快审核。 " );
			oldApiSms.setCompany( PropertiesUtil.getSms_name() );
			oldApiSms.setBusId( memberEntity.getBusId() );
			oldApiSms.setModel( 3 );
			requestUtils.setReqdata( oldApiSms );
			try {
			    requestService.sendSms( requestUtils );
			} catch ( Exception e ) {
			    LOG.error( "短信发送失败", e );
			}
		    }
		}
	    } else {
		Integer gtId = CommonUtil.toInteger( params.get( "gtId" ) );
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

//		MemberEntity memberEntity1 = new MemberEntity();
//		memberEntity1.setId( memberEntity.getId() );
//		memberEntity1.setPhone( phone );
//		memberMapper.updateById( memberEntity1 );

		Integer payType = CommonUtil.toInteger( params.get( "payType" ) );
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

	    if ( CommonUtil.isEmpty( memberEntity.getMcId() ) ) {
		throw new BusinessException( ResponseMemberEnums.PLEASE_GET_CARD );
	    }
//	    if ( CommonUtil.isEmpty( memberEntity.getPhone() ) ) {
//		throw new BusinessException( ResponseMemberEnums.PLEASE_BINDING_PHONE );
//	    }

	    //会员卡信息

	    MemberCard cardEntity = memberCardDAO.selectById( memberEntity.getMcId() );
	    if ( cardEntity.getIsChecked() == 0 || cardEntity.getCardStatus() == 1 ) {
		throw new BusinessException( ResponseMemberEnums.CARD_STATUS );
	    }

	    MemberFind memberfind = memberFindDAO.findByQianDao( memberEntity.getBusId() );
	    if(CommonUtil.isNotEmpty( memberfind )) {
		map.put( "qindaoJifen", memberfind.getIntegral() );
		map.put( "userQindao",1 );
	    }else{
		map.put( "userQindao",0 );
	    }

	    MemberParameter memberParameter = memberParameterMapper.findByMemberId( memberEntity.getId() );
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
	    int systemCount = systemnoticecallDAO.findCountByMemberId( memberEntity.getId() );
	    int noticeCount = noticeUserMapper.findCountNotice( memberEntity.getId(), new Date() );
	    int count = systemCount + noticeCount;
	    map.put( "count", count );

	    // 根据卡号查询信息
	    List< Map< String,Object > > card = cardMapper.findCardById( memberEntity.getMcId() );
	    map.put( "card", card.get( 0 ) );

	    if ( CommonUtil.isEmpty( memberEntity.getHeadimgurl() ) ) {
		MemberParameter mp = memberParameterMapper.findByMemberId( memberEntity.getId() );
		if ( CommonUtil.isNotEmpty( mp ) ) {
		    map.put( "headimg", PropertiesUtil.getRes_web_path()+mp.getHeadImg() );
		}
	    } else {
		map.put( "headimg", memberEntity.getHeadimgurl() );
	    }

	    // 联盟卡查询
	    String unionUrl = PropertiesUtil.getUntion_url() + "/cardPhone/#/toUnionCard?busId=" + busId;
	    map.put( "unionUrl", unionUrl );


	    map.put( "path",PropertiesUtil.getRes_web_path() );
	    MemberGradetype memberGradetype=memberGradetypeDAO.selectById( cardEntity.getGtId() );

	    if(cardEntity.getCtId()==2) {
		MemberGiverule giveRule = memberGiveruleDAO.findBybusIdAndGtIdAndCtId( memberEntity.getBusId(), cardEntity.getGtId(), cardEntity.getCtId() );
		map.put( "zhuKadiscount",giveRule.getGrDiscount()/10.0 );
	    }

	    if(memberGradetype.getAssistantCard()==1){
	        //卡通副卡
		List<Integer> fuCardList=memberGradetypeAssistantDAO.findAssistantBygtId( busId,cardEntity.getGtId() );
		map.put( "fuCardList",fuCardList );  //卡通副卡集合
		for(Integer fuka:fuCardList){
		    //查询折扣卡折扣
		    if(fuka==2){
			MemberGradetypeAssistant memberGradetypeAssistant= memberGradetypeAssistantDAO.findAssistantBygtIdAndFuctId( busId,cardEntity.getGtId(),2);
			map.put( "fuKadiscount",memberGradetypeAssistant.getDiscount()/10.0);
		    }
		}
	    }
	    return map;
	} catch ( BusinessException e ) {
	    throw e;
	} catch ( Exception e ) {
	    LOG.error( "查询会员信息异常", e );
	    throw new BusinessException( ResponseEnums.ERROR );
	}
    }

    public Map< String,Object > findCardrecordNew( Map< String,Object > params, Integer memberId, Integer recordType ) {
	Map< String,Object > map = new HashMap<>();
	Integer page = CommonUtil.toInteger( params.get( "page" ) );
	Integer pageSize = 10;
	page=page*pageSize;

	//
	List< Integer > memberIds = memberCommonService.findMemberIds( memberId );
	List< Map< String,Object > > listMap = memberCardrecordNewDAO.findCardrecordByMemberIdAndRecordType( memberIds, recordType, page, pageSize );
	map.put( "page", listMap );
	MemberEntity memberEntity = memberEntityDAO.selectById( memberId );
	map.put( "jifen", memberEntity.getIntegral() );
	map.put( "fenbi", memberEntity.getFansCurrency() );
	map.put( "flow", memberEntity.getFlow() );
	return map;
    }

    public Map<String,Object> findBusUserFlow( Integer memberId,Integer busId ) {
	Map<String,Object> map=new HashMap<>(  );
        List< BusFlow > busFlows = busFlowDAO.getBusFlowsByUserId( busId );
	MemberEntity memberEntity=memberEntityDAO.selectById( memberId );
	map.put( "memberFlow",memberEntity.getFlow() );
	map.put( "busFlows",busFlows );

	return map;
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
	    String notifyUrl = PropertiesUtil.getWebHome() + "/memberNodoInterceptor/memberNotDo/changeFlow.do";
	    adcServicesInfo.setNotifyUrl( notifyUrl );
	    requestUtils.setReqdata( adcServicesInfo );
	    String result = requestService.changeFlow( requestUtils );
	    JSONObject json = JSONObject.parseObject( result );
	    if ( !"0".equals( CommonUtil.toString( json.get( "code" ) ) ) ) {
		throw new BusinessException( ResponseMemberEnums.ERROR_CHARGE_FLOW.getCode(), CommonUtil.toString( json.get( "msg" ) ) );
	    }
	    memberCommonService.saveCardRecordOrderCodeNew( memberId, 4, prizeCount.doubleValue(), "流量兑换中", busId, flowBalance.doubleValue(), orderCode, 0 );
	} catch ( BusinessException e ) {
	    throw e;
	}
    }

    public List< Map< String,Object > > findRecharge( Map< String,Object > params ) throws BusinessException {
	Integer busId = CommonUtil.toInteger( params.get( "busId" ) );
	Integer memberId = CommonUtil.toInteger( params.get( "memberId" ) );
	Integer ctId = CommonUtil.toInteger( params.get( "ctId" ) );
	if ( CommonUtil.isEmpty( busId ) || CommonUtil.isEmpty( memberId ) || CommonUtil.isEmpty( ctId ) ) {
	    throw new BusinessException( ResponseMemberEnums.NULL );
	}

	MemberEntity memberEntity = memberEntityDAO.selectById( memberId );
	MemberCard memberCard = cardMapper.selectById( memberEntity.getMcId() );
	MemberGiverule memberGiverule = memberGiveruleDAO.selectById( memberCard.getGrId() );
	return null;
    }

    /**
     * 会员权益
     *
     * @param member
     *
     * @return
     */
    public Map< String,Object > findMemberEquities( Member member ) throws BusinessException {
	try {
	    Map< String,Object > map = new HashMap<>();
	    MemberEntity memberEntity = memberMapper.selectById( member.getId() );
	    MemberCard card = cardMapper.selectById( member.getMcId() );
	    if ( CommonUtil.isNotEmpty( card ) ) {
		List< Map< String,Object > > giveRules = memberGiveruleDAO.findBybusIdAndCtId1( member.getBusid(), card.getCtId() );
		map.put( "giveRules", giveRules );

		List< Map< String,Object > > recharges = memberRechargegiveDAO.findBybusId( member.getBusid(), card.getCtId() );
		map.put( "recharges", recharges );
	    }
	    return map;
	} catch ( Exception e ) {
	    throw new BusinessException( ResponseEnums.ERROR );
	}
    }

    public Map< String,Object > findRecharge( String json, Integer busId, Integer memberId ) throws BusinessException {
	try {
	    JSONObject jsonObject = JSON.parseObject( json );
	    Map< String,Object > map = new HashMap<>();
	    MemberEntity memberEntity = memberEntityDAO.selectById( memberId );
	    MemberCard memberCard = cardMapper.selectById( memberEntity.getMcId() );
	    MemberDate memberDate = memberCommonService.findMemeberDate( busId, memberCard.getCtId() );
	    Integer chongzhiCtId = CommonUtil.toInteger( jsonObject.get( "chongzhiCtId" ) );

	    if ( memberCard.getCtId() != 4 && memberCard.getCtId() == chongzhiCtId ) {
		if ( CommonUtil.isNotEmpty( memberDate ) ) {
		    List< MemberRechargegive > recharges = memberRechargegiveDAO.findBybusIdAndGrId( busId, memberCard.getGrId(), 1 );
		    map.put( "recharges", recharges );
		    map.put( "cardDate", "1" );
		} else {
		    List< MemberRechargegive > recharges = memberRechargegiveDAO.findBybusIdAndGrId( busId, memberCard.getGrId(), 0 );
		    map.put( "recharges", recharges );
		    map.put( "cardDate", "0" );
		}
	    }
	    if ( memberCard.getCtId() == 4 && memberCard.getCtId() == chongzhiCtId ) {
		//时效卡
		List< Map< String,Object > > xiaoshikaRecharges = memberGiveruleDAO.findByBusIdAndCtId( busId, 4 );
		map.put( "xiaoshikaRecharges", xiaoshikaRecharges );
	    }

	    //查询会员模板是否开通副卡
	    MemberGradetype gradetype = memberGradetypeDAO.selectById( memberCard.getGtId() );
	    if ( gradetype.getAssistantCard() == 1 ) {
		//卡通副卡
		MemberGradetypeAssistant memberGradetypeAssistant = memberGradetypeAssistantDAO.findAssistantBygtIdAndFuctId( busId, memberCard.getGtId(), chongzhiCtId );
		if ( chongzhiCtId == 4 ) {
		    //副卡时效卡
		    List< Map< String,Object > > rechargegiveAssistant = memberRechargegiveAssistantDAO.findByAssistantId( busId, memberGradetypeAssistant.getId() );
		    map.put( "shixiaokarechargegive", rechargegiveAssistant );
		}

		if ( chongzhiCtId == 5 ) {
		    //副卡次卡
		    List< Map< String,Object > > rechargegiveAssistant = memberRechargegiveAssistantDAO.findByAssistantId( busId, memberGradetypeAssistant.getId() );
		    map.put( "cikakarechargegive", rechargegiveAssistant );
		}
	    }
	    return map;
	} catch ( Exception e ) {
	    throw new BusinessException( ResponseEnums.ERROR );
	}

    }

    public String rechargeMemberCard( String json, Integer busId, Integer memberId ) throws BusinessException {
	try {
	    Map< String,Object > params = JSON.toJavaObject( JSON.parseObject( json ), Map.class );

	    Integer ctId = CommonUtil.toInteger( params.get( "ctId" ) );  //充值选择的卡片
	    Double money = CommonUtil.toDouble( params.get( "money" ) );
	    String returnUrl = CommonUtil.toString( params.get( "returnUrl" ) );
	    Integer payWay = CommonUtil.toInteger( params.get( "payWay" ) );
	    MemberEntity memberEntity = memberEntityDAO.selectById( memberId );
	    MemberCard card = cardMapper.selectById( memberEntity.getMcId() );
	    if ( CommonUtil.isEmpty( card ) ) {
		throw new BusinessException( ResponseMemberEnums.MEMBER_NOT_CARD );
	    }

	    // 获取当前登录人所属门店
	    Integer shopId = 0;
	    WxShop wxShop = wxShopDAO.selectMainShopByBusId( busId );
	    if ( CommonUtil.isNotEmpty( wxShop ) ) {
		shopId = wxShop.getId();
	    }

	    // 添加会员记录
	    UserConsumeNew uc = new UserConsumeNew();
	    uc.setBusId( busId );
	    uc.setMemberId( memberEntity.getId() );
	    uc.setMcId( card.getMcId() );
	    uc.setCtId( card.getCtId() );
	    uc.setGtId( card.getGtId() );
	    uc.setRecordType( 1 );
	    uc.setUcType( 7 );
	    uc.setTotalMoney( money );
	    uc.setDiscountAfterMoney( money );
	    uc.setCreateDate( new Date() );
	    uc.setPayStatus( 0 );
	    uc.setIschongzhi( 1 );
	    uc.setFukaCtId( ctId );

	    String orderCode = CommonUtil.getMEOrderCode();
	    uc.setOrderCode( orderCode );
	    userConsumeNewDAO.insert( uc );

	    //微信和支付支付
	    SubQrPayParams sub = new SubQrPayParams();
	    sub.setTotalFee( uc.getDiscountAfterMoney() );
	    sub.setModel( 2 );
	    sub.setBusId( busId );
	    sub.setAppidType( 0 );
	    sub.setOrderNum( uc.getOrderCode() );
	    sub.setMemberId( uc.getMemberId() );
	    sub.setDesc( "会员卡充值" );
	    sub.setIsreturn( 1 );
	    sub.setReturnUrl( returnUrl );
	    String notifyUrl = PropertiesUtil.getWebHome() + "memberNodoInterceptor/memberNotDo/paySuccess";
	    sub.setNotifyUrl( notifyUrl );
	    sub.setIsSendMessage( 0 );
	    sub.setPayWay( payWay );
	    String url = requestService.payApi( sub );
	    return url;
	} catch ( BusinessException e ) {
	    throw e;
	} catch ( Exception e ) {
	    LOG.error( "充值调用支付异常", e );
	    throw new BusinessException( ResponseEnums.ERROR );
	}
    }

    public Map< String,Object > findMemberUser( Map< String,Object > params, Integer memberId ) throws BusinessException {
	Map< String,Object > map = new HashMap<>();

	Integer busId = CommonUtil.toInteger( params.get( "busId" ) );
	// 查询会员资料设置
	MemberOption memberOption = memberOptionDAO.findByBusId( busId );
	map.put( "memberOption", memberOption );
	MemberEntity memberEntity = memberEntityDAO.selectById( memberId );
	map.put( "member", memberEntity );
	MemberParameter memberParamter = memberParameterMapper.findByMemberId( memberId );
	map.put( "memberParamter", memberParamter );
	List< Map< String,Object > > provinceList = basisCityDAO.findBasisCity();
	map.put( "provinceList", provinceList );
	if ( CommonUtil.isNotEmpty( memberParamter ) && CommonUtil.isNotEmpty( memberParamter.getProvinceCode() ) ) {
	    List< Map< String,Object > > cityList = basisCityDAO.findBaseisCityByCode( memberParamter.getProvinceCode() );
	    map.put( "cityList", cityList );
	}

	if ( CommonUtil.isNotEmpty( memberParamter ) && CommonUtil.isNotEmpty( memberParamter.getCityCode() ) ) {
	    List< Map< String,Object > > countyList = basisCityDAO.findBaseisCityByCode( memberParamter.getCityCode() );
	    map.put( "countyList", countyList );
	}
	return map;
    }

    public String findCardNoByMemberId( Integer memberId ) {
	MemberEntity memberEntity = memberEntityDAO.selectById( memberId );
	MemberCard memberCard = memberCardDAO.selectById( memberEntity.getMcId() );
	String cardNoEncrypt = EncryptUtil.encrypt( PropertiesUtil.getCardNoKey(), memberCard.getCardNo() );
	return cardNoEncrypt;
    }

    public List<BusFlow> findBusFlowByBusId(Integer busId){
       return busFlowDAO.getBusFlowsByUserId( busId );
    }

    @Transactional
    public void qiandao(Integer memberId,Integer busId)throws BusinessException {
	try {
	    MemberFind memberFind = memberFindDAO.findByQianDao( busId );
	    if ( CommonUtil.isEmpty( memberFind ) ) {
		throw new BusinessException( ResponseMemberEnums.NOT_SET_QIANDAO );
	    }
	    MemberParameter memberParameter = memberParameterMapper.findByMemberId( memberId );
	    if ( CommonUtil.isNotEmpty( memberParameter.getSignDate() ) && DateTimeKit.isSameDay( memberParameter.getSignDate(), new Date() ) ) {
		throw new BusinessException( ResponseMemberEnums.MEMBER_QIANDAO );
	    }

	    MemberEntity memberEntity = memberEntityDAO.selectById( memberId );
	    Integer balaceJifen = memberEntity.getIntegral() + memberFind.getIntegral();

	    MemberEntity m = new MemberEntity();
	    m.setId( memberId );
	    m.setIntegral( balaceJifen );
	    memberMapper.updateById( m );

	    if ( CommonUtil.isEmpty( memberParameter ) ) {
		memberParameter = new MemberParameter();
		memberParameter.setMemberId( memberId );
		memberParameter.setSignDate( new Date() );
		memberParameterMapper.insert( memberParameter );
	    } else {
		MemberParameter mp = new MemberParameter();
		mp.setId( memberParameter.getId() );
		mp.setSignDate( new Date() );
		memberParameterMapper.updateById( mp );
	    }
	    memberCommonService.saveCardRecordOrderCodeNew( memberId, 2, memberFind.getIntegral().doubleValue(), "签到送积分", busId, balaceJifen.doubleValue(), "", 1 );
	} catch ( BusinessException e ) {
	    throw e;
	} catch ( Exception e ) {
	    LOG.error( "签到失败", e );
	    throw new BusinessException( ResponseEnums.ERROR );

	}
    }



    public List<Map<String,Object>> findSystemNotice(Integer memberId){
	List<Map<String, Object>> systemNotices=systemnoticecallDAO.findByMemberId( memberId );
	systemnoticecallDAO.updateByMemberId(memberId);
	return systemNotices;
    }

    public List<Map<String,Object>> findMemberNotice(Integer memberId){
	List<Map<String, Object>> noticeUsers = noticeUserMapper.findNotice(memberId, DateTimeKit.getDateTime());
	noticeUserMapper.updateStatus(memberId);
	return noticeUsers;
    }


    public Map<String,Object> findRecommend(Integer memberId )throws  BusinessException{
       // memberRecommendDAO.countRecommendByCardId(  )
	return null;
    }

}
