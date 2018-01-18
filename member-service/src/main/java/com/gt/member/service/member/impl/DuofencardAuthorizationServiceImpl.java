package com.gt.member.service.member.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.gt.common.entity.WxShop;
import com.gt.member.base.BaseServiceImpl;
import com.gt.member.dao.DuofencardAuthorizationDAO;
import com.gt.member.dao.MemberEntityDAO;
import com.gt.member.dao.common.WxShopDAO;
import com.gt.member.entity.DuofencardAuthorization;
import com.gt.member.entity.MemberEntity;
import com.gt.member.service.member.DuofencardAuthorizationService;
import com.gt.member.util.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author zhanbing
 * @since 2018-01-11
 */
@Service
public class DuofencardAuthorizationServiceImpl extends BaseServiceImpl< DuofencardAuthorizationDAO,DuofencardAuthorization > implements DuofencardAuthorizationService {

    @Autowired
    private DuofencardAuthorizationDAO authorizationMapper;

    @Autowired
    private MemberEntityDAO MemberEntityMapper;

    @Autowired
    private WxShopDAO wxShopMapper;

    public Page getAuthorizationUser( Integer curPage, Integer pageSize, Integer busId ) {

	EntityWrapper< DuofencardAuthorization > authorizationCondition = new EntityWrapper< DuofencardAuthorization >();
	authorizationCondition.eq( "busId", busId );

	Integer recordCount = authorizationMapper.selectCount( authorizationCondition );
	if ( recordCount == 0 ) {
	    return new Page();
	}

	List< Map< String,Object > > listItem = authorizationMapper
			    .selectMapsPage( new com.baomidou.mybatisplus.plugins.Page< DuofencardAuthorization >( curPage, pageSize ), authorizationCondition );
	for ( Map< String,Object > authrization : listItem ) {
	    System.out.println( authrization.toString() );
	    MemberEntity member = MemberEntityMapper.selectById( (Serializable) authrization.get( "memberId" ) );
	    authrization.put( "nickName", member.getNickname() );
	    WxShop wxshop =wxShopMapper.selectById( (Serializable) authrization.get( "shopId" ) );
	    authrization.put( "businessName",wxshop.getBusinessName());
	}

	Page page = new Page( curPage, pageSize, recordCount, "" );
	page.setSubList( listItem );
	return page;
    }
}
