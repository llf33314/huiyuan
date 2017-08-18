package com.gt.member.entity;

import com.baomidou.mybatisplus.annotations.TableId;
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
@TableName("t_pay_successcard")
public class PaySuccesscard extends Model<PaySuccesscard> {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 支付金额
     */
	private Double payMoney;
    /**
     * 自定义优惠券id
     */
	private Integer cardId;
    /**
     * 微信货架id
     */
	private Integer cardShelveId;
    /**
     * 微信货架跳转地址
     */
	private String cardShelveUrl;
    /**
     * 主表id t_pay_success
     */
	private Integer paySuccessId;
    /**
     * 积分赠送
     */
	private Double jifenCount;
    /**
     * 粉币赠送
     */
	private Double fenbiCount;
	private Integer rId;
	private Integer num;


	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "PaySuccesscard{" +
			"id=" + id +
			", payMoney=" + payMoney +
			", cardId=" + cardId +
			", cardShelveId=" + cardShelveId +
			", cardShelveUrl=" + cardShelveUrl +
			", paySuccessId=" + paySuccessId +
			", jifenCount=" + jifenCount +
			", fenbiCount=" + fenbiCount +
			", rId=" + rId +
			", num=" + num +
			"}";
	}
}
