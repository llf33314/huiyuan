package com.gt.member.service.member.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gt.api.bean.session.Member;
import com.gt.api.enums.ResponseEnums;
import com.gt.api.util.KeysUtil;
import com.gt.api.util.RequestUtils;
import com.gt.api.util.SessionUtils;
import com.gt.common.entity.*;
import com.gt.member.constant.CommonConst;
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
import com.gt.util.entity.param.pay.ApiEnterprisePayment;
import com.gt.util.entity.param.pay.SubQrPayParams;
import com.gt.util.entity.param.sms.OldApiSms;
import com.gt.util.entity.result.shop.WsWxShopInfoExtend;
import com.gt.util.entity.result.wx.WxJsSdkResult;
import org.apache.ibatis.builder.BuilderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.smartcardio.Card;
import java.text.DecimalFormat;
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

    @Autowired
    private MemberPicklogDAO memberPicklogDAO;

    @Autowired
    private PublicParametersetDAO publicParametersetDAO;

    @Autowired
    private MemberCardLentDAO memberCardLentDAO;

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
	String returnUrl =PropertiesUtil.getWebHome()+"/html/phone/index.html#/home/"+consumeNew.getBusId();
	String sucessUrl = PropertiesUtil.getWebHome() + "/memberNodoInterceptor/memberNotDo/buyCardPaySuccess.do";
	subQrPayParams.setReturnUrl( returnUrl );
	subQrPayParams.setNotifyUrl( sucessUrl );//异步回调，注：1、会传out_trade_no--订单号,payType--支付类型(0:微信，1：支付宝2：多粉钱包),2接收到请求处理完成后，必须返回回调结果：code(0:成功,-1:失败),msg(处理结果,如:成功)
	subQrPayParams.setIsSendMessage( 0 );//是否需要消息推送,1:需要(sendUrl比传),0:不需要(为0时sendUrl不用传)
	//  subQrPayParams.setSendUrl( PropertiesUtil.getHomeUrl() + "mallOrder/toIndex.do" );//推送路径(尽量不要带参数)

	subQrPayParams.setPayWay( payType );//支付方式  0----系统根据浏览器判断   1---微信支付 2---支付宝 3---多粉钱包支付
	String url = requestService.payApi(subQrPayParams);
	return url;
    }

    /**
     * @param busId
     *
     * @return
     */
    public Map< String,Object > findLingquData( HttpServletRequest request, Integer busId ) {
	Map< String,Object > map = new HashMap<>();
	//查询支付方式
	List<Map<String,Object>> payTypes= requestService.getPayType(busId );
	map.put( "payTypes",payTypes );

	List< Map< String,Object > > gradeTypes = memberGradetypeDAO.findBybusId1( busId );
	map.put( "gradeTypes", gradeTypes );

	MemberOption memberoption = memberOptionDAO.findByBusId( busId );
	map.put( "memberoption", memberoption );

	//省
	List< Map< String,Object > > basisCitys = basisCityDAO.findBasisCity();
	map.put( "basisCitys", basisCitys );

	map.put( "loginImg", requestService.loginImg( busId ) );
	return map;
    }


    @Transactional
    public void judgeMemberCard(Integer memberId, Integer busId,
		    String phone, String vcode,Integer areaId,String areacode){
	try {
	    if (CommonUtil.isEmpty(vcode)) {
		throw new BusinessException( ResponseMemberEnums.PLEASE_PHONE_CODE );
	    }
	    String value = redisCacheUtil.get( phone + "_" + vcode );
	    if ( CommonUtil.isEmpty( value ) ) {
		throw new BusinessException( ResponseMemberEnums.NO_PHONE_CODE );
	    }
	    MemberEntity member = memberMapper.selectById(memberId);
	    memberCommonService.newMemberMerge(member,busId, phone); // 数据合并
	    MemberEntity m=new MemberEntity();
	    m.setId(memberId);
	    m.setPhone(phone);
	    memberMapper.updateById(member);

	    MemberParameter memberParameter=memberParameterMapper.findByMemberId( memberId );
	    MemberParameter mp=new MemberParameter();
	    mp.setArerCode(areacode);
	    mp.setArerId( areaId);
	    if(CommonUtil.isNotEmpty( memberParameter )){
		mp.setId( memberParameter.getId() );
		memberParameterMapper.updateById( mp );
	    }else{
		memberParameterMapper.insert( mp );
	    }


	}catch ( BusinessException e ){
	    throw e;
	}catch (Exception e) {
	    LOG.error("领取会员卡绑定实体卡异常", e);
	    throw new BusinessException(ResponseEnums.ERROR);
	}
    }

    public List< Map< String,Object > > findBuyGradeTypes( String json ) {
	JSONObject params = JSON.parseObject( json );
	Integer busId = CommonUtil.toInteger( params.get( "busId" ) );
	Integer ctId = CommonUtil.toInteger( params.get( "ctId" ) );
	List< Map< String,Object > > gradeTypes = memberGradetypeDAO.findAllBybusId( busId, ctId );
	return gradeTypes;
    }


    public void judgeMember(Integer busId,String phone){
        MemberEntity memberEntity=memberEntityDAO.findByPhone( busId,phone );
        if(CommonUtil.isEmpty( memberEntity ) || CommonUtil.isEmpty( memberEntity.getMcId() )){
            throw new BusinessException( ResponseMemberEnums.NOT_MEMBER_CAR );
	}
    }


    /**
     * 已有会员卡，合并数据并登录
     * @param params
     * @return
     * @throws BusinessException
     */
    public void loginMemberCard(Map< String,Object > params,Integer memberId  ) throws BusinessException{
	Map<String,Object> map=new HashMap<>(  );
	Integer busId = CommonUtil.toInteger( params.get( "busId" ) );
	String phone = CommonUtil.toString( params.get( "phone" ) );
	MemberEntity memberEntity = memberEntityDAO.selectById( memberId );
	String code = CommonUtil.toString( params.get( "code" ) );
	String value = redisCacheUtil.get( phone + "_" + code );
	if ( CommonUtil.isEmpty( value ) ) {
	    throw new BusinessException( ResponseMemberEnums.NO_PHONE_CODE );
	}

	// 判断相同的手机号码存在会员卡
	memberCommonService.newMemberMerge( memberEntity, busId, phone );
	}


    /**
     * uc端注册并领取会员卡
     *
     * @throws Exception
     */
    @Transactional
    public Map<String,Object> linquMemberCard(HttpServletRequest request, Map< String,Object > params, Integer memberId ) throws BusinessException {
	try {
	    Map<String,Object> map=new HashMap<>(  );
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
	    if( CommonUtil.isEmpty( wxShop )){
	        throw new BusinessException( ResponseMemberEnums.PLESAS_SET_SHOP );
	    }

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

		if ( CommonUtil.isNotEmpty( params.get( "areacode" ) ) ) {
		    mp.setArerCode( params.get( "areacode" ).toString() );
		    flag = true;
		}
		if ( CommonUtil.isNotEmpty( params.get( "id" ) ) ) {
		    mp.setArerId( CommonUtil.toInteger( params.get( "id" ) ));
		    flag = true;
		}

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

		card.setIsChecked( CommonUtil.toInteger( gradeTypes.get( 0 ).get( "isCheck" )  ));
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
			recommend.setName( memberEntity.getNickname() );
			recommend.setMemberId( member2.getId() );
			recommend.setCode( tuijianCode );
			recommend.setPhone( phone );
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
		map.put( "pleasyBuyCard" ,0);
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
		//数据来源 0:pc端 1:微信 2:uc端 3:小程序 4魔盒 5:ERP
		Integer dataSource=memberCommonService.dataSource(request);
		uc.setDataSource( dataSource );

		userConsumeNewDAO.insert( uc );

		Integer payType = CommonUtil.toInteger( params.get( "payType" ) );
		String url = wxPayWay( uc, payType );
		map.put( "pleasyBuyCard" ,1);
		map.put( "url" ,url);
	    }
	    return map;
	} catch ( BusinessException e ) {
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
	    if ( CommonUtil.isEmpty( memberEntity.getPhone() ) ) {
		map.put( "fanMember", 1 );
	    }

	    //会员卡信息
	    MemberCard cardEntity = memberCardDAO.selectById( memberEntity.getMcId() );
	    if ( cardEntity.getIsChecked() == 0 || cardEntity.getCardStatus() == 1 ) {
		map.put( "status", 1 );
	    }

	    MemberFind memberfind = memberFindDAO.findByQianDao( memberEntity.getBusId() );
	    if ( CommonUtil.isNotEmpty( memberfind ) ) {
		map.put( "qindaoJifen", memberfind.getIntegral() );
		map.put( "userQindao", 1 );
	    } else {
		map.put( "userQindao", 0 );
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
		    map.put( "headimg", PropertiesUtil.getRes_web_path() + mp.getHeadImg() );
		}
	    } else {
		map.put( "headimg", memberEntity.getHeadimgurl() );
	    }

	    String duofenCardUrl=PropertiesUtil.getWxmp_home()+"/phone_2MemberController/79B4DE7C/memberCardList_1.do?busId=42";
	    map.put( "duofenCardUrl", duofenCardUrl );
	    // 联盟卡查询
	    String unionUrl = PropertiesUtil.getUntion_url() + "/cardPhone/#/toUnionCard?busId=" + busId;
	    map.put( "unionUrl", unionUrl );

	    map.put( "webType",0 );

	    map.put( "path", PropertiesUtil.getRes_web_path() );
	    MemberGradetype memberGradetype = memberGradetypeDAO.selectById( cardEntity.getGtId() );

	    if ( cardEntity.getCtId() == 2 ) {
		MemberGiverule giveRule = memberGiveruleDAO.findBybusIdAndGtIdAndCtId( memberEntity.getBusId(), cardEntity.getGtId(), cardEntity.getCtId() );
		map.put( "zhuKadiscount", giveRule.getGrDiscount() / 10.0 );
	    }

	    if ( memberGradetype.getAssistantCard() == 1 ) {
		//卡通副卡
		List< Integer > fuCardList = memberGradetypeAssistantDAO.findAssistantBygtId( busId, cardEntity.getGtId() );
		map.put( "fuCardList", fuCardList );  //卡通副卡集合
		for ( Integer fuka : fuCardList ) {
		    //查询折扣卡折扣
		    if ( fuka == 2 ) {
			MemberGradetypeAssistant memberGradetypeAssistant = memberGradetypeAssistantDAO.findAssistantBygtIdAndFuctId( busId, cardEntity.getGtId(), 2 );
			map.put( "fuKadiscount", memberGradetypeAssistant.getDiscount());
		    }
		}
	    }

	    //
	  PublicParameterset parameterset=publicParametersetDAO.findBybusId( busId );
	    if(CommonUtil.isNotEmpty( parameterset ) && parameterset.getButtonType()==0){
	        String youhuiMaidanUrl=PropertiesUtil.getWxmp_home()+"/phone_2MemberController/79B4DE7C/discountPay.do?busId="+busId;
	        map.put( "youhuiMaidanUrl",youhuiMaidanUrl );
	    }
	    map.put( "parameterset",parameterset );
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
	page = (page-1) * pageSize;

	//
	List< Integer > memberIds = memberCommonService.findMemberIds( memberId );
	List< Map< String,Object > > listMap = memberCardrecordNewDAO.findCardrecordByMemberIdAndRecordType( memberIds, recordType, page, pageSize );
	map.put( "page", listMap );
	MemberEntity memberEntity = memberEntityDAO.selectById( memberId );
	map.put( "jifen", memberEntity.getIntegral() );
	map.put( "fenbi", memberEntity.getFansCurrency() );
	map.put( "flow", memberEntity.getFlow() );
	String jifenUrl = PropertiesUtil.getMallHome() + "/html/phone/index.html/#/integral/index/" + memberEntity.getBusId();
	map.put( "jifenUrl", jifenUrl );
	return map;
    }

    public Map< String,Object > findBusUserFlow( Integer memberId, Integer busId ) {
	Map< String,Object > map = new HashMap<>();
	List< BusFlow > busFlows = busFlowDAO.getBusFlowsByUserId( busId );
	MemberEntity memberEntity = memberEntityDAO.selectById( memberId );
	map.put( "memberFlow", memberEntity.getFlow() );
	map.put( "busFlows", busFlows );
	map.put( "phone", memberEntity.getPhone() );

	return map;
    }

    @Rollback
    public void changeFlow( Map< String,Object > params, Integer memberId ) throws BusinessException {
	try {
	    Integer busId = CommonUtil.toInteger( params.get( "busId" ) );
	    String phone = CommonUtil.toString( params.get( "phone" ) );
	    if ( CommonUtil.isEmpty( phone ) ) {
		throw new BusinessException( ResponseMemberEnums.NO_DATA );
	    }

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
	    MemberEntity m=new MemberEntity();
	    m.setFlow( flowBalance );
	    m.setId( memberEntity.getId() );
	    memberEntityDAO.updateById( m );

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
	    MemberCard card = cardMapper.selectById( memberEntity.getMcId() );
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

    public Map< String,Object > findRecharge(HttpServletRequest request,String json, Integer busId, Integer memberId ) throws BusinessException {
	try {
	    JSONObject jsonObject = JSON.parseObject( json );
	    Map< String,Object > map = new HashMap<>();
	    MemberEntity memberEntity = memberEntityDAO.selectById( memberId );
	    MemberCard memberCard = cardMapper.selectById( memberEntity.getMcId() );
	    MemberDate memberDate = memberCommonService.findMemeberDate( busId, memberCard.getCtId() );
	    Integer chongzhiCtId = CommonUtil.toInteger( jsonObject.get( "chongzhiCtId" ) );
	    map.put( "cikaCiShu",memberCard.getFrequency() );
	    map.put( "chuZhikamoney",memberCard.getMoney() );
	    map.put( "shixiaoKaTime",memberCard.getExpireDate() );

	    //支付方式
	    List<Map<String,Object>> payTypes= requestService.getPayType(busId );
	    map.put( "payTypes",payTypes );

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
	    map.put( "ctId",memberCard.getCtId() );
	    map.put( "chongzhiCtId",chongzhiCtId );
	    return map;
	} catch ( Exception e ) {
	    throw new BusinessException( ResponseEnums.ERROR );
	}

    }

    public String rechargeMemberCard( HttpServletRequest request,String json, Integer busId, Integer memberId ) throws BusinessException {
	try {
	    Map< String,Object > params = JSON.toJavaObject( JSON.parseObject( json ), Map.class );

	    Integer ctId = CommonUtil.toInteger( params.get( "ctId" ) );  //充值选择的卡片
	    Double money = CommonUtil.toDouble( params.get( "money" ) );
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
	    Integer dataSource=memberCommonService.dataSource(request);
	    uc.setDataSource( dataSource );

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
	    String returnUrl=PropertiesUtil.getWebHome()+"/html/phone/index.html#/home/"+busId;
	    sub.setReturnUrl( returnUrl );
	    String notifyUrl = PropertiesUtil.getWebHome() + "/memberNodoInterceptor/memberNotDo/paySuccess";
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

    public void updateMemberUser( String json, Integer memberId ) throws BusinessException {

	Map< String,Object > parma = JSON.parseObject( json, Map.class );

	MemberEntity memberOld = memberMapper.selectById( memberId );

	MemberEntity member = new MemberEntity();

	if ( CommonUtil.isNotEmpty( parma.get( "phone" ) ) ) {
	    member.setPhone( parma.get( "phone" ).toString() );
	}

	if ( CommonUtil.isNotEmpty( parma.get( "pwd" ) ) ) {
	    member.setPwd( SHA1.encode( parma.get( "pwd" ).toString() ) );
	}

	// 修改手机号
	if ( ( CommonUtil.isEmpty( memberOld.getPhone() ) && CommonUtil.isNotEmpty( member.getPhone() ) ) || ( !memberOld.getPhone().equals( member.getPhone() ) ) ) {
	    Object vcode = parma.get( "vcode" );
	    if ( CommonUtil.isEmpty( vcode ) ) {
		throw new BusinessException( ResponseMemberEnums.PLEASE_PHONE_CODE );
	    }
	    String vcode1 = redisCacheUtil.get( member.getPhone() + "_" + vcode );
	    if ( CommonUtil.isEmpty( vcode1 ) ) {
		throw new BusinessException( ResponseMemberEnums.NO_PHONE_CODE );
	    }
	    memberCommonService.newMemberMerge( memberOld, memberOld.getBusId(), member.getPhone() );
	}

	boolean bool=false;
	MemberParameter memberParameter = new MemberParameter();

	if ( CommonUtil.isNotEmpty( parma.get( "areacode" ) ) ) {
	    memberParameter.setArerCode( parma.get( "areacode" ).toString() );
	    bool = true;
	}
	if ( CommonUtil.isNotEmpty( parma.get( "id" ) ) ) {
	    memberParameter.setArerId( CommonUtil.toInteger( parma.get( "id" ) ));
	    bool = true;
	}

	if ( CommonUtil.isNotEmpty( parma.get( "provincecode" ) ) ) {
	    memberParameter.setProvinceCode( parma.get( "provincecode" ).toString() );
	    bool=true;
	}
	if ( CommonUtil.isNotEmpty( parma.get( "city" ) ) ) {
	    memberParameter.setCityCode( parma.get( "city" ).toString() );
	    bool=true;
	}
	if ( CommonUtil.isNotEmpty( parma.get( "countyCode" ) ) ) {
	    memberParameter.setCountyCode( parma.get( "countyCode" ).toString() );
	    bool=true;
	}

	if ( CommonUtil.isNotEmpty( parma.get( "address" ) ) ) {
	    memberParameter.setAddress( parma.get( "address" ).toString() );
	    bool=true;
	}

	if ( CommonUtil.isNotEmpty( parma.get( "getmoney" ) ) ) {
	    memberParameter.setGetMoney( CommonUtil.toDouble( parma.get( "getmoney" ) ) );
	    bool=true;
	}

	MemberParameter memberParameter1 = memberParameterMapper.findByMemberId( memberId);

	if(bool) {
	    if ( CommonUtil.isEmpty( memberParameter1 ) ) {
		memberParameter.setMemberId( memberId );
		memberParameterMapper.insert( memberParameter );
	    } else {
		memberParameter.setId( memberParameter1.getId() );
		memberParameterMapper.updateById( memberParameter );
	    }
	}

	if ( parma.get( "email" ) != null ) {
	    member.setEmail( parma.get( "email" ).toString() );
	} else {
	    member.setEmail( "" );
	}
	if ( parma.get( "name" ) != null ) {
	    member.setName( parma.get( "name" ).toString() );
	}

	if ( CommonUtil.isNotEmpty( parma.get( "gender" ) ) ) {
	    member.setSex( Integer.parseInt( parma.get( "gender" ).toString() ) );
	}

	if ( parma.get( "birth" ) != null ) {
	    member.setBirth( DateTimeKit.parseDate( parma.get( "birth" ).toString(),"yyyy-MM-dd" ) );
	}
	if ( CommonUtil.isNotEmpty( parma.get( "cardId" ) ) ) {
	    member.setCardId( parma.get( "cardId" ).toString() );
	} else {
	    member.setCardId( "" );
	}

	if ( CommonUtil.isNotEmpty( parma.get( "imageurls" ) ) ) {
	    String[] str = parma.get( "imageurls" ).toString().split( "," );
	    for ( int i = 0; i < str.length; i++ ) {
		if ( CommonUtil.isNotEmpty( str[i] ) ) {
		    if ( CommonUtil.isEmpty( member.getCardImg() ) ) {
			member.setCardImg( str[i].split( "/upload" )[1] );
		    } else if ( CommonUtil.isEmpty( member.getCardImgback() ) ) {
			member.setCardImgback( str[i].split( "/upload" )[1] );
			break;
		    }
		    member.setCardChecked( 1 );
		}
	    }
	}
	member.setId( memberId );

	memberMapper.updateById( member );

	// 判断是否是否是完善资料 还是修改资料
	memberCommonService.giveMemberGift( memberOld, memberParameter1 );
    }

    public String findCardNoByMemberId( Integer memberId ) throws Exception{
	MemberEntity memberEntity = memberEntityDAO.selectById( memberId );
	MemberCard memberCard = memberCardDAO.selectById( memberEntity.getMcId() );
	String cardNo = memberCard.getCardNo() + "?time=" + new Date().getTime();
	String cardNoEncrypt = EncryptUtil.encrypt( PropertiesUtil.getCardNoKey(), cardNo );
	return cardNoEncrypt;
    }

    public List< BusFlow > findBusFlowByBusId( Integer busId ) {
	return busFlowDAO.getBusFlowsByUserId( busId );
    }

    @Transactional
    public void qiandao( Integer memberId, Integer busId ) throws BusinessException {
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

    public Map< String,Object > findMemberNotice( Integer memberId ) {
	Map< String,Object > map = new HashMap<>();
	List< Map< String,Object > > noReadnoticeUsers = noticeUserMapper.findNotice( memberId, 0, DateTimeKit.getDateTime() );
	map.put( "noReadnoticeUsers", noReadnoticeUsers );

	List< Map< String,Object > > readnoticeUsers = noticeUserMapper.findNotice( memberId, 1, DateTimeKit.getDateTime() );
	map.put( "readnoticeUsers", readnoticeUsers );

	List< Map< String,Object > > noReadsystemNoticeS = systemnoticecallDAO.findByMemberId( memberId, 0 );
	map.put( "noReadsystemNoticeS", noReadsystemNoticeS );

	List< Map< String,Object > > readsystemNoticeS = systemnoticecallDAO.findByMemberId( memberId, 1 );
	map.put( "readsystemNoticeS", readsystemNoticeS );

	noticeUserMapper.updateStatus( memberId );
	systemnoticecallDAO.updateByMemberId( memberId );
	return map;

    }

    public Map< String,Object > findRecommend( HttpServletRequest request, Integer memberId ) throws BusinessException {
	MemberEntity memberEntity = memberEntityDAO.selectById( memberId );
	Map< String,Object > map = new HashMap<>();
	List< Integer > ids = memberCommonService.findMemberIds( memberId );
	List< Map< String,Object > > recommends = memberRecommendDAO.findByMemberId( ids );
	map.put( "recommends", recommends );

	Integer jifenMemberCount = 0;
	Double fenbiMemberCount = 0.0;
	Integer flowMemberCount = 0;
	Double memberMoneyCount = 0.0;
	Integer memberCount = 0;

	Integer jifenYhjCount = 0;
	Double fenbiYhjCount = 0.0;
	Integer flowYhjCount = 0;
	Integer getCount = 0;
	Integer useCount = 0;
	Double userMoney = 0.0;

	for ( int i = 0; i < recommends.size(); i++ ) {
	    //优惠券
	    if ( "1".equals( CommonUtil.toString( recommends.get( i ).get( "recommendType" ) ) ) ) {
		getCount = getCount + CommonUtil.toInteger( recommends.get( i ).get( "lingquNum" ) );
		useCount = useCount + CommonUtil.toInteger( recommends.get( i ).get( "userNum" ) );
		userMoney = userMoney + CommonUtil.toInteger( recommends.get( i ).get( "userNum" ) ) * CommonUtil.toDouble( recommends.get( i ).get( "money" ) );
		jifenYhjCount += CommonUtil.toInteger( recommends.get( i ).get( "userNum" ) ) * CommonUtil.toInteger( recommends.get( i ).get( "integral" ) );
		fenbiYhjCount += CommonUtil.toInteger( recommends.get( i ).get( "userNum" ) ) * CommonUtil.toDouble( recommends.get( i ).get( "fenbi" ) );
		flowYhjCount += CommonUtil.toInteger( recommends.get( i ).get( "userNum" ) ) * CommonUtil.toInteger( recommends.get( i ).get( "flow" ) );

	    } else {
		memberCount++;
		jifenMemberCount += CommonUtil.toInteger( recommends.get( i ).get( "integral" ) );
		fenbiMemberCount += CommonUtil.toDouble( recommends.get( i ).get( "fenbi" ) );
		flowMemberCount += CommonUtil.toInteger( recommends.get( i ).get( "flow" ) );
		if ( CommonUtil.isNotEmpty( recommends.get( i ).get( "money" ) ) ) {
		    memberMoneyCount += CommonUtil.toDouble( recommends.get( i ).get( "money" ) );
		}
	    }
	}
	map.put( "jifenYhjCount", jifenYhjCount );
	map.put( "fenbiYhjCount", fenbiYhjCount );
	map.put( "flowYhjCount", flowYhjCount );

	map.put( "jifenMemberCount", jifenMemberCount );
	map.put( "fenbiMemberCount", fenbiMemberCount );
	map.put( "flowMemberCount", flowMemberCount );
	map.put( "memberMoneyCount", memberMoneyCount );

	map.put( "memberCount", memberCount );
	map.put( "getCount", getCount );
	map.put( "useCount", useCount );
	map.put( "userMoney", userMoney );

	Integer browser = CommonUtil.judgeBrowser( request );
	WxPublicUsersEntity wxPublicUsers = wxPublicUsersMapper.selectByUserId( memberEntity.getBusId() );
	if ( browser.equals( 1 ) && CommonUtil.isNotEmpty( wxPublicUsers ) ) {
	    map.put( "payType", 1 );  //微信支付
	}

	List< MemberPicklog > pickList = memberPicklogDAO.findByMemberId( memberEntity.getBusId(), ids );
	map.put( "pickList", pickList );
	Double pickMoney = 0.0;
	for ( MemberPicklog memberPickLog : pickList ) {
	    if ( CommonUtil.isNotEmpty( memberPickLog.getPickMoney() ) ) {
		pickMoney = pickMoney + memberPickLog.getPickMoney();
	    }
	}
	map.put( "pickMoney", pickMoney );

	MemberCard card = cardMapper.selectById( memberEntity.getMcId() );
	PublicParameterset parameterset = publicParametersetDAO.findBybusId( memberEntity.getBusId() );
	map.put( "tuijianMoney", card.getGiveMoney() );
	if ( CommonUtil.isNotEmpty( parameterset ) ) {
	    map.put( "lessPickMoney", parameterset.getPickMoney() );
	}
	map.put( "code",card.getSystemcode() );

	return map;
    }

    @Transactional
    public void pickMoney( Integer memberId, Integer busId, Double pickMoney ) throws BusinessException {
	try {
	    WxPublicUsersEntity wxPublicUsers = wxPublicUsersMapper.selectByUserId( busId );
	    MemberEntity member = memberMapper.selectById( memberId );
	    MemberCard card = cardMapper.selectById( member.getMcId() );
	    PublicParameterset parameterset = publicParametersetDAO.findBybusId( busId );

	    if ( CommonUtil.isEmpty( parameterset ) ) {
		throw new BusinessException( ResponseMemberEnums.ERROR_USER_DEFINED.getCode(), "商家未设置提取最低值" );
	    }
	    if ( card.getGiveMoney() < parameterset.getPickMoney() || pickMoney < parameterset.getPickMoney() ) {
		throw new BusinessException( ResponseMemberEnums.ERROR_USER_DEFINED.getCode(), "提取金额不足，必须要大于" + parameterset.getPickMoney() + "元才能提取" );
	    }

	    RequestUtils< ApiEnterprisePayment > requestUtils = new RequestUtils< ApiEnterprisePayment >();
	    ApiEnterprisePayment apiEnterprisePayment = new ApiEnterprisePayment();
	    apiEnterprisePayment.setAppid( wxPublicUsers.getAppid() );
	    apiEnterprisePayment.setPartner_trade_no( "YJ" + new Date().getTime() );
	    apiEnterprisePayment.setOpenid( member.getOpenid() );
	    apiEnterprisePayment.setDesc( "会员推荐佣金发放" );
	    apiEnterprisePayment.setAmount( pickMoney );
	    apiEnterprisePayment.setBusId( busId );
	    apiEnterprisePayment.setModel( 14 );
	    apiEnterprisePayment.setPaySource( 0 );
	    requestUtils.setReqdata( apiEnterprisePayment );
	    Map< String,Object > returnMap = requestService.enterprisePayment( requestUtils );
	    if ( "0".equals( returnMap.get( "code" ).toString() ) ) {
		// 新增佣金提交记录
		MemberPicklog mpl = new MemberPicklog();
		mpl.setBusId( busId );
		mpl.setMemberId( memberId );
		mpl.setPickDate( new Date() );
		mpl.setPickMoney( pickMoney );
		memberPicklogDAO.insert( mpl );
		MemberCard c = new MemberCard();
		c.setMcId( card.getMcId() );
		Double balaceGiveMoney = card.getGiveMoney() - pickMoney;
		c.setGiveMoney( balaceGiveMoney );
		cardMapper.updateById( c );
	    } else {
		LOG.error( "用户领取推荐佣金调用微信企业支付失败:" + returnMap.get( "msg" ) );
		throw new BusinessException( ResponseMemberEnums.ERROR_USER_DEFINED.getCode(), returnMap.get( "msg" ).toString() );
	    }
	} catch ( BusinessException e ) {
	    throw e;
	} catch ( Exception e ) {
	    LOG.error( "用户领取推荐佣金", e );
	    throw new BusinessException( ResponseEnums.ERROR );
	}
    }

    public List< Map< String,Object > > updateImage( HttpServletRequest request, Integer memberId, Integer busId ) throws BusinessException {
	List< Map< String,Object > > list = new ArrayList<>();
	if ( request instanceof MultipartHttpServletRequest ) {
	    MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
	    List< MultipartFile > userfile = multipartRequest.getFiles( "file" );
	    if ( CommonUtil.isNotEmpty( userfile ) && userfile.size() != 0 ) {
		for ( int i = 0; i < userfile.size(); i++ ) {
		    MultipartFile file = userfile.get( i );
		    Map< String,Object > map = new HashMap<>();
		    Map< String,Object > returnMap = CommonUtil.fileUploadByBusUser( file, busId );
		    if ( "0".equals( CommonUtil.toString( returnMap.get( "code" ) ) ) ) {
			map.put( "url", returnMap.get( "url" ) );
			map.put( "pathHead", PropertiesUtil.getRes_web_path() );
		    } else {
			map.put( "message", "上传文件失败" );
		    }
		    list.add( map );
		}
	    }
	}
	return list;
    }

    public List< Map > findWxShop( Integer busId, Double longt1, Double lat1 ) throws BusinessException {
        List<Map >  list = requestService.findShopAllByBusId( busId );
	if ( CommonUtil.isEmpty( longt1 ) || CommonUtil.isEmpty( lat1 ) ) {
	    return list;
	}
	List< Map > returnList = new ArrayList<>();
	DecimalFormat df   = new DecimalFormat("######0.00");
	for ( Map map : list ) {
	    Double longitude = CommonUtil.toDouble(  map.get( "longitude" ) );
	    Double latitude =  CommonUtil.toDouble(map.get( "latitude" ));
	    Double distance = CommonUtil.getDistance( longitude, latitude, longt1, lat1 );
	    map.put( "distance", df.format( distance/1000.0 ) );
	    returnList.add( map );
	}
	return returnList;
    }

    public String tuijianQRcode( Integer busId,String systemCode) {
	String url = PropertiesUtil.getWebHome() + "/html/phone/index.html#/home/" + busId + "/" + systemCode;
	return url;
    }

    public Map<String,Object> judgeTuijian(Integer memberId,String systemCode){
        Map<String,Object> map=new HashMap<>(  );
	map.put( "tuijianCode",1 );
        MemberEntity memberEntity=memberEntityDAO.selectById( memberId );
        if(CommonUtil.isNotEmpty( memberEntity ) && CommonUtil.isNotEmpty( memberEntity.getMcId() )){
            MemberCard memberCard=memberCardDAO.selectById(memberEntity.getMcId() );
            if(systemCode.equals( memberCard.getSystemcode() )){
		map.put( "tuijianCode",0 );
	    }
	}
	return map;
    }

    public WxJsSdkResult wxshareCard( Integer memberId, Integer busId,String url ) {
	WxPublicUsersEntity wxPublicUsersEntity = wxPublicUsersMapper.selectByUserId( busId );
	if ( CommonUtil.isEmpty( wxPublicUsersEntity ) ) {
	    return null;
	}
	return requestService.wxShare( wxPublicUsersEntity.getId(), url );

    }


    public Map<String,Object> findMemberCardNo(Integer memberId){
	Map<String,Object> map=new HashMap<>(  );
	MemberEntity memberEntity = memberMapper.selectById( memberId );
	MemberCard card = memberCardDAO.selectById( memberEntity.getMcId() );
	map.put( "cardNo",card.getCardNo() );
	map.put( "ctId",card.getCtId() );
	map.put( "money",card.getMoney() );
	return map;
    }

    public String memberLentMoney(Integer memberId,Double money)throws BusinessException{
	MemberEntity memberEntity = memberMapper.selectById( memberId );
	MemberCard card = memberCardDAO.selectById( memberEntity.getMcId() );
	if(card.getMoney()<money){
	    throw new BusinessException( ResponseMemberEnums.MEMBER_LESS_MONEY );
	}
	MemberCardLent c = new MemberCardLent();
	String key = CommonUtil.getNominateCode(32);
	c.setMcId(card.getMcId());
	c.setCode(key);
	c.setUsestate(0);
	c.setLentMoney(money);
	c.setCreateDate(new Date());
	memberCardLentDAO.insert(c);
	return key;
    }

    public Map<String,Object> judgememberLent(Integer memberId,String memberLentKey){
	Map<String,Object> map=new HashMap<>(  );
	map.put( "lentCode",1 );
	MemberCardLent c=memberCardLentDAO.findByCode( memberLentKey );
	MemberEntity memberEntity=memberEntityDAO.selectById( memberId );
	if(CommonUtil.isNotEmpty( memberEntity.getMcId() ) && CommonUtil.toInteger( memberEntity.getMcId() ).equals( c.getMcId() )){
	    map.put( "lentCode",0 );
	}
	return map;
    }

}
