package com.gt.member.util.sign;

import com.alibaba.fastjson.JSONObject;
import com.gt.member.exception.BusinessException;
import com.gt.member.util.HttpUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/8/10 0010.
 */
public class SignHttpUtils {

    public static String postByHttp(String url, Object paramObj, String signKey)
		    throws BusinessException{
	if (signKey == null) {
	    throw new BusinessException("signKey could not be null");
	}
	String param = JSONObject.toJSONString(paramObj);
	SignBean signBean = SignUtils.sign(signKey, param);
	Map< String,String > headers = new HashMap();
	headers.put("sign", JSONObject.toJSONString(signBean));
	String result = HttpUtils.sendPostByHeaders( url,headers,param );
	return result;
    }




}
