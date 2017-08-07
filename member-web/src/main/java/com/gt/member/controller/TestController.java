package com.gt.member.controller;

import com.gt.member.base.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Administrator on 2017/7/27 0027.
 */
@Controller
@RequestMapping( "/" )
public class TestController extends BaseController {

    @ResponseBody
    @GetMapping( { "", "/" } )
    public String hello() {
	return "你好..";
    }


    @RequestMapping(value = "/test",method = RequestMethod.GET)
    public String toIndex(){
	return "/index.jsp";
    }
}