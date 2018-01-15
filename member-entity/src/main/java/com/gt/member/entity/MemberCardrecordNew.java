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
 * 
 * </p>
 *
 * @author pengjiangli
 * @since 2017-10-23
 */
@Data
@Accessors(chain = true)
@TableName("t_member_cardrecord_new")
public class MemberCardrecordNew extends Model<MemberCardrecordNew> {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Long id;
	@TableField("memberId")
	private Integer memberId;
    /**
     * 记录类型  1充值或消费  2积分 3粉笔 4 流量
     */
	@TableField("recordType")
	private Integer recordType;
    /**
     * 数量
     */
	private Double number;
	@TableField("createDate")
	private Date createDate;
    /**
     * 消费名目
     */
	@TableField("itemName")
	private String itemName;
    /**
     * 余额
     */
	private Double balance;
    /**
     * 消费类型 0消费  1赠送  积分清0操作
     */
	private Integer rtype;
	@TableField("busId")
	private Integer busId;
    /**
     * 订单号
     */
	@TableField("orderCode")
	private String orderCode;

    	@TableField("unit")
	private String unit;

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "MemberCardrecordNew{" +
			"id=" + id +
			", memberId=" + memberId +
			", recordType=" + recordType +
			", number=" + number +
			", createDate=" + createDate +
			", itemName=" + itemName +
			", balance=" + balance +
			", rtype=" + rtype +
			", busId=" + busId +
			", orderCode=" + orderCode +
			"}";
	}
}
