package com.gt.member.dao.common;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gt.common.entity.BusFlow;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
  * 商家购买流量表 Mapper 接口
 * </p>
 *
 * @author pengjiangli
 * @since 2017-11-02
 */
public interface BusFlowDAO extends BaseMapper<BusFlow > {

    /**
     * 获取用户全部的流量信息集合
     * @param userId
     * @return
     */
    List<BusFlow> getBusFlowsByUserId( @Param( "userId" ) Integer userId );

}