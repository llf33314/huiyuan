package com.gt.member.dao;

import com.gt.member.entity.PaySuccesslog;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

/**
 * <p>
  *  Mapper 接口
 * </p>
 *
 * @author pengjiangli
 * @since 2017-07-25
 */
public interface PaySuccesslogDAO extends BaseMapper<PaySuccesslog> {

    PaySuccesslog findByOrderNo(@Param("memberId")Integer memberId,@Param("orderNo")String orderNo);


    PaySuccesslog findByModelAndMemberId(@Param("model")Integer model,@Param("memberId")Integer memberId);

    int updateByModelAndMemberId(@Param("model")Integer model,@Param("memberId")Integer memberId,@Param("successId")Integer successId);

    int countByModelAndMemberId(@Param("model")Integer model,@Param("memberId")Integer memberId);

    int countByModelAndMemberIdAndWeek(@Param("model")Integer model,@Param("memberId")Integer memberId,@Param("date")Date date);

}