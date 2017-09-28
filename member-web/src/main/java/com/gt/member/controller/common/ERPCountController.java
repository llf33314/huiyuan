package com.gt.member.controller.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gt.api.util.sign.SignHttpUtils;
import com.gt.member.base.BaseController;
import com.gt.member.exception.BusinessException;
import com.gt.member.service.count.ERPCountService;
import com.gt.entityBo.queryBo.MallAllEntityQuery;
import com.gt.entityBo.queryBo.MallEntityQuery;
import com.gt.member.util.CommonUtil;
import com.gt.member.util.PropertiesUtil;
import com.gt.member.util.RedisCacheUtil;
import com.sun.xml.bind.v2.TODO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * erp 统一结算页面
 * </p>
 *
 * @author pengjiangli
 * @since 2017-08-09
 */
@Api(value = "erp 统一结算页面",description = "erp 统一结算页面")
@Controller
@RequestMapping( "/erpCount" )
public class ERPCountController extends BaseController {

    private static final Logger LOG = LoggerFactory.getLogger( ERPCountController.class );

    @Autowired
    private ERPCountService erpCountService;

    @Autowired
    private RedisCacheUtil redisCacheUtil;

    @Autowired
    private PropertiesUtil propertiesUtil;

    @RequestMapping( value = "/aa" )
    public String aa( HttpServletRequest request, HttpServletResponse response,@RequestParam String orderCode ) {
	MallAllEntityQuery m = new MallAllEntityQuery();
	m.setOrderCode( orderCode );  //订单号
	m.setTotalMoney( 0.01 ); //订单总金额
	List< MallEntityQuery > list = new ArrayList<>();
	MallEntityQuery m1 = new MallEntityQuery();
	m1.setMallId( 1 );  //商品id or 生成序列号
	m1.setNumber( 1 );  // 商品数量
	m1.setTotalMoneyOne( 0.01 );  //商品应付单价格
	m1.setTotalMoneyAll( 0.01 ); //商品订单总价格
	m1.setUserCard( 1 );  //是否能用会员卡打折  0不参与 1参与
	m1.setUseCoupon( 1 );    //是否使用优惠券 0不参与 1参与
	m1.setUseFenbi( 1 );    //是否使用粉币 0不参与 1参与
	m1.setUserJifen( 1 );  //是否使用积分 0不参与 1参与
	m1.setUseLeague( 1 );     //商品是否能使用联盟卡 0不参与 1参与
	list.add( m1 );

	m.setUcType( 103 );   //消费类型  请查看A003消费类型  没有请添加
	m.setBusId( 42 );  //商家主账户id
	m.setShopId( 17 );  //门店订单
	m.setSuccessNoticeUrl( "http://www.baid.com" );  //支付成功 通知地址
	m.setJumpUrl( "http://www.baid.com" );  //支付成功跳转地址
	m.setJumphttpPOST( 0 );   //支付成功回调地址   0代表SignHttpUtils.postByHttp   1代表 SignHttpUtils.WxmppostByHttp()
	m.setDerateMoney( 0.0 );   //减免金额

	m.setMalls( list );

	Map< String,Object > redisMap = new HashMap<>();
	redisMap.put( "redisKey", orderCode );
	redisMap.put( "redisValue", JSONObject.toJSONString( m ) );
	redisMap.put( "setime", 300 );
	try {
	    SignHttpUtils.WxmppostByHttp( propertiesUtil.getWxmp_home() + "/8A5DA52E/redis/SetExApi.do", redisMap, propertiesUtil.getWxmpsignKey() );
	}catch ( Exception e ){
	    LOG.error( "存储redis值异常",e );
	}
	request.setAttribute( "orderCodeKey", m.getOrderCode() );
	return "index";
    }

    @ApiOperation( value = "erp统一跳转结算页面入口", notes = "erp统一跳转结算页面入口" )
    @RequestMapping( value = "/countErpIndex")
    public String countErpIndex( HttpServletRequest request, HttpServletResponse response, @RequestParam String orderCodeKey ) {
	try {
	    Map< String,Object > redisMap = new HashMap<>();
	    redisMap.put( "redisKey", orderCodeKey );
	    String obj=SignHttpUtils.WxmppostByHttp( propertiesUtil.getWxmp_home()+"/8A5DA52E/redis/getExApi.do", redisMap, propertiesUtil.getWxmpsignKey() );

	    JSONObject json=JSONObject.parseObject(obj  );

	    MallAllEntityQuery mallAllEntityQuery= JSONObject.parseObject(json.getString( "data" ) ,MallAllEntityQuery.class);
	    request.setAttribute( "mallAllEntityQueryStr", json.getString( "data" ) );
	    request.setAttribute( "mallAllEntityQuery", mallAllEntityQuery );
	    request.setAttribute( "member_count","member_count_"+mallAllEntityQuery.getOrderCode() );
	    request.setAttribute("member_socketHost", propertiesUtil.getSocket_url());
	} catch ( Exception e ) {
	    e.printStackTrace();
	    LOG.error( "转换异常",e );
	    request.setAttribute("error", 0);
	}
	return "count/erp_count";
    }

    /**
     * 查询会员信息
     *
     * @param request
     * @param response
     * @param busId
     * @param shopId
     * @param cardNo
     */
    @RequestMapping( value = "/findMemberERP" )
    public void findMemberERP( HttpServletRequest request, HttpServletResponse response, @RequestParam( "busId" ) Integer busId, @RequestParam( "shopId" ) Integer shopId,
		    @RequestParam( "cardNo" ) String cardNo ) throws IOException {
	Map< String,Object > map = erpCountService.findMemberByERP( busId, shopId, cardNo );
	CommonUtil.write( response, map );
    }

    /**
     * 计算会员
     *
     * @param request
     * @param response
     */
    @RequestMapping( value = "/erpCountMoney" )
    public void erpCountMoney( HttpServletRequest request, HttpServletResponse response, @RequestParam( "mallAllEntityQuery" ) String mallAllEntityQuery,
		    @RequestParam( "param" ) String param ) throws IOException {
	try {
	    Map< String,Object > map = erpCountService.erpCountMoney( mallAllEntityQuery, param );
	    CommonUtil.writeToAilibaba( response, map );
	} catch ( Exception e ) {
	    e.printStackTrace();
	}
    }

    /**
     * 储值卡支付
     *
     * @param request
     * @param response
     * @param mallAllEntityQuery
     * @param param
     */
    @RequestMapping( value = "/erpChuzhiPayMent" )
    public void erpChuzhiPayMent( HttpServletRequest request, HttpServletResponse response, @RequestParam( "mallAllEntityQuery" ) String mallAllEntityQuery,
		    @RequestParam( "param" ) String param ) throws IOException {
	Map< String,Object > map = new HashMap<>();
	try {
	    erpCountService.erpChuzhiPayMent( mallAllEntityQuery, param );
	    map.put( "code", 0 );
	    map.put( "msg", "支付成功" );
	} catch ( BusinessException e ) {
	    map.put( "code", e.getCode() );
	    map.put( "msg", e.getMessage() );
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
    @RequestMapping( value = "/saomaPayMent" )
    public void saomaPayMent( HttpServletRequest request, HttpServletResponse response, @RequestParam( "mallAllEntityQuery" ) String mallAllEntityQuery,
		    @RequestParam( "param" ) String param ) throws IOException {
	Map< String,Object > map = new HashMap<>();
	try{
	    map=erpCountService.saomaPayMent(mallAllEntityQuery,param);
	} catch ( BusinessException e ) {
	    map.put( "code", e.getCode() );
	    map.put( "msg", e.getMessage() );
	}
	CommonUtil.write( response, map );
    }

    /***
     * 微信钱包支付
     * @param request
     * @param response
     * @param param
     */
    @RequestMapping( value = "/saomaQianBaoPay" )
    public void saomaQianBaoPay(HttpServletRequest request, HttpServletResponse response,
		    @RequestParam( "mallAllEntityQuery" ) String mallAllEntityQuery,
		    @RequestParam( "param" ) String param )throws IOException{
	Map< String,Object > map = new HashMap<>();
	try{
	    map=erpCountService.saomaQianBaoPay(mallAllEntityQuery,param);
	} catch ( BusinessException e ) {
	    map.put( "code", e.getCode() );
	    map.put( "msg", e.getMessage() );
	}
	CommonUtil.write( response, map );
    }

    /**
     * 支付成功回调
     * @param request
     * @param response
     * @param params
     */
    @RequestMapping( value = "/79B4DE7C/successPay" )
    public void successPay(HttpServletRequest request, HttpServletResponse response,@RequestBody Map<String,Object> params)throws IOException{
	Map<String ,Object> map=erpCountService.successPay(params);
	CommonUtil.write( response,map );
    }

}
