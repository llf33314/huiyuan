package com.gt.member.dao;

import com.gt.member.entity.MemberGiverule;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
  * 赠送规则表 Mapper 接口
 * </p>
 *
 * @author pengjiangli
 * @since 2017-07-25
 */
public interface MemberGiveruleDAO extends BaseMapper<MemberGiverule> {
    /**
     * 根据公众号id和卡片类型查询
     * @param publicId
     * @param ctId
     * @return
     */
    List<Map<String, Object>> findByBusIdAndCtId(@Param("busId") Integer busId,@Param("ctId") Integer ctId);


    /**
     * 根据公众号id和卡片类型查询 联合卡片等级表查询
     * @param publicId
     * @param ctId
     * @return
     */
    List<Map<String, Object>> findBybusIdAndCtId1(@Param("busId") Integer busId, @Param("ctId") Integer ctId);



    void deleteBygrIds(@Param("grIds")List<Integer> grIds);

    /**
     * 根据公众号id和等级id获取id
     * @param publicId
     * @param gtId
     * @return
     */
    Integer findBybusIdAndGtId(@Param("busId") Integer busId,@Param("gtId") Integer gtId);

    /**
     * 根据商户id 卡片等级模板id 卡片类型id查询赠送规则
     * @param publicId
     * @param gtId
     * @param ctId
     * @return
     */
    MemberGiverule findBybusIdAndGtIdAndCtId(@Param("busId") Integer busId,@Param("gtId") Integer gtId,@Param("ctId") Integer ctId);


}