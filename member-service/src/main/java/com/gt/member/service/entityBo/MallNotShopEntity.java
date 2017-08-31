package com.gt.member.service.entityBo;

import java.util.Map;

/**
 * 不跨门店总订单
 * Created by pengjiangli on 2017/8/2 0002.
 */
public class MallNotShopEntity {

    private String orderCode; //订单号
    private Integer visitor=0;  //1为游客 以下数据不做业务任何处理 就返回支付方式 支付金额

    private Integer shopId;  //门店id  not null
    private Integer memberId;  //粉丝信息  not null
    private Double totalMoney = 0.0; //订单总金额 not null
    private Map<Integer, MallEntity> malls;  //门店商品信息  key为门店id not null

    private Double discountMemberMoney = 0.0; //会员优惠券金额

    private Integer useCoupon=0;  //是否使用优惠券  0未使用 1使用 not null
    private Integer couponType; //优惠券类型 0微信 1多粉优惠券   useCoupon=1 必填
    private Integer coupondId;  //卡券id     useCoupon=1 必填
    private String codes;  //使用优惠券code值 用来核销卡券
    private Integer couponNum=1;  //使用优惠券数量
    private Integer canUseConpon=0;  //是否能用优惠券
    private Double discountConponMoney=0.0;   //优惠券优惠券金额

    private Integer useFenbi = 0;  //是否使用粉币 not null
    private Double fenbiNum = 0.0;  //使用粉币数量
    private Double discountfenbiMoney = 0.0; //粉币抵扣金额
    private Integer canUsefenbi = 0;  //是否能用粉币

    private Integer userJifen = 0;  //是否使用积分 not null
    private Integer jifenNum = 0;  //使用积分数量
    private Double discountjifenMoney = 0.0; //积分抵扣金额
    private Integer canUseJifen = 0;  //是否能用积分

    private Integer userLeague = 0;  //是否使用联盟卡 not null  0未使用 1使用
    private Double leagueDiscount = 1.0; //联盟折扣   userLeague=1 not null
    private Integer leagueJifen = 0; //联盟积分  传入最高能使用积分值   userLeague=1 not null

    private Integer leagueJifenNum = 0;  //联盟积分使用数量
    private Double leagueMoney = 0.0;  //联盟卡优惠金额

    private Double derateMoney=0.0;  //减免金额 计算完减去减免金额

    private Integer payType=0;  //支付方式   字典1198
    private Double balanceMoney = 0.0;  //支付金额



    public Integer getShopId() {
        return shopId;
    }

    public void setShopId( Integer shopId ) {
        this.shopId = shopId;
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

    public Map< Integer,MallEntity > getMalls() {
        return malls;
    }

    public void setMalls( Map< Integer,MallEntity > malls ) {
        this.malls = malls;
    }

    public Double getDiscountMemberMoney() {
        return discountMemberMoney;
    }

    public void setDiscountMemberMoney( Double discountMemberMoney ) {
        this.discountMemberMoney = discountMemberMoney;
    }

    public Integer getUseCoupon() {
        return useCoupon;
    }

    public void setUseCoupon( Integer useCoupon ) {
        this.useCoupon = useCoupon;
    }

    public Integer getCouponType() {
        return couponType;
    }

    public void setCouponType( Integer couponType ) {
        this.couponType = couponType;
    }

    public Integer getCoupondId() {
        return coupondId;
    }

    public void setCoupondId( Integer coupondId ) {
        this.coupondId = coupondId;
    }

    public String getCodes() {
        return codes;
    }

    public void setCodes( String codes ) {
        this.codes = codes;
    }

    public Integer getCouponNum() {
        return couponNum;
    }

    public void setCouponNum( Integer couponNum ) {
        this.couponNum = couponNum;
    }

    public Integer getCanUseConpon() {
        return canUseConpon;
    }

    public void setCanUseConpon( Integer canUseConpon ) {
        this.canUseConpon = canUseConpon;
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

    public Double getBalanceMoney() {
        return balanceMoney;
    }

    public void setBalanceMoney( Double balanceMoney ) {
        this.balanceMoney = balanceMoney;
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType( Integer payType ) {
        this.payType = payType;
    }

    public Integer getVisitor() {
        return visitor;
    }

    public void setVisitor( Integer visitor ) {
        this.visitor = visitor;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode( String orderCode ) {
        this.orderCode = orderCode;
    }

    public Double getDerateMoney() {
        return derateMoney;
    }

    public void setDerateMoney( Double derateMoney ) {
        this.derateMoney = derateMoney;
    }
}
