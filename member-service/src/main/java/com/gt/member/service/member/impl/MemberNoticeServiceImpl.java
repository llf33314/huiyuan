/**
 * P 2016年3月24日
 */
package com.gt.member.service.member.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gt.api.bean.session.BusUser;
import com.gt.api.enums.ResponseEnums;
import com.gt.api.util.RequestUtils;
import com.gt.member.dao.*;
import com.gt.member.entity.MemberNotice;
import com.gt.member.entity.MemberNoticeuser;
import com.gt.member.entity.SystemNotice;
import com.gt.member.enums.ResponseMemberEnums;
import com.gt.member.exception.BusinessException;
import com.gt.member.service.common.membercard.RequestService;
import com.gt.member.service.member.MemberNoticeService;
import com.gt.member.util.*;
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

    @Autowired
    private SystemNoticeDAO systemNoticeDAO;

    @Autowired
    private SystemCalltypeDAO systemCalltypeDAO;


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
    private RequestService requestService;

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
	List< Map > msgTemplates = requestService.selectTempObjByBusId( busId );
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
		    if ( systemNotice.getSmsStatus() == 1 ) {
			String smsContent = systemNotice.getSmsContent();
			if ( CommonUtil.isNotEmpty( smsContent ) && smsContent.length() > 70 ) {
			    throw new BusinessException( ResponseMemberEnums.SMS_BIG_THAN_70 );
			}
		    }
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
	    if ( id > 0 ) {
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
		map.put( "memberCount", count );
	    }
	    BusUser busUser=requestService.findBususer( busId );
	    map.put( "smscount", busUser.getSmsCount() );
	    return map;
	} catch ( BusinessException e ) {
	    throw e;
	} catch ( Exception e ) {
	    throw new BusinessException( ResponseEnums.ERROR );
	}
    }

    public Integer countMember( String ctIds, Integer busId ) {
	int count = 0;
	if ( "0".equals( ctIds ) ) {
	    // 查询公众号下所有会员
	    count = memberCardDAO.countCardAll( busId );
	} else {
	    if ( ctIds != null && !"".equals( ctIds ) ) {
		String[] str = ctIds.split( "," );
		List< Integer > list = new ArrayList< Integer >();
		for ( int i = 0; i < str.length; i++ ) {
		    if ( CommonUtil.isNotEmpty( str[i] ) ) {
			list.add( Integer.valueOf( str[i] ) );
		    }
		}
		count = memberCardDAO.countCard( busId, list );
	    }
	}
	return count;
    }

    @Transactional
    public void saveMemberNotice( Integer busId, Map<String,Object> obj ) throws BusinessException {
	Map< String,Object > map = new HashMap< String,Object >();
	try {
	    if ( CommonUtil.isEmpty( busId ) ) {
		throw new BusinessException( ResponseMemberEnums.INVALID_SESSION );
	    }

	    MemberNotice memberNotice = new MemberNotice();
	    memberNotice.setContent( CommonUtil.toString( obj.get( "content" ) ) );
	    memberNotice.setId( CommonUtil.toInteger( obj.get( "id" ) ) );
	    memberNotice.setNoticeMember( CommonUtil.toInteger( obj.get( "noticeUser" ) ) );
	    memberNotice.setSendMsg( CommonUtil.toInteger( obj.get( "sendMsg" ) ) );
	    memberNotice.setSendSms( CommonUtil.toInteger( obj.get( "sendSms" ) ) );
	    memberNotice.setTitle( CommonUtil.toString( obj.get( "title" ) ) );
	    memberNotice.setNoticeUser( CommonUtil.toString( obj.get( "noticeUser" ) ) );
	    if ( CommonUtil.isNotEmpty( obj.get( "smsContent" ) ) ) {
		String smsContent = CommonUtil.toString( obj.get( "smsContent" ) );
		if ( smsContent.length() > 70 ) {
		    throw new BusinessException( ResponseMemberEnums.SMS_BIG_THAN_70 );
		}
		memberNotice.setSmsContent( smsContent );
	    }

	    String sendDate = CommonUtil.toString( obj.get( "sendDate" ) );
	    memberNotice.setSendType( CommonUtil.toInteger( obj.get( "sendType" ) ) );

	    if ( CommonUtil.isNotEmpty( sendDate ) ) {
		Date date = DateTimeKit.parse( sendDate, "yyyy-MM-dd HH:mm:ss" );
		memberNotice.setSendDate( date );
	    }
	    memberNotice.setBusId( busId );
	    memberNotice.setCreateDate( new Date() );
	    if ( memberNotice.getSendType() == 0 ) {
		memberNotice.setSendDate( new Date() );
		memberNotice.setSendStuts( 1 );
	    }
	    if ( memberNotice.getSendType() == 2 ) {
		memberNotice.setSendStuts( 3 );
	    }
	    if ( memberNotice.getSendType() == 3 ) {
		memberNotice.setSendStuts( 4 );
	    }

	    if ( memberNotice.getId() > 0 ) {
		memberNoticeDAO.updateById( memberNotice );
		//删除之前信息
		memberNoticeuserDAO.deleteByNoticeId( memberNotice.getId() );

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
			    if(memberNotice.getSendType() == 0){
				nu.setStatus( 3 );
			    }else {
				nu.setStatus( 2 );
			    }
			    if(CommonUtil.isEmpty( member.get( "phone" ) )){
			        continue;
			    }
			    nu.setPhone( CommonUtil.toString( member.get( "phone" ) ) );
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
		    sendNotice( memberNotice.getId() );
		}
	    }

	} catch ( BusinessException e ) {
	    throw e;
	} catch ( Exception e ) {
	    LOG.error( "保存消息模板异常", e );
	    throw new BusinessException( ResponseEnums.ERROR );
	}

    }

    @Transactional
    public void deleteMemberNotice( Integer id ) throws BusinessException {
	try {
	    memberNoticeuserDAO.deleteByNoticeId( id );
	    memberNoticeDAO.deleteById( id );
	} catch ( Exception e ) {
	    LOG.error( "删除会员通知消息异常", e );
	    throw new BusinessException( ResponseEnums.ERROR );
	}
    }

    public void sendNotice( Integer id ) throws BusinessException {
	try {
	    MemberNotice memberNotice = memberNoticeDAO.selectById( id );
	    List< Map< String,Object > > memberNoticeUsers = memberNoticeuserDAO.findByNoticeId( id );
	    StringBuffer phoneSb = new StringBuffer();
	    List< Integer > ids = new ArrayList<>();
	    Integer i = 0;
	    for ( Map< String,Object > map : memberNoticeUsers ) {
		if ( CommonUtil.isNotEmpty( map.get( "phone" ) ) ) {
		    phoneSb.append( map.get( "phone" ) + "," );
		    ids.add( CommonUtil.toInteger( map.get( "id" ) ) );
		    i++;
		    if ( i < 100 && i < memberNoticeUsers.size() ) {
			continue;
		    }

		    RequestUtils< OldApiSms > requestUtils = new RequestUtils< OldApiSms >();
		    String phone = phoneSb.toString();
		    String phones = phone.substring( 0, phone.lastIndexOf( "," ) );
		    OldApiSms oldApiSms = new OldApiSms();
		    oldApiSms.setMobiles( phones );
		    oldApiSms.setContent( memberNotice.getSmsContent() );
		    oldApiSms.setCompany( PropertiesUtil.getSms_name() );
		    oldApiSms.setBusId( memberNotice.getBusId() );
		    oldApiSms.setModel( 3 );
		    String notifyUrl = PropertiesUtil.getWebHome() + "/memberNodoInterceptor/memberNotDo/smsNotice";
		    oldApiSms.setNotifyUrl( notifyUrl );
		    requestUtils.setReqdata( oldApiSms );
		    try {
			String result = requestService.sendSms( requestUtils );
			JSONObject json = JSON.parseObject( result );

			if ( "0".equals( CommonUtil.toString( json.get( "code" ) ) ) ) {
			    JSONObject jsonOb = JSON.parseObject( json.getString( "data" ) );
			    memberNoticeuserDAO.updateByIds( ids, CommonUtil.toInteger( jsonOb.get( "msgid" ) ) );
			}
			ids.clear();
		    } catch ( Exception e ) {
			LOG.error( "短信发送失败", e );
		    }
		}

	    }
	} catch ( Exception e )
	{
	    LOG.error( "发送消息异常", e );
	    throw new BusinessException( ResponseEnums.ERROR );

	}

    }

    public Page findMemberNotice( Integer busId, String paramsStr ) {
	Map< String,Object > params = JSON.toJavaObject( JSON.parseObject( paramsStr ), Map.class );
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
	    String[] strs = memberIds.split( "," );
	    MemberNoticeuser noticeUser = null;
	    String[] str=memberIds.split( "," );
	    List<Integer> memberIdList=new ArrayList<>(  );
	    for ( int i=0;i<str.length;i++ ){
	        if(CommonUtil.isNotEmpty( str[i] )){
	            memberIdList.add( CommonUtil.toInteger( str[i] ) );
		}
	    }

	    List<MemberNoticeuser> list=new ArrayList<>(  );
	    if(memberNotice.getSendSms()==1) {
		List< Map< String,Object > > members = memberMapper.findByMemberIds( memberNotice.getBusId(), memberIdList );
		for ( Map<String, Object> map: members) {
		    noticeUser = new MemberNoticeuser();
		    noticeUser.setNoticeId( id );
		    noticeUser.setBusId( CommonUtil.toInteger( map.get( "id" ) ) );
		    noticeUser.setSendDate( new Date(  ) );
		    noticeUser.setMsgType( 0 );
		    noticeUser.setStatus(3);
		    noticeUser.setPhone( CommonUtil.toString( map.get( "phone" ) ) );
		    list.add( noticeUser );
		}
	    }

	    if(memberNotice.getSendMsg()==1){
	        for(Integer memberId:memberIdList) {
		    noticeUser = new MemberNoticeuser();
		    noticeUser.setNoticeId( id );
		    noticeUser.setBusId( memberId );
		    noticeUser.setSendDate(  new Date(  ) );
		    noticeUser.setMsgType( 1 );
		    noticeUser.setStatus( 0);
		    list.add( noticeUser );
		}
	    }

	    if ( list.size() > 0 ) {
		memberNoticeuserDAO.saveList( list );
	    }

	    if ( memberNotice.getSendSms() == 1 ) {
		sendNotice(id);
	    }
	} catch ( Exception e ) {
	    LOG.error( "方法sendNoticeToUser:发送信息给用户异常" );
	    throw new BusinessException( ResponseEnums.ERROR );
	}
    }

    public Page findNoticeUser( String paramstr ) throws BusinessException {
	Map< String,Object > params = JSON.toJavaObject( JSON.parseObject( paramstr ), Map.class );
	params.put( "curPage", CommonUtil.isEmpty( params.get( "curPage" ) ) ? 1 : CommonUtil.toInteger( params.get( "curPage" ) ) );
	int pageSize = 10;
	Integer status = 0;
	if ( CommonUtil.isNotEmpty( params.get( "status" ) ) ) {
	    status = CommonUtil.toInteger( params.get( "status" ) );
	}
	Integer noticeId = CommonUtil.toInteger( params.get( "noticeId" ) );

	int rowCount = memberNoticeuserDAO.countNoticeuser( noticeId, status );

	Page page = new Page( CommonUtil.toInteger( params.get( "curPage" ) ), pageSize, rowCount, "" );
	params.put( "firstResult", pageSize * ( ( page.getCurPage() <= 0 ? 1 : page.getCurPage() ) - 1 ) );

	List< Map< String,Object > > list = memberNoticeuserDAO.findNoticeuser( noticeId, status, Integer.parseInt( params.get( "firstResult" ).toString() ), pageSize );
	page.setSubList( list );
	return page;
    }

    public void resendNoticeUser( String params ) throws BusinessException {
	try {
	    Map< String,Object > paramaMap = JSON.toJavaObject( JSON.parseObject( params ), Map.class );
	    Integer noticeId = CommonUtil.toInteger( paramaMap.get( "noticeId" ) );
	    List< Map< String,Object > > memberNoticeUsers = memberNoticeuserDAO.findReSendNoticeUser( noticeId );
	    MemberNotice memberNotice = memberNoticeDAO.selectById( noticeId );

	    StringBuffer phoneSb = new StringBuffer();
	    List< Integer > ids = new ArrayList<>();
	    Integer i = 0;
	    for ( Map< String,Object > map : memberNoticeUsers ) {
		if ( CommonUtil.isNotEmpty( map.get( "phone" ) ) ) {
		    phoneSb.append( map.get( "phone" ) + "," );
		    ids.add( CommonUtil.toInteger( map.get( "id" ) ) );
		}
		//粉丝在100条内
		if ( memberNoticeUsers.size() < 100 ) {
		    RequestUtils< OldApiSms > requestUtils = new RequestUtils< OldApiSms >();
		    String phone = phoneSb.toString();
		    phone = phone.substring( 0, phone.lastIndexOf( "," ) );
		    OldApiSms oldApiSms = new OldApiSms();
		    oldApiSms.setMobiles( phone );
		    oldApiSms.setContent( memberNotice.getSmsContent() );
		    oldApiSms.setCompany( PropertiesUtil.getSms_name() );
		    oldApiSms.setBusId( memberNotice.getId() );
		    oldApiSms.setModel( 3 );
		    requestUtils.setReqdata( oldApiSms );
		    try {
			String result = requestService.sendSms( requestUtils );
			JSONObject json = JSON.parseObject( result );

			if ( "0".equals( CommonUtil.toString( json.get( "code" ) ) ) ) {
			    JSONObject jsonOb = JSON.parseObject( json.getString( "data" ) );
			    memberNoticeuserDAO.updateByIds( ids, CommonUtil.toInteger( jsonOb.get( "msgid" ) ) );
			}
			ids.clear();
		    } catch ( Exception e ) {
			LOG.error( "短信发送失败", e );
		    }
		} else {
		    if ( ( i % 100 == 0 ) || ( i + 1 == memberNoticeUsers.size() ) ) {
			phoneSb = new StringBuffer();
			RequestUtils< OldApiSms > requestUtils = new RequestUtils< OldApiSms >();
			String phone = phoneSb.toString();
			String phones = phone.substring( 0, phone.lastIndexOf( "," ) );
			phones = phones.substring( 0, phones.lastIndexOf( "," ) );
			OldApiSms oldApiSms = new OldApiSms();
			oldApiSms.setMobiles( phones );
			oldApiSms.setContent( memberNotice.getSmsContent() );
			oldApiSms.setCompany( PropertiesUtil.getSms_name() );
			oldApiSms.setBusId( memberNotice.getId() );
			oldApiSms.setModel( 3 );
			String notifyUrl = PropertiesUtil.getWebHome() + "/memberNodoInterceptor/memberNotDo/smsNotice";
			oldApiSms.setNotifyUrl( notifyUrl );
			requestUtils.setReqdata( oldApiSms );
			try {
			    String result = requestService.sendSms( requestUtils );
			    JSONObject json = JSON.parseObject( result );

			    if ( "0".equals( CommonUtil.toString( json.get( "code" ) ) ) ) {
				JSONObject jsonOb = JSON.parseObject( json.getString( "data" ) );
				memberNoticeuserDAO.updateByIds( ids, CommonUtil.toInteger( jsonOb.get( "msgid" ) ) );
			    }
			    ids.clear();
			} catch ( Exception e ) {
			    LOG.error( "短信发送失败", e );
			}
		    }
		}
		i++;
	    }
	} catch ( Exception e ) {
	    LOG.error( "发送消息异常", e );
	    throw new BusinessException( ResponseEnums.ERROR );

	}
    }



    public void deleteMemberNoticeModel(Integer id)throws BusinessException{
      Integer count= memberNoticeuserDAO.countByNoticeId( id );
      if(count>0){
          throw new BusinessException( ResponseMemberEnums.SEND_NOTICE );
      }
      memberNoticeDAO.deleteById( id );

    }

}
