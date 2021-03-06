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

    List<Map<String, Object>> findNotice(@Param("memberId") Integer memberId,@Param("sendDate")String sendDate);

    void updateStatus(@Param("memberId") Integer memberId);

    List<Map<String, Object>> findNotice1(@Param("memberId")Integer memberId, @Param("noticeId")Integer noticeId);

    int deleteByNoticeId(@Param("noticeId") Integer noticeId);

    int saveList(@Param("noticeUsers") List<MemberNoticeuser> noticeUsers);
}