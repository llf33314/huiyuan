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
 * 会员卡副卡充值记录表
 * </p>
 *
 * @author pengjiangli
 * @since 2017-10-16
 */
@Data
@Accessors(chain = true)
@TableName("t_member_rechargegive_assistant")
public class MemberRechargegiveAssistant extends Model<MemberRechargegiveAssistant> {

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
	@TableField("giveCount")
	private Integer giveCount;
    /**
     * 次数(次卡)
     */
	private Integer number;
    /**
     * 副卡主卡关系表
     */
	@TableField("assistantId")
	private Integer assistantId;
    /**
     * 卡片类型Id
     */
	@TableField("ctId")
	private Integer ctId;
    /**
     * 是否会员日 0不是 1是
     */
	@TableField("isDate")
	private Integer isDate;
	@TableField("busId")
	private Integer busId;
    /**
     * 时效卡有效期
     */
	@TableField("validDate")
	private Integer validDate;


	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "MemberRechargegiveAssistant{" +
			"id=" + id +
			", money=" + money +
			", giveCount=" + giveCount +
			", number=" + number +
			", assistantId=" + assistantId +
			", ctId=" + ctId +
			", isDate=" + isDate +
			", busId=" + busId +
			", validDate=" + validDate +
			"}";
	}
}
