package com.gt.member.controller;

import com.alibaba.fastjson.JSONObject;
import com.gt.api.enums.ResponseEnums;
import com.gt.member.dto.ServerResponse;
import com.gt.entityBo.MallNotShopEntity;
import com.gt.entityBo.MemberShopEntity;
import com.gt.member.service.memberApi.MemberApiService;
import com.gt.member.service.memberApi.MemberCountMoneyApiService;
import com.gt.entityBo.MallAllEntity;
import io.swagger.annotations.Api;
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
@Api(value = "结算算法接口",description = "结算算法接口")
@Controller
@RequestMapping("/memberAPI/memberCountApi")
public class MemberCountApiController {

    @Autowired
    private MemberCountMoneyApiService memberCountMoneyApiService;

    @Autowired
    private MemberApiService memberApiService;


    @ApiOperation(value = "门店计算方法(跨门店)", notes = "传入值具体描述请看实体类")
    @ResponseBody
    @RequestMapping(value = "memberCountMoneyByShop",method = RequestMethod.POST)
    public ServerResponse memberCountMoneyByShop(HttpServletRequest request,
                                         HttpServletResponse response, @RequestBody String  mallAllEntity ){
        try {
            MallAllEntity mallAllEntity1=JSONObject.toJavaObject( JSONObject.parseObject( mallAllEntity ),MallAllEntity.class ) ;
            mallAllEntity1 = memberCountMoneyApiService.mallSkipShopCount(mallAllEntity1);
          return ServerResponse.createBySuccess(JSONObject.toJSON(mallAllEntity1));
      }catch (Exception e){
          return ServerResponse.createByError( ResponseEnums.ERROR.getCode(),"计算失败");
      }
    }


    @ApiOperation(value = "门店计算方法(不跨门店)", notes = "传入值具体描述请看实体类")
    @ResponseBody
    @RequestMapping(value = "memberCountMoneyByBusUser",method = RequestMethod.POST)
    public ServerResponse memberCountMoneyByBusUser(HttpServletRequest request,
                    HttpServletResponse response,@RequestBody String   mallNotShopEntity ){
        try {
            MallNotShopEntity  mallNotShopEntity1=JSONObject.toJavaObject( JSONObject.parseObject( mallNotShopEntity ),MallNotShopEntity.class ) ;
            mallNotShopEntity1 = memberCountMoneyApiService.mallSkipNotShopCount(mallNotShopEntity1);
            return ServerResponse.createBySuccess(JSONObject.toJSON(mallNotShopEntity1));
        }catch (Exception e){
            return ServerResponse.createByError(ResponseEnums.ERROR.getCode(),"计算失败");
        }
    }

    @ApiOperation(value = "门店计算方法(不包含订单明细，只有订单金额)", notes = "传入值具体描述请看实体类")
    @ResponseBody
    @RequestMapping(value = "publicMemberCountMoney",method = RequestMethod.POST)
    public ServerResponse publicMemberCountMoney(HttpServletRequest request,
                    HttpServletResponse response,@RequestBody String  memberShopEntity){
        try {
            MemberShopEntity  memberShopEntity1=JSONObject.toJavaObject( JSONObject.parseObject( memberShopEntity ),MemberShopEntity.class ) ;
            memberShopEntity1 = memberCountMoneyApiService.publicMemberCountMoney(memberShopEntity1);
            return ServerResponse.createBySuccess(JSONObject.toJSON(memberShopEntity1));
        }catch (Exception e){
            return ServerResponse.createByError(ResponseEnums.ERROR.getCode(),"计算失败");
        }
    }
}
