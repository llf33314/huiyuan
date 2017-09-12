package com.gt.member.service.common;

import com.alibaba.fastjson.JSONObject;
import com.gt.api.enums.ResponseEnums;
import com.gt.api.util.sign.SignHttpUtils;
import com.gt.common.entity.BusUserEntity;
import com.gt.common.entity.WxPublicUsersEntity;
import com.gt.member.dao.*;
import com.gt.member.dao.common.BusUserDAO;
import com.gt.member.dao.common.WxPublicUsersDAO;
import com.gt.member.entity.*;
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

import java.lang.reflect.Member;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private MemberEntityDAO memberDao;

    @Autowired
    private MemberQcodeWxDAO memberQcodeWxDAO;

    @Autowired
    private PropertiesUtil propertiesUtil;

    @Autowired
    private WxPublicUsersDAO wxPublicUsersDAO;



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
	    List<Map< String,Object >> dict= dictService.getDict( "1058" );
	    Double ratio = CommonUtil.toDouble( dict.get( 0 ).get( "item_value" ) );
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
	List<Map< String,Object >> dict= dictService.getDict( "1058" );
	Double ratio = CommonUtil.toDouble( dict.get( 0 ).get( "1" ) );
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
    public Double deductFenbi(Map<String, Object> dict, Double fenbiMoney) {
	Double ratio = CommonUtil.toDouble(dict.get("item_value"));
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


    /**
     * 添加会员卡记录((新数据接口))
     *
     * @param cardId
     *            卡类型id
     * @param recordType
     *            消费类型
     * @param number
     *            数量
     * @param itemName
     *            物品名称
     *            公众号
     * @param balance
     *            余额
     */
    @Override
    public MemberCardrecord saveCardRecordNew(Integer cardId, Byte recordType,
		    String number, String itemName, Integer busId, String balance,
		    Integer ctId, double amount) {
	if ( CommonUtil.isEmpty(busId)) {
	    return null;
	}

	MemberCardrecord cr = new MemberCardrecord();
	cr.setCardId(cardId);
	cr.setRecordType(recordType.intValue());
	cr.setNumber(number);
	cr.setCreateDate(new Date());
	cr.setItemName(itemName);
	cr.setBusId(busId);
	cr.setBalance(balance);
	cr.setCtId(ctId);
	cr.setAmount(amount);
	try {
	    memberCardrecordDAO.insert(cr);
	    if (recordType == 2) {
		MemberEntity memberEntity = memberDao.findByMcId1(cardId);
		// 积分变动通知
		systemMsgService.jifenMsg(cr, memberEntity );
	    }

	} catch (Exception e) {
	    e.printStackTrace();
	    LOG.error("保存手机端记录异常", e);
	}
	return cr;
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

}
