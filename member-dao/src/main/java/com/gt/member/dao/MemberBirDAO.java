package com.gt.member.dao;

import com.gt.member.entity.MemberBir;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
  *  Mapper 接口
 * </p>
 *
 * @author pengjiangli
 * @since 2017-07-25
 */
public interface MemberBirDAO extends BaseMapper<MemberBir> {
    int insertList(List<MemberBir> list);
}