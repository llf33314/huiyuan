package com.gt.member.service.memberApi;

import com.alibaba.fastjson.JSON;
import com.gt.api.enums.ResponseEnums;
import com.gt.member.dao.*;
import com.gt.member.entity.*;
import com.gt.member.enums.ResponseMemberEnums;
import com.gt.member.exception.BusinessException;
import com.gt.member.service.common.membercard.MemberCommonService;
import com.gt.member.service.member.SystemMsgService;
import com.gt.member.util.CommonUtil;
import com.gt.member.util.DateTimeKit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/11/24.
 */
@Service
public class MemberNodoInterceptorServiceImp implements MemberNodoInterceptorService {

    private static final Logger LOG = LoggerFactory.getLogger( MemberNodoInterceptorServiceImp.class );

    @Autowired
    private MemberEntityDAO memberDAO;

    @Autowired
    private SystemMsgService systemMsgService;

    @Autowired
    private MemberCommonService memberCommonService;

    @Autowired
    private MemberCardDAO memberCardDAO;

    @Autowired
    private UserConsumeNewDAO userConsumeNewDAO;

    @Autowired
    private UserConsumePayDAO userConsumePayDAO;

    @Autowired
    private MemberNoticeuserDAO memberNoticeuserDAO;

    @Autowired
    private MemberGiveruleDAO memberGiveruleDAO;

    @Transactional
    public void changeFlow( Map< String,Object > params ) throws BusinessException {
	try {
	    Integer id = CommonUtil.toInteger( params.get( "id" ) );
	    Integer status = CommonUtil.toInteger( params.get( "status" ) );
	    if ( CommonUtil.isEmpty( id ) || CommonUtil.isEmpty( status ) ) {
		throw new BusinessException( ResponseMemberEnums.NULL );
	    }

	    UserConsumeNew uc = userConsumeNewDAO.selectById( id );
	    if ( CommonUtil.isEmpty( uc ) ) {
		LOG.error( "流量兑换订单数据不存在" + id );
		throw new BusinessException( ResponseMemberEnums.NOT_ORDER );
	    }
	    if ( status == 0 ) {
		//兑换成功
		UserConsumeNew newUc = new UserConsumeNew();
		newUc.setId( id );
		newUc.setFlowState( 1 );
		userConsumeNewDAO.updateById( newUc );
		memberCommonService.saveCardRecordOrderCodeNew( uc.getMemberId(), 4, uc.getChangeFlow().doubleValue(), "流量兑换成功", uc.getBusId(), uc.getFlowbalance().doubleValue(),
				uc.getOrderCode(), 0 );
	    } else {
		//兑换失败  流量回滚
		MemberEntity memberEntity = memberDAO.selectById( uc.getMemberId() );
		if ( CommonUtil.isEmpty( memberEntity ) ) {
		    memberEntity = memberDAO.findByMcIdAndbusId( uc.getBusId(), uc.getMcId() );
		}
		Integer flow = memberEntity.getFlow() + uc.getFlowbalance();
		MemberEntity m1 = new MemberEntity();
		m1.setId( memberEntity.getId() );
		m1.setFlow( flow );
		memberDAO.updateById( m1 );
		memberCommonService.saveCardRecordOrderCodeNew( memberEntity.getId(), 4, uc.getChangeFlow().doubleValue(), "流量兑换失败已退回", uc.getBusId(), flow.doubleValue(),
				uc.getOrderCode(), 0 );
	    }
	} catch ( BusinessException e ) {
	    throw e;
	} catch ( Exception e ) {
	    throw new BusinessException( ResponseEnums.ERROR );
	}

    }

    /**
     * 短信通知回调
     *
     * @param params
     *
     * @throws BusinessException
     */
    public void smsNotice( Map< String,Object > params ) throws BusinessException {
	//msgId,phone,status
	try {
	    Integer msgId = CommonUtil.toInteger( params.get( "msgId" ) );
	    String phone = CommonUtil.toString( params.get( "phone" ) );
	    Integer status = CommonUtil.toInteger( params.get( "status" ) );

	    //正常已发送
	    if ( status == 0 ) {
		memberNoticeuserDAO.updateByMsgIdAndPhone( msgId, phone, 4 );
	    } else {
		memberNoticeuserDAO.updateByMsgIdAndPhone( msgId, phone, 5 );
	    }
	} catch ( Exception e ) {
	    LOG.error( "短信通知回调异常,请求参数:" + JSON.toJSONString( params ), e );
	    throw new BusinessException( ResponseEnums.ERROR );
	}

    }

    public void paySuccess( Map< String,Object > params ) throws BusinessException {
	try {
	    String orderCode = CommonUtil.toString( params.get( "out_trade_no" ) );
	    UserConsumeNew uc = userConsumeNewDAO.findOneByCode( orderCode );
	    if ( CommonUtil.isEmpty( uc ) ) {
		throw new BusinessException( ResponseMemberEnums.NOT_ORDER );
	    }
	    Integer ctId = uc.getFukaCtId();
	    MemberCard card = memberCardDAO.selectById( uc.getMcId() );
	    Double money = uc.getDiscountAfterMoney();
	    Integer busId = uc.getBusId();
	    Integer memberId = uc.getMemberId();

	    UserConsumeNew uc1 = new UserConsumeNew();
	    uc1.setId( uc.getId() );
	    Integer numberCount = 0;

	    MemberCardrecordNew memberCardrecordNew = null;
	    //判断是否主卡充值 还是 副卡充值
	    if ( uc.getCtId() == ctId ) {
		//主卡充值 赠送数量
		if ( ctId == 4 ) {
		    MemberCard newCard = new MemberCard();
		    newCard.setMcId( card.getMcId() );

		    //时效卡
		    Map< String,Object > returnMap = memberCommonService.findTimeCard( money, busId );
		    Date expireDate = card.getExpireDate();
		    Integer grValidDate = CommonUtil.toInteger( returnMap.get( "grValidDate" ));
		    if ( expireDate == null ) {
			newCard.setExpireDate( DateTimeKit.addMonths( grValidDate ) );
		    } else {
			if ( DateTimeKit.laterThanNow( card.getExpireDate() ) ) {
			    newCard.setExpireDate( DateTimeKit.addMonths( expireDate, grValidDate ) );
			} else {
			    newCard.setExpireDate( DateTimeKit.addMonths( new Date(), grValidDate ) );
			}
		    }

		    // 会员日延期多少天
		    if ( CommonUtil.isNotEmpty( returnMap.get( "delayDay" ) ) ) {
			Integer delayDay = CommonUtil.toInteger( returnMap.get( "delayDay" ) );
			newCard.setExpireDate( DateTimeKit.addDate( newCard.getExpireDate(), delayDay ) );
		    }
		    Integer grId = CommonUtil.toInteger( returnMap.get( "grId" ) );
		    Integer gtId = CommonUtil.toInteger( returnMap.get( "gtId" ) );
		    newCard.setGrId( grId );
		    newCard.setGtId( gtId );

		    memberCardDAO.updateById( newCard );
		    memberCardrecordNew = memberCommonService.saveCardRecordOrderCodeNew( memberId, 1, uc.getDiscountAfterMoney(), "会员充值", busId, 0.0, uc.getOrderCode(), 0 );

		} else if ( ctId == 3 ) {
		    MemberRechargegive rechargegive = memberCommonService.findRechargegive( money, card.getGrId(), busId, card.getCtId() );
		    //储值卡充值
		    MemberCard newCard = new MemberCard();
		    newCard.setMcId( card.getMcId() );

		    if ( CommonUtil.isNotEmpty( rechargegive ) ) {
			money = money + rechargegive.getGiveCount(); //充值+赠送金额
		    }
		    Double balance = money + card.getMoney();
		    newCard.setMoney( balance );
		    memberCardDAO.updateById( newCard );

		    memberCardrecordNew = memberCommonService.saveCardRecordOrderCodeNew( memberId, 1, uc.getDiscountAfterMoney(), "会员充值", busId, balance, uc.getOrderCode(), 0 );

		    uc1.setBalance( balance );

		} else if ( ctId == 5 ) {
		    MemberRechargegive rechargegive = memberCommonService.findRechargegive( money, card.getGrId(), busId, card.getCtId() );
		    //次卡充值
		    //储值卡充值
		    MemberCard newCard = new MemberCard();
		    newCard.setMcId( card.getMcId() );
		    if ( CommonUtil.isEmpty( rechargegive ) ) {
			throw new BusinessException( ResponseMemberEnums.NOT_RECHARGE );
		    }
		    numberCount = rechargegive.getNumber();
		    Integer frequency = card.getFrequency();
		    frequency = frequency + rechargegive.getGiveCount() + rechargegive.getNumber(); //充值+赠送金额
		    numberCount = rechargegive.getNumber() + rechargegive.getGiveCount();
		    newCard.setFrequency( frequency );
		    memberCardDAO.updateById( newCard );
		    uc1.setBalanceCount( frequency );

		    memberCardrecordNew = memberCommonService
				    .saveCardRecordOrderCodeNew( memberId, 1, uc.getDiscountAfterMoney(), "会员充值", busId, frequency.doubleValue(), uc.getOrderCode(), 0 );
		}

	    } else {
		//副卡充值
		MemberRechargegiveAssistant rechargegiveAssistant = memberCommonService.findAssistantrechargegive( money, card.getGtId(), busId, ctId );
		if ( CommonUtil.isEmpty( rechargegiveAssistant ) ) {
		    throw new BusinessException( ResponseMemberEnums.NOT_RECHARGE );
		}
		//次卡充值 赠送数量
		if ( ctId == 4 ) {
		    MemberCard newCard = new MemberCard();
		    newCard.setMcId( card.getMcId() );

		    //时效卡
		    Date expireDate = card.getExpireDate();
		    if ( expireDate == null ) {
			newCard.setExpireDate( DateTimeKit.addMonths( rechargegiveAssistant.getValidDate() ) );
		    } else {
			if ( DateTimeKit.laterThanNow( card.getExpireDate() ) ) {
			    newCard.setExpireDate( DateTimeKit.addMonths( expireDate, rechargegiveAssistant.getValidDate() ) );
			} else {
			    newCard.setExpireDate( DateTimeKit.addMonths( new Date(), rechargegiveAssistant.getValidDate() ) );
			}
		    }
		    memberCardDAO.updateById( newCard );
		} else if ( ctId == 3 ) {
		    //储值卡充值
		    MemberCard newCard = new MemberCard();
		    newCard.setMcId( card.getMcId() );
		    money = money + rechargegiveAssistant.getGiveCount(); //充值+赠送金额
		    Double balance = money + card.getMoney();
		    newCard.setMoney( balance );
		    memberCardDAO.updateById( newCard );
		    memberCardrecordNew = memberCommonService.saveCardRecordOrderCodeNew( memberId, 1, uc.getDiscountAfterMoney(), "会员充值", busId, balance, uc.getOrderCode(), 0 );
		    uc1.setBalance( balance );

		} else if ( ctId == 5 ) {
		    //次卡充值
		    //储值卡充值
		    MemberCard newCard = new MemberCard();
		    newCard.setMcId( card.getMcId() );
		    Integer frequency = card.getFrequency();

		    frequency = frequency + rechargegiveAssistant.getGiveCount() + rechargegiveAssistant.getNumber(); //充值+赠送金额
		    numberCount = rechargegiveAssistant.getNumber() + rechargegiveAssistant.getGiveCount();
		    newCard.setFrequency( frequency );
		    memberCardDAO.updateById( newCard );
		    uc1.setBalanceCount( frequency );

		    memberCardrecordNew = memberCommonService
				    .saveCardRecordOrderCodeNew( memberId, 1, uc.getDiscountAfterMoney(), "会员充值", busId, frequency.doubleValue(), uc.getOrderCode(), 0 );
		}

	    }

	    uc1.setPayStatus( 1 );
	    userConsumeNewDAO.updateById( uc1 );
	    UserConsumePay userConsumePay = new UserConsumePay();
	    userConsumePay.setPayMoney( uc.getDiscountAfterMoney() );
	    userConsumePay.setUcId( uc1.getId() );

	    //支付类型(0:微信，1：支付宝2：多粉钱包),
	    Integer payType = CommonUtil.toInteger( params.get( "payType" ) );
	    if ( payType == 0 ) {
		userConsumePay.setPaymentType( 1 );
	    } else if ( payType == 1 ) {
		userConsumePay.setPaymentType( 0 );
	    } else if ( payType == 2 ) {
		userConsumePay.setPaymentType( 15 );
	    }

	    userConsumePayDAO.insert( userConsumePay );

	    MemberEntity member = memberDAO.selectById( uc.getMemberId() );
	    if ( uc.getCtId() == 3 ) {
		systemMsgService.sendChuzhiCard( member, memberCardrecordNew );
	    } else if ( uc.getCtId() == 5 ) {
		systemMsgService.sendCikaCard( member, money, numberCount );
	    }

	    //立即送
	    memberCommonService.findGiveRule( orderCode );
	} catch ( BusinessException e ) {
	    throw e;
	} catch ( Exception e ) {
	    throw new BusinessException( ResponseEnums.ERROR );
	}
    }

    @Transactional
    public void buyCardPaySuccess( Map< String,Object > params ) throws BusinessException {
	try {
	    String orderCode = CommonUtil.toString( params.get( "out_trade_no" ) );
	    LOG.error( "支付包支付回调订单单号 ：" + orderCode );
	    UserConsumeNew uc = userConsumeNewDAO.findOneByCode( orderCode );
	    if ( CommonUtil.isEmpty( uc ) ) {
		LOG.error( "支付回调查询订单出现异常" );
		throw new BusinessException( ResponseEnums.ERROR );
	    }
	    // 修改订单
	    UserConsume newUc = new UserConsume();
	    newUc.setId( uc.getId() );
	    newUc.setPayStatus( 1 );
	    userConsumeNewDAO.updateById( uc );
	    UserConsumePay userConsumePay = new UserConsumePay();
	    userConsumePay.setPayMoney( uc.getDiscountAfterMoney() );
	    userConsumePay.setUcId( uc.getId() );

	    //支付类型(0:微信，1：支付宝2：多粉钱包),
	    Integer payType = CommonUtil.toInteger( params.get( "payType" ) );
	    if ( payType == 0 ) {
		userConsumePay.setPaymentType( 1 );
	    } else if ( payType == 1 ) {
		userConsumePay.setPaymentType( 0 );
	    } else if ( payType == 2 ) {
		userConsumePay.setPaymentType( 15 );
	    }
	    userConsumePayDAO.insert( userConsumePay );

	    // 添加会员卡
	    MemberCard card = new MemberCard();
	    card.setBusId( uc.getBusId() );
	    card.setIsChecked( 1 );
	    card.setCardNo( CommonUtil.getCode() );
	    card.setCtId( uc.getCtId() );

	    card.setSystemcode( CommonUtil.getNominateCode() );
	    card.setApplyType( 3 );
	    card.setMemberId( uc.getMemberId() );
	    card.setGtId( uc.getGtId() );
	    MemberGiverule giveRule = memberGiveruleDAO.findBybusIdAndGtIdAndCtId( uc.getBusId(), card.getGtId(), card.getCtId() );

	    card.setGrId( giveRule.getGrId() );
	    card.setReceiveDate( new Date() );
	    card.setIsbinding( 1 );
	    card.setFrequency( 0 );
	    card.setMoney( 0.0 );
	    memberCardDAO.insert( card );

	    MemberEntity member = new MemberEntity();
	    member.setId( uc.getMemberId() );
	    member.setIsBuy( 1 );
	    member.setMcId( card.getMcId() );
	    memberDAO.updateById( member );

	    memberCommonService.saveCardRecordOrderCodeNew( uc.getMemberId(), 1, uc.getDiscountAfterMoney(), "购买会员卡", uc.getBusId(), 0.0, uc.getOrderCode(), 0 );

	    // 新增会员短信通知
	    member = memberDAO.selectById( uc.getMemberId() );
	    systemMsgService.sendNewMemberMsg( member );
	} catch ( BusinessException e ) {
	    throw e;
	} catch ( Exception e ) {
	    throw new BusinessException( ResponseEnums.ERROR );
	}
    }
}
