package com.gt.member.entity;

import com.baomidou.mybatisplus.enums.IdType;
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
 * 订单支付表
 * </p>
 *
 * @author pengjiangli
 * @since 2017-10-12
 */
@Data
@Accessors(chain = true)
@TableName("t_wx_user_consume_pay")
public class UserConsumePay extends Model<UserConsumePay> {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 会员订单id
     */
	@TableField("ucId")
	private Integer ucId;
    /**
     * 支付金额
     */
	@TableField("payMoney")
	private Double payMoney;
    /**
     * 支付方式 1198
     */
	@TableField("paymentType")
	private Integer paymentType;


	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "UserConsumePay{" +
			"id=" + id +
			", ucId=" + ucId +
			", payMoney=" + payMoney +
			", paymentType=" + paymentType +
			"}";
	}
}
