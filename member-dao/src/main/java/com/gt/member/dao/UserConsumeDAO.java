package com.gt.member.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gt.member.entity.UserConsume;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
  * 会员消费记录总表 Mapper 接口
 * </p>
 *
 * @author pengjiangli
 * @since 2017-07-25
 */
public interface UserConsumeDAO extends BaseMapper<UserConsume > {
    /**
     * 分页查询
     */
    List<Map<String,Object>> findUserConume( @Param( "busId" ) Integer busId, @Param( "first" ) Integer first, @Param( "pageSize" ) Integer pageSize, @Param( "ctId" ) Integer ctId,
		    @Param( "recordType" ) Integer recordType, @Param( "startDate" ) String startDate, @Param( "endDate" ) String endDate, @Param( "search" ) String search );

    int countUserConume( @Param( "busId" ) Integer busId, @Param( "ctId" ) Integer ctId, @Param( "recordType" ) Integer recordType, @Param( "startDate" ) String startDate,
		    @Param( "endDate" ) String endDate, @Param( "search" ) String search );

    /**
     * 根据用户id和公众号id查询
     */
    List<Map<String, Object>> findByMemberId( @Param( "busId" ) Integer busId, @Param( "memberId" ) Integer memberId );

    /**
     * 根据单号查询
     */
    List<Map<String, Object>> findByOrderCode( @Param( "orderCode" ) String orderCode );

    UserConsume findByOrderCode1( @Param( "orderCode" ) String orderCode );

    /**
     * 批量新增
     */
    int insertBatchFlowRecord( List< UserConsume > list );

    /**
     * 查询卡券核销记录
     */
    List<Map<String, Object>> findWxcardReceive( @Param( "busId" ) Integer busId, @Param( "first" ) Integer first, @Param( "pageSize" ) Integer pageSize,
		    @Param( "shopIds" ) List< Integer > shopIds, @Param( "cardType" ) Integer cardType );

    /**
     * 分页统计
     */
    Integer countWxcardReceive( @Param( "busId" ) Integer busId, @Param( "shopIds" ) List< Integer > shopIds, @Param( "cardType" ) Integer cardType );




    /**
     * 分页查询
     */
    List<Map<String,Object>> findUserConume1( @Param( "busId" ) Integer busId, @Param( "first" ) Integer first, @Param( "pageSize" ) Integer pageSize,
		    @Param( "recordType" ) Integer recordType, @Param( "startDate" ) String startDate, @Param( "endDate" ) String endDate, @Param( "payStatus" ) Integer payStatus );

    int countUserConume1( @Param( "busId" ) Integer busId, @Param( "recordType" ) Integer recordType, @Param( "startDate" ) String startDate, @Param( "endDate" ) String endDate,
		    @Param( "payStatus" ) Integer payStatus );


    /**
     * 查询卡券核销记录
     */
    List<Map<String, Object>> findDuofencardReceive( @Param( "busId" ) Integer busId, @Param( "first" ) Integer first, @Param( "pageSize" ) Integer pageSize,
		    @Param( "shopIds" ) List< Integer > shopIds, @Param( "cardType" ) Integer cardType );

    /**
     * 分页统计
     */
    Integer countDuofencardReceive( @Param( "busId" ) Integer busId, @Param( "shopIds" ) List< Integer > shopIds, @Param( "cardType" ) Integer cardType );

    /**
     * id查询单条数据
     */
    List<Map<String, Object>> findById( @Param( "id" ) Integer id );

    /**
     * 分页查询
     */
    List<Map<String,Object>> findUserConumeByMember( @Param( "busId" ) Integer busId, @Param( "memberId" ) Integer memberId, @Param( "first" ) Integer first,
		    @Param( "pageSize" ) Integer pageSize, @Param( "startDate" ) String startDate, @Param( "endDate" ) String endDate );

    int countUserConumeByMember( @Param( "busId" ) Integer busId, @Param( "memberId" ) Integer memberId, @Param( "startDate" ) String startDate,
		    @Param( "endDate" ) String endDate );

    /**
     * 积分卡兑换物品
     */
    List<Map<String, Object>> countJifenDuiHan( @Param( "busId" ) Integer busId );

    int countOrder( @Param( "busId" ) Integer busId, @Param( "ctId" ) Integer ctId );

    int countUseJifen( @Param( "busId" ) Integer busId );

    /**
     * 计算消费总金额
     */
    Double sumXiaofei( @Param( "busId" ) Integer busId, @Param( "ctId" ) Integer ctId );

    /**
     * 计算购买会员卡总金额
     */
    Double buyCard( @Param( "busId" ) Integer busId, @Param( "ctId" ) Integer ctId );

    /**
     * 查询7天的交易总额
     */
    List<Map<String, Object>> sum7DayOrder( @Param( "busId" ) Integer busId, @Param( "ctId" ) Integer ctId, @Param( "date" ) Date date );

    /**
     * 查询7天的售卡交易金额
     */
    List<Map<String, Object>> sum7DayBuyCard( @Param( "busId" ) Integer busId, @Param( "ctId" ) Integer ctId, @Param( "date" ) Date date );

    /**
     * 查询近7天的折扣额
     */
    List<Map<String, Object>> sum7DayDiscount( @Param( "busId" ) Integer busId );

    /**
     * 折扣总额
     */
    Double sumDisCount( @Param( "busId" ) Integer busId );

    /**
     * 查询储值卡充值总额
     */
    Double sumChongzhi( @Param( "busId" ) Integer busId );

    /**
     * 查询近7天走势
     */
    List<Map<String, Object>> sumChongzhi7Day( @Param( "busId" ) Integer busId );

    /**
     * 次卡消费统计
     */
    List<Map<String, Object>> sumCiKa( @Param( "busId" ) Integer busId );

    /**
     * 查询次卡消费次数
     */
    Integer userCiKa( @Param( "busId" ) Integer busId );

    /**
     * 查询订单数据
     */
    UserConsume findBybusIdAndOrderCode( @Param( "busId" ) Integer busId, @Param( "moduleType" ) Integer moduleType, @Param( "orderCode" ) String orderCode );


    UserConsume findUserConsumeByBusIdAndOrderCode( @Param( "busId" ) Integer busId, @Param( "orderCode" ) String orderCode );
}