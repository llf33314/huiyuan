package com.gt.member.service.entityBo;

/**
 * 统一门店计算
 * @author pengjiangli
 *
 */
public class MemberShopEntity {

    private Double totalMoney; //订单金额 not null
    private Integer memberId;  //粉丝信息 not null
    private Double discountMemberMoney = 0.0; //会员优惠券金额

    private Integer useCoupon=0;  //是否使用优惠券 0不是用 1使用  not null
    private Integer couponType; //优惠券类型 0微信 1多粉优惠券
    private Integer coupondId;  //卡券id
    private String codes;  //使用优惠券code值 用来核销卡券 不存在set
    private Integer couponNum = 1;  //使用优惠券数量 不存在set
    private Integer canUseConpon = 0;  //是否能用优惠券  0不允许用 1允许用
    private Double discountConponMoney = 0.0;


    private Integer userLeague = 0;  //是否使用联盟卡 not null  0未使用 1使用
    private Double leagueDiscount = 1.0; //联盟折扣   userLeague=1 not null
    private Integer leagueJifen = 0; //联盟积分  传入最高能使用积分值   userLeague=1 not null

    private Integer useFenbi=0;  //是否使用粉币  not null  0未使用 1使用
    private Integer fenbiNum;  //使用粉币数量
    private Double discountfenbiMoney = 0.0; //粉币抵扣金额
    private Integer canUsefenbi = 0;  //是否能用粉币   0不允许用 1允许用

    private Integer userJifen;  //是否使用积分  not null  0未使用 1使用
    private Integer jifenNum;  //使用积分数量 不存在set
    private Double discountjifenMoney = 0.0; //积分抵扣金额
    private Integer canUseJifen = 0;  //是否能用积分   0不允许用 1允许用

    private Double balanceMoney = 0.0;  //支付金额

    public Double getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney( Double totalMoney ) {
        this.totalMoney = totalMoney;
    }

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId( Integer memberId ) {
        this.memberId = memberId;
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

    public Integer getUseFenbi() {
        return useFenbi;
    }

    public void setUseFenbi( Integer useFenbi ) {
        this.useFenbi = useFenbi;
    }

    public Integer getFenbiNum() {
        return fenbiNum;
    }

    public void setFenbiNum( Integer fenbiNum ) {
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

    public Double getBalanceMoney() {
        return balanceMoney;
    }

    public void setBalanceMoney( Double balanceMoney ) {
        this.balanceMoney = balanceMoney;
    }
}