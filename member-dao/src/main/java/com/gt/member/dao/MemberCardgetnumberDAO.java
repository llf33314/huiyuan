package com.gt.member.dao;

import com.gt.member.entity.MemberCardgetnumber;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
  *  Mapper 接口
 * </p>
 *
 * @author pengjiangli
 * @since 2017-07-25
 */
public interface MemberCardgetnumberDAO extends BaseMapper<MemberCardgetnumber> {

    Integer findBybusIdAndNominateCode(@Param("busId")Integer busId,@Param("nominateCode") String nominateCode);

    MemberCardgetnumber findByNominateCode(@Param("busId")Integer busId,@Param("nominateCode") String nominateCode);
}