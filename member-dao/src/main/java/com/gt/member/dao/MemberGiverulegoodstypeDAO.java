package com.gt.member.dao;

import com.gt.member.entity.MemberGiverulegoodstype;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
  * 赠送规则与赠送物品详情表 Mapper 接口
 * </p>
 *
 * @author pengjiangli
 * @since 2017-07-25
 */
public interface MemberGiverulegoodstypeDAO extends BaseMapper<MemberGiverulegoodstype> {
    List<MemberGiverulegoodstype> findInGrId(@Param("grIds")List<Integer> grId);

    List<Map<String, Object>> findByGrId(@Param("grId")Integer grId);

    void deleteByGrIds(@Param("grIds") List<Integer> grIds);

    int deleteByBusIdAndCtId(@Param("busId")Integer busId,@Param( "ctId" )Integer ctId);


    List<Map<String, Object>> findBybusIdAndCtId(@Param("busId")Integer busId,@Param( "ctId" )Integer ctId);


    List<MemberGiverulegoodstype> findGriveGoodByBusIdAndCtId(@Param( "busId" )Integer busId,@Param( "ctId" )Integer ctId);
}