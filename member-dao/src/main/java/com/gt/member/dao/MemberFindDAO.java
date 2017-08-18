package com.gt.member.dao;

import com.gt.member.entity.MemberFind;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
  *  Mapper 接口
 * </p>
 *
 * @author pengjiangli
 * @since 2017-07-25
 */
public interface MemberFindDAO extends BaseMapper<MemberFind> {
    List<Map<String,Object>> findBybusId(@Param("busId")Integer busId, @Param("soure") Integer soure);


    Integer countByModel(@Param("busId")Integer busId);

    MemberFind findByQianDao(@Param("busId")Integer busId);
}