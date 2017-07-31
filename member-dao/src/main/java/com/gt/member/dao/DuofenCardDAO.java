package com.gt.member.dao;

import com.gt.member.entity.DuofenCard;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * <p>
  * 多粉卡券 Mapper 接口
 * </p>
 *
 * @author pengjiangli
 * @since 2017-07-25
 */
public interface DuofenCardDAO extends BaseMapper<DuofenCard> {

    int countDuofenCard(Integer busId,String title);

    List<Map<String,Object>> findDuofenCard(Integer busId, String title, Integer firstResult, Integer pageSize);

    /**
     * 根据id集合查询卡券信息
     * @param publicId
     * @param cardIds
     * @return
     */
    List<Map<String, Object>> findByCardIds(Integer busId,List<Integer> cardIds);


    /**
     * 根据id集合查询卡券信息
     * @param cardIds
     * @return
     */
    List<DuofenCard> findInCardIds(List<Integer> cardIds);

    /**
     * 查询已审核通过的优惠券
     * @param busId
     * @return
     */
    List<DuofenCard> findByBusId(Integer busId);

}