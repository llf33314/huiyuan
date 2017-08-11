package com.gt.member.controller;


import com.gt.member.BasicTest;
import com.gt.member.util.sign.SignHttpUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhangmz
 * @create 2017/7/9
 */
public class BusUserServiceTest extends BasicTest {



    @Test
    public void testSelect() {
	Map<String, Object> map = new HashMap<>();
	map.put("id", 197);
	String res=SignHttpUtils.postByHttp("http://192.168.2.240:8090/api/memberApi/findByMemberId",map,"MV8MMFQUMU1HJ6F2GNH40ZFJJ7Q8LNVM");
	System.out.print( res );

    }

}