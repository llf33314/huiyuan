package com.gt.member.service.core.ws;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapters;

import com.gt.member.service.core.ws.util.XmlMapAdapter;
import org.apache.cxf.annotations.GZIP;

import java.util.Map;

/**
 * 会员代理类ws接口
 * 
 * @author pengjiangli
 *
 */
@WebService
@GZIP
public interface MemberpProxyWSservice {

	/**
	 * 调用统一方法接口
	 * @param json
	 * @return
	 */
	@WebMethod
	public String reInvoke(String json);


    /**
     * 调用统一方法接口
     * @param json
     * @return
     */
    @WebMethod
    @XmlJavaTypeAdapter(XmlMapAdapter.class)
    public Map<String,Object> test(String json);

}
