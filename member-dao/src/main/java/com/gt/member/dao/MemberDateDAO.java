package com.gt.member.dao;

import com.gt.member.entity.MemberDate;
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
public interface MemberDateDAO extends BaseMapper<MemberDate> {
    MemberDate findByBusIdAndCtId(@Param("busId")Integer busId,@Param("ctId")Integer ctId);
}