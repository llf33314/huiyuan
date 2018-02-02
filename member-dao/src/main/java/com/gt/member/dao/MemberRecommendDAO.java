package com.gt.member.dao;

import com.baomidou.mybatisplus.plugins.Page;
import com.gt.member.entity.MemberRecommend;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
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
public interface MemberRecommendDAO extends BaseMapper<MemberRecommend> {

    List<Map<String,Object>> findByMemberId(@Param("memberIds")List<Integer> memberIds);


    MemberRecommend findBycode(@Param("code")String code,@Param("ctId")Integer ctId);

    /**
     * 推荐总数统计
     * @param busId
     * @param ctId
     * @return
     */
    int countTuiJian(@Param("busId")Integer busId,@Param("ctId")Integer ctId);

    /**
     * 查询未使用的推荐码
     * @param record
     * @return
     */
    List<Map<String,Object>> selectIsNotUserCode(MemberRecommend record);


    /**
     *
     * @param busId
     * @param code
     * @return
     */
    MemberRecommend findRecommendByCode(@Param("code")String code);

    /**
     * 查询卡券推荐
     * @param cardId
     * @param memberId
     * @return
     */
    Integer countRecommendByCardId(@Param("cardId")Integer cardId,@Param("memberId")Integer memberId);


    MemberRecommend findRecommendByCardId(@Param("cardId")Integer cardId,@Param("memberId")Integer memberId);

    /**
     * 推荐手机号码查询
     * @param phone
     * @param memberId
     * @return
     */
    int findRecommendByphone(@Param("phone")String phone,@Param("memberId")Integer memberId);


    /**
     * 查询卡券成功推荐已使用
     * @param memberId
     * @return
     */
    Map<String,Object> countRecommendSuccessByMemberId(@Param("memberId")Integer memberId);


    /**
     * 查询卡券推荐
     * @param memberId
     * @return
     */
    Integer countRecommendByMemberId(@Param("memberId")Integer memberId);

    List<Map<String,Object>> findRecommendPageByMemberId(@Param("memberId")Integer memberId,@Param( "firstResult" )Integer firstResult,@Param( "pageSize" )Integer pageSize);

    Integer selectRecommendListCount( HashMap<String, Object> condition );

    /**
     * 推荐列表
     * @param pagination
     * @param condition
     * @return
     */
    List<Map<String,Object>> selectRecommendList( Page<MemberRecommend> pagination, HashMap<String, Object> condition );

    Integer selectRecommendReceiveListCount( HashMap<String, Object> condition );

    /**
     * 推荐领取列表
     * @param pagination
     * @param condition
     * @return
     */
    List<Map<String,Object>> selectRecommendReceiveList( Page<MemberRecommend> pagination, HashMap<String, Object> condition );

    Integer selectWithdrawListCount( HashMap<String, Object> condition );

    List<Map<String,Object>> selectWithdrawList( Page<MemberRecommend> pagination, HashMap<String, Object> condition );

    MemberRecommend findRecommendByBusIdAndCode(@Param( "busId" )Integer busId,@Param( "code" )String code);
}