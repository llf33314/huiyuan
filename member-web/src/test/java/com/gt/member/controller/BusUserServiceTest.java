package com.gt.member.controller;


import com.alibaba.fastjson.JSON;
import com.gt.api.exception.SignException;
import com.gt.api.util.HttpClienUtils;
import com.gt.api.util.RequestUtils;
import com.gt.api.util.sign.SignHttpUtils;
import com.gt.member.BasicTest;
import com.gt.member.dao.MemberEntityDAO;
import com.gt.member.entity.MemberEntity;
import com.gt.member.enums.ResponseMemberEnums;
import com.gt.member.exception.BusinessException;
import com.gt.member.service.common.membercard.RequestService;
import com.gt.member.util.HttpClienUtil;
import com.gt.member.util.PropertiesUtil;
import com.gt.util.entity.result.shop.WsWxShopInfo;
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

    @Autowired
    private MemberEntityDAO memberEntityDAO;

    @Autowired
    private RequestService requestService;


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
//	String wxmpsignKey="MV8MMFQUMU1HJ6F2GNH40ZFJJ7Q8LNVM";
//	String socketUrl="https://member.deeptel.com.cn/memberAPI/member/updateMemberPhoneByMemberId";
//	Map<String,Object> socketMap=new HashMap<>(  );
//	socketMap.put( "memberId",1225936 );
//	socketMap.put( "busId",36 );
//	socketMap.put( "phone","15986670850" );
//	String ss=SignHttpUtils.WxmppostByHttp( socketUrl, socketMap, wxmpsignKey );  //推送
//	System.out.println(ss);
	//System.out.println(aa);

	//requestService.getPowerApi( 0,42,1.0,"消费" );


	Map< String,Object > map = new HashMap<>();
	map.put( "busId", "33" );
	map.put( "shopId", "23" );
	map.put( "cardNo", "18688980706" );
//	map.put( "startTime","1511107200" );
//	//	//{cardNo=13510941813,busId=42,schoolId=1,shopId=17}
//	//
//	//
//	//https://member.deeptel.com.cn//memberAPI/cardCouponseApi/findCardReceive==={"busId":36,"memberId":1225628,"page":1}
//	//http://113.106.202.53:13887/memberAPI/member/findMemberCard.do
	String url="http://member.yifriend.net/memberAPI/member/findMemberCard.do";

	String bbb = SignHttpUtils.WxmppostByHttp( url, map, "MV8MMFQUMU1HJ6F2GNH40ZFJJ7Q8LNVM" );
	System.out.println( bbb );

//	String url = "https://deeptel.com.cn/8A5DA52E/shopapi/6F6D9AD2/79B4DE7C/selectMainShopByBusId.do";
//	RequestUtils< Integer > requestUtils = new RequestUtils<>();
//	requestUtils.setReqdata( 36 );
//	String shopStr = HttpClienUtils.reqPostUTF8( com.alibaba.fastjson.JSONObject.toJSONString( requestUtils ), url, String.class, "WXMP2017");
//	com.alibaba.fastjson.JSONObject json = JSON.parseObject( shopStr );
//	if ( "0".equals( json.getString( "code" ) ) ) {
//	    WsWxShopInfo wsWxShopInfo = JSON.parseObject( json.getString( "data" ), WsWxShopInfo.class );
//	} else {
//	    throw new BusinessException( ResponseMemberEnums.QUERY_SHOP_BUSID );
//	}

    }

}