package com.gt.member.service.core.ws.entitybo.returnBo;

import java.util.Date;

/**
 * 多粉卡券接口 返回参数
 *
 * Created by pengjiangli on 2017/7/31 0031.
 */
public class DuofenCardBo {



    /**
     * 接口名:findDuofenCardByMemberId 返回对象DuofenCardListBo
     *
     * 返回参数:image  gId code addUser countId discount card_type cash_least_cost reduce_cost
     *
     */
    private String  image;   //图片
    private Integer gId;   //用户拥有的卡券id
    private String  code;  //用户拥有卡券code
    private Integer addUser; //是否允许叠加使用  0不允许 1已允许
    private Integer countId;  //减免券能叠加使用最高使用数量值
    private Double  discount;  //折扣值
    private Integer card_type; // 卡券类型 0折扣券 1减免券
    private Double  cash_least_cost;  //抵扣条件
    private Double  reduce_cost;  //抵扣金额
    private Integer id;  //卡券id
    private String  title;  //卡券名称
    private String  color;  //颜色
    private String  type;  //使用时间类型 DATE_TYPE_FIX_TIME_RANGE 时间范围  DATE_TYPE_FIX_TERM自领取多少天生效
    private Date    begin_timestamp;  //起用时间
    private Date    end_timestamp;   //结束时间
    private Integer fixed_term;    //自领取后多少天内有效
    private Integer fixed_begin_term;  //自领取后多少天开始生效

    public String getImage() {
	return image;
    }

    public void setImage( String image ) {
	this.image = image;
    }

    public Integer getgId() {
	return gId;
    }

    public void setgId( Integer gId ) {
	this.gId = gId;
    }

    public String getCode() {
	return code;
    }

    public void setCode( String code ) {
	this.code = code;
    }

    public Integer getAddUser() {
	return addUser;
    }

    public void setAddUser( Integer addUser ) {
	this.addUser = addUser;
    }

    public Integer getCountId() {
	return countId;
    }

    public void setCountId( Integer countId ) {
	this.countId = countId;
    }

    public Double getDiscount() {
	return discount;
    }

    public void setDiscount( Double discount ) {
	this.discount = discount;
    }

    public Integer getCard_type() {
	return card_type;
    }

    public void setCard_type( Integer card_type ) {
	this.card_type = card_type;
    }

    public Double getCash_least_cost() {
	return cash_least_cost;
    }

    public void setCash_least_cost( Double cash_least_cost ) {
	this.cash_least_cost = cash_least_cost;
    }

    public Double getReduce_cost() {
	return reduce_cost;
    }

    public void setReduce_cost( Double reduce_cost ) {
	this.reduce_cost = reduce_cost;
    }

    public String getTitle() {
	return title;
    }

    public void setTitle( String title ) {
	this.title = title;
    }

    public String getColor() {
	return color;
    }

    public void setColor( String color ) {
	this.color = color;
    }

    public Integer getId() {
	return id;
    }

    public void setId( Integer id ) {
	this.id = id;
    }

    public String getType() {
	return type;
    }

    public void setType( String type ) {
	this.type = type;
    }

    public Date getBegin_timestamp() {
	return begin_timestamp;
    }

    public void setBegin_timestamp( Date begin_timestamp ) {
	this.begin_timestamp = begin_timestamp;
    }

    public Date getEnd_timestamp() {
	return end_timestamp;
    }

    public void setEnd_timestamp( Date end_timestamp ) {
	this.end_timestamp = end_timestamp;
    }

    public Integer getFixed_term() {
	return fixed_term;
    }

    public void setFixed_term( Integer fixed_term ) {
	this.fixed_term = fixed_term;
    }

    public Integer getFixed_begin_term() {
	return fixed_begin_term;
    }

    public void setFixed_begin_term( Integer fixed_begin_term ) {
	this.fixed_begin_term = fixed_begin_term;
    }
}
