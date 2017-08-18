package com.gt.member.entity;

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
 * @since 2017-07-25
 */
@Data
@Accessors(chain = true)
@TableName("t_wx_user_consume_refund")
public class WxUserConsumeRefund extends Model<WxUserConsumeRefund> {

    private static final long serialVersionUID = 1L;

    /**
     * 消费记录id(t_wx_user_consume表id)
     */
	private Integer consumeId;
    /**
     * 退款id(t_wx_user_consume表id)
     */
	private Integer refundId;


	@Override
	protected Serializable pkVal() {
		return this.consumeId;
	}

	@Override
	public String toString() {
		return "WxUserConsumeRefund{" +
			"consumeId=" + consumeId +
			", refundId=" + refundId +
			"}";
	}
}
