package com.gt.member.controller.phone;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gt.api.bean.session.Member;
import com.gt.api.enums.ResponseEnums;
import com.gt.api.util.RequestUtils;
import com.gt.api.util.SessionUtils;
import com.gt.common.entity.AlipayUser;
import com.gt.common.entity.BusFlow;
import com.gt.common.entity.WxPublicUsersEntity;
import com.gt.member.controller.RemoteAuthori.AuthorizeOrLoginController;
import com.gt.member.dao.common.BusFlowDAO;
import com.gt.member.dao.common.WxShopDAO;
import com.gt.member.dto.ServerResponse;
import com.gt.member.entity.MemberCardmodel;
import com.gt.member.entity.MemberCardtype;
import com.gt.member.entity.MemberGift;
import com.gt.member.entity.MemberOption;
import com.gt.member.enums.ResponseMemberEnums;
import com.gt.member.exception.BusinessException;
import com.gt.member.service.common.membercard.MemberCommonService;
import com.gt.member.service.common.membercard.RequestService;
import com.gt.member.service.member.MemberCardPhoneService;
import com.gt.member.service.member.MemberCardService;
import com.gt.member.service.member.MemberNoticeService;
import com.gt.member.util.*;
import com.gt.util.entity.param.sms.NewApiSms;
import com.gt.util.entity.result.wx.WxJsSdkResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.jbarcode.JBarcode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 手机端会员卡
 * </p>
 *
 * @author pengjiangli
 * @since 2017-08-09
 */
@Api( value = "手机端会员卡", description = "手机端会员卡" )
@Controller
@CrossOrigin
@RequestMapping( "/memberPhone/cardPhone" )
public class CardPhoneController extends AuthorizeOrLoginController {

    private static final Logger LOG = LoggerFactory.getLogger( CardPhoneController.class );

    @Autowired
    private MemberCardPhoneService memberCardPhoneService;

    @Autowired
    private RedisCacheUtil redisCacheUtil;

    @Autowired
    private RequestService requestService;

    @Autowired
    private MemberCommonService memberCommonService;

    @ApiOperation( value = "查询手机端login图片地址", notes = "查询手机端login图片地址" )
    @ApiImplicitParam( name = "busId", value = "商家id", paramType = "query", required = false, dataType = "int" )
    @ResponseBody
    @RequestMapping( value = "/findLoginImg", method = RequestMethod.POST )
    public ServerResponse findLoginImg( HttpServletRequest request, HttpServletResponse response, @RequestParam String json ) {
	try {
	    Map< String,Object > params = JSON.toJavaObject( JSON.parseObject( json ), Map.class );
	    Integer busId = CommonUtil.toInteger( params.get( "busId" ) );
	    String loginImg = requestService.loginImg( busId );
	    return ServerResponse.createBySuccess( loginImg );
	} catch ( BusinessException e ) {
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    LOG.error( "查询会员卡类型异常：", e );
	    e.printStackTrace();
	    return ServerResponse.createByError();
	}
    }

    @ApiOperation( value = "查询领取会员卡页面数据", notes = "查询领取会员卡页面数据" )
    @ApiImplicitParams( { @ApiImplicitParam( name = "busId", value = "商家id", paramType = "query", required = false, dataType = "string" ),
		    @ApiImplicitParam( name = "requestUrl", value = "授权回调地址", paramType = "query", required = false, dataType = "int" ) } )
    @ResponseBody
    @RequestMapping( value = "/findLingquData", method = RequestMethod.GET )
    public ServerResponse findLingquData( HttpServletRequest request, HttpServletResponse response, @RequestParam String json ) {
	try {
	    Map< String,Object > params = JSON.toJavaObject( JSON.parseObject( json ), Map.class );
	    Integer busId = CommonUtil.toInteger( params.get( "busId" ) );
	    Member member = SessionUtils.getLoginMember( request, busId );
	    if ( CommonUtil.isEmpty( member ) ) {
		String url = authorizeMember( request, response, params );
		if ( CommonUtil.isNotEmpty( url ) ) {
		    return ServerResponse.createByError( ResponseMemberEnums.USERGRANT.getCode(), ResponseMemberEnums.USERGRANT.getMsg(), url );
		}
	    }
	    Map< String,Object > returnMap = memberCardPhoneService.findLingquData( request, busId );
	    returnMap.put( "member", member );
	    return ServerResponse.createBySuccess( returnMap );
	} catch ( BusinessException e ) {
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    LOG.error( "查询会员卡类型异常：", e );
	    e.printStackTrace();
	    return ServerResponse.createByError( ResponseEnums.ERROR.getCode(), "查询会员卡类型异常" );
	}
    }

    @ApiOperation( value = "选择省市区", notes = "选择省市区" )
    @ApiImplicitParams( { @ApiImplicitParam( name = "busId", value = "商家id", paramType = "query", required = false, dataType = "string" ),
		    @ApiImplicitParam( name = "requestUrl", value = "授权回调地址", paramType = "query", required = false, dataType = "int" ),
		    @ApiImplicitParam( name = "cityCode", value = "请求市区code", paramType = "query", required = false, dataType = "int" ) } )
    @ResponseBody
    @RequestMapping( value = "/findCityByCityCode", method = RequestMethod.POST )
    public ServerResponse findCityByCityCode( HttpServletRequest request, HttpServletResponse response, @RequestParam String json ) {
	try {
	    Map< String,Object > params = JSON.toJavaObject( JSON.parseObject( json ), Map.class );
	    String cityCode = CommonUtil.toString( params.get( "cityCode" ) );
	    List< Map< String,Object > > listMap = memberCommonService.findCityByCityCode( cityCode );
	    return ServerResponse.createBySuccess( listMap );
	} catch ( BusinessException e ) {
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    LOG.error( "查询会员卡类型异常：", e );
	    e.printStackTrace();
	    return ServerResponse.createByError();
	}
    }

    @ApiOperation( value = "发送短信", notes = "发送短信" )
    @ApiImplicitParams( { @ApiImplicitParam( name = "busId", value = "商家id", paramType = "query", required = false, dataType = "string" ),
		    @ApiImplicitParam( name = "phone", value = "手机号码", paramType = "query", required = false, dataType = "int" ) } )
    @ResponseBody
    @RequestMapping( value = "/sendMsg", method = RequestMethod.POST )
    public ServerResponse sendMsg( HttpServletResponse response, HttpServletRequest request, @RequestParam String json ) {
	try {
	    Map< String,Object > map = JSON.parseObject( json, Map.class );
	    String phone = CommonUtil.toString( map.get( "phone" ) );
	    Integer busId = CommonUtil.toInteger( map.get( "busId" ) );
	    RequestUtils< NewApiSms > requestUtils = new RequestUtils< NewApiSms >();
	    String no = CommonUtil.getPhoneCode();
	    redisCacheUtil.set( phone + "_" + no, no, 5 * 60 );
	    LOG.debug( "进入短信发送,手机号:" + no );

	    NewApiSms newApiSms = new NewApiSms();
	    newApiSms.setMobile( phone );
	    newApiSms.setParamsStr( "会员短信校验码:" + no );
	    newApiSms.setBusId( busId );
	    newApiSms.setModel( 9 );
	    newApiSms.setTmplId( Long.parseLong( PropertiesUtil.getSms_tmplId() ) );
	    String smsStr = requestService.sendSmsNew( requestUtils );
	    JSONObject returnJson = JSONObject.parseObject( smsStr );
	    if ( "0".equals( CommonUtil.toString( returnJson.get( "code" ) ) ) ) {
		return ServerResponse.createBySuccess();
	    } else {
		return ServerResponse.createByError( "发送失败:" + returnJson.get( "msg" ) );
	    }
	} catch ( BusinessException e ) {
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    e.printStackTrace();
	    LOG.error( "短信发送失败", e );
	    return ServerResponse.createByError();
	}
    }

    @ApiOperation( value = "查询购买等级信息", notes = "查询购买等级信息" )
    @ApiImplicitParams( { @ApiImplicitParam( name = "busId", value = "商家id", paramType = "query", required = false, dataType = "string" ),
		    @ApiImplicitParam( name = "requestUrl", value = "授权回调地址", paramType = "query", required = false, dataType = "int" ) } )
    @ResponseBody
    @RequestMapping( value = "/findBuyGradeTypes", method = RequestMethod.GET )
    public ServerResponse findBuyGradeTypes( HttpServletRequest request, HttpServletResponse response, @RequestParam String json ) {
	try {
	    List< Map< String,Object > > listMap = memberCardPhoneService.findBuyGradeTypes( json );
	    return ServerResponse.createBySuccess( listMap );
	} catch ( BusinessException e ) {
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );
	}
    }

    @ApiOperation( value = "领取会员卡", notes = "领取会员卡" )
    @ApiImplicitParams( { @ApiImplicitParam( name = "busId", value = "商家id", paramType = "query", required = false, dataType = "string" ),
		    @ApiImplicitParam( name = "requestUrl", value = "授权回调地址", paramType = "query", required = false, dataType = "int" ) } )
    @ResponseBody
    @RequestMapping( value = "/linquMemberCard", method = RequestMethod.GET )
    public ServerResponse linquMemberCard( HttpServletRequest request, HttpServletResponse response, @RequestParam String json ) {
	try {
	    Map< String,Object > params = JSON.toJavaObject( JSON.parseObject( json ), Map.class );
	    Integer busId = CommonUtil.toInteger( params.get( "busId" ) );
	    Member member = SessionUtils.getLoginMember( request, busId );
	    memberCardPhoneService.linquMemberCard( params, member.getId() );
	    return ServerResponse.createBySuccess();
	} catch ( BusinessException e ) {
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );
	}
    }

    @ApiOperation( value = "查询会员卡信息", notes = "查询会员卡信息" )
    @ResponseBody
    @RequestMapping( value = "/findMember", method = RequestMethod.GET )
    public ServerResponse findMember( HttpServletRequest request, HttpServletResponse response, @RequestParam String json ) {
	try {
	    Map< String,Object > params = JSON.toJavaObject( JSON.parseObject( json ), Map.class );
	    Integer busId = CommonUtil.toInteger( params.get( "busId" ) );
	    Member member = SessionUtils.getLoginMember( request, busId );
	    if ( CommonUtil.isEmpty( member ) ) {
		String url = authorizeMember( request, response, params );
		if ( CommonUtil.isNotEmpty( url ) ) {
		    return ServerResponse.createByError( ResponseMemberEnums.USERGRANT.getCode(), ResponseMemberEnums.USERGRANT.getMsg(), url );
		}
	    }
	    Map< String,Object > map = memberCardPhoneService.findMember( request, busId );
	    return ServerResponse.createBySuccess( map );
	} catch ( BusinessException e ) {
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    return ServerResponse.createByError( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getMsg() );
	}
    }

    @ApiOperation( value = "查询会员权益", notes = "查询会员权益" )
    @ResponseBody
    @RequestMapping( value = "/findMemberEquities", method = RequestMethod.GET )
    public ServerResponse findMemberEquities( HttpServletRequest request, HttpServletResponse response, @RequestParam String json ) {
	try {
	    Map< String,Object > params = JSON.toJavaObject( JSON.parseObject( json ), Map.class );
	    Integer busId = CommonUtil.toInteger( params.get( "busId" ) );
	    Member member = SessionUtils.getLoginMember( request, busId );
	    if ( CommonUtil.isEmpty( member ) ) {
		String url = authorizeMember( request, response, params );
		if ( CommonUtil.isNotEmpty( url ) ) {
		    return ServerResponse.createByError( ResponseMemberEnums.USERGRANT.getCode(), ResponseMemberEnums.USERGRANT.getMsg(), url );
		}
	    }
	    Map< String,Object > map = memberCardPhoneService.findMemberEquities( member );
	    return ServerResponse.createBySuccess( map );
	} catch ( BusinessException e ) {
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    return ServerResponse.createByError( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getMsg() );
	}

    }

    @ApiOperation( value = "查询会员交易记录", notes = "查询会员交易记录" )
    @ApiImplicitParams( { @ApiImplicitParam( name = "busId", value = "商家id", paramType = "query", required = false, dataType = "string" ),
		    @ApiImplicitParam( name = "page", value = "授权回调地址", paramType = "query", required = false, dataType = "int" ) } )

    @ResponseBody
    @RequestMapping( value = "/findCardrecordNew", method = RequestMethod.GET )
    public ServerResponse findCardrecordNew( HttpServletRequest request, HttpServletResponse response, @RequestParam String json ) {
	try {
	    Map< String,Object > params = JSON.toJavaObject( JSON.parseObject( json ), Map.class );
	    Integer busId = CommonUtil.toInteger( params.get( "busId" ) );
	    Member member = SessionUtils.getLoginMember( request, busId );
	    if ( CommonUtil.isEmpty( member ) ) {
		String url = authorizeMember( request, response, params );
		if ( CommonUtil.isNotEmpty( url ) ) {
		    return ServerResponse.createByError( ResponseMemberEnums.USERGRANT.getCode(), ResponseMemberEnums.USERGRANT.getMsg(), url );
		}
	    }
	    Map< String,Object > map = memberCardPhoneService.findCardrecordNew( params, member.getId(), 4 );
	    return ServerResponse.createBySuccess( map );
	} catch ( BusinessException e ) {
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    return ServerResponse.createByError( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getMsg() );
	}
    }

    @ApiOperation( value = "查询会员积分记录", notes = "查询会员积分记录" )
    @ResponseBody
    @RequestMapping( value = "/findCardrecordNewJifen", method = RequestMethod.GET )
    public ServerResponse findCardrecordNewJifen( HttpServletRequest request, HttpServletResponse response, @RequestParam String json ) {
	try {
	    Map< String,Object > params = JSON.toJavaObject( JSON.parseObject( json ), Map.class );
	    Integer busId = CommonUtil.toInteger( params.get( "busId" ) );
	    Member member = SessionUtils.getLoginMember( request, busId );
	    if ( CommonUtil.isEmpty( member ) ) {
		String url = authorizeMember( request, response, params );
		if ( CommonUtil.isNotEmpty( url ) ) {
		    return ServerResponse.createByError( ResponseMemberEnums.USERGRANT.getCode(), ResponseMemberEnums.USERGRANT.getMsg(), url );
		}
	    }
	    Map< String,Object > map = memberCardPhoneService.findCardrecordNew( params, member.getId(), 2 );
	    return ServerResponse.createBySuccess( map );
	} catch ( BusinessException e ) {
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    return ServerResponse.createByError( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getMsg() );
	}

    }

    @ApiOperation( value = "查询会员粉币记录", notes = "查询会员粉币记录" )
    @ResponseBody
    @RequestMapping( value = "/findCardrecordNewFenbi", method = RequestMethod.GET )
    public ServerResponse findCardrecordNewFenbi( HttpServletRequest request, HttpServletResponse response, @RequestParam String json ) {
	try {
	    Map< String,Object > params = JSON.toJavaObject( JSON.parseObject( json ), Map.class );
	    Integer busId = CommonUtil.toInteger( params.get( "busId" ) );
	    Member member = SessionUtils.getLoginMember( request, busId );
	    if ( CommonUtil.isEmpty( member ) ) {
		String url = authorizeMember( request, response, params );
		if ( CommonUtil.isNotEmpty( url ) ) {
		    return ServerResponse.createByError( ResponseMemberEnums.USERGRANT.getCode(), ResponseMemberEnums.USERGRANT.getMsg(), url );
		}
	    }
	    Map< String,Object > map = memberCardPhoneService.findCardrecordNew( params, member.getId(), 3 );
	    return ServerResponse.createBySuccess( map );
	} catch ( BusinessException e ) {
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    return ServerResponse.createByError( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getMsg() );
	}
    }

    @ApiOperation( value = "查询会员流量记录", notes = "查询会员流量记录" )
    @ResponseBody
    @RequestMapping( value = "/findCardrecordNewFlow", method = RequestMethod.GET )
    public ServerResponse findCardrecordNewFlow( HttpServletRequest request, HttpServletResponse response, @RequestParam String json ) {
	try {
	    Map< String,Object > params = JSON.toJavaObject( JSON.parseObject( json ), Map.class );
	    Integer busId = CommonUtil.toInteger( params.get( "busId" ) );
	    Member member = SessionUtils.getLoginMember( request, busId );
	    if ( CommonUtil.isEmpty( member ) ) {
		String url = authorizeMember( request, response, params );
		if ( CommonUtil.isNotEmpty( url ) ) {
		    return ServerResponse.createByError( ResponseMemberEnums.USERGRANT.getCode(), ResponseMemberEnums.USERGRANT.getMsg(), url );
		}
	    }
	    Map< String,Object > map = memberCardPhoneService.findCardrecordNew( params, member.getId(), 4 );
	    return ServerResponse.createBySuccess( map );
	} catch ( BusinessException e ) {
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    return ServerResponse.createByError( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getMsg() );
	}
    }

    @ApiOperation( value = "查询商家流量信息", notes = "查询商家流量信息" )
    @ResponseBody
    @RequestMapping( value = "/findBusUserFlow", method = RequestMethod.GET )
    public ServerResponse findBusUserFlow( HttpServletRequest request, HttpServletResponse response, @RequestParam String json ) {
	try {
	    Map< String,Object > params = JSON.toJavaObject( JSON.parseObject( json ), Map.class );
	    Integer busId = CommonUtil.toInteger( params.get( "busId" ) );
	    Member member = SessionUtils.getLoginMember( request, busId );
	    if ( CommonUtil.isEmpty( member ) ) {
		String url = authorizeMember( request, response, params );
		if ( CommonUtil.isNotEmpty( url ) ) {
		    return ServerResponse.createByError( ResponseMemberEnums.USERGRANT.getCode(), ResponseMemberEnums.USERGRANT.getMsg(), url );
		}
	    }
	    Map< String,Object > map = memberCardPhoneService.findBusUserFlow( member.getId(), busId );
	    return ServerResponse.createBySuccess( map );
	} catch ( BusinessException e ) {
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    return ServerResponse.createByError( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getMsg() );
	}
    }

    @ApiOperation( value = "流量兑换", notes = "流量兑换" )
    @ResponseBody
    @RequestMapping( value = "/changeFlow", method = RequestMethod.POST )
    public ServerResponse changeFlow( HttpServletRequest request, HttpServletResponse response, @RequestParam String json ) {
	try {
	    Map< String,Object > params = JSON.toJavaObject( JSON.parseObject( json ), Map.class );
	    Integer busId = CommonUtil.toInteger( params.get( "busId" ) );
	    Member member = SessionUtils.getLoginMember( request, busId );
	    memberCardPhoneService.changeFlow( params, member.getId() );
	    return ServerResponse.createBySuccess();
	} catch ( BusinessException e ) {
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    return ServerResponse.createByError( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getMsg() );
	}
    }

    @ApiOperation( value = "查询商家设置会员卡充值信息", notes = "查询商家设置会员卡充值信息" )
    @ResponseBody
    @RequestMapping( value = "/findRecharge", method = RequestMethod.POST )
    public ServerResponse findRecharge( HttpServletRequest request, HttpServletResponse response, @RequestParam String json ) {

	try {
	    Map< String,Object > params = JSON.toJavaObject( JSON.parseObject( json ), Map.class );
	    Integer busId = CommonUtil.toInteger( params.get( "busId" ) );
	    Member member = SessionUtils.getLoginMember( request, busId );
	    if ( CommonUtil.isEmpty( member ) ) {
		String url = authorizeMember( request, response, params );
		if ( CommonUtil.isNotEmpty( url ) ) {
		    return ServerResponse.createByError( ResponseMemberEnums.USERGRANT.getCode(), ResponseMemberEnums.USERGRANT.getMsg(), url );
		}
	    }
	    Map< String,Object > map = memberCardPhoneService.findRecharge( request, json, busId, member.getId() );
	    return ServerResponse.createBySuccess( map );
	} catch ( BusinessException e ) {
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    return ServerResponse.createByError( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getMsg() );
	}
    }

    @ApiOperation( value = "会员卡充值", notes = "会员卡充值" )
    @ResponseBody
    @RequestMapping( value = "/rechargeMemberCard", method = RequestMethod.POST )
    public ServerResponse rechargeMemberCard( HttpServletRequest request, HttpServletResponse response, @RequestParam String json ) {
	try {
	    Map< String,Object > params = JSON.toJavaObject( JSON.parseObject( json ), Map.class );
	    Integer busId = CommonUtil.toInteger( params.get( "busId" ) );
	    Member member = SessionUtils.getLoginMember( request, busId );
	    if ( CommonUtil.isEmpty( member ) ) {
		String url = authorizeMember( request, response, params );
		if ( CommonUtil.isNotEmpty( url ) ) {
		    return ServerResponse.createByError( ResponseMemberEnums.USERGRANT.getCode(), ResponseMemberEnums.USERGRANT.getMsg(), url );
		}
	    }
	    String url = memberCardPhoneService.rechargeMemberCard(request, json, busId, member.getId() );
	    return ServerResponse.createBySuccess( "", url );
	} catch ( BusinessException e ) {
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    return ServerResponse.createByError( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getMsg() );
	}
    }

    @ApiOperation( value = "查询会员资料", notes = "查询会员资料" )
    @ResponseBody
    @RequestMapping( value = "/findMemberUser", method = RequestMethod.POST )
    public ServerResponse findMemberUser( HttpServletRequest request, HttpServletResponse response, @RequestParam String json ) {
	try {
	    Map< String,Object > params = JSON.toJavaObject( JSON.parseObject( json ), Map.class );
	    Integer busId = CommonUtil.toInteger( params.get( "busId" ) );
	    Member member = SessionUtils.getLoginMember( request, busId );
	    if ( CommonUtil.isEmpty( member ) ) {
		String url = authorizeMember( request, response, params );
		if ( CommonUtil.isNotEmpty( url ) ) {
		    return ServerResponse.createByError( ResponseMemberEnums.USERGRANT.getCode(), ResponseMemberEnums.USERGRANT.getMsg(), url );
		}
	    }
	    Map< String,Object > map = memberCardPhoneService.findMemberUser( params, member.getId() );
	    return ServerResponse.createBySuccess( map );
	} catch ( Exception e ) {
	    return ServerResponse.createByError();
	}
    }

    @ApiOperation( value = "身份证上传", notes = "身份证上传" )
    @ResponseBody
    @RequestMapping( value = "/updateImage", method = RequestMethod.POST )
    public ServerResponse updateImage( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > json ) {
	try {
	    Integer busId = CommonUtil.toInteger( json.get( "busId" ) );
	    Member member = SessionUtils.getLoginMember( request, busId );
	    if ( CommonUtil.isEmpty( member ) ) {
		String url = authorizeMember( request, response, json );
		if ( CommonUtil.isNotEmpty( url ) ) {
		    return ServerResponse.createByError( ResponseMemberEnums.USERGRANT.getCode(), ResponseMemberEnums.USERGRANT.getMsg(), url );
		}
	    }
	    List< Map< String,Object > > list = memberCardPhoneService.updateImage( request, member.getId(), busId );
	    return ServerResponse.createBySuccess( list );
	} catch ( Exception e ) {
	    return ServerResponse.createByError();
	}
    }

    @ApiOperation( value = "修改会员资料", notes = "查询会员资料" )
    @ResponseBody
    @RequestMapping( value = "/updateMemberUser", method = RequestMethod.POST )
    public ServerResponse updateMemberUser( HttpServletRequest request, HttpServletResponse response, @RequestParam String json ) {
	try {
	    Map< String,Object > params = JSON.toJavaObject( JSON.parseObject( json ), Map.class );
	    Integer busId = CommonUtil.toInteger( params.get( "busId" ) );
	    Member member = SessionUtils.getLoginMember( request, busId );
	    if ( CommonUtil.isEmpty( member ) ) {
		String url = authorizeMember( request, response, params );
		if ( CommonUtil.isNotEmpty( url ) ) {
		    return ServerResponse.createByError( ResponseMemberEnums.USERGRANT.getCode(), ResponseMemberEnums.USERGRANT.getMsg(), url );
		}
	    }
	    memberCardPhoneService.updateMemberUser( json, member.getId() );
	    return ServerResponse.createBySuccess();
	} catch ( BusinessException e ) {
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    return ServerResponse.createByError();
	}
    }

    @ApiOperation( value = "查询会员卡卡号", notes = "查询会员卡卡号" )
    @ResponseBody
    @RequestMapping( value = "/findMemberCardNO", method = RequestMethod.POST )
    public ServerResponse findMemberCardNO( HttpServletRequest request, HttpServletResponse response, @RequestParam String json ) {
	try {
	    Map< String,Object > params = JSON.toJavaObject( JSON.parseObject( json ), Map.class );
	    Integer busId = CommonUtil.toInteger( params.get( "busId" ) );
	    Member member = SessionUtils.getLoginMember( request, busId );
	    if ( CommonUtil.isEmpty( member ) ) {
		String url = authorizeMember( request, response, params );
		if ( CommonUtil.isNotEmpty( url ) ) {
		    return ServerResponse.createByError( ResponseMemberEnums.USERGRANT.getCode(), ResponseMemberEnums.USERGRANT.getMsg(), url );
		}
	    }
	    Map< String,Object > map = memberCardPhoneService.findMemberCardNo( member.getId() );
	    return ServerResponse.createBySuccess( map );
	} catch ( BusinessException e ) {
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    return ServerResponse.createByError();
	}
    }

    @ApiOperation( value = "向商家支付扫码二维码", notes = "向商家支付扫码" )
    @ResponseBody
    @RequestMapping( value = "/findPayQrcodeCardNo", method = RequestMethod.GET )
    public void findPayQrcodeCardNo( HttpServletRequest request, HttpServletResponse response, @RequestParam Integer busId ) throws Exception {
	Member member = SessionUtils.getLoginMember( request, busId );
	String cardNO = memberCardPhoneService.findCardNoByMemberId( member.getId() );
	QRcodeKit.buildQRcode( cardNO, 500, 500, response );
    }

    @ApiOperation( value = "向商家支付扫码条形码", notes = "向商家支付扫码条形码" )
    @ResponseBody
    @RequestMapping( value = "/findPayJBarcodeCardNo", method = RequestMethod.GET )
    public void findPayJBarcodeCardNo( HttpServletRequest request, HttpServletResponse response, @RequestParam Integer busId ) throws Exception {
	Member member = SessionUtils.getLoginMember( request, busId );
	String cardNO = memberCardPhoneService.findCardNoByMemberId( member.getId() );
	JBarcodeUtil.getJbarCode( cardNO, response );
    }

    @ApiOperation( value = "用户签到", notes = "用户签到" )
    @ResponseBody
    @RequestMapping( value = "/qiandao", method = RequestMethod.POST )
    public ServerResponse qiandao( HttpServletRequest request, HttpServletResponse response, @RequestParam String json ) {
	try {
	    Map< String,Object > params = JSON.toJavaObject( JSON.parseObject( json ), Map.class );
	    Integer busId = CommonUtil.toInteger( params.get( "busId" ) );
	    Member member = SessionUtils.getLoginMember( request, busId );
	    if ( CommonUtil.isEmpty( member ) ) {
		String url = authorizeMember( request, response, params );
		if ( CommonUtil.isNotEmpty( url ) ) {
		    return ServerResponse.createByError( ResponseMemberEnums.USERGRANT.getCode(), ResponseMemberEnums.USERGRANT.getMsg(), url );
		}
	    }
	    memberCardPhoneService.qiandao( member.getId(), busId );
	    return ServerResponse.createBySuccess();
	} catch ( BusinessException e ) {
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    return ServerResponse.createByError();
	}
    }

    @ApiOperation( value = "查询会员和系统消息", notes = "查询会员消息" )
    @ResponseBody
    @RequestMapping( value = "/findMemberNotice", method = RequestMethod.POST )
    public ServerResponse findMemberNotice( HttpServletRequest request, HttpServletResponse response, @RequestParam String json ) {
	try {
	    Map< String,Object > params = JSON.toJavaObject( JSON.parseObject( json ), Map.class );
	    Integer busId = CommonUtil.toInteger( params.get( "busId" ) );
	    Member member = SessionUtils.getLoginMember( request, busId );
	    if ( CommonUtil.isEmpty( member ) ) {
		String url = authorizeMember( request, response, params );
		if ( CommonUtil.isNotEmpty( url ) ) {
		    return ServerResponse.createByError( ResponseMemberEnums.USERGRANT.getCode(), ResponseMemberEnums.USERGRANT.getMsg(), url );
		}
	    }
	    Map< String,Object > map = memberCardPhoneService.findMemberNotice( member.getId() );
	    return ServerResponse.createBySuccess( map );
	} catch ( BusinessException e ) {
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    LOG.error( "查询会员信息异常", e );
	    return ServerResponse.createByError();
	}
    }

    @ApiOperation( value = "查询推荐信息", notes = "查询推荐信息" )
    @ResponseBody
    @RequestMapping( value = "/findRecommend", method = RequestMethod.POST )
    public ServerResponse findRecommend( HttpServletRequest request, HttpServletResponse response, @RequestParam String json ) {

	try {
	    Map< String,Object > params = JSON.toJavaObject( JSON.parseObject( json ), Map.class );
	    Integer busId = CommonUtil.toInteger( params.get( "busId" ) );
	    Member member = SessionUtils.getLoginMember( request, busId );
	    if ( CommonUtil.isEmpty( member ) ) {
		String url = authorizeMember( request, response, params );
		if ( CommonUtil.isNotEmpty( url ) ) {
		    return ServerResponse.createByError( ResponseMemberEnums.USERGRANT.getCode(), ResponseMemberEnums.USERGRANT.getMsg(), url );
		}
	    }
	    Map< String,Object > map = memberCardPhoneService.findRecommend( request, member.getId() );
	    return ServerResponse.createBySuccess( map );
	} catch ( BusinessException e ) {
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    LOG.error( "查询推荐信息异常", e );
	    return ServerResponse.createByError();
	}
    }

    @ApiOperation( value = "提取佣金", notes = "提取佣金" )
    @ResponseBody
    @RequestMapping( value = "/pickMoney", method = RequestMethod.POST )
    public ServerResponse pickMoney( HttpServletRequest request, HttpServletResponse response, @RequestParam String json ) {
	try {
	    Map< String,Object > params = JSON.toJavaObject( JSON.parseObject( json ), Map.class );
	    Integer busId = CommonUtil.toInteger( params.get( "busId" ) );
	    Member member = SessionUtils.getLoginMember( request, busId );
	    if ( CommonUtil.isEmpty( member ) ) {
		String url = authorizeMember( request, response, params );
		if ( CommonUtil.isNotEmpty( url ) ) {
		    return ServerResponse.createByError( ResponseMemberEnums.USERGRANT.getCode(), ResponseMemberEnums.USERGRANT.getMsg(), url );
		}
	    }
	    memberCardPhoneService.pickMoney( member.getId(), busId, CommonUtil.toDouble( params.get( "pickMoney" ) ) );
	    return ServerResponse.createBySuccess();
	} catch ( BusinessException e ) {
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    LOG.error( "查询推荐信息异常", e );
	    return ServerResponse.createByError();
	}
    }

    @ApiOperation( value = "查询门店信息", notes = "查询门店信息" )
    @ResponseBody
    @RequestMapping( value = "/findShop", method = RequestMethod.POST )
    public ServerResponse findShop( HttpServletRequest request, HttpServletResponse response, @RequestParam String json ) {
	try {
	    Map< String,Object > params = JSON.toJavaObject( JSON.parseObject( json ), Map.class );
	    Integer busId = CommonUtil.toInteger( params.get( "busId" ) );
	    Member member = SessionUtils.getLoginMember( request, busId );
	    if ( CommonUtil.isEmpty( member ) ) {
		String url = authorizeMember( request, response, params );
		if ( CommonUtil.isNotEmpty( url ) ) {
		    return ServerResponse.createByError( ResponseMemberEnums.USERGRANT.getCode(), ResponseMemberEnums.USERGRANT.getMsg(), url );
		}
	    }
	    Double longitude = CommonUtil.toDouble( params.get( "longitude" ) );
	    Double latitude = CommonUtil.toDouble( params.get( "latitude" ) );
	    List< Map > list = memberCardPhoneService.findWxShop( busId, longitude, latitude );
	    return ServerResponse.createBySuccess( list );
	} catch ( BusinessException e ) {
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    LOG.error( "查询门店信息异常", e );
	    return ServerResponse.createByError();
	}
    }

    @ApiOperation( value = "推荐信息", notes = "推荐信息" )
    @ResponseBody
    @RequestMapping( value = "/tuijianQRcode", method = RequestMethod.GET )
    public void tuijianQRcode( HttpServletRequest request, HttpServletResponse response, @RequestParam Integer busId ) {
	Member member = SessionUtils.getLoginMember( request, busId );
	String url = memberCardPhoneService.tuijianQRcode( member.getId() );
	QRcodeKit.buildQRcode( url, 500, 500, response );
    }


    @ApiOperation( value = "会员卡分享推荐", notes = "会员卡分享推荐" )
    @ResponseBody
    @RequestMapping( value = "/wxshareUrl", method = RequestMethod.POST )
    public ServerResponse wxshareUrl( HttpServletRequest request, HttpServletResponse response, @RequestParam String json ) {
	try {
	    Map< String,Object > params = JSON.toJavaObject( JSON.parseObject( json ), Map.class );
	    Integer busId = CommonUtil.toInteger( params.get( "busId" ) );
	    Member member = SessionUtils.getLoginMember( request, busId );
	    String url = memberCardPhoneService.tuijianQRcode( member.getId() );
	    return ServerResponse.createBySuccess( "",url );
	} catch ( BusinessException e ) {
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    LOG.error( "分享推荐异常", e );
	    return ServerResponse.createByError();
	}
    }


    @ApiOperation( value = "会员卡分享推荐", notes = "会员卡分享推荐" )
    @ResponseBody
    @RequestMapping( value = "/wxshareCard", method = RequestMethod.POST )
    public ServerResponse wxshareCard( HttpServletRequest request, HttpServletResponse response, @RequestParam String json ) {
	try {
	    Map< String,Object > params = JSON.toJavaObject( JSON.parseObject( json ), Map.class );
	    Integer busId = CommonUtil.toInteger( params.get( "busId" ) );
	    Member member = SessionUtils.getLoginMember( request, busId );
	    if ( CommonUtil.isEmpty( member ) ) {
		String url = authorizeMember( request, response, params );
		if ( CommonUtil.isNotEmpty( url ) ) {
		    return ServerResponse.createByError( ResponseMemberEnums.USERGRANT.getCode(), ResponseMemberEnums.USERGRANT.getMsg(), url );
		}
	    }
	    WxJsSdkResult wxJsSdkResult = memberCardPhoneService.wxshareCard( member.getId(), busId );
	    return ServerResponse.createBySuccess( wxJsSdkResult );
	} catch ( BusinessException e ) {
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    LOG.error( "分享推荐异常", e );
	    return ServerResponse.createByError();
	}
    }

    @ApiOperation( value = "储值卡转借他人", notes = "会员卡分享推荐" )
    @ResponseBody
    @RequestMapping( value = "/memberLent", method = RequestMethod.POST )
    public ServerResponse memberLent( HttpServletRequest request, HttpServletResponse response, @RequestParam String json ) {
	try {
	    Map< String,Object > params = JSON.toJavaObject( JSON.parseObject( json ), Map.class );
	    Integer busId = CommonUtil.toInteger( params.get( "busId" ) );
	    Member member = SessionUtils.getLoginMember( request, busId );
	    if ( CommonUtil.isEmpty( member ) ) {
		String url = authorizeMember( request, response, params );
		if ( CommonUtil.isNotEmpty( url ) ) {
		    return ServerResponse.createByError( ResponseMemberEnums.USERGRANT.getCode(), ResponseMemberEnums.USERGRANT.getMsg(), url );
		}
	    }
	    String key = memberCardPhoneService.memberLentMoney( member.getId(), CommonUtil.toDouble( params.get( "money" ) ) );
	    return ServerResponse.createBySuccess( "", key );
	} catch ( BusinessException e ) {
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    LOG.error( "储值卡转借他人", e );
	    return ServerResponse.createByError();
	}
    }

    @ApiOperation( value = "授权判断", notes = "授权判断" )
    @ResponseBody
    @RequestMapping( value = "/authorizeOrLogin", method = RequestMethod.POST )
    public ServerResponse authorizeOrLogin( HttpServletRequest request, HttpServletResponse response, @RequestParam String json ) {
	try {
	    Map< String,Object > params = JSON.toJavaObject( JSON.parseObject( json ), Map.class );
	    Integer busId = CommonUtil.toInteger( params.get( "busId" ) );
	    Member member = SessionUtils.getLoginMember( request, busId );
	    if ( CommonUtil.isEmpty( member ) ) {
		String url = authorizeMember( request, response, params );
		if ( CommonUtil.isNotEmpty( url ) ) {
		    return ServerResponse.createByError( ResponseMemberEnums.USERGRANT.getCode(), ResponseMemberEnums.USERGRANT.getMsg(), url );
		}
	    }
	    return ServerResponse.createBySuccess();
	} catch ( BusinessException e ) {
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    LOG.error( "授权判断异常", e );
	    return ServerResponse.createByError();
	}
    }

    @ApiOperation( value = "转借他人二维码", notes = "转借他人二维码" )
    @ResponseBody
    @RequestMapping( value = "/memberLentImage", method = RequestMethod.GET )
    public void memberLentImage( HttpServletRequest request, HttpServletResponse response, @RequestParam Integer busId, @RequestParam String memberLentKey ) {
	Member member = SessionUtils.getLoginMember( request, busId );
	String content = member.getId() + "_" + memberLentKey;
	QRcodeKit.buildQRcode( content, 500, 500, response );
    }

}
