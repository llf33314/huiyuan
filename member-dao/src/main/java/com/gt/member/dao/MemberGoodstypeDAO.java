package com.gt.member.dao;

import com.gt.member.entity.MemberGoodstype;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
  * 赠送物品信息表 Mapper 接口
 * </p>
 *
 * @author pengjiangli
 * @since 2017-07-25
 */
public interface MemberGoodstypeDAO extends BaseMapper<MemberGoodstype> {

    /**
     * 获取平台赠送物品
     * @return
     */
    List<MemberGoodstype> findBy0(@Param("gradeIds")List<Integer> gradeIds);

    /**
     * 查询未赠送的物品
     * @param gradeIds
     * @param goodTypeIds
     * @return
     */
    List<MemberGoodstype> findNotInidAndInGtypeId(@Param("gradeIds") List<Integer> gradeIds,@Param("goodTypeIds") List<Integer> goodTypeIds);

}