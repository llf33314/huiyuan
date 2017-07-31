package com.gt.member.dao;

import com.gt.member.entity.MemberGiveconsume;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
  * 会员消费赠送记录表 Mapper 接口
 * </p>
 *
 * @author pengjiangli
 * @since 2017-07-25
 */
public interface MemberGiveconsumeDAO extends BaseMapper<MemberGiveconsume> {

    /**
     * 根据订单查询赠送物品详细
     * @param ucId
     * @return
     */
    List<MemberGiveconsume> findByUcId(@Param("ucId")Integer ucId);

    void deleteByUcId(@Param("ucId")Integer ucId);
}