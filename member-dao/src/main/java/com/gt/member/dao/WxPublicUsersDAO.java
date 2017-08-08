package com.gt.member.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gt.common.entity.WxPublicUsers;

/**
 * <p>
  * 微信订阅号用户表
 Mapper 接口
 * </p>
 *
 * @author pengjiangli
 * @since 2017-07-26
 */
public interface WxPublicUsersDAO extends BaseMapper<WxPublicUsers> {
    WxPublicUsers selectByUserId(int userId);
}