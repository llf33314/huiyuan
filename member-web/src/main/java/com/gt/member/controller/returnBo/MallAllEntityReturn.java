package com.gt.member.controller.returnBo;

import java.util.Map;

/**
 * 回调通知总订单
 *
 * @author pengjiangli
 */
public class MallAllEntityReturn {

    private String orderCode;  //订单号
    private Integer memberId;  //粉丝信息
    private Double totalMoney = 0.0; //订单总金额
    private Map<Integer,MallEntityReturn > malls;  //商品订单详情

    private Double discountMemberMoney = 0.0; //会员优惠券金额

    private Double discountConponMoney = 0.0;   //优惠券券优惠金额

    private Integer useFenbi = 0;  //是否使用粉币 0未使用 1使用
    private Double fenbiNum = 0.0;  //使用粉币数量
    private Double discountfenbiMoney = 0.0; //粉币抵扣金额
    private Integer canUsefenbi = 0;  //是否能用粉币

    private Integer userJifen = 0;  //是否使用积分 0未使用 1使用
    private Integer jifenNum = 0;  //使用积分数量
    private Double discountjifenMoney = 0.0; //积分抵扣金额
    private Integer canUseJifen = 0;  //是否能用积分

    private Integer userLeague = 0;  //是否使用联盟卡 0未使用 1使用
    private Double leagueDiscount = 1.0; //联盟折扣
    private Integer leagueJifen = 0; //联盟积分  传入最高能使用积分值

    private Integer leagueJifenNum = 0;  //联盟积分使用数量
    private Double leagueMoney = 0.0;  //联盟卡优惠金额

    private Integer payType=0;  //支付方式

    private Double balanceMoney = 0.0;  //支付金额

    private Integer print=0;  //打印易联云 0不打印 1打印

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode( String orderCode ) {
        this.orderCode = orderCode;
    }

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId( Integer memberId ) {
        this.memberId = memberId;
    }

    public Double getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney( Double totalMoney ) {
        this.totalMoney = totalMoney;
    }

    public Map< Integer,MallEntityReturn > getMalls() {
        return malls;
    }

    public void setMalls( Map< Integer,MallEntityReturn > malls ) {
        this.malls = malls;
    }

    public Double getDiscountMemberMoney() {
        return discountMemberMoney;
    }

    public void setDiscountMemberMoney( Double discountMemberMoney ) {
        this.discountMemberMoney = discountMemberMoney;
    }

    public Double getDiscountConponMoney() {
        return discountConponMoney;
    }

    public void setDiscountConponMoney( Double discountConponMoney ) {
        this.discountConponMoney = discountConponMoney;
    }

    public Integer getUseFenbi() {
        return useFenbi;
    }

    public void setUseFenbi( Integer useFenbi ) {
        this.useFenbi = useFenbi;
    }

    public Double getFenbiNum() {
        return fenbiNum;
    }

    public void setFenbiNum( Double fenbiNum ) {
        this.fenbiNum = fenbiNum;
    }

    public Double getDiscountfenbiMoney() {
        return discountfenbiMoney;
    }

    public void setDiscountfenbiMoney( Double discountfenbiMoney ) {
        this.discountfenbiMoney = discountfenbiMoney;
    }

    public Integer getCanUsefenbi() {
        return canUsefenbi;
    }

    public void setCanUsefenbi( Integer canUsefenbi ) {
        this.canUsefenbi = canUsefenbi;
    }

    public Integer getUserJifen() {
        return userJifen;
    }

    public void setUserJifen( Integer userJifen ) {
        this.userJifen = userJifen;
    }

    public Integer getJifenNum() {
        return jifenNum;
    }

    public void setJifenNum( Integer jifenNum ) {
        this.jifenNum = jifenNum;
    }

    public Double getDiscountjifenMoney() {
        return discountjifenMoney;
    }

    public void setDiscountjifenMoney( Double discountjifenMoney ) {
        this.discountjifenMoney = discountjifenMoney;
    }

    public Integer getCanUseJifen() {
        return canUseJifen;
    }

    public void setCanUseJifen( Integer canUseJifen ) {
        this.canUseJifen = canUseJifen;
    }

    public Integer getUserLeague() {
        return userLeague;
    }

    public void setUserLeague( Integer userLeague ) {
        this.userLeague = userLeague;
    }

    public Double getLeagueDiscount() {
        return leagueDiscount;
    }

    public void setLeagueDiscount( Double leagueDiscount ) {
        this.leagueDiscount = leagueDiscount;
    }

    public Integer getLeagueJifen() {
        return leagueJifen;
    }

    public void setLeagueJifen( Integer leagueJifen ) {
        this.leagueJifen = leagueJifen;
    }

    public Integer getLeagueJifenNum() {
        return leagueJifenNum;
    }

    public void setLeagueJifenNum( Integer leagueJifenNum ) {
        this.leagueJifenNum = leagueJifenNum;
    }

    public Double getLeagueMoney() {
        return leagueMoney;
    }

    public void setLeagueMoney( Double leagueMoney ) {
        this.leagueMoney = leagueMoney;
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType( Integer payType ) {
        this.payType = payType;
    }

    public Double getBalanceMoney() {
        return balanceMoney;
    }

    public void setBalanceMoney( Double balanceMoney ) {
        this.balanceMoney = balanceMoney;
    }
}

