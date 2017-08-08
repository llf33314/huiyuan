package com.gt.member.dao;

import com.gt.member.entity.DuofenCardGet;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
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
public interface DuofenCardGetDAO extends BaseMapper<DuofenCardGet> {

    int countCardGet(@Param("cardReceiveId")Integer cardReceiveId,@Param("memberId")Integer memberId,@Param("getDate")Date getDate);

    /**
     * 分页数据
     */
    int countCardGetByCardcardId(@Param("cardId")Integer cardId);

    /**
     * 分页查询数据
     * @param cardId
     * @param firstResult
     * @param pageSize
     * @return
     */
    List<Map<String, Object>> findCardGetBycardId(@Param("cardId")Integer cardId,@Param("firstResult")Integer firstResult,@Param("pageSize")Integer pageSize);

    /**
     * 查询用户名下所有的卡券信息
     * @param memberId
     * @return
     */
    List<Map<String, Object>> findCardGetByMemberId(@Param("memberIds")List<Integer> memberIds);

    /**
     * 根据code查询卡券信息
     * @param publicId
     * @param code
     * @return
     */
    Map<String, Object> findByCode(@Param("busId")Integer busId,@Param("code")String code);

    /**
     * 朋友的券
     * @param memberId
     * @return
     */
    List<Map<String, Object>> findByFriendMemberId(@Param("busId")Integer busId,@Param("memberId")Integer memberId);

    /**
     * 查询用户拥有的卡券 包括朋友的券
     * @param memberId
     * @return
     */
    List<Map<String, Object>> findCardByMemberId(@Param("memberId")Integer memberId);


    List<Map<String, Object>> findByCardId(@Param("cardId")Integer cardId,@Param("memberId")Integer memberId,@Param("cardNum")Integer cardNum);

    /**
     * 卡券批量核销
     * @param codes
     * @return
     */
    int updateByCodes(@Param("codes")List<String> codes);


    /**
     * 卡券批量核销
     * @param codes
     * @param storeId
     * @return
     */
    int updateStoreIdByCodes(@Param("codes")List<String> codes,@Param("storeId")Integer storeId);

    int insertList(@Param("duofencardGets")List<DuofenCardGet> duofencardGets);

    List<DuofenCardGet> findUserCardByReceiveId(@Param("cardReceiveId")Integer cardReceiveId,@Param("memberId")Integer memberId);

    /**
     * 过期的第三方优惠券
     * @param memberId
     * @return
     */
    List<DuofenCardGet> findThreeByOverTime(@Param("cardReceiveId")Integer cardReceiveId, @Param("memberId")Integer memberId);

    /**
     * 查询卡券状态
     * @param cardReceiveId
     * @param memberId
     * @param state
     * @return
     */
    List<Map<String, Object>> findUserCardByState(@Param("cardReceiveId")Integer cardReceiveId,@Param("memberIds")List<Integer> memberIds,@Param("state")byte state);

    /**
     * 分组统计领取数
     * @param busId
     * @param cardId
     * @param getType
     * @return
     */
    List<Map<String, Object>> countGroupbyGetType(@Param("busId")Integer busId,@Param("cardId")Integer cardId,@Param("getType")Byte getType,@Param("startTime")Date startTime,@Param("endTime")Date endTime);

    /**
     * 领取总数量
     * @param busId
     * @param cardId
     * @param getType
     * @param startTime
     * @param endTime
     * @return
     */
    int countDuofenCard(@Param("busId")Integer busId,@Param("cardId")Integer cardId,@Param("getType")Byte getType,@Param("startTime")Date startTime,@Param("endTime")Date endTime);


    /**
     * 统计使用数
     * @param busId
     * @param cardId
     * @param getType
     * @return
     */
    List<Map<String, Object>> countByUserGroupbyGetType(@Param("busId")Integer busId,@Param("cardId")Integer cardId,@Param("getType")Byte getType,@Param("startTime")Date startTime,@Param("endTime")Date endTime,@Param("storeId")Integer storeId);

    /**
     * 统计使用总数
     * @param busId
     * @param cardId
     * @param getType
     * @param startTime
     * @param endTime
     * @return
     */
    int countDuofenCardByUser(@Param("busId")Integer busId,@Param("cardId")Integer cardId,@Param("getType")Byte getType,@Param("startTime")Date startTime,@Param("endTime")Date endTime,@Param("storeId")Integer storeId);

    /**
     * 统计7天领取数
     * @param busId
     * @return
     */
    List<Map<String, Object>> select7Day(@Param("busId")Integer busId);


    /**
     * 统计7天使用数
     * @param busId
     * @return
     */
    List<Map<String, Object>> selectUser7Day(@Param("busId")Integer busId,@Param("storeId")Integer storeId);


    /**
     * 统计7天领取数
     * @param busId
     * @return
     */
    List<Map<String, Object>> select7Month(@Param("busId")Integer busId);


    /**
     * 统计7天使用数
     * @param busId
     * @return
     */
    List<Map<String, Object>> selectUser7Month(@Param("busId")Integer busId,@Param("storeId")Integer storeId);


    List<Map<String, Object>> findByCodes(@Param("codes")List<String> codes);

    /**
     * 统计每个门店的核销数量
     * @param busIds
     * @return
     */
    List<Map<String, Object>> countCode(@Param("busId")Integer busId,@Param("storeIds")List<Integer> storeIds);


    List<Map<String, Object>> findByMemberIdAndCardReceiveId(@Param("memberId")Integer memberId,@Param("cardReceiveId")Integer cardReceiveId);

    int deleteByIds(@Param("ids")List<Integer> ids);

    List<Map<String,Object>> findMeiRongCardGetByMemberId(@Param( "memberIds" )List<Integer> memberIds,@Param( "receiceId" )Integer receiceId);
}