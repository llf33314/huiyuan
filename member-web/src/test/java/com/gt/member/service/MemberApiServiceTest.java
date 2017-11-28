package com.gt.member.service;

import com.alibaba.fastjson.JSONObject;
import com.gt.member.BasicTest;
import com.gt.member.dao.MemberCardDAO;
import com.gt.member.service.memberApi.MemberApiService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/8/8 0008.
 */
public class MemberApiServiceTest extends BasicTest{
    @Autowired
    private MemberApiService memberApiService;



    @Autowired
    private MemberCardDAO memberCardDAO;


    @Test
    public void findByMemberId(){
	List<Map<String, Object>> map= memberCardDAO.findGroupBygtId(42,1);
        //MemberCard c=memberCardDAO.selectById( 296 );

        //	Member member = memberDAO.selectById( 194 );
	Assert.assertNotNull( map );
	logger.info( "======>> {}", JSONObject.toJSONString( map ) );
    }}
