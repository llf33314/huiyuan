package com.gt.member.dao;

import com.gt.member.entity.MemberCardrecord;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
  * 卡片记录 Mapper 接口
 * </p>
 *
 * @author pengjiangli
 * @since 2017-07-25
 */
public interface MemberCardrecordDAO extends BaseMapper<MemberCardrecord> {

    List<Map<String, Object>> findByCardId(@Param("cardId") Integer cardId);

    List<Map<String, Object>> findByCardIdFeiBi(@Param("cardId") Integer cardId,@Param("page")Integer page);

    List<Map<String, Object>> findByCardIdJifen(@Param("cardId") Integer cardId,@Param("page")Integer page);

    List<Map<String, Object>> findByCardIdFlow(@Param("cardId") Integer cardId, @Param("page")Integer page);

    /**
     * 查询前年赠送积分情况
     * @param busIds
     * @return
     */
    List<Map<String, Object>> sumByBusId(@Param("busIds")List<Integer> busIds,@Param("startDate")Date startDate,@Param("endDate")Date endDate);

    List<Map<String, Object>> sumCurrentByBusId(@Param("busIds")List<Integer> busIds,@Param("startDate")Date startDate,@Param("endDate")Date endDate);

    List<Map<String,Object>> findCardrecordByMcId(@Param( "mcId" )Integer mcId,@Param( "page" )Integer page,@Param( "pageSize" )Integer pageSize);
}