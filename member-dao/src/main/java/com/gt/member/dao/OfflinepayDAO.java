package com.gt.member.dao;

import com.gt.member.entity.Offlinepay;
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
public interface OfflinepayDAO extends BaseMapper<Offlinepay> {

    List<Map<String, Object>> findAll(@Param("busId")Integer busId);

    List<Map<String, Object>> findByShopIds(@Param("busId")Integer busId,@Param("shopIds")List<Integer> shopIds);

    List<Map<String, Object>> findShop(@Param("busId")Integer busId);

    List<Map<String, Object>> findShopByBusId(@Param("busId")Integer busId, @Param("shopIds")List<Integer> shopIds);

    Offlinepay findByshopId(@Param("busId")Integer busId,@Param("shopId")Integer shopId);


    Offlinepay findByPublicId(@Param("publicId")Integer publicId,@Param("shopId")Integer shopId);

}