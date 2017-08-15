package com.gt.member.dao.common;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gt.common.entity.DictItems;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
import java.util.SortedMap;

/**
 * <p>
  *  Mapper 接口
 * </p>
 *
 * @author pengjiangli
 * @since 2017-08-15
 */
public interface DictItemsDAO extends BaseMapper<DictItems > {

    /**
     * 查询字典 键值对
     * @return
     */
    List<Map<String,Object>> getDictReturnKeyAndValue(@Param( "dictType" )String dictType);

    /**
     * 查询字典 返回value 值
     * @param key
     * @return
     */
    String getDiceReturnValue(@Param( "dictType" )String dictType,@Param( "key" )Integer key);

    /**
     * 查询会员数量
     * @param dictstyle
     * @param level
     * @return
     */
    List<Map<String,Object>> findMemberCountByUserId(@Param( "dictstyle" )String dictstyle,@Param( "level" )Integer level);

}