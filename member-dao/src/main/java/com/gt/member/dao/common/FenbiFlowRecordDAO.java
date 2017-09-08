package com.gt.member.dao.common;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gt.common.entity.FenbiFlowRecord;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
  *  Mapper 接口
 * </p>
 *
 * @author pengjiangli
 * @since 2017-08-18
 */
public interface FenbiFlowRecordDAO extends BaseMapper<FenbiFlowRecord > {

    Double getFenbiSurplus(@Param("userId")Integer userId,
		    @Param("rec_type")Integer rec_type,
		    @Param("fre_type")Integer fre_type,
		    @Param("fkId")Integer fkId);

    /**
     * 更改粉币冻结额度(已使用加)
     * @param userId
     * @param num 数量
     * @param fkId 外键
     * @return
     */
    int updateFenbiReduce(
		    @Param("userId")Integer userId,
		    @Param("num")Integer num,
		    @Param("fkId")Integer fkId,
		    @Param("fre_type")Integer fre_type);

}