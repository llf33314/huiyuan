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
@TableName("t_pay_success")
public class PaySuccess extends Model<PaySuccess> {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 名称
     */
	private String payname;
    /**
     * 模块名称
     */
	private String modelName;
    /**
     * 模块id 1餐饮
     */
	private Integer model;
    /**
     * 选项 1抽奖 2微信优惠券 3平台优惠券
     */
	private Integer optionType;
    /**
     * 游戏地址
     */
	private String gameUrl;
    /**
     * 备注
     */
	private String remarks;
	private Integer publicId;
	private String phone;
	private Integer countType;
	private Integer jifenOpen;
	private Integer fenbiOpen;
	private String buttonText;
	private Integer wxyhjOpen;
	private Integer duofenCardOpen;
	private Integer receiveType;
	private String image;
    /**
     * 商户id
     */
	private Integer busId;
	private Integer firstGive;
	private Integer firstType;
	private String cardIds;


	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "PaySuccess{" +
			"id=" + id +
			", payname=" + payname +
			", modelName=" + modelName +
			", model=" + model +
			", optionType=" + optionType +
			", gameUrl=" + gameUrl +
			", remarks=" + remarks +
			", publicId=" + publicId +
			", phone=" + phone +
			", countType=" + countType +
			", jifenOpen=" + jifenOpen +
			", fenbiOpen=" + fenbiOpen +
			", buttonText=" + buttonText +
			", wxyhjOpen=" + wxyhjOpen +
			", duofenCardOpen=" + duofenCardOpen +
			", receiveType=" + receiveType +
			", image=" + image +
			", busId=" + busId +
			", firstGive=" + firstGive +
			", firstType=" + firstType +
			", cardIds=" + cardIds +
			"}";
	}
}
