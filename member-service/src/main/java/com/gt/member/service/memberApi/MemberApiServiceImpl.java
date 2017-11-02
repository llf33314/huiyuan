/**
 * P 2016年4月5日
 */
package com.gt.member.service.memberApi;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gt.api.enums.ResponseEnums;
import com.gt.common.entity.BusUserEntity;
import com.gt.common.entity.WxPublicUsersEntity;
import com.gt.common.entity.WxShop;
import com.gt.entityBo.*;
import com.gt.member.dao.*;
import com.gt.member.dao.common.BusUserDAO;
import com.gt.member.dao.common.FenbiFlowRecordDAO;
import com.gt.member.dao.common.WxPublicUsersDAO;
import com.gt.member.dao.common.WxShopDAO;
import com.gt.member.entity.*;
import com.gt.member.enums.ResponseMemberEnums;
import com.gt.member.exception.BusinessException;
import com.gt.member.service.common.membercard.MemberCommonService;
import com.gt.member.service.common.dict.DictService;
import com.gt.member.service.member.MemberCardService;
import com.gt.member.service.member.SystemMsgService;
import com.gt.member.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author pengjiangli
 * @version 创建时间:2016年4月5日
 */
@Service
public class MemberApiServiceImpl implements MemberApiService {

    private static final Logger LOG = LoggerFactory.getLogger( MemberApiServiceImpl.class );


    @Autowired
    private MemberEntityDAO memberDAO;

    @Autowired
    private MemberGiveruleDAO giveRuleMapper;

    @Autowired
    private MemberGiverulegoodstypeDAO giveRuleGoodsTypeMapper;

    @Autowired
    private MemberGiveconsumeDAO giveConsumeMapper;

    @Autowired
    private WxPublicUsersDAO wxPublicUsersMapper;

    @Autowired
    private BusUserDAO busUserMapper;

    @Autowired
    private MemberCardrecordDAO cardRecordMapper;

    @Autowired
    private MemberRechargegiveDAO rechargeGiveMapper;

    @Autowired
    private MemberGradetypeDAO gradeTypeMapper;

    @Autowired
    private MemberCardbuyDAO cardBuyMapper;

    @Autowired
    private MemberDateDAO memberDateMapper;

    @Autowired
    private MemberCardLentDAO cardLentMapper;

    @Autowired
    private PublicParametersetDAO publicParameterSetMapper;

    @Autowired
    private SystemMsgService systemMsgService;

    @Autowired
    private MemberAppletOpenidDAO memberAppletOpenidMapper;

    @Autowired
    private MemberOldDAO memberOldMapper;

    @Autowired
    private MemberCardService memberCardService;

    @Autowired
    private WxCardReceiveDAO wxCardReceiveMapper;

    @Autowired
    private DuofenCardGetDAO duofenCardGetMapper;

    @Autowired
    private MemberRecommendDAO recommendMapper;

    @Autowired
    private WxShopDAO wxShopDAO;

    @Autowired
    private CardCouponsApiService cardCouponsApiService;

    @Autowired
    private RedisCacheUtil redisCacheUtil;

    @Autowired
    private MemberGradetypeDAO memberGradetypeDAO;

    @Autowired
    private MemberCardrecordDAO memberCardrecordDAO;

    @Autowired
    private MemberCommonService memberCommonService;

    @Autowired
    private FenbiFlowRecordDAO fenbiFlowRecordDAO;

    @Autowired
    private MemberCardDAO memberCardDAO;

    @Autowired
    private MemberParameterDAO memberParameterDAO;

    @Autowired
    private DictService dictService;

    @Autowired
    private DuofenCardDAO duofenCardDAO;

    @Autowired
    private MemberCardLentDAO memberCardLentDAO;

    @Autowired
    private UserConsumeNewDAO userConsumeNewDAO;

    @Autowired
    private UserConsumePayDAO userConsumePayDAO;

    @Autowired
    private MemberCardrecordNewDAO memberCardrecordNewDAO;

    @Autowired
    private UserConsumeDAO userConsumeMapper;

    /**
     * 查询粉丝信息
     *
     * @param memberId
     *
     * @return
     */
    public MemberEntity findByMemberId( Integer memberId ) throws BusinessException {
	try {
	    return memberDAO.selectById( memberId );
	} catch ( Exception e ) {
	    LOG.error( "查询粉丝信息异常", e );
	    throw new BusinessException( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getMsg() );
	}
    }


    public Map<String,Object> findMemberCardByMemberId( Integer memberId ) throws BusinessException{
	Map< String,Object > map = new HashMap< String,Object >();
	// 查询卡号是否存在
	try {
	    MemberEntity memberEntity = memberDAO.selectById( memberId );
	    MemberCard card = null;
	    // 查询卡号是否存在
	    if ( CommonUtil.isNotEmpty( memberEntity.getMcId() ) ) {
		card = memberCardDAO.selectById( memberEntity.getMcId() );
	    }
	    if ( CommonUtil.isEmpty( card ) ) {
		throw new BusinessException( ResponseMemberEnums.MEMBER_NOT_CARD.getCode(), ResponseMemberEnums.MEMBER_NOT_CARD.getMsg() );
	    } else if ( card.getCardStatus() == 1 ) {
		throw new BusinessException( ResponseMemberEnums.CARD_STATUS.getCode(), ResponseMemberEnums.CARD_STATUS.getMsg() );
	    } else {
		List< Map< String,Object > > cards = memberCardDAO.findCardById( card.getMcId() );
		MemberGiverule giveRule = giveRuleMapper.selectById( card.getGrId() );
		map.put( "nickName", memberEntity.getNickname() );
		map.put( "phone", memberEntity.getPhone() );
		map.put( "ctName", cards.get( 0 ).get( "ct_name" ) );
		map.put( "gradeName", cards.get( 0 ).get( "gt_grade_name" ) );
		map.put( "cardNo", card.getCardNo() );
		map.put( "ctId", card.getCtId() );
		map.put( "discount", giveRule.getGrDiscount() / 10.0 );
		map.put( "money", card.getMoney() );
		map.put( "frequency", card.getFrequency() );
		map.put( "fans_currency", memberEntity.getFansCurrency() );
		map.put( "integral", memberEntity.getIntegral() );
		map.put( "memberId", memberEntity.getId() );
		map.put( "cardId", card.getMcId() );
	    }
	    return map;
	} catch ( BusinessException e ) {
	    throw new BusinessException( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    LOG.error( "查询会员信息异常", e );
	    throw new BusinessException( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getMsg() );
	}
    }

    /**
     * 查询赠送规则
     *
     * @param busId
     * @param gtId
     * @param ctId
     *
     * @return
     */
    public MemberGiverule findGive( Integer busId, Integer gtId, Integer ctId ) {
	MemberGiverule gr = giveRuleMapper.findBybusIdAndGtIdAndCtId( busId, gtId, ctId );
	return gr;
    }

    /**
     * @param busId
     * @param gtId
     * @param totalmoney
     */
    public Map< String,Object > findNextGradeCtId4( Integer busId, Integer gtId, Double totalmoney ) {
	Map< String,Object > map = new HashMap< String,Object >();
	List< Map< String,Object > > giveRules = giveRuleMapper.findByBusIdAndCtId( busId, 4 );
	if ( giveRules.size() != 0 ) {
	    for ( Map< String,Object > giveRule : giveRules ) {
		if ( CommonUtil.isNotEmpty( giveRule.get( "gr_rechargeMoney" ) ) ) {
		    Double rechargeMoney = CommonUtil.toDouble( giveRule.get( "gr_rechargeMoney" ) );
		    if ( totalmoney.equals( rechargeMoney ) ) {
			map.put( "gtId", giveRule.get( "gt_id" ) );
			map.put( "grId", giveRule.get( "gr_id" ) );
			return map;
		    }
		}
	    }
	}
	return null;
    }

    /**
     * 判断是否升级
     * <p>
     * 公众号
     *
     * @param ctId       卡类型
     * @param gtId       等级id
     * @param integral   累计积分
     * @param totalmoney 累计消费金额
     *
     * @return
     */
    @Override
    public Map< String,Object > findNextGrade( Integer busId, Integer ctId, Integer gtId, Integer integral, double totalmoney ) {
	Map< String,Object > map = new HashMap< String,Object >();
	// <!--查询下一个等级start-->
	List< Map< String,Object > > gradeTypes = gradeTypeMapper.findByCtId( busId, ctId );

	if ( gradeTypes != null ) {
	    for ( int i = 0; i < gradeTypes.size(); i++ ) {
		if ( CommonUtil.isNotEmpty( gradeTypes.get( i ).get( "gtId" ) ) ) {
		    if ( gtId.equals( gradeTypes.get( i ).get( "gtId" ) ) ) {
			if ( i < gradeTypes.size() - 1 ) {
			    // 下一级id
			    if ( CommonUtil.isNotEmpty( gradeTypes.get( i + 1 ).get( "gtId" ) ) ) {
				Integer id = Integer.parseInt( gradeTypes.get( i + 1 ).get( "gtId" ).toString() );
				MemberGiverule nextGiveRule = giveRuleMapper.findBybusIdAndGtIdAndCtId( busId, id, Integer.parseInt( ctId.toString() ) );
				if ( CommonUtil.isEmpty( nextGiveRule ) ) {
				    break;
				}
				// 金额升级
				if ( 0 == nextGiveRule.getGrUpgradeType() ) {
				    if ( totalmoney >= nextGiveRule.getGrUpgradeCount() ) {
					map.put( "gtId", id );
					map.put( "grId", nextGiveRule.getGrId() );
					return map;
				    }
				}
				if ( 1 == nextGiveRule.getGrUpgradeType() ) {
				    if ( integral >= nextGiveRule.getGrUpgradeCount() ) {
					map.put( "gtId", id );
					map.put( "grId", nextGiveRule.getGrId() );
					return map;
				    }
				}
			    }
			}
		    }
		}
	    }

	    MemberGiverule nextGiveRule = giveRuleMapper.findBybusIdAndGtIdAndCtId( busId, gtId, Integer.parseInt( ctId.toString() ) );
	    map.put( "gtId", gtId );
	    map.put( "grId", nextGiveRule.getGrId() );
	    return map;
	}
	return null;
    }

    /**
     * 根据赠送规则和商户id查询
     */
    public int findRechargegive( double price, Integer grId, Integer busId, Integer ctId ) {
	MemberDate memberdate = memberCommonService.findMemeberDate( busId, ctId );
	List< Map< String,Object > > rechargeGives = null;
	if ( CommonUtil.isNotEmpty( memberdate ) ) {
	    rechargeGives = rechargeGiveMapper.findBybusIdAndGrId( busId, grId, 1 );
	} else {
	    rechargeGives = rechargeGiveMapper.findBybusIdAndGrId( busId, grId, 0 );
	}
	if ( rechargeGives == null || rechargeGives.size() == 0 ) {
	    return 0;
	}
	for ( int i = 0; i < rechargeGives.size(); i++ ) {
	    if ( i + 1 == rechargeGives.size() ) {
		double money = Double.parseDouble( rechargeGives.get( i ).get( "money" ).toString() );
		if ( money <= price ) {
		    return Integer.parseInt( rechargeGives.get( i ).get( "giveCount" ).toString() );
		} else {
		    return 0;
		}
	    }
	    if ( CommonUtil.isNotEmpty( rechargeGives.get( i ).get( "money" ) ) ) {
		double money = Double.parseDouble( rechargeGives.get( i ).get( "money" ).toString() );
		double money1 = Double.parseDouble( rechargeGives.get( i + 1 ).get( "money" ).toString() );
		if ( price >= money && price < money1 ) {
		    return Integer.parseInt( rechargeGives.get( i ).get( "giveCount" ).toString() );
		}
	    }

	}
	return 0;
    }

    /**
     * 查询时效卡的有效时间
     *
     * @param money
     *
     * @return
     */
    public List< Integer > findTimeCard( Double money, Integer busId, MemberDate memberDate ) {
	List< Map< String,Object > > giveRules = giveRuleMapper.findByBusIdAndCtId( busId, 4 );
	List< Integer > times = new ArrayList< Integer >();
	if ( giveRules.size() == 0 ) {
	    return null;
	}
	for ( Map< String,Object > map : giveRules ) {
	    if ( CommonUtil.isNotEmpty( map.get( "gr_rechargeMoney" ) ) ) {
		Double rechargeMoney = CommonUtil.toDouble( map.get( "gr_rechargeMoney" ) );

		if ( money.equals( rechargeMoney ) ) {
		    times.add( CommonUtil.toInteger( map.get( "gr_validDate" ) ) );
		    if ( CommonUtil.isNotEmpty( memberDate ) ) {
			times.add( CommonUtil.toInteger( map.get( "delayDay" ) ) );
		    }
		    return times;
		}
	    }
	}
	return null;
    }

    @Override
    public Double findCardTypeReturnDiscount( Integer memberId ) throws BusinessException {
	try {
	    if ( CommonUtil.isEmpty( memberId ) ) {
		throw new BusinessException( ResponseMemberEnums.NULL );
	    }
	    MemberEntity memberEntity = memberDAO.selectById( memberId );
	    if ( CommonUtil.isEmpty( memberEntity ) || CommonUtil.isEmpty( memberEntity.getMcId() ) ) {
		throw new BusinessException( ResponseMemberEnums.NO_DATA );
	    }
	    MemberCard card = memberCardDAO.selectById( memberEntity.getMcId() );
	    if ( CommonUtil.isEmpty( card ) ) {
		throw new BusinessException( ResponseMemberEnums.NO_DATA );
	    }

	    if ( card.getCtId() == 2 ) {
		MemberDate memberDate = memberCommonService.findMemeberDate( memberEntity.getBusId(), 2 );

		MemberGiverule giveRule = giveRuleMapper.selectById( card.getGrId() );
		if ( CommonUtil.isNotEmpty( memberDate ) ) {
		    return new Double( giveRule.getGrDiscount() * memberDate.getDiscount() ) / 1000;
		} else {
		    return new Double( giveRule.getGrDiscount() ) / 100;
		}
	    }
	    return 1.0;
	} catch ( BusinessException e ) {
	    throw new BusinessException( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    LOG.error( "查询会员折扣异常", e );
	    throw new BusinessException( ResponseEnums.ERROR );
	}
    }

    /**
     * 可供商家选择的会员卡
     */
    @Override
    public List< MemberCardtype > findMemberCard_1( Integer busId ) {
	List< MemberCardtype > mapList = new ArrayList< MemberCardtype >();
	MemberCardtype ct = null;

	List< Map< String,Object > > gradeTypes = gradeTypeMapper.findBybusId1( busId );
	if ( CommonUtil.isNotEmpty( gradeTypes ) && gradeTypes.size() > 0 ) {
	    for ( Map< String,Object > map : gradeTypes ) {
		ct = new MemberCardtype();
		ct.setCtId( CommonUtil.toInteger( map.get( "ctId" ) ) );
		ct.setCtName( CommonUtil.toString( map.get( "ctName" ) ) );
		mapList.add( ct );
	    }
	}

	return mapList;
    }

    /**
     * 判断用户是否是会员 false 不是 true 是
     */
    @Override
    public void isMemember( Integer memberId ) throws BusinessException {
	MemberEntity memberEntity = memberDAO.selectById( memberId );
	if ( CommonUtil.isNotEmpty( memberEntity ) && CommonUtil.isNotEmpty( memberEntity.getMcId() ) ) {
	    MemberCard card = memberCardDAO.selectById( memberEntity.getMcId() );
	    if ( card.getIsChecked() == 0 || card.getCardStatus() == 1 ) {
		throw new BusinessException( ResponseMemberEnums.CARD_STATUS );
	    }
	} else {
	    throw new BusinessException( ResponseMemberEnums.NOT_MEMBER_CAR );
	}
    }

    /**
     * 判断用户的卡类型 1积分卡 2折扣卡 3储值卡 4时效卡 5次卡
     *
     * @param memberId
     *
     * @return
     */
    public Integer isCardType( Integer memberId ) throws BusinessException {
	if ( CommonUtil.isEmpty( memberId ) ) {
	    throw new BusinessException( ResponseMemberEnums.NULL );
	}
	MemberEntity memberEntity = memberDAO.selectById( memberId );
	if ( CommonUtil.isEmpty( memberId ) || CommonUtil.isEmpty( memberEntity.getMcId() ) ) {
	    throw new BusinessException( ResponseMemberEnums.NOT_MEMBER_CAR );
	}
	MemberCard card = memberCardDAO.selectById( memberEntity.getMcId() );
	if ( CommonUtil.isEmpty( card ) ) {
	    throw new BusinessException( ResponseMemberEnums.CARD_STATUS );
	}
	return card.getCtId();
    }

    /**
     * 获取card信息
     *
     * @param memberId
     *
     * @return
     */
    public MemberCard findCardByMemberId( Integer memberId ) {
	if ( CommonUtil.isEmpty( memberId ) ) {
	    return null;
	}
	MemberEntity memberEntity = memberDAO.selectById( memberId );
	if ( CommonUtil.isEmpty( memberId ) || CommonUtil.isEmpty( memberEntity.getMcId() ) ) {
	    return null;
	}
	MemberCard card = memberCardDAO.selectById( memberEntity.getMcId() );
	return card;
    }

    /**
     * 根据粉丝id
     */
    @Override
    public Map< String,Object > findCardGiveRule( Integer memberId, Double totalMoney ) {
	Map< String,Object > map = new HashMap< String,Object >();
	//TODO
	//	if ( !isMemember( memberId ) ) {
	//	    map.put( "result", false );
	//	    map.put( "message", "非会员" );
	//	    return map;
	//	}

	// 根据会员id查询赠送规则
	MemberEntity memberEntity = memberDAO.selectById( memberId );
	MemberCard card = memberCardDAO.selectById( memberEntity.getMcId() );
	MemberGiverule giveRule = giveRuleMapper.selectById( card.getGrId() );
	switch ( card.getCtId() ) {
	    case 1:
		// 积分卡 查询赠送积分
		//		GiveRuleGoodsTypeKey key = new GiveRuleGoodsTypeKey();
		//		key.setGrId( card.getGrId() );
		//		key.setGtId( 1 );
		//		GiveRuleGoodsType grgt = giveRuleGoodsTypeMapper.selectById( key );
		//		Double count = totalMoney / grgt.getMoney();
		//		Integer number = count.intValue() * grgt.getNumber();
		//		map.put( "result", true );
		//		map.put( "ctId", 1 );
		//		map.put( "number", number );
		//		return map;
	    case 2:
		// 查询折扣
		double discount = giveRule.getGrDiscount() / 100.0;
		map.put( "result", true );
		map.put( "ctId", 2 );
		map.put( "discount", discount );
		map.put( "disCountmoney", totalMoney * discount );
		return map;
	    case 3:
		// 查询剩余金额
		map.put( "result", true );
		map.put( "ctId", 3 );
		map.put( "balance", card.getMoney() );
		return map;
	    default:
		break;
	}

	map.put( "result", false );
	map.put( "message", "会员,非指定卡类型" );
	return null;
    }

    @Override
    public MemberGradetype findGradeType( Integer memberId ) {
	MemberEntity memberEntity = memberDAO.selectById( memberId );
	if ( CommonUtil.isEmpty( memberEntity ) || CommonUtil.isEmpty( memberEntity.getMcId() ) ) {
	    return null;
	}
	Integer mcId = memberEntity.getMcId();
	MemberCard card = memberCardDAO.selectById( mcId );
	MemberGradetype gradeType = gradeTypeMapper.selectById( card.getGtId() );
	return gradeType;
    }

    @Override
    public Map< String,Object > findMember( String openId ) {
	Map< String,Object > returnMap = new HashMap< String,Object >();
	MemberEntity memberEntity = memberDAO.queryOpenid( openId );
	if ( CommonUtil.isEmpty( memberEntity ) || CommonUtil.isEmpty( memberEntity.getMcId() ) ) {
	    returnMap.put( "result", 0 );
	    returnMap.put( "message", "非会员" );
	    return returnMap;
	}

	MemberCard card = memberCardDAO.selectById( memberEntity.getMcId() );
	if ( card.getIsChecked() == 0 || card.getCardStatus() == 1 ) {
	    returnMap.put( "result", 0 );
	    returnMap.put( "message", "非会员" );
	    return returnMap;
	}
	// 会员查看粉币抵扣金额和积分抵扣金额
	Double jifenMoney = memberCommonService.integralCount( null, memberEntity.getIntegral().doubleValue(), memberEntity.getBusId() );
	returnMap.put( "jifenMoney", jifenMoney );

	Double fenbiMoney = memberCommonService.currencyCount( null, memberEntity.getFansCurrency() );

	returnMap.put( "fenbiMoney", fenbiMoney );

	if ( card.getCtId() == 2 ) {
	    MemberDate memberDate = memberCommonService.findMemeberDate( memberEntity.getPublicId(), 2 );

	    MemberGiverule giveRule = giveRuleMapper.selectById( card.getGrId() );
	    if ( CommonUtil.isNotEmpty( memberDate ) ) {
		returnMap.put( "memberDate", true );
		returnMap.put( "discount", new Double( giveRule.getGrDiscount() * memberDate.getDiscount() ) / 1000 );
	    } else {
		returnMap.put( "discount", new Double( giveRule.getGrDiscount() ) / 100 );
	    }
	}
	MemberGradetype gt = gradeTypeMapper.selectById( card.getGtId() );
	returnMap.put( "dengji", gt.getGtGradeName() );
	switch ( card.getCtId() ) {
	    case 1:
		returnMap.put( "leixing", "积分卡" );
		break;
	    case 2:
		returnMap.put( "leixing", "折扣卡" );
		break;
	    case 3:
		returnMap.put( "leixing", "储值卡" );
		break;
	    case 4:
		returnMap.put( "leixing", "时效卡" );
		break;
	    case 5:
		returnMap.put( "leixing", "次卡" );
		break;

	    default:
		break;
	}
	returnMap.put( "member", memberEntity );
	returnMap.put( "result", 1 );
	returnMap.put( "card", card );
	return returnMap;
    }

    @Transactional
    @Override
    public MemberEntity bingdingPhone( Integer memberId, String phone, String code, Integer busId ) throws BusinessException {
	Map< String,Object > map = new HashMap< String,Object >();
	try {
	    // 短信校验
	    if ( CommonUtil.isEmpty( memberId ) ) {
		throw new BusinessException( ResponseMemberEnums.NULL );
	    }

	    if ( CommonUtil.isEmpty( code ) ) {
		throw new BusinessException( ResponseMemberEnums.NULL );
	    }

	    if ( CommonUtil.isEmpty( phone ) ) {
		throw new BusinessException( ResponseMemberEnums.NULL );
	    }

	    if ( CommonUtil.isEmpty( busId ) ) {
		throw new BusinessException( ResponseMemberEnums.NULL );
	    }
	    // 短信判断

	    if ( CommonUtil.isEmpty( redisCacheUtil.get( code ) ) ) {
		throw new BusinessException( ResponseMemberEnums.NO_PHONE_CODE );
	    }
	    // 查询要绑定的手机号码
	    MemberEntity oldMemberEntity = memberDAO.findByPhone( busId, phone );

	    redisCacheUtil.del( code );

	    if ( CommonUtil.isEmpty( oldMemberEntity ) ) {
		// 新用户
		MemberEntity memberEntity = memberDAO.selectById( memberId );
		MemberEntity m = new MemberEntity();
		m.setId( memberEntity.getId() );
		m.setPhone( phone );
		memberDAO.updateById( m );
		memberEntity.setPhone( phone );
		return memberEntity;
	    } else {
		MemberEntity m1 = memberDAO.selectById( memberId );
		memberCommonService.newMemberMerge( m1, busId, phone );
		return m1;
	    }
	} catch ( BusinessException e ) {
	    throw new BusinessException( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    e.printStackTrace();
	    LOG.error( "小程序绑定手机号码异常", e );
	    throw new BusinessException( ResponseEnums.ERROR );
	}
    }

    @Override
    public List< Map< String,Object > > findMemberCardRecharge( Integer busId, String cardNo ) {
	MemberCard card = memberCardDAO.findCardByCardNo( busId, cardNo );
	if ( CommonUtil.isEmpty( card ) ) {
	    return null;
	}
	List< Map< String,Object > > recharges = null;
	// 判断会员日
	MemberDate memberDate = memberCommonService.findMemeberDate( busId, card.getCtId() );
	if ( CommonUtil.isNotEmpty( memberDate ) ) {
	    recharges = rechargeGiveMapper.findBybusIdAndGrId( busId, card.getGrId(), 1 );
	} else {
	    recharges = rechargeGiveMapper.findBybusIdAndGrId( busId, card.getGrId(), 0 );
	}
	return recharges;
    }

    @Override
    public Map< String,Object > findMemberByCardNo( String cardNo ) {
	List< Map< String,Object > > listMap = memberDAO.findCardNo( cardNo );
	if ( CommonUtil.isEmpty( listMap ) || listMap.size() <= 0 ) {
	    return null;
	}
	return listMap.get( 0 );
    }

    @Override
    public List< Integer > findMemberIds( Integer memberId ) {
	List< Integer > list = new ArrayList< Integer >();
	MemberEntity memberEntity = memberDAO.selectById( memberId );
	if ( CommonUtil.isEmpty( memberEntity.getOldId() ) ) {
	    list.add( memberId );
	    return list;
	}
	String[] str = memberEntity.getOldId().split( "," );
	for ( int i = 0; i < str.length; i++ ) {
	    if ( CommonUtil.isNotEmpty( str[i] ) && !str[i].contains( "null" ) && !list.contains( CommonUtil.toInteger( str[i] ) ) ) {
		list.add( CommonUtil.toInteger( str[i] ) );
	    }
	}

	if ( !list.contains( memberId ) ) {
	    list.add( memberId );
	}

	return list;
    }

    @Override
    public Map< String,Object > findMemberShopId( String phone, Integer busId, Integer shopId ) throws Exception {
	Map< String,Object > map = new HashMap<>();

	MemberEntity memberEntity = memberDAO.findByPhone( busId, phone );
	if ( CommonUtil.isEmpty( memberEntity ) || CommonUtil.isEmpty( memberEntity.getMcId() ) ) {
	    map.put( "result", false );
	    map.put( "message", "当前用户非会员" );
	    return map;
	}
	MemberCard card = memberCardDAO.selectById( memberEntity.getMcId() );

	if ( card.getShopId() > 0 ) {
	    if ( shopId != card.getShopId() && !shopId.equals( card.getShopId() ) ) {
		map.put( "result", false );
		map.put( "message", "当前用户会员不是该门店会员" );
		return map;
	    }
	}

	List< Map< String,Object > > cards = memberDAO.findMemberBybusIdAndPhone( busId, card.getMcId() );

	map.put( "result", true );
	map.put( "message", "当前用户会员是该门店会员" );
	if ( CommonUtil.isNotEmpty( cards ) && cards.size() > 0 ) {
	    Map< String,Object > c = cards.get( 0 );
	    if ( "0".equals( CommonUtil.toString( c.get( "isChecked" ) ) ) ) {
		map.put( "result", false );
		map.put( "message", "当前用户会员还未审核" );
		return map;
	    }

	    if ( "1".equals( CommonUtil.toString( c.get( "cardStatus" ) ) ) ) {
		map.put( "result", false );
		map.put( "message", "当前用户会员已禁用" );
		return map;
	    }
	    map.put( "card", cards.get( 0 ) );
	}
	return map;

    }

    @Override
    public List< Map< String,Object > > findGradeTypeByApplyType( Integer busId ) {
	return gradeTypeMapper.findGradeTypeByApplyType( busId );
    }

    /**
     * 根据粉丝id获取优惠券信息
     *
     * @param memberId
     * @param shopId
     *
     * @return
     * @throws Exception
     */
    public Map< String,Object > findMemberCardByMemberId( Integer memberId, Integer shopId ) throws BusinessException {
	Map< String,Object > map = new HashMap< String,Object >();
	// 查询卡号是否存在
	try {
	    MemberEntity memberEntity = memberDAO.selectById( memberId );
	    MemberCard card = null;
	    // 查询卡号是否存在
	    if ( CommonUtil.isNotEmpty( memberEntity.getMcId() ) ) {
		card = memberCardDAO.selectById( memberEntity.getMcId() );
	    }

	    if ( CommonUtil.isEmpty( card ) ) {
		throw new BusinessException( ResponseMemberEnums.MEMBER_NOT_CARD.getCode(), ResponseMemberEnums.MEMBER_NOT_CARD.getMsg() );

	    } else if ( card.getCardStatus() == 1 ) {
		throw new BusinessException( ResponseMemberEnums.CARD_STATUS.getCode(), ResponseMemberEnums.CARD_STATUS.getMsg() );
	    } else {
		List< Map< String,Object > > cards = memberCardDAO.findCardById( card.getMcId() );
		MemberGiverule giveRule = giveRuleMapper.selectById( card.getGrId() );
		map.put( "nickName", memberEntity.getNickname() );
		map.put( "phone", memberEntity.getPhone() );
		map.put( "ctName", cards.get( 0 ).get( "ct_name" ) );
		map.put( "gradeName", cards.get( 0 ).get( "gt_grade_name" ) );
		map.put( "cardNo", card.getCardNo() );
		map.put( "ctId", card.getCtId() );
		map.put( "discount", giveRule.getGrDiscount() / 10.0 );
		map.put( "money", card.getMoney() );
		map.put( "frequency", card.getFrequency() );
		map.put( "fans_currency", memberEntity.getFansCurrency() );
		map.put( "integral", memberEntity.getIntegral() );
		map.put( "memberId", memberEntity.getId() );
		map.put( "cardId", card.getMcId() );

		Double fenbiMoeny = memberCommonService.currencyCount( null, memberEntity.getFansCurrency() );
		map.put( "fenbiMoeny", fenbiMoeny );

		map.put( "getFenbiMoeny", 10 );

		Double jifenMoeny = memberCommonService.integralCount( null, new Double( memberEntity.getIntegral() ), memberEntity.getBusId() );
		map.put( "jifenMoeny", jifenMoeny );

		PublicParameterset ps = publicParameterSetMapper.findBybusId( memberEntity.getBusId() );
		if ( CommonUtil.isNotEmpty( ps ) ) {
		    map.put( "getJifenMoeny", ps.getStartMoney() );
		    map.put( "jifenRatio", ps.getIntegralRatio() );
		    map.put( "jifenStartMoney", ps.getStartMoney() );
		}

		SortedMap< String,Object > dict = dictService.getDict( "1058" );
		Double ratio = CommonUtil.toDouble( dict.get( "1" ) );
		map.put( "fenbiRatio", ratio );
		map.put( "fenbiStartMoney", 10 );



		WxShop wxShop = wxShopDAO.selectById( shopId );

		WxPublicUsersEntity wxPublicUsersEntity = wxPublicUsersMapper.selectByUserId( memberEntity.getBusId() );

		if ( CommonUtil.isNotEmpty( wxPublicUsersEntity ) && CommonUtil.isNotEmpty( memberEntity.getOpenid() ) && CommonUtil.isNotEmpty( wxShop )
				&& wxShop.getStatus() == 2 ) {
		    // 查询优惠券信息
		    List< Map< String,Object > > cardList = wxCardReceiveMapper.findByOpenId1( wxPublicUsersEntity.getId(), memberEntity.getOpenid() );
		    List< Map< String,Object > > list = new ArrayList< Map< String,Object > >();
		    if ( CommonUtil.isNotEmpty( cardList ) && cardList.size() > 0 ) {
			for ( Map< String,Object > map2 : cardList ) {
			    // 时间判断
			    if ( CommonUtil.isNotEmpty( map2.get( "begin_timestamp" ) ) && CommonUtil.isNotEmpty( map2.get( "end_timestamp" ) ) ) {
				if ( DateTimeKit.laterThanNow( DateTimeKit.parse( map2.get( "begin_timestamp" ).toString(), "yyyy-MM-dd hh:mm:ss" ) ) ) {
				    continue;
				}
				if ( !DateTimeKit.laterThanNow( DateTimeKit.parse( map2.get( "end_timestamp" ).toString(), "yyyy-MM-dd hh:mm:ss" ) ) ) {
				    continue;
				}
			    } else {
				if ( DateTimeKit.laterThanNow( DateTimeKit.addDays( DateTimeKit.parse( map2.get( "ctime" ).toString(), "yyyy-MM-dd hh:mm:ss" ),
						CommonUtil.toInteger( map2.get( "fixed_begin_term" ) ) ) ) ) {
				    continue;
				}
				if ( !DateTimeKit.laterThanNow( DateTimeKit.addDays( DateTimeKit.parse( map2.get( "ctime" ).toString(), "yyyy-MM-dd hh:mm:ss" ),
						CommonUtil.toInteger( map2.get( "fixed_term" ) ) ) ) ) {
				    continue;
				}
			    }

			    String day = DateTimeKit.getDayToEnglish();
			    if ( !map2.get( "time_limit" ).toString().contains( day ) ) {
				continue;
			    }

			    if ( map2.get( "location_id_list" ).toString().contains( CommonUtil.toString( wxShop.getPoiId() ) ) ) {
				list.add( map2 );
			    }
			}
		    }
		    if ( list.size() > 0 ) {
			map.put( "code", 1 );
			map.put( "cardList", JSONArray.toJSONString( list ) );
		    }
		}
		// 查询能使用的多粉优惠券
		List< Map< String,Object > > duofenCards = findDuofenCardByMemberId( memberEntity.getId(), shopId );
		map.put( "duofenCards", duofenCards );

		MemberDate memberDate = memberCommonService.findMemeberDate( memberEntity.getBusId(), card.getCtId() );
		if ( card.getCtId() == 2 ) {
		    if ( CommonUtil.isNotEmpty( memberDate ) ) {
			map.put( "memberDiscount", memberDate.getDiscount() * giveRule.getGrDiscount() / 100.0 );
			map.put( "memberDate", 1 );
		    }
		}

	    }

	    return map;
	} catch ( BusinessException e ) {
	    throw new BusinessException( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    LOG.error( "ERP查询会员信息异常", e );
	    throw new BusinessException( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getMsg() );
	}
    }

    @Override
    public Map< String,Object > findMemberCard( Integer busId, String cardNoKey, String cardNo, Integer shopId ) throws BusinessException {
	Map< String,Object > map = new HashMap<>();
	String cardNodecrypt = "";
	try {
	    try {
		// 如果手动输入 会出现异常
		cardNodecrypt = EncryptUtil.decrypt( cardNoKey, cardNo );
	    } catch ( Exception e ) {
	    }

	    BusUserEntity busUserEntity = busUserMapper.selectById( busId );
	    if ( busUserEntity.getPid() != 0 ) {
		busId = dictService.pidUserId( busUserEntity.getPid() );
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
			throw new BusinessException( ResponseMemberEnums.ERROR_QR_CODE.getCode(), ResponseMemberEnums.ERROR_QR_CODE.getMsg() );
		    }
		}
	    }

	    MemberCard card = null;

	    // 判断是否借给他人
	    MemberCardLent c = cardLentMapper.findByCode( cardNo );
	    if ( CommonUtil.isNotEmpty( c ) ) {
		// 判断时间是否在有效时间内
		// 5分钟后为超时
		if ( DateTimeKit.secondBetween( c.getCreateDate(), new Date() ) > 300 ) {
		    // 二维码已超时
		    throw new BusinessException( ResponseMemberEnums.ERROR_QR_CODE.getCode(), ResponseMemberEnums.ERROR_QR_CODE.getMsg() );

		}
		card = memberCardDAO.selectById( c.getMcId() );

		map.put( "jie", 1 );
		map.put( "lentMoney", c.getLentMoney() );
		map.put( "clId", c.getId() );  //用于处理借款状态
	    }

	    MemberEntity memberEntity = null;
	    // 查询卡号是否存在
	    if ( CommonUtil.isEmpty( card ) ) {
		card = memberCardDAO.findCardByCardNo( busId, cardNo );
		if ( CommonUtil.isNotEmpty( card ) ) {
		    memberEntity = memberDAO.findByMcIdAndbusId( busId, card.getMcId() );
		}

	    }

	    if ( CommonUtil.isEmpty( card ) ) {
		memberEntity = memberDAO.findByPhone( busId, cardNo );
		if ( CommonUtil.isNotEmpty( memberEntity ) ) {
		    card = memberCardDAO.selectById( memberEntity.getMcId() );
		}
	    }

	    if ( CommonUtil.isEmpty( card ) ) {
		throw new BusinessException( ResponseMemberEnums.NOT_MEMBER_CAR.getCode(), ResponseMemberEnums.NOT_MEMBER_CAR.getMsg() );
	    } else if ( card.getCardStatus() == 1 ) {
		throw new BusinessException( ResponseMemberEnums.CARD_STATUS.getCode(), ResponseMemberEnums.CARD_STATUS.getMsg() );
	    } else {
		List< Map< String,Object > > cards = memberCardDAO.findCardById( card.getMcId() );
		MemberGiverule giveRule = giveRuleMapper.selectById( card.getGrId() );
		map.put( "nickName", memberEntity.getNickname() );
		map.put( "phone", memberEntity.getPhone() );
		map.put( "ctName", cards.get( 0 ).get( "ct_name" ) );
		map.put( "gradeName", cards.get( 0 ).get( "gt_grade_name" ) );
		map.put( "cardNo", card.getCardNo() );
		map.put( "ctId", card.getCtId() );
		map.put( "discount", giveRule.getGrDiscount() / 10.0 );
		map.put( "money", card.getMoney() );
		map.put( "frequency", card.getFrequency() );
		map.put( "fans_currency", memberEntity.getFansCurrency() );
		map.put( "integral", memberEntity.getIntegral() );
		map.put( "memberId", memberEntity.getId() );
		map.put( "cardId", card.getMcId() );

		Double fenbiMoeny = memberCommonService.currencyCount( null, memberEntity.getFansCurrency() );
		map.put( "fenbiMoeny", fenbiMoeny );
		map.put( "getFenbiMoeny", 10 );

		Double jifenMoeny = memberCommonService.integralCount( null, new Double( memberEntity.getIntegral() ), busId );
		map.put( "jifenMoeny", jifenMoeny );
		PublicParameterset ps = publicParameterSetMapper.findBybusId( busId );
		if ( CommonUtil.isNotEmpty( ps ) ) {
		    map.put( "getJifenMoeny", ps.getStartMoney() );
		    map.put( "jifenRatio", ps.getIntegralRatio() );
		    map.put( "jifenStartMoney", ps.getStartMoney() );
		}

		SortedMap< String,Object > dict = dictService.getDict( "1058" );
		Double ratio = CommonUtil.toDouble( dict.get( "1" ) );
		map.put( "fenbiRatio", ratio );
		map.put( "fenbiStartMoney", 10 );



		WxShop wxShop = wxShopDAO.selectById( shopId );

		WxPublicUsersEntity wxPublicUsersEntity = wxPublicUsersMapper.selectByUserId( busId );

		if ( CommonUtil.isNotEmpty( wxPublicUsersEntity ) && CommonUtil.isNotEmpty( memberEntity.getOpenid() ) && CommonUtil.isNotEmpty( wxShop ) && wxShop.getStatus() == 2 ) {
		    // 查询优惠券信息
		    List< Map< String,Object > > cardList = wxCardReceiveMapper.findByOpenId1( wxPublicUsersEntity.getId(), memberEntity.getOpenid() );
		    List< Map< String,Object > > list = new ArrayList< Map< String,Object > >();
		    if ( CommonUtil.isNotEmpty( cardList ) && cardList.size() > 0 ) {
			for ( Map< String,Object > map2 : cardList ) {
			    // 时间判断
			    if ( CommonUtil.isNotEmpty( map2.get( "begin_timestamp" ) ) && CommonUtil.isNotEmpty( map2.get( "end_timestamp" ) ) ) {
				if ( DateTimeKit.laterThanNow( DateTimeKit.parse( map2.get( "begin_timestamp" ).toString(), "yyyy-MM-dd hh:mm:ss" ) ) ) {
				    continue;
				}
				if ( !DateTimeKit.laterThanNow( DateTimeKit.parse( map2.get( "end_timestamp" ).toString(), "yyyy-MM-dd hh:mm:ss" ) ) ) {
				    continue;
				}
			    } else {
				if ( DateTimeKit.laterThanNow( DateTimeKit.addDays( DateTimeKit.parse( map2.get( "ctime" ).toString(), "yyyy-MM-dd hh:mm:ss" ),
						CommonUtil.toInteger( map2.get( "fixed_begin_term" ) ) ) ) ) {
				    continue;
				}
				if ( !DateTimeKit.laterThanNow( DateTimeKit.addDays( DateTimeKit.parse( map2.get( "ctime" ).toString(), "yyyy-MM-dd hh:mm:ss" ),
						CommonUtil.toInteger( map2.get( "fixed_term" ) ) ) ) ) {
				    continue;
				}
			    }

			    String day = DateTimeKit.getDayToEnglish();
			    if ( !map2.get( "time_limit" ).toString().contains( day ) ) {
				continue;
			    }

			    if ( map2.get( "location_id_list" ).toString().contains( CommonUtil.toString( wxShop.getPoiId() ) ) ) {
				list.add( map2 );
			    }
			}
		    }
		    if ( list.size() > 0 ) {
			map.put( "cardList", JSONArray.toJSONString( list ) );
		    }
		}

		// 查询能使用的多粉优惠券
		List< Map< String,Object > > duofenCards = findDuofenCardByMemberId( memberEntity.getId(), shopId );
		map.put( "duofenCards", duofenCards );

		MemberDate memberDate = memberCommonService.findMemeberDate( busId, card.getCtId() );
		if ( card.getCtId() == 2 ) {
		    if ( CommonUtil.isNotEmpty( memberDate ) ) {
			map.put( "memberDiscount", memberDate.getDiscount() * giveRule.getGrDiscount() / 100.0 );
			map.put( "memberDate", 1 );
		    }
		}
	    }

	    return map;
	} catch ( BusinessException e ) {
	    throw new BusinessException( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    LOG.error( "ERP查询会员信息异常", e );
	    throw new BusinessException( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getMsg() );
	}
    }

    /**
     * 判断用户是否是会员 false 不是 true 是
     */
    @Override
    public boolean isMememberByApplet( BusUserEntity busUserEntity, String cardNoKey, String cardNo ) {
	Map< String,Object > map = new HashMap< String,Object >();
	String cardNodecrypt = "";
	try {
	    // 如果手动输入 会出现异常
	    cardNodecrypt = EncryptUtil.decrypt( cardNoKey, cardNo );
	} catch ( Exception e ) {
	}

	int busId = busUserEntity.getId();
	if ( busUserEntity.getPid() != 0 ) {
	    busId = dictService.pidUserId( busUserEntity.getPid() );
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
		    return false;
		}
	    }
	}
	MemberCard card = null;
	try {
	    MemberEntity memberEntity = null;
	    // 查询卡号是否存在
	    if ( CommonUtil.isEmpty( card ) ) {
		card = memberCardDAO.findCardByCardNo( busId, cardNo );
		if ( CommonUtil.isNotEmpty( card ) ) {
		    memberEntity = memberDAO.findByMcIdAndbusId( busId, card.getMcId() );
		}

	    }

	    if ( CommonUtil.isEmpty( card ) ) {
		memberEntity = memberDAO.findByPhone( busId, cardNo );

		if ( CommonUtil.isNotEmpty( memberEntity ) ) {
		    card = memberCardDAO.selectById( memberEntity.getMcId() );
		}
	    }

	    if ( CommonUtil.isEmpty( card ) ) {
		return false;
	    } else if ( card.getCardStatus() == 1 ) {
		return false;
	    }
	    return true;
	} catch ( Exception e ) {
	    LOG.error( "小程序查询会员信息异常", e );
	    return false;
	}
    }

    /**
     * 查询用户拥有的优惠券
     */
    public List< Map< String,Object > > findDuofenCardByMemberId( Integer memberId, Integer wxshopId ) {
	List< Map< String,Object > > duofencardgets = duofenCardGetMapper.findCardByMemberId( memberId );
	if ( CommonUtil.isEmpty( duofencardgets ) || duofencardgets.size() == 0 ) {
	    return null;
	}

	List< Map< String,Object > > duofencards = new ArrayList< Map< String,Object > >();
	for ( Map< String,Object > map : duofencardgets ) {
	    if ( "2".equals( map.get( "card_type" ).toString() ) || "3".equals( map.get( "card_type" ).toString() ) || "4".equals( map.get( "card_type" ).toString() ) ) {
		continue;
	    }

	    String day = DateTimeKit.getDayToEnglish();
	    if ( !map.get( "time_limit" ).toString().contains( day ) ) {
		continue;
	    }

	    if ( CommonUtil.isNotEmpty( map.get( "location_id_list" ) ) ) {
		String location_id_list = CommonUtil.toString( map.get( "location_id_list" ) );
		if ( location_id_list.contains( wxshopId.toString() ) ) {
		    duofencards.add( map );
		}
	    } else {
		duofencards.add( map );
	    }
	}
	return duofencards;
    }

    public Map< String,Object > verificationCard_2( Map< String,Object > params ) {
	Map< String,Object > map = new HashMap< String,Object >();
	try {
	    if ( CommonUtil.isEmpty( params.get( "codes" ) ) ) {
		map.put( "result", false );
		map.put( "message", "没有要核销的卡券code值" );
		return map;
	    }

	    // 多粉优惠券处理
	    String codes = params.get( "codes" ).toString();
	    String[] str = codes.split( "," );
	    List< String > codeList = new ArrayList< String >();
	    for ( String s : str ) {
		if ( CommonUtil.isNotEmpty( s ) ) {
		    codeList.add( CommonUtil.toString( s ) );
		}
	    }

	    List< Map< String,Object > > stateMap = duofenCardGetMapper.findByCodes( codeList );
	    if ( CommonUtil.isEmpty( stateMap ) || stateMap.size() == 0 ) {
		map.put( "result", false );
		map.put( "message", "卡券不存在" );
		return map;
	    }
	    for ( Map< String,Object > map2 : stateMap ) {
		if ( "1".equals( map2.get( "state" ) ) || "2".equals( map2.get( "state" ) ) ) {
		    map.put( "result", false );
		    map.put( "message", "卡券过期或已核销,不能执行卡券核销操作" );
		    return map;
		}
	    }

	    if ( CommonUtil.isEmpty( params.get( "storeId" ) ) ) {
		duofenCardGetMapper.updateByCodes( codeList ); // 卡券核销
	    } else {
		Integer storeId = CommonUtil.toInteger( params.get( "storeId" ) );
		duofenCardGetMapper.updateStoreIdByCodes( codeList, storeId ); // 卡券核销
	    }

	    //推荐优惠券赠送
	    for ( Map< String,Object > map2 : stateMap ) {
		if ( CommonUtil.toInteger( map2.get( "recommendId" ) ) > 0 ) {
		    MemberRecommend recommend = recommendMapper.selectById( CommonUtil.toInteger( map2.get( "recommendId" ) ) );
		    memberCommonService.tuijianGive( recommend );
		}
	    }

	    map.put( "result", true );
	    map.put( "message", "核销成功" );
	} catch ( Exception e ) {
	    map.put( "result", false );
	    map.put( "message", "核销失败" );
	}
	return map;
    }

    @Transactional
    @Override
    public void paySuccess( PaySuccessBo paySuccessBo ) throws BusinessException {
	try {
	    UserConsumeNew uc = new UserConsumeNew();
	    MemberEntity memberEntity = memberDAO.selectById( paySuccessBo.getMemberId() );
	    Double totalMoney = paySuccessBo.getPay();
	    //会员消费记录添加
	    uc.setBusId( memberEntity.getBusId() );
	    uc.setMemberId( memberEntity.getId() );
	    uc.setRecordType( 2 );
	    uc.setUcType( paySuccessBo.getUcType() );
	    uc.setTotalMoney( paySuccessBo.getTotalMoney() );
	    uc.setIntegral( paySuccessBo.getJifenNum() );
	    uc.setFenbi( paySuccessBo.getFenbiNum() );
	    uc.setDiscountMoney( paySuccessBo.getTotalMoney() - paySuccessBo.getDiscountMoney() );
	    uc.setDiscountAfterMoney( paySuccessBo.getDiscountMoney() );
	    uc.setPayStatus( 1 );
	    uc.setCardType( paySuccessBo.getCouponType() );
	    uc.setDisCountdepict( paySuccessBo.getCodes() );
	    uc.setOrderCode( paySuccessBo.getOrderCode() );
	    uc.setShopId( paySuccessBo.getStoreId() );
	    uc.setDataSource( paySuccessBo.getDataSource() );
	    uc.setIsend( 0 );
	    uc.setIsendDate( paySuccessBo.getIsendDate() );

	    MemberCard card = null;
	    if ( CommonUtil.isNotEmpty( memberEntity.getMcId() ) ) {
		card = memberCardDAO.selectById( memberEntity.getMcId() );
		uc.setMcId( memberEntity.getMcId() );
		uc.setCtId( card.getCtId() );
		uc.setGtId( card.getGtId() );
	    }
	    if ( paySuccessBo.getPayType() == 5 ) {
		//储值卡支付
		if ( CommonUtil.isEmpty( memberEntity.getMcId() ) ) {
		    throw new BusinessException( ResponseMemberEnums.NOT_MEMBER_CAR );
		}
		if ( CommonUtil.isNotEmpty( card ) ) {
		    if ( card.getCtId() == 3 ) {
			if ( card.getMoney() < totalMoney ) {
			    throw new BusinessException( ResponseMemberEnums.MEMBER_LESS_MONEY );
			}
			double banlan = card.getMoney() - totalMoney;
			card.setMoney( banlan );
			memberCardDAO.updateById( card );
			memberCommonService
					.saveCardRecordOrderCodeNew( memberEntity.getId(), 1, totalMoney, "消费", memberEntity.getBusId(), banlan, paySuccessBo.getOrderCode(), 0 );
			systemMsgService.sendChuzhiXiaofei( memberEntity, totalMoney );

			uc.setBalance( banlan );
		    } else {
			throw new BusinessException( ResponseMemberEnums.MEMBER_CHUZHI_CARD );
		    }
		}
	    }

	    //优惠券
	    if ( paySuccessBo.isUseCoupon() ) {
		if ( paySuccessBo.getCouponType() == 0 ) {
		    //微信
		    Integer publicId = wxPublicUsersMapper.selectByUserId( memberEntity.getBusId() ).getId();
		    cardCouponsApiService.wxCardReceive( publicId, paySuccessBo.getCodes() );
		    String code = paySuccessBo.getCodes();
		    uc.setDisCountdepict( paySuccessBo.getCodes() );
		} else {
		    //多粉
		    Map< String,Object > duofenMap = new HashMap<>();
		    duofenMap.put( "codes", paySuccessBo.getCodes() );
		    duofenMap.put( "storeId", paySuccessBo.getStoreId() );
		    cardCouponsApiService.verificationCard_2( duofenMap );
		    uc.setDisCountdepict( paySuccessBo.getCodes() );
		}
	    }

	    //粉币使用
	    if ( paySuccessBo.isUserFenbi() && CommonUtil.isNotEmpty( memberEntity.getMcId() ) ) {
		Double balance = memberEntity.getFansCurrency() - paySuccessBo.getFenbiNum();
		memberCommonService.reduceFansCurrency( memberEntity, paySuccessBo.getFenbiNum() );
		memberCommonService.saveCardRecordOrderCodeNew( memberEntity.getId(), 3, paySuccessBo.getFenbiNum().doubleValue(), "消费粉币", memberEntity.getBusId(), balance,
				paySuccessBo.getOrderCode(), 0 );

	    }
	    //积分使用
	    if ( paySuccessBo.isUserJifen() && CommonUtil.isNotEmpty( memberEntity.getMcId() ) ) {
		MemberEntity memberEntity1 = new MemberEntity();
		memberEntity1.setId( memberEntity.getId() );
		Integer banlan = memberEntity.getIntegral() - paySuccessBo.getJifenNum();
		memberEntity1.setIntegral( banlan );
		memberDAO.updateById( memberEntity1 );
		memberCommonService.saveCardRecordOrderCodeNew( memberEntity.getId(), 2, paySuccessBo.getJifenNum().doubleValue(), "消费积分", memberEntity.getBusId(),
				banlan.doubleValue(), paySuccessBo.getOrderCode(), 0 );

	    }
	    uc.setPayStatus( 1 );
	    userConsumeNewDAO.insert( uc );

	    UserConsumePay userConsumePay = new UserConsumePay();
	    userConsumePay.setUcId( uc.getId() );
	    userConsumePay.setPayMoney( paySuccessBo.getDiscountMoney() );
	    userConsumePay.setPaymentType( paySuccessBo.getPayType() );
	    userConsumePayDAO.insert( userConsumePay );

	    if ( paySuccessBo.getPayType() != 5 ) {
		memberCommonService.saveCardRecordOrderCodeNew( memberEntity.getId(), 1, totalMoney, "消费", memberEntity.getBusId(), 0.0, paySuccessBo.getOrderCode(), 0 );

	    }

	    if ( CommonUtil.isNotEmpty( memberEntity.getMcId() ) ) {
		//会员立即送 和 延迟送  TODO
		//		if ( paySuccessBo.getDelay() == 0 ) {
		//		    findGiveRuleDelay( paySuccessBo.getOrderCode() );  //延迟送
		//		} else if ( paySuccessBo.getDelay() == 1 ) {
		//		    findGiveRule( paySuccessBo.getOrderCode(), "消费会员赠送", (byte) 1 );
		//		}
	    }

	} catch ( BusinessException e ) {
	    throw new BusinessException( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    LOG.error( "支付成功回调异常" + JSONArray.toJSONString( paySuccessBo ), e );
	    throw new BusinessException( ResponseEnums.ERROR );
	}

    }

    @Override
    public Map< String,Object > countMember( Integer busId ) {
	WxShop wxShop = wxShopDAO.selectMainShopByBusId( busId );

	// 统计会员卡
	List< Map< String,Object > > countCard = memberCardDAO.countMember( busId );
	if ( CommonUtil.isEmpty( countCard ) || countCard.size() == 0 ) {
	    return null;
	}
	Integer onlinecount0 = 0; // 线上
	Integer onlinecount1 = 0; // 线下
	for ( Map< String,Object > map : countCard ) {
	    if ( CommonUtil.isEmpty( map.get( "shopId" ) ) ) {
		if ( "0".equals( CommonUtil.toString( map.get( "online" ) ) ) ) {
		    onlinecount0 = CommonUtil.toInteger( map.get( "countId" ) );
		}

		if ( "1".equals( CommonUtil.toString( map.get( "online" ) ) ) ) {
		    onlinecount1 = CommonUtil.toInteger( map.get( "countId" ) );
		}
	    }
	}

	List< Map< String,Object > > countMemberCard = new ArrayList<>();
	for ( Map< String,Object > map : countCard ) {
	    if ( CommonUtil.isEmpty( map.get( "shopId" ) ) ) {
		continue;
	    }
	    // 主店铺
	    if ( wxShop.getId().equals( CommonUtil.toInteger( map.get( "shopId" ) ) ) ) {
		if ( "0".equals( CommonUtil.toString( map.get( "online" ) ) ) ) {
		    map.put( "countId", CommonUtil.toInteger( map.get( "countId" ) ) + onlinecount0 );
		}

		if ( "1".equals( CommonUtil.toString( map.get( "online" ) ) ) ) {
		    map.put( "countId", CommonUtil.toInteger( map.get( "countId" ) ) + onlinecount1 );
		}
	    }
	    countMemberCard.add( map );
	}

	Map< Integer,Map< String,Object > > map1 = new HashMap<>();
	for ( Map< String,Object > map : countMemberCard ) {
	    if ( CommonUtil.isEmpty( map1.get( CommonUtil.toInteger( map.get( "shopId" ) ) ) ) ) {
		if ( "0".equals( CommonUtil.toString( map.get( "online" ) ) ) ) {
		    map.put( "countId1", 0 );
		}

		if ( "1".equals( CommonUtil.toString( map.get( "online" ) ) ) ) {
		    map.put( "countId1", map.get( "countId" ) );
		    map.put( "countId", 0 );

		}
		map1.put( CommonUtil.toInteger( map.get( "shopId" ) ), map );
	    } else {
		if ( "1".equals( CommonUtil.toString( map.get( "online" ) ) ) ) {
		    map.put( "countId1", map.get( "countId" ) );
		}
		map.put( "countId", map1.get( CommonUtil.toInteger( map.get( "shopId" ) ) ).get( "countId" ) );

		map1.put( CommonUtil.toInteger( map.get( "shopId" ) ), map );
	    }
	}

	Map< String,Object > memMap = null;

	Integer online0 = 0;
	Integer online1 = 0;
	List< Map< String,Object > > list = new ArrayList<>();
	for ( Integer in : map1.keySet() ) {
	    memMap = map1.get( in );// 得到每个key多对用value的值
	    online0 = online0 + CommonUtil.toInteger( memMap.get( "countId" ) );
	    online1 = online1 + CommonUtil.toInteger( memMap.get( "countId1" ) );
	    list.add( memMap );
	}
	Map< String,Object > maps = new HashMap<>();
	maps.put( "cardMember", list );
	maps.put( "online0", online0 );
	maps.put( "online1", online1 );
	return maps;
    }

    @Override
    public void isAdequateMoney( Integer memberId, Double money ) throws BusinessException {
	try {
	    MemberEntity memberEntity = memberDAO.selectById( memberId );
	    if ( CommonUtil.isEmpty( memberEntity.getMcId() ) ) {
		throw new BusinessException( ResponseMemberEnums.NOT_MEMBER_CAR );
	    }
	    MemberCard card = memberCardDAO.selectById( memberEntity.getMcId() );
	    if ( CommonUtil.isNotEmpty( card ) ) {
		if ( card.getCtId() == 3 ) {
		    if ( card.getMoney() >= money ) {
			return;
		    }
		}
	    }
	    throw new BusinessException( ResponseMemberEnums.MEMBER_LESS_MONEY );
	} catch ( BusinessException e ) {
	    throw new BusinessException( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    throw new BusinessException( ResponseEnums.ERROR );
	}
    }

    @Override
    public List< Map< String,Object > > findMemberByIds( Integer busId, String memberIds ) throws BusinessException {
	try {
	    String[] str = memberIds.split( "," );
	    List< Integer > ids = new ArrayList<>();
	    for ( int i = 0; i < str.length; i++ ) {
		if ( CommonUtil.isNotEmpty( str[i] ) ) {
		    ids.add( CommonUtil.toInteger( str[i] ) );
		}
	    }
	    List< Map< String,Object > > list = memberDAO.findByMemberIds( busId, ids );

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

	    return memberList;
	} catch ( Exception e ) {
	    throw new BusinessException( ResponseEnums.ERROR );
	}
    }

    @Override
    public List< Map< String,Object > > findBuyGradeType( Integer busId ) {
	return memberGradetypeDAO.findBuyGradeType( busId );
    }

    public List< Map< String,Object > > findCardrecord( Integer memberId, Integer page, Integer pageSize ) {
	return memberCardrecordNewDAO.findCardrecordByMemberId( memberId, page * pageSize, pageSize );
    }

    public MemberCard findMemberCardByMcId( Integer mcId ) {
	return memberCardDAO.selectById( mcId );
    }

    public Map< String,Object > findMemberCardByMemberIdAndshopIds( Integer memberId, String shopIds ) throws BusinessException {
	Map< String,Object > map = new HashMap< String,Object >();
	// 查询卡号是否存在
	try {
	    MemberEntity memberEntity = memberDAO.selectById( memberId );
	    MemberCard card = null;
	    // 查询卡号是否存在
	    if ( CommonUtil.isNotEmpty( memberEntity.getMcId() ) ) {
		card = memberCardDAO.selectById( memberEntity.getMcId() );
	    }

	    if ( CommonUtil.isEmpty( card ) ) {
		throw new BusinessException( ResponseMemberEnums.MEMBER_NOT_CARD.getCode(), ResponseMemberEnums.MEMBER_NOT_CARD.getMsg() );

	    } else if ( card.getCardStatus() == 1 ) {
		throw new BusinessException( ResponseMemberEnums.CARD_STATUS.getCode(), ResponseMemberEnums.CARD_STATUS.getMsg() );
	    } else {
		List< Map< String,Object > > cards = memberCardDAO.findCardById( card.getMcId() );
		MemberGiverule giveRule = giveRuleMapper.selectById( card.getGrId() );
		map.put( "nickName", memberEntity.getNickname() );
		map.put( "phone", memberEntity.getPhone() );
		map.put( "ctName", cards.get( 0 ).get( "ct_name" ) );
		map.put( "gradeName", cards.get( 0 ).get( "gt_grade_name" ) );
		map.put( "cardNo", card.getCardNo() );
		map.put( "ctId", card.getCtId() );
		map.put( "discount", giveRule.getGrDiscount() / 10.0 );
		map.put( "money", card.getMoney() );
		map.put( "frequency", card.getFrequency() );
		map.put( "fans_currency", memberEntity.getFansCurrency() );
		map.put( "integral", memberEntity.getIntegral() );
		map.put( "memberId", memberEntity.getId() );
		map.put( "cardId", card.getMcId() );

		Double fenbiMoeny = memberCommonService.currencyCount( null, memberEntity.getFansCurrency() );
		map.put( "fenbiMoeny", fenbiMoeny );

		Double jifenMoeny = memberCommonService.integralCount( null, new Double( memberEntity.getIntegral() ), memberEntity.getBusId() );
		map.put( "jifenMoeny", jifenMoeny );

		WxPublicUsersEntity wxPublicUsersEntity = wxPublicUsersMapper.selectByUserId( memberEntity.getBusId() );

		String[] str = shopIds.split( "," );
		for ( int i = 0; i < str.length; i++ ) {
		    if ( CommonUtil.isEmpty( str[i] ) ) continue;
		    Integer shopId = CommonUtil.toInteger( str[i] );
		    WxShop wxShop = wxShopDAO.selectById( shopId );
		    if ( CommonUtil.isNotEmpty( wxPublicUsersEntity ) && CommonUtil.isNotEmpty( memberEntity.getOpenid() ) && wxShop.getStatus() == 2 ) {
			// 查询优惠券信息
			List< Map< String,Object > > cardList = wxCardReceiveMapper.findByOpenId1( wxPublicUsersEntity.getId(), memberEntity.getOpenid() );
			List< Map< String,Object > > list = new ArrayList< Map< String,Object > >();
			if ( CommonUtil.isNotEmpty( cardList ) && cardList.size() > 0 ) {
			    for ( Map< String,Object > map2 : cardList ) {
				// 时间判断
				if ( CommonUtil.isNotEmpty( map2.get( "begin_timestamp" ) ) && CommonUtil.isNotEmpty( map2.get( "end_timestamp" ) ) ) {
				    if ( DateTimeKit.laterThanNow( DateTimeKit.parse( map2.get( "begin_timestamp" ).toString(), "yyyy-MM-dd hh:mm:ss" ) ) ) {
					continue;
				    }
				    if ( !DateTimeKit.laterThanNow( DateTimeKit.parse( map2.get( "end_timestamp" ).toString(), "yyyy-MM-dd hh:mm:ss" ) ) ) {
					continue;
				    }
				} else {
				    if ( DateTimeKit.laterThanNow( DateTimeKit.addDays( DateTimeKit.parse( map2.get( "ctime" ).toString(), "yyyy-MM-dd hh:mm:ss" ),
						    CommonUtil.toInteger( map2.get( "fixed_begin_term" ) ) ) ) ) {
					continue;
				    }
				    if ( !DateTimeKit.laterThanNow( DateTimeKit.addDays( DateTimeKit.parse( map2.get( "ctime" ).toString(), "yyyy-MM-dd hh:mm:ss" ),
						    CommonUtil.toInteger( map2.get( "fixed_term" ) ) ) ) ) {
					continue;
				    }
				}

				String day = DateTimeKit.getDayToEnglish();
				if ( !map2.get( "time_limit" ).toString().contains( day ) ) {
				    continue;
				}

				if ( map2.get( "location_id_list" ).toString().contains( CommonUtil.toString( wxShop.getPoiId() ) ) ) {
				    list.add( map2 );
				}
			    }
			}
			if ( list.size() > 0 ) {
			    map.put( "code", 1 );
			    map.put( "cardList" + shopId, JSONArray.toJSONString( list ) );
			}
		    }
		}

		for ( int i = 0; i < str.length; i++ ) {
		    if ( CommonUtil.isEmpty( str[i] ) ) continue;
		    Integer shopId = CommonUtil.toInteger( str[i] );
		    // 查询能使用的多粉优惠券
		    List< Map< String,Object > > duofenCards = findDuofenCardByMemberId( memberEntity.getId(), shopId );
		    map.put( "duofenCards" + shopId, duofenCards );
		}

		MemberDate memberDate = memberCommonService.findMemeberDate( memberEntity.getBusId(), card.getCtId() );
		if ( card.getCtId() == 2 ) {
		    if ( CommonUtil.isNotEmpty( memberDate ) ) {
			map.put( "memberDiscount", memberDate.getDiscount() * giveRule.getGrDiscount() / 100.0 );
			map.put( "memberDate", 1 );
		    }
		}
	    }

	    return map;
	} catch ( BusinessException e ) {
	    throw new BusinessException( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    LOG.error( "ERP查询会员信息异常", e );
	    throw new BusinessException( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getMsg() );
	}
    }

    @Transactional
    public void updateMemberPhoneByMemberId( Map< String,Object > map ) throws BusinessException {
	try {
	    Map< String,Object > returnMap = new HashMap<>();

	    Integer memberId = CommonUtil.toInteger( map.get( "memberId" ) );
	    String phone = CommonUtil.toString( map.get( "phone" ) );
	    Integer busId = CommonUtil.toInteger( map.get( "busId" ) );

	    MemberEntity memberEntity = memberDAO.findByPhone( busId, phone );
	    if (CommonUtil.isNotEmpty( memberEntity ) && !memberEntity.getId().equals( memberId ) ) {
		throw new BusinessException( ResponseMemberEnums.IS_BINDING_PHONE );
	    }

	    MemberEntity m = new MemberEntity();
	    m.setId( memberId );
	    m.setPhone( phone );
	    memberDAO.updateById( m );
	}catch ( BusinessException e ){
	    throw  e;
	}catch ( Exception e ) {
	    LOG.error( "绑定手机号码错误",e );
	    throw new BusinessException( ResponseEnums.ERROR );
	}
    }

    public List< Map< String,Object > > findMemberByIds( Map< String,Object > map ) throws BusinessException {
	try {
	    Integer busId = CommonUtil.toInteger( map.get( "busId" ) );
	    String ids = CommonUtil.toString( map.get( "ids" ) );
	    List< Integer > list = new ArrayList<>();
	    String[] str = ids.split( "," );
	    for ( int i = 0; i < str.length; i++ ) {
		if ( CommonUtil.isNotEmpty( str[i] ) ) {
		    list.add( CommonUtil.toInteger( str[i] ) );
		}
	    }
	    if ( list.size() > 0 ) {

		List< Map< String,Object > > membertS = memberDAO.findMemberByIds( busId, list );
		List< Map< String,Object > > memberList = new ArrayList< Map< String,Object > >();
		for ( Map< String,Object > memberMap : membertS ) {
		    if ( memberMap.containsKey( "nickname" ) ) {
			try {
			    byte[] bytes = (byte[]) memberMap.get( "nickname" );
			    memberMap.put( "nickname", new String( bytes, "UTF-8" ) );
			} catch ( Exception e ) {
			    memberMap.put( "nickname", null );
			}
			memberList.add( memberMap );
		    } else {
			memberList.add( memberMap );
		    }

		}

	    }
	    return null;
	} catch ( Exception e ) {
	    throw new BusinessException( ResponseEnums.ERROR );
	}
    }

    /**
     * 根据手机号查询粉丝信息
     *
     * @param map
     *
     * @return
     * @throws BusinessException
     */
    public List< Map< String,Object > > findMemberByPhoneAndBusId( Map< String,Object > map ) throws BusinessException {
	try {
	    Integer busId = CommonUtil.toInteger( map.get( "busId" ) );
	    String phone = CommonUtil.toString( map.get( "phone" ) );

	    List< Map< String,Object > > list = memberDAO.findMemberByPhoneAndBusId( busId, phone );
	    List< Map< String,Object > > memberList = new ArrayList< Map< String,Object > >();
	    for ( Map< String,Object > memberMap : list ) {
		if ( memberMap.containsKey( "nickname" ) ) {
		    try {
			byte[] bytes = (byte[]) memberMap.get( "nickname" );
			memberMap.put( "nickname", new String( bytes, "UTF-8" ) );
		    } catch ( Exception e ) {
			memberMap.put( "nickname", null );
		    }
		    memberList.add( memberMap );
		} else {
		    memberList.add( memberMap );
		}

	    }
	    return memberList;

	} catch ( Exception e ) {
	    throw new BusinessException( ResponseEnums.ERROR );
	}
    }

    public Map< String,Object > findMemberCardTypeByBusId( Map< String,Object > map ) throws BusinessException {
	try {
	    Map< String,Object > returnMap = new HashMap<>();
	    Integer busId = CommonUtil.toInteger( map.get( "busId" ) );
	    List< Map< String,Object > > mapList = memberGradetypeDAO.findBybusId1( busId );
	    returnMap.put( "cardType", mapList );
	    if ( mapList.size() > 0 ) {
		List< Map< String,Object > > gradeTypes = gradeTypeMapper.findGradeTyeBybusIdAndctId( busId, CommonUtil.toInteger( mapList.get( 0 ).get( "ctId" ) ) );
		if ( gradeTypes.size() > 0 ) {
		    if ( "3".equals( gradeTypes.get( 0 ).get( "applyType" ) ) ) {
			returnMap.put( "gradeTypes", JSON.toJSON( gradeTypes ) );
		    } else {
			List< Map< String,Object > > list = new ArrayList<>();
			list.add( gradeTypes.get( 0 ) );
			returnMap.put( "gradeTypes", JSON.toJSON( list ) );
		    }
		}
	    }
	    return returnMap;
	} catch ( Exception e ) {
	    throw new BusinessException( ResponseEnums.ERROR );
	}
    }

    public List< Map< String,Object > > findMemberGradeTypeByctId( Map< String,Object > map ) throws BusinessException {
	Integer busId = CommonUtil.toInteger( map.get( "busId" ) );
	Integer ctId = CommonUtil.toInteger( map.get( "ctId" ) );
	List< Map< String,Object > > gradeTypes = gradeTypeMapper.findGradeTyeBybusIdAndctId( busId, ctId );
	if ( gradeTypes.size() > 0 ) {
	    if ( "3".equals( CommonUtil.toString( gradeTypes.get( 0 ).get( "applyType" ) ) ) ) {
		return gradeTypes;
	    } else {
		List< Map< String,Object > > returnMap = new ArrayList<>();
		returnMap.add( gradeTypes.get( 0 ) );
		return returnMap;
	    }
	}
	return null;
    }

    /**
     * 墨盒会员卡充值接口
     *
     * @return
     * @throws BusinessException
     */
    public Map< String,Object > findMemberAndChongZhi( Map< String,Object > params ) throws BusinessException {
	Map< String,Object > map = new HashMap<>();
	try {
	    MemberCard card = null;

	    MemberEntity memberEntity = null;
	    // 查询卡号是否存在
	    Integer busId = CommonUtil.toInteger( params.get( "busId" ) );
	    String phone = CommonUtil.toString( params.get( "phone" ) );

	    String cardNodecrypt = null;
	    try {
		// 如果手动输入 会出现异常
		cardNodecrypt = EncryptUtil.decrypt( PropertiesUtil.getCardNoKey(), phone );

		if ( CommonUtil.isNotEmpty( cardNodecrypt ) && cardNodecrypt.contains( "?time" ) ) {
		    // 查询卡号是否存在
		    Long time = Long.parseLong( cardNodecrypt.substring( cardNodecrypt.indexOf( "?time=" ) + 6 ) );

		    String cardNo = cardNodecrypt.substring( 0, cardNodecrypt.indexOf( "?time" ) );
		    card = memberCardDAO.findCardByCardNo( busId, cardNo );
		    if ( card.getCtId() == 3 ) {
			// 2分钟后为超时
			if ( DateTimeKit.secondBetween( new Date( time ), new Date() ) > 120 ) {
			    // 二维码已超时
			    throw new BusinessException( ResponseMemberEnums.ERROR_QR_CODE );
			}
		    }
		    memberEntity = memberDAO.findByMcIdAndbusId( busId, card.getMcId() );
		}

	    } catch ( Exception e ) {
		// 如果不是扫码 判断商家是否允许不扫码
		//		List<Map< String,Object >> list = dictService.getDict("A001");
		//		boolean saomao=false;
		//		for (Map<String, Object> map2 : list) {
		//		    if(busId.equals( map2.get( "item_key" ) )){
		//			saomao=true;
		//		    }
		//		}
		//		if(true){
		//		    throw new BusinessException( ResponseMemberEnums. )
		//		}
		card = memberCardDAO.findCardByCardNo( busId, phone );
	    }
	    if ( CommonUtil.isNotEmpty( card ) ) {
		memberEntity = memberDAO.findByMcIdAndbusId( busId, card.getMcId() );
	    } else {
		memberEntity = memberDAO.findByPhone( busId, phone );
	    }

	    if ( CommonUtil.isNotEmpty( memberEntity ) ) {
		if ( CommonUtil.isEmpty( card ) ) {
		    card = memberCardDAO.selectById( memberEntity.getMcId() );
		}
	    }
	    if ( CommonUtil.isEmpty( card ) ) {
		throw new BusinessException( ResponseMemberEnums.NO_DATA.getCode(), ResponseMemberEnums.NO_DATA.getMsg() );
	    } else if ( card.getCardStatus() == 1 ) {
		throw new BusinessException( ResponseMemberEnums.CARD_STATUS.getCode(), ResponseMemberEnums.CARD_STATUS.getMsg() );
	    } else {
		List< Map< String,Object > > cards = memberCardDAO.findCardById( card.getMcId() );
		MemberGiverule giveRule = giveRuleMapper.selectById( card.getGrId() );
		map.put( "nickName", memberEntity.getNickname() );
		map.put( "phone", memberEntity.getPhone() );
		map.put( "ctName", cards.get( 0 ).get( "ct_name" ) );
		map.put( "gradeName", cards.get( 0 ).get( "gt_grade_name" ) );
		map.put( "cardNo", card.getCardNo() );
		map.put( "ctId", card.getCtId() );
		map.put( "discount", giveRule.getGrDiscount() / 10 );
		map.put( "money", card.getMoney() );
		map.put( "frequency", card.getFrequency() );
		map.put( "fans_currency", memberEntity.getFansCurrency() );
		map.put( "integral", memberEntity.getIntegral() );
		map.put( "memberId", memberEntity.getId() );

		MemberDate memberDate = memberCommonService.findMemeberDate( memberEntity.getBusId(), card.getCtId() );
		if ( CommonUtil.isNotEmpty( memberDate ) ) {
		    List< Map< String,Object > > recharges = rechargeGiveMapper.findBybusIdAndGrId( busId, card.getGrId(), 1 );
		    map.put( "recharges", recharges );
		    map.put( "cardDate", "1" );
		} else {
		    List< Map< String,Object > > recharges = rechargeGiveMapper.findBybusIdAndGrId( busId, card.getGrId(), 0 );
		    map.put( "recharges", recharges );
		    map.put( "cardDate", "0" );
		}

		return map;
	    }
	} catch ( BusinessException e ) {
	    throw new BusinessException( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    LOG.error( "ERP查询会员信息异常", e );
	    throw new BusinessException( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getMsg() );
	}
    }

    /**
     * 领取会员卡
     */
    @Transactional
    public void linquMemberCard( Map< String,Object > params ) throws BusinessException {
	try {
	    Integer busId = CommonUtil.toInteger( params.get( "busId" ) );
	    busId = dictService.pidUserId( busId );
	    BusUserEntity busUserEntity = busUserMapper.selectById( busId );
	    int count = memberCardDAO.countCardisBinding( busId );
	    String dictNum = dictService.dictBusUserNum( busUserEntity.getId(), busUserEntity.getLevel(), 4, "1093" ); // 多粉 翼粉
	    if ( CommonUtil.toInteger( dictNum ) < count ) {
		throw new BusinessException( ResponseMemberEnums.NOT_MEMBER_COUNT );
	    }

	    MemberEntity memberEntity = null;

	    String phone = CommonUtil.toString( params.get( "phone" ) );

	    if ( CommonUtil.isNotEmpty( params.get( "memberId" ) ) ) {
		Integer memberId = CommonUtil.toInteger( params.get( "memberId" ) );
		memberEntity = memberDAO.selectById( memberId );
		memberCommonService.newMemberMerge( memberEntity, busId, phone );
	    }

	    if ( CommonUtil.isEmpty( memberEntity ) ) {
		memberEntity = memberDAO.findByPhone( busId, phone );
	    }

	    if ( CommonUtil.isEmpty( memberEntity ) ) {
		// 新增用户
		memberEntity = new MemberEntity();
		memberEntity.setPhone( phone );
		memberEntity.setBusId( busUserEntity.getId() );
		memberEntity.setLoginMode( 1 );
		memberEntity.setNickname( "Fans_" + phone.substring( 4 ) );
		memberDAO.insert( memberEntity );
		MemberParameter memberParameter = memberParameterDAO.findByMemberId( memberEntity.getId() );
		if ( CommonUtil.isEmpty( memberParameter ) ) {
		    MemberParameter mp = new MemberParameter();
		    mp.setMemberId( memberEntity.getId() );
		    memberParameterDAO.insert( mp );
		}
	    } else {
		if ( CommonUtil.isNotEmpty( memberEntity.getMcId() ) ) {
		    throw new BusinessException( ResponseMemberEnums.IS_MEMBER_CARD );
		}
	    }

	    Integer ctId = CommonUtil.toInteger( params.get( "ctId" ) );
	    Integer shopId = CommonUtil.toInteger( params.get( "shopId" ) );

	    // 根据卡片类型 查询第一等级
	    List< Map< String,Object > > gradeTypes = gradeTypeMapper.findBybusIdAndCtId3( busId, ctId );
	    Integer applyType = CommonUtil.toInteger( gradeTypes.get( 0 ).get( "applyType" ) );
	    if ( applyType != 3 ) {
		//非购买会员卡  直接分配会员卡
		MemberCard card = new MemberCard();
		card.setIsChecked( 1 );
		card.setCardNo( CommonUtil.getCode() );
		card.setCtId( ctId );
		if ( card.getCtId() == 4 ) {
		    card.setExpireDate( new Date() );
		}

		card.setSystemcode( CommonUtil.getNominateCode() );
		card.setApplyType( CommonUtil.toInteger( gradeTypes.get( 0 ).get( "applyType" ) ) );
		if ( gradeTypes != null && gradeTypes.size() > 0 ) {
		    card.setGtId( Integer.parseInt( gradeTypes.get( 0 ).get( "gt_id" ).toString() ) );
		    MemberGiverule giveRule = giveRuleMapper.findBybusIdAndGtIdAndCtId( busId, Integer.parseInt( gradeTypes.get( 0 ).get( "gt_id" ).toString() ), ctId );
		    card.setGrId( giveRule.getGrId() );
		}
		card.setBusId( busId );
		card.setReceiveDate( new Date() );
		card.setIsbinding( 1 );

		card.setShopId( shopId );
		card.setOnline( 0 );
		memberCardDAO.insert( card );

		MemberEntity memberEntity1 = new MemberEntity();
		memberEntity1.setMcId( card.getMcId() );
		memberEntity1.setPhone( phone );
		memberEntity1.setId( memberEntity.getId() );
		memberDAO.updateById( memberEntity1 );

	    } else {
		throw new BusinessException( ResponseMemberEnums.PLEASE_PAY );
	    }

	} catch ( BusinessException e ) {
	    throw new BusinessException( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    LOG.error( "erp 领取会员卡异常", e );
	    throw new BusinessException( ResponseEnums.ERROR );
	}
    }

    @Transactional
    public void successChongZhi( Map< String,Object > params ) throws BusinessException {
	Integer memberId = CommonUtil.toInteger( params.get( "memberId" ) );
	Double totalMoney = CommonUtil.toDouble( params.get( "money" ) );
	Integer paymentType = CommonUtil.toInteger( params.get( "paymentType" ) );
	Integer shopId = CommonUtil.toInteger( params.get( "shopId" ) );
	try {
	    MemberEntity member = memberDAO.selectById( memberId );
	    MemberCard card = memberCardDAO.selectById( member.getMcId() );
	    // 添加会员记录
	    UserConsume uc = new UserConsume();
	    uc.setBusUserId( member.getBusId() );
	    uc.setMemberId( memberId );
	    uc.setMcId( card.getMcId() );
	    uc.setCtId( card.getCtId() );
	    uc.setGtId( card.getGtId() );
	    uc.setRecordType( 1 );
	    uc.setUcType( 7 );
	    uc.setTotalMoney( totalMoney );
	    uc.setCreateDate( new Date() );
	    uc.setPayStatus( 1 );
	    uc.setDiscountMoney( totalMoney );
	    uc.setDataSource( 4 );
	    uc.setIschongzhi( 1 );
	    if ( card.getCtId() == 3 ) {
		MemberGiverule gr = findGive( member.getBusId(), card.getGtId(), 3 );
		if ( CommonUtil.isNotEmpty( gr ) ) {
		    Integer count = findRechargegive( totalMoney, gr.getGrId(), member.getBusId(), card.getCtId() );
		}
		uc.setUccount( 0 );
	    } else if ( card.getCtId() == 5 ) {
		MemberGiverule gr = findGive( member.getBusId(), card.getGtId(), 5 );
		if ( CommonUtil.isNotEmpty( gr ) ) {
		    int givecount = findRechargegive( totalMoney, gr.getGrId(), member.getBusId(), card.getCtId() );
		    uc.setUccount( givecount );
		}
	    }
	    uc.setStoreId( shopId );
	    String orderCode = CommonUtil.getMEOrderCode();
	    uc.setOrderCode( orderCode );
	    userConsumeMapper.insert( uc );

	    // 判断是否是会员日
	    MemberDate membetdate = memberCommonService.findMemeberDate( member.getBusId(), card.getCtId() );

	    double balance = 0.0;
	    if ( CommonUtil.isNotEmpty( card ) && CommonUtil.isNotEmpty( card.getMoney() ) ) {
		if ( card.getCtId() == 4 ) {
		    // 时效卡
		    List< Integer > dateCount = findTimeCard( totalMoney, card.getBusId(), membetdate );
		    if ( dateCount == null ) {
			throw new BusinessException( ResponseMemberEnums.NOT_FIND_CHONGZHI_MSG );
		    }
		    if ( dateCount.size() == 0 ) {
			card.setExpireDate( null );
		    } else {
			Date expireDate = card.getExpireDate();
			if ( expireDate == null ) {
			    card.setExpireDate( DateTimeKit.addMonths( dateCount.get( 0 ) ) );
			} else {
			    if ( DateTimeKit.laterThanNow( card.getExpireDate() ) ) {
				card.setExpireDate( DateTimeKit.addMonths( expireDate, dateCount.get( 0 ) ) );
			    } else {
				card.setExpireDate( DateTimeKit.addMonths( new Date(), dateCount.get( 0 ) ) );
			    }
			}

			// 会员日延期多少天
			if ( CommonUtil.isNotEmpty( membetdate ) ) {
			    card.setExpireDate( DateTimeKit.addDate( card.getExpireDate(), dateCount.get( 0 ) ) );
			}
		    }
		} else {
		    // 次卡和储值卡充值 修改卡片信息
		    balance = card.getMoney();
		    if ( CommonUtil.isNotEmpty( totalMoney ) && totalMoney != 0 ) {
			Double giftCount = uc.getGiftCount().doubleValue();
			if ( card.getCtId() == 3 ) {
			    totalMoney = totalMoney + giftCount;
			    card.setMoney( balance + totalMoney );
			}
		    }

		    if ( CommonUtil.isNotEmpty( uc.getUccount() ) ) {
			Integer uccount = uc.getUccount();
			if ( uccount != 0 ) {
			    Integer giftCount = uc.getGiftCount();
			    uccount = uccount + giftCount;
			    card.setFrequency( card.getFrequency() + uccount );
			}
		    }
		}
		memberCardDAO.updateById( card );

		memberCommonService.saveCardRecordOrderCodeNew( member.getId(), 1, totalMoney, "会员卡充值", member.getBusId(), card.getMoney(), orderCode, 0 );

	    }
	} catch ( BusinessException e ) {
	    throw e;
	} catch ( Exception e ) {
	    LOG.error( "充值调用异常:" + com.alibaba.fastjson.JSONObject.toJSONString( params ), e );
	    throw new BusinessException( ResponseEnums.ERROR );
	}
    }

    @Transactional
    public void successChongZhiVer1( Map< String,Object > params ) throws BusinessException {
	Integer memberId = CommonUtil.toInteger( params.get( "memberId" ) );
	Double totalMoney = CommonUtil.toDouble( params.get( "money" ) );
	Integer paymentType = CommonUtil.toInteger( params.get( "paymentType" ) );
	Integer shopId = CommonUtil.toInteger( params.get( "shopId" ) );
	try {
	    if ( CommonUtil.isEmpty( memberId ) || CommonUtil.isEmpty( totalMoney ) || CommonUtil.isEmpty( paymentType ) || CommonUtil.isEmpty( shopId ) ) {
		throw new BusinessException( ResponseMemberEnums.NULL );
	    }
	    MemberEntity member = memberDAO.selectById( memberId );
	    MemberCard card = memberCardDAO.selectById( member.getMcId() );
	    if ( CommonUtil.isEmpty( card ) ) {
		throw new BusinessException( ResponseMemberEnums.MEMBER_NOT_CARD );
	    }
	    // 添加会员记录
	    UserConsumeNew uc = new UserConsumeNew();
	    uc.setBusId( member.getBusId() );
	    uc.setMemberId( memberId );
	    uc.setMcId( card.getMcId() );
	    uc.setCtId( card.getCtId() );
	    uc.setGtId( card.getGtId() );
	    uc.setRecordType( 1 );
	    uc.setUcType( 7 );
	    uc.setTotalMoney( totalMoney );
	    uc.setCreateDate( new Date() );
	    uc.setPayStatus( 1 );
	    uc.setDiscountMoney( 0.0 );
	    uc.setDiscountAfterMoney( totalMoney );
	    uc.setDataSource( 4 );
	    uc.setIschongzhi( 1 );
	    uc.setIsend( 1 );
	    uc.setIsendDate( new Date() );
	    uc.setPayStatus( 1 );

	    Integer count = 0;

	    if ( card.getCtId() == 3 ) {
		MemberGiverule gr = findGive( member.getBusId(), card.getGtId(), 3 );
		if ( CommonUtil.isNotEmpty( gr ) ) {
		    count = findRechargegive( totalMoney, gr.getGrId(), member.getBusId(), card.getCtId() );
		}
		uc.setUccount( 0 );
	    } else if ( card.getCtId() == 5 ) {
		MemberGiverule gr = findGive( member.getBusId(), card.getGtId(), 5 );
		if ( CommonUtil.isNotEmpty( gr ) ) {
		    count = findRechargegive( totalMoney, gr.getGrId(), member.getBusId(), card.getCtId() );
		    uc.setUccount( 0 );
		}
	    }
	    uc.setShopId( shopId );
	    String orderCode = CommonUtil.getMEOrderCode();
	    uc.setOrderCode( orderCode );

	    // 判断是否是会员日
	    MemberDate membetdate = memberCommonService.findMemeberDate( member.getBusId(), card.getCtId() );

	    double balance = 0.0;
	    if ( card.getCtId() == 4 ) {
		// 时效卡
		List< Integer > dateCount = findTimeCard( totalMoney, card.getBusId(), membetdate );
		if ( dateCount == null ) {
		    throw new BusinessException( ResponseMemberEnums.NOT_FIND_CHONGZHI_MSG );
		}
		if ( dateCount.size() == 0 ) {
		    card.setExpireDate( null );
		} else {
		    Date expireDate = card.getExpireDate();
		    if ( expireDate == null ) {
			card.setExpireDate( DateTimeKit.addMonths( dateCount.get( 0 ) ) );
		    } else {
			if ( DateTimeKit.laterThanNow( card.getExpireDate() ) ) {
			    card.setExpireDate( DateTimeKit.addMonths( expireDate, dateCount.get( 0 ) ) );
			} else {
			    card.setExpireDate( DateTimeKit.addMonths( new Date(), dateCount.get( 0 ) ) );
			}
		    }
		}
	    } else if ( card.getCtId() == 3 ) {
		balance = card.getMoney();
		if ( CommonUtil.isNotEmpty( totalMoney ) && totalMoney != 0 ) {
		    Double giftCount = count.doubleValue();
		    if ( card.getCtId() == 3 ) {
			totalMoney = totalMoney + count + balance;
			card.setMoney( totalMoney );
			uc.setBalance( totalMoney );
			memberCommonService.saveCardRecordOrderCodeNew( member.getId(), 1, totalMoney, "会员卡充值", member.getBusId(), totalMoney.doubleValue(), orderCode, 0 );
		    }
		}
	    } else {
		// 次卡和储值卡充值 修改卡片信息
		if ( CommonUtil.isNotEmpty( uc.getUccount() ) ) {
		    Integer uccount = uc.getUccount();
		    if ( uccount != 0 ) {
			Integer giftCount = count;
			uccount = uccount + giftCount + card.getFrequency();
			card.setFrequency( uccount );
			uc.setBalanceCount( uccount );
			memberCommonService.saveCardRecordOrderCodeNew( member.getId(), 1, totalMoney, "会员卡充值", member.getBusId(), uccount.doubleValue(), orderCode, 0 );
		    }
		}
	    }
	    memberCardDAO.updateById( card );
	    userConsumeNewDAO.insert( uc );

	    UserConsumePay userConsumePay = new UserConsumePay();
	    userConsumePay.setUcId( uc.getId() );
	    userConsumePay.setPaymentType( paymentType );
	    userConsumePay.setPayMoney( totalMoney );
	    userConsumePayDAO.insert( userConsumePay );
	} catch ( BusinessException e ) {
	    throw e;
	} catch ( Exception e ) {
	    LOG.error( "充值调用异常:" + com.alibaba.fastjson.JSONObject.toJSONString( params ), e );
	    throw new BusinessException( ResponseEnums.ERROR );
	}
    }

    /**
     * (商城）评论修改会员积分或粉币
     *
     * @param params
     */
    @Transactional
    public void updateJifenAndFenBiByPinglu( Map< String,Object > params ) throws BusinessException {
	try {
	    Integer memberId = CommonUtil.toInteger( params.get( "memberId" ) );
	    Integer jifen = CommonUtil.toInteger( params.get( "jifen" ) );
	    Double fenbi = CommonUtil.toDouble( params.get( "fenbi" ) );
	    MemberEntity memberEntity = memberDAO.selectById( memberId );
	    MemberEntity m1 = new MemberEntity();
	    m1.setId( memberId );
	    boolean bool = false;
	    if ( CommonUtil.isNotEmpty( jifen ) && jifen > 0 ) {
		m1.setIntegral( memberEntity.getIntegral() + jifen );
		bool = true;
		Integer yuejifen = memberEntity.getIntegral() + jifen;
		memberCommonService.saveCardRecordOrderCodeNew( memberEntity.getId(), 2, jifen.doubleValue(), "评论赠送积分", memberEntity.getBusId(), yuejifen.doubleValue(), "", 1 );
	    }
	    if ( CommonUtil.isNotEmpty( fenbi ) && fenbi > 0 ) {
		Double yueFenbi = memberEntity.getFansCurrency() + fenbi;
		memberCommonService.giveFansCurrency( memberId, fenbi );
		memberCommonService.saveCardRecordOrderCodeNew( memberEntity.getId(), 3, fenbi.doubleValue(), "评论赠送粉币", memberEntity.getBusId(), yueFenbi, "", 1 );

		bool = true;
	    }
	    if ( bool ) {
		memberDAO.updateById( m1 );
	    }
	} catch ( BusinessException e ) {
	    throw e;
	} catch ( Exception e ) {
	    throw new BusinessException( ResponseEnums.ERROR );
	}
    }

    public Map< String,Object > jifenAndFenbiRule( Integer busId ) throws BusinessException {
	try {
	    Map< String,Object > map = new HashMap< String,Object >();
	    SortedMap< String,Object > dict = dictService.getDict( "1058" );
	    Double ratio = CommonUtil.toDouble( dict.get( "1" ) );
	    map.put( "fenbiRatio", ratio );
	    map.put( "fenbiStartMoney", 10 );

	    PublicParameterset ps = publicParameterSetMapper.findBybusId_1( busId );
	    if ( CommonUtil.isNotEmpty( ps ) ) {
		map.put( "jifenRatio", ps.getIntegralRatio() );
		map.put( "jifenStartMoney", ps.getStartMoney() );
	    }
	    return map;
	} catch ( Exception e ) {
	    throw new BusinessException( ResponseEnums.ERROR );
	}

    }

    /**
     * erp计算 会员卡核销接口（包括储值卡扣款 、 借款、优惠券核销 、积分、粉币）
     *
     * @param newerpPaySuccessBo
     */
    @Transactional
    public void newPaySuccessByErpBalance( String newerpPaySuccessBo ) throws BusinessException {
	try {
	    NewErpPaySuccessBo erpPaySuccess = JSON.toJavaObject( JSON.parseObject( newerpPaySuccessBo ), NewErpPaySuccessBo.class );
	    UserConsumeNew uc = new UserConsumeNew();

	    MemberEntity memberEntity = memberDAO.selectById( erpPaySuccess.getMemberId() );

	    //会员消费记录添加
	    uc.setBusId( memberEntity.getBusId() );
	    uc.setMemberId( memberEntity.getId() );
	    uc.setRecordType( 2 );
	    uc.setUcType( erpPaySuccess.getUcType() );
	    uc.setTotalMoney( erpPaySuccess.getTotalMoney() );
	    uc.setDiscountMoney( erpPaySuccess.getDiscountMoney() ); //优惠金额
	    uc.setDiscountAfterMoney( erpPaySuccess.getDiscountAfterMoney() );  //优惠后金额

	    uc.setOrderCode( erpPaySuccess.getOrderCode() );
	    uc.setShopId( erpPaySuccess.getStoreId() );
	    uc.setDataSource( erpPaySuccess.getDataSource() );
	    uc.setIsend( 0 );
	    if ( erpPaySuccess.getIsendDate() > 0 ) {
		uc.setIsendDate( new Date( erpPaySuccess.getIsendDate() ) );
	    } else {
		uc.setIsendDate( new Date() );
	    }
	    uc.setIntegral( erpPaySuccess.getJifenNum() );
	    uc.setFenbi( erpPaySuccess.getFenbiNum() );
	    if ( erpPaySuccess.getUseCoupon() == 1 ) {
		//优惠券
		if ( erpPaySuccess.getCouponType() == 0 ) {
		    //微信
		    Integer publicId = wxPublicUsersMapper.selectByUserId( memberEntity.getBusId() ).getId();
		    //微信
		    WxCardReceive wxCardReceive = wxCardReceiveMapper.selectById( erpPaySuccess.getCardId() );
		    cardCouponsApiService.wxCardReceive( publicId, wxCardReceive.getUserCardCode() );
		    uc.setDisCountdepict( wxCardReceive.getUserCardCode() );
		} else {
		    //多粉优惠券
		    DuofenCardGet dfget = duofenCardGetMapper.selectById( erpPaySuccess.getCardId() );
		    DuofenCard dfcard = duofenCardDAO.selectById( dfget.getCardId() );
		    List< Map< String,Object > > dfcg = duofenCardGetMapper.findByCardId( dfcard.getId(), erpPaySuccess.getMemberId(), erpPaySuccess.getNumber() );
		    if ( dfcg.size() > 0 ) {
			String duofenCode = "";
			for ( Map< String,Object > map : dfcg ) {
			    duofenCode += map.get( "code" ) + ",";
			}
			Map< String,Object > duofenMap = new HashMap<>();
			duofenMap.put( "codes", duofenCode );
			duofenMap.put( "storeId", erpPaySuccess.getStoreId() );
			cardCouponsApiService.verificationCard_2( duofenMap );
			uc.setDisCountdepict( duofenCode );
		    }
		}
		uc.setCardType( erpPaySuccess.getCouponType() );
	    }

	    boolean bool = false;
	    MemberCard card = null;
	    List< PayTypeBo > payTypeBos = erpPaySuccess.getPayTypeBos();
	    if ( CommonUtil.isNotEmpty( memberEntity.getMcId() ) ) {
		card = memberCardDAO.selectById( memberEntity.getMcId() );
		if ( CommonUtil.isEmpty( card ) ) {
		    throw new BusinessException( ResponseMemberEnums.NOT_MEMBER_CAR );
		}
		uc.setCtId( card.getCtId() );
		uc.setGtId( card.getGtId() );
		uc.setMcId( card.getMcId() );

		for ( PayTypeBo payTypeBo : payTypeBos ) {
		    if ( payTypeBo.getPayType() == 5 ) {
			if ( card.getCtId() == 3 ) {
			    if ( payTypeBo.getJie() == 1 ) {
				//借款消费
				MemberCardLent memberCardLent = memberCardLentDAO.selectById( payTypeBo.getClId() );
				if ( memberCardLent.getLentMoney() > 0 && memberCardLent.getLentMoney() < payTypeBo.getPayMoney() ) {
				    throw new BusinessException( ResponseMemberEnums.LESS_THAN_CARD );
				}

				MemberCardLent memberCardLent1 = new MemberCardLent();
				memberCardLent1.setId( payTypeBo.getClId() );
				memberCardLent1.setUsestate( 1 );
				memberCardLentDAO.updateById( memberCardLent1 );
			    }

			    if ( card.getMoney() < payTypeBo.getPayMoney() ) {
				throw new BusinessException( ResponseMemberEnums.MEMBER_LESS_MONEY );
			    }

			    double banlan = card.getMoney() - payTypeBo.getPayMoney();

			    MemberCard updateCard = new MemberCard();
			    updateCard.setMcId( card.getMcId() );
			    updateCard.setMoney( banlan );
			    memberCardDAO.updateById( updateCard );

			    bool = true;
			    memberCommonService.saveCardRecordOrderCodeNew( memberEntity.getId(), 1, payTypeBo.getPayMoney(), "储值卡消费", memberEntity.getBusId(), banlan,
					    erpPaySuccess.getOrderCode(), 0 );

			    systemMsgService.sendChuzhiXiaofei( memberEntity, payTypeBo.getPayMoney() );
			    uc.setBalance( banlan );
			} else {
			    throw new BusinessException( ResponseMemberEnums.MEMBER_CHUZHI_CARD );
			}
		    }
		}
	    }

	    //粉币使用
	    if ( erpPaySuccess.getUserFenbi() == 1 && CommonUtil.isNotEmpty( memberEntity.getMcId() )&& erpPaySuccess.getFenbiNum()>0 ) {
		Double fenbi = memberEntity.getFansCurrency() - erpPaySuccess.getFenbiNum();
		memberCommonService.reduceFansCurrency( memberEntity, erpPaySuccess.getFenbiNum() );

		memberCommonService.saveCardRecordOrderCodeNew( memberEntity.getId(), 3, erpPaySuccess.getFenbiNum().doubleValue(), "消费粉币", memberEntity.getBusId(), fenbi,
				erpPaySuccess.getOrderCode(), 0 );

	    }
	    //积分使用
	    if ( erpPaySuccess.getUserJifen() == 1 && CommonUtil.isNotEmpty( memberEntity.getMcId() ) && erpPaySuccess.getJifenNum()>0 ) {
		MemberEntity memberEntity1 = new MemberEntity();
		memberEntity1.setId( memberEntity.getId() );
		Integer banlan = memberEntity.getIntegral() - erpPaySuccess.getJifenNum();
		memberEntity1.setIntegral( banlan );
		memberDAO.updateById( memberEntity1 );
		memberCommonService.saveCardRecordOrderCodeNew( memberEntity.getId(), 2, erpPaySuccess.getJifenNum().doubleValue(), "消费积分", memberEntity.getBusId(),
				banlan.doubleValue(), erpPaySuccess.getOrderCode(), 0 );

	    }
	    userConsumeNewDAO.insert( uc );

	    //保存支付记录
	    for ( PayTypeBo payTypeBo : payTypeBos ) {
		UserConsumePay userConsumePay = new UserConsumePay();
		userConsumePay.setPaymentType( payTypeBo.getPayType() );
		userConsumePay.setPayMoney( payTypeBo.getPayMoney() );
		userConsumePay.setUcId( uc.getId() );
		userConsumePayDAO.insert( userConsumePay );
	    }

	    if ( CommonUtil.isNotEmpty( memberEntity.getMcId() ) ) {
		//立即送 TODO
		//findGiveRule( uc.getOrderCode(), "消费会员赠送", (byte) 1 );
	    }

	    if ( !bool ) {
		memberCommonService.saveCardRecordOrderCodeNew( memberEntity.getId(), 1, erpPaySuccess.getDiscountAfterMoney(), "消费", memberEntity.getBusId(), 0.0,
				erpPaySuccess.getOrderCode(), 0 );
	    }

	} catch ( BusinessException e ) {
	    throw e;
	} catch ( Exception e ) {
	    LOG.error( "支付成功回调异常", e );
	    throw new BusinessException( ResponseEnums.ERROR );
	}

    }

    /**
     * erp计算 会员卡核销接口（包括储值卡扣款 、 借款、优惠券核销 、积分、粉币）
     *
     * @param newErpPaySuccessBos
     */
    @Transactional
    public void newPaySuccessShopsByErpBalance( String newErpPaySuccessBos ) throws BusinessException {
	List< NewErpPaySuccessBo > list = JSON.parseArray( newErpPaySuccessBos, NewErpPaySuccessBo.class );
	for ( NewErpPaySuccessBo newErpPaySuccessBo : list ) {
	    newPaySuccessByErpBalance( JSON.toJSONString( newErpPaySuccessBo ) );
	}
    }

    public void refundErp( String erpRefundBo ) throws BusinessException {
	try {
	    ErpRefundBo erfb = JSON.toJavaObject( JSON.parseObject( erpRefundBo ), ErpRefundBo.class );
	    UserConsumeNew uc = userConsumeNewDAO.findByCode( erfb.getBusId(), erfb.getOrderCode() );
	    if ( CommonUtil.isEmpty( uc ) ) {
		throw new BusinessException( ResponseMemberEnums.NOT_ORDER );
	    }
	    if ( !DateTimeKit.laterThanNow( uc.getIsendDate() ) ) {
		throw new BusinessException( ResponseMemberEnums.END_ORDER );
	    }
	    UserConsumeNew updateUc = new UserConsumeNew();
	    updateUc.setId( uc.getId() );
	    Double refundMoney = uc.getRefundMoney() + erfb.getRefundMoney();
	    updateUc.setRefundMoney( refundMoney );

	    Boolean bool = false;
	    MemberEntity member = memberDAO.selectById( uc.getMemberId() );
	    MemberCard card = null;
	    if ( CommonUtil.isNotEmpty( uc.getMcId() ) ) {
		card = memberCardDAO.selectById( uc.getMcId() );
	    }

	    MemberEntity upmember = new MemberEntity();
	    upmember.setId( member.getId() );
	    if ( erfb.getRefundJifen() > 0 ) {
		Integer refundJifen = uc.getRefundJifen() + erfb.getRefundJifen();
		updateUc.setRefundJifen( refundJifen );

		Integer jifen = member.getIntegral() + erfb.getRefundJifen();
		upmember.setIntegral( jifen );

		if ( CommonUtil.isNotEmpty( card ) ) {
		    memberCommonService.saveCardRecordOrderCodeNew( member.getId(), 2, erfb.getRefundJifen().doubleValue(), "退积分", member.getBusId(), jifen.doubleValue(),
				    erfb.getOrderCode(), 1 );
		}
		bool = true;
	    }

	    if ( erfb.getRefundFenbi() > 0 ) {
		Double refundFenbi = uc.getRefundFenbi() + erfb.getRefundFenbi();
		updateUc.setRefundFenbi( refundFenbi );
		memberCommonService.giveFansCurrency( member.getId(), erfb.getRefundFenbi() );
		double fenbi = member.getFansCurrency() + erfb.getRefundFenbi();
		if ( CommonUtil.isNotEmpty( card ) ) {
		    memberCommonService
				    .saveCardRecordOrderCodeNew( member.getId(), 3, erfb.getRefundFenbi().doubleValue(), "退粉币", member.getBusId(), fenbi, erfb.getOrderCode(), 1 );
		}

	    }
	    updateUc.setRefundDate( new Date() );
	    userConsumeNewDAO.updateById( updateUc );

	    if ( erfb.getRefundPayType() == 5 ) {
		//储值卡退款
		if ( CommonUtil.isEmpty( card ) ) {
		    throw new BusinessException( ResponseMemberEnums.MEMBER_NOT_CARD );
		}
		MemberCard mc = new MemberCard();
		mc.setMcId( card.getMcId() );
		Double money = card.getMoney() + erfb.getRefundMoney();
		mc.setMoney( money );
		memberCardDAO.updateById( mc );
		memberCommonService.saveCardRecordOrderCodeNew( member.getId(), 1, erfb.getRefundMoney(), "退款", member.getBusId(), money, erfb.getOrderCode(), 1 );
	    } else {
		memberCommonService.saveCardRecordOrderCodeNew( member.getId(), 1, erfb.getRefundMoney(), "退款", member.getBusId(), 0.0, erfb.getOrderCode(), 1 );
	    }
	    if ( bool ) {
		memberDAO.updateById( upmember );
	    }
	} catch ( BusinessException e ) {
	    throw e;
	} catch ( Exception e ) {
	    throw new BusinessException( ResponseEnums.ERROR );
	}
    }

    /**
     * 商场积分赠送
     *
     * @param memberId
     * @param jifen
     */
    @Override
    public void updateJifen( Integer memberId, Integer jifen ) throws BusinessException {
	MemberEntity memberEntity = memberDAO.selectById( memberId );
	Integer mIntergral = memberEntity.getIntegral();
	MemberEntity memberEntity1 = new MemberEntity();
	memberEntity1.setId( memberEntity.getId() );
	Integer banlan = memberEntity.getIntegral() + jifen;
	memberEntity1.setIntegral( banlan );
	memberDAO.updateById( memberEntity1 );

	if ( CommonUtil.isNotEmpty( memberEntity.getMcId() ) ) {
	    memberCommonService.saveCardRecordOrderCodeNew( memberId, 2, jifen.doubleValue(), "商场积分", memberEntity.getBusId(), banlan.doubleValue(), null, 0 );
	}
    }

    public void jifenExchange( String json ) throws BusinessException {
	try {
	    JSONObject jsonObject = JSONObject.parseObject( json );

	    Integer memberId = jsonObject.getInteger( "memberId" );
	    Integer shopId = jsonObject.getInteger( "shopId" );

	    MemberEntity member = memberDAO.selectById( memberId );
	    if ( CommonUtil.isEmpty( member.getMcId() ) ) {
		throw new BusinessException( ResponseMemberEnums.NOT_MEMBER_CAR );
	    }

	    Integer intergral = jsonObject.getInteger( "intergral" );

	    Integer jifen = member.getIntegral();
	    if ( intergral > jifen ) {
		throw new BusinessException( ResponseMemberEnums.MEMBER_LESS_JIFEN );
	    }

	    MemberEntity mem = new MemberEntity();
	    mem.setId( member.getId() );
	    Integer shenyuJifen = member.getIntegral() - intergral;
	    mem.setIntegral( shenyuJifen );
	    memberDAO.updateById( mem );

	    MemberCard memberCard = memberCardDAO.selectById( member.getMcId() );
	    // 添加会员记录
	    UserConsumeNew uc = new UserConsumeNew();
	    uc.setBusId( member.getBusId() );
	    uc.setMemberId( member.getId() );
	    uc.setMcId( memberCard.getMcId() );
	    uc.setCtId( memberCard.getCtId() );
	    uc.setGtId( memberCard.getGtId() );
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

	    uc.setShopId( shopId );

	    userConsumeNewDAO.insert( uc );

	    UserConsumePay ucPay = new UserConsumePay();
	    ucPay.setUcId( uc.getId() );
	    ucPay.setPaymentType( 11 );
	    ucPay.setPayMoney( 0.0 );
	    userConsumePayDAO.insert( ucPay );

	    memberCommonService.saveCardRecordOrderCodeNew( member.getId(), 2, intergral.doubleValue(), "积分兑换", member.getBusId(), shenyuJifen.doubleValue(), orderCode, 0 );

	} catch ( BusinessException e ) {
	    throw e;
	} catch ( Exception e ) {
	    throw new BusinessException( ResponseEnums.ERROR );
	}
    }


    /**
     * 分页查询会员卡信息
     * @throws BusinessException
     */
    public Page findMemberPage(String json) throws BusinessException{
	try {
	    Map<String,Object> params=JSON.parseObject( json,Map.class );
	    Integer busId=CommonUtil.toInteger( params.get( "busId" ) );
	    params.put( "curPage", CommonUtil.isEmpty( params.get( "curPage" ) ) ? 1 : CommonUtil.toInteger( params.get( "curPage" ) ) );
	    int pageSize = CommonUtil.isEmpty( params.get( "pageSize" ) ) ? 10 : CommonUtil.toInteger( params.get( "pageSize" ) ) ;
	    Object search1 = params.get( "cardNo" );
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

	    int rowCount = memberDAO.countMember( busId, search, phone, ctId, gtId, source, changeCardType, startDate, endDate );
	    Page page = new Page( CommonUtil.toInteger( params.get( "curPage" ) ), pageSize, rowCount, "member/findMember.do" );
	    params.put( "firstResult", pageSize * ( ( page.getCurPage() <= 0 ? 1 : page.getCurPage() ) - 1 ) );
	    params.put( "maxResult", pageSize );

	    List< Map< String,Object > > list = null;
	    List< Map< String,Object > > members = null;
	    if ( CommonUtil.isEmpty( phone ) ) {
		list = memberDAO.findMemberBybusId( Integer.parseInt( params.get( "firstResult" ).toString() ), pageSize, busId, search, ctId, gtId, source, changeCardType,
				startDate, endDate );
		// 采用数据拼接方式
		List< Integer > mcIds = new ArrayList< Integer >();
		for ( Map< String,Object > map : list ) {
		    if ( CommonUtil.isNotEmpty( map.get( "mc_id" ) ) ) {
			mcIds.add( CommonUtil.toInteger( map.get( "mc_id" ) ) );
		    }
		}
		if ( mcIds.size() > 0 ) {
		    members = memberDAO.findMemberBymcIds( busId, mcIds, phone );
		}
	    } else {
		members = memberDAO.findMemberByPhone( busId, phone );
		if ( CommonUtil.isNotEmpty( members ) && members.size() > 0 ) {
		    list = memberDAO.findMemberBybusIdAndPhone( busId, CommonUtil.toInteger( members.get( 0 ).get( "mc_id" ) ) );
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
	    LOG.error( "分页查询会员异常",e );
	    throw new BusinessException( ResponseEnums.ERROR );
	}
    }


    public List<Map<String,Object>> findGradeTypeBybusId(Integer busId){
       return memberGradetypeDAO.findGradeTypeByBusId( busId );
    }


    public Map<String,Object> coutMemberCard(Integer busId,Integer shopId){
        Map<String,Object> map=new HashMap<>(  );
	// 今日新增统计
	String date = DateTimeKit.getDate() + " 00:00:00";

	WxShop wxShop=wxShopDAO.selectMainShopByBusId( busId );
	if((CommonUtil.isNotEmpty( wxShop ) && wxShop.getId().equals( shopId )) || CommonUtil.isEmpty( shopId )){
	    int count = memberCardDAO.countCardByTime(busId, date);
	    map.put( "jinriCount",count );

	    int num = memberCardDAO.countCardisBinding(busId);
	    map.put( "totalCount",num );

	}else{
	    int count = memberCardDAO.countShopIdCardByTime(busId,shopId, date);
	    map.put( "jinriCount",count );

	    int num = memberCardDAO.countShopIdCardisBinding(busId,shopId);
	    map.put( "totalCount",num );
	}

	return map;
    }



}
