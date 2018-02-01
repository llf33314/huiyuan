package com.gt.member.dao.duofencard;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.gt.duofencard.entity.DuofenCardGetNew;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
  *  Mapper 接口
 * </p>
 *
 * @author pengjiangli
 * @since 2018-01-16
 */
public interface DuofenCardGetNewDAO extends BaseMapper<DuofenCardGetNew > {

    /**
     * 统计粉丝领取的优惠券数量
     * @param cardId
     * @param date
     * @return
     */
    public Integer countDuofenCardGetByCardId(@Param( "cardId" ) Integer cardId,@Param( "memberId" )Integer memberId,@Param( "date" )Date date);

    Integer selectReceiveCouponCount( HashMap< String,Object > couponReceiveWrapper );

    List<Map<String,Object>> getReceiveCouponList( Page<DuofenCardGetNew> pagination, HashMap< String,Object > couponReceiveWrapper );

    List<Map<String,Object>> countByCardIds(@Param( "cardIds" ) List<Integer> cardIds);


    List<Map<String,Object>> findByMemberId(@Param( "memberId" ) Integer memberId);

    List<Map<String,Object>> findFriendByMemberId(@Param( "busId" ) Integer busId,@Param( "getIds" ) List<Integer> getIds);

    List<Map<String,Object>> findInvalidByMemberId(@Param( "memberId" ) Integer memberId);


    Integer countByCardId(@Param( "cardId" )Integer cardId);

    /**
     * 查询粉丝的优惠券信息
     * @param busId
     * @param code
     * @return
     */
    DuofenCardGetNew findByCode(@Param( "busId" )Integer busId,@Param( "code" )String code);

    List<Map<String,Object>> usageStatistics( Integer busId );

    Integer selectUsageListCount( HashMap<String, Object> condition );

    List<Map<String,Object>> selectUsageList( Page<DuofenCardGetNew> pagination, HashMap<String, Object> condition );
}