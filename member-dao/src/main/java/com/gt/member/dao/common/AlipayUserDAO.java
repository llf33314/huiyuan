package com.gt.member.dao.common;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gt.common.entity.AlipayUser;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
  * 支付宝用户表 Mapper 接口
 * </p>
 *
 * @author pengjiangli
 * @since 2017-10-31
 */
public interface AlipayUserDAO extends BaseMapper<AlipayUser > {

    AlipayUser selectByBusId( @Param( "busId" ) Integer busId );
}