package com.gt.member.dao;

import com.gt.member.entity.MemberCard;
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
public interface MemberCardDAO extends BaseMapper<MemberCard> {


    List<MemberCard> selectByPrimaryKeys(@Param("ids")String ids);

    /**
     * 查询所有的会员
     * @return
     */
    Integer countCardAll(@Param("busId") Integer busId);


    /**
     * 查询所有的会员领取的card数量
     * @return
     */
    Integer countCardisBinding(@Param("busId") Integer busId);

    Integer countShopIdCardisBinding(@Param("busId") Integer busId,@Param("shopId") Integer shopId);

    Integer countCard(@Param("busId") Integer busId,@Param("ctIds")List<Integer> ctIds);

    Integer countCard1(@Param("busId") Integer busId,@Param("ctId")Integer ctId);

    Integer countCardByTime(@Param("busId") Integer busId,@Param("date") String date);

    Integer countShopIdCardByTime(@Param("busId") Integer busId,@Param("shopId") Integer shopId,@Param("date") String date);

    /**
     * 根据卡号查询
     * @param publicId
     * @param cardNo
     * @return
     */
    Integer countCardByCardNo(@Param("busId") Integer busId,@Param("cardNo") String cardNo);

    List<Map<String,Object>> findCardById(@Param("id") Integer id);

    MemberCard findCardByCardNo(@Param("busId")Integer busId,@Param("cardNo")String cardNo);

    List<Map<String,Object>> findCardAll(@Param("busId") Integer busId);

    List<Map<String,Object>> findCardByCtIds(@Param("busId") Integer busId,@Param("ctIds")List<Integer> ctIds);

    Integer countByNominateCode(@Param("busId") Integer busId,@Param("nominateCode") String nominateCode);

    Integer countBySystemCode(@Param("busId") Integer busId,@Param("systemcode")String systemCode);

    MemberCard findBySystemCode(@Param("busId") Integer busId,@Param("systemcode")String systemCode);


    List<Map<String,Object>> findCardByCtIdsAndGtIds(@Param("publicId") Integer publicId,@Param("ctIds")List<Integer> ctIds,@Param("gtIds")List<Integer> gtIds);


    /**
     * 分组统计会员等级数量
     * @param busId
     * @param ctId
     * @return
     */
    List<Map<String, Object>> findGroupBygtId(@Param("busId")Integer busId, @Param("ctId")Integer ctId);

    /**
     * 查询储值卡会员剩余总金额
     * @param busId
     * @return
     */
    Double sumMoney(@Param("busId")Integer busId);

    Integer sumfrequency(@Param("busId")Integer busId);

    /**
     * ERP会员统计
     * @param busId
     * @return
     */
    List<Map<String, Object>> countMember(@Param("busId")Integer busId);

    /**
     * 查询7天会员新增数量
     * @param busId
     * @return
     */
    List<Map<String,Object>> sum7CardGroupByDate(@Param( "busId" )Integer busId);
}