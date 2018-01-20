package com.gt.member.controller.member_pc;

import com.alibaba.fastjson.JSON;
import com.gt.api.bean.session.Member;
import com.gt.api.enums.ResponseEnums;
import com.gt.api.util.SessionUtils;
import com.gt.bean.vo.DuofenCardNewVO;
import com.gt.member.controller.RemoteAuthori.AuthorizeOrLoginController;
import com.gt.member.dto.ServerResponse;
import com.gt.member.entity.DuofencardAuthorization;
import com.gt.member.enums.ResponseMemberEnums;
import com.gt.member.exception.BusinessException;
import com.gt.member.service.common.dict.DictService;
import com.gt.member.service.member.DuofenCardNewService;
import com.gt.member.service.member.DuofencardAuthorizationService;
import com.gt.member.util.CommonUtil;
import com.gt.member.util.Page;
import com.gt.member.util.QRcodeKit;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
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
@Slf4j
public class DuofenCardController extends AuthorizeOrLoginController {
    private static final Logger LOG = LoggerFactory.getLogger( DuofenCardController.class );

    @Autowired
    private DuofenCardNewService duofenCardNewService;

    @Autowired
    private DictService dictService;

    @Autowired
    private DuofencardAuthorizationService authorizationService;

    @ApiOperation( value = "新增优惠券", notes = "新增优惠券" )
    @ResponseBody
    @RequestMapping( value = "/addCoupon", method = RequestMethod.POST )
    public ServerResponse addCoupon( HttpServletRequest request, HttpServletResponse response, @RequestBody DuofenCardNewVO coupon ) {
	try {
	    Integer busId = SessionUtils.getPidBusId( request );
	    //Member member = SessionUtils.getLoginMember( request, busId );
	    Member member =new Member();
	    member.setId(35);

	    coupon.setBusId( busId );
	    coupon.setCreateBusId( member.getId() );

	    // 自动审核
	    boolean auditFlag = dictService.getAutoAuditFlag( busId );
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
    public ServerResponse updateCouponById( HttpServletRequest request, HttpServletResponse response, @RequestBody DuofenCardNewVO coupon ) {
	try {
	    duofenCardNewService.updateCouponById( coupon );
	    return ServerResponse.createBySuccess(  );
	} catch ( BusinessException e ) {
	    LOG.error( "优惠券修改异常", e );
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );

	}
    }

    @ApiOperation( value = "优惠券详情", notes = "优惠券详情" )
    @ResponseBody
    @RequestMapping( value = "/findCouponById", method = RequestMethod.GET )
    public ServerResponse findCouponById( HttpServletRequest request, HttpServletResponse response, @RequestParam Integer id ) {
	try {
	    List< Map< String,Object > > coupon = duofenCardNewService.selectById( id );
	    return ServerResponse.createBySuccess( coupon );
	} catch ( Exception e ) {
	    LOG.error( "优惠券查询异常", e );
	    return ServerResponse.createByError( ResponseEnums.ERROR.getCode(), "优惠券查询异常" );
	}
    }

    @ApiOperation( value = "获取优惠券列表", notes = "获取优惠券列表" )
    @ResponseBody
    @RequestMapping( value = "/getCouponListByBusId", method = RequestMethod.GET )
    public ServerResponse getCouponListByBusId( HttpServletRequest request, HttpServletResponse response, @RequestParam Integer curPage, @RequestParam Integer pageSize,Integer cardStatus, String couponName, Integer useType, Integer busId ) {
	try {
	    if ( busId == null ) {
		busId = SessionUtils.getPidBusId( request );
	    }
	    Page page = duofenCardNewService.getCouponListByBusId(  curPage, pageSize,busId, cardStatus, couponName, useType );
	    return ServerResponse.createBySuccess( page );
	} catch ( BusinessException e ) {
	    LOG.error( "获取优惠券异常", e );
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );
	}
    }

    @ApiOperation( value = "优惠券领取列表", notes = "优惠券领取列表" )
    @ResponseBody
    @RequestMapping( value = "/getReceiveCouponListById", method = RequestMethod.GET )
    public ServerResponse getReceiveCouponListById( HttpServletRequest request, HttpServletResponse response, @RequestParam Integer curPage, @RequestParam Integer pageSize,
		    @RequestParam Integer couponId, String searchContent ) {
        try {
	    Page page = duofenCardNewService.getReceiveCouponListById( curPage, pageSize, couponId, searchContent );
	    return ServerResponse.createBySuccess( page );
	} catch ( BusinessException e ) {
	    LOG.error( "获取领取列表异常", e );
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );
	}
    }

    @ApiOperation( value = "删除核销人员", notes = "删除核销人员" )
    @ResponseBody
    @RequestMapping( value = "/authorization/deleteById", method = RequestMethod.GET )
    public ServerResponse deleteById( HttpServletRequest request, HttpServletResponse response, @RequestParam Integer id ) {
	authorizationService.deleteById( id );
	return ServerResponse.createBySuccess();
    }

    @ApiOperation( value = "核销人员列表", notes = "核销人员列表" )
    @ResponseBody
    @RequestMapping( value = "/authorization/getAuthorizationUser", method = RequestMethod.GET )
    public ServerResponse getAuthorizationUser( HttpServletRequest request, HttpServletResponse response, @RequestParam Integer curPage, @RequestParam Integer pageSize ) {
	Integer busId = SessionUtils.getPidBusId( request );
	Page page = authorizationService.getAuthorizationUser( curPage, pageSize, busId );
	return ServerResponse.createBySuccess( page );
    }

    @ApiOperation( value = "增加核销用户", notes = "增加核销用户" )
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
    }

    @ApiOperation( value = "生成二维码", notes = "生成二维码" )
    @ResponseBody
    @RequestMapping( value = "/qrCode", method = RequestMethod.GET )
    public void qrCode( HttpServletRequest request, HttpServletResponse response, String content, Integer width, Integer height ) {
	QRcodeKit.buildQRcode( content, width, height, response );
    }

}
