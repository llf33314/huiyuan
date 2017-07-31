package com.gt.member.entity;

import com.baomidou.mybatisplus.annotations.TableField;
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
 * 赠送规则表
 * </p>
 *
 * @author pengjiangli
 * @since 2017-07-25
 */
@Data
@Accessors(chain = true)
@TableName("t_member_giverule")
public class MemberGiverule extends Model<MemberGiverule> {

    private static final long serialVersionUID = 1L;

	@TableId(value="gr_id", type= IdType.AUTO)
	private Integer grId;
    /**
     * 商户id
     */
	@TableField("public_id")
	private Integer publicId;
    /**
     * 卡片等级模板id
     */
	@TableField("gt_id")
	private Integer gtId;
    /**
     * 卡片类型id
     */
	@TableField("ct_id")
	private Integer ctId;
    /**
     * 充值金额
     */
	@TableField("gr_rechargeMoney")
	private Double grRechargeMoney;
    /**
     * 次数（针对次卡）
     */
	@TableField("gr_number")
	private Integer grNumber;
    /**
     * 折扣（针对折扣卡）
     */
	@TableField("gr_discount")
	private Integer grDiscount;
    /**
     * 赠送数量
     */
	@TableField("gr_giveCount")
	private Integer grGiveCount;
    /**
     * 赠送方式 0:无 1:金额 2:次数
     */
	@TableField("gr_giveType")
	private Integer grGiveType;
    /**
     * 启用日期
     */
	@TableField("gr_startDate")
	private Date grStartDate;
    /**
     * 有效时间(0:长期有效)
     */
	@TableField("gr_validDate")
	private Integer grValidDate;
    /**
     * 升级方式 0:金额 1:积分
     */
	@TableField("gr_upgradeType")
	private Integer grUpgradeType;
    /**
     * 升级数量
     */
	@TableField("gr_upgradeCount")
	private Integer grUpgradeCount;
    /**
     * 会员权益
     */
	@TableField("gr_equities")
	private String grEquities;
    /**
     * 创建人
     */
	@TableField("create_userId")
	private Integer createUserId;
    /**
     * 创建时间
     */
	private Date creatDate;
	private Integer delayDay;
	private Integer busId;


	@Override
	protected Serializable pkVal() {
		return this.grId;
	}

	@Override
	public String toString() {
		return "MemberGiverule{" +
			"grId=" + grId +
			", publicId=" + publicId +
			", gtId=" + gtId +
			", ctId=" + ctId +
			", grRechargeMoney=" + grRechargeMoney +
			", grNumber=" + grNumber +
			", grDiscount=" + grDiscount +
			", grGiveCount=" + grGiveCount +
			", grGiveType=" + grGiveType +
			", grStartDate=" + grStartDate +
			", grValidDate=" + grValidDate +
			", grUpgradeType=" + grUpgradeType +
			", grUpgradeCount=" + grUpgradeCount +
			", grEquities=" + grEquities +
			", createUserId=" + createUserId +
			", creatDate=" + creatDate +
			", delayDay=" + delayDay +
			", busId=" + busId +
			"}";
	}
}
