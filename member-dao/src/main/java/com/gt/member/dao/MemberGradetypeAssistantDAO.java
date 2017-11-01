package com.gt.member.dao;

import com.gt.member.entity.MemberGradetypeAssistant;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
  * 会员卡副卡和主卡关系表 Mapper 接口
 * </p>
 *
 * @author pengjiangli
 * @since 2017-10-16
 */
public interface MemberGradetypeAssistantDAO extends BaseMapper<MemberGradetypeAssistant> {

    /**
     * 查询卡通的副卡类型
     * @param busId
     * @param gtId
     * @return
     */
    List<Integer> findAssistantBygtId(@Param( "busId" ) Integer busId,@Param( "gtId" )Integer gtId);



    List<Map<String,Object>> findAssistantByctId(@Param( "busId" )Integer busId,@Param( "ctId" )Integer ctId);





}