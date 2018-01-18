package com.gt.member.dao.duofencard;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gt.duofencard.entity.DuofenCardGetNew;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

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

}