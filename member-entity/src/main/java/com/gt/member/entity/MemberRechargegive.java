package com.gt.member.entity;

import com.baomidou.mybatisplus.annotations.TableField;
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
 * 次卡和储值卡充值规则
 * </p>
 *
 * @author pengjiangli
 * @since 2017-07-25
 */
@Data
@Accessors(chain = true)
@TableName("t_member_rechargegive")
public class MemberRechargegive extends Model<MemberRechargegive> {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 充值金额
     */
	private Double money;
    /**
     * 赠送数量
     */
	private Integer giveCount;
    /**
     * 次数(次卡)
     */
	private Integer number;
    /**
     * 赠送规则id
     */
	@TableField("gr_id")
	private Integer grId;
    /**
     * 公众号id
     */
	private Integer publicId;
    /**
     * 卡片类型Id
     */
	private Integer ctId;
	private Integer isDate;
	private Integer busId;


	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "MemberRechargegive{" +
			"id=" + id +
			", money=" + money +
			", giveCount=" + giveCount +
			", number=" + number +
			", grId=" + grId +
			", publicId=" + publicId +
			", ctId=" + ctId +
			", isDate=" + isDate +
			", busId=" + busId +
			"}";
	}
}
