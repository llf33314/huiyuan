package com.gt.member.controller;

import com.gt.member.base.BaseController;
import com.gt.member.util.sign.SignHttpUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

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
	map.put("id", 197);
	SignHttpUtils.postByHttp("http://192.168.2.240:8090/api/memberApi/findByMemberId",map,"MV8MMFQUMU1HJ6F2GNH40ZFJJ7Q8LNVM");
    }

}