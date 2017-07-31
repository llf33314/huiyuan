package com.gt.member.dao;

import com.gt.member.entity.SystemCalltype;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * <p>
  * 系统通知类型 Mapper 接口
 * </p>
 *
 * @author pengjiangli
 * @since 2017-07-25
 */
public interface SystemCalltypeDAO extends BaseMapper<SystemCalltype> {

    /**
     * 查询所有
     * @return
     */
    List<Map<String,Object>> findAll();
}