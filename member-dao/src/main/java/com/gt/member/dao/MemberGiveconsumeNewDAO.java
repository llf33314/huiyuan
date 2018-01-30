package com.gt.member.dao;

import com.gt.member.entity.MemberGiveconsumeNew;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
  * 商家赠送粉丝信息 Mapper 接口
 * </p>
 *
 * @author pengjiangli
 * @since 2017-11-18
 */
public interface MemberGiveconsumeNewDAO extends BaseMapper<MemberGiveconsumeNew> {

    List<MemberGiveconsumeNew> findByUcId(@Param( "ucId" ) Integer ucId);

}