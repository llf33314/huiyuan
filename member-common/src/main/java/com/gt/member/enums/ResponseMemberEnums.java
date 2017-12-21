package com.gt.member.enums;

/**
 * Created by Administrator on 2017/8/3 0003.
 */
public enum ResponseMemberEnums {
    ERROR_USER_DEFINED(4000,""),  //接口返回自定义异常
    ERROR_QR_CODE(4001, "二维码已超时"),
    NO_DATA(4002, "数据不存在"),
    NULL(4003,"数据空值"),
    NO_PHONE_CODE(4004,"短信验证码过时或不存在"),
    VERIFICATION_BUSUSER(4005,"用户权限不足,请登录主账户操作"),  //判断主账户信息
    USERGRANT(4006,"粉丝未授权"),
    LOGIN_FAIL(4007,"用户不存在"),
    QUERY_SHOP_BUSID(4008,"查询子账户管理的门店信息异常"),
    MANAGE_SHOP_THAN2(4009,"用户管理了多个门店,不能操作当前业务"),
    PLESAS_SET_SHOP(4010,"请设置主门店信息"),
    PLEASE_PHONE_CODE(4011,"请输入验证码"),
    PLEASE_LOGIN(4012,"请登录"),

    CARD_STATUS(5001, "会员卡已停用"),
    MEMBER_LESS_JIFEN(5002,"积分不足"),
    MEMBER_LESS_FENBI(5003,"粉币不足"),
    MEMBER_NOT_CARD(5004,"会员卡不存在"),
    NOT_MEMBER_CAR(5005,"非会员"),
    MEMBER_LESS_MONEY(5006,"余额不足"),
    MEMBER_CHUZHI_CARD(5007,"非储值会员卡"),
    NOT_ORDER_DATA(5008,"订单数据不存在"),
    NOT_MEMBER_COUNT(5009,"会员卡数量不足"),
    NOT_FIND_CHONGZHI_MSG(5010,"未查询到充值信息"),
    IS_MEMBER_CARD(5011,"粉丝已成为会员"),
    IS_BINDING_PHONE(5012,"手机号码已经绑定过"),
    LESS_THAN_CARD(5013,"储值卡借款金额不足"),
    LESS_THAN_FENBI(5014,"商家粉币不足"),
    PLEASE_SCAN_CODE(5015,"请扫码核销"),
    DISABLE_MEMBER_CARD(5016,"会员卡被拉黑"),
    PLEASE_BUY_CARD(5017,"支付地址"),
    NOT_MEMBER_PHONE(5018,"会员手机号码不存在"),
    LESS_THAN_FLOE(5019,"流量不足"),
    ERROR_CHARGE_FLOW(5020,""),  //流量兑换失败
    NO_DELETE_CARD(5021,"会员卡已经领取,不能删除会员卡"),
    SMS_BIG_THAN_70(5022,"短信内容请输入70字以内"),
    NOT_RECHARGE(5023,"未查询到改充值规则"),
    NOT_TIME_CARD(5024,"未查询到时效卡充值规则"),
    PLEASE_GET_CARD(5025,"请领取会员卡"),
    PLEASE_BINDING_PHONE(5026,"请绑定手机号码"),
    GET_JBARCODE_ERROR(5027,"条形码异常"),
    NOT_SET_QIANDAO(5028,"商家未设置签到"),
    MEMBER_QIANDAO(5029,"亲,你已经签到过"),
    NOT_RECHARGE_JFIEN(5030,"积分卡不能充值"),
    SEND_NOTICE(5031,"模板已经发送不能删除"),



    COUPONSE_VERIFICATION(6001, "卡券核销失败"),
    COUPONSE_NO_NUM(6002, "券包您已领取完"),
    COUPONSE_NO_NUM_TODAY(6003,"券包您今天已领取完"),
    COUPONSE_NO_EXIST(6004,"卡券不存在"),
    COUPONSE_NO_GUOQI(6005,"卡券过期或已核销,不能执行卡券核销操作"),



    NOT_PAY_TYPE(7001,"不存在当前支付方式"),
    LESS_THAN_CASH(7002,"收取现金不够"),
    PLEASE_PAY(7003,"请支付购买会员卡金额"),
    ORDER_PAY_REPEAT(7004,"订单重复支付"),
    ORDER_PAY_ERROR(7005,"当前订单支付存在问题,将影响收入"),
    NOT_ORDER(7006,"订单不存在"),
    END_ORDER(7007,"订单已终结,不能退款"),
    REPEAT_ORDER(7008,"订单号重复"),
    INVALID_SESSION(7009,"SESSION失效,请重新登录"),


    GIFT_EXIST(8001,"赠送物品已存在,请修改"),
    CAN_NOT_MINUS(8002,"不能是负值"),
    IMP_ERROR(8003,"导入数据异常,请查看下载的错误信息")


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
