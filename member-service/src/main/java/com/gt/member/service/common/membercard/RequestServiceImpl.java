package com.gt.member.service.common.membercard;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gt.api.bean.session.BusUser;
import com.gt.api.bean.session.WxPublicUsers;
import com.gt.api.enums.ResponseEnums;
import com.gt.api.util.HttpClienUtils;
import com.gt.api.util.KeysUtil;
import com.gt.api.util.RequestUtils;
import com.gt.api.util.sign.SignHttpUtils;
import com.gt.member.enums.ResponseMemberEnums;
import com.gt.member.exception.BusinessException;
import com.gt.member.util.CommonUtil;
import com.gt.member.constant.CommonConst;
import com.gt.member.enums.ResponseMemberEnums;
import com.gt.member.exception.BusinessException;
import com.gt.member.util.CommonUtil;
import com.gt.member.util.EncryptUtil;
import com.gt.member.util.PropertiesUtil;
import com.gt.util.entity.param.fenbiFlow.AdcServicesInfo;
import com.gt.util.entity.param.fenbiFlow.BusFlow;
import com.gt.util.entity.param.pay.ApiEnterprisePayment;
import com.gt.util.entity.param.pay.PayWay;
import com.gt.util.entity.param.pay.SubQrPayParams;
import com.gt.util.entity.param.sms.NewApiSms;
import com.gt.util.entity.param.sms.OldApiSms;
import com.gt.util.entity.param.wx.QrcodeCreateFinal;
import com.gt.util.entity.param.wx.SendWxMsgTemplate;
import com.gt.util.entity.param.wxcard.CodeConsume;
import com.gt.util.entity.result.shop.WsWxShopInfo;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.gt.util.entity.param.wx.WxJsSdk;
import com.gt.util.entity.param.wxcard.CodeConsume;
import com.gt.util.entity.result.shop.WsWxShopInfoExtend;
import com.gt.util.entity.result.wx.WxJsSdkResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/10/24.
 */
@Service
@Slf4j
public class RequestServiceImpl implements RequestService {

    private final static Logger LOG = LoggerFactory.getLogger( RequestServiceImpl.class );

    //微信卡券核销
    private final String CODE_CONSUME = "/8A5DA52E/wxcardapi/6F6D9AD2/79B4DE7C/codeConsume.do";

    //发送短信
    private final String SEND_SMS = "/8A5DA52E/smsapi/6F6D9AD2/79B4DE7C/sendSmsOld.do";

    private final String SEND_SMS_NEW = "/8A5DA52E/smsapi/6F6D9AD2/79B4DE7C/sendSmsNew.do";

    private final static String SEND_WXMSG = "/8A5DA52E/wxpublicapi/6F6D9AD2/79B4DE7C/sendWxMsgTemplate.do";

    private static final String GETWXPULICMSG = "/8A5DA52E/busUserApi/getWxPulbicMsg.do";

    //验证主账户信息
    private final static String VERSION_BUS_PID = "/8A5DA52E/childBusUserApi/VersionBusPid.do";

    private final static String CHANGE_FLOW_URL = "/8A5DA52E/fenbiflow/6F6D9AD2/79B4DE7C/adcServices.do";

    //查询门店信息
    private final static String WXSHOP_BYBUSID = "/8A5DA52E/shopapi/6F6D9AD2/79B4DE7C/queryWxShopByBusId.do";

    private final static String PAY_API = "/8A5DA52E/payApi/6F6D9AD2/79B4DE7C/payapi.do";

    private final static String POWER_API = "/8A5DA52E/busPowerApi/getPowerApi.do";

    private final static String ENTERPRISE_PAY_MENT = "/8A5DA52E/payApi/6F6D9AD2/79B4DE7C/enterprisePayment.do";

    private final static String WX_SHARE = "/8A5DA52E/wxphone/6F6D9AD2/79B4DE7C/wxjssdk.do";

    private final static String GETVIDEOURL = "/8A5DA52E/videoCourceApi/getVoiceUrl.do";

    private final static String AREAPHONE="/8A5DA52E/areaPhoneApi/selectList.do";

    private final static String PAY_TYPE="/8A5DA52E/payApi/6F6D9AD2/79B4DE7C/getPayWay.do";

    private final static String SELECTMAINSHOPBYBUSID="/8A5DA52E/shopapi/6F6D9AD2/79B4DE7C/selectMainShopByBusId.do";

    private final static String GETBUSUSERAPI="/8A5DA52E/busUserApi/getBusUserApi.do";

    private final static String GETBUSFLOWSBYUSERID="/8A5DA52E/fenbiflow/6F6D9AD2/79B4DE7C/getBusFlowsByUserId.do";

    private final static String GETWXPUBLICUSERS="/8A5DA52E/wxpublicapi/6F6D9AD2/79B4DE7C/selectByUserId.do";

    private final static String SELECTTEMPOBJBYBUSID="/8A5DA52E/wxpublicapi/6F6D9AD2/79B4DE7C/selectTempObjByBusId.do";

    private final static String GETSHOPBYID="/8A5DA52E/shopapi/6F6D9AD2/79B4DE7C/getShopById.do";

    private final static String GETMAINBUSID="/8A5DA52E/childBusUserApi/getMainBusId.do";

    private final static String QUERYBASISBYNAME="8A5DA52E/shopapi/6F6D9AD2/79B4DE7C/queryBasisByName.do";

    private final static String NEW_QRCODE_CREATE_FINAL="/8A5DA52E/wxpublicapi/6F6D9AD2/79B4DE7C/newqrcodeCreateFinal.do";


    public String codeConsume( String cardId, String code, Integer busId ) throws Exception {
	try {
	    LOG.error( "codeConsume请求李逢喜参数:"+cardId+","+code+","+busId);
	    RequestUtils<CodeConsume > requestUtils=new RequestUtils<>(  );
	    CodeConsume codeConsume=new CodeConsume();
	    String url=PropertiesUtil.getWxmp_home()+CODE_CONSUME;
	    codeConsume.setCard_id(  cardId );
	    codeConsume.setCode(  code );
	    codeConsume.setBusId( busId );
	    String result = HttpClienUtils.reqPostUTF8( JSONObject.toJSONString( requestUtils ), url, String.class, PropertiesUtil.getWxmpsignKey() );
	    return result;
	} catch ( Exception e ) {
	    LOG.error( "卡券核销异常",e );
	    throw new Exception();
	}

    }

    public String sendSms( RequestUtils< OldApiSms > requestUtils ) {
	String url = PropertiesUtil.getWxmp_home() + SEND_SMS;
	String smsStr = HttpClienUtils.reqPostUTF8( JSONObject.toJSONString( requestUtils ), url, String.class, PropertiesUtil.getWxmpsignKey() );
	return smsStr;
    }

    public String sendSmsNew( RequestUtils< NewApiSms > requestUtils ) {
	log.error( "sendSmsNew短信参数:"+JSONObject.toJSONString( requestUtils ) );
	String url = PropertiesUtil.getWxmp_home() + SEND_SMS_NEW;
	String smsStr = HttpClienUtils.reqPostUTF8( JSONObject.toJSONString( requestUtils ), url, String.class, PropertiesUtil.getWxmpsignKey() );
	return smsStr;
    }

    public void setSendWxmsg( SendWxMsgTemplate sendWxMsgTemplate ) {
	LOG.error( "setSendWxmsg请求李逢喜参数:"+JSONObject.toJSONString( sendWxMsgTemplate ));
	RequestUtils< SendWxMsgTemplate > requestUtils = new RequestUtils<>();
	String url = PropertiesUtil.getWxmp_home() + SEND_WXMSG;
	String smsStr = HttpClienUtils.reqPostUTF8( JSONObject.toJSONString( requestUtils ), url, String.class, PropertiesUtil.getWxmpsignKey() );

    }

    public void getWxPulbicMsg( Integer busId ) throws BusinessException {
	try {
	    Map< String,Object > getWxPublicMap = new HashMap<>();
	    getWxPublicMap.put( "busId", busId );
	    String wxpublic = SignHttpUtils.WxmppostByHttp( PropertiesUtil.getWxmp_home() + GETWXPULICMSG, getWxPublicMap, PropertiesUtil.getWxmpsignKey() );
	    JSONObject json = JSONObject.parseObject( wxpublic );
	    Integer code = CommonUtil.toInteger( json.get( "code" ) );
	    if ( code == 0 ) {
		Object guoqi = json.get( "guoqi" );
		if ( CommonUtil.isNotEmpty( guoqi ) ) {
		    //商家已过期
		    throw new BusinessException( ResponseMemberEnums.OVERDUE_BUSUSER );
		}
	    } else {
		throw new BusinessException( code, CommonUtil.toString( json.get( "msg" ) ) );
	    }
	} catch ( BusinessException e ) {
	    throw e;
	} catch ( Exception e ) {
	    throw new BusinessException( ResponseEnums.ERROR );
	}
    }


    public Integer getPowerApi(Integer status,Integer busId,Double powNum,String remarks){
	try {
	    LOG.error( "请求陈丹参数:消费状态:"+status+"  ，商家"+busId+"  ，粉币值"+powNum );
	    Map< String,Object > map = new HashMap<>();
	    map.put( "status", status );
	    map.put( "busId", busId );
	    map.put( "powNum", powNum );
	    map.put( "model", 3 );
	    map.put( "remarks", remarks );
	    String url = PropertiesUtil.getWxmp_home() + POWER_API;
	    String returnMsg = SignHttpUtils.WxmppostByHttp( url, map, PropertiesUtil.getWxmpsignKey() );
	    if( CommonUtil.isNotEmpty( returnMsg )){
		Map<String,Object> returnParam= JSON.parseObject( returnMsg,Map.class );
		if(0!=CommonUtil.toInteger( returnParam.get( "code" ) )){
		    LOG.error( "调用陈丹粉币接口："+JSON.toJSONString( returnParam ) );
		}
		return CommonUtil.toInteger( returnParam.get( "code" ) );
	    }
	}catch ( Exception e ){
	    LOG.error( "调用扣除粉币支付异常",e );
	}
	return 1;


    }

    /**
     * 子账号商家输入主商家的用户名和密码进行验证
     *
     * @param userName 用户名
     * @param userPwd  密码
     * @param childId  子账户id
     *
     * @return
     */
    public boolean verificationBusUser( String userName, String userPwd, Integer childId ) throws BusinessException {
	try {
	    String url = PropertiesUtil.getWxmp_home() + VERSION_BUS_PID;
	    Map< String,Object > map = new HashMap<>();
	    map.put( "login_name", userName );
	    map.put( "password", userPwd );
	    map.put( "childId", childId );
	    String result = SignHttpUtils.WxmppostByHttp( url, map, PropertiesUtil.getWxmpsignKey() );
	    JSONObject jsonObject = JSONObject.parseObject( result );
	    if ( "0".equals( jsonObject.getString( "code" ) ) ) {
		return true;
	    } else {
		throw new BusinessException( ResponseMemberEnums.VERIFICATION_BUSUSER.getCode(), jsonObject.getString( "msg" ) );
	    }

	} catch ( Exception e ) {
	    throw new BusinessException( ResponseEnums.ERROR );
	}
    }

    public String changeFlow( RequestUtils< AdcServicesInfo > requestUtils ) {
	String url = PropertiesUtil.getWxmp_home() + CHANGE_FLOW_URL;
	String changeFlowStr = HttpClienUtils.reqPostUTF8( JSONObject.toJSONString( requestUtils ), url, String.class, PropertiesUtil.getWxmpsignKey() );
	return changeFlowStr;
    }

    public WsWxShopInfo findMainShop(Integer busId){
	LOG.error( "请求主门店信息参数:"+busId);
	String url = PropertiesUtil.getWxmp_home() + SELECTMAINSHOPBYBUSID;
	LOG.error( "请求地址:"+url );
	RequestUtils< Integer > requestUtils = new RequestUtils<>();
	requestUtils.setReqdata( busId );
	String shopStr = HttpClienUtils.reqPostUTF8( JSONObject.toJSONString( requestUtils ), url, String.class, PropertiesUtil.getWxmpsignKey() );
	JSONObject json = JSON.parseObject( shopStr );
	if ( "0".equals( json.getString( "code" ) ) ) {
	    WsWxShopInfo  wsWxShopInfo = JSON.parseObject( json.getString( "data" ), WsWxShopInfo.class );
	    return wsWxShopInfo;
	} else {
	    throw new BusinessException( ResponseMemberEnums.QUERY_SHOP_BUSID );
	}
    }

    public WsWxShopInfo getShopById(Integer shopId){
	LOG.error( "调用李逢喜请求门店信息参数:"+shopId);
	String url = PropertiesUtil.getWxmp_home() + SELECTMAINSHOPBYBUSID;
	LOG.error( "请求地址:"+url );
	RequestUtils< Integer > requestUtils = new RequestUtils<>();
	requestUtils.setReqdata( shopId );
	String shopStr = HttpClienUtils.reqPostUTF8( JSONObject.toJSONString( requestUtils ), url, String.class, PropertiesUtil.getWxmpsignKey() );
	JSONObject json = JSON.parseObject( shopStr );
	if ( "0".equals( json.getString( "code" ) ) ) {
	    WsWxShopInfo  wsWxShopInfo = JSON.parseObject( json.getString( "data" ), WsWxShopInfo.class );
	    return wsWxShopInfo;
	} else {
	    throw new BusinessException( ResponseMemberEnums.QUERY_SHOP_BUSID );
	}
    }

    public List< WsWxShopInfoExtend > findShopsByBusId( Integer busId ) {
	LOG.error( "请求当前用户管理的门店信息参数1:"+busId);
	String url = PropertiesUtil.getWxmp_home() + WXSHOP_BYBUSID;
	RequestUtils< Integer > requestUtils = new RequestUtils<>();
	requestUtils.setReqdata( busId );
	String shopStr = HttpClienUtils.reqPostUTF8( JSONObject.toJSONString( requestUtils ), url, String.class, PropertiesUtil.getWxmpsignKey() );
	JSONObject json = JSON.parseObject( shopStr );
	if ( "0".equals( json.getString( "code" ) ) ) {
	    List< WsWxShopInfoExtend > wxShopInfoExtends = JSON.parseArray( json.getString( "data" ), WsWxShopInfoExtend.class );
	    return wxShopInfoExtends;
	} else {
	    throw new BusinessException( ResponseMemberEnums.QUERY_SHOP_BUSID );
	}
    }



    public List< Map > findShopAllByBusId( Integer busId ) {
	LOG.error( "请求当前用户管理的门店信息参数:"+busId);
	RequestUtils< Integer > requestUtils = new RequestUtils< Integer >();
	requestUtils.setReqdata( busId );
	String url = PropertiesUtil.getWxmp_home() + WXSHOP_BYBUSID;
	String returnData = HttpClienUtils.reqPostUTF8( JSONObject.toJSONString( requestUtils ), url, String.class, PropertiesUtil.getWxmpsignKey() );
	JSONObject json = JSON.parseObject( returnData );
	if ( "0".equals( json.getString( "code" ) ) ) {
	    List< Map > mapList = JSONArray.parseArray( json.getString( "data" ), Map.class );
	    return mapList;
	} else {
	    throw new BusinessException( ResponseMemberEnums.QUERY_SHOP_BUSID );
	}
    }

    /**
     * 微信支付包 多粉钱包支付
     *
     * @return
     */
    public String payApi( SubQrPayParams subQrPayParams ) throws Exception {
	String obj = KeysUtil.getEncString( JSON.toJSONString( subQrPayParams ) );
	String url = PropertiesUtil.getWxmp_home() + PAY_API+"?obj="+obj;
	return url;
    }

    public String loginImg( Integer busId ) {
	return null;
    }



    public Map<String,Object> getPowerApiMsg( Integer status, Integer busId, Double powNum, String remarks ) {
	try {
	    Map< String,Object > map = new HashMap<>();
	    map.put( "status", status );
	    map.put( "busId", busId );
	    map.put( "powNum", powNum );
	    map.put( "model", 3 );
	    map.put( "remarks", remarks );
	    String url = PropertiesUtil.getWxmp_home() + POWER_API;
	    String returnMsg = SignHttpUtils.WxmppostByHttp(  url, map, PropertiesUtil.getWxmpsignKey() );
	    if ( CommonUtil.isNotEmpty( returnMsg ) ) {
		Map< String,Object > returnParam = JSON.parseObject( returnMsg, Map.class );
		return returnParam;
	    }
	} catch ( Exception e ) {
	    LOG.error( "调用扣除粉币支付异常", e );
	}
	return null;
    }



    public Map< String,Object > enterprisePayment( RequestUtils< ApiEnterprisePayment > requestUtils ) {
	String url = PropertiesUtil.getWxmp_home() + ENTERPRISE_PAY_MENT;
	String returnData = HttpClienUtils.reqPostUTF8( JSONObject.toJSONString( requestUtils ), url, String.class, PropertiesUtil.getWxmpsignKey() );
	Map< String,Object > map = JSON.parseObject( returnData, Map.class );
	return map;

    }



    public WxJsSdkResult wxShare( Integer publicId, String url ) {
	String wxshareUrl = PropertiesUtil.getWxmp_home() + WX_SHARE;
	RequestUtils< WxJsSdk > requestUtils = new RequestUtils<>();
	WxJsSdk wxJsSdk = new WxJsSdk();
	wxJsSdk.setPublicId( publicId );
	wxJsSdk.setUrl( url );
	requestUtils.setReqdata( wxJsSdk );
	String returnData = HttpClienUtils.reqPostUTF8( JSONObject.toJSONString( requestUtils ), wxshareUrl, String.class, PropertiesUtil.getWxmpsignKey() );
	JSONObject json = JSON.parseObject( returnData );
	if ( "0".equals( json.getString( "code" ) ) ) {
	    WxJsSdkResult wxJsSdkResult = JSONObject.parseObject( json.getString( "data" ), WxJsSdkResult.class );
	    return wxJsSdkResult;
	}
	return null;
    }

    public String getVideoUrl( Integer model ) {
	try {
	    String memberVideo = "";
	    switch ( model ) {
		case 60:
		    memberVideo = CommonConst.MEMBER_VIDEO_URL_60;
		    return memberVideo;
		case 107:
		    memberVideo = CommonConst.MEMBER_VIDEO_URL_107;
		    return memberVideo;
		default:
		    break;
	    }

	    if ( CommonUtil.isEmpty( memberVideo ) ) {
		Map< String,Object > map = new HashMap<>();
		map.put( "courceModel", model );
		String url = PropertiesUtil.getWxmp_home() + GETVIDEOURL;
		String returnMsg = SignHttpUtils.WxmppostByHttp( url, map, PropertiesUtil.getWxmpsignKey() );
		if ( CommonUtil.isNotEmpty( returnMsg ) ) {
		    Map< String,Object > returnParam = JSON.parseObject( returnMsg, Map.class );
		    if ( "0".equals( CommonUtil.toString( returnParam.get( "code" ) ) ) ) {
			JSONObject jsonObject = JSON.parseObject( CommonUtil.toString( returnParam.get( "data" ) ) );
			switch ( model ) {
			    case 60:
				CommonConst.MEMBER_VIDEO_URL_60 = CommonUtil.toString( jsonObject.get( "voiceUrl" ) );
				break;
			    case 107:
				CommonConst.MEMBER_VIDEO_URL_107 = CommonUtil.toString( jsonObject.get( "voiceUrl" ) );
				break;
			    default:
				break;
			}
			return CommonUtil.toString( jsonObject.get( "voiceUrl" ) );
		    }
		}
	    }
	    return memberVideo;
	} catch ( Exception e ) {
	    LOG.error( "调用会员视频异常", e );
	}
	return null;
    }

    public List<Map> findAreaPhone(){
	try {
	    String url = PropertiesUtil.getWxmp_home() + AREAPHONE;
	    String returnMsg = SignHttpUtils.WxmppostByHttp( url, null, PropertiesUtil.getWxmpsignKey() );
	    if ( CommonUtil.isNotEmpty( returnMsg ) ) {
		Map< String,Object > json = JSON.parseObject( returnMsg, Map.class );
		if("0".equals( CommonUtil.toString( json.get( "code" ) ) )){
		    return JSONArray.parseArray( CommonUtil.toString( json.get( "data" ) ) ,Map.class );
		}

	    }
	}catch ( Exception e ){
	    LOG.error( "调用陈丹区号异常",e );
	}
	return null;
    }


    public List<Map<String,Object>> getPayType(Integer busId,Integer type){
        LOG.error( "查询支付方式接口参数:"+busId+" 请求类型 "+type );
	List<Map<String,Object>> list=new ArrayList<>(  );
	RequestUtils< Integer > requestUtils = new RequestUtils< Integer >();
	requestUtils.setReqdata( busId );
	String url = PropertiesUtil.getWxmp_home() + PAY_TYPE;
	String returnData = HttpClienUtils.reqPostUTF8( JSONObject.toJSONString( requestUtils ), url, String.class, PropertiesUtil.getWxmpsignKey() );
	JSONObject json = JSON.parseObject( returnData );
	if ( "0".equals( json.getString( "code" ) ) ) {
	    PayWay payWay = JSONObject.parseObject( json.getString( "data" ), PayWay.class );
	    if(payWay.getWxpay()==0 && type==1){
		Map<String,Object> map=new HashMap<>(  );
		map.put( "payType", 1 );  //微信支付
		map.put( "name","微信" );
		list.add( map );
	    }

	    if(payWay.getAlipay()==0 && type==99){
		Map<String,Object> map=new HashMap<>(  );
		map.put( "payType", 2 );  //支付宝支付
		map.put( "name","支付宝" );
		list.add( map );
	    }

//	    if(payWay.getDfpay()==0){
//		Map<String,Object> map=new HashMap<>(  );
//		map.put( "payType", 3 );  //微信支付
//		map.put( "name","多粉钱包" );
//		list.add( map );
//	    }
	}
	return list;
    }


    public BusUser findBususer(Integer busId){
	try {
	    String url = PropertiesUtil.getWxmp_home() + GETBUSUSERAPI;
	    Map<String,Object> map=new HashMap<>(  );
	    map.put( "userId",busId );
	    String returnMsg = SignHttpUtils.WxmppostByHttp( url, map, PropertiesUtil.getWxmpsignKey() );
	    if ( CommonUtil.isNotEmpty( returnMsg ) ) {
		Map< String,Object > json = JSON.parseObject( returnMsg, Map.class );
		if("0".equals( CommonUtil.toString( json.get( "code" ) ) )){
		  return JSONObject.parseObject( CommonUtil.toString( json.get( "data" ) ),BusUser.class );
		}else{
		    LOG.error( "调用陈丹商家信息异常"+returnMsg );
		}

	    }
	}catch ( Exception e ){
	    LOG.error( "调用陈丹商家信息异常",e );
	}
	return null;

    }

    public List<BusFlow > getBusFlowsByUserId(Integer busId){
	LOG.error( "查询李逢喜商家流量接口参数:"+busId);
	List<Map<String,Object>> list=new ArrayList<>(  );
	RequestUtils< Integer > requestUtils = new RequestUtils< Integer >();
	requestUtils.setReqdata( busId );
	String url = PropertiesUtil.getWxmp_home() + GETBUSFLOWSBYUSERID;
	String returnData = HttpClienUtils.reqPostUTF8( JSONObject.toJSONString( requestUtils ), url, String.class, PropertiesUtil.getWxmpsignKey() );
	JSONObject json = JSON.parseObject( returnData );
	if ( "0".equals( json.getString( "code" ) ) ) {
	    List<BusFlow> returnList = JSONArray.parseArray( json.getString( "data" ), BusFlow.class );
	    return returnList;
	}else{
	    LOG.error( "查询李逢喜商家流量接口返回参数:"+returnData);
	    return null;
	}
    }

    public WxPublicUsers findWxPublicUsersByBusId(Integer busId){
	LOG.error( "查询李逢喜公众号接口参数:"+busId);
	List<Map<String,Object>> list=new ArrayList<>(  );
	RequestUtils< Integer > requestUtils = new RequestUtils< Integer >();
	requestUtils.setReqdata( busId );
	String url = PropertiesUtil.getWxmp_home() + GETWXPUBLICUSERS;
	String returnData = HttpClienUtils.reqPostUTF8( JSONObject.toJSONString( requestUtils ), url, String.class, PropertiesUtil.getWxmpsignKey() );
	JSONObject json = JSON.parseObject( returnData );
	if ( "0".equals( json.getString( "code" ) ) ) {
	    WxPublicUsers wxPublicUsers = JSONObject.parseObject( json.getString( "data" ), WxPublicUsers.class );
	    return wxPublicUsers;
	}else{
	    LOG.error( "查查询李逢喜公众号接口参数:"+returnData);
	    return null;
	}
    }

    public WxPublicUsers findWxPublicUsersById(Integer id){
	LOG.error( "查询李逢喜公众号接口参数1:"+id);
	List<Map<String,Object>> list=new ArrayList<>(  );
	RequestUtils< Integer > requestUtils = new RequestUtils< Integer >();
	requestUtils.setReqdata( id );
	String url = PropertiesUtil.getWxmp_home() + GETSHOPBYID;
	String returnData = HttpClienUtils.reqPostUTF8( JSONObject.toJSONString( requestUtils ), url, String.class, PropertiesUtil.getWxmpsignKey() );
	JSONObject json = JSON.parseObject( returnData );
	if ( "0".equals( json.getString( "code" ) ) ) {
	    WxPublicUsers wxPublicUsers = JSONObject.parseObject( json.getString( "data" ), WxPublicUsers.class );
	    return wxPublicUsers;
	}else{
	    LOG.error( "查查询李逢喜公众号接口参数1:"+returnData);
	    return null;
	}
    }


    public List<Map> selectTempObjByBusId(Integer busId){
	LOG.error( "查询李逢喜消息模板接口参数:"+busId);
	List<Map<String,Object>> list=new ArrayList<>(  );
	RequestUtils< Integer > requestUtils = new RequestUtils< Integer >();
	requestUtils.setReqdata( busId );
	String url = PropertiesUtil.getWxmp_home() + SELECTTEMPOBJBYBUSID;
	String returnData = HttpClienUtils.reqPostUTF8( JSONObject.toJSONString( requestUtils ), url, String.class, PropertiesUtil.getWxmpsignKey() );
	JSONObject json = JSON.parseObject( returnData );
	if ( "0".equals( json.getString( "code" ) ) ) {
	    WxPublicUsers wxPublicUsers = JSONObject.parseObject( json.getString( "data" ), WxPublicUsers.class );
	    List<Map> returnList=JSONArray.parseArray( json.getString( "data" ),Map.class );
	    return returnList;
	}else{
	    LOG.error( "查查询李逢喜消息模板接口参数:"+returnData);
	    return null;
	}
    }


    /**
     * 获取主账户id
     * @param busId
     * @return
     */
    public Integer getMainBusId(Integer busId){
	try {
	    String url = PropertiesUtil.getWxmp_home() + GETMAINBUSID;
	    String returnMsg = SignHttpUtils.WxmppostByHttp( url, null, PropertiesUtil.getWxmpsignKey() );
	    if ( CommonUtil.isNotEmpty( returnMsg ) ) {
		Map< String,Object > json = JSON.parseObject( returnMsg, Map.class );
		if("0".equals( CommonUtil.toString( json.get( "code" ) ) )){
		    JSONObject obj=JSONObject.parseObject(CommonUtil.toString( json.get( "data" ) )  );
		    return CommonUtil.toInteger( obj.get( "mainBusId" ) );
		}else{
		    LOG.error( "调用陈丹主账户异常"+returnMsg );
		}

	    }
	}catch ( Exception e ){
	    LOG.error( "调用陈丹区号异常",e );
	}
	return null;
    }

    public Map<String, Object> queryBasisByName(String cityCode){
	LOG.error( "查询李逢喜省市区接口参数:"+cityCode);
	List<Map<String,Object>> list=new ArrayList<>(  );
	RequestUtils< String > requestUtils = new RequestUtils< String >();
	requestUtils.setReqdata( cityCode );
	String url = PropertiesUtil.getWxmp_home() + QUERYBASISBYNAME;
	String returnData = HttpClienUtils.reqPostUTF8( JSONObject.toJSONString( requestUtils ), url, String.class, PropertiesUtil.getWxmpsignKey() );
	JSONObject json = JSON.parseObject( returnData );
	if ( "0".equals( json.getString( "code" ) ) ) {
	    Map<String, Object> map=JSONObject.parseObject( json.getString( "data" ),Map.class );
	    return map;
	}else{
	    LOG.error( "查查询李逢喜省市区接口参数:"+returnData);
	    return null;
	}
    }

    public List<Map> queryCityByParentId(Integer pId){
	LOG.error( "查询李逢喜根据父级ID查询城市数据接口参数:"+pId);
	List<Map<String,Object>> list=new ArrayList<>(  );
	RequestUtils< Integer > requestUtils = new RequestUtils< Integer >();
	requestUtils.setReqdata( pId );
	String url = PropertiesUtil.getWxmp_home() + QUERYBASISBYNAME;
	String returnData = HttpClienUtils.reqPostUTF8( JSONObject.toJSONString( requestUtils ), url, String.class, PropertiesUtil.getWxmpsignKey() );
	JSONObject json = JSON.parseObject( returnData );
	if ( "0".equals( json.getString( "code" ) ) ) {
	    List<Map> returnlist=JSONArray.parseArray( json.getString( "data" ),Map.class );
	    return returnlist;
	}else{
	    LOG.error( "查查询李逢喜根据父级ID查询城市数据接口参数:"+returnData);
	    return null;
	}
    }

    public List<Map> queryCityByLevel(){
	LOG.error( "查询李逢喜获取所有的省接口参数:");
	List<Map<String,Object>> list=new ArrayList<>(  );
	RequestUtils< Integer > requestUtils = new RequestUtils< Integer >();
	requestUtils.setReqdata( 2 );
	String url = PropertiesUtil.getWxmp_home() + QUERYBASISBYNAME;
	String returnData = HttpClienUtils.reqPostUTF8( JSONObject.toJSONString( requestUtils ), url, String.class, PropertiesUtil.getWxmpsignKey() );
	JSONObject json = JSON.parseObject( returnData );
	if ( "0".equals( json.getString( "code" ) ) ) {
	    List<Map> returnlist=JSONArray.parseArray( json.getString( "data" ),Map.class );
	    return returnlist;
	}else{
	    LOG.error( "查查询李逢喜获取所有的省数据接口参数:"+returnData);
	    return null;
	}
    }

    /**
     * 公众号关注
     * @param publicId
     * @return
     */
    public String newqrcodeCreateFinal(Integer publicId){
	LOG.error( "调用关注接口请求参数",publicId );
	RequestUtils<QrcodeCreateFinal > requestUtils=new RequestUtils<>(  );
	QrcodeCreateFinal qrcodeCreateFinal=new QrcodeCreateFinal();
	qrcodeCreateFinal.setPublicId( publicId );
	qrcodeCreateFinal.setModel( 14 );
	requestUtils.setReqdata( qrcodeCreateFinal );
	String url=PropertiesUtil.getWxmp_home()+NEW_QRCODE_CREATE_FINAL;
	String returnData = HttpClienUtils.reqPostUTF8( JSONObject.toJSONString( requestUtils ), url, String.class, PropertiesUtil.getWxmpsignKey() );
	JSONObject json = JSON.parseObject( returnData );
	if ( "0".equals( json.getString( "code" ) ) ) {
	    return  json.getString( "data" );
	}else{
	    LOG.error( "调用李逢喜关注接口异常"+returnData );
	}
	return null;
    }

}
