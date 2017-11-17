package com.gt.member.dao;

import com.gt.member.entity.MemberNotice;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
  * 通知信息表 Mapper 接口
 * </p>
 *
 * @author pengjiangli
 * @since 2017-07-25
 */
public interface MemberNoticeDAO extends BaseMapper<MemberNotice> {

    public List<Map<String, Object>> findMemberNotice(@Param("busId")Integer busId,@Param( "sendStuts" )Integer sendStuts,
                                                      @Param("fristpage") Integer fristpage,@Param("pagesize") Integer pagesize);

    /**
     * 统计数量
     * @param publicId
     * @return
     */
    public int countMemberNotice(@Param("busId")Integer busId,@Param( "sendStuts" )Integer sendStuts);


    public List<Map<String, Object>> findBySendStruts(@Param("busId")Integer busId);

    /**
     * 查询所有未发送的短信消息
     * @return
     */
    public List<Map<String, Object>> findAllNotSend(@Param("sendDate")Date sendDate);


    /**
     * 查询所有未发送的会员消息
     * @return
     */
    public Integer findAllNotSendByMsg(@Param("sendDate")Date sendDate);

    public int updateNotice(@Param("sendDate")Date sendDate);

}