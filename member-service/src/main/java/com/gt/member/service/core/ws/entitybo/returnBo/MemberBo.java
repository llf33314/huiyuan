package com.gt.member.service.core.ws.entitybo.returnBo;

import java.io.Serializable;
import java.util.List;

/**
 * MemberBo 返回会员信息 参数大杂烩
 * Created by Administrator on 2017/7/27 0027.
 */
public class MemberBo implements  Serializable{

    private static final long serialVersionUID = 1L;

    /**
     * 接口名:isAdequateMoney 返回数据
     * 是否余额充足
     */
    private boolean isAdequateMoney;

    /**
     * 接口名:isCardType 返回数据 -1不是会员卡 1积分卡 2折扣卡 3储值卡 4时效卡 5次卡
     */
    private Integer cardType;

    /**
     *
     * 接口名:findMemberIds 返回数据
     * 返回会员id 集合
     *
     *
     */
    private List<Integer> ids;
    /**
     *
     * 接口名:isMemberById 返回数据
     *
     *
     * 是否是会员
     */
    private boolean  isMemberCard;


    //<!---------------------接口名称findMemberById、bingdingPhone 返回以下数据---------------------------->
    /**
     * 主键
     */
    private Integer id;
    /**
     * 公众号表ID
     */
    private Integer publicId;
    /**
     * 粉币
     */
    private Double  fansCurrency;
    /**
     * 流量
     */
    private Integer flow;
    /**
     * 积分
     */
    private Integer integral;

    private String  openid;
    /**
     * 手机号码
     */
    private String  phone;
    /**
     * 昵称
     */
    private String  nickname;
    /**
     * 性别 0未知 1男 2女
     */
    private Integer sex;
    /**
     * 省份
     */
    private String province;
    /**
     * 城市
     */
    private String city;
    /**
     * 国家
     */
    private String country;
    /**
     * 卡片id
     */
    private Integer mcId;
    private Integer busId;
    private String  pwd;
    private String  oldId;

    public boolean isMemberCard() {
	return isMemberCard;
    }

    public void setMemberCard( boolean memberCard ) {
	isMemberCard = memberCard;
    }

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

    public Double getFansCurrency() {
	return fansCurrency;
    }

    public void setFansCurrency( Double fansCurrency ) {
	this.fansCurrency = fansCurrency;
    }

    public Integer getFlow() {
	return flow;
    }

    public void setFlow( Integer flow ) {
	this.flow = flow;
    }

    public Integer getIntegral() {
	return integral;
    }

    public void setIntegral( Integer integral ) {
	this.integral = integral;
    }

    public String getOpenid() {
	return openid;
    }

    public void setOpenid( String openid ) {
	this.openid = openid;
    }

    public String getPhone() {
	return phone;
    }

    public void setPhone( String phone ) {
	this.phone = phone;
    }

    public String getNickname() {
	return nickname;
    }

    public void setNickname( String nickname ) {
	this.nickname = nickname;
    }

    public Integer getSex() {
	return sex;
    }

    public void setSex( Integer sex ) {
	this.sex = sex;
    }

    public String getProvince() {
	return province;
    }

    public void setProvince( String province ) {
	this.province = province;
    }

    public String getCity() {
	return city;
    }

    public void setCity( String city ) {
	this.city = city;
    }

    public String getCountry() {
	return country;
    }

    public void setCountry( String country ) {
	this.country = country;
    }

    public Integer getMcId() {
	return mcId;
    }

    public void setMcId( Integer mcId ) {
	this.mcId = mcId;
    }

    public Integer getBusId() {
	return busId;
    }

    public void setBusId( Integer busId ) {
	this.busId = busId;
    }

    public String getPwd() {
	return pwd;
    }

    public void setPwd( String pwd ) {
	this.pwd = pwd;
    }

    public String getOldId() {
	return oldId;
    }

    public void setOldId( String oldId ) {
	this.oldId = oldId;
    }

    public List< Integer > getIds() {
	return ids;
    }

    public void setIds( List< Integer > ids ) {
	this.ids = ids;
    }

    public Integer getCardType() {
	return cardType;
    }

    public void setCardType( Integer cardType ) {
	this.cardType = cardType;
    }

    public boolean isAdequateMoney() {
	return isAdequateMoney;
    }

    public void setAdequateMoney( boolean adequateMoney ) {
	isAdequateMoney = adequateMoney;
    }
}
