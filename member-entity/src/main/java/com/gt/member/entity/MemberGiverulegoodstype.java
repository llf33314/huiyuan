package com.gt.member.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
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
 * @since 2017-07-25
 */
@Data
@Accessors(chain = true)
@TableName("t_member_giverulegoodstype")
public class MemberGiverulegoodstype extends Model<MemberGiverulegoodstype> {

    private static final long serialVersionUID = 1L;

    /**
     * 赠送规则主键
     */
    @TableId("gr_id")
	private Integer grId;
    /**
     * 赠送物品主键
     */
	@TableField("gt_id")
	private Integer gtId;
    /**
     * 规则：0表示单次送 1:金额等于多少积分(向下取整) 2未启用
     */
	@TableField("give_type")
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
	private Integer upperLmit;


	@Override
	protected Serializable pkVal() {
		return this.grId;
	}

	@Override
	public String toString() {
		return "MemberGiverulegoodstype{" +
			"grId=" + grId +
			", gtId=" + gtId +
			", giveType=" + giveType +
			", money=" + money +
			", number=" + number +
			", upperLmit=" + upperLmit +
			"}";
	}
}
