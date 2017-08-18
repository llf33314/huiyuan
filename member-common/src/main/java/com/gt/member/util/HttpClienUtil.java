package com.gt.member.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;



public class HttpClienUtil {
	private static Logger logger = Logger.getLogger(HttpClienUtil.class); // 日志记录

	/**
	 * post请求
	 * 
	 * @param url
	 *            url地址
	 * @param jsonParam
	 *            参数
	 * @param noNeedResponse
	 *            不需要返回结果
	 * @return
	 * @throws Exception
	 */
	public static JSONObject httpPost(String url, JSONObject jsonParam,
			boolean noNeedResponse) throws Exception {
		// post请求返回结果
		CloseableHttpClient httpClient = HttpClients.createDefault();
		// 设置超时时间
		// 构建请求配置信息
		RequestConfig config = RequestConfig.custom().setConnectTimeout(1000) // 创建连接的最长时间
				.setConnectionRequestTimeout(500) // 从连接池中获取到连接的最长时间
				.setSocketTimeout(10 * 1000) // 数据传输的最长时间
				.setStaleConnectionCheckEnabled(true) // 提交请求前测试连接是否可用
				.build();
		// 设置请求配置信息
		JSONObject jsonResult = null;
		HttpPost post = new HttpPost(url);
		post.setConfig(config);
		try {
			if (null != jsonParam) {
				// 解决中文乱码问题
				StringEntity entity = new StringEntity(jsonParam.toString(),
						"utf-8");
				entity.setContentEncoding("UTF-8");
				entity.setContentType("application/json");
				post.setEntity(entity);
			}
			HttpResponse response = httpClient.execute(post);
			url = URLDecoder.decode(url, "UTF-8");
			/** 请求发送成功，并得到响应 **/
			if (response.getStatusLine().getStatusCode() == 200) {
				String str = "";
				try {
					/** 读取服务器返回过来的json字符串数据 **/
					if (noNeedResponse) {
						return null;
					}
					HttpEntity entity = response.getEntity();
					if (entity != null) {
						InputStream instreams = entity.getContent();
						str = convertStreamToString(instreams);
						post.abort();
					}

					/** 把json字符串转换成json对象 **/
					jsonResult = JSONObject.fromObject(str);
				} catch (Exception e) {
					logger.error("post请求提交失败:" + url, e);
					throw new Exception();
				}
			}
		} catch (IOException e) {
			logger.error("post请求提交失败:" + url, e);
			throw new Exception();
		}
		return jsonResult;
	}

	/**
	 * 发送get请求
	 * 
	 * @param url
	 *            路径
	 * @return
	 * @throws Exception 
	 */
	public static JSONObject httpGet(String url) throws Exception {
		// get请求返回结果
		JSONObject jsonResult = null;
		try {
			CloseableHttpClient httpClient = HttpClients.createDefault();
			// 构建请求配置信息
			RequestConfig config = RequestConfig.custom()
					.setConnectTimeout(1000) // 创建连接的最长时间
					.setConnectionRequestTimeout(500) // 从连接池中获取到连接的最长时间
					.setSocketTimeout(10 * 1000) // 数据传输的最长时间
					.setStaleConnectionCheckEnabled(true) // 提交请求前测试连接是否可用
					.build();

			// 发送get请求
			HttpGet httpGet = new HttpGet(url);
			httpGet.setConfig(config);
			HttpResponse response = httpClient.execute(httpGet);
			/** 请求发送成功，并得到响应 **/
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				/** 读取服务器返回过来的json字符串数据 **/
				String str="";
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					InputStream instreams = entity.getContent();
					str = convertStreamToString(instreams);
					httpGet.abort();
				}
				/** 把json字符串转换成json对象 **/
				jsonResult = JSONObject.fromObject(str);
				url = URLDecoder.decode(url, "UTF-8");
			} else {
				logger.error("get请求提交失败:" + url);
				throw new Exception();
			}
		} catch (IOException e) {
			logger.error("get请求提交失败:" + url, e);
			throw new Exception();
		}
		return jsonResult;
	}

	public static String convertStreamToString(InputStream is) {
		StringBuilder sb1 = new StringBuilder();
		byte[] bytes = new byte[4096];
		int size = 0;

		try {
			while ((size = is.read(bytes)) > 0) {
				String str = new String(bytes, 0, size, "UTF-8");
				sb1.append(str);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb1.toString();
	}
	
	public static void main(String[] args) {
		String url="http://192.168.2.240/dict/79B4DE7C/getDict.do";
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("dict_type", 1058);
		JSONObject obj=JSONObject.fromObject(map);
		try {
			JSONObject json = HttpClienUtil.httpPost(url, obj, false);
			System.out.println(json);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
