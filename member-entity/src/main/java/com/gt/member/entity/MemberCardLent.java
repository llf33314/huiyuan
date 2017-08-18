package com.gt.member.entity;

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
@TableName("t_member_card_lent")
public class MemberCardLent extends Model<MemberCardLent> {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
	private Integer mcId;
    /**
     * 标识码
     */
	private String code;
    /**
     * 使用状态 0未使用 1已使用
     */
	private Integer usestate;
    /**
     * 创建时间
     */
	private Date createDate;
    /**
     * 转借金额 0为无上限 储值卡最大金额
     */
	private Double lentMoney;


	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "MemberCardLent{" +
			"id=" + id +
			", mcId=" + mcId +
			", code=" + code +
			", usestate=" + usestate +
			", createDate=" + createDate +
			", lentMoney=" + lentMoney +
			"}";
	}
}
