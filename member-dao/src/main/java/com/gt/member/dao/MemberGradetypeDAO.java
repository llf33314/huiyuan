package com.gt.member.dao;

import com.gt.member.entity.MemberGradetype;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
  * 卡片模板信息表 Mapper 接口
 * </p>
 *
 * @author pengjiangli
 * @since 2017-07-25
 */
public interface MemberGradetypeDAO extends BaseMapper<MemberGradetype> {

    /**
     * 查询商户拥有的卡片类型
     * @param busId
     * @return
     */
    List<Map<String,Object>> findBybusId(@Param("busId") Integer busId);

    List<Map<String,Object>> findBybusIdAndCtId(@Param("busId") Integer busId,@Param("ctId")Integer ctId);

    List<Map<String,Object>> findBybusIdAndCtId1(@Param("busId") Integer busId,@Param("ctId")Integer ctId);

    List<Map<String,Object>> findBybusIdAndCmType(@Param("busId") Integer busId,@Param("cmType")Integer cmType);


    List<Map<String, Object>> findByPulicIdAndCtId2(@Param("busId") Integer busId,@Param("ctId")Integer ctId);

    int deleteBybusIdAndCtId(@Param("busId") Integer busId,@Param("ctId")Integer ctId);

    List<Map<String, Object>> findBybusId1(@Param("busId")Integer busId);

    /**
     * 兼容数据
     * @param busId
     * @return
     */
    List<Map<String, Object>> findByPublicId1(@Param("publicId")Integer publicId);


    List<Map<String, Object>> findBybusId2(@Param("busId")Integer busId,@Param("ctId")Integer ctId);

    List<Map<String,Object>> findBybusIdAndCtId3(@Param("busId") Integer busId,@Param("ctId")Integer ctId);

    List<Map<String, Object>> findByCtId(@Param("busId")Integer busId,@Param("ctId")Integer ctId);

    int updateByCtId(@Param("busId")Integer busId,@Param("ctId")Integer ctId,@Param("isChecked")Byte isChecked,
                     @Param("applyType")Byte applyType,@Param("giveIntegral") Integer giveIntegral,@Param("isView") Byte isView,
                     @Param("givefenbi") Integer givefenbi,@Param("giveflow") Integer giveflow,
                     @Param("isUploadImg") Byte isUploadImg,@Param("imgExplain") String imgExplain,
                     @Param("giveMoney") Double giveMoney,@Param("pickMoney") Double pickMoney);

    List<Map<String, Object>> findByApplyType(@Param("busId")Integer busId);

    /**
     * 商城调用
     * @param busId
     * @return
     */
    List<Map<String, Object>> findBybusId3(@Param("busId")Integer busId);


    int updateUrlByCtId(@Param("busId")Integer busId,@Param("ctId")Integer ctId,@Param("url")String url);

    List<Map<String, Object>> findAllBybusId(@Param("busId")Integer busId,@Param("ctId")Integer ctId);

    List<Map<String, Object>> findGroupByctId(@Param("busId")Integer busId,@Param("ctId")Integer ctId);

    List<Map<String, Object>>  findByIseasy(@Param("busId")Integer busId,@Param("ctId")Integer ctId);

    Integer countIseasy(@Param("busId")Integer busId);

    List<Map<String, Object>> findByBusIdAndCtIdAndIseasy(@Param("busId") Integer busId,@Param("ctId")Integer ctId);

    List<Map<String, Object>> findAllByBusIdAndCtId(@Param("busId") Integer busId,@Param("ctId")Integer ctId);


    Integer updateEasyApplyByCtId(@Param("busId") Integer busId,@Param("ctId")Integer ctId,@Param("easyApply")Integer easyApply);


    MemberGradetype findByIsesasy(@Param("busId") Integer busId);


    Integer updateEasyApplyBybusId(@Param("busId") Integer busId);

    Integer countIspublish(@Param("busId") Integer busId);

    List<Map<String, Object>> findGradeTyeBybusId(@Param("busId") Integer busId);

    /**
     * 查询出所有的简易会员卡信息
     * @param busId
     * @return
     */
    List<Map<String, Object>> findCtIdByIseasy(@Param("busId") Integer busId);

    /**
     * 查询出能升级的卡片类型
     */
    List<Map<String, Object>> findByBusIdAndIsPulish1(@Param("busId") Integer busId);


    /**
     * 查询会员等级
     * @param busId
     * @param ctId
     * @return
     */
    List<Map<String, Object>> findGtName(@Param("busId") Integer busId,@Param("ctIds")List<Integer> ctIds);

    List<Map<String, Object>> findGtNameByGtId(@Param("busId") Integer busId,@Param("gtIds")List<Integer> gtIds);


    /**
     * 查询是否开启会员日
     * @param busId
     * @param ctId
     * @return
     */
    MemberGradetype findIsmemberDateByCtId(@Param("busId")Integer busId,@Param("ctId")Integer ctId);

    /**
     * 查询启赠送金额
     * @param busId
     * @param ctId
     * @return
     */
    MemberGradetype findGiveMoney(@Param("busId")Integer busId,@Param("ctId")Integer ctId);

    /**
     * 查询商家非购买会员卡信息
     * @param busId
     * @return
     */
    List<Map<String, Object>> findGradeTypeByApplyType(@Param("busId")Integer busId);

    /**
     * 查询购买的会员卡
     * @param busId
     * @return
     */
    List<Map<String,Object>> findBuyGradeType(@Param( "busId" )Integer busId);
}