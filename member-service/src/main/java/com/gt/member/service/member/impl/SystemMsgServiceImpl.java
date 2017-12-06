package com.gt.member.service.member.impl;

import com.gt.api.util.RequestUtils;
import com.gt.common.entity.WxPublicUsersEntity;
import com.gt.member.dao.MemberCardDAO;
import com.gt.member.dao.SystemNoticeDAO;
import com.gt.member.dao.SystemnoticecallDAO;
import com.gt.member.dao.common.WxPublicUsersDAO;
import com.gt.member.entity.*;
import com.gt.member.service.common.membercard.RequestService;
import com.gt.member.service.member.SystemMsgService;
import com.gt.member.util.CommonUtil;
import com.gt.member.util.DateTimeKit;
import com.gt.member.util.PropertiesUtil;
import com.gt.util.entity.param.sms.OldApiSms;
import com.gt.util.entity.param.wx.SendWxMsgTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 系统消息 提醒包含（会员系统消息 微信消息模板 短信提醒）
 *
 * @author Administrator
 */
@Service
public class SystemMsgServiceImpl implements SystemMsgService {

    private static final Logger LOG = LoggerFactory.getLogger( SystemMsgServiceImpl.class );
    @Autowired
    private SystemNoticeDAO systemNoticeDAO;

    @Autowired
    private WxPublicUsersDAO wxPublicUsersDAO;

    @Autowired
    private RequestService requestService;

    @Autowired
    private MemberCardDAO memberCardDAO;

    @Autowired
    private SystemnoticecallDAO systemnoticecallDAO;

    @Override
    public boolean jifenMsg( MemberCardrecordNew cardRecord, MemberEntity memberEntity ) {
	try {
	    SystemNotice systemNotice = systemNoticeDAO.findBybusIdAndCallType( memberEntity.getBusId(), (byte) 2 );

	    if ( CommonUtil.isNotEmpty( systemNotice ) ) {
		if ( systemNotice.getPublicMsg() == 1 ) {
		    try {
			// 公众号消息推送
			WxPublicUsersEntity wxPublicUsers = wxPublicUsersDAO.selectByUserId( memberEntity.getBusId() );
			SendWxMsgTemplate sendWxMsgTemplate = new SendWxMsgTemplate();
			sendWxMsgTemplate.setId( systemNotice.getPublicIdMsgId() );
			sendWxMsgTemplate.setMemberId( memberEntity.getId() );
			sendWxMsgTemplate.setUrl( PropertiesUtil.getWebHome() + "/phoneMemberController/" + memberEntity.getBusId() + "/79B4DE7C/findMember_1.do" );

			List< Object > list = new ArrayList< Object >();
			// first,keyword1,keyword2,keyword3,keyword4,remark
			list.add( "积分提醒" );
			list.add( DateTimeKit.getDateTime() );
			list.add( cardRecord.getNumber() );
			list.add( cardRecord.getItemName() );
			list.add( memberEntity.getIntegral() );
			list.add( "积分详情：请到会员卡积分记录查看" );
			sendWxMsgTemplate.setObjs( list );
			requestService.setSendWxmsg( sendWxMsgTemplate );
		    } catch ( Exception e ) {
			LOG.error( "会员卡升级通知异常", e );
		    }
		}

		if ( systemNotice.getSmsStatus() == 1 && CommonUtil.isNotEmpty( memberEntity.getPhone() ) ) {
		    RequestUtils< OldApiSms > requestUtils = new RequestUtils< OldApiSms >();
		    OldApiSms oldApiSms = new OldApiSms();
		    oldApiSms.setMobiles( memberEntity.getPhone() );
		    String content = "尊敬的用户,您" + DateTimeKit.getDate() + "消费了" + cardRecord.getNumber() + "积分,当前剩余" + memberEntity.getIntegral() + "积分";
		    oldApiSms.setContent( content );
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

		if ( systemNotice.getMsgStatus() == 1 ) {
		    //会员系统通知
		    String content = "尊敬的" + memberEntity.getNickname() + "用户,您" + DateTimeKit.getDate() + "消费了" + cardRecord.getNumber() + "积分,当前剩余" + memberEntity.getIntegral()
				    + "积分";
		    Systemnoticecall noticeCall = new Systemnoticecall();
		    noticeCall.setMemberId( memberEntity.getId() );
		    noticeCall.setDescribes( content );
		    noticeCall.setCreateDate( new Date() );
		    systemnoticecallDAO.insert( noticeCall );
		}
		return true;

	    }
	} catch ( Exception e ) {
	    LOG.error( "会员卡积分提醒异常", e );
	}
	return false;
    }

    @Override
    public boolean fenbiMsg( MemberCardrecordNew cardRecord, MemberEntity memberEntity ) {
	// TODO Auto-generated method stub
	return false;
    }

    @Override
    public boolean flowMsg( MemberCardrecordNew cardRecord, MemberEntity memberEntity ) {
	// TODO Auto-generated method stub
	return false;
    }

    @Override
    public boolean upgradeMemberMsg( MemberEntity memberEntity, String cardNo, String dateTime ) {
	try {
	    SystemNotice systemNotice = systemNoticeDAO.findBybusIdAndCallType( memberEntity.getBusId(), (byte) 7 );
	    // 公众号消息推送
	    if ( CommonUtil.isNotEmpty( systemNotice ) ) {
		if ( systemNotice.getPublicMsg() == 1 ) {
		    try {
			WxPublicUsersEntity wxPublicUsers = wxPublicUsersDAO.selectByUserId( memberEntity.getBusId() );
			SendWxMsgTemplate sendWxMsgTemplate = new SendWxMsgTemplate();
			sendWxMsgTemplate.setId( systemNotice.getPublicIdMsgId() );
			sendWxMsgTemplate.setMemberId( memberEntity.getId() );
			sendWxMsgTemplate.setUrl( PropertiesUtil.getWebHome() + "/phoneMemberController/" + memberEntity.getBusId() + "/79B4DE7C/findMember_1.do" );

			List< Object > list = new ArrayList< Object >();
			// first,keyword1,keyword2,keyword3,keyword4,remark
			list.add( "会员升级通知" );
			list.add( cardNo );
			list.add( dateTime );
			list.add( "通知详情：请到公众号会员卡信息将会体现" );
			sendWxMsgTemplate.setObjs( list );
			requestService.setSendWxmsg( sendWxMsgTemplate );
		    } catch ( Exception e ) {
			LOG.error( "会员卡升级通知异常", e );
		    }
		}

		if ( systemNotice.getSmsStatus() == 1 && CommonUtil.isNotEmpty( memberEntity.getPhone() ) ) {
		    RequestUtils< OldApiSms > requestUtils = new RequestUtils< OldApiSms >();
		    OldApiSms oldApiSms = new OldApiSms();
		    oldApiSms.setMobiles( memberEntity.getPhone() );
		    String content = "尊敬的用户,您当前会员卡已经升级了";
		    oldApiSms.setContent( content );
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

		if ( systemNotice.getMsgStatus() == 1 ) {
		    //会员系统通知
		    String content = "尊敬的" + memberEntity.getNickname() + "用户,您当前会员卡已经升级了";
		    Systemnoticecall noticeCall = new Systemnoticecall();
		    noticeCall.setMemberId( memberEntity.getId() );
		    noticeCall.setDescribes( content );
		    noticeCall.setCreateDate( new Date() );
		    systemnoticecallDAO.insert( noticeCall );
		}

	    }

	    return true;

	} catch ( Exception e ) {
	    LOG.error( "会员卡升级通知异常", e );
	}
	return false;
    }

    @Override
    public boolean sendNewMemberMsg( MemberEntity memberEntity ) {
	SystemNotice systemNotice = systemNoticeDAO.findBybusIdAndCallType( memberEntity.getBusId(), (byte) 11 );
	if ( CommonUtil.isNotEmpty( systemNotice ) ) {
	    if ( systemNotice.getPublicMsg() == 1 ) {
		//新增会员模板没有
	    }

	    if ( systemNotice.getSmsStatus() == 1 && CommonUtil.isNotEmpty( memberEntity.getPhone() ) && CommonUtil.isNotEmpty( systemNotice.getSmsContent() ) ) {
		RequestUtils< OldApiSms > requestUtils = new RequestUtils< OldApiSms >();
		OldApiSms oldApiSms = new OldApiSms();
		oldApiSms.setMobiles( memberEntity.getPhone() );
		oldApiSms.setContent( systemNotice.getSmsContent() );
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

	    if ( systemNotice.getMsgStatus() == 1 ) {
		//会员系统通知
		String content = "尊敬的" + memberEntity.getNickname() + "用户,欢迎您使用会员卡,您将享受会员卡相应的权益。";
		Systemnoticecall noticeCall = new Systemnoticecall();
		noticeCall.setMemberId( memberEntity.getId() );
		noticeCall.setDescribes( content );
		noticeCall.setCreateDate( new Date() );
		systemnoticecallDAO.insert( noticeCall );
	    }
	}
	return false;
    }

    @Override
    public boolean sendChuzhiCard( MemberEntity memberEntity, MemberCardrecordNew cardRecord ) {
	try {
	    SystemNotice systemNotice = systemNoticeDAO.findBybusIdAndCallType( memberEntity.getBusId(), (byte) 4 );
	    // 公众号消息推送
	    if ( CommonUtil.isNotEmpty( systemNotice ) ) {
		MemberCard card = memberCardDAO.selectById( memberEntity.getMcId() );
		if ( systemNotice.getPublicMsg() == 1 ) {
		    WxPublicUsersEntity wxPublicUsers = wxPublicUsersDAO.selectByUserId( memberEntity.getBusId() );
		    SendWxMsgTemplate sendWxMsgTemplate = new SendWxMsgTemplate();
		    sendWxMsgTemplate.setId( systemNotice.getPublicIdMsgId() );
		    sendWxMsgTemplate.setMemberId( memberEntity.getId() );
		    sendWxMsgTemplate.setUrl( PropertiesUtil.getWebHome() + "/phoneMemberController/" + memberEntity.getBusId() + "/79B4DE7C/findMember_1.do" );

		    List< Object > list = new ArrayList< Object >();
		    list.add( cardRecord.getItemName() );
		    list.add( memberEntity.getNickname() );
		    list.add( DateTimeKit.getDateTime() );
		    list.add( cardRecord.getNumber() );
		    list.add( card.getMoney() + "元" );
		    list.add(cardRecord.getItemName() );
		    list.add( "消费详情：请到会员卡交易记录查看" );
		    sendWxMsgTemplate.setObjs( list );
		    requestService.setSendWxmsg( sendWxMsgTemplate );
		}

		if ( systemNotice.getSmsStatus() == 1 && CommonUtil.isNotEmpty( memberEntity.getPhone() ) ) {

		    RequestUtils< OldApiSms > requestUtils = new RequestUtils< OldApiSms >();
		    OldApiSms oldApiSms = new OldApiSms();
		    oldApiSms.setMobiles( memberEntity.getPhone() );
		    String content = "尊敬的用户,您" + cardRecord.getItemName() + "" + cardRecord.getNumber() + "当前卡内余额:" + card.getMoney() + "元";
		    oldApiSms.setContent( content );
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
		if ( systemNotice.getMsgStatus() == 1 ) {
		    //会员系统通知
		    String content = "尊敬的用户,您" + cardRecord.getItemName() + "" + cardRecord.getNumber() + "当前卡内余额:" + card.getMoney() + "元";
		    Systemnoticecall noticeCall = new Systemnoticecall();
		    noticeCall.setMemberId( memberEntity.getId() );
		    noticeCall.setDescribes( content );
		    noticeCall.setCreateDate( new Date() );
		    systemnoticecallDAO.insert( noticeCall );
		}

	    }
	} catch ( Exception e ) {
	    LOG.error( "储值卡充值消息推送异常", e );
	    return false;
	}
	return false;
    }

    @Override
    public boolean sendCikaCard( MemberEntity memberEntity, double money, int count ) {
	try {
	    SystemNotice systemNotice = systemNoticeDAO.findBybusIdAndCallType( memberEntity.getBusId(), (byte) 5 );
	    // 公众号消息推送
	    if ( CommonUtil.isNotEmpty( systemNotice ) ) {
		MemberCard card = memberCardDAO.selectById( memberEntity.getMcId() );
		if ( systemNotice.getPublicMsg() == 1 ) {
		    WxPublicUsersEntity wxPublicUsers = wxPublicUsersDAO.selectByUserId( memberEntity.getBusId() );
		    SendWxMsgTemplate sendWxMsgTemplate = new SendWxMsgTemplate();
		    sendWxMsgTemplate.setId( systemNotice.getPublicIdMsgId() );
		    sendWxMsgTemplate.setMemberId( memberEntity.getId() );
		    sendWxMsgTemplate.setUrl( PropertiesUtil.getWebHome() + "/phoneMemberController/" + memberEntity.getBusId() + "/79B4DE7C/findMember_1.do" );

		    try {
			List< Object > list = new ArrayList< Object >();
			list.add( "次卡充值提醒" );
			list.add( money + "元" );
			list.add( count + "次" );
			list.add( card.getFrequency() + "次" );
			list.add( DateTimeKit.getDateTime() );
			list.add( "充值详情：请到会员卡交易记录查看" );
			sendWxMsgTemplate.setObjs( list );
			requestService.setSendWxmsg( sendWxMsgTemplate );
		    } catch ( Exception e ) {
			LOG.error( "模板发送异常", e );
		    }
		}

		if ( systemNotice.getSmsStatus() == 1 && CommonUtil.isNotEmpty( memberEntity.getPhone() ) ) {
		    RequestUtils< OldApiSms > requestUtils = new RequestUtils< OldApiSms >();
		    OldApiSms oldApiSms = new OldApiSms();
		    oldApiSms.setMobiles( memberEntity.getPhone() );
		    String content = "尊敬的用户,您当前充值" + money + "元,当前卡内剩余:" + card.getFrequency() + "次";
		    oldApiSms.setContent( content );
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
		if ( systemNotice.getMsgStatus() == 1 ) {
		    //会员系统通知
		    String content = "尊敬的用户,您当前充值" + money + "元,当前卡内剩余:" + card.getFrequency() + "次";
		    Systemnoticecall noticeCall = new Systemnoticecall();
		    noticeCall.setMemberId( memberEntity.getId() );
		    noticeCall.setDescribes( content );
		    noticeCall.setCreateDate( new Date() );
		    systemnoticecallDAO.insert( noticeCall );
		}
	    }
	    return true;
	} catch ( Exception e ) {
	    LOG.error( "次卡充值消息推送异常", e );
	}
	return false;
    }

    @Override
    public boolean sendCikaXiaofei( MemberEntity memberEntity ) {
	try {
	    SystemNotice systemNotice = systemNoticeDAO.findBybusIdAndCallType( memberEntity.getBusId(), (byte) 8 );
	    // 公众号消息推送
	    if ( CommonUtil.isNotEmpty( systemNotice ) ) {
		MemberCard card = memberCardDAO.selectById( memberEntity.getMcId() );
		if ( systemNotice.getPublicMsg() == 1 ) {
		    WxPublicUsersEntity wxPublicUsers = wxPublicUsersDAO.selectByUserId( memberEntity.getBusId() );
		    SendWxMsgTemplate sendWxMsgTemplate = new SendWxMsgTemplate();
		    sendWxMsgTemplate.setId( systemNotice.getPublicIdMsgId() );
		    sendWxMsgTemplate.setMemberId( memberEntity.getId() );
		    sendWxMsgTemplate.setUrl( PropertiesUtil.getWebHome() + "/phoneMemberController/" + memberEntity.getBusId() + "/79B4DE7C/findMember_1.do" );
		    try {
			List< Object > list = new ArrayList< Object >();
			list.add( "次卡消费提醒" );
			list.add( card.getCardNo() );
			list.add( DateTimeKit.getDateTime() );
			list.add( card.getFrequency() + "次" );
			list.add( DateTimeKit.getDateTime() );
			list.add( "消费详情：请到会员卡交易记录查看" );
			sendWxMsgTemplate.setObjs( list );
			requestService.setSendWxmsg( sendWxMsgTemplate );
		    } catch ( Exception e ) {
			LOG.error( "模板发送异常", e );
		    }
		}


		if ( systemNotice.getSmsStatus() == 1 && CommonUtil.isNotEmpty( memberEntity.getPhone() ) ) {
		    RequestUtils< OldApiSms > requestUtils = new RequestUtils< OldApiSms >();
		    OldApiSms oldApiSms = new OldApiSms();
		    oldApiSms.setMobiles( memberEntity.getPhone() );
		    String content = "尊敬的用户,您消费后当前卡内剩余:" + card.getFrequency() + "次";
		    oldApiSms.setContent( content );
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
		if ( systemNotice.getMsgStatus() == 1 ) {
		    //会员系统通知
		    String content = "尊敬的用户,您消费后当前卡内剩余:" + card.getFrequency() + "次";
		    Systemnoticecall noticeCall = new Systemnoticecall();
		    noticeCall.setMemberId( memberEntity.getId() );
		    noticeCall.setDescribes( content );
		    noticeCall.setCreateDate( new Date() );
		    systemnoticecallDAO.insert( noticeCall );
		}


	    }
	    return true;
	} catch ( Exception e ) {
	    LOG.error( "次卡消费消息推送异常", e );
	}
	return false;
    }


}
