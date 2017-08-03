package com.gt.member.controller;

import com.gt.member.dto.ServerResponse;
import com.gt.member.entity.DuofenCard;
import com.gt.member.entity.DuofenCardGet;
import com.gt.member.entity.DuofenCardReceive;
import com.gt.member.entity.WxCard;
import com.gt.member.enums.ResponseEnums;
import com.gt.member.exception.BusinessException;
import com.gt.member.service.memberApi.CardCouponsApiService;
import com.gt.member.util.CommonUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
@Controller
@RequestMapping("/api/cardCouponseApi")
public class CardCouponseApiController {

    @Autowired
    private CardCouponsApiService cardCouponsApiService;

    @ApiOperation(value = "查询微信优惠券信息", notes = "根据公众号查询微信卡券信息")
    @ApiImplicitParam(name = "publicId", value = "公众号id", paramType = "query", required = true, dataType = "int")
    @ResponseBody
    @GetMapping("/79B4DE7C/findWxCard")
    public ServerResponse findWxCard(HttpServletRequest request,
                                     HttpServletResponse response, Integer publicId) {
        List<Map<String, Object>> cardList = cardCouponsApiService.findWxCard(publicId);
        return ServerResponse.createBySuccess(cardList);
    }

    @ApiOperation(value = "核销微信优惠券", notes = "微信卡券核销返回卡券id和name")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "publicId", value = "公众号id", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "code", value = "卡券code值", paramType = "query", required = true, dataType = "String")
    })
    @ResponseBody
    @Update("/79B4DE7C/findWxCard")
    public ServerResponse wxCardReceiveBackName(HttpServletRequest request,
                                                HttpServletResponse response, Integer publicId, String code) {
        try {
            Map<String, Object> map = cardCouponsApiService.wxCardReceiveBackName(publicId, code);
            return ServerResponse.createBySuccess(map);
        }catch (BusinessException e){
            return ServerResponse.createByError(e.getCode(),e.getMessage());
        }catch (Exception e) {
            return ServerResponse.createByError(ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getDesc());
        }
    }

    @ApiOperation(value = "查询卡券信息", notes = "根据id查询微信卡券信息")
    @ApiImplicitParam(name = "cardId", value = "卡券id", paramType = "query", required = true, dataType = "int")
    @ResponseBody
    @GetMapping("/79B4DE7C/findWxCardById")
    public ServerResponse findWxCardById(HttpServletRequest request,
                                         HttpServletResponse response, Integer cardId) {
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
    @GetMapping("/79B4DE7C/findDuofenCardByMemberIdAndMoney")
    public ServerResponse findDuofenCardByMemberIdAndMoney(HttpServletRequest request,
                                                           HttpServletResponse response, Integer memberId, Integer wxshopId, Double money){
        try {
            List<Map<String, Object>> cardList = cardCouponsApiService.findDuofenCardByMemberIdAndMoney(memberId, wxshopId, money);
            return ServerResponse.createBySuccess(cardList);
        }catch (Exception e){
            return ServerResponse.createByError(ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getDesc());
        }
    }


    @ApiOperation(value = "查询商家拥有的卡包信息", notes = "根据商家id查询商家拥有的卡包信息")
    @ApiImplicitParam(name = "busId", value = "商家id", paramType = "query", required = true, dataType = "int")
    @ResponseBody
    @GetMapping("/79B4DE7C/findReceiveByBusUserId")
    public ServerResponse findReceiveByBusUserId(HttpServletRequest request,
                                                 HttpServletResponse response, Integer busId){
        List<Map<String,Object>> cardList=cardCouponsApiService.findReceiveByBusUserId(busId);
        return ServerResponse.createBySuccess(cardList);
    }


    @ApiOperation(value = "查询本公众号商场投放的包", notes = "根据商家id查询本公众号商场投放的包")
    @ApiImplicitParam(name = "busId", value = "商家id", paramType = "query", required = true, dataType = "int")
    @ResponseBody
    @GetMapping("/79B4DE7C/findReceiveBybusId")
    public ServerResponse findReceiveBybusId(HttpServletRequest request,
                                             HttpServletResponse response, Integer busId){
        List<DuofenCardReceive> duofenCardReceives=cardCouponsApiService.findReceiveBybusId(busId);
        return ServerResponse.createBySuccess(duofenCardReceives);
    }


    @ApiOperation(value = "查询卡券信息", notes = "根据卡包查询卡券信息")
    @ApiImplicitParam(name = "receiveId", value = "多粉卡包id", paramType = "query", required = true, dataType = "int")
    @ResponseBody
    @GetMapping("/79B4DE7C/findCardByReceiveId")
    public ServerResponse findCardByReceiveId(HttpServletRequest request,
                                              HttpServletResponse response,Integer receiveId){
        Map<String,Object> map=cardCouponsApiService.findCardByReceiveId(receiveId);
        return ServerResponse.createBySuccess(map);
    }


    @ApiOperation(value = "分页查询第三方平台下所有优惠券", notes = "查询第三方平台下所有优惠券")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "threeMemberId", value = "第三平台下粉丝id", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "page", value = "页数", paramType = "query", required = true, dataType = "int")
    })
    @ResponseBody
    @GetMapping("/79B4DE7C/findByThreeMemberId")
    public ServerResponse findByThreeMemberId(HttpServletRequest request,
                                              HttpServletResponse response,Integer threeMemberId, Integer page){
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
    @Update("/79B4DE7C/successPayBack")
    public ServerResponse successPayBack(HttpServletRequest request,
                                         HttpServletResponse response,Integer receiveId, Integer num, Integer memberId){
        try {
            cardCouponsApiService.successPayBack(receiveId, num, memberId);
            return ServerResponse.createBySuccess();
        }catch (BusinessException e){
            return ServerResponse.createByError(e.getCode(), e.getMessage());
        }
    }

    @ApiOperation(value = "查询卡券信息", notes = "根据卡包id查询卡券信息 map中key guoqi=1标示该包或该券过期")
    @ApiImplicitParam(name = "receiveId", value = "卡包id", paramType = "query", required = true, dataType = "int")
    @ResponseBody
    @GetMapping("/79B4DE7C/findDuofenCardByReceiveId")
    public ServerResponse findDuofenCardByReceiveId(HttpServletRequest request,
                                                    HttpServletResponse response,Integer receiveId){
        try {
            Map<String, Object> map = cardCouponsApiService.findDuofenCardByReceiveId(receiveId);
            return ServerResponse.createBySuccess(map);
        }catch (BusinessException e){
            return ServerResponse.createByError(e.getCode(), e.getMessage());
        }
    }

    @ApiOperation(value = "卡包投放", notes = "根据卡包id卡包投放")
    @ApiImplicitParam(name = "receiveId", value = "卡包id", paramType = "query", required = true, dataType = "int")
    @ResponseBody
    @Update("/79B4DE7C/publishShelve")
    public ServerResponse publishShelve(HttpServletRequest request,
                                        HttpServletResponse response,Integer receiveId){
        try {
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
    @Update("/79B4DE7C/threeShopGetCard")
    public ServerResponse threeShopGetCard(HttpServletRequest request,
                                           HttpServletResponse response,Integer threeMemberId, Integer memberId, Integer busId, Integer bagId){
        try {
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
    @GetMapping("/79B4DE7C/findUserCardByReceiveId")
    public ServerResponse findUserCardByReceiveId(HttpServletRequest request,
                                                  HttpServletResponse response,Integer memberId, Integer receiveId){
       List<DuofenCardGet> duofenCardGets=cardCouponsApiService.findUserCardByReceiveId(memberId,receiveId);
       return ServerResponse.createBySuccess(duofenCardGets);
    }


    @ApiOperation(value = "查询优惠券商场过期优惠券信息", notes = "根据粉丝id和卡包id查询优惠券商场过期优惠券信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "memberId", value = "粉丝id", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "receiveId", value = "卡包id", paramType = "query", required = true, dataType = "int")
    })
    @ResponseBody
    @GetMapping("/79B4DE7C/findCardOverTime")
    public ServerResponse findCardOverTime(HttpServletRequest request,
                                           HttpServletResponse response,Integer memberId, Integer receiveId){
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
    @GetMapping("/79B4DE7C/findCardReceive")
    public ServerResponse findCardReceive(HttpServletRequest request,
                                          HttpServletResponse response,Integer busId, Integer memberId, Integer page){
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
    @GetMapping("/79B4DE7C/findCardReceiveBuy")
    public ServerResponse findCardReceiveBuy(HttpServletRequest request,
                                             HttpServletResponse response,Integer busId, Integer memberId, Integer page){
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
    @GetMapping("/79B4DE7C/pcBuyReceive")
    public ServerResponse pcBuyReceive(HttpServletRequest request,
                                       HttpServletResponse response,Integer memberId, Integer busId, Integer cardreceiveId){
        try {
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
    @Update("/79B4DE7C/successBuyReceive")
    public ServerResponse successBuyReceive(HttpServletRequest request,
                                            HttpServletResponse response,Integer receiveId, Integer num, Integer memberId){
        try {
            cardCouponsApiService.successBuyReceive(receiveId, num, memberId);
            return ServerResponse.createBySuccess();
        }catch (BusinessException e){
            return ServerResponse.createByError(e.getCode(), e.getMessage());
        }
    }

    @ApiOperation(value = "卡包信息（购买） 美容", notes = "卡包信息（购买） 美容")
    @ApiImplicitParam(name = "busId", value = "商家id", paramType = "query", required = true, dataType = "int")
    @ResponseBody
    @GetMapping("/79B4DE7C/findReceiveByMeiRong")
    public ServerResponse findReceiveByMeiRong(HttpServletRequest request,
                                      HttpServletResponse response,Integer busId){
        try {
            List<Map<String, Object>> mapList=cardCouponsApiService.findReceive(busId);
            return ServerResponse.createBySuccess(mapList);
        }catch (Exception e){
            return ServerResponse.createByError(ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getDesc());
        }
    }

    @ApiOperation(value = "卡包中卡券信息 美容", notes = "卡包中卡券信息 美容")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "busId", value = "商家id", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "receiveId", value = "卡包id", paramType = "query", required = true, dataType = "int")
    })
    @ResponseBody
    @GetMapping("/79B4DE7C/findDuofenCardByMeiRong")
    public ServerResponse findDuofenCardByMeiRong(HttpServletRequest request,
                                                  HttpServletResponse response,Integer busId,Integer receiveId){
        try {
            List<Map<String, Object>> mapList=cardCouponsApiService.findDuofenCard(busId,receiveId);
            return ServerResponse.createBySuccess(mapList);
        }catch (Exception e){
            return ServerResponse.createByError(ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getDesc());
        }
    }
    @ApiOperation(value = "卡包中卡券信息 美容", notes = "卡包中卡券信息 美容")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "busId", value = "商家id", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "receiveId", value = "卡包id", paramType = "query", required = true, dataType = "int")
    })
    @ResponseBody
    @GetMapping("/79B4DE7C/findReceviceAllByMeiRong")
    public ServerResponse findReceviceAllByMeiRong(HttpServletRequest request,
                                          HttpServletResponse response,Integer busId,Integer receiveId){
        try {
            List<Map<String, Object>> mapList=cardCouponsApiService.findReceviceAll(busId,receiveId);
            return ServerResponse.createBySuccess(mapList);
        }catch (BusinessException e){
            return ServerResponse.createByError(e.getCode(),e.getMessage());
        }catch (Exception e){
            return ServerResponse.createByError(ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getDesc());
        }
    }

    @ApiOperation(value = "赠送卡包 回滚卡包数据", notes = "赠送卡包 回滚卡包数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "memberId", value = "粉丝id", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "receiveId", value = "卡包id", paramType = "query", required = true, dataType = "int")
    })
    @ResponseBody
    @Update("/79B4DE7C/backDuofenCardGet")
    public ServerResponse backDuofenCardGet(HttpServletRequest request,
                                            HttpServletResponse response,Integer memberId, Integer receiveId){
        try {
            cardCouponsApiService.backDuofenCardGet(memberId,receiveId);
            return ServerResponse.createBySuccess();
        }catch (BusinessException e){
            return ServerResponse.createByError(e.getCode(), e.getMessage());
        }
    }

    @ApiOperation(value = "查询游戏卡包信息", notes = "根据商家id查询游戏卡包信息")
    @ApiImplicitParam(name = "busId", value = "商家id", paramType = "query", required = true, dataType = "int")
    @ResponseBody
    @Update("/79B4DE7C/gameDuofenCardRecevice")
    public ServerResponse gameDuofenCardRecevice(HttpServletRequest request,
                                                 HttpServletResponse response,Integer busId){
        try {
            List<Map<String, Object>> map=cardCouponsApiService.gameDuofenCardRecevice(busId);
            return ServerResponse.createBySuccess(map);
        }catch (Exception e){
            return ServerResponse.createByError(ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getDesc());
        }
    }

    @ApiOperation(value = "游戏领取优惠券", notes = "游戏领取优惠券")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "receiveId", value = "卡包id", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "num", value = "数量", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "memberId", value = "粉丝id", paramType = "query", required = true, dataType = "int")
    })
    @ResponseBody
    @Update("/79B4DE7C/getDuofenCardByGame")
    public ServerResponse getDuofenCardByGame(HttpServletRequest request,
                                              HttpServletResponse response,Integer receiveId, Integer num, Integer memberId){
        try {
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
    @Update("/79B4DE7C/verificationCardReturnName")
    public ServerResponse verificationCardReturnName(HttpServletRequest request,
                                             HttpServletResponse response,String codes,Integer storeId){
        Map<String,Object> params=new HashMap<>();
        params.put("codes",codes);
        params.put("storeId",storeId);
        try {
            Map<String, Object> map=cardCouponsApiService.verificationCard_3(params);
            return ServerResponse.createBySuccess(map);
        }catch (BusinessException e){
            return ServerResponse.createByError(e.getCode(), e.getMessage());
        }
    }
}
