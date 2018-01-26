package com.gt.member.controller.member_pc;

import com.gt.api.bean.session.Member;
import com.gt.api.enums.ResponseEnums;
import com.gt.api.util.SessionUtils;
import com.gt.bean.vo.DuofenCardNewVO;
import com.gt.member.dto.ServerResponse;
import com.gt.member.exception.BusinessException;
import com.gt.member.service.common.membercard.MemberCommonService;
import com.gt.member.service.member.DuofenCardNewService;
import com.gt.member.service.member.DuofencardAuthorizationService;
import com.gt.member.util.Page;
import com.gt.member.util.QRcodeKit;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author zhanbing
 * @since 2018年1月17日 16:03:23
 */
@Api( value = "DuofenCardController", description = "多粉优惠券" )
@Controller
@CrossOrigin
@RequestMapping( "/memberPc/duofenCoupon" )
public class DuofenCardController {
    private static final Logger log = LoggerFactory.getLogger( DuofenCardController.class );

    @Autowired
    private DuofenCardNewService duofenCardNewService;

    @Autowired
    private MemberCommonService  memberCommonService;

    @Autowired
    private DuofencardAuthorizationService authorizationService;

    @ApiOperation( value = "新增优惠券", notes = "新增优惠券" )
    @ResponseBody
    @RequestMapping( value = "/addCoupon", method = RequestMethod.POST )
    //@PostMapping(value="add",produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ServerResponse addCoupon( HttpServletRequest request, HttpServletResponse response,@RequestBody @ModelAttribute DuofenCardNewVO coupon ) {
	try {
	    Integer busId = SessionUtils.getPidBusId( request );
	    Integer createBusId = SessionUtils.getLoginUser( request ).getId();

	    coupon.setBusId( busId );
	    coupon.setCreateBusId( createBusId );

	    // 自动审核
	    boolean auditFlag = memberCommonService.getAutoAuditFlag( busId );
	    if ( auditFlag ) {
		coupon.setCardStatus( 2 );
	    }

	    duofenCardNewService.addCoupon( coupon );
	    return ServerResponse.createBySuccess();
	} catch ( BusinessException e ) {
	    log.error( "新增优惠券异常：", e );
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );
	}
    }

    @ApiOperation( value = "修改优惠券", notes = "修改优惠券" )
    @ResponseBody
    @RequestMapping( value = "/updateCouponById", method = RequestMethod.POST )
    public ServerResponse updateCouponById( HttpServletRequest request, HttpServletResponse response, @RequestBody @ModelAttribute DuofenCardNewVO coupon ) {
	try {
	    if(coupon.getId()==null){
		return ServerResponse.createByError( "修改优惠券不存在" );
	    }
	    duofenCardNewService.updateCouponById( coupon );
	    return ServerResponse.createBySuccess(  );
	} catch ( BusinessException e ) {
	    log.error( "优惠券修改异常", e );
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );
	}
    }

    @ApiOperation( value = "优惠券信息", notes = "优惠券信息" )
    @ResponseBody
    @RequestMapping( value = "/findCouponById",method ={RequestMethod.POST,RequestMethod.GET} )
    public ServerResponse findCouponById( HttpServletRequest request, HttpServletResponse response, @RequestParam Integer id ) {
	try {
	     Map< String,Object >  coupon = duofenCardNewService.findCouponById( id );
	    return ServerResponse.createBySuccess( coupon );
	} catch ( Exception e ) {
	    log.error( "优惠券查询异常", e );
	    return ServerResponse.createByError( ResponseEnums.ERROR.getCode(), "优惠券查询异常" );
	}
    }

    @ApiOperation( value = "优惠券详情", notes = "优惠券详情" )
    @ResponseBody
    @RequestMapping( value = "/findCouponDetail" ,method ={RequestMethod.POST,RequestMethod.GET})
    public ServerResponse findCouponDetail( HttpServletRequest request, HttpServletResponse response, @RequestParam Integer id ) {
	try {
	    Map< String,Object >  coupon = duofenCardNewService.findCouponDetail( id );
	    return ServerResponse.createBySuccess( coupon );
	} catch ( Exception e ) {
	    log.error( "优惠券查询异常", e );
	    return ServerResponse.createByError( ResponseEnums.ERROR.getCode(), "优惠券查询异常" );
	}
    }

  /* 获取会员已领取优惠券数量 receiveRule 领取规则 0=每人每天最多领取 1=每人最多领取
    @ResponseBody
    @RequestMapping( value = "/getReceiveQuantity", method = RequestMethod.GET )
    public ServerResponse getReceiveQuantity( HttpServletRequest request, HttpServletResponse response, @RequestParam Integer userId, @RequestParam Integer couponId,
		    @RequestParam Integer receiveRule ) {
	Integer receiveQuantity = duofenCardNewService.getReceiveQuantity( userId, couponId, receiveRule );
	return ServerResponse.createBySuccess( receiveQuantity );
    }*/

    @ApiOperation( value = "优惠券已领取数量  ", notes = "优惠券已领取数量  " )
    @ResponseBody
    @RequestMapping( value = "/getCouponReceiveQuantity", method = RequestMethod.GET )
    public ServerResponse getReceiveQuantity( HttpServletRequest request, HttpServletResponse response,  @RequestParam Integer id ) {
	Integer receiveQuantity = duofenCardNewService.getCouponReceiveQuantity(id );
	return ServerResponse.createBySuccess( receiveQuantity );
    }


    @ApiOperation( value = "获取优惠券列表", notes = "获取优惠券列表" )
    @ResponseBody
    @RequestMapping( value = "/getCouponListByBusId",method ={RequestMethod.POST,RequestMethod.GET} )
    public ServerResponse getCouponListByBusId( HttpServletRequest request, HttpServletResponse response, @RequestParam(defaultValue ="1" ) Integer curPage, @RequestParam(defaultValue ="10" ) Integer pageSize,Integer cardStatus, String couponName, Integer useType, Integer busId ) {
	try {
	    if ( busId == null ) {
		busId = SessionUtils.getPidBusId( request );
	    }
	    Page page = duofenCardNewService.getCouponListByBusId(  curPage, pageSize,busId, cardStatus, couponName, useType );
	    return ServerResponse.createBySuccess( page );
	} catch ( BusinessException e ) {
	    log.error( "获取优惠券异常", e );
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );
	}
    }

    @ApiOperation( value = "优惠券领取列表", notes = "优惠券领取列表" )
    @ResponseBody
    @RequestMapping( value = "/getReceiveCouponListById",method ={RequestMethod.POST,RequestMethod.GET} )
    public ServerResponse getReceiveCouponListById( HttpServletRequest request, HttpServletResponse response, @RequestParam(defaultValue ="1" ) Integer curPage, @RequestParam(defaultValue ="10" ) Integer pageSize,
		    @RequestParam Integer couponId, String searchContent ) {
        try {
	    Page page = duofenCardNewService.getReceiveCouponListById( curPage, pageSize, couponId, searchContent );
	    return ServerResponse.createBySuccess( page );
	} catch ( BusinessException e ) {
	    log.error( "获取领取列表异常", e );
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );
	}
    }

    @ApiOperation( value = "优惠券购买详情", notes = "优惠券购买详情" )
    @ResponseBody
    @RequestMapping( value = "/getPaymentDetailById",method ={RequestMethod.POST,RequestMethod.GET} )
    public ServerResponse getPaymentDetailById( HttpServletRequest request, HttpServletResponse response, @RequestParam Integer id ) {
	try {
	    Map<String,Object> map = duofenCardNewService.getPaymentDetailById( id );
	    return ServerResponse.createBySuccess( map );
	} catch ( BusinessException e ) {
	    log.error( "查询购买详情异常", e );
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );
	}
    }

    @ApiOperation( value = "删除核销人员", notes = "删除核销人员" )
    @ResponseBody
    @RequestMapping( value = "/authorization/deleteById",method ={RequestMethod.POST,RequestMethod.GET} )
    public ServerResponse deleteById( HttpServletRequest request, HttpServletResponse response, @RequestParam Integer id ) {
	authorizationService.deleteById( id );
	return ServerResponse.createBySuccess();
    }

    @ApiOperation( value = "核销人员列表", notes = "核销人员列表" )
    @ResponseBody
    @RequestMapping( value = "/authorization/getAuthorizationUser",method ={RequestMethod.POST,RequestMethod.GET} )
    public ServerResponse getAuthorizationUser( HttpServletRequest request, HttpServletResponse response, @RequestParam(defaultValue ="1" ) Integer curPage, @RequestParam(defaultValue ="10" ) Integer pageSize ) {
	Integer busId = SessionUtils.getPidBusId( request );
	Page page = authorizationService.getAuthorizationUser( curPage, pageSize, busId );
	return ServerResponse.createBySuccess( page );
    }

  /*  @ApiOperation( value = "增加核销用户", notes = "增加核销用户" )
    @ResponseBody
    @RequestMapping( value = "/authorization/authorizationUser", method = RequestMethod.POST )
    public ServerResponse authorizationUser( HttpServletRequest request, HttpServletResponse response, @RequestParam String json ) {
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
	    DuofencardAuthorization authorizationInfo = new DuofencardAuthorization();
	    authorizationInfo.setOpenId( member.getOpenid() );
	    authorizationInfo.setStatus( 0 );
	    authorizationInfo.setShopId( CommonUtil.toInteger( params.get( "shopId" ) ) );
	    authorizationInfo.setBusId( busId );
	    authorizationInfo.setMemberId( member.getId() );
	    authorizationService.insert( authorizationInfo );
	    return ServerResponse.createBySuccess();
	} catch ( BusinessException e ) {
	    LOG.error( "增加核销用户异常", e );
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    return ServerResponse.createByError( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getMsg() );
	}
    }*/




    @ApiOperation( value = "生成二维码", notes = "生成二维码" )
    @ResponseBody
    @RequestMapping( value = "/qrCode",method ={RequestMethod.POST,RequestMethod.GET})
    public void qrCode( HttpServletRequest request, HttpServletResponse response, String content, Integer width, Integer height ) {
	QRcodeKit.buildQRcode( content, width, height, response );
    }

}
