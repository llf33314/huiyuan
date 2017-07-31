package com.gt.member.dao;

import com.gt.member.entity.MemberGift;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
  * 会员赠送积分 粉币 流量 优惠券 Mapper 接口
 * </p>
 *
 * @author pengjiangli
 * @since 2017-07-25
 */
public interface MemberGiftDAO extends BaseMapper<MemberGift> {

    List<Map<String,Object>> findBybusId(@Param("busId")Integer busId);

    MemberGift findBybusIdAndmodelCode(@Param("busId")Integer busId,@Param("modelCode")Integer modelCode);
}