package com.gt.member.service.common;

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
import org.apache.cxf.transport.http.UntrustedURLConnectionIOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Member;
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
    private UserConsumeNewDAO userConsumeNewDAO;

    private MemberGiveruleDAO MemberGiveruleDAO;



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
		MemberEntity memberEntity = memberEntityDAO.findByMcId1(cardId);
		// 积分变动通知
		systemMsgService.jifenMsg(cr, memberEntity );
	    }

	} catch (Exception e) {
	    e.printStackTrace();
	    LOG.error("保存手机端记录异常", e);
	}
	return cr;
    }


    /**
     *
     * @param cardId
     * @param recordType  记录类型  1充值或消费  2积分 3粉笔 4 流量
     * @param number 加单位
     * @param itemName
     * @param busId
     * @param balance
     * @param ctId
     * @param amount
     * @return
     */
    public MemberCardrecord saveCardRecordOrderCodeNew(Integer cardId, Byte recordType, String number,
		    String itemName, Integer busId, String balance, Integer ctId, double amount,String orderCode){
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
	cr.setOrderCode( orderCode );
	try {
	    memberCardrecordDAO.insert(cr);
	    if (recordType == 2) {
		MemberEntity memberEntity = memberEntityDAO.findByMcId1(cardId);
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
     * 根据订单号添加赠送物品记录
     *
     * @param orderCode
     *            订单号
     * @throws Exception
     */
    public void findGiveRule(Integer busId,String orderCode) throws Exception {
//	UserConsumeNew ucs = userConsumeNewDAO
//			.findByCode(busId,orderCode);
//	if (CommonUtil.isEmpty(ucs)) {
//	    LOG.error("赠送物品查询订单出现异常");
//	    throw new Exception();
//	}
//
//	try {
//	    Integer busUserId = ucs.getBusId();
//
//	    Integer gtId = ucs.getGtId();
//	    Integer ctId = ucs.getCtId();
//	    double price = ucs.getDiscountAfterMoney();
//
//
//	    // 如果是次卡 和 储值卡 就查询第一个等级的赠送规则
//	    MemberGiverule gr = null;
//	    if (ctId == 5 || ctId == 3) {
//		List<Map<String, Object>> grs = MemberGiveruleDAO
//				.findByBusIdAndCtId(busUserId, ctId);
//		gr = new GiveRule();
//		if (grs.size() != 0) {
//		    gr.setGrId(CommonUtil.toInteger(grs.get(0).get("gr_id")));
//		}
//	    } else {
//		gr = giveRuleMapper.findBybusIdAndGtIdAndCtId(busUserId, gtId,
//				ctId);
//	    }
//
//	    Double fans_currency = 0.0;// 粉笔
//	    int integral = 0; // 积分
//	    int flow = 0;
//
//	    MemberDate memberday = findMemeberDate(busUserId, ctId);
//	    boolean flag = false; // 表示今天是否是会员日
//	    if (CommonUtil.isNotEmpty(memberday)) {
//		flag = true;
//	    }
//
//	    if (type == 1) {
//		if (CommonUtil.isNotEmpty(gr)) {
//		    List<Map<String, Object>> grgts = giveRuleGoodsTypeMapper
//				    .findByGrId(gr.getGrId());
//		    GiveConsume giveConsume = null;
//		    GiveRuleGoodsType grgt = null;
//		    for (Map<String, Object> map : grgts) {
//			giveConsume = new GiveConsume();
//			if (CommonUtil.isEmpty(map.get("gId")))
//			    continue;
//			if ("1".equals(map.get("gId").toString())) {
//			    if ("1".equals(map.get("give_type").toString())) {
//				// 积分
//				if (CommonUtil.isEmpty(map.get("money")))
//				    continue;
//				Double money = Double.parseDouble(map.get(
//						"money").toString());
//				int count = (int) Math.floor(price / money);
//				if (count == 0)
//				    continue;
//				if (CommonUtil.isEmpty(map.get("number")))
//				    continue;
//				int num = count
//						* Integer.parseInt(map.get("number")
//						.toString());
//				Integer upperLmit = Integer.parseInt(map.get(
//						"upperLmit").toString());
//				if (upperLmit != 0) {
//				    num = num > upperLmit ? upperLmit : num;
//				}
//				// 会员日 积分赠送
//				if (flag) {
//				    num = num * memberday.getIntegral();
//				}
//
//				giveConsume.setGcTotal(num);
//				giveConsume.setGtId(Integer.parseInt(map.get(
//						"gId").toString()));
//				giveConsume.setGtName(map.get("gt_name")
//						.toString());
//				giveConsume.setGtUnit(map.get("gt_unit")
//						.toString());
//				giveConsume.setUcId(ucId);
//				giveConsume.setMemberid(CommonUtil
//						.toInteger(ucs.get(0).get("memberId")));
//				giveConsume.setSenddate(new Date());
//				giveConsumeMapper.insertSelective(giveConsume);
//				integral = num;
//			    }
//			} else {
//			    // 添加赠送物品记录
//			    Integer upperLmit = Integer.parseInt(map.get(
//					    "upperLmit").toString());
//
//			    if ("2".equals(map.get("gId").toString())
//					    || "3".equals(map.get("gId").toString())
//					    || upperLmit > 0) {
//				Integer count = Integer.parseInt(map.get(
//						"number").toString());
//				Double money = Double.parseDouble(map.get(
//						"money").toString());
//				if (price < money)
//				    continue;
//				if (upperLmit < count) {
//				    // 扣除商家粉币数量
//				    if ("3".equals(map.get("gId").toString())) {
//					if (fenbi < count) {
//					    continue;
//					}
//					// 会员日 粉币赠送
//					if (flag) {
//					    count = count
//							    * memberday
//							    .getFansCurrency();
//					}
//
//					giveConsume.setGcTotal(count);
//					// 冻结商家粉笔数量
//					fenbiFlowRecordMapper
//							.updateFenbiReduce(busUserId,
//									count, ctId,
//									recFreezeType);
//
//					fans_currency = (double) count;
//				    } else if ("2".equals(map.get("gId")
//						    .toString())) {
//					Integer flowCount = Integer
//							.parseInt(map.get("number")
//									.toString());
//					// 会员日赠送流量
//					if (flag) {
//					    flowCount = flowCount
//							    * memberday.getFlow();
//					    giveConsume.setGcTotal(flowCount);
//					    flow = flowCount
//							    * memberday.getFlow();
//					} else {
//					    giveConsume.setGcTotal(flowCount);
//					    flow = flowCount;
//					}
//
//				    }
//				    // 上限非等于0 认为是商家自定义物品
//				    if (upperLmit != 0) {
//					giveConsume.setGcTotal(upperLmit);
//				    }
//				} else {
//				    giveConsume.setGcTotal(count);
//				}
//
//				giveConsume.setGtId(Integer.parseInt(map.get(
//						"gId").toString()));
//				giveConsume.setGtName(map.get("gt_name")
//						.toString());
//				giveConsume.setGtUnit(map.get("gt_unit")
//						.toString());
//				giveConsume.setUcId(ucId);
//				giveConsume.setMemberid(CommonUtil
//						.toInteger(ucs.get(0).get("memberId")));
//				giveConsume.setSenddate(new Date());
//				giveConsumeMapper.insertSelective(giveConsume);
//
//				if (!"2".equals(map.get("gId").toString())
//						&& !"3".equals(map.get("gId")
//						.toString())) {
//				    // 修改赠送规则物品剩余数量(商家自定义物品)
//				    grgt = new GiveRuleGoodsType();
//				    grgt.setGrId(Integer.parseInt(map.get(
//						    "gr_id").toString()));
//				    grgt.setGtId(Integer.parseInt(map
//						    .get("gId").toString()));
//
//				    if (upperLmit < count) {
//					grgt.setUpperlmit(0);
//					grgt.setGiveType((byte) 2);
//				    } else {
//					grgt.setUpperlmit(upperLmit - count);
//				    }
//				    giveRuleGoodsTypeMapper
//						    .updateByPrimaryKeySelective(grgt);
//				}
//			    }
//			}
//		    }
//		}
//	    }
//
//	    if (CommonUtil.isNotEmpty(ucs.get(0).get("mcId"))) {
//		Card card = cardMapper.selectByPrimaryKey(Integer.parseInt(ucs
//				.get(0).get("mcId").toString()));
//
//		// 修改会员的流量 粉笔 积分信息
//		Member member1 = memberMapper.findByMcIdAndbusId(
//				card.getBusid(),
//				Integer.parseInt(ucs.get(0).get("mcId").toString()));
//		// 消费 积分为负数 改为正数
//		if (integral < 0) {
//		    integral = -integral;
//		}
//
//		if (CommonUtil.isNotEmpty(member1)) {
//		    Member member = new Member();
//		    member.setId(member1.getId());
//		    member.setFansCurrency(member1.getFansCurrency()
//				    + fans_currency);
//		    member.setFlow(member1.getFlow() + flow);
//		    member.setIntegral(member1.getIntegral() + integral);
//		    member.setFlowdate(new Date());
//		    member.setIntegraldate(new Date());
//		    member.setTotalintegral(member1.getTotalintegral()
//				    + integral);
//		    if (ctId == 5) {
//			if (CommonUtil.isNotEmpty(ucs.get(0).get("totalMoney"))) {
//			    price = Double.parseDouble(ucs.get(0)
//					    .get("totalMoney").toString());
//			}
//		    }
//		    member.setTotalmoney(member1.getTotalmoney() + price);
//		    try {
//			memberMapper.updateByPrimaryKeySelective(member);
//		    } catch (Exception e) {
//			e.printStackTrace();
//		    }
//		}
//		Map<String, Object> map = null;
//		// 判断时效卡升级
//		if (ctId == 4) {
//		    map = findNextGradeCtId4(busUserId, gtId, price);
//		} else if (card.getApplytype() != 4) { // 泛会员升级
//		    // 判断会员是否是要升级
//		    map = findNextGrade(busUserId, ctId, gtId,
//				    member1.getTotalintegral() + integral,
//				    member1.getTotalmoney() + price);
//		}
//
//		// 用来标示该价格正负
//		if (!"1".equals(recordType)) {
//		    price = -price;
//		}
//
//		double balance = 0.0;
//		if (CommonUtil.isNotEmpty(card)
//				&& CommonUtil.isNotEmpty(card.getMoney())) {
//		    balance = card.getMoney();
//		    if ("3".equals(CommonUtil.toString(ucs.get(0).get(
//				    "paymentType")))
//				    || "5".equals(CommonUtil.toString(ucs.get(0).get(
//				    "paymentType")))) {
//			card.setMoney(balance + price > 0 ? balance + price : 0);
//		    }
//
//		    if (CommonUtil.isNotEmpty(ucs.get(0).get("uccount"))) {
//			Integer uccount = Integer.parseInt(ucs.get(0)
//					.get("uccount").toString());
//			if (ctId == 5) {
//			    if (CommonUtil.isNotEmpty(ucs.get(0).get(
//					    "giftCount"))) {
//				Integer giftCount = Integer.parseInt(ucs.get(0)
//						.get("giftCount").toString());
//				uccount = uccount + giftCount;
//			    }
//			}
//			if (uccount != 0) {
//			    card.setFrequency(card.getFrequency() - uccount);
//			}
//		    }
//		    // 修改会员卡等级和赠送规则
//		    if (CommonUtil.isNotEmpty(map)) {
//			card.setGtId(Integer.parseInt(map.get("gtId")
//					.toString()));
//			card.setGrId(Integer.parseInt(map.get("grId")
//					.toString()));
//
//			// 升级通知
//			systemMsgService
//					.upgradeMemberMsg(
//							member1,
//							card.getCardno(),
//							CommonUtil.isEmpty(card.getExpiredate()) ? "长期有效"
//									: DateTimeKit.format(card
//									.getExpiredate()));
//		    }
//		    cardMapper.updateByPrimaryKeySelective(card);
//		}
//		if (card.getCtId() == 5) {
//		    if ("1".equals(ucs.get(0).get("recordType").toString())) {
//			saveCardRecordNew(Integer.parseInt(ucs.get(0)
//							.get("mcId").toString()), (byte) 1,
//					ucs.get(0).get("uccount") + "次,送"
//							+ ucs.get(0).get("giftcount") + "次",
//					itemName, member1.getBusid(), card
//							.getFrequency().toString(),
//					card.getCtId(), 0.0);
//		    } else {
//			if ("0".equals(CommonUtil.toString(ucs.get(0).get(
//					"uccount")))) {
//			    saveCardRecordNew(
//					    Integer.parseInt(ucs.get(0).get("mcId")
//							    .toString()), (byte) 1,
//					    price + "元", itemName, member1.getBusid(),
//					    card.getFrequency().toString(),
//					    card.getCtId(), 0.0);
//			} else {
//			    saveCardRecordNew(Integer.parseInt(ucs.get(0)
//							    .get("mcId").toString()), (byte) 1, ucs
//							    .get(0).get("uccount") + "次", itemName,
//					    member1.getBusid(), card.getFrequency()
//							    .toString(), card.getCtId(), 0.0);
//			}
//
//		    }
//		} else {
//		    saveCardRecordNew(
//				    Integer.parseInt(ucs.get(0).get("mcId").toString()),
//				    (byte) 1, price + "元", itemName,
//				    member1.getBusid(), card.getMoney().toString(),
//				    card.getCtId(), 0.0);
//		}
//	    }
//	} catch (Exception e) {
//	    LOG.error("添加赠送记录数据查询异常异常", e);
//	    throw new Exception();
//	}
    }

}
