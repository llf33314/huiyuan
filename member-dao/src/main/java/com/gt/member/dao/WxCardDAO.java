package com.gt.member.dao;

import com.gt.member.entity.WxCard;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
  * 微信卡券 Mapper 接口
 * </p>
 *
 * @author pengjiangli
 * @since 2017-07-25
 */
public interface WxCardDAO extends BaseMapper<WxCard> {
    /**
     * 根据微信卡券ID查询
     * @param cardId
     * @return
     */
    WxCard selectByCardId(@Param("card_id")String cardId);

    List<Map<String,Object>> findByTitle(@Param("firstResult")Integer firstResult,@Param("pageSize")Integer pageSize,@Param("publicId")Integer publicId,@Param("title")String title);

    Integer countWxCard(@Param("publicId")Integer publicId,@Param("title")String title);


    List<Map<String, Object>> findWxCard(@Param("publicId")Integer publicId, @Param("endTime") Date endTime);

    Integer updateCardIsDelivery(@Param("cardIds")List<Integer> cardIds);

    Integer updateCardByCardId(@Param("cardId")String cardId);

    Integer updateCardByCardId1(@Param("card_status")Integer card_status,@Param("cardId")String cardId);

    Integer deleteCardById(@Param("id")Integer id);
}