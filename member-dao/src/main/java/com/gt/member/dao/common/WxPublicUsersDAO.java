package com.gt.member.dao.common;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gt.common.entity.WxPublicUsersEntity;

/**
 * <p>
  * 微信订阅号用户表
 Mapper 接口
 * </p>
 *
 * @author pengjiangli
 * @since 2017-07-26
 */
public interface WxPublicUsersDAO extends BaseMapper<WxPublicUsersEntity > {
    WxPublicUsersEntity selectByUserId(int userId);
}