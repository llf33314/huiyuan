package com.gt.member.dao;

import com.gt.member.entity.WxShop;
import com.baomidou.mybatisplus.mapper.BaseMapper;

/**
 * <p>
  *  Mapper 接口
 * </p>
 *
 * @author pengjiangli
 * @since 2017-08-02
 */
public interface WxShopDAO extends BaseMapper<WxShop> {

    WxShop selectMainShopByBusId(Integer busId);

}