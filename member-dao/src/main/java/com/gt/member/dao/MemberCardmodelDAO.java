package com.gt.member.dao;

import com.gt.member.entity.MemberCardmodel;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
  * 卡片模板表 Mapper 接口
 * </p>
 *
 * @author pengjiangli
 * @since 2017-07-25
 */
public interface MemberCardmodelDAO extends BaseMapper<MemberCardmodel> {

    /**
     * 获取卡片模板
     * @return
     */
    List<MemberCardmodel> findCardModelByBusId(@Param("busId")Integer busId);

    /**
     * 根据类型查询模板
     * @param cmType
     * @return
     */
    List<MemberCardmodel> findByType(@Param("cmType") Integer cmType);

    Integer findCmType(@Param("publicId") Integer publicId,@Param("ctId") Integer ctId);

    List<MemberCardmodel> findCardNotInCmId(@Param("cmType") Integer cmType,@Param("cmIds") List cmIds);

    MemberCardmodel findCardModel();

    /**
     * 根据gtid查询会员卡的名字颜色卡号等级及背景
     * @param gtId
     * @return
     */
    List<Map<String, Object>> findCardBackgroundModel(@Param("gtId")Integer gtId);

}