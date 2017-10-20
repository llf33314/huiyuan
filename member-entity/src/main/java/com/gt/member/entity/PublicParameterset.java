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
 * 
 * </p>
 *
 * @author pengjiangli
 * @since 2017-10-18
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
	@TableField("integralRatio")
	private Double integralRatio;
    /**
     * 抵扣金额
     */
	@TableField("changeMoney")
	private Double changeMoney;
    /**
     * 启兑金额
     */
	@TableField("startMoney")
	private Double startMoney;
	@TableField("publicId")
	private Integer publicId;
    /**
     * 启兑方式 0积分启兑 1订单启兑
     */
	@TableField("startType")
	private Integer startType;
	@TableField("findImage")
	private String findImage;
    /**
     * 是否开启卡券领取
     */
	@TableField("isOpenCard")
	private Integer isOpenCard;
	@TableField("busId")
	private Integer busId;
	@TableField("loginImage")
	private String loginImage;
    /**
     * 是否开启积分清零 0不开启 1开启
     */
	@TableField("isclearJifen")
	private Integer isclearJifen;
    /**
     * 清0月份
     */
	private Integer month;
    /**
     * 0老会员视图 1九宫格 2 竖状（简洁版）
     */
	@TableField("memberView")
	private Integer memberView;
    /**
     * 优惠买单按钮文字
     */
	@TableField("youhuiButton")
	private String youhuiButton;
    /**
     * 优惠买单说明
     */
	@TableField("youhuiText")
	private String youhuiText;
    /**
     * 0优惠买单 1自定义按钮
     */
	@TableField("buttonType")
	private Integer buttonType;
    /**
     * 跳转地址
     */
	@TableField("buttonUrl")
	private String buttonUrl;
    /**
     * 是否开启自定义入口 0不开启 1开启
     */
	@TableField("isOpen")
	private Integer isOpen;


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
			", isOpen=" + isOpen +
			"}";
	}
}
