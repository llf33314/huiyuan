package com.gt.member.dao;

import com.gt.member.entity.Systemnoticecall;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
  * 系统消息记录 Mapper 接口
 * </p>
 *
 * @author pengjiangli
 * @since 2017-07-25
 */
public interface SystemnoticecallDAO extends BaseMapper<Systemnoticecall> {
    void updateByMemberId(@Param("memberId")Integer memberId);

    List<Map<String, Object>> findByMemberId(@Param("memberId")Integer memberId,@Param("status")Integer status);

    Integer findCountByMemberId(@Param("memberId")Integer memberId);


    /**
     * 批量保存数据
     * @param notices
     * @return
     */
    int saveList(@Param("notices")List<Systemnoticecall> notices);

}