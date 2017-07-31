package com.gt.member.dao;

import com.gt.member.entity.PaySuccesscard;
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
public interface PaySuccesscardDAO extends BaseMapper<PaySuccesscard> {

    List<Map<String, Object>> findBySuccessId(@Param("successId")Integer successId);

    Integer deleteBySuccessId(@Param("successId")Integer successId);
}