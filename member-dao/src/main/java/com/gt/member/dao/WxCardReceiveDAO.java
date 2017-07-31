package com.gt.member.dao;

import com.gt.member.entity.WxCardReceive;
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
public interface WxCardReceiveDAO extends BaseMapper<WxCardReceive> {
    /**
     * 根据code查询
     * @param id
     * @return
     */
    WxCardReceive selectByCode(@Param("code")String code);

    List<Map<String,Object>> findByCardId(@Param("cardId")String cardId,@Param("firstResult")Integer firstResult,@Param("pageSize")Integer pageSize);

    Integer findByCardIdCount(@Param("cardId")String cardId);

    List<Map<String, Object>> findByOpenId(@Param("publicId") Integer publicId,@Param("openid")String openid);

    List<Map<String, Object>> findByOpenId1(@Param("publicId") Integer publicId,@Param("openid")String openid);


    WxCardReceive findByCode(@Param("publicId")Integer publicId,@Param("code")String code);

    WxCardReceive findByCode1(@Param("publicId")Integer publicId,@Param("code")String code);

    List<Map<String, Object>> findByCodeAndPublicId(@Param("publicId")Integer publicId, @Param("cardcode")String cardcode);

}