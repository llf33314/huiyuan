package com.gt.member.controller;

import com.gt.member.base.BaseController;
import com.gt.member.dto.ServerResponse;
import com.gt.member.entity.Member;
import com.gt.member.entity.MemberCard;
import com.gt.member.entity.MemberGradetype;
import com.gt.member.enums.ResponseEnums;
import com.gt.member.exception.BusinessException;
import com.gt.member.service.memberApi.MemberApiService;
import com.gt.member.util.CommonUtil;
import com.gt.member.util.MemberConfig;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 会员httpservice 接口
 *
 * @author Administrator
 */
@Controller
@RequestMapping( "/memberAPI/member" )
public class MemberApiController extends BaseController {

    private static final Logger LOG = Logger.getLogger( MemberApiController.class );

    @Autowired
    private MemberConfig memberConfig;

    @Autowired
    private MemberApiService memberApiService;

    @ApiOperation( value = "根据memberId和门店查询会员数据 返回数据包含会员信息、微信卡券、多粉卡券", notes = "根据memberId和门店查询会员数据" )
    @ApiImplicitParams( { @ApiImplicitParam( name = "memberId", value = "卡号或手机号", paramType = "query", required = true, dataType = "int" ),
		    @ApiImplicitParam( name = "shopId", value = "门店id", paramType = "query", required = true, dataType = "int" ) } )
    @ResponseBody
    @RequestMapping( value = "/findCardByMembeId", method = RequestMethod.POST )
    public ServerResponse findCardByMembeId( HttpServletRequest request, HttpServletResponse response, @RequestBody Map requestBody ) {
	try {
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
    public ServerResponse findMemberCard( HttpServletRequest request, HttpServletResponse response, @RequestBody Map requestBody ) {
	try {
	    String cardNo = CommonUtil.toString( requestBody.get( "cardNo" ) );
	    Integer busId = CommonUtil.toInteger( requestBody.get( "busId" ) );
	    Integer shopId = CommonUtil.toInteger( requestBody.get( "shopId" ) );
	    String cardNoKey = memberConfig.getCardNoKey();
	    Map< String,Object > map = memberApiService.findMemberCard( busId, cardNoKey, cardNo, shopId );
	    return ServerResponse.createBySuccess( map );
	} catch ( BusinessException e ) {
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    return ServerResponse.createByError( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getDesc() );
	}
    }

    @ApiOperation( value = "根据粉丝id获取粉丝信息", notes = "获取粉丝信息" )
    @ApiImplicitParam( name = "memberId", value = "粉丝id", paramType = "query", required = true, dataType = "int" )
    @ResponseBody
    @RequestMapping( value = "/findByMemberId", method = RequestMethod.POST )
    public ServerResponse findByMemberId( HttpServletRequest request, HttpServletResponse response, @RequestBody Map requestBody ) {
	try {
	    Integer memberId = CommonUtil.toInteger( requestBody.get( "memberId" ) );
	    Member member = memberApiService.findByMemberId( memberId );
	    return ServerResponse.createBySuccess( member );
	} catch ( BusinessException e ) {
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );
	}
    }

    @ApiOperation( value = "判断储值卡金额是否充足", notes = "判断储值卡金额是否充足" )
    @ApiImplicitParams( { @ApiImplicitParam( name = "memberId", value = "粉丝id", paramType = "query", required = true, dataType = "int" ),
		    @ApiImplicitParam( name = "money", value = "消费金额", paramType = "query", required = true, dataType = "int" ), } )
    @ResponseBody
    @RequestMapping( value = "/isAdequateMoney", method = RequestMethod.POST )
    public ServerResponse isAdequateMoney( HttpServletRequest request, HttpServletResponse response, @RequestBody Map requestBody ) {
	try {
	    Integer memberId = CommonUtil.toInteger( requestBody.get( "memberId" ) );
	    Double money = CommonUtil.toDouble( requestBody.get( "money" ) );
	    memberApiService.isAdequateMoney( memberId, money );
	    return ServerResponse.createBySuccess();
	} catch ( BusinessException e ) {
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );
	}
    }

    @ApiOperation( value = "根据粉丝id获取会员折扣", notes = "根据粉丝id获取会员折扣" )
    @ApiImplicitParam( name = "memberId", value = "粉丝id", paramType = "query", required = true, dataType = "int" )
    @ResponseBody
    @RequestMapping( value = "/findCardTypeReturnDiscount", method = RequestMethod.POST )
    public ServerResponse findCardTypeReturnDiscount( HttpServletRequest request, HttpServletResponse response, @RequestBody Map requestBody ) {
	try {
	    Integer memberId = CommonUtil.toInteger( requestBody.get( "memberId" ) );
	    Double discount = memberApiService.findCardTypeReturnDiscount( memberId );
	    return ServerResponse.createBySuccess( discount );
	} catch ( BusinessException e ) {
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );
	}
    }

    @ApiOperation( value = "小程序绑定手机号码", notes = "小程序绑定手机号码 返回member对象数据" )
    @ApiImplicitParams( { @ApiImplicitParam( name = "memberId", value = "粉丝id", paramType = "query", required = true, dataType = "int" ),
		    @ApiImplicitParam( name = "code", value = "短信校验码", paramType = "query", required = true, dataType = "String" ),
		    @ApiImplicitParam( name = "phone", value = "手机号码", paramType = "query", required = true, dataType = "String" ),
		    @ApiImplicitParam( name = "busId", value = "商家id", paramType = "query", required = true, dataType = "int" ) } )
    @ResponseBody
    @RequestMapping( value = "/bingdingPhone", method = RequestMethod.POST )
    public ServerResponse bingdingPhone( HttpServletRequest request, HttpServletResponse response, @RequestBody Map requestBody ) {
	try {
	    Integer memberId = CommonUtil.toInteger( requestBody.get( "memberId" ) );
	    Integer busId = CommonUtil.toInteger( requestBody.get( "busId" ) );
	    String phone = CommonUtil.toString( requestBody.get( "phone" ) );
	    String code = CommonUtil.toString( requestBody.get( "code" ) );
	    Member member = memberApiService.bingdingPhone( memberId, phone, code, busId );
	    return ServerResponse.createBySuccess( member );
	} catch ( BusinessException e ) {
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );
	}
    }



    @ApiOperation( value = "统计会员数量", notes = "根据商家id统计会员数量" )
    @ApiImplicitParam( name = "busId", value = "商家id", paramType = "query", required = true, dataType = "int" )
    @ResponseBody
    @RequestMapping( value = "/countMember", method = RequestMethod.POST )
    public ServerResponse countMember( HttpServletRequest request, HttpServletResponse response, @RequestBody Map requestBody ) {
	try {
	    Integer busId = CommonUtil.toInteger( requestBody.get( "busId" ) );
	    Map< String,Object > countMember = memberApiService.countMember( busId );
	    return ServerResponse.createBySuccess( countMember );
	} catch ( Exception e ) {
	    logger.error( "统计会员异常", e );
	    return ServerResponse.createByError( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getDesc() );
	}
    }

    @ApiOperation( value = "查询根据ids查询粉丝信息", notes = "分页查询根据ids查询粉丝信息" )
    @ApiImplicitParams( { @ApiImplicitParam( name = "busId", value = "商家id", paramType = "query", required = true, dataType = "int" ),
		    @ApiImplicitParam( name = "ids", value = "粉丝id字符集合 逗号隔开", paramType = "query", required = true, dataType = "String" ) } )
    @ResponseBody
    @RequestMapping( value = "/findMemberByids", method = RequestMethod.POST )
    public ServerResponse findMemberByids( HttpServletRequest request, HttpServletResponse response, @RequestBody Map requestBody ) {
	try {
	    Integer busId = CommonUtil.toInteger( requestBody.get( "busId" ) );
	    String ids = CommonUtil.toString( requestBody.get( "ids" ) );
	    List< Map< String,Object > > mapList = memberApiService.findMemberByIds( busId, ids );
	    return ServerResponse.createBySuccess( mapList );
	} catch ( Exception e ) {
	    logger.error( "查询根据ids查询粉丝信息异常", e );
	    return ServerResponse.createByError( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getDesc() );
	}
    }

    @ApiOperation( value = "根据id查询粉丝信息id集合", notes = "根据id查询粉丝信息id集合" )
    @ApiImplicitParam( name = "memberId", value = "粉丝id", paramType = "query", required = true, dataType = "int" )
    @ResponseBody
    @RequestMapping( value = "/findMemberIdsByid", method = RequestMethod.POST )
    public ServerResponse findMemberIdsByid( HttpServletRequest request, HttpServletResponse response, @RequestBody Map requestBody ) {
	try {
	    Integer memberId = CommonUtil.toInteger( requestBody.get( "memberId" ) );
	    List< Integer > mapList = memberApiService.findMemberIds( memberId );
	    return ServerResponse.createBySuccess( mapList );
	} catch ( Exception e ) {
	    logger.error( "根据id查询粉丝信息id集合异常", e );
	    return ServerResponse.createByError( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getDesc() );
	}
    }

    @ApiOperation( value = "判断粉丝是否是会员", notes = "判断粉丝是否是会员" )
    @ApiImplicitParam( name = "memberId", value = "", paramType = "query", required = true, dataType = "int" )
    @ResponseBody
    @RequestMapping( value = "/isMember", method = RequestMethod.POST )
    public ServerResponse isMember( HttpServletRequest request, HttpServletResponse response, @RequestBody Map requestBody ) {
	try {
	    Integer memberId = CommonUtil.toInteger( requestBody.get( "memberId" ) );
	    memberApiService.isMemember( memberId );
	    return ServerResponse.createBySuccess();
	} catch ( BusinessException e ) {
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    logger.error( "判断粉丝是否是会员异常", e );
	    return ServerResponse.createByError( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getDesc() );
	}
    }

    @ApiOperation( value = "商场关注赠送积分", notes = "商场关注赠送积分" )
    @ApiImplicitParams( { @ApiImplicitParam( name = "memberId", value = "", paramType = "query", required = true, dataType = "int" ),
		    @ApiImplicitParam( name = "jifen", value = "", paramType = "query", required = true, dataType = "int" ), } )
    @ResponseBody
    @RequestMapping( value = "/updateJifen", method = RequestMethod.POST )
    public ServerResponse updateJifen( HttpServletRequest request, HttpServletResponse response, @RequestBody Map requestBody ) {
	try {
	    Integer memberId = CommonUtil.toInteger( requestBody.get( "memberId" ) );
	    Integer jifen = CommonUtil.toInteger( requestBody.get( "jifen" ) );
	    memberApiService.updateJifen( memberId, jifen );
	    return ServerResponse.createBySuccess();
	} catch ( Exception e ) {
	    logger.error( "商场关注赠送积分异常", e );
	    return ServerResponse.createByError( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getDesc() );
	}
    }

    @ApiOperation( value = "会员卡类型", notes = "会员卡类型" )
    @ApiImplicitParam( name = "memberId", value = "", paramType = "query", required = true, dataType = "int" )
    @ResponseBody
    @RequestMapping( value = "/isCardType", method = RequestMethod.POST )
    public ServerResponse isCardType( HttpServletRequest request, HttpServletResponse response, @RequestBody Map requestBody ) {
	try {
	    Integer memberId = CommonUtil.toInteger( requestBody.get( "memberId" ) );
	    Integer ctId = memberApiService.isCardType( memberId );
	    return ServerResponse.createBySuccess( ctId );
	}catch ( BusinessException e ){
	    return ServerResponse.createByError(e.getCode(),e.getMessage());
	}catch ( Exception e ) {
	    logger.error( "会员卡类型异常", e );
	    return ServerResponse.createByError( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getDesc() );
	}
    }

    @ApiOperation( value = "查询购买的会员卡模板", notes = "查询购买的会员卡模板" )
    @ApiImplicitParam( name = "busId", value = "", paramType = "query", required = true, dataType = "int" )
    @ResponseBody
    @RequestMapping( value = "/findBuyGradeType", method = RequestMethod.POST )
    public ServerResponse findBuyGradeType(HttpServletRequest request, HttpServletResponse response, @RequestBody Map requestBody ){
	try {
	    Integer busId = CommonUtil.toInteger( requestBody.get( "busId" ) );
	    List<Map<String,Object>> maplist = memberApiService.findBuyGradeType( busId );
	    return ServerResponse.createBySuccess( maplist );
	}catch ( Exception e ) {
	    logger.error( "查询购买的会员卡模板异常", e );
	    return ServerResponse.createByError( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getDesc() );
	}
    }

    @ApiOperation( value = "查询会员卡片名称", notes = "查询会员卡片名称" )
    @ApiImplicitParam( name = "memberId", value = "", paramType = "query", required = true, dataType = "int" )
    @ResponseBody
    @RequestMapping( value = "/findGradeType", method = RequestMethod.POST )
    public ServerResponse findGradeType(HttpServletRequest request, HttpServletResponse response, @RequestBody Map requestBody ){
	try {
	    Integer memberId = CommonUtil.toInteger( requestBody.get( "memberId" ) );
	    MemberGradetype gradeType = memberApiService.findGradeType( memberId );
	    return ServerResponse.createBySuccess( gradeType );
	}catch ( Exception e ) {
	    logger.error( "查询会员卡片名称异常", e );
	    return ServerResponse.createByError( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getDesc() );
	}
    }

    @ApiOperation( value = "商场修改订单状态", notes = "商场修改订单状态" )
    @ApiImplicitParams({
		    @ApiImplicitParam( name = "orderNo", value = "订单号", paramType = "query", required = true, dataType = "Striing" ),
		    @ApiImplicitParam( name = "payType", value = "支付方式", paramType = "query", required = true, dataType = "int" ),
		    @ApiImplicitParam( name = "payStatus", value = "支付状态", paramType = "query", required = true, dataType = "int" )
    })
    @ResponseBody
    @RequestMapping( value = "/updateUserConsume", method = RequestMethod.POST )
    public ServerResponse updateUserConsume(HttpServletRequest request, HttpServletResponse response, @RequestBody Map requestBody){
	try {
	    String orderNo = CommonUtil.toString( requestBody.get( "orderNo" ) );
	    Integer payType = CommonUtil.toInteger( requestBody.get( "payType" ) );
	    Integer payStatus = CommonUtil.toInteger( requestBody.get( "payStatus" ) );
	    memberApiService.updateUserConsume(orderNo,payType,payStatus);
	    return ServerResponse.createBySuccess( );
	}catch ( BusinessException e ){
	    return ServerResponse.createByError( e.getCode(),e.getMessage() );
	}catch ( Exception e ) {
	    logger.error( "查询会员卡片名称异常", e );
	    return ServerResponse.createByError( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getDesc() );
	}
    }


    @ApiOperation( value = "退款包括了储值卡退款(不包括积分和粉币)", notes = "退款包括了储值卡退款" )
    @ApiImplicitParams( { @ApiImplicitParam( name = "busId", value = "商家id", paramType = "query", required = true, dataType = "String" ),
		    @ApiImplicitParam( name = "orderNo", value = "订单号", paramType = "query", required = true, dataType = "String" ),
		    @ApiImplicitParam( name = "money", value = "退款金额", paramType = "query", required = true, dataType = "double" ) } )
    @ResponseBody
    @RequestMapping( value = "/refundMoney", method = RequestMethod.POST )
    public ServerResponse refundMoney( HttpServletRequest request, HttpServletResponse response, @RequestBody Map requestBody ) {
	try {
	    Integer busId = CommonUtil.toInteger( requestBody.get( "busId" ) );
	    String orderNo = CommonUtil.toString( requestBody.get( "orderNo" ) );
	    Double money = CommonUtil.toDouble( requestBody.get( "money" ) );
	    memberApiService.refundMoney( busId, orderNo, money );
	    return ServerResponse.createBySuccess();
	} catch ( BusinessException e ) {
	    return ServerResponse.createByError( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getDesc() );
	}
    }


    @ApiOperation( value = "退款包括了储值卡退款(包括积分和粉币)", notes = "退款包括了储值卡退款" )
    @ApiImplicitParams( { @ApiImplicitParam( name = "busId", value = "商家id", paramType = "query", required = true, dataType = "int" ),
		    @ApiImplicitParam( name = "orderNo", value = "订单号", paramType = "query", required = true, dataType = "String" ),
		    @ApiImplicitParam( name = "money", value = "退款金额", paramType = "query", required = true, dataType = "double" ),
		    @ApiImplicitParam( name = "fenbi", value = "粉币", paramType = "query", required = true, dataType = "double" ),
		    @ApiImplicitParam( name = "jifen", value = "积分", paramType = "query", required = true, dataType = "int" ),
    } )
    @ResponseBody
    @RequestMapping( value = "/refundMoneyAndJifenAndFenbi", method = RequestMethod.POST )
    public ServerResponse refundMoneyAndJifenAndFenbi(HttpServletRequest request, HttpServletResponse response, @RequestBody Map requestBody ){
	try {
	    memberApiService.refundMoneyAndJifenAndFenbi( requestBody );
	    return ServerResponse.createBySuccess();
	} catch ( BusinessException e ) {
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );
	}
    }

    @ApiOperation( value = "（商城）查询会员积分记录", notes = "（商城）查询会员积分记录" )
    @ApiImplicitParams({
		    @ApiImplicitParam( name = "mcId", value = "商家id", paramType = "query", required = true, dataType = "int" ),
		    @ApiImplicitParam( name = "page", value = "页数", paramType = "query", required = true, dataType = "int" ),
		    @ApiImplicitParam( name = "pageSize", value = "条数", paramType = "query", required = true, dataType = "int" )
    })

    @ResponseBody
    @RequestMapping( value = "/findCardrecord", method = RequestMethod.POST )
    public ServerResponse findCardrecord(HttpServletRequest request, HttpServletResponse response, @RequestBody Map requestBody ){
	try {
	    Integer mcId=CommonUtil.toInteger( requestBody.get( "mcId" ) );
	    Integer page=CommonUtil.toInteger( requestBody.get( "page" ) );
	    Integer pageSize=CommonUtil.toInteger( requestBody.get( "pageSize" ) );
	    List<Map<String,Object>> listMap=  memberApiService.findCardrecord( mcId,page,pageSize );
	    return ServerResponse.createBySuccess(listMap);
	} catch ( BusinessException e ) {
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );
	}catch ( Exception e ) {
	    logger.error( "商城）查询会员积分记录异常", e );
	    return ServerResponse.createByError( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getDesc() );
	}
    }

    @ApiOperation( value = "查询会员卡信息", notes = "查询会员卡信息" )
    @ApiImplicitParam( name = "mcId", value = "会员卡id", paramType = "query", required = true, dataType = "int" )
    @ResponseBody
    @RequestMapping( value = "/findMemberCardByMcId", method = RequestMethod.POST )
    public ServerResponse findMemberCardByMcId( HttpServletRequest request, HttpServletResponse response, @RequestBody Map requestBody ) {
	try {
	    Integer mcId = CommonUtil.toInteger( requestBody.get( "mcId" ) );
	    MemberCard card = memberApiService.findMemberCardByMcId( mcId );
	    return ServerResponse.createBySuccess( card );
	} catch ( BusinessException e ) {
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    logger.error( "查询会员卡信息异常", e );
	    return ServerResponse.createByError( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getDesc() );
	}
    }


    @ApiOperation( value = "跨门店 根据memberId和门店集合查询会员数据 返回数据包含会员信息、微信卡券、多粉卡券", notes = "根据memberId和门店集合查询会员数据" )
    @ApiImplicitParams( { @ApiImplicitParam( name = "memberId", value = "卡号或手机号", paramType = "query", required = true, dataType = "int" ),
		    @ApiImplicitParam( name = "shopIds", value = "门店id集合逗号隔开", paramType = "query", required = true, dataType = "int" ) } )
    @ResponseBody
    @RequestMapping( value = "/findCardAndShopIdsByMembeId", method = RequestMethod.POST )
    public ServerResponse findCardAndShopIdsByMembeId( HttpServletRequest request, HttpServletResponse response, @RequestBody Map requestBody ) {
	try {
	    Integer memberId = CommonUtil.toInteger( requestBody.get( "memberId" ) );
	    String shopIds = CommonUtil.toString( requestBody.get( "shopIds" ) );
	    Map< String,Object > map = memberApiService.findMemberCardByMemberIdAndshopIds( memberId, shopIds );
	    return ServerResponse.createBySuccess( map );
	} catch ( BusinessException e ) {
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );
	}
    }

    @ApiOperation( value = "（教育)手机端微信授权后合并单前的登录用户信息", notes = "（教育)手机端微信授权后合并单前的登录用户信息" )
    @ApiImplicitParams( { @ApiImplicitParam( name = "parentId", value = "学员家长pc注册", paramType = "query", required = true, dataType = "int" ),
		    @ApiImplicitParam( name = "wxMemberId", value = "微信授权得到memberId", paramType = "query", required = true, dataType = "int" ) } )
    @ResponseBody
    @RequestMapping( value = "/updateMemberByTeach", method = RequestMethod.POST )
    public ServerResponse updateMemberByTeach( HttpServletRequest request, HttpServletResponse response, @RequestBody Map requestBody){
	try {
	    Map< String,Object > map = memberApiService.updateMemberByTeach(requestBody );
	    return ServerResponse.createBySuccess( map );
	} catch ( BusinessException e ) {
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );
	}

    }

    @ApiOperation( value = "（教育)保存家长信息", notes = "（教育)保存家长信息" )
    @ApiImplicitParams( { @ApiImplicitParam( name = "name", value = "家长姓名", paramType = "query", required = true, dataType = "String" ),
		    @ApiImplicitParam( name = "phone", value = "手机号码", paramType = "query", required = true, dataType = "String" ),
		    @ApiImplicitParam( name = "busId", value = "商家id", paramType = "query", required = true, dataType = "int" )} )
    @ResponseBody
    @RequestMapping( value = "/saveMemberByTeach", method = RequestMethod.POST )
    public ServerResponse saveMemberByTeach(HttpServletRequest request, HttpServletResponse response, @RequestBody List<Map<String,Object>> requestBody){
	try {
	    List<Map< String,Object >> mapList = memberApiService.saveMemberByTeach(requestBody );
	    return ServerResponse.createBySuccess( mapList );
	} catch ( BusinessException e ) {
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );
	}
    }

}
