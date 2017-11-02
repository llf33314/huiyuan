/**
 * P 2016年3月8日
 */
package com.gt.member.service.member.impl;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.*;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gt.api.bean.session.BusUser;
import com.gt.api.bean.session.WxPublicUsers;
import com.gt.api.enums.ResponseEnums;
import com.gt.api.util.HttpClienUtils;
import com.gt.api.util.RequestUtils;
import com.gt.api.util.SessionUtils;
import com.gt.common.entity.BusUserEntity;
import com.gt.common.entity.WxPublicUsersEntity;
import com.gt.common.entity.WxShop;
import com.gt.member.dao.*;
import com.gt.member.dao.common.BusUserBranchRelationDAO;
import com.gt.member.dao.common.BusUserDAO;
import com.gt.member.dao.common.WxPublicUsersDAO;
import com.gt.member.dao.common.WxShopDAO;
import com.gt.member.entity.*;
import com.gt.member.enums.ResponseMemberEnums;
import com.gt.member.exception.BusinessException;
import com.gt.member.export.ExcelStyle;
import com.gt.member.service.bo.ErrorWorkbook;
import com.gt.member.service.common.dict.DictService;
import com.gt.member.service.common.membercard.MemberCommonService;
import com.gt.member.service.member.MemberCardService;
import com.gt.member.util.*;

import com.gt.util.entity.param.sms.OldApiSms;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

/**
 * @author pengjiangli
 * @version 创建时间:2016年3月8日
 */
@Service
public class MemberCardServiceImpl implements MemberCardService {

    private static final Logger LOG = Logger.getLogger( MemberCardServiceImpl.class );

    //发送短信
    private final String SEND_SMS = "/8A5DA52E/smsapi/6F6D9AD2/79B4DE7C/sendSmsOld.do";

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
    private MemberGiverulegoodstypeDAO memberGiverulegoodstypeDAO;

    @Autowired
    private MemberGoodstypeDAO memberGoodstypeDAO;

    @Autowired
    private MemberRechargegiveDAO memberRechargegiveDAO;

    @Autowired
    private MemberOptionDAO memberOptionDAO;

    @Autowired
    private MemberGradetypeAssistantDAO memberGradetypeAssistantDAO;

    @Autowired
    private MemberRechargegiveAssistantDAO memberRechargegiveAssistantDAO;

    @Autowired
    private PublicParametersetDAO publicParametersetDAO;

    @Autowired
    private MemberGiftDAO memberGiftDAO;

    @Autowired
    private MemberCardDAO memberCardDAO;

    @Autowired
    private BusUserDAO busUserDAO;

    @Autowired
    private DictService dictService;

    @Autowired
    private WxShopDAO wxShopDAO;

    @Autowired
    private MemberCardOldDAO memberCardOldDAO;

    @Autowired
    private UserConsumeNewDAO userConsumeNewDAO;

    @Autowired
    private UserConsumePayDAO userConsumePayDAO;

    @Autowired
    private MemberCardLentDAO memberCardLentDAO;

    @Autowired
    private BusUserBranchRelationDAO busUserBranchRelationDAO;

    @Autowired
    private WxPublicUsersDAO wxPublicUsersDAO;

    @Autowired
    private WxCardReceiveDAO wxCardReceiveDAO;

    @Autowired
    private MemberRecommendDAO memberRecommendDAO;

    @Autowired
    private PublicParametersetDAO publicParameterSetMapper;

    @Autowired
    private MemberPicklogDAO memberPicklogDAO;

    @Autowired
    private MemberOldDAO memberOldDAO;

    /**
     * 查询会员卡类型
     *
     * @param busId
     *
     * @return
     */
    public List< MemberCardtype > findCardType( Integer busId ) {
	return memberCardtypeDAO.findByBusId( busId );
    }

    /**
     * 分组查询卡片信息
     */
    @Override
    public Map<String,Object>  findCardModelByBusId( Integer busId ) throws BusinessException {
	if ( CommonUtil.isEmpty( busId ) ) {
	    throw new BusinessException( ResponseMemberEnums.INVALID_SESSION );
	}
	List< MemberCardmodel > cardmodels= cardModelMapper.findCardModelByBusId( busId );
	Map<String,Object> map=new HashMap<>(  );
	map.put( "cardmodels",cardmodels );
	map.put( "path",PropertiesUtil.getRes_web_path() );
	return map;
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
	if ( CommonUtil.isEmpty( busId ) ) {
	    throw new BusinessException( ResponseMemberEnums.INVALID_SESSION );
	}
	JSONArray json = JSONArray.parseArray( param );
	MemberCardmodel cardModel = cardModelMapper.findCardModel();
	int i = 0;
	for ( Object object : json ) {
	    i++;
	    JSONObject obj = JSONObject.parseObject( object.toString() );
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
	if ( CommonUtil.isEmpty( busId ) ) {
	    throw new BusinessException( ResponseMemberEnums.INVALID_SESSION );
	}
	Map< String,Object > map = new HashMap<>();
	if ( CommonUtil.isNotEmpty( ctId ) && ctId > 0 ) {
	    MemberCardtype memberCardtype = memberCardtypeDAO.selectById( ctId );  //会员卡类型
	    List< MemberCardtype > list = new ArrayList<>();
	    list.add( memberCardtype );
	    map.put( "cardTypes", memberCardtype );
	    List< Map< String,Object > > gradeTypes = memberGradetypeDAO.findAllByBusIdAndCtId( busId, ctId );
	    if ( ctId == 2 || ctId == 3 ) {
		map.put( "fuka", 1 );
		if(gradeTypes.size()>0) {
		    Integer gtId = CommonUtil.toInteger( gradeTypes.get( 0 ).get( "gtId" ) );
		    //查询开通的副卡
		    List< Integer > assistantS = memberGradetypeAssistantDAO.findAssistantBygtId( busId, gtId );
		    map.put( "xuanZhongfuka", assistantS );
		}
	    } else {
		map.put( "fuka", 0 );
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
	List< Map< String,Object > > fukaList = findCtId( ctId );
	map.put( "fukaList", fukaList );
	MemberFind memberFind = memberFindDAO.findByQianDao( busId );
	map.put( "qiandaojifen", "" );
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
	if ( CommonUtil.isEmpty( busId ) ) {
	    throw new BusinessException( ResponseMemberEnums.INVALID_SESSION );
	}
	Map< String,Object > map = new HashMap<>();
	List< Map< String,Object > > gradeTypes = memberGradetypeDAO.findAllByBusIdAndCtId( busId, ctId );
	if ( CommonUtil.isNotEmpty( gradeTypes ) && gradeTypes.size() > 0 ) {
	    map.put( "isleft", gradeTypes.get( 0 ).get( "isleft" ) );
	    map.put( "gradeTypes", gradeTypes );
	    map.put( "imagePaht", PropertiesUtil.getRes_web_path() );
	}
	return map;
    }

    public List< Map< String,Object > > findCtId( Integer ctId ) {
	List< Map< String,Object > > list = new ArrayList<>();
	Map< String,Object > map = null;
	switch ( ctId ) {
	    case 2:
		map = new HashMap<>();
		map.put( "id", 4 );
		map.put( "name", "时效卡" );

		list.add( map );

		map = new HashMap<>();
		map.put( "id", 5 );
		map.put( "name", "次卡" );

		list.add( map );
		return list;
	    case 3:
		map = new HashMap<>();
		map.put( "id", 2 );
		map.put( "name", "折扣卡" );
		list.add( map );

		map = new HashMap<>();
		map.put( "id", 4 );
		map.put( "name", "时效卡" );
		list.add( map );

		map = new HashMap<>();
		map.put( "id", 5 );
		map.put( "name", "次卡" );
		list.add( map );
		return list;
	}
	return list;
    }

    /**
     * 新增会员第3步信息pc
     *
     * @param busId
     * @param ctId
     */
    public Map< String,Object > editGradeTypeThird( Integer busId, Integer ctId ) {
	if ( CommonUtil.isEmpty( busId ) ) {
	    throw new BusinessException( ResponseMemberEnums.INVALID_SESSION );
	}
	Map< String,Object > map = new HashMap<>();

	PublicParameterset parameterset= publicParametersetDAO.findBybusId( busId );
	map.put( "parameterset",parameterset );

	List< Map< String,Object > > gradeTypes = memberGradetypeDAO.findAllByBusIdAndCtId( busId, ctId );
	if ( CommonUtil.isNotEmpty( gradeTypes ) && gradeTypes.size() > 0 ) {
	    map.put( "gradeTypes", gradeTypes );

	    List< Map< String,Object > > giveRules = memberGiveruleDAO.findByBusIdAndCtId( busId, ctId );
	    map.put( "giveRules", giveRules );

	    if ( CommonUtil.isNotEmpty( giveRules ) && giveRules.size() > 0 ) {
		Integer gr_id = CommonUtil.toInteger( giveRules.get( 0 ).get( "gr_id" ) );
		List< Map< String,Object > > goodTypes = memberGiverulegoodstypeDAO.findByGrId( gr_id );
		map.put( "goodTypes", goodTypes );
		map.put( "grEquities", giveRules.get( 0 ).get( "gr_equities" ));
		map.put( "grUpgradeCount", giveRules.get( 0 ).get( "gr_upgradeCount" ));
		map.put( "upgradeType", giveRules.get( 0 ).get( "gr_upgradeType" ));
		if(ctId==2) {
		    List<Integer> discountS=new ArrayList<>(  );
		    for ( Map< String,Object > giveRule : giveRules ) {
			discountS.add( CommonUtil.toInteger( giveRule.get( "gr_discount" ) ) );
		    }
		    map.put( "discountS", discountS);
		}
	    }
	    List< Integer > gtIds = new ArrayList<>();
	    for ( Map< String,Object > gradeType : gradeTypes ) {
		Integer gtId = CommonUtil.toInteger( gradeType.get( "gtId" ) );
		gtIds.add( gtId );
	    }

	    Integer gtId = CommonUtil.toInteger( gradeTypes.get( 0 ).get( "gtId" ) );
	    //查询开通的副卡
	    List< Integer > assistantS = memberGradetypeAssistantDAO.findAssistantBygtId( busId, gtId );

	    for ( Integer asstistantCtId : assistantS ) {
		if ( asstistantCtId == 2 ) {
		    List< Map< String,Object > > disCountFuka = memberGradetypeAssistantDAO.findAssistantByctId( busId, 2 );
		    map.put( "discountFuka", disCountFuka );
		}
		if ( asstistantCtId == 4 ) {
		    //时效卡
		    Map< String,Object > rechargeGiveMap = new HashMap<>();
		    List< Map< String,Object > > rechargeGiveList = null;
		    List< Map< String,Object > > rechargeGives = memberRechargegiveAssistantDAO.findByCtIdAndfuCtId( busId, ctId, 4 );
		    for ( Map< String,Object > gradeType : gradeTypes ) {
			rechargeGiveList = new ArrayList<>();
			for ( Map< String,Object > rechargeGive : rechargeGives ) {
			    if ( CommonUtil.toInteger( gradeType.get( "gtId" ) ).equals( CommonUtil.toInteger( rechargeGive.get( "gtId" ) ) ) ) {
				rechargeGiveList.add( rechargeGive );
			    }
			}
			rechargeGiveMap.put( CommonUtil.toString( gradeType.get( "gtId" ) ), rechargeGiveList );
		    }
		    map.put( "shixiaoKaiRechargeGive", rechargeGiveMap );
		}

		if ( asstistantCtId == 5 ) {
		    //时效卡
		    Map< String,Object > rechargeGiveMap = new HashMap<>();
		    List< Map< String,Object > > rechargeGiveList = null;
		    List< Map< String,Object > > rechargeGives = memberRechargegiveAssistantDAO.findByCtIdAndfuCtId( busId, ctId, 5 );
		    for ( Map< String,Object > gradeType : gradeTypes ) {
			rechargeGiveList = new ArrayList<>();
			for ( Map< String,Object > rechargeGive : rechargeGives ) {
			    if ( CommonUtil.toInteger( gradeType.get( "gtId" ) ).equals( CommonUtil.toInteger( rechargeGive.get( "gtId" ) ) ) ) {
				rechargeGiveList.add( rechargeGive );
			    }
			}
			rechargeGiveMap.put( CommonUtil.toString( gradeType.get( "gtId" ) ), rechargeGiveList );
		    }
		    map.put( "ciKaKaiRechargeGive", rechargeGiveMap );
		}

	    }

	}
	return map;
    }

    public MemberOption findOption( Integer busId ) throws BusinessException {
	try {
	    if ( CommonUtil.isEmpty( busId ) ) {
		throw new BusinessException( ResponseMemberEnums.INVALID_SESSION );
	    }
	    MemberOption memberOption = memberOptionDAO.findByBusId( busId );
	    return memberOption;
	} catch ( Exception e ) {
	    throw new BusinessException( ResponseEnums.ERROR );
	}
    }

    public void saveOrUpdateOption( String json, Integer busId ) throws BusinessException {
	if ( CommonUtil.isEmpty( busId ) ) {
	    throw new BusinessException( ResponseMemberEnums.INVALID_SESSION );
	}
	JSONObject jsonObject = JSONObject.parseObject( json );
	MemberOption memberOption = new MemberOption();
	memberOption.setId( CommonUtil.toInteger( jsonObject.get( "id" ) ) );
	memberOption.setNameOption( CommonUtil.toInteger( jsonObject.get( "nameRadio" ) ) );
	memberOption.setSexOption( CommonUtil.toInteger( jsonObject.get( "sexRadio" ) ) );
	memberOption.setPhoneOption( CommonUtil.toInteger( jsonObject.get( "phoneRadio" ) ) );
	memberOption.setAddrOption( CommonUtil.toInteger( jsonObject.get( "addressRadio" ) ) );
	memberOption.setAddrDetailOption( CommonUtil.toInteger( jsonObject.get( "addressMoreRadio" ) ) );
	memberOption.setMailOption( CommonUtil.toInteger( jsonObject.get( "emailRadio" ) ) );
	memberOption.setGetMoneyOption( CommonUtil.toInteger( jsonObject.get( "incomeRadio" ) ) );
	memberOption.setBirthOption( CommonUtil.toInteger( jsonObject.get( "birthRadio" ) ) );
	memberOption.setCardOption( CommonUtil.toInteger( jsonObject.get( "idNumRadio" ) ) );

	memberOption.setNameShow1( CommonUtil.toInteger( JSON.parseArray( CommonUtil.toString( jsonObject.get( "nameCheck" ) ) ).get( 0 ) ) );
	memberOption.setSexShow1( CommonUtil.toInteger( JSON.parseArray( CommonUtil.toString( jsonObject.get( "sexCheck" ) ) ).get( 0 ) ) );
	memberOption.setAddrShow1( CommonUtil.toInteger( JSON.parseArray( CommonUtil.toString( jsonObject.get( "adressCheck1" ) ) ).get( 0 ) ) );
	memberOption.setAddrShow( CommonUtil.toInteger( JSON.parseArray( CommonUtil.toString( jsonObject.get( "adressCheck" ) ) ).get( 0 ) ) );
	memberOption.setAddrDetailShow1( CommonUtil.toInteger( JSON.parseArray( CommonUtil.toString( jsonObject.get( "adressMoreCheck1" ) ) ).get( 0 ) ) );
	memberOption.setAddrDetailShow( CommonUtil.toInteger( JSON.parseArray( CommonUtil.toString( jsonObject.get( "adressMoreCheck" ) ) ).get( 0 ) ) );
	memberOption.setMailShow1( CommonUtil.toInteger( JSON.parseArray( CommonUtil.toString( jsonObject.get( "emailCheck1" ) ) ).get( 0 ) ) );
	memberOption.setMailShow( CommonUtil.toInteger( JSON.parseArray( CommonUtil.toString( jsonObject.get( "emailCheck" ) ) ).get( 0 ) ) );
	memberOption.setGetMoneyShow1( CommonUtil.toInteger( JSON.parseArray( CommonUtil.toString( jsonObject.get( "incomeCheck1" ) ) ).get( 0 ) ) );
	memberOption.setGetMoneyShow( CommonUtil.toInteger( JSON.parseArray( CommonUtil.toString( jsonObject.get( "incomeCheck" ) ) ).get( 0 ) ) );
	memberOption.setBirthShow1( CommonUtil.toInteger( JSON.parseArray( CommonUtil.toString( jsonObject.get( "birthCheck1" ) ) ).get( 0 ) ) );
	memberOption.setBirthShow( CommonUtil.toInteger( JSON.parseArray( CommonUtil.toString( jsonObject.get( "birthCheck" ) ) ).get( 0 ) ) );
	memberOption.setCardShow1( CommonUtil.toInteger( JSON.parseArray( CommonUtil.toString( jsonObject.get( "idNumCheck1" ) ) ).get( 0 ) ) );
	memberOption.setCardShow( CommonUtil.toInteger( JSON.parseArray( CommonUtil.toString( jsonObject.get( "idNumCheck" ) ) ).get( 0 ) ) );

	memberOption.setUploadCard( CommonUtil.toInteger( JSON.parseArray( CommonUtil.toString( jsonObject.get( "idCheck" ) ) ).get( 0 ) ) );

	memberOption.setBusId( busId );
	if ( CommonUtil.isNotEmpty( memberOption.getId() ) ) {
	    memberOptionDAO.updateById( memberOption );
	} else {
	    memberOptionDAO.insert( memberOption );
	}
    }

    /**
     * 保存会员卡设置
     *
     * @param json
     */
    @Transactional
    public void saveOrUpdateGradeType( String json, Integer busUserId ) throws BusinessException {
	try {
	    if ( CommonUtil.isEmpty( busUserId ) ) {
		throw new BusinessException( ResponseMemberEnums.INVALID_SESSION );
	    }
	    Map< String,Object > publicParams = JSON.parseObject( json );
	    //设置
	    Integer ctId = CommonUtil.toInteger( publicParams.get( "ctId" ) );
	    if ( CommonUtil.isNotEmpty( publicParams.get( "qiandao" ) ) ) {
		Integer qiandao = CommonUtil.toInteger( publicParams.get( "qiandao" ) );
		if ( qiandao > 0 ) {
		    MemberFind memberFind = memberFindDAO.findByQianDao( busUserId );
		    if ( CommonUtil.isEmpty( memberFind ) ) {
			MemberFind mf = new MemberFind();
			mf.setIntegral( qiandao );
			mf.setModel( 1 );
			mf.setBusId( busUserId );
			mf.setSoure( 0 );
			mf.setTitle( "签到" );
			mf.setType( 2 );
			memberFindDAO.insert( mf );
		    } else {
			memberFind.setIntegral( qiandao );
			memberFind.setModel( 1 );
			memberFind.setBusId( busUserId );
			memberFind.setSoure( 0 );
			memberFind.setTitle( "签到" );
			memberFind.setType( 2 );
			memberFindDAO.updateById( memberFind );
		    }
		}
	    }

	    //积分规则抵扣

	    PublicParameterset ps = new PublicParameterset();
	    if ( CommonUtil.isNotEmpty( publicParams.get( "integralRatio" ) ) ) {
		ps.setIntegralRatio( CommonUtil.toDouble( publicParams.get( "integralRatio" ) ) );
	    }
	    if ( CommonUtil.isNotEmpty( publicParams.get( "startMoney" ) ) ) {
		ps.setStartMoney( CommonUtil.toDouble( publicParams.get( "startMoney" ) ) );
	    }
	    if ( CommonUtil.isNotEmpty( publicParams.get( "isclearJifen" ) ) ) {
		ps.setIsclearJifen( CommonUtil.toInteger( publicParams.get( "isclearJifen" ) ) );
	    }
	    if ( CommonUtil.isNotEmpty( publicParams.get( "integralRatio" ) ) ) {
		ps.setIntegralRatio( CommonUtil.toDouble( publicParams.get( "integralRatio" ) ) );
	    }
	    if ( CommonUtil.isNotEmpty( publicParams.get( "parametersetId" ) ) ) {
		ps.setId( CommonUtil.toInteger( publicParams.get( "parametersetId" ) ) );
	    }

	    ps.setBusId( busUserId );
	    if ( CommonUtil.isNotEmpty( ps.getId() ) ) {
		publicParameterSetMapper.updateById( ps );
	    } else {
		publicParameterSetMapper.insert( ps );
	    }

	    //会员日操作
	    Integer ismemberDate = CommonUtil.toInteger( publicParams.get( "ismemberDate" ) );
	    if ( ismemberDate == 1 ) {
		MemberDate memberdate = new MemberDate();
		memberdate.setBusId( busUserId );
		memberdate.setCtId( ctId );
		if ( CommonUtil.isNotEmpty( publicParams.get( "dateType" ) ) ) {
		    Integer dateType = CommonUtil.toInteger( publicParams.get( "dateType" ) );
		    memberdate.setDateType( dateType );
		}
		if ( CommonUtil.isNotEmpty( publicParams.get( "dateStr" ) ) ) {
		    String dateStr = CommonUtil.toString( publicParams.get( "dateStr" ) );
		    memberdate.setDateStr( dateStr );
		}
		if ( CommonUtil.isNotEmpty( publicParams.get( "discount" ) ) ) {
		    Integer discount = CommonUtil.toInteger( publicParams.get( "discount" ) );
		    memberdate.setDiscount( discount );
		}
		if ( CommonUtil.isNotEmpty( publicParams.get( "fans_currency" ) ) ) {
		    Integer fans_currency = CommonUtil.toInteger( publicParams.get( "fans_currency" ) );
		    memberdate.setFansCurrency( fans_currency );
		}
		if ( CommonUtil.isNotEmpty( publicParams.get( "flow" ) ) ) {
		    Integer flow = CommonUtil.toInteger( publicParams.get( "flow" ) );
		    memberdate.setFlow( flow );
		}
		if ( CommonUtil.isNotEmpty( publicParams.get( "integral" ) ) ) {
		    Integer integral = CommonUtil.toInteger( publicParams.get( "integral" ) );
		    memberdate.setIntegral( integral );
		}

		if ( CommonUtil.isNotEmpty( publicParams.get( "memberDateId" ) ) ) {
		    Integer id = CommonUtil.toInteger( publicParams.get( "memberDateId" ) );
		    memberdate.setId( id );
		}

		if ( CommonUtil.isNotEmpty( memberdate.getId() ) ) {
		    memberDateDAO.updateById( memberdate );
		} else {
		    memberDateDAO.insert( memberdate );
		}
	    }

	    List< Map > gradeTypes = JSON.parseArray( CommonUtil.toString( publicParams.get( "gradeType" ) ), Map.class );
	    for ( Map map : gradeTypes ) {
		//模板设置
		MemberGradetype gt = new MemberGradetype();
		gt.setGtId( CommonUtil.toInteger( map.get( "gtId" ) ) );
		gt.setBusId( busUserId );
		gt.setGtGradeName( CommonUtil.toString( map.get( "gtGradeName" ) ) );
		gt.setIsleft( CommonUtil.toInteger( map.get( "isleft" ) ) );
		gt.setGtName( CommonUtil.toString( map.get( "gtName" ) ) );
		gt.setGtNameColor( CommonUtil.toString( map.get( "gtNameColor" ) ) );
		gt.setCmId( CommonUtil.toInteger( map.get( "cmId" ) ) );
		gt.setGtTextColor( CommonUtil.toString( map.get( "gtTextColor" ) ) );
		gt.setCtId( ctId );
		String gtLoginUrl = CommonUtil.toString( map.get( "gtLoginUrl" ) );
		if ( "/images/add_Image.png".equals( gtLoginUrl ) ) {
		    gt.setGtLoginUrl( null );
		}
		if ( !"/images/add_Image.png".equals( gtLoginUrl ) && CommonUtil.isNotEmpty( gtLoginUrl ) ) {
		    gt.setGtLoginUrl( gtLoginUrl.split( "/upload" )[1] );
		}

		int buyModel = CommonUtil.toInteger( map.get( "buyModel" ) );
		gt.setBuyModel( buyModel );
		if ( buyModel == 1 ) {
		    gt.setApplyType( 3 );
		}

		if ( CommonUtil.isNotEmpty( map.get( "buyMoney" ) ) ) {
		    Double buyMoney = CommonUtil.toDouble( map.get( "buyMoney" ) );
		    gt.setBuyMoney( buyMoney );
		}
		if ( CommonUtil.isNotEmpty( map.get( "costMoney" ) ) ) {
		    Double costMoney = CommonUtil.toDouble( map.get( "costMoney" ) );
		    gt.setCostMoney( costMoney );
		}
		if ( CommonUtil.isNotEmpty( map.get( "balance" ) ) ) {
		    String balance = CommonUtil.toString( map.get( "balance" ) );
		    gt.setBalance( balance );
		}

		gt.setIsmemberDate( ismemberDate );
		int view = CommonUtil.toInteger( map.get( "view" ) );
		gt.setIsView( view );

		//会员审核
		int isCheck = CommonUtil.toInteger( map.get( "isCheck" ) );
		gt.setIsCheck( isCheck );

		int assistantCard = CommonUtil.toInteger( map.get( "assistantCard" ) );
		gt.setAssistantCard( assistantCard );

		int easyApply = CommonUtil.toInteger( map.get( "easyApply" ) );
		gt.setEasyApply( easyApply );

		//推荐信息
		Integer isrecommend = CommonUtil.toInteger( publicParams.get( "isrecommend" ) );
		gt.setIsrecommend( isrecommend );
		if ( isrecommend == 1 ) {
		    if ( CommonUtil.isNotEmpty( publicParams.get( "giveflow" ) ) ) {
			gt.setGiveflow( CommonUtil.toInteger( publicParams.get( "giveflow" ) ) );

		    }
		    if ( CommonUtil.isNotEmpty( publicParams.get( "givefenbi" ) ) ) {
			gt.setGivefenbi( CommonUtil.toInteger( publicParams.get( "givefenbi" ) ) );

		    }
		    if ( CommonUtil.isNotEmpty( publicParams.get( "giveIntegral" ) ) ) {
			gt.setGiveIntegral( CommonUtil.toInteger( publicParams.get( "giveIntegral" ) ) );

		    }
		    if ( CommonUtil.isNotEmpty( publicParams.get( "giveMoney" ) ) ) {
			gt.setGiveMoney( CommonUtil.toDouble( publicParams.get( "giveMoney" ) ) );

		    }

		}

		if ( CommonUtil.isEmpty( gt.getGtId() ) ) {
		    memberGradetypeDAO.insert( gt );
		} else {
		    memberGradetypeDAO.updateById( gt );
		}

		//赠送规则设置
		List< Map > giveRuleBean = JSON.parseArray( CommonUtil.toString( map.get( "giveRule" ) ), Map.class );
		for ( Map grule : giveRuleBean ) {
		    MemberGiverule gr = new MemberGiverule();
		    gr.setCreateUserId( busUserId );
		    gr.setCtId( ctId );
		    if ( CommonUtil.isNotEmpty( grule.get( "grDiscount" ) ) ) {
			Double discount = Double.valueOf( grule.get( "grDiscount" ).toString() ) * 10;
			Integer discount1 = ( new Double( discount ) ).intValue();
			gr.setGrDiscount( discount1 );
		    }
		    if ( CommonUtil.isNotEmpty( grule.get( "grEquities" ) ) ) {
			gr.setGrEquities( grule.get( "grEquities" ).toString() );
		    }
		    if ( CommonUtil.isNotEmpty( grule.get( "grGivecount" ) ) ) {
			gr.setGrGiveCount( Integer.parseInt( grule.get( "grGivecount" ).toString() ) );
		    }
		    if ( CommonUtil.isNotEmpty( grule.get( "grGivetype" ) ) ) {
			gr.setGrGiveType( Integer.parseInt( grule.get( "grGivetype" ).toString() ) );
		    }

		    if ( CommonUtil.isNotEmpty( grule.get( "grId" ) ) ) {
			gr.setGrId( Integer.parseInt( grule.get( "grId" ).toString() ) );
		    }

		    if ( CommonUtil.isNotEmpty( grule.get( "grNumber" ) ) ) {
			gr.setGrNumber( Integer.parseInt( grule.get( "grNumber" ).toString() ) );
		    }

		    if ( CommonUtil.isNotEmpty( grule.get( "grRechargemoney" ) ) ) {
			gr.setGrRechargeMoney( Double.parseDouble( grule.get( "grRechargemoney" ).toString() ) );
		    }

		    if ( CommonUtil.isNotEmpty( grule.get( "grStartDate" ) ) ) {
			gr.setGrStartDate( DateTimeKit.parse( grule.get( "grStartDate" ).toString(), "yyyy-MM-dd" ) );
		    }

		    if ( CommonUtil.isNotEmpty( grule.get( "grUpgradeCount" ) ) ) {
			gr.setGrUpgradeCount( Integer.parseInt( grule.get( "grUpgradeCount" ).toString() ) );
		    }

		    if ( CommonUtil.isNotEmpty( grule.get( "grUpgradeType" ) ) ) {
			gr.setGrUpgradeType( Integer.parseInt( grule.get( "grUpgradeType" ).toString() ) );
		    }
		    if ( CommonUtil.isNotEmpty( grule.get( "grValidDate" ) ) && !"0".equals( grule.get( "grValidDate" ) ) ) {
			Integer grValiddate = Integer.parseInt( grule.get( "grValidDate" ).toString() );
			gr.setGrValidDate( grValiddate );
		    }

		    gr.setGtId( gt.getGtId() );

		    gr.setBusId( busUserId );
		    if ( CommonUtil.isNotEmpty( grule.get( "delayDay" ) ) ) {
			gr.setDelayDay( CommonUtil.toInteger( grule.get( "delayDay" ) ) );
		    }
		    if ( CommonUtil.isNotEmpty( grule.get( "upgradeType" ) ) ) {
			int upgradeType = CommonUtil.toInteger( grule.get( "upgradeType" ) );
			gr.setGrUpgradeType( upgradeType );
		    }

		    if ( CommonUtil.isNotEmpty( gr.getGrId() ) && gr.getGrId() != 0 ) {
			// 保存卡片规则总表
			memberGiveruleDAO.updateById( gr );
		    } else {
			memberGiveruleDAO.insert( gr );
		    }

		    // 针对储值卡和次卡充值
		    if ( CommonUtil.isNotEmpty( map.get( "rechargeGives" ) ) ) {
			List< Map > rechargeGives = JSONArray.parseArray( map.get( "rechargeGives" ).toString(), Map.class );
			// 先删除再添加
			memberRechargegiveDAO.deleteBybusIdAndGrid( busUserId, gr.getGrId() );
			for ( Map rechargeGive : rechargeGives ) {
			    MemberRechargegive rg = new MemberRechargegive();
			    rg.setCtId( ctId );
			    if ( CommonUtil.isNotEmpty( rechargeGive.get( "giveCount" ) ) ) {
				rg.setGiveCount( CommonUtil.toInteger( rechargeGive.get( "giveCount" ) ) );
			    }
			    if ( CommonUtil.isNotEmpty( rechargeGive.get( "money" ) ) ) {
				rg.setMoney( CommonUtil.toDouble( rechargeGive.get( "money" ) ) );
			    }
			    if ( CommonUtil.isNotEmpty( rechargeGive.get( "number" ) ) ) {
				rg.setNumber( CommonUtil.toInteger( rechargeGive.get( "number" ) ) );
			    }
			    if ( CommonUtil.isNotEmpty( rechargeGive.get( "isDate" ) ) ) {
				int isDate = CommonUtil.toInteger( rechargeGive.get( "isDate" ) );
				rg.setIsDate( isDate );
			    }
			    rg.setBusId( busUserId );
			    rg.setGrId( gr.getGrId() );
			    memberRechargegiveDAO.insert( rg );
			}

			//副卡操作 只能添加 不能修改
			if ( assistantCard == 1 ) {
			    List< Map > memberGradetypeAssistants = JSON.parseArray( CommonUtil.toString( map.get( "memberGradetypeAssistants" ) ), Map.class );
			    for ( Map memberGradetypeAssistant : memberGradetypeAssistants ) {
				MemberGradetypeAssistant mgta = new MemberGradetypeAssistant();
				mgta.setCtId( ctId );
				mgta.setGtId( gt.getGtId() );
				mgta.setId( CommonUtil.toInteger( memberGradetypeAssistant.get( "fukaId" ) ) );
				mgta.setDiscount( CommonUtil.toInteger( memberGradetypeAssistant.get( "discount" ) ) );
				mgta.setBusId( busUserId );
				mgta.setFuctId( CommonUtil.toInteger( memberGradetypeAssistant.get( "fukaCtId" ) ) );
				if ( CommonUtil.isEmpty( mgta.getId() ) ) {
				    memberGradetypeAssistantDAO.insert( mgta );
				} else {
				    memberGradetypeAssistantDAO.updateById( mgta );
				}

				if ( CommonUtil.isNotEmpty( memberGradetypeAssistant.get( "memberRechargegiveAssistants" ) ) ) {
				    //副卡充值
				    memberRechargegiveAssistantDAO.deleteBybusIdAndGtid( busUserId, gt.getGtId() );

				    List< Map > memberRechargegiveAssistants = JSON
						    .parseArray( CommonUtil.toString( memberGradetypeAssistant.get( "memberRechargegiveAssistants" ) ), Map.class );
				    for ( Map rechargegiveAssistant : memberRechargegiveAssistants ) {
					MemberRechargegiveAssistant rechargea = new MemberRechargegiveAssistant();
					rechargea.setFuctId( CommonUtil.toInteger( memberGradetypeAssistant.get( "fukaCtId" ) ) );
					if ( CommonUtil.isNotEmpty( rechargegiveAssistant.get( "giveCount" ) ) ) {
					    rechargea.setGiveCount( CommonUtil.toInteger( rechargegiveAssistant.get( "giveCount" ) ) );
					}
					if ( CommonUtil.isNotEmpty( rechargegiveAssistant.get( "money" ) ) ) {
					    rechargea.setMoney( CommonUtil.toDouble( rechargegiveAssistant.get( "money" ) ) );
					}
					if ( CommonUtil.isNotEmpty( rechargegiveAssistant.get( "number" ) ) ) {
					    rechargea.setNumber( CommonUtil.toInteger( rechargegiveAssistant.get( "number" ) ) );
					}
					if ( CommonUtil.isNotEmpty( rechargegiveAssistant.get( "isDate" ) ) ) {
					    int isDate = CommonUtil.toInteger( rechargegiveAssistant.get( "isDate" ) );
					    rechargea.setIsDate( isDate );
					}
					if ( CommonUtil.isNotEmpty( rechargegiveAssistant.get( "validDate" ) ) ) {
					    int validDate = CommonUtil.toInteger( rechargegiveAssistant.get( "validDate" ) );
					    rechargea.setValidDate( validDate );
					}

					rechargea.setGtId( gt.getGtId() );
					rechargea.setAssistantId( mgta.getId() );
					rechargea.setBusId( busUserId );
					rechargea.setCtId( ctId );
					if ( CommonUtil.isNotEmpty( rechargegiveAssistant.get( "rechargegiveAssistantId" ) ) ) {
					    int rechargegiveAssistantId = CommonUtil.toInteger( rechargegiveAssistant.get( "rechargegiveAssistantId" ) );
					    rechargea.setId( rechargegiveAssistantId );
					}
					if ( CommonUtil.isNotEmpty( rechargea.getId() ) ) {
					    memberRechargegiveAssistantDAO.updateById( rechargea );
					} else {
					    memberRechargegiveAssistantDAO.insert( rechargea );
					}
				    }
				}
			    }
			}
		    }
		}

		//物品赠送
		List< Map > goodsTypes = JSON.parseArray( CommonUtil.toString( publicParams.get( "goodsTypes" ) ), Map.class );
		if ( CommonUtil.isNotEmpty( goodsTypes ) ) {
		    for ( Map goodtype : goodsTypes ) {
			MemberGiverulegoodstype grgt = new MemberGiverulegoodstype();
			grgt.setGrId( 0 );
			grgt.setGtId( CommonUtil.toInteger( goodtype.get( "gtId" ) ) );
			grgt.setGiveType( CommonUtil.toInteger( goodtype.get( "giveType" ) ) );
			grgt.setMoney( CommonUtil.toDouble( goodtype.get( "money" ) ) );
			grgt.setNumber( CommonUtil.toInteger( goodtype.get( "number" ) ) );
			if ( CommonUtil.isNotEmpty( goodtype.get( "upperLmit" ) ) ) {
			    grgt.setUpperLmit( CommonUtil.toInteger( goodtype.get( "upperLmit" ) ) );
			}
			grgt.setBusId( busUserId );
			grgt.setCtId( ctId );
			Integer goodTypeId = CommonUtil.toInteger( goodtype.get( "goodTypeId" ) );
			if ( CommonUtil.isEmpty( goodTypeId ) ) {
			    memberGiverulegoodstypeDAO.insert( grgt );
			} else {
			    grgt.setId( goodTypeId );
			    memberGiverulegoodstypeDAO.updateById( grgt );
			}
		    }
		}
	    }
	} catch ( Exception e ) {
	    e.printStackTrace();
	    LOG.error( "方法saveOrUpdate：保存和修改赠送规则异常", e );
	    throw new BusinessException( ResponseEnums.ERROR );
	}
    }

    public Map< String,Object > findtongyongSet( Integer busId ) throws BusinessException {
	try {
	    if ( CommonUtil.isEmpty( busId ) ) {
		throw new BusinessException( ResponseMemberEnums.INVALID_SESSION );
	    }
	    Map< String,Object > map = new HashMap<>();
	    PublicParameterset parameterset = publicParametersetDAO.findBybusId( busId );
	    List< Map< String,Object > > memberGifts = memberGiftDAO.findBybusId( busId );
	    map.put( "parameterset", parameterset );
	    map.put( "memberGifts", memberGifts );
	    return map;
	} catch ( Exception e ) {
	    throw new BusinessException( ResponseEnums.ERROR );
	}

    }

    public void saveTongyongSet( String json, Integer busId ) throws BusinessException {
	try {
	    if ( CommonUtil.isEmpty( busId ) ) {
		throw new BusinessException( ResponseMemberEnums.INVALID_SESSION );
	    }
	    Map< String,Object > map = JSON.parseObject( json, Map.class );
	    PublicParameterset parameterset = publicParametersetDAO.findBybusId( busId );
	    if ( CommonUtil.isEmpty( parameterset ) ) {
		parameterset = new PublicParameterset();
	    }
	    parameterset.setBusId( busId );
	    parameterset.setMemberView( CommonUtil.toInteger( map.get( "memberView" ) ) );
	    if ( CommonUtil.isNotEmpty( map.get( "loginImage" ) ) ) {
		String loginImage = CommonUtil.toString( map.get( "loginImage" ) ).split( "/upload" )[1];
		parameterset.setLoginImage( loginImage );
	    }

	    if ( CommonUtil.isNotEmpty( map.get( "isOpen" ) ) ) {
		Integer isOpen = CommonUtil.toInteger( map.get( "isOpen" ) );
		parameterset.setIsOpen( isOpen );
		if ( isOpen == 1 ) {
		    parameterset.setYouhuiButton( CommonUtil.toString( map.get( "youhuiButton" ) ) );
		    parameterset.setYouhuiText( CommonUtil.toString( map.get( "youhuiText" ) ) );
		    parameterset.setButtonType( CommonUtil.toInteger( map.get( "buttonType" ) ) );
		    parameterset.setButtonUrl( CommonUtil.toString( map.get( "buttonUrl" ) ) );
		}
	    }
	    if ( CommonUtil.isNotEmpty( parameterset.getId() ) ) {
		publicParametersetDAO.updateById( parameterset );
	    } else {
		publicParametersetDAO.insert( parameterset );
	    }
	} catch ( BusinessException e ) {
	    throw e;
	} catch ( Exception e ) {
	    LOG.error( "保存通用设置失败异常：", e );
	    throw new BusinessException( ResponseEnums.ERROR );
	}
    }

    /**
     * 编辑礼品设置
     *
     * @param id
     *
     * @return
     */
    public MemberGift editGift( Integer id ) {
	if ( id > 0 ) {
	    MemberGift memberGift = memberGiftDAO.selectById( id );
	    return memberGift;
	}
	return null;
    }

    public void saveOrUpdateGift( String json, Integer busId ) throws BusinessException {
	try {
	    if ( CommonUtil.isEmpty( busId ) ) {
		throw new BusinessException( ResponseMemberEnums.INVALID_SESSION );
	    }
	    MemberGift gift = (MemberGift) JSON.toJavaObject( JSON.parseObject( json ), MemberGift.class );
	    if ( CommonUtil.isNotEmpty( gift.getId() ) ) {
		memberGiftDAO.updateById( gift );
	    } else {
		List< Map< String,Object > > gifts = memberGiftDAO.findBybusId( busId );
		if ( gifts.size() > 0 ) {
		    for ( Map< String,Object > g : gifts ) {
			if ( g.get( "modelCode" ).toString().equals( gift.getModelCode().toString() ) ) {
			    throw new BusinessException( ResponseMemberEnums.GIFT_EXIST );
			}
		    }
		}
		gift.setBusId( busId );
		memberGiftDAO.insert( gift );
	    }
	} catch ( BusinessException e ) {
	    throw e;
	} catch ( Exception e ) {
	    LOG.error( "礼品编辑保存异常", e );
	    throw new BusinessException( ResponseEnums.ERROR );
	}
    }

    public Page findMember( Integer busId, Map< String,Object > params ) {
	Map< String,Object > map = new HashMap<>();

	List< Map< String,Object > > cardTypes = memberGradetypeDAO.findGradeTyeBybusId( busId );
	if ( CommonUtil.isEmpty( params.get( "ctId" ) ) ) {
	    if ( cardTypes.size() > 0 ) {
		params.put( "ctId", cardTypes.get( 0 ).get( "ctId" ) );
	    }
	}
	Page page = findMemberBybusId( busId, params );
	return page;
    }

    public Page findMemberBybusId( Integer busId, Map< String,Object > params ) {
	try {
	    params.put( "curPage", CommonUtil.isEmpty( params.get( "curPage" ) ) ? 1 : CommonUtil.toInteger( params.get( "curPage" ) ) );
	    int pageSize = 10;
	    Object search1 = params.get( "search" );
	    String search = null;
	    if ( CommonUtil.isNotEmpty( search1 ) ) {
		search = search1.toString();
	    }
	    Object ctIdObj = params.get( "ctId" );
	    Integer ctId = 0;
	    Byte source = null;
	    if ( CommonUtil.isNotEmpty( ctIdObj ) ) {
		ctId = Integer.parseInt( ctIdObj.toString() );
		if ( ctId == -1 ) {
		    ctId = ctId == -1 ? 0 : ctId;
		    source = 1;
		}
	    }
	    Byte changeCardType = null;
	    if ( CommonUtil.isNotEmpty( params.get( "changeCardType" ) ) ) {
		changeCardType = 1;
	    }
	    Object gtIdObj = params.get( "gtId" );
	    Integer gtId = 0;
	    if ( CommonUtil.isNotEmpty( gtIdObj ) ) {
		gtId = Integer.parseInt( gtIdObj.toString() );
	    }
	    String startDate = null;
	    if ( CommonUtil.isNotEmpty( params.get( "startDate" ) ) ) {
		startDate = CommonUtil.toString( params.get( "startDate" ) ) + " 00:00:00";
	    }
	    String endDate = null;
	    if ( CommonUtil.isNotEmpty( params.get( "endDate" ) ) ) {
		endDate = CommonUtil.toString( params.get( "endDate" ) ) + " 23:59:59";
	    }

	    Object phone1 = params.get( "phone" );
	    String phone = null;
	    if ( CommonUtil.isNotEmpty( phone1 ) ) {
		phone = phone1.toString();
	    }

	    int rowCount = memberMapper.countMember( busId, search, phone, ctId, gtId, source, changeCardType, startDate, endDate );
	    Page page = new Page( CommonUtil.toInteger( params.get( "curPage" ) ), pageSize, rowCount, "member/findMember.do" );
	    params.put( "firstResult", pageSize * ( ( page.getCurPage() <= 0 ? 1 : page.getCurPage() ) - 1 ) );
	    params.put( "maxResult", pageSize );

	    List< Map< String,Object > > list = null;
	    List< Map< String,Object > > members = null;
	    if ( CommonUtil.isEmpty( phone ) ) {
		list = memberMapper.findMemberBybusId( Integer.parseInt( params.get( "firstResult" ).toString() ), pageSize, busId, search, ctId, gtId, source, changeCardType,
				startDate, endDate );
		// 采用数据拼接方式
		List< Integer > mcIds = new ArrayList< Integer >();
		for ( Map< String,Object > map : list ) {
		    if ( CommonUtil.isNotEmpty( map.get( "mc_id" ) ) ) {
			mcIds.add( CommonUtil.toInteger( map.get( "mc_id" ) ) );
		    }
		}
		if ( mcIds.size() > 0 ) {
		    members = memberMapper.findMemberBymcIds( busId, mcIds, phone );
		}
	    } else {
		members = memberMapper.findMemberByPhone( busId, phone );
		if ( CommonUtil.isNotEmpty( members ) && members.size() > 0 ) {
		    list = memberMapper.findMemberBybusIdAndPhone( busId, CommonUtil.toInteger( members.get( 0 ).get( "mc_id" ) ) );
		}

	    }

	    List< Map< String,Object > > memberList = new ArrayList< Map< String,Object > >();
	    for ( Map< String,Object > map : list ) {
		for ( Map< String,Object > member : members ) {
		    if ( CommonUtil.isNotEmpty( map.get( "mc_id" ) ) && CommonUtil.isNotEmpty( member.get( "mc_id" ) ) && CommonUtil.toInteger( map.get( "mc_id" ) )
				    .equals( CommonUtil.toInteger( member.get( "mc_id" ) ) ) ) {
			map.put( "id", member.get( "id" ) );
			map.put( "fans_currency", member.get( "fans_currency" ) );
			map.put( "flow", member.get( "flow" ) );
			map.put( "integral", member.get( "integral" ) );
			map.put( "phone", member.get( "phone" ) );
			map.put( "nickname", member.get( "nickname" ) );
			map.put( "sex", member.get( "sex" ) );
			map.put( "totalMoney", member.get( "totalMoney" ) );
			map.put( "cardChecked", member.get( "cardChecked" ) );
			map.put( "remark", member.get( "remark" ) );
			map.put( "mc_id", member.get( "mc_id" ) );
			if ( member.containsKey( "nickname" ) ) {
			    try {
				byte[] bytes = (byte[]) map.get( "nickname" );
				map.put( "nickname", new String( bytes, "UTF-8" ) );
			    } catch ( Exception e ) {
				map.put( "nickname", null );
			    }
			    memberList.add( map );
			} else {
			    memberList.add( map );
			}
		    }
		}
	    }
	    page.setSubList( memberList );
	    return page;
	} catch ( Exception e ) {
	    e.printStackTrace();
	}
	return null;
    }

    public void cardBatchApplyChecked( Integer busId, String memberIds, Integer ischecked ) throws BusinessException {
	try {
	    if ( CommonUtil.isEmpty( busId ) ) {
		throw new BusinessException( ResponseMemberEnums.INVALID_SESSION );
	    }
	    String[] str = memberIds.split( "," );
	    StringBuffer ids = new StringBuffer();// 会员Id集合
	    for ( int i = 0; i < str.length; i++ ) {
		if ( i == 0 ) {
		    ids.append( str[i] );
		} else {
		    ids.append( "," + str[i] );
		}
	    }
	    List< MemberEntity > memberList = memberMapper.selectByPrimaryKeys( memberIds.toString() );

	    StringBuffer mcIds = new StringBuffer();
	    StringBuffer phoneSb = new StringBuffer();
	    for ( int i = 0; i < memberList.size(); i++ ) {
		if ( i == 0 ) {
		    mcIds.append( memberList.get( i ).getMcId() );
		    phoneSb.append( memberList.get( i ).getPhone() );
		} else {
		    mcIds.append( "," + memberList.get( i ).getMcId() );
		    phoneSb.append( "," + memberList.get( i ).getPhone() );
		}
	    }
	    List< MemberCard > cardList = memberCardDAO.selectByPrimaryKeys( mcIds.toString() );
	    for ( int b = 0; b < cardList.size(); b++ ) {
		MemberCard card = cardList.get( b );
		if ( card.getIsChecked() != 1 ) {
		    if ( ischecked == 1 ) {
			card.setIsChecked( 1 );
			memberCardDAO.updateById( card );
		    } else {
			memberCardDAO.deleteById( card.getMcId() );
			if ( CommonUtil.isNotEmpty( card.getMcId() ) ) {
			    memberMapper.updateMemberByMcId( card.getBusId(), card.getMcId() );
			}

		    }
		}
	    }
	    // 发送短信
	    String content = "";
	    if ( ischecked == 0 ) {
		content = "尊敬的用户您好：您的会员卡,商家未审核通过,暂时无法享受会员权益.";
	    } else {
		content = "尊敬的用户您好：您的会员卡,商家已审核通过,各种会员权益等您来体验！";
	    }
	    if ( CommonUtil.isNotEmpty( phoneSb.toString() ) ) {
		String url = PropertiesUtil.getWxmp_home() + SEND_SMS;
		RequestUtils< OldApiSms > requestUtils = new RequestUtils< OldApiSms >();

		OldApiSms oldApiSms = new OldApiSms();
		oldApiSms.setMobiles( phoneSb.toString() );
		oldApiSms.setContent( content );
		oldApiSms.setCompany( PropertiesUtil.getSms_name() );
		oldApiSms.setBusId( busId );
		oldApiSms.setModel( 3 );
		requestUtils.setReqdata( oldApiSms );
		try {
		    String smsStr = HttpClienUtils.reqPostUTF8( JSONObject.toJSONString( requestUtils ), url, String.class, PropertiesUtil.getWxmpsignKey() );
		} catch ( Exception e ) {
		    LOG.error( "短信发送失败", e );
		}
	    }
	} catch ( BusinessException e ) {
	    throw e;
	} catch ( Exception e ) {
	    e.printStackTrace();
	    LOG.error( "会员卡审核异常", e );
	    throw new BusinessException( ResponseEnums.ERROR );
	}
    }

    public void cardApplyCheckedByOne( Integer busId, Integer memberId, Integer ischecked ) throws BusinessException {
	try {
	    if ( CommonUtil.isEmpty( busId ) ) {
		throw new BusinessException( ResponseMemberEnums.INVALID_SESSION );
	    }

	    MemberEntity member = memberMapper.selectById( memberId );

	    MemberCard card = new MemberCard();
	    card.setMcId( member.getMcId() );

	    if ( ischecked == 1 ) {
		card.setIsChecked( 1 );
		memberCardDAO.updateById( card );
	    } else {
		memberCardDAO.deleteById( card.getMcId() );
		if ( CommonUtil.isNotEmpty( card.getMcId() ) ) {
		    memberMapper.updateMemberByMcId( card.getBusId(), card.getMcId() );
		}

	    }
	    // 发送短信
	    String content = "";
	    if ( ischecked == 0 ) {
		content = "尊敬的用户您好：您的会员卡,商家未审核通过,暂时无法享受会员权益.";
	    } else {
		content = "尊敬的用户您好：您的会员卡,商家已审核通过,各种会员权益等您来体验！";
	    }

	    String url = PropertiesUtil.getWxmp_home() + SEND_SMS;
	    RequestUtils< OldApiSms > requestUtils = new RequestUtils< OldApiSms >();

	    OldApiSms oldApiSms = new OldApiSms();
	    oldApiSms.setMobiles( member.getPhone() );
	    oldApiSms.setContent( content );
	    oldApiSms.setCompany( PropertiesUtil.getSms_name() );
	    oldApiSms.setBusId( busId );
	    oldApiSms.setModel( 3 );
	    requestUtils.setReqdata( oldApiSms );
	    try {
		String smsStr = HttpClienUtils.reqPostUTF8( JSONObject.toJSONString( requestUtils ), url, String.class, PropertiesUtil.getWxmpsignKey() );
	    } catch ( Exception e ) {
		LOG.error( "短信发送失败", e );
	    }

	} catch ( BusinessException e ) {
	    throw e;
	} catch ( Exception e ) {
	    e.printStackTrace();
	    LOG.error( "会员卡审核异常", e );
	    throw new BusinessException( ResponseEnums.ERROR );
	}
    }

    @Transactional
    public void addIntegralAndfenbi( Integer busId, String json ) throws BusinessException {
	try {
	    if ( CommonUtil.isEmpty( busId ) ) {
		throw new BusinessException( ResponseMemberEnums.INVALID_SESSION );
	    }
	    Map< String,Object > map = JSON.parseObject( json, Map.class );
	    String memberIds = CommonUtil.toString( map.get( "memberIds" ) );
	    Integer giveType = CommonUtil.toInteger( map.get( "giveType" ) );
	    Double number = CommonUtil.toDouble( map.get( "number" ) );

	    if ( number < 0 ) {
		throw new BusinessException( ResponseMemberEnums.CAN_NOT_MINUS );
	    }
	    Integer count = 0;
	    String[] str = memberIds.split( "," );
	    List< Integer > ids = new ArrayList<>();
	    for ( int i = 0; i < str.length; i++ ) {
		if ( CommonUtil.isNotEmpty( str[i] ) ) {
		    ids.add( CommonUtil.toInteger( str[i] ) );
		    count++;
		}
	    }
	    if ( giveType == 2 ) {
		//赠送积分
		memberMapper.updateMemberJifen( ids, number.intValue() );
		List< Map< String,Object > > list = memberMapper.findMemberByIds( busId, ids );
		for ( Map< String,Object > member : list ) {
		    Double integral = CommonUtil.toInteger( member.get( "integral" ) ) + number;
		    memberCommonService.saveCardRecordOrderCodeNew( CommonUtil.toInteger( member.get( "id" ) ), 2, number, "商家赠送", busId, integral, "", 1 );
		}
	    } else {
		//判断商家粉币是否充值
		BigDecimal fenBiCount = new BigDecimal( count * number );
		BusUserEntity busUserEntity = busUserDAO.selectById( busId );
		if ( busUserEntity.getFansCurrency().compareTo( fenBiCount ) == -1 ) {
		    throw new BusinessException( ResponseMemberEnums.MEMBER_LESS_FENBI );
		}

		for ( Integer id : ids ) {
		    //赠送粉币
		    memberCommonService.giveFansCurrency( id, number );
		}
		List< Map< String,Object > > list = memberMapper.findMemberByIds( busId, ids );
		for ( Map< String,Object > member : list ) {
		    Double fans_currency = CommonUtil.toInteger( member.get( "fans_currency" ) ) + number;
		    memberCommonService.saveCardRecordOrderCodeNew( CommonUtil.toInteger( member.get( "id" ) ), 3, number, "商家赠送", busId, fans_currency, "", 1 );
		}
	    }
	} catch ( BusinessException e ) {
	    throw e;
	} catch ( Exception e ) {
	    throw new BusinessException( ResponseEnums.ERROR );
	}
    }

    public void updateDis( Integer mcId, Integer cardStatus ) {
	MemberCard c = new MemberCard();
	c.setMcId( mcId );
	c.setCardStatus( cardStatus );
	memberCardDAO.updateById( c );
    }

    public Map< String,Object > findMemberDetails( Integer memberId ) {
	return memberMapper.findById( memberId ).get( 0 );
    }

    /**
     * 导入会员信息
     *
     * @throws Exception
     */
    @Transactional
    public List< ErrorWorkbook > upLoadMember( Integer busId, InputStream input ) throws BusinessException {

	try {
	    List< MemberCard > cards = new ArrayList< MemberCard >();
	    List< MemberEntity > members = new ArrayList< MemberEntity >();
	    boolean error = false;
	    List< String > cardNos = new ArrayList< String >();

	    Workbook workBook = WorkbookFactory.create( input );
	    Sheet sheet = workBook.getSheetAt( 0 );
	    boolean flag = false; // 用来判断校验数据
	    // 查询商户的卡片表
	    List< Map< String,Object > > list = memberGradetypeDAO.findBybusId1( busId );
	    List< Map< String,Object > > cardList = memberCardDAO.findCardAll( busId );
	    if ( cardList != null ) {
		for ( Map< String,Object > map : cardList ) {
		    if ( CommonUtil.isNotEmpty( map.get( "cardNo" ) ) ) {
			cardNos.add( map.get( "cardNo" ).toString() );
		    }
		}
	    }
	    List< ErrorWorkbook > wbs = new ArrayList< ErrorWorkbook >();
	    ErrorWorkbook wb = null;
	    Row row = null;
	    String phones = "";

	    int rowLength = sheet.getPhysicalNumberOfRows();
	    // 判断会员领卡数量
	    int count = memberCardDAO.countCardisBinding( busId );

	    BusUserEntity busUser = busUserDAO.selectById( busId );

	    WxShop wxshop = wxShopDAO.selectMainShopByBusId( busId );

	    SortedMap< String,Object > dictMap = dictService.getDict( "1093" );
	    int level = busUser.getLevel();
	    for ( String dict : dictMap.keySet() ) {
		if ( level == CommonUtil.toInteger( dict ) ) {
		    if ( count + rowLength > CommonUtil.toInteger( dictMap.get( dict ) ) ) {
			return wbs;
		    }
		    break;
		}
	    }

	    for ( int i = 1; i < rowLength; i++ ) {

		wb = new ErrorWorkbook();
		row = sheet.getRow( i );
		String cardNo = ExcelStyle.getStringCellValue( row.getCell( 0 ) );
		if ( cardNos.contains( cardNo ) ) {
		    wb.setCardNo( "卡号已存在" + cardNo ); // 设置内容
		    wb.setCardNoStyle( "1" );
		    flag = true;
		} else if ( CommonUtil.isEmpty( cardNo ) ) {
		    wb.setCardNo( "不能为空" ); // 设置内容
		    wb.setCardNoStyle( "1" );
		    flag = true;
		} else {
		    wb.setCardNo( cardNo ); // 设置内容
		    wb.setCardNoStyle( "0" );
		}
		cardNos.add( cardNo );

		String ct_name = ExcelStyle.getStringCellValue( row.getCell( 7 ) );
		if ( CommonUtil.isEmpty( ct_name ) || !CommonUtil.isInteger( ct_name ) ) {
		    wb.setMemberType( "会员卡类型请填写1~5数字:" + ct_name ); // 设置内容
		    wb.setMemberTypeStyle( "1" ); // 填充样式
		    flag = true;
		} else {
		    boolean bool = false;
		    for ( Map< String,Object > gradeType : list ) {
			if ( CommonUtil.isNotEmpty( gradeType.get( "ctId" ) ) ) {
			    if ( !gradeType.get( "ctId" ).toString().equals( ct_name ) ) {
				wb.setMemberType( ct_name + "没有此会员或该卡片未发布" ); // 设置内容
				wb.setMemberTypeStyle( "1" ); // 填充样式
				bool = true;
			    } else {
				wb.setMemberType( ct_name ); // 设置内容
				wb.setMemberTypeStyle( "0" ); // 填充样式
				bool = false;
				break;
			    }
			}
		    }
		    if ( bool ) {
			flag = true;
		    }
		}

		String gt_grade_name = ExcelStyle.getStringCellValue( row.getCell( 8 ) );
		Map< String,Object > gradeType = null;
		if ( CommonUtil.isEmpty( gt_grade_name ) || !CommonUtil.isInteger( gt_grade_name ) ) {
		    wb.setMemberGrade( "会员等级不存在：" + gt_grade_name ); // 设置内容
		    wb.setMemberGradeStyle( "1" ); // 填充样式
		    flag = true;
		} else {
		    if ( CommonUtil.isInteger( ct_name ) ) {
			List< Map< String,Object > > gradeTypeList = memberGradetypeDAO.findBybusIdAndCtId3( busId, Integer.parseInt( ct_name ) );
			if ( CommonUtil.isInteger( gt_grade_name ) ) {
			    if ( Integer.parseInt( gt_grade_name ) <= gradeTypeList.size() ) {
				gradeType = gradeTypeList.get( Integer.parseInt( gt_grade_name ) - 1 );
				wb.setMemberGrade( gt_grade_name ); // 设置内容
				wb.setMemberGradeStyle( "0" ); // 填充样式
			    } else {
				wb.setMemberGrade( "没有此会员等级：" + gt_grade_name ); // 设置内容
				wb.setMemberGradeStyle( "1" ); // 填充样式
				flag = true;
			    }
			}
		    } else {
			wb.setMemberGrade( "会员卡类型不对,未查找到想对应等级:" + gt_grade_name ); // 设置内容
			wb.setMemberGradeStyle( "1" ); // 填充样式
			flag = true;
		    }
		}

		String nickname = ExcelStyle.getStringCellValue( row.getCell( 1 ) );
		wb.setCname( nickname ); // 设置内容
		wb.setCnameStyle( "0" ); // 填充样式

		String sex = ExcelStyle.getStringCellValue( row.getCell( 2 ) );
		wb.setSex( sex ); // 设置内容
		wb.setSexStyle( "0" ); // 填充样式
		if ( CommonUtil.isNotEmpty( sex ) && CommonUtil.isInteger( sex ) ) {
		    if ( Integer.parseInt( sex ) > 2 ) {
			sex = "0";
		    }
		} else {
		    sex = "0";
		}

		String phone = ExcelStyle.getStringCellValue( row.getCell( 3 ) );
		if ( CommonUtil.isEmpty( phone ) || !CommonUtil.isMobileNO( phone.trim() ) ) {
		    wb.setPhone( "电话号码错误:" + phone ); // 设置内容
		    wb.setPhoneStyle( "1" ); // 填充样式
		    flag = true;
		} else if ( phones.contains( phone ) ) {
		    wb.setPhone( "电话号码重复了:" + phone ); // 设置内容
		    wb.setPhoneStyle( "1" ); // 填充样式
		    flag = true;
		} else {
		    phones += phone + ",";
		    wb.setPhone( phone ); // 设置内容
		    wb.setPhoneStyle( "0" ); // 填充样式
		}

		String receivedate = ExcelStyle.getStringCellValue( row.getCell( 4 ) );
		Date date = null;
		if ( CommonUtil.isEmpty( receivedate ) ) {
		    date = new Date();
		} else {
		    if ( CommonUtil.isValidDate( receivedate ) ) {
			date = DateTimeKit.parse( receivedate, "yyyy/MM/dd" );
			wb.setLingquDate( receivedate ); // 设置内容
			wb.setLingquDateStyle( "0" ); // 填充样式
		    } else {
			wb.setLingquDate( "日期不对:" + receivedate ); // 设置内容
			wb.setLingquDateStyle( "1" ); // 填充样式
			flag = true;
		    }
		}

		String money = ExcelStyle.getStringCellValue( row.getCell( 5 ) );
		if ( CommonUtil.isEmpty( money ) ) {
		    money = "0";
		} else {
		    if ( !CommonUtil.isDouble( money ) ) {
			wb.setBalance( "数据类型不对:" + money ); // 设置内容
			wb.setBalanceStyle( "1" ); // 填充样式
			flag = true;
		    } else {
			wb.setBalance( money ); // 设置内容
			wb.setBalanceStyle( "0" ); // 填充样式
		    }
		}

		String integral = ExcelStyle.getStringCellValue( row.getCell( 6 ) );
		if ( CommonUtil.isEmpty( integral ) ) {
		    integral = "0";
		} else {
		    if ( !CommonUtil.isInteger( integral ) ) {
			wb.setJifen( "积分格式不对：" + integral ); // 设置内容
			wb.setJifenStyle( "1" ); // 填充样式
			flag = true;
		    } else {
			wb.setJifen( integral ); // 设置内容
			wb.setJifenStyle( "0" ); // 填充样式
		    }
		}

		if ( !flag ) {
		    MemberCard card = new MemberCard();
		    card.setCardNo( cardNo );
		    card.setIsbinding( 0 );
		    card.setCtId( Integer.parseInt( ct_name ) );
		    card.setGtId( Integer.parseInt( gradeType.get( "gt_id" ).toString() ) );
		    card.setBusId( busId );
		    card.setReceiveDate( date );
		    card.setSource( 1 );

		    if ( Integer.parseInt( ct_name ) == 3 ) {
			card.setMoney( Double.parseDouble( money ) );
		    } else if ( Integer.parseInt( ct_name ) == 5 ) {
			card.setFrequency( Integer.parseInt( money ) );
		    }

		    card.setShopId( wxshop.getId() );

		    card.setOnline( 0 );

		    Integer grId = memberGiveruleDAO.findBybusIdAndGtId( busId, Integer.parseInt( gradeType.get( "gt_id" ).toString() ) );
		    card.setGrId( grId );
		    card.setApplyType( 0 );

		    cards.add( card );

		    MemberEntity member = new MemberEntity();
		    member.setBusId( busId );
		    member.setIntegral( Integer.parseInt( integral ) );
		    member.setPhone( phone );
		    member.setNickname( nickname );
		    member.setSex( Integer.parseInt( sex ) );
		    members.add( member );
		} else {
		    error = true;
		}
		wbs.add( wb );
	    }

	    if ( !error ) {
		MemberCard c = null;
		for ( int i = 0; i < cards.size(); i++ ) {
		    // 查询导入实体卡之前 用户是否领取了会员卡，领取会员卡合并
		    MemberEntity member = members.get( i );
		    String phone = member.getPhone();
		    MemberEntity m = memberMapper.findByPhone( busId, phone ); // 已经存在的粉丝信息
		    if ( CommonUtil.isNotEmpty( m ) ) {
			if ( CommonUtil.isNotEmpty( m.getMcId() ) ) {
			    // 已是会员
			    MemberCard card = memberCardDAO.selectById( m.getMcId() );
			    // 合并数据前 将之前的会员卡数据移到cardOld表
			    MemberCardOld old = JSON.toJavaObject( JSON.parseObject( JSON.toJSONString( card ) ), MemberCardOld.class );
			    memberCardOldDAO.insert( old );

			    c = cards.get( i );
			    card.setApplyType( 0 );
			    card.setCardNo( c.getCardNo() );
			    card.setIsbinding( 1 );
			    card.setCtId( c.getCtId() );
			    card.setGtId( c.getGtId() );
			    card.setBusId( busId );
			    card.setSource( 1 );

			    if ( c.getCtId() == 3 ) {
				card.setMoney( card.getMoney() + c.getMoney() );
			    } else if ( c.getCtId() == 5 ) {
				card.setFrequency( card.getFrequency() + c.getFrequency() );
			    }

			    card.setGrId( c.getGrId() );
			    memberCardDAO.updateById( card );

			    MemberEntity m1 = new MemberEntity();
			    m1.setLoginMode( 0 );
			    m1.setId( m.getId() );
			    m1.setIntegral( m.getIntegral() + member.getIntegral() );
			    memberMapper.updateById( m1 );
			} else {
			    // 非会员
			    c = cards.get( i );
			    c.setSource( 1 );
			    c.setIsbinding( 1 );
			    c.setApplyType( 0 );
			    memberCardDAO.insert( c );

			    MemberEntity m1 = new MemberEntity();
			    m1.setId( m.getId() );
			    m1.setMcId( cards.get( i ).getMcId() );
			    m1.setIntegral( m.getIntegral() + member.getIntegral() );
			    m1.setLoginMode( 0 );
			    memberMapper.updateById( m1 );
			}
		    } else {
			c = cards.get( i );
			c.setApplyType( 0 );
			memberCardDAO.insert( c );
			member.setMcId( c.getMcId() );
			member.setBusId( busId );
			member.setLoginMode( 2 );
			memberMapper.insert( member );
		    }
		}
		return null;
	    }
	    return wbs;
	} catch ( Exception e ) {
	    LOG.error( "导入数据,数据保存异常", e );
	    throw new BusinessException( ResponseEnums.ERROR );
	}
    }

    @Override
    public SXSSFWorkbook errorMember( List< ErrorWorkbook > wbs ) {
	SXSSFWorkbook wb = new SXSSFWorkbook();
	try {
	    // 创建工作簿 冻结第一行
	    Sheet errorSheet = wb.createSheet( "错误会员卡信息" );
	    errorSheet.createFreezePane( 0, 1, 0, 1 ); // 冻结行
	    // 设置表头
	    Row row1 = errorSheet.createRow( 0 ); // 创建行, 位于第0行
	    ExcelStyle.setFirstRow( errorSheet, wb, row1, 0, "卡号（必填）", 15 );
	    ExcelStyle.setFirstRow( errorSheet, wb, row1, 1, "姓名", 25 );
	    ExcelStyle.setFirstRow( errorSheet, wb, row1, 2, "性别(0、女1、男 2、未知)", 25 );
	    ExcelStyle.setFirstRow( errorSheet, wb, row1, 3, "手机号码（必填）", 20 );
	    ExcelStyle.setFirstRow( errorSheet, wb, row1, 4, "领卡时间(yyyy-MM-dd)", 15 );
	    ExcelStyle.setFirstRow( errorSheet, wb, row1, 5, "余额或次数", 15 );
	    ExcelStyle.setFirstRow( errorSheet, wb, row1, 6, "积分", 15 );
	    ExcelStyle.setFirstRow( errorSheet, wb, row1, 7, "会员类型(1、积分卡2、折扣卡 3、储值卡4、时效卡5、次卡)", 35 );
	    ExcelStyle.setFirstRow( errorSheet, wb, row1, 8, "会员等级（1-4等）", 35 );
	    // 主体样式
	    CellStyle cellStyle = ExcelStyle.otherRowFont( wb, (byte) 2 );
	    CellStyle errorCellStyle = ExcelStyle.footRowFont( wb ); // 错误数据样式
	    Row row = null;
	    int i = 1;
	    for ( ErrorWorkbook ewb : wbs ) {
		row = errorSheet.createRow( i++ );

		Cell cell = row.createCell( 0 );
		cell.setCellValue( ewb.getCardNo() ); // 设置内容
		if ( "1".equals( ewb.getCardNoStyle() ) ) {
		    cell.setCellStyle( errorCellStyle ); // 填充样式
		} else {
		    cell.setCellStyle( cellStyle ); // 填充样式
		}

		cell = row.createCell( 1 );
		cell.setCellValue( ewb.getCname() ); // 设置内容
		if ( "1".equals( ewb.getCnameStyle() ) ) {
		    cell.setCellStyle( errorCellStyle ); // 填充样式
		} else {
		    cell.setCellStyle( cellStyle ); // 填充样式
		}

		cell = row.createCell( 2 );
		cell.setCellValue( ewb.getSex() ); // 设置内容
		if ( "1".equals( ewb.getSexStyle() ) ) {
		    cell.setCellStyle( errorCellStyle ); // 填充样式
		} else {
		    cell.setCellStyle( cellStyle ); // 填充样式
		}

		cell = row.createCell( 3 );
		cell.setCellValue( ewb.getPhone() ); // 设置内容
		if ( "1".equals( ewb.getPhoneStyle() ) ) {
		    cell.setCellStyle( errorCellStyle ); // 填充样式
		} else {
		    cell.setCellStyle( cellStyle ); // 填充样式
		}

		cell = row.createCell( 4 );
		cell.setCellValue( ewb.getLingquDate() ); // 设置内容
		if ( "1".equals( ewb.getLingquDateStyle() ) ) {
		    cell.setCellStyle( errorCellStyle ); // 填充样式
		} else {
		    cell.setCellStyle( cellStyle ); // 填充样式
		}

		cell = row.createCell( 5 );
		cell.setCellValue( ewb.getBalance() ); // 设置内容
		if ( "1".equals( ewb.getBalanceStyle() ) ) {
		    cell.setCellStyle( errorCellStyle ); // 填充样式
		} else {
		    cell.setCellStyle( cellStyle ); // 填充样式
		}

		cell = row.createCell( 6 );
		cell.setCellValue( ewb.getJifen() ); // 设置内容
		if ( "1".equals( ewb.getJifenStyle() ) ) {
		    cell.setCellStyle( errorCellStyle ); // 填充样式
		} else {
		    cell.setCellStyle( cellStyle ); // 填充样式
		}

		cell = row.createCell( 7 );
		cell.setCellValue( ewb.getMemberType() ); // 设置内容
		if ( "1".equals( ewb.getMemberTypeStyle() ) ) {
		    cell.setCellStyle( errorCellStyle ); // 填充样式
		} else {
		    cell.setCellStyle( cellStyle ); // 填充样式
		}

		cell = row.createCell( 8 );
		cell.setCellValue( ewb.getMemberGrade() ); // 设置内容
		if ( "1".equals( ewb.getMemberGradeStyle() ) ) {
		    cell.setCellStyle( errorCellStyle ); // 填充样式
		} else {
		    cell.setCellStyle( cellStyle ); // 填充样式
		}
	    }
	} catch ( Exception e ) {
	    LOG.error( "导出excel异常", e );
	    e.printStackTrace();
	}
	return wb;

    }

    @Override
    public List< Map< String,Object > > findMember( Integer busId, String json ) {

	JSONObject jsonObject = JSONObject.parseObject( json );
	Integer ctId = CommonUtil.toInteger( jsonObject.get( "ctId" ) );
	Integer gtId = CommonUtil.toInteger( jsonObject.get( "gtId" ) );
	List< Map< String,Object > > list = memberMapper.findMember( busId, "", ctId, gtId );
	List< Map< String,Object > > memberList = new ArrayList< Map< String,Object > >();
	for ( Map< String,Object > map : list ) {
	    if ( map.containsKey( "nickname" ) ) {
		try {
		    byte[] bytes = (byte[]) map.get( "nickname" );
		    map.put( "nickname", new String( bytes, "UTF-8" ) );
		} catch ( Exception e ) {
		    map.put( "nickname", null );
		}
		memberList.add( map );
	    } else {
		memberList.add( map );
	    }
	}
	return list;
    }

    public Map< String,Object > findMemberCardByCardNo( Integer busId, String cardNo ) throws BusinessException {
	Map< String,Object > map = new HashMap< String,Object >();
	String cardNodecrypt = "";
	try {
	    // 如果手动输入 会出现异常
	    cardNodecrypt = EncryptUtil.decrypt( PropertiesUtil.getCardNoKey(), cardNo );
	} catch ( Exception e ) {
	    // 如果不是扫码 判断商家是否允许不扫码
	    SortedMap< String,Object > maps = dictService.getDict( "A001" );
	    Object obj = maps.get( busId.toString() );
	    if ( CommonUtil.isEmpty( obj ) ) {
		throw new BusinessException( ResponseMemberEnums.PLEASE_SCAN_CODE );
	    }
	}

	if ( cardNodecrypt.contains( "?time" ) ) {
	    // 查询卡号是否存在
	    Long time = Long.parseLong( cardNodecrypt.substring( cardNodecrypt.indexOf( "?time=" ) + 6 ) );

	    cardNo = cardNodecrypt.substring( 0, cardNodecrypt.indexOf( "?time" ) );

	    MemberCard card1 = memberCardDAO.findCardByCardNo( busId, cardNo );
	    if ( card1.getCtId() == 3 ) {
		// 2分钟后为超时
		if ( DateTimeKit.secondBetween( new Date( time ), new Date() ) > 120 ) {
		    // 二维码已超时
		    map.put( "result", false );
		    map.put( "message", "二维码已超时!" );
		    return map;
		}
	    }
	}

	MemberCard card = null;
	try {
	    // 查询卡号是否存在
	    if ( CommonUtil.isEmpty( card ) ) {
		card = memberCardDAO.findCardByCardNo( busId, cardNo );
	    }

	    if ( CommonUtil.isEmpty( card ) ) {
		MemberEntity member = memberMapper.findByPhone( busId, cardNo );
		if ( CommonUtil.isNotEmpty( member ) ) {
		    card = memberCardDAO.selectById( member.getMcId() );
		}
	    }
	    if ( CommonUtil.isEmpty( card ) ) {
		throw new BusinessException( ResponseMemberEnums.MEMBER_NOT_CARD );
	    } else if ( card.getCardStatus() == 1 ) {
		throw new BusinessException( ResponseMemberEnums.DISABLE_MEMBER_CARD );
	    } else {
		List< Map< String,Object > > cards = memberCardDAO.findCardById( card.getMcId() );
		MemberGiverule giveRule = memberGiveruleDAO.selectById( card.getGrId() );
		MemberEntity member = memberMapper.findByMcIdAndbusId( busId, card.getMcId() );
		map.put( "nickName", member.getNickname() );
		map.put( "phone", member.getPhone() );
		map.put( "ctName", cards.get( 0 ).get( "ct_name" ) );
		map.put( "gradeName", cards.get( 0 ).get( "gt_grade_name" ) );
		map.put( "cardNo", card.getCardNo() );
		map.put( "ctId", card.getCtId() );
		map.put( "discount", giveRule.getGrDiscount() );
		map.put( "money", card.getMoney() );
		map.put( "frequency", card.getFrequency() );
		map.put( "integral", member.getIntegral() );
	    }
	    return map;
	} catch ( BusinessException e ) {
	    throw e;
	} catch ( Exception e ) {
	    throw new BusinessException( ResponseEnums.ERROR );
	}
    }

    @Transactional
    public void intergralConsume( Integer busId, Integer intergral, String cardNo ) throws BusinessException {

	try {
	    MemberCard card = memberCardDAO.findCardByCardNo( busId, cardNo );
	    if ( CommonUtil.isEmpty( card ) ) {
		throw new BusinessException( ResponseMemberEnums.NOT_MEMBER_CAR );
	    }
	    MemberEntity member = memberMapper.findByMcIdAndbusId( busId, card.getMcId() );
	    Integer jifen = member.getIntegral();
	    if ( intergral > jifen ) {
		throw new BusinessException( ResponseMemberEnums.MEMBER_LESS_JIFEN );
	    }
	    MemberEntity mem = new MemberEntity();
	    mem.setId( member.getId() );
	    Integer shenyuJifen = member.getIntegral() - intergral;
	    mem.setIntegral( shenyuJifen );
	    memberMapper.updateById( mem );
	    // 添加会员记录
	    UserConsumeNew uc = new UserConsumeNew();
	    uc.setBusId( busId );
	    uc.setMemberId( member.getId() );
	    uc.setMcId( card.getMcId() );
	    uc.setCtId( card.getCtId() );
	    uc.setGtId( card.getGtId() );
	    uc.setRecordType( 0 );
	    uc.setCreateDate( new Date() );
	    uc.setUcType( 5 );
	    uc.setIntegral( intergral );
	    uc.setFenbi( 0.0 );
	    uc.setUccount( 0 );
	    uc.setDiscountMoney( 0.0 );
	    String orderCode = CommonUtil.getMEOrderCode();
	    uc.setOrderCode( orderCode );
	    uc.setDataSource( 0 );
	    uc.setIsendDate( new Date() );
	    uc.setIsend( 1 );
	    uc.setBalance( shenyuJifen.doubleValue() );
	    uc.setPayStatus( 1 );
	    WxShop shop = wxShopDAO.selectMainShopByBusId( busId );
	    if ( CommonUtil.isNotEmpty( shop ) ) {
		uc.setShopId( shop.getId() );
	    }
	    userConsumeNewDAO.insert( uc );

	    UserConsumePay ucPay = new UserConsumePay();
	    ucPay.setUcId( uc.getId() );
	    ucPay.setPaymentType( 11 );
	    ucPay.setPayMoney( 0.0 );
	    userConsumePayDAO.insert( ucPay );

	    memberCommonService.saveCardRecordOrderCodeNew( member.getId(), 2, intergral.doubleValue(), "积分兑换", busId, shenyuJifen.doubleValue(), orderCode, 0 );

	} catch ( BusinessException e ) {
	    throw e;
	} catch ( Exception e ) {
	    throw new BusinessException( ResponseEnums.ERROR );
	}
    }

    /**
     * 会员卡统计
     *
     * @param busId
     * @param ctId
     *
     * @return
     */
    public Map< String,Object > memberTongJi( Integer busId, Integer ctId, String startTime ) throws BusinessException {
	try {
	    Map< String,Object > map = new HashMap<>();

	    List< Map< String,Object > > grades = memberGradetypeDAO.findGradeTyeBybusId( busId );
	    if ( CommonUtil.isEmpty( grades ) || grades.size() == 0 ) {
		return null;
	    }
	    map.put( "cardType", grades );

	    if ( CommonUtil.isEmpty( ctId ) || ctId == 0 ) {
		ctId = CommonUtil.toInteger( grades.get( 0 ).get( "ctId" ) );
	    }
	    map.put( "ctId", ctId );

	    // 会员总数
	    Integer countMember = memberCardDAO.countCard1( busId, ctId );
	    map.put( "countMember", countMember );

	    // 查询推荐总数
	    Integer tuijianNum = memberRecommendDAO.countTuiJian( busId, ctId );
	    map.put( "tuijianNum", tuijianNum );

	    // 订单统计
	    Integer orderCount = userConsumeNewDAO.countOrder( busId, ctId );
	    map.put( "orderCount", orderCount );

	    // 性别分组统计
	    List< Map< String,Object > > sexMap = memberMapper.findSexGroupBySex( busId, ctId );
	    map.put( "sexMap", JSON.toJSONString( sexMap ) );

	    // 等级分组统计
	    List< Map< String,Object > > cardCount = memberCardDAO.findGroupBygtId( busId, ctId );
	    map.put( "cardCount", JSON.toJSONString( cardCount ) );

	    // 查询消费总和 和售卡总和
	    Double sumXiaofei = userConsumeNewDAO.sumXiaofei( busId, ctId );
	    Double buyCard = userConsumeNewDAO.buyCard( busId, ctId );
	    map.put( "sumXiaofei", sumXiaofei );
	    map.put( "buyCard", buyCard );
	    // 表格 7销售总额和售卡总额
	    Date date = new Date();
	    if ( CommonUtil.isNotEmpty( startTime ) ) {
		date = DateTimeKit.parse( startTime, "yyyy/MM/dd" );
	    }
	    List< Map< String,Object > > sumdayOrder = userConsumeNewDAO.sum7DayOrder( busId, ctId, date );

	    List< Map< String,Object > > sumbuyCard = userConsumeNewDAO.sum7DayBuyCard( busId, ctId, date );
	    List< Map< String,Object > > sumMemberOrder = new ArrayList< Map< String,Object > >();
	    for ( Map< String,Object > map1 : sumdayOrder ) {
		boolean flag = false;
		for ( Map< String,Object > sumbuy : sumbuyCard ) {
		    if ( CommonUtil.toString( sumbuy.get( "buytime1" ) ).equals( CommonUtil.toString( map1.get( "time" ) ) ) ) {
			map1.put( "buyDiscountAfterMoney", CommonUtil.toString( map.get( "buyDiscountAfterMoney" ) ) );
			sumMemberOrder.add( map1 );
			flag = true;
		    }
		}
		if ( !flag ) {
		    map1.put( "buyDiscountAfterMoney", 0 );
		    sumMemberOrder.add( map1 );
		}
	    }
	    map.put( "sumMemberOrder", sumMemberOrder );
	    switch ( ctId ) {
		case 1:
		    // 积分卡
		    // 查询积分兑换情况
		    List< Map< String,Object > > jifenMap = userConsumeNewDAO.countJifenDuiHan( busId );
		    Collections.reverse( jifenMap );
		    map.put( "jifenMap", JSONArray.toJSONString( jifenMap ) );

		    // 剩余积分统计
		    Integer shuyunJifen = memberMapper.countJifen( busId );
		    map.put( "shuyunJifen", shuyunJifen );

		    // 消耗积分统计
		    Integer xiaohaoJifen = userConsumeNewDAO.countUseJifen( busId );
		    map.put( "xiaohaoJifen", xiaohaoJifen );
		    break;
		case 2: // 折扣卡
		    // 会员折扣总和
		    List< Map< String,Object > > discountOrder = userConsumeNewDAO.sum7DayDiscount( busId );
		    Collections.reverse( discountOrder );
		    map.put( "discountOrder", JSON.toJSONString( discountOrder ) );

		    // 折扣总额
		    Double disCountMoney = userConsumeNewDAO.sumDisCount( busId );
		    map.put( "disCountMoney", disCountMoney );

		    break;
		case 3:
		    // 储值卡
		    // 查询会员卡中剩余总额
		    Double yueMoney = memberCardDAO.sumMoney( busId );
		    map.put( "yueMoney", yueMoney );

		    // 会员累计充值总额
		    Double chongzhiMoney = userConsumeNewDAO.sumChongzhi( busId );
		    map.put( "chongzhiMoney", chongzhiMoney );
		    // 会员充值走势
		    List< Map< String,Object > > chongzhiMap = userConsumeNewDAO.sumChongzhi7Day( busId );
		    Collections.reverse( chongzhiMap );
		    map.put( "chongzhiMap", JSON.toJSONString( chongzhiMap ) );
		    break;
		case 4:
		    // 时效卡

		    break;
		case 5:
		    // 次卡
		    // 查询次卡7天消费次数
		    List< Map< String,Object > > cikaMap = userConsumeNewDAO.sumCiKa( busId );
		    Collections.reverse( cikaMap );
		    map.put( "cikaMap", JSON.toJSONString( cikaMap ) );

		    // 会员剩余次数
		    Integer sumfrequency = memberCardDAO.sumfrequency( busId );
		    map.put( "sumfrequency", sumfrequency );
		    // 会员消费次数
		    Integer userCiKa = userConsumeNewDAO.userCiKa( busId );
		    map.put( "userCiKa", userCiKa );
		    break;

		default:
		    break;
	    }
	    return map;
	} catch ( Exception e ) {
	    throw new BusinessException( ResponseEnums.ERROR );
	}
    }

    public Page findChongZhiLog( Integer busId, Map< String,Object > params ) throws BusinessException {
	try {
	    params.put( "curPage", CommonUtil.isEmpty( params.get( "curPage" ) ) ? 1 : CommonUtil.toInteger( params.get( "curPage" ) ) );
	    int pageSize = 10;

	    Integer memberId = null;
	    if ( CommonUtil.isNotEmpty( params.get( "cardNo" ) ) ) {
		MemberEntity memberEntity = memberMapper.findByPhone( busId, CommonUtil.toString( params.get( "cardNo" ) ) );
		if ( CommonUtil.isEmpty( memberEntity ) ) {
		    MemberCard memberCard = memberCardDAO.findCardByCardNo( busId, CommonUtil.toString( params.get( "cardNo" ) ) );
		    memberEntity = memberMapper.findByMcIdAndbusId( busId, memberCard.getMcId() );
		    if ( CommonUtil.isNotEmpty( memberEntity ) ) {
			memberId = memberEntity.getId();
		    } else {
			memberId = 0;
		    }
		} else {
		    memberId = memberEntity.getId();
		}
	    }

	    String startDate = null;
	    if ( CommonUtil.isNotEmpty( params.get( "startTime" ) ) ) {
		startDate = CommonUtil.toString( params.get( "startTime" ) ) + " 00:00:00";
	    }

	    int rowCount = userConsumeNewDAO.countUserConsumeChongZhiByMemberId( busId, memberId, startDate );

	    Page page = new Page( CommonUtil.toInteger( params.get( "curPage" ) ), pageSize, rowCount, "" );
	    params.put( "firstResult", pageSize * ( ( page.getCurPage() <= 0 ? 1 : page.getCurPage() ) - 1 ) );
	    params.put( "maxResult", pageSize );

	    List< Map< String,Object > > list = userConsumeNewDAO
			    .findUserConsumeChongZhiByMemberId( busId, memberId, startDate, CommonUtil.toInteger( params.get( "firstResult" ) ),
					    CommonUtil.toInteger( params.get( "maxResult" ) ) );
	    page.setSubList( list );
	    return page;
	} catch ( Exception e ) {
	    LOG.error( "分页查询充值记录异常", e );
	    throw new BusinessException( ResponseEnums.ERROR );
	}
    }

    public Map< String,Object > findChongZhiLogDetails( Integer ucId ) throws BusinessException {
	try {
	    Map< String,Object > map = new HashMap<>();
	    UserConsumeNew ucNew = userConsumeNewDAO.selectById( ucId );
	    MemberEntity memberEntity = memberMapper.selectById( ucNew.getMemberId() );
	    if ( CommonUtil.isEmpty( memberEntity ) ) {
		memberEntity = memberMapper.findMemberByOldId( ucNew.getBusId(), ucNew.getMemberId() );
	    }
	    map.put( "nickName", memberEntity.getNickname() );
	    map.put( "headImg", memberEntity.getHeadimgurl() );
	    List< Map< String,Object > > cards = memberCardDAO.findCardById( memberEntity.getMcId() );
	    map.put( "ctName", cards.get( 0 ).get( "ct_name" ) );
	    map.put( "gradeName", cards.get( 0 ).get( "gt_grade_name" ) );
	    map.put( "cardNo", cards.get( 0 ).get( "cardNo" ) );

	    /**
	     * 订单
	     */
	    map.put( "orderCode", ucNew.getOrderCode() );
	    map.put( "dateTime", DateTimeKit.getDateTime( ucNew.getIsendDate() ) );
	    map.put( "money", ucNew.getDiscountAfterMoney() );
	    map.put( "payStatus", ucNew.getPayStatus() );

	    List< UserConsumePay > userConsumePays = userConsumePayDAO.findByUcId( ucId );
	    SortedMap< String,Object > sortemap = dictService.getDict( "1198" );
	    String payType = "";
	    for ( UserConsumePay userConsumePay : userConsumePays ) {
		payType = CommonUtil.toString( sortemap.get( userConsumePay.getPaymentType() ) ) + "   ";
	    }
	    map.put( "payType", payType );
	    return map;
	} catch ( Exception e ) {
	    LOG.error( "查询订单详情异常", e );
	    throw new BusinessException( ResponseEnums.ERROR );
	}

    }

    public Page findDuiHuanLog( Integer busId, Map< String,Object > params ) throws BusinessException {
	try {
	    params.put( "curPage", CommonUtil.isEmpty( params.get( "curPage" ) ) ? 1 : CommonUtil.toInteger( params.get( "curPage" ) ) );
	    int pageSize = 10;

	    Integer memberId = null;
	    if ( CommonUtil.isNotEmpty( params.get( "cardNo" ) ) ) {
		MemberEntity memberEntity = memberMapper.findByPhone( busId, CommonUtil.toString( params.get( "cardNo" ) ) );
		if ( CommonUtil.isEmpty( memberEntity ) ) {
		    MemberCard memberCard = memberCardDAO.findCardByCardNo( busId, CommonUtil.toString( params.get( "cardNo" ) ) );
		    memberEntity = memberMapper.findByMcIdAndbusId( busId, memberCard.getMcId() );
		    if ( CommonUtil.isNotEmpty( memberEntity ) ) {
			memberId = memberEntity.getId();
		    } else {
			memberId = 0;
		    }
		} else {
		    memberId = memberEntity.getId();
		}
	    }

	    String startDate = null;
	    if ( CommonUtil.isNotEmpty( params.get( "startTime" ) ) ) {
		startDate = CommonUtil.toString( params.get( "startTime" ) ) + " 00:00:00";
	    }

	    int rowCount = userConsumeNewDAO.countUserConsumeDuiHuanByMemberId( busId, memberId, startDate );

	    Page page = new Page( CommonUtil.toInteger( params.get( "curPage" ) ), pageSize, rowCount, "" );
	    params.put( "firstResult", pageSize * ( ( page.getCurPage() <= 0 ? 1 : page.getCurPage() ) - 1 ) );
	    params.put( "maxResult", pageSize );

	    List< Map< String,Object > > list = userConsumeNewDAO.findUserConsumeDuiHuanByMemberId( busId, memberId, startDate, CommonUtil.toInteger( params.get( "firstResult" ) ),
			    CommonUtil.toInteger( params.get( "maxResult" ) ) );
	    page.setSubList( list );
	    return page;
	} catch ( Exception e ) {
	    LOG.error( "分页查询积分兑换记录异常", e );
	    throw new BusinessException( ResponseEnums.ERROR );
	}
    }

    public Map< String,Object > findDuiHuanLogDetails( Integer ucId ) throws BusinessException {
	try {
	    Map< String,Object > map = new HashMap<>();
	    UserConsumeNew ucNew = userConsumeNewDAO.selectById( ucId );
	    MemberEntity memberEntity = memberMapper.selectById( ucNew.getMemberId() );
	    if ( CommonUtil.isEmpty( memberEntity ) ) {
		memberEntity = memberMapper.findMemberByOldId( ucNew.getBusId(), ucNew.getMemberId() );
	    }
	    map.put( "nickName", memberEntity.getNickname() );
	    map.put( "headImg", memberEntity.getHeadimgurl() );
	    List< Map< String,Object > > cards = memberCardDAO.findCardById( memberEntity.getMcId() );
	    map.put( "ctName", cards.get( 0 ).get( "ct_name" ) );
	    map.put( "gradeName", cards.get( 0 ).get( "gt_grade_name" ) );
	    map.put( "cardNo", cards.get( 0 ).get( "cardNo" ) );

	    /**
	     * 订单
	     */
	    map.put( "orderCode", ucNew.getOrderCode() );
	    map.put( "dateTime", DateTimeKit.getDateTime( ucNew.getIsendDate() ) );
	    map.put( "integral", ucNew.getIntegral() );

	    List< UserConsumePay > userConsumePays = userConsumePayDAO.findByUcId( ucId );
	    SortedMap< String,Object > sortemap = dictService.getDict( "1198" );
	    String payType = "";
	    for ( UserConsumePay userConsumePay : userConsumePays ) {
		payType = CommonUtil.toString( sortemap.get( userConsumePay.getPaymentType() ) ) + "   ";
	    }
	    map.put( "payType", payType );
	    return map;
	} catch ( Exception e ) {
	    LOG.error( "查询订单详情异常", e );
	    throw new BusinessException( ResponseEnums.ERROR );
	}

    }

    public Page findCikaLog( Integer busId, Map< String,Object > params ) throws BusinessException {
	try {
	    params.put( "curPage", CommonUtil.isEmpty( params.get( "curPage" ) ) ? 1 : CommonUtil.toInteger( params.get( "curPage" ) ) );
	    int pageSize = 10;

	    Integer memberId = null;
	    if ( CommonUtil.isNotEmpty( params.get( "cardNo" ) ) ) {
		MemberEntity memberEntity = memberMapper.findByPhone( busId, CommonUtil.toString( params.get( "cardNo" ) ) );
		if ( CommonUtil.isEmpty( memberEntity ) ) {
		    MemberCard memberCard = memberCardDAO.findCardByCardNo( busId, CommonUtil.toString( params.get( "cardNo" ) ) );
		    memberEntity = memberMapper.findByMcIdAndbusId( busId, memberCard.getMcId() );
		    if ( CommonUtil.isNotEmpty( memberEntity ) ) {
			memberId = memberEntity.getId();
		    } else {
			memberId = 0;
		    }
		} else {
		    memberId = memberEntity.getId();
		}
	    }

	    String startDate = null;
	    if ( CommonUtil.isNotEmpty( params.get( "startTime" ) ) ) {
		startDate = CommonUtil.toString( params.get( "startTime" ) ) + " 00:00:00";
	    }

	    int rowCount = userConsumeNewDAO.countUserConsumeCikaByMemberId( busId, memberId, startDate );

	    Page page = new Page( CommonUtil.toInteger( params.get( "curPage" ) ), pageSize, rowCount, "" );
	    params.put( "firstResult", pageSize * ( ( page.getCurPage() <= 0 ? 1 : page.getCurPage() ) - 1 ) );
	    params.put( "maxResult", pageSize );

	    List< Map< String,Object > > list = userConsumeNewDAO.findUserConsumeCikaByMemberId( busId, memberId, startDate, CommonUtil.toInteger( params.get( "firstResult" ) ),
			    CommonUtil.toInteger( params.get( "maxResult" ) ) );
	    page.setSubList( list );
	    return page;
	} catch ( Exception e ) {
	    LOG.error( "分页查询次卡记录异常", e );
	    throw new BusinessException( ResponseEnums.ERROR );
	}
    }

    /**
     * 次卡消费次数详情
     *
     * @param ucId
     *
     * @return
     */
    public Map< String,Object > findCikaLogDetails( Integer ucId ) throws BusinessException {
	try {
	    Map< String,Object > map = new HashMap<>();
	    UserConsumeNew ucNew = userConsumeNewDAO.selectById( ucId );
	    MemberEntity memberEntity = memberMapper.selectById( ucNew.getMemberId() );
	    if ( CommonUtil.isEmpty( memberEntity ) ) {
		memberEntity = memberMapper.findMemberByOldId( ucNew.getBusId(), ucNew.getMemberId() );
	    }
	    map.put( "nickName", memberEntity.getNickname() );
	    map.put( "headImg", memberEntity.getHeadimgurl() );
	    List< Map< String,Object > > cards = memberCardDAO.findCardById( memberEntity.getMcId() );
	    map.put( "ctName", cards.get( 0 ).get( "ct_name" ) );
	    map.put( "gradeName", cards.get( 0 ).get( "gt_grade_name" ) );
	    map.put( "cardNo", cards.get( 0 ).get( "cardNo" ) );

	    /**
	     * 订单
	     */
	    map.put( "orderCode", ucNew.getOrderCode() );
	    map.put( "dateTime", DateTimeKit.getDateTime( ucNew.getIsendDate() ) );
	    map.put( "uccount", ucNew.getUccount() );

	    return map;
	} catch ( Exception e ) {
	    LOG.error( "查询订单详情异常", e );
	    throw new BusinessException( ResponseEnums.ERROR );
	}
    }

    public Page findXiaoFeiLog( Integer busId, Map< String,Object > params ) throws BusinessException {
	try {
	    params.put( "curPage", CommonUtil.isEmpty( params.get( "curPage" ) ) ? 1 : CommonUtil.toInteger( params.get( "curPage" ) ) );
	    int pageSize = 10;

	    Integer memberId = null;
	    if ( CommonUtil.isNotEmpty( params.get( "cardNo" ) ) ) {
		MemberEntity memberEntity = memberMapper.findByPhone( busId, CommonUtil.toString( params.get( "cardNo" ) ) );
		if ( CommonUtil.isEmpty( memberEntity ) ) {
		    MemberCard memberCard = memberCardDAO.findCardByCardNo( busId, CommonUtil.toString( params.get( "cardNo" ) ) );
		    memberEntity = memberMapper.findByMcIdAndbusId( busId, memberCard.getMcId() );
		    if ( CommonUtil.isNotEmpty( memberEntity ) ) {
			memberId = memberEntity.getId();
		    } else {
			memberId = 0;
		    }
		} else {
		    memberId = memberEntity.getId();
		}
	    }

	    String startDate = null;
	    if ( CommonUtil.isNotEmpty( params.get( "startTime" ) ) ) {
		startDate = CommonUtil.toString( params.get( "startTime" ) ) + " 00:00:00";
	    }

	    Integer payStatus = null;
	    if ( CommonUtil.isNotEmpty( params.get( "payStatus" ) ) ) {
		payStatus = CommonUtil.toInteger( params.get( "payStatus" ) );
	    }

	    int rowCount = userConsumeNewDAO.countUserConsumeXiaoFeiByMemberId( busId, memberId, startDate, payStatus );

	    Page page = new Page( CommonUtil.toInteger( params.get( "curPage" ) ), pageSize, rowCount, "" );
	    params.put( "firstResult", pageSize * ( ( page.getCurPage() <= 0 ? 1 : page.getCurPage() ) - 1 ) );
	    params.put( "maxResult", pageSize );

	    List< Map< String,Object > > list = userConsumeNewDAO
			    .findUserConsumeXiaoFeiByMemberId( busId, memberId, startDate, payStatus, CommonUtil.toInteger( params.get( "firstResult" ) ),
					    CommonUtil.toInteger( params.get( "maxResult" ) ) );

	    SortedMap< String,Object > sortemap = dictService.getDict( "1197" );
	    List< Map< String,Object > > userConsumes = new ArrayList<>();
	    for ( Map< String,Object > map : list ) {
		Object obj = sortemap.get( CommonUtil.toString( map.get( "ucType" ) ) );
		if ( CommonUtil.isNotEmpty( obj ) ) {
		    map.put( "ucTypeName", obj );
		} else {
		    map.put( "ucTypeName", "未知" );
		}
		userConsumes.add( map );
	    }
	    page.setSubList( userConsumes );
	    return page;
	} catch ( Exception e ) {
	    LOG.error( "分页查询消费记录异常", e );
	    throw new BusinessException( ResponseEnums.ERROR );
	}
    }

    public Map< String,Object > findXiaoFeiLogDetails( Integer ucId ) throws BusinessException {
	try {
	    Map< String,Object > map = new HashMap<>();
	    UserConsumeNew ucNew = userConsumeNewDAO.selectById( ucId );
	    MemberEntity memberEntity = memberMapper.selectById( ucNew.getMemberId() );
	    if ( CommonUtil.isEmpty( memberEntity ) ) {
		memberEntity = memberMapper.findMemberByOldId( ucNew.getBusId(), ucNew.getMemberId() );
	    }
	    map.put( "nickName", memberEntity.getNickname() );
	    map.put( "headImg", memberEntity.getHeadimgurl() );
	    List< Map< String,Object > > cards = memberCardDAO.findCardById( memberEntity.getMcId() );
	    map.put( "ctName", cards.get( 0 ).get( "ct_name" ) );
	    map.put( "gradeName", cards.get( 0 ).get( "gt_grade_name" ) );
	    map.put( "cardNo", cards.get( 0 ).get( "cardNo" ) );

	    /**
	     * 订单
	     */
	    map.put( "orderCode", ucNew.getOrderCode() );
	    map.put( "isend", ucNew.getIsend() );
	    map.put( "dateTime", DateTimeKit.getDateTime( ucNew.getIsendDate() ) );
	    map.put( "totalMoney", ucNew.getTotalMoney() );
	    map.put( "discountMoney", ucNew.getDiscountMoney() );
	    map.put( "discountAfterMoney", ucNew.getDiscountAfterMoney() );
	    map.put( "integral", ucNew.getIntegral() );
	    map.put( "fenbi", ucNew.getFenbi() );
	    map.put( "disCountdepict", ucNew.getDisCountdepict() );
	    map.put( "balance", ucNew.getBalance() );

	    //退款
	    map.put( "refundMoney", ucNew.getRefundMoney() );
	    map.put( "refundFenbi", ucNew.getRefundFenbi() );
	    map.put( "refundJifen", ucNew.getRefundJifen() );
	    map.put( "refundDate", DateTimeKit.getDateTime( ucNew.getRefundDate() ) );

	    map.put( "payStatus", ucNew.getPayStatus() );
	    List< UserConsumePay > userConsumePays = userConsumePayDAO.findByUcId( ucId );
	    SortedMap< String,Object > sortemap = dictService.getDict( "1198" );
	    String payType = "";
	    for ( UserConsumePay userConsumePay : userConsumePays ) {
		payType = CommonUtil.toString( sortemap.get( userConsumePay.getPaymentType() ) ) + "   ";
	    }
	    map.put( "payType", payType );
	    return map;
	} catch ( Exception e ) {
	    LOG.error( "查询订单详情异常", e );
	    throw new BusinessException( ResponseEnums.ERROR );
	}
    }

    public Page findCommend( Integer busId, Map< String,Object > params ) throws BusinessException {
	try {
	    params.put( "curPage", CommonUtil.isEmpty( params.get( "curPage" ) ) ? 1 : CommonUtil.toInteger( params.get( "curPage" ) ) );
	    int pageSize = 10;
	    Object search1 = params.get( "search" );
	    String search = null;
	    if ( CommonUtil.isNotEmpty( search1 ) ) {
		search = search1.toString();
	    }
	    Integer recommendType = null;
	    if ( CommonUtil.isNotEmpty( params.get( "recommendType" ) ) ) {
		recommendType = CommonUtil.toInteger( params.get( "recommendType" ) );
	    }

	    int rowCount = memberMapper.countCommend( busId, search, recommendType );

	    Page page = new Page( CommonUtil.toInteger( params.get( "curPage" ) ), pageSize, rowCount, "" );
	    params.put( "firstResult", pageSize * ( ( page.getCurPage() <= 0 ? 1 : page.getCurPage() ) - 1 ) );
	    params.put( "maxResult", pageSize );
	    List< Map< String,Object > > list = memberMapper.findCommend( busId, search, CommonUtil.toInteger( params.get( "firstResult" ) ), pageSize, recommendType );
	    List< Map< String,Object > > memberList = new ArrayList< Map< String,Object > >();
	    for ( Map< String,Object > map : list ) {
		if ( map.containsKey( "nickname" ) ) {
		    try {
			byte[] bytes = (byte[]) map.get( "nickname" );
			map.put( "nickname", new String( bytes, "UTF-8" ) );
		    } catch ( Exception e ) {
			map.put( "nickname", null );
		    }
		    memberList.add( map );
		} else {
		    memberList.add( map );
		}
	    }
	    page.setSubList( memberList );
	    return page;
	} catch ( Exception e ) {
	    LOG.error( "查询推荐记录异常", e );
	    throw new BusinessException( ResponseEnums.ERROR );
	}
    }

    /**
     * 推荐佣金提取记录
     *
     * @param busId
     * @param params
     *
     * @return
     */
    public Page findPickLog( Integer busId, Map< String,Object > params ) throws BusinessException {
	try {
	    params.put( "curPage", CommonUtil.isEmpty( params.get( "curPage" ) ) ? 1 : CommonUtil.toInteger( params.get( "curPage" ) ) );
	    int pageSize = 10;
	    Object search1 = params.get( "phone" );
	    String search = null;
	    if ( CommonUtil.isNotEmpty( search1 ) ) {
		search = search1.toString();
	    }
	    int rowCount = memberPicklogDAO.countPickLog( busId, search );
	    Page page = new Page( CommonUtil.toInteger( params.get( "curPage" ) ), pageSize, rowCount, "" );
	    params.put( "firstResult", pageSize * ( ( page.getCurPage() <= 0 ? 1 : page.getCurPage() ) - 1 ) );
	    params.put( "maxResult", pageSize );
	    List< Map< String,Object > > list = memberPicklogDAO.findPickLog( busId, search, CommonUtil.toInteger( params.get( "firstResult" ) ), pageSize );
	    List< Map< String,Object > > memberList = new ArrayList< Map< String,Object > >();
	    for ( Map< String,Object > map : list ) {
		if ( map.containsKey( "nickname" ) ) {
		    try {
			byte[] bytes = (byte[]) map.get( "nickname" );
			map.put( "nickname", new String( bytes, "UTF-8" ) );
		    } catch ( Exception e ) {
			map.put( "nickname", null );
		    }
		    memberList.add( map );
		} else {
		    memberList.add( map );
		}

	    }
	    page.setSubList( memberList );
	    return page;
	} catch ( Exception e ) {
	    LOG.error( "佣金提取记录异常", e );
	    throw new BusinessException( ResponseEnums.ERROR );
	}
    }

    /**
     * 删除会员卡
     *
     * @param memberId
     *
     * @throws BusinessException
     */
    public void deleteMemberCard( HttpServletRequest request, Integer memberId ) throws BusinessException {
	Integer pidBusId = SessionUtils.getPidBusId( request );
	BusUser busUser = SessionUtils.getLoginUser( request );
	if ( !pidBusId.equals( busUser.getId() ) ) {
	    throw new BusinessException( ResponseMemberEnums.VERIFICATION_BUSUSER );
	}
	MemberEntity memberEntity = memberMapper.selectById( memberId );
	MemberOld old = JSONObject.toJavaObject( JSON.parseObject( JSONObject.toJSONString( memberEntity ) ), MemberOld.class );

	// 删除数据做移出到memberold
	memberOldDAO.insert( old );
	memberMapper.updateMcIdBymemberId( memberId );

	MemberCard memberCard = memberCardDAO.selectById( memberEntity.getMcId() );
	MemberCardOld memberCardOld = JSONObject.toJavaObject( JSON.parseObject( JSONObject.toJSONString( memberCard ) ), MemberCardOld.class );
	memberCardOldDAO.insert( memberCardOld );
	memberCard.deleteById( memberEntity.getMcId() );
    }

}
