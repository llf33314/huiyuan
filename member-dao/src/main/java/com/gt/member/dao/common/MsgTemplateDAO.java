package com.gt.member.dao.common;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gt.common.entity.MsgTemplate;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
  *  Mapper 接口
 * </p>
 *
 * @author pengjiangli
 * @since 2017-10-18
 */
public interface MsgTemplateDAO extends BaseMapper<MsgTemplate > {

    /**
     * 获取模板信息（id，title）
     * @param busId
     * @return
     */
    List<Map<String, Object> > selectTempObjByBusId(@Param("busId")Integer busId);
}