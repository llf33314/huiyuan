package com.gt.member.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import java.util.Date;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;
import com.baomidou.mybatisplus.enums.FieldFill;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 会员消费赠送记录表
 * </p>
 *
 * @author pengjiangli
 * @since 2017-07-25
 */
@Data
@Accessors(chain = true)
@TableName("t_member_giveconsume")
public class MemberGiveconsume extends Model<MemberGiveconsume> {

    private static final long serialVersionUID = 1L;

    /**
     * 消费记录id
     */
    @TableId("uc_id")
	private Integer ucId;
    /**
     * 物品类型id
     */
	@TableField("gt_id")
	private Integer gtId;
    /**
     * 数量
     */
	@TableField("gc_total")
	private Integer gcTotal;
    /**
     * 物品名称
     */
	@TableField("gt_name")
	private String gtName;
    /**
     * 物品单位
     */
	@TableField("gt_unit")
	private String gtUnit;
    /**
     * 是否送出 0未送出 1已送出
     */
	private Integer sendType;
	private Date sendDate;
	private Integer memberId;


	@Override
	protected Serializable pkVal() {
		return this.ucId;
	}

	@Override
	public String toString() {
		return "MemberGiveconsume{" +
			"ucId=" + ucId +
			", gtId=" + gtId +
			", gcTotal=" + gcTotal +
			", gtName=" + gtName +
			", gtUnit=" + gtUnit +
			", sendType=" + sendType +
			", sendDate=" + sendDate +
			", memberId=" + memberId +
			"}";
	}
}
