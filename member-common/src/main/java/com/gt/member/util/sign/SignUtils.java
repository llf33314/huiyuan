package com.gt.member.util.sign;

import com.gt.member.enums.SignEnum;
import com.gt.member.util.CommonUtil;
import com.gt.member.util.MD5Utils;

public class SignUtils
{
    public static SignBean sign(String signKey, String param)
    {
	String timeStamp = String.valueOf(System.currentTimeMillis());
	String randNum = String.valueOf((int)((Math.random() * 9.0D + 1.0D) * 10000.0D));
	String sign = MD5Utils.getMD5SM(signKey + timeStamp + randNum + param);
	SignBean signBean = new SignBean(sign, timeStamp, randNum);
	return signBean;
    }

    public static String decSign(String signKey, SignBean signBean, String param)
    {
        if( CommonUtil.isEmpty( signBean )){
	    return SignEnum.SIGN_ERROR.getCode();
	}
	String reqTime = signBean.getTimeStamp();

	boolean timeOut = contrastTimeNow(Long.valueOf(Long.parseLong(reqTime))) > 10L;
	if (timeOut) {
	    return SignEnum.TIME_OUT.getCode();
	}

	String reqSign = signBean.getSign();
	String sign = MD5Utils.getMD5SM(signKey + reqTime + signBean.getRandNum() + param);
	boolean signFail = !sign.equals(reqSign);
	if (signFail) {
	    return SignEnum.SIGN_ERROR.getCode();
	}
	return SignEnum.SUCCESS.getCode();
    }

    private static long contrastTime(Long bigTime, Long smallTime)
    {
	return (bigTime.longValue() - smallTime.longValue()) / 60000L;
    }

    private static long contrastTimeNow(Long paramTime)
    {
	return contrastTime(Long.valueOf(System.currentTimeMillis()), paramTime);
    }
}