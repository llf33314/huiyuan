package com.gt.member.service.common.membercard;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gt.api.bean.session.BusUser;
import com.gt.api.enums.ResponseEnums;
import com.gt.api.util.sign.SignHttpUtils;
import com.gt.common.entity.BusUserEntity;
import com.gt.common.entity.WxPublicUsersEntity;
import com.gt.member.dao.*;
import com.gt.member.dao.common.BusUserDAO;
import com.gt.member.dao.common.WxPublicUsersDAO;
import com.gt.member.entity.*;
import com.gt.member.enums.ResponseMemberEnums;
import com.gt.member.exception.BusinessException;
import com.gt.member.service.common.dict.DictService;
import com.gt.member.service.member.SystemMsgService;
import com.gt.member.util.CommonUtil;
import com.gt.member.util.DateTimeKit;
import com.gt.member.util.PropertiesUtil;
import net.sf.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;

/**
 * Created by Administrator on 2017/8/15 0015.
 */
@Service
public class MemberCommonServiceImp implements MemberCommonService {

    private static final Logger LOG = LoggerFactory.getLogger( MemberCommonServiceImp.class );

    @Autowired
    private DictService dictService;

    @Autowired
    private PublicParametersetDAO publicParameterSetMapper;

    @Autowired
    private MemberGradetypeDAO gradeTypeMapper;

    @Autowired
    private MemberDateDAO memberDateMapper;

    @Autowired
    private BusUserDAO       busUserDAO;
    @Autowired
    private SystemMsgService systemMsgService;

    @Autowired
    private MemberEntityDAO memberEntityDAO;

    @Autowired
    private MemberQcodeWxDAO memberQcodeWxDAO;

    @Autowired
    private PropertiesUtil propertiesUtil;

    @Autowired
    private WxPublicUsersDAO wxPublicUsersDAO;

    @Autowired
    private MemberOldDAO memberOldDao;

    @Autowired
    private MemberParameterDAO memberParameterDAO;

    @Autowired
    private MemberAppletOpenidDAO memberAppletOpenidDAO;

    @Autowired
    private MemberCardrecordNewDAO memberCardrecordNewDAO;

    @Autowired
    private MemberCardDAO memberCardDAO;

    @Autowired
    private MemberRecommendDAO memberRecommendDAO;

    @Autowired
    private MemberRechargegiveDAO memberRechargegiveDAO;

    @Autowired
    private MemberGradetypeAssistantDAO memberGradetypeAssistantDAO;

    @Autowired
    private MemberRechargegiveAssistantDAO memberRechargegiveAssistantDAO;

    @Autowired
    private MemberGiveruleDAO memberGiveruleDAO;

    @Autowired
    private UserConsumeNewDAO userConsumeNewDAO;

    @Autowired
    private MemberGiverulegoodstypeDAO memberGiverulegoodstypeDAO;

    @Autowired
    private MemberGiveconsumeNewDAO memberGiveconsumeNewDAO;

    /**
     * 粉币计算
     *
     * @param totalMoney    能抵抗消费金额
     * @param fans_currency 粉币值
     *
     * @return 返回兑换金额
     */
    @Override
    public Double currencyCount( Double totalMoney, Double fans_currency ) {
	try {
	    SortedMap< String,Object > dict = dictService.getDict( "1058" );
	    Double ratio = CommonUtil.toDouble( dict.get( "1" ) );
	    if ( fans_currency < ratio * 10 ) {
		return 0.0;
	    }
	    Integer money = new Double( fans_currency / ratio / 10 ).intValue();
	    if ( CommonUtil.isEmpty( totalMoney ) || totalMoney == 0 ) {
		return new Double( money * 10 );
	    } else {
		if ( totalMoney >= money * 10 ) {
		    return new Double( money * 10 );
		} else {
		    return totalMoney;
		}
	    }
	} catch ( Exception e ) {
	    LOG.error( "计算粉币抵扣异常" );
	    e.printStackTrace();
	}
	return 0.0;
    }

    @Override
    public Double deductFenbi( Double jifenMoney, int busId ) {
	SortedMap< String,Object > dict = dictService.getDict( "1058" );
	Double ratio = CommonUtil.toDouble( dict.get( "1" ) );
	Double fenbi = jifenMoney * ratio;
	return fenbi;
    }

    @Override
    public Integer deductJifen( Double jifenMoney, int busId ) {
	PublicParameterset pps = publicParameterSetMapper.findBybusId( busId );
	if ( CommonUtil.isEmpty( pps ) ) {
	    return 0;
	}
	Integer jifen = new Double( jifenMoney / pps.getChangeMoney() * pps.getIntegralRatio() ).intValue();
	return jifen;
    }

    /**
     * 积分计算
     *
     * @param totalMoney 能抵抗消费金额
     * @param integral   积分
     *
     * @return
     */
    @Override
    public Double integralCount( Double totalMoney, Double integral, int busId ) {
	try {
	    PublicParameterset ps = publicParameterSetMapper.findBybusId( busId );
	    if ( CommonUtil.isEmpty( ps ) ) {
		return 0.0;
	    }
	    double startMoney = ps.getStartMoney();
	    double integralratio = ps.getIntegralRatio();
	    double changMoney = ps.getChangeMoney();
	    if ( integralratio <= 0 ) {
		return 0.0;
	    }

	    // 积分启兑
	    double integralNum = startMoney * integralratio;
	    if ( integral < integralNum ) {
		return 0.0;
	    }

	    if ( CommonUtil.isNotEmpty( totalMoney ) ) {
		// 订单金额小于订单启兑金额
		if ( totalMoney < changMoney ) {
		    return 0.0;
		}
		Integer money = new Double( integral / integralratio ).intValue();
		if ( totalMoney >= money ) {
		    return new Double( money );
		} else {
		    return totalMoney;
		}
	    } else {
		Integer money = new Double( integral / integralratio ).intValue();
		return new Double( money );
	    }
	} catch ( Exception e ) {
	    LOG.error( "计算积分抵扣异常" );
	    e.printStackTrace();
	}
	return 0.0;
    }

    /**
     * 判断是否是会员日
     */
    public MemberDate findMemeberDate( Integer busId, Integer ctId ) {
	try {
	    MemberGradetype gradeType = gradeTypeMapper.findIsmemberDateByCtId( busId, ctId );
	    if ( CommonUtil.isEmpty( gradeType ) || gradeType.getIsmemberDate() == 1 ) {
		return null;
	    }
	    // 未设置会员日
	    MemberDate memberdate = memberDateMapper.findByBusIdAndCtId( busId, ctId );
	    if ( CommonUtil.isEmpty( memberdate ) ) {
		return null;
	    }
	    switch ( memberdate.getDateType() ) {
		case 0:
		    int d = DateTimeKit.getNow().getDay();
		    Integer day = 0;
		    if ( d == 0 ) {
			day = 7;
		    } else if ( d == 1 ) {
			day = 1;
		    } else if ( d == 2 ) {
			day = 2;
		    } else if ( d == 3 ) {
			day = 3;
		    } else if ( d == 4 ) {
			day = 4;
		    } else if ( d == 5 ) {
			day = 5;
		    } else if ( d == 6 ) {
			day = 6;
		    }
		    if ( day == CommonUtil.toInteger( memberdate.getDateStr() ) ) {
			return memberdate;
		    }
		    break;
		case 1:
		    Integer date = DateTimeKit.getNow().getDate();
		    if ( date == CommonUtil.toInteger( memberdate.getDateStr() ) ) {
			return memberdate;
		    }
		    break;
		case 2:
		    if ( DateTimeKit.isSameDay( new Date(), DateTimeKit.parseDate( memberdate.getDateStr() ) ) ) {
			return memberdate;
		    }
		    break;
		case 3:
		    // 区间
		    String dateStr = memberdate.getDateStr();
		    List< Map< String,Object > > list = JSONArray.toList( JSONArray.fromObject( dateStr ), Map.class );
		    Integer year = DateTimeKit.getYear( new Date() );
		    for ( Map< String,Object > map : list ) {
			String time = CommonUtil.toString( map.get( "time" ) );
			if ( time.length() == 1 ) {
			    time = "0" + time;
			}
			String time1 = CommonUtil.toString( map.get( "time1" ) );
			if ( time1.length() == 1 ) {
			    time1 = "0" + time1;
			}
			String time2 = CommonUtil.toString( map.get( "time2" ) );
			if ( time2.length() == 1 ) {
			    time2 = "0" + time2;
			}
			String time3 = CommonUtil.toString( map.get( "time3" ) );
			if ( time3.length() == 1 ) {
			    time3 = "0" + time3;
			}

			String date1 = year + "-" + time + "-" + time1 + " 00:00:00";
			String date2 = year + "-" + time2 + "-" + time3 + " 23:59:59";
			Date d1 = DateTimeKit.parse( date1, "yyyy-MM-dd HH:mm:ss" );
			Date d2 = DateTimeKit.parse( date2, "yyyy-MM-dd HH:mm:ss" );
			if ( DateTimeKit.isBetween( d1, d2 ) ) {
			    return memberdate;
			}
		    }
		    break;
		default:
		    break;
	    }
	    return null;
	} catch ( Exception e ) {
	    e.printStackTrace();
	}
	return null;
    }

    @Override
    public Double deductJifen( PublicParameterset pps, Double jifenMoney, int busId ) {
	if ( CommonUtil.isEmpty( pps ) ) {
	    return 0.0;
	}
	Double jifen = jifenMoney / pps.getChangeMoney() * pps.getIntegralRatio();
	return jifen;
    }

    /**
     * 金额计算使用粉币数量
     *
     * @return
     */
    public Double deductFenbi( SortedMap< String,Object > dict, Double fenbiMoney ) {
	Double ratio = CommonUtil.toDouble( dict.get( "1" ) );
	Double fenbi = fenbiMoney * ratio;
	return formatNumber( fenbi );
    }

    /**
     * 数字处理
     *
     * @param number
     *
     * @return
     */
    public Double formatNumber( Double number ) {
	DecimalFormat df = new DecimalFormat( "######0.00" );
	return CommonUtil.toDouble( df.format( number ) );
    }

    public void guihuiBusUserFenbi( Integer busId, Double fenbi ) throws BusinessException {
	try {
	    BusUserEntity busUserEntity = busUserDAO.selectById( busId );
	    // 归还到商家账户
	    BigDecimal b1 = new BigDecimal( fenbi );
	    BusUserEntity b = new BusUserEntity();
	    b.setId( busUserEntity.getId() );
	    b.setFansCurrency( busUserEntity.getFansCurrency().add( b1 ) );
	    busUserDAO.updateById( b );
	} catch ( Exception e ) {
	    LOG.error( "归还商家粉币异常参数商家id", e );
	    throw new BusinessException( ResponseEnums.ERROR );
	}
    }

    public void saveCardRecordOrderCodeNew( Integer memberId, Integer recordType, Double number, String itemName, Integer busId, Double balance, String orderCode, Integer rtype ) {
	MemberCardrecordNew cr = new MemberCardrecordNew();
	cr.setMemberId( memberId );
	cr.setRecordType( recordType );
	cr.setNumber( number );
	cr.setCreateDate( new Date() );
	cr.setItemName( itemName );
	cr.setBusId( busId );
	cr.setBalance( balance );
	cr.setOrderCode( orderCode );
	try {
	    memberCardrecordNewDAO.insert( cr );
	    if ( recordType == 2 ) {
		MemberEntity memberEntity = memberEntityDAO.selectById( memberId );
		// 积分变动通知
		systemMsgService.jifenMsg( cr, memberEntity );
	    }

	} catch ( Exception e ) {
	    e.printStackTrace();
	    LOG.error( "保存手机端记录异常", e );
	}
    }

    public String findWxQcode( Integer busId, Integer busType, String scene_id ) {
	try {
	    MemberQcodeWx mqw = memberQcodeWxDAO.findByBusId( busId, 0 );
	    String imgUrl = "";
	    if ( CommonUtil.isEmpty( mqw ) ) {
		WxPublicUsersEntity wxPublicUsersEntity = wxPublicUsersDAO.selectByUserId( busId );
		Map< String,Object > querymap = new HashMap<>();
		querymap.put( "scene_id", scene_id );
		querymap.put( "publicId", wxPublicUsersEntity.getId() );
		String url = propertiesUtil.getWxmp_home() + "/8A5DA52E/wxpublicapi/6F6D9AD2/79B4DE7C/qrcodeCreateFinal.do";
		String json = SignHttpUtils.WxmppostByHttp( url, querymap, propertiesUtil.getWxmpsignKey() );

		JSONObject obj = JSONObject.parseObject( json );

		imgUrl = obj.getString( "imgUrl" );
		if ( CommonUtil.isNotEmpty( imgUrl ) ) {
		    mqw = new MemberQcodeWx();
		    mqw.setBusId( busId );
		    mqw.setBusType( busType );
		    mqw.setCodeUrl( imgUrl );
		    memberQcodeWxDAO.insert( mqw );
		}
	    } else {
		imgUrl = mqw.getCodeUrl();
	    }
	    return imgUrl;
	} catch ( Exception e ) {
	    throw new BusinessException( ResponseEnums.ERROR );
	}
    }

    /**
     * 新增会员处理数据合并问题
     *
     * @param busId
     * @param phone
     */
    public void newMemberMerge( MemberEntity memberEntity, Integer busId, String phone ) throws BusinessException {
	if ( CommonUtil.isNotEmpty( memberEntity ) && CommonUtil.isNotEmpty( memberEntity.getMcId() ) ) {
	    throw new BusinessException( ResponseMemberEnums.IS_MEMBER_CARD );
	}

	MemberEntity m1 = memberEntityDAO.findByPhone( busId, phone );
	if ( CommonUtil.isNotEmpty( m1 ) && !memberEntity.getId().equals( m1.getId() ) ) {
	    // 合并member数据
	    m1.setFlow( m1.getFlow() + memberEntity.getFlow() );
	    m1.setIntegral( m1.getIntegral() + memberEntity.getIntegral() );
	    m1.setFansCurrency( m1.getFansCurrency() + memberEntity.getFansCurrency() );
	    if ( CommonUtil.isNotEmpty( memberEntity.getPwd() ) ) {
		m1.setPwd( memberEntity.getPwd() );
	    }

	    if ( CommonUtil.isNotEmpty( m1.getOldId() ) ) {
		m1.setOldId( m1.getOldId() + "," + memberEntity.getId() );
	    } else {
		m1.setOldId( m1.getId() + "," + memberEntity.getId() );
	    }

	    if ( CommonUtil.isNotEmpty( memberEntity.getOpenid() ) && CommonUtil.isEmpty( m1.getOpenid() ) ) {
		m1.setOpenid( memberEntity.getOpenid() );
	    }

	    m1.setPhone( phone );
	    m1.setMcId( memberEntity.getMcId() );
	    m1.setNickname( memberEntity.getNickname() );
	    m1.setHeadimgurl( memberEntity.getHeadimgurl() );
	    m1.setTotalMoney( memberEntity.getTotalMoney() + m1.getTotalMoney() );
	    m1.setTotalIntegral( memberEntity.getTotalIntegral() + m1.getTotalIntegral() );
	    m1.setRemark( memberEntity.getRemark() );
	    m1.setLoginMode( 0 );
	    MemberOld old = JSONObject.toJavaObject( JSON.parseObject( JSONObject.toJSONString( memberEntity ) ), MemberOld.class );

	    // 删除数据做移出到memberold
	    memberOldDao.insert( old );

	    memberEntityDAO.deleteById( memberEntity.getId() );

	    memberEntityDAO.updateById( m1 );

	    MemberParameter mp = memberParameterDAO.findByMemberId( memberEntity.getId() );
	    if ( CommonUtil.isNotEmpty( mp ) ) {
		memberParameterDAO.deleteById( mp.getId() );
	    }
	    // 修改小程序之前openId对应的memberId
	    memberAppletOpenidDAO.updateMemberId( m1.getId(), memberEntity.getId() );
	}

	if ( CommonUtil.isNotEmpty( m1 ) && CommonUtil.isNotEmpty( m1.getMcId() ) ) {
	    throw new BusinessException( ResponseMemberEnums.IS_MEMBER_CARD );
	}
    }

    @Override
    public void reduceFansCurrency( MemberEntity memberEntity, Double fenbi ) throws BusinessException {
	try {
	    if ( memberEntity.getFansCurrency() < fenbi ) {
		throw new BusinessException( ResponseMemberEnums.MEMBER_LESS_FENBI.getCode(), ResponseMemberEnums.MEMBER_LESS_FENBI.getMsg() );
	    }
	    MemberEntity m = new MemberEntity();
	    m.setId( memberEntity.getId() );
	    Double yueFenbi = memberEntity.getFansCurrency() - fenbi;
	    m.setFansCurrency( yueFenbi );
	    memberEntityDAO.updateById( m );

	    BusUserEntity busUserEntity = busUserDAO.selectById( memberEntity.getBusId() );
	    BusUserEntity busUserEntity1 = new BusUserEntity();
	    busUserEntity1.setId( memberEntity.getBusId() );
	    Double fenbi1 = busUserEntity.getFansCurrency().doubleValue() + fenbi;
	    busUserEntity1.setFansCurrency( BigDecimal.valueOf( fenbi1 ) );
	    busUserDAO.updateById( busUserEntity1 );
	} catch ( BusinessException e ) {
	    LOG.error( "粉币抵扣异常", e );
	    throw new BusinessException( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    throw new BusinessException( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getMsg() );
	}
    }

    /*
   * 粉币赠送
   * @param request
   * @param memberEntity
   * @param busId
   * @param Fenbi 粉币
   * @return
   */
    public void giveFansCurrency( Integer memberId, Double fenbi ) throws BusinessException {
	try {
	    MemberEntity m = memberEntityDAO.selectById( memberId );

	    BusUserEntity busUserEntity = busUserDAO.selectById( m.getBusId() );
	    if ( busUserEntity.getFansCurrency().doubleValue() < fenbi ) {
		LOG.error( "商家粉币不足" );
		throw new BusinessException( ResponseMemberEnums.LESS_THAN_FENBI );
	    }

	    BusUserEntity busUserEntity1 = new BusUserEntity();
	    busUserEntity1.setId( m.getBusId() );
	    Double fenbi1 = busUserEntity.getFansCurrency().doubleValue() - fenbi;
	    busUserEntity1.setFansCurrency( BigDecimal.valueOf( fenbi1 ) );
	    busUserDAO.updateById( busUserEntity1 );

	    MemberEntity updateMember = new MemberEntity();
	    updateMember.setId( memberId );
	    Double yueFenbi = m.getFansCurrency() + fenbi;
	    updateMember.setFansCurrency( yueFenbi );
	    memberEntityDAO.updateById( updateMember );
	} catch ( BusinessException e ) {
	    LOG.error( "商家赠送粉币", e );
	    throw new BusinessException( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    throw new BusinessException( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getMsg() );
	}
    }

    public boolean deductionFansCurrency( Integer busId, Double fenbi ) {
	BusUserEntity busUserEntity = busUserDAO.selectById( busId );
	if ( busUserEntity.getFansCurrency().doubleValue() < fenbi ) {
	    LOG.error( "商家粉币不足" );
	    return false;
	}

	BusUserEntity busUserEntity1 = new BusUserEntity();
	busUserEntity1.setId( busId );
	Double fenbi1 = busUserEntity.getFansCurrency().doubleValue() - fenbi;
	busUserEntity1.setFansCurrency( BigDecimal.valueOf( fenbi1 ) );
	busUserDAO.updateById( busUserEntity1 );
	return true;

    }

    /**
     * 多粉卡券核销 推荐调用
     *
     * @param recommend
     *
     * @throws Exception
     */
    public void tuijianGive( MemberRecommend recommend ) {
	boolean flag = false;

	//赠送积分、粉币、流量、金额
	MemberEntity tuijianMemberEntity = memberEntityDAO.selectById( recommend.getMemberId() );
	MemberEntity m1 = new MemberEntity();
	m1.setId( recommend.getMemberId() );
	if ( recommend.getIntegral() > 0 ) {
	    //积分记录
	    Integer balance = tuijianMemberEntity.getIntegral() + recommend.getIntegral();
	    m1.setIntegral( balance );
	    saveCardRecordOrderCodeNew( tuijianMemberEntity.getId(), 2, recommend.getIntegral().doubleValue(), "推荐优惠券赠送", tuijianMemberEntity.getBusId(), balance.doubleValue(), "",
			    1 );
	    flag = true;
	}
	if ( recommend.getFenbi() > 0 ) {

	    BusUserEntity busUserEntity = busUserDAO.selectById( tuijianMemberEntity.getBusId() );
	    if ( busUserEntity.getFansCurrency().doubleValue() >= recommend.getFenbi() ) {

		BusUserEntity b = new BusUserEntity();
		b.setId( busUserEntity.getId() );
		Double fenbi1 = busUserEntity.getFansCurrency().doubleValue() - recommend.getFenbi();
		b.setFansCurrency( BigDecimal.valueOf( fenbi1 ) );
		busUserDAO.updateById( b );

		//粉币记录
		Double balance = tuijianMemberEntity.getFansCurrency() + recommend.getFenbi();
		m1.setFansCurrency( balance );

		saveCardRecordOrderCodeNew( tuijianMemberEntity.getId(), 3, recommend.getFenbi().doubleValue(), "推荐优惠券赠送", tuijianMemberEntity.getBusId(), balance.doubleValue(),
				"", 1 );

		flag = true;
	    }
	}

	if ( recommend.getFlow() > 0 ) {
	    Integer balance = tuijianMemberEntity.getFlow() + recommend.getFlow();
	    m1.setFlow( balance );
	    //流量记录
	    saveCardRecordOrderCodeNew( tuijianMemberEntity.getId(), 4, recommend.getFlow().doubleValue(), "推荐优惠券赠送", tuijianMemberEntity.getBusId(), balance.doubleValue(), "",
			    1 );

	    flag = true;
	}
	if ( flag ) {
	    memberEntityDAO.updateById( m1 );
	}

	if ( recommend.getMoney() > 0 && CommonUtil.isNotEmpty( tuijianMemberEntity.getMcId() ) ) {
	    MemberCard card = memberCardDAO.selectById( tuijianMemberEntity.getMcId() );
	    MemberCard c = new MemberCard();
	    c.setMcId( card.getMcId() );
	    Double balance = card.getGiveMoney() + recommend.getMoney();
	    c.setGiveMoney( balance );
	    memberCardDAO.updateById( c );
	    //
	    saveCardRecordOrderCodeNew( tuijianMemberEntity.getId(), 1, recommend.getMoney().doubleValue(), "推荐优惠券赠送", tuijianMemberEntity.getBusId(), balance.doubleValue(), "",
			    1 );
	}

	MemberRecommend r = new MemberRecommend();
	r.setId( recommend.getId() );
	r.setUserNum( recommend.getUserNum() + 1 );
	memberRecommendDAO.updateById( r );
    }

    @Override
    public List< Integer > findMemberIds( Integer memberId ) {
	List< Integer > list = new ArrayList< Integer >();
	MemberEntity member = memberEntityDAO.selectById( memberId );
	if ( CommonUtil.isEmpty( member.getOldId() ) ) {
	    list.add( memberId );
	    return list;
	}
	String[] str = member.getOldId().split( "," );
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

    public MemberRechargegive findRechargegive( double price, Integer grId, Integer busId, Integer ctId ) throws BusinessException {
	MemberDate memberdate = findMemeberDate( busId, ctId );
	List< MemberRechargegive > rechargeGives = null;
	if ( CommonUtil.isNotEmpty( memberdate ) ) {
	    rechargeGives = memberRechargegiveDAO.findBybusIdAndGrId( busId, grId, 1 );
	} else {
	    rechargeGives = memberRechargegiveDAO.findBybusIdAndGrId( busId, grId, 0 );
	}
	if ( rechargeGives == null || rechargeGives.size() == 0 ) {
	    return null;
	}
	for ( int i = 0; i < rechargeGives.size(); i++ ) {
	    if ( i + 1 == rechargeGives.size() ) {
		double money = rechargeGives.get( i ).getMoney();
		if ( money <= price ) {
		    return rechargeGives.get( i );
		} else {
		    return null;
		}
	    }
	    if ( CommonUtil.isNotEmpty( rechargeGives.get( i ).getMoney() ) ) {
		double money = rechargeGives.get( i ).getMoney();
		double money1 = rechargeGives.get( i + 1 ).getMoney();
		if ( price >= money && price < money1 ) {
		    return rechargeGives.get( i );
		}
	    }

	}
	return null;
    }

    public List< Integer > findTimeCard( Double money, Integer busId ) throws BusinessException {
	List< Map< String,Object > > giveRules = memberGiveruleDAO.findByBusIdAndCtId( busId, 4 );
	MemberDate memberDate = findMemeberDate( busId, 4 );
	List< Integer > times = new ArrayList< Integer >();
	if ( giveRules.size() == 0 ) {
	    throw new BusinessException( ResponseMemberEnums.NOT_TIME_CARD );
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
	throw new BusinessException( ResponseMemberEnums.NOT_TIME_CARD );
    }

    public MemberRechargegiveAssistant findAssistantrechargegive( double price, Integer gtId, Integer busId, Integer fuctId ) throws BusinessException {
	List< MemberRechargegiveAssistant > rechargeGives = memberRechargegiveAssistantDAO.findByBusIdAndGtId( busId, gtId );
	if ( rechargeGives == null || rechargeGives.size() == 0 ) {
	    return null;
	}
	for ( int i = 0; i < rechargeGives.size(); i++ ) {
	    if ( i + 1 == rechargeGives.size() ) {
		double money = rechargeGives.get( i ).getMoney();
		if ( money <= price ) {
		    return rechargeGives.get( i );
		} else {
		    return null;
		}
	    }
	    if ( CommonUtil.isNotEmpty( rechargeGives.get( i ).getMoney() ) ) {
		double money = rechargeGives.get( i ).getMoney();
		double money1 = rechargeGives.get( i + 1 ).getMoney();
		if ( price >= money && price < money1 ) {
		    return rechargeGives.get( i );
		}
	    }

	}
	return null;
    }

    public void findGiveRuleDelay( String orderNo ) {
	try {
	    UserConsumeNew ucs = userConsumeNewDAO.findOneByCode( orderNo );
	    Integer busId = ucs.getBusId();
	    Integer gtId = ucs.getGtId();
	    Integer ctId = ucs.getCtId();
	    double price = ucs.getDiscountAfterMoney();
	    // 判断是否是会员日
	    MemberDate memberDate = findMemeberDate( busId, ctId );
	    boolean flag = false; // 表示今天是否是会员日
	    if ( CommonUtil.isNotEmpty( memberDate ) ) {
		flag = true;
	    }

	    List< MemberGiverulegoodstype > grgts = memberGiverulegoodstypeDAO.findGriveGoodByBusIdAndCtId( busId, ctId );
	    MemberGiveconsumeNew giveConsume = null;

	    for ( MemberGiverulegoodstype grgt : grgts ) {
		giveConsume = new MemberGiveconsumeNew();
		if ( 1 == grgt.getGtId() ) {
		    if ( 1 == grgt.getGiveType() ) {
			// 积分
			Double money = grgt.getMoney();
			int count = (int) Math.floor( price / money );
			if ( count == 0 ) continue;
			int num = count * grgt.getNumber();
			Integer upperLmit = grgt.getUpperLmit();
			if ( upperLmit != 0 ) {
			    num = num > upperLmit ? upperLmit : num;
			}
			// 会员日赠送翻倍
			if ( flag ) {
			    num = num * memberDate.getIntegral();
			}
			// 添加赠送物品记录
			giveConsume.setGcTotal( num );
			giveConsume.setGtId( 1 );
			giveConsume.setGtName( "积分" );
			giveConsume.setGtUnit( "个" );
			giveConsume.setUcId( ucs.getId() );
			giveConsume.setMemberId( ucs.getMemberId() );
			giveConsume.setSendType( 0 );
			giveConsume.setSendDate( new Date() );
			memberGiveconsumeNewDAO.insert( giveConsume );
		    }
		} else {
		    Integer count = grgt.getNumber();
		    Double money = grgt.getMoney();
		    if ( price < money ) {
			continue;
		    }
		    // 扣除商家粉币数量
		    if ( "3".equals( grgt.getGtId() ) ) {
			// 会员日赠送翻倍
			if ( flag ) {
			    count = count * memberDate.getFansCurrency();
			}
			giveConsume.setGcTotal( count );
			giveConsume.setGtId( 3 );
			giveConsume.setGtName( "粉币" );
			giveConsume.setGtUnit( "个" );
			giveConsume.setUcId( ucs.getId() );
			giveConsume.setMemberId( ucs.getMemberId() );
			giveConsume.setSendType( 0 );
			giveConsume.setSendDate( new Date() );
			memberGiveconsumeNewDAO.insert( giveConsume );
		    } else if ( "2".equals( grgt.getGtId() ) ) {
			Integer flowCount = grgt.getNumber();
			if ( flag ) {
			    flowCount = flowCount * memberDate.getFlow();
			}

			giveConsume.setGcTotal( flowCount );
			giveConsume.setGtId( 2 );
			giveConsume.setGtName( "流量" );
			giveConsume.setGtUnit( "MB" );
			giveConsume.setUcId( ucs.getId() );
			giveConsume.setMemberId( ucs.getMemberId() );
			giveConsume.setSendType( 0 );
			giveConsume.setSendDate( new Date() );
			memberGiveconsumeNewDAO.insert( giveConsume );
		    }
		}
	    }
	} catch ( Exception e ) {
	    e.printStackTrace();
	    LOG.error( "延迟送赠送物品异常", e );
	}
    }

    /**
     * 根据订单号添加赠送物品记录
     *
     * @param orderCode 订单号
     *
     * @throws Exception
     */
    public void findGiveRule( String orderCode ) {

	try {
	    UserConsumeNew ucs = userConsumeNewDAO.findOneByCode( orderCode );
	    Integer busId = ucs.getBusId();
	    Integer gtId = ucs.getGtId();
	    Integer ctId = ucs.getCtId();
	    double price = ucs.getDiscountAfterMoney();
	    BusUserEntity busUser = busUserDAO.selectById( busId );
	    BigDecimal fenbi = busUser.getFansCurrency();

	    Double fans_currency = 0.0;// 粉笔
	    Integer integral = 0; // 积分
	    Integer flow = 0;

	    MemberDate memberday = findMemeberDate( busId, ctId );
	    boolean flag = false; // 表示今天是否是会员日
	    if ( CommonUtil.isNotEmpty( memberday ) ) {
		flag = true;
	    }

	    List< MemberGiverulegoodstype > grgts = memberGiverulegoodstypeDAO.findGriveGoodByBusIdAndCtId( busId, ctId );
	    MemberGiveconsumeNew giveConsume = null;

	    for ( MemberGiverulegoodstype grgt : grgts ) {
		giveConsume = new MemberGiveconsumeNew();
		if ( "1".equals( grgt.getGtId() ) ) {
		    if ( "1".equals( grgt.getGiveType() ) ) {
			// 积分
			Double money = grgt.getMoney();
			int count = (int) Math.floor( price / money );
			int num = count * grgt.getNumber();
			Integer upperLmit = grgt.getUpperLmit();
			if ( upperLmit != 0 ) {
			    num = num > upperLmit ? upperLmit : num;
			}
			// 会员日 积分赠送
			if ( flag ) {
			    num = num * memberday.getIntegral();
			}
			giveConsume.setGcTotal( num );
			giveConsume.setGtId( 1 );
			giveConsume.setGtName( "积分" );
			giveConsume.setGtUnit( "个" );
			giveConsume.setUcId( ucs.getId() );
			giveConsume.setMemberId( ucs.getMemberId() );
			giveConsume.setSendDate( new Date() );
			giveConsume.setSendType( 1 );
			memberGiveconsumeNewDAO.insert( giveConsume );
			integral = num;
		    }
		} else {
		    // 添加赠送物品记录
		    Integer count = grgt.getNumber();
		    Double money = grgt.getMoney();
		    if ( price < money ) continue;
		    // 扣除商家粉币数量
		    if ( "3".equals( grgt.getGtId() ) ) {
			// 会员日 粉币赠送
			if ( flag ) {
			    count = count * memberday.getFansCurrency();
			}
			if ( !CommonUtil.compareTo( fenbi, count ) ) {
			    continue;
			}

			// 扣除商家粉笔数量
			boolean bool = deductionFansCurrency( busId, count.doubleValue() );
			if ( bool == true ) {
			    fans_currency = (double) count;
			    giveConsume.setGcTotal( count );
			    giveConsume.setGtId( 3 );
			    giveConsume.setGtName( "粉币" );
			    giveConsume.setGtUnit( "个" );
			    giveConsume.setUcId( ucs.getId() );
			    giveConsume.setMemberId( ucs.getMemberId() );
			    giveConsume.setSendDate( new Date() );
			    memberGiveconsumeNewDAO.insert( giveConsume );
			}
		    } else if ( "2".equals( grgt.getGtId() ) ) {
			Integer flowCount = grgt.getNumber();
			// 会员日赠送流量
			if ( flag ) {
			    flowCount = flowCount * memberday.getFlow();

			    flow = flowCount * memberday.getFlow();
			} else {
			    flow = flowCount;
			}
			giveConsume.setGcTotal( flowCount );
			giveConsume.setGtId( 2 );
			giveConsume.setGtName( "流量" );
			giveConsume.setGtUnit( "MB" );
			giveConsume.setUcId( ucs.getId() );
			giveConsume.setMemberId( ucs.getMemberId() );
			giveConsume.setSendDate( new Date() );
			memberGiveconsumeNewDAO.insert( giveConsume );
		    }
		}
	    }

	    MemberCard card = memberCardDAO.selectById( ucs.getMcId() );

	    // 修改会员的流量 粉笔 积分信息
	    MemberEntity member1 = memberEntityDAO.selectById( ucs.getMemberId() );

	    if ( CommonUtil.isNotEmpty( member1 ) ) {
		MemberEntity member = new MemberEntity();
		member.setId( member1.getId() );
		member.setFansCurrency( member1.getFansCurrency() + fans_currency );
		member.setFlow( member1.getFlow() + flow );
		member.setIntegral( member1.getIntegral() + integral );
		member.setTotalIntegral( member1.getTotalIntegral() + integral );
		if ( CommonUtil.isNotEmpty( ucs.getDiscountAfterMoney() ) ) {
		    price = ucs.getDiscountAfterMoney();
		}
		if ( ctId == 5 ) {
		    price = ucs.getUccount();
		}
		if(card.getCtId()==3 && ucs.getRecordType()==1) {
		    member.setTotalMoney( member1.getTotalMoney() + price );  //储值卡 金额升级只在充值的时候升级
		}else if(card.getCtId()!=3){
		    member.setTotalMoney( member1.getTotalMoney() + price );  //其他卡 在消费的时候升级
		}

		memberEntityDAO.updateById( member );
		//添加积分、粉币、流量赠送记录
		if(integral>0){
		    saveCardRecordOrderCodeNew( member.getId(), 2, integral.doubleValue(), "赠送积分", member1.getBusId(), member.getIntegral().doubleValue(), ucs.getOrderCode(),
				    1 );
		}
		if(fans_currency>0){
		    saveCardRecordOrderCodeNew( member.getId(), 3, fans_currency, "赠送粉币", member1.getBusId(), member.getFansCurrency(), ucs.getOrderCode(),
				    1 );
		}
		if(flow>0){
		    saveCardRecordOrderCodeNew( member.getId(), 4, flow.doubleValue(), "赠送流量", member1.getBusId(), member.getFlow().doubleValue(), ucs.getOrderCode(),
				    1 );
		}


	    }
	    Map< String,Object > map = null;
	    // 判断时效卡升级
	    if ( ctId == 4 ) {
		map = findNextGradeCtId4( busId, gtId, price );
	    } else if ( card.getApplyType() != 4 ) { // 泛会员升级
		// 判断会员是否是要升级
		map = findNextGrade( busId, ctId, gtId, member1.getTotalIntegral(), member1.getTotalMoney() );
	    }

	    double balance = 0.0;
	    if ( CommonUtil.isNotEmpty( card ) && CommonUtil.isNotEmpty( card.getMoney() ) ) {
		// 修改会员卡等级和赠送规则
		if ( CommonUtil.isNotEmpty( map ) ) {
		    card.setGtId( Integer.parseInt( map.get( "gtId" ).toString() ) );
		    card.setGrId( Integer.parseInt( map.get( "grId" ).toString() ) );

		    // 升级通知
		    systemMsgService.upgradeMemberMsg( member1, card.getCardNo(),
				    CommonUtil.isEmpty( card.getExpireDate() ) ? "长期有效" : DateTimeKit.format( card.getExpireDate() ) );
		}
		memberCardDAO.updateById( card );
	    }

	} catch ( Exception e ) {
	    LOG.error( "添加赠送记录数据查询异常异常", e );
	    //  throw new Exception();
	}
    }

    /**
     * 判断时效卡升级
     *
     * @return
     */
    public Map< String,Object > findNextGradeCtId4( Integer busId, Integer gtId, Double totalmoney ) {
	Map< String,Object > map = new HashMap< String,Object >();
	List< Map< String,Object > > giveRules = memberGiveruleDAO.findByBusIdAndCtId( busId, 4 );
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
     * 非时效卡判断是否升级
     *
     * @return
     */
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
				MemberGiverule nextGiveRule = memberGiveruleDAO.findBybusIdAndGtIdAndCtId( busId, id, Integer.parseInt( ctId.toString() ) );
				if ( CommonUtil.isEmpty( nextGiveRule ) ) {
				    break;
				}
				// 积分升级
				if ( 0 == nextGiveRule.getGrUpgradeType() ) {
				    if ( integral >= nextGiveRule.getGrUpgradeCount() ) {
					map.put( "gtId", id );
					map.put( "grId", nextGiveRule.getGrId() );
					return map;
				    }
				}

				// 金额升级
				if ( 1 == nextGiveRule.getGrUpgradeType() ) {
				    if ( totalmoney >= nextGiveRule.getGrUpgradeCount() ) {
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
	    return null;
	}
	return null;
    }

}
