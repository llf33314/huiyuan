package com.gt.member.service.memberApi.entityBo;

/**
 * Created by Administrator on 2017/8/2 0002.
 */
public class MallEntity {
    private Integer mallId;  //商品id or 生成序列号  not null
    private Integer number = 1;  // 商品数量  not null
    private Double totalMoneyOne;  //商品应付单价格  not null
    private Double totalMoneyAll; //商品订单总价格  用来计算  not null

    private Integer userCard = 0;  //是否能用会员卡打折  0不参与 1参与  not null
    private Double discountMemberMoney = 0.0; //会员优惠券金额
    private Integer canUserCard = 0;  //是否用了会员

    private Integer useCoupon = 0;  //是否使用优惠券  not null
    private Double discountConponMoney = 0.0;
    private Integer canUseConpon = 0;  //是否能用优惠券


    private Integer useFenbi = 0;  //是否使用粉币  not null
    private Double fenbiNum = 0.0;  //使用粉币数量
    private Double discountfenbiMoney = 0.0; //粉币抵扣金额
    private Integer canUsefenbi = 0;  //是否能用粉币


    private Integer userJifen = 0;  //是否使用积分  not null
    private Double jifenNum = 0.0;  //使用积分数量
    private Double discountjifenMoney = 0.0; //积分抵扣金额
    private Integer canUseJifen = 0;  //是否能用积分


    private Integer useLeague = 1;     //商品是否能使用联盟卡
    private Double leagueJifen = 0.0;   //  商品联盟积分使用数量
    private Double leagueMoney = 0.0;  //联盟卡优惠券金额
    private Integer canUseLeagueJifen = 1;  //是否能用联盟积分

    private Double unitPrice = 0.0;  // 实付单个商品金额

    private Double balanceMoney = 0.0;  //支付总金额

    public Integer getMallId() {
        return mallId;
    }

    public void setMallId(Integer mallId) {
        this.mallId = mallId;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Double getTotalMoneyOne() {
        return totalMoneyOne;
    }

    public void setTotalMoneyOne(Double totalMoneyOne) {
        this.totalMoneyOne = totalMoneyOne;
    }

    public Double getTotalMoneyAll() {
        return totalMoneyAll;
    }

    public void setTotalMoneyAll(Double totalMoneyAll) {
        this.totalMoneyAll = totalMoneyAll;
    }

    public Integer getUserCard() {
        return userCard;
    }

    public void setUserCard(Integer userCard) {
        this.userCard = userCard;
    }

    public Double getDiscountMemberMoney() {
        return discountMemberMoney;
    }

    public void setDiscountMemberMoney(Double discountMemberMoney) {
        this.discountMemberMoney = discountMemberMoney;
    }

    public Integer getCanUserCard() {
        return canUserCard;
    }

    public void setCanUserCard(Integer canUserCard) {
        this.canUserCard = canUserCard;
    }

    public Integer getUseCoupon() {
        return useCoupon;
    }

    public void setUseCoupon(Integer useCoupon) {
        this.useCoupon = useCoupon;
    }

    public Double getDiscountConponMoney() {
        return discountConponMoney;
    }

    public void setDiscountConponMoney(Double discountConponMoney) {
        this.discountConponMoney = discountConponMoney;
    }

    public Integer getCanUseConpon() {
        return canUseConpon;
    }

    public void setCanUseConpon(Integer canUseConpon) {
        this.canUseConpon = canUseConpon;
    }

    public Integer getUseFenbi() {
        return useFenbi;
    }

    public void setUseFenbi(Integer useFenbi) {
        this.useFenbi = useFenbi;
    }

    public Double getFenbiNum() {
        return fenbiNum;
    }

    public void setFenbiNum(Double fenbiNum) {
        this.fenbiNum = fenbiNum;
    }

    public Double getDiscountfenbiMoney() {
        return discountfenbiMoney;
    }

    public void setDiscountfenbiMoney(Double discountfenbiMoney) {
        this.discountfenbiMoney = discountfenbiMoney;
    }

    public Integer getCanUsefenbi() {
        return canUsefenbi;
    }

    public void setCanUsefenbi(Integer canUsefenbi) {
        this.canUsefenbi = canUsefenbi;
    }

    public Integer getUserJifen() {
        return userJifen;
    }

    public void setUserJifen(Integer userJifen) {
        this.userJifen = userJifen;
    }

    public Double getJifenNum() {
        return jifenNum;
    }

    public void setJifenNum(Double jifenNum) {
        this.jifenNum = jifenNum;
    }

    public Double getDiscountjifenMoney() {
        return discountjifenMoney;
    }

    public void setDiscountjifenMoney(Double discountjifenMoney) {
        this.discountjifenMoney = discountjifenMoney;
    }

    public Integer getCanUseJifen() {
        return canUseJifen;
    }

    public void setCanUseJifen(Integer canUseJifen) {
        this.canUseJifen = canUseJifen;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Double getBalanceMoney() {
        return balanceMoney;
    }

    public void setBalanceMoney(Double balanceMoney) {
        this.balanceMoney = balanceMoney;
    }

    public Integer getUseLeague() {
        return useLeague;
    }

    public void setUseLeague(Integer useLeague) {
        this.useLeague = useLeague;
    }

    public Double getLeagueJifen() {
        return leagueJifen;
    }

    public void setLeagueJifen(Double leagueJifen) {
        this.leagueJifen = leagueJifen;
    }

    public Double getLeagueMoney() {
        return leagueMoney;
    }

    public void setLeagueMoney(Double leagueMoney) {
        this.leagueMoney = leagueMoney;
    }

    public Integer getCanUseLeagueJifen() {
        return canUseLeagueJifen;
    }

    public void setCanUseLeagueJifen(Integer canUseLeagueJifen) {
        this.canUseLeagueJifen = canUseLeagueJifen;
    }
}
