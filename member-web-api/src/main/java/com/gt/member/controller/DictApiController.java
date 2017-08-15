package com.gt.member.controller;

import com.gt.member.base.BaseController;
import com.gt.member.dto.ServerResponse;
import com.gt.member.entity.Member;
import com.gt.member.enums.ResponseEnums;
import com.gt.member.exception.BusinessException;
import com.gt.member.service.common.dict.DictService;
import com.gt.member.service.memberApi.MemberApiService;
import com.gt.member.util.CommonUtil;
import com.gt.member.util.MemberConfig;
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
import java.util.SortedMap;

/**
 * 字典接口
 *
 * @author Administrator
 */
@Controller
@RequestMapping( "/memberAPI/dict" )
public class DictApiController extends BaseController {

    private static final Logger LOG = Logger.getLogger( DictApiController.class );

    @Autowired
    private DictService dictService;



    @ApiOperation( value = "查询字典 返回键值对", notes = "查询字典 返回键值对" )
    @ApiImplicitParam( name = "dictType", value = "属性", paramType = "query", required = true, dataType = "String" )
    @ResponseBody
    @RequestMapping( value = "/getdict", method = RequestMethod.POST )
    public ServerResponse getdict( HttpServletRequest request, HttpServletResponse response, @RequestBody Map requestBody ) {
	try {
	    String dictType = CommonUtil.toString( requestBody.get( "dictType" ) );
	    SortedMap<String, Object> map= dictService.getDict( dictType );
	    return ServerResponse.createBySuccess(map);
	} catch ( BusinessException e ) {
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );
	}
    }


    @ApiOperation( value = "查询字典 value值", notes = "查询字典 value值" )
   @ApiImplicitParams({
		   @ApiImplicitParam( name = "dictType", value = "属性", paramType = "query", required = true, dataType = "String" ),
		   @ApiImplicitParam( name = "key", value = "key值", paramType = "query", required = true, dataType = "int" )
   })

    @ResponseBody
    @RequestMapping( value = "/getDictRuturnValue", method = RequestMethod.POST )
    public ServerResponse getDictRuturnValue( HttpServletRequest request, HttpServletResponse response, @RequestBody Map requestBody ) {
	try {
	    String dictType = CommonUtil.toString( requestBody.get( "dictType" ) );
	    Integer key = CommonUtil.toInteger( requestBody.get( "key" ) );
	    String value= dictService.getDictRuturnValue( dictType,key );
	    return ServerResponse.createBySuccess(value);
	} catch ( BusinessException e ) {
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );
	}
    }

}
