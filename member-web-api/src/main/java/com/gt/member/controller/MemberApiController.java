package com.gt.member.controller;

import com.alibaba.fastjson.JSONObject;
import com.gt.api.enums.ResponseEnums;
import com.gt.member.base.BaseController;
import com.gt.member.dto.ServerResponse;
import com.gt.member.entity.MemberCard;
import com.gt.member.entity.MemberEntity;
import com.gt.member.entity.MemberGradetype;
import com.gt.member.exception.BusinessException;
import com.gt.member.service.memberApi.MemberApiService;
import com.gt.member.util.CommonUtil;
import com.gt.member.util.Page;
import com.gt.member.util.PropertiesUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 会员httpservice 接口
 *
 * @author Administrator
 */
@Api( value = "粉丝和会员接口", description = "粉丝和会员接口" )
@Controller
@RequestMapping( "/memberAPI/member/" )
public class MemberApiController extends BaseController {

    private static final Logger LOG = Logger.getLogger( MemberApiController.class );

    @Autowired
    private MemberApiService memberApiService;

    @ApiOperation( value = "根据memberId和门店查询会员数据 返回数据包含会员信息、微信卡券、多粉卡券", notes = "根据memberId和门店查询会员数据" )
    @ApiImplicitParams( { @ApiImplicitParam( name = "memberId", value = "卡号或手机号", paramType = "query", required = true, dataType = "int" ),
		    @ApiImplicitParam( name = "shopId", value = "门店id", paramType = "query", required = true, dataType = "int" ) } )
    @ResponseBody
    @RequestMapping( value = "/findCardByMembeId", method = RequestMethod.POST )
    public ServerResponse findCardByMembeId( HttpServletRequest request, HttpServletResponse response, @RequestBody String param ) {
	try {
	    Map< String,Object > requestBody = JSONObject.parseObject( param );
	    Integer memberId = CommonUtil.toInteger( requestBody.get( "memberId" ) );
	    Integer shopId = CommonUtil.toInteger( requestBody.get( "shopId" ) );
	    Map< String,Object > map = memberApiService.findMemberCardByMemberId( memberId, shopId );
	    return ServerResponse.createBySuccess( map );
	} catch ( BusinessException e ) {
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );
	}
    }

    @ApiOperation( value = "根据手机号或会员卡号获取会员信息", notes = "手机号或卡号查询会员相关信息包括微信多粉优惠券 \n " + "cardNo 卡号或手机号 ,busId 商家id,shopId 门店id" )
    @ApiImplicitParams( { @ApiImplicitParam( name = "cardNo", value = "卡号或手机号", paramType = "query", required = true, dataType = "String" ),
		    @ApiImplicitParam( name = "busId", value = "商家id", paramType = "query", required = true, dataType = "int" ),
		    @ApiImplicitParam( name = "shopId", value = "门店id", paramType = "query", required = true, dataType = "int" ) } )
    @ResponseBody
    @RequestMapping( value = "/findMemberCard", method = RequestMethod.POST )
    public ServerResponse findMemberCard( HttpServletRequest request, HttpServletResponse response, @RequestBody String param ) {
	try {
	    Map< String,Object > requestBody = JSONObject.parseObject( param );
	    String cardNo = CommonUtil.toString( requestBody.get( "cardNo" ) );
	    Integer busId = CommonUtil.toInteger( requestBody.get( "busId" ) );
	    Integer shopId = CommonUtil.toInteger( requestBody.get( "shopId" ) );
	    String cardNoKey = PropertiesUtil.getCardNoKey();
	    Map< String,Object > map = memberApiService.findMemberCard( busId, cardNoKey, cardNo, shopId );
	    return ServerResponse.createBySuccess( map );
	} catch ( BusinessException e ) {
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    return ServerResponse.createByError( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getMsg() );
	}
    }

    @ApiOperation( value = "根据粉丝id获取粉丝信息", notes = "获取粉丝信息" )
    @ApiImplicitParam( name = "memberId", value = "粉丝id", paramType = "query", required = true, dataType = "int" )
    @ResponseBody
    @RequestMapping( value = "/findByMemberId", method = RequestMethod.POST )
    public ServerResponse findByMemberId( HttpServletRequest request, HttpServletResponse response, @RequestBody String param ) {
	try {
	    Map< String,Object > requestBody = JSONObject.parseObject( param );
	    Integer memberId = CommonUtil.toInteger( requestBody.get( "memberId" ) );
	    MemberEntity memberEntity = memberApiService.findByMemberId( memberId );
	    return ServerResponse.createBySuccess( memberEntity );
	} catch ( BusinessException e ) {
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );
	}
    }

    @ApiOperation( value = "根据粉丝id获取粉丝和会员卡信息", notes = "根据粉丝id获取粉丝和会员卡信息" )
    @ApiImplicitParam( name = "memberId", value = "粉丝id", paramType = "query", required = true, dataType = "int" )
    @ResponseBody
    @RequestMapping( value = "/findMemberCardByMemberId", method = RequestMethod.POST )
    public ServerResponse findMemberCardByMemberId(HttpServletRequest request, HttpServletResponse response, @RequestBody String param){
	try {
	    Map< String,Object > requestBody = JSONObject.parseObject( param );
	    Integer memberId = CommonUtil.toInteger( requestBody.get( "memberId" ) );
	    Map<String,Object> map = memberApiService.findMemberCardByMemberId( memberId );
	    return ServerResponse.createBySuccess( map );
	} catch ( BusinessException e ) {
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );
	}

    }



    @ApiOperation( value = "判断储值卡金额是否充足", notes = "判断储值卡金额是否充足" )
    @ApiImplicitParams( { @ApiImplicitParam( name = "memberId", value = "粉丝id", paramType = "query", required = true, dataType = "int" ),
		    @ApiImplicitParam( name = "money", value = "消费金额", paramType = "query", required = true, dataType = "int" ), } )
    @ResponseBody
    @RequestMapping( value = "/isAdequateMoney", method = RequestMethod.POST )
    public ServerResponse isAdequateMoney( HttpServletRequest request, HttpServletResponse response, @RequestBody String param ) {
	try {
	    Map< String,Object > requestBody = JSONObject.parseObject( param );
	    Integer memberId = CommonUtil.toInteger( requestBody.get( "memberId" ) );
	    Double money = CommonUtil.toDouble( requestBody.get( "money" ) );
	    memberApiService.isAdequateMoney( memberId, money );
	    return ServerResponse.createBySuccess();
	} catch ( BusinessException e ) {
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );
	}
    }

    @ApiOperation( value = "根据粉丝id获取会员折扣（0到1）", notes = "根据粉丝id获取会员折扣" )
    @ApiImplicitParam( name = "memberId", value = "粉丝id", paramType = "query", required = true, dataType = "int" )
    @ResponseBody
    @RequestMapping( value = "/findCardTypeReturnDiscount", method = RequestMethod.POST )
    public ServerResponse findCardTypeReturnDiscount( HttpServletRequest request, HttpServletResponse response, @RequestBody String param ) {
	try {
	    Map< String,Object > requestBody = JSONObject.parseObject( param );
	    Integer memberId = CommonUtil.toInteger( requestBody.get( "memberId" ) );
	    Double discount = memberApiService.findCardTypeReturnDiscount( memberId );
	    return ServerResponse.createBySuccess( discount );
	} catch ( BusinessException e ) {
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );
	}
    }

    @ApiOperation( value = "小程序绑定手机号码", notes = "小程序绑定手机号码 返回member对象数据" )
    @ApiImplicitParams( { @ApiImplicitParam( name = "memberId", value = "粉丝id", paramType = "query", required = true, dataType = "int" ),
		    @ApiImplicitParam( name = "phone", value = "手机号码", paramType = "query", required = true, dataType = "String" ),
		    @ApiImplicitParam( name = "busId", value = "商家id", paramType = "query", required = true, dataType = "int" ) } )
    @ResponseBody
    @RequestMapping( value = "/bingdingPhone", method = RequestMethod.POST )
    public ServerResponse bingdingPhone( HttpServletRequest request, HttpServletResponse response, @RequestBody String param ) {
	try {
	    Map< String,Object > requestBody = JSONObject.parseObject( param );
	    Integer memberId = CommonUtil.toInteger( requestBody.get( "memberId" ) );
	    Integer busId = CommonUtil.toInteger( requestBody.get( "busId" ) );
	    String phone = CommonUtil.toString( requestBody.get( "phone" ) );
	    MemberEntity memberEntity = memberApiService.bingdingPhone( memberId, phone, busId );
	    return ServerResponse.createBySuccess( memberEntity );
	} catch ( BusinessException e ) {
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );
	}
    }

    @ApiOperation( value = "统计会员数量", notes = "根据商家id统计会员数量" )
    @ApiImplicitParam( name = "busId", value = "商家id", paramType = "query", required = true, dataType = "int" )
    @ResponseBody
    @RequestMapping( value = "/countMember", method = RequestMethod.POST )
    public ServerResponse countMember( HttpServletRequest request, HttpServletResponse response, @RequestBody String param ) {
	try {
	    Map< String,Object > requestBody = JSONObject.parseObject( param );
	    Integer busId = CommonUtil.toInteger( requestBody.get( "busId" ) );
	    Map< String,Object > countMember = memberApiService.countMember( busId );
	    return ServerResponse.createBySuccess( countMember );
	} catch ( Exception e ) {
	    logger.error( "统计会员异常", e );
	    return ServerResponse.createByError( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getMsg() );
	}
    }

    @ApiOperation( value = "查询根据ids查询粉丝信息", notes = "分页查询根据ids查询粉丝信息" )
    @ApiImplicitParams( { @ApiImplicitParam( name = "busId", value = "商家id", paramType = "query", required = true, dataType = "int" ),
		    @ApiImplicitParam( name = "ids", value = "粉丝id字符集合 逗号隔开", paramType = "query", required = true, dataType = "String" ) } )
    @ResponseBody
    @RequestMapping( value = "/findMemberByids", method = RequestMethod.POST )
    public ServerResponse findMemberByids( HttpServletRequest request, HttpServletResponse response, @RequestBody String param ) {
	try {
	    Map< String,Object > requestBody = JSONObject.parseObject( param );
	    Integer busId = CommonUtil.toInteger( requestBody.get( "busId" ) );
	    String ids = CommonUtil.toString( requestBody.get( "ids" ) );
	    List< Map< String,Object > > mapList = memberApiService.findMemberByIds( busId, ids );
	    return ServerResponse.createBySuccess( mapList );
	} catch ( Exception e ) {
	    logger.error( "查询根据ids查询粉丝信息异常", e );
	    return ServerResponse.createByError( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getMsg() );
	}
    }

    @ApiOperation( value = "根据memberId查询粉丝信息id集合", notes = "根据id查询粉丝信息id集合" )
    @ApiImplicitParam( name = "memberId", value = "粉丝id", paramType = "query", required = true, dataType = "int" )
    @ResponseBody
    @RequestMapping( value = "/findMemberIdsByid", method = RequestMethod.POST )
    public ServerResponse findMemberIdsByid( HttpServletRequest request, HttpServletResponse response, @RequestBody String param ) {
	try {
	    Map< String,Object > requestBody = JSONObject.parseObject( param );
	    Integer memberId = CommonUtil.toInteger( requestBody.get( "memberId" ) );
	    List< Integer > mapList = memberApiService.findMemberIds( memberId );
	    return ServerResponse.createBySuccess( mapList );
	} catch ( Exception e ) {
	    logger.error( "根据id查询粉丝信息id集合异常", e );
	    return ServerResponse.createByError( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getMsg() );
	}
    }

    @ApiOperation( value = "判断粉丝是否是会员", notes = "判断粉丝是否是会员" )
    @ApiImplicitParam( name = "memberId", value = "", paramType = "query", required = true, dataType = "int" )
    @ResponseBody
    @RequestMapping( value = "/isMember", method = RequestMethod.POST )
    public ServerResponse isMember( HttpServletRequest request, HttpServletResponse response, @RequestBody String param ) {
	try {
	    Map< String,Object > requestBody = JSONObject.parseObject( param );
	    Integer memberId = CommonUtil.toInteger( requestBody.get( "memberId" ) );
	    memberApiService.isMemember( memberId );
	    return ServerResponse.createBySuccess();
	} catch ( BusinessException e ) {
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    logger.error( "判断粉丝是否是会员异常", e );
	    return ServerResponse.createByError( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getMsg() );
	}
    }

    @ApiOperation( value = "商场关注赠送积分", notes = "商场关注赠送积分" )
    @ApiImplicitParams( { @ApiImplicitParam( name = "memberId", value = "", paramType = "query", required = true, dataType = "int" ),
		    @ApiImplicitParam( name = "jifen", value = "", paramType = "query", required = true, dataType = "int" ), } )
    @ResponseBody
    @RequestMapping( value = "/updateJifen", method = RequestMethod.POST )
    public ServerResponse updateJifen( HttpServletRequest request, HttpServletResponse response, @RequestBody String param ) {
	try {
	    Map< String,Object > requestBody = JSONObject.parseObject( param );
	    Integer memberId = CommonUtil.toInteger( requestBody.get( "memberId" ) );
	    Integer jifen = CommonUtil.toInteger( requestBody.get( "jifen" ) );
	    memberApiService.updateJifen( memberId, jifen );
	    return ServerResponse.createBySuccess();
	} catch ( Exception e ) {
	    logger.error( "商场关注赠送积分异常", e );
	    return ServerResponse.createByError( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getMsg() );
	}
    }

    @ApiOperation( value = "会员卡类型", notes = "会员卡类型" )
    @ApiImplicitParam( name = "memberId", value = "", paramType = "query", required = true, dataType = "int" )
    @ResponseBody
    @RequestMapping( value = "/isCardType", method = RequestMethod.POST )
    public ServerResponse isCardType( HttpServletRequest request, HttpServletResponse response, @RequestBody String param ) {
	try {
	    Map< String,Object > requestBody = JSONObject.parseObject( param );
	    Integer memberId = CommonUtil.toInteger( requestBody.get( "memberId" ) );
	    Integer ctId = memberApiService.isCardType( memberId );
	    return ServerResponse.createBySuccess( ctId );
	} catch ( BusinessException e ) {
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    logger.error( "会员卡类型异常", e );
	    return ServerResponse.createByError( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getMsg() );
	}
    }

    @ApiOperation( value = "查询购买的会员卡模板", notes = "查询购买的会员卡模板" )
    @ApiImplicitParam( name = "busId", value = "", paramType = "query", required = true, dataType = "int" )
    @ResponseBody
    @RequestMapping( value = "/findBuyGradeType", method = RequestMethod.POST )
    public ServerResponse findBuyGradeType( HttpServletRequest request, HttpServletResponse response, @RequestBody String param ) {
	try {
	    Map< String,Object > requestBody = JSONObject.parseObject( param );
	    Integer busId = CommonUtil.toInteger( requestBody.get( "busId" ) );
	    List< Map< String,Object > > maplist = memberApiService.findBuyGradeType( busId );
	    return ServerResponse.createBySuccess( maplist );
	} catch ( Exception e ) {
	    logger.error( "查询购买的会员卡模板异常", e );
	    return ServerResponse.createByError( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getMsg() );
	}
    }

    @ApiOperation( value = "查询会员卡片名称", notes = "查询会员卡片名称" )
    @ApiImplicitParam( name = "memberId", value = "", paramType = "query", required = true, dataType = "int" )
    @ResponseBody
    @RequestMapping( value = "/findGradeType", method = RequestMethod.POST )
    public ServerResponse findGradeType( HttpServletRequest request, HttpServletResponse response, @RequestBody String param ) {
	try {
	    Map< String,Object > requestBody = JSONObject.parseObject( param );
	    Integer memberId = CommonUtil.toInteger( requestBody.get( "memberId" ) );
	    MemberGradetype gradeType = memberApiService.findGradeType( memberId );
	    return ServerResponse.createBySuccess( gradeType );
	} catch ( Exception e ) {
	    logger.error( "查询会员卡片名称异常", e );
	    return ServerResponse.createByError( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getMsg() );
	}
    }

    @ApiOperation( value = "（商城）查询会员积分记录", notes = "（商城）查询会员积分记录" )
    @ApiImplicitParams( { @ApiImplicitParam( name = "memberId", value = "粉丝id", paramType = "query", required = true, dataType = "int" ),
		    @ApiImplicitParam( name = "page", value = "页数", paramType = "query", required = true, dataType = "int" ),
		    @ApiImplicitParam( name = "pageSize", value = "条数", paramType = "query", required = true, dataType = "int" ) } )

    @ResponseBody
    @RequestMapping( value = "/findCardrecord", method = RequestMethod.POST )
    public ServerResponse findCardrecord( HttpServletRequest request, HttpServletResponse response, @RequestBody String param ) {
	try {
	    Map< String,Object > requestBody = JSONObject.parseObject( param );
	    Integer memberId = CommonUtil.toInteger( requestBody.get( "memberId" ) );
	    Integer page = CommonUtil.toInteger( requestBody.get( "page" ) );
	    Integer pageSize = CommonUtil.toInteger( requestBody.get( "pageSize" ) );
	    List< Map< String,Object > > listMap = memberApiService.findCardrecord( memberId, page, pageSize );
	    return ServerResponse.createBySuccess( listMap );
	} catch ( BusinessException e ) {
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    logger.error( "商城）查询会员积分记录异常", e );
	    return ServerResponse.createByError( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getMsg() );
	}
    }

    @ApiOperation( value = "（商城）评论修改会员积分或粉币", notes = "（商城）评论修改会员积分或粉币" )
    @ApiImplicitParams( { @ApiImplicitParam( name = "memberId", value = "粉丝Id", paramType = "query", required = true, dataType = "int" ),
		    @ApiImplicitParam( name = "jifen", value = "积分", paramType = "query", required = true, dataType = "int" ),
		    @ApiImplicitParam( name = "fenbi", value = "粉币", paramType = "query", required = true, dataType = "int" ) } )

    @ResponseBody
    @RequestMapping( value = "/updateJifenAndFenBiByPinglu", method = RequestMethod.POST )
    public ServerResponse updateJifenAndFenBiByPinglu( HttpServletRequest request, HttpServletResponse response, @RequestBody String param ) {
	try {
	    Map< String,Object > requestBody = JSONObject.parseObject( param );
	    memberApiService.updateJifenAndFenBiByPinglu( requestBody );
	    return ServerResponse.createBySuccess();
	} catch ( BusinessException e ) {
	    logger.error( "商城）评论修改会员积分或粉币异常", e );
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );
	}

    }

    @ApiOperation( value = "查询会员卡信息", notes = "查询会员卡信息" )
    @ApiImplicitParam( name = "mcId", value = "会员卡id", paramType = "query", required = true, dataType = "int" )
    @ResponseBody
    @RequestMapping( value = "/findMemberCardByMcId", method = RequestMethod.POST )
    public ServerResponse findMemberCardByMcId( HttpServletRequest request, HttpServletResponse response, @RequestBody String param ) {
	try {
	    Map< String,Object > requestBody = JSONObject.parseObject( param );
	    Integer mcId = CommonUtil.toInteger( requestBody.get( "mcId" ) );
	    MemberCard card = memberApiService.findMemberCardByMcId( mcId );
	    return ServerResponse.createBySuccess( card );
	} catch ( BusinessException e ) {
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    logger.error( "查询会员卡信息异常", e );
	    return ServerResponse.createByError( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getMsg() );
	}
    }

    @ApiOperation( value = "跨门店 根据memberId和门店集合查询会员数据 返回数据包含会员信息、微信卡券、多粉卡券", notes = "根据memberId和门店集合查询会员数据" )
    @ApiImplicitParams( { @ApiImplicitParam( name = "memberId", value = "卡号或手机号", paramType = "query", required = true, dataType = "int" ),
		    @ApiImplicitParam( name = "shopIds", value = "门店id集合逗号隔开", paramType = "query", required = true, dataType = "int" ) } )
    @ResponseBody
    @RequestMapping( value = "/findCardAndShopIdsByMembeId", method = RequestMethod.POST )
    public ServerResponse findCardAndShopIdsByMembeId( HttpServletRequest request, HttpServletResponse response, @RequestBody String param ) {
	try {
	    Map< String,Object > requestBody = JSONObject.parseObject( param );
	    Integer memberId = CommonUtil.toInteger( requestBody.get( "memberId" ) );
	    String shopIds = CommonUtil.toString( requestBody.get( "shopIds" ) );
	    Map< String,Object > map = memberApiService.findMemberCardByMemberIdAndshopIds( memberId, shopIds );
	    return ServerResponse.createBySuccess( map );
	} catch ( BusinessException e ) {
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );
	}
    }

    @ApiOperation( value = "根据ids集合查询粉丝信息返回包含数据(id,昵称，手机号码,头像)", notes = "查询粉丝信息返回包含数据((id,昵称，手机号码,头像))" )
    @ApiImplicitParams( { @ApiImplicitParam( name = "ids", value = "粉丝id集合字符串 逗号隔开", paramType = "query", required = true, dataType = "String" ),
		    @ApiImplicitParam( name = "busId", value = "商家id", paramType = "query", required = true, dataType = "int" ) } )
    @ResponseBody
    @RequestMapping( value = "/findMemberByIds", method = RequestMethod.POST )
    public ServerResponse findMemberByIds( HttpServletRequest request, HttpServletResponse response, @RequestBody String param ) {
	try {
	    Map< String,Object > requestBody = JSONObject.parseObject( param );
	    List< Map< String,Object > > memberList = memberApiService.findMemberByIds( requestBody );
	    return ServerResponse.createBySuccess( memberList );
	} catch ( BusinessException e ) {
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );
	}
    }

    @ApiOperation( value = "根据粉丝手机号码查询粉丝信息返回包含数据((id,昵称，手机号码,头像))", notes = "查询粉丝信息返回包含数据((id,昵称，手机号码,头像))" )
    @ApiImplicitParams( { @ApiImplicitParam( name = "phone", value = "phone", paramType = "query", required = true, dataType = "String" ),
		    @ApiImplicitParam( name = "busId", value = "商家id", paramType = "query", required = true, dataType = "int" ) } )
    @ResponseBody
    @RequestMapping( value = "/findMemberByPhone", method = RequestMethod.POST )
    public ServerResponse findMemberByPhone( HttpServletRequest request, HttpServletResponse response, @RequestBody String param ) {
	try {
	    Map< String,Object > requestBody = JSONObject.parseObject( param );
	    List< Map< String,Object > > memberList = memberApiService.findMemberByPhoneAndBusId( requestBody );
	    return ServerResponse.createBySuccess( memberList );
	} catch ( BusinessException e ) {
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );
	}
    }

    @ApiOperation( value = "（魔盒）查询会员发布的会员卡类型", notes = "（墨盒）查询会员发布的会员卡类型" )
    @ApiImplicitParam( name = "busId", value = "商家id", paramType = "query", required = true, dataType = "int" )
    @ResponseBody
    @RequestMapping( value = "/findMemberCardType", method = RequestMethod.POST )
    public ServerResponse findMemberCardType( HttpServletRequest request, HttpServletResponse response, @RequestBody String param ) {
	try {
	    Map< String,Object > requestBody = JSONObject.parseObject( param );
	    Map< String,Object > cardTypes = memberApiService.findMemberCardTypeByBusId( requestBody );
	    return ServerResponse.createBySuccess( cardTypes );
	} catch ( BusinessException e ) {
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );
	}
    }

    @ApiOperation( value = "（魔盒）查询会员发布的会员卡等级", notes = "（墨盒）查询会员发布的会员卡会员卡等级" )
    @ApiImplicitParams( { @ApiImplicitParam( name = "ctId", value = "会员卡类型", paramType = "query", required = true, dataType = "int" ),
		    @ApiImplicitParam( name = "busId", value = "商家id", paramType = "query", required = true, dataType = "int" ) } )
    @ResponseBody
    @RequestMapping( value = "/findMemberGradeType", method = RequestMethod.POST )
    public ServerResponse findMemberGradeType( HttpServletRequest request, HttpServletResponse response, @RequestBody String param ) {
	try {
	    Map< String,Object > requestBody = JSONObject.parseObject( param );
	    List< Map< String,Object > > cardTypes = memberApiService.findMemberGradeTypeByctId( requestBody );
	    return ServerResponse.createBySuccess( cardTypes );
	} catch ( Exception e ) {
	    return ServerResponse.createByError( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getMsg() );
	}
    }

    @ApiOperation( value = "（魔盒）领取会员卡", notes = "（墨盒）领取会员卡" )
    @ApiImplicitParams( { @ApiImplicitParam( name = "busId", value = "商家id", paramType = "query", required = true, dataType = "int" ),
		    @ApiImplicitParam( name = "ctId", value = "会员卡类型", paramType = "query", required = true, dataType = "int" ),
		    @ApiImplicitParam( name = "phone", value = "手机号", paramType = "query", required = true, dataType = "String" ),
		    @ApiImplicitParam( name = "memberId", value = "粉丝id", paramType = "query", required = false, dataType = "int" ),
		    @ApiImplicitParam( name = "shopId", value = "门店", paramType = "query", required = true, dataType = "int" )

    } )
    @ResponseBody
    @RequestMapping( value = "/linquMemberCard", method = RequestMethod.POST )
    public ServerResponse linquMemberCard( HttpServletRequest request, HttpServletResponse response, @RequestBody String param ) {
	try {
	    Map< String,Object > requestBody = JSONObject.parseObject( param );
	    memberApiService.linquMemberCard( requestBody );
	    return ServerResponse.createBySuccess();
	} catch ( BusinessException e ) {
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );
	}
    }

    @ApiOperation( value = "（魔盒）根据手机号或会员卡号获取会员信息包括会员卡类型 会员卡等级 会员积分和粉币、余额、充值信息", notes = "手机号或卡号查询会员相关信息" )
    @ApiImplicitParams( { @ApiImplicitParam( name = "phone", value = "手机号", paramType = "query", required = true, dataType = "String" ),
		    @ApiImplicitParam( name = "busId", value = "商家id", paramType = "query", required = true, dataType = "int" ) } )
    @ResponseBody
    @RequestMapping( value = "/findMemberAndChongZhi", method = RequestMethod.POST )
    public ServerResponse findMemberAndChongZhi( HttpServletRequest request, HttpServletResponse response, @RequestBody String param ) {
	try {
	    Map< String,Object > requestBody = JSONObject.parseObject( param );
	    Map< String,Object > map = memberApiService.findMemberAndChongZhi( requestBody );
	    return ServerResponse.createBySuccess( map );
	} catch ( BusinessException e ) {
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    return ServerResponse.createByError( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getMsg() );
	}
    }

    @ApiOperation( value = "废弃（魔盒）会员卡充值 支付方式 0支付宝 1微信 10现金", notes = "（魔盒）会员卡充值" )
    @ApiImplicitParams( { @ApiImplicitParam( name = "memberId", value = "粉丝id", paramType = "query", required = true, dataType = "int" ),
		    @ApiImplicitParam( name = "money", value = "充值金额", paramType = "query", required = true, dataType = "int" ),
		    @ApiImplicitParam( name = "paymentType", value = "支付方式", paramType = "query", required = true, dataType = "int" ),
		    @ApiImplicitParam( name = "shopId", value = "门店", paramType = "query", required = true, dataType = "int" ) } )
    @ResponseBody
    @RequestMapping( value = "/successChongZhi", method = RequestMethod.POST )
    public ServerResponse successChongZhi( HttpServletRequest request, HttpServletResponse response, @RequestBody String param ) {
	try {
	    Map< String,Object > requestBody = JSONObject.parseObject( param );
	    memberApiService.successChongZhi( requestBody );
	    return ServerResponse.createBySuccess();
	} catch ( BusinessException e ) {
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );
	}
    }

    @ApiOperation( value = "（魔盒）会员卡充值 支付方式 0支付宝 1微信 10现金", notes = "（魔盒）会员卡充值" )
    @ApiImplicitParams( { @ApiImplicitParam( name = "memberId", value = "粉丝id", paramType = "query", required = true, dataType = "int" ),
		    @ApiImplicitParam( name = "money", value = "充值金额", paramType = "query", required = true, dataType = "int" ),
		    @ApiImplicitParam( name = "paymentType", value = "支付方式", paramType = "query", required = true, dataType = "int" ),
		    @ApiImplicitParam( name = "shopId", value = "门店", paramType = "query", required = true, dataType = "int" ) } )
    @ResponseBody
    @RequestMapping( value = "/successChongZhiVer1", method = RequestMethod.POST )
    public ServerResponse successChongZhiVer1( HttpServletRequest request, HttpServletResponse response, @RequestBody String param ) {
	try {
	    Map< String,Object > requestBody = JSONObject.parseObject( param );
	    memberApiService.successChongZhiVer1( requestBody );
	    return ServerResponse.createBySuccess();
	} catch ( BusinessException e ) {
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );
	}
    }

    @ApiOperation( value = "修改粉丝手机号码", notes = "修改粉丝手机号码" )
    @ApiImplicitParams( { @ApiImplicitParam( name = "memberId", value = "粉丝id", paramType = "query", required = true, dataType = "int" ),
		    @ApiImplicitParam( name = "phone", value = "手机号码", paramType = "query", required = true, dataType = "string" ),
		    @ApiImplicitParam( name = "busId", value = "商家id", paramType = "query", required = true, dataType = "int" ) } )
    @ResponseBody
    @RequestMapping( value = "/updateMemberPhoneByMemberId", method = RequestMethod.POST )
    public ServerResponse updateMemberPhoneByMemberId( HttpServletRequest request, HttpServletResponse response, @RequestBody String param ) {
	try {
	    Map< String,Object > requestBody = JSONObject.parseObject( param );
	    memberApiService.updateMemberPhoneByMemberId( requestBody );
	    return ServerResponse.createBySuccess();
	} catch ( BusinessException e ) {
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );
	}
    }

    @ApiOperation( value = "查询会员的积分和粉币规则", notes = "查询会员的积分和粉币规则" )
    @ApiImplicitParam( name = "busId", value = "商家id", paramType = "query", required = true, dataType = "int" )
    @ResponseBody
    @RequestMapping( value = "/jifenAndFenbiRule", method = RequestMethod.POST )
    public ServerResponse jifenAndFenbiRule( HttpServletRequest request, HttpServletResponse response, @RequestBody String param ) {
	try {
	    Map< String,Object > requestBody = JSONObject.parseObject( param );
	    Integer busId = CommonUtil.toInteger( requestBody.get( "busId" ) );
	    Map< String,Object > map = memberApiService.jifenAndFenbiRule( busId );
	    return ServerResponse.createBySuccess( map );
	} catch ( BusinessException e ) {
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );
	}
    }

    //    @ApiOperation(value = "支付成功回调", notes = "传入值具体描述请看实体类 储值卡支付 直接调用 回调类以处理储值卡扣款")
    //    @ApiImplicitParam( name = "paySuccessBo", value = "PaySuccessBo 实体类", paramType = "query", required = true, dataType = "String" )
    //    @ResponseBody
    //    @RequestMapping(value = "/paySuccess",method = RequestMethod.POST)
    //    public ServerResponse paySuccess(HttpServletRequest request,
    //		    HttpServletResponse response,@RequestBody String paySuccessBo){
    //	try {
    //	    PaySuccessBo paySuccessBo1=JSONObject.toJavaObject( JSONObject.parseObject( paySuccessBo ),PaySuccessBo.class ) ;
    //	    memberApiService.paySuccess(paySuccessBo1);
    //	    return ServerResponse.createBySuccess();
    //	}catch (BusinessException e){
    //	    return ServerResponse.createByError(e.getCode(),e.getMessage());
    //	}
    //    }

    @ApiOperation( value = "（单门店）结算支付成功会员处理支持多种支付数据保存 支付多种支付", notes = "结算支付成功会员处理（包括储值卡扣款、卡券核销、积分粉币扣除、赠送物品）" )
    @ApiImplicitParam( name = "newErpPaySuccessBo", value = "erp结算核销对象 实体类ErpPaySuccessBo", paramType = "query", required = true, dataType = "String" )
    @ResponseBody
    @RequestMapping( value = "/newPaySuccessByErpBalance", method = RequestMethod.POST )
    public ServerResponse newpaySuccessByErpBalance( HttpServletRequest request, HttpServletResponse response, @RequestBody String newErpPaySuccessBo ) {
	try {
	    memberApiService.newPaySuccessByErpBalance( newErpPaySuccessBo );
	    return ServerResponse.createBySuccess();
	} catch ( BusinessException e ) {
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );
	}
    }

    @ApiOperation( value = "(跨门店)结算支付成功会员处理支持多种支付数据保存 支付多种支付", notes = "结算支付成功会员处理（包括储值卡扣款、卡券核销、积分粉币扣除、赠送物品）" )
    @ApiImplicitParam( name = "newErpPaySuccessBos", value = "erp结算核销对象 实体类ErpPaySuccessBo集合", paramType = "query", required = true, dataType = "String" )
    @ResponseBody
    @RequestMapping( value = "/newPaySuccessShopsByErpBalance", method = RequestMethod.POST )
    public ServerResponse newPaySuccessShopsByErpBalance( HttpServletRequest request, HttpServletResponse response, @RequestBody String newErpPaySuccessBos ) {
	try {
	    memberApiService.newPaySuccessShopsByErpBalance( newErpPaySuccessBos );
	    return ServerResponse.createBySuccess();
	} catch ( BusinessException e ) {
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );
	}
    }

    @ApiOperation( value = "结算退款", notes = "结算退款" )
    @ApiImplicitParam( name = "erpRefundBo", value = "erp 实体类erpRefundBo", paramType = "query", required = true, dataType = "String" )
    @ResponseBody
    @RequestMapping( value = "/refundErp", method = RequestMethod.POST )
    public ServerResponse refundErp( HttpServletRequest request, HttpServletResponse response, @RequestBody String erpRefundBo ) {
	try {
	    memberApiService.refundErp( erpRefundBo );
	    return ServerResponse.createBySuccess();
	} catch ( BusinessException e ) {
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );
	}
    }

    @ApiOperation( value = "积分兑换商品", notes = "积分兑换商品" )
    @ApiImplicitParams( { @ApiImplicitParam( name = "memberId", value = "粉丝id", paramType = "query", required = true, dataType = "int" ),
		    @ApiImplicitParam( name = "shopId", value = "门店id", paramType = "query", required = true, dataType = "int" ),
		    @ApiImplicitParam( name = "intergral", value = "兑换积分", paramType = "query", required = true, dataType = "int" ),

    } )
    @ResponseBody
    @RequestMapping( value = "/jifenExchange", method = RequestMethod.POST )
    public ServerResponse jifenExchange( HttpServletRequest request, HttpServletResponse response, @RequestBody String param ) {
	try {
	    memberApiService.jifenExchange( param );
	    return ServerResponse.createBySuccess();
	} catch ( BusinessException e ) {
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );
	}

    }

    @ApiOperation( value = "统计会员卡总数和今日新增", notes = "统计会员卡总数和今日新增" )
    @ApiImplicitParams( { @ApiImplicitParam( name = "busId", value = "商家id", paramType = "query", required = true, dataType = "int" ),
		     @ApiImplicitParam( name = "shopId", value = "门店id", paramType = "query", required = true, dataType = "int" )
    } )
    @ResponseBody
    @RequestMapping( value = "/countMemberCard", method = RequestMethod.POST )
    public ServerResponse countMemberCard(HttpServletRequest request, HttpServletResponse response, @RequestBody String param ){

	try {
	    Map< String,Object > requestBody = JSONObject.parseObject( param );
	    Map<String,Object> map = memberApiService.coutMemberCard( CommonUtil.toInteger( requestBody.get( "busId" ) ) ,CommonUtil.toInteger( requestBody.get( "shopId" ) ));
	    return ServerResponse.createBySuccess( map );
	} catch ( Exception e ) {
	    return ServerResponse.createByError( ResponseEnums.ERROR.getMsg());
	}
    }


    @ApiOperation( value = "查询商家发布的会员卡信息", notes = "查询商家发布的会员卡信息" )
    @ApiImplicitParams( { @ApiImplicitParam( name = "busId", value = "商家id", paramType = "query", required = true, dataType = "int" ),
    } )
    @ResponseBody
    @RequestMapping( value = "/findGradeTypeBybusId", method = RequestMethod.POST )
    public ServerResponse findGradeTypeBybusId( HttpServletRequest request, HttpServletResponse response, @RequestBody String param ) {
	try {
	    Map< String,Object > requestBody = JSONObject.parseObject( param );
	    List< Map< String,Object > > list = memberApiService.findGradeTypeBybusId( CommonUtil.toInteger( requestBody.get( "busId" ) ) );
	    return ServerResponse.createBySuccess( list );
	} catch ( Exception e ) {
	    return ServerResponse.createByError( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getMsg() );
	}
    }

    @ApiOperation( value = "findMemberPage", notes = "分页查询会员信息" )
    @ApiImplicitParams( { @ApiImplicitParam( name = "pageSize", value = "每页条数", paramType = "query", required = true, dataType = "int" ),
		    @ApiImplicitParam( name = "curPage", value = "当前页", paramType = "query", required = true, dataType = "int" ),
		    @ApiImplicitParam( name = "cardNo", value = "卡号", paramType = "query", required = true, dataType = "string" ),
		    @ApiImplicitParam( name = "phone", value = "手机号", paramType = "query", required = true, dataType = "string" ),
		    @ApiImplicitParam( name = "ctId", value = "会员卡类型", paramType = "query", required = true, dataType = "int" ),
		    @ApiImplicitParam( name = "startDate", value = "开始时间", paramType = "query", required = true, dataType = "string" ),
		    @ApiImplicitParam( name = "endDate", value = "结束时间", paramType = "query", required = true, dataType = "string" )

    } )
    @ResponseBody
    @RequestMapping( value = "/findMemberPage", method = RequestMethod.POST )
    public ServerResponse findMemberPage( HttpServletRequest request, HttpServletResponse response, @RequestBody String param ) {
	try {
	    Page page = memberApiService.findMemberPage( param );
	    return ServerResponse.createBySuccess( page );
	} catch ( BusinessException e ) {
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );
	}
    }

    @ApiOperation( value = "loginMemberByPhone", notes = "手机号+验证码登录,验证码不足校验处理" )
    @ApiImplicitParams( { @ApiImplicitParam( name = "phone", value = "手机号码", paramType = "query", required = true, dataType = "int" ),
		    @ApiImplicitParam( name = "busId", value = "商家id", paramType = "query", required = true, dataType = "int" )
    } )
    @ResponseBody
    @RequestMapping( value = "/loginMemberByPhone", method = RequestMethod.POST )
    public ServerResponse loginMemberByPhone(HttpServletRequest request, HttpServletResponse response, @RequestBody String param){
	try {
	    Map< String,Object > requestBody = JSONObject.parseObject( param );
	    Integer busId=CommonUtil.toInteger( requestBody.get("busId") );
	    String phone=CommonUtil.toString( requestBody.get( "phone" ) );
	    memberApiService.loginMemberByPhone(request,busId, phone );
	    return ServerResponse.createBySuccess( );
	} catch ( BusinessException e ) {
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );
	}
    }

    @ApiOperation( value = "查询会员卡最高等级", notes = "查询会员卡最高等级" )
    @ApiImplicitParam( name = "busId", value = "商家id", paramType = "query", required = true, dataType = "int" )
    @ResponseBody
    @RequestMapping( value = "/findCardDengji", method = RequestMethod.POST )
    public ServerResponse findCardDengji(HttpServletRequest request, HttpServletResponse response, @RequestBody String param){

	try {
	    Map< String,Object > requestBody = JSONObject.parseObject( param );
	    Integer busId=CommonUtil.toInteger( requestBody.get("busId") );
	    Integer dengji=memberApiService.findCardDengji(busId);
	    return ServerResponse.createBySuccess(dengji );
	} catch ( Exception e ) {
	    LOG.error( "查询会员卡最高等级异常",e );
	    return ServerResponse.createByError( );
	}
    }

}