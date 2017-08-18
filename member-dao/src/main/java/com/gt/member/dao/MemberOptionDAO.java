package com.gt.member.dao;

import com.gt.member.entity.MemberOption;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
  * 会员资料设置信息表 Mapper 接口
 * </p>
 *
 * @author pengjiangli
 * @since 2017-07-25
 */
public interface MemberOptionDAO extends BaseMapper<MemberOption> {
    /**
     * 根据公众号id查询
     * @param publicId
     * @return
     */
    MemberOption findByBusId(@Param("busId") Integer busId);
}