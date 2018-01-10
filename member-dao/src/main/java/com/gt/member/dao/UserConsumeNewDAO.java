package com.gt.member.dao;

import com.gt.member.entity.UserConsumeNew;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
  *  Mapper 接口
 * </p>
 *
 * @author pengjiangli
 * @since 2017-10-17
 */
public interface UserConsumeNewDAO extends BaseMapper<UserConsumeNew> {

    /**
     * 查询订单数据
     * @param busId
     * @param orderCode
     * @return
     */
    UserConsumeNew findByCode(@Param( "busId" ) Integer busId,@Param( "orderCode" )String orderCode);

    /**
     * 根据订单查询订单数据
     * @param orderCode
     * @return
     */
    UserConsumeNew findOneByCode(@Param( "orderCode" )String orderCode);


    List<Map<String,Object>> findUserConsumeChongZhiByMohe(@Param( "busId" )Integer busId,@Param( "memberId" )Integer memberId,@Param( "shopId" )Integer shopId,@Param("startDate")String startDate,@Param("endDate")String endDate,@Param("first")Integer first,
                    @Param("pageSize")Integer pageSize);

    List<Map<String,Object>> findUserConsumeChongZhiByMemberId(@Param( "busId" )Integer busId,@Param( "memberId" )Integer memberId,@Param("startDate")String startDate,@Param("endDate")String endDate,@Param("first")Integer first,
                    @Param("pageSize")Integer pageSize);

    /**
     * 查询订单总数
     * @param busId
     * @param ctId
     * @return
     */
    int countOrder(@Param("busId")Integer busId,@Param("ctId")Integer ctId);

    /**
     * 计算消费总金额
     */
    Double sumXiaofei(@Param("busId")Integer busId,@Param("ctId")Integer ctId);

    /**
     * 计算购买会员卡总金额
     */
    Double buyCard(@Param("busId")Integer busId,@Param("ctId")Integer ctId);

    /**
     * 查询7天的交易总额
     */
    List<Map<String, Object>> sum7DayOrder(@Param("busId")Integer busId,@Param("ctId")Integer ctId,
                    @Param("startDate")Date startDate,@Param("endDate")Date endDate);


    /**
     * 查询7天的售卡交易金额
     */
    List<Map<String, Object>> sum7DayBuyCard(@Param("busId")Integer busId,@Param("ctId")Integer ctId,
                    @Param("startDate")Date startDate,@Param("endDate")Date endDate);


    /**
     * 积分卡兑换物品
     */
    List<Map<String, Object>> countJifenDuiHan(@Param("busId")Integer busId);

    /**
     * 统计使用的积分
     * @param busId
     * @return
     */
    Integer countUseJifen(@Param("busId")Integer busId);


    /**
     * 查询近7天的折扣额
     */
    List<Map<String, Object>> sum7DayDiscount(@Param("busId")Integer busId);


    /**
     * 折扣总额
     */
    Double sumDisCount(@Param("busId")Integer busId);


    /**
     * 查询储值卡充值总额
     */
    Double sumChongzhi(@Param("busId")Integer busId);


    /**
     * 查询近7天走势
     */
    List<Map<String, Object>> sumChongzhi7Day(@Param("busId")Integer busId);

    /**
     * 次卡消费统计
     */
    List<Map<String, Object>> sumCiKa(@Param("busId")Integer busId);


    /**
     * 查询次卡消费次数
     */
    Integer userCiKa(@Param("busId")Integer busId);


    List<Map<String,Object>> findUserConsumeChongZhiByMemberId(@Param( "busId" )Integer busId,@Param( "memberId" )Integer memberId,@Param("startDate")String startDate,@Param("endDate")String endDate,@Param("first")Integer first,
                    @Param("pageSize")Integer pageSize);

    Integer countUserConsumeChongZhiByMemberId(@Param( "busId" )Integer busId,@Param( "memberId" )Integer memberId,@Param("startDate")String startDate,@Param("endDate")String endDate);


    List<Map<String,Object>> findUserConsumeDuiHuanByMemberId(@Param( "busId" )Integer busId,@Param( "memberId" )Integer memberId,@Param("startDate")String startDate,@Param("endDate")String endDate,@Param("first")Integer first,
                    @Param("pageSize")Integer pageSize);

    Integer countUserConsumeDuiHuanByMemberId(@Param( "busId" )Integer busId,@Param( "memberId" )Integer memberId,@Param("startDate")String startDate,@Param("endDate")String endDate);


    List<Map<String,Object>> findUserConsumeCikaByMemberId(@Param( "busId" )Integer busId,@Param( "memberId" )Integer memberId,@Param("startDate")String startDate,@Param("endDate")String endDate,@Param("first")Integer first,
                    @Param("pageSize")Integer pageSize);

    Integer countUserConsumeCikaByMemberId(@Param( "busId" )Integer busId,@Param( "memberId" )Integer memberId,@Param("startDate")String startDate,@Param("endDate")String endDate);


    List<Map<String,Object>> findUserConsumeXiaoFeiByMemberId(@Param( "busId" )Integer busId,@Param( "memberId" )Integer memberId,@Param("startDate")String startDate,@Param("endDate")String endDate,@Param("payStatus")Integer payStatus,@Param("first")Integer first,
                    @Param("pageSize")Integer pageSize);

    Integer countUserConsumeXiaoFeiByMemberId(@Param( "busId" )Integer busId,@Param( "memberId" )Integer memberId,@Param("startDate")String startDate,@Param("endDate")String endDate,@Param("payStatus")Integer payStatus);

}