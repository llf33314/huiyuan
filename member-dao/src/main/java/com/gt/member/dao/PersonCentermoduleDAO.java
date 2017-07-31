package com.gt.member.dao;

import com.gt.member.entity.PersonCentermodule;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
  * 模块设置 Mapper 接口
 * </p>
 *
 * @author pengjiangli
 * @since 2017-07-25
 */
public interface PersonCentermoduleDAO extends BaseMapper<PersonCentermodule> {
    List<Map<String,Object>> findBybusId(@Param("busId")Integer busId);
}