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
@TableName("t_member_cardgetnumber")
public class MemberCardgetnumber extends Model<MemberCardgetnumber> {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
	private Integer ctId;
	private Integer publicId;
	private String nominateCode;
	private Integer number;
	private Date createDate;
	private Integer createUser;
	private Integer busId;


	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "MemberCardgetnumber{" +
			"id=" + id +
			", ctId=" + ctId +
			", publicId=" + publicId +
			", nominateCode=" + nominateCode +
			", number=" + number +
			", createDate=" + createDate +
			", createUser=" + createUser +
			", busId=" + busId +
			"}";
	}
}
