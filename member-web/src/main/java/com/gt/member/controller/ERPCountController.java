package com.gt.member.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gt.member.base.BaseController;
import com.gt.member.exception.BusinessException;
import com.gt.member.service.count.ERPCountService;
import com.gt.member.service.count.queryBo.MallAllEntityQuery;
import com.gt.member.service.count.queryBo.MallEntityQuery;
import com.gt.member.service.memberApi.entityBo.MallAllEntity;
import com.gt.member.service.memberApi.entityBo.MallEntity;
import com.gt.member.service.memberApi.entityBo.MallNotShopEntity;
import com.gt.member.service.memberApi.entityBo.MallShopEntity;
import com.gt.member.util.CommonUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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


    private static final Logger LOG= LoggerFactory.getLogger( ERPCountController.class );

    @Autowired
    private ERPCountService erpCountService;

    @RequestMapping(value = "/aa")
    public String aa(HttpServletRequest request,
		    HttpServletResponse response){

	MallAllEntityQuery m=new MallAllEntityQuery();
	m.setOrderCode("ME123456789");  //订单号
	m.setTotalMoney(100.0); //订单总金额


	Map<Integer, MallEntityQuery> map=new HashMap<>(  );

	MallEntityQuery m1=new MallEntityQuery();
	m1.setMallId(1);  //商品id or 生成序列号
	m1.setNumber(2);  // 商品数量
	m1.setTotalMoneyOne(50.0);  //商品应付单价格
	m1.setTotalMoneyAll(100.0); //商品订单总价格

	m1.setUserCard(1);  //是否能用会员卡打折  0不参与 1参与

	m1.setUseCoupon(1);    //是否使用优惠券 0不参与 1参与

	m1.setUseFenbi(1);    //是否使用粉币 0不参与 1参与

	m1.setUserJifen (1);  //是否使用积分 0不参与 1参与

	m1.setUseLeague(1);     //商品是否能使用联盟卡 0不参与 1参与

	map.put( m1.getMallId(),m1 );

	m.setMalls(map);  //商品订单详情

	m.setUcType(103);   //消费类型  请查看A003消费类型  没有请添加
	m.setBusId(42);  //商家主账户id
	m.setShopId(17);  //门店订单

	m.setSuccessNoticeUrl("http://www.baid.com");  //支付成功 通知地址
	m.setJumpUrl( "http://www.baid.com" );  //支付成功跳转地址

	return indexCountErp(request,response,m);
    }

    @ApiOperation(value = "erp统一跳转结算页面入口", notes = "erp统一跳转结算页面入口")
    @RequestMapping(value = "/countErpIndex")
    public String  indexCountErp(HttpServletRequest request,
		    HttpServletResponse response,@RequestBody MallAllEntityQuery mallAllEntityQuery) {
	request.setAttribute( "mallAllEntityQueryStr",JSONObject.toJSONString( mallAllEntityQuery ) );
	request.setAttribute( "mallAllEntityQuery",mallAllEntityQuery );
	return "/count/erp_count";
    }

    /**
     * 查询会员信息
     * @param request
     * @param response
     * @param busId
     * @param shopId
     * @param cardNo
     */
    @RequestMapping(value = "/findMemberERP")
    public void findMemberERP(HttpServletRequest request,
		    HttpServletResponse response,@RequestParam("busId") Integer busId,@RequestParam("shopId")Integer shopId,
		    @RequestParam("cardNo")String cardNo)throws IOException {
	Map<String,Object> map=erpCountService.findMemberByERP(busId,shopId,cardNo  );
	System.out.println( JSON.toJSONString(map));
	CommonUtil.write(response, map);
    }

    /**
     * 计算会员
     * @param request
     * @param response
     */
    @RequestMapping(value = "/erpCountMoney")
    public void erpCountMoney(HttpServletRequest request,
		    HttpServletResponse response,@RequestParam("mallAllEntityQuery") String mallAllEntityQuery,@RequestParam("param") String param)throws IOException{
        try {
	    Map< String,Object > map = erpCountService.erpCountMoney( mallAllEntityQuery, param );
	    CommonUtil.writeToAilibaba( response, map );
	}catch ( Exception e ){
            e.printStackTrace();
	}
    }

    /**
     * 储值卡支付
     * @param request
     * @param response
     * @param mallAllEntityQuery
     * @param param
     */
    @RequestMapping(value = "/erpChuzhiPayMent")
    public void erpChuzhiPayMent(HttpServletRequest request,
		    HttpServletResponse response,@RequestParam("mallAllEntityQuery") String mallAllEntityQuery,@RequestParam("param") String param)throws IOException{
	Map<String,Object> map=new HashMap<>(  );
        try {
	    erpCountService.erpChuzhiPayMent( mallAllEntityQuery, param );
	    map.put( "code",0 );
	    map.put( "msg","支付成功" );
	}catch ( BusinessException e ){
	    map.put( "code",e.getCode() );
	    map.put( "msg",e.getMessage() );
	}
	CommonUtil.write( response, map );
    }

    /***
     * 扫码支付
     * @param request
     * @param response
     * @param mallAllEntityQuery
     * @param param
     */
    @RequestMapping(value = "/saomaPayMent")
    public void saomaPayMent(HttpServletRequest request,
		    HttpServletResponse response,@RequestParam("mallAllEntityQuery") String mallAllEntityQuery,@RequestParam("param") String param){

    }

}
