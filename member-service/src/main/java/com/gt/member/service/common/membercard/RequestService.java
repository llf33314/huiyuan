package com.gt.member.service.common.membercard;

import com.gt.api.util.RequestUtils;
import com.gt.member.exception.BusinessException;
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
     * 判断商家是否过期
     * @param busId
     */
    public void getWxPulbicMsg(Integer busId)throws BusinessException;

}
