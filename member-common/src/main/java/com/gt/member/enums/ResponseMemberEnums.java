package com.gt.member.enums;

/**
 * Created by Administrator on 2017/8/3 0003.
 */
public enum ResponseMemberEnums {
    ERROR_QR_CODE(4001, "二维码已超时"),
    NO_DATA(4002, "数据不存在"),
    NULL(4003,"数据空值"),



    CARD_STATUS(5001, "会员卡已停用"),
    MEMBER_LESS_JIFEN(5002,"积分不足"),
    MEMBER_LESS_FENBI(5003,"粉币不足"),
    MEMBER_NOT_CARD(5004,"会员卡不存在"),
    NOT_MEMBER_CAR(5005,"非会员"),
    MEMBER_LESS_MONEY(5006,"余额不足"),
    MEMBER_CHUZHI_CARD(5007,"非储值会员卡"),



    COUPONSE_VERIFICATION(6001, "卡券核销失败"),
    COUPONSE_NO_NUM(6002, "券包您已领取完"),
    COUPONSE_NO_NUM_TODAY(6003,"券包您今天已领取完"),
    COUPONSE_NO_EXIST(6004,"卡券不存在"),
    COUPONSE_NO_GUOQI(6005,"卡券过期或已核销,不能执行卡券核销操作"),
    ;

    private Integer code;
    private String msg;


    public Integer getCode() {
        return code;
    }


    public String getMsg() {
        return msg;
    }

    ResponseMemberEnums(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
