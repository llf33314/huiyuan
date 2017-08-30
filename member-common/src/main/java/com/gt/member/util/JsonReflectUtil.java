package com.gt.member.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

/**
 * JSON工具类
 *
 * @author qusk
 * @date 2016年1月9日
 * @description
 */
public class JsonReflectUtil {

    /**
     * 将JSONArray对象转换成Map-List集合
     *
     * @param json
     * @return
     */
    public static Object reflect(JSONArray json) {
	List<Object> list = new ArrayList<Object>();
	for (Object o : json) {
	    if (o instanceof JSONArray) {
		list.add(reflect((JSONArray) o));
	    } else if (o instanceof JSONObject) {
		list.add(reflect((JSONObject) o));
	    } else
		list.add(o);
	}
	return list;
    }

    /**
     * 将JSONObjec对象转换成Map-List集合
     *
     * @param json
     * @return
     */
    public static HashMap<String, Object> reflect(JSONObject json) {
	HashMap<String, Object> map = new HashMap<String, Object>();
	Set<?> keys = json.keySet();
	for (Object key : keys) {
	    Object o = json.get(key);
	    if (o instanceof JSONArray)
		map.put((String) key, reflect((JSONArray) o));
	    else if (o instanceof JSONObject)
		map.put((String) key, reflect((JSONObject) o));
	    else
		map.put((String) key, o);
	}
	return map;
    }


    /**
     * json2map
     * @return
     */
    public static Map<String, Object> json2Map(String jsonStr) {
	Map<String, Object> map = new HashMap<String, Object>();
	JSONObject jsonMap = JSONObject.fromObject(jsonStr);
	Iterator<String> it = jsonMap.keys();
	while(it.hasNext()) {
	    String key = (String) it.next();
	    map.put(key, jsonMap.get(key));
	}
	return map;
    }

    /**
     * json2map
     * @return
     */
    public static Map<String, Object> json2Map(String jsonStr,JsonConfig jsonConfig) {
	Map<String, Object> map = new HashMap<String, Object>();
	JSONObject jsonMap = JSONObject.fromObject(jsonStr,jsonConfig);
	Iterator<String> it = jsonMap.keys();
	while(it.hasNext()) {
	    String key = (String) it.next();
	    map.put(key, jsonMap.get(key));
	}
	return map;
    }

    /**
     * json2List
     * @param jsonStr
     * @return
     */
    public static List<Map<String, Object>> json2List(String jsonStr){
	JSONArray jsonArray = JSONArray.fromObject(jsonStr);
	List<Map<String,Object>> mapListJson = (List)jsonArray;
	return mapListJson;
    }

    /**
     * json转map 支持多级
     * @param jsonStr
     * @return
     */
    public static Map<String, Object> parseJSON2Map(String jsonStr){

	//最外层解析
	if(jsonStr!=null&&jsonStr.startsWith("{")&&jsonStr.endsWith("}")){
	    Map<String, Object> map = new HashMap<String, Object>();

	    JSONObject json = JSONObject.fromObject(jsonStr);
	    for(Object k : json.keySet()){

		Object v = json.get(k);
		//如果内层还是数组的话，继续解析
		if(v instanceof JSONArray){
		    List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		    Iterator<JSONObject> it = ((JSONArray)v).iterator();
		    while(it.hasNext()){
			JSONObject json2 = it.next();
			list.add(parseJSON2Map(json2.toString()));
		    }
		    map.put(k.toString(), list);
		} else {
		    Map<String, Object> m = parseJSON2Map(v.toString());
		    if(m==null)
			map.put(k.toString(), v);
		    else
			map.put(k.toString(), m);
		}
	    }
	    return map;
	}else{
	    return null;
	}
    }


    public static <T> List<T> asList(String json, Class<T> clazzz) {
	@SuppressWarnings("unchecked")
	T[] array = (T[]) JSONArray.toArray(JSONArray.fromObject(json), clazzz);
	return Arrays.asList(array);
    }


    public static <T>  T asObject(String json, Class<T> clazzz) {
	if(json.startsWith("{")){
	    json = "["+json;
	}
	if(json.endsWith("}")){
	    json = json+"]";
	}
	List<T> list=asList(json, clazzz);
	if(list!=null &&  list.size()!=0){
	    return list.get(0);
	}
	return null;
    }


    /**
     * json字符串转List
     * @param jsonStr
     * @param clazz
     * @return
     */
    public static List JsonStrConverToList(String jsonStr,Class clazz) {

	JSONArray json = JSONArray.fromObject(jsonStr);
	List list = (List) JSONArray.toCollection(json, clazz);
	return list;
    }
}
