package com.gt.member.service.member.impl;

import com.gt.api.enums.ResponseEnums;
import com.gt.member.base.BaseServiceImpl;
import com.gt.member.dao.DuofencardAuthorizationDAO;
import com.gt.member.dao.MemberEntityDAO;
import com.gt.member.entity.DuofencardAuthorization;
import com.gt.member.exception.BusinessException;
import com.gt.member.service.common.membercard.RequestService;
import com.gt.member.service.member.DuofencardAuthorizationService;
import com.gt.member.util.CommonUtil;
import com.gt.member.util.Page;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
    private RequestService requestService;

    private static final Logger LOG = Logger.getLogger( DuofenCardNewServiceImpl.class );

    public Page getAuthorizationUser( Integer curPage, Integer pageSize, Integer busId, String searchContent ) {
	Page page = null;
	try {
	    HashMap< String,Object > condition = new HashMap< String,Object >();
	    condition.put( "busId", busId );
	    if(searchContent!=null){
		condition.put( "searchContent", searchContent );
	    }

	    Integer recordCount = authorizationMapper.getAuthorizationUserCount( condition );
	    if ( recordCount == 0 ) {
		return new Page();
	    }
	    com.baomidou.mybatisplus.plugins.Page< DuofencardAuthorization > pagination = new com.baomidou.mybatisplus.plugins.Page< DuofencardAuthorization >(                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          curPage, pageSize );
	    List< Map< String,Object > > listItem = authorizationMapper.getAuthorizationUserList( pagination, condition );
	    List< Map > wxShops = requestService.findShopAllByBusId( busId );
	    for ( Map< String,Object > authrization : listItem ) {
		byte[] bytes = (byte[]) authrization.get( "nickname" );
		authrization.put( "nickname", new String( bytes, "UTF-8" ) );

		//添加门店
		if ( !authrization.get( "shopId" ).equals( 0 ) ) {
		    for ( Map wxShop : wxShops ) {
			if ( Objects.equals( wxShop.get( "id" ), authrization.get( "shopId" ) ) ) {
			    authrization.put( "businessName",wxShop.get( "businessName" ));
			    break;
			}
		    }
		}

	    }

	    page = new Page( curPage, pageSize, recordCount, "" );
	    page.setSubList( listItem );
	} catch ( Exception e ) {
	    e.printStackTrace();
	    LOG.error( "查询授权列表异常" + e.getMessage() );
	    throw new BusinessException( ResponseEnums.ERROR.getCode(), "查询授权列表异常" );
	}
	return page;
    }
}
