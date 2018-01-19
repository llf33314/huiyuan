package com.gt.member.service.member.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.pagination.PageHelper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.google.common.base.Objects;
import com.gt.api.enums.ResponseEnums;
import com.gt.duofencard.entity.*;
import com.gt.member.base.BaseServiceImpl;
import com.gt.member.dao.MemberEntityDAO;
import com.gt.member.dao.UserConsumeNewDAO;
import com.gt.member.dao.duofencard.DuofenCardGetNewDAO;
import com.gt.member.dao.duofencard.DuofenCardNewDAO;
import com.gt.member.dao.duofencard.DuofenCardPublishDAO;
import com.gt.member.dao.duofencard.DuofenCardTimeDAO;
import com.gt.member.entity.MemberEntity;
import com.gt.member.entity.UserConsumeNew;
import com.gt.member.exception.BusinessException;
import com.gt.member.service.member.DuofenCardNewService;
import com.gt.member.util.DateTimeKit;
import com.gt.member.util.Page;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
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
public class DuofenCardNewServiceImpl extends BaseServiceImpl<DuofenCardNewDAO,DuofenCardNew > implements DuofenCardNewService {
    private static final Logger LOG = Logger.getLogger( DuofenCardNewServiceImpl.class );

    @Autowired
    private DuofenCardNewDAO     cardMapper;
    @Autowired
    private DuofenCardTimeDAO    cardTimeMapper;
    @Autowired
    private DuofenCardPublishDAO cardPublishMapper;
    @Autowired
    private DuofenCardGetNewDAO  cardGetMapper;
    @Autowired
    private MemberEntityDAO      MemberEntityMapper;
    @Autowired
    private UserConsumeNewDAO    userConsumeMapper;

    public Integer addCoupon( DuofenCardNewVO coupon ) throws BusinessException {
	try {
	    coupon.setCreateDate( new Date() );
	    Integer cardId = baseMapper.insert( coupon );

	    coupon.setCardId( cardId );

	    DuofenCardTime cardTime = new DuofenCardTime();
	    DuofenCardPublish cardPublish = new DuofenCardPublish();

	    BeanUtils.copyProperties( cardTime, coupon );
	    BeanUtils.copyProperties( cardPublish, coupon );

	    cardTimeMapper.insert( cardTime );
	    cardPublishMapper.insert( cardPublish );
	    return cardId;
	} catch ( Exception e ) {
	    LOG.error( "添加优惠失败", e );
	    throw new BusinessException( ResponseEnums.ERROR.getCode(), "添加优惠失败" );
	}
    }

    public Integer updateCouponById( DuofenCardNewVO coupon ) {
	try {
	    DuofenCardTime cardTime = new DuofenCardTime();
	    DuofenCardPublish cardPublish = new DuofenCardPublish();
	    BeanUtils.copyProperties( cardTime, coupon );
	    BeanUtils.copyProperties( cardPublish, coupon );

	    baseMapper.updateById( coupon );
	    cardTimeMapper.update( cardTime, new EntityWrapper< DuofenCardTime >().eq( "cardId", coupon.getCardId() ) );
	    cardPublishMapper.update( cardPublish, new EntityWrapper< DuofenCardPublish >().eq( "cardId", coupon.getCardId() ) );
	    return 1;
	} catch ( Exception e ) {
	    LOG.error( "更新优惠券异常", e );
	    throw new BusinessException( ResponseEnums.ERROR.getCode(), "更新优惠券异常" );
	}
    }

    @Override
    public Page getReceiveCouponListById( Integer curPage, Integer pageSize, Integer couponId, String searchContent ) {

	try {
	    HashMap<String, Object> condition = new HashMap<String, Object>();
	    condition.put( "cardId",couponId );

	    if ( searchContent != null ) {
		condition.put( "searchContent",searchContent );
	    }

	    Integer recordCount = cardGetMapper.selectReceiveCouponCount( condition );

	    com.baomidou.mybatisplus.plugins.Page< DuofenCardGetNew > pagination = new com.baomidou.mybatisplus.plugins.Page< DuofenCardGetNew >( curPage, pageSize );
	    List< Map< String,Object > > listItem = cardGetMapper.getReceiveCouponList(pagination,condition );

//	    else {
//		recordCount = cardGetMapper.selectCount( couponReceiveWrapper );
//		listItem = cardGetMapper
//				.selectMapsPage( new com.baomidou.mybatisplus.plugins.Page< DuofenCardGetNew >( curPage, pageSize ), couponReceiveWrapper );
//
//	    }


	    if ( recordCount == 0 ) {
		return new Page();
	    }




	    for ( Map< String,Object > couponReceiveMap : listItem ) {

		MemberEntity member = MemberEntityMapper.selectById( (Serializable) couponReceiveMap.get( "memberId" ) );
		couponReceiveMap.put( "phone", member.getPhone() );
		couponReceiveMap.put( "nickname", member.getNickname() );

		if ( Objects.equal( 1, couponReceiveMap.get( "isbuy" ) ) ) {
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
		}
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

    public List< Map< String,Object > > selectById( Integer id ) {

	try {
	    List< Map< String,Object > > couponMapItem = baseMapper.selectMaps( new EntityWrapper< DuofenCardNew >().eq( "id", id ) );
	    Map< String,Object > couponMap = couponMapItem.get( 0 );

	    Integer cardId = (Integer) couponMap.get( "cardId" );

	    //发布设置参数
	    List< Map< String,Object > > cardPublishItem = cardPublishMapper.selectMaps( new EntityWrapper< DuofenCardPublish >().eq( "cardId", cardId ) );

	    //时间设置参数
	    List< Map< String,Object > > cardTimeItem = cardTimeMapper.selectMaps( new EntityWrapper< DuofenCardTime >().eq( "cardId", cardId ) );

	    couponMap.putAll( cardPublishItem.get( 0 ) );
	    couponMap.putAll( cardTimeItem.get( 0 ) );

	    //领取数量
	    Integer receiveQuantity = cardGetMapper.selectCount( new EntityWrapper< DuofenCardGetNew >().eq( "cardId", cardId ) );
	    //使用数量
	    Integer usedQuantity = cardGetMapper.selectCount( new EntityWrapper< DuofenCardGetNew >().eq( "cardId", cardId ).eq( "state", 1 ) );
	    couponMap.put( "receiveQuantity", receiveQuantity );
	    couponMap.put( "usedQuantity", usedQuantity );
	    return couponMapItem;
	} catch ( Exception e ) {
	    LOG.error( "优惠券详情获取异常" );
	    throw new BusinessException( ResponseEnums.ERROR.getCode(), "优惠券详情获取异常" );
	}
    }

    @Override
    public Page getCouponListByBusId( Integer curPage, Integer pageSize, Integer busId, Integer expired, Integer cardStatus, String title, Integer useType ) {

	EntityWrapper< DuofenCardNew > couponCondition = new EntityWrapper< DuofenCardNew >();
	couponCondition.eq( "busId", busId );
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
	    Iterator< Map< String,Object > > it = listItem.iterator();
	    while ( it.hasNext() ) {
		Map< String,Object > couponMap = it.next();
		boolean expireFlag = false;
		if ( couponMap.get( "beginTimestamp" ) != null && couponMap.get( "endTimestamp" ) != null ) {
		    boolean isDateValid = DateTimeKit
				    .isValidDate( couponMap.get( "beginTimestamp" ).toString(), couponMap.get( "endTimestamp" ).toString(), DateTimeKit.DEFAULT_DATE_FORMAT );
		    if ( !isDateValid ) { //优惠券已经过期
			expireFlag = true;
		    }
		}

		if ( expired != null ) {
		    if ( expireFlag && expired == 2 ) {  //只查询未过期的
			it.remove();
			break;
		    }
		    if ( ( !expireFlag && expired == 1 ) && couponMap.get( "startDate" ) != null ) {  //只查询已经过期的   领取后有效不做判断
			it.remove();
			break;
		    }
		}
		couponMap.put( "cardStatus", ( expireFlag == true ? 99 : null ) );  //99为已过期标识

		Integer cardId = (Integer) couponMap.get( "cardId" );

		//领取数量
		Integer receiveQuantity = cardGetMapper.selectCount( new EntityWrapper< DuofenCardGetNew >().eq( "cardId", cardId ) );
		couponMap.put( "receiveQuantity", receiveQuantity );

		//发布设置参数
		List< Map< String,Object > > cardPublishItem = cardPublishMapper.selectMaps( new EntityWrapper< DuofenCardPublish >().eq( "cardId", cardId ) );

		//时间设置参数
		List< Map< String,Object > > cardTimeItem = cardTimeMapper.selectMaps( new EntityWrapper< DuofenCardTime >().eq( "cardId", cardId ) );
                if(cardPublishItem.size()>0)
		couponMap.putAll( cardPublishItem.get( 0 ) );
		if(cardTimeItem.size()>0)
		couponMap.putAll( cardTimeItem.get( 0 ) );
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
