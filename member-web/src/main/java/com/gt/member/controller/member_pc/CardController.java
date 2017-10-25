package com.gt.member.controller.member_pc;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gt.api.enums.ResponseEnums;
import com.gt.api.util.HttpClienUtils;
import com.gt.api.util.RequestUtils;
import com.gt.api.util.SessionUtils;
import com.gt.common.entity.BusUserEntity;
import com.gt.common.entity.TCommonStaffEntity;
import com.gt.common.entity.WxPublicUsersEntity;
import com.gt.member.dao.MemberEntityDAO;
import com.gt.member.dao.MemberGradetypeDAO;
import com.gt.member.dao.MemberQcodeWxDAO;
import com.gt.member.dao.common.BusUserDAO;
import com.gt.member.dao.common.WxPublicUsersDAO;
import com.gt.member.dao.common.WxShopDAO;
import com.gt.member.dto.ServerResponse;
import com.gt.member.entity.MemberCardmodel;
import com.gt.member.entity.MemberCardtype;
import com.gt.member.entity.MemberEntity;
import com.gt.member.entity.MemberQcodeWx;
import com.gt.member.service.common.dict.DictService;
import com.gt.member.service.member.CardERPService;
import com.gt.member.service.member.MemberCardService;
import com.gt.member.util.*;
import com.gt.util.entity.param.sms.OldApiSms;
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
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 会员卡规则新增和修改
 * </p>
 *
 * @author pengjiangli
 * @since 2017-08-09
 */
@Api( value = "会员卡规则新增和修改", description = "会员卡规则新增和修改" )
@Controller
@RequestMapping( "/memberPc/editCard" )
public class CardController {

    private static final Logger LOG = LoggerFactory.getLogger( CardController.class );

    @Autowired
    private MemberCardService memberCardService;

    @Autowired
    private BusUserDAO busUserMapper;


    @ApiOperation( value = "查询会员卡类型", notes = "查询会员卡类型" )
    @ResponseBody
    @RequestMapping( value="/findCardType", method = RequestMethod.GET )
    public ServerResponse findCardType(HttpServletRequest request, HttpServletResponse response){
	try {
	    Integer busId= SessionUtils.getPidBusId( request);
	    List< MemberCardtype > cardTypes = memberCardService.findCardType( busId);
	    return ServerResponse.createBySuccess( cardTypes );
	}catch ( Exception e ){
	    LOG.error( "查询会员卡类型异常：", e );
	    e.printStackTrace();
	    return ServerResponse.createByError( ResponseEnums.ERROR.getCode(), "查询会员卡类型异常" );
	}
    }


    @ApiOperation( value = "会员卡新增和修改第1步页面", notes = "会员卡新增和修改第1步页面" )
    @ApiImplicitParams( @ApiImplicitParam( name = "ctId", value = "会员卡类型id", paramType = "query", required = false, dataType = "int" ) )
    @ResponseBody
    @RequestMapping( value="/editGradeTypeFrist", method = RequestMethod.GET )
    public ServerResponse editGradeTypeFrist( HttpServletRequest request, HttpServletResponse response, Integer ctId ) {
	try {
	    Integer busId = SessionUtils.getPidBusId( request );
	    Map< String,Object > map = memberCardService.editGradeTypeFrist( busId, ctId );
	    return ServerResponse.createBySuccess( map );
	} catch ( Exception e ) {
	    LOG.error( "会员卡新增和修改第1步页面：", e );
	    e.printStackTrace();
	    return ServerResponse.createByError( ResponseEnums.ERROR.getCode(), "会员卡新增和修改异常" );
	}
    }

    @ApiOperation( value = "会员卡新增和修改第2步页面", notes = "会员卡新增和修改第2步页面" )
    @ApiImplicitParams( @ApiImplicitParam( name = "ctId", value = "会员卡类型id", paramType = "query", required = false, dataType = "int" ) )
    @ResponseBody
    @RequestMapping( value="/editGradeTypeSecond", method = RequestMethod.GET )
    public ServerResponse editGradeTypeSecond( HttpServletRequest request, HttpServletResponse response, Integer ctId ) {
	try {
	    Integer busId = SessionUtils.getPidBusId( request );
	    Map< String,Object > map = memberCardService.editGradeTypeSecond( busId, ctId );
	    return ServerResponse.createBySuccess( map );
	} catch ( Exception e ) {
	    LOG.error( "会员卡新增和修改第2步页面：", e );
	    e.printStackTrace();
	    return ServerResponse.createByError( ResponseEnums.ERROR.getCode(), "会员卡新增和修改异常" );
	}
    }

    @ApiOperation( value = "查询卡片背景模板", notes = "查询卡片背景模板" )
    @ResponseBody
    @RequestMapping( value="/findCardModel", method = RequestMethod.GET )
    public ServerResponse findCardModel( HttpServletRequest request, HttpServletResponse response ) {
	try {
	    Integer busId = SessionUtils.getPidBusId( request );
	    List< MemberCardmodel > cardmodels = memberCardService.findCardModel( busId );
	    return ServerResponse.createBySuccess( cardmodels );
	} catch ( Exception e ) {
	    LOG.error( "查询卡片背景模板异常：", e );
	    e.printStackTrace();
	    return ServerResponse.createByError( ResponseEnums.ERROR.getCode(), "查询卡片背景模板异常" );
	}
    }

    @ApiOperation( value = "保存卡片背景模板", notes = "保存卡片背景模板" )
    @ApiImplicitParams( @ApiImplicitParam( name = "param", value = "json字符传", paramType = "query", required = false, dataType = "int" ) )
    @ResponseBody
    @RequestMapping( value = "/saveCardModel" , method = RequestMethod.GET)
    public ServerResponse saveCardModel( HttpServletRequest request, HttpServletResponse response, @RequestParam String param){
	try {
	    Integer busId = SessionUtils.getPidBusId( request );
	    memberCardService.saveCardModel(busId, param);
	    return ServerResponse.createBySuccess(  );
	} catch ( Exception e ) {
	    LOG.error( "查询卡片背景模板异常：", e );
	    e.printStackTrace();
	    return ServerResponse.createByError( ResponseEnums.ERROR.getCode(), "查询卡片背景模板异常" );
	}
    }

    @ApiOperation( value = "会员卡新增和修改第3步页面", notes = "会员卡新增和修改第3步页面" )
    @ApiImplicitParams( @ApiImplicitParam( name = "ctId", value = "会员卡类型id", paramType = "query", required = false, dataType = "int" ) )
    @ResponseBody
    @RequestMapping( value = "/editGradeTypeThird" , method = RequestMethod.GET)
    public ServerResponse editGradeTypeThird( HttpServletRequest request, HttpServletResponse response, Integer ctId ){
	try {
	    Integer busId = SessionUtils.getPidBusId( request );
	    memberCardService.editGradeTypeThird(busId, ctId);
	    return ServerResponse.createBySuccess(  );
	} catch ( Exception e ) {
	    LOG.error( "会员卡新增和修改第3步页面异常：", e );
	    e.printStackTrace();
	    return ServerResponse.createByError( ResponseEnums.ERROR.getCode(), "会员卡新增和修改第3步页面异常" );
	}

    }

}
