package com.gt.member.dao;

import com.gt.member.entity.UserConsumeNew;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
  *  Mapper 接口
 * </p>
 *
 * @author pengjiangli
 * @since 2017-10-17
 */
public interface UserConsumeNewDAO extends BaseMapper<UserConsumeNew> {

    UserConsumeNew findByCode(@Param( "busId" ) Integer busId,@Param( "orderCode" )String orderCode);

}