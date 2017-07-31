package com.gt.member.service.core.ws.entitybo.queryBo;

import java.io.Serializable;
import java.util.Map;

/**
 * 参数父级类
 * @author Administrator
 *
 */
public class BaseParam<T> implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 业务请求参数
	 */
	private T reqdata;
	
	
	

	/**
	 * 请求操作
	 */
	private String action ;
	
	/**
	 * 请求token
	 */
	private String requestToken;
	
	
	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}
	
	public BaseParam(){
		
	}

	/**
	 * 插入请求参数
	 * @param data
	 */
	 public BaseParam(T reqdata,String action) {
		       this.reqdata = reqdata;
		       this.action=action;
	 }
	public static void main(String[] args) {
//		BaseResult data=new BaseResult();
//		data.setCode(0);
//		data.setMsg("111");
//		BaseParam baseParam=new BaseParam ();
//		baseParam.setData(data);
//		System.out.println(JSONObject.toJSONString(baseParam));
//		System.out.println(JSONObject.toJSONString(baseParam.getData()));
	}

	public String getRequestToken() {
		return requestToken;
	}

	public void setRequestToken(String requestToken) {
		this.requestToken = requestToken;
	}

	public T getReqdata() {
		return reqdata;
	}

	public void setReqdata(T reqdata) {
		this.reqdata = reqdata;
	}

}
