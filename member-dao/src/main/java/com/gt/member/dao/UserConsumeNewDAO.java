package com.gt.member.dao;

import com.gt.member.entity.UserConsumeNew;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

<<<<<<< HEAD
import java.util.Date;
import java.util.List;
import java.util.Map;

=======
>>>>>>> ae76849aca3d2f4538456ff77be4f3c544ac352b
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
    List<Map<String, Object>> sum7DayOrder(@Param("busId")Integer busId,@Param("ctId")Integer ctId,@Param("date")Date date);


    /**
     * 查询7天的售卡交易金额
     */
    List<Map<String, Object>> sum7DayBuyCard(@Param("busId")Integer busId,@Param("ctId")Integer ctId,@Param("date")Date date);


    /**
     * 积分卡兑换物品
     */
    List<Map<String, Object>> countJifenDuiHan(@Param("busId")Integer busId);

    /**
     * 统计使用的积分
     * @param busId
     * @return
     */
    int countUseJifen(@Param("busId")Integer busId);


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


    UserConsumeNew findOneByCode(@Param( "orderCode" )String orderCode);

}