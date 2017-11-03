package com.gt.member.dao.common;

import com.gt.common.entity.WxShop;
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
 * @since 2017-08-02
 */
public interface WxShopDAO extends BaseMapper<WxShop> {

    WxShop selectMainShopByBusId( Integer busId );

    /**
     *
     * @param busId
     * @param verson 版本2
     * @return
     */
    List<Map<String, Object> > findWxShopbyPublicId1Ver2( @Param( "busId" ) Integer busId );

}