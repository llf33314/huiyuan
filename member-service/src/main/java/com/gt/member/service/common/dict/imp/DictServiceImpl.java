package com.gt.member.service.common.dict.imp;

import java.util.*;

import com.gt.api.enums.ResponseEnums;
import com.gt.common.entity.BusUserEntity;
import com.gt.member.dao.common.BusUserDAO;
import com.gt.member.dao.common.BusUserNumDAO;
import com.gt.member.dao.common.DictItemsDAO;
import com.gt.member.exception.BusinessException;
import com.gt.member.service.common.dict.DictService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DictServiceImpl implements DictService {

    private static final Logger LOG = LoggerFactory.getLogger( DictServiceImpl.class );

    @Autowired
    private DictItemsDAO dictItemsDAO;

    @Autowired
    private BusUserDAO busUserDAO;

    @Autowired
    private BusUserNumDAO busUserNumDAO;

    @Override
    public SortedMap<String, Object> getDict( String type ) throws BusinessException {
	try {
	    SortedMap<String, Object> map = new TreeMap<String, Object>();
	    List<Map<String, Object>> list =  dictItemsDAO.getDictReturnKeyAndValue( type );
	    for (Map<String, Object> map2 : list) {
		map.put(map2.get("item_key").toString(), map2.get("item_value"));
	    }
	    return map;
	} catch ( Exception e ) {
	    LOG.error( "查询字典异常", e );
	    throw new BusinessException( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getMsg() );
	}
    }

    @Override
    public String getDictRuturnValue( String type, Integer key ) throws BusinessException {
	try {
	    return dictItemsDAO.getDiceReturnValue( type, key );
	} catch ( Exception e ) {
	    LOG.error( "查询字典值异常", e );
	    throw new BusinessException( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getMsg() );
	}
    }

    @Override
    public String dictBusUserNum( Integer userid, Integer level, Integer style, String dictstyle ) {
	List<Map<String,Object>> obj = busUserNumDAO.findBusUserNum( userid, style);
	if(obj.size()>0){
	    return obj.get(0).get("usernum").toString();
	}else{
	    List<Map<String,Object>> obj1 =  dictItemsDAO.findMemberCountByUserId(dictstyle,level  );
	    if(obj1.size()>0){
		return  obj1.get(0).get("item_value").toString();
	    }
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

    @Override
    public Integer pidUserId( Integer user_id ) {
	// TODO Auto-generated method stub
	BusUserEntity busUserEntity = busUserDAO.selectById( user_id );
	if ( busUserEntity.getPid() > 0 ) {
	    pidUserId( busUserEntity.getPid() );
	}
	return busUserEntity.getId();
    }

}
