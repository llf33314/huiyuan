package com.gt.member.controller.queryBo;

/**
 * erp 统一请求订单详情参数
 * Created by pengjiangli on 2017/8/2 0002.
 */
public class MallEntityQuery {

    private Integer mallId;  //商品id or 生成序列号
    private Integer number = 1;  // 商品数量
    private Double totalMoneyOne;  //商品应付单价格
    private Double totalMoneyAll; //商品订单总价格

    private Integer userCard = 0;  //是否能用会员卡打折  0不参与 1参与

    private Integer useCoupon = 0;  //是否使用优惠券 0不参与 1参与

    private Integer useFenbi = 0;  //是否使用粉币 0不参与 1参与

    private Integer userJifen = 0;  //是否使用积分 0不参与 1参与

    private Integer useLeague = 1;     //商品是否能使用联盟卡 0不参与 1参与

    public Integer getMallId() {
        return mallId;
    }

    public void setMallId( Integer mallId ) {
        this.mallId = mallId;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber( Integer number ) {
        this.number = number;
    }

    public Double getTotalMoneyOne() {
        return totalMoneyOne;
    }

    public void setTotalMoneyOne( Double totalMoneyOne ) {
        this.totalMoneyOne = totalMoneyOne;
    }

    public Double getTotalMoneyAll() {
        return totalMoneyAll;
    }

    public void setTotalMoneyAll( Double totalMoneyAll ) {
        this.totalMoneyAll = totalMoneyAll;
    }

    public Integer getUserCard() {
        return userCard;
    }

    public void setUserCard( Integer userCard ) {
        this.userCard = userCard;
    }

    public Integer getUseCoupon() {
        return useCoupon;
    }

    public void setUseCoupon( Integer useCoupon ) {
        this.useCoupon = useCoupon;
    }

    public Integer getUseFenbi() {
        return useFenbi;
    }

    public void setUseFenbi( Integer useFenbi ) {
        this.useFenbi = useFenbi;
    }

    public Integer getUserJifen() {
        return userJifen;
    }

    public void setUserJifen( Integer userJifen ) {
        this.userJifen = userJifen;
    }

    public Integer getUseLeague() {
        return useLeague;
    }

    public void setUseLeague( Integer useLeague ) {
        this.useLeague = useLeague;
    }
}
