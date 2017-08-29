package com.gt.member.controller;

import com.gt.member.base.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/7/27 0027.
 */
@Controller
public class TestController extends BaseController {

    @ResponseBody
    @GetMapping( { "", "/" } )
    public String hello() {
	return "你好..";
    }


    @RequestMapping(value = "/test")
    public String toIndex(){
	return "/index";
    }


    public static  void main(String[] arg){
	Map<String, Object> map = new HashMap<>();
	map.put("memberId", 197);
	map.put("busId", 42);
	map.put("page", 1);
	//SignHttpUtils.postByHttp("http://192.168.2.240:8090/api/memberApi/findByMemberId",map,"MV8MMFQUMU1HJ6F2GNH40ZFJJ7Q8LNVM");


	String aa=SignHttpUtils.postByHttp("POST /memberAPI/cardCouponseApi/findCardReceiveBuy",map,"MV8MMFQUMU1HJ6F2GNH40ZFJJ7Q8LNVM");
	System.out.println(aa);
    }

}