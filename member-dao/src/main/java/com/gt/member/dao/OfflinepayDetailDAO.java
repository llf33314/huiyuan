package com.gt.member.dao;

import com.gt.member.entity.OfflinepayDetail;
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
public interface OfflinepayDetailDAO extends BaseMapper<OfflinepayDetail> {

    int deleteByoffId(@Param("offId")Integer offId);

    List<Map<String, Object>> findByOffId(@Param("offId")Integer offId);

}