package com.gt.member.service.core.ws.entitybo.returnBo;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.util.Date;

/**
 * 微信卡券Bo类
 *
 * Created by pengjiangli on 2017/7/28 0028.
 */
public class WxCardBo implements Serializable {

    /**
     * 接口名:findWxCardByShopId
     * 返回参数:card_type discount cash_least_cost reduce_cost user_card_code image
     *
     */
    /**
     * 接口名:findWxCardById
     * 返回参数:id  card_id  title
     */
    private Integer id;
    private String card_id;  //微信卡券id
    private String title;    //卡券名
    private String card_type;
    private Integer discount;  //折扣值
    private Double cash_least_cost; // 条件值
    private Double reduce_cost; //减免金额
    private String user_card_code;  //卡券code
    private String image;  //图标

    public String getCard_type() {
	return card_type;
    }

    public void setCard_type( String card_type ) {
	this.card_type = card_type;
    }

    public Integer getDiscount() {
	return discount;
    }

    public void setDiscount( Integer discount ) {
	this.discount = discount;
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

    public String getUser_card_code() {
	return user_card_code;
    }

    public void setUser_card_code( String user_card_code ) {
	this.user_card_code = user_card_code;
    }

    public String getImage() {
	return image;
    }

    public void setImage( String image ) {
	this.image = image;
    }

    public Integer getId() {
	return id;
    }

    public void setId( Integer id ) {
	this.id = id;
    }

    public String getCard_id() {
	return card_id;
    }

    public void setCard_id( String card_id ) {
	this.card_id = card_id;
    }

    public String getTitle() {
	return title;
    }

    public void setTitle( String title ) {
	this.title = title;
    }
}
