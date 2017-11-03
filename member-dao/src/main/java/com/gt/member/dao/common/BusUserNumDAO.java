package com.gt.member.dao.common;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gt.common.entity.BusUserNum;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
  *  Mapper 接口
 * </p>
 *
 * @author pengjiangli
 * @since 2017-08-15
 */
public interface BusUserNumDAO extends BaseMapper<BusUserNum > {

    List<Map<String,Object>> findBusUserNum( @Param( "userId" ) Integer userId, @Param( "style" ) Integer style );
}