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
@TableName("t_offlinepay")
public class Offlinepay extends Model<Offlinepay> {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
	private Integer shopId;
	private String title;
	private String subheading;
	private Double eachMoney;
	private Double reduceMoney;
	private String explainText;
	private Integer busId;
	private String phone;
	private Integer publicId;
	private Date createdate;
	private Integer type;
	private String rule;
	private Integer timeType;
	private Integer fenbiOpen;
	private Integer jifenOpen;
	private Integer balanceType;
	private Integer holdNum;


	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "Offlinepay{" +
			"id=" + id +
			", shopId=" + shopId +
			", title=" + title +
			", subheading=" + subheading +
			", eachMoney=" + eachMoney +
			", reduceMoney=" + reduceMoney +
			", explainText=" + explainText +
			", busId=" + busId +
			", phone=" + phone +
			", publicId=" + publicId +
			", createdate=" + createdate +
			", type=" + type +
			", rule=" + rule +
			", timeType=" + timeType +
			", fenbiOpen=" + fenbiOpen +
			", jifenOpen=" + jifenOpen +
			", balanceType=" + balanceType +
			", holdNum=" + holdNum +
			"}";
	}
}
