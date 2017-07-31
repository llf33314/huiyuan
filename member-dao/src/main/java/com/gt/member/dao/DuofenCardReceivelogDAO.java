package com.gt.member.dao;

import com.gt.member.entity.DuofenCardReceivelog;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
  * 卡包领取记录 Mapper 接口
 * </p>
 *
 * @author pengjiangli
 * @since 2017-07-25
 */
public interface DuofenCardReceivelogDAO extends BaseMapper<DuofenCardReceivelog> {
    /**
     * 查询该卡包历史领取数量
     * @param crId
     * @return
     */
    List<Map<String, Object>> countByCrId(@Param("crIds")List<Integer> crIds, @Param("memberId")Integer memberId);


    /**
     * 查询该用户卡包历史领取数量
     * @param crId
     * @return
     */
    Map<String, Object> countByCrIdAndMemberId(@Param("crId")Integer crId, @Param("memberId")Integer memberId);


    /**
     * 查询该卡包历史领取数量
     * @param crId
     * @param beginDate
     * @param endDate
     * @return
     */
    Map<String, Object> countByCrIdAndDate(@Param("crId")Integer crId,@Param("memberId")Integer memberId,@Param("beginDate")Date beginDate);

    /**
     * 查询第三方平台下所有优惠券
     * @param threeMemberId
     * @return
     */
    List<Map<String, Object>> findByThreeMemberId(@Param("threeMemberId")Integer threeMemberId,@Param("firstResult")Integer firstResult,@Param("pageSize")Integer pageSize);

}