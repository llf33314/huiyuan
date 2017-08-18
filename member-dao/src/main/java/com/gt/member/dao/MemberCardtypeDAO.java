package com.gt.member.dao;

import com.gt.member.entity.MemberCardtype;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
  * 卡片类型表 Mapper 接口
 * </p>
 *
 * @author pengjiangli
 * @since 2017-07-25
 */
public interface MemberCardtypeDAO extends BaseMapper<MemberCardtype> {
    /**
     * 查询所以卡片类型
     * @return
     */
    List<MemberCardtype> findByBusId(@Param("busId") Integer busId);
}