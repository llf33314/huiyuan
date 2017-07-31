package com.gt.member.dao;

import com.gt.member.entity.PublicParameterset;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
  *  Mapper 接口
 * </p>
 *
 * @author pengjiangli
 * @since 2017-07-25
 */
public interface PublicParametersetDAO extends BaseMapper<PublicParameterset> {

    PublicParameterset findBybusId(@Param("busId")Integer busId);

    PublicParameterset findByPublicId(@Param("publicId")Integer publicId);

    PublicParameterset findByPublicId_1(@Param("publicId")Integer publicId);

    PublicParameterset findBybusId_1(@Param("busId")Integer busId);

    List<PublicParameterset> findMonth();

}