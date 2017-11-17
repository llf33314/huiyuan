package com.gt.member.controller;

import com.gt.member.dto.ServerResponse;
import com.gt.member.service.common.quartz.MemberQuartzService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 定时任务api接口
 */
@Api(value = "定时任务api接口",description = "定时任务api接口")
@Controller
@RequestMapping("/memberAPI/memberQuartz")
public class MemberQuartzController {

    @Autowired
    private MemberQuartzService memberQuartzService;

    /**
     * 过期卡券 修改领取卡券状态
     */
    @RequestMapping( value = "/updateCardGet", method = RequestMethod.POST )
    public ServerResponse updateCardGet(HttpServletRequest request, HttpServletResponse response){
        try {
            memberQuartzService.updateCardGet();
            return ServerResponse.createBySuccess(  );
        } catch ( Exception e ) {
            return ServerResponse.createByError( "错误" );
        }
    }

    /**
     * 修改会员消息信息
     */
    @RequestMapping( value = "/updateNotice", method = RequestMethod.POST )
    public ServerResponse updateNotice(HttpServletRequest request, HttpServletResponse response){
        try {
            memberQuartzService.updateNotice();
            return ServerResponse.createBySuccess(  );
        } catch ( Exception e ) {
            return ServerResponse.createByError( "错误" );
        }
    }


    /**
     * 定时发送短信
     */
    @RequestMapping( value = "/sendNotice", method = RequestMethod.POST )
    public ServerResponse sendNotice(HttpServletRequest request, HttpServletResponse response){
        try {
            memberQuartzService.sendNotice();
            return ServerResponse.createBySuccess(  );
        } catch ( Exception e ) {
            return ServerResponse.createByError( "错误" );
        }
    }

    /**
     * 积分清0 七天前短信 或系统消息提醒
     */
    @RequestMapping( value = "/clearJifenSendMessage", method = RequestMethod.POST )
    public ServerResponse clearJifenSendMessage(HttpServletRequest request, HttpServletResponse response){
        try {
            memberQuartzService.clearJifenSendMessage();
            return ServerResponse.createBySuccess(  );
        } catch ( Exception e ) {
            return ServerResponse.createByError( "错误" );
        }
    }

    /**
     * 每月22号触发 短信
     */
    @RequestMapping( value = "/clearJifenSendSmsMessage", method = RequestMethod.POST )
    public ServerResponse  clearJifenSendSmsMessage(HttpServletRequest request, HttpServletResponse response){
        try {
            memberQuartzService.clearJifenSendSmsMessage();
            return ServerResponse.createBySuccess(  );
        } catch ( Exception e ) {
            return ServerResponse.createByError( "错误" );
        }
    }


    /**
     * 积分清0
     */
    @RequestMapping( value = "/clearJifen", method = RequestMethod.POST )
    public ServerResponse clearJifen(HttpServletRequest request, HttpServletResponse response){
        try {
            memberQuartzService.clearJifen();
            return ServerResponse.createBySuccess(  );
        } catch ( Exception e ) {
            return ServerResponse.createByError( "错误" );
        }
    }

    /**
     * 会员生日推送
     */
    @RequestMapping( value = "/birthdayMsg", method = RequestMethod.POST )
    public ServerResponse  birthdayMsg(HttpServletRequest request, HttpServletResponse response){
        try {
            memberQuartzService.birthdayMsg();
            return ServerResponse.createBySuccess(  );
        } catch ( Exception e ) {
            return ServerResponse.createByError( "错误" );
        }
    }

    /**
     * 会员生日发送短信
     */
    @RequestMapping( value = "/sendBir", method = RequestMethod.POST )
    public ServerResponse sendBir(HttpServletRequest request, HttpServletResponse response){
        try {
            memberQuartzService.sendBir();
            return ServerResponse.createBySuccess(  );
        } catch ( Exception e ) {
            return ServerResponse.createByError( "错误" );
        }
    }


    /**
     * 短信发送
     */
    @RequestMapping( value = "/birthdaySms", method = RequestMethod.POST )
    public ServerResponse birthdaySms(HttpServletRequest request, HttpServletResponse response){
        try {
            memberQuartzService.birthdaySms();
            return ServerResponse.createBySuccess(  );
        } catch ( Exception e ) {
            return ServerResponse.createByError( "错误" );
        }
    }
}
