package com.gt.member.util.sign;

import com.alibaba.fastjson.JSON;
import com.gt.member.util.HttpUtils;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

public class SignFilterUtils
{
    public static String postByHttp(ServletRequest servletRequest, String signKey)
    {
	String param = HttpUtils.getBodyString(servletRequest);

	String signStr = ((HttpServletRequest)servletRequest).getHeader("sign");

	SignBean signBean = (SignBean)JSON.parseObject(signStr, SignBean.class);

	String code = SignUtils.decSign(signKey, signBean, param);
	return code;
    }
}