package com.gt.member.dao.common;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gt.common.entity.BasisCity;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
  *  Mapper 接口
 * </p>
 *
 * @author pengjiangli
 * @since 2017-10-31
 */
public interface BasisCityDAO extends BaseMapper<BasisCity > {

    /**
     * 查询省
     * @return
     */
    List<Map<String,Object>> findBasisCity();

    List<Map<String,Object>> findBaseisCityByCode(@Param( "cityCode" ) String cityCode);
}