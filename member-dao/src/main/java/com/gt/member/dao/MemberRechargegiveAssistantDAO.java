package com.gt.member.dao;

import com.gt.member.entity.MemberRechargegiveAssistant;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
  * 会员卡副卡充值记录表 Mapper 接口
 * </p>
 *
 * @author pengjiangli
 * @since 2017-10-16
 */
public interface MemberRechargegiveAssistantDAO extends BaseMapper<MemberRechargegiveAssistant> {

    int deleteBybusIdAndGtid(@Param("busId")Integer busId,@Param("gtId")Integer gtId);

    List<Map<String,Object>> findByCtIdAndfuCtId(@Param( "busId" )Integer busId,@Param( "ctId" )Integer ctId,@Param( "fuctId" )Integer fuctId);

    int deleteBybusIdAndCtId(@Param("busId")Integer busId,@Param("ctId")Integer ctId);

    /**
     * 查询副卡充值信息
     * @param busId
     * @param assistantId
     * @return
     */
    List<Map<String,Object>> findByAssistantId(@Param("busId")Integer busId ,@Param("assistantId")Integer assistantId);

    /**
     * 查询会员当前等级充值信息
     * @param busId
     * @param gtId
     * @return
     */
    List<MemberRechargegiveAssistant> findByBusIdAndGtId(@Param("busId")Integer busId,@Param("gtId")Integer gtId,@Param("fuctId")Integer fuctId);
}