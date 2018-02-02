package com.gt.member.controller;

import com.alibaba.fastjson.JSONObject;
import com.gt.bean.vo.DuofenCardNewVO;
import com.gt.duofencard.entity.DuofenCardNew;
import com.gt.member.base.BaseController;
import com.gt.member.dto.ServerResponse;
import com.gt.member.exception.BusinessException;
import com.gt.member.service.member.DuofenCardNewService;
import com.gt.member.util.CommonUtil;
import com.gt.member.util.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

/**
 * 优惠券接口
 *
 * @author zhanbing 2018年1月23日 14:23:48
 */
@Api( value = "优惠券接口", description = "优惠券接口" )
@Controller
@RequestMapping( "/memberAPI/coupon/" )
public class CouponApiController extends BaseController {

    private static final Logger LOG = Logger.getLogger( CouponApiController.class );

    @Autowired
    private DuofenCardNewService duofenCardNewService;

    @ApiOperation( value = "获取优惠券列表", notes = "获取优惠券列表" )
    @ResponseBody
    @RequestMapping( value = "/getCouponListByBusId", method =RequestMethod.POST )
    public ServerResponse getCouponListByBusId( HttpServletRequest request, HttpServletResponse response,@RequestBody String param) {
	try {
	    Map< String,Object > requestBody = JSONObject.parseObject( param );

	    Integer curPage;
	    Integer pageSize;
	    curPage = (Integer) CommonUtil.get("curPage",requestBody);
	    pageSize= (Integer) CommonUtil.get("pageSize",requestBody);
	    Integer busId= (Integer) CommonUtil.get("busId",requestBody);
	    if(curPage==null){
	        curPage=1;
	    }
	    if(pageSize ==null){
		pageSize=10;
	    }
	    Integer cardStatus= (Integer) CommonUtil.get("cardStatus",requestBody);
	    String couponName= (String) CommonUtil.get("couponName",requestBody);
	    Integer useType= (Integer) CommonUtil.get("useType",requestBody);

	    Page page = duofenCardNewService.getCouponListByBusId2( curPage, pageSize, busId, cardStatus, couponName, useType );
	    return ServerResponse.createBySuccess( page );
	} catch ( BusinessException e ) {
	    LOG.error( "获取优惠券异常", e );
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );
	}
    }

    @ApiOperation( value = "审核", notes = "审核" )
    @ResponseBody
    @RequestMapping( value = "/updateCouponById", method = RequestMethod.POST )
    public ServerResponse updateCouponById( HttpServletRequest request, HttpServletResponse response, @RequestBody DuofenCardNewVO couponVO ) {
	try {
	    DuofenCardNew couponPO =new DuofenCardNew();
	    couponPO.setId( couponVO.getId() );
	    couponPO.setExamineId( couponVO.getExamineId() );
	    couponPO.setExaminedDetail( couponVO.getExaminedDetail() );
	    couponPO.setPassTime(new Date( ));
	    duofenCardNewService.updateById( couponPO );
	    return ServerResponse.createBySuccess( );
	} catch ( Exception e ) {
	    LOG.error( "优惠券修改异常", e );
	    return ServerResponse.createByError( "优惠券修改异常" );
	}
    }

}