package com.gt.member.controller.member_pc;

import com.gt.api.enums.ResponseEnums;
import com.gt.duofencard.entity.DuofenCardNewVO;
import com.gt.duofencard.entity.DuofenCardTime;
import com.gt.member.dto.ServerResponse;
import com.gt.member.entity.DuofenCard;
import com.gt.member.service.member.DuofenCardNewService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Administrator on 2018/1/16.
 */
@Api( value = "DuofenCardController", description = "多粉优惠券" )
@Controller
@CrossOrigin
@RequestMapping( "/memberPc/duofenCard" )
@Slf4j
public class DuofenCardController {
    @Autowired
    private DuofenCardNewService duofenCardNewService;
    @Autowired
    private DuofenCardNewService duofenCardNewService;

    @ApiOperation( value = "新增优惠券", notes = "新增优惠券" )
    @ResponseBody
    @RequestMapping( value = "/addCoupon", method = RequestMethod.POST )
    public ServerResponse addCoupon( HttpServletRequest request, HttpServletResponse response,@RequestParam DuofenCardNewVO coupon ) {
	try {
	    duofenCardNewService.addCoupon(coupon);
	    return ServerResponse.createBySuccess();
	} catch ( Exception e ) {
	    log.error( "新增优惠券异常：", e );
	    e.printStackTrace();
	    return ServerResponse.createByError( ResponseEnums.ERROR.getCode(), "查询会员卡类型异常" );
	}
    }


}
