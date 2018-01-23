package com.gt.member.service.common.quartz;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.gt.api.util.RequestUtils;
import com.gt.member.dao.*;
import com.gt.member.entity.*;
import com.gt.member.service.common.membercard.MemberCommonService;
import com.gt.member.service.common.membercard.RequestService;
import com.gt.member.util.CommonUtil;
import com.gt.member.util.DateTimeKit;
import com.gt.member.util.PropertiesUtil;
import com.gt.util.entity.param.sms.OldApiSms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@Component
public class MemberQuartzServiceImpl implements MemberQuartzService {

    private static final Logger LOG = LoggerFactory.getLogger( MemberQuartzServiceImpl.class );


    @Autowired
    private MemberEntityDAO memberMapper;

    @Autowired
    private MemberNoticeDAO memberNoticeMapper;

    @Autowired
    private MemberNoticeuserDAO memberNoticeuserDAO;

    @Autowired
    private PublicParametersetDAO publicParameterSetMapper;

    @Autowired
    private SystemnoticecallDAO systemNoticeCallMapper;

    @Autowired
    private SystemNoticeDAO systemNoticeMapper;

    @Autowired
    private MemberBirDAO memberBirMapper;

    @Autowired
    private RequestService requestService;

    @Autowired
    private MemberCardrecordNewDAO memberCardrecordNewDAO;

    @Autowired
    private MemberCommonService memberCommonService;

    /**
     * 过期卡券 修改领取卡券状态
     * 每天早上1点扫描
     */
    @Override
    public void updateCardGet() {
	//		try {
	//			String getDateTime = DateTimeKit.getDateTime();
	//			String sql = "UPDATE t_duofen_card_get  SET state=2 WHERE state=0 AND endTime<'"
	//					+ getDateTime + "'";
	//
	//			daoUtil.update(sql);
	//		} catch (Exception e) {
	//			e.printStackTrace();
	//			logger.error("修改优惠券过期状态异常", e);
	//		}
    }

    /**
     * 修改会员消息信息 2小时扫描一次
     */
    @Override
    public void updateNotice() {
	try {
	    Date date = new Date();
	    Integer count = memberNoticeMapper.findAllNotSendByMsg( date );
	    if ( count > 0 ) {
		memberNoticeMapper.updateNotice( date );
	    }
	} catch ( Exception e ) {
	    LOG.error( "修改修改会员消息信息异常", e );
	}
    }

    /**
     * 会员短信定时发送扫描
     * 每2小时访问一次
     */
    @Override
    public void sendNotice() {
	try {

	    List< Map< String,Object > > memberNotices = memberNoticeMapper.findAllNotSend( new Date() );

	    for ( Map< String,Object > notice : memberNotices ) {
		Integer noticeId = CommonUtil.toInteger( notice.get( "id" ) );
		String smsContent = CommonUtil.toString( notice.get( "smsContent" ) );
		Integer busId = CommonUtil.toInteger( notice.get( "busId" ) );
		List< Map< String,Object > > memberNoticeUsers = memberNoticeuserDAO.findByNoticeId( noticeId );
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
			oldApiSms.setContent( smsContent );
			oldApiSms.setCompany( PropertiesUtil.getSms_name() );
			oldApiSms.setBusId( noticeId );
			oldApiSms.setModel( 3 );
			requestUtils.setReqdata( oldApiSms );
			try {
			    String result = requestService.sendSms( requestUtils );
			    com.alibaba.fastjson.JSONObject json = JSON.parseObject( result );

			    if ( "0".equals( CommonUtil.toString( json.get( "code" ) ) ) ) {
				com.alibaba.fastjson.JSONObject jsonOb = JSON.parseObject( json.getString( "data" ) );
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
			    oldApiSms.setContent( smsContent );
			    oldApiSms.setCompany( PropertiesUtil.getSms_name() );
			    oldApiSms.setBusId( noticeId );
			    oldApiSms.setModel( 3 );
			    String notifyUrl = PropertiesUtil.getWebHome() + "/memberNodoInterceptor/memberNotDo/smsNotice";
			    oldApiSms.setNotifyUrl( notifyUrl );
			    requestUtils.setReqdata( oldApiSms );
			    try {
				String result = requestService.sendSms( requestUtils );
				com.alibaba.fastjson.JSONObject json = JSON.parseObject( result );

				if ( "0".equals( CommonUtil.toString( json.get( "code" ) ) ) ) {
				    com.alibaba.fastjson.JSONObject jsonOb = JSON.parseObject( json.getString( "data" ) );
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
		MemberNotice memberNotice = new MemberNotice();
		memberNotice.setId( noticeId );
		memberNotice.setSendStuts( 1 );
		memberNoticeMapper.updateById( memberNotice );
	    }
	} catch ( Exception e ) {
	    LOG.error( "发送消息定时器异常", e );
	}
    }

    /**
     * 每月1号凌晨积分清零
     */
    @Override
    public void clearJifen() {
	//
	List< PublicParameterset > publicParameterSets = publicParameterSetMapper.findMonth();
	if ( CommonUtil.isEmpty( publicParameterSets ) || publicParameterSets.size() == 0 ) {
	    return;
	}
	Integer month = DateTimeKit.getMonth( new Date() );

	List< Integer > busIds = new ArrayList<>();
	for ( PublicParameterset p : publicParameterSets ) {
	    if ( p.getMonth() != month ) {
		continue;
	    }
	    // 执行积分清零
	    busIds.add( p.getBusId() );
	}
	if ( CommonUtil.isEmpty( busIds.toString() ) ) {
	    return;
	}

	try {
	    Date date = DateTimeKit.addMonths( new Date(), -12 );
	    Date startdate = DateTimeKit.addMonths( new Date(), -24 );
	    for ( int i = 0; i < busIds.size(); i++ ) {
		Integer busId = busIds.get( i );
		if ( CommonUtil.isEmpty( busId ) ) continue;
		List< Map< String,Object > > upperYear = memberCardrecordNewDAO.sumByBusId( busId, startdate, date ); // 前一年数据统计
		List< Map< String,Object > > currentYear = memberCardrecordNewDAO.sumCurrentByBusId( busId, date, new Date() );
		for ( Map< String,Object > map : upperYear ) {
		    Double number = CommonUtil.toDouble( map.get( "number" ) );
		    Integer memberId = CommonUtil.toInteger( map.get( "memberId" ) );
		    MemberEntity member = memberMapper.selectById( memberId );

		    if ( CommonUtil.isNotEmpty( number ) && number > 0 ) {
			if ( currentYear.size() == 0 ) {
			    Double balace = member.getIntegral() - number;
			    // 执行清楚计划
			    memberCommonService.saveCardRecordOrderCodeNew( memberId, 2, number, "积分清除", busId, balace, "0", 2 );
			    // 修改粉丝的积分
			    MemberEntity m1 = new MemberEntity();
			    m1.setId( member.getId() );
			    m1.setIntegral( balace.intValue() );
			    memberMapper.updateById( m1 );
			}

			for ( Map< String,Object > map2 : currentYear ) {
			    Integer memberId1 = CommonUtil.toInteger( map2.get( "memberId" ) );
			    Double number1 = CommonUtil.toDouble( map2.get( "number" ) );
			    if ( memberId.equals( memberId1 ) ) {

				if ( number > number1 ) {
				    Double numberBalace = number - number1;
				    // 执行清楚计划
				    Double balace = member.getIntegral() - numberBalace;
				    // 执行清楚计划
				    memberCommonService.saveCardRecordOrderCodeNew( memberId, 2, numberBalace, "积分清除", busId, balace, "0", 2 );
				    // 修改粉丝的积分
				    MemberEntity m1 = new MemberEntity();
				    m1.setId( member.getId() );
				    m1.setIntegral( balace.intValue() );
				    memberMapper.updateById( m1 );
				    break;

				}
			    }
			}
		    }
		}
	    }
	} catch ( Exception e ) {
	    LOG.error( "积分清0异常了", e );
	}

    }

    /**
     * 每月22号0 点触发 系统消息提醒
     */
    @Override
    public void clearJifenSendMessage() {
	try {
	    List< PublicParameterset > publicParameterSets = publicParameterSetMapper.findMonth();
	    if ( CommonUtil.isEmpty( publicParameterSets ) || publicParameterSets.size() == 0 ) {
		return;
	    }
	    Integer month = DateTimeKit.getMonth( new Date() );

	    month = month + 1;
	    List< Integer > busIds = new ArrayList< Integer >();
	    for ( PublicParameterset p : publicParameterSets ) {
		if ( p.getMonth() != month ) {
		    continue;
		}
		// 执行积分清零
		busIds.add( p.getBusId() );
	    }

	    if ( busIds.size() <= 0 ) {
		return;
	    }

	    List< Map< String,Object > > systemNotices = systemNoticeMapper.findMsgBybusIdEq13( busIds );

	    List< Integer > newBusIds = new ArrayList< Integer >();
	    // 查询用户积分系统消息发送的商家
	    for ( Map< String,Object > map : systemNotices ) {
		if ( "1".equals( map.get( "msgStatus" ).toString() ) ) {
		    newBusIds.add( CommonUtil.toInteger( map.get( "busId" ) ) );
		}
	    }
	    if ( newBusIds.size() == 0 ) {
		return;
	    }

	    String dateStr = DateTimeKit.getCurrentYear() + "-" + month + "-01";
	    Date d = DateTimeKit.parse( dateStr, "yyyy-MM-dd" );
	    Date date = DateTimeKit.addMonths( d, -12 );
	    Date startdate = DateTimeKit.addMonths( d, -24 );

	    for ( int i = 0; i < busIds.size(); i++ ) {
		Integer busId = busIds.get( i );
		if ( CommonUtil.isEmpty( busId ) ) continue;
		List< Map< String,Object > > upperYear = memberCardrecordNewDAO.sumByBusId( busId, startdate, date ); // 前一年数据统计

		List< Map< String,Object > > currentYear = memberCardrecordNewDAO.sumCurrentByBusId( busId, date, new Date() );

		List< Systemnoticecall > sncList = new ArrayList< Systemnoticecall >();
		Systemnoticecall s = null;
		for ( Map< String,Object > cmap : upperYear ) {
		    boolean flag = false;
		    Double number = CommonUtil.toDouble( cmap.get( "number" ) );
		    Integer memberId = CommonUtil.toInteger( cmap.get( "memberId" ) );

		    if ( CommonUtil.isNotEmpty( number ) && number > 0 ) {
			if ( currentYear.size() == 0 ) {
			    s = new Systemnoticecall();
			    s.setDescribes( "尊敬的用户：你去年还有" + number + "积分未使用,请你在本月内使用完。不然，我们将会下个月1号将清除去年未使用的积分。" );
			    s.setMemberId( memberId );
			    s.setCreateDate( new Date() );
			    sncList.add( s );
			}

			for ( Map< String,Object > map2 : currentYear ) {
			    Integer memberId1 = CommonUtil.toInteger( map2.get( "memberId" ) );
			    Double number1 = CommonUtil.toDouble( map2.get( "number1" ) );
			    if ( memberId.equals( memberId1 ) ) {
				if ( number > number1 ) {
				    Double amount = number - number1;
				    s = new Systemnoticecall();
				    s.setDescribes( "尊敬的用户：你去年还有" + amount + "积分未使用,请你在本月内使用完。不然，我们将会下个月1号将清除去年未使用的积分。" );
				    s.setMemberId( memberId );
				    s.setCreateDate( new Date() );
				    sncList.add( s );
				}
			    }
			}

		    }
		    if ( sncList.size() > 0 ) {
			try {
			    // 保存数据
			    systemNoticeCallMapper.saveList( sncList );
			} catch ( Exception e ) {
			    e.printStackTrace();
			}
		    }
		}
	    }
	} catch ( Exception e ) {
	    LOG.error( "clearJifenSendMessage保存系统通知异常", e );
	}
    }

    /**
     * 每月24号下午3点触发 短信提醒
     */
    @Override
    public void clearJifenSendSmsMessage() {
        try {
	    List< PublicParameterset > publicParameterSets = publicParameterSetMapper.findMonth();
	    if ( CommonUtil.isEmpty( publicParameterSets ) || publicParameterSets.size() == 0 ) {
		return;
	    }
	    Integer month = DateTimeKit.getMonth( new Date() );

	    month = month + 1;

	    List< Integer > busIds = new ArrayList< Integer >();
	    for ( PublicParameterset p : publicParameterSets ) {
		if ( p.getMonth() != month ) {
		    continue;
		}
		// 执行积分清零
		busIds.add( p.getBusId() );
	    }

	    if ( busIds.size() <= 0 ) {
		return;
	    }

	    // 短信通知积分
	    List< Map< String,Object > > systemNotices = systemNoticeMapper.findMsgBybusIdEq13( busIds );

	    String dateStr = DateTimeKit.getCurrentYear() + "-" + month + "-01";
	    Date d = DateTimeKit.parse( dateStr, "yyyy-MM-dd" );

	    Date date = DateTimeKit.addMonths( d, -12 );
	    Date startdate = DateTimeKit.addMonths( d, -24 );

	    for ( int i = 0; i < busIds.size(); i++ ) {
		Integer busId = busIds.get( i );
		if ( CommonUtil.isEmpty( busId ) ) continue;
		List< Map< String,Object > > upperYear = memberCardrecordNewDAO.sumByBusId( busId, startdate, date ); // 前一年数据统计
		List< Map< String,Object > > currentYear = memberCardrecordNewDAO.sumCurrentByBusId( busId, date, new Date() );
		try {
		    for ( Map< String,Object > cmap : upperYear ) {
			Double number = CommonUtil.toDouble( cmap.get( "number" ) );
			Integer memberId = CommonUtil.toInteger( cmap.get( "memberId" ) );
			MemberEntity m = memberMapper.selectById( memberId );
			boolean flag = false;
			if ( CommonUtil.isNotEmpty( number ) && number > 0 ) {

			    if ( currentYear.size() == 0 ) {
				String content = "尊敬的用户：你去年还有" + number + "积分未使用,请你在本月内使用完。不然，我们将会下个月1号将清除去年未使用的积分。";
				RequestUtils< OldApiSms > requestUtils = new RequestUtils< OldApiSms >();
				OldApiSms oldApiSms = new OldApiSms();
				oldApiSms.setMobiles( m.getPhone() );
				oldApiSms.setContent( content );
				oldApiSms.setCompany( PropertiesUtil.getSms_name() );
				oldApiSms.setBusId( busId );
				oldApiSms.setModel( 3 );
				requestUtils.setReqdata( oldApiSms );
				try {
				    String smsStr = requestService.sendSms( requestUtils );
				} catch ( Exception e ) {
				    LOG.error( "短信发送失败", e );
				}
			    }

			    for ( Map< String,Object > map2 : currentYear ) {
				Integer member1 = CommonUtil.toInteger( map2.get( "member" ) );
				Double number1 = CommonUtil.toDouble( cmap.get( "number" ) );
				if ( memberId.equals( member1 ) ) {
				    if ( number > number1 ) {
					Double amount = number - number1;
					String content = "尊敬的用户：你去年还有" + amount + "积分未使用,请你在本月内使用完。不然，我们将会下个月1号将清除去年未使用的积分。";

					RequestUtils< OldApiSms > requestUtils = new RequestUtils< OldApiSms >();
					OldApiSms oldApiSms = new OldApiSms();
					oldApiSms.setMobiles( m.getPhone() );
					oldApiSms.setContent( content );
					oldApiSms.setCompany( PropertiesUtil.getSms_name() );
					oldApiSms.setBusId( busId );
					oldApiSms.setModel( 3 );
					requestUtils.setReqdata( oldApiSms );
					try {
					    String smsStr = requestService.sendSms( requestUtils );
					} catch ( Exception e ) {
					    LOG.error( "短信发送失败", e );
					}
				    }
				}
			    }
			}
		    }
		} catch ( Exception e ) {
		    LOG.error( "积分清零发送短信异常", e );
		}
	    }
	}catch ( Exception e ){
	    LOG.error("clearJifenSendSmsMessage积分清零",e);
	}
    }

    /**
     * 每天凌晨 生日推送
     */
    //  @Scheduled( cron = "0 0 3 * * ?" )
    @Override
    public void birthdayMsg() {

	List< Map< String,Object > > memberList = memberMapper.findMemberBir();
	//
	List< Map< String,Object > > sysNoticeList = systemNoticeMapper.findMsgBybusIdEq10();

	List< Systemnoticecall > sncs = new ArrayList< Systemnoticecall >();
	Systemnoticecall s = null;
	for ( Map< String,Object > sn : sysNoticeList ) {
	    if ( CommonUtil.isEmpty( sn.get( "msgContent" ) ) || CommonUtil.toInteger( sn.get( "msgStatus" ) ) == 0 ) {
		continue;
	    }
	    for ( Map< String,Object > map : memberList ) {
		if ( CommonUtil.toString( sn.get( "busId" ) ).equals( CommonUtil.toString( map.get( "busId" ) ) ) ) {
		    s = new Systemnoticecall();
		    s.setMemberId( CommonUtil.toInteger( map.get( "id" ) ) );
		    s.setDescribes( CommonUtil.toString( sn.get( "msgContent" ) ) );
		    s.setCreateDate( new Date() );
		    sncs.add( s );
		}
	    }
	}

	if ( sncs.size() > 0 ) {
	    systemNoticeCallMapper.saveList( sncs );
	}
    }

    /**
     * 每天凌晨3点触发  过滤数据
     */
    // @Scheduled( cron = "0 0 3 * * ?" )
    @Override
    public void birthdaySms() {

	List< Map< String,Object > > memberList = memberMapper.findMemberBir();

	List< MemberBir > list = new ArrayList<>();
	for ( Map< String,Object > map : memberList ) {
	    if ( CommonUtil.isNotEmpty( map.get( "phone" ) ) ) {
		MemberBir mb = new MemberBir();
		mb.setPhone( CommonUtil.toString( map.get( "phone" ) ) );
		mb.setBusId( CommonUtil.toInteger( map.get( "busId" ) ) );
		list.add( mb );
	    }
	}
	if ( list.size() > 0 ) {
	    memberBirMapper.insertList( list );
	}
    }

    /**
     * 每天凌晨9点触发 短信提醒
     */
    //  @Scheduled( cron = "0 0 9 * * ?" )
    public void sendBir() {

	List< Map< String,Object > > memberBirList = memberBirMapper.findAll();
	List< Map< String,Object > > sysNoticeList = systemNoticeMapper.findSmsBybusIdEq10();

	Integer busId = 0;
	for ( Map< String,Object > sn : sysNoticeList ) {
	    String phone = "";
	    for ( Map< String,Object > map : memberBirList ) {
		if ( CommonUtil.toString( sn.get( "busId" ) ).equals( CommonUtil.toString( map.get( "busId" ) ) ) && CommonUtil.isNotEmpty( map.get( "phone" ) ) ) {
		    phone += map.get( "phone" ) + ",";
		}
	    }
	    if ( CommonUtil.isEmpty( phone ) ) {
		continue;
	    }
	    phone = phone.substring( 0, phone.lastIndexOf( "," ) );

	    //发送短信
	    String content = CommonUtil.toString( sn.get( "smsContent" ) );

	    RequestUtils< OldApiSms > requestUtils = new RequestUtils< OldApiSms >();
	    OldApiSms oldApiSms = new OldApiSms();
	    oldApiSms.setMobiles( content );
	    oldApiSms.setContent( content );
	    oldApiSms.setCompany( PropertiesUtil.getSms_name() );
	    oldApiSms.setBusId( CommonUtil.toInteger( sn.get( "busId" ) ) );
	    oldApiSms.setModel( 3 );
	    requestUtils.setReqdata( oldApiSms );
	    try {
		String smsStr = requestService.sendSms( requestUtils );
	    } catch ( Exception e ) {
		LOG.error( "短信发送失败", e );
	    }
	}
	memberBirMapper.deleteAll();
    }

}
