/**
 * P 2016年3月24日
 */
package com.gt.member.service.member.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gt.api.bean.session.BusUser;
import com.gt.api.enums.ResponseEnums;
import com.gt.api.util.HttpClienUtils;
import com.gt.api.util.RequestUtils;
import com.gt.common.entity.BusUserEntity;
import com.gt.member.dao.*;
import com.gt.member.dao.common.BusUserDAO;
import com.gt.member.dao.common.MsgTemplateDAO;
import com.gt.member.entity.MemberNotice;
import com.gt.member.entity.MemberNoticeuser;
import com.gt.member.entity.SystemNotice;
import com.gt.member.enums.ResponseMemberEnums;
import com.gt.member.exception.BusinessException;
import com.gt.member.service.member.MemberNoticeService;
import com.gt.member.util.CommonUtil;
import com.gt.member.util.DateTimeKit;
import com.gt.member.util.Page;
import com.gt.member.util.PropertiesUtil;
import com.gt.util.entity.param.sms.OldApiSms;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author pengjiangli
 * @version 创建时间:2016年3月24日
 */
@Service
public class MemberNoticeServiceImpl implements MemberNoticeService {

    private static final Logger LOG = Logger.getLogger( MemberNoticeServiceImpl.class );

    //发送短信
    private static final String SEND_SMS = "/8A5DA52E/smsapi/6F6D9AD2/79B4DE7C/sendSmsOld.do";

    @Autowired
    private SystemNoticeDAO systemNoticeDAO;

    @Autowired
    private SystemCalltypeDAO systemCalltypeDAO;

    @Autowired
    private MsgTemplateDAO msgTemplateDAO;

    @Autowired
    private MemberNoticeDAO memberNoticeDAO;

    @Autowired
    private MemberNoticeuserDAO memberNoticeuserDAO;

    @Autowired
    private MemberEntityDAO memberMapper;

    @Autowired
    private MemberGradetypeDAO memberGradetypeDAO;

    @Autowired
    private MemberCardDAO memberCardDAO;

    @Autowired
    private BusUserDAO busUserDAO;

    /**
     * 系统通知设置
     *
     * @param busId
     *
     * @return
     * @throws BusinessException
     */
    public Map< String,Object > findSystemNotice( Integer busId ) throws BusinessException {
	Map< String,Object > map = new HashMap<>();
	if ( CommonUtil.isEmpty( busId ) ) {
	    throw new BusinessException( ResponseMemberEnums.INVALID_SESSION );
	}
	List< Map< String,Object > > systemNotices = systemNoticeDAO.findBybusId( busId );
	map.put( "systemNotices", systemNotices );

	List< Map< String,Object > > callTypes = systemCalltypeDAO.findAll();
	map.put( "callTypes", callTypes );
	List< Map< String,Object > > msgTemplates = msgTemplateDAO.selectTempObjByBusId( busId );
	map.put( "msgTemplates", msgTemplates );
	return map;
    }

    public void saveSystemNotice( Integer busId, String json ) throws BusinessException {
	try {
	    if ( CommonUtil.isEmpty( busId ) ) {
		throw new BusinessException( ResponseMemberEnums.INVALID_SESSION );
	    }
	    List< SystemNotice > systemNotices = JSONArray.parseArray( json, SystemNotice.class );
	    if ( systemNotices.size() != 0 ) {
		for ( SystemNotice systemNotice : systemNotices ) {
		    systemNotice.setBusId( busId );
		    if ( CommonUtil.isNotEmpty( systemNotice.getId() ) ) {
			systemNoticeDAO.updateById( systemNotice );
		    } else {
			systemNoticeDAO.insert( systemNotice );
		    }
		}
	    }
	} catch ( BusinessException e ) {
	    throw e;
	} catch ( Exception e ) {
	    LOG.error( "方法saveSystemNotice：保存消息异常", e );
	    throw new BusinessException( ResponseEnums.ERROR );
	}
    }

    public Map< String,Object > editMemberNotice( Integer busId, Integer id ) throws BusinessException {
	try {
	    if ( CommonUtil.isEmpty( busId ) ) {
		throw new BusinessException( ResponseMemberEnums.INVALID_SESSION );
	    }
	    Map< String,Object > map = new HashMap<>();
	    List< Map< String,Object > > listMap = memberGradetypeDAO.findGradeTyeBybusId( busId );
	    map.put( "gradeType", listMap );
	    Integer count = 0;
	    if ( id != 0 ) {
		MemberNotice notice = memberNoticeDAO.selectById( id );
		String noticeUser = notice.getNoticeUser();
		if ( "0".equals( noticeUser ) ) {
		    count = memberCardDAO.countCardAll( busId );
		} else {
		    String[] users = noticeUser.split( "," );
		    List< Integer > ctIds = new ArrayList< Integer >();
		    for ( String str : users ) {
			ctIds.add( CommonUtil.toInteger( str ) );
		    }
		    count = memberCardDAO.countCard( busId, ctIds );
		}
		map.put( "notice", notice );
		map.put( "imagePath", PropertiesUtil.getRes_web_path() );
		map.put( "memberCount", count );
	    }

	    BusUserEntity busUser = busUserDAO.selectById( busId );
	    map.put( "smscount", busUser.getSmsCount() );
	    return map;
	} catch ( BusinessException e ) {
	    throw e;
	} catch ( Exception e ) {
	    throw new BusinessException( ResponseEnums.ERROR );
	}
    }

    @Transactional
    public void saveMemberNotice( Integer busId, String json, String sendDate ) throws BusinessException {
	Map< String,Object > map = new HashMap< String,Object >();
	try {
	    if ( CommonUtil.isEmpty( busId ) ) {
		throw new BusinessException( ResponseMemberEnums.INVALID_SESSION );
	    }

	    JSONObject obj = JSON.parseObject( json );

	    //MemberNotice memberNotice = (MemberNotice) JSON.toJavaObject( JSON.parseObject( json ), MemberNotice.class );

	    MemberNotice memberNotice = new MemberNotice();
	    memberNotice.setContent( CommonUtil.toString( obj.get( "content" ) ) );
	    memberNotice.setId( CommonUtil.toInteger( obj.get( "id" ) ) );
	    memberNotice.setNoticeMember( CommonUtil.toInteger( obj.get( "noticeMember" ) ) );
	    memberNotice.setSendMsg( CommonUtil.toInteger( obj.get( "sendMsg" ) ) );
	    memberNotice.setSendSms( CommonUtil.toInteger( obj.get( "sendSms" ) ) );
	    memberNotice.setTitle( CommonUtil.toString( obj.get( "title" ) ) );
	    memberNotice.setNoticeUser( CommonUtil.toString( obj.get( "noticeUser" ) ) );
	    memberNotice.setSmsContent( CommonUtil.toString( obj.get( "smsContent" ) ) );
	    memberNotice.setSendType( CommonUtil.toInteger( obj.get( "sendType" ) ) );
	    if ( CommonUtil.isNotEmpty( sendDate ) ) {
		Date date = DateTimeKit.parse( sendDate, "yyyy-MM-dd HH:mm:ss" );
		memberNotice.setSendDate( date );
	    }
	    memberNotice.setBusId( busId );
	    memberNotice.setCreateDate( new Date() );
	    if ( memberNotice.getSendType() == 0 ) {
		memberNotice.setSendDate( new Date() );
	    }

	    if ( CommonUtil.isNotEmpty( memberNotice.getId() ) ) {
		memberNoticeDAO.updateById( memberNotice );
	    } else {
		memberNoticeDAO.insert( memberNotice );
	    }

	    // 消息 添加到用户信息表中
	    String noticeUser = memberNotice.getNoticeUser();
	    if ( CommonUtil.isNotEmpty( noticeUser ) ) {
		List< Map< String,Object > > cards = null;
		if ( "0".equals( noticeUser ) ) {
		    cards = memberCardDAO.findCardAll( busId );
		} else {
		    String[] str = noticeUser.split( "," );
		    List< Integer > ctIds = new ArrayList< Integer >();
		    for ( int i = 0; i < str.length; i++ ) {
			if ( CommonUtil.isNotEmpty( str[i] ) ) {
			    ctIds.add( Integer.parseInt( str[i] ) );
			}
		    }
		    cards = memberCardDAO.findCardByCtIds( busId, ctIds );
		}

		List< Integer > mcIds = new ArrayList< Integer >();
		for ( Map< String,Object > card : cards ) {
		    if ( CommonUtil.isNotEmpty( card.get( "mc_id" ) ) ) {
			mcIds.add( CommonUtil.toInteger( card.get( "mc_id" ) ) );
		    }
		}
		if ( mcIds.size() > 0 ) {
		    List< Map< String,Object > > members = memberMapper.findBymcIds( busId, mcIds );
		    MemberNoticeuser nu = null;

		    memberNoticeuserDAO.deleteByNoticeId( memberNotice.getId() );

		    List< MemberNoticeuser > list = new ArrayList< MemberNoticeuser >();
		    if ( memberNotice.getSendMsg() == 1 ) {
			for ( Map< String,Object > member : members ) {
			    nu = new MemberNoticeuser();
			    nu.setBusId( CommonUtil.toInteger( member.get( "id" ) ) );
			    nu.setMsgType( 1 );
			    nu.setNoticeId( memberNotice.getId() );
			    nu.setStatus( 0 );
			    nu.setSendDate( memberNotice.getSendDate() );
			    list.add( nu );
			}
		    }

		    if ( memberNotice.getSendSms() == 1 ) {
			for ( Map< String,Object > member : members ) {
			    nu = new MemberNoticeuser();
			    nu.setBusId( CommonUtil.toInteger( member.get( "id" ) ) );
			    nu.setMsgType( 0 );
			    nu.setNoticeId( memberNotice.getId() );
			    nu.setStatus( 2 );
			    nu.setSendDate( memberNotice.getSendDate() );
			    list.add( nu );
			}
		    }

		    if ( list.size() > 0 ) {
			memberNoticeuserDAO.saveList( list );
		    }
		}
	    }

	    if ( memberNotice.getSendType() == 0 ) {
		if ( memberNotice.getSendSms() == 1 ) {
		    // 如果立即发送
		    sendNotice(memberNotice.getId());
		}
	    }

	} catch ( BusinessException e ) {
	    throw e;
	} catch ( Exception e ) {
	    LOG.error( "保存消息模板异常", e );
	    throw new BusinessException( ResponseEnums.ERROR );
	}

    }

    public void sendNotice( Integer id ) throws BusinessException {
	try {
	    MemberNotice memberNotice = memberNoticeDAO.selectById( id );

	    List< Map< String,Object > > memberNoticeUsers = memberNoticeuserDAO.findByNoticeId( id );

	    List< Integer > memberIds = null;
	    StringBuffer  sb = new StringBuffer();;
	    Map< String,Object > params = new HashMap< String,Object >();
	    List< Integer > ctId = null;
	    List< Map< String,Object > > members = null;

	    for ( Map< String,Object > map : memberNoticeUsers ) {
		// 修改方式状态
		memberIds = new ArrayList< Integer >();
		Integer memberId = CommonUtil.toInteger( map.get( "busId" ) );
		memberIds.add( memberId );
		Integer busId = memberNotice.getBusId();
		members = memberMapper.findByMemberIds( busId, memberIds );
		for ( Map< String,Object > member : members ) {
		    if ( CommonUtil.isNotEmpty( member.get( "phone" ) ) ) {
			sb.append( member.get( "phone" ) + "," );
		    }
		}
	    }

	    try {
		if ( CommonUtil.isNotEmpty( sb.toString() ) ) {
		    String phone = sb.substring( 0, sb.lastIndexOf( "," ) );
		    String url = PropertiesUtil.getWxmp_home() + SEND_SMS;
		    RequestUtils< OldApiSms > requestUtils = new RequestUtils< OldApiSms >();

		    OldApiSms oldApiSms = new OldApiSms();
		    oldApiSms.setMobiles( phone );
		    oldApiSms.setContent( memberNotice.getSmsContent() );
		    oldApiSms.setCompany( PropertiesUtil.getSms_name() );
		    oldApiSms.setBusId( memberNotice.getBusId() );
		    oldApiSms.setModel( 3 );
		    requestUtils.setReqdata( oldApiSms );
		    try {
			String smsStr = HttpClienUtils.reqPostUTF8( JSONObject.toJSONString( requestUtils ), url, String.class, PropertiesUtil.getWxmpsignKey() );
		    } catch ( Exception e ) {
			LOG.error( "短信发送失败", e );
			throw new BusinessException( ResponseEnums.ERROR );
		    }
		}

	    } catch ( Exception e ) {
		LOG.error( "循环发送信息异常", e );
		throw new BusinessException( ResponseEnums.ERROR );
	    }

    } catch(Exception e){
	LOG.error( "发送消息异常", e );
	throw new BusinessException( ResponseEnums.ERROR );

    }

}

    /**
     * 修改消息状态
     *
     * @param noticeId
     * @param sendstuts
     */
    public void updateMemberNotice( Integer noticeId, int sendstuts ) throws BusinessException {
	try {
	    // 修改方式状态
	    MemberNotice memberNotice = new MemberNotice();
	    memberNotice.setId( noticeId );
	    memberNotice.setSendStuts( sendstuts );
	    memberNotice.setSendDate( new Date() );
	    memberNoticeDAO.updateById( memberNotice );
	} catch ( Exception e ) {
	    throw new BusinessException( ResponseEnums.ERROR );
	}
    }

    public Page findMemberNotice( Integer busId, Map< String,Object > params ) {
	params.put( "curPage", CommonUtil.isEmpty( params.get( "curPage" ) ) ? 1 : CommonUtil.toInteger( params.get( "curPage" ) ) );
	int pageSize = 10;
	Integer sendStuts = 1;
	if ( CommonUtil.isNotEmpty( params.get( "sendStuts" ) ) ) {
	    sendStuts = CommonUtil.toInteger( params.get( "sendStuts" ) );
	}
	int rowCount = memberNoticeDAO.countMemberNotice( busId, sendStuts );

	Page page = new Page( CommonUtil.toInteger( params.get( "curPage" ) ), pageSize, rowCount, "" );
	params.put( "firstResult", pageSize * ( ( page.getCurPage() <= 0 ? 1 : page.getCurPage() ) - 1 ) );

	List< Map< String,Object > > list = memberNoticeDAO.findMemberNotice( busId, sendStuts, Integer.parseInt( params.get( "firstResult" ).toString() ), pageSize );
	page.setSubList( list );
	return page;
    }

    @Transactional
    public void sendNoticeToUser( Integer id, String memberIds ) throws BusinessException {
	try {
	    MemberNotice memberNotice = memberNoticeDAO.selectById( id );
	    memberNotice.setSendType( 0 );
	    memberNotice.setNoticeMember( 1 );
	    memberNoticeDAO.updateById( memberNotice );
	    String[] strs = memberIds.split( "," );
	    MemberNoticeuser noticeUser = null;
	    for ( int i = 0; i < strs.length; i++ ) {
		if ( CommonUtil.isNotEmpty( strs[i] ) ) {
		    noticeUser = memberNoticeuserDAO.findByNoticeIdAndMemberId( id, CommonUtil.toInteger( strs[i] ) );
		    if ( CommonUtil.isNotEmpty( noticeUser ) ) {
			noticeUser.setSendDate( memberNotice.getSendDate() );
			noticeUser.setStatus( 0 );
			memberNoticeuserDAO.updateById( noticeUser );
			continue;
		    }

		    noticeUser = new MemberNoticeuser();
		    noticeUser.setNoticeId( id );
		    noticeUser.setBusId( Integer.parseInt( strs[i] ) );
		    noticeUser.setSendDate( memberNotice.getSendDate() );
		    if ( memberNotice.getSendMsg() == 1 ) {
			noticeUser.setMsgType( 1 );
		    }
		    memberNoticeuserDAO.insert( noticeUser );
		}
	    }
	    if ( memberNotice.getSendSms() == 1 ) {
		sendNoticebyMemberIds( memberNotice, memberIds );
	    }
	} catch ( Exception e ) {
	    LOG.error( "方法sendNoticeToUser:发送信息给用户异常" );
	    throw new BusinessException( ResponseEnums.ERROR );
	}
    }

    /**
     * 会员管理选择会员发送短信
     */
    public void sendNoticebyMemberIds( MemberNotice memberNotice, String memberIds ) {
	try {
	    if ( CommonUtil.isEmpty( memberNotice ) ) {
		return;
	    }
	    StringBuffer sb = null;
	    Map< String,Object > params = new HashMap< String,Object >();
	    List< Integer > ctId = null;
	    sb = new StringBuffer();
	    // 修改方式状态
	    BusUserEntity busUser = busUserDAO.selectById( memberNotice.getBusId() );
	    if ( CommonUtil.isEmpty( busUser ) ) {
		return;
	    }

	    String[] ids = memberIds.split( "," );
	    List< Integer > memberList = new ArrayList<>();
	    for ( int i = 0; i < ids.length; i++ ) {
		if ( CommonUtil.isNotEmpty( ids[i] ) ) {
		    memberList.add( CommonUtil.toInteger( ids[i] ) );
		}
	    }
	    // 通知某个人群
	    List< Map< String,Object > > members = memberMapper.findByMemberIds( memberNotice.getBusId(), memberList );
	    if ( CommonUtil.isEmpty( members ) || members.size() <= 0 ) {
		return;
	    }
	    for ( Map< String,Object > member : members ) {
		if ( CommonUtil.isNotEmpty( member.get( "phone" ) ) ) {
		    sb.append( member.get( "phone" ) + "," );
		}
	    }

	    if ( CommonUtil.isNotEmpty( sb.toString() ) ) {
		String phone = sb.substring( 0, sb.lastIndexOf( "," ) );
		String url = PropertiesUtil.getWxmp_home() + SEND_SMS;
		RequestUtils< OldApiSms > requestUtils = new RequestUtils< OldApiSms >();

		OldApiSms oldApiSms = new OldApiSms();
		oldApiSms.setMobiles( phone );
		oldApiSms.setContent( memberNotice.getSmsContent() );
		oldApiSms.setCompany( PropertiesUtil.getSms_name() );
		oldApiSms.setBusId( busUser.getId() );
		oldApiSms.setModel( 3 );
		requestUtils.setReqdata( oldApiSms );
		try {
		    String smsStr = HttpClienUtils.reqPostUTF8( JSONObject.toJSONString( requestUtils ), url, String.class, PropertiesUtil.getWxmpsignKey() );
		} catch ( Exception e ) {
		    LOG.error( "短信发送失败", e );
		}
	    }
	} catch ( Exception e ) {
	    LOG.error( "发送消息异常", e );
	    throw new BusinessException( ResponseEnums.ERROR );
	}
    }
}
