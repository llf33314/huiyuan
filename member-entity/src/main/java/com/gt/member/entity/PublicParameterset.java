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
@TableName("t_public_parameterset")
public class PublicParameterset extends Model<PublicParameterset> {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 积分
     */
	private Double integralRatio;
    /**
     * 抵扣金额
     */
	private Double changeMoney;
    /**
     * 启兑金额
     */
	private Double startMoney;
	private Integer publicId;
    /**
     * 启兑方式 0积分启兑 1订单启兑
     */
	private Integer startType;
	private String findImage;
    /**
     * 是否开启卡券领取
     */
	private Integer isOpenCard;
	private Integer busId;
	private String loginImage;
    /**
     * 是否开启积分清零 0不开启 1开启
     */
	private Integer isclearJifen;
    /**
     * 清0月份
     */
	private Integer month;
    /**
     * 0老会员视图 1九宫格 2 竖状（简洁版）
     */
	private Integer memberView;
    /**
     * 优惠买单按钮文字
     */
	private String youhuiButton;
    /**
     * 优惠买单说明
     */
	private String youhuiText;
    /**
     * 0优惠买单 1自定义按钮
     */
	private Integer buttonType;
    /**
     * 跳转地址
     */
	private String buttonUrl;


	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "PublicParameterset{" +
			"id=" + id +
			", integralRatio=" + integralRatio +
			", changeMoney=" + changeMoney +
			", startMoney=" + startMoney +
			", publicId=" + publicId +
			", startType=" + startType +
			", findImage=" + findImage +
			", isOpenCard=" + isOpenCard +
			", busId=" + busId +
			", loginImage=" + loginImage +
			", isclearJifen=" + isclearJifen +
			", month=" + month +
			", memberView=" + memberView +
			", youhuiButton=" + youhuiButton +
			", youhuiText=" + youhuiText +
			", buttonType=" + buttonType +
			", buttonUrl=" + buttonUrl +
			"}";
	}
}
