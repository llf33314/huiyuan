package com.gt.member.dao;

import com.gt.member.entity.MemberCardrecordNew;
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
 * @since 2017-10-23
 */
public interface MemberCardrecordNewDAO extends BaseMapper<MemberCardrecordNew> {


    List<Map<String,Object>> findCardrecordByMemberId( @Param( "memberIds" ) List<Integer> memberIds, @Param( "page" ) Integer page, @Param( "pageSize" ) Integer pageSize );

    List<Map<String,Object>> findCardrecordByMemberIdAndRecordType( @Param( "memberIds" ) List<Integer> memberIds, @Param( "recordType" ) Integer recordType, @Param( "page" ) Integer page,
		    @Param( "pageSize" ) Integer pageSize );

}