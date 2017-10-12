package com.gt.member.entity;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
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
 * 商家赠送粉丝信息
 * </p>
 *
 * @author pengjiangli
 * @since 2017-10-12
 */
@Data
@Accessors(chain = true)
@TableName("t_member_giveconsume_new")
public class MemberGiveconsumeNew extends Model<MemberGiveconsumeNew> {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 消费记录id
     */
	@TableField("ucId")
	private Integer ucId;
    /**
     * 物品类型id
     */
	@TableField("gtId")
	private Integer gtId;
    /**
     * 数量
     */
	@TableField("gcTotal")
	private Integer gcTotal;
    /**
     * 物品名称
     */
	@TableField("gtName")
	private String gtName;
    /**
     * 物品单位
     */
	@TableField("gtUnit")
	private String gtUnit;
    /**
     * 是否送出 0未送出 1已送出
     */
	@TableField("sendType")
	private Integer sendType;
    /**
     * 赠送时间
     */
	@TableField("sendDate")
	private Date sendDate;
    /**
     * 粉丝id
     */
	@TableField("memberId")
	private Integer memberId;


	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "MemberGiveconsumeNew{" +
			"id=" + id +
			", ucId=" + ucId +
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
