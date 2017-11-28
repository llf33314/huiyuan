package com.gt.member.controller;


import com.gt.api.exception.SignException;
import com.gt.api.util.sign.SignHttpUtils;
import com.gt.member.BasicTest;
import com.gt.member.util.PropertiesUtil;
import net.sf.json.JSONObject;
import org.junit.Test;

import java.util.Date;
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

//	for(int i=0;i<1000;i++) {
//	    long start=new Date(  ).getTime();
//
//	    String wxmpsignKey = "WXMP2017";
//	    String socketUrl = "https://nb.deeptel.com.cn/8A5DA52E/socket/getSocketApi.do";
//	    Map< String,Object > socketMap = new HashMap<>();
//	    socketMap.put( "pushName", 11 );
//	    socketMap.put( "pushMsg", 11 );
//
//	    String ss = SignHttpUtils.WxmppostByHttp( socketUrl, socketMap, wxmpsignKey );  //推送
//	    System.out.println("第"+i+"请求数据返回数据"+ ss );
//	    long end=new Date(  ).getTime();
//	    System.out.println("第"+i+"请求超时时间:"+(end-start));
//	}
	for(int i=0 ;   i<100;i++) {
	    long start = new Date().getTime();
	    Map< String,Object > map = new HashMap<>();
	    map.put( "busId", "36" );
	    map.put( "memberId", "1225510" );
	    map.put( "code", "am3the" );
	    //map.put("receiceId", "434");
	    String aa = SignHttpUtils.WxmppostByHttp( "http://member.yifriend.net/memberAPI/cardCouponseApi/lingquDuofenCardReceive", map, "MV8MMFQUMU1HJ6F2GNH40ZFJJ7Q8LNVM" );
	    System.out.println( aa );
	    long end = new Date().getTime();
	    System.out.println( "第请求超时时间:" + ( end - start ) );
	}
    }

}