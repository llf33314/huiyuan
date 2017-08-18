package com.gt.member.dao;

import com.gt.member.entity.PersonCenter;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
  * 个人中心设置 Mapper 接口
 * </p>
 *
 * @author pengjiangli
 * @since 2017-07-25
 */
public interface PersonCenterDAO extends BaseMapper<PersonCenter> {


    PersonCenter findBybusId(@Param("busId")Integer busId);
}