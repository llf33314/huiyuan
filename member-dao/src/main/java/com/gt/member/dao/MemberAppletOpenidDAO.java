package com.gt.member.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gt.member.entity.MemberAppletOpenid;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
  *  Mapper 接口
 * </p>
 *
 * @author pengjiangli
 * @since 2017-07-25
 */
public interface MemberAppletOpenidDAO extends BaseMapper<MemberAppletOpenid> {

    /**
     * 根据OPENID查询
     * @param openid
     * @return
     */
    MemberAppletOpenid findByOpenId(String openid);


    /**
     * 根据OPENID和busid查询
     * @param openid
     * @return
     */
    MemberAppletOpenid findByOpenIdAndBusId(@Param("openid")String openid, @Param("busId")Integer busId);

    /**
     * 根据会员ID和类型获取OPENID
     * @param memberId
     * @param style
     * @return
     */
    MemberAppletOpenid findByMemberId(@Param("memberId")Integer memberId,@Param("style")Integer style);

    int updateMemberId(@Param("memberId")Integer memberId,@Param("memberIdOld")Integer memberIdOld);
}