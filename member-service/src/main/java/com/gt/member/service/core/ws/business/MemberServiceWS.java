package com.gt.member.service.core.ws.business;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gt.member.dao.MemberAppletOpenidDAO;
import com.gt.member.dao.MemberCardDAO;
import com.gt.member.dao.MemberDAO;
import com.gt.member.dao.MemberOldDAO;
import com.gt.member.entity.Member;
import com.gt.member.entity.MemberCard;
import com.gt.member.entity.MemberOld;
import com.gt.member.service.MemberNewService;
import com.gt.member.service.core.ws.entitybo.queryBo.*;
import com.gt.member.service.core.ws.entitybo.returnBo.BaseResult;
import com.gt.member.service.core.ws.entitybo.returnBo.MemberBo;
import com.gt.member.service.core.ws.entitybo.returnBo.ReturnCode;
import com.gt.member.service.old.member.SystemMsgService;
import com.gt.member.util.CommonUtil;
import com.gt.member.util.JedisUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 会员业务处理类
 * Created by Administrator on 2017/7/27 0027.
 */
@Service( value = "memberServiceWS" )
public class MemberServiceWS {

    private static final Logger LOG = Logger.getLogger( MemberServiceWS.class );

    @Autowired
    private MemberDAO memberDAO;

    @Autowired
    private MemberCardDAO memberCardDAO;

    @Autowired
    private MemberOldDAO memberOldDAO;

    @Autowired
    private MemberAppletOpenidDAO memberAppletOpenidDAO;

    @Autowired
    private MemberNewService memberNewService;

    @Autowired
    private SystemMsgService systemMsgService;

    /**
     * 查询粉丝信息
     *
     * @param baseParam
     *
     * @return
     */
    public BaseResult< MemberBo > findMemberById( BaseParam baseParam ) {
	BaseResult br = new BaseResult();
	try {
	    GetById byId = JSONObject.parseObject( JSONObject.toJSONString( baseParam.getReqdata() ), GetById.class );
	    Member member = memberDAO.selectById( byId.getId() );
	    MemberBo memberBo = new MemberBo();
	    memberBo.setId( member.getId() );
	    memberBo.setBusId( member.getBusId() );
	    memberBo.setCity( member.getCity() );
	    memberBo.setCountry( member.getCountry() );
	    memberBo.setFansCurrency( member.getFansCurrency() );
	    memberBo.setFlow( member.getFlow() );
	    memberBo.setMcId( member.getMcId() );
	    memberBo.setNickname( member.getNickname() );
	    memberBo.setIntegral( member.getIntegral() );
	    memberBo.setOpenid( member.getOpenid() );
	    memberBo.setPhone( member.getPhone() );
	    memberBo.setPublicId( member.getPublicId() );
	    memberBo.setPwd( member.getPwd() );
	    memberBo.setSex( member.getSex() );
	    br.setCode( ReturnCode.SUCCESS );
	    br.setData( JSONObject.toJSONString( memberBo ) );
	} catch ( Exception e ) {
	    LOG.error( "MemberServiceWS类中findMemberById方法异常了", e );
	    br.setCode( ReturnCode.ERROR_1 );
	    br.setMsg( "调用数据接口异常了" );
	}
	return br;
    }

    /**
     * 判断是否是会员
     *
     * @param baseParam
     *
     * @return
     */
    public BaseResult< MemberBo > isMemberById( BaseParam baseParam ) {
	BaseResult br = new BaseResult();
	try {
	    GetById byId = JSONObject.parseObject( JSONObject.toJSONString( baseParam.getReqdata() ), GetById.class );
	    Member member = memberDAO.selectById( byId.getId() );
	    MemberBo memberBo = new MemberBo();
	    memberBo.setMemberCard( true );
	    if ( CommonUtil.isEmpty( member.getMcId() ) ) {
		memberBo.setMemberCard( false );
	    } else {
		MemberCard mc = memberCardDAO.selectById( member.getMcId() );
		if ( mc.getCardStatus() == 1 ) {
		    memberBo.setMemberCard( false );
		}
	    }
	    br.setCode( ReturnCode.SUCCESS );
	    br.setData( JSONObject.toJSONString( memberBo ) );
	} catch ( Exception e ) {
	    LOG.error( "MemberServiceWS类中isMemberById方法异常了", e );
	    br.setCode( ReturnCode.ERROR_1 );
	    br.setMsg( "转换MemberBo对象异常了" );
	}
	return br;
    }

    /**
     * 查询粉丝所有id
     *
     * @param memberId
     *
     * @return
     */
    public BaseResult< MemberBo > findMemberIds( Integer memberId ) {
	BaseResult br = new BaseResult();
	List< Integer > list = null;
	try {
	    MemberBo mb = new MemberBo();
	    list = new ArrayList< Integer >();
	    Member member = memberDAO.selectById( memberId );
	    if ( CommonUtil.isEmpty( member.getOldId() ) ) {
		list.add( memberId );
		mb.setIds( list );
	    } else {
		String[] str = member.getOldId().split( "," );
		for ( int i = 0; i < str.length; i++ ) {
		    if ( CommonUtil.isNotEmpty( str[i] ) && !str[i].contains( "null" ) && !list.contains( CommonUtil.toInteger( str[i] ) ) ) {
			list.add( CommonUtil.toInteger( str[i] ) );
		    }
		}

		if ( !list.contains( memberId ) ) {
		    list.add( memberId );
		}
		mb.setIds( list );
	    }
	    br.setCode( ReturnCode.SUCCESS );
	    br.setData( JSONObject.toJSONString( mb ) );
	} catch ( Exception e ) {
	    LOG.error( "MemberServiceWS类中findMemberIds方法异常了", e );
	    LOG.error( "转换MemberBo对象异常了", e );
	    br.setCode( ReturnCode.ERROR_1 );
	    br.setMsg( "转换MemberBo对象异常了" );
	}
	return br;
    }

    /**
     * 绑定手机号码
     *
     * @param baseParam
     *
     * @return
     * @throws Exception
     */
    @Transactional( rollbackFor = Exception.class )
    public BaseResult< MemberBo > bingdingPhone( BaseParam baseParam ) throws Exception {
	BaseResult br = new BaseResult();
	try {
	    BingdingPhoneBo bdpb = JSONObject.parseObject( JSONObject.toJSONString( baseParam.getReqdata() ), BingdingPhoneBo.class );

	    // 短信校验
	    Integer memberId = bdpb.getMemberId();
	    if ( CommonUtil.isEmpty( memberId ) ) {
		br.setCode( ReturnCode.ERROR_5 );
		br.setMsg( "数据不完整" );
		return br;
	    }
	    String code = bdpb.getCode();
	    if ( CommonUtil.isEmpty( code ) ) {
		br.setCode( ReturnCode.ERROR_5 );
		br.setMsg( "请输入校验码" );
		return br;
	    }
	    String phone = bdpb.getPhone();
	    if ( CommonUtil.isEmpty( phone ) ) {
		br.setCode( ReturnCode.ERROR_5 );
		br.setMsg( "数据不完整" );
		return br;
	    }
	    Integer busId = bdpb.getBusId();
	    if ( CommonUtil.isEmpty( busId ) ) {
		br.setCode( ReturnCode.ERROR_5 );
		br.setMsg( "数据不完整" );
		return br;
	    }
	    // 短信判断
	    if ( CommonUtil.isEmpty( JedisUtil.get( code ) ) ) {
		br.setCode( ReturnCode.ERROR_5 );
		br.setMsg( "短信校验码不正确" );
		return br;
	    }
	    // 查询要绑定的手机号码
	    Member oldMember = memberDAO.findByPhone( busId, phone );
	    Member member = null;
	    if ( CommonUtil.isEmpty( oldMember ) ) {
		// 新用户
		member = memberDAO.selectById( memberId );
		Member m = new Member();
		m.setId( member.getId() );
		m.setPhone( phone );
		memberDAO.updateById( m );
		member.setPhone( phone );
	    } else {
		Member m1 = memberDAO.selectById( memberId );

		member = new Member();
		member.setFlow( m1.getFlow() + oldMember.getFlow() );
		member.setIntegral( m1.getIntegral() + oldMember.getIntegral() );
		member.setFansCurrency( m1.getFansCurrency() + oldMember.getFansCurrency() );
		member.setId( oldMember.getId() );

		if ( CommonUtil.isNotEmpty( oldMember.getOldId() ) && !oldMember.getOldId().contains( oldMember.getId().toString() ) ) {
		    member.setOldId( oldMember.getOldId() + "," + oldMember.getId() + "," + m1.getId() );
		} else {
		    if ( CommonUtil.isNotEmpty( oldMember.getOldId() ) ) {
			member.setOldId( oldMember.getOldId() + "," + m1.getId() );
		    } else {
			member.setOldId( m1.getId() + "," );
		    }
		}
		if ( CommonUtil.isEmpty( oldMember.getOpenid() ) ) {
		    member.setOpenid( m1.getOpenid() );
		}

		if ( CommonUtil.isEmpty( oldMember.getPublicId() ) && CommonUtil.isNotEmpty( m1.getPublicId() ) ) {
		    member.setPublicId( m1.getPublicId() );
		}

		if ( CommonUtil.isEmpty( oldMember.getHeadimgurl() ) ) {
		    member.setHeadimgurl( m1.getHeadimgurl() );
		}

		memberDAO.deleteById( m1.getId() );
		memberDAO.updateById( member );

		MemberOld old = (MemberOld) net.sf.json.JSONObject.toBean( net.sf.json.JSONObject.fromObject( m1 ), MemberOld.class );
		memberOldDAO.insert( old );

		// 修改小程序之前openId对应的memberId
		memberAppletOpenidDAO.updateMemberId( member.getId(), m1.getId() );

		member.setPhone( phone );

	    }
	    MemberBo mb = new MemberBo();
	    MemberBo memberBo = new MemberBo();
	    memberBo.setId( member.getId() );
	    memberBo.setBusId( member.getBusId() );
	    memberBo.setCity( member.getCity() );
	    memberBo.setCountry( member.getCountry() );
	    memberBo.setFansCurrency( member.getFansCurrency() );
	    memberBo.setFlow( member.getFlow() );
	    memberBo.setMcId( member.getMcId() );
	    memberBo.setNickname( member.getNickname() );
	    memberBo.setIntegral( member.getIntegral() );
	    memberBo.setOpenid( member.getOpenid() );
	    memberBo.setPhone( member.getPhone() );
	    memberBo.setPublicId( member.getPublicId() );
	    memberBo.setPwd( member.getPwd() );
	    memberBo.setSex( member.getSex() );
	    JedisUtil.del( code );
	    br.setCode( ReturnCode.SUCCESS );
	    br.setMsg( "绑定成功" );
	    br.setData( JSONObject.toJSONString( mb ) );
	} catch ( Exception e ) {
	    e.printStackTrace();
	    LOG.error( "小程序绑定手机号码异常", e );
	    throw new Exception();
	}
	return br;
    }

    /**
     * 判断用户的卡类型 1积分卡 2折扣卡 3储值卡 4时效卡 5次卡
     *
     * @return
     */
    public BaseResult< MemberBo > isCardType( BaseParam baseParam ) {
	BaseResult br = new BaseResult();
	try {
	    br.setCode( ReturnCode.SUCCESS );
	    GetById byId = JSONObject.parseObject( JSONObject.toJSONString( baseParam.getReqdata() ), GetById.class );
	    MemberBo mb=new MemberBo();
	    if ( CommonUtil.isEmpty( byId.getId() ) ) {
		mb.setCardType( -1 );
		br.setData( JSONObject.toJSONString( mb ) );
		return br;
	    }
	    Member member = memberDAO.selectById( byId.getId() );
	    if ( CommonUtil.isEmpty( member ) || CommonUtil.isEmpty( member.getMcId() ) ) {
		mb.setCardType( -1 );
		br.setData( JSONObject.toJSONString( mb ) );
		return br;
	    }
	    MemberCard card = memberCardDAO.selectById( member.getMcId() );
	    if ( CommonUtil.isEmpty( card ) ) {
		mb.setCardType( -1 );
		br.setData( JSONObject.toJSONString( mb ) );
		return br;
	    }
	    mb.setCardType( card.getCtId());
	    br.setData(JSONObject.toJSONString( mb ));
	    return br;
	} catch ( Exception e ) {
	    br.setCode( ReturnCode.ERROR_1 );
	    br.setMsg( "MemberServiceWS类isCardType方法异常" );
	    return br;
	}
    }

    /**
     * 判断储值卡金额是否充足
     * @return
     */
    public BaseResult<MemberBo> isAdequateMoney(BaseParam baseParam) {
	BaseResult br = new BaseResult();
	try {
	    br.setCode( ReturnCode.SUCCESS );
	    MemberBo mb=new MemberBo();
	    AdequateMoneyBo amb = JSONObject.parseObject( JSONObject.toJSONString( baseParam.getReqdata() ), AdequateMoneyBo.class );
	    Member member = memberDAO.selectById( amb.getMemberId() );
	    if ( CommonUtil.isEmpty( member.getMcId() ) ) {
		mb.setAdequateMoney( false );
		br.setData( JSONObject.toJSONString( mb ) );
		return br;
	    }
	    MemberCard card = memberCardDAO.selectById( member.getMcId() );
	    if ( CommonUtil.isNotEmpty( card ) ) {
		if ( card.getCtId() == 3 ) {
		    if ( card.getMoney() >= amb.getTotalMoney()) {
			mb.setAdequateMoney( true );
			br.setData( JSONObject.toJSONString( mb ) );
			return br;
		    }
		}
	    }
	    mb.setAdequateMoney( false );
	    br.setData( JSONObject.toJSONString( mb ) );
	    return br;
	}catch ( Exception e ){
	    LOG.error( "isAdequateMoney接口异常了",e );
	    br.setCode( ReturnCode.ERROR_1 );
	    br.setMsg( "isAdequateMoney接口异常了" );
	    return br;
	}
    }


    /**
     * 储值卡退款订单
     *
     */
    public BaseResult chargeBack(BaseParam baseParam) {
	BaseResult br=new BaseResult();
	try {
	    ChargeBackBo cbc = JSONObject.parseObject( JSONObject.toJSONString( baseParam.getReqdata() ), ChargeBackBo.class );
	    Member member = memberDAO.selectById(cbc.getMemberId());
	    if (CommonUtil.isEmpty(member)
			    || CommonUtil.isEmpty(member.getMcId())) {
		br.setCode( ReturnCode.ERROR_7 );
		br.setMsg("未找到该会员数据");
		return br;
	    }
	    MemberCard card = memberCardDAO.selectById(member.getMcId());
	    MemberCard card1 = new MemberCard();
	    // 储值卡
	    Double refundMoney=cbc.getRefundMoney();
	    card1.setMcId(member.getMcId());
	    card1.setMoney(card.getMoney() + refundMoney);
	    memberCardDAO.updateById(card1);
	    br.setCode( ReturnCode.SUCCESS );
	    br.setMsg("退款成功");

	    memberNewService.saveCardRecordNew(card.getMcId(), 1, refundMoney + "",
			    "储值卡退款", card.getBusId(), null, 0, 0);
	    systemMsgService.sendChuzhiTuikuan(member, refundMoney);

	} catch (Exception e) {
	    e.printStackTrace();
	    LOG.error( "chargeBack储值卡退款订单异常",e );
	    br.setCode( ReturnCode.ERROR_1 );
	    br.setMsg("退款失败");
	}
	return br ;
    }



}
