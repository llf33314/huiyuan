package com.gt.member.service.common.membercard;

import com.gt.api.util.RequestUtils;
import com.gt.member.exception.BusinessException;
import com.gt.util.entity.param.fenbiFlow.AdcServicesInfo;
import com.gt.util.entity.param.sms.NewApiSms;
import com.gt.util.entity.param.sms.OldApiSms;
import com.gt.util.entity.param.wx.SendWxMsgTemplate;

/**
 * 请求接口
 * Created by Administrator on 2017/10/24.
 */
public interface RequestService {

    /**
     * 微信卡券核销
     * @param cardId
     * @param code
     * @param busId
     */
    public String codeConsume(String cardId,String code,Integer busId)throws Exception;

    /**
     * 短信发送
     * @param requestUtils
     */
    public void sendSms(RequestUtils<OldApiSms> requestUtils);


    public String sendSmsNew(RequestUtils<NewApiSms > requestUtils);

    /**
     * 消息模板发送
     * @param sendWxMsgTemplate
     */
    public void setSendWxmsg(SendWxMsgTemplate sendWxMsgTemplate);


    /**
     * 子账号商家输入主商家的用户名和密码进行验证
     * @param userName 用户名
     * @param userPwd  密码
     * @param childId 子账户id
     * @return
     */
    public boolean verificationBusUser(String userName,String userPwd,Integer childId)throws BusinessException;

    /**
     * 流量兑换
     * @param requestUtils
     * @return
     */
    public String changeFlow(RequestUtils<AdcServicesInfo > requestUtils);

}
