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
 * 
 * </p>
 *
 * @author pengjiangli
 * @since 2017-07-25
 */
@Data
@Accessors(chain = true)
@TableName("t_member_card_old")
public class MemberCardOld extends Model<MemberCardOld> {

    private static final long serialVersionUID = 1L;

	@TableId(value="mc_id", type= IdType.AUTO)
	private Integer mcId;
	private String cardNo;
	@TableField("ct_id")
	private Integer ctId;
	@TableField("gt_id")
	private Integer gtId;
	@TableField("public_Id")
	private Integer publicId;
	private Date receiveDate;
	private Integer source;
	private Integer isbinding;
	private Double money;
	private Integer frequency;
	@TableField("gr_id")
	private Integer grId;
	private Date expireDate;
	private Integer ucId;
	private String nominateCode;
	private String systemcode;
	private Integer isChecked;
	private Integer applyType;
	private Integer memberId;
	private Integer changeCardType;
	private Integer entityMemberId;
	private Integer cardStatus;
	private Integer busId;
	private Integer oldId;
	private Double giveMoney;
	private Integer shopId;
	private Integer online;


	@Override
	protected Serializable pkVal() {
		return this.mcId;
	}

	@Override
	public String toString() {
		return "MemberCardOld{" +
			"mcId=" + mcId +
			", cardNo=" + cardNo +
			", ctId=" + ctId +
			", gtId=" + gtId +
			", publicId=" + publicId +
			", receiveDate=" + receiveDate +
			", source=" + source +
			", isbinding=" + isbinding +
			", money=" + money +
			", frequency=" + frequency +
			", grId=" + grId +
			", expireDate=" + expireDate +
			", ucId=" + ucId +
			", nominateCode=" + nominateCode +
			", systemcode=" + systemcode +
			", isChecked=" + isChecked +
			", applyType=" + applyType +
			", memberId=" + memberId +
			", changeCardType=" + changeCardType +
			", entityMemberId=" + entityMemberId +
			", cardStatus=" + cardStatus +
			", busId=" + busId +
			", oldId=" + oldId +
			", giveMoney=" + giveMoney +
			", shopId=" + shopId +
			", online=" + online +
			"}";
	}
}
