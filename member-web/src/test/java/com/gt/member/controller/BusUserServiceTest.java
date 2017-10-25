package com.gt.member.controller;


import com.gt.api.exception.SignException;
import com.gt.api.util.sign.SignHttpUtils;
import com.gt.member.BasicTest;
import com.gt.member.util.HttpClienUtil;
import net.sf.json.JSONObject;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import springfox.documentation.spring.web.json.Json;

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
//	for(int i=0;i<100000000;i++) {
//	    String uname = "";
//	    String upwd = "";
//	    String url = "http://www.pcac.org.cn/index.php/common/login.html";
//	    Map< String,Object > map = new HashMap<>();
//	    map.put( "uname", 1 );
//	    map.put( "upwd", 2 );
//	    try {
//		JSONObject aa = HttpClienUtil.httpPost( url, JSONObject.fromObject( map ), false );
//		System.out.print( aa );
//	    } catch ( Exception e ) {
//
//	    }
//	}
//
//	String wxmpsignKey=memberConfig.getWxmpsignKey();
//	String socketUrl="http://sz.yifriend.net/8A5DA52E/socket/getSocketApi.do";
//	Map<String,Object> socketMap=new HashMap<>(  );
//	socketMap.put( "pushName","member_count_"+23 );
//	String ss=SignHttpUtils.WxmppostByHttp( socketUrl, socketMap, wxmpsignKey );  //推送
//	System.out.println(ss);
	Map<String, Object> map = new HashMap<>();
	map.put("memberId", "1071");
	map.put("receiceId", "434");
	String aa=SignHttpUtils.postByHttp("http://192.168.2.240:8888/memberAPI/cardCouponseApi/findMeiRongDuofenCardGetByReceiveId",map,"MV8MMFQUMU1HJ6F2GNH40ZFJJ7Q8LNVM");
	System.out.println(aa);

    }

}