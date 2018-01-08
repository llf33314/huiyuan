package com.gt.member.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;
import com.baomidou.mybatisplus.enums.FieldFill;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 会员卡副卡和主卡关系表
 * </p>
 *
 * @author pengjiangli
 * @since 2018-01-08
 */
@Data
@Accessors(chain = true)
@TableName("t_member_gradetype_assistant_old")
public class MemberGradetypeAssistantOld extends Model<MemberGradetypeAssistantOld> {

    private static final long serialVersionUID = 1L;

	private Integer id;
    /**
     * 会员卡模板id
     */
	@TableField("gtId")
	private Integer gtId;
    /**
     * 主卡会员卡类型
     */
	@TableField("ctId")
	private Integer ctId;
    /**
     * 折扣卡折扣
     */
	private Double discount;
    /**
     * 商家id
     */
	@TableField("busId")
	private Integer busId;
    /**
     * 副卡id
     */
	@TableField("fuctId")
	private Integer fuctId;


	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "MemberGradetypeAssistantOld{" +
			"id=" + id +
			", gtId=" + gtId +
			", ctId=" + ctId +
			", discount=" + discount +
			", busId=" + busId +
			", fuctId=" + fuctId +
			"}";
	}
}
