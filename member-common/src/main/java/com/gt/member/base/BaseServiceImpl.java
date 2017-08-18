package com.gt.member.base;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * BaseServiceImpl
 *
 * @author zhangmz
 * @create 2017/7/10
 */
public class BaseServiceImpl< M extends BaseMapper< T >, T > extends ServiceImpl< M,T > implements BaseService< T > {
    protected  static final Logger LOGGER = LoggerFactory.getLogger(BaseService.class);
}
