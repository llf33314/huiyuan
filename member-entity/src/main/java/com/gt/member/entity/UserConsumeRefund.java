package com.gt.member.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

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
public class UserConsumeRefund extends Model<UserConsumeRefund> {

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
		return "UserConsumeRefund{" +
			"consumeId=" + consumeId +
			", refundId=" + refundId +
			"}";
	}
}
