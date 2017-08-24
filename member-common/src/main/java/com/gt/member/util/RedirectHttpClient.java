package com.gt.member.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Administrator on 2017/8/22 0022.
 */
public class RedirectHttpClient {

    public  static String getPostResponse( String url, JSONObject jsonObject ) throws IOException {

	String result = "";
	PostMethod post = new PostMethod( url );
	HttpClient client = new HttpClient();
	Iterator it = jsonObject.entrySet().iterator();
	NameValuePair[] param = new NameValuePair[jsonObject.size()];
	int i = 0;
	while ( it.hasNext() ) {
	    Map.Entry parmEntry = (Map.Entry) it.next();
	    post.setParameter(CommonUtil.toString(parmEntry.getKey()  ), CommonUtil.toString(parmEntry.getValue())   );
//	    param[i++] = new NameValuePair(CommonUtil.toString(parmEntry.getKey()  ), CommonUtil.toString(parmEntry.getValue()) );
	}


	//post.setRequestBody( param );
	try {
	    int statusCode = client.executeMethod( post );

	    if ( statusCode == HttpStatus.SC_MOVED_PERMANENTLY || statusCode == HttpStatus.SC_MOVED_TEMPORARILY ) {
		Header locationHeader = post.getResponseHeader( "location" );
		String location = "";
		if ( locationHeader != null ) {
		    location = locationHeader.getValue();
		    result = getPostResponse( location, jsonObject );//用跳转后的页面重新请求�??
		}
	    } else if ( statusCode == HttpStatus.SC_OK ) {
		result = post.getResponseBodyAsString();
	    }
	} catch ( IOException ex ) {
	} finally {
	    post.releaseConnection();
	}
	return result;
    }

}
