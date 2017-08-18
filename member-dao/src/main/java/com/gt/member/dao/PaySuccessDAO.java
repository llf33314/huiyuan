package com.gt.member.dao;

import com.gt.member.entity.PaySuccess;
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
public interface PaySuccessDAO extends BaseMapper<PaySuccess> {
    List<Map<String, Object>> findBybusId(@Param("busId")Integer busId);

    PaySuccess findBybusIdAndModel(@Param("busId")Integer busId,@Param("model")Integer model);
}