package com.gt.member.dao;

import com.gt.member.entity.UserConsumeNew;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

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


    UserConsumeNew findOneByCode(@Param( "orderCode" )String orderCode);

    /**
     * 统计充值信息
     * @param busId
     * @param shopId
     * @param startDate
     * @param endDate
     * @return
     */
    Map<String,Object> countUserConsumeChongZhiByMohe(@Param( "busId" )Integer busId,@Param( "shopId" )Integer shopId,@Param("startDate")String startDate,@Param("endDate")String endDate);

    List<Map<String,Object>> findUserConsumeChongZhiByMohe(@Param( "busId" )Integer busId,@Param( "memberId" )Integer memberId,@Param( "shopId" )Integer shopId,@Param("startDate")String startDate,@Param("endDate")String endDate,@Param("first")Integer first,
                    @Param("pageSize")Integer pageSize);

    List<Map<String,Object>> findUserConsumeChongZhiByMemberId(@Param( "busId" )Integer busId,@Param( "memberId" )Integer memberId,@Param("startDate")String startDate,@Param("endDate")String endDate,@Param("first")Integer first,
                    @Param("pageSize")Integer pageSize);

}