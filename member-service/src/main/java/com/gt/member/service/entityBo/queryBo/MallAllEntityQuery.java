package com.gt.member.service.entityBo.queryBo;

import java.util.List;

/**
 * 总订单请求参数  包含非业务参数
 *
 * @author pengjiangli
 */
public class MallAllEntityQuery {
    private String orderCode;  //订单号
    private Double totalMoney = 0.0; //订单总金额
    private List<MallEntityQuery> malls;  //商品订单详情

    private Integer ucType;   //消费类型  请查看1197消费类型  没有请添加
    private Integer busId;  //商家主账户id
    private Integer shopId;  //门店

    private Double derateMoney;  //减免金额 计算完减去减免金额

    private String successNoticeUrl;  //支付成功 通知地址

    private String jumpUrl;  //支付成功跳转地址

    private String sign;  //支付成功回调秘钥

    private Integer jumphttpPOST=0;  //回调请求方式 0默认postByHttp   1：WxmppostByHttp请求方式


    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode( String orderCode ) {
        this.orderCode = orderCode;
    }

    public Double getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney( Double totalMoney ) {
        this.totalMoney = totalMoney;
    }

    public List< MallEntityQuery > getMalls() {
        return malls;
    }

    public void setMalls( List< MallEntityQuery > malls ) {
        this.malls = malls;
    }

    public Integer getUcType() {
        return ucType;
    }

    public void setUcType( Integer ucType ) {
        this.ucType = ucType;
    }

    public Integer getBusId() {
        return busId;
    }

    public void setBusId( Integer busId ) {
        this.busId = busId;
    }

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId( Integer shopId ) {
        this.shopId = shopId;
    }

    public String getSuccessNoticeUrl() {
        return successNoticeUrl;
    }

    public void setSuccessNoticeUrl( String successNoticeUrl ) {
        this.successNoticeUrl = successNoticeUrl;
    }

    public String getJumpUrl() {
        return jumpUrl;
    }

    public void setJumpUrl( String jumpUrl ) {
        this.jumpUrl = jumpUrl;
    }
    public String getSign() {
        return sign;
    }

    public void setSign( String sign ) {
        this.sign = sign;
    }

    public Integer getJumphttpPOST() {
        return jumphttpPOST;
    }

    public void setJumphttpPOST( Integer jumphttpPOST ) {
        this.jumphttpPOST = jumphttpPOST;
    }

    public Double getDerateMoney() {
        return derateMoney;
    }

    public void setDerateMoney( Double derateMoney ) {
        this.derateMoney = derateMoney;
    }
}

