package com.gt.member.dao;

import com.gt.member.entity.WxCardShelves;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
  * 卡券货架 Mapper 接口
 * </p>
 *
 * @author pengjiangli
 * @since 2017-07-25
 */
public interface WxCardShelvesDAO extends BaseMapper<WxCardShelves> {
    List<Map<String,Object>> findAll(@Param("publicId")Integer publicId,@Param("endDate") Date endDate);

    List<Map<String,Object>> findOne(@Param("id")Integer id);

    int countWxCardShelves(@Param("publicId")Integer publicId);

    List<Map<String, Object>> findWxCardShelves(@Param("publicId")Integer publicId, @Param("firstResult")Integer firstResult, @Param("pageSize")Integer pageSize);

}