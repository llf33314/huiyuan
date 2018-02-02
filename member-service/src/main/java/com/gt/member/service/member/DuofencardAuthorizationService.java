package com.gt.member.service.member;

import com.gt.member.base.BaseService;
import com.gt.member.entity.DuofencardAuthorization;
import com.gt.member.util.Page;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhanbing
 * @since 2018-01-11
 */
public interface DuofencardAuthorizationService extends BaseService<DuofencardAuthorization > {


    Page getAuthorizationUser( Integer curPage, Integer pageSize, Integer busId, String searchContent );

}
