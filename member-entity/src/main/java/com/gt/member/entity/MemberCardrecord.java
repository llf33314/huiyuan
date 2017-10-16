package com.gt.member.entity;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;
import com.baomidou.mybatisplus.enums.FieldFill;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 卡片记录
 * </p>
 *
 * @author pengjiangli
 * @since 2017-10-13
 */
@Data
@Accessors(chain = true)
@TableName("t_member_cardrecord")
public class MemberCardrecord extends Model<MemberCardrecord> {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 卡片id
     */
	@TableField("cardId")
	private Integer cardId;
    /**
     * 记录类型  1充值或消费  2积分 3粉笔 4 流量
     */
	@TableField("recordType")
	private Integer recordType;
    /**
     * 数量(加) 可以代表 粉笔 流量 钱 次数
     */
	private String number;
    /**
     * 时间
     */
	@TableField("createDate")
	private Date createDate;
    /**
     * 物品名称
     */
	@TableField("itemName")
	private String itemName;
    /**
     * 公众号id
     */
	@TableField("publicId")
	private Integer publicId;
    /**
     * 余额
     */
	private String balance;
    /**
     * 卡类型id
     */
	@TableField("ctId")
	private Integer ctId;
    /**
     * 数量 用于清0
     */
	private Double amount;
	@TableField("busId")
	private Integer busId;
    /**
     * 订单号
     */
	@TableField("orderCode")
	private String orderCode;


	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "MemberCardrecord{" +
			"id=" + id +
			", cardId=" + cardId +
			", recordType=" + recordType +
			", number=" + number +
			", createDate=" + createDate +
			", itemName=" + itemName +
			", publicId=" + publicId +
			", balance=" + balance +
			", ctId=" + ctId +
			", amount=" + amount +
			", busId=" + busId +
			", orderCode=" + orderCode +
			"}";
	}
}
