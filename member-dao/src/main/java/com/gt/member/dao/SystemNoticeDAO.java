package com.gt.member.dao;

import com.gt.member.entity.SystemNotice;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
  * 商户系统通知表 Mapper 接口
 * </p>
 *
 * @author pengjiangli
 * @since 2017-07-25
 */
public interface SystemNoticeDAO extends BaseMapper<SystemNotice> {
    List<Map<String,Object>> findBybusId(@Param("busId") Integer busId);

    SystemNotice findBybusIdAndCallType(@Param("busId") Integer busId,@Param("callType") Byte callType);

    /**
     * 查询积分清0系统消息
     * @param busId
     * @return
     */
    List<Map<String, Object>> findMsgBybusIdEq13(@Param("busIds") List<Integer> busId);


    /**
     * 查询积分清0系统短信消息
     * @param busId
     * @return
     */
    List<Map<String, Object>> findSmsBybusIdEq13(@Param("busIds") List<Integer> busId);


    /**
     * 生日消息提醒
     * @param busId
     * @return
     */
    List<Map<String, Object>> findMsgBybusIdEq10();

    /**
     * 生日短信提醒
     * @param busId
     * @return
     */
    List<Map<String, Object>> findSmsBybusIdEq10();
}