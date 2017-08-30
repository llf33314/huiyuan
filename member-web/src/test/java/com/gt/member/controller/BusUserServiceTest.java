package com.gt.member.controller;


import com.gt.api.exception.SignException;
import com.gt.api.util.sign.SignHttpUtils;
import com.gt.member.BasicTest;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhangmz
 * @create 2017/7/9
 */
public class BusUserServiceTest extends BasicTest {



    @Test
    public void testSelect() throws SignException {
//        for ( int i=0;i<1;i++ ) {
//	    Long startTime = new Date().getTime();
//	    Map< String,Object > map = new HashMap<>();
//	    map.put( "memberId", 1225352 );
//	    map.put( "shopId", 17 );
//	    String res = SignHttpUtils.postByHttp( "http://192.168.2.240:8090/memberAPI/member/findCardByMembeId", map, "MV8MMFQUMU1HJ6F2GNH40ZFJJ7Q8LNVM" );
//	    System.out.print( res );
//	    Long endTime = new Date().getTime();
//	  logger.error( "用时:"+(endTime-startTime) );
//	}


	Map<String, Object> map = new HashMap<>();
	map.put("memberId", "1225636");
	map.put("shopId", "17");

	//SignHttpUtils.postByHttp("http://192.168.2.240:8090/api/memberApi/findByMemberId",map,"MV8MMFQUMU1HJ6F2GNH40ZFJJ7Q8LNVM");


	String aa= SignHttpUtils.postByHttp("http://192.168.2.240:8090/memberAPI/member/findCardByMembeId.do",map,"MV8MMFQUMU1HJ6F2GNH40ZFJJ7Q8LNVM");
	System.out.println(aa);
    }

}