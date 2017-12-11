package com.gt.member.dao;

import com.gt.member.entity.MemberNoticeuser;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
  * 通知用户关系表 Mapper 接口
 * </p>
 *
 * @author pengjiangli
 * @since 2017-07-25
 */
public interface MemberNoticeuserDAO extends BaseMapper<MemberNoticeuser> {

    List<Map<String, Object>> findByNoticeId(@Param("noticeId") Integer noticeId);

    Integer findCountNotice(@Param("memberId") Integer memberId,@Param("sendDate") Date sendDate);

    List<Map<String, Object>> findNotice(@Param("memberId") Integer memberId,@Param("status") Integer  status,@Param("sendDate")String sendDate);

    void updateStatus(@Param("memberId") Integer memberId);

    List<Map<String, Object>> findNotice1(@Param("memberId")Integer memberId, @Param("noticeId")Integer noticeId);

    int deleteByNoticeId(@Param("noticeId") Integer noticeId);

    int saveList(@Param("noticeUsers") List<MemberNoticeuser> noticeUsers);

    MemberNoticeuser findByNoticeIdAndMemberId(@Param("noticeId") Integer noticeId,@Param("memberId")Integer memberId);

    /**
     * 修改短信发送信息
     * @param ids
     * @param msgId
     */
    void updateByIds(@Param("ids")List<Integer> ids,@Param( "msgId" ) Integer msgId);

    /**
     * 修改短信发送状态
     * @param msgId
     * @param phone
     */
    void updateByMsgIdAndPhone(@Param( "msgId" ) Integer msgId,@Param( "phone" )String phone,@Param( "status" )Integer status);

    /**
     * 总数量
     * @param noticeId
     * @param status
     * @return
     */
    Integer countNoticeuser(@Param( "noticeId" ) Integer noticeId,@Param( "status" )Integer status);

    /**
     * 分页查询
     * @param noticeId
     * @param status
     * @param fristpage
     * @param pagesize
     * @return
     */
    List<Map<String,Object>> findNoticeuser(@Param( "noticeId" ) Integer noticeId,@Param( "status" )Integer status,@Param("fristpage") Integer fristpage,@Param("pagesize") Integer pagesize);

    /**
     * 查询未发送成功的信息
     * @param noticeId
     * @return
     */
    List<Map<String,Object>> findReSendNoticeUser(@Param( "noticeId" )Integer noticeId);

    /**
     * 查询模板发送条数
     * @param noticeId
     * @return
     */
    Integer countByNoticeId(@Param( "noticeId" )Integer noticeId);
}