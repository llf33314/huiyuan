package com.gt.member.service.member.impl;

import com.gt.api.util.RequestUtils;
import com.gt.common.entity.WxPublicUsersEntity;
import com.gt.member.dao.MemberCardDAO;
import com.gt.member.dao.SystemNoticeDAO;
import com.gt.member.dao.common.WxPublicUsersDAO;
import com.gt.member.entity.MemberCard;
import com.gt.member.entity.MemberCardrecordNew;
import com.gt.member.entity.MemberEntity;
import com.gt.member.entity.SystemNotice;
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

    @Override
    public boolean jifenMsg( MemberCardrecordNew cardRecord, MemberEntity memberEntity ) {
	try {
        SystemNotice systemNotice = systemNoticeDAO.findBybusIdAndCallType( memberEntity.getBusId(), (byte) 2 );
	// 公众号消息推送
	if ( CommonUtil.isNotEmpty( systemNotice ) && systemNotice.getPublicMsg() == 1 ) {
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
	    return true;
	}
	}catch ( Exception e ){
	    LOG.error( "会员卡积分提醒异常",e );
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
	    if ( CommonUtil.isNotEmpty( systemNotice ) && systemNotice.getPublicMsg() == 1 ) {
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
		return true;
	    }
	}catch ( Exception e ){
	    LOG.error( "会员卡升级通知异常",e );
	}
	return false;
    }

    @Override
    public boolean sendNewMemberMsg( MemberEntity memberEntity ) {
	SystemNotice systemNotice = systemNoticeDAO.findBybusIdAndCallType( memberEntity.getBusId(), (byte) 11 );
	if ( CommonUtil.isNotEmpty( systemNotice ) && CommonUtil.isNotEmpty( memberEntity.getPhone() ) ) {

	    RequestUtils< OldApiSms > requestUtils = new RequestUtils< OldApiSms >();

	    OldApiSms oldApiSms = new OldApiSms();
	    oldApiSms.setMobiles( memberEntity.getPhone() );
	    oldApiSms.setContent( systemNotice.getMsgContent() );
	    oldApiSms.setCompany( PropertiesUtil.getSms_name() );
	    oldApiSms.setBusId( memberEntity.getBusId() );
	    oldApiSms.setModel( 3 );
	    requestUtils.setReqdata( oldApiSms );
	    try {
		requestService.sendSms( requestUtils );
	    } catch ( Exception e ) {
		LOG.error( "短信发送失败", e );
	    }

	    return true;
	}
	return false;
    }

    @Override
    public boolean sendChuzhiCard( MemberEntity memberEntity, Double money ) {
	try {
	    SystemNotice systemNotice = systemNoticeDAO.findBybusIdAndCallType( memberEntity.getBusId(), (byte) 4 );
	    // 公众号消息推送
	    if ( CommonUtil.isNotEmpty( systemNotice ) && systemNotice.getPublicMsg() == 1 ) {
		WxPublicUsersEntity wxPublicUsers = wxPublicUsersDAO.selectByUserId( memberEntity.getBusId() );
		SendWxMsgTemplate sendWxMsgTemplate = new SendWxMsgTemplate();
		sendWxMsgTemplate.setId( systemNotice.getPublicIdMsgId() );
		sendWxMsgTemplate.setMemberId( memberEntity.getId() );
		sendWxMsgTemplate.setUrl( PropertiesUtil.getWebHome() + "/phoneMemberController/" + memberEntity.getBusId() + "/79B4DE7C/findMember_1.do" );

		List< Object > list = new ArrayList< Object >();
		// {{first.DATA}}
		// 充值金额：{{keyword1.DATA}}
		// 充值次数：{{keyword2.DATA}}
		// 剩余次数：{{keyword3.DATA}}
		// 充值时间：{{keyword4.DATA}}
		// {{remark.DATA}}

		// {{first.DATA}}
		// 用户名：{{keyword1.DATA}}
		// 变动时间：{{keyword2.DATA}}
		// 金额变动：{{keyword3.DATA}}
		// 可用余额：{{keyword4.DATA}}
		// 变动原因：{{keyword5.DATA}}
		// {{remark.DATA}}
		MemberCard card = memberCardDAO.selectById( memberEntity.getMcId() );
		// first,keyword1,keyword2,keyword3,keyword4,remark
		list.add( "储值卡充值" );
		list.add( memberEntity.getNickname() );
		list.add( DateTimeKit.getDateTime() );
		list.add( money + "元" );
		list.add( card.getMoney() + "元" );
		list.add( "储值卡充值" );
		list.add( "充值金额详情：请到会员卡交易记录查看" );
		sendWxMsgTemplate.setObjs( list );
		requestService.setSendWxmsg( sendWxMsgTemplate );
		return true;
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
	    if ( CommonUtil.isNotEmpty( systemNotice ) && systemNotice.getPublicMsg() == 1 ) {
		WxPublicUsersEntity wxPublicUsers = wxPublicUsersDAO.selectByUserId( memberEntity.getBusId() );
		SendWxMsgTemplate sendWxMsgTemplate = new SendWxMsgTemplate();
		sendWxMsgTemplate.setId( systemNotice.getPublicIdMsgId() );
		sendWxMsgTemplate.setMemberId( memberEntity.getId() );
		sendWxMsgTemplate.setUrl( PropertiesUtil.getWebHome() + "/phoneMemberController/" + memberEntity.getBusId() + "/79B4DE7C/findMember_1.do" );

		List< Object > list = new ArrayList< Object >();
		// {{first.DATA}}
		// 充值金额：{{keyword1.DATA}}
		// 充值次数：{{keyword2.DATA}}
		// 剩余次数：{{keyword3.DATA}}
		// 充值时间：{{keyword4.DATA}}
		// {{remark.DATA}}

		MemberCard card = memberCardDAO.selectById( memberEntity.getMcId() );

		// first,keyword1,keyword2,keyword3,keyword4,remark
		list.add( "次卡充值提醒" );
		list.add( money + "元" );
		list.add( count + "次" );
		list.add( card.getFrequency() + "次" );
		list.add( DateTimeKit.getDateTime() );
		list.add( "充值详情：请到会员卡交易记录查看" );
		sendWxMsgTemplate.setObjs( list );
		requestService.setSendWxmsg( sendWxMsgTemplate );
		return true;
	    }
	} catch ( Exception e ) {
	    LOG.error( "次卡充值消息推送异常", e );
	    return false;
	}
	return false;
    }

    @Override
    public boolean sendCikaXiaofei( MemberEntity memberEntity ) {
	try {
	    SystemNotice systemNotice = systemNoticeDAO.findBybusIdAndCallType( memberEntity.getBusId(), (byte) 8 );
	    // 公众号消息推送
	    if ( CommonUtil.isNotEmpty( systemNotice ) && systemNotice.getPublicMsg() == 1 ) {
		WxPublicUsersEntity wxPublicUsers = wxPublicUsersDAO.selectByUserId( memberEntity.getBusId() );
		SendWxMsgTemplate sendWxMsgTemplate = new SendWxMsgTemplate();
		sendWxMsgTemplate.setId( systemNotice.getPublicIdMsgId() );
		sendWxMsgTemplate.setMemberId( memberEntity.getId() );
		sendWxMsgTemplate.setUrl( PropertiesUtil.getWebHome() + "/phoneMemberController/" + memberEntity.getBusId() + "/79B4DE7C/findMember_1.do" );

		List< Object > list = new ArrayList< Object >();

		MemberCard card = memberCardDAO.selectById( memberEntity.getMcId() );
		// {{first.DATA}}
		// 卡号：{{keyword1.DATA}}
		// 消费时间：{{keyword2.DATA}}
		// 剩余次数：{{keyword3.DATA}}
		// 有效期：{{keyword4.DATA}}
		// {{remark.DATA}}

		list.add( "次卡消费提醒" );
		list.add( card.getCardNo() );
		list.add( DateTimeKit.getDateTime() );
		list.add( card.getFrequency() + "次" );
		list.add( DateTimeKit.getDateTime() );
		list.add( "消费详情：请到会员卡交易记录查看" );
		sendWxMsgTemplate.setObjs( list );
		requestService.setSendWxmsg( sendWxMsgTemplate );
		return true;
	    }
	} catch ( Exception e ) {
	    LOG.error( "次卡消费消息推送异常", e );
	    return false;
	}
	return false;
    }

    @Override
    public boolean sendChuzhiXiaofei( MemberEntity memberEntity, Double money ) {
	try {
	    SystemNotice systemNotice = systemNoticeDAO.findBybusIdAndCallType( memberEntity.getBusId(), (byte) 12 );
	    // 公众号消息推送
	    if ( CommonUtil.isNotEmpty( systemNotice ) && systemNotice.getPublicMsg() == 1 ) {
		WxPublicUsersEntity wxPublicUsers = wxPublicUsersDAO.selectByUserId( memberEntity.getBusId() );
		SendWxMsgTemplate sendWxMsgTemplate = new SendWxMsgTemplate();
		sendWxMsgTemplate.setId( systemNotice.getPublicIdMsgId() );
		sendWxMsgTemplate.setMemberId( memberEntity.getId() );
		sendWxMsgTemplate.setUrl( PropertiesUtil.getWebHome() + "/phoneMemberController/" + memberEntity.getBusId() + "/79B4DE7C/findMember_1.do" );

		List< Object > list = new ArrayList< Object >();
		MemberCard card = memberCardDAO.selectById( memberEntity.getMcId() );
		// first,keyword1,keyword2,keyword3,keyword4,remark
		list.add( "储值卡消费" );
		list.add( memberEntity.getNickname() );
		list.add( DateTimeKit.getDateTime() );
		list.add( money + "元" );
		list.add( card.getMoney() + "元" );
		list.add( "储值卡消费" );
		list.add( "消费详情：请到会员卡交易记录查看" );
		sendWxMsgTemplate.setObjs( list );
		requestService.setSendWxmsg( sendWxMsgTemplate );
		return true;
	    }
	} catch ( Exception e ) {
	    LOG.error( "储值卡消费消息推送异常", e );
	    return false;
	}
	return false;
    }

    @Override
    public boolean sendChuzhiTuikuan( MemberEntity memberEntity, Double money ) {
	try {
	    SystemNotice systemNotice = systemNoticeDAO.findBybusIdAndCallType( memberEntity.getBusId(), (byte) 12 );
	    // 公众号消息推送
	    if ( CommonUtil.isNotEmpty( systemNotice ) && systemNotice.getPublicMsg() == 1 ) {
		WxPublicUsersEntity wxPublicUsers = wxPublicUsersDAO.selectByUserId( memberEntity.getBusId() );
		SendWxMsgTemplate sendWxMsgTemplate = new SendWxMsgTemplate();
		sendWxMsgTemplate.setId( systemNotice.getPublicIdMsgId() );
		sendWxMsgTemplate.setMemberId( memberEntity.getId() );
		sendWxMsgTemplate.setUrl( PropertiesUtil.getWebHome() + "/phoneMemberController/" + memberEntity.getBusId() + "/79B4DE7C/findMember_1.do" );

		List< Object > list = new ArrayList< Object >();
		MemberCard card = memberCardDAO.selectById( memberEntity.getMcId() );
		// first,keyword1,keyword2,keyword3,keyword4,remark
		list.add( "储值卡退款" );
		list.add( memberEntity.getNickname() );
		list.add( DateTimeKit.getDateTime() );
		list.add( money + "元" );
		list.add( card.getMoney() + "元" );
		list.add( "储值卡退款" );
		list.add( "退款详情：请到会员卡交易记录查看" );
		sendWxMsgTemplate.setObjs( list );
		requestService.setSendWxmsg( sendWxMsgTemplate );
		return true;
	    }
	} catch ( Exception e ) {
	    LOG.error( "储值卡充值消息推送异常", e );
	    return false;
	}
	return false;
    }

}
