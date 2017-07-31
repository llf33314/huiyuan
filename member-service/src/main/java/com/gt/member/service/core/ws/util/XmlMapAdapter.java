package com.gt.member.service.core.ws.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.adapters.XmlAdapter;


/**
 * Map 处理
 * @author pengjiangli
 *
 */
public class XmlMapAdapter extends XmlAdapter<MyStringMap, Map<String, String>>{
	 @Override  
	    public Map<String, String> unmarshal(MyStringMap v) throws Exception {  
	        // TODO Auto-generated method stub  
	          
	        Map<String, String> result = new HashMap<String, String>();
	          
	        for (MyStringMap.Entry entry : v.getEntries()) {
	            result.put(entry.getKey(), entry.getValue());  
	        }  
	          
	        return result;  
	    }  
	  
	    @Override  
	    public MyStringMap marshal(Map<String, String> v) throws Exception {  
	        // TODO Auto-generated method stub  
	        MyStringMap msm = new MyStringMap();  
	        List<MyStringMap.Entry> eList = new ArrayList<MyStringMap.Entry>();
	        for(String key : v.keySet()) {  
	            MyStringMap.Entry entry = new MyStringMap.Entry();
	            entry.setKey(key);  
	            entry.setValue(v.get(key));  
	            eList.add(entry);  
	        }  
	        msm.setEntries(eList);  
	        return msm;  
	    }  
}
