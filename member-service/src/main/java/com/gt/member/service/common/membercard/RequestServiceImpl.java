package com.gt.member.service.common.membercard;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gt.api.enums.ResponseEnums;
import com.gt.api.util.HttpClienUtils;
import com.gt.api.util.KeysUtil;
import com.gt.api.util.RequestUtils;
import com.gt.api.util.sign.SignHttpUtils;
import com.gt.member.enums.ResponseMemberEnums;
import com.gt.member.exception.BusinessException;
import com.gt.member.util.EncryptUtil;
import com.gt.member.util.PropertiesUtil;
import com.gt.util.entity.param.fenbiFlow.AdcServicesInfo;
import com.gt.util.entity.param.pay.SubQrPayParams;
import com.gt.util.entity.param.sms.NewApiSms;
import com.gt.util.entity.param.sms.OldApiSms;
import com.gt.util.entity.param.wx.SendWxMsgTemplate;
import com.gt.util.entity.result.shop.WsWxShopInfoExtend;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/10/24.
 */
@Service
public class RequestServiceImpl implements RequestService {
    //微信卡券核销
    private final String CODE_CONSUME = "/8A5DA52E/wxcardapi/6F6D9AD2/79B4DE7C/codeConsume.do";

    //发送短信
    private final String SEND_SMS = "/8A5DA52E/smsapi/6F6D9AD2/79B4DE7C/sendSmsOld.do";

    private final String SEND_SMS_NEW="/8A5DA52E/smsapi/6F6D9AD2/79B4DE7C/sendSmsNew.do";

    private final static String SEND_WXMSG="8A5DA52E/wxpublicapi/6F6D9AD2/79B4DE7C/sendWxMsgTemplate.do";

    //验证主账户信息
    private final static String VERSION_BUS_PID="/8A5DA52E/childBusUserApi/VersionBusPid.do";

    private final static String CHANGE_FLOW_URL="/8A5DA52E/fenbiflow/6F6D9AD2/79B4DE7C/adcServices.do";

    //查询门店信息
    private final static String WXSHOP_BYBUSID= "8A5DA52E/shopapi/6F6D9AD2/79B4DE7C/queryWxShopByBusId.do";

    private final static String PAY_API="/8A5DA52E/payApi/6F6D9AD2/79B4DE7C/payapi.do";


    public String codeConsume(String cardId,String code,Integer busId) throws Exception{
        try {
	    Map< String,Object > map = new HashMap< String,Object >();
	    String url = PropertiesUtil.getWxmp_home() + CODE_CONSUME;
	    String getWxmpsignKey = PropertiesUtil.getWxmpsignKey();
	    map.put( "card_id", cardId );
	    map.put( "code", code );
	    map.put( "busId", busId );
	    String result = SignHttpUtils.postByHttp( url, map, getWxmpsignKey );
	    return result;
	}catch ( Exception e ){
            throw new Exception(  );
	}

    }

    public String sendSms(RequestUtils<OldApiSms> requestUtils){
	String url=PropertiesUtil.getWxmp_home()+SEND_SMS;
	String smsStr = HttpClienUtils.reqPostUTF8( JSONObject.toJSONString( requestUtils ), url,String.class, PropertiesUtil.getWxmpsignKey() );
    	return smsStr;
    }


    public String sendSmsNew(RequestUtils<NewApiSms> requestUtils){
	String url=PropertiesUtil.getWxmp_home()+SEND_SMS_NEW;
	String smsStr = HttpClienUtils.reqPostUTF8( JSONObject.toJSONString( requestUtils ), url,String.class, PropertiesUtil.getWxmpsignKey() );
    	return smsStr;
    }

    public void setSendWxmsg(SendWxMsgTemplate sendWxMsgTemplate){
	RequestUtils<SendWxMsgTemplate > requestUtils=new RequestUtils<>(  );
	String url=PropertiesUtil.getWxmp_home()+SEND_WXMSG;
	String smsStr = HttpClienUtils.reqPostUTF8( JSONObject.toJSONString( requestUtils ), url,String.class, PropertiesUtil.getWxmpsignKey() );

    }


    /**
     * 子账号商家输入主商家的用户名和密码进行验证
     * @param userName 用户名
     * @param userPwd  密码
     * @param childId 子账户id
     * @return
     */
    public boolean verificationBusUser(String userName,String userPwd,Integer childId)throws BusinessException{
	try {
	    String url = PropertiesUtil.getWxmp_home() + VERSION_BUS_PID;
	    Map< String,Object > map = new HashMap<>();
	    map.put( "login_name", userName );
	    map.put( "password", userPwd );
	    map.put( "childId", childId );
	    String result = SignHttpUtils.WxmppostByHttp( url, map, PropertiesUtil.getWxmpsignKey() );
	    JSONObject jsonObject=JSONObject.parseObject( result );
	    if("0".equals( jsonObject.getString( "code" ) )){
		return true;
	    }else{
		throw new BusinessException( ResponseMemberEnums.VERIFICATION_BUSUSER.getCode(),jsonObject.getString( "msg" ) );
	    }


	}catch ( Exception e ){
	    throw new BusinessException( ResponseEnums.ERROR);
	}
    }


    public String changeFlow(RequestUtils<AdcServicesInfo > requestUtils){
	String url=PropertiesUtil.getWxmp_home()+CHANGE_FLOW_URL;
	String changeFlowStr = HttpClienUtils.reqPostUTF8( JSONObject.toJSONString( requestUtils ), url,String.class, PropertiesUtil.getWxmpsignKey() );
	return changeFlowStr;
    }


    public List<WsWxShopInfoExtend > findShopsByBusId(Integer busId){
	String url=PropertiesUtil.getWxmp_home()+WXSHOP_BYBUSID;
	RequestUtils<Integer> requestUtils=new RequestUtils<>(  );
	requestUtils.setReqdata( busId );
	String shopStr = HttpClienUtils.reqPostUTF8( JSONObject.toJSONString( requestUtils ), url,String.class, PropertiesUtil.getWxmpsignKey() );
	JSONObject json= JSON.parseObject(  shopStr);
	if("0".equals( json.getString( "code" ))){
	    List<WsWxShopInfoExtend > wxShopInfoExtends=JSON.parseArray( json.getString( "data" ), WsWxShopInfoExtend.class);
	    return wxShopInfoExtends;
	}else{
	    throw new BusinessException( ResponseMemberEnums.QUERY_SHOP_BUSID );
	}
    }


    /**
     * 微信支付包 多粉钱包支付
     * @return
     */
    public String payApi(SubQrPayParams subQrPayParams)throws  Exception{
	String obj=KeysUtil.getEncString(JSON.toJSONString( subQrPayParams ));
	String url=PropertiesUtil.getWxmp_home()+PAY_API;
	return url;
    }

    public String loginImg(Integer busId){
        return null;
    }


}
