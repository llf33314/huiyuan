package com.gt.member.entity;

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
 * 
 * </p>
 *
 * @author pengjiangli
 * @since 2017-07-25
 */
@Data
@Accessors(chain = true)
@TableName("t_member_cardbuy")
public class MemberCardbuy extends Model<MemberCardbuy> {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
	private Integer publicId;
	private Integer ctId;
	private Integer memberId;
	private Double buyMoney;
	private Integer busId;


	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "MemberCardbuy{" +
			"id=" + id +
			", publicId=" + publicId +
			", ctId=" + ctId +
			", memberId=" + memberId +
			", buyMoney=" + buyMoney +
			", busId=" + busId +
			"}";
	}
}
