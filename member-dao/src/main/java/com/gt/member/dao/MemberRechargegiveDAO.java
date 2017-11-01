package com.gt.member.dao;

import com.gt.member.entity.MemberRechargegive;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
  * 次卡和储值卡充值规则 Mapper 接口
 * </p>
 *
 * @author pengjiangli
 * @since 2017-07-25
 */
public interface MemberRechargegiveDAO extends BaseMapper<MemberRechargegive> {

    List<Map<String,Object>> findBybusId(@Param("busId")Integer busId, @Param("ctId")Integer ctId);

    int deleteBybusIdAndGrid(@Param("busId")Integer busId,@Param("grId")Integer grId);

    List<Map<String, Object>> findBybusIdAndGrId(@Param("busId")Integer busId, @Param("grId")Integer grId, @Param("isDate")Integer isDate);

}