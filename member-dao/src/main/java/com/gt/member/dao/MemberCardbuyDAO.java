package com.gt.member.dao;

import com.gt.member.entity.MemberCardbuy;
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
public interface MemberCardbuyDAO extends BaseMapper<MemberCardbuy> {
    MemberCardbuy findByMemberId1(@Param("busId") Integer busId,@Param("memberId") Integer MemberId);
}