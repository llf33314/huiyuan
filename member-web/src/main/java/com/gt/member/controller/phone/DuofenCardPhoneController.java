package com.gt.member.controller.phone;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gt.api.bean.session.Member;
import com.gt.api.enums.ResponseEnums;
import com.gt.api.util.RequestUtils;
import com.gt.api.util.SessionUtils;
import com.gt.member.controller.RemoteAuthori.AuthorizeOrLoginController;
import com.gt.member.dto.ServerResponse;
import com.gt.member.enums.ResponseMemberEnums;
import com.gt.member.exception.BusinessException;
import com.gt.member.service.common.membercard.RequestService;
import com.gt.member.service.member.DuofenCardNewPhoneService;
import com.gt.member.util.CommonUtil;
import com.gt.member.util.QRcodeKit;
import com.gt.member.util.RedisCacheUtil;
import com.gt.util.entity.param.sms.NewApiSms;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/1/16.
 */
@Api( value = "手机端优惠券", description = "手机端优惠券" )
@Controller
@CrossOrigin
@RequestMapping( "/memberPhone/duofenCardPhone" )
@Slf4j
public class DuofenCardPhoneController extends AuthorizeOrLoginController {

    @Autowired
    private DuofenCardNewPhoneService duofenCardNewPhoneService;

    @Autowired
    private RequestService requestService;

    @Autowired
    private RedisCacheUtil redisCacheUtil;

    @ApiOperation( value = "查询发布的优惠券信息（通用券）", notes = "查询发布的优惠券信息" )
    @ApiImplicitParams( { @ApiImplicitParam( name = "busId", value = "商家id", paramType = "query", required = false, dataType = "string" ),
		    @ApiImplicitParam( name = "requestUrl", value = "授权回调地址", paramType = "query", required = false, dataType = "int" ),
		    @ApiImplicitParam( name = "uclogin", value = "uc端可以不登陆", paramType = "query", required = false, dataType = "int" ) } )
    @ResponseBody
    @RequestMapping( value = "/findPublishDuofenCard", method = RequestMethod.POST )
    public ServerResponse findPublishDuofenCard( HttpServletRequest request, HttpServletResponse response, @RequestParam String json ) {
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
	    Integer memberId=null;
	    if(CommonUtil.isNotEmpty( member )){
		memberId=member.getId();
	    }
	    List< Map< String,Object > > list = duofenCardNewPhoneService.findPublishDuofenCard( busId,memberId,params);
	    return ServerResponse.createBySuccess( list );
	} catch ( Exception e ) {
	    log.error( "查询发布的优惠券信息（通用券）：", e );
	    e.printStackTrace();
	    return ServerResponse.createByError( ResponseEnums.ERROR.getCode(),  ResponseEnums.ERROR.getMsg() );

	}
    }


    @ApiOperation( value = "查询发布的优惠券详情（通用券）", notes = "查询发布的优惠券详情" )
    @ApiImplicitParams( { @ApiImplicitParam( name = "busId", value = "商家id", paramType = "query", required = false, dataType = "string" ),
		    @ApiImplicitParam( name = "requestUrl", value = "授权回调地址", paramType = "query", required = false, dataType = "int" ),
		    @ApiImplicitParam( name = "uclogin", value = "uc端可以不登陆", paramType = "query", required = false, dataType = "int" ) } )
    @ResponseBody
    @RequestMapping( value = "/findDuofenCardDetails", method = RequestMethod.POST )
    public ServerResponse findDuofenCardDetails(HttpServletRequest request, HttpServletResponse response, @RequestParam String json){
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
	    Integer cardId=CommonUtil.toInteger( params.get( "cardId" ) );
	    Map< String,Object >  map = duofenCardNewPhoneService.findDuofenCardNewByCardId( cardId);
	    return ServerResponse.createBySuccess( map );
	} catch ( Exception e ) {
	    log.error( "查询优惠券详情（通用券）：", e );
	    e.printStackTrace();
	    return ServerResponse.createByError( ResponseEnums.ERROR.getCode(),  ResponseEnums.ERROR.getMsg() );

	}
    }

    @ApiOperation( value = "免费领取优惠券", notes = "免费领取优惠券" )
    @ApiImplicitParams( { @ApiImplicitParam( name = "busId", value = "商家id", paramType = "query", required = false, dataType = "string" ),
		    @ApiImplicitParam( name = "requestUrl", value = "授权回调地址", paramType = "query", required = false, dataType = "int" )} )
    @ResponseBody
    @RequestMapping( value = "/getDuofenCard", method = RequestMethod.POST )
    public ServerResponse getDuofenCard(HttpServletRequest request, HttpServletResponse response, @RequestParam String json){
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
	    Integer cardId=CommonUtil.toInteger( params.get( "cardId" ) );
	    duofenCardNewPhoneService.getDuofenCard( cardId,member.getId());
	    return ServerResponse.createBySuccess(  );
	}catch ( BusinessException e ){
	    return ServerResponse.createByError(e.getCode(),e.getMessage());
	} catch ( Exception e ) {
	    log.error( "免费领取优惠券异常：", e );
	    e.printStackTrace();
	    return ServerResponse.createByError( ResponseEnums.ERROR.getCode(),  ResponseEnums.ERROR.getMsg());

	}
    }

    @ApiOperation( value = "购买优惠券信息查询", notes = "购买优惠券信息查询" )
    @ApiImplicitParams( { @ApiImplicitParam( name = "busId", value = "商家id", paramType = "query", required = false, dataType = "string" ),
		    @ApiImplicitParam( name = "requestUrl", value = "授权回调地址", paramType = "query", required = false, dataType = "int" )} )
    @ResponseBody
    @RequestMapping( value = "/findBuyDuofenCardDetails", method = RequestMethod.POST )
    public ServerResponse findBuyDuofenCardDetails(HttpServletRequest request, HttpServletResponse response, @RequestParam String json){
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
	    Integer cardId=CommonUtil.toInteger( params.get( "cardId" ) );
	    Map<String,Object> map=duofenCardNewPhoneService.findBuyDuofenCardDetails(request, cardId,member.getId());
	    return ServerResponse.createBySuccess( map );
	}catch ( BusinessException e ){
	    return ServerResponse.createByError(e.getCode(),e.getMessage());
	} catch ( Exception e ) {
	    log.error( "购买优惠券信息查询异常：", e );
	    e.printStackTrace();
	    return ServerResponse.createByError( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getMsg() );

	}
    }


    @ApiOperation( value = "购买优惠券", notes = "购买优惠券" )
    @ApiImplicitParams( { @ApiImplicitParam( name = "busId", value = "商家id", paramType = "query", required = false, dataType = "string" ),
		    @ApiImplicitParam( name = "requestUrl", value = "授权回调地址", paramType = "query", required = false, dataType = "int" )} )
    @ResponseBody
    @RequestMapping( value = "/buyDuofenCard", method = RequestMethod.POST )
    public ServerResponse buyDuofenCard(HttpServletRequest request, HttpServletResponse response, @RequestParam String json){
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
	    Integer cardId=CommonUtil.toInteger( params.get( "cardId" ) );
	    duofenCardNewPhoneService.buyDuofenCard(request, cardId,member.getId(),params);
	    return ServerResponse.createBySuccess(  );
	}catch ( BusinessException e ){
	    return ServerResponse.createByError(e.getCode(),e.getMessage());
	} catch ( Exception e ) {
	    log.error( "购买优惠券信息查询异常：", e );
	    e.printStackTrace();
	    return ServerResponse.createByError( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getMsg() );

	}
    }

    @ApiOperation( value = "我的优惠券", notes = "我的优惠券" )
    @ApiImplicitParams( { @ApiImplicitParam( name = "busId", value = "商家id", paramType = "query", required = false, dataType = "string" ),
		    @ApiImplicitParam( name = "requestUrl", value = "授权回调地址", paramType = "query", required = false, dataType = "int" )} )
    @ResponseBody
    @RequestMapping( value = "/myDuofenCard", method = RequestMethod.POST )
    public ServerResponse myDuofenCard(HttpServletRequest request, HttpServletResponse response,@RequestParam String json){
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
	    Map<String,Object> map=duofenCardNewPhoneService.myDuofenCard(busId,member.getId());
	    return ServerResponse.createBySuccess( map  );
	}catch ( BusinessException e ){
	    return ServerResponse.createByError(e.getCode(),e.getMessage());
	} catch ( Exception e ) {
	    log.error( "我的优惠券查询异常：", e );
	    e.printStackTrace();
	    return ServerResponse.createByError( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getMsg() );
	}
    }

    @ApiOperation( value = "已失效的优惠券", notes = "已失效的优惠券" )
    @ApiImplicitParams( { @ApiImplicitParam( name = "busId", value = "商家id", paramType = "query", required = false, dataType = "string" ),
		    @ApiImplicitParam( name = "requestUrl", value = "授权回调地址", paramType = "query", required = false, dataType = "int" )} )
    @ResponseBody
    @RequestMapping( value = "/invalidDuofenCard", method = RequestMethod.POST )
    public ServerResponse invalidDuofenCard(HttpServletRequest request, HttpServletResponse response,@RequestParam String json){
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
	    List<Map<String,Object>> invalidS=duofenCardNewPhoneService.invalidDuofenCard(member.getId());
	    return ServerResponse.createBySuccess( invalidS  );
	}catch ( BusinessException e ){
	    return ServerResponse.createByError(e.getCode(),e.getMessage());
	} catch ( Exception e ) {
	    log.error( "我的优惠券查询异常：", e );
	    e.printStackTrace();
	    return ServerResponse.createByError( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getMsg() );
	}
    }



    @ApiOperation( value = "单张优惠券使用", notes = "单张优惠券使用" )
    @ApiImplicitParams( { @ApiImplicitParam( name = "busId", value = "商家id", paramType = "query", required = false, dataType = "string" ),
		    @ApiImplicitParam( name = "requestUrl", value = "授权回调地址", paramType = "query", required = false, dataType = "int" )} )
    @ResponseBody
    @RequestMapping( value = "/useDuofenCardByCardId", method = RequestMethod.POST )
    public ServerResponse useDuofenCardByCardId(HttpServletRequest request, HttpServletResponse response,@RequestParam String json){
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
	    Integer receiveId=CommonUtil.toInteger( params.get( "receiveId" ) );
	    Map<String,Object> map=duofenCardNewPhoneService.useDuofenCardByCardId(receiveId,member.getId());
	    return ServerResponse.createBySuccess( map  );
	}catch ( BusinessException e ){
	    return ServerResponse.createByError(e.getCode(),e.getMessage());
	} catch ( Exception e ) {
	    log.error( "单张优惠券使用查询异常：", e );
	    e.printStackTrace();
	    return ServerResponse.createByError( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getMsg() );
	}
    }


    @ApiOperation( value = "购买优惠券详情", notes = "购买优惠券详情" )
    @ResponseBody
    @RequestMapping( value = "/findDuofenCardDetailsByCardId", method = RequestMethod.POST )
    public ServerResponse findBuyDuofenCardDetailsByDuofenCardGetId(HttpServletRequest request, HttpServletResponse response,@RequestParam String json){
	try {
	    Map< String,Object > params = JSON.toJavaObject( JSON.parseObject( json ), Map.class );
	    Integer cardId=CommonUtil.toInteger( params.get( "cardId" ) );
	    Map<String,Object> map=duofenCardNewPhoneService.findDuofenCardDetailsByCardId(cardId);
	    return ServerResponse.createBySuccess( map  );
	}catch ( BusinessException e ){
	    return ServerResponse.createByError(e.getCode(),e.getMessage());
	} catch ( Exception e ) {
	    log.error( "我的优惠券查询异常：", e );
	    e.printStackTrace();
	    return ServerResponse.createByError( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getMsg() );
	}
    }


    @ApiOperation( value = "单张优惠券详情", notes = "单张优惠券详情" )
    @ApiImplicitParams( { @ApiImplicitParam( name = "busId", value = "商家id", paramType = "query", required = false, dataType = "string" ),
		    @ApiImplicitParam( name = "requestUrl", value = "授权回调地址", paramType = "query", required = false, dataType = "int" )} )
    @ResponseBody
    @RequestMapping( value = "/findDuofenCardDetailsByreceiveId", method = RequestMethod.POST )
    public ServerResponse findDuofenCardDetailsByreceiveId(HttpServletRequest request, HttpServletResponse response,@RequestParam String json){
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
	    Integer receiveId=CommonUtil.toInteger( params.get( "receiveId" ) );
	    Map<String,Object> map=duofenCardNewPhoneService.findDuofenCardDetailsByreceiveId(receiveId);
	    return ServerResponse.createBySuccess( map  );
	}catch ( BusinessException e ){
	    return ServerResponse.createByError(e.getCode(),e.getMessage());
	} catch ( Exception e ) {
	    log.error( "我的优惠券查询异常：", e );
	    e.printStackTrace();
	    return ServerResponse.createByError( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getMsg() );
	}
    }


    @ApiOperation( value = "查询世界手机区号", notes = "查询世界手机区号" )
    @ResponseBody
    @RequestMapping( value = "/findAreaPhone", method = RequestMethod.POST )
    public ServerResponse findAreaPhone(HttpServletResponse response, HttpServletRequest request){
	try {
	    List< Map > listMap = requestService.findAreaPhone(  );
	    return ServerResponse.createBySuccess( listMap );
	} catch ( BusinessException e ) {
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    log.error( "查询世界手机区号异常：", e );
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
	    String country=CommonUtil.toString( map.get( "country" ) );
	    RequestUtils< NewApiSms > requestUtils = new RequestUtils< NewApiSms >();
	    String no = CommonUtil.getPhoneCode();
	    redisCacheUtil.set( phone + "_" + no, no, 5 * 60 );
	    log.debug( "进入短信发送,手机号:" + no );

	    NewApiSms newApiSms = new NewApiSms();
	    newApiSms.setMobile( phone );
	    newApiSms.setParamsStr(no );
	    newApiSms.setBusId( busId );
	    newApiSms.setModel( 9 );
	    newApiSms.setTmplId( 11510L );
	    newApiSms.setCountry( country );
	    requestUtils.setReqdata( newApiSms );
	    String smsStr = requestService.sendSmsNew( requestUtils );
	    JSONObject returnJson = JSONObject.parseObject( smsStr );
	    if ( "0".equals( CommonUtil.toString( returnJson.get( "code" ) ) ) ) {
		return ServerResponse.createBySuccess("",no);
	    } else {
		return ServerResponse.createByError( "发送失败:" + returnJson.get( "msg" ) );
	    }
	} catch ( BusinessException e ) {
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    e.printStackTrace();
	    log.error( "短信发送失败", e );
	    return ServerResponse.createByError();
	}
    }


    @ApiOperation( value = "优惠券绑定手机号码,将会把新的member信息set到session中", notes = "H5绑定手机号码,将会把新的member信息set到session中" )
    @ApiImplicitParams( {
		    @ApiImplicitParam( name = "phone", value = "手机号码", paramType = "query", required = true, dataType = "String" ),
		    @ApiImplicitParam( name = "busId", value = "商家id", paramType = "query", required = true, dataType = "int" ) } )
    @ResponseBody
    @RequestMapping( value = "/bingdingPhone", method = RequestMethod.POST )
    public ServerResponse bingdingPhone(HttpServletRequest request, HttpServletResponse response, @RequestParam String json){

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
	    Integer memberId = member.getId();
	    String phone = CommonUtil.toString( params.get( "phone" ) );
	    String vcode = CommonUtil.toString( params.get( "vcode" ) );
	    duofenCardNewPhoneService.bingdingPhone( request,memberId, phone, busId,vcode );
	    return ServerResponse.createBySuccess(  );
	} catch ( BusinessException e ) {
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    e.printStackTrace();
	    log.error( "优惠券绑定手机号码异常", e );
	    return ServerResponse.createByError();
	}
    }

    @ApiOperation( value = "优惠券核销", notes = "优惠券核销" )
    @ApiImplicitParams( {
		    @ApiImplicitParam( name = "phone", value = "手机号码", paramType = "query", required = true, dataType = "String" ),
		    @ApiImplicitParam( name = "busId", value = "商家id", paramType = "query", required = true, dataType = "int" ) } )
    @ResponseBody
    @RequestMapping( value = "/useVerificationDuofenCard", method = RequestMethod.POST )
    public ServerResponse useVerificationDuofenCard(HttpServletRequest request, HttpServletResponse response, @RequestParam String json){
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
	    Integer receiveId = CommonUtil.toInteger( params.get( "receiveId" ) );
	    Map<String,Object> map=duofenCardNewPhoneService.useVerificationDuofenCard( receiveId );
	    return ServerResponse.createBySuccess( map );
	} catch ( BusinessException e ) {
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    e.printStackTrace();
	    log.error( "优惠券核销展现异常", e );
	    return ServerResponse.createByError();
	}
    }

    @ApiOperation( value = "关注公众号二维码", notes = "关注公众号二维码" )
    @ResponseBody
    @RequestMapping( value = "/guangzhuiQrcode", method = RequestMethod.GET )
    public void guangzhuiQrcode( HttpServletRequest request, HttpServletResponse response, @RequestParam String url ) throws Exception {
	QRcodeKit.buildQRcode( url, 500, 500, response );
    }


    @ApiOperation( value = "优惠券二维码", notes = "优惠券二维码" )
    @ResponseBody
    @RequestMapping( value = "/useVerificationQrcode", method = RequestMethod.GET )
    public void useVerificationQrcode( HttpServletRequest request, HttpServletResponse response, @RequestParam String code ) throws Exception {
	QRcodeKit.buildQRcode( code, 500, 500, response );
    }

    @ApiOperation( value = "查询门店信息", notes = "查询门店信息" )
    @ResponseBody
    @RequestMapping( value = "/findShopByReceiveId", method = RequestMethod.POST )
    public ServerResponse findShopByReceiveId(HttpServletRequest request, HttpServletResponse response, @RequestParam String json){
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
	    List<Map> returnList=duofenCardNewPhoneService.findShopByReceiveId( params );
	    return ServerResponse.createBySuccess( returnList );
	} catch ( BusinessException e ) {
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    e.printStackTrace();
	    log.error( "查询门店信息异常", e );
	    return ServerResponse.createByError();
	}
    }



    @ApiOperation( value = "自助核销优惠券", notes = "自助核销优惠券" )
    @ResponseBody
    @RequestMapping( value = "/verificationDuofenCardGet", method = RequestMethod.POST )
    public ServerResponse verificationDuofenCardGet(HttpServletRequest request, HttpServletResponse response, @RequestParam String json){
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
	    Integer receiveId = CommonUtil.toInteger( params.get( "receiveId" ) );
	    Integer shopId = CommonUtil.toInteger( params.get( "shopId" ) );
	    duofenCardNewPhoneService.verificationDuofenCardGet( receiveId,shopId );
	    return ServerResponse.createBySuccess(  );
	} catch ( BusinessException e ) {
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    e.printStackTrace();
	    log.error( "优惠券核销展现异常", e );
	    return ServerResponse.createByError();
	}
    }
}
