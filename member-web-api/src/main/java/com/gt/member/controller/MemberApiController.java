package com.gt.member.controller;

import com.gt.member.base.BaseController;
import com.gt.member.dto.ServerResponse;
import com.gt.member.entity.Member;
import com.gt.member.enums.ResponseEnums;
import com.gt.member.exception.BusinessException;
import com.gt.member.service.memberApi.MemberApiService;
import com.gt.member.util.CommonUtil;
import com.gt.member.util.MemberConfig;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 会员httpservice 接口
 *
 * @author Administrator
 */
@Controller
@RequestMapping("/api/memberApi")
public class MemberApiController extends BaseController {

    private static final Logger LOG = Logger.getLogger(MemberApiController.class);

    @Autowired
    private MemberConfig memberConfig;

    @Autowired
    private MemberApiService memberApiService;

    @ApiOperation(value = "根据memberId和门店查询会员数据", notes = "根据memberId和门店查询会员数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "memberId", value = "卡号或手机号", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "shopId", value = "门店id", paramType = "query", required = true, dataType = "int")
    })
    @ResponseBody
    @GetMapping("/findMemberCardByMemberId")
    public ServerResponse findCardByMembeId(HttpServletRequest request,
                                            HttpServletResponse response, Integer memberId, Integer shopId) {
        try {
            Map<String, Object> map = memberApiService.findMemberCardByMemberId(memberId, shopId);
            return ServerResponse.createBySuccess(map);
        } catch (BusinessException e) {
            return ServerResponse.createByError(e.getCode(),e.getMessage());
        }
    }

    @ApiOperation(value = "根据手机号或会员卡号获取会员信息", notes = "手机号或卡号查询会员相关信息包括微信多粉优惠券 \n " +
            "cardNo 卡号或手机号 ,busId 商家id,shopId 门店id")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cardNo", value = "卡号或手机号", paramType = "query", required = true, dataType = "String"),
            @ApiImplicitParam(name = "busId", value = "商家id", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "shopId", value = "门店id", paramType = "query", required = true, dataType = "int")
    })
    @ResponseBody
    @GetMapping("/findMemberCard")
    public ServerResponse findMemberCard(HttpServletRequest request,
                                         HttpServletResponse response, String cardNo, Integer busId, Integer shopId) {
        if (CommonUtil.isEmpty("cardNo") || CommonUtil.isEmpty("busId") || CommonUtil.isEmpty("shopId")) {
            return ServerResponse.createByError("缺少参数");
        }
        String cardNoKey = memberConfig.getCardNoKey();
        try {
             Map<String,Object> map=memberApiService.findMemberCard(busId, cardNoKey, cardNo, shopId);
            return ServerResponse.createBySuccess(map);
        } catch (BusinessException e){
            return ServerResponse.createByError(e.getCode(),e.getMessage());
        }
    }


    @ApiOperation(value = "根据粉丝id获取粉丝信息", notes = "获取粉丝信息")
    @ApiImplicitParam(name = "memberId", value = "粉丝id", paramType = "query", required = true, dataType = "int")
    @ResponseBody
    @RequestMapping (value = "/findByMemberId",method = RequestMethod.GET)
    public ServerResponse findByMemberId(HttpServletRequest request,
                                         HttpServletResponse response,@RequestBody Map requestBody) {
       try {
           Integer memberId=CommonUtil.toInteger( requestBody.get( "memberId" ));
           Member member = memberApiService.findByMemberId(memberId);
           return ServerResponse.createBySuccess(member);
       }catch (BusinessException e){
           return ServerResponse.createByError(e.getCode(),e.getMessage());
       }
    }

    @ApiOperation(value = "判断储值卡金额是否充足", notes = "判断储值卡金额是否充足")
    @ApiImplicitParams( {
                    @ApiImplicitParam( name = "memberId", value = "粉丝id", paramType = "query", required = true, dataType = "int" ),
                    @ApiImplicitParam( name = "money", value = "消费金额", paramType = "query", required = true, dataType = "int" ),
    })
    @ResponseBody
    @RequestMapping (value = "/isAdequateMoney",method = RequestMethod.GET)
    public ServerResponse isAdequateMoney(HttpServletRequest request,
                    HttpServletResponse response, @RequestBody Map requestBody){
        try {
            Integer memberId=CommonUtil.toInteger( requestBody.get( "memberId" ));
            Double money=CommonUtil.toDouble( requestBody.get( "money" ));
            memberApiService.isAdequateMoney(memberId,money);
            return ServerResponse.createBySuccess();
        }catch (BusinessException e){
            return ServerResponse.createByError(e.getCode(),e.getMessage());
        }
    }



    @ApiOperation(value = "根据粉丝id获取会员折扣", notes = "根据粉丝id获取会员折扣")
    @ApiImplicitParam(name = "memberId", value = "粉丝id", paramType = "query", required = true, dataType = "int")
    @ResponseBody
    @RequestMapping (value = "/findCardTypeReturnDiscount",method = RequestMethod.GET)
    public ServerResponse findCardTypeReturnDiscount(HttpServletRequest request,
                    HttpServletResponse response, @RequestBody Map requestBody){
        try {
            Integer memberId=CommonUtil.toInteger( requestBody.get( "memberId" ));
            Double discount = memberApiService.findCardTypeReturnDiscount(memberId);
            return ServerResponse.createBySuccess(discount);
        }catch (BusinessException e){
            return ServerResponse.createByError(e.getCode(),e.getMessage());
        }
    }

    @ApiOperation(value = "小程序绑定手机号码", notes = "小程序绑定手机号码 返回member对象数据")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "memberId", value = "粉丝id", paramType = "query", required = true, dataType = "int"),
        @ApiImplicitParam(name = "code", value = "短信校验码", paramType = "query", required = true, dataType = "String"),
        @ApiImplicitParam(name = "phone", value = "手机号码", paramType = "query", required = true, dataType = "String"),
        @ApiImplicitParam(name = "busId", value = "商家id", paramType = "query", required = true, dataType = "int")
    })
    @ResponseBody
    @RequestMapping (value = "/bingdingPhone",method = RequestMethod.GET)
    public ServerResponse bingdingPhone(HttpServletRequest request,
                    HttpServletResponse response, @RequestBody Map requestBody){
        try {
            Integer memberId=CommonUtil.toInteger( requestBody.get( "memberId" ));
            Integer busId=CommonUtil.toInteger( requestBody.get( "busId" ));
            String phone=CommonUtil.toString( requestBody.get( "phone" ));
            String code=CommonUtil.toString( requestBody.get( "code" ));
            Member member= memberApiService.bingdingPhone(memberId,phone,code,busId);
            return ServerResponse.createBySuccess(member);
        }catch (BusinessException e){
            return ServerResponse.createByError(e.getCode(),e.getMessage());
        }
    }


    @ApiOperation(value = "退款包括了储值卡退款", notes = "退款包括了储值卡退款")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "busId", value = "商家id", paramType = "query", required = true, dataType = "String"),
            @ApiImplicitParam(name = "orderNo", value = "订单号", paramType = "query", required = true, dataType = "String"),
            @ApiImplicitParam(name = "ucType", value = "消费类型", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "money", value = "退款金额", paramType = "query", required = true, dataType = "double")
    })
    @ResponseBody
    @RequestMapping (value = "/refundMoney",method = RequestMethod.GET)
    public ServerResponse refundMoney(HttpServletRequest request,
                                     HttpServletResponse response,@RequestBody Map requestBody) {
        try {
            Integer ucType=CommonUtil.toInteger( requestBody.get( "ucType" ));
            Integer busId=CommonUtil.toInteger( requestBody.get( "busId" ));
            String orderNo=CommonUtil.toString( requestBody.get( "orderNo" ));
            Double money=CommonUtil.toDouble( requestBody.get( "money" ));
            memberApiService.refundMoney(busId, orderNo, ucType, money);
            return ServerResponse.createBySuccess();
        } catch (BusinessException e) {
            return ServerResponse.createByError(ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getDesc());
        }
    }


    @ApiOperation(value = "统计会员数量", notes = "根据商家id统计会员数量")
    @ApiImplicitParam(name = "busId", value = "商家id", paramType = "query", required = true, dataType = "int")
    @ResponseBody
    @RequestMapping (value = "/countMember",method = RequestMethod.GET)
    public ServerResponse countMember(HttpServletRequest request,
                                      HttpServletResponse response,@RequestBody Map requestBody){
        try {
            Integer busId=CommonUtil.toInteger( requestBody.get( "busId" ) );
            Map<String, Object> countMember = memberApiService.countMember(busId);
            return ServerResponse.createBySuccess(countMember);
        } catch (Exception e) {
            logger.error("统计会员异常",e);
            return ServerResponse.createByError(ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getDesc());
        }
    }


}
