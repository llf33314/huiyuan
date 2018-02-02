package com.gt.member.service.member.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.gt.api.enums.ResponseEnums;
import com.gt.bean.vo.DuofenCardNewVO;
import com.gt.duofencard.entity.*;
import com.gt.member.base.BaseServiceImpl;
import com.gt.member.dao.*;
import com.gt.member.dao.duofencard.DuofenCardGetNewDAO;
import com.gt.member.dao.duofencard.DuofenCardNewDAO;
import com.gt.member.dao.duofencard.DuofenCardPublishDAO;
import com.gt.member.dao.duofencard.DuofenCardTimeDAO;
import com.gt.member.entity.*;
import com.gt.member.exception.BusinessException;
import com.gt.member.service.common.dict.DictService;
import com.gt.member.service.common.membercard.RequestService;
import com.gt.member.service.member.DuofenCardNewService;
import com.gt.member.util.CommonUtil;
import com.gt.member.util.Page;
import com.gt.member.util.PropertiesUtil;
import org.apache.log4j.Logger;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.*;

/**
 * 多粉优惠券pc端
 * <p>
 * Created by Administrator on 2018/1/16.
 */
@SuppressWarnings( "ALL" )
@Service
public class DuofenCardNewServiceImpl extends BaseServiceImpl< DuofenCardNewDAO,DuofenCardNew > implements DuofenCardNewService {
    private static final Logger LOG = Logger.getLogger( DuofenCardNewServiceImpl.class );

    @Autowired
    private DuofenCardNewDAO     cardMapper;
    @Autowired
    private DuofenCardTimeDAO    cardTimeMapper;
    @Autowired
    private DuofenCardPublishDAO  cardPublishMapper;
    @Autowired
    private DuofenCardGetNewDAO   cardGetMapper;
    @Autowired
    private MemberEntityDAO       MemberEntityMapper;
    @Autowired
    private UserConsumeNewDAO     userConsumeMapper;
    @Autowired
    private UserConsumePayDAO     userConsumePayMapper;
    @Autowired
    private DictService           dictService;
    @Autowired
    private RequestService        requestService;
    @Autowired
    private MemberRecommendDAO    memberRecommendMapper;
    @Autowired
    private PublicParametersetDAO publicParametersetMapper;

    public Integer addCoupon( DuofenCardNewVO coupon ) throws BusinessException {
	try {
	    coupon.setCreateDate( new Date() );
	    //使用场景通用券默认0
	    coupon.setUseScene( 0 );

	    DuofenCardNew cardNew = new DuofenCardNew();
	    BeanUtils.copyProperties( coupon, cardNew );
	    Integer success = baseMapper.insert( cardNew );

	    coupon.setCardId( cardNew.getId() );

	    DuofenCardTime cardTime = new DuofenCardTime();
	    BeanUtils.copyProperties( coupon, cardTime );
	    cardTimeMapper.insert( cardTime );

	    DuofenCardPublish cardPublish = new DuofenCardPublish();
	    BeanUtils.copyProperties( coupon, cardPublish );
	    cardPublishMapper.insert( cardPublish );
	    return success;
	} catch ( Exception e ) {
	    LOG.error( "添加优惠失败", e );
	    throw new BusinessException( ResponseEnums.ERROR.getCode(), "添加优惠失败" );
	}
    }

    public Integer updateCouponById( DuofenCardNewVO coupon ) {
	try {

	    DuofenCardNew cardNew = new DuofenCardNew();
	    DuofenCardTime cardTime = new DuofenCardTime();
	    DuofenCardPublish cardPublish = new DuofenCardPublish();

	    BeanUtils.copyProperties( coupon, cardNew );
	    coupon.setCardId( coupon.getId() );
	    BeanUtils.copyProperties( coupon, cardTime );
	    BeanUtils.copyProperties( coupon, cardPublish );

	    baseMapper.updateById( cardNew );
	    cardTimeMapper.updateById( cardTime );
	    cardPublishMapper.updateById( cardPublish );


	    return 1;
	} catch ( Exception e ) {
	    e.printStackTrace();
	    LOG.error( "更新优惠券异常", e );
	    throw new BusinessException( ResponseEnums.ERROR.getCode(), "更新优惠券异常" );
	}
    }

    @Override
    public Page getReceiveCouponListById( Integer curPage, Integer pageSize, Integer couponId, String searchContent ) {

	try {
	    HashMap< String,Object > condition = new HashMap< String,Object >();
	    condition.put( "cardId", couponId );

	    if ( searchContent != null ) {
		condition.put( "searchContent", searchContent );
	    }

	    Integer recordCount = cardGetMapper.selectReceiveCouponCount( condition );

	    com.baomidou.mybatisplus.plugins.Page< DuofenCardGetNew > pagination = new com.baomidou.mybatisplus.plugins.Page< DuofenCardGetNew >( curPage, pageSize );
	    List< Map< String,Object > > listItem = cardGetMapper.getReceiveCouponList( pagination, condition );

	    if ( recordCount == 0 ) {
		return new Page();
	    }

	    for ( Map< String,Object > couponReceiveMap : listItem ) {

		MemberEntity member = MemberEntityMapper.selectById( (Serializable) couponReceiveMap.get( "memberId" ) );
		couponReceiveMap.put( "phone", member.getPhone() );
		couponReceiveMap.put( "nickname", member.getNickname() );

		/*if ( Objects.equal( 1, couponReceiveMap.get( "isbuy" ) ) ) {
		    Wrapper< UserConsumeNew > consumeCondition = new EntityWrapper< UserConsumeNew >();
		    consumeCondition.eq( "busId", couponReceiveMap.get( "busId" ) );
		    consumeCondition.eq( "recordType", 2 );
		    consumeCondition.eq( "ucType", 201 );
		    consumeCondition.eq( "cardType", 2 );
		    consumeCondition.eq( "dvId", couponId );

		    List< Map< String,Object > > consumeItem = userConsumeMapper.selectMaps( consumeCondition );
		    if ( consumeItem.size() > 0 ) {
			couponReceiveMap.putAll( consumeItem.get( 0 ) );
		    }
		}*/
	    }

	    Page page = new Page( curPage, pageSize, recordCount, "" );
	    page.setSubList( listItem );
	    return page;
	} catch ( Exception e ) {
	    e.printStackTrace();
	    LOG.error( "优惠券领取列表查询异常" );
	    throw new BusinessException( ResponseEnums.ERROR.getCode(), "优惠券领取列表查询异常" );
	}
    }

    public Map< String,Object > findCouponDetail( Integer id ) {

	try {
	    List< Map< String,Object > > couponMapItem = baseMapper.selectMaps( new EntityWrapper< DuofenCardNew >().eq( "id", id ) );
	    Map< String,Object > couponMap = couponMapItem.get( 0 );

	    Integer cardId = (Integer) couponMap.get( "id" );

	    //发布设置参数
	    List< Map< String,Object > > cardPublishItem = cardPublishMapper.selectMaps( new EntityWrapper< DuofenCardPublish >().eq( "cardId", cardId ) );

	    //时间设置参数
	    List< Map< String,Object > > cardTimeItem = cardTimeMapper.selectMaps( new EntityWrapper< DuofenCardTime >().eq( "cardId", cardId ) );

	    if ( cardPublishItem.size() > 0 ) {
		couponMap.putAll( cardPublishItem.get( 0 ) );
	    }
	    if ( cardTimeItem.size() > 0 ) {
		couponMap.putAll( cardTimeItem.get( 0 ) );
	    }

	    //领取数量
	    Integer receiveQuantity = cardGetMapper.selectCount( new EntityWrapper< DuofenCardGetNew >().eq( "cardId", cardId ) );
	    //使用数量
	    Integer usedQuantity = cardGetMapper.selectCount( new EntityWrapper< DuofenCardGetNew >().eq( "cardId", cardId ).eq( "state", 1 ) );
	    couponMap.put( "receiveQuantity", receiveQuantity );
	    couponMap.put( "usedQuantity", usedQuantity );
	    return couponMap;
	} catch ( Exception e ) {
	    LOG.error( "优惠券详情获取异常" );
	    throw new BusinessException( ResponseEnums.ERROR.getCode(), "优惠券详情获取异常" );
	}
    }

    @Override
    public Map< String,Object > findCouponById( Integer id ) {
	try {
	    List< Map< String,Object > > couponMapItem = baseMapper.selectMaps( new EntityWrapper< DuofenCardNew >().eq( "id", id ) );
	    Map< String,Object > couponMap = couponMapItem.get( 0 );

	    Integer cardId = (Integer) couponMap.get( "id" );

	    //发布设置参数
	    List< Map< String,Object > > cardPublishItem = cardPublishMapper.selectMaps( new EntityWrapper< DuofenCardPublish >().eq( "cardId", cardId ) );

	    //时间设置参数
	    List< Map< String,Object > > cardTimeItem = cardTimeMapper.selectMaps( new EntityWrapper< DuofenCardTime >().eq( "cardId", cardId ) );

	    if ( cardPublishItem.size() > 0 ) {
		couponMap.putAll( cardPublishItem.get( 0 ) );
	    }
	    if ( cardTimeItem.size() > 0 ) {
		couponMap.putAll( cardTimeItem.get( 0 ) );
	    }
	    couponMap.put( "domain", PropertiesUtil.getRes_web_path() );
	    return couponMap;
	} catch ( Exception e ) {
	    LOG.error( "优惠券信息获取异常" );
	    throw new BusinessException( ResponseEnums.ERROR.getCode(), "优惠券信息获取异常" );
	}
    }

    @Override
    public Map< String,Object > getPaymentDetailById( Integer couponId, Integer code ) {
	try {
	    Wrapper< UserConsumeNew > consumeCondition = new EntityWrapper< UserConsumeNew >();
	    consumeCondition.eq( "recordType", 2 );
	    consumeCondition.eq( "ucType", 201 );
	    consumeCondition.eq( "cardType", 2 );
	    consumeCondition.eq( "dvId", couponId );
	    consumeCondition.eq( "disCountdepict", code );

	    List< Map< String,Object > > consumeItem = userConsumeMapper.selectMaps( consumeCondition );
	    if ( consumeItem.size() <= 0 ) {
		return new HashMap<>();
	    }
	    Map< String,Object > consumeMap = consumeItem.get( 0 );
	    String paymentNames = null;
	    List< Map< String,Object > > payMentItem = userConsumePayMapper.selectMaps( new EntityWrapper< UserConsumePay >().eq( "ucId", consumeMap.get( "id" ) ) );
	    for ( Map< String,Object > payment : payMentItem ) {
		//支付来源字典
		SortedMap< String,Object > payMaps = dictService.getDict( "1198" );
		String paymentType = CommonUtil.toString( payment.get( "paymentType" ) );
		if ( paymentNames == null ) {
		    paymentNames = payMaps.get( paymentType ).toString();
		} else {
		    paymentNames = paymentNames.concat( "," ).concat( payMaps.get( paymentType ).toString() );
		}
		consumeMap.put( "paymentNames", paymentNames );

	    }
	    return consumeMap;
	} catch ( Exception e ) {
	    LOG.error( "查询购买详情异常" );
	    throw new BusinessException( ResponseEnums.ERROR.getCode(), "查询购买详情异常" );
	}
    }

    @Override
    public Integer getCouponReceiveQuantity( Integer id ) {
	return cardGetMapper.selectCount( new EntityWrapper< DuofenCardGetNew >().eq( "cardId", id ) );

    }

    @Override
    public List< Map< String,Object > > usageStatistics( Integer busId ) {
	List< Map< String,Object > > statistics = cardGetMapper.usageStatistics( busId );
	return statistics;
    }

    @Override
    public Page usageList( Integer curPage, Integer pageSize, Integer busId, String searchContent ) {
	try {
	    HashMap< String,Object > condition = new HashMap< String,Object >();
	    condition.put( "busId", busId );
	    condition.put( "state", 1 );
	    if ( searchContent != null ) {
		condition.put( "searchContent", searchContent );
	    }

	    Integer recordCount = cardGetMapper.selectUsageListCount( condition );

	    com.baomidou.mybatisplus.plugins.Page< DuofenCardGetNew > pagination = new com.baomidou.mybatisplus.plugins.Page< DuofenCardGetNew >( curPage, pageSize );
	    List< Map< String,Object > > usageList = cardGetMapper.selectUsageList( pagination, condition );

	    if ( recordCount == 0 ) {
		return new Page();
	    }

	    List< Map > wxShops = requestService.findShopAllByBusId( busId );
	    SortedMap< String,Object > useLocations = dictService.getDict( "A003" );
	    for ( Map< String,Object > receiveCoupon : usageList ) {
		//添加门店
		if ( !receiveCoupon.get( "storeId" ).equals( 0 ) ) {
		    for ( Map wxShop : wxShops ) {
			if ( Objects.equals( wxShop.get( "id" ), receiveCoupon.get( "storeId" ) ) ) {
			    receiveCoupon.put( "businessName", wxShop.get( "businessName" ) );
			    break;
			}
		    }
		}
		//添加使用场景
		String useLocation = CommonUtil.toString( receiveCoupon.get( "useLocation" ) );
		if ( useLocation != null ) {
		    receiveCoupon.put( "useLocationName",useLocations.get( useLocation ).toString() );
		}
	    }
	    Page page = new Page( curPage, pageSize, recordCount, "" );
	    page.setSubList( usageList );
	    return page;
	} catch ( Exception e ) {
	    e.printStackTrace();
	    LOG.error( "查询优惠券核销列表异常" + e.getMessage() );
	    throw new BusinessException( ResponseEnums.ERROR.getCode(), "查询优惠券核销列表异常" );
	}
    }

    @Override
    public Page recommendList( Integer curPage, Integer pageSize, Integer busId, String searchContent ) {
	try {
	    HashMap< String,Object > condition = new HashMap< String,Object >();
	    condition.put( "busId", busId );
	    if ( searchContent != null ) {
		condition.put( "searchContent", searchContent );
	    }
	    Integer recordCount = memberRecommendMapper.selectRecommendListCount( condition );

	    com.baomidou.mybatisplus.plugins.Page< MemberRecommend > pagination = new com.baomidou.mybatisplus.plugins.Page< MemberRecommend >( curPage, pageSize );
	    List< Map< String,Object > > recommendList = memberRecommendMapper.selectRecommendList( pagination, condition );

	    if ( recordCount == 0 ) {
		return new Page();
	    }
	    Page page = new Page( curPage, pageSize, recordCount, "" );
	    page.setSubList( recommendList );
	    return page;
	} catch ( Exception e ) {
	    e.printStackTrace();
	    LOG.error( "查询优惠券推荐列表异常" + e.getMessage() );
	    throw new BusinessException( ResponseEnums.ERROR.getCode(), "查询优惠券推荐列表异常" );
	}
    }

    public Map<String,Object> getCouponQuantity( Integer busId ) {
	try {
	    Map<String,Object> mapItem =new HashMap<>(  );
	    //全部
	    Integer couponQuantity =baseMapper.selectCount(new EntityWrapper< DuofenCardNew >().eq( "busId", busId ));
	    //待送审
	    Integer auditQuantity =baseMapper.selectCount(new EntityWrapper< DuofenCardNew >().eq( "busId", busId ).in( "cardStatus",new String[]{"0","3"} ));
	    //审核中
	    Integer auditingQuantity =baseMapper.selectCount(new EntityWrapper< DuofenCardNew >().eq( "busId", busId ).in( "cardStatus","1" ));
	    //已审核
	    Integer auditedQuantity =baseMapper.selectCount(new EntityWrapper< DuofenCardNew >().eq( "busId", busId ).in( "cardStatus","2" ));
	    //已过期
	    Integer expiredQuantity =baseMapper.selectCount(new EntityWrapper< DuofenCardNew >().eq( "busId", busId ).in( "cardStatus","4" ));
	    mapItem.put( "couponQuantity",couponQuantity );
	    mapItem.put( "auditQuantity",auditQuantity );
	    mapItem.put( "auditingQuantity",auditingQuantity );
	    mapItem.put( "auditedQuantity",auditedQuantity );
	    mapItem.put( "expiredQuantity",expiredQuantity );
	    return mapItem;
	} catch ( Exception e ) {
	    e.printStackTrace();
	    LOG.error( "查询数量异常" + e.getMessage() );
	    throw new BusinessException( ResponseEnums.ERROR.getCode(), "查询数量异常" );
	}
    }

    @Override
    public List< Map< String,Object > > getUsageListByBusId( Integer busId ) {
	try {
	    HashMap<String,Object>  condition =new HashMap<>(  );
	    condition.put( "busId", busId );
	    condition.put( "state", 1 );


	    com.baomidou.mybatisplus.plugins.Page< DuofenCardGetNew > pagination = new com.baomidou.mybatisplus.plugins.Page< DuofenCardGetNew >( 0, 299999 );
	    List< Map< String,Object > > usageList = cardGetMapper.selectUsageList( pagination, condition );

	    List< Map > wxShops = requestService.findShopAllByBusId( busId );
	    SortedMap< String,Object > useLocations = dictService.getDict( "A003" );
	    for ( Map< String,Object > receiveCoupon : usageList ) {
		//添加门店
		if ( !receiveCoupon.get( "storeId" ).equals( 0 ) ) {
		    for ( Map wxShop : wxShops ) {
			if ( Objects.equals( wxShop.get( "id" ), receiveCoupon.get( "storeId" ) ) ) {
			    receiveCoupon.put( "businessName", wxShop.get( "businessName" ) );
			    break;
			}
		    }
		}
		//添加使用场景
		String useLocation = CommonUtil.toString( receiveCoupon.get( "useLocation" ) );
		if ( useLocation != null ) {
		    receiveCoupon.put( "useLocationName",useLocations.get( useLocation ).toString() );
		}
	    }

	    return usageList;
	} catch ( Exception e ) {
	    e.printStackTrace();
	    LOG.error( "查询异常" + e.getMessage() );
	    throw new BusinessException( ResponseEnums.ERROR.getCode(), "查询异常" );
	}
    }

    @Override
    public Integer updateCouponPublishById( DuofenCardNewVO coupon ) {
	try {
	    DuofenCardPublish cardPublish = new DuofenCardPublish();
	    BeanUtils.copyProperties( coupon, cardPublish );

	    cardPublishMapper.update( cardPublish ,new EntityWrapper< DuofenCardPublish >(  ).eq("cardId",coupon.getId()));

	    return 1;
	} catch ( Exception e ) {
	    e.printStackTrace();
	    LOG.error( "更新优惠券异常", e );
	    throw new BusinessException( ResponseEnums.ERROR.getCode(), "更新优惠券异常" );
	}
    }

    @Override
    public Integer updateCouponTimeById( DuofenCardNewVO coupon ) {
	try {
	    DuofenCardTime cardTime = new DuofenCardTime();
	    BeanUtils.copyProperties( coupon, cardTime );
	    cardTimeMapper.update( cardTime,new EntityWrapper< DuofenCardTime >(  ).eq("cardId",coupon.getId()));

	    return 1;
	} catch ( Exception e ) {
	    e.printStackTrace();
	    LOG.error( "更新优惠券异常", e );
	    throw new BusinessException( ResponseEnums.ERROR.getCode(), "更新优惠券异常" );
	}
    }

    @Override
    public Integer updateCouponNewById( DuofenCardNewVO coupon ) {
	try {

	    DuofenCardNew cardNew = new DuofenCardNew();
	    BeanUtils.copyProperties( coupon, cardNew );
	    baseMapper.updateById( cardNew );
	    return 1;
	} catch ( Exception e ) {
	    e.printStackTrace();
	    LOG.error( "更新优惠券异常", e );
	    throw new BusinessException( ResponseEnums.ERROR.getCode(), "更新优惠券异常" );
	}
    }

    @Override
    public List< Map< String,Object > > getReceiveCouponListByCouponId( Integer couponId ) {
	try {
	    HashMap< String,Object > condition = new HashMap< String,Object >();
	    condition.put( "cardId", couponId );

	    com.baomidou.mybatisplus.plugins.Page< DuofenCardGetNew > pagination = new com.baomidou.mybatisplus.plugins.Page< DuofenCardGetNew >( 0, 299999 );
	    List< Map< String,Object > > listItem = cardGetMapper.getReceiveCouponList( pagination, condition );


	    for ( Map< String,Object > couponReceiveMap : listItem ) {

		MemberEntity member = MemberEntityMapper.selectById( (Serializable) couponReceiveMap.get( "memberId" ) );
		couponReceiveMap.put( "phone", member.getPhone() );
		couponReceiveMap.put( "nickname", member.getNickname() );

		if ( Objects.equals( 1, couponReceiveMap.get( "isbuy" ) ) ) {
		    Wrapper< UserConsumeNew > consumeCondition = new EntityWrapper< UserConsumeNew >();
		    consumeCondition.eq( "recordType", 2 );
		    consumeCondition.eq( "ucType", 201 );
		    consumeCondition.eq( "cardType", 2 );
		    consumeCondition.eq( "dvId", couponId );
		    consumeCondition.eq( "disCountdepict", couponReceiveMap.get("code") );


		    List< Map< String,Object > > consumeItem = userConsumeMapper.selectMaps( consumeCondition );
		    if ( consumeItem.size() <= 0 ) {
			break;
		    }
		    Map< String,Object > consumeMap = consumeItem.get( 0 );
		    String paymentNames = null;
		    List< Map< String,Object > > payMentItem = userConsumePayMapper.selectMaps( new EntityWrapper< UserConsumePay >().eq( "ucId", consumeMap.get( "id" ) ) );
		    for ( Map< String,Object > payment : payMentItem ) {
			//支付来源字典
			SortedMap< String,Object > payMaps = dictService.getDict( "1198" );
			String paymentType = CommonUtil.toString( payment.get( "paymentType" ) );
			if ( paymentNames == null ) {
			    paymentNames = payMaps.get( paymentType ).toString();
			} else {
			    paymentNames = paymentNames.concat( "," ).concat( payMaps.get( paymentType ).toString() );
			}
			couponReceiveMap.put( "paymentNames", paymentNames );

		    }

		}
	    }
	    return listItem;
	} catch ( Exception e ) {
	    e.printStackTrace();
	    LOG.error( "优惠券领取列表查询异常" );
	    throw new BusinessException( ResponseEnums.ERROR.getCode(), "优惠券领取列表查询异常" );
	}
    }

    @Override
    public Integer deleteByCouponId( Integer couponId ) {
	try{
	  Integer number =  baseMapper.selectCount( new EntityWrapper<DuofenCardNew>().eq( "id" ,couponId).eq("cardStatus",0) );
	  if(number<=0){
	      throw new BusinessException( ResponseEnums.ERROR.getCode(), "删除的优惠券不是待审核状态" );
	  }
	    baseMapper.deleteById( couponId );
	    cardTimeMapper.delete( new EntityWrapper< DuofenCardTime>().eq( "cardId",couponId ) );
	    cardPublishMapper.delete( new EntityWrapper< DuofenCardPublish>().eq( "cardId",couponId ) );
	    return 1;
	} catch ( Exception e ) {
	    e.printStackTrace();
	    LOG.error( "优惠券删除异常" );
	    throw new BusinessException( ResponseEnums.ERROR.getCode(), e.getMessage() );
	}
    }

    @Override
    public Page getCouponListByBusId2( Integer curPage, Integer pageSize, Integer busId, Integer cardStatus, String title, Integer useType ) {
	EntityWrapper< DuofenCardNew > couponCondition = new EntityWrapper< DuofenCardNew >();
	if ( busId != null ) {
	    couponCondition.eq( "busId", busId );
	}
	if ( cardStatus != null ) {
	    couponCondition.eq( "cardStatus", cardStatus );
	}
	if ( title != null ) {
	    couponCondition.like( "title", title );
	}
	if ( useType != null ) {
	    couponCondition.eq( "useType", useType );
	}

	Integer recordCount = baseMapper.selectCount( couponCondition );

	if ( recordCount == 0 ) {
	    return new Page();
	}

	List< Map< String,Object > > listItem = baseMapper.selectMapsPage( new com.baomidou.mybatisplus.plugins.Page< DuofenCardNew >( curPage, pageSize ), couponCondition );

	try {
	    //	    Iterator< Map< String,Object > > it = listItem.iterator();
	    //	    while ( it.hasNext() ) {
	    //		Map< String,Object > couponMap = it.next();
	    //
	    //		if ( cardStatus != null && couponMap.get( "beginTimestamp" ) != null ) {
	    //		    if ( cardStatus != 4 && couponMap.get( "cardStatus" ).equals( 4 ) ) {  //只查询未过期的
	    //			it.remove();
	    //			break;
	    //		    }
	    //		    if ( !couponMap.get( "cardStatus" ).equals( 4 ) && cardStatus == 4 ) {  //只查询已经过期的
	    //			it.remove();
	    //			break;
	    //		    }
	    //		}
	    for ( Map< String,Object > couponMap : listItem ) {
		Integer cardId = (Integer) couponMap.get( "id" );

		//领取数量
		Integer receiveQuantity = getCouponReceiveQuantity( cardId );
		couponMap.put( "receiveQuantity", receiveQuantity );

		//发布设置参数
		List< Map< String,Object > > cardPublishItem = cardPublishMapper.selectMaps( new EntityWrapper< DuofenCardPublish >().eq( "cardId", cardId ) );

		//时间设置参数
		List< Map< String,Object > > cardTimeItem = cardTimeMapper.selectMaps( new EntityWrapper< DuofenCardTime >().eq( "cardId", cardId ) );
		if ( cardPublishItem.size() > 0 ) couponMap.putAll( cardPublishItem.get( 0 ) );
		if ( cardTimeItem.size() > 0 ) couponMap.putAll( cardTimeItem.get( 0 ) );
	    }

	    Page page = new Page( curPage, pageSize, recordCount, "" );

	    page.setSubList( listItem );
	    return page;
	} catch ( Exception e ) {
	    LOG.error( "获取优惠卷列表异常" );
	    throw new BusinessException( ResponseEnums.ERROR.getCode(), "获取优惠卷列表异常" );
	}
    }

    @Override
    public Page recommendReceiveList( Integer curPage, Integer pageSize, Integer recommendId, String searchContent ) {
	try {
	    HashMap< String,Object > condition = new HashMap< String,Object >();
	    condition.put( "recommendId", recommendId );
	    if ( searchContent != null ) {
		condition.put( "searchContent", searchContent );
	    }
	    Integer recordCount = memberRecommendMapper.selectRecommendReceiveListCount( condition );

	    com.baomidou.mybatisplus.plugins.Page< MemberRecommend > pagination = new com.baomidou.mybatisplus.plugins.Page< MemberRecommend >( curPage, pageSize );
	    List< Map< String,Object > > recommendList = memberRecommendMapper.selectRecommendReceiveList( pagination, condition );

	    if ( recordCount == 0 ) {
		return new Page();
	    }
	    Page page = new Page( curPage, pageSize, recordCount, "" );
	    page.setSubList( recommendList );
	    return page;
	} catch ( Exception e ) {
	    e.printStackTrace();
	    LOG.error( "查询优惠券推荐列表异常" + e.getMessage() );
	    throw new BusinessException( ResponseEnums.ERROR.getCode(), "查询优惠券推荐列表异常" );
	}
    }

    @Override
    public Page withdrawList( Integer curPage, Integer pageSize, Integer busId, String searchContent ) {
	try {
	    HashMap< String,Object > condition = new HashMap< String,Object >();
	    condition.put( "busId", busId );
	    if ( searchContent != null ) {
		condition.put( "searchContent", searchContent );
	    }
	    Integer recordCount = memberRecommendMapper.selectWithdrawListCount( condition );

	    com.baomidou.mybatisplus.plugins.Page< MemberRecommend > pagination = new com.baomidou.mybatisplus.plugins.Page< MemberRecommend >( curPage, pageSize );
	    List< Map< String,Object > > recommendList = memberRecommendMapper.selectWithdrawList( pagination, condition );

	    if ( recordCount == 0 ) {
		return new Page();
	    }
	    Page page = new Page( curPage, pageSize, recordCount, "" );
	    page.setSubList( recommendList );
	    return page;
	} catch ( Exception e ) {
	    e.printStackTrace();
	    LOG.error( "查询推荐提现列表异常" + e.getMessage() );
	    throw new BusinessException( ResponseEnums.ERROR.getCode(), "查询推荐提现列表异常" );
	}
    }

    @Override
    public Integer withdrawMoneySet( Integer busId, Integer money ) {
	try {
	    Integer count =publicParametersetMapper.selectCount( new EntityWrapper<PublicParameterset>().eq("busId",busId) );
	    PublicParameterset parameterset =new PublicParameterset();
	    parameterset.setDfcardPickMoney( CommonUtil.toDouble( money ) );
	    if(count==0){
		parameterset.setBusId( busId );
		publicParametersetMapper.insert(parameterset);
	    }else{
		publicParametersetMapper.update(parameterset,new EntityWrapper<PublicParameterset>().eq("busId",busId));
	    }
	    return 1;
	} catch ( Exception e ) {
	    LOG.error( "提现金额设置异常" );
	    throw new BusinessException( ResponseEnums.ERROR.getCode(), "提现金额设置异常" );
	}
    }

    @Override
    public Page getCouponListByBusId( Integer curPage, Integer pageSize, Integer busId, String cardStatus, String title, Integer useType ) {

	EntityWrapper< DuofenCardNew > couponCondition = new EntityWrapper< DuofenCardNew >();
	if ( busId != null ) {
	    couponCondition.eq( "busId", busId );
	}
	if ( cardStatus != null ) {
	    couponCondition.in( "cardStatus", cardStatus );
	}
	if ( title != null ) {
	    couponCondition.like( "title", title );
	}
	if ( useType != null ) {
	    couponCondition.eq( "useType", useType );
	}

	Integer recordCount = baseMapper.selectCount( couponCondition );

	if ( recordCount == 0 ) {
	    return new Page();
	}

	List< Map< String,Object > > listItem = baseMapper.selectMapsPage( new com.baomidou.mybatisplus.plugins.Page< DuofenCardNew >( curPage, pageSize ), couponCondition );

	try {
	    //	    Iterator< Map< String,Object > > it = listItem.iterator();
	    //	    while ( it.hasNext() ) {
	    //		Map< String,Object > couponMap = it.next();
	    //
	    //		if ( cardStatus != null && couponMap.get( "beginTimestamp" ) != null ) {
	    //		    if ( cardStatus != 4 && couponMap.get( "cardStatus" ).equals( 4 ) ) {  //只查询未过期的
	    //			it.remove();
	    //			break;
	    //		    }
	    //		    if ( !couponMap.get( "cardStatus" ).equals( 4 ) && cardStatus == 4 ) {  //只查询已经过期的
	    //			it.remove();
	    //			break;
	    //		    }
	    //		}
	    for ( Map< String,Object > couponMap : listItem ) {
		Integer cardId = (Integer) couponMap.get( "id" );
               //优惠券二维码领取url
		String receiveCouponUrl = PropertiesUtil.getWebHome()+"/html/phone/index.html#/home/" + busId+cardId;
		couponMap.put( "receiveCouponUrl", receiveCouponUrl );

		//领取数量
		Integer receiveQuantity = getCouponReceiveQuantity( cardId );
		couponMap.put( "receiveQuantity", receiveQuantity );

		//发布设置参数
		List< Map< String,Object > > cardPublishItem = cardPublishMapper.selectMaps( new EntityWrapper< DuofenCardPublish >().eq( "cardId", cardId ) );

		//时间设置参数
		List< Map< String,Object > > cardTimeItem = cardTimeMapper.selectMaps( new EntityWrapper< DuofenCardTime >().eq( "cardId", cardId ) );
		if ( cardPublishItem.size() > 0 ) couponMap.putAll( cardPublishItem.get( 0 ) );
		if ( cardTimeItem.size() > 0 ) couponMap.putAll( cardTimeItem.get( 0 ) );
	    }

	    Page page = new Page( curPage, pageSize, recordCount, "" );

	    page.setSubList( listItem );
	    return page;
	} catch ( Exception e ) {
	    LOG.error( "获取优惠卷列表异常" );
	    throw new BusinessException( ResponseEnums.ERROR.getCode(), "获取优惠卷列表异常" );
	}
    }

}
