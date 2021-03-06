package com.gt.member.controller.queryBo;

import java.util.Map;

/**
 * 总订单请求参数  包含非业务参数
 *
 * @author pengjiangli
 */
public class MallAllEntityQuery {
    private String orderCode;  //订单号
    private Double totalMoney = 0.0; //订单总金额
    private Map<Integer,MallEntityQuery > malls;  //商品订单详情

    private Integer ucType;   //消费类型  请查看A003消费类型  没有请添加
    private Integer busId;  //商家主账户id
    private Integer shopId;  //门店订单

    private String returnUrl;  //支付成功 通知地址

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

    public Map< Integer,MallEntityQuery > getMalls() {
        return malls;
    }

    public void setMalls( Map< Integer,MallEntityQuery > malls ) {
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

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl( String returnUrl ) {
        this.returnUrl = returnUrl;
    }
}

