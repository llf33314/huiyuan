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
 * 赠送规则与赠送物品详情表
 * </p>
 *
 * @author pengjiangli
 * @since 2018-01-08
 */
@Data
@Accessors(chain = true)
@TableName("t_member_giverulegoodstype_old")
public class MemberGiverulegoodstypeOld extends Model<MemberGiverulegoodstypeOld> {

    private static final long serialVersionUID = 1L;

    /**
     * 赠送规则主键
     */
	private Integer grId;
    /**
     * 赠送物品主键
     */
	private Integer gtId;
    /**
     * 规则：0表示单次送 1:金额等于多少积分(向下取整) 2未启用
     */
	private Integer giveType;
    /**
     * 金额
     */
	private Double money;
    /**
     * 数量
     */
	private Integer number;
    /**
     * 单次上限 0：无上限 自定义物品：赠送总数量
     */
	@TableField("upperLmit")
	private Integer upperLmit;
	private Integer id;
	@TableField("busId")
	private Integer busId;
	@TableField("ctId")
	private Integer ctId;


	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "MemberGiverulegoodstypeOld{" +
			"grId=" + grId +
			", gtId=" + gtId +
			", giveType=" + giveType +
			", money=" + money +
			", number=" + number +
			", upperLmit=" + upperLmit +
			", id=" + id +
			", busId=" + busId +
			", ctId=" + ctId +
			"}";
	}
}
