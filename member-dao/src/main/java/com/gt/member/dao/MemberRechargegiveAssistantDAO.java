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

}