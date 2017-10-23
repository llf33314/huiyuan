/**
 * P 2016年3月8日
 */
package com.gt.member.service.member.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gt.member.dao.*;
import com.gt.member.entity.*;
import com.gt.member.service.common.MemberCommonService;
import com.gt.member.service.member.MemberCardService;
import com.gt.member.util.CommonUtil;
import com.gt.member.util.PropertiesUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author pengjiangli
 * @version 创建时间:2016年3月8日
 */
@Service
public class MemberCardServiceImpl implements MemberCardService {

    private static final Logger LOG = Logger.getLogger( MemberCardServiceImpl.class );

    @Autowired
    private MemberCardmodelDAO cardModelMapper;

    @Autowired
    private MemberCardrecordDAO cardRecordMapper;

    @Autowired
    private MemberEntityDAO memberMapper;

    @Autowired
    private MemberCommonService memberCommonService;

    @Autowired
    private MemberGradetypeDAO memberGradetypeDAO;

    @Autowired
    private MemberCardtypeDAO memberCardtypeDAO;

    @Autowired
    private MemberDateDAO memberDateDAO;

    @Autowired
    private MemberFindDAO memberFindDAO;

    @Autowired
    private MemberCardmodelDAO memberCardmodelDAO;

    @Autowired
    private MemberGiveruleDAO memberGiveruleDAO;

    @Autowired
    private MemberGiverulegoodstypeDAO	memberGiverulegoodstypeDAO;


    /**
     * 查询会员卡类型
     * @param busId
     * @return
     */
    public List<MemberCardtype> findCardType(Integer busId){
	return memberCardtypeDAO.findByBusId(  busId);
    }

    /**
     * 分组查询卡片信息
     */
    @Override
    public List< MemberCardmodel > findGroupByType( Integer busId ) {
	return cardModelMapper.findGroupByType( busId );
    }

    /**
     * 根据类型查询模板
     */
    @Override
    public List< MemberCardmodel > findByType( Integer cmType ) {
	return cardModelMapper.findByType( cmType );
    }

    /**
     * 根据商户id和模板id 查询同一类型模板
     */
    @Override
    public List< MemberCardmodel > findCmType( Integer publicId, Integer ctId ) {
	Integer cmType = cardModelMapper.findCmType( publicId, ctId );
	return findByType( cmType );
    }

    @Transactional
    @Override
    public void saveCardModel( Integer busId, String param ) {
	JSONArray json = JSONArray.fromObject( param );
	MemberCardmodel cardModel = cardModelMapper.findCardModel();
	int i = 0;
	for ( Object object : json ) {
	    i++;
	    JSONObject obj = JSONObject.fromObject( object );
	    MemberCardmodel cm = new MemberCardmodel();
	    cm.setCmUrl( obj.getString( "url" ).split( "upload" )[1] );
	    cm.setCmType( cardModel.getCmType() + 1 );
	    cm.setCmSort( i );
	    cm.setBusId( busId );
	    cardModelMapper.insert( cm );
	}

    }

    /**
     * 查询会员卡信息
     *
     * @param busId
     * @param ctId
     *
     * @return
     */
    public Map< String,Object > editGradeTypeFrist( Integer busId, Integer ctId ) {
	Map< String,Object > map = new HashMap<>();
	if ( CommonUtil.isNotEmpty( ctId ) && ctId > 0 ) {
	    MemberCardtype memberCardtype = memberCardtypeDAO.selectById( ctId );  //会员卡类型
	    List< MemberCardtype > list = new ArrayList<>();
	    list.add( memberCardtype );
	    map.put( "cardTypes", memberCardtype );
	    List< Map< String,Object > > gradeTypes = memberGradetypeDAO.findAllByBusIdAndCtId( busId, ctId );
	    if ( ctId == 2 || ctId == 3 ) {
		map.put( "fuka", 1 );


	    }
	    if ( CommonUtil.isNotEmpty( gradeTypes ) && gradeTypes.size() > 0 ) {
		map.put( "fanhuiyuan", gradeTypes.get( 0 ).get( "iseasy" ) );
		map.put( "shenhe", gradeTypes.get( 0 ).get( "isCheck" ) );
		map.put( "assistantCard", gradeTypes.get( 0 ).get( "assistantCard" ) );
		map.put( "ismemberDate", gradeTypes.get( 0 ).get( "ismemberDate" ) );
		if ( "0".equals( CommonUtil.toString( gradeTypes.get( 0 ).get( "ismemberDate" ) ) ) ) {
		    MemberDate memberdate = memberDateDAO.findByBusIdAndCtId( busId, ctId );
		    map.put( "cardTypes", memberdate );
		}
		//员卡模板创建的等级
		if ( "1".equals( CommonUtil.toString( gradeTypes.get( 0 ).get( "iseasy" ) ) ) ) {
		    map.put( "dengji", gradeTypes.size() - 1 );
		} else {
		    map.put( "dengji", gradeTypes.size() );
		}
	    }
	} else {
	    //新增会员卡
	    List< MemberCardtype > cardTypes = memberCardtypeDAO.findByBusId( busId );
	    map.put( "cardTypes", cardTypes );
	}
	List<Integer> fukaList = findCtId( ctId );
	map.put( "fukaList",fukaList );
	MemberFind memberFind = memberFindDAO.findByQianDao( busId );
	if ( CommonUtil.isNotEmpty( memberFind ) ) {
	    map.put( "qiandaojifen", memberFind.getIntegral() );
	}
	return map;
    }

    /**
     * @param busId
     * @param ctId
     */
    public Map< String,Object > editGradeTypeSecond( Integer busId, Integer ctId ) {
	Map< String,Object > map = new HashMap<>();
	List< Map< String,Object > > gradeTypes = memberGradetypeDAO.findAllByBusIdAndCtId( busId, ctId );
	if ( CommonUtil.isNotEmpty( gradeTypes ) && gradeTypes.size() > 0 ) {
	    map.put( "isleft", gradeTypes.get( 0 ).get( "isleft" ) );
	    map.put( "gradeTypes", gradeTypes );
	    map.put( "imagePaht", PropertiesUtil.getRes_web_path() );
	}
	return map;
    }

    public List<Integer> findCtId( Integer ctId ) {
	List<Integer> list=new ArrayList<>(  );
	switch ( ctId ) {
	    case 2:
		list.add( 4 );
		list.add( 5 );
		return list;
	    case 3:
		list.add( 2 );
		list.add( 4 );
		list.add( 5 );
		return list;
	}
	return list;
    }

    /**
     * 新增会员第3步信息pc
     * @param busId
     * @param ctId
     */
    public Map< String,Object > editGradeTypeThird(Integer busId,Integer ctId){
	Map< String,Object > map = new HashMap<>();
	List< Map< String,Object > > gradeTypes = memberGradetypeDAO.findAllByBusIdAndCtId( busId, ctId );
	if ( CommonUtil.isNotEmpty( gradeTypes ) && gradeTypes.size() > 0 ) {
	    map.put( "gradeTypes", gradeTypes );
	    List<Map<String, Object>> giveRules=memberGiveruleDAO.findByBusIdAndCtId( busId, ctId );
	    map.put( "giveRules", giveRules );
	    if(CommonUtil.isNotEmpty( giveRules ) && giveRules.size()>0){
	        Integer gr_id=CommonUtil.toInteger(giveRules.get( 0 ).get( "gr_id" ));
		List<Map<String, Object>>  goodTypes=memberGiverulegoodstypeDAO.findByGrId( gr_id );
		map.put( "goodTypes",goodTypes );
	    }
	}
	return map;
    }


    /**
     * 商家卡片背景模板
     *
     * @param busId
     *
     * @return
     */
    public List< MemberCardmodel > findCardModel( Integer busId ) {
	List< MemberCardmodel > cardmodels = memberCardmodelDAO.findBybusId( busId );
	return cardmodels;
    }

    @Override
    public void clearJifen( String busIds ) {
	try {
	    List< Integer > list = new ArrayList< Integer >();
	    String[] str = busIds.split( "," );
	    for ( String s : str ) {
		if ( CommonUtil.isNotEmpty( s ) ) {
		    list.add( CommonUtil.toInteger( s ) );
		}
	    }
	    //	memberService.clearJifen(list);
	} catch ( Exception e ) {
	    LOG.error( "httpclien调用积分清0异常", e );
	}
    }

    @Override
    public void jifenLog( String str ) {
	JSONObject obj = JSONObject.fromObject( str );
//	memberCommonService.saveCardRecordNew( obj.getInt( "mcId" ), (byte) 2, obj.getString( "number" ), obj.getString( "itemName" ), obj.getInt( "busId" ), null, null,
//			obj.getDouble( "amount" ) );

    }

}
