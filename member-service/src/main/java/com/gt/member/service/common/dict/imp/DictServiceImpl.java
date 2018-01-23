package com.gt.member.service.common.dict.imp;

import java.util.*;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gt.api.enums.ResponseEnums;
import com.gt.api.util.sign.SignHttpUtils;
import com.gt.member.exception.BusinessException;
import com.gt.member.service.common.dict.DictService;
import com.gt.member.util.CommonUtil;
import com.gt.member.util.PropertiesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DictServiceImpl implements DictService {

    private static final Logger LOG = LoggerFactory.getLogger( DictServiceImpl.class );

    private static final String GETDICTAPI="/8A5DA52E/dictApi/getDictApi.do";

    private static final String GETDIBSERNUM="/8A5DA52E/dictApi/getDiBserNum.do";


    @Override
    public SortedMap<String, Object> getDict( String type) throws BusinessException {
	try {
	    String url= PropertiesUtil.getWxmp_home()+GETDICTAPI;
	    Map< String,Object > queryMap = new HashMap<>();
	    queryMap.put( "style", type );
	    String result = SignHttpUtils.WxmppostByHttp( url, queryMap, PropertiesUtil.getWxmpsignKey() );
	    JSONObject jsonObject = JSONObject.parseObject( result );
	    SortedMap<String, Object> map = new TreeMap<String, Object>();
	    if ( "0".equals( jsonObject.getString( "code" ) ) ) {
	        JSONObject  data=JSONObject.parseObject(jsonObject.getString( "data" )  );
		List<Map> list=JSONArray.parseArray( data.getString( "dictJSON" ),Map.class );
		for (Map map2 : list) {
		    map.put(map2.get("item_key").toString(), map2.get("item_value"));
		}
	    }
	    return map;
	} catch ( Exception e ) {
	    LOG.error( "查询字典异常", e );
	    throw new BusinessException( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getMsg() );
	}
    }

    /**
     * 根据类型获取字典详细信息
     * @param type
     * @return
     */
    public List<Map<String, Object>> getDictbyList(String type) throws BusinessException{
	try {
	    String url= PropertiesUtil.getWxmp_home()+GETDICTAPI;
	    Map< String,Object > queryMap = new HashMap<>();
	    queryMap.put( "style", type );
	    String result = SignHttpUtils.WxmppostByHttp( url, queryMap, PropertiesUtil.getWxmpsignKey() );
	    JSONObject jsonObject = JSONObject.parseObject( result );
	    if ( "0".equals( jsonObject.getString( "code" ) ) ) {
		JSONObject  data=JSONObject.parseObject(jsonObject.getString( "data" )  );
		List list=JSONArray.parseArray( data.getString( "dictJSON" ),List.class );
		return list;
	    }
	} catch ( Exception e ) {
	    LOG.error( "查询字典异常", e );
	    throw new BusinessException( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getMsg() );
	}
	return null;
    }

    @Override
    public String getDictRuturnValue( String type, Integer key ) throws BusinessException {
	try {
	    String url= PropertiesUtil.getWxmp_home()+GETDICTAPI;
	    Map< String,Object > queryMap = new HashMap<>();
	    queryMap.put( "style", type );
	    String result = SignHttpUtils.WxmppostByHttp( url, queryMap, PropertiesUtil.getWxmpsignKey() );
	    JSONObject jsonObject = JSONObject.parseObject( result );
	    if ( "0".equals( jsonObject.getString( "code" ) ) ) {
		JSONObject  data=JSONObject.parseObject(jsonObject.getString( "data" )  );
		List<Map> list=JSONArray.parseArray( data.getString( "dictJSON" ),Map.class );
		for (Map map2 : list) {
		  return CommonUtil.toString( map2.get( key.toString() ) );
		}
	    }
	} catch ( Exception e ) {
	    LOG.error( "查询字典异常", e );
	    throw new BusinessException( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getMsg() );
	}
	return null;
    }

    @Override
    public String dictBusUserNum( Integer userid, Integer style ) {
	try {
	    String url= PropertiesUtil.getWxmp_home()+GETDIBSERNUM;
	    Map< String,Object > queryMap = new HashMap<>();
	    queryMap.put( "userId", userid );
	    queryMap.put( "dictstyle", style );
	    queryMap.put( "modelStyle", 4 );
	    String result = SignHttpUtils.WxmppostByHttp( url, queryMap, PropertiesUtil.getWxmpsignKey() );
	    JSONObject jsonObject = JSONObject.parseObject( result );
	    if ( "0".equals( jsonObject.getString( "code" ) ) ) {
		JSONObject  data=JSONObject.parseObject(jsonObject.getString( "data" )  );
		return  CommonUtil.toString( data.get( "dictBusUserNum" ) );
	    }
	} catch ( Exception e ) {
	    LOG.error( "查询字典异常", e );
	    throw new BusinessException( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getMsg() );
	}
	return "0";
    }

    @Override
    public Integer shopuserid( Integer userid ) {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public Map< String,Object > pidUserMap( Integer user_id ) {
	// TODO Auto-generated method stub
	return null;
    }

}
