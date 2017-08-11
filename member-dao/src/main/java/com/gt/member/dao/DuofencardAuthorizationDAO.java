package com.gt.member.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gt.member.entity.DuofencardAuthorization;
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
public interface DuofencardAuthorizationDAO extends BaseMapper<DuofencardAuthorization> {

    int countAuthorization(@Param("busId")Integer busId);

    List<Map<String, Object>> findAuthorization(@Param("busId")Integer busId,@Param("first")Integer first,@Param("pageSize") Integer pageSize);

    int findByMemberOpenId(@Param("busId")Integer busId,@Param("openId")String openId);

    DuofencardAuthorization findByOpenId(@Param("busId")Integer busId,@Param("openId")String openId);


}