package com.gt.member.dao.duofencard;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gt.duofencard.entity.DuofenCardTime;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
  *  Mapper 接口
 * </p>
 *
 * @author pengjiangli
 * @since 2018-01-16
 */
public interface DuofenCardTimeDAO extends BaseMapper<DuofenCardTime> {

    public DuofenCardTime findDuofenCardTimeByCardId(@Param( "cardId" )Integer cardId);
}