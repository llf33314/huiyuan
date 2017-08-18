package com.gt.member.dao;

import com.gt.member.entity.MemberCardLent;
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
public interface MemberCardLentDAO extends BaseMapper<MemberCardLent> {

    MemberCardLent findByCode(@Param("code")String code);
}