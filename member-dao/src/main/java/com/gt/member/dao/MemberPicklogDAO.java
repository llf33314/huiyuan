package com.gt.member.dao;

import com.gt.member.entity.MemberPicklog;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
  *  Mapper 接口
 * </p>
 *
 * @author pengjiangli
 * @since 2017-07-25
 */
public interface MemberPicklogDAO extends BaseMapper<MemberPicklog> {

    List<MemberPicklog> findByMemberId(@Param("busId")Integer busId,@Param("memberIds")List<Integer> memberIds);

    int countPickLog(@Param("busId")Integer busId,@Param("search")String search);

    List<Map<String, Object>> findPickLog(@Param("busId")Integer busId, @Param("search")String search, @Param("fristpage")Integer fristpage,
                                          @Param("pagesize")Integer pagesize);


    List<MemberPicklog> findPickMoneyByMemberId(@Param("busId")Integer busId,@Param("memberId")Integer memberId);
}