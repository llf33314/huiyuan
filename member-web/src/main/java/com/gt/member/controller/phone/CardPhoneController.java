package com.gt.member.controller.phone;

import com.alibaba.fastjson.JSON;
import com.gt.api.bean.session.Member;
import com.gt.api.enums.ResponseEnums;
import com.gt.api.util.SessionUtils;
import com.gt.common.entity.BusFlow;
import com.gt.member.controller.RemoteAuthori.AuthorizeOrLoginController;
import com.gt.member.dao.common.BusFlowDAO;
import com.gt.member.dto.ServerResponse;
import com.gt.member.entity.MemberCardmodel;
import com.gt.member.entity.MemberCardtype;
import com.gt.member.entity.MemberGift;
import com.gt.member.entity.MemberOption;
import com.gt.member.enums.ResponseMemberEnums;
import com.gt.member.exception.BusinessException;
import com.gt.member.service.member.MemberCardPhoneService;
import com.gt.member.service.member.MemberCardService;
import com.gt.member.service.member.MemberNoticeService;
import com.gt.member.util.CommonUtil;
import com.gt.member.util.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    @ApiOperation( value = "查询领取会员卡页面数据", notes = "查询领取会员卡页面数据" )
    @ApiImplicitParams( { @ApiImplicitParam( name = "busId", value = "商家id", paramType = "query", required = false, dataType = "string" ),
		    @ApiImplicitParam( name = "requestUrl", value = "授权回调地址", paramType = "query", required = false, dataType = "int" ) } )
    @ResponseBody
    @RequestMapping( value = "/findLingquData", method = RequestMethod.GET )
    public ServerResponse findLingquData( HttpServletRequest request, HttpServletResponse response, @RequestParam String json ) {
	try {
	    Map< String,Object > params=JSON.toJavaObject( JSON.parseObject( json ),Map.class );
	    Integer busId = CommonUtil.toInteger( params.get( "busId" ) );
	    Member member = SessionUtils.getLoginMember( request, busId );
	    if ( CommonUtil.isEmpty( member ) ) {
		String url = authorizeMember( request, response, params );
		if ( CommonUtil.isNotEmpty( url ) ) {
		    return ServerResponse.createByError( ResponseMemberEnums.USERGRANT.getCode(), url );
		}
	    }
	    Map< String,Object > returnMap = memberCardPhoneService.findLingquData( request, busId );
	    returnMap.put( "member", member );
	    return ServerResponse.createBySuccess( returnMap );
	} catch ( Exception e ) {
	    LOG.error( "查询会员卡类型异常：", e );
	    e.printStackTrace();
	    return ServerResponse.createByError( ResponseEnums.ERROR.getCode(), "查询会员卡类型异常" );
	}
    }

    @ApiOperation( value = "领取会员卡", notes = "领取会员卡" )
    @ApiImplicitParams( { @ApiImplicitParam( name = "busId", value = "商家id", paramType = "query", required = false, dataType = "string" ),
		    @ApiImplicitParam( name = "requestUrl", value = "授权回调地址", paramType = "query", required = false, dataType = "int" ) } )
    @ResponseBody
    @RequestMapping( value = "/linquMemberCard", method = RequestMethod.GET )
    public ServerResponse linquMemberCard( HttpServletRequest request, HttpServletResponse response,@RequestParam String json ) {
	try {
	    Map< String,Object > params=JSON.toJavaObject( JSON.parseObject( json ),Map.class );
	    memberCardPhoneService.linquMemberCard( params );
	    return ServerResponse.createBySuccess();
	} catch ( BusinessException e ) {
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );
	}
    }


    @ApiOperation( value = "查询会员卡信息", notes = "查询会员卡信息" )
    @ResponseBody
    @RequestMapping( value = "/findMember", method = RequestMethod.GET )
    public ServerResponse findMember( HttpServletRequest request, HttpServletResponse response,@RequestParam String json) {
	try {
	    Map< String,Object > params=JSON.toJavaObject( JSON.parseObject( json ),Map.class );
	    Integer busId = CommonUtil.toInteger( params.get( "busId" ) );
	    Map< String,Object > map = memberCardPhoneService.findMember( request, busId );
	    return ServerResponse.createBySuccess( map );
	} catch ( Exception e ) {
	    return ServerResponse.createByError( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getMsg() );
	}
    }

    @ApiOperation( value = "查询会员权益", notes = "查询会员权益" )
    @ResponseBody
    @RequestMapping( value = "/findMemberEquities", method = RequestMethod.GET )
    public ServerResponse findMemberEquities( HttpServletRequest request, HttpServletResponse response, @RequestParam String json) {
	try {
	    Map< String,Object > params=JSON.toJavaObject( JSON.parseObject( json ),Map.class );
	    Integer busId = CommonUtil.toInteger( params.get( "busId" ) );
	    Member member = SessionUtils.getLoginMember( request, busId );
	    Map< String,Object > map = memberCardPhoneService.findMemberEquities( member );
	    return ServerResponse.createBySuccess( map );
	} catch ( Exception e ) {
	    return ServerResponse.createByError( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getMsg() );
	}

    }


    @ApiOperation( value = "查询会员交易记录", notes = "查询会员交易记录" )
    @ApiImplicitParams( { @ApiImplicitParam( name = "busId", value = "商家id", paramType = "query", required = false, dataType = "string" ),
		    @ApiImplicitParam( name = "page", value = "授权回调地址", paramType = "query", required = false, dataType = "int" ) } )

    @ResponseBody
    @RequestMapping( value = "/findCardrecordNew", method = RequestMethod.GET )
    public ServerResponse findCardrecordNew( HttpServletRequest request, HttpServletResponse response,@RequestParam String json ) {
	try {
	    Map< String,Object > params=JSON.toJavaObject( JSON.parseObject( json ),Map.class );
	    Integer busId = CommonUtil.toInteger( params.get( "busId" ) );
	    Member member = SessionUtils.getLoginMember( request, busId );
	    Map< String,Object > map = memberCardPhoneService.findCardrecordNew( params, member.getId(), 4 );
	    return ServerResponse.createBySuccess( map );
	} catch ( Exception e ) {
	    return ServerResponse.createByError( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getMsg() );
	}
    }

    @ApiOperation( value = "查询会员积分记录", notes = "查询会员积分记录" )
    @ResponseBody
    @RequestMapping( value = "/findCardrecordNewJifen", method = RequestMethod.GET )
    public ServerResponse findCardrecordNewJifen( HttpServletRequest request, HttpServletResponse response, @RequestParam String json ) {
	try {
	    Map< String,Object > params=JSON.toJavaObject( JSON.parseObject( json ),Map.class );
	    Integer busId = CommonUtil.toInteger( params.get( "busId" ) );
	    Member member = SessionUtils.getLoginMember( request, busId );
	    Map< String,Object > map = memberCardPhoneService.findCardrecordNew( params, member.getId(), 2 );
	    return ServerResponse.createBySuccess( map );
	} catch ( Exception e ) {
	    return ServerResponse.createByError( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getMsg() );
	}

    }


    @ApiOperation( value = "查询会员粉币记录", notes = "查询会员粉币记录" )
    @ResponseBody
    @RequestMapping( value = "/findCardrecordNewFenbi", method = RequestMethod.GET )
    public ServerResponse findCardrecordNewFenbi( HttpServletRequest request, HttpServletResponse response, @RequestParam String json ) {
	try {
	    Map< String,Object > params=JSON.toJavaObject( JSON.parseObject( json ),Map.class );
	    Integer busId = CommonUtil.toInteger( params.get( "busId" ) );
	    Member member = SessionUtils.getLoginMember( request, busId );
	    Map< String,Object > map = memberCardPhoneService.findCardrecordNew( params, member.getId(), 3 );
	    return ServerResponse.createBySuccess( map );
	} catch ( Exception e ) {
	    return ServerResponse.createByError( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getMsg() );
	}
    }


    @ApiOperation( value = "查询会员流量记录", notes = "查询会员流量记录" )
    @ResponseBody
    @RequestMapping( value = "/findCardrecordNewFlow", method = RequestMethod.GET )
    public ServerResponse findCardrecordNewFlow( HttpServletRequest request, HttpServletResponse response, @RequestParam String json ) {
	try {
	    Map< String,Object > params=JSON.toJavaObject( JSON.parseObject( json ),Map.class );
	    Integer busId = CommonUtil.toInteger( params.get( "busId" ) );
	    Member member = SessionUtils.getLoginMember( request, busId );
	    Map< String,Object > map = memberCardPhoneService.findCardrecordNew( params, member.getId(), 4 );
	    return ServerResponse.createBySuccess( map );
	} catch ( Exception e ) {
	    return ServerResponse.createByError( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getMsg() );
	}
    }


    @ApiOperation( value = "查询商家流量信息", notes = "查询商家流量信息" )
    @ResponseBody
    @RequestMapping( value = "/findBusUserFlow", method = RequestMethod.GET )
    public ServerResponse findBusUserFlow(HttpServletRequest request, HttpServletResponse response, @RequestParam String json){
	try {
	    Map< String,Object > params=JSON.toJavaObject( JSON.parseObject( json ),Map.class );
	    Integer busId = CommonUtil.toInteger( params.get( "busId" ) );
	    List<BusFlow> busFlows = memberCardPhoneService.findBusUserFlow(busId);
	    return ServerResponse.createBySuccess( busFlows );
	} catch ( Exception e ) {
	    return ServerResponse.createByError( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getMsg() );
	}
    }

    @ApiOperation( value = "流量兑换", notes = "流量兑换" )
    @ResponseBody
    @RequestMapping( value = "/changeFlow", method = RequestMethod.POST )
    public ServerResponse changeFlow(HttpServletRequest request, HttpServletResponse response, @RequestParam String json){
	try {
	    Map< String,Object > params=JSON.toJavaObject( JSON.parseObject( json ),Map.class );
	    Integer busId = CommonUtil.toInteger( params.get( "busId" ) );
	    Member member=SessionUtils.getLoginMember( request,busId );
	    memberCardPhoneService.changeFlow(params,member.getId());
	    return ServerResponse.createBySuccess(  );
	}catch ( BusinessException e){
	    return ServerResponse.createByError(e.getCode(),e.getMessage());
	}catch ( Exception e ) {
	    return ServerResponse.createByError( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getMsg() );
	}
    }


    @ApiOperation( value = "查询商家设置会员卡充值信息", notes = "查询商家设置会员卡充值信息" )
    @ResponseBody
    @RequestMapping( value = "/findRecharge", method = RequestMethod.POST )
    public ServerResponse findRecharge(HttpServletRequest request, HttpServletResponse response,@RequestParam String json){

	try {
	    Map< String,Object > params=JSON.toJavaObject( JSON.parseObject( json ),Map.class );
	    Integer busId = CommonUtil.toInteger( params.get( "busId" ) );
	    Member member=SessionUtils.getLoginMember( request,busId );
	    memberCardPhoneService.findRecharge(json,busId,member.getId());
	    return ServerResponse.createBySuccess(  );
	}catch ( BusinessException e){
	    return ServerResponse.createByError(e.getCode(),e.getMessage());
	}
    }

    @ApiOperation( value = "会员卡充值", notes = "会员卡充值" )
    @ResponseBody
    @RequestMapping( value = "/rechargeMemberCard", method = RequestMethod.POST )
    public ServerResponse rechargeMemberCard(HttpServletRequest request, HttpServletResponse response,@RequestParam String json){
	try {
	    Map< String,Object > params=JSON.toJavaObject( JSON.parseObject( json ),Map.class );
	    Integer busId = CommonUtil.toInteger( params.get( "busId" ) );
	    Member member=SessionUtils.getLoginMember( request,busId );
	    String url= memberCardPhoneService.rechargeMemberCard(json,busId,member.getId());
	    return ServerResponse.createBySuccess( url );
	}catch ( BusinessException e){
	    return ServerResponse.createByError(e.getCode(),e.getMessage());
	}
    }






}
