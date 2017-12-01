package com.gt.member.service.common.membercard;

import com.alibaba.fastjson.JSONObject;
import com.gt.api.enums.ResponseEnums;
import com.gt.api.util.HttpClienUtils;
import com.gt.api.util.RequestUtils;
import com.gt.api.util.sign.SignHttpUtils;
import com.gt.member.enums.ResponseMemberEnums;
import com.gt.member.exception.BusinessException;
import com.gt.member.util.CommonUtil;
import com.gt.member.util.PropertiesUtil;
import com.gt.util.entity.param.sms.NewApiSms;
import com.gt.util.entity.param.sms.OldApiSms;
import com.gt.util.entity.param.wx.SendWxMsgTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/10/24.
 */
@Service
@Slf4j
public class RequestServiceImpl implements RequestService {
    //微信卡券核销
    private final String CODE_CONSUME = "/8A5DA52E/wxcardapi/6F6D9AD2/79B4DE7C/codeConsume.do";

    //发送短信
    private final String SEND_SMS = "/8A5DA52E/smsapi/6F6D9AD2/79B4DE7C/sendSmsOld.do";

    private final String SEND_SMS_NEW = "/8A5DA52E/smsapi/6F6D9AD2/79B4DE7C/sendSmsNew.do";

    private final static String SEND_WXMSG = "8A5DA52E/wxpublicapi/6F6D9AD2/79B4DE7C/sendWxMsgTemplate.do";

    private static final String GETWXPULICMSG = "/8A5DA52E/busUserApi/getWxPulbicMsg.do";

    public String codeConsume( String cardId, String code, Integer busId ) throws Exception {
	try {
	    Map< String,Object > map = new HashMap< String,Object >();
	    String url = PropertiesUtil.getWxmp_home() + CODE_CONSUME;
	    String getWxmpsignKey = PropertiesUtil.getWxmpsignKey();
	    map.put( "card_id", cardId );
	    map.put( "code", code );
	    map.put( "busId", busId );
	    String result = SignHttpUtils.postByHttp( url, map, getWxmpsignKey );
	    return result;
	} catch ( Exception e ) {
	    throw new Exception();
	}

    }

    public void sendSms( RequestUtils< OldApiSms > requestUtils ) {
	String url = PropertiesUtil.getWxmp_home() + SEND_SMS;
	String smsStr = HttpClienUtils.reqPostUTF8( JSONObject.toJSONString( requestUtils ), url, String.class, PropertiesUtil.getWxmpsignKey() );
    }

    public String sendSmsNew( RequestUtils< NewApiSms > requestUtils ) {
	log.error( "短信参数:"+JSONObject.toJSONString( requestUtils ) );
	String url = PropertiesUtil.getWxmp_home() + SEND_SMS_NEW;
	String smsStr = HttpClienUtils.reqPostUTF8( JSONObject.toJSONString( requestUtils ), url, String.class, PropertiesUtil.getWxmpsignKey() );
	return smsStr;
    }

    public void setSendWxmsg( SendWxMsgTemplate sendWxMsgTemplate ) {
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
}
