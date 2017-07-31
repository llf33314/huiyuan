package com.gt.member.dao;

import com.gt.member.entity.MemberParameter;
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
public interface MemberParameterDAO extends BaseMapper<MemberParameter> {
    MemberParameter findByMemberId(@Param("memberId")Integer memberId);
}