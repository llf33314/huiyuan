package com.gt.member.controller;

import com.gt.api.enums.ResponseEnums;
import com.gt.member.dto.ServerResponse;
import com.gt.member.entity.DuofenCard;
import com.gt.member.entity.DuofenCardGet;
import com.gt.member.entity.DuofenCardReceive;
import com.gt.member.entity.WxCard;
import com.gt.member.exception.BusinessException;
import com.gt.member.service.memberApi.CardCouponsApiService;
import com.gt.member.util.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 优惠券卡券查询
 * <p>
 * Created by Administrator on 2017/8/2 0002.
 */
@Api(value = "优惠券卡券接口",description = "优惠券卡券接口")
@Controller
@RequestMapping("/memberAPI/cardCouponseApi")
public class CardCouponseApiController {

    @Autowired
    private CardCouponsApiService cardCouponsApiService;

    @ApiOperation(value = "查询微信优惠券信息", notes = "根据公众号查询微信卡券信息")
    @ApiImplicitParam(name = "publicId", value = "公众号id", paramType = "query", required = true, dataType = "int")
    @ResponseBody
    @RequestMapping (value = "/findWxCard",method = RequestMethod.POST)
    public ServerResponse findWxCard(HttpServletRequest request,
                                     HttpServletResponse response,@RequestBody Map<String,Object> requestBody) {
      try {
          Integer publicId = CommonUtil.toInteger( requestBody.get( "publicId" ) );
          List< Map< String,Object > > cardList = cardCouponsApiService.findWxCard( publicId );
          return ServerResponse.createBySuccess( cardList );
      }catch (Exception e) {
          return ServerResponse.createByError(ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getMsg());
      }
    }

    @ApiOperation(value = "查询商户下所有用的卡券信息 card_type:判断卡片类型 card_type=DISCOUNT折扣券 discount=值 折扣数\n" + "     * card_type=CASH 代金券 cash_least_cost=值 条件值 reduce_cost=值 减免金额\n"
                    + " user_card_code 卡券code image 卡券图标", notes = "")
    @ApiImplicitParams( {
                    @ApiImplicitParam(name = "publicId", value = "公众号id", paramType = "query", required = true, dataType = "int"),
                    @ApiImplicitParam(name = "shopId", value = "门店id", paramType = "query", required = true, dataType = "int"),
                    @ApiImplicitParam(name = "memberId", value = "粉丝id", paramType = "query", required = true, dataType = "int")
    } )
    @ResponseBody
    @RequestMapping (value = "/findWxCardByShopId",method = RequestMethod.POST)
    public ServerResponse findWxCardByShopId(HttpServletRequest request,
                    HttpServletResponse response,@RequestBody Map<String,Object> requestBody){
        try {
            Integer publicId = CommonUtil.toInteger( requestBody.get( "publicId" ) );
            Integer shopId = CommonUtil.toInteger( requestBody.get( "shopId" ) );
            Integer memberId = CommonUtil.toInteger( requestBody.get( "memberId" ) );
            List< Map< String,Object > > cardList = cardCouponsApiService.findWxCardByShopId( shopId, publicId, memberId );
            return ServerResponse.createBySuccess( cardList );
        }catch (Exception e) {
            return ServerResponse.createByError(ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getMsg());
        }
    }


    @ApiOperation(value = "核销微信优惠券", notes = "微信卡券核销返回卡券id和name")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "publicId", value = "公众号id", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "code", value = "卡券code值", paramType = "query", required = true, dataType = "String")
    })
    @ResponseBody
    @RequestMapping (value = "/wxCardReceiveBackName",method = RequestMethod.POST)
    public ServerResponse wxCardReceiveBackName(HttpServletRequest request,
                                                HttpServletResponse response,@RequestBody Map<String,Object> requestBody) {
        try {
            Integer publicId= CommonUtil.toInteger(requestBody.get( "publicId" ));
            String code= CommonUtil.toString(requestBody.get( "code" ));
            Map<String, Object> map = cardCouponsApiService.wxCardReceiveBackName(publicId, code);
            return ServerResponse.createBySuccess(map);
        }catch (BusinessException e){
            return ServerResponse.createByError(e.getCode(),e.getMessage());
        }catch (Exception e) {
            return ServerResponse.createByError(ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getMsg());
        }
    }

    @ApiOperation(value = "查询卡券信息", notes = "根据id查询微信卡券信息")
    @ApiImplicitParam(name = "cardId", value = "卡券id", paramType = "query", required = true, dataType = "int")
    @ResponseBody
    @RequestMapping (value = "/findWxCardById",method = RequestMethod.POST)
    public ServerResponse findWxCardById(HttpServletRequest request,
                                         HttpServletResponse response, @RequestBody Map<String,Object> requestBody) {
        Integer cardId= CommonUtil.toInteger(requestBody.get( "cardId" ));
        WxCard wxCard=cardCouponsApiService.findWxCardById(cardId);
        return ServerResponse.createBySuccess(wxCard);
    }

    @ApiOperation(value = "查询卡券信息", notes = "查询用户拥有的优惠券 过滤不满足的金额卡券" +
            "返回数据中能使用的到值 属性 image：图片 gId：用户拥有卡券表id, code=用户拥有卡券表 卡券code,\n" +
            " addUser是否允许叠加使用 0不允许 1已允许 , countId=1, 减免券能叠加使用最高使用数量值\n" +
            " discount=0.0, //折扣值 card_type=1, //卡券类型 0折扣券 1减免券\n" +
            " cash_least_cost=10.0, 抵扣条件 reduce_cost=5.0, 抵扣金额 cId=43 卡券id")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "memberId", value = "粉丝Id", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "wxshopId", value = "门店id", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "money", value = "消费金额", paramType = "query", required = true, dataType = "double")
    })
    @ResponseBody
    @RequestMapping (value = "/findDuofenCardByMemberIdAndMoney",method = RequestMethod.POST)
    public ServerResponse findDuofenCardByMemberIdAndMoney(HttpServletRequest request,
                                                           HttpServletResponse response,@RequestBody Map<String,Object> requestBody ){
        try {
            Integer memberId= CommonUtil.toInteger(requestBody.get( "memberId" ));
            Integer wxshopId= CommonUtil.toInteger(requestBody.get( "wxshopId" ));
            Double money= CommonUtil.toDouble(requestBody.get( "money" ));
            List<Map<String, Object>> cardList = cardCouponsApiService.findDuofenCardByMemberIdAndMoney(memberId, wxshopId, money);
            return ServerResponse.createBySuccess(cardList);
        }catch (Exception e){
            return ServerResponse.createByError(ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getMsg());
        }
    }


    @ApiOperation(value = "查询商家拥有的卡包信息", notes = "根据商家id查询商家拥有的卡包信息")
    @ApiImplicitParam(name = "busId", value = "商家id", paramType = "query", required = true, dataType = "int")
    @ResponseBody
    @RequestMapping (value = "/findReceiveByBusUserId",method = RequestMethod.POST)
    public ServerResponse findReceiveByBusUserId(HttpServletRequest request,
                                                 HttpServletResponse response,@RequestBody Map<String,Object> requestBody ){
        Integer busId=CommonUtil.toInteger( requestBody.get( "busId" ) );
        List<Map<String,Object>> cardList=cardCouponsApiService.findReceiveByBusUserId(busId);
        return ServerResponse.createBySuccess(cardList);
    }


    @ApiOperation(value = "查询本公众号商场投放的包", notes = "根据商家id查询本公众号商场投放的包")
    @ApiImplicitParam(name = "busId", value = "商家id", paramType = "query", required = true, dataType = "int")
    @ResponseBody
    @RequestMapping (value = "/findReceiveBybusId",method = RequestMethod.POST)
    public ServerResponse findReceiveBybusId(HttpServletRequest request,
                                             HttpServletResponse response, @RequestBody Map<String,Object> requestBody){
        Integer busId=CommonUtil.toInteger( requestBody.get( "busId" ) );
        List<DuofenCardReceive> duofenCardReceives=cardCouponsApiService.findReceiveBybusId(busId);
        return ServerResponse.createBySuccess(duofenCardReceives);
    }


    @ApiOperation(value = "查询卡券信息", notes = "根据卡包查询卡券信息")
    @ApiImplicitParam(name = "receiveId", value = "多粉卡包id", paramType = "query", required = true, dataType = "int")
    @ResponseBody
    @RequestMapping (value = "/findCardByReceiveId",method = RequestMethod.POST)
    public ServerResponse findCardByReceiveId(HttpServletRequest request,
                                              HttpServletResponse response,@RequestBody Map<String,Object> requestBody){
        Integer receiveId=CommonUtil.toInteger( requestBody.get( "receiveId" ) );
        Map<String,Object> map=cardCouponsApiService.findCardByReceiveId(receiveId);
        return ServerResponse.createBySuccess(map);
    }


    @ApiOperation(value = "分页查询第三方平台下所有优惠券", notes = "查询第三方平台下所有优惠券")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "threeMemberId", value = "第三平台下粉丝id", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "page", value = "页数", paramType = "query", required = true, dataType = "int")
    })
    @ResponseBody
    @RequestMapping (value = "/findByThreeMemberId",method = RequestMethod.POST)
    public ServerResponse findByThreeMemberId(HttpServletRequest request,
                                              HttpServletResponse response,@RequestBody Map<String,Object> requestBody){
        Integer threeMemberId=CommonUtil.toInteger( requestBody.get( "threeMemberId" ) );
        Integer page=CommonUtil.toInteger( requestBody.get( "page" ) );
        Map<String, Object> map=cardCouponsApiService.findByThreeMemberId(threeMemberId,page);
        return ServerResponse.createBySuccess(map);
    }

    @ApiOperation(value = "商场支付成功回调 分配卡券", notes = "商场支付成功回调 分配卡券")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "receiveId", value = "卡包id", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "num", value = "数量", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "memberId", value = "粉丝id", paramType = "query", required = true, dataType = "int")
    })
    @ResponseBody
    @RequestMapping (value = "/successPayBack",method = RequestMethod.POST)
    public ServerResponse successPayBack(HttpServletRequest request,
                                         HttpServletResponse response,@RequestBody Map<String,Object> requestBody){
        try {
            Integer receiveId=CommonUtil.toInteger( requestBody.get( "receiveId" ) );
            Integer num=CommonUtil.toInteger( requestBody.get( "num" ) );
            Integer memberId=CommonUtil.toInteger( requestBody.get( "memberId" ) );
            cardCouponsApiService.successPayBack(receiveId, num, memberId);
            return ServerResponse.createBySuccess();
        }catch (BusinessException e){
            return ServerResponse.createByError(e.getCode(), e.getMessage());
        }
    }

    @ApiOperation(value = "查询卡券信息", notes = "根据卡包id查询卡券信息 map中key guoqi=1标示该包或该券过期")
    @ApiImplicitParam(name = "receiveId", value = "卡包id", paramType = "query", required = true, dataType = "int")
    @ResponseBody
    @RequestMapping (value = "/findDuofenCardByReceiveId",method = RequestMethod.POST)
    public ServerResponse findDuofenCardByReceiveId(HttpServletRequest request,
                                                    HttpServletResponse response,@RequestBody Map<String,Object> requestBody){
        try {
            Integer receiveId=CommonUtil.toInteger( requestBody.get( "receiveId" ) );
            Map<String, Object> map = cardCouponsApiService.findDuofenCardByReceiveId(receiveId);
            return ServerResponse.createBySuccess(map);
        }catch (BusinessException e){
            return ServerResponse.createByError(e.getCode(), e.getMessage());
        }
    }

    @ApiOperation(value = "卡包投放", notes = "根据卡包id卡包投放")
    @ApiImplicitParam(name = "receiveId", value = "卡包id", paramType = "query", required = true, dataType = "int")
    @ResponseBody
    @RequestMapping (value = "/publishShelve",method = RequestMethod.POST)
    public ServerResponse publishShelve(HttpServletRequest request,
                                        HttpServletResponse response,@RequestBody Map<String,Object> requestBody){
        try {
            Integer receiveId=CommonUtil.toInteger( requestBody.get( "receiveId" ) );
            Map<String, Object> map = cardCouponsApiService.findDuofenCardByReceiveId(receiveId);
            return ServerResponse.createBySuccess(map);
        }catch (BusinessException e){
            return ServerResponse.createByError(e.getCode(), e.getMessage());
        }
    }

    @ApiOperation(value = "优惠券商城购买领取优惠券", notes = "优惠券商城购买领取优惠券")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "threeMemberId", value = "第三方粉丝id", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "memberId", value = "本商家下粉丝id", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "busId", value = "商家id", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "bagId", value = "卡包id", paramType = "query", required = true, dataType = "int")
    })
    @ResponseBody
    @RequestMapping (value = "/threeShopGetCard",method = RequestMethod.POST)
    public ServerResponse threeShopGetCard(HttpServletRequest request,
                                           HttpServletResponse response,@RequestBody Map<String,Object> requestBody){
        try {
            Integer threeMemberId=CommonUtil.toInteger( requestBody.get( "threeMemberId" ) );
            Integer memberId=CommonUtil.toInteger( requestBody.get( "memberId" ) );
            Integer busId=CommonUtil.toInteger( requestBody.get( "busId" ) );
            Integer bagId=CommonUtil.toInteger( requestBody.get( "bagId" ) );
            cardCouponsApiService.threeShopGetCard(threeMemberId,memberId,busId,bagId);
            return ServerResponse.createBySuccess();
        }catch (BusinessException e){
            return ServerResponse.createByError(e.getCode(), e.getMessage());
        }
    }

    @ApiOperation(value = "根据卡包id和粉丝id查询粉丝的卡券信息", notes = "根据卡包id 和 粉丝信息 查询粉丝的卡券信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "memberId", value = "粉丝id", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "receiveId", value = "卡包id", paramType = "query", required = true, dataType = "int")
    })
    @ResponseBody
    @RequestMapping (value = "/findUserCardByReceiveId",method = RequestMethod.POST)
    public ServerResponse findUserCardByReceiveId(HttpServletRequest request,
                                                  HttpServletResponse response,@RequestBody Map<String,Object> requestBody){
        Integer memberId=CommonUtil.toInteger( requestBody.get( "memberId" ) );
        Integer receiveId=CommonUtil.toInteger( requestBody.get( "receiveId" ) );
       List<DuofenCardGet> duofenCardGets=cardCouponsApiService.findUserCardByReceiveId(memberId,receiveId);
       return ServerResponse.createBySuccess(duofenCardGets);
    }


    @ApiOperation(value = "查询优惠券商场过期优惠券信息", notes = "根据粉丝id和卡包id查询优惠券商场过期优惠券信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "memberId", value = "粉丝id", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "receiveId", value = "卡包id", paramType = "query", required = true, dataType = "int")
    })
    @ResponseBody
    @RequestMapping (value = "/findCardOverTime",method = RequestMethod.POST)
    public ServerResponse findCardOverTime(HttpServletRequest request,
                                           HttpServletResponse response,@RequestBody Map<String,Object> requestBody){
        Integer memberId=CommonUtil.toInteger( requestBody.get( "memberId" ) );
        Integer receiveId=CommonUtil.toInteger( requestBody.get( "receiveId" ) );
        List<DuofenCard> duofenCards=cardCouponsApiService.findCardOverTime(memberId,receiveId);
        return ServerResponse.createBySuccess(duofenCards);
    }
    @ApiOperation(value = "查询能赠送的卡包信息(免费领取) (汽车ERP)", notes = "根据商家id和粉丝id查询能赠送的卡包信息(免费领取) (汽车ERP)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "memberId", value = "粉丝id", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "busId", value = "商家id", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "page", value = "页数", paramType = "query", required = true, dataType = "int")
    })
    @ResponseBody
    @RequestMapping (value = "/findCardReceive",method = RequestMethod.POST)
    public ServerResponse findCardReceive(HttpServletRequest request,
                                          HttpServletResponse response,@RequestBody Map<String,Object> requestBody){
        Integer busId=CommonUtil.toInteger( requestBody.get( "busId" ) );
        Integer memberId=CommonUtil.toInteger( requestBody.get( "memberId" ) );
        Integer page=CommonUtil.toInteger( requestBody.get( "page" ) );
        List<Map<String,Object>> maps=cardCouponsApiService.findCardReceive(busId,memberId,page);
        return ServerResponse.createBySuccess(maps);
    }

    @ApiOperation(value = "查询能赠送的卡包信息(购买领取) (汽车ERP)", notes = "根据商家id和粉丝id查询能赠送的卡包信息(购买领取) (汽车ERP)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "memberId", value = "粉丝id", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "busId", value = "商家id", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "page", value = "页数", paramType = "query", required = true, dataType = "int")
    })
    @ResponseBody
    @RequestMapping (value = "/findCardReceiveBuy",method = RequestMethod.POST)
    public ServerResponse findCardReceiveBuy(HttpServletRequest request,
                                             HttpServletResponse response,@RequestBody Map requestBody){
        Integer busId=CommonUtil.toInteger( requestBody.get( "busId" ) );
        Integer memberId=CommonUtil.toInteger( requestBody.get( "memberId" ) );
        Integer page=CommonUtil.toInteger( requestBody.get( "page" ) );
        List<Map<String,Object>> maps=cardCouponsApiService.findCardReceive1(busId,memberId,page);
        return ServerResponse.createBySuccess(maps);
    }


    @ApiOperation(value = "(汽车ERP) 购买 或免费领取 pc端", notes = "(汽车ERP) 购买 或免费领取 pc端")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "memberId", value = "粉丝id", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "busId", value = "商家id", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "cardreceiveId", value = "卡包id", paramType = "query", required = true, dataType = "int")
    })
    @ResponseBody
    @RequestMapping (value = "/pcBuyReceive",method = RequestMethod.POST)
    public ServerResponse pcBuyReceive(HttpServletRequest request,
                                       HttpServletResponse response,@RequestBody Map<String,Object> requestBody ){
        try {
            Integer memberId=CommonUtil.toInteger( requestBody.get( "memberId" ) );
            Integer busId=CommonUtil.toInteger( requestBody.get( "busId" ) );
            Integer cardreceiveId=CommonUtil.toInteger( requestBody.get( "cardreceiveId" ) );
            cardCouponsApiService.pcBuyReceive(memberId, busId, cardreceiveId);
            return ServerResponse.createBySuccess();
        }catch (BusinessException e){
            return ServerResponse.createByError(e.getCode(), e.getMessage());
        }
    }

    @ApiOperation(value = "(汽车ERP) 购买 手机端购买支付成功 回调", notes = "(汽车ERP) 购买 手机端购买支付成功 回调")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "receiveId", value = "卡包id", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "num", value = "数量", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "memberId", value = "粉丝id", paramType = "query", required = true, dataType = "int")
    })
    @ResponseBody
    @RequestMapping (value = "/successBuyReceive",method = RequestMethod.POST)
    public ServerResponse successBuyReceive(HttpServletRequest request,
                                            HttpServletResponse response,@RequestBody Map<String,Object> requestBody ){
        try {
            Integer receiveId=CommonUtil.toInteger( requestBody.get( "receiveId" ) );
            Integer num=CommonUtil.toInteger( requestBody.get( "num" ) );
            Integer memberId=CommonUtil.toInteger( requestBody.get( "memberId" ) );
            cardCouponsApiService.successBuyReceive(receiveId, num, memberId);
            return ServerResponse.createBySuccess();
        }catch (BusinessException e){
            return ServerResponse.createByError(e.getCode(), e.getMessage());
        }
    }

    @ApiOperation(value = "卡包信息（购买） 美容", notes = "卡包信息（购买） 美容")
    @ApiImplicitParam(name = "busId", value = "商家id", paramType = "query", required = true, dataType = "int")
    @ResponseBody
    @RequestMapping (value = "/findReceiveByMeiRong",method = RequestMethod.POST)
    public ServerResponse findReceiveByMeiRong(HttpServletRequest request,
                                      HttpServletResponse response,@RequestBody Map<String,Object> requestBody){
        try {
            Integer busId=CommonUtil.toInteger( requestBody.get( "busId" ) );
            List<Map<String, Object>> mapList=cardCouponsApiService.findReceive(busId);
            return ServerResponse.createBySuccess(mapList);
        }catch (Exception e){
            return ServerResponse.createByError(ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getMsg());
        }
    }

    @ApiOperation(value = "卡包中卡券信息 美容", notes = "卡包中卡券信息 美容")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "busId", value = "商家id", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "receiveId", value = "卡包id", paramType = "query", required = true, dataType = "int")
    })
    @ResponseBody
    @RequestMapping (value = "/findDuofenCardByMeiRong",method = RequestMethod.POST)
    public ServerResponse findDuofenCardByMeiRong(HttpServletRequest request,
                                                  HttpServletResponse response,@RequestBody Map<String,Object> requestBody){
        try {
            Integer busId=CommonUtil.toInteger( requestBody.get( "busId" ) );
            Integer receiveId=CommonUtil.toInteger( requestBody.get( "receiveId" ) );
            List<Map<String, Object>> mapList=cardCouponsApiService.findDuofenCard(busId,receiveId);
            return ServerResponse.createBySuccess(mapList);
        }catch (Exception e){
            return ServerResponse.createByError(ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getMsg());
        }
    }
    @ApiOperation(value = "卡包中卡券信息 美容", notes = "卡包中卡券信息 美容")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "busId", value = "商家id", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "receiveId", value = "卡包id", paramType = "query", required = true, dataType = "int")
    })
    @ResponseBody
    @RequestMapping (value = "/findReceviceAllByMeiRong",method = RequestMethod.POST)
    public ServerResponse findReceviceAllByMeiRong(HttpServletRequest request,
                                          HttpServletResponse response,@RequestBody Map<String,Object> requestBody){
        try {
            Integer busId=CommonUtil.toInteger( requestBody.get( "busId" ) );
            Integer receiveId=CommonUtil.toInteger( requestBody.get( "receiveId" ) );
            List<Map<String, Object>> mapList=cardCouponsApiService.findReceviceAll(busId,receiveId);
            return ServerResponse.createBySuccess(mapList);
        }catch (BusinessException e){
            return ServerResponse.createByError(e.getCode(),e.getMessage());
        }catch (Exception e){
            return ServerResponse.createByError(ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getMsg());
        }
    }

    @ApiOperation(value = "赠送卡包 回滚卡包数据", notes = "赠送卡包 回滚卡包数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "memberId", value = "粉丝id", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "receiveId", value = "卡包id", paramType = "query", required = true, dataType = "int")
    })
    @ResponseBody
    @RequestMapping (value = "/backDuofenCardGet",method = RequestMethod.POST)
    public ServerResponse backDuofenCardGet(HttpServletRequest request,
                                            HttpServletResponse response,@RequestBody Map<String,Object> requestBody){
        try {
            Integer memberId=CommonUtil.toInteger( requestBody.get( "memberId" ) );
            Integer receiveId=CommonUtil.toInteger( requestBody.get( "receiveId" ) );
            cardCouponsApiService.backDuofenCardGet(memberId,receiveId);
            return ServerResponse.createBySuccess();
        }catch (BusinessException e){
            return ServerResponse.createByError(e.getCode(), e.getMessage());
        }
    }

    @ApiOperation(value = "查询游戏卡包信息", notes = "根据商家id查询游戏卡包信息")
    @ApiImplicitParam(name = "busId", value = "商家id", paramType = "query", required = true, dataType = "int")
    @ResponseBody
    @RequestMapping (value = "/gameDuofenCardRecevice",method = RequestMethod.POST)
    public ServerResponse gameDuofenCardRecevice(HttpServletRequest request,
                                                 HttpServletResponse response,@RequestBody Map<String,Object> requestBody){
        try {
            Integer busId=CommonUtil.toInteger( requestBody.get( "busId" ) );
            List<Map<String, Object>> map=cardCouponsApiService.gameDuofenCardRecevice(busId);
            return ServerResponse.createBySuccess(map);
        }catch (Exception e){
            return ServerResponse.createByError(ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getMsg());
        }
    }

    @ApiOperation(value = "游戏领取优惠券", notes = "游戏领取优惠券")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "receiveId", value = "卡包id", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "num", value = "数量", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "memberId", value = "粉丝id", paramType = "query", required = true, dataType = "int")
    })
    @ResponseBody
    @RequestMapping (value = "/getDuofenCardByGame",method = RequestMethod.POST)
    public ServerResponse getDuofenCardByGame(HttpServletRequest request,
                                              HttpServletResponse response,@RequestBody Map<String,Object> requestBody){
        try {
            Integer receiveId=CommonUtil.toInteger( requestBody.get( "receiveId" ) );
            Integer num=CommonUtil.toInteger( requestBody.get( "num" ) );
            Integer memberId=CommonUtil.toInteger( requestBody.get( "memberId" ) );
            cardCouponsApiService.getDuofenCardGame(receiveId,num,memberId);
            return ServerResponse.createBySuccess();
        }catch (BusinessException e){
            return ServerResponse.createByError(e.getCode(), e.getMessage());
        }
    }

    @ApiOperation(value = "卡券核销 并返回卡券id和名称", notes = "卡券核销 并返回卡券id和名称")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "codes", value = "卡券code值", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "storeId", value = "门店", paramType = "query", required = true, dataType = "int")

    })
    @ResponseBody
    @RequestMapping (value = "/verificationCardReturnName",method = RequestMethod.POST)
    public ServerResponse verificationCardReturnName(HttpServletRequest request,
                                             HttpServletResponse response,@RequestBody Map<String,Object> requestBody){
        Map<String,Object> params=new HashMap<>();
        params.put("codes",requestBody.get( "codes" ));
        params.put("storeId",requestBody.get( "storeId" ));
        try {
            Map<String, Object> map=cardCouponsApiService.verificationCard_3(params);
            return ServerResponse.createBySuccess(map);
        }catch (BusinessException e){
            return ServerResponse.createByError(e.getCode(), e.getMessage());
        }
    }

    @ApiOperation(value = "美容小程序（根据粉丝id查询卡券包信息）", notes = "美容（根据粉丝id查询卡券包信息）")
    @ApiImplicitParam(name = "memberId", value = "粉丝id", paramType = "query", required = true, dataType = "int")
    @ResponseBody
    @RequestMapping (value = "/findMeiRongDuofenCardByMemberId",method = RequestMethod.POST)
    public ServerResponse findMeiRongDuofenCardByMemberId(HttpServletRequest request,
                    HttpServletResponse response,@RequestBody Map<String,Object> requestBody){
        try {
            Integer memberId=CommonUtil.toInteger( requestBody.get( "memberId" ) );
            List<Map<String,Object>> list=cardCouponsApiService.findMeiRongDuofenCardByMemberId( memberId );
            return ServerResponse.createBySuccess(list);
        }catch ( Exception e ){
            return ServerResponse.createByError(ResponseEnums.ERROR.getCode(),ResponseEnums.ERROR.getMsg());
        }
    }

    @ApiOperation(value = "美容小程序（根据卡包id和粉丝id查询卡券信息）", notes = "美容小程序（根据卡包id和粉丝id查询卡券信息）")
    @ApiImplicitParams( {
                    @ApiImplicitParam(name = "memberId", value = "粉丝id", paramType = "query", required = true, dataType = "int"),
                    @ApiImplicitParam(name = "receiceId", value = "卡包id", paramType = "query", required = true, dataType = "int")
    } )
    @ResponseBody
    @RequestMapping (value = "/findMeiRongDuofenCardGetByReceiveId",method = RequestMethod.POST)
    public ServerResponse findMeiRongDuofenCardGetByReceiveId(HttpServletRequest request,
                    HttpServletResponse response,@RequestBody Map<String,Object> requestBody){
        try {
            Integer memberId=CommonUtil.toInteger( requestBody.get( "memberId" ) );
            Integer receiceId=CommonUtil.toInteger( requestBody.get( "receiceId" ) );
            List<Map<String,Object>> list=cardCouponsApiService.findMeiRongCardGetByMemberId( memberId,receiceId );
            return ServerResponse.createBySuccess(list);
        }catch ( Exception e ){
            return ServerResponse.createByError(ResponseEnums.ERROR.getCode(),ResponseEnums.ERROR.getMsg());
        }
    }

    @ApiOperation(value = "美容小程序（卡券gId查询卡券信息 门店信息暂时没有）", notes = "美容小程序（卡券gId查询卡券信息）")
    @ApiImplicitParam(name = "gId", value = "领取的卡券id", paramType = "query", required = true, dataType = "int")
    @ResponseBody
    @RequestMapping (value = "/findDuofenCardOne",method = RequestMethod.POST)
    public ServerResponse findDuofenCardOne(HttpServletRequest request,
                    HttpServletResponse response,@RequestBody Map<String,Object> requestBody){
        try {
            Integer gId=CommonUtil.toInteger( requestBody.get( "gId" ) );
            Map<String,Object> map=cardCouponsApiService.findDuofenCardOne(gId);
            return ServerResponse.createBySuccess(map);
        }catch ( Exception e ){
            return ServerResponse.createByError(ResponseEnums.ERROR.getCode(),ResponseEnums.ERROR.getMsg());
        }
    }

    @ApiOperation(value = "美容小程序（卡券gId查询卡券详情信息）", notes = "美容小程序（卡券gId查询卡券详情信息）")
    @ApiImplicitParam(name = "gId", value = "领取的卡券id", paramType = "query", required = true, dataType = "int")
    @ResponseBody
    @RequestMapping (value = "/findCardDetails",method = RequestMethod.POST)
    public ServerResponse findCardDetails(HttpServletRequest request,
                    HttpServletResponse response,@RequestBody Map<String,Object> requestBody){
        try {
            Integer gId=CommonUtil.toInteger( requestBody.get( "gId" ) );
            Map<String,Object> map=cardCouponsApiService.findCardDetails(gId);
            return ServerResponse.createBySuccess(map);
        }catch ( Exception e ){
            return ServerResponse.createByError(ResponseEnums.ERROR.getCode(),ResponseEnums.ERROR.getMsg());
        }
    }




}
