package com.gt.member.dao;

import com.gt.member.entity.MemberQcodeWx;
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
public interface MemberQcodeWxDAO extends BaseMapper<MemberQcodeWx> {
    MemberQcodeWx findByBusId(@Param("busId")Integer busId,@Param("busType") Integer busType);
}