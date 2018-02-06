package com.gt.member.dao.duofencard;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gt.duofencard.entity.DuofenCardPublish;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
  *  Mapper 接口
 * </p>
 *
 * @author pengjiangli
 * @since 2018-01-16
 */
public interface DuofenCardPublishDAO extends BaseMapper<DuofenCardPublish > {


    public List<Map<String,Object>> findPublishDuofenCard(@Param( "busId" )Integer busId,@Param( "page" )Integer page,@Param( "pageSize" )Integer pageSize);

    public DuofenCardPublish findByCardId(@Param( "cardId" )Integer cardId);


    public List<Map<String,Object>> findPublishByCardId(@Param( "cardId" )Integer cardId);


    public List<Map<String,Object>> findPublishDuofenCardByPublishId(@Param( "publishId" )Integer publishId);

}