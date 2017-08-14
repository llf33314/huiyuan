package com.gt.member.controller;

import com.alibaba.fastjson.JSONObject;
import com.gt.member.dto.ServerResponse;
import com.gt.member.enums.ResponseEnums;
import com.gt.member.exception.BusinessException;
import com.gt.member.service.memberApi.MemberApiService;
import com.gt.member.service.memberApi.MemberCountMoneyApiService;
import com.gt.member.service.memberApi.entityBo.MallAllEntity;
import com.gt.member.service.memberApi.entityBo.PaySuccessBo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 会员计算api
 *
 * Created by pengjiangli on 2017/8/2 0002.
 */
@Controller
@RequestMapping("/memberAPI/memberCountApi")
public class MemberCountApiController {

    @Autowired
    private MemberCountMoneyApiService memberCountMoneyApiService;

    @Autowired
    private  MemberApiService memberApiService;


    @ApiOperation(value = "门店计算方法", notes = "传入值具体描述请看实体类")
    @ResponseBody
    @RequestMapping(value = "memberCountMoneyByShop",method = RequestMethod.POST)
    public ServerResponse memberCountMoneyByShop(HttpServletRequest request,
                                         HttpServletResponse response, MallAllEntity mallAllEntity ){
        try {
          mallAllEntity = memberCountMoneyApiService.mallSkipShopCount(mallAllEntity);
          return ServerResponse.createBySuccess(JSONObject.toJSON(mallAllEntity));
      }catch (Exception e){
          return ServerResponse.createByError(ResponseEnums.ERROR.getCode(),"计算失败");
      }
    }


    @ApiOperation(value = "支付成功回调", notes = "传入值具体描述请看实体类 储值卡支付 直接调用 回调类以处理储值卡扣款")
    @ResponseBody
    @GetMapping("/paySuccess")
    @RequestMapping(value = "paySuccess",method = RequestMethod.POST)
    public ServerResponse paySuccess(HttpServletRequest request,
                                     HttpServletResponse response,@RequestBody PaySuccessBo paySuccessBo){
        try {
            memberApiService.paySuccess(paySuccessBo);
            return ServerResponse.createBySuccess();
        }catch (BusinessException e){
            return ServerResponse.createByError(e.getCode(),e.getMessage());
        }
    }
}
