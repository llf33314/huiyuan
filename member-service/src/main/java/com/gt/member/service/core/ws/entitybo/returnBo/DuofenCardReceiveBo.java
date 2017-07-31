package com.gt.member.service.core.ws.entitybo.returnBo;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 多粉卡券投放信息
 *
 * Created by pengjiangli on 2017/7/31 0031.
 */
public class DuofenCardReceiveBo implements Serializable{

    /**
     * 根据卡包查询卡券信息
     * 接口名：findCardByReceiveId
     * 参数：duofenCardBos 以下DuofenCardReceiveBo对象
     *
     * duofenCardBos包含:
     *
     */
     private List<DuofenCardBo> duofenCardBos;


    /**
     * 根据查询本商家商场投放的包
     * 接口名:findReceiveBybusId
     * 参数：以下所有
     *
     */
    private Integer id;
    private Integer publicId;
    /**
     * 卡包名称
     */
    private String  cardsName;
    /**
     * 卡包图片
     */
    private String  cardImage;
    /**
     * 领取结束时间
     */
    private Date    receiveDate;
    /**
     * 投放地点 0本公众号 1第三方
     */
    private Integer deliveryAddr;
    /**
     * 本公众号投放方式 1普通 2会员领取  3商场购买  4二维码下载  5游戏
     */
    private Integer deliveryType;
    /**
     * 第三方 0免费领取 1商场购买
     */
    private Integer deliveryType1;
    /**
     * 领取数量限制 0不限制 1限制
     */
    private Integer numlimit;
    /**
     * 最多领取数量规则设置
     */
    private Integer maxNumType;
    /**
     * 最多领取数量
     */
    private Integer maxNum;
    /**
     * 商场购买券包设置
     */
    private String  cardMessage;
    /**
     * 购买金额
     */
    private Double  buyMoney;
    /**
     * 券号短信投放通知人群
     */
    private String ctIds;
    /**
     * 券号短信投放会员等级
     */
    private String gtIds;
    /**
     * 卡包审核后生成code
     */
    private String code;
    /**
     * 卡包状态 0未审核 1审核通过已启用 2已停用
     */
    private Integer state;
    private String titleCard;
    private String cardIds;
    /**
     * 卡包背景颜色
     */
    private String backColor;
    private Integer isCallSms;
    private String mobilePhone;
    /**
     * 卡包 0非礼券包 1礼券包（其他券为赠送）
     */
    private Integer cardType;
    /**
     * 礼券id
     */
    private Integer lqId;
    /**
     * 积分购买值
     */
    private Integer jifen;
    /**
     * 粉币购买值
     */
    private Integer fenbi;
    /**
     * 商家id
     */
    private Integer busId;
    /**
     * 第三方商场id
     */
    private Integer threeMallId;
    /**
     * 第三方商城投放数量
     */
    private Integer threeNum;
    /**
     * 投放类型
     */
    private Integer classifyId;
    /**
     * 是否推荐 0不允许 1允许
     */
    private Integer isrecommend;
    /**
     * 推荐赠送积分
     */
    private Integer giveJifen;
    /**
     * 推荐赠送粉币
     */
    private Integer giveFenbi;
    /**
     * 推荐赠送金额
     */
    private Double giveMoney;
    /**
     * 推荐赠送流量
     */
    private Integer giveFlow;

    private Double  lpjBuyMoney;  //礼品券购买金额

    public Integer getId() {
	return id;
    }

    public void setId( Integer id ) {
	this.id = id;
    }

    public Integer getPublicId() {
	return publicId;
    }

    public void setPublicId( Integer publicId ) {
	this.publicId = publicId;
    }

    public String getCardsName() {
	return cardsName;
    }

    public void setCardsName( String cardsName ) {
	this.cardsName = cardsName;
    }

    public String getCardImage() {
	return cardImage;
    }

    public void setCardImage( String cardImage ) {
	this.cardImage = cardImage;
    }

    public Date getReceiveDate() {
	return receiveDate;
    }

    public void setReceiveDate( Date receiveDate ) {
	this.receiveDate = receiveDate;
    }

    public Integer getDeliveryAddr() {
	return deliveryAddr;
    }

    public void setDeliveryAddr( Integer deliveryAddr ) {
	this.deliveryAddr = deliveryAddr;
    }

    public Integer getDeliveryType() {
	return deliveryType;
    }

    public void setDeliveryType( Integer deliveryType ) {
	this.deliveryType = deliveryType;
    }

    public Integer getDeliveryType1() {
	return deliveryType1;
    }

    public void setDeliveryType1( Integer deliveryType1 ) {
	this.deliveryType1 = deliveryType1;
    }

    public Integer getNumlimit() {
	return numlimit;
    }

    public void setNumlimit( Integer numlimit ) {
	this.numlimit = numlimit;
    }

    public Integer getMaxNumType() {
	return maxNumType;
    }

    public void setMaxNumType( Integer maxNumType ) {
	this.maxNumType = maxNumType;
    }

    public Integer getMaxNum() {
	return maxNum;
    }

    public void setMaxNum( Integer maxNum ) {
	this.maxNum = maxNum;
    }

    public String getCardMessage() {
	return cardMessage;
    }

    public void setCardMessage( String cardMessage ) {
	this.cardMessage = cardMessage;
    }

    public Double getBuyMoney() {
	return buyMoney;
    }

    public void setBuyMoney( Double buyMoney ) {
	this.buyMoney = buyMoney;
    }

    public String getCtIds() {
	return ctIds;
    }

    public void setCtIds( String ctIds ) {
	this.ctIds = ctIds;
    }

    public String getGtIds() {
	return gtIds;
    }

    public void setGtIds( String gtIds ) {
	this.gtIds = gtIds;
    }

    public String getCode() {
	return code;
    }

    public void setCode( String code ) {
	this.code = code;
    }

    public Integer getState() {
	return state;
    }

    public void setState( Integer state ) {
	this.state = state;
    }

    public String getTitleCard() {
	return titleCard;
    }

    public void setTitleCard( String titleCard ) {
	this.titleCard = titleCard;
    }

    public String getCardIds() {
	return cardIds;
    }

    public void setCardIds( String cardIds ) {
	this.cardIds = cardIds;
    }

    public String getBackColor() {
	return backColor;
    }

    public void setBackColor( String backColor ) {
	this.backColor = backColor;
    }

    public Integer getIsCallSms() {
	return isCallSms;
    }

    public void setIsCallSms( Integer isCallSms ) {
	this.isCallSms = isCallSms;
    }

    public String getMobilePhone() {
	return mobilePhone;
    }

    public void setMobilePhone( String mobilePhone ) {
	this.mobilePhone = mobilePhone;
    }

    public Integer getCardType() {
	return cardType;
    }

    public void setCardType( Integer cardType ) {
	this.cardType = cardType;
    }

    public Integer getLqId() {
	return lqId;
    }

    public void setLqId( Integer lqId ) {
	this.lqId = lqId;
    }

    public Integer getJifen() {
	return jifen;
    }

    public void setJifen( Integer jifen ) {
	this.jifen = jifen;
    }

    public Integer getFenbi() {
	return fenbi;
    }

    public void setFenbi( Integer fenbi ) {
	this.fenbi = fenbi;
    }

    public Integer getBusId() {
	return busId;
    }

    public void setBusId( Integer busId ) {
	this.busId = busId;
    }

    public Integer getThreeMallId() {
	return threeMallId;
    }

    public void setThreeMallId( Integer threeMallId ) {
	this.threeMallId = threeMallId;
    }

    public Integer getThreeNum() {
	return threeNum;
    }

    public void setThreeNum( Integer threeNum ) {
	this.threeNum = threeNum;
    }

    public Integer getClassifyId() {
	return classifyId;
    }

    public void setClassifyId( Integer classifyId ) {
	this.classifyId = classifyId;
    }

    public Integer getIsrecommend() {
	return isrecommend;
    }

    public void setIsrecommend( Integer isrecommend ) {
	this.isrecommend = isrecommend;
    }

    public Integer getGiveJifen() {
	return giveJifen;
    }

    public void setGiveJifen( Integer giveJifen ) {
	this.giveJifen = giveJifen;
    }

    public Integer getGiveFenbi() {
	return giveFenbi;
    }

    public void setGiveFenbi( Integer giveFenbi ) {
	this.giveFenbi = giveFenbi;
    }

    public Double getGiveMoney() {
	return giveMoney;
    }

    public void setGiveMoney( Double giveMoney ) {
	this.giveMoney = giveMoney;
    }

    public Integer getGiveFlow() {
	return giveFlow;
    }

    public void setGiveFlow( Integer giveFlow ) {
	this.giveFlow = giveFlow;
    }

    public List< DuofenCardBo > getDuofenCardBos() {
	return duofenCardBos;
    }

    public void setDuofenCardBos( List< DuofenCardBo > duofenCardBos ) {
	this.duofenCardBos = duofenCardBos;
    }
    public Double getLpjBuyMoney() {
	return lpjBuyMoney;
    }

    public void setLpjBuyMoney( Double lpjBuyMoney ) {
	this.lpjBuyMoney = lpjBuyMoney;
    }
}
