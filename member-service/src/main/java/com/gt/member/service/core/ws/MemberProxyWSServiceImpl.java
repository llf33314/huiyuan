package com.gt.member.service.core.ws;
//package com.gt.member.service.core.ws;
//
import javax.jws.WebService;

import com.gt.member.service.core.ws.MemberpProxyWSservice;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 代理实现类
 * @author pengjiangli
 *
 */
@WebService(serviceName="memberProxyWS")
@Component
public class MemberProxyWSServiceImpl implements MemberpProxyWSservice {

	@Override
	public String reInvoke(String json) {
		//BusinessEntity busionessEntity=(BusinessEntity) JSONObject.toBean(JSONObject.fromObject(json),BusinessEntity.class);

		return null;
	}

   	 @Override
	public Map<String,Object> test(String json){
		return null;
	}


}
