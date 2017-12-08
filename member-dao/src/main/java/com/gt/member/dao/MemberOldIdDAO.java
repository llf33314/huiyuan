package com.gt.member.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gt.member.entity.MemberOldId;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
  *  Mapper 接口
 * </p>
 *
 * @author pengjiangli
 * @since 2017-12-08
 */
public interface MemberOldIdDAO extends BaseMapper<MemberOldId> {

    Integer findMemberId(@Param( "oldId" )Integer old);

}