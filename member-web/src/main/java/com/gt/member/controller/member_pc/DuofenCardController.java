package com.gt.member.controller.member_pc;

import com.gt.api.enums.ResponseEnums;
import com.gt.api.util.SessionUtils;
import com.gt.member.dto.ServerResponse;
import com.gt.member.entity.MemberCardtype;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by Administrator on 2018/1/16.
 */
@Api( value = "多粉优惠券", description = "多粉优惠券" )
@Controller
@CrossOrigin
@RequestMapping( "/memberPc/duofenCard" )
@Slf4j
public class DuofenCardController {

    @ApiOperation( value = "新增优惠券", notes = "新增优惠券" )
    @ResponseBody
    @RequestMapping( value = "/saveOrUpdateDuofenCard", method = RequestMethod.POST )
    public ServerResponse saveOrUpdateDuofenCard( HttpServletRequest request, HttpServletResponse response,@RequestParam String params ) {
	try {

	    return ServerResponse.createBySuccess(  );
	} catch ( Exception e ) {
	    log.error( "查询会员卡类型异常：", e );
	    e.printStackTrace();
	    return ServerResponse.createByError( ResponseEnums.ERROR.getCode(), "查询会员卡类型异常" );
	}
    }


}
