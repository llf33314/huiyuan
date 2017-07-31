package com.gt.member.service.core.ws;

import com.gt.member.service.core.ws.entitybo.queryBo.BaseParam;
import com.gt.member.service.core.ws.entitybo.returnBo.BaseResult;
import com.gt.member.service.core.ws.entitybo.returnBo.ReturnCode;
import com.gt.member.util.CommonUtil;
import net.sf.json.JSONObject;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/7/27 0027.
 */


public class MemberNewWSImpl implements  MemberNewWS {


    private static Map<String, String> obj =new HashMap<String, String>();
    static{
	obj.put("getShopById", "wsShopService");
	obj.put("sendSmsOld", "wsSmsService");
	obj.put("getFenbiSurplus", "wxFenBiFlowService");
	obj.put("getFenbiSurplus", "wxFenBiFlowService");
	obj.put("updateFenbiReduce", "wxFenBiFlowService");
	obj.put("codeConsume", "wsWxCardService");
    }

    @Override
    public String reInvoke( String json ) {
	BaseResult result=new BaseResult(0,"success");
	net.sf.json.JSONObject jsonObject=net.sf.json.JSONObject.fromObject(json);
	try {
	    BaseParam baseParam=(BaseParam) net.sf.json.JSONObject.toBean(jsonObject, BaseParam.class);
	    JSONObject busData=jsonObject.getJSONObject("reqdata");
	    baseParam.setReqdata(busData);

	    Class<?>  cls = CommonUtil.getApplicationContext().getBean(obj.get(baseParam.getAction())).getClass();
	    Method m = cls.getDeclaredMethod(baseParam.getAction(),baseParam.getClass());
	    result=(BaseResult) m.invoke(CommonUtil.getApplicationContext().getBean(obj.get(baseParam.getAction())), baseParam);
	} catch (Exception e) {
	    e.printStackTrace();
	    result=new BaseResult( ReturnCode.ERROR_1,"系统异常");
	}
	return JSONObject.fromObject(result).toString();
    }
}
