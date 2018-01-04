package com.gt.member.dao;

import com.gt.member.entity.UserConsumePay;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
  * 订单支付表 Mapper 接口
 * </p>
 *
 * @author pengjiangli
 * @since 2017-10-17
 */
public interface UserConsumePayDAO extends BaseMapper<UserConsumePay> {


    List<UserConsumePay> findByUcId(@Param( "ucId" )Integer ucId);
}