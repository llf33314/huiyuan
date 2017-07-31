package com.gt.member.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import java.util.Date;
import com.baomidou.mybatisplus.enums.IdType;
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
 * @since 2017-07-25
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
	private Integer cardId;
    /**
     * 记录类型  1充值或消费  2积分 3粉笔 4 流量
     */
	private Integer recordType;
    /**
     * 数量(加) 可以代表 粉笔 流量 钱 次数
     */
	private String number;
    /**
     * 时间
     */
	private Date createDate;
    /**
     * 物品名称
     */
	private String itemName;
    /**
     * 公众号id
     */
	private Integer publicId;
    /**
     * 余额
     */
	private String balance;
    /**
     * 卡类型id
     */
	private Integer ctId;
    /**
     * 数量 用于清0
     */
	private Double amount;
	private Integer busId;


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
			"}";
	}
}
