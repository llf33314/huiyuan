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
 * 
 * </p>
 *
 * @author pengjiangli
 * @since 2017-07-25
 */
@Data
@Accessors(chain = true)
@TableName("t_pay_successlog")
public class PaySuccesslog extends Model<PaySuccesslog> {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 消费金额
     */
	private Double totalMoney;
    /**
     * 用户id
     */
	private Integer memberId;
    /**
     * 模块id
     */
	private Integer model;
    /**
     * 消费时间
     */
	private Date date;
	private Integer state;
	private String orderNo;
    /**
     * 支付有礼规则id
     */
	private Integer successId;
    /**
     * 赠送积分值
     */
	private Integer jifen;
    /**
     * 赠送粉币值
     */
	private Integer fenbi;
    /**
     * 第一次领取 0未领取 1已领取
     */
	private Integer oneGet;


	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "PaySuccesslog{" +
			"id=" + id +
			", totalMoney=" + totalMoney +
			", memberId=" + memberId +
			", model=" + model +
			", date=" + date +
			", state=" + state +
			", orderNo=" + orderNo +
			", successId=" + successId +
			", jifen=" + jifen +
			", fenbi=" + fenbi +
			", oneGet=" + oneGet +
			"}";
	}
}
