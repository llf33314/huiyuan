package com.gt.member.dao.common;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gt.common.entity.BusUserBranchRelation;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
  *  Mapper 接口
 * </p>
 *
 * @author pengjiangli
 * @since 2017-10-19
 */
public interface BusUserBranchRelationDAO extends BaseMapper<BusUserBranchRelation > {


    /**
     * 查询登陆用户所拥有的门店信息
     * @param busId
     * @return
     */
    List<Map<String, Object> > findBusUserShop(@Param("busId")Integer busId);
}