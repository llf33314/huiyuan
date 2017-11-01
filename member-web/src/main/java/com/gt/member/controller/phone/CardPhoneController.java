package com.gt.member.controller.phone;

import com.alibaba.fastjson.JSON;
import com.gt.api.bean.session.Member;
import com.gt.api.enums.ResponseEnums;
import com.gt.api.util.SessionUtils;
import com.gt.member.controller.RemoteAuthori.AuthorizeOrLoginController;
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
 * 卡片设置和消息设置
 * </p>
 *
 * @author pengjiangli
 * @since 2017-08-09
 */
@Api( value = "卡片设置和消息设置", description = "卡片设置和消息设置" )
@Controller
@CrossOrigin
@RequestMapping( "/memberPhone/member" )
public class CardPhoneController extends AuthorizeOrLoginController {

    private static final Logger LOG = LoggerFactory.getLogger( CardPhoneController.class );

    @Autowired
    private MemberCardPhoneService memberCardPhoneService;


    @ApiOperation( value = "查询领取会员卡页面数据", notes = "查询领取会员卡页面数据" )
    @ApiImplicitParams({
		    @ApiImplicitParam( name = "busId", value = "商家id", paramType = "query", required = false, dataType = "string" ),
		    @ApiImplicitParam( name = "requestUrl", value = "授权回调地址", paramType = "query", required = false, dataType = "int" )
    })
    @ResponseBody
    @RequestMapping( value = "/findLingquData", method = RequestMethod.GET )
    public ServerResponse findLingquData( HttpServletRequest request, HttpServletResponse response,String json ) {
	try {
	    Map< String,Object > map = JSON.parseObject( json, Map.class );
	    Integer busId=CommonUtil.toInteger( map.get( "busId" ) );
	    Member member= SessionUtils.getLoginMember( request,busId );
	    if(CommonUtil.isEmpty( member )) {
		String url = authorizeMember( request, response, map );
		if ( CommonUtil.isNotEmpty( url ) ) {
		    return ServerResponse.createByError(ResponseMemberEnums.USERGRANT.getCode(), url );
		}
	    }
	    Map<String,Object> returnMap= memberCardPhoneService.findLingquData(  request,busId);
	    returnMap.put( "member",member );
	    return ServerResponse.createBySuccess( returnMap );
	} catch ( Exception e ) {
	    LOG.error( "查询会员卡类型异常：", e );
	    e.printStackTrace();
	    return ServerResponse.createByError( ResponseEnums.ERROR.getCode(), "查询会员卡类型异常" );
	}
    }

    @ApiOperation( value = "领取会员卡", notes = "领取会员卡" )
    @ApiImplicitParams({
		    @ApiImplicitParam( name = "busId", value = "商家id", paramType = "query", required = false, dataType = "string" ),
		    @ApiImplicitParam( name = "requestUrl", value = "授权回调地址", paramType = "query", required = false, dataType = "int" )
    })
    @ResponseBody
    @RequestMapping( value = "/linquMemberCard", method = RequestMethod.GET )
    public ServerResponse linquMemberCard(HttpServletRequest request, HttpServletResponse response,@RequestBody Map<String,Object> params){
	try {
	    memberCardPhoneService.linquMemberCard( params );
	    return ServerResponse.createBySuccess(  );
	}catch ( BusinessException e ){
	    return ServerResponse.createByError(e.getCode(),e.getMessage());
	}
    }



    @ApiOperation( value = "购买会员卡支付回调", notes = "购买会员卡支付回调" )
    @ResponseBody
    @RequestMapping( value = "/buyMemberCard", method = RequestMethod.GET )
    public ServerResponse buyMemberCard(HttpServletRequest request, HttpServletResponse response,@RequestBody Map<String,Object> params){
	try {
	    memberCardPhoneService.buyMemberCard( params );
	    return ServerResponse.createBySuccess(  );
	}catch ( Exception e ){
	    return ServerResponse.createByError( ResponseEnums.ERROR.getCode(),ResponseEnums.ERROR.getMsg() );
	}
    }
}
