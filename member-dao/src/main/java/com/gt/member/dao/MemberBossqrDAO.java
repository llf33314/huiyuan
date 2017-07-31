package com.gt.member.dao;

import com.gt.member.entity.MemberBossqr;
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
public interface MemberBossqrDAO extends BaseMapper<MemberBossqr> {


    List<Map<String, Object>> findBybusId(@Param("busId")Integer busId);

    List<Map<String, Object>> findBybusId1(@Param("busId")Integer busId, @Param("page")Integer page);
}