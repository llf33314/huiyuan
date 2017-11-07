package com.gt.member.controller;

import com.gt.member.dto.ServerResponse;
import com.gt.member.exception.BusinessException;
import com.gt.member.service.memberApi.MemberApiService;
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
import java.util.Map;

/**
 * 不处理拦截器 api
 *
 * Created by Administrator on 2017/11/3.
 */
@Api(value = "不处理签名API",description = "不处理签名API")
@Controller
@RequestMapping("/memberNodoInterceptor/memberNotDo")
public class MemberNotDoInterceptorController {

    private static final Logger LOG = Logger.getLogger( MemberNotDoInterceptorController.class );

    @Autowired
    private MemberApiService memberApiService;

    @ApiOperation( value = "流量兑换通知", notes = "流量兑换通知" )
    @ApiImplicitParams( { @ApiImplicitParam( name = "id", value = "订单id", paramType = "query", required = true, dataType = "int" ),
		    @ApiImplicitParam( name = "status", value = "状态", paramType = "query", required = true, dataType = "int" )

    } )
    @ResponseBody
    @RequestMapping( value = "/changeFlow", method = RequestMethod.POST )
    public ServerResponse changeFlow(HttpServletRequest request, HttpServletResponse response, @RequestBody Map<String,Object> params ){
	try {
	    memberApiService.changeFlow( params );
	    return ServerResponse.createBySuccess(  );
	} catch ( BusinessException e ) {
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );
	}

    }
}
