package com.gt.member.dao.duofencard;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gt.duofencard.entity.DuofenCardShare;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
  *  Mapper 接口
 * </p>
 *
 * @author pengjiangli
 * @since 2018-01-23
 */
public interface DuofenCardShareDAO extends BaseMapper<DuofenCardShare > {

    List<Map<String,Object>> findDuofenCardShareByMemeberId(@Param( "memberId" ) Integer memberId);

    /**
     * 查询分享的券
     * @param getId
     * @param memberId
     * @return
     */
    Integer countByGetIdAndMemberId(@Param( "getId" )Integer getId,@Param( "memberId" )Integer memberId);

}