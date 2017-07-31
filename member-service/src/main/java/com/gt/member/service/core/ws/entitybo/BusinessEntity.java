package com.gt.member.service.core.ws.entitybo;

import java.io.Serializable;

/**
 * 业务处理实体类
 * @author pengjiangli
 *
 */
public class BusinessEntity<T> implements Serializable{

	private String serviceName;  //请求业务处理接口名
	
	private String methodName; //请求方法名称
	
	private T data;  //请求数据

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
	
}
