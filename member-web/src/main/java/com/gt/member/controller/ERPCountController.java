package com.gt.member.controller;

import com.alibaba.fastjson.JSONObject;
import com.gt.member.base.BaseController;
import com.gt.member.controller.queryBo.MallAllEntityQuery;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *  erp 统一结算页面
 * </p>
 *
 * @author pengjiangli
 * @since 2017-08-09
 */
@Controller
@RequestMapping("/erpCount")
public class ERPCountController extends BaseController {


    @ApiOperation(value = "erp统一跳转结算页面入口", notes = "erp统一跳转结算页面入口")
    @RequestMapping(value = "/countErpIndex")
    public String  indexCountErp(HttpServletRequest request,
		    HttpServletResponse response, String requestBody) {
	MallAllEntityQuery mallAllEntityQuery= JSONObject.toJavaObject( JSONObject.parseObject( requestBody ),MallAllEntityQuery.class );
	request.setAttribute( "mallAllEntityQuery",mallAllEntityQuery );
	return "";
    }


}
