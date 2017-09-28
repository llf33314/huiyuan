package com.gt.member.dao;

import com.gt.member.entity.DuofenCardReceive;
import com.baomidou.mybatisplus.mapper.BaseMapper;
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
public interface DuofenCardReceiveDAO extends BaseMapper<DuofenCardReceive> {
    /**
     * 分页统计
     * @param publicId
     * @return
     */
    int countCardReceive(@Param("busId")Integer busId);

    /**
     * 分页数据查询
     * @param publicId
     * @return
     */
    List<Map<String, Object>> findCardReceive(@Param("busId")Integer busId, @Param("first")Integer first, @Param("pageSize") Integer pageSize);

    /**
     * 根据code查询卡包
     * @param publicId
     * @param code
     * @return
     */
    DuofenCardReceive findByCode(@Param("busId")Integer busId,@Param("code")String code);

    /**
     * 查询会员等级能领取的优惠券
     * @param receiveDate
     * @param grId
     * @return
     */
    List<Map<String, Object>> findByGtId(@Param("busId")Integer busId, @Param("receiveDate")Date receiveDate, @Param("gtId")Integer grId);



    List<Map<String, Object>> findInIds(@Param("ids")List<Integer> ids);


    List<Map<String, Object>> findById(@Param("id")Integer id,@Param("receiveDate")Date receiveDate);


    /**
     * 第三方优惠券
     * @param publicId
     * @return
     */
    int countThreeCardReceive(@Param("busId")Integer busId);

    /**
     * 三方优惠券分页数据查询
     * @param publicId
     * @return
     */
    List<Map<String, Object>> findThreeCardReceive(@Param("busId")Integer busId,@Param("first")Integer first,@Param("pageSize") Integer pageSize);


    /**
     * 根据公众号查询商场投放包
     * @param busId
     * @return
     */
    List<DuofenCardReceive> findCardReceiveBybusId(@Param("busId")Integer busId);

    /**
     * 根据公众号查询商场投放包
     * @param publicId
     * @return
     */
    List<DuofenCardReceive> findCardReceiveByPublicId(@Param("publicId")Integer PublicId);

    /**
     * 根据公众号查询普通投放卡券
     * @param publicId
     * @return
     */
    List<Map<String, Object>> findCardReceiveBybusId1(@Param("busId")Integer busId);

    /**
     * 根据商家查询商家拥有的卡券信息
     * @param busUserId
     * @return
     */
    List<Map<String, Object>> findByBusUserId(@Param("busUserId")Integer busUserId,@Param("date") Date date);


    /**
     * 根据商家查询商家拥有的卡券信息
     * @param busUserId
     * @return
     */
    List<Map<String, Object>> findToMallByBusUserId(@Param("busUserId")Integer busUserId,@Param("date") Date date);




    /**
     * 统计该会员还有没有领取的卡券信息
     * @param publicId
     * @param memberId
     * @return
     */
    List<Map<String, Object>> findGtIds(@Param("busId")Integer busId,@Param("memberId")Integer memberId,@Param("receiveDate")Date date);

    /**
     * 查询商户下ERP下 免费 或购买的卡包信息
     * @param busId
     * @return
     */
    List<Map<String, Object>> findCardReceiveBydeliveryType1(@Param("busId")Integer busId,@Param("receiveDate")Date receiveDate,@Param("deliveryType1")Integer deliveryType1,@Param("first")Integer first,@Param("pageSize") Integer pageSize);


    /**
     * 查询商户下ERP下 免费 或购买的卡包信息
     * @param busId
     * @return
     */
    int countCardReceiveBydeliveryType1(@Param("busId")Integer busId,@Param("receiveDate")Date receiveDate,@Param("deliveryType1")Integer deliveryType1);



    /**
     * 根据商家查询商家拥有的卡券信息
     * @param busUserId
     * @return
     */
    List<Map<String, Object>> findByBusUserIdAndTime(@Param("busUserId")Integer busUserId,@Param("receiveDate") Date receiveDate);


    List<Map<String, Object>> findCardReceiveById(@Param("id")Integer id);


    /**
     * 查询商户下ERP下 免费 或购买的卡包信息（不分页）
     * @param busId
     * @return
     */
    List<Map<String, Object>> findCardReceivesBydeliveryType1(@Param("busId")Integer busId, @Param("receiveDate")Date receiveDate, @Param("deliveryType1")Integer deliveryType1);



    /**
     * 查询商户下ERP下 免费和购买的卡包信息（不分页）
     * @param busId
     * @return
     */
    List<Map<String, Object>> findCardReceivesBydeliveryTypeAll(@Param("busId")Integer busId,@Param("receiveDate")Date receiveDate);


    List<Map<String, Object>> findFreeCardReceive(@Param("busId")Integer busId,@Param("receiveDate")Date receiveDate);

    /**
     * 查询游戏卡包信息
     * @param busId
     * @param receiveDate
     * @return
     */
    List<Map<String, Object>> findCardRecevice(@Param("busId")Integer busId,@Param("receiveDate")Date receiveDate);

    List<Map<String,Object>> findMeiRongCardReceviceByMemberId(@Param( "memberIds" )List<Integer> memberIds);
}