package com.gt.member.service.common.membercard;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
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

    private static final Logger LOG= LoggerFactory.getLogger(MemberCommonServiceImp.class  );

    @Autowired
    private DictService dictService;

    @Autowired
    private PublicParametersetDAO publicParameterSetMapper;

    @Autowired
    private MemberGradetypeDAO gradeTypeMapper;

    @Autowired
    private MemberDateDAO memberDateMapper;

    @Autowired
    private  BusUserDAO busUserDAO;

    @Autowired
    private MemberCardrecordDAO memberCardrecordDAO;

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
	    SortedMap<String, Object> dict= dictService.getDict( "1058" );
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
	SortedMap<String, Object> dict= dictService.getDict( "1058" );
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
    public Double deductJifen( PublicParameterset pps ,Double jifenMoney, int busId ) {
	if ( CommonUtil.isEmpty( pps ) ) {
	    return 0.0;
	}
	Double jifen = jifenMoney / pps.getChangeMoney() * pps.getIntegralRatio() ;
	return jifen;
    }

    /**
     * 金额计算使用粉币数量
     *
     * @return
     */
    public Double deductFenbi(SortedMap<String, Object> dict, Double fenbiMoney) {
	Double ratio = CommonUtil.toDouble(dict.get("1"));
	Double fenbi = fenbiMoney * ratio;
	return formatNumber(fenbi);
    }

    /**
     * 数字处理
     *
     * @param number
     * @return
     */
    public Double formatNumber(Double number) {
	DecimalFormat df = new DecimalFormat("######0.00");
	return CommonUtil.toDouble(df.format(number));
    }

    public void guihuiBusUserFenbi(Integer busId,Double fenbi)throws BusinessException{
	try {
	    BusUserEntity busUserEntity = busUserDAO.selectById( busId );
	    // 归还到商家账户
	    BigDecimal b1 = new BigDecimal( fenbi );
	    BusUserEntity b = new BusUserEntity();
	    b.setId( busUserEntity.getId() );
	    b.setFansCurrency( busUserEntity.getFansCurrency().add( b1 ) );
	    busUserDAO.updateById( b );
	}catch ( Exception e ){
	    LOG.error( "归还商家粉币异常参数商家id",e );
	    throw new BusinessException( ResponseEnums.ERROR );
	}
    }





    public void saveCardRecordOrderCodeNew(Integer memberId, Integer recordType, Double number,
		    String itemName, Integer busId, Double balance, String orderCode,Integer rtype){
	MemberCardrecordNew cr = new MemberCardrecordNew();
	cr.setMemberId(memberId);
	cr.setRecordType(recordType);
	cr.setNumber(number);
	cr.setCreateDate(new Date());
	cr.setItemName(itemName);
	cr.setBusId(busId);
	cr.setBalance(balance);
	cr.setOrderCode( orderCode );
	try {
	    memberCardrecordNewDAO.insert(cr);
	    if (recordType == 2) {
		MemberEntity memberEntity = memberEntityDAO.selectById( memberId );
		// 积分变动通知
		systemMsgService.jifenMsg(cr, memberEntity );
	    }

	} catch (Exception e) {
	    e.printStackTrace();
	    LOG.error("保存手机端记录异常", e);
	}
    }


    public String findWxQcode(Integer busId,Integer busType,String scene_id){
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
	}catch ( Exception e ){
	    throw  new BusinessException( ResponseEnums.ERROR);
	}
    }


    /**
     * 新增会员处理数据合并问题
     * @param busId
     * @param phone
     */
    public void newMemberMerge(MemberEntity memberEntity,Integer busId,String phone)throws BusinessException{
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
	    MemberOld old =  JSONObject.toJavaObject( JSON.parseObject( JSONObject.toJSONString( memberEntity ) ), MemberOld.class  );

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

	if (CommonUtil.isNotEmpty( m1 ) &&  CommonUtil.isNotEmpty( m1.getMcId() ) ) {
	    throw new BusinessException( ResponseMemberEnums.IS_MEMBER_CARD );
	}
    }


    @Override
    public void reduceFansCurrency( MemberEntity memberEntity,  Double fenbi) throws BusinessException {
	try {
	    if ( memberEntity.getFansCurrency() < fenbi ) {
		throw new BusinessException( ResponseMemberEnums.MEMBER_LESS_FENBI.getCode(), ResponseMemberEnums.MEMBER_LESS_FENBI.getMsg() );
	    }
	    MemberEntity m = new MemberEntity();
	    m.setId( memberEntity.getId() );
	    Double yueFenbi=memberEntity.getFansCurrency() - fenbi;
	    m.setFansCurrency(yueFenbi);
	    memberEntityDAO.updateById( m );


	    BusUserEntity busUserEntity = busUserDAO.selectById( memberEntity.getBusId() );
	    BusUserEntity busUserEntity1 = new BusUserEntity();
	    busUserEntity1.setId(  memberEntity.getBusId() );
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
    public void giveFansCurrency( Integer memberId,  Double fenbi ) throws BusinessException{
	try {
	    MemberEntity m = memberEntityDAO.selectById(  memberId);


	    BusUserEntity busUserEntity = busUserDAO.selectById( m.getBusId() );
	    if(busUserEntity.getFansCurrency().doubleValue()<fenbi){
	       LOG.error( "商家粉币不足" );
	       return;
	    }

	    BusUserEntity busUserEntity1 = new BusUserEntity();
	    busUserEntity1.setId(  m.getBusId() );
	    Double fenbi1 = busUserEntity.getFansCurrency().doubleValue() - fenbi;
	    busUserEntity1.setFansCurrency( BigDecimal.valueOf( fenbi1 ) );
	    busUserDAO.updateById( busUserEntity1 );


	    MemberEntity updateMember = new MemberEntity();
	    updateMember.setId( memberId );
	    Double yueFenbi=m.getFansCurrency() + fenbi;
	    updateMember.setFansCurrency(yueFenbi);
	    memberEntityDAO.updateById( m );
	} catch ( BusinessException e ) {
	    LOG.error( "商家赠送粉币", e );
	    throw new BusinessException( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    throw new BusinessException( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getMsg() );
	}
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
	    Integer balance=tuijianMemberEntity.getIntegral() + recommend.getIntegral();
	    m1.setIntegral( balance );
	    saveCardRecordOrderCodeNew( tuijianMemberEntity.getId(),2,recommend.getIntegral().doubleValue(), "推荐优惠券赠送",tuijianMemberEntity.getBusId(),balance.doubleValue(),"",1);
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
		Double balance=tuijianMemberEntity.getFansCurrency() + recommend.getFenbi();
		m1.setFansCurrency(balance);

		saveCardRecordOrderCodeNew( tuijianMemberEntity.getId(),3,recommend.getFenbi().doubleValue(), "推荐优惠券赠送",tuijianMemberEntity.getBusId(),balance.doubleValue(),"",1);

		flag = true;
	    }
	}

	if ( recommend.getFlow() > 0 ) {
	    Integer balance=tuijianMemberEntity.getFlow() + recommend.getFlow();
	    m1.setFlow( balance );
	    //流量记录
	    saveCardRecordOrderCodeNew( tuijianMemberEntity.getId(),4,recommend.getFlow().doubleValue(), "推荐优惠券赠送",tuijianMemberEntity.getBusId(),balance.doubleValue(),"",1);


	    flag = true;
	}
	if ( flag ) {
	    memberEntityDAO.updateById( m1 );
	}

	if ( recommend.getMoney() > 0  && CommonUtil.isNotEmpty( tuijianMemberEntity.getMcId() )) {
	    MemberCard card = memberCardDAO.selectById( tuijianMemberEntity.getMcId() );
	    MemberCard c = new MemberCard();
	    c.setMcId( card.getMcId() );
	    Double balance=card.getGiveMoney() + recommend.getMoney();
	    c.setGiveMoney( balance );
	    memberCardDAO.updateById( c );
	    //
	    saveCardRecordOrderCodeNew( tuijianMemberEntity.getId(),1,recommend.getMoney().doubleValue(), "推荐优惠券赠送",tuijianMemberEntity.getBusId(),balance.doubleValue(),"",1);
	}

	MemberRecommend r = new MemberRecommend();
	r.setId( recommend.getId() );
	r.setUserNum( recommend.getUserNum() + 1 );
	memberRecommendDAO.updateById( r );
    }
}
