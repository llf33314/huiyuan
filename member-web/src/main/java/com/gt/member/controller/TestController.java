package com.gt.member.controller;

import com.gt.api.util.sign.SignHttpUtils;
import com.gt.member.base.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
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
    }

}